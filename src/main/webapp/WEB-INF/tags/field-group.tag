<%-- 
	This tag can be used to make a group of fields that represent the individual attributes
	of an object. You specify a name, type, and label for each field. Valid values for the
	type attribute are "text" and "select" (for a dropdown list). If the repeatable attribute 
	is set to "true", a plus button will be added after the first field so that the group of 
	fields can be repeated. For example, a Reading Room Object has a list of Subjects.  
	Each subject has a name, LC authority URL, and a VIAF authority URL. The following example illustrates how to use
	this tag to create inputs that allow the user to create the list of Subjects:
		
	<vhmml:field-group 
		fieldName="subjects" 
		childFieldCount="3"
		items="${readingRoomObjectForm.subjects}"
		field_1_type="text"
		field_1_name="name"
		field_1_label="Subject"
		field_2_type="text"			
		field_2_name="authorityUriLC"
		field_2_label="LC Authority URL"
		field_3_type="text"			
		field_3_name="authorityUriVIAF"
		field_3_label="VIAF Authority URL"			
		repeatable="true"/> 
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags" %>

<%@ attribute name="fieldName" required="true" rtexprvalue="true" %>
<%@ attribute name="childFieldCount" required="true" rtexprvalue="true" type="java.lang.Integer" %> 
<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.Collection" %> 
<%@ attribute name="repeatable" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ tag dynamic-attributes="childFields" %>

<c:set var="cssClass" value=""/>
<c:set var="groupCssClass" value="groupedFields"/>

<c:if test="${repeatable}">
	<c:set var="cssClass" value="repeatableField"/>
	<c:set var="groupCssClass" value="${groupCssClass} repeatableFieldGroup"/>
</c:if>

<div id="${fieldName}" class="${cssClass}">
	<c:set var="fieldCount" value="0"/>
	<c:if test="${items != null}">
		<c:set var="fieldCount" value="${items.size() - 1}"/>
	</c:if>
	
	<c:forEach begin="0" end="${fieldCount}" varStatus="loopStatus">		
		<div class="${groupCssClass}">					
			<c:forEach begin="1" end="${childFieldCount}" varStatus="childFieldLoop">
				<c:set var="repeatButton" value="false"/>
				<c:if test="${repeatable && childFieldLoop.index == 0}"> 
					<c:set var="repeatButton" value="true"/>			
				</c:if>
				
				<c:set var="childFieldType" value="field_${childFieldLoop.index}_type"/>
				<c:set var="childFieldName" value="field_${childFieldLoop.index}_name"/>
				<c:set var="childFieldLabel" value="field_${childFieldLoop.index}_label"/>
				<c:set var="isDisabled" value="field_${childFieldLoop.index}_disabled"/>
				
				<c:choose>
					<c:when test="${childFields[childFieldType] == 'select'}">
						<c:set var="fieldOptions" value="field_${childFieldLoop.index}_options"/>
						<c:set var="optionLabelProp" value="field_${childFieldLoop.index}_optionLabelProperty"/>
						<c:set var="optionValueProp" value="field_${childFieldLoop.index}_optionValueProperty"/>						
						<vhmml:select fieldName="${fieldName}[${loopStatus.index}].${childFields[childFieldName]}" label="${childFields[childFieldLabel]}" options="${childFields[fieldOptions]}" optionLabelProperty="${childFields[optionLabelProp]}" optionValueProperty="${childFields[optionValueProp]}" repeatable="${repeatButton}" />
					</c:when>
					<c:otherwise>
						<vhmml:input fieldName="${fieldName}[${loopStatus.index}].${childFields[childFieldName]}" label="${childFields[childFieldLabel]}" repeatable="${repeatButton}" disabled="${childFields[isDisabled]}"></vhmml:input>
					</c:otherwise>
				</c:choose>				
			</c:forEach>												
		</div>
	</c:forEach>
</div>
