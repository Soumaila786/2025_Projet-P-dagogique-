@extends('base')
@section('titre', 'Modifier une Dette')

@section('contenue')
    <div class="form-container">
        <div class="form-box">
            <h2>Modifier une Dette</h2>
            

            @if ($errors->any())
            <div class="alert alert-danger">
                <ul>
                    @foreach($errors->all() as $error)
                    <li>{{ $error }}</li>
                    @endforeach
                </ul>
            </div>
            @endif

            <form action="{{ route('app_dettes.update', $dette) }}" method="POST">
                @csrf
                @method('PUT')

                <!-- Client -->
                <div class=" d-flex align-items-center">
                    <select name="client_id" id="client_id" class="form-input me-2" required>
                        <option value="">-- Choisir un client --</option>
                        @foreach($clients as $client)
                            <option value="{{ $client->id }}" {{ $dette->client_id == $client->id ? 'selected' : '' }}>
                                {{ $client->nom }}
                            </option>
                        @endforeach
                    </select>
                </div>

                <!-- Produits -->
                <div>
                    <input type="text" name="produits" class="form-input" value="{{ implode(',', (array)$dette->produits) }}" required>
                        <small>Saisissez tous les produits séparés par des virgules.</small>
                </div>

                <!-- Montant -->
                <div>
                    <label for="montant" class="form-label">Montant total</label>
                    <input type="number" name="montant" class="form-input" value="{{ $dette->montant }}" required>
                </div>

                <!-- Date -->
                <div>
                    <label for="date" class="form-label">Date</label>
                    <input type="date" name="date" class="form-input"
                        value="{{ $dette->date->format('Y-m-d') }}" required>
                </div>
                <hr>

                <!-- Status -->
                <button type="submit" class="form-button" onclick="return confirm('Confirmez cette mise a jour?')">Mettre à jour la dette</button>
                <div>

                    <a href="{{ route('app_dettes.index') }}" class="btn-retour badge bg-success">← Retour à la liste</a>
                </div>

            </form>
        </div>
    </div>
@endsection
