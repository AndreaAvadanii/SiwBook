package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @Override
    Iterable<Book> findAll();

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorsId(Long authorId);
}
