package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.repository.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

    public void delete(Book book) {
        bookRepository.delete(book);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> findByTitleContainingIgnoreCase(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> findByAuthorsId(Long authorId) {
        return bookRepository.findByAuthorsId(authorId);
    }
}
