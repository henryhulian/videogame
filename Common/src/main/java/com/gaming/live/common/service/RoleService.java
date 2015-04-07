package com.gaming.live.common.service;

import java.util.List;

public interface RoleService {
	boolean isUserAllowed( Integer commandId );
	boolean isUserAllowed( Integer commandId , List<String> roles);
	void rolesAllowed( Integer commandId , List<String> roles);
}
