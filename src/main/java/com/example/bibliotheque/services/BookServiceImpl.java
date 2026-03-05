package com.example.bibliotheque.services;

import com.example.bibliotheque.models.Book;
import com.example.bibliotheque.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation des services de gestion des livres.
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book createBook(Book book) {
        // Initialise la date d'ajout
        book.setAddedDate(LocalDateTime.now());
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return List.of(); // Retourne liste vide si titre vide
        }
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return List.of();
        }
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    @Override
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0);
    }

    @Override
    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'id: " + id));

        // Met à jour les champs modifiables
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublisher(bookDetails.getPublisher());
        book.setPublicationDate(bookDetails.getPublicationDate());
        book.setDescription(bookDetails.getDescription());
        book.setTotalCopies(bookDetails.getTotalCopies());
        book.setAvailableCopies(bookDetails.getAvailableCopies());

        // Note: addedDate ne change pas
        // Note: categories et borrows sont gérés séparément

        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Livre non trouvé avec l'id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).isPresent();
    }
}