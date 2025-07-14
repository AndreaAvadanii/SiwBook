package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Author;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

    @Override
    Iterable<Author> findAll();

    List<Author> findBySurnameContainingIgnoreCase(String surname);

    List<Author> findByNationalityContainingIgnoreCase(String nationality);

    List<Author> findByBooksId(Long bookId);
}
