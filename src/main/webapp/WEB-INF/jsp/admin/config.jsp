<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css?version=${applicationVersion}"/>
<script src="https://cdn.ckeditor.com/4.4.7/standard/ckeditor.js"></script>
<script src="${pageContext.request.contextPath}/static/js/admin/app-config.js?version=${applicationVersion}"></script>

<br /><em>REMEMBER: separate multiple email addresses with a comma <kbd>,</kbd> (do not use semicolons).</em>
<p> </p>		
<form id="appConfigForm" name="appConfigForm" action="${pageContext.request.contextPath}/admin/config" method="post">
	<table>
		<thead>
			<tr>
				<th>Name</th>
				<th>Value</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="configValue" items="${configValues.values()}" varStatus="status">
				<tr data-config-key="${configValue.key}" data-config-type="${configValue.inputType}">
					<td>${configValue.key}</td>
					<td>
						<c:choose>
							<c:when test="${configValue.inputType == 'textarea'}">
								<form:textarea path=""/>
								<textarea name="configValue_${configValue.key}" rows="10" cols="50" class="ckEditor configValue">${configValue.value}</textarea>
							</c:when>
							<c:when test="${configValue.inputType == 'checkbox'}">
								<c:set var="checked" value=""/>
								<c:if test="${configValue.value == 'true'}">
									<c:set var="checked" value="checked"/>
								</c:if>
								<input type="checkbox" ${checked} class="configValue"/>
							</c:when>
							<c:otherwise>
								<input type="text" value="${configValue.value}" class="configValue"/>
							</c:otherwise>
						</c:choose>					
					</td>				
				</tr>
			</c:forEach>
		</tbody>
	</table>	
</form>

						
			
<button id="saveConfigButton" class="btn admin">Save</button>
