var popularityReportTemplate;
var userReportTemplate;
var objectReportTemplate;
var pagingControl;

$(function() {
	
	popularityReportTemplate = Handlebars.compile($('#popularity-report-template').html());
	userReportTemplate = Handlebars.compile($('#user-report-template').html());
	objectReportTemplate = Handlebars.compile($('#object-report-template').html());
	pagingControl = PagingControl.get('reportTable');
	pagingControl.setSort(getDefaultSort(reportType));
	
	$('.reportControls .reportParam.' + reportType).show();
	
	$('.date').datepicker();
	
	$('#runReport').click(function(e) {		
		newReport();	
	});
	
	$('#username, #hmmlProjectNumber').on('keydown', function(e) {
		if(e.which === 13) {
			newReport();
		}
	});
	
	$('#clearButton').click(function() {
		pagingControl.setPage(0);
		pagingControl.setSort();
	});	
	
	$('body').on('click', 'button.export', function() {
		exportReport();
	});
});

function newReport() {
	pagingControl.setPage(0);
	pagingControl.setSort();
	runReport();
}

function runReport() {
	if($('#startDate').val() && $('#endDate').val()) {		
		
		var pagingData = pagingControl.getPagingData();		
		var data = $.extend({}, pagingData, {reportParameters: getReportParameters()});
		
		$.ajax({
			url: contextPath + '/admin/runReport/' + reportType.toUpperCase(),
			method: 'GET',
			data:  data,
			dataType: 'json',
			contentType:'application/json',
			success: function(response) {
				pagingControl.updatePaging(response);
				renderReport(response);
			}
		});
	} else {
		new Dialog({
			title: 'No date range selected',
			message: 'Please select a start date and an end date to run a report.',
			closeButtonLabel: 'OK',
			buttonsCssClass: 'admin'					
		}).show();
	}
}

function renderReport(reportData) {
	var html = '';
	
	switch(reportType) {
		case 'popularity':
			reportData.aggregationFieldLabel = $('#aggregationField option:selected').text();
			html = popularityReportTemplate(reportData);
			break;
		case 'object':
			html = objectReportTemplate(reportData);
			break;
		case 'user':
		case 'cataloger':
			var dateLabel = reportType == 'cataloger' ? 'Date Added/Edited' : 'Date Accessed';
			reportData = $.extend(reportData, {dateLabel: dateLabel});
			html = userReportTemplate(reportData);
			break;
	}
	
	$('#reportTable').html(html);
}

function sort(sortBy) {
	pagingControl.setSort(sortBy);
	runReport();
}

function getDefaultSort() {
	var defaultSort = '';
	
	switch(reportType) {
		case 'popularity':
			defaultSort = '_count,DESC';
			break;
		case 'object':
			defaultSort = 'hmmlProjectNumber';
			break;
		case 'user':
		case 'cataloger':
			defaultSort = 'username';
			break;
	}
	
	return defaultSort;
}

function getReportParameters() {
	
	return {			
		hmmlProjectNumber: $('#hmmlProjectNumber').val(),				
		username: $('#username').val(),
		aggregationField: $('#aggregationField').val(),
		aggregationMax: $('#aggregationMax').val(),
		startDate: $('#startDate').val(),		
		endDate: $('#endDate').val()			
	};
}

function exportReport() {	
	if($('#startDate').val() && $('#endDate').val()) {
		// we want default sortin on exported reports but no paging, all rows should be in the csv
		var params = 'sort=' + encodeURIComponent(getDefaultSort());
		params += '&' + $('#reportForm').serialize();
		window.location = contextPath + '/admin/exportReport/' + reportType.toUpperCase() + '?' + params;
	} else {
		new Dialog({
			title: 'No date range selected',
			message: 'Please select a start date and an end date to run a report.',
			closeButtonLabel: 'OK',
			buttonsCssClass: 'admin'					
		}).show();
	}
}