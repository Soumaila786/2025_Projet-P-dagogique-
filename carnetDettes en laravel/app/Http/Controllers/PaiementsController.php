<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Paiement;
use App\Models\Dette;
use App\Models\Client;
use Illuminate\Support\Facades\Auth;  // <-- Ajoute ceci

class PaiementsController extends Controller

{

    // 🔹 Middleware pour obliger l'utilisateur à être connecté
    public function __construct()
    {
        $this->middleware('auth');
    }

    //
    public function Paiements()
    {
        return view('Paiements.paiements');
    }


    // Liste de toutes les dettes
    public function index()
    {
        // On récupère toutes les dettes avec le client lié
        // Donne-moi toutes les dettes avec leur client associé.

        // On récupère seulement les dettes du user connecté avec leur client associé
        $dettes = Dette::with('client')
                    ->where('user_id', Auth::id())
                    ->get();

        // On envoie à la vue
        return view('Paiements.index', compact('dettes'));
    }



    public function payer(Request $request, Dette $dette)
    {
        // on verifie d'abord voir si le input n'est pas vide
        if (!Auth::check()) {
            return redirect()->route('login')->with('error', 'Vous devez être connecté pour effectuer un paiement.');
        }
        $validated = $request->validate([
            'montant_paye' => 'required|numeric|min:0',
        ]);
        // on affecte le montant saisie dans la variable $montantPaye
        $montantPaye = $request->input('montant_paye');
        // on verife voir le montant saisie ne depasse pas le reste.
        if ($montantPaye > $dette->reste) {
            return back()->with('error', 'Le paiement dépasse le reste dû.');
        }
        // Créer le paiement
        $paiement = new Paiement();
        $paiement->dette_id = $dette->id;
        $paiement->montant = $montantPaye;
        $paiement->date = now();
        $paiement->user_id = Auth::id(); // 🔑 associer l’utilisateur connecté
        $paiement->save();

        // Réduire le reste
        //  on prend le montantPaye et soustraite avec le reste
        $dette->reste -= $montantPaye;
        // Mettre à jour le statut
        if ($dette->reste == 0) {
            $dette->status = 'payée';
        } elseif ($dette->reste > 0 && $dette->reste < $dette->montant) {
            $dette->status = 'partielle';
        } else {
            $dette->status = 'non payée';
        }
        $dette->save();

        return back()->with('success', 'Paiement enregistré avec succès.');
    }

    //==============================================================================================================================================

    // Formulaire édition
    public function edit(Paiement $paiement)
    {

        return view('Paiements.edit', compact('paiement'));
    }



//==============================================================================================================================================

    // Mettre à jour une dette
    public function update(Request $request, Paiement $paiement)
    {
        $validated = $request->validate([
            'dette_id' => 'required|exists:dettes,id',
            'montant' => 'required|numeric|min:0',
            'date' => 'required|date',
        ]);

        $paiement->update([
            'dette_id' => $validated['dette_id'],
            'montant'   => $validated['montant'],
            'date'      => $validated['date'],
        ]);

        return redirect()->route('app_paiements.index')
                        ->with('success', 'Paiement mise à jour avec succès !');
    }


//==============================================================================================================================================

    // Supprimer une dette
    public function destroy(Paiement $paiement)
    {
        $paiement->delete();
        return redirect()->route('app_paiements.historiques')
                ->with('success', 'Paiement supprimé avec succès !');

    }


//==============================================================================================================================================

    public function historiques() {
            $paiements = Paiement::with('dette.client')
                                    ->where('user_id', Auth::id())
                                    ->get();
            return view('Paiements.historique', compact('paiements'));
    }


    public function annuler(Paiement $paiement)
    {
        $dette = $paiement->dette;

        // Ajouter le montant payé au reste de la dette
        $dette->reste += $paiement->montant;

        // Mettre à jour le statut de la dette
        if ($dette->reste == $dette->montant) {
            $dette->status = 'non payée';
        } elseif ($dette->reste > 0 && $dette->reste < $dette->montant) {
            $dette->status = 'partielle';
        }

        $dette->save();

        // Supprimer le paiement annulé
        $paiement->delete();

        return redirect()->route('app_paiements.historiques')
                            ->with('success', 'Le paiement a été annulé et la dette mise à jour.');
    }

}
