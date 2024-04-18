package com.example.techpowerhousebackend.card;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card,Integer> {
    Card findById(int id);

    @Query("SELECT b FROM Card b WHERE (LOWER(b.name) LIKE LOWER(CONCAT('%',:name,'%')))")
    Page<Card> findByTitle(@Param("name") String name, Pageable pageable);


    @Query("SELECT c " +
            "FROM Card c " +
            "WHERE (LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL) AND " +
            "(LOWER(c.creator) LIKE LOWER(CONCAT('%', :creator, '%')) OR :creator IS NULL) AND " +
            "(LOWER(c.publisher) LIKE LOWER(CONCAT('%', :publisher, '%')) OR :publisher IS NULL) AND " +
            "(LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%')) OR :category IS NULL)")
    Page<Card> advancedSearch(@Param("name") String name,
                              @Param("creator") String creator,
                              @Param("publisher") String publisher,
                              @Param("category") String category,
                              Pageable pageable
    );
}