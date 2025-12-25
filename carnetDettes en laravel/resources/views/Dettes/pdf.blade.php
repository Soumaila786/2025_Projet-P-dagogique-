<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des Dettes</title>
    <style>
        body { font-family: DejaVu Sans, sans-serif; font-size: 12px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #0000008e; padding: 8px; text-align: center; }
        h2{ text-align: center; }
        th { background: #f2f2f2; }
        h1{ background-color:rgb(0, 0, 0);color:rgb(255, 255, 255);border: 1px solid #000; padding: 8px; text-align: center;border-radius: 20px}
    </style>
</head>
<body>
    <h1>Carnet-Dettes @2025</h1>
    <h2>Liste des Dettes</h2>
    <table>
        <thead>
            <tr>
                <th>Réference</th>
                <th>Client</th>
                <th>Produits</th>
                <th>Montant</th>
                <th>Date</th>
            </tr>
        </thead>
        <tbody>
            @foreach($dettes as $dette)
                <tr>
                    <td>{{ 'DET' . sprintf('%04d', $loop->iteration) }}</td>
                    <td>{{ $dette->client->nom ?? '---' }}</td>
                    <td>
                            @if(!empty($dette->produits) && is_array($dette->produits))
                                {{ implode(', ', $dette->produits) }}
                            @else
                                <span class="text-muted">Aucun produit</span>
                            @endif
                        </td>
                    <td>{{ number_format($dette->montant, 0, ',', ' ') }} FCFA</td>
                    <td>{{ $dette->created_at->format('d/m/Y') }}</td>
                </tr>
            @endforeach
        </tbody>
    </table>
</body>
</html>
