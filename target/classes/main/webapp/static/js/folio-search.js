var searchHitTemplate;
var searchHitHighlightTemplate;
var searchHitToggleTemplate;
var editObjectControlsTemplate;
var overviewTabTemplate;
var overviewTemplate;
var descriptionTemplate;
var transcriptionTemplate;
var searchHitButtonsTemplate;
var noSearchResultsTemplate;

$(function() {
	noSearchResultsTemplate = Handlebars.compile($('#no-search-results-template').html());
	searchHitTemplate = Handlebars.compile($('#search-hit-template').html());
	searchHitHighlightTemplate = Handlebars.compile($('#search-hit-highlight-template').html());
	searchHitToggleTemplate = Handlebars.compile($('#search-hit-toggle-template').html());		
	
	// edit controls that are used on each tab
	editObjectControlsTemplate = Handlebars.registerPartial('editObjectControlsTemplate', $('#edit-object-controls-template').html());
	
	// these are the tab content templates
	overviewTemplate = Handlebars.registerPartial('overviewTemplate', $('#overview-template').html());
	descriptionTemplate = Handlebars.registerPartial('descriptionTemplate', $('#description-template').html());
	transcriptionTemplate = Handlebars.registerPartial('transcriptionTemplate', $('#transcription-template').html());
	
	// the "tab" templates just combine the edit controls and the tab content template
	overviewTabTemplate = Handlebars.compile($('#overview-tab-template').html());
	descriptionTemplate = Handlebars.compile($('#description-tab-template').html());
	transcriptionTemplate = Handlebars.compile($('#transcription-tab-template').html());
	
	searchHitButtonsTemplate = Handlebars.compile($('#search-hit-buttons-template').html());
	
	if(selectedObjectJson) {
		viewImages(selectedObjectJson.id, 'UNREGISTERED');
	}
});

function initSearch(beginDate, endDate) {		
		
	// init page-scope paging control
	pagingControl = PagingControl.get('searchResults');
	$('#searchPanel select.bootstrap-select').selectpicker();	
	initDateSlider(beginDate, endDate);				
	
	$('button.searchButton').click(function() {
		newSearch();
	});
			
	$('span.menuToggleIcon, div.searchToggleBar').click(function() {
		var $selectedMenu = $('#' + $(this).attr('data-menu'));
		slideMenu($selectedMenu);
	});	
}

function getSearchTerms() {
	var $searchForm = $('#searchPanel form:visible');
	var currentYear = new Date().getFullYear();		
	var dateRange = $($searchForm.find('input.slider')[0]).val();		
	var dateRangeArray = dateRange.split(',');
	var beginDate = dateRange.length > 0 ? +dateRangeArray[0] - 5 : '';
	var endDate = dateRange.length > 0 ? +dateRangeArray[1] + 5 : '';
	
	if(beginDate <= 1 && endDate >= currentYear) {
		beginDate = '';
		endDate = '';	
	}

	return {			
		keyword: $searchForm.find('textarea.keyword').val(),				
		beginDate: beginDate,
		endDate: endDate,		
		active: $searchForm.find('input.pendingOnly').is(':checked') ? false : '',		
		language: getMultiSelectVal($searchForm, 'select.language'),
		script: getMultiSelectVal($searchForm, 'select.script'),
		writingSystem: getMultiSelectVal($searchForm, 'select.writingSystem')			
	};
}

function refreshMetaData(object) {		
	window.scrollTo(0, 0);
	var $metaDataPanel = $('#metaDataPanel');
	$metaDataPanel.find('li.overview a').click();
	
	$('#overview').html(overviewTabTemplate(object));
	$('#description').html(descriptionTemplate(object));
	$('#transcription').html(transcriptionTemplate(object));	
}

function showImageThumbnail(object) {
	$('#searchResultsWrapper').hide();
	
	var imageWidth = $('.bodyContent').width() * .8;	
	var thumbnailUrl = contextPath + '/image/thumbnail/FOLIO/' + object.folioObjectNumber + '/' + object.iconName + '/' + imageWidth;
	var thumbnailHtml = '<img class="thumb responsive viewImagesLink" src="' + thumbnailUrl + '"/>';		

	// hide mirador viewer
	$('#viewer').hide();
	var $thumbViewer = $('#thumbnail-viewer');
	
	showSpinner({element: $thumbViewer});
	$thumbViewer.show();
		
	preloadImage(thumbnailUrl, function() {
		$thumbViewer.html(thumbnailHtml);
	});			
}

function getSearchHitHtml(hit, fieldDisplayNames) {
	var $searchHit = $('<div class="searchHit"></div>');
	$searchHit.append(searchHitTemplate(hit));
	
	if(hit.highlightFields) {
		var hitHighlights = '';
		var i = 0;			
		
		for(field in hit.highlightFields) {
			var cssClass = i > 2 ? ' vhmml-hide toggleHit' : '';
			hitHighlights += searchHitHighlightTemplate({fieldName: fieldDisplayNames[field], value: hit.highlightFields[field], cssClass: cssClass});
			i++
		}
		
		$searchHit.append(hitHighlights);
		
		if(i > 3) {
			$searchHit.append(searchHitToggleTemplate());
		}
	}
	
	$searchHit.append(searchHitButtonsTemplate(hit.folioObject));
			
	return $searchHit[0].outerHTML;		
}

function getFolioSortByOptions() {
	var html = '';	
	html += '<option value="language">Language</option>';
	html += '<option value="writingSystem">Writing System</option>';
	html += '<option value="script">Script</option>';
	html += '<option value="beginDate">Date</option>';
	html += '<option value="country">Country</option>';				
	html += '<option value="city">City</option>';
	html += '<option value="repository">Repository</option>';
	html += '<option value="folioObjectNumber">Folio Object Number</option>';
	
	return html;
}