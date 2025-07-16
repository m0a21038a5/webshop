package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public int save(User user) {
		return userRepository.save(user);
	}

	public boolean userExists(String username) {
		return userRepository.findByUsername(username) != null;
	}

	public int userUpdate(User user) {
		return userRepository.update(user);
	}

	

	public void updateuserAddress(User user) {
		userRepository.updateAll(user);
	}

	//ポイント 清水追加
	public void point(User user) {
		userRepository.pointUpdate(user);
	}

	public List<User> findAllUser() {
		return userRepository.findAllUser();
	}

	public void updateEnabled(User user) {
		userRepository.updateEnabled(user);
	}

	public void updateRole(User user) {
		userRepository.updateRole(user);
	}

}
