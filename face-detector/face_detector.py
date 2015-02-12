import cv2
import threading
import sys
import json
import signal
from flask import Flask, url_for
from flask import request


class FaceDetector(object):
    def __init__(self, classifier_file):

        # Initialize Flask
        self.rest = Flask(__name__)
        # Initialize OpenCV
        self.stopped = False
        self.detected_faces = None
        self.classifier_file = classifier_file
        self.width = -1
        self.height = -1

    def rest_service(self):

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
            return json.dumps(self.detected_faces)

        self.rest.run()

    def faces_detect_thread(self):

        video_capture = cv2.VideoCapture(0)
        self.width = video_capture.get(3)
        self.height = video_capture.get(4)
        classifier = cv2.CascadeClassifier(self.classifier_file)

        print('Using resolution: ' + str(self.width) + '*' + str(self.height))

        while not self.stopped:

            # Capture frame-by-frame
            ret, frame = video_capture.read()
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            self.detected_faces = classifier.detectMultiScale(
                gray,
                scaleFactor=1.1,
                minNeighbors=5,
                minSize=(30, 30),
                flags=cv2.cv.CV_HAAR_SCALE_IMAGE
            )

            print("Detected " + str(len(self.detected_faces)) + " faces")

            # Draw a rectangle around the faces
            for (x, y, w, h) in self.detected_faces:
                cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

            # Display the resulting frame
            cv2.imshow('Camera', frame)

            # cv2.imwrite('file.jpg', frame)

            if cv2.waitKey(1) & 0xFF == ord('q'):
                self.stop()

        # When everything is done, release the capture
        print("Shutting down OpenCV...")
        video_capture.release()
        cv2.destroyAllWindows()

    def start(self):
        t = threading.Thread(target=self.faces_detect_thread, args=())
        t.start()

        self.rest_service()

    def stop(self):
        self.stopped = True


def signal_term_handler(signal, frame):
    print 'got SIGTERM'
    sys.exit(0)

signal.signal(signal.SIGTERM, signal_term_handler)

cl_file = sys.argv[1]
face_detector = FaceDetector(cl_file)
face_detector.start()
