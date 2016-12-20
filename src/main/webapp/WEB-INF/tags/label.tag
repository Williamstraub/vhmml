<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="fieldName" required="false" rtexprvalue="true" %>
<%@ attribute name="label" required="false" rtexprvalue="true" %>
<%@ attribute name="labelClass" required="false" rtexprvalue="true" %>
<%@ attribute name="printedLabel" required="false" rtexprvalue="true" %>
<%@ attribute name="required" required="false" rtexprvalue="true" %>
<%@ attribute name="deprecated" required="false" rtexprvalue="true" type="java.lang.Boolean" %>

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