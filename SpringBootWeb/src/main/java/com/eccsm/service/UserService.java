package com.eccsm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.eccsm.auth.AuthConfig;
import com.eccsm.auth.JwtTokenUtil;
import com.eccsm.model.PasswordResetToken;
import com.eccsm.model.User;
import com.eccsm.repository.IPasswordResetToken;
import com.eccsm.repository.IUser;
import com.eccsm.repository.IUserCustom;

@Service
@Transactional
public class UserService implements IUserCustom {

	@Autowired
	IUser repository;

	@Autowired
	IPasswordResetToken tokenRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private AuthConfig authConfig;

	public List<User> getAllUsers() {
		List<User> userList = repository.findAll();

		if (userList.size() > 0) {
			return userList;
		} else {
			return new ArrayList<User>();
		}
	}

	public User getUserById(Long id) throws NoSuchElementException {
		Optional<User> user = repository.findById(id);

		if (user.isPresent()) {
			return user.get();
		} else {
			throw new NoSuchElementException("User not exist for given ID");
		}
	}

	public User createUser(User entity) throws NoSuchElementException {

		entity.setPassword(authConfig.passwordEncoder().encode(entity.getPassword()));
		entity = repository.save(entity);

		return entity;

	}

	public User updateUser(long id, User entity) throws NoSuchElementException {

		Optional<User> user = repository.findById(id);

		if (user.isPresent()) {
			User newEntity = user.get();
			newEntity.setUsername(entity.getUsername());
			newEntity.setPassword(entity.getPassword());
			newEntity.setEmail(entity.getEmail());
			newEntity.setFullname(entity.getFullname());
			newEntity.setIsBlocked(entity.getIsBlocked());
			newEntity.setRoleId(entity.getRoleId());
			newEntity = repository.save(newEntity);

			return newEntity;
		}

		else {
			throw new NoSuchElementException("User not exist for given ID");
		}

	}

	public void deleteUserById(Long id) throws NoSuchElementException {
		Optional<User> User = repository.findById(id);

		if (User.isPresent()) {
			repository.deleteById(id);
		} else {
			throw new NoSuchElementException("User not exist for given ID");
		}
	}

	@Override
	public User findUserByUsername(String username) {

		List<User> userList = repository.findAll();

		for (User u : userList) {
			if (u.getUsername().equals(username)) {
				return u;
			}
		}

		return null;

	}

	public ResponseEntity<?> login(User user) {
		User getted = findUserByUsername(user.getUsername());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		if (encoder.matches(user.getPassword(), getted.getPassword())) {
			final String token = jwtTokenUtil.generateToken(getted);
			List<String> userData = new ArrayList<String>();
			userData.add(token);
			userData.add(getted.getUsername());
			userData.add(getted.getEmail());
			userData.add(String.valueOf(getted.getId()));
			userData.add(String.valueOf(getted.getRoleId()));
			userData.add(getted.getFullname());
			return new ResponseEntity<Object>(userData.listIterator(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}

	}

	public User updatePassword(long id, String password) throws NoSuchElementException {

		Optional<User> user = repository.findById(id);

		if (user.isPresent()) {
			User newEntity = user.get();
			// newEntity.setUsername(user.get().getUsername());
			newEntity.setPassword(password);
			// newEntity.setEmail(user.get().getEmail());
			// newEntity.setFullname(user.get().getFullname());
			// newEntity.setIsBlocked(user.get().getIsBlocked());
			// newEntity.setRoleId(user.get().getRoleId());
			// newEntity = repository.save(newEntity);

			return newEntity;
		}

		else {
			throw new NoSuchElementException("User not exist for given ID");
		}

	}

	public void PasswordReset(String token, String password) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		PasswordResetToken myToken = tokenRepository.findByToken(token);
		User user = myToken.getUser();
		String updatedPassword = passwordEncoder.encode(password);
		updatePassword(user.getId(), updatedPassword);
		// tokenRepository.deleteById(token.get);
	}

}
