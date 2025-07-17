// src/main/java/it/uniroma3/siw/controller/BookController.java
package it.uniroma3.siw.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.service.BookService;
import jakarta.validation.Valid;
import it.uniroma3.siw.service.AuthorService;

@Controller
public class BookController {

	@Autowired private BookService bookService;
	@Autowired private AuthorService authorService;
	
//    @GetMapping({ "/", "/index" })
//    public String home(Model model) {
//        List<Book> books = (List<Book>) bookService.findAll();
//        model.addAttribute("books", books);
//        model.addAttribute("lastIndex", books.size() - 1);
//        return "index";
//    }


	// UC4: Elenco paginato di tutti i libri
	@GetMapping("/books")
	public String listBooks(Model model) {
		model.addAttribute("books", bookService.findAll());
		return "books.html";
	}

	// UC5: Dettagli di un libro
	@GetMapping("/book/{id}")
	public String showBook(@PathVariable Long id, Model model) {
		Book book = bookService.findById(id);
		model.addAttribute("book", book);
		return "book.html";
	}    

	// UC1: Form per nuovo libro (admin)
	@GetMapping("/admin/formNewBook")
	public String formNewBook(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("allAuthors", authorService.findAll());
		return "admin/formNewBook.html";
	}

	// UC1: Salva nuovo libro
	@PostMapping("/admin/book")
	public String newBook(@Valid @ModelAttribute Book book,
			BindingResult bindingResult,
			@RequestParam(required = false) List<Long> authorIds,
			Model model) {
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
		bookService.save(book);
		return "redirect:/book/" + book.getId();
	}

	// UC2: Form edit libro (admin)
	@GetMapping("/admin/editBook/{id}")
	public String formEditBook(@PathVariable Long id, Model model) {
		model.addAttribute("book", bookService.findById(id));
		model.addAttribute("allAuthors", authorService.findAll());
		return "admin/formEditBook.html";
	}

	// UC2: Salva modifica libro
	@PostMapping("/admin/editBook/{id}")
	public String editBook(@PathVariable Long id,
			@Valid @ModelAttribute Book book,
			BindingResult bindingResult,
			@RequestParam(required = false) List<Long> authorIds,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("allAuthors", authorService.findAll());
			return "admin/formEditBook.html";
		}
		// invece di setId, prendi l’esistente e aggiornalo
		Book existing = bookService.findById(id);
		existing.setTitle(book.getTitle());
		existing.setYear(book.getYear());
		existing.setUrlImage(book.getUrlImage());
		if (authorIds != null) {
			Set<Author> auths = new HashSet<>();
			for (Long aid : authorIds) {
				Author a = authorService.findById(aid);
				if (a != null) auths.add(a);
			}
			existing.setAuthors(auths);
		}
		bookService.save(existing);
		return "redirect:/admin/manageBooks";
	}

	// Admin: lista di gestione libri
	@GetMapping("/admin/manageBooks")
	public String manageBooks(Model model) {
		model.addAttribute("books", bookService.findAll());
		return "admin/manageBooks.html";
	}

	// UC2: Rimuovi libro
	@GetMapping("/admin/removeBook/{id}")
	public String removeBook(@PathVariable Long id) {
		Book book = bookService.findById(id);
		bookService.delete(book);
		return "redirect:/admin/manageBooks";
	}
}