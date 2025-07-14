package it.uniroma3.siw.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

public class Book {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer year;
    private String urlImage;

    @ManyToMany
    private Set<Author> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    public Book() {}

    public Book(String title, Integer year, String urlImage) {
        this.title = title;
        this.year = year;
        this.urlImage = urlImage;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}
