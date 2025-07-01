package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.BuyLog;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;

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

	public void AddAdmin() {
		userRepository.AddAdmin();
	}


	public void insertLog(String username, Product p) {
		userRepository.insertLog(username, p);

	}

	public List<BuyLog> findByLog(String username) {
		return userRepository.findByLog(username);

	}
	
	public void updateuserAddress(User user){
		userRepository.updateAll(user);
	}
	
	//ポイント 清水追加
		public void point(User user) {
			userRepository.pointUpdate(user);
		}
		
		public void SendCartMessage(String mailaddress,String about,List<Product> list) {
			userRepository.SendCartMessage(mailaddress,about,list);
		}
		
		public void SendRegisterMessage(String mailaddress,String about, String message) {
			userRepository.SendRegisterMessage(mailaddress, about, message);
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

	//////ここから　0618中村追加　通報
			//alert_review　に　通報者と通報されたユーザ　の組み合わせがあるか確認する
			public int Search_alert_count(String user_send_alert, String username) {
				return userRepository.Search_alert_countRepo(user_send_alert, username);
			}
			
			public void ReviewAlertService(int user_alert_count, String username) {
				userRepository.ReviewAlertRepo(user_alert_count, username);
			}
	//////ここまで　0618中村追加　通報
}
