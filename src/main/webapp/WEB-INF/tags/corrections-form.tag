<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<%@ attribute name="correctionType" required="true" rtexprvalue="true" %>
<%@ attribute name="categories" required="false" rtexprvalue="true" %>
<%@ attribute name="hmmlProjectNumber" required="false" rtexprvalue="true" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/corrections.css"/>

<div id="correctionsFormWrapper" class="hidden">
	<form id="correctionsFormTemplate" name="correctionForm" class="correctionsForm">
		<input id="correctionType" type="hidden" name="type" value="${correctionType}"/>
		<input id="hmmlProjectNumber" name="hmmlProjectNumber" type="hidden" value="${hmmlProjectNumber}"/>
		
		<div class="row">
			<div class="col-lg-12">
				<textarea name="corrections" rows="7" cols="50"></textarea>
			</div>
		</div>		
		
		<c:if test="${not empty categories}">
			<div class="row">
				<div class="col-lg-7 text-left">
					Please choose the category or
					categories for your comments.
					This will help us route the
					information to the proper
					HMML staff member.
				</div>
				<div class="col-lg-5 text-right">
					<select name="categories" multiple="true">
						<c:forEach var="category" items="${categories}">
							<option>${category}</option>
						</c:forEach>
					</select>
				</div>
			</div>								
		</c:if>
		
		<label>If necessary, may we contact you about this correction/addition?</label>
		<input class="allowContactYes" type="radio" name="allowContact" value="true"/><label for="allowContactYes">yes</label>
		<input class="allowContactNo" type="radio" name="allowContact" value="false" checked="checked"/><label for="allowContactNo">no</label>
	
		<security:authorize access="!isAuthenticated()">
			<div class="contactEmailWrapper">
				<label for="contactEmail">email</label>
				<input type="text" name="contactEmail" class="contactEmail" disabled="disabled"/>
			</div>
		</security:authorize>	
	</form>
</div>