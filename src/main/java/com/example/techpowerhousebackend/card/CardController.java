package com.example.techpowerhousebackend.card;

import com.example.techpowerhousebackend.support.exceptions.CardNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:9090", allowedHeaders = "*")
public class CardController {
    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/cards")
    public ResponseEntity<?> getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        List<Card> cards = this.cardService.findAll(pageNumber, pageSize, sortBy);
        if (cards.isEmpty()) {
            return new ResponseEntity<>("No cards found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @GetMapping("/cards/name")
    public ResponseEntity<?> getByTitle(@RequestParam(value = "name") String name,
                                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        List<Card> cards = this.cardService.findByTitle(name, pageNumber, pageSize, sortBy);
        if (cards.isEmpty()) {
            return new ResponseEntity<>("No cards found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @GetMapping("/cards/advancedSearch")
    public ResponseEntity<?> advancedSearch(@RequestParam(value = "name", defaultValue = "") String name,
                                            @RequestParam(value = "creator", defaultValue = "") String creator,
                                            @RequestParam(value = "publisher", defaultValue = "") String publisher,
                                            @RequestParam(value = "category", defaultValue = "") String category,
                                            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        List<Card> cards = cardService.advancedSearch(name, creator, publisher, category, pageNumber, pageSize, sortBy);
        if (cards.isEmpty()) {
            return new ResponseEntity<>("No cards found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @PutMapping("/admin/cards/{id}/updatePrice")
    public ResponseEntity<?> updatePrice(@PathVariable int id, @RequestParam float newPrice) {
        try {
            cardService.updatePrice(id, newPrice);
            return new ResponseEntity<>("Price updated successfully", HttpStatus.OK);
        } catch (CardNotFoundException e) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
    }

}