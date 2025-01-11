import "react";
import { FiSettings, FiLogIn, FiFileText } from "react-icons/fi";

const Sidebar = ({ setCurrentSetting }) => {
  const menuItems = [
    { id: "update-status", label: "Update Status", icon: <FiSettings /> },
    { id: "join-role", label: "Join Role", icon: <FiFileText /> },
    { id: "configuration", label: "Configuration", icon: <FiLogIn /> },
  ];

  return (
    <div className="w-64 bg-gray-900 text-white flex flex-col shadow-lg">
      <div className="p-6">
        <h2 className="text-3xl font-bold">Settings</h2>
        <p className="text-sm text-gray-400">Manage your bot easily</p>
      </div>
      <ul className="flex-grow mt-6">
        {menuItems.map((item) => (
          <li
            key={item.id}
            onClick={() => setCurrentSetting(item.id)}
            className="flex items-center space-x-4 p-4 hover:bg-gray-700 cursor-pointer transition duration-300"
          >
            <span className="text-lg">{item.icon}</span>
            <span className="font-medium">{item.label}</span>
          </li>
        ))}
      </ul>
      <footer className="p-4 text-center text-gray-500 text-xs">
        Bot Control Panel © {new Date().getFullYear()}
      </footer>
    </div>
  );
};

export default Sidebar;
