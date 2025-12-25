@extends('base')

@section('titre', "Tableau de bord")

@section('contenue')
<div class="card text-black bg-light mb-3 shadow d-flex flex-row align-items-center p-4">
    <h1 class="text-center mb-5 mt-5 flex-grow-1">Tableau de bord</h1>
    <a href="{{ route('app_dettes.create') }}" class="btn-ajouter bg-success ms-auto mt-5 p-2">
        <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" fill="currentColor" class="bi bi-plus" viewBox="0 0 16 16">
            <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4"/>
        </svg>
        Ajouter une Dette
    </a>
</div>


<div class="container-fluid mt-4">
    <!-- Ligne 1 : 4 cartes -->
    <div class="row">
        <div class="col-md-3">
            <div class="card text-black bg-light mb-3 shadow d-flex flex-row align-items-center p-3">
                <i class="fas fa-users card-icon"></i>
                <div>
                    <h5 class="card-title">Clients</h5>
                    <p class="card-text display-6">{{ $totalClients }}</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-black bg-light mb-3 shadow d-flex flex-row align-items-center p-3">
                <i class="fas fa-file-invoice-dollar card-icon"></i>
                <div>
                    <h5 class="card-title">Total dettes</h5>
                    <p class="card-text display-6">{{ number_format($totalDettes,0,',',' ') }} FCFA</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-black bg-light mb-3 shadow d-flex flex-row align-items-center p-3">
                <i class="fas fa-hand-holding-usd card-icon"></i>
                <div>
                    <h5 class="card-title">Total paiements</h5>
                    <p class="card-text display-6">{{ number_format($totalPaiements,0,',',' ') }} FCFA</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-black bg-light mb-3 shadow d-flex flex-row align-items-center p-3">
                <i class="fas fa-wallet card-icon"></i>
                <div>
                    <h5 class="card-title">Total non payée</h5>
                    <p class="card-text display-6">{{ number_format($soldeGlobal,0,',',' ') }} FCFA</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Ligne 2 : Graphiques -->
    <div class="row mt-4">
        <div class="col-md-6">
            <div class="card shadow mb-3">
                <div class="card-header bg-light">
                    <h5 class="mb-0">Évolution des paiements</h5>
                </div>
                <div class="card-body">
                    <canvas id="chartPaiements"></canvas>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card shadow mb-3">
                <div class="card-header bg-light">
                    <h5 class="mb-0">Top clients endettés</h5>
                </div>
                <div class="card-body">
                    <canvas id="chartClients"></canvas>
                </div>
            </div>
        </div>
    </div>

    <!-- Ligne 3 : Dettes récentes -->
    <div class="row mt-4">
        <div class="col-12">
            <div class="card shadow">
                <div class="card-header bg-light">
                    <h5 class="mb-0">Dettes récentes</h5>
                </div>
                <div class="card-body">
                    <table class="paiements-table table table-striped">
                        <thead>
                            <tr>
                                <th>Réf Dette</th>
                                <th>Nom Client</th>
                                <th>Montant</th>
                                <th>Reste</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            @forelse($recentDettes as $dette)
                                <tr>
                                    <td>{{ 'DET' . sprintf('%04d', $loop->iteration) }}</td>
                                    <td>{{ $dette->client->nom }}</td>
                                    <td>{{ number_format($dette->montant,0,',',' ') }}</td>
                                    <td>{{ number_format($dette->reste,0,',',' ') }}</td>
                                    <td>{{ $dette->date->format('d/m/Y') }}</td>
                                </tr>
                            @empty
                                <tr>
                                    <td colspan="5" class="text-center">Aucune dette récente</td>
                                </tr>
                            @endforelse
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

</div>

<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<!-- Passer les données PHP à JS -->
<script>
window.dashboardData = {
    paiementsLabels: {!! json_encode($paiementsLabels) !!},
    paiementsData: {!! json_encode($paiementsData) !!},
    topClientsLabels: {!! json_encode($topClientsLabels) !!},
    topClientsData: {!! json_encode($topClientsData) !!}
};
</script>

<!-- JS du dashboard -->
<script src="{{ asset('/public/assets/main/TableauBord/tableaubord.js') }}"></script>
@endsection
