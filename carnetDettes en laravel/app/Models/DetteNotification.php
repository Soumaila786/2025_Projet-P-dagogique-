<?php

namespace App\Notifications;

use Illuminate\Bus\Queueable;
use Illuminate\Notifications\Notification;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Notifications\Messages\MailMessage;
use App\Models\Dette;

class DetteNotification extends Notification
{
    use Queueable;

    protected $dette;
    protected $type;

    public function __construct(Dette $dette, $type)
    {
        $this->dette = $dette;
        $this->type = $type; // 'paiement', 'proche_echeance', 'soldée'
    }

    public function via($notifiable)
    {
        return ['database', 'mail']; // database pour notifications internes, mail pour email
    }

    public function toMail($notifiable)
    {
        $message = (new MailMessage)->subject('Notification Dette');

        if ($this->type == 'paiement') {
            $message->line("Un paiement de {$this->dette->montant} FCFA a été effectué pour la dette {$this->dette->reference}.");
        } elseif ($this->type == 'soldée') {
            $message->line("La dette {$this->dette->reference} est maintenant soldée.");
        } elseif ($this->type == 'proche_echeance') {
            $message->line("Attention : la dette {$this->dette->reference} arrive bientôt à échéance !");
        }

        return $message->action('Voir les dettes', url('/CarnetDettes/Dettes'));
    }

    public function toDatabase($notifiable)
    {
        return [
            'dette_id' => $this->dette->id,
            'reference' => $this->dette->reference,
            'type' => $this->type,
            'message' => "Notification concernant la dette {$this->dette->reference}."
        ];
    }
}
