import cv2
import threading
import json
from optparse import OptionParser
from flask import Flask
from flask import request
from flask import render_template
import time
import flask
import socket


class FaceDetector(object):
    def __init__(self, classifier_file, cameraindex, width, height, host, port):

        # Initialize Flask
        self.rest = Flask(__name__)
        # Initialize OpenCV
        self.stopped = False
        self.detected_faces = None
        self.classifier_file = classifier_file
        self.width = width
        self.height = height
        self.cameraindex = cameraindex
        self.buffer = None
        self.port = port
        self.host = host

    def rest_service(self):

        @self.rest.after_request
        def add_cors(resp):

            """ Ensure all responses have the CORS headers. This ensures any failures are also accessible
                by the client. """
            resp.headers['Access-Control-Allow-Origin'] = flask.request.headers.get('Origin', '*')
            resp.headers['Access-Control-Allow-Credentials'] = 'true'
            resp.headers['Access-Control-Allow-Methods'] = 'POST, OPTIONS, GET'
            resp.headers['Access-Control-Allow-Headers'] = flask.request.headers.get(
                'Access-Control-Request-Headers', 'Authorization')
            # set low for debugging
            if self.rest.debug:
                resp.headers['Access-Control-Max-Age'] = '1'
            return resp

        @self.rest.route('/config/frame')
        def frame():
            data = {
                'width': self.width,
                'height': self.height
            }
            return json.dumps(data)

        @self.rest.route('/shutdown', methods=['POST'])
        def shutdown():

            # Terminate face recognition loop
            self.stopped = True
            # Give it the time to terminate
            time.sleep(2)

            # Terminate Flask
            func = request.environ.get('werkzeug.server.shutdown')
            if func is None:
                raise RuntimeError('Not running with the Werkzeug Server')
            msg = "Shutting down Web Server..."
            print(msg)
            func()
            return msg

        @self.rest.route('/faces')
        def list_faces():

            faces = list()
            if self.detected_faces is not None:
                for (x, y, w, h) in self.detected_faces:
                    face = {'x': x.item(), 'y': y.item(), 'w': w.item(), 'h': h.item()}
                    faces.append(face)
            print faces
            return json.dumps(faces)

        @self.rest.route('/frame')
        def last_frame():

            resp = flask.make_response(self.buffer[1].tostring())
            resp.content_type = "image/jpeg"
            return resp

        @self.rest.route('/')
        def root():
            return render_template('index.html', serverUrl=self.host + ":" + str(self.port))

        self.rest.debug = True
        self.rest.run(host='0.0.0.0', port=self.port, use_reloader=False)

    def faces_detect_thread(self):

        image_scale = 2
        haar_scale = 1.2
        min_neighbors = 2

        video_capture = cv2.VideoCapture(self.cameraindex)
        video_capture.set(3, self.width)
        video_capture.set(4, self.height)
        classifier = cv2.CascadeClassifier(options.cascade)

        print "Using resolution " + str(self.width) + "*" + str(self.height)

        while not self.stopped:

            # Measure time
            start = time.time()

            ret, frame = video_capture.read()

            small_frame = cv2.resize(frame, (self.width / image_scale, self.height / image_scale))
            gray = cv2.cvtColor(small_frame, cv2.COLOR_RGB2GRAY)
            cv2.equalizeHist(gray, gray)

            detected_faces = classifier.detectMultiScale(
                gray,
                scaleFactor=haar_scale,
                minNeighbors=min_neighbors
            )

            # Draw a rectangle around the faces
            for (x, y, w, h) in detected_faces:
                pt1 = (int(x * image_scale), int(y * image_scale))
                pt2 = (int((x + w) * image_scale), int((y + h) * image_scale))
                cv2.rectangle(frame, pt1, pt2, (0, 255, 0), 2)

            # cv2.imwrite('detected.jpg', frame)

            self.buffer = cv2.imencode('.jpg', frame)

            print("Detected " + str(len(detected_faces)) + " faces in " + str(time.time() - start) + "s")

    def start(self):
        t = threading.Thread(target=self.faces_detect_thread, args=())
        t.daemon = True
        t.start()
        self.rest_service()

    def stop(self):
        self.stopped = True


parser = OptionParser(usage="usage: %prog [options]")
parser.add_option("-c", "--cascade", action="store", dest="cascade", type="str",
                  help="Haar cascade file, default %default",
                  default="haarcascade_frontalface_default.xml")
parser.add_option("-i", "--camera-index", action="store", dest="cameraindex", type="int",
                  help="Camera index, default %default",
                  default="0")
parser.add_option("-x", "--width", action="store", dest="width", type="int",
                  help="Width (px), default %default",
                  default="320")
parser.add_option("-y", "--height", action="store", dest="height", type="int",
                  help="Height (px), default %default",
                  default="200")
parser.add_option("-p", "--port", action="store", dest="port", type="int",
                  help="Port number, default %default",
                  default="5000")
parser.add_option("-H", "--host", action="store", dest="host", type="str",
                  help="Host name, default %default",
                  default="localhost")
(options, args) = parser.parse_args()

face_detector = FaceDetector(options.cascade, options.cameraindex, options.width, options.height, options.host, options.port)
face_detector.start()