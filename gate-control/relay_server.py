import time
import atexit
import RPi.GPIO as GPIO
from flask import Flask
from flask import request

PIN = 4
app = Flask(__name__)
GPIO.setmode(GPIO.BCM)
GPIO.setup(PIN, GPIO.OUT, initial=GPIO.HIGH)

@app.route('/toggle-relay', methods=['POST'])
def toggle_relay():
    duration = request.args.get('duration')
    if duration is None:
      duration = '2'   
    GPIO.output(PIN, GPIO.LOW)
    time.sleep(float(duration))
    GPIO.output(PIN, GPIO.HIGH)
    return'ok', 200
    
@atexit.register
def exit():
    GPIO.cleanup()

if __name__ == '__main__':
      app.run(host='0.0.0.0', port=5000)
