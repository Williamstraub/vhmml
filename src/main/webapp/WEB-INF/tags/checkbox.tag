<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags" %>

<%@ attribute name="fieldName" required="true" rtexprvalue="true" %>
<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="printedLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="listName" required="false" rtexprvalue="true" %>
<%@ attribute name="listIndex" required="false" rtexprvalue="true" %>
<%@ attribute name="cssClass" required="false" rtexprvalue="true" %>
<%@ attribute name="required" required="false" rtexprvalue="true" %>
<%@ attribute name="helpText" required="false" rtexprvalue="true" %>
<%@ attribute name="helpIconClass" required="false" rtexprvalue="true" %>

<div class="form-group">
	<vhmml:label fieldName="${fieldName}" label="${label}" labelClass="${labelClass}" printedLabel="${printedLabel}" required="${required}" />	
	
	<div class="col-sm-9">
		<c:set var="completeFieldName" value="${fieldName}"/>
		
		<c:if test="${not empty listName && not empty listIndex}">
			<c:set var="completeFieldName" value="${listName}[${listIndex}]"/>
			
			<!-- if it's a list but they didn't pass a field name, then it's something simple like a list of strings, e.g. alternateSurrogateFormats -->
			<c:if test="${not empty fieldName}">
				<c:set var="completeFieldName" value="${completeFieldName}.${fieldName}"/>				
			</c:if>
		</c:if>
		
		<form:checkbox path="${completeFieldName}" cssClass="form-control ${cssClass}" data-list-name="${listName}" data-list-index="${listIndex}"/>	
		
		<c:if test="${helpText != null}">
			<i class="glyphicon glyphicon-question-sign ${helpIconClass}" data-toggle="tooltip" title="${helpText}" data-placement="right" data-trigger="click hover"></i>			
		</c:if>
	</div>	
</div>	