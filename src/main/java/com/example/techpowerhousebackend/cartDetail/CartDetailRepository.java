package com.example.techpowerhousebackend.cartDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {
    CartDetail findById(int id);

    CartDetail findByCardIdAndCartId(int bookId, int cartId);
}
