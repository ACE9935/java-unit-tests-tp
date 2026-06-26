# language: fr
Fonctionnalité: Parcours client d'une boutique en ligne

  # --- US 1 : Création de compte ---
  Scénario: Création de compte réussie
    Étant donné que l'utilisateur est sur le formulaire d'inscription
    Quand il saisit un nom d'utilisateur "jean_dupont", un email "jean@example.com" et un mot de passe "Securise123!"
    Alors le compte est créé avec succès et un message de confirmation s'affiche

  Scénario: Échec de création de compte avec un identifiant déjà existant
    Étant donné que l'utilisateur est sur le formulaire d'inscription
    Et qu'un compte existe déjà avec l'identifiant "identifiant_pris"
    Quand il saisit un nom d'utilisateur "identifiant_pris", un email "autre@example.com" et un mot de passe "Securise123!"
    Alors une erreur est renvoyée indiquant que l'identifiant existe déjà

  # --- US 2 : Connexion ---
  Scénario: Connexion réussie et redirection
    Étant donné que l'utilisateur est sur le formulaire de connexion
    Quand il saisit son identifiant "jean_dupont" et son mot de passe "Securise123!"
    Alors il est redirigé vers la page d'accueil de la boutique

  Scénario: Échec de la connexion avec de mauvais identifiants
    Étant donné que l'utilisateur est sur le formulaire de connexion
    Quand il saisit son identifiant "jean_dupont" et un mauvais mot de passe "WrongPass"
    Alors un message d'erreur de connexion s'affiche

  # --- US 3 : Recherche de produits ---
  Scénario: Recherche de produits par mot-clé et prix maximum
    Étant donné que la boutique possède des produits
    Quand l'utilisateur recherche le mot-clé "Ordinateur" avec un prix maximum de 1000 euros
    Alors une liste de résultats pertinents contenant "Ordinateur Portable" s'affiche

  # --- US 4 : Navigation par catégorie ---
  Scénario: Navigation et affichage des produits d'une catégorie
    Étant donné que la boutique possède des produits classés par catégories
    Quand l'utilisateur sélectionne la catégorie "Livres"
    Alors l'application retourne les produits correspondants à la catégorie "Livres"

  # --- US 5 : Ajout de produit à une commande ---
  Scénario: Ajout réussi d'un nouveau produit à une commande existante
    Étant donné qu'une commande existante est active
    Quand l'utilisateur clique sur "Ajouter à la commande" pour le produit "Livre Java"
    Alors une confirmation s'affiche et la quantité du produit passe à 1

  Scénario: Augmentation de la quantité d'un produit déjà présent
    Étant donné qu'une commande active contient déjà 1 exemplaire du produit "Livre Java"
    Quand l'utilisateur clique sur "Ajouter à la commande" pour le produit "Livre Java"
    Alors la quantité de ce produit dans la commande passe à 2

  Scénario: Erreur lors de l'ajout si la commande n'existe pas
    Étant donné qu'aucune commande n'est active
    Quand l'utilisateur tente d'ajouter le produit "Livre Java" à la commande
    Alors une erreur est renvoyée indiquant que la commande n'existe pas

  # --- US 6 : Suppression / Diminution de produit d'une commande ---
  Scénario: Diminution de la quantité d'un produit supérieur à 1
    En tant qu'utilisateur ayant une commande active avec 2 exemplaires du produit "Livre Java"
    Quand il clique sur le bouton "Supprimer" pour le produit "Livre Java"
    Alors la quantité du produit diminue et passe à 1

  Scénario: Retrait complet du produit si la quantité est égale à 1
    En tant qu'utilisateur ayant une commande active avec 1 exemplaire du produit "Livre Java"
    Quand il clique sur le bouton "Supprimer" pour le produit "Livre Java"
    Alors le produit est complètement retiré de la commande

  Scénario: Erreur si le produit à supprimer n'est pas dans la commande
    En tant qu'utilisateur ayant une commande active vide
    Quand il clique sur le bouton "Supprimer" pour le produit "Livre Java"
    Alors une erreur est renvoyée indiquant que le produit n'est pas dans la commande

  # --- US 7 : Validation de commande ---
  Scénario: Validation réussie d'une commande
    Étant donné que l'utilisateur est sur le formulaire de validation avec une commande active
    Quand il valide sa commande
    Alors il reçoit une confirmation de commande finale avec succès