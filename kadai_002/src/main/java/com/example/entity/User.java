package com.example.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name="users")
@Data
public class User {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")
private Integer id;

@Column(name="name")
private String name;

@Column(name = "furigana")
private String furigana;    
   
@Column(name = "email")
private String email;
    
@Column(name = "password")
private String password;    

@Column(name = "phone_number")
private String phoneNumber;

@Column(name = "membership_type")
private int membershipType;
public static final int FREE_MEMBER = 0;
public static final int PAID_MEMBER = 1;


public boolean isPaidMember() {
    return this.membershipType == PAID_MEMBER;
}

public int getMembershipType() {
    return membershipType;
}

public void setMembershipType(int membershipType) {
    this.membershipType = membershipType;
}

@Column(name = "date_of_birth")
private LocalDate dateOfBirth;

@Column(name = "occupation")
private String occupation;


@ManyToOne
@JoinColumn(name = "role_id")
private Role role;   

@Column(name = "enabled")
private Boolean enabled;

@Column(name = "stripe_customer_id")
private String stripeCustomerId;

@Column(name = "stripe_subscription_id")
private String stripeSubscriptionId;

@Column(name = "created_at", insertable = false, updatable = false)
private Timestamp createdAt;

@Column(name = "updated_at", insertable = false, updatable = false)
private Timestamp updatedAt;     
}