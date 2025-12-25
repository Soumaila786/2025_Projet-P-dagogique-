
$('#btn-inscrire').click(function () {
    const nom = $('#nom').val();
    const prenom = $('#prenom').val();
    let email = $('#email').val();
    const password = $('#password').val();
    const confirm_password = $('#confirm_password').val(); // 🔥 même ID partout
    const taille_password = password.length;
    let agreeTerms = $('#agreeTerms');

    // Validation du nom
    if (nom !== "" && /^[a-zA-Z À-ÖØ-öø-ÿ]+$/.test(nom)) {
        $('#nom').removeClass('is-invalid').addClass('is-valid');
        $('#erreur-register-nom').text('');

        // Validation du prénom
        if (prenom !== "" && /^[a-zA-Z À-ÖØ-öø-ÿ]+$/.test(prenom)) {
            $('#prenom').removeClass('is-invalid').addClass('is-valid');
            $('#erreur-register-prenom').text('');


            // Validation de l'email
            if (email !== "" && /^[a-z0-9._-]+@[a-z0-9.-]+\.[a-z]{2,10}$/i.test(email)) {
                $('#email').removeClass('is-invalid').addClass('is-valid');
                $('#erreur-register-email').text('');


                // Validation du mot de passe
                if (taille_password >= 8) {
                    $('#password').removeClass('is-invalid').addClass('is-valid');
                    $('#erreur-register-password').text('');


                    // Vérification confirmation mot de passe
                    if (password === confirm_password) {
                        $('#confirm_password').removeClass('is-invalid').addClass('is-valid');
                        $('#erreur-register-confirm-password').text('');


                                // L'email n'existe pas, on peut envoyer le formulaire
                                $('#form-inscription').submit() ;

                    } else {
                        $('#confirm_password').addClass('is-invalid').removeClass('is-valid');
                        $('#erreur-register-confirm-password').text('Vos mots de passe ne sont pas identiques !');
                    }


                } else {
                    $('#password').addClass('is-invalid').removeClass('is-valid');
                    $('#erreur-register-password').text('Minimum 8 caractères !');
                }


            } else {
                $('#email').addClass('is-invalid').removeClass('is-valid');
                $('#erreur-register-email').text('Email invalide !');
            }


        } else {
            $('#prenom').addClass('is-invalid').removeClass('is-valid');
            $('#erreur-register-prenom').text('Prénom invalide !');
        }


    } else {
        $('#nom').addClass('is-invalid').removeClass('is-valid');
        $('#erreur-register-nom').text('Nom invalide !');
    }
});



