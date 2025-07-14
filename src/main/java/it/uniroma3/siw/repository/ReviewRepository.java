package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Review;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    @Override
    Iterable<Review> findAll();

    List<Review> findByBookId(Long bookId);

    List<Review> findByUserId(Long userId);

    Optional<Review> findByBookIdAndUserId(Long bookId, Long userId);
}
