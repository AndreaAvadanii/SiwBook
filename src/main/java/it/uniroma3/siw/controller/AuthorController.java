package it.uniroma3.siw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.service.AuthorService;
import jakarta.validation.Valid;

@Controller
public class AuthorController {

    private static final String UPLOAD_DIR = "src/main/resources/static/images";

    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "authors.html";
    }

    @GetMapping("/author/{id}")
    public String showAuthor(@PathVariable Long id, Model model) {
        model.addAttribute("author", authorService.findById(id));
        return "author.html";
    }

    /* ---------- AREA ADMIN ---------- */
    @GetMapping("/admin/formNewAuthor")
    public String formNewAuthor(Model model) {
        model.addAttribute("author", new Author());
        return "admin/formNewAuthor.html";
    }

    @PostMapping("/admin/author")
    public String newAuthor(@Valid @ModelAttribute Author author,
                            BindingResult bindingResult,
                            @RequestParam("image") MultipartFile image,
                            Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            return "admin/formNewAuthor.html";
        }
        if (image != null && !image.isEmpty()) {
            String filename = image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + File.separator + filename);
            Files.write(filePath, image.getBytes());
            author.setUrlImage(filename);
        }
        authorService.save(author);
        return "redirect:/author/" + author.getId();
    }

    @GetMapping("/admin/formUpdateAuthor/{id}")
    public String formUpdateAuthor(@PathVariable Long id, Model model) {
        model.addAttribute("author", authorService.findById(id));
        return "admin/formUpdateAuthor.html";
    }

    @PostMapping("/admin/formUpdateAuthor/{id}")
    public String updateAuthor(@PathVariable Long id,
                               @Valid @ModelAttribute Author author,
                               BindingResult bindingResult,
                               @RequestParam("image") MultipartFile image,
                               Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            return "admin/formUpdateAuthor.html";
        }
        Author existing = authorService.findById(id);
        existing.setName(author.getName());
        existing.setSurname(author.getSurname());
        existing.setBirthdate(author.getBirthdate());
        existing.setDeathdate(author.getDeathdate());
        existing.setNationality(author.getNationality());

        if (image != null && !image.isEmpty()) {
            String filename = image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + File.separator + filename);
            Files.write(filePath, image.getBytes());
            existing.setUrlImage(filename);
        }
        authorService.save(existing);
        return "redirect:/admin/manageAuthors";
    }

    @GetMapping("/admin/manageAuthors")
    public String manageAuthors(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "admin/manageAuthors.html";
    }

    @GetMapping("/admin/removeAuthor/{id}")
    public String removeAuthor(@PathVariable Long id) {
        Author author = authorService.findById(id);
        authorService.delete(author);
        return "redirect:/admin/manageAuthors";
    }
}
