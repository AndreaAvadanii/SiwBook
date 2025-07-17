// src/main/java/it/uniroma3/siw/validator/BookValidator.java
package it.uniroma3.siw.validator;

import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.repository.BookRepository;

@Component
public class BookValidator implements Validator {

    private static final int MIN_YEAR = 1450;
    private static final int MAX_YEAR = Year.now().getValue();

    @Autowired
    private BookRepository bookRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book b = (Book) target;

        // titolo non vuoto
        if (b.getTitle() == null || b.getTitle().trim().isEmpty()) {
            errors.rejectValue("title", "book.title.empty");
        }

        // anno di pubblicazione in range
        if (b.getYear() == null || b.getYear() < MIN_YEAR || b.getYear() > MAX_YEAR) {
            errors.rejectValue("year", "book.year.invalid");
        }

        // duplicato: stesso titolo + anno
        if (b.getTitle() != null && b.getYear() != null) {
            boolean exists = bookRepository.existsByTitleAndYear(b.getTitle(), b.getYear());
            if (exists && b.getId() == null) {
                errors.reject("book.duplicate");
            }
        }
    }
}
