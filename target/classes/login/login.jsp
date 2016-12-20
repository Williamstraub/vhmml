<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
		
	#loginForm label {
		margin-right: 15px;
	}
	
	#loginForm .form-group {
		display: flex;
	}
	
	#loginForm .align-right {
		justify-content: flex-end;
	}
	
	#loginForm .form-group {
		margin-bottom: 20px;
	}
	
	#loginForm .form-group:last-child {
		margin-bottom: 0;
	}
	
	#loginForm a.white {
		text-decoration: none;
	}
</style>
<form id="loginForm" name="loginForm" class="form-horizontal vhmmlForm" action="${pageContext.request.contextPath}/j_spring_security_check" method="POST">
	<div class="form-group">
		<label for="j_username" class="control-label">Email</label>
		<input type="text" name="j_username" class="form-control" value="" autocomplete="off" class="form-control" />
	</div>

	<div class="form-group">
		<label for="j_password" class="control-label">Password</label>
		<input type="password" name="j_password" autocomplete="off" class="form-control" />
	</div>
	<div class="form-group align-right">
		<button name="cancel" class="btn btn-default" type="button" onclick="javascript:window.history.back();">Cancel</button>
		<button name="submit" type="submit" class="btn home btn-preferred">Sign in</button>			
	</div>
	<div class="form-group align-right">
		<span class="white-link"><a href="${pageContext.request.contextPath}/user/forgotPassword" class="white small signIn">Forgot your password?</a></span>&nbsp;|&nbsp;
		<span class="white-link"><a href="${pageContext.request.contextPath}/registration"  class="white small signIn">Need to register?</a></span>
	</div>
	
</form>

<c:if test="${not empty param['error'] && SPRING_SECURITY_LAST_EXCEPTION != null}">
	<script type="text/javascript">
		$(function() {
			Messages.addMessage({
				message: '${SPRING_SECURITY_LAST_EXCEPTION.message}',
				severity: 'ERROR',
				test: '${SPRING_SECURITY_LAST_EXCEPTION}'
			});
		});
	</script>
</c:if>