#!flask/bin/python
from flask import Flask, Response, jsonify, request
import os

app = Flask(__name__)

count = 0
@app.route('/')
def moveAlong():
    return 'these are not the droids you''re looking for'

@app.route('/doSomethingCool', methods=['POST'])
def doSomethingCool():
    global count
    parsed = request.get_json()
    thing = parsed['thing']
    count += 1
    return jsonify({'count': count, 'thing': thing})

if __name__ == '__main__':
    # Bind to PORT if defined, otherwise default to 5000.
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port)