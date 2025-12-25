@extends('base')
@section('titre',"Gestions des paiements")

@section('contenue')
    <div class="paiement-container">
        <div class="card text-black bg-light mb-2 shadow d-flex flex-row align-items-center ">
            <h1 class="text-center mb-5 mt-5 flex-grow-1">Historiques des paiements effectués</h1>
    </div>


        <div class="action">
            <a href="{{ route('app_paiements_index') }}" class="btn-retour badge bg-success">← Retour à la liste</a>
            <form class="search-form" method="GET" action="">
                <input type="search" name="search" placeholder="Rechercher un paiement">
                <button type="submit">Recherche</button>
            </form>
        </div>
        <hr>
        @if(session('success'))
            <div class="alert-success" role="alert">
                {{ session('success') }}
            </div>
        @endif

        <div class="table-responsive">
            <table class="paiement-table">
                <thead>
                    <tr>
                        <th>Réf Dette</th>
                        <th>Nom Client</th>
                        <th>Montant Payé</th>
                        <th>Reste</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    @forelse($paiements as $paiement)
                        <tr>
                            <td>{{ 'PYMT' . sprintf('%04d', $loop->iteration) }}</td>
                            <td>{{ $paiement->dette->client->nom ?? 'Client inconnu ou supprimé' }}</td>
                            <td>{{ number_format($paiement->montant, 0, ',', ' ') }} FCFA</td>
                            <td>{{ number_format($paiement->dette->reste ?? 0, 0, ',', ' ') }} FCFA</td>
                            <td>{{ $paiement->date }}</td>
                            <td>
                                <form action="{{ route('app_paiements.annuler', $paiement) }}" method="POST" class="d-inline">
                                    @csrf
                                    @method('PUT')
                                    <button type="submit" class="p-2 badge bg-dark" onclick="return confirm('Voulez-vous vraiment annuler ce paiement ?')">
                                        Annuler le paiement
                                    </button>
                                </form>

                                <form action="{{ route('app_paiements.destroy', $paiement) }}" method="POST" class="d-inline">
                                    @csrf
                                    @method('DELETE')
                                    <button type="submit" class="p-2 badge bg-danger" onclick="return confirm('Supprimer ce paiement ?')">Supprimer</button>
                                </form>
                            </td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="6" class="no-paiement">Aucun paiement enregistré.</td>
                        </tr>
                    @endforelse
                </tbody>
            </table>
        </div>
    </div>
@endsection
