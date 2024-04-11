package com.example.techpowerhousebackend.card;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card,Integer> {
    Card findById(int id);

    @Query("SELECT b FROM Card b WHERE (LOWER(b.name) LIKE LOWER(CONCAT('%',:title,'%')))")
    Page<Card> findByTitle(@Param("title") String title, Pageable pageable);



}