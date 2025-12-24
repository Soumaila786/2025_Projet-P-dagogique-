<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Client;
use Illuminate\Support\Facades\Auth;  // <-- Ajoute ceci

class ClientController extends Controller
{
    //
    public function Clients()
    {
        return view('Ges_Client.index');
    }


//=============================================================================================================================================


    // public function index(Request $request)
    // {
    //     //  Prépare une requête Eloquent sur le modèle Client
    //     // Cela ne récupère pas encore les données, mais crée un objet query qu'on peut modifier.
    //     $requete = Client::query();

    //     // Vérifie si la requête HTTP contient le champ 'search' ET qu'il n'est pas vide
    //     // $request->filled('search') retourne true si l'utilisateur a saisi quelque chose
    //     if ($request->filled('search')) {

    //         //  Récupère la valeur saisie dans le champ 'search'
    //         $valeurSaisie = $request->search;

    //         //  Ajoute une condition "WHERE" sur la colonne 'nom' pour filtrer par le texte saisi
    //         // "%{$valeurSaisie}%" signifie : contient la valeur de $valeurSaisie (LIKE en SQL)
    //         $requete->where('nom', 'like', "%{$valeurSaisie}%")

    //             //  Ajoute une condition "OR WHERE" sur la colonne 'telephone'
    //             // Si le nom ne correspond pas, on vérifie si le téléphone contient la valeur recherchée
    //             ->orWhere('telephone', 'like', "%{$valeurSaisie}%")

    //             //  Ajoute une autre condition "OR WHERE" sur la colonne 'adresse'
    //             // Si ni le nom ni le téléphone ne correspondent, on vérifie l'adresse
    //             ->orWhere('adresse', 'like', "%{$valeurSaisie}%");
    //     }

    //     //  Exécute la requête et récupère tous les clients correspondant aux conditions
    //     // get() transforme l'objet query en une collection de résultats
    //     $clients = $requete->get();

    //     // Retourne la vue avec la variable clients
    //     return view('Ges_Client.index', compact('clients'));
    // }


    public function index(Request $request)
    {
        $requete = Client::query();

        // Si recherche remplie
        if ($request->filled('search')) {
            $valeurSaisie = $request->search;
            $requete->where(function($q) use ($valeurSaisie) {
                $q->where('nom', 'like', "%{$valeurSaisie}%")
                ->orWhere('telephone', 'like', "%{$valeurSaisie}%")
                ->orWhere('adresse', 'like', "%{$valeurSaisie}%");
            });
        }

        // Filtrer par utilisateur connecté
        $requete->where('user_id', Auth::id());

        $clients = $requete->get();

        return view('Ges_Client.index', compact('clients'));
    }



//=============================================================================================================================================


    //Cette fonction permet de reourner une vue qui contient un  formulaire d'ajout d'un clients
    public function create(){
        return view('Ges_Client.create');
    }


//=============================================================================================================================================


    // Enregistrer un nouveau client
    public function store(Request $request)
    {
        // Validation des données .verifie d'abord si ya des donnees dans les input
        $validated = $request->validate([
            'nom' => 'required|string|max:255',
            'telephone' => 'required|string|max:15',
            'adresse'=>'required|string|max:255',
        ]);

        // Insertion dans la base
        //Client::create($validated);

        // Création manuelle du client
        $client = new Client();
        $client->nom = $validated['nom'];
        $client->telephone = $validated['telephone'];
        $client->adresse = $validated['adresse'];
        $client->user_id = Auth::id();  // Associe au user connecté
        // Sauvegarde dans la base
        $client->save();

        return redirect()->route('app_clients.index')
                        ->with('success', 'Client ajouté avec succès !');
    }


//=============================================================================================================================================


    // Supprimer un client
    public function destroy(Client $client)
    {
        // On supprime uniquement une dette appartenant à l'utilisateur connecté
        //$client = Client::where('user_id', auth()->id())->findOrFail($id);
        $client->delete();
        return redirect()->route('app_clients.index')
                        ->with('success', 'Client supprimé avec succès');
    }

//=============================================================================================================================================


    public function edit(Client $client)
    {
        // On envoie le client à la vue pour pré-remplir le formulaire
        return view('Ges_Client.edit', compact('client'));
    }


//=============================================================================================================================================

    public function update(Request $request, Client $client)
    {
        //$client = Client::where('user_id',auth()->id()->findOrFail($id));
        $request->validate([
            'nom' => 'required',
            'telephone' => 'required',
            'adresse' => 'nullable',
        ]);

        // Mise à jour des données
        $client->update($request->all());

        return redirect()->route('app_clients.index')
                        ->with('success', 'Client mis à jour avec succès');
    }







}
