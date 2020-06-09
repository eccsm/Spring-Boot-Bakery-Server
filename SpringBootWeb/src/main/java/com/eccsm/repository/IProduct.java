package com.eccsm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eccsm.model.Product;

@Repository
public interface IProduct extends JpaRepository<Product, Long> {

}
