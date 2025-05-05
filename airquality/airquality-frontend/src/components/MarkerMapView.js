import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import axios from "axios";
import { LineChart, Line, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer } from "recharts";
import L from "leaflet";

const redIcon = new L.Icon({
  iconUrl: process.env.PUBLIC_URL + "/markers/red-marker.png",
  iconSize: [32, 32],
  iconAnchor: [16, 32],
  popupAnchor: [0, -32]
});

const greenIcon = new L.Icon({
  iconUrl: process.env.PUBLIC_URL + "/markers/green-marker.png",
  iconSize: [32, 32],
  iconAnchor: [16, 32],
  popupAnchor: [0, -32]
});

const blueIcon = new L.Icon({
  iconUrl: process.env.PUBLIC_URL + "/markers/blue-marker.png",
  iconSize: [32, 32],
  iconAnchor: [16, 32],
  popupAnchor: [0, -32]
});

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8083";

export default function MarkerMapView() {
  const [points, setPoints] = useState([]);
  const [series, setSeries] = useState({}); // { "lat,lon": [veri, ...] }

  useEffect(() => {
    axios.get(`${API_URL}/api/air_quality_data`)
      .then(res => {
        setPoints(res.data);
      });
  }, []);

  // Marker'a tıklanınca veri çek
  const handlePopupOpen = (lat, lon) => {
    const key = `${lat},${lon}`;
    if (series[key]) return; // daha önce çekildiyse tekrar çekme
    axios.get(`${API_URL}/api/air_quality_data/by-location?latitude=${lat}&longitude=${lon}`)
      .then(res => {
        // Tarihe göre sırala
        const sorted = res.data
          .map(d => ({
            timestamp: d.timestamp.split("T")[0] + " " + d.timestamp.split("T")[1].split(".")[0],
            pm25: d.pm25,
            pm10: d.pm10,
            no2: d.no2,
            so2: d.so2,
            o3: d.o3
          }))
          .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
        setSeries(prev => ({ ...prev, [key]: sorted }));
      });
  };

  return (
    <div style={{
      background: "#fff",
      borderRadius: 12,
      boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
      padding: 24,
      marginBottom: 32
    }}>
      <h2 style={{ color: "#222", marginBottom: 24 }}>İstasyon Markerları</h2>
      <MapContainer center={[39, 35]} zoom={2} style={{ height: "600px", width: "100%" }}>
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        {points.map((point, i) => {
          const key = `${point.latitude},${point.longitude}`;
          return (
            <Marker
              key={i}
              position={[point.latitude, point.longitude]}
              icon={point.anomaly ? redIcon : greenIcon}
              eventHandlers={{
                popupopen: () => handlePopupOpen(point.latitude, point.longitude)
              }}
            >
              <Popup minWidth={340}>
                <div>
                  <b>PM2.5:</b> {point.pm25}<br />
                  <b>PM10:</b> {point.pm10}<br />
                  <b>NO2:</b> {point.no2}<br />
                  <b>SO2:</b> {point.so2}<br />
                  <b>O3:</b> {point.o3}<br />
                  <b>Tarih:</b> {point.timestamp?.replace("T", " ").split(".")[0]}
                  <hr />
                  <b>Zaman Serisi (Tüm Parametreler):</b>
                  <div style={{ width: 300, height: 150 }}>
                    <ResponsiveContainer>
                      <LineChart data={series[key] || []}>
                        <XAxis dataKey="timestamp" hide />
                        <YAxis hide />
                        <Tooltip />
                        <Legend />
                        <Line type="monotone" dataKey="pm25" stroke="#8884d8" dot={false} name="PM2.5" />
                        <Line type="monotone" dataKey="pm10" stroke="#82ca9d" dot={false} name="PM10" />
                        <Line type="monotone" dataKey="no2" stroke="#ff7300" dot={false} name="NO2" />
                        <Line type="monotone" dataKey="so2" stroke="#387908" dot={false} name="SO2" />
                        <Line type="monotone" dataKey="o3" stroke="#0088FE" dot={false} name="O3" />
                      </LineChart>
                    </ResponsiveContainer>
                  </div>
                </div>
              </Popup>
            </Marker>
          );
        })}
      </MapContainer>
    </div>
  );
}
