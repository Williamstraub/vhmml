<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ tag dynamic-attributes="dynamicAtts" %>

<%@ attribute name="fieldName" required="false" rtexprvalue="true" %>
<%@ attribute name="label" required="false" rtexprvalue="true" %>
<%@ attribute name="labelClass" required="false" rtexprvalue="true" %>
<%@ attribute name="printedLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="listName" required="false" rtexprvalue="true" %>
<%@ attribute name="listIndex" required="false" rtexprvalue="true" %>
<%@ attribute name="required" required="false" rtexprvalue="true" %>
<%@ attribute name="hidden" required="false" rtexprvalue="true" %>
<%@ attribute name="cssClass" required="false" rtexprvalue="true" %>
<%@ attribute name="helpText" required="false" rtexprvalue="true" %>
<%@ attribute name="helpIconClass" required="false" rtexprvalue="true" %>
<%@ attribute name="repeatable" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="errorPlacement" required="false" rtexprvalue="true" %>
<%@ attribute name="textarea" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="disabled" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="note" required="false" rtexprvalue="true" %>
<%@ attribute name="noteClass" required="false" rtexprvalue="true" %>
<%@ attribute name="importedValue" required="false" rtexprvalue="true" %>
<%@ attribute name="deprecated" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="fieldValue" required="false" rtexprvalue="true" %>

<div class="form-group ${cssClass}">
	<c:set var="labelClass" value="col-sm-3 control-label ${labelClass}"/>
	<c:if test="${required}">
		<c:set var="labelClass" value="${labelClass} red"/>
	</c:if>	
	
	<c:if test="${not empty printedLabel}">
		<%-- if there's a special label for printed items, hide the regular label on manuscript items and vice-versa --%>
		<label for="${fieldName}" class="${labelClass} hideForManuscript">
			${printedLabel}
			<c:if test="${required}"><span class="error">*</span></c:if>
			
			<c:if test="${deprecated}">
				&nbsp;<em class="highlight deprecated">(deprecated)</em>
			</c:if>
		</label>
		
		<c:set var="labelClass" value="${labelClass} hideForPrinted"/>
	</c:if>	
	
	<label for="${fieldName}" class="${labelClass}">
		${label}
		<c:if test="${required}"><span class="error">*</span></c:if>
		<c:if test="${deprecated}">
			&nbsp;<em class="highlight deprecated">(deprecated)</em>
		</c:if>
	</label>
	<div class="col-sm-9">
		<c:set var="errorPlacement" value="${(empty errorPlacement) ? 'bottom' : errorPlacement}" />
		<c:set var="customAttributes" value='test="test"'/>
		
		<c:forEach var="att" items="${dynamicAtts}">
			<c:set var="customAttributes" value='${customAttributes} ${att.key}="${att.value}"'/>
		</c:forEach>
		
		<c:set var="completeFieldName" value="${fieldName}"/>
		
		<c:if test="${not empty listName && not empty listIndex}">
			<c:set var="completeFieldName" value="${listName}[${listIndex}]"/>
			
			<!-- if it's a list but they didn't pass a field name, then it's something simple like a list of strings, e.g. alternateSurrogateFormats -->
			<c:if test="${not empty fieldName}">
				<c:set var="completeFieldName" value="${completeFieldName}.${fieldName}"/>				
			</c:if>
		</c:if>
		
		<c:choose>
			<c:when test="${textarea}">
				<form:textarea path="${completeFieldName}" cssClass="form-control ${cssClass}" disabled="${disabled}" rows="5" cols="80" data-error-placement="${errorPlacement}" data-field="${fieldName}" data-list-name="${listName}" data-list-index="${listIndex}"></form:textarea>
			</c:when>
			<c:when test="${hidden}">
				<input type="hidden" name="${completeFieldName}" data-field="${fieldName}" data-list-name="${listName}" data-list-index="${listIndex}" value="${fieldValue}" class="${cssClass}"/>
			</c:when>
			<c:otherwise>
				<form:input path="${completeFieldName}" cssClass="form-control ${cssClass}" disabled="${disabled}" data-error-placement="${errorPlacement}" data-field="${fieldName}" data-list-name="${listName}" data-list-index="${listIndex}"/>
			</c:otherwise>
		</c:choose>
		
		<c:if test="${repeatable == true}">
			<button type="button" class="btn btn-success btn-xs addField"><span class="glyphicon glyphicon-plus"></span></button>
			<button type="button" class="btn btn-danger btn-xs removeField"><span class="glyphicon glyphicon-minus"></span></button>
		</c:if>
		
		<c:if test="${helpText != null}">
			<i class="glyphicon glyphicon-question-sign ${helpIconClass}" data-toggle="tooltip" title="${helpText}" data-placement="right" data-trigger="click hover"></i>			
		</c:if>
		
		<c:if test="${not empty note}">
			<label class="note ${noteClass}">${note}</label>
			<c:if test="${noteClass == 'importedValue'}">
				<span class="glyphicon glyphicon-remove-circle error" title="Remove imported value"/>
			</c:if>
		</c:if>
		
		<c:if test="${not empty importedValue and not empty readingRoomObjectForm[listName][listIndex][importedValue]}">		
			<c:if test="${not empty listName && not empty listIndex}">			
				<c:set var="importFieldName" value="${listName}[${listIndex}].${importedValue}"/>				
			</c:if>			
			<form:hidden path="${importFieldName}" cssClass="importedValue"/>		 			
 			<label class="note importedValue">Imported Value: ${readingRoomObjectForm[listName][listIndex][importedValue]}</label>
			<span class="glyphicon glyphicon-remove-circle error" title="Remove imported value" data-field="${importFieldName}"/>
		</c:if>	
	</div>
</div>	