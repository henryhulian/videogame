package com.gaming.live.common.service;

import com.gaming.live.common.entity.Session;

public interface SessionService {

	Session createSession( Session session );
	Session findSessionByToken(String token);
}
