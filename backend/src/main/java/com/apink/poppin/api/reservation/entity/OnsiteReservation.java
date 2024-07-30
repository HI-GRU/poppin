package com.apink.poppin.api.reservation.entity;

import com.apink.poppin.api.popup.entity.Popup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "onsite_reservation")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnsiteReservation {

    @Id
    @NotNull
    private Long onsiteReservationId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "popup_id", nullable = false)
    private Popup popup;

    @Size(max = 16)
    @NotNull
    @Column(nullable = false, length = 16)
    private String phoneNumber;

    @NotNull
    @Column(nullable = false)
    private LocalDate visitedDate;

    @NotNull
    private int reservationCount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_statement_id", nullable = false)
    private ReservationStatement reservationStatement;
}