package com.eccsm.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccsm.model.Product;
import com.eccsm.model.ProductImages;
import com.eccsm.repository.IProduct;
import com.eccsm.repository.IProductImages;

@Service
@Transactional
public class ProductService {

	@Autowired
	IProduct repository;

	@Autowired
	IProductImages imageRepository;

	public List<Product> getAllProducts() {
		List<Product> productList = repository.findAll();

		if (productList.size() > 0) {
			return productList;
		} else {
			return new ArrayList<Product>();
		}
	}

	public Product getProductById(Long id) throws NoSuchElementException {
		Optional<Product> product = repository.findById(id);

		if (product.isPresent()) {
			return product.get();
		} else {
			throw new NoSuchElementException("Product not exist for given ID");
		}
	}

	public Product createProduct(Product entity) throws NoSuchElementException {

		entity = repository.save(entity);

		return entity;

	}

	public Product updateProduct(long id, Product entity) throws NoSuchElementException {

		Optional<Product> product = repository.findById(id);

		if (product.isPresent()) {
			Product newEntity = product.get();
			newEntity.setName(entity.getName());
			newEntity.setDescription(entity.getDescription());
			newEntity.setCategoryId(entity.getCategoryId());
			newEntity = repository.save(newEntity);

			return newEntity;
		}

		else {
			throw new NoSuchElementException("Product not exist for given ID");
		}

	}

	public void deleteProductById(Long id) throws NoSuchElementException {
		Optional<Product> product = repository.findById(id);

		if (product.isPresent()) {
			repository.deleteById(id);
		} else {
			throw new NoSuchElementException("Product not exist for given ID");
		}
	}

	public ProductImages saveImage(ProductImages file) throws IOException {

		imageRepository.save(file);

		return file;
	}

	public void deleteImage(Long id) throws NoSuchElementException {

		Optional<ProductImages> image = imageRepository.findById(id);

		if (image.isPresent()) {
			imageRepository.deleteById(id);
		} else {
			throw new NoSuchElementException("Image not exist for given ID");
		}
	}
	
	public ProductImages updateImage(long id, ProductImages file) throws NoSuchElementException {

		Optional<ProductImages> image = imageRepository.findById(id);

		if (image.isPresent()) {
			ProductImages newEntity = image.get();
			newEntity.setFileName(file.getFileName());
			newEntity.setFileDownloadUri(file.getFileDownloadUri());
			newEntity.setProduct(file.getProduct());
			newEntity.setSize(file.getSize());
			newEntity.setType(file.getType());
			newEntity = imageRepository.save(newEntity);

			return newEntity;
		}

		else {
			throw new NoSuchElementException("Product not exist for given ID");
		}

	}

	public ProductImages getImageById(Long id) throws NoSuchElementException {

		Optional<ProductImages> img = imageRepository.findById(id);

		if (img.isPresent()) {
			return img.get();
		} else {
			throw new NoSuchElementException("Image not exist for given ID");
		}
	}

	public List<ProductImages> getAllImages() throws NoSuchElementException {

		List<ProductImages> productImagesList = imageRepository.findAll();

		if (productImagesList.size() > 0) {
			return productImagesList;
		} else {
			return new ArrayList<ProductImages>();
		}
	}

}
