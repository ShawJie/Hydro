package com.sfan.hydro.service.impl;

import java.util.List;


import com.sfan.hydro.dao.UserDao;
import com.sfan.hydro.domain.enumerate.UserGroup;
import com.sfan.hydro.domain.model.User;
import com.sfan.hydro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserDao userDao;
	
	@Override
	public void saveUser(User user) {
		userDao.saveUser(user);
	}

	@Override
	public void updateUser(User user) {
		userDao.updateUser(user);
	}

	@Override
	public void remove(int id) {
		userDao.remove(id);
	}

	@Override
	public User getById(int id) {
		return userDao.getById(id);
	}

	@Override
	public List<User> listUser(User user) {
		return userDao.listUser(user);
	}

	@Override
	public User getUserByGroup(UserGroup userGroup) {
		return userDao.getUserByGroup(userGroup.GetVal());
	}
}
