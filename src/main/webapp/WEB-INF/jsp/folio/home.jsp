<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags" %>

<tiles:importAttribute name="pageCategory" />

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/slider.css" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/mirador/css/mirador-combined.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/vhmml-search-page.css?version=${applicationVersion}"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/folio.css?version=${applicationVersion}"/>

<c:if test="${not empty selectedObjectJson}">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reading-room-view-object.css?version=${applicationVersion}"/>
</c:if>

<div class="bodyContent">	
	<div id="searchPanel" class="slideOutMenu slideOutMenuLeft" data-width="350px">
		<div class="searchWrapper">
			<%@ include file="/WEB-INF/jsp/folio/folioSearch.jsp" %>
		</div>										
	</div>	
	<div class="searchToggleBar" data-menu="searchPanel">
		<span class="menuToggleIcon" data-menu="searchPanel">&raquo;</span><div class="rotateTextClockwise">&nbsp;&nbsp;Search</div>
	</div>
		
	<div id="searchResultsWrapper" class="flex">			
		<vhmml:paging-controls tableId="searchResults" searchFunction="pagingSearch" sortBy="${sortBy}" pageSize="${pageSize}" currentPage="${currentPage}" pageCategory="${pageCategory}" showResultsData="true"  showJumpToPage="true" showPageSize="true" sortByOptions="getFolioSortByOptions"/>

		<div id="searchResults">
			<%-- don't show the description if they already have an object selected --%>
			<c:if test="${empty selectedObjectJson}">
				<jsp:include page="/WEB-INF/jsp/folio/folioDescription.jsp"></jsp:include>
			</c:if>					
		</div>
			
		<vhmml:paging-controls tableId="searchResults" searchFunction="pagingSearch" sortBy="${sortBy}" pageSize="${pageSize}" currentPage="${currentPage}" pageCategory="${pageCategory}" showJumpToPage="true" showPageSize="true" sortByOptions="getFolioSortByOptions"/>
	</div>
		
	<div id="viewer" class="flex"></div>
	<div id="thumbnail-viewer" class="flex text-center" onmousedown="return false"></div>
	
	<div class="searchToggleBar metaDataPanel" data-menu="metaDataPanel">
		<span class="menuToggleIcon" data-menu="metaDataPanel">&laquo;</span><div class="rotateTextClockwise">&nbsp;&nbsp;Description</div>
	</div>
	<div id="metaDataPanel" class="slideOutMenu slideOutMenuRight folioMetaData" data-width="575px">
		<span class="menuToggleIcon reading-room-link" style="top: 38px; left: 12px;" data-menu="metaDataPanel"><span class="menuCloseIcon">&raquo;</span></span>		
		
		<ul class="nav nav-tabs" role="tablist">
			<li role="presentation" class="active overview">
				<a href="#overview" aria-controls="overview" role="tab" data-toggle="tab">Overview</a>
			</li>			
			<li role="presentation" class="description">
				<a href="#description" aria-controls="description" role="tab" data-toggle="tab">Paleographic Description</a>
			</li>
			<li role="presentation" class="transcription">
				<a href="#transcription" aria-controls="transcription" role="tab" data-toggle="tab">Transcription</a>
			</li>
			<li class="backToResults"><button type="button" class="btn btn-backToResults folio"><small><span class="glyphicon glyphicon-share-alt flipHorizontal"></span>Results</small></button></li>
		</ul>
		
		<div class="tab-content">
			<div id="overview" role="tabpanel" class="tab-pane active"></div>
			<div id="description" role="tabpanel" class="tab-pane"></div>
			<div id="transcription" role="tabpanel" class="tab-pane"></div>			
		</div>	
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/search-common-templates.jsp"></jsp:include>

<vhmml:corrections-form correctionType="FOLIO" categories="Image(s),Bibliography,Description,Transcription,Other"/>

<script id="no-search-results-template" type="text/x-handlebars-template">
	<div class="noResults"> 
		<h2>Your search returned 0 results</h2>
		<p>Tips:</p>
		<ul>
			<li>Check your spelling</li>
			<li>If searching for an author or title, try an alternative or shorter form</li>
			<li>If searching with native script, try it again without it</li>
			<li>Facet your searches by using the drop down menus to increase accuracy</li>
			<li>Move the Date Range sliders out to the extremes to include all dates</li>
		</ul>
		<p>You can find more information on our <a href="${pageContext.request.contextPath}/folio/help#SEARCHING">Help Page</a>.</p>
		Still having issues with search? <a href="${pageContext.request.contextPath}/contact"> Contact us</a>.
	</div>
</script>

<script id="search-hit-template" type="text/x-handlebars-template">
	<div class="row row-eq-height">
		{{#if folioObject.iconName}}
			<div class="rr-thumbnail" onmousedown="return false">
				<img src="${pageContext.request.contextPath}/image/thumbnail/FOLIO/{{folioObject.folioObjectNumber}}/{{folioObject.iconName}}" class="viewImagesLink" data-object-id="{{folioObject.id}}" data-access="UNREGISTERED">
			</div>
		{{/if}}

		<div class="flex">
			<div class="searchHitSection">
				<h4 class="section-title">
					{{~#if folioObject.city~}}
						{{{folioObject.city}}}
						{{~#if folioObject.country~}},&nbsp;{{~/if~}}
					{{~/if~}}
		
					{{{folioObject.country}}}
				</h4>
				<h4 class="section-title">
					{{~#if folioObject.repository~}}
						{{{folioObject.repository}}}
						{{~#if folioObject.shelfMark~}},&nbsp;{{~/if~}}
					{{~/if~}}
					{{{folioObject.shelfMark}}}
				</h4>
				<h4 class="section-title">{{{folioObject.dateCentury}}}</h4>
			</div>
				
			<div class="searchHitSection">
				{{#if folioObject.language}}
					<div>
						<b>Language:&nbsp;</b><i>{{{folioObject.language}}}</i>
					</div>
				{{/if}}

				{{#if folioObject.writingSystem}}
					<div>
						<b>Writing System:&nbsp;</b><i>{{{folioObject.writingSystem}}}</i>
					</div>
				{{/if}}

				{{#if folioObject.script}}
					<div>
						<b>Script:&nbsp;</b><i>{{{folioObject.script}}}</i>
					</div>
				{{/if}}					
			</div>
		</div>

		<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_FOLIO_CREATOR">
			<div>
				{{#if folioObject.active}}
					<span class="activeLabel">Active <div class="activeIndicator btn-success"/></span>
				{{else}}
					<span class="activeLabel">Pending <div class="activeIndicator btn-danger"/></span>
				{{/if}}
			</div>
		
			{{#if folioObject.editable}}
				<div><a class="btn folio" href="${pageContext.request.contextPath}/folio/admin/edit/{{folioObject.id}}">Edit</a></div>
			{{/if}}
		</security:authorize>
	</div>
</script>

<script id="search-hit-highlight-template" type="text/x-handlebars-template">
	<div class="row searchHitText {{{cssClass}}}">
		<hr class="separateHighlightFields"/>
		<span class="glyphicon glyphicon-check"/>&nbsp;{{{value}}}<br/>
		<small>Found in <b><i>{{{fieldName}}}</i></b></small>
	</div>
</script>

<script id="search-hit-toggle-template" type="text/x-handlebars-template">
	<div class="row">
		<hr class="separateHighlightFields"/>
		<label><a class="toggleHiddenMatches">Show additional matches...</a></label>
	</div>
</script>

<script id="search-hit-buttons-template" type="text/x-handlebars-template">
	<div class="row pull-left searchHitButtons">
		<button class="btn folio viewImagesLink" data-object-id="{{id}}" data-access="UNREGISTERED">View</button>
	</div>
	<div class="clearfix"></div>
</script>

<script id="overview-tab-template" type="text/x-handlebars-template">
	{{> editObjectControlsTemplate}}
	{{> overviewTemplate}}
</script>

<script id="description-tab-template" type="text/x-handlebars-template">
	{{> editObjectControlsTemplate}}
	{{> descriptionTemplate}}
</script>

<script id="transcription-tab-template" type="text/x-handlebars-template">
	{{> editObjectControlsTemplate}}
	{{> transcriptionTemplate}}
</script>

<script id="edit-object-controls-template" type="text/x-handlebars-template">
	<a class="pull-right correctionsLink" href="javascript:showCorrectionsDialog('{{folioObjectNumber}}');"><img src="${pageContext.request.contextPath}/static/img/glyphicons-conversation2.png" width="20" alt="" />&nbsp;Corrections or additions?</a>
	
	<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_FOLIO_CREATOR">								
		<div class="editControls">
			{{#if editable}}
				<a class="btn folio right" href="${pageContext.request.contextPath}/folio/admin/edit/{{id}}">Edit</a>
			{{/if}}

			{{#if active}}
				<span class="activeLabel right">Active <div class="activeIndicator btn-success"/></span>
			{{else}}
				<span class="activeLabel right">Pending <div class="activeIndicator btn-danger"/></span>
			{{/if}}
		</div>									
	</security:authorize>
</script>

<script id="overview-template" type="text/x-handlebars-template">
	<table>		
		{{#if language}}
			{{render 'meta-data-field-search-multiple-link-template' label="Language(s)" values=language fieldName="language"}}
		{{/if}}
		
		{{#if writingSystem}}
			{{render 'meta-data-field-search-multiple-link-template' label="Writing System(s)" values=writingSystem fieldName="writingSystem"}}
		{{/if}}
		
		{{#if script}}
			{{render 'meta-data-field-search-multiple-link-template' label="Script(s)" values=script fieldName="script"}}
		{{/if}}
				
		{{render 'meta-data-field-template' label="Country" value=country}}
		{{render 'meta-data-field-template' label="City" value=city}}
		{{render 'meta-data-field-template' label="Repository" value=repository}}		
		{{render 'meta-data-field-template' label="Shelfmark" value=shelfMark}}
		{{render 'meta-data-field-template' label="Common Name" value=commonName}}
		{{render 'meta-data-field-template' label="Date Century" value=dateCentury}}
		{{render 'meta-data-field-template' label="Year Range" value=yearRange}}
		{{render 'meta-data-field-template' label="Date Precise" value=datePrecise}}
		{{render 'meta-data-field-template' label="Place Of Origin" value=placeOfOrigin}}
		{{render 'meta-data-field-template' label="Provenance" value=provenance}}
		{{render 'meta-data-field-template' label="Title" value=title}}
		{{render 'meta-data-field-template' label="Text" value=text}}
		{{render 'meta-data-field-template' label="Description" value=description}}
		{{render 'meta-data-field-template' label="Bibliography" value=bibliography}}
		{{render 'meta-data-link-template' label="External Facsimile" link=externalUrl linkText=externalUrl  newWindow="true"}}
		{{render 'permalink-template' label="Permanent link" link="https://${configValues['permalink.url'].value}/folio/view" id=id}}
		{{render 'meta-data-link-template' label="Rights" link="https://www.vhmml.org/terms" linkText="http://www.vhmml.org/terms"}}
		{{render 'meta-data-field-template' label="Acknowledgements" value=acknowledgements}}		
	</table>
</script>

<script id="description-template" type="text/x-handlebars-template">
	<table>		
		{{#if language}}
			{{render 'meta-data-field-search-multiple-link-template' label="Language(s)" values=language fieldName="language"}}
		{{/if}}
		
		{{#if writingSystem}}
			{{render 'meta-data-field-search-multiple-link-template' label="Writing System(s)" values=writingSystem fieldName="writingSystem"}}
		{{/if}}
		
		{{#if script}}
			{{render 'meta-data-field-search-multiple-link-template' label="Script(s)" values=script fieldName="script"}}
		{{/if}}
				
		{{render 'meta-data-field-template' label="Paleographic Features" value=specialFeatures}}			
	</table>
</script>

<script id="transcription-template" type="text/x-handlebars-template">
	<table>		
		{{render 'meta-data-field-template' value=transcription class="transcription"}}				
	</table>
</script>

<!-- mirador has to come before bootstrap-slider because there is a conflict between the two due to the fact that mirador includes jquery-ui -->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.5/handlebars.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/corrections.js?version=${applicationVersion}"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/mirador/mirador.js?version=${applicationVersion}"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/bootstrap-slider.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/vhmml-search-common.js?version=${applicationVersion}"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/folio-search.js?version=${applicationVersion}"></script>

<script type="text/javascript">
	
	var selectedObjectJson;	
	
	<c:if test="${not empty selectedObjectJson}">
		selectedObjectJson = ${selectedObjectJson};
	</c:if>
	
	$(function() {
		initSearch('${savedSearch.searchTerms.beginDate}', '${savedSearch.searchTerms.endDate}');
	});

</script>
