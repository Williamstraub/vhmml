<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="basicSearchHeading" class="panel-heading" role="tab">
	<h4 class="panel-title">
		<a data-toggle="collapse" data-parent="#searchControl" href="#basicSearch" aria-expanded="true" aria-controls="basicSearch">						
			Basic Search <span class="caret"></span>
		</a>
		
		<span class="menuToggleIcon reading-room-link right" data-menu="searchPanel">
			<span class="menuCloseIcon">&laquo;</span>
		</span>
	</h4>									
</div>

<div id="basicSearch" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="basicSearchHeading">					
	<div class="panel-body">
		<form name="searchForm" onsubmit="javascript:newSearch();return false;" class="basic" data-search-url="/readingRoom/search" data-object-type="READING_ROOM">
			<div class="form-group">
				<label>Keyword&nbsp;&nbsp;&nbsp;(Excluding Project Number)</label><textarea class="keyword">${vhmmlSession.savedReadingRoomSearch.searchTerms.keyword}</textarea>
			</div>

			<div class="form-group">
				<label>Name</label><textarea class="author">${vhmmlSession.savedReadingRoomSearch.searchTerms.author}</textarea>
			</div>

			<div class="form-group">
				<label>Title</label><textarea class="title">${vhmmlSession.savedReadingRoomSearch.searchTerms.title}</textarea>
			</div>
			
			<div class="form-group">
				<label>HMML Project Number</label><input class="hmmlProjectNumber" type="text" value="${vhmmlSession.savedReadingRoomSearch.searchTerms.hmmlProjectNumber}"/>
			</div>

			<div class="form-group">
				<label>Language(s)</label><select name="language" class="language bootstrap-select" multiple="true" data-default="All Languages" data-size="10" title="All Languages"></select>
			</div>
			<div class="form-group">				
				<label>Record Type</label><select name="objectType" class="objectType bootstrap-select">
					<option value="">All Record Types</option>
				</select>
			</div>
			<div class="form-group dateRange">
				<label class="wide">Date Range</label>
				<div class="text-center small">
					<span class="minDate">1 CE</span>&nbsp;-&nbsp;<span class="maxDate"></span>
				</div>
				<input type="text" class="slider">
			</div>

			<div class="checkbox">
				<c:set var="checked" value="checked='checked'"/>
				<c:if test="${vhmmlSession.savedReadingRoomSearch.searchTerms.format != null && !vhmmlSession.savedReadingRoomSearch.listTermContains('format', 'digital')}">
					<c:set var="checked" value=""/>
				</c:if>
				<label class="wide"><input type="checkbox" ${checked} class="formatFilter" data-value="digital">Digital images</label>				
			</div>

			<div class="checkbox">
				<c:set var="checked" value="checked='checked'"/>
				<c:if test="${vhmmlSession.savedReadingRoomSearch.searchTerms.format != null && !vhmmlSession.savedReadingRoomSearch.listTermContains('format', 'scanned microform')}">
					<c:set var="checked" value=""/>
				</c:if>
				<label class="wide"><input type="checkbox" ${checked} class="formatFilter" data-value="scanned microform">Scanned microform</label>
			</div>

			<div class="checkbox">
				<c:set var="checked" value="checked='checked'"/>
				<c:if test="${vhmmlSession.savedReadingRoomSearch.searchTerms.format != null && !vhmmlSession.savedReadingRoomSearch.listTermContains('format', 'microform')}">
					<c:set var="checked" value=""/>
				</c:if>
				<label class="wide"><input type="checkbox" ${checked} class="formatFilter" data-value="microform">Microform</label>
			</div>
			
			<%-- hid until downloadable functionality is available sometime after July 2016
			<div class="checkbox">
				<c:set var="checked" value=""/>
				<c:if test="${vhmmlSession.savedReadingRoomSearch.searchTerms.downloadable}">
					<c:set var="checked" value="checked='checked'"/>
				</c:if>
				<label class="wide"><input type="checkbox" ${checked} name="downloadable" class="downloadable">Downloadable</label>
			</div>
			 --%>
			 
			 
			<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER,ROLE_LEVEL_III_CATALOGER,ROLE_SCHOLAR">
				<div class="checkbox">				
					<label class="wide"><input type="checkbox" name="pending" class="pendingOnly">Pending only</label>
				</div>
			</security:authorize>
			<%--magnifying glass icon: <span class="glyphicon glyphicon-search"></span>&nbsp; --%>	
			<button class="btn right ${pageCategory} searchButton">Search</button>
			<button type="button" class="btn right clearSearchButton">Clear</button>
			<br /><br /><p class="text-right"><a href="${pageContext.request.contextPath}/readingRoom" class="new-search">New search</a></p>
		</form>
	</div>
</div>