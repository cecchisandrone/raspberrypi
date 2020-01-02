from flask import Flask
from flask import request
from flask import jsonify
from time import sleep
import subprocess

cmd = ["bash -o pipefail -c \"aurora -a 2 -D -d 0 -e /dev/ttyUSB0 | cut -f2 -d '=' | tr -s '\n' '\n'\""]
fields = {
            0: "input_1_voltage",
            1: "input_1_current",
            2: "input_1_power",
            3: "input_2_voltage",
            4: "input_2_current",
            5: "input_2_power",
            6: "grid_voltage_reading",
            7: "grid_current_reading",
            8: "grid_power_reading",
            9: "grid_frequency_reading",
            10: "dc_ac_conversion_efficiency",
            11: "inverter_temperature",
            12: "booster_temperature",
            13: "daily_energy",
            14: "weekly_energy",
            15: "monthly_energy",
            16: "yearly_energy",
            17: "total_energy",
            18: "partial_energy",
            19: "vbulk",
            20: "riso",
            21: "power_peak_today",
            22: "power_pin_1",
            23: "power_pin_2",
            24: "vbulk_mid",
            25: "vbulk_dc_dc",
            26: "ileak_dc_dc_reading",
            27: "ileak_inverter_reading",
            28: "grid_voltage_dc_dc",
            29: "grid_voltage_avg",
            30: "grid_voltage_neutral",
            31: "grid_frequency_dc_dc",
            32: "power_peak",
            33: "wind_generator_frequency"
         }

app = Flask(__name__)

@app.route('/metrics', methods=['GET'])
def metrics_endpoint():
  json = {}
  try:
    output = subprocess.check_output(cmd, shell=True)
    output = output.replace("Extended DSP Reporting",'\n').splitlines()
    # Delete empty lines (iterate backwards)
    for i in range(len(output) - 1, -1, -1):
      if output[i] == '':
        del(output[i])
    if len(output) != len(fields):
      return "Bad reading", 500
    for i in range(len(output)):
      number = output[i].lstrip().split(" ")[0]
      if i in fields:
        json[fields[i]] = number
  except subprocess.CalledProcessError as e:
    print e.returncode
    print e.output
    return "error", 500
  except OSError as e:
    print e
    return "error", 500
  return jsonify(json), 200

if __name__ == '__main__':
  app.run(host='0.0.0.0', port=6000)

