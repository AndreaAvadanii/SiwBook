package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthdate;
    private LocalDate deathdate;
    private String nationality;
    private String urlImage;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books;

    public Author() {}

    public Author(String name, String surname, LocalDate birthdate, LocalDate deathdate, String nationality, String urlImage) {
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.deathdate = deathdate;
        this.nationality = nationality;
        this.urlImage = urlImage;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public LocalDate getDeathdate() {
        return deathdate;
    }

    public String getNationality() {
        return nationality;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setDeathdate(LocalDate deathdate) {
        this.deathdate = deathdate;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}