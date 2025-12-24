@extends('base')
@section('titre',"Gestions des paiements")

@section('contenue')
<div class="paiement-container">
    <div class="paiement-container">
        <div class="card text-black bg-light mb-3 shadow d-flex flex-row align-items-center ">
            <h1 class="text-center mb-5 mt-5 flex-grow-1">Gestion des Paiements</h1>
    </div>


    <!-- Bouton Ajouter une dette -->
    <div class="action">
        <a href="{{ route('app_paiements.historiques') }}" class="btn-ajouter bg-success ">
            <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-eye" viewBox="0 0 16 16">
            <path d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8M1.173 8a13 13 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5s3.879 1.168 5.168 2.457A13 13 0 0 1 14.828 8q-.086.13-.195.288c-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5s-3.879-1.168-5.168-2.457A13 13 0 0 1 1.172 8z"/>
            <path d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5M4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0"/>
            </svg>
            Voir l'historiques des paiements </a>

        <!-- Recherche (optionnelle) -->
        <form class="search-form" method="GET" action="">
            <input type="search" name="search" placeholder="Rechercher une dette">
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

    <!-- Tableau des paiement -->
    <div class="table-responsive">
        <table class="paiement-table">
            <thead>
                <tr>
                    <th>Réf Dette</th>
                    <th>Nom Client</th>
                    <th>Montant</th>
                    <th>Reste</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Paiement</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                @forelse($dettes as $dette)
                    <tr>
                        <td>{{ 'DET' . sprintf('%04d', $loop->iteration) }}</td>
                        <td>{{ $dette->client->nom ?? 'Client supprimé' }}</td>
                        <td>{{ number_format($dette->montant, 2) }} FCFA</td>
                        <td>{{ number_format($dette->reste ?? $dette->montant, 2) }} FCFA</td>
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
                            @if($dette->reste > 0)
                                <form action="{{ route('app_paiements.payer', $dette) }}" method="POST" class="d-inline d-flex align-items-center">
                                    @csrf
                                    <input type="number" name="montant_paye" value="0" max="{{ $dette->reste }}" class="form-control form-control-sm me-1" required>
                                    <button type="submit" class="p-2 badge bg-success btn-sm">Payer</button>
                                </form>
                            @endif
                        </td>
                        <td>
                            <div class="btn-action">
                                <a href="{{ route('app_dettes.edit', $dette) }}" class=" p-2 btn-modifier badge bg-primary">Modifier</a>
                                <form action="{{ route('app_dettes.destroy', $dette) }}" method="POST" class="d-inline">
                                    @csrf
                                    @method('DELETE')
                                    <button type="submit" class="p-2 badge bg-danger" onclick="return confirm('Supprimer cette dette ?')">Supprimer</button>
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

@endsection
