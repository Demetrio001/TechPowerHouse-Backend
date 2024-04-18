package com.example.techpowerhousebackend.card;
import com.example.techpowerhousebackend.support.exceptions.CardNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CardServiceInterface {

    Card findById(int id) throws CardNotFoundException;

    @Transactional(readOnly = true)
    List<Card> advancedSearch(String title, String author, String publisher, String category,
                              int pageNumber, int pageSize, String sortBy);

    List<Card> findAll(int pageNumber, int pageSize, String sortBy);
    List<Card> findByTitle(String title, int pageNumber, int pageSize, String sortBy);

    void updatePrice(int id, float price) throws CardNotFoundException;

}