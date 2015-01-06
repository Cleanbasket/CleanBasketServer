package com.bridge4biz.wash.util;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class AuthorityUtils {

	public Integer getAuthority(Collection<? extends GrantedAuthority> authorities) {
		String authority = ((GrantedAuthority) authorities.toArray()[0]).getAuthority();
		if (authority.equals("ROLE_ADMIN")) {
			return Constant.ROLE_ADMIN;
		} else if (authority.equals("ROLE_DELIVERER")) {
			return Constant.ROLE_DELIVERER;
		} else if (authority.equals("ROLE_MEMBER")) {
			return Constant.ROLE_MEMBER;
		}
		return null;
	}

}
