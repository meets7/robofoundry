#!flask/bin/python
from flask import Flask, jsonify, request
import MySQLdb
import config
from flask import make_response


app = Flask(__name__)
cfg = config.get()


@app.route('/robofoundry/api/read/robots', methods=['GET'])
def get_robots():
    db = MySQLdb.connect(host=cfg['mysql']['host'],
                         user=cfg['mysql']['user'],
                         passwd=cfg['mysql']['password'],
                         db=cfg['mysql']['db'])
    query = 'SELECT userID, packageID, robotID from robot'
    cur = db.cursor()
    cur.execute(query)
    row_headers = [x[0] for x in cur.description]
    rv = cur.fetchall()
    db.close()
    json_data = []
    for result in rv:
        json_data.append(dict(zip(row_headers, result)))
    return jsonify(json_data)


@app.route('/robofoundry/api/read/robot/', methods=['GET'])
def get_robot():
    robotid = request.args.get('robotid')
    packageid = request.args.get('packageid')
    userid = request.args.get('userid')
    db = MySQLdb.connect(host=cfg['mysql']['host'],
                         user=cfg['mysql']['user'],
                         passwd=cfg['mysql']['password'],
                         db=cfg['mysql']['db'])
    query = 'SELECT id, RobotCode from robot where robotID="{0}" and '\
            'packageID="{1}" and userID = "{2}"'
    query = query.format(robotid, packageid, userid)
    cur = db.cursor()
    cur.execute(query)
    row_headers = [x[0] for x in cur.description]
    rv = cur.fetchall()
    json_data = []
    for result in rv:
        json_data.append(dict(zip(row_headers, result)))
    return jsonify(json_data)


@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)


if __name__ == '__main__':
    app.run(debug=True)
