from kivy.lang import Builder
from kivymd.app import MDApp
from kivymd.uix.screen import MDScreen
from kivy_garden.zbarcam import ZBarCam

KV = '''
MDScreen:
    ZBarCam:
        id: zbarcam
        on_symbols: app.on_qr_scanned(self.symbols)
'''

class QRCodeScannerApp(MDApp):
    def build(self):
        return Builder.load_string(KV)

    def on_qr_scanned(self, symbols):
        if symbols:
            for symbol in symbols:
                print(f"Ticket de : {symbol.data.decode('utf-8')}")
                

if __name__ == '__main__':
    QRCodeScannerApp().run()