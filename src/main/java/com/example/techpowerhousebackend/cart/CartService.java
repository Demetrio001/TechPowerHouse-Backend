package com.example.techpowerhousebackend.cart;

import com.example.techpowerhousebackend.card.Card;

import com.example.techpowerhousebackend.card.CardRepository;
import com.example.techpowerhousebackend.card.CardService;
import com.example.techpowerhousebackend.cartDetail.CartDetail;
import com.example.techpowerhousebackend.cartDetail.CartDetailRepository;
import com.example.techpowerhousebackend.order.Order;
import com.example.techpowerhousebackend.order.OrderRepository;
import com.example.techpowerhousebackend.orderDetail.OrderDetail;
import com.example.techpowerhousebackend.orderDetail.OrderDetailRepository;
import com.example.techpowerhousebackend.support.exceptions.*;
import com.example.techpowerhousebackend.user.User;
import com.example.techpowerhousebackend.user.UserService;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class CartService implements CartServiceInterface {
    private final CardService cardService;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final CardRepository cardRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserService userService;

    @Autowired
    public CartService(CardService cardService, CartDetailRepository cartDetailRepository,
                       OrderRepository orderRepository, CardRepository cardRepository,
                       OrderDetailRepository orderDetailRepository, UserService userService) {
        this.cardService = cardService;
        this.cartDetailRepository = cartDetailRepository;
        this.orderRepository = orderRepository;
        this.cardRepository = cardRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userService = userService;
    }
    // Metodo per ottenere il carrello di un utente
    @Override
    @Transactional
    public Cart getCart(String email) throws UserNotFoundException {

        Cart cart = this.userService.findByEmail(email).getCart();
        for(CartDetail cd: cart.getCartDetails()) {
            Card card = cd.getCard();
            cd.setPrice(card.getPrice());
            cd.setSubTotal(cd.getQuantity()*card.getPrice());
        }
        return cart;
    }
    // Metodo per aggiungere un elemento al carrello di uno specifico utente
    @Override
    @Transactional
    public void addToCart(int cardId, String email) throws CardNotFoundException, UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart();
        Card card = this.cardService.findById(cardId);
        CartDetail cd = this.cartDetailRepository.findByCardIdAndCartId(cardId, cart.getId());
        if(cd == null) { //controllo per verificare se esiste giá un cart detail e, quindi, il carrello sia vuoto o meno
            cd = new CartDetail(cart, card);
        }
        else {// si aggiunge al cart detail già esistente il nuovo contenuto
            int newQuantity = cd.getQuantity()+1;
            cd.setQuantity(newQuantity);
            cd.setPrice(card.getPrice());
            cd.setSubTotal(card.getPrice()*newQuantity);
        }
        this.cartDetailRepository.save(cd);
    }

    //Metodo che aggiorna la quantità di un oggetto nel carrello di un utente
    @Override
    @Transactional
    public void updateQuantity(int cartDetailId, int quantity, String email) throws OutdatedCartException,
                                                                                    UserNotFoundException {
        CartDetail cd = this.cartDetailRepository.findById(cartDetailId);
        User user = this.userService.findByEmail(email);
        if(cd == null || cd.getCart().getUser().getId() != user.getId()) {
            throw new OutdatedCartException();
        }// controllo se il cart detail é vuoto o che gli user non coincidono
        Card card = cd.getCard();
        cd.setQuantity(quantity);
        cd.setPrice(card.getPrice());
        cd.setSubTotal(quantity*card.getPrice());
        this.cartDetailRepository.save(cd);
    }

    //Metodo per eliminare una card dal carrello
    @Override
    @Transactional
    public void deleteItem(int cartDetailId, String email) throws OutdatedCartException, UserNotFoundException {
        CartDetail cd = this.cartDetailRepository.findById(cartDetailId);
        User user = this.userService.findByEmail(email);
        if(cd == null || cd.getCart().getUser().getId() != user.getId()) {
            throw new OutdatedCartException();
        }// controllo se il cart detail é vuoto o che gli user non coincidono
        cd.setCart(null);
        this.cartDetailRepository.delete(cd);
    }

    //Metodo per eliminare tutti i prodotti dal carrello
    @Override
    @Transactional
    public void clear(String email) throws UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart();
        for(CartDetail cd: cart.getCartDetails()) {
            cd.setCart(null);
            this.cartDetailRepository.delete(cd);
        }
    }

    //Meotodo per completare l'acquisto e, dunque, effetturare l'ordine
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order checkout(String email, LinkedList<CartDetail> cartDetails) throws OutdatedPriceException,
            NegativeQuantityException,OptimisticLockException, EmptyCartException, OutdatedCartException, UserNotFoundException {
        User user = this.userService.findByEmail(email);
        // ottieni i dettagli del carrello dall'utente dal database
        List<CartDetail> dbCartDetails = user.getCart().getCartDetails();
        // controlla se i cart details nel database corrispondono a quelli forniti
        if(dbCartDetails.size() != cartDetails.size()) {
            throw new OutdatedCartException();
        }
        // controlla se il carrello è vuoto
        if(cartDetails.isEmpty()) {
            throw new EmptyCartException();
        }

        // salva un nuovo ordine nel database per l'utente
        Order savedOrder = this.orderRepository.save(new Order(user));
        float total = 0;
        for(CartDetail cd: cartDetails) {
            // trova il cart detail corrispondente nel database
            CartDetail item = this.cartDetailRepository.findById(cd.getId());
            // se non esiste o non è associato all'utente corrente, lancia un'eccezione OutdatedCartException
            if(item == null || item.getCart().getUser().getId() != user.getId()) {
                throw new OutdatedCartException();
            }
            // se la quantità nel carrello fornito è diversa da quella nel database, lancia un'eccezione OutdatedCartException
            if(item.getQuantity() != cd.getQuantity()) {
                throw new OutdatedCartException();
            }

            Card card = item.getCard();
            float currentPrice = card.getPrice();
            float priceInCart = item.getPrice();
            // Se il prezzo nel carrello è diverso da quello attuale della carta, lancia un'eccezione OutdatedPriceException
            if(Math.abs(priceInCart-currentPrice) >= 0.01f) {
                throw new OutdatedPriceException();
            }

            // aggiorna la quantità della card disponibile nel db
            int quantity = item.getQuantity();
            int newQuantity = card.getQuantity()-quantity;
            // se la quantità disponibile è negativa, lancia un'eccezione NegativeQuantityException
            if(newQuantity < 0) {
                throw new NegativeQuantityException();
            }
            card.setQuantity(newQuantity);
            Card updatedCard = this.cardRepository.save(card);

            // salva gli order datails nel database
            OrderDetail od = new OrderDetail(updatedCard,savedOrder,priceInCart,quantity);
            this.orderDetailRepository.save(od);
            // rimuove il cart detail associato all'ordine
            item.setCart(null);
            this.cartDetailRepository.delete(item);

            // calcola il totale dell'ordine
            total += priceInCart*quantity;
        }
        // imposta il totale dell'ordine e restituisce l'ordine salvato
        savedOrder.setTotal(total);
        return savedOrder;
    }


}
