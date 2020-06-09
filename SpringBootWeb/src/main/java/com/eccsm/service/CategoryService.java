package com.eccsm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eccsm.model.Category;
import com.eccsm.model.Product;
import com.eccsm.repository.ICategory;

@Service
@Transactional
public class CategoryService {

	@Autowired
	ICategory repository;

	@Autowired
	ProductService productService;

	public List<Category> getAllCategories() {
		List<Category> CategoryList = repository.findAll();

		if (CategoryList.size() > 0) {
			return CategoryList;
		} else {
			return new ArrayList<Category>();
		}
	}

	public Category getCategoryById(Long id) throws NoSuchElementException {
		Optional<Category> category = repository.findById(id);

		if (category.isPresent()) {
			return category.get();
		} else {
			throw new NoSuchElementException("Category not exist for given ID");
		}
	}

	public Category createCategory(Category entity) throws NoSuchElementException {

		entity = repository.save(entity);

		return entity;

	}

	public Category updateCategory(long id, Category entity) throws NoSuchElementException {

		Optional<Category> category = repository.findById(id);

		if (category.isPresent()) {
			Category newEntity = category.get();
			newEntity.setName(entity.getName());
			newEntity = repository.save(newEntity);

			return newEntity;
		}

		else {
			throw new NoSuchElementException("Category not exist for given ID");
		}

	}

	public void deleteCategoryById(Long id) throws NoSuchElementException {
		Optional<Category> category = repository.findById(id);

		if (category.isPresent()) {

			List<Product> productList = productService.getAllProducts();

			for (Product p : productList) {

				if (p.getCategoryId() == id) {

					productService.deleteImage(p.getId());

				} else {
				}
			}

			repository.deleteById(id);
		} else {
			throw new NoSuchElementException("Category not exist for given ID");
		}
	}
}
