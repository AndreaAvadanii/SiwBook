package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

	public Review findById(Long id) {
		return reviewRepository.findById(id).orElse(null);
	}

	public Iterable<Review> findAll() {
		return reviewRepository.findAll();
	}

    public Review save(Long bookId, Long userId, Review review) {
        Book book = bookService.findById(bookId);
        User user = userService.findById(userId);
        review.setBook(book);
        review.setUser(user);
        return reviewRepository.save(review);
    }

	public void delete(Review review) {
		reviewRepository.delete(review);
	}

	public void deleteById(Long id) {
		reviewRepository.deleteById(id);
	}

	public List<Review> findByBookId(Long bookId) {
		return reviewRepository.findByBookId(bookId);
	}

	public List<Review> findByUserId(Long userId) {
		return reviewRepository.findByUserId(userId);
	}

	public boolean existsByBookIdAndUserId(Long bookId, Long userId) {
		return reviewRepository.findByBookIdAndUserId(bookId, userId).isPresent();
	}

	public List<Review> findByUser(Long userId) {
		return reviewRepository.findByUserId(userId);
	}
}
