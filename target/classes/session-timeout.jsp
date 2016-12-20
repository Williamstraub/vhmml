<!-- 
	If a user's session timeouts, they are redirected to this page. Ajax
	requests automatically follow that redirect and receive the HTML for this
	page as the response to the original ajax request, without realizing a 
	redirect happened. Therefore, we have the "vhmmlSessionTimeout" div below
	so that we can always check ajax responses to see if they are HTML and
	contain this element, which means the user's session timed out. In
	vhmml.js we have an ajaxComplete function bound at the document level
	that contains the logic to check for this vhmmlSessionTimeout element 
	and send the user to this page.
-->
<div id="vhmmlSessionTimeout"></div>

<script type="text/javascript">
	$(function() {
		new Dialog({			
			message: 'Your session has timed out due to inactivity. You will now be redirected to the login page.',
			showCloseIcon: false,
			closeButtonLabel: 'OK',
			closeButtonFunction: function() {
				window.location = '${pageContext.request.contextPath}/login';
			}
		}).show();
	});
</script>