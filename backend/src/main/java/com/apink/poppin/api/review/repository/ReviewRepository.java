package com.apink.poppin.api.review.repository;

import com.apink.poppin.api.review.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = "comments", type = EntityGraph.EntityGraphType.FETCH)
    @NonNull
    Optional<Review> findById(@NonNull Long reviewId);
}
