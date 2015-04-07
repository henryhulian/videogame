package com.gaming.live.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gaming.live.common.service.RoleService;

public class RoleServiceImpl implements RoleService{
	
	private Map<Integer, List<String>> roleMap=new HashMap<Integer, List<String>>();

	@Override
	public boolean isUserAllowed( Integer commandId ) {
		
		List<String> roles = roleMap.get(commandId);
		// 如果未设置权限，不做权限验证
		if (roles == null || roles.size() == 0) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean isUserAllowed( Integer commandId , List<String> roles) {
		
		// 验证是否有权限访问
		if(roles.stream().anyMatch(roleHas->{
			return roleMap.get(commandId).stream().anyMatch( roleAllowed->{
				if( roleHas.equals(roleAllowed)){
					return true;
				}
				return false;
			});
		})){
			return true;
		}
		return false;
	}
	
	@Override
	public void rolesAllowed(Integer commandId, List<String> roles) {
		roleMap.put(commandId, roles);
	}

	public Map<Integer, List<String>> getRoleMap() {
		return roleMap;
	}

	public void setRoleMap(Map<Integer, List<String>> roleMap) {
		this.roleMap = roleMap;
	}

}
