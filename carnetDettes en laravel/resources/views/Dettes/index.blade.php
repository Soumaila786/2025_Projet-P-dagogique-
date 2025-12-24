@extends('base')
@section('titre', 'Gestion des Dettes')

@section('contenue')
<div class="dettes-container">
    <div class="paiement-container">
        <div class="card text-black bg-light mb-3 shadow d-flex flex-row align-items-center ">
            <h1 class="text-center mb-5 mt-5 flex-grow-1">Gestion des dettes</h1>
        </div>
        <div class="d-flex flex-row align-items-center ">

            <a href="{{ route('app_dettes.export.pdf') }}" class="btn btn-dark mb-4 ms-auto">
                <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" fill="currentColor" class="bi bi-printer" viewBox="0 0 16 16">
                <path d="M2.5 8a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1"/>
                <path d="M5 1a2 2 0 0 0-2 2v2H2a2 2 0 0 0-2 2v3a2 2 0 0 0 2 2h1v1a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2v-1h1a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2h-1V3a2 2 0 0 0-2-2zM4 3a1 1 0 0 1 1-1h6a1 1 0 0 1 1 1v2H4zm1 5a2 2 0 0 0-2 2v1H2a1 1 0 0 1-1-1V7a1 1 0 0 1 1-1h12a1 1 0 0 1 1 1v3a1 1 0 0 1-1 1h-1v-1a2 2 0 0 0-2-2zm7 2v3a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1v-3a1 1 0 0 1 1-1h6a1 1 0 0 1 1 1"/>
                </svg>
                Imprimer la liste
            </a>
        </div>
        <!-- Bouton Ajouter une dette -->
    <div class="action">
        <a href="{{ route('app_dettes.create') }}" class="btn-ajouter bg-success">
            <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" fill="currentColor" class="bi bi-plus" viewBox="0 0 16 16">
                <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4"/>
            </svg>
            Ajouter une Dette </a>

            
            <!-- Recherche (optionnelle) -->
            <form class="search-form" method="GET" action="">
                <input type="search" name="search" placeholder="Rechercher une dette" value="{{ request('search') }}">
                <button type="submit">Recherche</button>
            </form>
        </div>
        <hr>
        
    <!-- Message de succès -->
    @if(session('success'))
        <div class="alert-success" role="alert">
            {{ session('success') }}
        </div>
    @endif

    <!-- Tableau des dettes -->
    <div class="table-responsive">
        <table class="dettes-table">
            <thead>
                <tr>
                    <th>Réf Dette</th>
                    <th>Nom Client</th>
                    <th>Produits</th>
                    <th>Montant</th>
                    <th>Reste</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                @forelse($dettes as $dette)
                    <tr>
                        <td>{{ 'DET' . sprintf('%04d', $loop->iteration) }}</td>
                        <td>{{ $dette->client->nom ?? 'Client supprimé' }}</td>
                        <td>
                            @if(!empty($dette->produits) && is_array($dette->produits))
                                {{ implode(', ', $dette->produits) }}
                            @else
                                <span class="text-muted">Aucun produit</span>
                            @endif
                        </td>
                        <td>{{ number_format($dette->montant, 2) }}</td>
                        <td>{{ number_format($dette->reste ?? $dette->montant, 2) }}</td>
                        <td>{{ \Carbon\Carbon::parse($dette->date)->format('d-m-Y') }}</td>
                        <td>
                            @if(isset($dette->status) && $dette->status == 'payée')
                                <span class="badge bg-success">payée</span>
                            @elseif(isset($dette->status) && $dette->status == 'partielle')
                                <span class="badge bg-secondary">Partielle</span>
                            @else
                                <span class="badge bg-dark">Non payée</span>
                            @endif
                        </td>
                        <td>
                            <div class="btn-action">
                                <a href="{{ route('app_dettes.edit', $dette) }}" class="btn-modifier p-2 badge bg-primary" onclick="return confirm('Voulez-vous modifier la dette de {{ $dette->client->nom }}')">Modifier</a>
                                <form action="{{ route('app_dettes.destroy', $dette) }}" method="POST" class="d-inline">
                                    @csrf
                                    @method('DELETE')
                                    <button type="submit" class="btn-supprimer p-2 badge bg-danger" onclick="return confirm('Supprimer cette dette ?')">
                                        Supprimer
                                    </button>
                                </form>
                            </div>
                        </td>

                    </tr>
                @empty
                    <tr>
                        <td colspan="8" class="no-dettes">Aucune dette enregistrée.</td>
                    </tr>
                @endforelse
            </tbody>
        </table>
    </div>
</div>
@endsection
