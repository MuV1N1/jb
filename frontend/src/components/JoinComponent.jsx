import {useState, useEffect, useRef} from "react";

const JoinComponent = () => {
  const [role, setRole] = useState({ id: "", name: "" });
  const [roles, setRoles] = useState([]);
  const [channel, setChannel] = useState({ id: "", name: "" });
  const [channels, setChannels] = useState([]);
  const [message, setMessage] = useState({ title: "", message: "", showAvatar: false });
  const ws = useRef(null);

  const handleStatusChange = (e) => {
    const selectedRole = roles.find(r => r.id === e.target.value);
    if (selectedRole) {
      setRole({ id: selectedRole.id, name: selectedRole.name });
    }
  };
  const handleChannelChange = (e) => {
    const selectedChannel = channels.find(c => c.id === e.target.value);
    if (selectedChannel) {
      setChannel({ id: selectedChannel.id, name: selectedChannel.name });
    }
  };
  const handleMessageTitleChange = (e) => {
    setMessage({ title: e.target.value, message: message.message, showAvatar: message.showAvatar });
  }
  const handleMessageMessageChange = (e) => {
    setMessage({ title: message.title, message: e.target.value, showAvatar: message.showAvatar });
  }
  const handleMessageShowAvatarChange = (e) => {
    setMessage({ title: message.title, message: message.message, showAvatar: e.target.checked });
  }

  useEffect(() => {
    ws.current = new WebSocket("ws://localhost:8080/ws/bot");

    ws.current.onopen = () => {
      console.log("Connected to WebSocket");
      ws.current.send("getRoles");
      ws.current.send("getChannels");
      ws.current.send("getMessage")
    }
    ws.current.onmessage = (event) => {
      if(event.data === "ping") {
        ws.current.send("pong")
      }else {
        let eventData = JSON.parse(event.data)
        console.log(eventData)
        if (eventData.type === "roles") {
          setRoles(JSON.parse(event.data).data);
        }
        if (eventData.type === "channels") {
          setChannels(JSON.parse(event.data).data[0]);
        }
        if(eventData.type === "message"){
          setMessage(eventData.data)
        }
      }
    }
    ws.current.onclose = () => {
      console.log("WebSocket closed");
    }
    ws.current.onerror = (error) => {
      console.error("WebSocket error:", error);
    }
  }, []);

  const handleRoleUpdate = () => {
    if(ws.current && role){
      const name = JSON.stringify(role.name)
      const id = JSON.stringify(role.id)
      console.log(`updateRole:{"data":[{"id":${id},"name":${name}]}`)
      ws.current.send(`updateRole:{"data":[{"id":${id},"name":${name}}],"type":"role"}`);
    }
  };
  const handleChannelUpdate = () => {
    if(ws.current && channel){
      ws.current.send(`updateRole:${channel}`);
    }
  };
  const handleMessageUpdate = () => {
    if(ws.current && message){
      ws.current.send(`updateRole:${message}`);
    }
  }
  return (
    <>
      <div className="bg-white shadow-lg rounded-xl p-8 max-w-lg mx-auto mt-10">
        <h2 className="text-3xl font-bold text-gray-800 mb-6">Update role on Join</h2>
        <form onSubmit={handleRoleUpdate} className="space-y-6">
          <select value={role.id} onChange={handleStatusChange} className="w-full p-3 border rounded-lg">
            <option value="" disabled>Select a role</option>
            {Array.isArray(roles) && roles.map((role) => (
              <option key={role.id} value={role.id}>{role.name}</option>
            ))}
          </select>
          <button
            type="submit"
            className="w-full bg-blue-600 text-white font-semibold py-3 rounded-lg hover:bg-blue-700 transition duration-300"
          >
            Update Role
          </button>
        </form>
      </div>
      <div className="bg-white shadow-lg rounded-xl p-8 max-w-lg mx-auto mt-10">
        <h2 className="text-3xl font-bold text-gray-800 mb-6">Update join message channel</h2>
        <form onSubmit={handleChannelUpdate} className="space-y-6">
          <select value={channel.id} onChange={handleChannelChange} className="w-full p-3 border rounded-lg">
            <option value="" disabled>Select a channel for the message</option>
            {Array.isArray(channels) && channels.map((channel) => (
              <option key={channel.id} value={channel.id}>{channel.name}</option>
            ))}
          </select>
          <button
            type="submit"
            className="w-full bg-blue-600 text-white font-semibold py-3 rounded-lg hover:bg-blue-700 transition duration-300"
          >
            Update Channel
          </button>
        </form>
      </div>
      <div className="bg-white shadow-lg rounded-xl p-8 max-w-lg mx-auto mt-10">
        <h2 className="text-3xl font-bold text-gray-800 mb-6">Update join message</h2>
        <form onSubmit={handleMessageUpdate} className="space-y-6">
          <label htmlFor="title">Title: </label>
          <input type="text" id="title" name="title" placeholder="Title"
                 className="w-full p-3 border rounded-lg" value={message.title} onChange={handleMessageTitleChange}/><br/>
          <label htmlFor="message">Message: </label>
          <input type="text" id="message" name="message" placeholder="Message"
                 className="w-full p-3 border rounded-lg" value={message.message} onChange={handleMessageMessageChange}/><br/>
          <label htmlFor="showAvatar">Show Avatar</label>
          <input type="checkbox" id="showAvatar" name="showAvatar" value={message.showAvatar} onChange={handleMessageShowAvatarChange} />

          <br/>
          <button
            type="submit"
            className="w-full bg-blue-600 text-white font-semibold py-3 rounded-lg hover:bg-blue-700 transition duration-300"
          >
            Update Channel
          </button>
        </form>
      </div>
    </>
  )
    ;
};

export default JoinComponent;