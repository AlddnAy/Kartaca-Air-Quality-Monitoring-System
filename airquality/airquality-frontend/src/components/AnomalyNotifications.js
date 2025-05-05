import React, { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

const WS_URL = process.env.REACT_APP_API_URL || "http://localhost:8083";

export default function AnomalyNotifications() {
  const [anomalies, setAnomalies] = useState([]);

  // 1. İlk açılışta son 10 anomaliyi çek
  useEffect(() => {
    const now = new Date();
    const start = new Date(now.getTime() - 24 * 60 * 60 * 1000); // son 24 saat
    const startStr = start.toISOString().split(".")[0];
    const endStr = now.toISOString().split(".")[0];

    fetch("http://localhost:8083/api/anomalies?start=2024-06-08T00:00:00&end=2024-06-09T23:59:59")
      .then((res) => res.json())
      .then((data) => {
        // En yeni 10 tanesini al
        const sorted = data.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
        setAnomalies(sorted.slice(0, 10));
      });
  }, []);

  // 2. WebSocket ile yeni gelenleri ekle
  useEffect(() => {
    const socket = new SockJS(`${WS_URL}/ws`);
    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: () => {
        stompClient.subscribe("/topic/anomalies", (message) => {
          const anomaly = JSON.parse(message.body);
          setAnomalies((prev) => {
            // Aynı id'li anomaliyi tekrar ekleme
            const exists = prev.find((a) => a.id === anomaly.id);
            if (exists) return prev;
            return [anomaly, ...prev].slice(0, 10);
          });
        });
      },
    });
    stompClient.activate();

    return () => {
      stompClient.deactivate();
      socket.close();
    };
  }, []);

  if (anomalies.length === 0) {
    return (
      <div style={{
        background: "#f6ffed",
        border: "1px solid #b7eb8f",
        borderRadius: 8,
        padding: 16,
        margin: "16px 0",
        color: "#389e0d"
      }}>
        <span role="img" aria-label="check">✅</span> Son 24 saatte anomali yok.
      </div>
    );
  }

  return (
    <div style={{
      background: "#fff",
      borderRadius: 12,
      boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
      padding: 24,
      marginBottom: 32
    }}>
      <h2 style={{ color: "#ff4d4f", marginBottom: 24 }}>
        <span role="img" aria-label="alert">⚠️</span> Uyarı Paneli
      </h2>
      <div style={{
        background: "#fff0f0",
        border: "2px solid #ff4d4f",
        borderRadius: 8,
        padding: 16,
        margin: "16px 0",
        boxShadow: "0 2px 8px rgba(255,0,0,0.08)"
      }}>
        <h4 style={{color: "#ff4d4f", marginBottom: 12}}>
          <span role="img" aria-label="alert">⚠️</span> Son 10 Anomali Bildirimi
        </h4>
        <ul style={{listStyle: "none", padding: 0, margin: 0}}>
          {anomalies.map((a, i) => (
            <li key={a.id || i} style={{
              marginBottom: 8,
              padding: 8,
              background: "#fff",
              borderRadius: 4,
              border: "1px solid #ffeaea"
            }}>
              <b>{a.timestamp?.replace("T", " ").split(".")[0]}</b>
              <span style={{marginLeft: 8, color: "#ff4d4f"}}>●</span>
              <span style={{marginLeft: 8}}>PM2.5: <b>{a.pm25}</b></span>
              <span style={{marginLeft: 8}}>PM10: <b>{a.pm10}</b></span>
              <span style={{marginLeft: 8}}>NO2: <b>{a.no2}</b></span>
              <span style={{marginLeft: 8}}>SO2: <b>{a.so2}</b></span>
              <span style={{marginLeft: 8}}>O3: <b>{a.o3}</b></span>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}