import React, { useEffect, useState } from "react";

const StatusComponent = () => {
  const [status, setStatus] = useState({ data: "", type: "" });
  const ws = React.useRef(null);

  useEffect(() => {
    ws.current = new WebSocket("ws://localhost:8080/ws/bot");

    ws.current.onopen = () => {
      console.log("Connected to WebSocket");
      // Request current status on connection
      ws.current.send("getStatus");
    };

    ws.current.onmessage = (event) => {
      console.log(event.data);
      try {
        const eventData = JSON.parse(event.data);
        if (eventData.type === "ping") {
          ws.current.send("pong");
        }
        if (eventData.type === "status") {
          setStatus({ data: eventData.data[0].status, type: eventData.type });
        }
      } catch (error) {
        console.error("Error processing message: ", error);
      }
    };

    ws.current.onclose = () => {
      console.log("WebSocket closed");
    };

    ws.current.onerror = (error) => {
      console.error("WebSocket error:", error);
    };

    return () => {
      ws.current.close();
    };
  }, []);

  useEffect(() => {
    console.log("Updated status:", status);
  }, [status]);

  const sendStatusUpdate = () => {
    if (ws.current && status) {
      const jsonPayload = JSON.stringify({
        data: [{ status: status.data }],
        type: status.type || "status", // Default type if not set
      });

      // Format the message as "updateRole:${JSON}"
      const message = `updateStatus:${jsonPayload}`;

      console.log("Sending message:", message);
      ws.current.send(message); // Send the formatted message
    }
  };


  const handleChange = (e) => {
    const data = e.target.value;
    setStatus((prev) => ({ ...prev, data })); // Update only `data` field
  };

  return (
    <div className="bg-white shadow-lg rounded-xl p-8 max-w-lg mx-auto mt-10">
      <h2 className="text-3xl font-bold text-gray-800 mb-6">Update Bot Status</h2>
      <input
        type="text"
        value={status.data}
        onChange={handleChange}
        placeholder="Enter new bot status"
        required
        className="w-full border border-gray-300 p-4 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none transition"
      />
      <button
        onClick={sendStatusUpdate}
        className="w-full bg-blue-600 text-white font-semibold py-3 rounded-lg hover:bg-blue-700 transition duration-300"
      >
        Update Status
      </button>
    </div>
  );
};

export default StatusComponent;
