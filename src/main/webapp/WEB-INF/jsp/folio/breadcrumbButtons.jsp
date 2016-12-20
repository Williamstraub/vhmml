<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_FOLIO_CREATOR">
	<a href="${pageContext.request.contextPath}/folio/admin/add" class="breadcrumb-button">
		<button type="button" class="btn btn-success add">Add Folio Object</button>
	</a>
</security:authorize>