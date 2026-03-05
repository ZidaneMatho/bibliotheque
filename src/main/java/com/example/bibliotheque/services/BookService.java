package com.example.bibliotheque.services;

import com.example.bibliotheque.models.Book;
import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des livres.
 */
public interface BookService {

    /** Crée un nouveau livre. */
    Book createBook(Book book);

    /** Retourne tous les livres. */
    List<Book> getAllBooks();

    /** Recherche un livre par son ID. */
    Optional<Book> getBookById(Long id);

    /** Recherche un livre par son ISBN. */
    Optional<Book> getBookByIsbn(String isbn);

    /** Recherche des livres par titre (partiel, insensible à la casse). */
    List<Book> getBooksByTitle(String title);

    /** Recherche des livres par auteur (partiel, insensible à la casse). */
    List<Book> getBooksByAuthor(String author);

    /** Retourne les livres disponibles (avec au moins un exemplaire). */
    List<Book> getAvailableBooks();

    /** Met à jour un livre existant. */
    Book updateBook(Long id, Book bookDetails);

    /** Supprime un livre. */
    void deleteBook(Long id);

    /** Vérifie si un ISBN existe déjà. */
    boolean existsByIsbn(String isbn);
}