import React from "react";

export default function Navbar({ current, setCurrent }) {
  const menu = [
    { key: "heatmap", label: "Isı Haritası" },
    { key: "markers", label: "İstasyon Markerları" },
    { key: "charts", label: "Grafikler" },
    { key: "alerts", label: "Uyarı Paneli" }
  ];

  return (
    <nav style={{
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      background: "#222",
      padding: "14px 0",
      boxShadow: "0 2px 8px rgba(0,0,0,0.08)"
    }}>
      {menu.map(item => (
        <button
          key={item.key}
          onClick={() => setCurrent(item.key)}
          style={{
            background: current === item.key ? "#ff4d4f" : "transparent",
            color: current === item.key ? "#fff" : "#eee",
            border: "none",
            borderBottom: current === item.key ? "3px solid #fff" : "3px solid transparent",
            fontSize: 18,
            fontWeight: "bold",
            margin: "0 24px",
            padding: "8px 16px",
            borderRadius: 6,
            cursor: "pointer",
            transition: "all 0.2s"
          }}
        >
          {item.label}
        </button>
      ))}
    </nav>
  );
}
