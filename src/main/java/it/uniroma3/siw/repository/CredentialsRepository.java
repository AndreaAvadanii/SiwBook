package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Credentials;

@Repository
public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

    @Override
    Iterable<Credentials> findAll();

    Optional<Credentials> findByUsername(String username);

    List<Credentials> findByRole(String role);
}
