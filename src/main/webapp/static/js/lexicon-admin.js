function getAdminActionsHtml(term) {
	var html = '';
	var $selectedLetter = $('span.searchLetter.selected');
	var selectedLetter = $selectedLetter.length ? $selectedLetter.text() : '';
	var editUrl = contextPath + '/lexicon/admin/edit/' + term.id + '?searchText=' + $('#searchText').val() + '&startsWithLetter=' + selectedLetter + '&selectedPage=' + pagingControl.getPagingData().page;
	
	html += '<td class="col-md-2 actions">';									
		html += '<a href="' + editUrl + '" class="btn btn-primary" role="button">Edit</a>';
		html += '<a onclick="javascript:removeTerm(this);" class="btn btn-danger" role="button">Delete</a>';
	html += '</td>';
	
	return html;
}