import cv2
import threading
import json
from optparse import OptionParser
from flask import Flask
from flask import request
from flask import render_template
import time
import flask
import logging
import os.path
import picamera
import picamera.array
import servo_control


class FaceDetector(object):

    DEFAULT_PAN = 50
    DEFAULT_TILT = 90
    NO_PANTILT_INTERVAL = 15
    PAN_STEP = 3
    TILT_STEP = 4

    def __init__(self, options):

        if not os.path.isfile(options.classifier_file):
            raise IOError("Classifier file not found")
        # Initialize Flask
        self.rest = Flask(__name__)
        # Initialize OpenCV
        self.stopped = False
        self.detected_faces = None
        self.classifier_file = options.classifier_file
        self.width = options.width
        self.height = options.height
        self.buffer = None
        self.port = options.port
        self.host = options.host
        self.currentPan = FaceDetector.DEFAULT_PAN
        self.currentTilt = FaceDetector.DEFAULT_TILT
        self.panMotorPin = options.panMotorPin
        self.tiltMotorPin = options.tiltMotorPin

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
            return json.dumps(faces)

        @self.rest.route('/frame')
        def last_frame():

            resp = flask.make_response(self.buffer[1].tostring())
            resp.content_type = "image/jpeg"
            return resp

        @self.rest.route('/')
        def root():
            return render_template('index.html', serverUrl=self.host + ":" + str(self.port))

        log = logging.getLogger('werkzeug')
        log.setLevel(logging.ERROR)
        self.rest.debug = False
        self.rest.run(host='0.0.0.0', port=self.port, use_reloader=False)

    def faces_detect_thread(self):

        image_scale = 2
        haar_scale = 1.2
        min_neighbors = 3

        classifier = cv2.CascadeClassifier(self.classifier_file)

        print "Using resolution " + str(self.width) + "*" + str(self.height)

        with picamera.PiCamera() as camera:
            camera.resolution = (self.width, self.height)

            while not self.stopped:

                # Measure time
                start = time.time()

                with picamera.array.PiRGBArray(camera) as stream:

                    # Create the in-memory stream
                    camera.capture(stream, 'bgr', use_video_port=True)
                    frame = stream.array

                # Resize for better speed
                small_frame = cv2.resize(frame, (self.width / image_scale, self.height / image_scale))
                gray = cv2.cvtColor(small_frame, cv2.COLOR_RGB2GRAY)
                cv2.equalizeHist(gray, gray)

                self.detected_faces = classifier.detectMultiScale(gray, scaleFactor=haar_scale,
                                                                  minNeighbors=min_neighbors,
                                                                  minSize=(20, 20), flags=cv2.cv.CV_HAAR_SCALE_IMAGE)

                # Rescale to original size
                self.detected_faces = map(lambda x: x * image_scale, self.detected_faces)

                for (x, y, w, h) in self.detected_faces:
                    pt1 = (x, y)
                    pt2 = (x + w, y + h)

                    # Draw a rectangle around the faces
                    cv2.rectangle(frame, pt1, pt2, (0, 255, 0), 2)

                    halfx = self.width / 2
                    halfy = self.height / 2

                    if (x + w / 2) > halfx + FaceDetector.NO_PANTILT_INTERVAL:
                        self.currentPan += FaceDetector.PAN_STEP
                    elif (x + w / 2) < halfx - FaceDetector.NO_PANTILT_INTERVAL:
                        self.currentPan -= FaceDetector.PAN_STEP

                    if (y + h / 2) > halfy + FaceDetector.NO_PANTILT_INTERVAL:
                        self.currentTilt += FaceDetector.TILT_STEP
                    elif (y + h / 2) < halfy - FaceDetector.NO_PANTILT_INTERVAL:
                        self.currentTilt -= FaceDetector.TILT_STEP

                    servo_control.setServo(self.panMotorPin, self.currentPan)
                    servo_control.setServo(self.tiltMotorPin, self.currentTilt)

                    # Take the 1st face
                    break

                fps = str(round(1 / (time.time() - start), 2)) + " FPS"

                cv2.putText(frame, fps, (15, 15), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255))

                self.buffer = cv2.imencode('.jpg', frame)

                print("Detected " + str(len(self.detected_faces)) + " faces in " + str(time.time() - start) + "s")

    def start(self):
        t = threading.Thread(target=self.faces_detect_thread, args=())
        t.daemon = True
        t.start()
        self.rest_service()


    def stop(self):
        self.stopped = True


parser = OptionParser(usage="usage: %prog [options]")
parser.add_option("-c", "--classifierFile", action="store", dest="classifier_file", type="str",
                  help="Haar cascade file, default %default",
                  default=os.path.dirname(os.path.realpath(__file__)) + "/haarcascade_frontalface_default.xml")
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
parser.add_option("-P", "--panMotorPin", action="store", dest="panMotorPin", type="int",
                  help="Pan motor pin number, default %default",
                  default="16")
parser.add_option("-T", "--tiltMotorPin", action="store", dest="tiltMotorPin", type="int",
                  help="Tilt motor pin number, default %default",
                  default="18")
(options, args) = parser.parse_args()

face_detector = FaceDetector(options)
face_detector.start()