<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Paiement extends Model
{
    use HasFactory;
    protected $fillable = [
        'dette_id',
        'user_id',   // ⚠ doit absolument être là
        'montant',
        'date'
    ];

    public function dette() {
        return $this->belongsTo(Dette::class);
    }

    public function user()
    {
        return $this->belongsTo(User::class);
    }

}
