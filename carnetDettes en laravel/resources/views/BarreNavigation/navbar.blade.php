<nav class="navbar navbar-expand-lg bg-dark">
    <div class="container d-flex align-items-center justify-content-between">

        {{-- Logo + Nom --}}
        <a class="navbar-brand d-flex align-items-center" href="{{ route('app_accueil') }}">
            <img src="{{ asset('assets/images/logo.avif') }}" alt="Logo" class="logo me-2">
            <span class="app-name">{{ config('app.name') }}</span>
        </a>

        {{-- Menu (centré) --}}
        <div class="collapse navbar-collapse justify-content-center" id="navbarSupportedContent">
            @auth
                <ul class="navbar-nav mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link @if(Request::route()->getName() == 'app_tableauDeBord') active @endif"
                            href="{{ route('app_tableauDeBord') }}">Tableau de bord</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link @if(Request::route()->getName() == 'app_clients.index') active @endif"
                            href="{{ route('app_clients.index') }}">Clients</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link @if(Request::route()->getName() == 'app_dettes') active @endif"
                            href="{{ route('app_dettes.index') }}">Dettes</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link @if(Request::route()->getName() == 'app_paiements_index') active @endif"
                            href="{{ route('app_paiements_index') }}">Paiements</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="notificationsDropdown" role="button" data-bs-toggle="dropdown">
                            Notifications <span class="badge bg-danger">{{ auth()->user()->unreadNotifications->count() }}</span>
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="notificationsDropdown">
                            @foreach(auth()->user()->unreadNotifications as $notification)
                                <li>
                                    <a class="dropdown-item" href="{{ url('/CarnetDettes/Dettes') }}">
                                        {{ $notification->data['message'] }}
                                    </a>
                                </li>
                            @endforeach
                        </ul>
                    </li>

                </ul>
            @endauth
        </div>

        {{-- Profil + Déconnexion (droite) --}}
        @auth
            <div class="btnProfil">
                <span class="nav-link-profil text-white me-4">
                    <svg xmlns="http://www.w3.org/2000/svg" width="35" height="35" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
                        <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
                        <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
                    </svg>
                    {{ Auth::user()->email }}</span>
                <form method="POST" action="{{ route('app_logout') }}">
                    @csrf
                    <button class=" p-2 badge bg-danger" type="submit">
                        Déconnexion</button>
                </form>
            </div>
        @endauth

        {{-- Si non connecté --}}
        @guest
            <div class="d-flex align-items-center">
                <a class="nav-link text-white me-4" href="{{ route('login') }}">Connexion</a>
                <a class="nav-link text-white" href="{{ route('register') }}">Inscription</a>
            </div>
        @endguest

    </div>
</nav>
