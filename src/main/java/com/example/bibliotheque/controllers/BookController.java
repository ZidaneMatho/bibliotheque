package com.example.bibliotheque.controllers;

import com.example.bibliotheque.models.Book;
import com.example.bibliotheque.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des livres.
 * Toutes les routes commencent par /api/books
 */
@RestController
@RequestMapping("/api/books")
@Tag(name = "Livres", description = "API pour la gestion du catalogue de livres")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * Récupère tous les livres.
     * GET /api/books
     * @return liste de tous les livres
     */
    @Operation(summary = "Liste tous les livres",
            description = "Retourne la liste complète de tous les livres du catalogue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Récupère un livre par son ID.
     * GET /api/books/{id}
     * @param id l'identifiant du livre
     * @return le livre trouvé
     */
    @Operation(summary = "Recherche un livre par son ID",
            description = "Retourne un livre spécifique à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livre trouvé"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "ID du livre à rechercher", required = true)
            @PathVariable Long id) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'id: " + id));
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    /**
     * Récupère un livre par son ISBN.
     * GET /api/books/isbn/{isbn}
     * @param isbn l'ISBN du livre
     * @return le livre trouvé
     */
    @Operation(summary = "Recherche un livre par son ISBN",
            description = "Retourne un livre spécifique à partir de son code ISBN unique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livre trouvé"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    })
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(
            @Parameter(description = "ISBN du livre à rechercher", required = true, example = "9782070612758")
            @PathVariable String isbn) {
        Book book = bookService.getBookByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'ISBN: " + isbn));
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    /**
     * Recherche des livres par titre (recherche partielle).
     * GET /api/books/search/title?title=...
     * @param title le titre ou partie du titre à rechercher
     * @return liste des livres correspondants
     */
    @Operation(summary = "Recherche des livres par titre",
            description = "Recherche partielle et insensible à la casse par titre de livre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recherche effectuée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchBooksByTitle(
            @Parameter(description = "Titre ou partie du titre à rechercher", required = true, example = "prince")
            @RequestParam String title) {
        List<Book> books = bookService.getBooksByTitle(title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Recherche des livres par auteur (recherche partielle).
     * GET /api/books/search/author?author=...
     * @param author le nom ou partie du nom de l'auteur
     * @return liste des livres correspondants
     */
    @Operation(summary = "Recherche des livres par auteur",
            description = "Recherche partielle et insensible à la casse par nom d'auteur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recherche effectuée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> searchBooksByAuthor(
            @Parameter(description = "Nom ou partie du nom de l'auteur", required = true, example = "saint")
            @RequestParam String author) {
        List<Book> books = bookService.getBooksByAuthor(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Récupère les livres actuellement disponibles.
     * GET /api/books/available
     * @return liste des livres avec au moins un exemplaire disponible
     */
    @Operation(summary = "Liste les livres disponibles",
            description = "Retourne tous les livres ayant au moins un exemplaire disponible à l'emprunt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        List<Book> books = bookService.getAvailableBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Crée un nouveau livre.
     * POST /api/books
     * @param book les informations du livre à créer
     * @return le livre créé
     */
    @Operation(summary = "Crée un nouveau livre",
            description = "Ajoute un nouveau livre dans le catalogue (réservé aux administrateurs)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livre créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès interdit (réservé aux administrateurs)"),
            @ApiResponse(responseCode = "409", description = "ISBN déjà existant")
    })
    @PostMapping
    public ResponseEntity<Book> createBook(
            @Parameter(description = "Données du livre à créer", required = true)
            @RequestBody Book book) {
        // Vérifier si l'ISBN existe déjà
        if (book.getIsbn() != null && bookService.getBookByIsbn(book.getIsbn()).isPresent()) {
            throw new RuntimeException("Un livre avec l'ISBN '" + book.getIsbn() + "' existe déjà");
        }
        Book newBook = bookService.createBook(book);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }

    /**
     * Met à jour un livre existant.
     * PUT /api/books/{id}
     * @param id l'identifiant du livre à modifier
     * @param book les nouvelles informations
     * @return le livre mis à jour
     */
    @Operation(summary = "Met à jour un livre",
            description = "Modifie les informations d'un livre existant (réservé aux administrateurs)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livre mis à jour"),
            @ApiResponse(responseCode = "403", description = "Accès interdit (réservé aux administrateurs)"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé"),
            @ApiResponse(responseCode = "409", description = "ISBN déjà utilisé par un autre livre")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "ID du livre à modifier", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données du livre", required = true)
            @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    /**
     * Supprime un livre.
     * DELETE /api/books/{id}
     * @param id l'identifiant du livre à supprimer
     * @return réponse vide avec statut NO_CONTENT
     */
    @Operation(summary = "Supprime un livre",
            description = "Supprime définitivement un livre du catalogue (réservé aux administrateurs)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livre supprimé avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès interdit (réservé aux administrateurs)"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé"),
            @ApiResponse(responseCode = "409", description = "Livre actuellement emprunté, suppression impossible")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID du livre à supprimer", required = true)
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Vérifie si un livre avec l'ISBN donné existe.
     * GET /api/books/exists/isbn/{isbn}
     * @param isbn l'ISBN à vérifier
     * @return true si l'ISBN existe, false sinon
     */
    @Operation(summary = "Vérifie si un ISBN existe",
            description = "Permet de savoir si un code ISBN est déjà utilisé dans le catalogue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vérification effectuée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/exists/isbn/{isbn}")
    public ResponseEntity<Boolean> checkIsbnExists(
            @Parameter(description = "ISBN à vérifier", required = true, example = "9782070612758")
            @PathVariable String isbn) {
        boolean exists = bookService.getBookByIsbn(isbn).isPresent();
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}