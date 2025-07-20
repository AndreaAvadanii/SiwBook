package it.uniroma3.siw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import jakarta.validation.Valid;

@Controller
public class BookController {

    private static final String UPLOAD_DIR = "src/main/resources/static/images";

    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;

    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books.html";
    }

    @GetMapping("/book/{id}")
    public String showBook(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        double average = 0.0;
        if (book.getReviews() != null && !book.getReviews().isEmpty()) {
            average = book.getReviews()
                         .stream()
                         .mapToDouble(r -> r.getRating())
                         .average()
                         .orElse(0.0);
        }
        double roundedAvg = Math.round(average * 10.0) / 10.0;
        model.addAttribute("book", book);
        model.addAttribute("averageRating", roundedAvg);
        return "book";
    }


    @GetMapping("/admin/formNewBook")
    public String formNewBook(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("allAuthors", authorService.findAll());
        return "admin/formNewBook.html";
    }

    @PostMapping("/admin/book")
    public String newBook(@Valid @ModelAttribute Book book,
                          BindingResult bindingResult,
                          @RequestParam(required = false) List<Long> authorIds,
                          @RequestParam("images") MultipartFile[] images,
                          Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allAuthors", authorService.findAll());
            return "admin/formNewBook.html";
        }
        if (authorIds != null) {
            Set<Author> auths = new HashSet<>();
            for (Long aid : authorIds) {
                Author a = authorService.findById(aid);
                if (a != null) auths.add(a);
            }
            book.setAuthors(auths);
        }
        List<String> filenames = new ArrayList<>();
        for (MultipartFile file : images) {
            if (file != null && !file.isEmpty()) {
                String name = file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + File.separator + name);
                Files.write(filePath, file.getBytes());
                filenames.add(name);
            }
        }
        book.setUrlImages(filenames);
        bookService.save(book);
        return "redirect:/book/" + book.getId();
    }

    @GetMapping("/admin/formUpdateBook/{id}")
    public String formUpdateBook(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        model.addAttribute("allAuthors", authorService.findAll());
        return "admin/formUpdateBook.html";
    }

    @PostMapping("/admin/formUpdateBook/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute Book book,
                             BindingResult bindingResult,
                             @RequestParam(required = false) List<Long> authorIds,
                             @RequestParam("images") MultipartFile[] images,
                             Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allAuthors", authorService.findAll());
            return "admin/formUpdateBook.html";
        }
        Book existing = bookService.findById(id);
        existing.setTitle(book.getTitle());
        existing.setYear(book.getYear());
        // aggiorna autori
        if (authorIds != null) {
            Set<Author> auths = new HashSet<>();
            for (Long aid : authorIds) {
                Author a = authorService.findById(aid);
                if (a != null) auths.add(a);
            }
            existing.setAuthors(auths);
        }
        // aggiunge nuove immagini se ce ne sono
        List<String> filenames = new ArrayList<>();
        for (MultipartFile file : images) {
            if (file != null && !file.isEmpty()) {
                String name = file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, name);
                Files.write(filePath, file.getBytes());
                filenames.add(name);
            }
        }
        if (!filenames.isEmpty()) {
            existing.setUrlImages(filenames);
        }
        bookService.save(existing);
        return "redirect:/admin/manageBooks";
    }


    @GetMapping("/admin/manageBooks")
    public String manageBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "admin/manageBooks.html";
    }

    @GetMapping("/admin/removeBook/{id}")
    public String removeBook(@PathVariable Long id) {
        Book book = bookService.findById(id);
        bookService.delete(book);
        return "redirect:/admin/manageBooks";
    }
}
