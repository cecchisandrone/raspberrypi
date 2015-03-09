def setServo(servoPin, position):
    servoStr = "P1-%u=%u%%\n" % (servoPin, position)

    with open("/dev/servoblaster", "wb") as f:
        f.write(servoStr)
