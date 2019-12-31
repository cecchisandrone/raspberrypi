# Aurora Inverter monitor

HTTP endpoint to return aurora cmdline tool (http://www.curtronics.com/Solar/AuroraData.html) as JSON. In order to run, aurora executable must be in path

## How to install Aurora

````
sudo su
wget http://www.curtronics.com/Solar/ftp/aurora-1.8.3.tar.gz
tar -xzvf aurora-1.8.3.tar.gz
rm aurora-1.8.3.tar.gz
cd aurora-1.8.3
make
make install
stty -F /dev/ttyUSB0 19200   # you must set boud rate to 19200
aurora -a 2 -e /dev/ttyUSB0  # command for get cumulated energy readings
````

## How to start at boot

python /home/pi/raspberrypi/inverter-monitor/inverter-monitor.py &
