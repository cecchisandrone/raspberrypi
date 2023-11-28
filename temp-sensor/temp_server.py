from flask import Flask
from flask import request
from flask import jsonify

app = Flask(__name__)
device_id = '28-fcc15b1e64ff'
temp = 99999

@app.route('/temp', methods=['GET'])
def gettemp_endpoint():
  format = request.args.get('format')
  if format == "json":
    return jsonify(temperature=gettemp()), 200
  else:
    return gettemp(), 200

def gettemp():
  global temp
  try:
    filename = 'w1_slave'
    f = open('/sys/bus/w1/devices/' + device_id + '/' + filename, 'r')
    line = f.readline() # read 1st line
    if len(line) != 0:
      print(line)
      crc = line.rsplit(' ',1)
      crc = crc[1].replace('\n', '')
      if crc=='YES':
        line = f.readline() # read 2nd line
        splittedLine = line.rsplit('t=',1)
        temp = int(splittedLine[1])/float(1000)
      else:
        temp = 99999
    else:
      print("Returning previous cached value")
    f.close()
    return '{:.3f}'.format(temp)

  except Exception as e:
    print(e)
    return 99999

if __name__ == '__main__':
  app.run(host='0.0.0.0', port=6000)
