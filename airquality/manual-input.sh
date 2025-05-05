#!/bin/bash

# Kullanım: ./manual-input.sh <latitude> <longitude> <pm25> <pm10> <no2> <so2> <o3>

# Log dosyası
LOG_FILE="manual-input.log"

# Log fonksiyonu
log() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a "$LOG_FILE"
}

log "Script başlatıldı"

if [ "$#" -ne 7 ]; then
    log "HATA: Yanlış parametre sayısı. Beklenen: 7, Alınan: $#"
    log "Kullanım: $0 <latitude> <longitude> <pm25> <pm10> <no2> <so2> <o3>"
    exit 1
fi

LAT=$1
LON=$2
PM25=$3
PM10=$4
NO2=$5
SO2=$6
O3=$7

log "Parametreler alındı: LAT=$LAT, LON=$LON, PM25=$PM25, PM10=$PM10, NO2=$NO2, SO2=$SO2, O3=$O3"

# Sayısal değer kontrolü
for var in LAT LON PM25 PM10 NO2 SO2 O3; do
    if ! [[ "${!var}" =~ ^-?[0-9]+\.?[0-9]*$ ]]; then
        log "HATA: $var değeri sayısal değil: ${!var}"
        exit 1
    fi
done

# Anomali kontrolü (WHO limitlerine göre)
ANOMALY=false
if (( $(echo "$PM25 > 25" | awk '{print ($1 > $3)}') )); then 
    log "UYARI: PM2.5 değeri WHO limitini aşıyor: $PM25 > 25"
    ANOMALY=true
fi
if (( $(echo "$PM10 > 50" | awk '{print ($1 > $3)}') )); then 
    log "UYARI: PM10 değeri WHO limitini aşıyor: $PM10 > 50"
    ANOMALY=true
fi
if (( $(echo "$NO2 > 200" | awk '{print ($1 > $3)}') )); then 
    log "UYARI: NO2 değeri WHO limitini aşıyor: $NO2 > 200"
    ANOMALY=true
fi
if (( $(echo "$SO2 > 20" | awk '{print ($1 > $3)}') )); then 
    log "UYARI: SO2 değeri WHO limitini aşıyor: $SO2 > 20"
    ANOMALY=true
fi
if (( $(echo "$O3 > 100" | awk '{print ($1 > $3)}') )); then 
    log "UYARI: O3 değeri WHO limitini aşıyor: $O3 > 100"
    ANOMALY=true
fi

# API adresi
API_URL="http://localhost:8083/api/air_quality_data"
log "API URL: $API_URL"

# API bağlantı testi
log "API bağlantısı test ediliyor..."
if ! curl -s -o /dev/null -w "%{http_code}" "$API_URL" > /dev/null; then
    log "HATA: API'ye bağlanılamıyor"
    exit 1
fi

# JSON oluştur
TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%S.000Z")
JSON_DATA="{
    \"latitude\": $LAT,
    \"longitude\": $LON,
    \"timestamp\": \"$TIMESTAMP\",
    \"pm25\": $PM25,
    \"pm10\": $PM10,
    \"no2\": $NO2,
    \"so2\": $SO2,
    \"o3\": $O3,
    \"anomaly\": $ANOMALY
}"

log "Gönderilecek veri: $JSON_DATA"

# Veriyi gönder
log "Veri gönderiliyor..."
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$API_URL" \
    -H "Content-Type: application/json" \
    -H "Accept: application/json" \
    -d "$JSON_DATA")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
RESPONSE_BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" -eq 200 ]; then
    log "BAŞARILI: Veri başarıyla gönderildi"
    log "API Yanıtı: $RESPONSE_BODY"
else
    log "HATA: Veri gönderilemedi. HTTP Kodu: $HTTP_CODE"
    log "API Yanıtı: $RESPONSE_BODY"
    exit 1
fi