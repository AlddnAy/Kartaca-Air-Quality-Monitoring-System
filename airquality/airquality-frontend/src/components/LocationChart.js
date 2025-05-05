import React, { useEffect, useState } from "react";
import { Line } from "react-chartjs-2";
import axios from "axios";

// Chart.js scale ve elementlerini import et ve register et
import {
  Chart as ChartJS,
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  Title,
  Tooltip,
  Legend
);

export default function LocationChart({ latitude, longitude }) {
  const [chartData, setChartData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (latitude && longitude) {
      setChartData(null);
      setError(null);
      axios
        .get(
          `http://localhost:8083/api/air_quality_data/timeseries?latitude=${latitude}&longitude=${longitude}&tolerance=0.01`
        )
        .then((res) => {
          if (!res.data || res.data.length === 0) {
            setError("Bu konumda zaman serisi verisi yok.");
            return;
          }
          const labels = res.data.map((d) => new Date(d.timestamp).toLocaleString());
          setChartData({
            labels,
            datasets: [
              {
                label: "PM2.5",
                data: res.data.map((d) => d.pm25),
                borderColor: "rgba(255,99,132,1)",
                fill: false,
                tension: 0.4,
              },
              {
                label: "PM10",
                data: res.data.map((d) => d.pm10),
                borderColor: "rgba(54, 162, 235, 1)",
                fill: false,
                tension: 0.4,
              },
              {
                label: "NO2",
                data: res.data.map((d) => d.no2),
                borderColor: "rgba(255, 206, 86, 1)",
                fill: false,
                tension: 0.4,
              },
              {
                label: "SO2",
                data: res.data.map((d) => d.so2),
                borderColor: "rgba(75, 192, 192, 1)",
                fill: false,
                tension: 0.4,
              },
              {
                label: "O3",
                data: res.data.map((d) => d.o3),
                borderColor: "rgba(153, 102, 255, 1)",
                fill: false,
                tension: 0.4,
              },
            ],
          });
        })
        .catch((err) => setError("Veri alınamadı!"));
    }
  }, [latitude, longitude]);

  if (error) return <div style={{ color: "red" }}>{error}</div>;
  if (!chartData) return <div>Veri yükleniyor...</div>;

  const options = {
    responsive: true,
    plugins: {
      legend: { display: true },
      title: { display: false },
    },
    scales: {
      x: { type: "category", title: { display: true, text: "Zaman" } },
      y: { title: { display: true, text: "Değer" } },
    },
  };

  return (
    <div style={{ width: 300, height: 200 }}>
      <Line data={chartData} options={options} />
    </div>
  );
}