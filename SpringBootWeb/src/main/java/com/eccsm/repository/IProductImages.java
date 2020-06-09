package com.eccsm.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eccsm.model.ProductImages;

@Repository
public interface IProductImages extends JpaRepository<ProductImages, Long> {

}

