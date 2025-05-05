#!/bin/bash

# Kullanım: ./manual-input.sh <latitude> <longitude> <pm25> <pm10> <no2> <so2> <o3>

if [ "$#" -ne 7 ]; then
  echo "Kullanım: $0 <latitude> <longitude> <pm25> <pm10> <no2> <so2> <o3>"
  exit 1
fi

LAT=$1
LON=$2
PM25=$3
PM10=$4
NO2=$5
SO2=$6
O3=$7

# Anomali kontrolü (WHO limitlerine göre)
ANOMALY=false
if [ "$(echo "$PM25 > 25" | bc)" -eq 1 ]; then ANOMALY=true; fi
if [ "$(echo "$PM10 > 50" | bc)" -eq 1 ]; then ANOMALY=true; fi
if [ "$(echo "$NO2 > 200" | bc)" -eq 1 ]; then ANOMALY=true; fi
if [ "$(echo "$SO2 > 20" | bc)" -eq 1 ]; then ANOMALY=true; fi
if [ "$(echo "$O3 > 100" | bc)" -eq 1 ]; then ANOMALY=true; fi

# API adresi (Docker Compose ile çalışıyorsan gerekirse app:8083 olarak değiştir)
API_URL=${API_URL:-"http://localhost:8083/api/air_quality_data"}

# JSON oluştur ve gönder
curl -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d "{
    \"latitude\": $LAT,
    \"longitude\": $LON,
    \"timestamp\": \"$(date -Iseconds)\",
    \"pm25\": $PM25,
    \"pm10\": $PM10,
    \"no2\": $NO2,
    \"so2\": $SO2,
    \"o3\": $O3,
    \"anomaly\": $ANOMALY
  }"