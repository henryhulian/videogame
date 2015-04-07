package com.gaming.live.uc.usecase;

import com.gaming.live.uc.boundary.Response;
import com.gaming.live.uc.entity.User;

public interface LoginService {
	User login( String userName , String password , final Response response  );
}
