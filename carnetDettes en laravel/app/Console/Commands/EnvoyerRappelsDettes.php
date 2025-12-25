<?php

namespace App\Console\Commands;

use Illuminate\Console\Command;
use App\Models\Dette;
use App\Models\User;
use App\Notifications\RappelDetteNotification;
use Carbon\Carbon;

class EnvoyerRappelsDettes extends Command
{
    protected $signature = 'rappels:dettes';
    protected $description = 'Envoie des rappels pour les dettes proches de l’échéance';

    public function handle()
    {
        $dateLimite = Carbon::now()->addDays(3); // dettes à échéance dans 3 jours
        $dettes = Dette::where('date', '<=', $dateLimite)
                        ->where('status', 'non payée')
                        ->get();

        $gerants = User::where('role', 'gerant')->get();

        foreach ($dettes as $dette) {
            foreach ($gerants as $gerant) {
                $gerant->notify(new RappelDetteNotification($dette));
            }
        }

        $this->info('Rappels envoyés pour '. $dettes->count() .' dettes.');
    }
}
