<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-switch.min.css"/>	

<style type="text/css">
	.icons span.glyphicon {
		margin: 0 5px;
		cursor: pointer;
		top: 6px;
	}
	
	#editUserDialog {
		display: none;
	}
</style>

<div>
	<form name="searchForm" class="searchForm" id="adminSearchForm" onsubmit="javascript:newSearch();return false;">
		<label for="searchString" style="width: auto;">Search by</label>
		<div id="searchBy" class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
				<span class="selected">email</span> <span class="caret"></span>
			</button>
			<ul class="dropdown-menu" role="menu">
				<li><a href="#" data-value="LAST_NAME">last name</a></li>
				<li><a href="#" data-value="FIRST_NAME">first name</a></li>
				<li><a href="#" data-value="EMAIL">email</a></li>						    
				<li><a href="#" data-value="INSTITUTION">institution</a></li>						    
			</ul>
		</div>		
		<input id="searchType" type="hidden" name="searchType" value="${searchType}"/>
		<input id="searchString" type="text" name="searchString" value="${searchString}"/>
		<button id="searchButton" class="btn admin searchButton"><span class="glyphicon glyphicon-search"></span>&nbsp;Go</button>
		<button id="clearSearchButton" class="btn btn-default clearSearchButton" onclick="clearSearch()">&nbsp;Clear</button>
		
	</form>
		
</div>
		
<div class="row">
	<vhmml:paging-controls tableId="usersTable" searchFunction="userSearch" sortBy="${sortBy}" pageSize="${pageSize}" currentPage="${currentPage}" pageCategory="admin" showJumpToPage="true"/>
	
	<div id="noResultsMessage" class="alert alert-warning" role="alert"><span class="glyphicon glyphicon-exclamation-sign"></span>No results found for search</div>		
	
	<div class="table-responsive">		
		<table id="usersTable" class="table table-striped table-hover">				
			<thead>
				<tr>
					<th class="col-lg-2"><a href="javascript:sort('username');">Email</a></th>
					<th class="col-lg-2"><a href="javascript:sort('lastName');">User's Name</a></th>
					<th class="col-lg-2"><a href="javascript:sort('institution');">Institution</a></th>
					<th class="col-lg-2"><a href="javascript:sort('lastLogin');">Last Login</a></th>
					<th class="col-lg-1">Enabled</th>
					<th class="col-lg-1">Actions</th>												
				</tr>
			</thead>
			<tbody></tbody>		
		</table>			
	</div>		
	
	<vhmml:paging-controls tableId="usersTable" searchFunction="userSearch" sortBy="${sortBy}" pageSize="${pageSize}" currentPage="${currentPage}" pageCategory="admin" showJumpToPage="true"/>
	
</div>

<c:if test="${searchResult != null}">
	<script type="text/javascript">
		var initialSearchResult = ${searchResult};
		var roles = ${rolesJs};
	</script>	
</c:if>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/user-admin.js?version=${applicationVersion}"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/bootstrap-switch.min.js"></script>

<div id="editUserDialog">
	<label class="heading">Roles</label>

	<table class="userRoles table table-striped table-hover">				
		<thead>
			<tr>
				<c:forEach var="role" items="${roles}">
					<th class="col-lg-1 text-center" data-role-name="${role.name}">${role.displayName}</th>	
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="role" items="${roles}">
				<td class="text-center"><input type="checkbox" class="role" data-role-name="${role.name}" /></td>	
			</c:forEach>			
		</tbody>		
	</table>
</div>

<script>

function clearSearch() {
	document.getElementById("adminSearchForm").reset();		
};	

</script>