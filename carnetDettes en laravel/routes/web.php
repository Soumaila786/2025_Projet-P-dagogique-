<?php

use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AccueilController;
use App\Http\Controllers\LoginController;
use App\Http\Controllers\ClientController;
use App\Http\Controllers\DetteController;
use App\Http\Controllers\PaiementsController;
use App\Http\Controllers\TableauDeBordController;
use App\Http\Controllers\HistoriquesController;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/



Route::get('/',[AccueilController::class, 'Accueil'])->name('app_accueil');

Route::get('/logout',[LoginController::class, 'logout'])->name('app_logout');
//cette route ces pour la verification des emails pour eviter l'utilisation double des emails vu que les emails sont uniques




/* le match est utiliser ici parce quon aura besion de recevoir et d'envoyer  */
Route::match(['get', 'post'], '/CarnetDettes', [AccueilController::class , 'CarnetDettes'])->name('app_Accueil');




Route::prefix('/CarnetDettes')->middleware('auth')->group(function() {
    Route::get('/Clients', [ClientController::class, 'index'])->name('app_clients.index');
    Route::get('/Clients/ajout', [ClientController::class, 'create'])->name('app_clients.create');
    Route::post('/Clients', [ClientController::class, 'store'])->name('app_clients.store');
    Route::get('/Clients/Modifier/{client}', [ClientController::class, 'edit'])->name('app_clients.edit');
    Route::put('/Clients/Modifier/{client}', [ClientController::class, 'update'])->name('app_clients.update');
    Route::delete('/Clients/Supprimer/{client}', [ClientController::class, 'destroy'])->name('app_clients.destroy');
});


// Gestion des dettes (CRUD)
Route::prefix('/CarnetDettes')->middleware('auth')->group(function() {
    Route::get('/Dettes', [DetteController::class, 'index'])->name('app_dettes.index');          // liste
    Route::get('/Dettes/ajout', [DetteController::class, 'create'])->name('app_dettes.create');  // form ajout
    Route::post('/Dettes', [DetteController::class, 'ajouter'])->name('app_dettes.ajouter');         // enregistrement
    Route::get('/Dettes/Modifier/{dette}', [DetteController::class, 'edit'])->name('app_dettes.edit'); // form édition
    Route::put('/Dettes/Modifier/{dette}', [DetteController::class, 'update'])->name('app_dettes.update'); // mise à jour
    Route::delete('/Dettes/Supprimer/{dette}', [DetteController::class, 'destroy'])->name('app_dettes.destroy'); // suppression
    Route::post('Dettes/{dette}/payer', [DetteController::class, 'payer'])->name('app_dettes.payer');
    Route::get('/dettes/export/pdf', [DetteController::class, 'exportPDF'])->name('app_dettes.export.pdf');

});


//Gestion des paiements
Route::prefix('/CarnetDettes')->middleware('auth')->group(function(){
    Route::get('/Paiements',[PaiementsController::class , 'index'])->name('app_paiements_index');
    Route::get('/Paiements/historiques', [PaiementsController::class, 'historiques'])->name('app_paiements.historiques');  // form ajout
    Route::post('Paiements/{dette}/payer', [PaiementsController::class, 'payer'])->name('app_paiements.payer')->middleware('auth');
    Route::get('/Paiements/Modifier/{paiement}', [PaiementsController::class, 'edit'])->name('app_paiements.edit'); // form édition
    Route::put('/Paiements/Update/{paiement}', [PaiementsController::class, 'update'])->name('app_paiements.update'); // mise à jour
    Route::delete('/Paiements/Supprimer/{paiement}', [PaiementsController::class, 'destroy'])->name('app_paiements.destroy'); // suppression
    Route::put('/Paiements/Annuler/{paiement}', [PaiementsController::class, 'annuler'])->name('app_paiements.annuler');


});


Route::get('/CarnetDettes/TableauDeBord', [TableauDeBordController::class, 'index'])
    ->name('app_tableauDeBord');

