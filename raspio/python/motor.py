import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)
GPIO.setup(17, GPIO.OUT)
GPIO.setup(27, GPIO.OUT)
GPIO.setup(22, GPIO.OUT)
GPIO.setup(10, GPIO.OUT)

try:
   while(True):

        GPIO.output(17, False)    
        GPIO.output(27, True)
	GPIO.output(22, True)
	GPIO.output(10, False)	
		
	time.sleep(5)

      	GPIO.output(17, True)
      	GPIO.output(27, False)
        GPIO.output(22, False)
        GPIO.output(10, True)

      
	time.sleep(5)

except KeyboardInterrupt:
	GPIO.output(17, False)
	GPIO.output(27, False);
	GPIO.output(22, False)
	GPIO.output(10, False)	

