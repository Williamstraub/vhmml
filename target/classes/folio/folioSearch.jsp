<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="basicSearchHeading" class="panel-heading" role="tab">
	<h4 class="panel-title">
		<span data-parent="#searchControl" aria-controls="basicSearch">Search</span>
		
		<span class="menuToggleIcon reading-room-link right" data-menu="searchPanel">
			<span class="menuCloseIcon">&laquo;</span>
		</span>
	</h4>									
</div>

<div id="basicSearch" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="basicSearchHeading">					
	<div class="panel-body searchControl">
		<form name="searchForm" onsubmit="javascript:newSearch();return false;" class="basic search" data-search-url="/folio/search" data-object-type="FOLIO">
			<div class="form-group">
				<label>Keyword (BETA)</label>
				<textarea class="keyword"></textarea>
			</div>

			<div class="form-group">
				<label>Language(s)</label>
				<select name="language" class="language bootstrap-select" multiple="true" data-default="All Languages" data-size="10" title="All Languages">
					<c:forEach var="language" items="${languages}">
						<option>${language}</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="form-group">
				<label>Writing System(s)</label>
				<select name="writingSystem" class="writingSystem bootstrap-select" multiple="true" data-size="10" data-default="All Writing Systems" title="All Writing Systems">
					<c:forEach var="writingSystem" items="${writingSystems}">
						<option>${writingSystem}</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="form-group">
				<label>Script(s)</label>
				<select name="script" class="script bootstrap-select" multiple="true" data-default="All Scripts" data-size="10" title="All Scripts">
					<c:forEach var="script" items="${scripts}">
						<option>${script}</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="form-group dateRange">
				<label class="wide">Date Range</label>
				<div class="text-center small">
					<span class="minDate">1 CE</span>&nbsp;-&nbsp;<span class="maxDate"></span>
				</div>
				<input type="text" class="slider">
			</div>			 
			 
			<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER,ROLE_LEVEL_III_CATALOGER">
				<div class="checkbox">				
					<label class="wide"><input type="checkbox" name="pending" class="pendingOnly">Pending only</label>
				</div>
			</security:authorize>
			<%--magnifying glass icon: <span class="glyphicon glyphicon-search"></span>&nbsp; --%>	
			<button class="btn right ${pageCategory} searchButton">Search</button>
			<button type="button" class="btn right clearSearchButton">Clear</button>
			<br /><br /><p class="text-right"><a href="${pageContext.request.contextPath}/folio" class="new-search">New search</a></p>
		</form>
	</div>
</div>