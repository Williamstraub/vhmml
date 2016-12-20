<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
		
<div class="changePasswordDescription">
	<p>Please enter your current password and your desired password.</p>
	<p>Passwords must be a minimum of 6 characters in length, include at least one upper and lower case letter and a number.</p>
</div> 

<form:form commandName="changePasswordForm" action="${pageContext.request.contextPath}/user/changePassword" class="form-horizontal vhmmlForm" data-validation-rule-url="/user/changePassword/validationRules">	
	
	<div class="form-group">
		<label for="email" class="col-sm-5 control-label text-right">Registered Email</label>
		<div class="col-sm-7">
			<input type="text" value="${vhmmlSession.username}" class="form-control" disabled/>
		</div>
		
		<div class="info-icon-wrapper right" style="left: -4px; top: 28px;">
			<span class="info-icon">
				<i class="glyphicon glyphicon-info-sign pointer" aria-hidden="true" data-toggle="tooltip" data-placement="right" data-trigger="hover" title="Email address can only be changed by HMML staff"></i>
			</span>
		</div>
		
	</div>
	
	<div class="form-group">
		<label for="currentPassword" class="col-sm-5 control-label text-right">Current Password</label>
		<div class="col-sm-7">
			<form:password path="currentPassword" cssClass="form-control"/>
		</div>
	</div>
	
	<div class="form-group">
		<label for="newPassword" class="col-sm-5 control-label text-right">New Password</label>
		<div class="col-sm-7">
			<form:password path="newPassword" cssClass="form-control"/>
		</div>
	</div>
	
	<div class="form-group">
		<label for="newPasswordConfirm" class="col-sm-5 control-label text-right">Confirm New Password</label>
		<div class="col-sm-7">
			<form:password path="newPasswordConfirm" cssClass="form-control"/>
		</div>
	</div>
	
	<div class="form-group">
		<div class="col-sm-offset-5 col-sm-7 text-right">
			<button name="cancel" class="btn btn-default" type="button" onclick="javascript:window.history.back();">Cancel</button>
			<button id="submitButton" name="submit" type="submit" class="btn home btn-preferred">Save</button>					
		</div>
	</div>
</form:form>
