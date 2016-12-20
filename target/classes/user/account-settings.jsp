<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<c:set var="pageTitle" value="Researcher Registration"/>
<c:set var="saveUrl" value="/registration/register"/>
<c:set var="saveButtonLabel" value="Register"/>

<c:if test="${saveAccountSettingsAction != 'register'}">
	<c:set var="saveUrl" value="/user/accountSettings"/>
	<c:set var="saveButtonLabel" value="Save"/>
	<c:set var="pageTitle" value="Account Settings"/>
</c:if>

<c:if test="${saveAccountSettingsAction == 'adminAccountSettings'}">
	<c:set var="saveUrl" value="/admin/user/accountSettings"/>	
</c:if>
			
<div class="row">
	<div class="col-md-2 col-sm-3 hidden-sm hidden-xs">
		<div class="block-image-wrapper">
			<div>
				<img class="block-image-tall registration" src="${pageContext.request.contextPath}/static/img/ArabicBookKacmarcik27_004r_rev2_372x636.jpg" alt="Arabic with red and blue decorations forming apex" />
			</div>
			<div class="info-icon-wrapper">
				<span class="info-icon">
					<i class="glyphicon glyphicon-info-sign pointer" aria-hidden="true" data-toggle="tooltip" data-placement="right" data-trigger="hover" title="Image from HMML, Arca Artium, Kacmarcik, MS 27, f. 4r. Used under a CC BY-NC 4.0 license."></i>
				</span>
			</div>	
		</div>				
	</div>
	<div class="col-md-10 col-sm-9 col-xs-12 text-center">
		
		<div id="registrationWrapper">
			<h2>${pageTitle}</h2>
			
			<c:if test="${saveAccountSettingsAction == 'register'}">
				<div class="topLink">
					<a href="${pageContext.request.contextPath}/login"><em>Already registered? </em>&nbsp;Sign In</a>
				</div>
			</c:if>										
			
			<form:form commandName="registrationForm" action="${pageContext.request.contextPath}${saveUrl}" onsubmit="javascript:return false;" class="form-horizontal vhmmlForm" data-validation-rule-url="/registration/validationRules">
				<form:hidden path="id"/>
				<form:hidden path="enabled"/>
				
				<div class="form-group">
					<label for="email" class="col-sm-5 control-label text-right">Your Email is</label>
					<div class="col-sm-7">
						<c:set var="disabled" value="false"/>
						<security:authorize access="isAuthenticated()">
							<c:set var="disabled" value="true"/>
							<form:hidden path="username" />
						</security:authorize>
																		
						<form:input path="username" cssClass="form-control" data-error-placement="bottom" disabled="${disabled}"/>						
						<c:if test="${disabled}">
							<div class="info-icon-wrapper right" style="left: 8px; top: -5px;">
								<span class="info-icon">
									<i class="glyphicon glyphicon-info-sign pointer" aria-hidden="true" data-toggle="tooltip" data-placement="right" data-trigger="hover" title="Email address can only be changed by HMML staff"></i>
								</span>
							</div>
						</c:if>
					</div>
				</div>
				<div class="form-group">
					<label for="firstname" class="col-sm-5 control-label text-right">First Name</label>
					<div class="col-sm-7">
						<form:input path="firstName" cssClass="form-control" data-error-placement="bottom"/>
					</div>
				</div>
				<div class="form-group">
					<label for="lastname" class="col-sm-5 control-label text-right">Last Name</label>
					<div class="col-sm-7">
						<form:input path="lastName" cssClass="form-control" data-error-placement="bottom"/>
					</div>
				</div>
	
				<div class="form-group">
					<label for="affiliation" class="col-sm-5 control-label text-right">Institutional Affiliation (optional) </label>
					<div class="col-sm-7">
						<form:input path="institution" cssClass="form-control"/>
					</div>
				</div>
				<div class="form-group">
					<label for="address" class="col-sm-5 control-label text-right" >Mailing Address <br /><small>(Street/PO Box, City, State/Province, Country, and Postal Code)</small></label>
					
					<div class="col-sm-7">
						<form:textarea path="address" cssClass="form-control"  rows="3" data-error-placement="bottom"/>						
					</div>
					
				</div>
				<div class="form-group">
					<label for="phone" class="col-sm-5 control-label text-right">Phone</label>
					<div class="col-sm-7">
						<form:input path="phone" cssClass="form-control" data-error-placement="bottom"/>
					</div>
					<div class="info-icon-wrapper">
						<span class="info-icon">
							<i class="glyphicon glyphicon-info-sign pointer" aria-hidden="true" data-toggle="tooltip" data-placement="bottom" data-trigger="hover" title="If you are unable to provide a current physical mailing address or phone number, 
							please explain the extenuating circumstances and we will contact you."></i>
						</span>
					</div>
				</div>
				<div class="form-group">
					<label for="researchInterests" class="col-sm-5 control-label text-right">Research Interests</label>
					<div class="col-sm-7">
						<form:textarea path="researchInterests" cssClass="form-control" rows="5" data-error-placement="bottom"/>
					</div>
				</div>
				<div class="form-group">
					<label for="regionAndLangInterests" class="col-sm-5 control-label text-right">Regions and Languages of Interest</label>
					<div class="col-sm-7">
						<form:textarea path="regionAndLangInterests" cssClass="form-control" rows="5" data-error-placement="bottom"/>
					</div>
				</div>
				
				<security:authorize ifAnyGranted="ROLE_ADMIN">
					<div class="form-group">
						<label for="internalNotes" class="col-sm-5 control-label text-right">Internal Notes</label>
						<div class="col-sm-7">
							<form:textarea path="internalNotes" cssClass="form-control" rows="5"/>
						</div>
					</div>
				</security:authorize>
								
				<security:authorize access="!isAuthenticated()">				
					<div class="form-group">
						<span class="col-sm-5"></span>
						<div class="col-sm-7 captchaCheckboxes">
							<div class="captchaCheckboxWrapper">
								<label for="computer"><span>I am a computer </span><form:checkbox path="computer" cssClass="captchaCheckbox" data-error-placement="left"/></label>														        
								<label for="human"><span>I am human </span><form:checkbox path="human" cssClass="captchaCheckbox" data-error-placement="left" /></label>
							</div>							    					
						</div>
					</div>										
					
					<div class="form-group">
						<span class="white-link">
							<a href="${pageContext.request.contextPath}/registration/why-we-need-this-info" class="white">Why do we want this information?</a>
						</span>						
					</div>
					
					<p class="small">
						Approval for vHMML registration may be delayed until the next business day (weekends and public holidays are excluded). 
						Information provided as part of registration is used for internal purposes only and is not shared, sold or marketed.
						Please see our <span class="white-link"><a href="${pageContext.request.contextPath}/privacy" class="white">Privacy Policy</a></span>. 
					</p>
				</security:authorize>			
							
				<div class="form-group">
					<div class="col-sm-offset-5 col-sm-7 text-right">
						<button name="cancel" type="button" class="btn btn-default" onclick="javascript:window.history.back();">Cancel</button>
						<button id="submitButton" name="submitButton" type="submit" class="btn home btn-preferred">${saveButtonLabel}</button>					
					</div>
				</div>				
			</form:form>
						
		</div>				
	</div>
</div>