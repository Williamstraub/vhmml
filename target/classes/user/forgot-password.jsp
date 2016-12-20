<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
		
<div class="changePasswordDescription">
	<p>Please enter the email address that you use to sign in to <span class="redtext italicstext">v</span>HMML to have a temporary password sent to you.</p>
</div> 

<form:form commandName="forgotPasswordForm" action="${pageContext.request.contextPath}/user/forgotPassword" class="form-horizontal vhmmlForm" data-validation-rule-url="/user/forgotPassword/validationRules">
	<div class="form-group">
		<label for="emailAddress" class="col-sm-4 control-label text-right">Email</label>
		<div class="col-sm-8">
			<form:input path="emailAddress" cssClass="form-control"/>
		</div>
	</div>
	
	<div class="form-group">
		<div class="col-sm-offset-4 col-sm-8 text-right">
			<button name="cancel" class="btn btn-default" type="button" onclick="javascript:window.history.back();">Cancel</button>
			<button id="submitButton" name="submit" type="submit" class="btn home btn-preferred">Submit</button>					
		</div>
	</div>
</form:form>
