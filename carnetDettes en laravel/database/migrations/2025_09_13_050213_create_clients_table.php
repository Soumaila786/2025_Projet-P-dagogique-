<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateClientsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('clients', function (Blueprint $table) {
            $table->id(); // identifiant unique
            $table->foreignId('user_id')->constrained()->onDelete('cascade');
            $table->string('nom'); // nom du client
            $table->string('telephone'); // téléphone unique
            $table->string('adresse')->nullable(); // adresse optionnelle
            $table->timestamps(); // created_at et updated_at
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
        Schema::dropIfExists('clients');
    }
}
