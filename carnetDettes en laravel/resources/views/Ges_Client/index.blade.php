@extends('base')

@section('titre',"Gestion des clients")

@section('contenue')
    <div class="clients-container">

        <div class="card text-black bg-light mb-3 shadow d-flex flex-row align-items-center ">
            <h1 class="text-center mb-5 mt-5 flex-grow-1">Gestion des clients</h1>
        </div>

        <!-- Bouton Ajouter -->
        <div class="action">
            <a href="{{ route('app_clients.create') }}" class="btn-ajouter bg-success">
                <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" fill="currentColor" class="bi bi-plus" viewBox="0 0 16 16">
                    <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4"/>
                </svg>
                Ajouter un client</a>

            <form class="search-form" method="GET" action="">
                <input type="search" name="search" placeholder="Rechercher un client">
                <button type="submit">Recherche</button>
            </form>
        </div>
        <hr>

        <!-- Message de succès -->
        @if(session('success'))
            <div class="alert-success">
                {{ session('success') }}
            </div>
        @endif

        <!-- Table des clients -->
        <table class="clients-table">
            <thead>
                <tr>
                    <th>Référence</th>
                    <th>Nom</th>
                    <th>Téléphone</th>
                    <th>Adresse</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                @forelse($clients as $client)
                    <tr>
                        <td>{{ 'CLT' . sprintf('%04d', $loop->iteration) }}</td>
                        <td>{{ $client->nom }}</td>
                        <td>
                            <a href="https://wa.me/{{ $client->telephone }}" target="_blank"class="whatsapp-link">
                                <img src="https://upload.wikimedia.org/wikipedia/commons/6/6b/WhatsApp.svg" alt="WhatsApp" class="whatsapp-icon">
                                {{ $client->telephone }}
                            </a>
                        </td>


                        <td>{{ $client->adresse }}</td>
                        <td>
                            <a href="{{ route('app_clients.edit', $client) }}" class="btn-modifier p-2 badge bg-primary"  onclick="return confirm('Voulez-vous modifier {{ $client->nom }}')">Modifier</a>
                            <form action="{{ route('app_clients.destroy', $client) }}" method="POST" class="inline-form">
                                @csrf
                                @method('DELETE')
                                <button type="submit" class="btn-supprimer p-2 badge bg-danger" onclick="return confirm('Supprimer ce client ?')">Supprimer

                                </button>
                            </form>
                        </td>
                    </tr>
                @empty
                    <tr>
                        <td colspan="5" class="no-clients">Aucun client enregistré.</td>
                    </tr>
                @endforelse
            </tbody>
        </table>
    </div>
@endsection
