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
GPIO.setup(pin, GPIO.OUT)

def signal_handler(signal, frame):
	GPIO.output(pin, False)
        print('You pressed Ctrl+C!')
        sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)
GPIO.output(pin, True)
print('Press Ctrl+C to quit')
signal.pause()

