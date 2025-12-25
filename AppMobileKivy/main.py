
from kivymd.uix.screenmanager import MDScreenManager
from controller.admin_controller import AdminScreen
from controller.agent_controller import AgentScreen
from controller.login_controller import LoginScreen
from kivy.core.window import Window
from kivy.lang import Builder
from kivymd.app import MDApp

Window.size = (400, 800)  

class MainApp(MDApp):
    def build(self):
        self.title = "PARKING~MANAGEMENT"
        Builder.load_file("view/Admin.kv")
        Builder.load_file("view/Agent.kv")

        self.sm=MDScreenManager()
        self.sm.add_widget(AdminScreen())
        self.sm.add_widget(AgentScreen())
        self.sm.add_widget(LoginScreen(self))
        self.sm.current="login"

        return self.sm

    def retour_login(self):
        login_screen = self.sm.get_screen("login")
        login_screen.ids.name_input.text = ""
        login_screen.ids.password_input.text = ""
        self.sm.current = "login"

if __name__ == "__main__":
    MainApp().run()

