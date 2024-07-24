package com.apink.poppin.api.service;

import com.apink.poppin.User;
import com.apink.poppin.api.popup.entity.Popup;
import com.apink.poppin.api.review.dto.ReviewDto;
import com.apink.poppin.api.review.dto.ReviewUpdateRequestDto;
import com.apink.poppin.api.review.entity.Comment;
import com.apink.poppin.api.review.entity.Review;
import com.apink.poppin.api.review.repository.ReviewRepository;
import com.apink.poppin.api.review.service.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void getReviewByIdFound() {

        // given
        long reviewId = 1L;
        Popup popup = mock(Popup.class);
        User user = mock(User.class);
        List<Comment> comments = new ArrayList<>();
        Review review = Review.builder()
                .reviewId(reviewId)
                .title("Test")
                .popup(popup)
                .content("Test Content")
                .rating(4.0F)
                .user(user)
                .createdAt(Instant.now())
                .comments(comments)
                .build();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.ofNullable(review));

        // when
        ReviewDto reviewDto  = reviewService.getReviewById(reviewId);

        // then
        assertNotNull(reviewDto);
        assertEquals(reviewDto.getReviewId(), reviewId);
        assertEquals(reviewDto.getTitle(), "Test");
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void getReviewByIdNotFound() {

        // given
        long reviewId = 1L;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // when
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> reviewService.getReviewById(reviewId));

        // then
        assertEquals("Review not found", exception.getMessage());
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void updateReviewSuccess() {
        // given
        long reviewId = 1L;
        Popup popup = mock(Popup.class);
        User user = mock(User.class);
        List<Comment> comments = new ArrayList<>();

        Review existingReview = Review.builder()
                .reviewId(reviewId)
                .popup(popup)
                .user(user)
                .rating(5.0F)
                .title("existing title")
                .thumbnail("existing thumbnail")
                .content("existing content")
                .createdAt(Instant.now())
                .comments(comments)
                .build();

        ReviewUpdateRequestDto requestDto = ReviewUpdateRequestDto.builder()
                .rating(4.0F)
                .title("updated title")
                .thumbnail("updated thumbnail")
                .content("updated content")
                .build();

        // when
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.ofNullable(existingReview));

        // then
        reviewService.updateReview(reviewId, requestDto);

        assertEquals(existingReview.getRating(), requestDto.getRating());
        assertEquals(existingReview.getTitle(), requestDto.getTitle());
        assertEquals(existingReview.getThumbnail(), requestDto.getThumbnail());
        assertEquals(existingReview.getContent(), requestDto.getContent());
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewRepository, times(1)).save(existingReview);
    }
}
