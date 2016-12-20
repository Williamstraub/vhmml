<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="vhmml" uri="VhmmlFunctions" %>

<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="fieldName" required="true" rtexprvalue="true" %>
<%@ attribute name="listName" required="false" rtexprvalue="true" %>
<%@ attribute name="listIndex" required="false" rtexprvalue="true" %>
<%@ attribute name="cssClass" required="false" rtexprvalue="true" %>
<%@ attribute name="helpText" required="false" rtexprvalue="true" %>
<%@ attribute name="helpIconClass" required="false" rtexprvalue="true" %>
<%@ attribute name="note" required="false" rtexprvalue="true" %>
<%@ attribute name="noteClass" required="false" rtexprvalue="true" %>
<%@ attribute name="importedValue" required="false" rtexprvalue="true" %>


<div class="form-group ${cssClass}">						
	<label class="col-sm-3 control-label">${label}</label>
	<div class="col-sm-9">		
 		<c:set var="completeFieldName" value="${vhmml:getFieldName(fieldName, listName, listIndex)}"/>
			
		<label for="${completeFieldName}Year" class="inlineLabel">Year</label>
		<form:input path="${completeFieldName}Year" cssClass="form-control digitsOnly dateYear" maxlength="5" data-list-name="parts"/>
		
		<label for="${completeFieldName}Month" class="inlineLabel">Month</label>
		<form:select path="${completeFieldName}Month" cssClass="form-control dateMonth" data-list-name="${listName}">
			<form:option value="">--- Select ---</form:option>			
			<form:option value="00">January</form:option>
			<form:option value="01">February</form:option>
			<form:option value="02">March</form:option>
			<form:option value="03">April</form:option>
			<form:option value="04">May</form:option>
			<form:option value="05">June</form:option>
			<form:option value="06">July</form:option>
			<form:option value="07">August</form:option>
			<form:option value="08">September</form:option>
			<form:option value="09">October</form:option>
			<form:option value="10">November</form:option>
			<form:option value="11">December</form:option>			
		</form:select>
		
		<label for="${completeFieldName}Day" class="inlineLabel">Day</label>
		
		<form:select path="${completeFieldName}Day" class="form-control dateDay" data-list-name="${listName}">
			<form:option value="">--- Select ---</form:option>
			<c:forEach begin="1" end="31" varStatus="dayLoop">
				<form:option value="${dayLoop.index}"/>
			</c:forEach>
		</form:select>
		
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