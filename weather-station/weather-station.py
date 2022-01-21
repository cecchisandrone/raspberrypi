import RPi.GPIO as GPIO
from flask import Flask
from flask import request
from flask import jsonify
from time import sleep
import Adafruit_DHT

rainfallPin = 23
millimetersPerTick = 0.455
rainfallTicks = 0
dht22Pin = 17

def rainfallTick(channel):
  global rainfallTicks
  print("Rainfall tick")
  rainfallTicks = rainfallTicks + 1

GPIO.setmode(GPIO.BCM)
GPIO.setup(rainfallPin, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
GPIO.add_event_detect(rainfallPin, GPIO.FALLING, callback=rainfallTick, bouncetime=300)
app = Flask(__name__)

@app.route('/rainfall', methods=['GET'])
def rainfall_endpoint():
  global rainfallTicks
  format = request.args.get('format')
  rainfallMillimeters = rainfallTicks * millimetersPerTick
  rainfallTicks = 0
  if format == "json":
    return jsonify(rainfall=rainfallMillimeters), 200
  else:
    return str(rainfallMillimeters), 200

@app.route('/temperature', methods=['GET'])
def temperature_endpoint():
  format = request.args.get('format')
  humidity, temperature = Adafruit_DHT.read_retry(Adafruit_DHT.DHT22, dht22Pin)
  if temperature is not None:
    if format == "json":
      return jsonify(temperature=temperature), 200
    else:
      return str(temperature), 200
  return "Error while reading temperature", 500

@app.route('/humidity', methods=['GET'])
def humidity_endpoint():
  format = request.args.get('format')
  humidity, temperature = Adafruit_DHT.read_retry(Adafruit_DHT.DHT22, dht22Pin)
  if humidity is not None:
    if format == "json":
      return jsonify(humidity=humidity), 200
    else:
      return str(humidity), 200
  return "Error while reading humidity", 500

if __name__ == '__main__':
  app.run(host='0.0.0.0', port=7000)
  GPIO.cleanup()

