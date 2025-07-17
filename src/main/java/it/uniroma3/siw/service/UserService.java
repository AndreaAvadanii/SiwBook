package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
    @Autowired
    private CredentialsRepository credentialsRepository;

	 @Transactional
	 public User getUser(Long id) {
	        Optional<User> result = this.userRepository.findById(id);
	        return result.orElse(null);
	    }

	   
	  @Transactional
	   public User saveUser(User user) {
	        return this.userRepository.save(user);
	    }
	 

	    
	    @Transactional
	    public List<User> getAllUsers() {
	        List<User> result = new ArrayList<>();
	        Iterable<User> iterable = this.userRepository.findAll();
	        for(User user : iterable)
	            result.add(user);
	        return result;
	    }
	 
	    public User findById(Long id) {
	        return userRepository.findById(id).orElse(null);
	    }
	    
	    public void registerUser(User user, Credentials credentials) {
	        // 1) Salva l’utente
	        User saved = userRepository.save(user);
	        // 2) Collega le credenziali e salva
	        credentials.setUser(saved);
	        credentialsRepository.save(credentials);
	    }

}
