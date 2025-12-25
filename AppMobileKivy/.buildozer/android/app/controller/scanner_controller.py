from kivymd.uix.screen import MDScreen
from kivy.clock import Clock
from kivy.graphics.texture import Texture
from kivy.uix.image import Image as KivyImage
from kivymd.toast import toast
import numpy as np
import cv2

class ScannerBaseScreen(MDScreen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.capture = None
        self.event = None
        self.img_widget = None
        self.qr_detector = cv2.QRCodeDetector()


#============================================================================================================================================

    def demarrer_camera(self):
        container = self.ids.conteneur_camera
        container.clear_widgets()
        

        self.capture = cv2.VideoCapture(0)
        if not self.capture.isOpened():
            return

        self.img_widget = KivyImage()
        container.add_widget(self.img_widget)
        self.event = Clock.schedule_interval(self.mise_a_jour_camera, 1.0 / 30.0)


#============================================================================================================================================



    def arreter_camera(self):
        if self.capture:
            self.capture.release()
            self.capture = None
        if self.event:
            self.event.cancel()
            self.event = None
        # on essaie de nettoyer l'ecran
        try:
            container = self.ids.conteneur_camera
            container.clear_widgets() 
            self.ids.qr_message.text = "" 
            self.ids.typeEngin_input.text = ""
            self.img_widget = None
        except Exception as e:
            toast("Caméra arrêtée")


#============================================================================================================================================



    def mise_a_jour_camera(self, dt=0):
        if self.capture:
            ret, frame = self.capture.read()
            if ret:
                data, bbox, _ = self.qr_detector.detectAndDecode(frame)
                if data:
                    self.qr_detecte(data)

                buf = cv2.flip(frame, 0)
                buf = cv2.cvtColor(buf, cv2.COLOR_BGR2RGB)
                texture = Texture.create(size=(buf.shape[1], buf.shape[0]), colorfmt='rgb')
                texture.blit_buffer(buf.tobytes(), colorfmt='rgb', bufferfmt='ubyte')
                if self.img_widget:
                    self.img_widget.texture = texture
    

#============================================================================================================================================


    # pour convertir les image en binaire 
    def conversion_image(self,img):
        with open(img,'rb') as fichier:
            image_binaire=fichier.read()
            return image_binaire
    

    def reconvertir_en_image(blob):
        import cv2
        from kivy.graphics.texture import Texture

        arr = np.frombuffer(blob, dtype=np.uint8)
        img = cv2.imdecode(arr, cv2.IMREAD_COLOR)  # décodage PNG/JPEG
        if img is None:
            return None
        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        img = cv2.flip(img, 0)
        h, w = img.shape[:2]
        tex = Texture.create(size=(w, h), colorfmt='rgb')
        tex.blit_buffer(img.tobytes(), colorfmt='rgb', bufferfmt='ubyte')
        return tex
