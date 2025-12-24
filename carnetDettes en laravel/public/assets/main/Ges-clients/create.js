
$('#btn-ajouter').click(function () {
    const nom = $('#name').val();
    const numero = $('#numTel').val();
    const adresse = $('#adresse').val();

    // Validation du nom
    if (nom !== "" && /^[a-zA-Z À-ÖØ-öø-ÿ]+$/.test(nom)) {
        $('#nom').removeClass('is-invalid').addClass('is-valid');
        $('#erreur-register-nom').text('');
        // Validation du prénom
        if (numero !== "".test(numero)) {
            $('#numero').removeClass('is-invalid').addClass('is-valid');
            $('#erreur-register-numTel').text('');
            // Validation de l'adresse
            if (adresse !== "".test(adresse)) {
                $('#adresse').removeClass('is-invalid').addClass('is-valid');
                $('#erreur-register-adresse').text('');

                    $('#form-ajout').submit();
            
            } else {
                $('#adresse').addClass('is-invalid').removeClass('is-valid');
                $('#erreur-register-adresse').text('Adresse est vide !');
            }
        } else {
            $('#numero').addClass('is-invalid').removeClass('is-valid');
            $('#erreur-register-numTel').text('Numéro est vide !');
        }
    } else {
        $('#nom').addClass('is-invalid').removeClass('is-valid');
        $('#erreur-register-nom').text('Votre champs nom est vide !');
    }
});



