import React, {useEffect, useState} from "react";

const ConfigurationComponent = () => {
  const [messages, setMessages] = useState([]);
  const [status, setStatus] = useState("");
  const ws = React.useRef(null);

  useEffect(() => {
    ws.current = new WebSocket("ws://localhost:8080/ws/bot");

    ws.current.onopen = () => {
      console.log("Connected to WebSocket");
      // Request current status on connection
      ws.current.send("getStatus");
    };

    ws.current.onmessage = (event) => {
      console.log("Message received:", event.data);

      // Handle current status message
      if (event.data.startsWith("CurrentStatus:")) {
        setStatus(event.data.split(":")[1]);
      }

      setMessages((prev) => [...prev, event.data]);
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

  const sendStatusUpdate = () => {
    if (ws.current && status) {
      ws.current.send(`updateStatus:${status}`);
    }
  };

  return (
    <div>
      <h2>Bot Status</h2>
      <input
        type="text"
        value={status}
        onChange={(e) => setStatus(e.target.value)}
        placeholder="Enter new bot status"
      />
      <button onClick={sendStatusUpdate}>Update Status</button>
      <div>
        <h3>Messages:</h3>
        <ul>
          {messages.map((msg, idx) => (
            <li key={idx}>{msg}</li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default ConfigurationComponent;
