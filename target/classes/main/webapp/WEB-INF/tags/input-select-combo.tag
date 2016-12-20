<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<%@ tag dynamic-attributes="data-atts" %>

<%--
	This tag is used to render a text field and select list on the same line for the 
	purpose of inputting a number and selecting an option from a corresponding list. For
	example, the "extent" field needs to have values that are constrained to things like
	"200 Pages". If the "fieldName" attribute is specified, it is expected that the 
	form object will have an object object attribute with "count" and "type" properties.
	This is how the "extent" example works, ReadingRoomObjectForm.Extent is an object
	with count and type properties. The other option is to specify the countFieldName
	and typeFieldName properties. These properties should map to separate fields on the
	form object.
 --%>
<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="fieldName" required="false" rtexprvalue="true" %>
<%@ attribute name="listName" required="false" rtexprvalue="true" %>
<%@ attribute name="listIndex" required="false" rtexprvalue="true" %>
<%@ attribute name="countFieldName" required="false" rtexprvalue="true" %>
<%@ attribute name="typeFieldName" required="false" rtexprvalue="true" %>
<%@ attribute name="options" required="true" rtexprvalue="true" type="java.lang.Object" %>
<%@ attribute name="cssClass" required="false" rtexprvalue="true" %>
<%@ attribute name="countFieldCssClass" required="false" rtexprvalue="true" %>
<%@ attribute name="optionLabelProperty" required="false" rtexprvalue="true" %>
<%@ attribute name="optionValueProperty" required="false" rtexprvalue="true" %>
<%@ attribute name="required" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="repeatable" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="helpText" required="false" rtexprvalue="true" %>
<%@ attribute name="helpIconClass" required="false" rtexprvalue="true" %>
<%@ attribute name="multiSelect" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="note" required="false" rtexprvalue="true" %>
<%@ attribute name="noteClass" required="false" rtexprvalue="true" %>
<%@ attribute name="importedValue" required="false" rtexprvalue="true" %>
<%@ attribute name="addButtonType" required="false" rtexprvalue="true" %>
<%@ attribute name="addButtonLabel" required="false" rtexprvalue="true" %>

<div class="form-group inline ${cssClass}">
	<c:set var="labelClass" value="col-sm-3 control-label"/>
	<c:if test="${required}">
		<c:set var="labelClass" value="col-sm-3 control-label red"/>
	</c:if>
	<label for="${fieldName}" class="${labelClass}">	
		${label}
		<c:if test="${required}"><span class="error">*</span></c:if>
	</label>	
	
	<div class="col-sm-9">
		<!-- default to use "count" & "type" attributes for the count & type fields, e.g. if field name is extent, then input fields are extent.count & extent.type -->
		<c:if test="${empty countFieldName}">
			<c:set var="countFieldName" value="count"/>			
		</c:if>
		
		<c:if test="${empty typeFieldName}">
			<c:set var="typeFieldName" value="type"/>
		</c:if>
		
		<c:if test="${fieldName != null}">			
			<c:set var="countFieldName" value="${fieldName}.${countFieldName}"/>
			<c:set var="typeFieldName" value="${fieldName}.${typeFieldName}"/>
		</c:if>
		
		<c:if test="${not empty listName && not empty listIndex}">			
			<c:set var="countFieldName" value="${listName}[${listIndex}].${countFieldName}"/>
			<c:set var="typeFieldName" value="${listName}[${listIndex}].${typeFieldName}"/>	
		</c:if>
		
		<form:input path="${countFieldName}" cssClass="form-control combo-field ${countFieldCssClass}" data-field="${fieldName}" data-list-name="${listName}" data-list-index="${listIndex}"/>			
		<form:select path="${typeFieldName}" cssClass="form-control combo-field ${cssClass}" data-field="${fieldName}" data-list-name="${listName}" data-list-index="${listIndex}">
			<c:if test="${!multiSelect}">
				<option label="--- Select ---" value=""/>
			</c:if>			
			<c:forEach var="option" items="${options}">
				
				<c:set var="optionLabel" value="${option}"/>
				<c:if test="${optionLabelProperty != null}">
					<c:set var="optionLabel" value="${option[optionLabelProperty]}"/>
				</c:if>
				
				<c:set var="optionValue" value="${option}"/>
				<c:if test="${optionValueProperty != null}">
					<c:set var="optionValue" value="${option[optionValueProperty]}"/>
				</c:if>
				
				<form:option value="${optionValue}">${optionLabel}</form:option>
			</c:forEach>
		</form:select>
		
		<c:if test="${repeatable == true}">
			<button type="button" class="btn btn-success btn-xs addField"><span class="glyphicon glyphicon-plus"></span></button>
			<button type="button" class="btn btn-danger btn-xs removeField"><span class="glyphicon glyphicon-minus"></span></button>
		</c:if>
		
		<c:if test="${addButtonType != null}">
			<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER">
				<c:set var="addLabel" value="New ${label}"/>
				<c:if test="${not empty addButtonLabel}">
					<c:set var="addLabel" value="New ${addButtonLabel}"/>	
				</c:if>
				<button type="button" class="btn btn-success btn-xs addListOption ${completeFieldName}" data-add-type="${addButtonType}" data-auth-list-field="${isAuthorityListField}">${addLabel}</button>
			</security:authorize>			
		</c:if>
		
		<c:if test="${not empty note and (listIndex == null or listIndex == 0)}">
			<label class="note ${noteClass}">${note}</label>
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