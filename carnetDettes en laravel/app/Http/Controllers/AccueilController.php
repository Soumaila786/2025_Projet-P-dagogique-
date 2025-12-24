<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class AccueilController extends Controller
{
    // Pour la page d'accueil
    public function Accueil()
    {
        return view('Accueil.accueil');
    }

    //Fonction pour le tableau de bord
    public function CarnetDettes()
    {
        return view('Accueil.accueil');
    }
}
