$(function() {	
	$('#exportButtons').on('click', '.exportButton', function() {
		var $exportButton = $(this);
    	var exportToken = new Date().getTime();
    	disableExportButton($exportButton, exportToken);
    	var exportLink = $exportButton.hasClass('exportLinks') ? 'exportLinks' : 'exportObjects';
    	window.location = contextPath + '/catalogDatabase/' + exportLink + '?collectionName=' + $('#collectionName').val() + '&exportToken=' + exportToken;
	});
});

function disableExportButton($button, exportToken) {
    
	var $exportButtonWrapper = $button.parent('.exportButtonWrapper');
	var exportWrapperHtml = $exportButtonWrapper.html();
	
	showSpinner({
		element: $exportButtonWrapper,
		css: {'height': 'auto', 'display': 'inline-block'} 
	});
	
    fileDownloadCheckTimer = window.setInterval(function () {
    	var cookieValue = $.cookie('exportToken');
      
    	if (cookieValue == exportToken) {
    		window.clearInterval(fileDownloadCheckTimer);
			$.removeCookie('exportToken');
    		$exportButtonWrapper.html(exportWrapperHtml);			
    	}    	  
    }, 1000);
}
