package it.uniroma3.siw.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private Integer rating;

	@Column(length = 2000)
	private String text;

	@ManyToOne
	private Book book;

	@ManyToOne
	private User user;

	public Review() {}

	public Review(String title, Integer rating, String text, Book book, User user) {
		this.title = title;
		this.rating = rating;
		this.text = text;
		this.book = book;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Integer getRating() {
		return rating;
	}

	public String getText() {
		return text;
	}

	public Book getBook() {
		return book;
	}

	public User getUser() {
		return user;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public void setUser(User user) {
		this.user = user;
	}
}