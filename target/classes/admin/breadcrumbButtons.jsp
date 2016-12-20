<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<security:authorize ifAnyGranted="ROLE_ADMIN">
	<a href="${pageContext.request.contextPath}/admin/config?selectedMenuItem=config">
		<button type="button" class="btn admin">Configuration</button>
	</a>	
	
	<a href="#">
		<span class="reindexButtonWrapper">
			<button class="btn reindexButton admin" data-url="/admin/reindex">Re-Index</button>
		</span>			
	</a>
	
	<a href="${pageContext.request.contextPath}/admin/reports?selectedMenuItem=reports">
		<button type="button" class="btn admin">Reports</button>
	</a>
			
	<a href="${pageContext.request.contextPath}/admin/emailUsers?selectedMenuItem=emailUsers">
		<button type="button" class="btn admin">Send Email</button>
	</a>			
	
	<a href="${pageContext.request.contextPath}/admin/messages?selectedMenuItem=messages">
		<button type="button" class="btn admin">System Messages</button>
	</a>
		
	<a href="${pageContext.request.contextPath}/admin/users?selectedMenuItem=users">
		<button type="button" class="btn admin">Users</button>
	</a>

	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/search-admin.js?version=${applicationVersion}"></script>
</security:authorize>