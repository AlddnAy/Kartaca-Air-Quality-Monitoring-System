import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import { HeatmapLayer } from "react-leaflet-heatmap-layer-v3";
import axios from "axios";

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8083";

export default function HeatmapView() {
  const [points, setPoints] = useState([]);

  useEffect(() => {
    axios.get(`${API_URL}/api/air_quality_data`)
      .then(res => {
        const data = res.data.map(d => [
          d.latitude,
          d.longitude,
          d.pm25 // veya başka bir parametre
        ]);
        setPoints(data);
      });
  }, []);

  return (
    <div style={{
      background: "#fff",
      borderRadius: 12,
      boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
      padding: 24,
      margin: "32px auto",
      maxWidth: 900,
      display: "flex",
      flexDirection: "column",
      alignItems: "center"
    }}>
      <h2 style={{ color: "#222", marginBottom: 24 }}>Isı Haritası</h2>
      <MapContainer
        center={[39, 35]}
        zoom={2}
        style={{ height: "600px", width: "100%", maxWidth: 800 }}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <HeatmapLayer
          fitBoundsOnLoad
          fitBoundsOnUpdate
          points={points}
          longitudeExtractor={m => m[1]}
          latitudeExtractor={m => m[0]}
          intensityExtractor={m => m[2]}
          radius={20}
          blur={15}
          max={200}
        />
      </MapContainer>
    </div>
  );
}