// src/components/MapView.js
import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import axios from "axios";
import "leaflet/dist/leaflet.css";
import LocationChart from "./LocationChart";
import L from "leaflet";
import yesIconPng from "../assets/yes.png";
import noIconPng from "../assets/no.png";

// Marker ikonlarını tanımla
const normalIcon = new L.Icon({
  iconUrl: yesIconPng,
  iconSize: [32, 32], // ikon boyutunu isteğine göre ayarlayabilirsin
  iconAnchor: [16, 32],
  popupAnchor: [0, -32],
  shadowUrl: null,
  shadowSize: null,
  shadowAnchor: null,
});

const anomalyIcon = new L.Icon({
  iconUrl: noIconPng,
  iconSize: [32, 32],
  iconAnchor: [16, 32],
  popupAnchor: [0, -32],
  shadowUrl: null,
  shadowSize: null,
  shadowAnchor: null,
});

function MapView() {
  const [data, setData] = useState([]);
  const [selected, setSelected] = useState(null);

  useEffect(() => {
    axios
      .get("http://localhost:8083/api/air_quality_data")
      .then((res) => setData(res.data))
      .catch((err) => console.error(err));
  }, []);

  return (
    <div style={{ height: "80vh", width: "100%" }}>
      <MapContainer
        center={[20, 0]}
        zoom={2}
        style={{ height: "100%", width: "100%" }}
        scrollWheelZoom={true}
      >
        <TileLayer
          attribution='&copy; <a href="https://osm.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        {data.map((item, idx) => (
          <Marker
            key={idx}
            position={[item.latitude, item.longitude]}
            icon={item.anomaly ? anomalyIcon : normalIcon}
            eventHandlers={{
              click: () => setSelected(idx),
            }}
          >
            <Popup>
              <div style={{ minWidth: 320 }}>
                <b>PM2.5:</b> {item.pm25}<br />
                <b>PM10:</b> {item.pm10}<br />
                <b>NO2:</b> {item.no2}<br />
                <b>SO2:</b> {item.so2}<br />
                <b>O3:</b> {item.o3}<br />
                <b>Anomali:</b> {item.anomaly ? "Evet" : "Hayır"}
                <hr />
                <b>Zaman Serisi Grafiği:</b>
                {selected === idx ? (
                  <LocationChart latitude={item.latitude} longitude={item.longitude} />
                ) : (
                  <div>Grafik için marker'a tıklayın...</div>
                )}
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
}

export default MapView;