package com.example.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "reservations")
@Data

public class Reservation {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

@ManyToOne
@JoinColumn(name = "user_id")
private User user;

@ManyToOne
@JoinColumn(name= "shop_id")
private Shop shop;

@Column(name= "reservation_datetime", nullable = false)
private LocalDateTime reservationDatetime;

@Column(name= "number_of_people", nullable = false)
private Integer numberOfPeople;

@Enumerated(EnumType.STRING)
@Column(name="status", nullable = false)
private ReservationStatus status;

public enum ReservationStatus {
    CONFIRMED,
    CANCELLED,
    VISITED
}

/*@Column(name= "status", nullable = false)
private String status;*/

@CreationTimestamp
@Column(updatable =false)
private LocalDateTime createdAt;

@UpdateTimestamp
@Column(updatable = false)
private LocalDateTime updatedAt;
}