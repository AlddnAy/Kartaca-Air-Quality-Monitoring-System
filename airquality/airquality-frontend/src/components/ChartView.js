// src/components/ChartView.js
import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from "recharts";

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8083";

export default function ChartView() {
  const [data, setData] = useState([]);

  useEffect(() => {
    // Tüm veriyi çekiyoruz, istersen filtre ekleyebilirsin
    axios.get(`${API_URL}/api/air_quality_data`)
      .then(res => {
        // Tarihe göre sıralama
        const sorted = res.data
          .map(d => ({
            timestamp: d.timestamp.split("T")[0] + " " + d.timestamp.split("T")[1].split(".")[0], // okunabilir zaman
            pm25: d.pm25,
            pm10: d.pm10,
            no2: d.no2,
            so2: d.so2,
            o3: d.o3
          }))
          .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
        setData(sorted);
      });
  }, []);

  return (
    <div style={{
      background: "#fff",
      borderRadius: 12,
      boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
      padding: 24,
      marginBottom: 32
    }}>
      <h2 style={{ color: "#222", marginBottom: 24 }}>Zaman Serisi: Hava Kalitesi Parametreleri</h2>
      <div style={{ width: "100%", height: 500 }}>
        <ResponsiveContainer>
          <LineChart data={data}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="timestamp" minTickGap={40} />
            <YAxis />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="pm25" stroke="#8884d8" name="PM2.5" />
            <Line type="monotone" dataKey="pm10" stroke="#82ca9d" name="PM10" />
            <Line type="monotone" dataKey="no2" stroke="#ff7300" name="NO2" />
            <Line type="monotone" dataKey="so2" stroke="#387908" name="SO2" />
            <Line type="monotone" dataKey="o3" stroke="#0088FE" name="O3" />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
