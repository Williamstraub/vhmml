<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!-- cookie api used by export to keep user from doing more than one export at a time -->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>

<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER,ROLE_LEVEL_III_CATALOGER">
		<a href="${pageContext.request.contextPath}/catalogDatabase/add">
			<button type="button" class="btn catalog-database">Add Record</button>
		</a>
		
		<security:authorize ifAnyGranted="ROLE_ADMIN">
			<div class="btn-group">
				<button type="button" class="btn catalog-database dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					Authority Lists <span class="caret"></span>
				</button>
				<ul class="dropdown-menu dropdown-menu-right">
					<li><a href="${pageContext.request.contextPath}/catalogDatabase/authorityList/CONTAINER?selectedMenuItem=list">Archival Containers</a></li>
					<li><a href="${pageContext.request.contextPath}/catalogDatabase/authorityList/CITY?selectedMenuItem=list">Cities</a></li>
					<li><a href="${pageContext.request.contextPath}/catalogDatabase/authorityList/COUNTRY?selectedMenuItem=list">Countries</a></li>
					<li><a href="${pageContext.request.contextPath}/catalogDatabase/authorityList/FEATURE?selectedMenuItem=list">Features</a></li>
					<li><a href="${pageContext.request.contextPath}/catalogDatabase/authorityList/FORMAT?selectedMenuItem=list">Formats</a></li>
					<li><a href="${pageContext.request.contextPath}/catalogDatabase/authorityList/GENRE?selectedMenuItem=list">Genres/Forms</a></li>
					<li><a href="${pageContext.request.contextPath}/catalogDatabase/authorityList/INSTITUTION?selectedMenuItem=list">Institution</a></li>
					<li><a href="${pageContext.request.contextPath}/catalogDatabase/authorityList/LANGUAGE?selectedMenuItem=list">Languages</a></li>
					<li><a href="${pageContext.request.contextPath}/catalogDatabase/authorityList/CONTRIBUTOR?selectedMenuItem=list">Names</a></li>
					<li><a href="${pageContext.request.contextPath}/catalogDatabase/authorityList/REPOSITORY?selectedMenuItem=list">Repositories</a></li>						
					<li><a href="${pageContext.request.contextPath}/catalogDatabase/authorityList/titles?selectedMenuItem=list">Titles</a></li>						
					<li><a href="${pageContext.request.contextPath}/catalogDatabase/authorityList/UNIFORM_TITLE?selectedMenuItem=list">Uniform Titles</a></li>
				</ul>
			</div>
			
			<a id="exportButton" href="${pageContext.request.contextPath}/catalogDatabase/export">
				<button type="button" class="btn catalog-database">Export Data</button>
			</a>			
			
			<a href="${pageContext.request.contextPath}/catalogDatabase/import">
				<button type="button" class="btn catalog-database">Import Data</button>
			</a>
		</security:authorize>			
			
		<div class="btn-group">
			<button type="button" class="btn catalog-database dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				References <span class="caret"></span>
			</button>
			<ul class="dropdown-menu dropdown-menu-right">
				<li><a href="${pageContext.request.contextPath}/catalogDatabase/guidelines">HMML Cataloger Guidelines and Metadata Dictionaries</a></li>
				<li><a href="${pageContext.request.contextPath}/catalogDatabase/dictionaries">Dictionaries and Cataloging Aids</a></li>
				<li><a href="${pageContext.request.contextPath}/catalogDatabase/contact">Questions? Contact a Curator or Librarian</a></li>
			</ul>
		</div>
		
		<security:authorize ifAnyGranted="ROLE_ADMIN">
			<a href="#">
				<span class="reindexButtonWrapper">
					<button class="btn reindexButton catalog-database" data-url="/catalogDatabase/reindex">Re-Index</button>
				</span>			
			</a>
		</security:authorize>

	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/search-admin.js?version=${applicationVersion}"></script>	
</security:authorize>