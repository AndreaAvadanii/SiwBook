package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.service.AuthorService;

@Controller
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    // UC6: Elenco autori
    @GetMapping("/authors")
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "authors.html";
    }

    // UC6: Dettagli autore
    @GetMapping("/author/{id}")
    public String showAuthor(@PathVariable Long id, Model model) {
        Author author = authorService.findById(id);
        model.addAttribute("author", author);
        return "author.html";
    }
}
