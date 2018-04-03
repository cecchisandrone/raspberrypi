#!/usr/bin/env python
import signal
import sys
import time
import RPi.GPIO as GPIO
import argparse

parser = argparse.ArgumentParser(description='Enable GPIO pin')
parser.add_argument('pin', metavar='pin', type=int, help='The pin to enable')
args = parser.parse_args()

pin = args.pin

GPIO.setmode(GPIO.BCM)

def signal_handler(signal, frame):
        GPIO.output(pin, False)
        print('You pressed Ctrl+C!')
        sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)
print('Press Ctrl+C to quit')

status = True

while(True):

  GPIO.setup(pin, GPIO.OUT)
  print('Setting status to %s' % (not status))
  status = not status
  GPIO.output(pin, status)
  status = bool(GPIO.input(pin))
  print('Status is %s' % status)
  time.sleep(5)
