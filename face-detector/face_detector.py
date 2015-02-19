import io
import socket
import numpy as np
import picamera
import cv2
import threading
import sys
import json
import signal
from flask import Flask
from flask import request
from flask import render_template
import time
import flask


class FaceDetector(object):
    
    CAMERA_WIDTH = 640
    CAMERA_HEIGHT = 480

    def __init__(self, classifier_file):

        # Initialize Flask
        self.rest = Flask(__name__)
        # Initialize OpenCV
        self.stopped = False
        self.detected_faces = None
        self.classifier_file = classifier_file
        self.width = FaceDetector.CAMERA_WIDTH
        self.height = FaceDetector.CAMERA_HEIGHT
	self.stream = None

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
            print self.detected_faces
	    print faces       
            return json.dumps(faces)

    	@self.rest.route('/frame')
    	def last_frame():
            resp = flask.make_response(self.stream.getvalue())
            resp.content_type = "image/jpeg"
            return resp	    	
    
    	@self.rest.route('/')
    	def root():	    
    	    return render_template('index.html', serverUrl=serverUrl)
    	
    	self.rest.debug = True
        self.rest.run(host='0.0.0.0')

    def faces_detect_thread(self):

	# saving the picture to an in-program stream rather than a file
	self.stream = io.BytesIO()

	classifier = cv2.CascadeClassifier(self.classifier_file)

        print('Using resolution: ' + str(self.width) + '*' + str(self.height))

        while not self.stopped:

            # Capture frame-by-frame
	    with picamera.PiCamera() as camera:
        
	    	camera.resolution = (self.width, self.height)
	        # capture into stream
        	camera.capture(self.stream, format='jpeg')

		camera.close()

	    # convert image into numpy array
	    data = np.fromstring(self.stream.getvalue(), dtype=np.uint8)
	    # turn the array into a cv2 image
	    frame = cv2.imdecode(data, 1)


            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            self.detected_faces = classifier.detectMultiScale(
                gray,
                scaleFactor=1.1,
                minNeighbors=5,
                minSize=(30, 30),
                flags=cv2.cv.CV_HAAR_SCALE_IMAGE
            )

            # print("Detected " + str(len(self.detected_faces)) + " faces")

            # Draw a rectangle around the faces
            for (x, y, w, h) in self.detected_faces:
                cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

            # Display the resulting frame
            # cv2.imshow('Camera', frame)
            # cv2.imwrite('file.jpg', frame

            if cv2.waitKey(1) & 0xFF == ord('q'):
                self.stop()

        # When everything is done, release the capture
        print("Shutting down OpenCV...")
        video_capture.release()
        cv2.destroyAllWindows()

    def start(self):
        t = threading.Thread(target=self.faces_detect_thread, args=())
        t.daemon = True
        t.start()

        self.rest_service()

    def stop(self):
        self.stopped = True


def signal_term_handler(signal, frame):
    print 'got SIGTERM'
    sys.exit(0)


signal.signal(signal.SIGTERM, signal_term_handler)
serverUrl = '192.168.1.15:5000'
# socket.getfqdn()
cl_file = sys.argv[1]
face_detector = FaceDetector(cl_file)
face_detector.start()
