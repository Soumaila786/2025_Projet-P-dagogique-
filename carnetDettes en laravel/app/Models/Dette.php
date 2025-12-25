<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Dette extends Model
{
    use HasFactory;

    protected $fillable = [
        'client_id',
        'user_id',
        'produits',
        'montant',
        'reste',
        'date',
        'status',
    ];

    protected $casts = [
        'produits' => 'array',
        'date' => 'date',
    ];


    /**Cette petite fonction dit à Laravel :
    « Une dette appartient à un client. »
    La clé étrangère par défaut est client_id.
    La clé primaire dans clients est id. */
    public function client()
    {
        return $this->belongsTo(Client::class);
    }



    //ce que determine cette est : chaque peut avoir plusieurs paiements
    public function paiements()
    {
        return $this->hasMany(Paiement::class);
    }

    public function getSoldeAttribute()
    {
        $totalPaye = $this->paiements()->sum('montant');
        return $this->montant - $totalPaye;
    }

    public function user()
    {
        return $this->belongsTo(User::class);
    }

}
