<div id="advancedSearchHeading" class="panel-heading" role="tab">
	<h4 class="panel-title">
		<a data-toggle="collapse" data-parent="#searchControl" href="#advancedSearch" aria-expanded="true" aria-controls="advancedSearch">						
			Advanced Search<span class="caret"></span>
		</a>		
	</h4>									
</div>

<div id="advancedSearch" class="panel-collapse collapse" role="tabpanel" aria-labelledby="advancedSearchHeading">
	<div class="panel-body formSection">
		<form name="searchForm" onsubmit="javascript:search();return false;" class="advanced" data-search-url="/readingRoom/search" data-object-type="READING_ROOM">
			<div class="form-group">
				<label>Keyword (BETA)</label><textarea class="keyword" type="text">${vhmmlSession.savedReadingRoomSearch.searchTerms.keyword}</textarea>
			</div>
			
			<div class="form-group">
				<label>Name</label><textarea class="author" type="text">${vhmmlSession.savedReadingRoomSearch.searchTerms.author}</textarea>
			</div>
			<div class="form-group">
				<label>Title</label><textarea class="title" type="text">${vhmmlSession.savedReadingRoomSearch.searchTerms.title}</textarea>
			</div>
			<div class="form-group">
				<label>HMML Project Number</label><input class="hmmlProjectNumber" type="text" value="${vhmmlSession.savedReadingRoomSearch.searchTerms.hmmlProjectNumber}"/>
			</div>
			<div class="form-group">
				<label>Country</label><select name="country" class="country bootstrap-select" data-default="All Countries" data-size="10" value="${vhmmlSession.savedReadingRoomSearch.searchTerms.country}">
					<option value="">All Countries</option>
				</select>
			</div>
			<div class="form-group">
				<label>City</label><select name="city" class="city bootstrap-select" data-default="All Cities" data-depends-on="country" data-size="10" >
					<option value="">All Cities</option>
				</select>
			</div>
			<div class="form-group">
				<label>Repository</label><select name="repository" class="repository bootstrap-select" data-default="All Repositories" data-size="10" data-depends-on="city">
					<option value="">All Repositories</option>
				</select>
			</div>
			<div class="form-group">
				<label>Shelfmark</label><input class="shelfmark" type="text" value="${vhmmlSession.savedReadingRoomSearch.searchTerms.shelfmark}"/>
			</div>			
			<div class="form-group">
				<label>Place of Origin</label><input class="placeOfOrigin" type="text" value="${vhmmlSession.savedReadingRoomSearch.searchTerms.placeOfOrigin}"/>
			</div>
			<%-- unhide this search field after we have more subject metadata – in 2017 
			<div class="form-group">
				<label>Subject</label><input class="subject" type="text" value="${vhmmlSession.savedReadingRoomSearch.searchTerms.subject}"/>
			</div>
			--%>
			<div class="form-group">
				<label>Incipit</label><textarea class="incipit" type="text">${vhmmlSession.savedReadingRoomSearch.searchTerms.incipit}</textarea>
			</div>
			<div class="form-group">
				<label>Record Type</label><select name="objectType" class="objectType bootstrap-select" data-default="All Record Types">
					<option value="">All Record Types</option>
				</select>
			</div>
			<div class="form-group">
				<label>Language(s)</label><select name="language" class="language bootstrap-select" multiple="true" data-default="All Languages" data-size="10" title="All Languages"></select>
			</div>
			<div class="form-group">
				<label>Genre/Form(s)</label><select name="genres" class="genres bootstrap-select" multiple="true" data-default="All Genre/Forms" data-size="10" title="All Genre/Forms"></select>
			</div>
			<div class="form-group">
				<label>Feature(s)</label><select name="features" class="features bootstrap-select" multiple="true" data-default="All Features" data-size="10" title="All Features"></select>
			</div>
			<div class="form-group">
				<label>Script(s)</label><select name="script" class="script bootstrap-select" multiple="true" data-default="All Scripts" data-size="10" title="All Scripts"></select>
			</div>
			<div class="form-group">
				<label>Writing System(s)</label><select name="writingSystem" class="writingSystem bootstrap-select" multiple="true" data-size="10" data-default="All Writing Systems" title="All Writing Systems"></select>
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
			
			<button class="btn right ${pageCategory} searchButton">Search</button>
			<button type="button" class="btn right clearSearchButton">Clear</button>
			<br /><br /><p class="text-right"><a href="${pageContext.request.contextPath}/readingRoom" class="new-search">New search</a></p>
		
		</form>
	</div>
</div>