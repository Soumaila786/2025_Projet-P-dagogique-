@extends('base')
@section('titre',"Connexion")

@section('contenue')
<div class="login-container">

    <div class="login-form">
        <h1>Connexion</h1>
        <p class="sub-text">Veuillez entrer vos identifiants pour accéder à votre compte.</p>

        @if ($errors->any())
            <div class="alert">
                <ul>
                    @foreach ($errors->all() as $error)
                        <li>{{ $error }}</li>
                    @endforeach
                </ul>
            </div>
        @endif

        <form method="POST" action="{{ route('login') }}">
            @csrf
            <input type="email" name="email" id="email" value="{{ old('email') }}" required placeholder="Votre adresse email">
            <label ></label>
            <input type="password" name="password" id="password" required  placeholder="Mot de passe ">

            <div class="options">
                <label class="checkbox-label">
                    <input type="checkbox" name="souvenirdemoi" {{ old('souvenirdemoi') ? 'checked' : '' }}>
                    Se souvenir de moi
                </label>
                <a href="#" class="forgot-link">Mot de passe oublié ?</a>
            </div>

            <button type="submit">Se Connecter</button>

            <p class="register-text">Vous n’avez pas de compte ? <a href="{{ route('register') }}">Créez votre compte</a></p>
        </form>
    </div>
</div>
@endsection
