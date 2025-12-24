<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Client extends Model
{
    use HasFactory;
     // Champs qui peuvent être remplis en masse
    protected $fillable = [
        'nom',
        'user_id',
        'telephone',
        'adresse',
    ];

    public function dettes()
    {
        return $this->hasMany(Dette::class);
    }

    public function user()
    {
        return $this->belongsTo(User::class);
    }

}
