package com.example.techpowerhousebackend.user;

import com.example.techpowerhousebackend.cart.Cart;
import com.example.techpowerhousebackend.cart.CartRepository;
import com.example.techpowerhousebackend.support.Registration;
import com.example.techpowerhousebackend.support.RegistrationRequest;
import com.example.techpowerhousebackend.support.exceptions.KeycloackRegistrationException;
import com.example.techpowerhousebackend.support.exceptions.MailUserAlreadyExistsException;
import com.example.techpowerhousebackend.support.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public UserService(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    // Metodo per trovare tutti gli utenti
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    // Metodo per trovare un utente per email
    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) throws UserNotFoundException {
        // Controlla se l'utente esiste tramite l'email fornita
        if (!this.userRepository.existsByEmail(email)) {
            throw new UserNotFoundException();
        }
        // Restituisce l'utente trovato
        return this.userRepository.findByEmail(email);
    }

    // Metodo per registrare un nuovo utente
    @Override
    @Transactional
    public User register(RegistrationRequest registrationRequest) throws MailUserAlreadyExistsException,
            KeycloackRegistrationException {
        // Ottiene l'utente dalla richiesta di registrazione
        User user = registrationRequest.getUser();
        // Controlla se l'utente esiste già tramite l'email
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new MailUserAlreadyExistsException();
        }
        try {
            // Registra l'utente tramite Keycloak
            Registration.keycloakRegistration(registrationRequest);
        } catch (KeycloackRegistrationException ke) {
            // Gestisce eventuali eccezioni durante la registrazione
            throw new KeycloackRegistrationException();
        }
        // Salva l'utente nel database
        User savedUser = this.userRepository.save(user);
        // Crea un nuovo carrello per l'utente e lo associa
        Cart userCart = new Cart();
        userCart.setUser(savedUser);
        savedUser.setCart(this.cartRepository.save(userCart));
        // Restituisce l'utente salvato
        return savedUser;
    }
}

