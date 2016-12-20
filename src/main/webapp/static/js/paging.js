// static var to track list of paging control instances on a page
PagingControl.list = {};

function PagingControl(options) {
	
	var self = this; // make this visible to private functions
	
	// only create one PagingControl object for each table, even if there are more than one set of controls, i.e. one on top & one on bottom
	if(PagingControl.list[options.tableId]) {
		return;
	}
	
	var opts = initOptions(options);
	var paging = createPaging();
	
	initSortBy();
	initEvents();	
	
	$('select.bootstrap-select').selectpicker();
	PagingControl.list[opts.tableId] = this;
	
	// static functions
	PagingControl.get = function(tableId) {
		return PagingControl.list[tableId];
	};
	
	// privileged functions (publicly visible and have access to private public & private members)
	this.getPagingData = function() {
		return opts.pagingData;
	};
	
	this.setPagingDataFromSpringPageable = function(pagingData) {
		opts.pagingData = pagingData;
		opts.pagingData.size = pagingData.pageSize;
		opts.pagingData.page = pagingData.pageNumber;
		
		if(pagingData.sort && $.isArray(pagingData.sort)) {			
			var sortObject = opts.pagingData.sort[0];
			opts.pagingData.sort = sortObject.property + ',' + sortObject.direction;			
			opts.pagingData.sortBy = sortObject.property;
			opts.pagingData.direction = sortObject.direction;
			this.setSort(opts.pagingData.sort);
		}
	}
	
	this.setPage = function(pageNumber) {
		opts.pagingData.page = pageNumber;
	};
	
	this.setSort = function(sortBy) {
		var $sortByList = paging.$searchResultsData.find('select.sortBy');
		var $sortDirIcon = $sortByList.length ? $sortByList.parents('.sortByWrapper').find('button.sortDir .glyphicon') : null;
		
		if(sortBy) {
			var directionSpecified = sortBy.endsWith('ASC') || sortBy.endsWith('DESC');
			
			// direction & sortBy aren't used on the server side but it makes things convenient in the UI			
			if(directionSpecified) {
				var sortTokens = sortBy.split(',');			
				opts.pagingData.direction = sortTokens[sortTokens.length - 1];
				opts.pagingData.sortBy = sortBy.substring(0, sortBy.lastIndexOf(','));
				opts.pagingData.sort = sortBy;						
			} else {
				// if we're currently sorted by the field passed in ascending order, toggle the direction to descending
				var currentSort = opts.pagingData.sortBy;
				opts.pagingData.direction = currentSort && currentSort.startsWith(sortBy) && opts.pagingData.direction === 'ASC' ? 'DESC' : 'ASC';
				opts.pagingData.sortBy = sortBy;
				opts.pagingData.sort = sortBy + ',' + opts.pagingData.direction;
			}			
		} else {
			opts.pagingData.direction = null;
			opts.pagingData.sortBy = null;
			opts.pagingData.sort = null;					
		}
		
		if($sortByList && $sortDirIcon) {
			var sortByValue = opts.pagingData.sortBy ? opts.pagingData.sortBy : '_score';
			// we sort by endDate when beingDate DESC is the sort, but the drop down list only has a begin date option
			sortByValue = sortByValue == 'endDate' ? 'beginDate' : sortByValue;
			$sortByList.val(sortByValue);
			refreshBootstrapSelect($sortByList);
			
			if(opts.pagingData.direction == 'ASC') {
				$sortDirIcon.removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
			} else {
				$sortDirIcon.removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
			}			
		}		
	};
	
	this.hide = function() {
		paging.$controls.hide();
	};
	
	this.updatePaging = function(searchResult) {
				
		if(searchResult.totalElements) {
			if(searchResult.pageSize) {
				paging.page = searchResult.pageNumber;
				paging.currentPage = searchResult.pageNumber + 1;
				paging.pageCount = searchResult.totalPages;
				paging.$currentPage.text(paging.currentPage);
				paging.$pageCount.text(paging.pageCount);
				paging.$totalResults.text(searchResult.totalElements + ' ' + opts.totalLabel + ', Page ' + paging.currentPage + ' of ' + paging.pageCount);				
				var lastPossibleRecordOnPage = searchResult.offset + searchResult.pageSize;
				var lastRecordOnPage = lastPossibleRecordOnPage > searchResult.totalElements ? searchResult.totalElements : lastPossibleRecordOnPage;	
				
				if(paging.$jumpToPage.length) {
					paging.$jumpToPage.val('');
				}
				
			} else {
				// if there's no real paging data, they're browsing by starting letter, so just show x/26
				paging.currentPage = $('span.searchLetter').index($('span.searchLetter.selected')) + 1;
				paging.pageCount = $('span.searchLetter').length;
				paging.$currentPage.text(paging.currentPage);
				paging.$pageCount.text(paging.pageCount);			
			}
			
			paging.$nextButton.removeClass('disabled');
			paging.$previousButton.removeClass('disabled');
			
			if(paging.currentPage === paging.pageCount) {				
				paging.$nextButton.addClass('disabled');
			} 
			
			if(paging.currentPage === 1) {
				paging.$previousButton.addClass('disabled');
			}
			
			paging.$controls.show();
		} else {
			paging.$controls.hide();
		}		
	};
	
	this.sort = function() {
		opts.pagingData.page = 0;
		var searchParams = typeof opts.searchParams === 'function' ? opts.searchParams.call() : opts.searchParams;
		opts.searchFunction.call(this, searchParams);
	}
		
	// private functions
	function initOptions(options) {
		
		// spring paging needs attributes named page, size & sort but our tag uses pageSize & sortBy so it's easier to understand
		var defaultPaging = {page: 0, size: 25, sort: '', direction: 'DESC'};
		var userPaging = {page: options.currentPage, size: options.pageSize, sort: options.sortBy};		
		var pagingData = $.extend({}, defaultPaging, userPaging);
		
		var defaultOptions = {
			searchFunction: function() {}, // gets search result as a parameters
			onNextButton: function() {},
			onPrevButton: function() {},
			totalLabel: 'records', 
			displayedLabel: 'Records',
			pagingData: pagingData	
		};		
		
		var opts = $.extend({}, defaultOptions, options);		
		
		return opts;
	}
	
	function initSortBy() {
		
		if(opts.sortByOptions) {
			var sortByOptionsHtml = opts.sortByOptions.call();
			$('select.sortBy').each(function() {
				var $select = $(this);
				$select.append(sortByOptionsHtml);
				refreshBootstrapSelect($select);
			});
		}		
	}
	
	function createPaging() {
		var paging = {};
		
		paging.$controls = $('div.' + opts.tableId);		
		paging.$currentPage = paging.$controls.find('.currentPage');
		paging.$pageCount = paging.$controls.find('.pageCount');
		paging.$buttons = paging.$controls.find('a.previous, a.next');
		paging.$previousButton = paging.$controls.find('a.previous');
		paging.$nextButton = paging.$controls.find('a.next');
		paging.$searchResultsData = paging.$controls.children('.searchResultsData');
		paging.$totalResults = paging.$searchResultsData.children('.total');
		paging.$totalDisplayed = paging.$searchResultsData.children('.displayed');
		paging.$pageSize = paging.$controls.find('select.pageSize');
		
		paging.$jumpToPage = paging.$controls.find('input.jumpToPage');
		
		return paging;
	}

	function initEvents() {
		paging.$buttons.click(function() {
			var $this = $(this);
			
			if(!$this.hasClass('disabled')) {
				// disable the buttons while the search is executing
				paging.$buttons.addClass('disabled');
				var searchParams = typeof opts.searchParams === 'function' ? opts.searchParams.call() : opts.searchParams;			
				
				if($this.hasClass('next')){
					opts.pagingData.page++;
					opts.onNextButton.apply(this, $this);
				} else if($this.hasClass('previous')){
					opts.pagingData.page--;
					opts.onPrevButton.apply(this, $this);
				}
				
				opts.searchFunction.call($this, searchParams);
			}						
		});
		
		if(paging.$searchResultsData.length) {
			paging.$pageSize.change(function() {
				var newPageSize = $(this).val();
				opts.pagingData.size = newPageSize;				
				$('select.pageSize').val(newPageSize).selectpicker('refresh');
				self.sort();				
			});
			
			paging.$searchResultsData.find('select.sortBy').change(function() {
				var $this = $(this);
				var sortBy = $this.val();
				var direction = sortBy == '_score' ? 'DESC' : 'ASC';				
				self.setSort(sortBy + ',' + direction);
				self.sort();
			});
			
			paging.$searchResultsData.find('button.sortDir').click(function() {
				var $this = $(this);
				var direction = $this.children('.glyphicon').hasClass('glyphicon-chevron-up') ? 'DESC' : 'ASC';				
				var $selectList = $this.prev('.bootstrap-select').children('select.sortBy');				
				var sortBy = $selectList.val() == 'beginDate' && direction == 'DESC' ? 'endDate' : $selectList.val();
				self.setSort(sortBy + ',' + direction);
				self.sort();
			});
		}
		
		if(paging.$jumpToPage.length) {
			
			paging.$jumpToPage.next('button.btn-go').click(function() {
				opts.pagingData.page = $(this).prev('input.jumpToPage').val() - 1;
				var searchParams = typeof opts.searchParams === 'function' ? opts.searchParams.call() : opts.searchParams;
				opts.searchFunction.call($(this), searchParams);
			});
			
			paging.$jumpToPage.on('keyup', function(e) {
				if(e.which === 13) {
					var $this = $(this);					
					updateJumpToPage($this);
					$this.next('button.btn-go').click();							
				}
			});
			
			paging.$jumpToPage.blur(function() {
				updateJumpToPage($(this));				
			});
		}
	}
	
	function updateJumpToPage($jumpToPage) {		
		var pageNumber = $jumpToPage.val();
		
		if(pageNumber > paging.pageCount) {
			$jumpToPage.val(paging.pageCount);
		} else if(pageNumber == '') {
			$jumpToPage.val('');
		} else if(pageNumber <= 0) {
			$jumpToPage.val(1);
		}
	}
}