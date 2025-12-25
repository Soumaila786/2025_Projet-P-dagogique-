<?php

namespace App\Notifications;

use Illuminate\Bus\Queueable;
use Illuminate\Notifications\Notification;
use Illuminate\Notifications\Messages\MailMessage;
use App\Models\Dette;

class RappelDetteNotification extends Notification
{
    use Queueable;

    protected $dette;

    public function __construct(Dette $dette)
    {
        $this->dette = $dette;
    }

    public function via($notifiable)
    {
        return ['database', 'mail']; // database = notification interne, mail = email
    }

    public function toMail($notifiable)
    {
        return (new MailMessage)
                    ->subject('Rappel Dette')
                    ->line("La dette {$this->dette->reference} du client {$this->dette->client->nom} arrive bientôt à échéance.")
                    ->action('Voir les dettes', url('/CarnetDettes/Dettes'));
    }

    public function toDatabase($notifiable)
    {
        return [
            'dette_id' => $this->dette->id,
            'reference' => $this->dette->reference,
            'message' => "La dette {$this->dette->reference} arrive bientôt à échéance."
        ];
    }
}
