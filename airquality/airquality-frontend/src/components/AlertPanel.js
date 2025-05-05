import React from "react";
import AnomalyNotifications from "./AnomalyNotifications";
// ... diğer importlar ...

export default function AlertPanel() {
  return (
    <div>
      <h2>Uyarı Paneli</h2>
      <AnomalyNotifications />
      {/* Diğer uyarı/alert içerikleri */}
    </div>
  );
}