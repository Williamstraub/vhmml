var savedSearch;
var pagingControl = null;
var miradorViews = {};
var miradorMenuItemTemplate = Handlebars.compile($('#mirador-menu-item-template').html());

$(function() {
	
	var $body = $('body');
	
	$body.on('click', '#metaDataPanel .backToResults', function() {
		search();
	});
	
	// disable context menu on mirador viewer so users can't easily save images
	$body.on('contextmenu', '#viewer, .rr-thumbnail, #thumbnail-viewer img', function(e) {
		return false;
	});
	
	$body.on('mousedown', '#viewer, .rr-thumbnail', function(e) {
		// only allow mouse down on the add item link & manifest select screen, not on main Mirador image viewer
		if(!$('a.addItemLink').is(e.target) && !$('#manifest-select-menu').has(e.target)) {
			return false;
		}
	});
	
	$body.on('click', '.viewImagesLink', function() {
		viewImages($(this).attr('data-object-id'), $(this).attr('data-access'));
	});
	
	$body.on('click', '.viewDescription', function() {
		getMetaData($(this).attr('data-object-id'), viewDescription);
	});
	
	$body.on('click', 'a.toggleHiddenMatches', function() {
		var $link = $(this);
		var $toggleHits = $(this).parents('div.row').prevAll('div.toggleHit');
		
		$toggleHits.slideToggle({
			complete: function() {
				if($toggleHits.is(':hidden')) {
					$link.text('Show additional matches...');		
				} else {
					$link.text('Hide additional matches');
				}		
			}
		});
	});
	
	$('div.slideOutMenu').on('transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd', function() {		
		resizeMiradorSlots();		
	});
	
	$(window).resize(function() {
		resizeMiradorSlots();
	});
	
	$.subscribe('miradorAddSlot', function(event, data) {
		var $slots = $('div.layout-slot');
		var $newSlot = $($slots[$slots.length - 1]);
		// show the add button on the new slot so they can add a manifest to display
		$newSlot.find('div.slotIconContainer').show();
		resizeMiradorSlots();
	});
	
	$.subscribe('miradorFullScreen', function(event, isFullScreen) {
		if(isFullScreen) {
			$('#viewer .manifest-info i.fa-plus').hide();
		} else {
			$('#viewer .manifest-info i.fa-plus').show();
		}
	});
	
	$.subscribe('miradorViewCreated', function(event, view) {
		// remove the background spinner image once the image is loaded
		$('#viewer').css('background-image', '');
		var $slot = $(view.element).parents('div.layout-slot');
		var id = $slot.attr('data-layout-slot-id');
		
		// they selected a manifest and are viewing it in mirador now so hide the big add button 
		// even though they can't see it, it causes issues with clicking on some of our icons.
		$slot.find('div.slotIconContainer').hide();
		var $manifestNav = $slot.find('div.window-manifest-navigation');
				
		if(!$manifestNav.find('i.vhmml-mirador-icon').length) {
			var customIconsHtml = getCustomMiradorIcons(id);
			$manifestNav.append(customIconsHtml);
		}
		
		// if it's a view with no open seadragon viewer, i.e. thumbnail grid, hide the rotate buttons because we can't rotate images that aren't in an open seadragon viewer
		if(!view.osd) {
			$manifestNav.find('i.fa-rotate-left, i.fa-rotate-right').parent('a').css('display', 'none');
		} else {
			$manifestNav.find('i.fa-rotate-left, i.fa-rotate-right').parent('a').css('display', 'inline');
		}
		
		miradorViews[id] = view;
	});
	
	$.subscribe('miradorShowImageView miradorShowBookView', function(event, view) {
		var id = getMiradorViewId(view);
		$('div[data-layout-slot-id="' + id + '"]').find('i.fa-rotate-left, i.fa-rotate-right').parent('a').css('display', 'inline');
		miradorViews[id] = view;
	});
	
	$.subscribe('miradorShowThumbnailsView', function(event, view) {
		var id = getMiradorViewId(view);
		var $slot = $('div[data-layout-slot-id="' + id + '"]');
		$slot.find('i.fa-rotate-left, i.fa-rotate-right').parent('a').css('display', 'none');
		miradorViews[id] = view;
	});
	
	// fire a search when the user hits enter on a textarea
	$('div.searchControl textarea, div.searchControl input').on('keypress', function(e) {
		if(e.which === 13) {				
			newSearch();
			return false;
		}
	});
	
	$('button.clearSearchButton').click(function() {
		clearSearch();						
	});	
	
	$('#searchControl a[data-toggle="collapse"]').click(function() {
		clearSearch();
	});
});

function getMiradorViewId(view) {
	var $slot = $(view.element).parents('div.layout-slot');
	return $slot.attr('data-layout-slot-id');
}

function resizeMiradorSlots() {
	if(Mirador.viewer && Mirador.viewer.workspace) {
		Mirador.viewer.workspace.calculateLayout(miradorViews[0]);
	}		
}

function newSearch() {
	pagingControl.setPage(0);
	pagingControl.setSort();		
	search();
}

function runSavedSearch() {
	if(savedSearch) {
		if(savedSearch.pagingData) {
			pagingControl.setPagingDataFromSpringPageable(savedSearch.pagingData);
		}		
		search(savedSearch.searchTerms);
		var $searchForm = $('form.' + savedSearch.searchType);
		
		if(!$searchForm.is(':visible')) {
			$searchForm.parents('div.panel-default').find('div.panel-heading a').click();	
		}
		
		for(searchTerm in savedSearch.searchTerms) {
			var $input = $searchForm.find('.' + searchTerm);
			var textValue = savedSearch.searchTerms[searchTerm];
			
			if($input.is('select')) {
				var $option = $input.find('option').filter(function () {
					return $(this).html().trim() == textValue; 
				})
				var value = $option.attr('value') ? $option.attr('value') : textValue;
				$input.val(value);					
				refreshBootstrapSelect($input);	
			} else {
				$input.val(textValue);	
			}				
		}
	}
}

function pagingSearch() {
	search(null, true);
}

function search(searchTerms, isPagingSearch) {
	// reset the image viewer
	miradorViews = {};		
	var $resultsDisplay = $('#searchResults');		
	var pagingData = pagingControl.getPagingData();
	// searchTerms can be passed in for things like saved searches, linked on meta data panel, etc.
	searchTerms = searchTerms ? searchTerms : getSearchTerms();
	var data = $.extend({}, pagingData, {searchTerms: searchTerms});
	var $searchForm = $('#searchPanel form:visible');
	
	$.ajax({
		url : contextPath + $searchForm.attr('data-search-url'),
		data : data,
		method: 'GET',
		dataType: 'json',
		contentType:'application/json',
		beforeSend : function() {
			showSpinner({element: $resultsDisplay});
		},
		error : function(response) {
			pagingControl.hide();
			var html = '<h2>Cannot run search at this time</h2>';
			html += '<br />Unfortunately it appears that our Search server is currently not working.';
			html += '<br />Please try your search again later. We apologize for any inconvenience.';
			$resultsDisplay.html(html);
		},
		success : function(searchResult) {
			window.scrollTo(0, 0);
			$resultsDisplay.html('');
			$('#viewer, #thumbnail-viewer, #metaDataPanel, div.searchToggleBar.metaDataPanel').hide();
			$('#searchResultsWrapper').show();
			var $searchPanel = $('#searchPanel');
			
			// if the user is just paging through results, don't open the search panel again
			if(!isPagingSearch && $searchPanel.width() == 0) {
				slideMenu($searchPanel);
			}
			
			if (searchResult.searchHits.length) {
				pagingControl.updatePaging(searchResult);
				var html = '';
				var hits = searchResult.searchHits;

				for (var i = 0; i < hits.length; i++) {
					html += getSearchHitHtml(hits[i], searchResult.fieldDisplayNames);
				}					

				$resultsDisplay.append(html);
			} else {
				pagingControl.updatePaging(searchResult);
				$resultsDisplay.append(noSearchResultsTemplate());
			}			
		}
	});
}

function clearSearch() {
	$('#searchPanel form').each(function() {
		var $form = $(this);
		$form.find('input, textarea, select').val('');
		$form.find('input.formatFilter').prop('checked', true);
		$form.find('input.downloadable, input.pendingOnly').prop('checked', false);				
	});
	
	initDateSlider();
	$('#searchPanel select.bootstrap-select').selectpicker('val', '');
	
	var $countrySelect = $('#searchPanel select.country');
	
	if($countrySelect.length) {
		showAllCitiesAndRepos($('#searchPanel select.country'));	
	}		
}

function fieldLinkSearch(searchTerms) {
	var objectType = $('form.search').attr('data-object-type');
	var searchType = objectType === 'FOLIO' ? 'basic' : 'advanced';
	pagingControl.setPage(0);
	pagingControl.setSort();		
	clearSearch();	
	savedSearch = {searchType: searchType, searchTerms: searchTerms};
	runSavedSearch(savedSearch);		
}

function initDateSlider(beginDate, endDate) {
	var currentYear = new Date().getFullYear();
	beginDate = beginDate ? beginDate : 1;
	endDate = endDate ? endDate : currentYear;
	
	$('span.minDate').text(beginDate + (beginDate < 0 ? ' BCE' : ' CE'));		
	$('span.maxDate').text(endDate + (endDate < 0 ? ' BCE' : ' CE'));		

	var $slider = $('input.slider');
	$slider.slider({
		min: 1,
		max: currentYear,
		step: 25,
		value: [beginDate, endDate],
		tooltip: 'hide'
	}).on(
		'slide',
		function(e) {
			var values = e.value;
			var min = values[0];
			var max = values[1];				
			var $form = $(this).parents('form');				

			if (min > max) {
				min = values[1];
				max = values[0];
			}

			// value of slider can be larger than the actual max value if step > 1 				
			if(max > currentYear) {
				max = currentYear;
			}
			
			if(min > currentYear) {
				min = currentYear;
			}
			
			$form.find('.minDate').text(Math.abs(min) + (min < 0 ? ' BCE' : ' CE'));
			$form.find('.maxDate').text(Math.abs(max) + (max < 0 ? ' BCE' : ' CE'));
		}
	);
	
	$slider.slider('setValue', [beginDate, endDate]);

	$('div.slider-horizontal').css('width', '100%');
}

function slideMenu($menu) {
	var closing = isMenuOpen($menu);
	var newX = '0px'; 
	var isLeftMenu = $menu.hasClass('slideOutMenuLeft');
	var menuWidth = $menu.attr('data-width');
	
	if(closing) {
		newX = isLeftMenu ? '-' + menuWidth : menuWidth; 
	} else if(viewportWidth <= 980) {			
		var $otherMenu = $('div.slideOutMenu').not($menu);
		// if we're opening a menu and the other menu is open and the viewport is small, 
		// then close the other menu so there's room in the middle to view images
		if(isMenuOpen($otherMenu)) {
			slideMenu($otherMenu);	
		}			
	}
	
	var newWidth = newX == '0px' ? menuWidth : 0;
	var $toggleBar = isLeftMenu ? $menu.next('div.searchToggleBar') : $menu.prev('div.searchToggleBar'); 
	$menu.css({'transform': 'translateX(' + newX + ')', 'left': 'auto'}).width(newWidth).show();
	
	if(closing) {
		$toggleBar.show();	
	} else {
		$toggleBar.hide();
	}	
}

function isMenuOpen($menu) {
	return $menu.width() !== 0 && $menu.is(':visible');
}

function getCustomMiradorIcons(openSeaDragonId) {
	var html = '';
	
	html += miradorMenuItemTemplate({
		'href': 'javascript:rotate(\'' + openSeaDragonId + '\', -90);',
		'label': 'Rotate Left',
		'iconClass': 'fa fa-rotate-left vhmml-mirador-icon'
	});
	
	html += miradorMenuItemTemplate({
		'href': 'javascript:rotate(\'' + openSeaDragonId + '\', 90);',
		'label': 'Rotate Right',
		'iconClass': 'fa fa-rotate-right vhmml-mirador-icon'
	});
	
	html += miradorMenuItemTemplate({
		'href': 'javascript:closeMirador(\'' + openSeaDragonId + '\');',
		'label': 'Close',
		'iconClass': 'fa fa-times vhmml-mirador-icon'
	});		
	
	return html;
}

function closeMirador(openSeaDragonId) {
	if(Object.keys(miradorViews).length > 1) {
		// structure in Mirador is 
		var $slot = miradorViews[openSeaDragonId].parent.parent;
		Mirador.viewer.workspace.removeNode($slot);
		delete miradorViews[openSeaDragonId];
		resizeMiradorSlots();		
	} else {
		// if their are no viewers left, just close Mirador all together by re-running their last search to bring up the search results
		search();
	}	
}

function rotate(openSeaDragonId, degrees) {
	var openSeaDragonViewer = miradorViews[openSeaDragonId].osd;	
	
	if(openSeaDragonViewer && openSeaDragonViewer.viewport) {
		var viewport = openSeaDragonViewer.viewport;
    	var currentRotation = parseInt(viewport.getRotation());
        viewport.setRotation(currentRotation + degrees);            
        viewport.applyConstraints();
    }
}

function getMultiSelectVal($form, fieldName) {		
	var fieldValue = $form.find(fieldName).val();
	// we join on this weird token so we can easily split on it server side because the search terms might have commas, semi-colon, etc.
	return fieldValue ? fieldValue.join('</search-term>') : '';
}

function viewImages(objectId, accessRestriction) {
	if(accessRestriction == 'UNREGISTERED' || (isAuthenticated && (accessRestriction == 'REGISTERED' || accessRestriction == 'REGISTERED_ONLY'))) {
		getMetaData(objectId, refreshImageViewer);			
	} else if((accessRestriction == 'REGISTERED' || accessRestriction == 'REGISTERED_ONLY') && !isAuthenticated) {
		goToLogin();
	} 	
}

function getMetaData(objectId, showImageFunction) {
	var objectType = $('#searchPanel form').attr('data-object-type');
	var url = objectType == 'FOLIO' ? '/folio/object/' : '/readingRoom/object/';
	
	$.ajax({
		url : contextPath + url + objectId,
		success : function(object) {
			// page-specific versions of refreshMetaData are in reading-room & folio search js files
			refreshMetaData(object);
			showImageFunction(object);
		} 
	});
}

function viewDescription(object) {
	showImageThumbnail(object);
	slideMenu($('#metaDataPanel'));
	slideMenu($('#searchPanel'));
}

function refreshImageViewer(object) {
	var objectId = object.id;
	var port = serverPort == '80' ? '' : ':' + serverPort;
	var objectType = $('#searchPanel form').attr('data-object-type');
	var manifestUrl = scheme + '://' + serverName + port + contextPath + '/image/manifest/' + objectId + '?objectType=' + objectType;
	$('#searchResultsWrapper').hide();
	$('#thumbnail-viewer').hide();
	$('#viewer').html('').show();
	var $searchPanel = $('#searchPanel');
	var $metaDataPanel = $('#metaDataPanel');
	
	if(!isMenuOpen($metaDataPanel)) {
		slideMenu($metaDataPanel);	
	}	
	
	if(isMenuOpen($searchPanel)) {
		slideMenu($searchPanel);
	}
	
	Mirador({
		'id' : 'viewer', // The CSS ID selector for the containing element.
		'layout' : '1x1', // The number and arrangement of windows ('row'x'column')?
		'saveSession' : false,
		// This array holds the manifest URIs for the IIIF resources you want Mirador to make available to the user.
		// Each manifest object must have a manifest URI pointing to a valid IIIF manifest, and may also
		// provide a location to be displayed in the listing of available manifests.
		'data' : [{
			'manifestUri' : manifestUrl
		}],
		'mainMenuSettings' : {
			'show' : false
		},
		// This array allows the user to specify which of the included manifests should appear 
		// in the workspace, and what the configuration of the window (zoom level, open panels, etc.) 
		// ought to be. To begin with, we will leave it blank.	        
		'windowObjects' : [{
			'loadedManifest' : manifestUrl,
			'viewType' : 'ImageView',
			'availableViews': ['ThumbnailsView', 'ImageView', 'BookView'],			
			'displayLayout' : true,				
			'sidePanel' : false,			
			'annotationLayer' : false,
			'id': 'vhmmlMiradorViewer' // this is the windowId used internally by mirador, we set it so we can subscribe to Mirador events like osdOpen.windowId (OSD = OpenSeaDragon)
		}],
		'buildPath': contextPath + '/static/mirador/',
		'showLoadingAnimation': true,
		'externalDataProxyUrl': contextPath + '/image/loadData'
	});
}