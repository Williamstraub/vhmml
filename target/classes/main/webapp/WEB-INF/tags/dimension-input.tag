<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="fieldName" required="true" rtexprvalue="true" %>
<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="listName" required="false" rtexprvalue="true" %>
<%@ attribute name="listIndex" required="false" rtexprvalue="true" %>
<%@ attribute name="required" required="false" rtexprvalue="true" %>
<%@ attribute name="cssClass" required="false" rtexprvalue="true" %>
<%@ attribute name="helpText" required="false" rtexprvalue="true" %>
<%@ attribute name="helpIconClass" required="false" rtexprvalue="true" %>
<%@ attribute name="note" required="false" rtexprvalue="true" %>
<%@ attribute name="noteClass" required="false" rtexprvalue="true" %>
<%@ attribute name="includeDepth" required="false" rtexprvalue="true" type="java.lang.Boolean"%>
<%@ attribute name="importedValue" required="false" rtexprvalue="true" %>

<div class="form-group ${cssClass}">
	<c:set var="labelClass" value="col-sm-3 control-label"/>
	<c:if test="${required}">
		<c:set var="labelClass" value="col-sm-3 control-label red"/>
	</c:if>
	<label for="${fieldName}" class="${labelClass}">
		${label}
		<c:if test="${required}"><span class="error">*</span></c:if>		
	</label>
	<div class="col-sm-9">
		<c:set var="completeFieldName" value="${fieldName}"/>
		
		<c:if test="${not empty listName && not empty listIndex}">
			<c:set var="completeFieldName" value="${listName}[${listIndex}]"/>
			
			<!-- if it's a list but they didn't pass a field name, then it's something simple like a list of strings, e.g. alternateSurrogateFormats -->
			<c:if test="${not empty fieldName}">
				<c:set var="completeFieldName" value="${completeFieldName}.${fieldName}"/>				
			</c:if>
		</c:if>
		
		<label class="inlineLabel">Height</label>
		<form:input path="${completeFieldName}Height" cssClass="form-control dimension-input height positiveDecimalsOnly" data-list-name="${listName}" data-list-index="${listIndex}"/><span class="dimension-label">x</span>
		<label class="inlineLabel">Width</label>
		<form:input path="${completeFieldName}Width" cssClass="form-control dimension-input width positiveDecimalsOnly" data-list-name="${listName}" data-list-index="${listIndex}"/>
		
		<c:if test="${includeDepth}">
			<span class="dimension-label">x</span>
			<label class="inlineLabel">Thickness</label>
			<form:input path="${completeFieldName}Depth" cssClass="form-control dimension-input depth positiveDecimalsOnly" data-list-name="${listName}" data-list-index="${listIndex}"/>
		</c:if>
		
		<span class="dimension-label">cm</span>
		
		<c:if test="${helpText != null}">
			<i class="glyphicon glyphicon-question-sign ${helpIconClass}" data-toggle="tooltip" title="${helpText}" data-placement="right" data-trigger="click hover"></i>
		</c:if>
		
		<c:if test="${not empty note}">
			<label class="note ${noteClass}">${note}</label>
			<c:if test="${noteClass == 'importedValue'}">
				<span class="glyphicon glyphicon-remove-circle error" title="Remove imported value"/>
			</c:if>
		</c:if>
		
		<c:if test="${not empty importedValue}">		
			<c:choose>
				<c:when test="${not empty listName && not empty listIndex}">			
					<c:set var="importFieldName" value="${listName}[${listIndex}].${importedValue}"/>				
					<c:set var="importValue" value="${readingRoomObjectForm[listName][listIndex][importedValue]}"/>				
				</c:when>
				<c:otherwise>
					<c:set var="importFieldName" value="${importedValue}"/>
					<c:set var="importValue" value="${readingRoomObjectForm[importedValue]}"/>
				</c:otherwise>
			</c:choose>
			
			<c:if test="${not empty importValue}">
				<form:hidden path="${importFieldName}" cssClass="importedValue"/>		 			
	 			<label class="note importedValue">Imported Value: ${importValue}</label>
				<span class="glyphicon glyphicon-remove-circle error" title="Remove imported value" data-field="${importFieldName}"/>
			</c:if>			
		</c:if>
		
		<%-- <c:if test="${not empty importedValue and not empty readingRoomObjectForm[listName][listIndex][importedValue]}">		
			<c:if test="${not empty listName && not empty listIndex}">			
				<c:set var="importFieldName" value="${listName}[${listIndex}].${importedValue}"/>				
			</c:if>			
			<form:hidden path="${importFieldName}" cssClass="importedValue"/>		 			
 			<label class="note importedValue">Imported Value: ${readingRoomObjectForm[listName][listIndex][importedValue]}</label>
			<span class="glyphicon glyphicon-remove-circle error" title="Remove imported value" data-field="${importFieldName}"/>
		</c:if> --%>					
	</div>
</div>