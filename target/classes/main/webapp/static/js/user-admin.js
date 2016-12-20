var confirmDialog = new Dialog();
var pagingControl;
var initialSearchResult;

$(function() {
	
	pagingControl = PagingControl.get('usersTable');
	
	if(initialSearchResult) {
		renderSearchResults(initialSearchResult);
	}
	
	$('#searchBy li a').click(function() {
		var $this = $(this);
		var selectedSearchBy = $this.text();
		$('#searchBy span.selected').text(selectedSearchBy);
		$('#searchType').val($this.attr('data-value'));
	});
	
	$('#searchButton').click(function(e) {
		newSearch();
		e.preventDefault();
	});
});

function sort(sortBy) {
	pagingControl.setSort(sortBy);
	userSearch();
}

function newSearch() {
	pagingControl.setPage(0);
	pagingControl.setSort(null);
	userSearch();
}

function userSearch() {	
	
	var pagingData = pagingControl.getPagingData();
	var searchParams = {
		searchString: $('#searchString').val(),
		searchType: $('#searchType').val()
	};
	
	var data = $.extend({}, pagingData, searchParams);
	
	$.ajax({
		url: contextPath + '/admin/users/search',
		data: data,
		beforeSend: function() {
			showSpinner({element: $('#usersTable tbody')});
		},
		success: function(searchResult) {
			renderSearchResults(searchResult);
		}
	});
}

function renderSearchResults(searchResult) {
	pagingControl.updatePaging(searchResult);
	
	if(searchResult && searchResult.users && searchResult.users.length) {
		var html = '';
		var users = searchResult.users;
		
		for(var i = 0; i < users.length; i++) {
			var user = users[i];
			
			if(user.username !== 'admin') {
				html += '<tr id="' + user.id + '">';					
					var name = user.lastName + ', ' + user.firstName;
					var institution = user.institution;
					html += '<td><a href=' + contextPath + '/admin/user/accountSettings/' + user.id + '>' + user.username + '</a></td>';				
					html += '<td>' + name + '</td>';				
					html += '<td>' + institution + '</td>';
					html += '<td>' + user.lastLoginDisplay + '</td>';
					html += '<td>';
					html += '<input type="checkbox" class="activate"';
					
					if(user.enabled) {
						html += ' checked';					
					}
					
					html += '>';				
					html += '</td>';
					
					html += '<td class="icons">';					
						html += '<span class="glyphicon glyphicon-pencil edit" onclick="javascript:editUser(' + user.id + ');"/>';
						html += '<span class="glyphicon glyphicon-remove delete" onclick="javascript:removeUser(' + user.id + ');"/>';					
					html += '</td>';
					
				html += '</tr>';
			}
		}
					
		$('#noResultsMessage').hide();
		$('#usersTable tbody').html(html);
		$('input.activate').bootstrapSwitch({
			onSwitchChange: function(event, state) {
				setEnabled($(this).parents('tr').attr('id'), state);
			},
			size: 'small',
			onColor: 'success',
			offColor: 'danger',
			onText: 'yes',
			offText: 'no'
		});
		$('#usersTable').css('text-align', 'left').show();
	} else {
		$('#usersTable').hide();
		$('#noResultsMessage').show();
	}
};

function setEnabled(userId, enable) {
	$.ajax({
		url: contextPath + '/admin/user/setEnabled',
		data: {
			'userId': userId,
			'enable': enable
		},
		method: 'POST',
		complete: function(response) {
			Messages.removeAll();
			Messages.showResponseMessage(response);					
		}
	});
}

function toggleRole(userId, roleName, grant) {
	$.ajax({
		url: contextPath + '/admin/user/toggleRole',
		data: {
			'userId': userId,
			'roleName': roleName,
			'grant': grant
		},
		method: 'POST',
		complete: function(response) {
			// we only show error messages for this since they're just checking a checkbox
			if(response.status !== 200) {
				Messages.showResponseMessage(response);
			}									
		}
	});
}

function userHasRole(user, role) {
	
	var hasRole = false;
	
	if(user && user.roles && user.roles.length) {
		for(var i = 0; i < user.roles.length; i++) {
			if(user.roles[i].name === role) {
				hasRole = true;
				break;
			}
		}
	}
	
	return hasRole;
}

function editUser(id) {	
	$.ajax({
		url: contextPath + '/admin/user/' + id,
		success: function(user) {
			var $editUserDialog = $('#editUserDialog'); 
			
			$editUserDialog.find('input.role').each(function() {
				var $roleCheckbox = $(this);
				var roleName = $roleCheckbox.attr('data-role-name');
				
				if(userHasRole(user, roleName)) {
					$roleCheckbox.attr('checked', 'checked');
				} else {
					$roleCheckbox.removeAttr('checked');
				}
				
				$roleCheckbox.attr('onclick', 'javascript:toggleRole(' + id + ', \'' + roleName + '\', this.checked);');
			});
			
			var editDialog = new Dialog({
				title: 'Edit User ' + user.username,
				body: $editUserDialog.html(),
				closeButtonLabel: 'Close',
				cssClass: 'wide-dialog'
			});
			
			editDialog.show();
		}
	});		
}

function removeUser(id) {
	var confirmDialog = new Dialog();
	
	confirmDialog.show({
		title: 'Remove User?',
		message: 'Are you sure you want to remove this user?',
		closeButtonLabel: 'No',
		buttons: {
			'Yes': function() {					
				$.ajax({
					url: contextPath + '/admin/user/remove/' + id,
					method: 'POST',
					complete: function(response) {
						$('#' + id).remove();
						confirmDialog.hide();
						Messages.removeAll();
						Messages.showResponseMessage(response);					
					}
				});				
			}
		}
	});		
}