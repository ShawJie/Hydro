package com.sfan.hydro.dao;

import java.util.List;

import com.sfan.hydro.domain.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
	void saveUser(User user);
	void updateUser(User user);
	void remove(int Id);
	User getById(int Id);
	List<User> listUser(User user);
	User getUserByGroup(String userGroup);
}
