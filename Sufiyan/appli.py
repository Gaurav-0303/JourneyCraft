import subprocess
from flask import Flask, request
app = Flask(__name__)

@app.route('/')
def index():
	return "Hello!\n visit http://127.0.0.1:5000/data?startloc=0&starttime=12&endtime=18"

@app.route('/data')
def get_data():

	startloc = request.args.get('startloc')
	starttime = request.args.get('starttime')
	endtime = request.args.get('endtime')

	response = subprocess.check_output(['java', 'RT_Design', startloc,  starttime, endtime],universal_newlines=True)
	response = response.split(';')
	for i in range(0, len(response) - 1, 2):
		time=float(response[i+1])
		hours = time // 1
		mins = time % 1	
		mins *= 60
		mins //= 1
		response[i+1] = str(int(hours)) + ':' + str(int(mins))
		response[i+1] = f'{int(hours):02d}:{int(mins):02d}'
	response = ';'.join(response)
	return response[:-1]
	# return {"a":23,"b":45,"c":[3,3]}
if __name__ == '__main__':
	app.run(host='0.0.0.0')