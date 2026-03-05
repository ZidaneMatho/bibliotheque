package com.example.bibliotheque.controllers;

import com.example.bibliotheque.models.Book;
import com.example.bibliotheque.services.BookService;
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
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * Récupère tous les livres.
     * GET /api/books
     * @return liste de tous les livres
     */
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
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
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
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
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
    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
        List<Book> books = bookService.getBooksByTitle(title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Recherche des livres par auteur (recherche partielle).
     * GET /api/books/search/author?author=...
     * @param author le nom ou partie du nom de l'auteur
     * @return liste des livres correspondants
     */
    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> searchBooksByAuthor(@RequestParam String author) {
        List<Book> books = bookService.getBooksByAuthor(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Récupère les livres actuellement disponibles.
     * GET /api/books/available
     * @return liste des livres avec au moins un exemplaire disponible
     */
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
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
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
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    /**
     * Supprime un livre.
     * DELETE /api/books/{id}
     * @param id l'identifiant du livre à supprimer
     * @return réponse vide avec statut NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Vérifie si un livre avec l'ISBN donné existe.
     * GET /api/books/exists/isbn/{isbn}
     * @param isbn l'ISBN à vérifier
     * @return true si l'ISBN existe, false sinon
     */
    @GetMapping("/exists/isbn/{isbn}")
    public ResponseEntity<Boolean> checkIsbnExists(@PathVariable String isbn) {
        boolean exists = bookService.getBookByIsbn(isbn).isPresent();
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}