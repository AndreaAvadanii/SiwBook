package it.uniroma3.siw.controller;

import java.util.List;

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
    @Autowired private UserService        userService;
    
    @Autowired private BookService bookService;


    @Autowired private CredentialsValidator credentialsValidator;
    @Autowired private UserValidator        userValidator;

    /* ---------- PAGINE PUBBLICHE ---------- */

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

    /* ---------- HOME smart ---------- */

    @GetMapping({ "/", "/index" })
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            List<Book> books = (List<Book>) bookService.findAll();
            model.addAttribute("books", books);
            model.addAttribute("lastIndex", books.size() - 1);
            return "index";
        }
        return "redirect:/success";
    }


    /* ---------- DOPO LOGIN ---------- */

    @GetMapping("/success")
    public String defaultAfterLogin(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Credentials creds = credentialsService.getCredentials(userDetails.getUsername());
        User user = creds.getUser();

        model.addAttribute("user", user);
        model.addAttribute("reviews", user.getReviews()); 
        
        List<Book> books = (List<Book>) bookService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("lastIndex", books.size() - 1);


        if (Credentials.ADMIN_ROLE.equals(creds.getRole())) {
            return "admin/index";
        }
        return "user/index";
    }

    

    /* ---------- REGISTRA NUOVO UTENTE (→ AGENT) ---------- */

    @PostMapping(value = "/register" )
    public String registerUser(@Valid @ModelAttribute("user") User user,
                 BindingResult userBindingResult, @Valid
                 @ModelAttribute("credentials") Credentials credentials,
                 BindingResult credentialsBindingResult, Model model) {
		
		this.credentialsValidator.validate(credentials, credentialsBindingResult);
		this.userValidator.validate(user, userBindingResult);
		
		// se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!credentialsBindingResult.hasErrors() && !userBindingResult.hasErrors()) {
        	
        	userService.saveUser(user);
        	credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
                        
            model.addAttribute("user", user);
            return "login.html";
        }
        return "register.html";
    }
}
