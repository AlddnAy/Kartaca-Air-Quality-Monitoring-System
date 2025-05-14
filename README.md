# Kartaca-Air-Quality-Monitoring-System

Gerçek zamanlı hava kalitesi izleme platformu. Veriler REST API üzerinden alınır, RabbitMQ ile kuyruklanır,
 anomali tespiti yapılır ve sonuçlar TimescaleDB'de saklanır.
 WebSocket ile frontend'e anlık bildirim gönderilir.

---

##  Sistem Mimarisi

###  Genel Akış

1. **Veri Alımı**
   - Ölçüm verisi `POST` ile REST API'ye (Spring Boot) gönderilir.
   - Veri, RabbitMQ kuyruğuna yazılır.

2. **Asenkron İşleme**
   - Spring Boot uygulaması, RabbitMQ’dan verileri asenkron olarak çeker.
   - Anomali tespiti yapılır.
   - Veriler TimescaleDB’ye kaydedilir.
   - Anomali varsa, WebSocket ile frontend’e anlık bildirim gönderilir.

3. **Veri Saklama**
   - Tüm ölçümler ve anomali etiketleri, TimescaleDB (PostgreSQL) üzerinde tutulur.

4. **Frontend**
   - **Isı Haritası:** Ölçüm yoğunluğunu görsel olarak gösterir.
   - **Marker Haritası:** Her istasyonun son değerleri ve zaman serisi grafiği.
   - **Grafikler:** Tüm ölçümlerin zaman serisi.
   - **Uyarı Paneli:** Anomali tespitlerinde anlık bildirim.

---

##  Komponentler

###  Backend (Spring Boot)

- `AirQualityController`: REST API uç noktaları
- `AirQualityConsumer`: RabbitMQ’dan veri çekip işleyen servis
- `AnomalyDetectionService`: Anomali tespit algoritmaları
- `AnomalyNotificationService`: WebSocket ile bildirim
- `AirQualityRepository`: JPA ile veri erişimi
- `RabbitMQConfig`, `WebSocketConfig`: Sistem konfigürasyonları

###  Frontend (React)

- `HeatmapView.js`: Isı haritası (react-leaflet-heatmap-layer-v3)
- `MarkerMapView.js`: Marker haritası ve zaman serisi grafik
- `ChartView.js`: Ölçüm zaman serileri (recharts)
- `AnomalyNotifications.js`, `AlertPanel.js`: WebSocket ile anlık uyarılar
- `Navbar.js`: Sekmeli menü

###  RabbitMQ

- Ölçüm verilerini asenkron işlemek için kullanılır

###  PostgreSQL / TimescaleDB

- Tüm zaman serisi verilerinin ve anomalilerin saklandığı veritabanı

###  Scriptler

- `auto-test.py`: Otomatik rastgele ve anomali verisi gönderir
- açık kaynaklı bir siteden saniyede bir veri çekebilyor. İlk 500 veri için yaklaşık 
bir 30 dk bekletebiliyor. 

   NUM_LOCATIONS = 500
   ANOMALY_CHANCE = 0.05  # %5 anomali

bu değerleri değiştirip hem daha hızlı çıktı alınabilir. Hem de anolimi şansını değiştirebilirsiniz.

 -`manual-input.sh`: Komut satırından manuel veri gönderimi
  git bash de çalıştırılması gerekiyor.
 -örnek çalışma komutu -> manual-input.sh 41.0 29.0 15 30 20 10 50 

---

## Teknoloji Seçimleri ve Gerekçeleri

| Teknoloji | Neden Tercih Edildi |
|----------|---------------------|
| **Spring Boot** | Hızlı geliştirme, güçlü ekosistem, kolay RabbitMQ & WebSocket entegrasyonu |
| **React** | Modern, component tabanlı, hızlı ve esnek |
| **RabbitMQ** | Asenkron işleme, sistemler arası gevşek bağlılık |
| **PostgreSQL + TimescaleDB** | Zaman serisi verileri için güçlü açık kaynak DB |
| **Docker + Docker Compose** | Kolay dağıtım, taşınabilirlik |
| **Leaflet & Recharts** | Modern, açık kaynak harita ve grafik kütüphaneleri |

---


## Tüm Servisler ve Bağımlılıklar Docker Compose İçerisinde
--Proje, aşağıdaki tüm bileşenleri ve bağımlılıkları tek bir docker-compose.yaml dosyası ile yönetir:
--Backend (Spring Boot):
--Hava kalitesi verilerini alan, işleyen ve anomali tespiti yapan ana uygulama.
--Frontend (React):
--Isı haritası, marker haritası, grafikler ve uyarı paneli ile kullanıcı arayüzü.
--PostgreSQL/TimescaleDB:
--Zaman serisi verilerinin ve anomalilerin saklandığı veritabanı.
--RabbitMQ:
--Asenkron işleme için mesaj kuyruğu.
--Otomatik Test Scripti (auto-test.py):
--Sistemi otomatik olarak test eden ve veri gönderen script.

##  Kurulum Adımları

### Gereksinimler

- Docker
- Docker Compose
- Node.js
- Python
- Maven


### 1. Projeyi Klonla

```bash
git clone https://github.com/kullanici/air-quality-monitoring.git
cd air-quality-monitoring


2. Frontend Ortam Değişkeni Ayarla
airquality-frontend/.env dosyası oluştur:

REACT_APP_BACKEND_URL=http://localhost:8083

3. Docker Compose ile Sistemi Başlat
docker-compose up --build

Tüm servisler otomatik başlar:

Frontend: http://localhost:3000

Backend API: http://localhost:8083/api

RabbitMQ UI: http://localhost:15672 (user: guest, pass: guest)

PostgreSQL: localhost:5432 (user: postgres, pass: 1905, db: airqualitydb)

Kullanım Rehberi
Veri Gönderme
Postman veya curl ile

curl -X POST http://localhost:8083/api/air_quality_data \
-H "Content-Type: application/json" \
-d '{"stationId": "X01", "latitude": 40.1, "longitude": 29.0, "pm25": 30, "pm10": 60, "no2": 180, "so2": 15, "o3": 95}'
Otomatik Veri Gönderimi
Docker Compose ile birlikte auto-test.py scripti başlar ve rastgele/anomali verisi gönderir.

Manuel Veri Girişi

./scripts/manual-input.sh


  Arayüzler
Isı Haritası: Ölçüm yoğunluğu renkli olarak gösterilir.

İstasyon Markerları: Son değer ve popup içinde zaman serisi grafik.

Grafikler: Tüm ölçümler için zaman serisi gösterimi.

Uyarı Paneli: Anomali tespitlerinde WebSocket ile anlık bildirimler.

  API Dokümantasyonu
- Ölçüm Ekleme
POST /api/air_quality_data
Body (JSON):

json
{
  "stationId": "X01",
  "latitude": 40.1,
  "longitude": 29.0,
  "pm25": 30,
  "pm10": 60,
  "no2": 180,
  "so2": 15,
  "o3": 95
}
Cevap:

arduino
"Data queued successfully!"