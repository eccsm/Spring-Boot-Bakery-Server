package com.eccsm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eccsm.model.Category;
@Repository
public interface ICategory extends JpaRepository<Category, Long> {

}
