package com.example.bdd.steps;

import io.cucumber.java.Before;
import io.cucumber.java.fr.Étantdonné;
import io.cucumber.java.fr.Quand;
import io.cucumber.java.fr.Alors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

public class ShopStepDefinitions {

    // --- Interfaces Mockées (Repositories) ---
    interface UserRepository {
        boolean existsByUsername(String username);
        boolean checkCredentials(String username, String password);
    }
    
    interface ProductRepository {
        List<String> search(String keyword, double maxPrice);
        List<String> findByCategory(String category);
    }
    
    interface OrderRepository {
        boolean orderExists(String orderId);
    }

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    // --- Variables d'état du scénario ---
    private String currentUsername;
    private String currentEmail;
    private String currentPassword;
    private String executionError;
    private String activeOrderId;
    private Map<String, Integer> currentOrderCart;
    private List<String> searchResults;

    @Before
    public void setup() {
        // Initialisation fraîche des Mocks avant chaque Scénario
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        
        executionError = null;
        activeOrderId = null;
        currentOrderCart = new HashMap<>();
        searchResults = new ArrayList<>();
    }

    // =========================================================================
    // ETAPES : US 1 - Création de compte
    // =========================================================================

    @Étantdonné("l'utilisateur est sur le formulaire d'inscription")
    public void l_utilisateur_est_sur_le_formulaire_d_inscription() {
        // Initialisation de la vue d'inscription
    }

    @Étantdonné("qu'un compte existe déjà avec l'identifiant {string}")
    public void qu_un compte_existe_deja_avec_l_identifiant(String username) {
        when(userRepository.existsByUsername(username)).thenReturn(true);
    }

    @Quand("il saisit un nom d'utilisateur {string}, un email {string} et un mot de passe {string}")
    public void il_saisit_un_nom_d_utilisateur_un_email_et_un_mot_de_passe(String username, String email, String password) {
        this.currentUsername = username;
        
        if (userRepository.existsByUsername(username)) {
            this.executionError = "Identifiant déjà existant";
        } else {
            this.currentEmail = email;
            this.currentPassword = password;
        }
    }

    @Alors("le compte est créé avec succès et un message de confirmation s'affiche")
    public void le_compte_est_cree_avec_succes_et_un_message_de_confirmation_s_affiche() {
        assertNull(executionError);
        assertNotNull(currentUsername);
    }

    @Alors("une erreur est renvoyée indiquant que l'identifiant existe déjà")
    public void une_erreur_est_renvoyee_indiquant_que_l_identifiant_existe_deja() {
        assertEquals("Identifiant déjà existant", executionError);
    }

    // =========================================================================
    // ETAPES : US 2 - Connexion
    // =========================================================================

    @Étantdonné("l'utilisateur est sur le formulaire de connexion")
    public void l_utilisateur_est_sur_le_formulaire_de_connexion() {
        when(userRepository.checkCredentials("jean_dupont", "Securise123!")).thenReturn(true);
        when(userRepository.checkCredentials("jean_dupont", "WrongPass")).thenReturn(false);
    }

    @Quand("il saisit son identifiant {string} et son mot de passe {string}")
    public void il_saisit_son_identifiant_et_son_mot_de_passe(String username, String password) {
        boolean success = userRepository.checkCredentials(username, password);
        if (!success) {
            executionError = "Erreur de connexion";
        }
    }

    @Alors("il est redirigé vers la page d'accueil de la boutique")
    public void il_est_redirige_vers_la_page_d_accueil_de_la_boutique() {
        assertNull(executionError);
    }

    @Alors("un message d'erreur de connexion s'affiche")
    public void un_message_d_erreur_de_connexion_s_affiche() {
        assertEquals("Erreur de connexion", executionError);
    }

    // =========================================================================
    // ETAPES : US 3 - Recherche de produits
    // =========================================================================

    @Étantdonné("la boutique possède des produits")
    public void la_boutique_possede_des_produits() {
        when(productRepository.search("Ordinateur", 1000.0))
            .thenReturn(List.of("Ordinateur Portable"));
    }

    @Quand("l'utilisateur recherche le mot-clé {string} avec un prix maximum de {int} euros")
    public void l_utilisateur_recherche_le_mot_cle_avec_un_prix_maximum_de_euros(String keyword, Integer maxPrice) {
        searchResults = productRepository.search(keyword, maxPrice);
    }

    @Alors("une liste de résultats pertinents contenant {string} s'affiche")
    public void une_liste_de_resultats_pertinents_contenant_s_affiche(String expectedProduct) {
        assertTrue(searchResults.contains(expectedProduct));
    }

    // =========================================================================
    // ETAPES : US 4 - Navigation par catégorie
    // =========================================================================

    @Étantdonné("la boutique possède des produits classés par catégories")
    public void la_boutique_possede_des_produits_classes_par_categories() {
        when(productRepository.findByCategory("Livres")).thenReturn(List.of("Livre Java", "Livre BDD"));
    }

    @Quand("l'utilisateur sélectionne la catégorie {string}")
    public void l_utilisateur_selectionne_la_categorie(String category) {
        searchResults = productRepository.findByCategory(category);
    }

    @Alors("l'application retourne les produits correspondants à la catégorie {string}")
    public void l_application_retourne_les_produits_correspondants_a_la_categorie(String category) {
        assertFalse(searchResults.isEmpty());
    }

    // =========================================================================
    // ETAPES : US 5 - Ajout de produit à une commande
    // =========================================================================

    @Étantdonné("qu'une commande existante est active")
    public void qu_une_commande_existante_est_active() {
        activeOrderId = "CMD-123";
        when(orderRepository.orderExists(activeOrderId)).thenReturn(true);
    }

    @Étantdonné("qu'une commande active contient déjà {int} exemplaire du produit {string}")
    public void qu_une_commande_active_contient_deja_exemplaire_du_produit(Integer qty, String product) {
        activeOrderId = "CMD-123";
        when(orderRepository.orderExists(activeOrderId)).thenReturn(true);
        currentOrderCart.put(product, qty);
    }

    @Étantdonné("qu'aucune commande n'est active")
    public void qu_aucune_commande_n_est_active() {
        activeOrderId = null;
    }

    @Quand("l'utilisateur clique sur {string} pour le produit {string}")
    @Quand("l'utilisateur tente d'ajouter le produit {string} à la commande")
    public void l_utilisateur_clique_sur_pour_le_produit(String product) {
        if (activeOrderId == null || !orderRepository.orderExists(activeOrderId)) {
            executionError = "La commande n'existe pas";
            return;
        }
        currentOrderCart.put(product, currentOrderCart.getOrDefault(product, 0) + 1);
    }

    @Alors("une confirmation s'affiche et la quantité du produit passe à {int}")
    @Alors("la quantité de ce produit dans la commande passe à {int}")
    public void la_quantite_de_ce_produit_dans_la_commande_passe_a(Integer expectedQty) {
        assertEquals(expectedQty, currentOrderCart.get("Livre Java"));
    }

    @Alors("une erreur est renvoyée indiquant que la commande n'existe pas")
    public void une_erreur_est_renvoyee_indiquant_que_la_commande_n_existe_pas() {
        assertEquals("La commande n'existe pas", executionError);
    }

    // =========================================================================
    // ETAPES : US 6 - Suppression de produit d'une commande
    // =========================================================================

    @Étantdonné("en tant qu'utilisateur ayant une commande active avec {int} exemplaires du produit {string}")
    @Étantdonné("en tant qu'utilisateur ayant une commande active avec {int} exemplaire du produit {string}")
    public void ayant_une_commande_active_avec_produits(Integer qty, String product) {
        activeOrderId = "CMD-123";
        when(orderRepository.orderExists(activeOrderId)).thenReturn(true);
        currentOrderCart.put(product, qty);
    }

    @Étantdonné("en tant qu'utilisateur ayant une commande active vide")
    public void ayant_une_commande_active_vide() {
        activeOrderId = "CMD-123";
        when(orderRepository.orderExists(activeOrderId)).thenReturn(true);
    }

    @Quand("il clique sur le bouton {string} pour le produit {string}")
    public void il_clique_sur_le_bouton_supprimer(String product) {
        if (!currentOrderCart.containsKey(product)) {
            executionError = "Le produit n'est pas dans la commande";
            return;
        }
        
        int qty = currentOrderCart.get(product);
        if (qty > 1) {
            currentOrderCart.put(product, qty - 1);
        } else {
            currentOrderCart.remove(product);
        }
    }

    @Alors("la quantité du produit diminue et passe à {int}")
    public void la_quantite_du_produit_diminue_et_passe_a(Integer expectedQty) {
        assertEquals(expectedQty, currentOrderCart.get("Livre Java"));
    }

    @Alors("le produit est complètement retiré de la commande")
    public void le_produit_est_completement_retire_de_la_commande() {
        assertFalse(currentOrderCart.containsKey("Livre Java"));
    }

    @Alors("une erreur est renvoyée indiquant que le produit n'est pas dans la commande")
    public void une_erreur_est_renvoyee_indiquant_que_le_produit_n_est_pas_dans_la_commande() {
        assertEquals("Le produit n'est pas dans la commande", executionError);
    }

    // =========================================================================
    // ETAPES : US 7 - Validation de commande
    // =========================================================================

    @Étantdonné("l'utilisateur est sur le formulaire de validation avec une commande active")
    public void l_utilisateur_est_sur_le_formulaire_de_validation_avec_une_commande_active() {
        activeOrderId = "CMD-123";
        when(orderRepository.orderExists(activeOrderId)).thenReturn(true);
    }

    @Quand("il valide sa commande")
    public void il_valide_sa_commande() {
        if (activeOrderId == null) {
            executionError = "La commande n'existe pas";
        }
    }

    @Alors("il reçoit une confirmation de commande finale avec succès")
    public void il_recoit_une_confirmation_de_commande_finale_avec_succes() {
        assertNull(executionError);
    }
}