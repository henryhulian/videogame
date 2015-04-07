package com.gaming.live.uc.usecase;

import com.gaming.live.uc.boundary.Response;
import com.gaming.live.uc.entity.User;

public interface RegisterService {

	User register( User user , Response response);
	
}
