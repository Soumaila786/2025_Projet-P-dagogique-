@extends('base')
@section('titre', 'Ajouter une Dette')

@section('contenue')
<div class="form-container">
    <div class="form-box">
        <h2>Ajouter une Dette</h2>
        

        @if ($errors->any())
        <div class="alert alert-danger">
            <ul>
                @foreach($errors->all() as $error)
                    <li>{{ $error }}</li>
                @endforeach
            </ul>
        </div>
        @endif

        <form action="{{ route('app_dettes.ajouter') }}" method="POST">
            @csrf

            <div>
                <button type="button" class="form-button" data-bs-toggle="modal" data-bs-target="#modalAjouterClient">
                    Ajouter un client s'il n'existe pas
                </button>
            </div>
            <hr>
            <!-- Client -->
            <div class="mb-3 mt-3 d-flex align-items-center">
                <select name="client_id" id="client_id" class="form-input me-2" required>
                    <option value="">-- Choisir un client --</option>
                    @foreach($clients as $client)
                        <option value="{{ $client->id }}">{{ $client->nom }}</option>
                    @endforeach
                </select>
            </div>

            <!-- Produits (séparés par des virgules) -->
            <div class="mb-3">
                <input type="text" name="produits" class="form-input" placeholder="Ex: Produit1, Produit2, Produit3" required>
                <small>Saisissez tous les produits séparés par des virgules.</small>
            </div>

            <!-- Montant -->
            <div class="mb-3">
                <input type="number" name="montant" class="form-input" required placeholder="Montant total">
            </div>

            <!-- Date -->
            <div class="mb-3">
                <input type="date" name="date" class="form-input" required>
            </div>

            <!-- Status et reste -->
            <input type="hidden" name="status" value="non payée">
            <input type="hidden" name="reste" value="{{ old('montant') ?? 0 }}">
            <hr>

            <button type="submit" class="form-button">Enregistrer la dette</button>
            <div>

                <a href="{{ route('app_dettes.index') }}" class="btn-retour badge bg-success">← Retour à la liste</a>
            </div>
        </form>
    </div>
</div>

<!-- Modal pour ajouter un client -->
<div class="modal fade" id="modalAjouterClient" tabindex="-1" aria-labelledby="modalAjouterClientLabel" aria-hidden="true">
    <div class="modal-dialog">
        <form action="{{ route('app_clients.store') }}" method="POST" class="modal-content">
            @csrf
            <div class="modal-header">
                <h3 class="modal-title text-center mb-5" id="modalAjouterClientLabel">Ajouter un nouveau client</h3>
            </div>
            <div class="modal-body">
                <div>
                    <label for="nom_client" class="form-label">Nom du client</label>
                    <input type="text" name="nom" id="nom_client" class="form-input" required>
                </div>
                <div>
                    <label for="telephone_client" class="form-label">Téléphone</label>
                    <input type="text" name="telephone" id="telephone_client" class="form-input">
                </div>
                <div>
                    <label for="adresse" class="form-label">Adresse</label>
                    <input type="adresse" name="adresse" id="adresse" class="form-input">
                </div>
            </div>
            <div class="modal-footer align-text-center">
                <button type="submit" class="btn-ajouter">Enregistrer le client</button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
            </div>
        </form>
    </div>
</div>
@endsection
