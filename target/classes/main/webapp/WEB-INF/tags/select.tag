<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<%@ tag dynamic-attributes="data-atts" %>

<%@ attribute name="fieldName" required="true" rtexprvalue="true" %>
<%@ attribute name="listName" required="false" rtexprvalue="true" %>
<%@ attribute name="listIndex" required="false" rtexprvalue="true" %>
<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="printedLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="options" required="true" rtexprvalue="true" type="java.lang.Object" %>
<%@ attribute name="cssClass" required="false" rtexprvalue="true" %>
<%@ attribute name="formGroupClass" required="false" rtexprvalue="true" %>
<%@ attribute name="optionLabelProperty" required="false" rtexprvalue="true" %>
<%@ attribute name="optionValueProperty" required="false" rtexprvalue="true" %>
<%@ attribute name="isAuthorityListField" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="hasAssociatedUri" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="addButtonType" required="false" rtexprvalue="true" %>
<%@ attribute name="addButtonLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="required" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="repeatable" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="errorPlacement" required="false" rtexprvalue="true" %>
<%@ attribute name="helpText" required="false" rtexprvalue="true" %>
<%@ attribute name="helpIconClass" required="false" rtexprvalue="true" %>
<%@ attribute name="multiSelect" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="note" required="false" rtexprvalue="true" %>
<%@ attribute name="noteClass" required="false" rtexprvalue="true" %>
<%@ attribute name="labelColumns" required="false" rtexprvalue="true" %>
<%@ attribute name="fieldColumns" required="false" rtexprvalue="true" %>
<%@ attribute name="hideEmptyFirstOption" required="false" rtexprvalue="java.lang.Boolean" %>
<%@ attribute name="importedValue" required="false" rtexprvalue="true" %>

<c:if test="${labelColumns == null}">
	<c:set var="labelColumns" value="3"/>
</c:if>

<c:if test="${fieldColumns == null}">
	<c:set var="fieldColumns" value="9"/>
</c:if>

<div class="form-group ${formGroupClass}">
	<c:set var="labelClass" value="col-sm-${labelColumns} control-label"/>
	<c:if test="${required}">
		<c:set var="labelClass" value="col-sm-${labelColumns} control-label red"/>
	</c:if>
	
	<c:if test="${not empty printedLabel}">
		<%-- if there's a special label for printed items, hide the regular label on manuscript items and vice-versa --%>
		<label for="${fieldName}" class="${labelClass} hideForManuscript">
			${printedLabel}
			<c:if test="${required}"><span class="error">*</span></c:if>
		</label>
		
		<c:set var="labelClass" value="${labelClass} hideForPrinted"/>
	</c:if>
	
	<label for="${fieldName}" class="${labelClass}">
		${label}
		<c:if test="${required}"><span class="error">*</span></c:if>
	</label>
	
	<div class="col-sm-${fieldColumns}">	
		<c:set var="errorPlacement" value="${(empty errorPlacement) ? 'bottom' : errorPlacement}" />
		
		<c:set var="completeFieldName" value="${fieldName}"/>
		
		<c:if test="${not empty listName && not empty listIndex}">
			<c:set var="completeFieldName" value="${listName}[${listIndex}]"/>
			
			<!-- if it's a list but they didn't pass a field name, then it's something simple like a list of strings, e.g. alternateSurrogateFormats -->
			<c:if test="${not empty fieldName}">
				<c:set var="completeFieldName" value="${completeFieldName}.${fieldName}"/>				
			</c:if>
		</c:if>
		
		<c:if test="${isAuthorityListField}">
			<c:set var="cssClass" value="${cssClass} authorityListField"/>
		</c:if>	
		
		<c:if test="${hasAssociatedUri}">
			<c:set var="cssClass" value="${cssClass} hasAssociatedUri"/>
		</c:if>	
			
		<form:select path="${completeFieldName}" cssClass="form-control col-sm-${fieldColumns} ${cssClass}" data-value-property="${optionValueProperty}" data-error-placement="${errorPlacement}" data-field="${fieldName}" data-list-name="${listName}" data-list-index="${listIndex}">
			<c:if test="${!multiSelect && !hideEmptyFirstOption}">
				<form:option label="--- Select ---" value=""/>
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
				
				<c:set var="authorityUriLC" value=""/>
				<c:set var="authorityUriVIAF" value=""/>
				
				<c:if test="${isAuthorityListField}">				
					<c:if test="${option['authorityUriLC'] != null}">
						<c:set var="authorityUriLC" value="${option['authorityUriLC']}"/>
					</c:if>
					
					<c:if test="${option['authorityUriVIAF'] != null}">
						<c:set var="authorityUriVIAF" value="${option['authorityUriVIAF']}"/>
					</c:if>					
				</c:if>
				
				<c:set var="associatedUri" value=""/>
				
				<c:if test="${hasAssociatedUri}">
					<c:if test="${option['uri'] != null}">
						<c:set var="associatedUri" value="${option['uri']}"/>
					</c:if>
				</c:if>				
				
				<form:option value="${optionValue}" data-authority-uri-lc="${authorityUriLC}" data-authority-uri-viaf="${authorityUriVIAF}" data-associated-uri="${associatedUri}">${optionLabel}</form:option>
			</c:forEach>
		</form:select>
		
		<c:if test="${repeatable == true}">
			<button type="button" class="btn btn-success btn-xs addField"><span class="glyphicon glyphicon-plus"></span></button>
			<button type="button" class="btn btn-danger btn-xs removeField"><span class="glyphicon glyphicon-minus"></span></button>
		</c:if>	
				
		<c:if test="${addButtonType != null}">
			<security:authorize ifAnyGranted="ROLE_ADMIN">
				<c:set var="addLabel" value="New ${label}"/>
				<c:if test="${not empty addButtonLabel}">
					<c:set var="addLabel" value="New ${addButtonLabel}"/>	
				</c:if>
				<button type="button" class="btn btn-success btn-xs addListOption ${completeFieldName}" data-add-type="${addButtonType}" data-auth-list-field="${isAuthorityListField}">${addLabel}</button>
			</security:authorize>			
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
	</div>
</div>

<c:if test="${isAuthorityListField}">
	<c:set var="authFieldLabelClass" value="col-sm-3 control-label"/>
	
	<div class="form-group">
	
		<c:if test="${not empty printedLabel}">
			<%-- if there's a special label for printed items, hide the regular label on manuscript items and vice-versa --%>
			<label for="${completeFieldName}_LC_URI" class="col-sm-3 control-label hideForManuscript">
				${printedLabel} LC URL	
			</label>
			
			<c:set var="authFieldLabelClass" value="${authFieldLabelClass} hideForPrinted"/>					
		</c:if>
		
		<label for="${completeFieldName}_LC_URI" class="${authFieldLabelClass}">
			${label} LC URL	
		</label>
		<div class="col-sm-${fieldColumns}">	
			<input name="${completeFieldName}_LC_URI" type="text" value="" class="form-control authorityUriLC" disabled="disabled" data-field="${fieldName}" data-list-name="${listName}" data-list-index="${listIndex}"/>			
		</div>	
	</div>
	
	<div class="form-group">
		<c:if test="${not empty printedLabel}">
			<%-- if there's a special label for printed items, hide the regular label on manuscript items and vice-versa --%>
			<label for="${completeFieldName}_LC_URI" class="col-sm-3 control-label hideForManuscript">
				${printedLabel} VIAF URL	
			</label>								
		</c:if>
		<label for="${completeFieldName}_VIAF_URI" class="${authFieldLabelClass}">
			${label} VIAF URL	
		</label>
		<div class="col-sm-${fieldColumns}">	
			<input name="${completeFieldName}_VIAF_URI" type="text" value="" class="form-control authorityUriVIAF" disabled="disabled" data-field="${fieldName}" data-list-name="${listName}" data-list-index="${listIndex}"/>			
		</div>	
	</div>
</c:if>