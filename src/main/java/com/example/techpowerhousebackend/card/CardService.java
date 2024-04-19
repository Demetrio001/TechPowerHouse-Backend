package com.example.techpowerhousebackend.card;

import com.example.techpowerhousebackend.support.exceptions.CardNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardService implements CardServiceInterface {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    // Metodo per trovare una card per ID
    @Override
    @Transactional(readOnly = true)
    public Card findById(int id) throws CardNotFoundException {
        Card card = this.cardRepository.findById(id);
        if(card == null) {
            throw new CardNotFoundException(); // Se la card non è trovata, solleva un'eccezione
        }
        return card;
    }

    // Metodo per la ricerca avanzata di cards
    @Override
    @Transactional(readOnly = true)
    public List<Card> advancedSearch(String name, String creator, String publisher, String category,
                                     int pageNumber, int pageSize, String sortBy) {

        // Creazione dell'oggetto PageRequest per la paginazione e l'ordinamento
        PageRequest pageable = switch (sortBy) {
            case "Titolo A-Z" -> PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
            case "Titolo Z-A" -> PageRequest.of(pageNumber, pageSize, Sort.by("name").descending());
            case "Prezzo crescente" -> PageRequest.of(pageNumber, pageSize, Sort.by("price").ascending());
            case "Prezzo decrescente" -> PageRequest.of(pageNumber, pageSize, Sort.by("price").descending());
            default -> PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        };

        // Esecuzione della query avanzata nel repository e recupero del risultato paginato
        Page<Card> pagedResult = cardRepository.advancedSearch(name, creator, publisher, category, pageable);

        // Se il risultato paginato ha contenuti, restituisce la lista delle cards, altrimenti restituisce una lista vuota
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    // Metodo per trovare tutte le cards con paginazione e ordinamento
    @Override
    @Transactional(readOnly = true)
    public List<Card> findAll(int pageNumber, int pageSize, String sortBy) {
        // Creazione dell'oggetto Pageable per la paginazione e l'ordinamento
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        // Esecuzione della query nel repository e recupero del risultato paginato
        Page<Card> pagedResult = cardRepository.findAll(pageable);
        // Se il risultato paginato ha contenuti, restituisce la lista delle cards, altrimenti restituisce una lista vuota
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    // Metodo per trovare le cards per titolo con paginazione e ordinamento
    @Override
    @Transactional(readOnly = true)
    public List<Card> findByTitle(String name, int pageNumber, int pageSize, String sortBy) {
        // Creazione dell'oggetto Pageable per la paginazione e l'ordinamento
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        // Esecuzione della query nel repository e recupero del risultato paginato
        Page<Card> pagedResult = cardRepository.findByTitle(name, pageable);
        // Se il risultato paginato ha contenuti, restituisce la lista delle cards, altrimenti restituisce una lista vuota
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    // Metodo per aggiornare il prezzo di una card
    @Override
    @Transactional
    public void updatePrice(int id, float price) throws CardNotFoundException {
        // Trova la carta per ID
        Card card = findById(id);
        // Aggiorna il prezzo della card
        card.setPrice(price);
        // Salva la card aggiornata nel repository
        this.cardRepository.save(card);
    }
}
