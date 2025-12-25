from kivy.lang import Builder

from kivymd.app import MDApp


class Example(MDApp):
    def build(self):
        return Builder.load_string(
            '''
MDScreen:
    MDBoxLayout:
        orientation:"vertical"
        
        MDTopAppBar:
            title: " Paramètre"
            md_bg_color: 0, 0, 0, 1  # noir opaque
            specific_text_color: 1, 1, 1, 1  # texte blanc
            elevation: 4
            pos_hint: {"top": 1}
    
        MDBottomNavigation:
            panel_color: 1, 1, 1, 1


            MDBottomNavigationItem:
                name: 'screen 1'
                text: 'A propos'
                icon: "information-outline"
                FitImage:
                    source: "images/fond.jpg"
                    allow_stretch: True
                    keep_ratio: False

                AnchorLayout:
                    anchor_x: "center"
                    anchor_y: "center"
                    orientation: "vertical"


                    MDCard:
                        orientation: "vertical"
                        size_hint: None, None  
                        height:400
                        width:350
                        pos_hint: {"center_x": 0.5,"center_y":.5}
                        elevation: 1
                        padding: "20dp"
                        spacing: "15dp"
                        radius: [20, ]
                        MDBoxLayout:
                            orientation:'vertical'
                            padding:dp(10)
                            spacing:dp(10)
                        
                            MDCard:
                                size_hint: None, None
                                size: dp(100), dp(100)
                                radius: [20,]  # cercle parfait (demi-taile hauteur)
                                md_bg_color: 1, 1, 1, 1  # fond bleu opaque
                                elevation: 5
                                pos_hint: {"center_x": 0.5}

                                FitImage:
                                    source: "images/pkm.png"
                                    radius: [75,]
                                    keep_ratio: True
                                    allow_stretch: True
                            MDLabel:
                                text:"Version : 0.0.1"
                                halign:"left"
                                theme_text_color: "Custom"
                                text_color: 0, 0, 1, 1
                                font_name:"Roboto-Italic"
                                font_style: "H6"
                            MDLabel:
                            MDLabel:
                                text:"Cette application permet de gérer les entrées et sorties des Engins dans un parking, avec génération automatique des tickets et calcul des montants."
                                font_style:"Subtitle2"
                            MDLabel:
                            
                            MDLabel:
                                text:"Developpé par: Proj-Python~BF"
                                font_name:"Arial"
                                theme_text_color: "Custom"
                                text_color: 0, 0, 1, 1  
                                halign:"center"
                                font_style:"H6"
                            MDLabel:
                                
                            MDLabel:
                                text:"Whatsapp : +226 78612534/07930231/74580949"
                                font_name:"Arial"
                                theme_text_color: "Custom"
                                text_color: 0, 0, 1, 1  
                                halign:"center"
                                font_style:"Subtitle2"
                            MDLabel:
                            
                            
                            
                            MDLabel:
                                text:"© 2025 Tous droits réservés."
                                halign:"center"
                                font_style:"Subtitle2"

            MDBottomNavigationItem:
                name: 'screen 2'
                text: 'Changer Mot de passe'
                icon: 'lock'

                FitImage:
                    source: "images/fond.jpg"
                    allow_stretch: True
                    keep_ratio: False

                AnchorLayout:
                    anchor_x: "center"
                    anchor_y: "center"
                    orientation: "vertical"


                    MDCard:
                        orientation: "vertical"
                        size_hint: None, None  
                        height:400
                        width:350
                        pos_hint: {"center_x": 0.5,"center_y":.5}
                        elevation: 1
                        padding: "20dp"
                        spacing: "15dp"
                        radius: [20, ]
                        MDLabel:
                            text:"Changer votre mot de passe"
                            halign:"center"
                            bold:True
                            font_style:"H5"
                        Widget:
                        MDLabel:
                            text:"Veuillez remplir les champs afin de confirmer votre identité."
                            halign:"center"
                            font_name:"Roboto-Italic"
                            font_style: "Subtitle1"
                        MDLabel:
                        
                        MDSeparator
                            height:"2dp"
                        
                        MDTextField:
                            id: ancienMdp_input
                            hint_text:"ancien mot de passe"
                            mode:"rectangle"
                        MDTextField:
                            id: nouveauMdp_input
                            hint_text:"Nouveau mot de passe"
                            mode:"rectangle"
                        MDTextField:
                            id: confirmerMdp_input
                            hint_text:"Confirmer votre mot de passe"
                            mode:"rectangle"
                        Widget:
                        Widget:
                        MDBoxLayout:
                            orientation:"horizontal"
                            padding:dp(10)
                            spacing:dp(5)
                            MDRectangleFlatIconButton:
                                text: "Enregistrer"
                                icon: "check-bold" 
                                theme_text_color: "Custom"
                                text_color: 1, 1, 1, 1
                                line_color: 0, 0, 0, 0    
                                md_bg_color: 0, 0, 0, 1  
                                pos_hint: {"center_x": 0.5, "center_y": 0.5} 
                            MDRectangleFlatIconButton:
                                text: "Annuler"
                                icon: "close" 
                                theme_text_color: "Custom"
                                bold:True
                                text_color: 1, 1, .5, 1
                                md_bg_color: 1, 0, 0, 1  
                                pos_hint: {"center_x": 0.5, "center_y": 0.5} 

'''
        )


Example().run()