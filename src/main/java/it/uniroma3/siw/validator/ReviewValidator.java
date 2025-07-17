// src/main/java/it/uniroma3/siw/validator/ReviewValidator.java
package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;

@Component
public class ReviewValidator implements Validator {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Review.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Review r = (Review) target;

        // voto obbligatorio e compreso 1–5
        if (r.getRating() == null || r.getRating() < 1 || r.getRating() > 5) {
            errors.rejectValue("rating", "review.rating.invalid");
        }

        // testo non vuoto
        if (r.getText() == null || r.getText().trim().isEmpty()) {
            errors.rejectValue("text", "review.text.empty");
        }

        // duplicato: stessa recensione per utente/libro
        if (r.getBook() != null && r.getUser() != null) {
            boolean exists = reviewRepository
                .findByBookIdAndUserId(r.getBook().getId(), r.getUser().getId())
                .isPresent();
            if (exists && r.getId() == null) {
                errors.reject("review.duplicate");
            }
        }
    }
}
