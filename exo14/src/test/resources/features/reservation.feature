# language: fr
Fonctionnalité: Gestion des réservations d'ouvrages

  Scénario: Réservation d'un ouvrage indisponible
    Étant donné un ouvrage "1984" qui est actuellement emprunté
    Et un adhérent "Alice"
    Quand "Alice" demande à réserver "1984"
    Alors la réservation est acceptée
    Et "Alice" est ajoutée à la file d'attente pour "1984"

  Scénario: Plusieurs réservations sur le même ouvrage
    Étant donné un ouvrage "Dune" qui est actuellement emprunté
    Et un adhérent "Bob" a déjà réservé "Dune"
    Quand un adhérent "Charlie" demande à réserver "Dune"
    Alors la réservation est acceptée
    Et "Charlie" est placé en position 2 dans la file d'attente

  Scénario: Restitution d'un ouvrage réservé
    Étant donné un ouvrage "Fondation" qui vient d'être retourné
    Et que "Alice" est première dans la file d'attente pour "Fondation"
    Quand le retour de l'ouvrage est enregistré
    Alors l'ouvrage est mis de côté pour "Alice"
    Et "Alice" est notifiée de la disponibilité

  Scénario: Refus d'une réservation pour un adhérent suspendu
    Étant donné un adhérent "David" qui est suspendu
    Et un ouvrage "Le Hobbit" qui est emprunté
    Quand "David" demande à réserver "Le Hobbit"
    Alors la réservation est refusée avec le motif "Adhérent suspendu"

  # SCÉNARIO SUPPLÉMENTAIRE VALIDANT UNE RÈGLE MÉTIER
  Scénario: Annulation d'une réservation par l'adhérent
    Étant donné un adhérent "Emma" qui a réservé "L'Étranger"
    Et "Emma" est en position 1 dans la file d'attente
    Quand "Emma" annule sa réservation
    Alors "Emma" est retirée de la file d'attente pour "L'Étranger"