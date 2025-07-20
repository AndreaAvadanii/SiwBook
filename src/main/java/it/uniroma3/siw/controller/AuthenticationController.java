package it.uniroma3.siw.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.validator.CredentialsValidator;
import it.uniroma3.siw.validator.UserValidator;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

    @Autowired private CredentialsService credentialsService;
    @Autowired private UserService userService;
    @Autowired private BookService bookService;
    @Autowired private CredentialsValidator credentialsValidator;
    @Autowired private UserValidator userValidator;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "register.html";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login.html";
    }

    @GetMapping({ "/", "/index" })
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            List<Book> books = getFeaturedBooks();
            model.addAttribute("books", books);
            model.addAttribute("lastIndex", books.isEmpty() ? 0 : books.size() - 1);
            return "index";
        }
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String defaultAfterLogin(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Credentials creds = credentialsService.getCredentials(userDetails.getUsername());
        User user = creds.getUser();

        model.addAttribute("user", user);
        model.addAttribute("reviews", user.getReviews());

        List<Book> books = getFeaturedBooks();
        model.addAttribute("books", books);
        model.addAttribute("lastIndex", books.isEmpty() ? 0 : books.size() - 1);

        if (Credentials.ADMIN_ROLE.equals(creds.getRole())) {
            return "admin/index";
        }
        return "user/index";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult userBindingResult,
            @Valid @ModelAttribute("credentials") Credentials credentials,
            BindingResult credentialsBindingResult,
            Model model) {

        credentialsValidator.validate(credentials, credentialsBindingResult);
        userValidator.validate(user, userBindingResult);

        if (!credentialsBindingResult.hasErrors() && !userBindingResult.hasErrors()) {
            userService.saveUser(user);
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "login.html";
        }
        return "register.html";
    }

    private List<Book> getFeaturedBooks() {
        return ((Collection<Book>) bookService.findAll()).stream()
                .filter(book -> book.getReviews() != null && !book.getReviews().isEmpty())
                .filter(book -> book.getReviews()
                                    .stream()
                                    .mapToDouble(r -> r.getRating())
                                    .average()
                                    .orElse(0.0) >= 4.0)
                .collect(Collectors.toList());
    }
}
