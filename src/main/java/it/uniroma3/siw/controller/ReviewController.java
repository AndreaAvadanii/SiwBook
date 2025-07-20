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
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/admin/manageReviews")
    public String manageReviews(Model model) {
        List<Review> reviews = (List<Review>) reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "admin/manageReviews.html";
    }

    @GetMapping("/admin/removeReview/{id}")
    public String removeReview(@PathVariable Long id) {
        reviewService.deleteById(id);
        return "redirect:/admin/manageReviews";
    }

    @GetMapping("/book/{bookId}/formNewReview")
    public String formNewReview(@PathVariable Long bookId, Model model) {
        model.addAttribute("review", new Review());
        model.addAttribute("book", bookService.findById(bookId));
        return "user/formNewReview";
    }

    @GetMapping("/book/{bookId}/formUpdateReview/{reviewId}")
    public String formUpdateReview(@PathVariable Long bookId,
                                   @PathVariable Long reviewId,
                                   Principal principal,
                                   Model model) {
        Review review = reviewService.findById(reviewId);
        Long authenticatedUserId = credentialsService
            .findByUsername(principal.getName())
            .getUser()
            .getId();
        if (!review.getUser().getId().equals(authenticatedUserId)) {
            return "redirect:/access-denied";
        }
        model.addAttribute("review", review);
        model.addAttribute("book", bookService.findById(bookId));
        return "user/formUpdateReview";
    }

    @PostMapping("/book/{bookId}/review")
    public String newReview(@PathVariable Long bookId,
                            @Valid @ModelAttribute Review review,
                            BindingResult bindingResult,
                            Principal principal,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", bookService.findById(bookId));
            return "user/formNewReview";
        }
        User user = credentialsService
            .findByUsername(principal.getName())
            .getUser();
        reviewService.save(bookId, user.getId(), review);
        return "redirect:/book/" + bookId;
    }

    @PostMapping("/book/{bookId}/review/{reviewId}/update")
    public String updateReview(@PathVariable Long bookId,
                               @PathVariable Long reviewId,
                               @Valid @ModelAttribute Review review,
                               BindingResult bindingResult,
                               Principal principal,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", bookService.findById(bookId));
            return "user/formUpdateReview";
        }
        Review existing = reviewService.findById(reviewId);
        Long authenticatedUserId = credentialsService
            .findByUsername(principal.getName())
            .getUser()
            .getId();
        if (!existing.getUser().getId().equals(authenticatedUserId)) {
            return "redirect:/access-denied";
        }
        existing.setTitle(review.getTitle());
        existing.setRating(review.getRating());
        existing.setText(review.getText());
        reviewService.save(bookId, authenticatedUserId, existing);
        return "redirect:/book/" + bookId;
    }

    @GetMapping("/user/reviews")
    public String myReviews(Principal principal, Model model) {
        Long authenticatedUserId = credentialsService
            .findByUsername(principal.getName())
            .getUser()
            .getId();
        List<Review> reviews = reviewService.findByUser(authenticatedUserId);
        model.addAttribute("reviews", reviews);
        return "user/reviews";
    }
}
