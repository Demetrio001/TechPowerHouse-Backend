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

    @Override
    @Transactional
    public Cart getCart(String email) throws UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart();
        for(CartDetail cd: cart.getCartDetails()) {
            Card card = cd.getCard();
            cd.setPrice(card.getPrice());
            cd.setSubTotal(cd.getQuantity()*card.getPrice());
        }
        System.out.println(cart);
        return cart;
    }

    @Override
    @Transactional
    public void addToCart(int cardId, String email) throws CardNotFoundException, UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart();
        Card card = this.cardService.findById(cardId);
        CartDetail cd = this.cartDetailRepository.findByCardIdAndCartId(cardId, cart.getId());
        if(cd == null) {
            cd = new CartDetail(cart, card);
        }
        else {
            int newQuantity = cd.getQuantity()+1;
            cd.setQuantity(newQuantity);
            cd.setPrice(card.getPrice());
            cd.setSubTotal(card.getPrice()*newQuantity);
        }
        this.cartDetailRepository.save(cd);
    }

    @Override
    @Transactional
    public void updateQuantity(int cartDetailId, int quantity, String email) throws OutdatedCartException,
                                                                                    UserNotFoundException {
        CartDetail cd = this.cartDetailRepository.findById(cartDetailId);
        User user = this.userService.findByEmail(email);
        if(cd == null || cd.getCart().getUser().getId() != user.getId()) {
            throw new OutdatedCartException();
        }
        Card card = cd.getCard();
        cd.setQuantity(quantity);
        cd.setPrice(card.getPrice());
        cd.setSubTotal(quantity*card.getPrice());
        this.cartDetailRepository.save(cd);
    }

    @Override
    @Transactional
    public void deleteItem(int cartDetailId, String email) throws OutdatedCartException, UserNotFoundException {
        CartDetail cd = this.cartDetailRepository.findById(cartDetailId);
        User user = this.userService.findByEmail(email);
        if(cd == null || cd.getCart().getUser().getId() != user.getId()) {
            throw new OutdatedCartException();
        }
        cd.setCart(null);
        this.cartDetailRepository.delete(cd);
    }

    @Override
    @Transactional
    public void clear(String email) throws UserNotFoundException {
        Cart cart = this.userService.findByEmail(email).getCart();
        for(CartDetail cd: cart.getCartDetails()) {
            cd.setCart(null);
            this.cartDetailRepository.delete(cd);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order checkout(String email, LinkedList<CartDetail> cartDetails) throws OutdatedPriceException,
                                                                                    NegativeQuantityException,
                                                                                    OptimisticLockException,
                                                                                    EmptyCartException,
                                                                                    OutdatedCartException,
                                                                                    UserNotFoundException {
        User user = this.userService.findByEmail(email);
        List<CartDetail> dbCartDetails = user.getCart().getCartDetails();
        if(dbCartDetails.size() != cartDetails.size()) {
            throw new OutdatedCartException();
        }
        if(cartDetails.isEmpty()) {
            throw new EmptyCartException();
        }

        Order savedOrder = this.orderRepository.save(new Order(user));
        float total = 0;
        for(CartDetail cd: cartDetails) {
            CartDetail item = this.cartDetailRepository.findById(cd.getId());
            if(item == null || item.getCart().getUser().getId() != user.getId()) {
                throw new OutdatedCartException();
            }
            if(item.getQuantity() != cd.getQuantity()) {
                throw new OutdatedCartException();
            }

            Card card = item.getCard();
            float currentPrice = card.getPrice();
            float priceInCart = item.getPrice();
            if(Math.abs(priceInCart-currentPrice) >= 0.01f) {
                throw new OutdatedPriceException();
            }

            int quantity = item.getQuantity();
            int newQuantity = card.getQuantity()-quantity;
            if(newQuantity < 0) {
                throw new NegativeQuantityException();
            }
            card.setQuantity(newQuantity);
            Card updatedCard = this.cardRepository.save(card);

            OrderDetail od = new OrderDetail(updatedCard,savedOrder,priceInCart,quantity);
            this.orderDetailRepository.save(od);
            item.setCart(null);
            this.cartDetailRepository.delete(item);

            total += priceInCart*quantity;
        }
        savedOrder.setTotal(total);
        return savedOrder;
    }

}
