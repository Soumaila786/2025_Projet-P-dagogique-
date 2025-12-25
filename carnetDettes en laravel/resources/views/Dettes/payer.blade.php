@extends('base')
@section('titre', 'Paiement de la Dette')

@section('contenue')
<div class="form-container">
    <h2>Payer la Dette : {{ $dette->reference }}</h2>
    <p>Client : {{ $dette->client->nom }}</p>
    <p>Montant total : {{ $dette->montant }} | Reste : {{ $dette->reste }}</p>

    @if(session('success'))
        <div class="alert alert-success">{{ session('success') }}</div>
    @endif
    @if(session('error'))
        <div class="alert alert-danger">{{ session('error') }}</div>
    @endif

    <form action="{{ route('app_dettes.payer', $dette) }}" method="POST">
        @csrf
        <div class="mb-3">
            <label for="montant_paye">Montant à payer</label>
            <input type="number" name="montant_paye" class="form-control" max="{{ $dette->reste }}" required>
        </div>
        <button type="submit" class="btn btn-primary " onclick="return confirm('Veuillez confirmer le paiement de {{ $dette->client->nom }}')">Valider le paiement</button>
    </form>

    <a href="{{ route('app_dettes.index') }}" class="btn btn-secondary mt-3">← Retour à la liste</a>
</div>
@endsection
