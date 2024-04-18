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

    @Override
    @Transactional(readOnly = true)
    public Card findById(int id) throws CardNotFoundException {
        Card card = this.cardRepository.findById(id);
        if(card == null) {
            throw new CardNotFoundException();
        }
        return card;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Card> advancedSearch(String name, String creator, String publisher, String category,
                                     int pageNumber, int pageSize, String sortBy) {

        PageRequest pageable = switch (sortBy) {
            case "Titolo A-Z" -> PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
            case "Titolo Z-A" -> PageRequest.of(pageNumber, pageSize, Sort.by("name").descending());
            case "Prezzo crescente" -> PageRequest.of(pageNumber, pageSize, Sort.by("price").ascending());
            case "Prezzo decrescente" -> PageRequest.of(pageNumber, pageSize, Sort.by("price").descending());
            default -> PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        };
        Page<Card> pagedResult = cardRepository.advancedSearch(name,creator,publisher,category,pageable);
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }


    @Override
    @Transactional(readOnly = true)
    public List<Card> findAll(int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Card> pagedResult = cardRepository.findAll(pageable);
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> findByTitle(String name, int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Card> pagedResult = cardRepository.findByTitle(name, pageable);
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }



    @Override
    @Transactional
    public void updatePrice(int id, float price) throws CardNotFoundException {
        Card card = findById(id);
        card.setPrice(price);
        this.cardRepository.save(card);
    }

}