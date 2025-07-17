package it.uniroma3.siw.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.service.CredentialsService;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private CredentialsService credentialsService;

    // UC3: Form nuova recensione
    @GetMapping("/book/{bookId}/formNewReview")
    public String formNewReview(@PathVariable Long bookId,
                                Model model) {
        model.addAttribute("review", new Review());
        model.addAttribute("book", bookService.findById(bookId));
        return "formNewReview.html";
    }

    // UC3: Salva recensione
    @PostMapping("/book/{bookId}/review")
    public String newReview(@PathVariable Long bookId,
                            @Valid @ModelAttribute Review review,
                            BindingResult bindingResult,
                            Principal principal,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", bookService.findById(bookId));
            return "formNewReview.html";
        }
        String username = principal.getName();
        User user = credentialsService.findByUsername(username).getUser();
        reviewService.save(bookId, user.getId(), review);
        return "redirect:/book/" + bookId;
    }

    // UC3: Admin elimina recensione
    @GetMapping("/admin/removeReview/{id}")
    public String removeReview(@PathVariable Long id) {
        reviewService.deleteById(id);
        return "redirect:/admin/manageBooks";
    }

    // UC7: Visualizza proprie recensioni
    @GetMapping("/user/reviews")
    public String myReviews(Principal principal, Model model) {
        String username = principal.getName();
        User user = credentialsService.findByUsername(username).getUser();
        List<Review> reviews = reviewService.findByUser(user.getId());
        model.addAttribute("reviews", reviews);
        return "myReviews.html";
    }
}
