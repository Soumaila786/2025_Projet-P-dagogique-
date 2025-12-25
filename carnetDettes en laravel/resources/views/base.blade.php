<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        {{-- Ici on definit le titre de notre suivie du trait de 6 suivie le nom de la page courant --}}
        <title>  {{ config('app.name') }}-@yield('titre') </title>

        <!-- Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="stylesheet" href="{{ asset('assets/app.css') }}">
    </head>
    <body>

        {{-- Notre barre de navigation  --}}
        @include('BarreNavigation.navbar')

        {{-- Contenu principal --}}
        <main class="flex-grow-1">
            @yield('contenue')
        </main>

        {{-- Nos scripts   --}}
        @include('script')

        {{-- Notre footer  --}}
        @include('Footer.footer')
        
    </body>
</html>
