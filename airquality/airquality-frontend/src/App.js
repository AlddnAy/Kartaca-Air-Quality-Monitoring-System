import logo from './logo.svg';
import './App.css';

import React, { useState } from "react";
import "leaflet/dist/leaflet.css";
import Navbar from "./components/Navbar";
import HeatmapView from "./components/HeatmapView";
import ChartView from "./components/ChartView";
import AlertPanel from "./components/AlertPanel";
import MarkerMapView from "./components/MarkerMapView";

function App() {
  const [current, setCurrent] = useState("heatmap");

  return (
    <div style={{ background: "#f7f7f7", minHeight: "100vh" }}>
      <Navbar current={current} setCurrent={setCurrent} />
      <div style={{ maxWidth: 1200, margin: "32px auto", padding: 16 }}>
        {current === "heatmap" && <HeatmapView />}
        {current === "markers" && <MarkerMapView />}
        {current === "charts" && <ChartView />}
        {current === "alerts" && <AlertPanel />}
      </div>
    </div>
  );
}
export default App;

