import random
import time
import requests
from datetime import datetime
import os
from geopy.geocoders import Nominatim
import logging
import json

# Logging ayarları
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

API_URL = os.environ.get("API_URL", "http://app:8083/api/air_quality_data")
logger.info(f"API URL: {API_URL}")

WHO_LIMITS = {
    "pm25": (0, 25),
    "pm10": (0, 50),
    "no2": (0, 40),
    "so2": (0, 20),
    "o3": (0, 100)
}

NUM_LOCATIONS = 500
ANOMALY_CHANCE = 0.05  # %5 anomali

def random_value(limits, anomaly=False):
    if anomaly:
        return random.uniform(limits[1]*2, limits[1]*5)
    else:
        return random.uniform(limits[0], limits[1])

def random_land_coordinate(geolocator, max_attempts=20):
    for attempt in range(max_attempts):
        lat = random.uniform(-60, 80)
        lon = random.uniform(-180, 180)
        try:
            location = geolocator.reverse((lat, lon), language='en', exactly_one=True, timeout=10)
            if location and 'country' in location.raw.get('address', {}):
                logger.info(f"Lokasyon bulundu: {location.address}")
                return lat, lon
        except Exception as e:
            logger.warning(f"Lokasyon bulunamadı (deneme {attempt + 1}/{max_attempts}): {str(e)}")
            continue
    logger.warning("Maksimum deneme sayısına ulaşıldı, varsayılan lokasyon kullanılıyor")
    return 39.0, 35.0  # fallback: Türkiye

def generate_measurement(lat, lon, anomaly_chance):
    anomaly = random.random() < anomaly_chance
    data = {
        "latitude": lat,
        "longitude": lon,
        "timestamp": datetime.now().isoformat(),
        "pm25": random_value(WHO_LIMITS["pm25"], anomaly),
        "pm10": random_value(WHO_LIMITS["pm10"], anomaly),
        "no2": random_value(WHO_LIMITS["no2"], anomaly),
        "so2": random_value(WHO_LIMITS["so2"], anomaly),
        "o3": random_value(WHO_LIMITS["o3"], anomaly),
        "anomaly": anomaly
    }
    return data, anomaly

def main():
    logger.info("Script başlatılıyor...")
    geolocator = Nominatim(user_agent="airquality-tester")

    # 500 kara lokasyonu üret
    logger.info(f"{NUM_LOCATIONS} kara lokasyonu üretiliyor...")
    locations = []
    while len(locations) < NUM_LOCATIONS:
        lat, lon = random_land_coordinate(geolocator)
        if (lat, lon) not in locations:
            locations.append((lat, lon))
            logger.info(f"Lokasyon eklendi: {len(locations)}/{NUM_LOCATIONS}")

    while True:
        logger.info(f"{datetime.now()} - {NUM_LOCATIONS} lokasyona veri gönderiliyor...")
        for idx, (lat, lon) in enumerate(locations):
            data, anomaly = generate_measurement(lat, lon, ANOMALY_CHANCE)
            try:
                logger.info(f"Veri gönderiliyor: {idx+1}/{NUM_LOCATIONS}")
                logger.info(f"Gönderilen veri: {json.dumps(data, indent=2)}")
                r = requests.post(API_URL, json=data)
                status = "ANOMALİ" if anomaly else "NORMAL"
                logger.info(f"[{status}] {idx+1}/{NUM_LOCATIONS} -> {r.status_code}")
                if r.status_code != 200:
                    logger.error(f"Hata kodu: {r.status_code}, Yanıt: {r.text}")
            except Exception as e:
                logger.error(f"Veri gönderme hatası: {str(e)}")
                logger.error(f"Hata detayı: {type(e).__name__}")
        
        logger.info("40 dakika bekleniyor...")
        time.sleep(40 * 60)

if __name__ == "__main__":
    main()