var confirmDialog = new Dialog();
var pagingControl;
var isLexiconAdmin;
var initialSortBy;

$(function() {
	
	pagingControl = PagingControl.get('lexiconTable');
	
	$('#searchButton').click(function() {
		textSearch();
	});
	
	$('#clearSearchButton').click(function() {
		$('#searchForm').trigger('reset');
		initialSearchText = null;
		initalStartsWithLetter = null;
		selectedPage = null;
		initSearch();
	});
	
	$('span.searchLetter').click(function() {
		pagingControl.setPage(0);
		pagingControl.setSort(initialSortBy);
		$('#searchText').val('');
		refreshSelectedLetter(this);
		lexiconSearch();			
	});
	
	if(initialSortBy) {
		pagingControl.setSort(initialSortBy);
	}
	
	initCorrectionsDialog();
	initSearch();			
});

function textSearch(selectedPage) {
	$('span.searchLetter.selected').removeClass('selected');
	var page = selectedPage ? +selectedPage : 0;
	pagingControl.setPage(page);
	pagingControl.setSort(initialSortBy);
	lexiconSearch();
}

function sort(sortBy) {
	pagingControl.setSort(sortBy);
	lexiconSearch();
}

// this is in a separate function because it's wired to the "go" button, when the form is submitted and "by letter" search
function lexiconSearch() {
	var $selectedLetter = $('span.searchLetter.selected');
	var startsWithSearch = $selectedLetter && $selectedLetter.length;
	var searchText = $('#searchText').val();
	var url = startsWithSearch ? 'startsWith/' + $selectedLetter.text() : 'search/' + searchText;
	
	if(!startsWithSearch && (!searchText || searchText.length < 2)) {
		$('#lexiconTable, #noResultsMessage').hide();
		$('#invalidSearchMessage').show();
	} else {
		var pagingData = pagingControl.getPagingData();
		
		$.ajax({
			url: contextPath + '/lexicon/' + url,
			data: pagingData,
			success: function(searchResult) {				
				refreshLexiconTable(searchResult, $selectedLetter);
			}
		});	
	}	
}

function refreshLexiconTable(searchResult, $selectedLetter) {
	renderSearchResults(searchResult);						
	pagingControl.updatePaging(searchResult);
	
	$('#searchText').focus();
}

// this is a callback method that gets called when the paging controls are used or a search letter is clicked on
function refreshSelectedLetter(clickedButton) {
	var $clickedButton = $(clickedButton);
	var $selectedLetter = $('span.searchLetter.selected');		
	
	if($clickedButton.hasClass('next')) {
		var $nextLetter = $selectedLetter.next('span.searchLetter');
		$selectedLetter = $nextLetter.length > 0 ? $nextLetter : $selectedLetter.parent('div.searchLetterRow').next('div.searchLetterRow').children('span.searchLetter').first();  
	} else if($clickedButton.hasClass('previous')) {
		var $previousLetter = $selectedLetter.prev('span.searchLetter');
		$selectedLetter = $previousLetter.length > 0 ? $previousLetter : $selectedLetter.parent('div.searchLetterRow').prev('div.searchLetterRow').children('span.searchLetter').last();
	} else {
		$selectedLetter = $clickedButton;
	}
	
	$('span.searchLetter.selected').removeClass('selected');
	
	$selectedLetter.addClass('selected');
}

function renderSearchResults(searchResult) {
	if(searchResult && searchResult.terms && searchResult.terms.length) {		
		var terms = searchResult.terms;
		var html = getTableHeaderHtml();
		html += '<tbody>';
		
		for(var i = 0; i < terms.length; i++) {
			var term = terms[i];			
			
			html += '<tr id="' + term.id +'" data-term="' + term.term + '">';
				
				html += '<td class="col-md-2">';
					html += '<a href="javascript:viewTerm(\'' + term.id + '\');">' + term.term + '</a>';
				
				/* Removing highlighted second row for now since most of the time it's just a duplicate of the highlighted text in the first row.
				 * What we really need here is to only have the second line if the text that matched the search is in a field other than the
				 * term or definition, such as one of the other languages. We don't have time for that right now so we're just commenting this out.
				if(term.highlightFields) {
					var highlightedText = term.highlightFields[Object.keys(term.highlightFields)[0]];
					
					// hack to work around the fact that CKEditor sometimes saves paragraph tags without an end tag, which causes 
					// browsers to insert and end tag and an extra <br> tag which breaks the vertical alignment of the highlighted text row
					if(highlightedText.startsWith('<p>') && !highlightedText.endsWith('</p>')) {
						highlightedText += '</p>';
					}
					html += '<br/><span class="table-row-caption">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
					html += highlightedText;
					html += '</span><br/>';
				}
				*/
				
				html += '</td>';
				html += '<td class="col-md-8">' + term.displayDefinition + '</td>';
				
				// if they're logged in as an admin, the admin js will have been loaded which is where the getAdminActionsHtml function resides
				if(isLexiconAdmin) {
					html += getAdminActionsHtml(term);
				}					
			html += '</tr>';			
		}
					
		html += '</tbody>';
		
		$('#noResultsMessage, #invalidSearchMessage').hide();
		$('#lexiconTable').html(html);
		$('#lexiconTable').show();
	} else {
		$('#lexiconTable, #invalidSearchMessage').hide();
		$('#noResultsMessage').show();
	}
}

function getTableHeaderHtml() {
	var pagingData = pagingControl.getPagingData();
	var sortIconName = pagingData.direction === 'DESC' ? 'down' : 'up';
	var sortIcon = '&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-' + sortIconName + '"/>';	
	var html = 
		'<caption class="small text-left">' +
			'<em>Click a term to see the full definition</em>' + 
			'<a href="javascript:showCorrectionsDialog();" class="right"><img src="' + contextPath + '/static/img/glyphicons-conversation2.png" width="20" alt="" />&nbsp;Corrections or additions?</a>' +
		'</caption>' +
		'<thead>' +
			'<tr>' +
				'<th class="col-md-2"><a href="javascript:sort(\'term\');">TERMS' + (pagingData.sortBy === 'term' ? sortIcon : '') + '</a></th>' +
				'<th class="col-md-8">DEFINITION</th>';	
				
				if(isLexiconAdmin) {
					html += '<th class="col-md-2 text-center">ACTIONS</th>';
				}
				
			html += '</tr>';
		html += '</thead>';	
		
	return html;
}

function viewTerm(id) {
	var $selectedLetter = $('span.searchLetter.selected');
	var selectedLetter = $selectedLetter && $selectedLetter.length ? $selectedLetter.text() : '';
	var searchText = $('#searchText').val();
	var selectedPage = pagingControl.getPagingData().page;			
	var termUrl = contextPath + '/lexicon/definition/' + id + '?searchText=' + searchText + '&startsWithLetter=' + selectedLetter + '&selectedPage=' + selectedPage;
	window.location.replace(termUrl);
}

function removeTerm(button) {
	var $row = $(button).parents('tr');
	var id = $row.attr('id');
	var term = $row.attr('data-term');
	
	confirmDialog.show({
		title: 'Delete ' + term + '?',
		message: 'Are you sure you want to delete ' + term + ' from Lexicon?',
		buttons: {
			'Delete': function() {
				$.ajax({
					url: contextPath + '/lexicon/admin/remove/' + id,
					method: 'PUT',
					success: function() {
						$row.remove();
					},
					complete: function(response, textStatus) {
						Messages.showResponseMessage(response);
						confirmDialog.hide();
					}					
				});
			}
		}
	});		
}

function initSearch() {
	// initialSearchText, initalStartsWithLetter & selectedPage are initialized on home.jsp with EL expressions, they 
	// are the last known state of the home page that is used to return the user to the page the way they last saw it when
	// they are finished doing something like editing a lexicon term
	if(initialSearchText) {
		$('#searchText').val(initialSearchText);		
		textSearch(selectedPage);
	} else if(initialStartsWithLetter) {
		$('div.searchLetters span.searchLetter').each(function() {
			var $letter = $(this);
			if($letter.text() == initialStartsWithLetter) {
				$letter.click();
				return;
			}
		});
	} else {
		// click on the default letter to invoke a default search
		$('span.searchLetter.default').click();
	}
}