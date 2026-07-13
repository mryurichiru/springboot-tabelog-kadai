package com.example.entity;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name="shops")
@Data
public class Shop {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private Integer id;

@Column(name= "name")
private String name;

@Column(name = "description")
private String description;

@Column(name = "phone_number")
private String phoneNumber;

@Column(name = "email")
private String email;

@Column(name = "price_range")
private Integer priceRange;

@Column(name="opening_time")
private LocalTime openingTime;

@Column(name="closing_time")
private LocalTime closingTime;

@Column(name="holiday")
private String holiday;

@Column(name = "image_name")
private String imageName;

@Column(name = "postal_code")
private String postalCode;

@Column(name = "address")
private String address;

@Column(name = "created_at", insertable = false, updatable = false)
private Timestamp createdAt;

@Column(name = "updated_at", insertable = false, updatable = false)
private Timestamp updatedAt;

@ManyToMany
@JoinTable(
    name = "shop_categories",
    joinColumns = @JoinColumn(name = "shop_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id")
)
private List<Category> categories;
}