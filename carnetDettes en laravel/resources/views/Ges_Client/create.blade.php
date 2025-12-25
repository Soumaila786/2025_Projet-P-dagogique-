@extends('base')
@section('titre', 'Ajouter un client')

@section('contenue')
    <div class="form-container">
        <div class="form-box">
            <h2>Ajouter un client</h2>
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
            <form action="{{ route('app_clients.store') }}" method="POST" id="form-ajout">
                @csrf
                <input class="form-input" type="text" name="nom" id="name" placeholder="Nom complet du client" required>

                <input class="form-input" type="text" name="telephone" id="numTel" placeholder="Numéro de téléphone" required>

                <input class="form-input"  type="text" name="adresse" id="adresse" placeholder="Adresse">
                <hr>


                <button id="btn-ajouter" class="form-button" type="submit" onclick="return confirm('Voulez-vous ajouter cet ?')">Ajouter</button>
                <a href="{{ route('app_clients.index') }}" class="btn-retour badge bg-success">← Retour à la liste</a>
            </form>
        </div>
    </div>
@endsection
