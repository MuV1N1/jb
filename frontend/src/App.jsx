import { useState } from "react";
import Sidebar from "./components/Sidebar";
import StatusComponent from "./components/StatusComponent.jsx";
import JoinComponent from "./components/JoinComponent.jsx";

const App = () => {
  const [currentSetting, setCurrentSetting] = useState("update-status");

  const renderContent = () => {
    console.log(currentSetting);
    switch (currentSetting) {
      case "update-status":
        return <StatusComponent />;
      case "join-role":
        return <JoinComponent />;
      case "configuration":
        return (
          <div className="p-8">
            <h2 className="text-4xl font-semibold text-gray-800 mb-4">
              Configuration
            </h2>
            <p className="text-gray-600">
              Manage bot configuration settings (Coming Soon!).
            </p>
          </div>
        );
      default:
        return (
          <div className="p-8">
            <h2 className="text-4xl font-semibold text-gray-800">Welcome!</h2>
            <p className="text-gray-600">Select a setting from the sidebar.</p>
          </div>
        );
    }
  };

  return (
    <div className="flex h-screen bg-gray-100">
      <Sidebar setCurrentSetting={setCurrentSetting} />
      <div className="flex-grow flex flex-col">
        <header className="bg-white shadow p-4">
          <h1 className="text-2xl font-bold text-gray-700">Bot Control Panel</h1>
        </header>
        <main className="flex-grow p-6">{renderContent()}</main>
      </div>
    </div>
  );
};

export default App;
