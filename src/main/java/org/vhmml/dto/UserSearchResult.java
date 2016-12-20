package org.vhmml.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.vhmml.entity.User;

public class UserSearchResult extends SearchResult {
	private List<User> users = new ArrayList<User>();
	
	public UserSearchResult() {
		super();
	}

	public UserSearchResult(Page<User> searchResult) {

		this.users = searchResult.getContent();
		setPageNumber(searchResult.getNumber());
		setPageSize(searchResult.getSize());
		setTotalElements(searchResult.getTotalElements());
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
