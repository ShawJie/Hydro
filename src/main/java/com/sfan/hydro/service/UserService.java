package com.sfan.hydro.service;

import java.util.List;

import com.sfan.hydro.domain.enumerate.UserGroup;
import com.sfan.hydro.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
	
	void saveUser(User user);
	void updateUser(User user);
	void remove(int id);
	User getById(int id);
	List<User> listUser(User user);
	User getUserByGroup(UserGroup userGroup);
}
