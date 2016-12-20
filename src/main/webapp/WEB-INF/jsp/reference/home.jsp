<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/reference.css?version=${applicationVersion}" />

<div class="row">

	<div class="col-lg-3">	
		<div class="block-image-wrapper">
			<div>
				<img src="${pageContext.request.contextPath}/static/img/placeholder.png" class="block-image" title="selection from ..." />
			</div>
			<div class="info-icon-wrapper">
				<span class="info-icon">
					<i class="glyphicon glyphicon-info-sign pointer" aria-hidden="true" data-toggle="tooltip" title="Image from ... Used under a CC BY-NC 4.0 license." data-placement="right" data-trigger="hover"></i>
				</span>
			</div>			
		</div>		
	</div>
	<div class="col-lg-8 text-center">
		<p>
			Reference contains bibliographical resources searchable by keyword, title, or author's last name. Links to digitized versions 
			are provided when these are available. We welcome corrections and suggestions using the "Corrections or additions?" link. 
		</p>

		<div>
			<form id="searchForm" name="searchForm" class="searchForm" onsubmit="javascript:newSearch();return false;">
				<label for="searchString">Search by</label>
				<div id="searchBy" class="btn-group">
					<button type="button" class="btn reference dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
						<span class="selected">Keyword</span> <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu">
						<li><a href="#" data-value="AUTHOR" class="AUTHOR">Author</a></li>
						<li><a href="#" data-value="TITLE" class="TITLE">Title</a></li>
						<li><a href="#" data-value="KEYWORD" class="KEYWORD">Keyword</a></li>
					</ul>
				</div>
				<input id="searchType" type="hidden" name="searchType" value="${searchType}" /> <input id="searchString" type="text" name="searchString" value="${searchString}" />
				<button id="searchButton" class="btn reference searchButton">Go</button>
				<button id="clearSearchButton" class="btn btn-default clearSearchButton">Clear</button>
			</form>

		</div>
	</div>
</div>
<div class="row">
	<security:authorize ifAnyGranted="ROLE_REFERENCE_CREATOR,ROLE_ADMIN">
		<div class="col-sm-12 text-right actionButtons">
			<button id="deleteByCollection" type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteByGroupDialog">Delete by Zotero Collection</button>
			<div class="btn-group">
				<button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Add <span class="caret"></span></button>
				<ul class="dropdown-menu" role="menu">					
					<li><a href="${pageContext.request.contextPath}/reference/admin/import">Import</a></li>
				</ul>
			</div>
			<span class="reindexButtonWrapper">
				<button class="btn btn-primary reindexButton" data-url="/reference/admin/reindex">Re-Index</button>
			</span>
		</div>

		<!-- delete by group modal -->
		<div id="deleteByGroupDialog" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Delete Citations by Zotero Collection</h4>
					</div>
					<div class="modal-body">
						<vhmml:zotero-items-tree referenceGroups="${referenceGroups}" onSelectNode="selectReferenceItem" showImportedCount="true" hideEmptyCollections="true" />
					</div>
					<div class="modal-footer">
						<button id="deleteByCollectionButton" type="button" class="btn btn-danger disabled">Delete items from selected collection</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					</div>
				</div>
			</div>
		</div>
	</security:authorize>

	<vhmml:paging-controls tableId="referenceTable" searchFunction="referenceSearch" sortBy="${sortBy}" pageSize="${pageSize}" currentPage="${currentPage}" pageCategory="reference" showJumpToPage="true"/>

	<br />
	
	<div id="invalidSearchMessage" class="alert alert-warning" role="alert">
		<span class="glyphicon glyphicon-exclamation-sign"></span>Please enter at least 2 characters to perform a text search.		
	</div>
	
	<div id="noResultsMessage" class="alert alert-warning" role="alert">
		<span class="glyphicon glyphicon-exclamation-sign"></span>No results found for search
		<a href="javascript:showCorrectionsDialog();" class="right"><img src="${pageContext.request.contextPath}/static/img/glyphicons-conversation2.png" width="20" alt="" />&nbsp;Corrections or additions?</a> 
	</div>

	<div class="table-responsive">
		<table id="referenceTable" class="table table-striped table-nowrap"></table>
	</div>

	<vhmml:paging-controls tableId="referenceTable" searchFunction="referenceSearch" sortBy="${sortBy}" pageSize="${pageSize}" currentPage="${currentPage}" pageCategory="reference" showJumpToPage="true"/>

</div>

<vhmml:corrections-form correctionType="REFERENCE" />

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/corrections.js?version=${applicationVersion}"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/reference.js?version=${applicationVersion}"></script>

<script type="text/javascript">	
	
	<c:if test="${searchType != null}">
		selectedSearchType = '${searchType}';
	</c:if>	
	<c:if test="${initialSearchResult != null}">
		initialSearchResult = ${initialSearchResult}; 
	</c:if>

	<c:if test="${selectedEntry != null}">
		selectedEntry = ${selectedEntry};
	</c:if>
	
	<c:if test="${sortBy != null}">
		initialSortBy = '${sortBy}';
	</c:if>
	
	<security:authorize ifAnyGranted="ROLE_REFERENCE_CREATOR,ROLE_ADMIN">
		isReferenceAdmin = true;
	</security:authorize>
</script>

<security:authorize ifAnyGranted="ROLE_REFERENCE_CREATOR,ROLE_ADMIN">
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/reference-admin.js?version=${applicationVersion}"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/search-admin.js?version=${applicationVersion}"></script>
</security:authorize>