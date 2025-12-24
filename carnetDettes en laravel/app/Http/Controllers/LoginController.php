<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use App\Models\User;

class LoginController extends Controller
{
    // Déclaration de la variable pour stocker la requête
    protected $request;

    // Constructeur qui reçoit automatiquement la requête HTTP
    public function __construct(Request $request)
    {
        $this->request = $request;
    }

    /**
     * Déconnexion de l'utilisateur
     */
    public function logout()
    {
        Auth::logout(); // Déconnecte l'utilisateur
        return redirect()->route('login'); // Redirige vers la page de connexion
    }

}
