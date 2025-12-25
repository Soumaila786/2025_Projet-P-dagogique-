<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Dette;
use App\Models\Client;
use App\Models\Paiement;
use Barryvdh\DomPDF\Facade\Pdf;
use Illuminate\Support\Facades\Auth;  // <-- Ajoute ceci


class DetteController extends Controller
{
    // public function index(Request $request)
    // {
    //     $search = trim($request->input('search', ''));

    //     $query = Dette::with('client');

    //     if ($search !== '') {
    //         $query->where(function($q) use ($search) {
    //             // Recherche sur le nom du client
    //             $q->whereHas('client', function($q2) use ($search) {
    //                 $q2->where('nom', 'like', "%{$search}%");
    //             })
    //             // OU statut de la dette
    //             ->orWhere('status', 'like', "%{$search}%");
    //         });
    //     }

    //     $dettes = $query->orderBy('date', 'asc')->get();

    //     return view('Dettes.index', compact('dettes'));
    // }


    public function index(Request $request)
    {
        $search = trim($request->input('search', ''));

        // On commence toujours par filtrer sur l'utilisateur connecté
        $query = Dette::with('client')
                        ->where('user_id', Auth::id());

        if ($search !== '') {
            $query->where(function($q) use ($search) {
                // Recherche sur le nom du client
                $q->whereHas('client', function($q2) use ($search) {
                    $q2->where('nom', 'like', "%{$search}%");
                })
                // OU statut de la dette
                ->orWhere('status', 'like', "%{$search}%");
            });
        }

        $dettes = $query->orderBy('date', 'asc')->get();

        return view('Dettes.index', compact('dettes'));
    }





//==============================================================================================================================================

    // Formulaire pour créer une nouvelle dette
    public function create()
    {
        // récupérer uniquement les clients de l'utilisateur connecté
        $clients = Client::where('user_id', auth()->id())->get();

        return view('Dettes.create', compact('clients'));
    }



//==============================================================================================================================================


    // Enregistrement
    public function ajouter(Request $request)
    {
        /*Vérifie que les données envoyées dans la requête (formulaire, API, etc.) respectent les règles définies.
        Si les règles sont respectées → les données validées sont retournées dans $validated.
        Si les règles échouent → Laravel renvoie automatiquement une erreur de validation (et un message à l’utilisateur). */
        $validated = $request->validate([
            'client_id' => 'required|exists:clients,id',  // vérifie que la valeur de client_id existe bien dans la table clients, colonne id.
            'produits'  => 'required|string',
            'montant'   => 'required|numeric|min:0',  // min:0 → doit être ≥ 0 (pas de montant négatif).
            'date'      => 'required|date',
        ]);



        // cette ligne transforme tous les produit saisie en tableau pour pouvoir les inserer dans bd
        /*explode(',', $validated['produits']) découpe la chaîne en morceaux selon la virgule ,
        trim() à chaque élément du tableau → supprime les espaces avant/après chaque mot.
        Prends la chaîne de produits séparés par des virgules, coupe-la en plusieurs morceaux, puis enlève les espaces autour de chaque morceau. */
        $produits = array_map('trim', explode(',', $validated['produits']));



        $dette = new Dette();  //  Crée une instance vide
        // Remplit les champs
        $dette->client_id = $validated['client_id'];
        $dette->user_id = Auth::id(); // 🔑 lier à l’utilisateur connecté
        $dette->produits   = $produits;
        $dette->montant    = $validated['montant'];
        $dette->reste      = $validated['montant']; // reste = montant total au départ
        $dette->date       = $validated['date'];
        $dette->status     = 'non payée';
        //  Sauvegarde dans la base de données
        $dette->save();


        return redirect()->route('app_dettes.index')
                        ->with('success', 'Dette enregistrée avec succès !');
    }



//==============================================================================================================================================

    // Formulaire édition
    public function edit(Dette $dette)
    {
        // Charge tous les clients pour pouvoir remplir un <select> dans le formulaire.
        //  ce quon fait ici SELECT * FROM clients; et SELECT * FROM dettes WHERE id = id_dette LIMIT 1;
        $clients = Client::where('user_id', auth()->id())->get();

        return view('Dettes.edit', compact('dette', 'clients'));
    }



//==============================================================================================================================================

   // Mettre à jour une dette
    public function update(Request $request, $id)
    {
        // On récupère uniquement la dette de l'utilisateur connecté
        $dette = Dette::where('user_id', auth()->id())->findOrFail($id);

        $validated = $request->validate([
            'client_id' => 'required|exists:clients,id',
            'produits' => 'required|string',
            'montant' => 'required|numeric|min:0',
            'date' => 'required|date',
        ]);

        $dette->update([
            'client_id' => $validated['client_id'],
            'produits' => array_map('trim', explode(',', $validated['produits'])),
            'montant'   => $validated['montant'],
            'reste'     => $validated['montant'],
            'date'      => $validated['date'],
        ]);

        // Recalcul du reste et du statut
        $dette->reste = max(0, $dette->reste);

        if ($dette->reste == 0) {
            $dette->status = 'payée';
        } elseif ($dette->reste > 0 && $dette->reste < $dette->montant) {
            $dette->status = 'partielle';
        } else {
            $dette->status = 'non payée';
        }

        $dette->save();

        return redirect()->route('app_dettes.index')
                        ->with('success', 'Dette mise à jour avec succès !');
    }

    // Supprimer une dette
    public function destroy($id)
    {
        // On supprime uniquement une dette appartenant à l'utilisateur connecté
        $dette = Dette::where('user_id', auth()->id())->findOrFail($id);
        $dette->delete();

        return redirect()->route('app_dettes.index')
                        ->with('success', 'Dette supprimée avec succès !');
    }



//==============================================================================================================================================

    // public function payer(Request $request, Dette $dette)
    // {
    //     // on verifie d'abord voir si le input n'est pas vide
    //     $validated = $request->validate([
    //         'montant_paye' => 'required|numeric|min:0',
    //     ]);

    //     // on affecte le montant saisie dans la variable $montantPaye
    //     $montantPaye = $request->input('montant_paye');

    //     // on verife voir le montant saisie ne depasse pas le reste.
    //     if ($montantPaye > $dette->reste) {
    //         return back()->with('error', 'Le paiement dépasse le reste dû.');
    //     }

    //     if($montantPaye == 0){
    //         return back()->with('error','Veuillez saisir le montant a payée');
    //     }

    //     // Créer le paiement
    //     $paiement = new Paiement();
    //     $paiement->dette_id = $dette->id;
    //     $paiement->montant = $montantPaye;
    //     $paiement->date = now();
    //     $paiement->save();

    //     $dette->reste -= $montantPaye;
    //     // Mettre à jour le statut
    //     if ($dette->reste == 0) {
    //         $dette->status = 'payée';
    //     } elseif ($dette->reste > 0 && $dette->reste < $dette->montant) {
    //         $dette->status = 'partielle';
    //     } else {
    //         $dette->status = 'non payée';
    //     }
    //     $dette->save();

    //     return back()->with('success', 'Paiement enregistré avec succès.');
    // }


//==============================================================================================================================================

    public function exportPDF()
    {
        $dettes = Dette::where('user_id', auth()->id())->with('client')->get();

        $pdf = Pdf::loadView('dettes.pdf', compact('dettes'));
        return $pdf->download('dettes.pdf');
    }
}
