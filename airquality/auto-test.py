import random
import time
import requests
from datetime import datetime
import os
from geopy.geocoders import Nominatim

API_URL = os.environ.get("API_URL", "http://app:8083/api/air_quality_data")

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
    for _ in range(max_attempts):
        lat = random.uniform(-60, 80)
        lon = random.uniform(-180, 180)
        try:
            location = geolocator.reverse((lat, lon), language='en', exactly_one=True, timeout=10)
            if location and 'country' in location.raw.get('address', {}):
                return lat, lon
        except Exception:
            continue
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
    geolocator = Nominatim(user_agent="airquality-tester")

    # 1000 kara lokasyonu üret
    print("1000 kara lokasyonu üretiliyor...")
    locations = []
    while len(locations) < NUM_LOCATIONS:
        lat, lon = random_land_coordinate(geolocator)
        if (lat, lon) not in locations:
            locations.append((lat, lon))
        print(f"{len(locations)}/{NUM_LOCATIONS}", end="\r")

    while True:
        print(f"\n{datetime.now()} - 1000 lokasyona veri gönderiliyor...")
        for idx, (lat, lon) in enumerate(locations):
            data, anomaly = generate_measurement(lat, lon, ANOMALY_CHANCE)
            try:
                r = requests.post(API_URL, json=data)
                status = "ANOMALİ" if anomaly else "NORMAL"
                print(f"[{status}] {idx+1}/{NUM_LOCATIONS} -> {r.status_code}")
            except Exception as e:
                print("Hata:", e)
        print("10 dakika bekleniyor...\n")
        time.sleep(40 * 60)

if __name__ == "__main__":
    main()