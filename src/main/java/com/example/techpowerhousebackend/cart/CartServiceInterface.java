package com.example.techpowerhousebackend.cart;
import com.example.techpowerhousebackend.cartDetail.CartDetail;
import com.example.techpowerhousebackend.order.Order;
import com.example.techpowerhousebackend.support.exceptions.*;
import jakarta.persistence.OptimisticLockException;
import java.util.LinkedList;

public interface CartServiceInterface {
    Cart getCart(String email) throws UserNotFoundException;
    void addToCart(int bookId, String email) throws CardNotFoundException, UserNotFoundException;
    void updateQuantity(int cartDetailId, int quantity, String email) throws OutdatedCartException,
                                                                             UserNotFoundException;
    void deleteItem(int cartDetailId, String email) throws OutdatedCartException, UserNotFoundException;
    void clear(String email) throws UserNotFoundException;
    Order checkout(String email, LinkedList<CartDetail> cartDetails) throws OutdatedPriceException,
            NegativeQuantityException,
            OptimisticLockException,
            EmptyCartException,
            OutdatedCartException, UserNotFoundException;
}