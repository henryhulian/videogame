package com.gaming.live.common.service.impl;

import com.gaming.live.common.dao.SessionDao;
import com.gaming.live.common.entity.Session;
import com.gaming.live.common.service.SessionService;
import com.gaming.live.common.transaction.TransactionManager;
import com.google.common.cache.Cache;

public class SessionServiceImpl implements SessionService{
	
	private Cache<String, Session> sessionCache;
	private TransactionManager transactionManager;
	private SessionDao sessionDao;
	
	public SessionServiceImpl(Cache<String, Session> sessionCache,TransactionManager transactionManager,SessionDao sessionDao) {
		this.sessionCache=sessionCache;
		this.transactionManager=transactionManager;
		this.sessionDao=sessionDao;
	}

	@Override
	public Session createSession( Session session ) {
		sessionCache.put(session.getToken(), session);
		Session sessionC=transactionManager.doInTransaction(()->{
			sessionDao.createSession(session);
			return session;
		});
		return sessionC;
	}

	@Override
	public Session findSessionByToken(String token) {
		return sessionCache.getIfPresent(token);
	}

	public Cache<String, Session> getSessionCache() {
		return sessionCache;
	}

	public void setSessionCache(Cache<String, Session> sessionCache) {
		this.sessionCache = sessionCache;
	}

}
