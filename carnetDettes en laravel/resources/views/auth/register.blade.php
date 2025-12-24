@extends('base')
@section('titre',"Inscription")

@section('contenue')
<div class="container">
    <div class="row">
        <div class="col-md-5 mx-auto">
            <h1 class="text-center mb-1 mt-2">Page d'inscription</h1>
            <p class="text-center text-muted mb-3">Créez votre compte si vous n'en avez pas.</p>

            <form method="POST" action="{{ route('register') }}" class="row g-3" id="form-inscription">
                @csrf

                <div class="col-md-6 mt-4">
                    <label for="nom" class="form-label">Nom</label>
                    <input type="text" name="nom" id="nom" class="form-control" value="{{ old('nom') }}" required autocomplete="nom" autofocus>
                    <small class="text-danger" id="erreur-register-nom"></small>
                </div>

                <div class="col-md-6 mt-4">
                    <label for="prenom" class="form-label">Prénom</label>
                    <input type="text" name="prenom" id="prenom" class="form-control" value="{{ old('prenom') }}" required autocomplete="prenom">
                </div>

                <div class="col-md-12 mt-2">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" name="email" id="email" class="form-control" value="{{ old('email') }}" required autocomplete="email"">
                </div>

                <div class="col-md-6 mt-2">
                    <label for="password" class="form-label">Mot de passe</label>
                    <input type="password" name="password" id="password" class="form-control"value="{{ old('password') }}" required autocomplete="password" onfocus="">
                </div>

                <div class="col-md-6 mt-2 mb-4">
                    <label for="confirm_password" class="form-label">Confirmez le mot de passe</label>
                    <input type="password" name="confirm_password" id="confirm_password" class="form-control" value="{{ old('confirm_password') }}" required autocomplete="confirm_password" onfocus="">
                </div>

                <div class="d-grid gap-2 mt-2">
                    <button class="btn btn-primary" type="submit" id="btn-inscrire">S'inscrire</button>
                </div>
                <p class="text-center text-muted mt-2">Vous avez un compte ? <a href="{{ route('login') }}">Connectez-vous</a></p>
            </form>
        </div>
    </div>
</div>
@endsection
