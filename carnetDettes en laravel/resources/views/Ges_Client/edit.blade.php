@extends('base')
@section('titre', 'Modifier un client')


@section('contenue')
    <div class="form-container">
        <div class="form-box">
            <h2>Modifier le client</h2>
            <hr>
            @if ($errors->any())
                <div class="alert-errors">
                    <ul>
                        @foreach ($errors->all() as $error)
                            <li>{{ $error }}</li>
                        @endforeach
                    </ul>
                </div>
            @endif
            <form action="{{ route('app_clients.update', $client) }}" method="POST">
                @csrf
                @method('PUT')
                <input class="form-input" type="text" name="nom" id="name" placeholder="Nom complet du client" value="{{ $client->nom }}" required>

                <input class="form-input" type="text" name="telephone" id="numTel" placeholder="Numéro de téléphone" value="{{ $client->telephone }}" required>

                <input class="form-input"  type="text" name="adresse" id="adresse" placeholder="Adresse" value="{{ $client->adresse }}">

                <hr>

                <button  class="form-button" type="submit" onclick="return confirm('Voulez-vous modifier cet client ?')">Mettre à jour</button>
                <a href="{{ route('app_clients.index') }}" class="btn-retour badge bg-success">← Retour à la liste</a>
            </form>
        </div>
    </div>
@endsection
