package com.gaming.live.uc.dao;

import com.gaming.live.uc.entity.User;

public interface UserDao {

	User createUser(User user);

	User findUserByUserName(String userName);
}
