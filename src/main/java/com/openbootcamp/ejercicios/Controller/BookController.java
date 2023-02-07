package com.openbootcamp.ejercicios.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openbootcamp.ejercicios.Model.Entities.Book;
import com.openbootcamp.ejercicios.Model.Repository.BookRepository;

@RestController
@RequestMapping("/api")
public class BookController {

    private BookRepository bookRepository;
    private final Logger log = LoggerFactory.getLogger(BookController.class);

    // Constructor
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Read All
    @GetMapping("/books")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    // Read One
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> findOneById(@PathVariable Long id) {
        var response = bookRepository.findById(id);

        if (response.isPresent()) {
            log.info("Reading one book...");
            return ResponseEntity.ok(response.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create One
    @PostMapping("/books")
    public ResponseEntity<Book> create(@RequestBody Book book) {

        if (book.getId() == null) {
            log.warn("Intentando crear nuevo libro...");
            return ResponseEntity.ok(bookRepository.save(book));
        }

        return ResponseEntity.badRequest().build();
    }

    // Update One
    @PutMapping("/books/{id}")
    public ResponseEntity<Book> update(@RequestBody Book book, @PathVariable Long id) {

        if (id == null) {
            log.warn("Intentando actualizar un libro inexistente");
            return ResponseEntity.badRequest().build();
        }

        if (bookRepository.existsById(id)) {
            return ResponseEntity.ok(bookRepository.save(book));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Book> delete(@PathVariable Long id) {

        if (!bookRepository.existsById(id)) {
            log.warn("Intentando borrar un libro inexistente");
            return ResponseEntity.notFound().build();
        }

        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/books")
    public ResponseEntity<Book> deleteAll() {

        log.info("REST peticion para borrar todos los libros");
        bookRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
