package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;

@Controller
public class GlobalController {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @ModelAttribute("currentUser")
    public User getUser(Authentication authentication) {
        if (authentication==null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        String username = null;

        if (principal instanceof UserDetails) {
            // login form-based
            username = ((UserDetails) principal).getUsername();
        }
        else if (principal instanceof OidcUser) {
            // OAuth2 / OIDC
            OidcUser oidc = (OidcUser) principal;
            // di solito l'email è l'attributo più univoco
            username = oidc.getAttribute("email");
        }
        else if (principal instanceof OAuth2User) {
            // OAuth2 senza OIDC
            OAuth2User oauth2 = (OAuth2User) principal;
            username = oauth2.getAttribute("email");
        }

        if (username!=null) {
            return credentialsRepository
                     .findByUsername(username)
                     .map(Credentials::getUser)
                     .orElse(null);
        }
        return null;
    }
}
