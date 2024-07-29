package com.thantrick.springboot_jwt_security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="address_tbl")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(length=30, nullable=false)
    private String addressId;

    @Column(length=15, nullable=false)
    private String city;

    @Column(length=15, nullable=false)
    private String country;

    @Column(length=100, nullable=false)
    private String streetName;

    @Column(length=7, nullable=false)
    private String postalCode;

    @Column(length=10, nullable=false)
    private String type;

    @ManyToOne
    @JoinColumn(name="users_id")
    private UserEntity userDetails;
}
