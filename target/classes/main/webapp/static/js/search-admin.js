$(function() {
	$('.reindexButtonWrapper').on('click', '.reindexButton', function() {
		var $this = $(this);
		var imageCss = $this.is('a') ? {'height': '18px', 'padding': '0 18px'} : {};
		reindex($this.attr('data-url'), imageCss);
	});
});

function reindex(url, imageCss) {
	var $reindexButtonWrapper = $('.reindexButtonWrapper');
	var buttonHtml = $reindexButtonWrapper.html();
	
	$.ajax({
		url: contextPath + url,
		method: 'POST',
		beforeSend: function() {
			showSpinner({
				element: $reindexButtonWrapper,
				css: {'height': 'auto', 'display': 'inline-block'},
				imageCss: imageCss
			});
			Messages.removeAll();
			Messages.addMessage({message: 'Re-indexing...', severity: 'INFO'});
		},
		complete: function(response) {
			$reindexButtonWrapper.html(buttonHtml);
			Messages.removeAll();
			Messages.showResponseMessage(response);			
		}
	});
}