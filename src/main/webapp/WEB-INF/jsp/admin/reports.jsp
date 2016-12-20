<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css?version=${applicationVersion}"/>

<h3>Reports</h3>

<ul class="reports">
	<li><a href="${pageContext.request.contextPath}/admin/report/popularity">Popularity</a></li>
	<li><a href="${pageContext.request.contextPath}/admin/report/user">User</a></li>
	<li><a href="${pageContext.request.contextPath}/admin/report/object">Object</a></li>
	<li><a href="${pageContext.request.contextPath}/admin/report/cataloger">Cataloger</a></li>
</ul>

