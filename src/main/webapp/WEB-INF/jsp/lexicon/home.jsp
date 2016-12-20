<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags"%>

<style type="text/css">
	.content {
		text-align: center;
	}
	
	button.lexicon, button.lexicon:hover {
		display: inline;
	}
	
	span.icon {
		top: -2px;
		position: relative
	}
	
	.table-row-caption p {
		padding: 0;
		display: inline;
	}
</style>

<div class="row">
	<div class="col-lg-3">
		<div class="block-image-wrapper">
			<div>
				<img src="${pageContext.request.contextPath}/static/img/placeholder.png" class="block-image" />
			</div>
			<div class="info-icon-wrapper">
				<span class="info-icon">
					<i class="glyphicon glyphicon-info-sign pointer" aria-hidden="true" data-toggle="tooltip" title="Image from __. Used under a CC BY-NC 4.0 license." data-placement="right" data-trigger="hover"></i>
				</span>
			</div>			
		</div>
	</div>
	<div class="col-lg-6 text-center">
		<p>
			Lexicon introduces terms used in manuscript studies. You can search by term or browse entries alphabetically. Note: Lexicon is a work in progress. We welcome improvements: please use the "Corrections or additions?" link to send us your suggestions.
		</p>
		<div>
			<form id="searchForm" name="searchForm" class="searchForm" onsubmit="return false;">
				<label>Search by Term</label> <input id="searchText" type="text" name="searchText" />
				<button id="searchButton" class="btn lexicon searchButton">Go</button>
				<button id="clearSearchButton" class="btn btn-default searchButton">Clear</button>
			</form>
		</div>

		<div class="searchLetters">
			
			<div class="searchLetterRow">
				<span>Browse&nbsp;</span>
				<span class="searchLetter default" title="A">A</span>
				<span class="searchLetter" title="B">B</span> 
				<span class="searchLetter" title="C">C</span> 
				<span class="searchLetter" title="D">D</span> 
				<span class="searchLetter" title="E">E</span>
				<span class="searchLetter" title="F">F</span>
				<span class="searchLetter" title="G">G</span>
				<span class="searchLetter" title="H">H</span>
				<span class="searchLetter" title="I">I</span>
				<span class="searchLetter" title="J">J</span>
				<span class="searchLetter" title="K">K</span>
				<span class="searchLetter" title="L">L</span>
				<span class="searchLetter" title="M">M</span>
			</div>

			<div class="searchLetterRow">
			
				<span class="searchLetter" title="N">N</span> 
				<span class="searchLetter" title="O">O</span> 
				<span class="searchLetter" title="P">P</span> 
				<span class="searchLetter" title="Q">Q</span> 
				<span class="searchLetter" title="R">R</span> 
				<span class="searchLetter" title="S">S</span> 
				<span class="searchLetter" title="T">T</span> 
				<span class="searchLetter" title="U">U</span> 
				<span class="searchLetter" title="V">V</span> 
				<span class="searchLetter" title="W">W</span> 
				<span class="searchLetter" title="X">X</span> 
				<span class="searchLetter" title="Y">Y</span> 
				<span class="searchLetter" title="Z">Z</span>
			</div>

		</div>
	</div>
</div>

<div class="row">
	<vhmml:paging-controls tableId="lexiconTable"
		searchFunction="lexiconSearch" onNextButton="refreshSelectedLetter"
		onPrevButton="refreshSelectedLetter" pageCategory="lexicon"
		pageSize="${pageSize}" sortBy="${sortBy}"/>

	<div id="invalidSearchMessage" class="alert alert-warning" role="alert">
		<span class="glyphicon glyphicon-exclamation-sign"></span>Please enter at least 2 characters to perform a text search.		
	</div>
	
	<div id="noResultsMessage" class="alert alert-warning" role="alert">
		<span class="glyphicon glyphicon-exclamation-sign"></span>No results found for search
		<a href="javascript:showCorrectionsDialog();" class="right"><img src="${pageContext.request.contextPath}/static/img/glyphicons-conversation2.png" width="20" alt="" />&nbsp;Corrections or additions?</a> 
	</div>

	<div class="table-responsive">
		<security:authorize ifAnyGranted="ROLE_LEXICON_CREATOR,ROLE_ADMIN">
			<div class="actionButtons col-sm-12 text-right">
				<a href="${pageContext.request.contextPath}/lexicon/admin/add"
					class="btn btn-success">Add New</a> <span
					class="reindexButtonWrapper">
					<button class="btn btn-primary reindexButton"
						data-url="/lexicon/admin/reindex">Re-Index</button>
				</span>
			</div>
		</security:authorize>
		<table id="lexiconTable" class="table table-striped table-hover table-nowrap"></table>
	</div>

	<vhmml:paging-controls tableId="lexiconTable"
		searchFunction="lexiconSearch" onNextButton="refreshSelectedLetter"
		onPrevButton="refreshSelectedLetter" pageCategory="lexicon"
		pageSize="${pageSize}" sortBy="${sortBy}"/>

</div>

<vhmml:corrections-form correctionType="LEXICON" />

<script type="text/javascript">
	var initialSearchText = '${searchText}';
	var initialStartsWithLetter = '${startsWithLetter}';
	var selectedPage = '${selectedPage}';

	<security:authorize ifAnyGranted="ROLE_LEXICON_CREATOR,ROLE_ADMIN">
	isLexiconAdmin = true;
	</security:authorize>
</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/corrections.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lexicon.js"></script>

<security:authorize ifAnyGranted="ROLE_LEXICON_CREATOR,ROLE_ADMIN">
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/lexicon-admin.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/search-admin.js"></script>
</security:authorize>