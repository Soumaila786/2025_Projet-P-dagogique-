<?php

namespace App\Http\Controllers;

use App\Models\Client;
use App\Models\Dette;
use App\Models\Paiement;
use Illuminate\Support\Facades\Auth;

class TableauDeBordController extends Controller
{
    public function index()
    {
        $userId = Auth::id(); // Récupère l'utilisateur connecté

        // 1. Statistiques principales
        $totalClients = Client::where('user_id', $userId)->count();
        $totalDettes = Dette::where('user_id', $userId)->sum('montant');
        $totalPaiements = Paiement::where('user_id', $userId)->sum('montant');
        $soldeGlobal = $totalDettes - $totalPaiements;

        // 2. Paiements par jour
        $paiements = Paiement::where('user_id', $userId)
            ->selectRaw('DATE(date) as jour, SUM(montant) as total')
            ->groupBy('jour')
            ->orderBy('jour')
            ->get();

        $paiementsLabels = $paiements->pluck('jour')->map(function($d){
            return date("d-m-Y", strtotime($d));
        });
        $paiementsData = $paiements->pluck('total');

        // 3. Top clients endettés
        $clients = Client::where('user_id', $userId)
            ->with(['dettes' => function($q) use ($userId) {
                $q->where('user_id', $userId)->with('paiements');
            }])
            ->get()
            ->map(function($client){
                $totalDettes = $client->dettes->sum('montant');
                $totalPaiements = $client->dettes->flatMap->paiements->sum('montant');
                return [
                    'nom' => $client->nom,
                    'solde' => $totalDettes - $totalPaiements
                ];
            })
            ->sortByDesc('solde')
            ->take(5);

        $topClientsLabels = $clients->pluck('nom');
        $topClientsData = $clients->pluck('solde');

        // 4. Dettes récentes
        $recentDettes = Dette::where('user_id', $userId)
            ->with('client')
            ->orderBy('date','asc')
            ->take(5)
            ->get();

        // 5. Envoi à la vue
        return view('TableauBord.tableauBord', compact(
            'totalClients',
            'totalDettes',
            'totalPaiements',
            'soldeGlobal',
            'paiementsLabels',
            'paiementsData',
            'topClientsLabels',
            'topClientsData',
            'recentDettes'
        ));
    }
}
