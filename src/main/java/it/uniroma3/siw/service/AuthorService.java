package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.repository.AuthorRepository;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author findById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    public void save(Author author) {
        authorRepository.save(author);
    }

    public void delete(Author author) {
        authorRepository.delete(author);
    }

    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    public List<Author> findBySurnameContainingIgnoreCase(String surname) {
        return authorRepository.findBySurnameContainingIgnoreCase(surname);
    }

    public List<Author> findByNationalityContainingIgnoreCase(String nationality) {
        return authorRepository.findByNationalityContainingIgnoreCase(nationality);
    }

    public List<Author> findByBooksId(Long bookId) {
        return authorRepository.findByBooksId(bookId);
    }
}
