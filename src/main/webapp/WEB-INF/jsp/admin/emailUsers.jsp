<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<style type="text/css">
	#cke_message {
		width: auto !important;
	}
	
	.formWrapper {
		margin: inherit;
	}
</style>

<div class="formWrapper">
	<h3>Use the form below to send a message to all active users</h3>
	
	<form:form commandName="emailUsersForm" action="${pageContext.request.contextPath}/admin/emailUsers" class="form-horizontal vhmmlForm" data-validation-rule-url="/admin/emailUsers/validationRules">		
		<div class="form-group">
			<label for="subject" class="col-sm-2 control-label text-right">Subject</label>
			<div class="col-sm-10">
				<form:input path="subject" cssClass="form-control" data-error-placement="bottom"/>
			</div>
		</div>
		<div class="form-group">
			<label for="message" class="col-sm-2 control-label text-right">Message</label>
			<div class="col-sm-10">
				<form:textarea path="message" cssClass="form-control ckEditor" data-error-placement="bottom"/>
			</div>
		</div>			
					
		<div class="form-group">
			<div class="col-sm-offset-5 col-sm-7 text-right">
				<button name="cancel" type="button" class="btn btn-default" onclick="javascript:window.history.back();">Cancel</button>
				<button id="sendButton" name="sendButton" type="button" class="btn home btn-preferred">Send</button>					
			</div>
		</div>				
	</form:form>
</div>

<script src="https://cdn.ckeditor.com/4.4.7/standard/ckeditor.js"></script>
<script src="${pageContext.request.contextPath}/static/js/admin/email-users.js?version=${applicationVersion}"></script>