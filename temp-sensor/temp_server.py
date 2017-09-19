from flask import Flask
from flask import request

app = Flask(__name__)
id = '28-0316a02752ff'

@app.route('/temp', methods=['GET'])
def gettemp_enpoint():
  return gettemp(id), 200
    
def gettemp(id):
  
  try:
    mytemp = ''
    filename = 'w1_slave'
    f = open('/sys/bus/w1/devices/' + id + '/' + filename, 'r')
    line = f.readline() # read 1st line
    crc = line.rsplit(' ',1)
    crc = crc[1].replace('\n', '')
    if crc=='YES':
      line = f.readline() # read 2nd line
      mytemp = line.rsplit('t=',1)
    else:
      mytemp = 99999
    f.close()
    return '{:.3f}'.format(int(mytemp[1])/float(1000))

  except:
    return 99999

if __name__ == '__main__':
      app.run(host='0.0.0.0', port=6000)
