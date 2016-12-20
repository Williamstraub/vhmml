var vhmmlSessionTimeoutCheck;
var timeoutDialog;
var isMenuAnimating = false;
var viewportWidth;
var vhmmlTooltipTemplate = '<div class="tooltip" role="tooltip" style="min-width: 200px;"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>';

$(function() {
	viewportWidth = $(window).width();
	addMissingStringFunctions();	
	initMenu();	
	initTooltips();
	initPopovers();
	$.ajaxSetup({cache: false});
});

function addMissingStringFunctions() {
	if(typeof String.prototype.startsWith != 'function') {
		String.prototype.startsWith = function (str) {
			return this.slice(0, str.length) == str;
		};
	}
	
	if(typeof String.prototype.endsWith != 'function') {
		String.prototype.endsWith = function (str) {
			return this.slice(-str.length) == str;
		};
	}
}

function getId(str) {
	return "#" + str.replace( /(:|\.|\[|\]|,)/g, "\\$1" );
}

function initMenu() {
	var $accountSettingsLink = $('a.account-settings-link');	
	var $navItems = $('nav.main-nav ul');
	
	$('a.account-settings-link, a.hamburger-menu').on('keydown', function(e) {
		if(e.which === 40) { 
			var $menu = $('.' + $(this).attr('data-menu'));
			$menu.find('a:first-child').focus();
			return false;
		}
	});
	
	var $menuLinks = $('nav.main-nav a');
	var $accountLinks = $('nav.account-menu a');
	
	$menuLinks.on('keydown', function(e) {
		return navigateMenu($menuLinks, $(this), e);
	});	
	
	$accountLinks.on('keydown', function(e) {
		return navigateMenu($accountLinks, $(this), e);
	});
	
	$accountSettingsLink.click(function() {
		animateMenuItems($('nav.account-menu'));			
	});
	
	$(document).mouseup(function (e){
		checkMenuState(e);
	});
	
	$(document).on('touchend', function(e){
		checkMenuState(e);
	});
}

function navigateMenu($links, $selectedLink, keyEvent) {
	var selectedIndex = $links.index($selectedLink);
	
	if(keyEvent.which === 38) { // arrow up		
		$links.get(selectedIndex - 1).focus();
		return false;
	} else if(keyEvent.which === 40) { // arrow down
		var index = selectedIndex + 1 == $links.length ? 0 : selectedIndex + 1;
		$links.get(index).focus();
		return false;
	}
}

function animateMenuItems($menuItems) {
	if(!isMenuAnimating) {
		isMenuAnimating = true;
		
		$menuItems.slideToggle({
			duration: 200,
			always: function() {					
				isMenuAnimating = false;
			}
		});
	}
}

function checkMenuState(e) {	
	var $closeHamburgerButton = $('a.hamburger-menu.close-menu');
	var $hamburgerButton = $('a.hamburger-menu.open-menu');
	var $accountSettingsLink = $('a.account-settings-link');
	var $accountMenuItems = $('nav.account-menu');
	
	if($closeHamburgerButton.is(':visible')) {
		closeMenu(
			$hamburgerButton, 
			function() {
				$closeHamburgerButton[0].click();
			},
			e
		);
	}		
	
	closeMenu(
		$accountSettingsLink, 
		function() {
			$accountMenuItems.hide();
		}, 
		e
	);
}

function closeMenu($menuButton, closeMenuFunction, clickEvent) {
	// if the target of the click isn't the hamburger button or a descendant of the hamburger button or one of the main menu items, close the menu	
	var $mainNav = $('#main-nav');
	
    if(!$menuButton.is(clickEvent.target) && $menuButton.has(clickEvent.target).length === 0 && !$mainNav.is(clickEvent.target) && $mainNav.has(clickEvent.target).length === 0) {
    	closeMenuFunction.call();        
    }
}

function initTooltips() {
	$('body').tooltip({
	    selector: '[data-toggle=tooltip]',
	    template: vhmmlTooltipTemplate
	});
}

function showSpinner(options) {	
	var inlineCss = $.extend({'height': '400px'}, options.css);
	$element = options.element && options.element.length ? options.element : $('body');
	var originalHtml = $element.html();
	var spinnerName = options.spinnerName ? options.spinnerName : 'ajax-loader'; 
	var $spinner = $('<div class="spinner"></div>')
		.css(inlineCss)
		.html('<span class="vertical-align-helper"></span><img src="' + contextPath + '/static/img/' + spinnerName + '.gif"/>');
	
	if(options.imageCss) {
		$spinner.children('img').css(options.imageCss);
	}
	
	if(options.message) {	
		var $message = $('<label class="spinnerMessage">' + options.message + '</label>').css(options.messageCss);
		$spinner.append($message);
	}
	
	$element.html($spinner.show());	
	
	return {
		$element: $element,
		$spinner: $spinner,
		hide: function() {
			$element.html(originalHtml);
		}
	};
}

function preloadImage(imgSrc, callback) {
	var objImagePreloader = new Image();

	objImagePreloader.src = imgSrc;
	if (objImagePreloader.complete) {
		callback();
		objImagePreloader.onload = function() {
		};
	} else {
		objImagePreloader.onload = function() {
			callback();
			// clear onLoad, IE behaves irratically with animated gifs otherwise
			objImagePreloader.onload = function() {
			};
		}
	}
}

function initPopovers() {
	
	$('body').on('click', function (e) {
	    $('.popover-trigger').each(function () {	        
	    	var $popoverTrigger = $(this);
	        if (!$popoverTrigger.is(e.target) && // click wasn't on the popover's trigger
	        		$popoverTrigger.has(e.target).length === 0 && // the popover trigger doesn't contain the element that was clicked
	        		$('.popover').has(e.target).length === 0) { // there are no popovers that contain the element that was clicked
	        	$popoverTrigger.popover('hide');
	        }
	    });
	});
}

// Bootstrap popovers don't close automatically on touch devices so we need this special method to close them 
function closeTooltipsOnTouch(touchEvent) {
	
	$('.info-icon i.glyphicon').each(function () {	        
    	var $infoIcon = $(this);
    	
        if (!$infoIcon.is(touchEvent.target) && // click wasn't on the tooltip's trigger
        	$infoIcon.has(touchEvent.target).length === 0 && // the tooltip's trigger doesn't contain the element that was clicked
        	$('.tooltip').has(touchEvent.target).length === 0) { // there are no tooltips that contain the element that was clicked        	
        		$infoIcon.tooltip('hide');        		
        } else {
        	$infoIcon.tooltip({
        	    template: vhmmlTooltipTemplate}
        	).tooltip('show');
        }
    });
}

function toProperCase(str) {
	if(str) {
		str = str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
	}
	
    return str;
}

function scrollElementIntoView($element) {
	var offset = $element.offset().top;
	var visibleAreaStart = $(window).scrollTop();
    var visibleAreaEnd = visibleAreaStart + window.innerHeight;

    if(offset < visibleAreaStart || offset > visibleAreaEnd) {
    	$element[0].scrollIntoView();
    }			
}

function isResponseHtml(response) {
	var contentType = response.getResponseHeader('content-type');
	return contentType && contentType.indexOf('text/html') >= 0; 
}

function isJson(str) {
	var jsonObject = null;
	
    try {
    	jsonObject = JSON.parse(str);
    } catch (e) {
        return false;
    }
    
    return jsonObject;
}

function stripHtml(aString) {
	var div = document.createElement("div");
	div.innerHTML = aString;
	return div.textContent || div.innerText || "";
}

function initSessionTimeout() {
	timeoutDialog = new Dialog({showCloseIcon: false});
	sessionTimeoutCheck = setSessionTimeoutCheck();
	
	$(document).ajaxStart(function() {
    	clearTimeout(sessionTimeoutCheck);
    	sessionTimeoutCheck = setSessionTimeoutCheck();
    });    
    
	$(document).ajaxComplete(function(event, response, ajaxOptions) {
		// if the response to an ajax request was the session timeout page, send them to the session timeout page
		if(isResponseHtml(response) && $(response.responseText).find('#vhmmlSessionTimeout').length) {			
			window.location = contextPath + '/session-timeout';
		}		
	});
}

function setSessionTimeoutCheck() {
	// show the timeout warning 1 minute before the session times out or 30 seconds before it
	// times out if the session timeout is set at 1 minute
	// sessionTimeoutSeconds comes from HttpSession.getMaxInactiveInterval and is declared on site-template.jsp
	var minTimeoutSeconds = sessionTimeoutSeconds > 60 ? (sessionTimeoutSeconds - 60) : 30;
	return setTimeout(function() {
			sessionTimeoutWarning();
		}, 
		(1000 * minTimeoutSeconds)
	);
}

function sessionTimeoutWarning() {
	timeoutDialog.show({
		title: 'Still there?',
		message: 'Your session is about to timeout due to inactivity. Would you like to continue working?',
		showCloseIcon: false,
		closeButtonLabel: 'No',
		closeButtonFunction: function() {
			$.ajax({
				url: contextPath + '/user/endSession',
				method: 'POST',
				complete: function() {
					window.location = contextPath;
				}					
			});
		},
		buttons: {
			'Yes': function() {
				$.ajax({
					url: contextPath + '/user/validateSession',
					method: 'GET',
					success: function(isSessionValid) {
						if(!isSessionValid) {
							window.location = contextPath + '/user/session-timeout';
						} else {
							timeoutDialog.hide();
						}						
					}
				});
			}			
		}
	});	
}

function selectOptionByText($selectList, text) {
	$selectList.children('option').filter(function () { return $(this).html() == text; }).attr('selected', 'selected');
	$selectList.change();
}

function isTouchDevice() {
	return 'ontouchstart' in document.documentElement;
}

function lockViewport() {	
	$("meta[name=viewport]").attr('content', 'width=device-width, initial-scale=1');
}

function unlockViewport() {
	$("meta[name=viewport]").attr('content', '');
}

function refreshBootstrapSelect($select) {
	if($select.hasClass('bootstrap-select')) {
		$select.selectpicker('refresh');
	}
}