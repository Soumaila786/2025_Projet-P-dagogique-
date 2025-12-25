// Évolution des paiements
new Chart(document.getElementById('chartPaiements'), {
    type: 'line',
    data: {
        labels: window.dashboardData.paiementsLabels,
        datasets: [{
            label: 'Paiements par jour (FCFA)',
            data: window.dashboardData.paiementsData,
            borderColor: 'rgba(75, 192, 192, 1)',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            fill: true,
            tension: 0.3
        }]
    }
});

// Top clients endettés
new Chart(document.getElementById('chartClients'), {
    type: 'bar',
    data: {
        labels: window.dashboardData.topClientsLabels,
        datasets: [{
            label: 'Dettes restantes (FCFA)',
            data: window.dashboardData.topClientsData,
            backgroundColor: 'rgba(20, 12, 249, 0.77)'
        }]
    }
});
