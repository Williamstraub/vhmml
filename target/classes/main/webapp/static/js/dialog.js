function Dialog(options) {
	
	var self = this; // add self variable so private functions can access "this"
	var opts = setDefaultOptions(options);
	createDialog();
	initEvents();
	
	/* privileged functions (publicly visible and have access to private public & private members) */
	this.show = function(userOptions) {		
		// user can initialize the dialog with options or set them when they show the dialog
		if(userOptions) {
			opts = setDefaultOptions(userOptions);
		}
		
		refresh();				
		$('#vhmml-dialog').modal('show');		
	};	
	
	this.hide = function() {
		$('#vhmml-dialog').modal('hide');
	};
	
	this.getBody = function() {
		return $('#vhmml-dialog').find('div.modal-body');
	};
	
	function setDefaultOptions(userOptions) {
		
		var dialogDefaults = {
			title: '',
			type: 'message',
			closeButtonLabel: 'Cancel',
			showCloseIcon: true,
			// default close button to the buttonsCss class if they specified one
			closeButtonCssClass: userOptions && userOptions.buttonsCssClass ? userOptions.buttonsCssClass : 'btn-default',
			closeButtonFunction: null,
			message: '',
			cssClass: '',
			css: '',
			buttons: [],
			buttonsCssClass: 'home',
			footerHtml: '',
			moveable: false
		};
		
		if(userOptions) {
			switch(userOptions.type) {
				case 'progress':
					dialogDefaults.message = 'Processing...';
					break;
				case 'confirm':
					dialogDefaults.message = 'Are you sure?';
					break;
			}
		}		
		
		return $.extend({}, dialogDefaults, userOptions);
	}
	
	function refresh() {
		$('#vhmml-dialog .modal-title').html(opts.title);
		var bodyHtml = opts.body ? opts.body : '<p class="message">' + opts.message + '</p>';
		$('#vhmml-dialog div.modal-body').html(bodyHtml);
		initButtons(opts.buttons);
		initEvents();
		var $dialog = $('#vhmml-dialog .modal-dialog');
		// clear out any previously set cssClass, only class is modal-dialog and whatever the user specified 
		$dialog.attr('class', 'modal-dialog').addClass(opts.cssClass);
		
		if(opts.showCloseIcon) {
			$dialog.find('button.close-icon').show();
		} else {
			$dialog.find('button.close-icon').hide();
		}
		
		if(opts.moveable) {
			$('#vhmml-dialog .modal-content').draggable();
		}			
	}
	
	function createDialog() {
		
		if(!$('#vhmml-dialog').length) {
			var html = '';
			html += '<div id="vhmml-dialog" class="modal fade" tabindex="-1">';
				html += '<div class="modal-dialog ' + opts.cssClass + '">';
					html += '<div class="modal-content">';
						html += '<div class="modal-header">';
							html += '<button type="button" class="close close-icon" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>';							
							html += '<h4 class="modal-title">' + opts.title + '</h4>';
						html += '</div>';
						html += '<div class="modal-body">';
							if(opts.body) {
								html += opts.body;
							} else {
								html += '<p class="message">' + opts.message + '</p>';
							}							
						html += '</div>';
						html += '<div class="modal-footer buttons">';
							if(opts.footerHtml) {
								html += opts.footerHtml;
							}
							html += '<button type="button" class="btn ' + opts.closeButtonCssClass + '" data-dismiss="modal">' + opts.closeButtonLabel + '</button>';							
						html += '</div>';
					html += '</div>';
				html += '</div>';
			html += '</div>';


			$('body .container').append(html);
			
			
			$('#vhmml-dialog .modal-content')
				.on('mousemove', function(){ // Update containment each time it's dragged
				    $(this).draggable({
				        greedy: true, 
				        handle: '.modal-header',	
				        containment: // Set containment to current viewport
				        [
				         	$(document).scrollLeft(),
				         	$(document).scrollTop(),
				         	$(document).scrollLeft()+$(window).width()-$(this).outerWidth(),
				         	$(document).scrollTop()+$(window).height()-$(this).outerHeight()
				         ]
				    });
				})
				.draggable();			
		}				
	}
	
	function initButtons(buttons) {
		var $buttons = $('#vhmml-dialog div.buttons');
		var html = '';
		
		if(opts.footerHtml) {
			html += opts.footerHtml;
		}
		
		var $closeButton = $('<button type="button" class="btn closeBtn ' + opts.closeButtonCssClass + '" data-dismiss="modal">' + opts.closeButtonLabel + '</button>');
		html += $closeButton[0].outerHTML;
		$buttons.html(html);
		
		if(opts.closeButtonFunction) {
			$buttons.find('button.closeBtn').click(function() {
				opts.closeButtonFunction.call();
			});
		} else {
			$buttons.find('button.closeBtn').unbind('click');
		}
		
		for(button in opts.buttons) {
			var $button = $('<button type="button" class="btn ' + opts.buttonsCssClass + '">' + button + '</button>');
			$button.click(buttons[button]);			
			$buttons.append($button);
		}
	}
	
	function initEvents() {
		var $dialog = $('#vhmml-dialog');		
		
		$dialog.off('show.bs.modal').on('show.bs.modal', function (e) {
			var $this = $(this);
			$dialog.find('.modal-content').css({'top': '0px', 'left': '0px'});			
			
			if(opts.keyboard === false) {				
				$this.data('bs.modal').options.keyboard = false;
			} else {
				$this.data('bs.modal').options.keyboard = true;
			}
			
			if(opts.moveable) {
				$this.data('bs.modal').options.backdrop = false;
			} else {
				$this.data('bs.modal').options.backdrop = true;
			}
		});
	}
}