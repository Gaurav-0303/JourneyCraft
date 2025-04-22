import subprocess
from flask import Flask, request, render_template

app = Flask(__name__)

@app.route('/')
def index():
    return render_template("form.html")
@app.route('/data')
def get_data():
    startloc = request.args.get('startloc')
    starttime = request.args.get('starttime')
    endtime = request.args.get('endtime')

    response = subprocess.check_output(['java', 'RT_Design', startloc, starttime, endtime], universal_newlines=True)
    response = response.split(';')

    # Process the response (time conversion as needed)
    for i in range(0, len(response) - 1, 2):
        time = float(response[i + 1])
        hours = time // 1
        mins = (time % 1) * 60
        response[i + 1] = f'{int(hours):02d}:{int(mins):02d}'

    # Join the processed response
    processed_response = ';'.join(response)

    return render_template('out.html', response=processed_response)

if __name__ == '__main__':
    app.run(host='0.0.0.0')
