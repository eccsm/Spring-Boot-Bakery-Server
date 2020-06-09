package com.eccsm.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.eccsm.mail.CustomMailSender;
import com.eccsm.model.Category;
import com.eccsm.model.PasswordResetToken;
import com.eccsm.model.Product;
import com.eccsm.model.ProductImages;
import com.eccsm.model.Role;
import com.eccsm.model.User;
import com.eccsm.payload.UploadFileResponse;
import com.eccsm.repository.IPasswordResetToken;
import com.eccsm.service.CategoryService;
import com.eccsm.service.FileStorageService;
import com.eccsm.service.ProductService;
import com.eccsm.service.RoleService;
import com.eccsm.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(maxAge = 3600)
@RestController
public class APIController {

	@Autowired
	UserService userService;

	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	RoleService roleService;

	@Autowired
	private IPasswordResetToken tokenRepository;

	@Autowired
	private CustomMailSender sender;

	@Autowired
	private FileStorageService fileStorageService;

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> list = productService.getAllProducts();

		return new ResponseEntity<List<Product>>(list, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) throws NoSuchElementException {
		Product product = productService.getProductById(id);

		return new ResponseEntity<Product>(product, new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("/product/create")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) throws NoSuchElementException {
		Product created = productService.createProduct(product);
		return new ResponseEntity<Product>(created, new HttpHeaders(), HttpStatus.OK);
	}

	@PutMapping("/product/{id}")
	public ResponseEntity<Product> UpdateProduct(@PathVariable("id") Long id, @RequestBody Product product)
			throws NoSuchElementException {
		Product updated = productService.updateProduct(id, product);
		return new ResponseEntity<Product>(updated, new HttpHeaders(), HttpStatus.OK);
	}

	@DeleteMapping("/product/{id}")
	public HttpStatus deleteProductById(@PathVariable("id") Long id) throws NoSuchElementException {
		productService.deleteProductById(id);
		return HttpStatus.FORBIDDEN;
	}

	@GetMapping("/categories")
	public ResponseEntity<List<Category>> getAllCategories() {
		List<Category> list = categoryService.getAllCategories();

		return new ResponseEntity<List<Category>>(list, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/category/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id) throws NoSuchElementException {
		Category category = categoryService.getCategoryById(id);

		return new ResponseEntity<Category>(category, new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("/category/create")
	public ResponseEntity<Category> createCategory(@RequestBody Category category) throws NoSuchElementException {
		Category created = categoryService.createCategory(category);
		return new ResponseEntity<Category>(created, new HttpHeaders(), HttpStatus.OK);
	}

	@PutMapping("/category/{id}")
	public ResponseEntity<Category> UpdateCategory(@PathVariable("id") Long id, @RequestBody Category category)
			throws NoSuchElementException {
		Category updated = categoryService.updateCategory(id, category);
		return new ResponseEntity<Category>(updated, new HttpHeaders(), HttpStatus.OK);
	}

	@DeleteMapping("/category/{id}")
	public HttpStatus deleteCategoryById(@PathVariable("id") Long id) throws NoSuchElementException {
		categoryService.deleteCategoryById(id);
		return HttpStatus.FORBIDDEN;
	}

	@GetMapping("/images")
	public ResponseEntity<List<ProductImages>> getAllImages() {

		List<ProductImages> list = productService.getAllImages();

		return new ResponseEntity<List<ProductImages>>(list, new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("/image/{id}")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id) {
		String fileName = fileStorageService.storeFile(file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(fileName)
				.toUriString();

		ProductImages img = new ProductImages(fileName, fileDownloadUri, file.getContentType(), file.getSize(),
				productService.getProductById(id));

		try {
			productService.saveImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}

	@GetMapping("/image/{name}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("name") String name, HttpServletRequest request)
			throws Exception {

		Resource resource = fileStorageService.loadFileAsResource(name);

		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			throw new IOException("Could not determine file type.");
		}

		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);

	}

	@PutMapping("/image/{id}")
	public ResponseEntity<ProductImages> UpdateImage(@PathVariable("id") Long id, MultipartFile file)
			throws NoSuchElementException {
		String fileName = fileStorageService.storeFile(file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(fileName)
				.toUriString();
		ProductImages img = new ProductImages(fileName, fileDownloadUri, file.getContentType(), file.getSize(),
				productService.getProductById(id));
		ProductImages updated = productService.updateImage(id, img);
		return new ResponseEntity<ProductImages>(updated, new HttpHeaders(), HttpStatus.OK);
	}

	@DeleteMapping("/image/{id}")
	public HttpStatus deleteImage(@PathVariable("id") Long id) throws NoSuchElementException {
		productService.deleteImage(id);
		return HttpStatus.FORBIDDEN;
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> list = userService.getAllUsers();

		return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Long id) throws NoSuchElementException {
		User user = userService.getUserById(id);

		return new ResponseEntity<User>(user, new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("/user")
	public ResponseEntity<User> createUser(@RequestBody User user) throws NoSuchElementException {
		User created = userService.createUser(user);
		return new ResponseEntity<User>(created, new HttpHeaders(), HttpStatus.OK);
	}

	@PutMapping("/user/{id}")
	public ResponseEntity<User> UpdateUser(@PathVariable("id") Long id, @RequestBody User user)
			throws NoSuchElementException {
		User updated = userService.updateUser(id, user);
		return new ResponseEntity<User>(updated, new HttpHeaders(), HttpStatus.OK);
	}

	@DeleteMapping("/user/{id}")
	public HttpStatus deleteUserById(@PathVariable("id") Long id) throws NoSuchElementException {
		userService.deleteUserById(id);
		return HttpStatus.FORBIDDEN;
	}

	@GetMapping("/roles")
	public ResponseEntity<List<Role>> getAllRoles() {
		List<Role> list = roleService.getAllRoles();

		return new ResponseEntity<List<Role>>(list, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/role/{id}")
	public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id) throws NoSuchElementException {
		Role user = roleService.getRoleById(id);

		return new ResponseEntity<Role>(user, new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("/role")
	public ResponseEntity<Role> createRole(@RequestBody Role role) throws NoSuchElementException {
		Role created = roleService.createRole(role);
		return new ResponseEntity<Role>(created, new HttpHeaders(), HttpStatus.OK);
	}

	@PutMapping("/role/{id}")
	public ResponseEntity<Role> UpdateRole(@PathVariable("id") Long id, @RequestBody Role role)
			throws NoSuchElementException {
		Role updated = roleService.updateRole(id, role);
		return new ResponseEntity<Role>(updated, new HttpHeaders(), HttpStatus.OK);
	}

	@DeleteMapping("/role/{id}")
	public HttpStatus deleteRoleById(@PathVariable("id") Long id) throws NoSuchElementException {
		roleService.deleteRoleById(id);
		return HttpStatus.FORBIDDEN;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) {

		return userService.login(user);

	}

	@PostMapping("/contact")
	public HttpStatus contactSubmit(@RequestBody String body) throws JsonMappingException, JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();

		Map<?, ?> map = objectMapper.readValue(body, Map.class);

		sender.sendEmail(map.get("name").toString(), map.get("email").toString(), map.get("message").toString());
				
		return HttpStatus.OK;

	}

	@PostMapping("/forgotpassword")
	public HttpStatus processForgotPasswordForm(@RequestBody String form)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		Map<?, ?> map = objectMapper.readValue(form, Map.class);

		User user = userService.findUserByUsername(map.get("form").toString());

		if (user != null) {

			PasswordResetToken token = new PasswordResetToken();
			token.setToken(UUID.randomUUID().toString());
			token.setUser(user);
			token.setExpiryDate(30);
			tokenRepository.save(token);

			sender.sendPasswordEmail(token.getToken(), user.getEmail());
			
			return HttpStatus.OK;
		} else {

			System.out.println("We could not find an account for that username.");
			
			return HttpStatus.FORBIDDEN;
		}
	}

	@GetMapping("/resetpassword")
	public String displayResetPasswordPage(@RequestParam(required = false) String token, Model model) {

		PasswordResetToken resetToken = tokenRepository.findByToken(token);
		if (resetToken == null) {
			model.addAttribute("error", "Could not find password reset token.");
		} else if (resetToken.isExpired()) {
			model.addAttribute("error", "Token has expired, please request a new password reset.");
		} else {
			model.addAttribute("token", resetToken.getToken());
		}

		return "resetpassword";
	}

	@PostMapping("/resetpassword")
	public HttpStatus handlePasswordReset(@RequestBody String body) throws JsonMappingException, JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();

		Map<?, ?> map = objectMapper.readValue(body, Map.class);

		userService.PasswordReset(map.get("token").toString(), map.get("password").toString());
		
		return HttpStatus.OK;

	}

}
