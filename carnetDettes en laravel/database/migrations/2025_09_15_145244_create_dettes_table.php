<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateDettesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('dettes', function (Blueprint $table) {
            $table->id();
            $table->unsignedBigInteger('user_id'); // 🔑 propriétaire
            $table->foreignId('client_id')->constrained()->onDelete('cascade');
            $table->json('produits'); // Stockage des produits sous forme JSON
            $table->decimal('montant', 12, 2);
            $table->decimal('reste', 12, 2);   // Montant restant après paiements
            $table->date('date');
            $table->enum('status', ['non payée', 'partielle', 'payée'])->default('non payée');
            $table->timestamps();
            $table->foreign('user_id')->references('id')->on('users')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('dettes');
    }
}
