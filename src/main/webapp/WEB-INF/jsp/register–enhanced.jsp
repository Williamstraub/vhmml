<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="row">
	<div class="col-sm-4">
		<img class="block-image" src="${pageContext.request.contextPath}/static/img/ArabicBookKacmarcik27_004r_rev2_372x636.jpg"/>
	</div>
	<div class="col-sm-8">
		
		<form:form commandName="registrationForm" action="${pageContext.request.contextPath}/user/create" class="form-horizontal vhmmlForm">
			<div class="form-group">
				<label for="username" class="col-sm-5 control-label text-right">Username</label>
				<div class="col-sm-7">
					<form:input path="username" cssClass="form-control"/>
				</div>
			</div>
			
			<div class="form-group">
				<label for="password" class="col-sm-5 control-label text-right">Password</label>
				<div class="col-sm-7">
					<form:password path="password" cssClass="form-control"/>
				</div>
			</div>
			
			<div class="form-group">
				<label for="passwordConfirm" class="col-sm-5 control-label text-right">Confirm Password</label>
				<div class="col-sm-7">
					<form:password path="passwordConfirm" cssClass="form-control"/>
				</div>
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-5 col-sm-7 text-right">
					<button id="submitButton" name="submit" type="submit" class="btn home">Register</button>					
				</div>
			</div>
		</form:form>					
	</div>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/register.js?version=${applicationVersion}"></script>