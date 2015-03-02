import RPi.GPIO as GPIO
import time
import os

GPIO.setmode(GPIO.BCM)

GPIO.setup(18, GPIO.IN, pull_up_down=GPIO.PUD_UP)

try:
	GPIO.wait_for_edge(18, GPIO.FALLING)
 	print('Executing shutdown...')
        os.system("sudo shutdown -h now");

except KeyboardInterrupt:        
	GPIO.cleanup()



