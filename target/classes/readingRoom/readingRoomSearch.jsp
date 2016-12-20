<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags" %>

<tiles:importAttribute name="pageCategory" />

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/slider.css" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/mirador/css/mirador-combined.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/vhmml-search-page.css?version=${applicationVersion}"/>
 
<c:if test="${not empty selectedObjectJson}">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/reading-room-view-object.css?version=${applicationVersion}"/>
</c:if>

<div class="bodyContent">	
	<div id="searchPanel" class="slideOutMenu slideOutMenuLeft" data-width="350px">
		<div id="searchControl" class="panel-group searchControl" role="tablist" aria-multiselectable="true">
			<div class="panel panel-default">				
				<%@ include file="/WEB-INF/jsp/readingRoom/basicSearch.jsp" %>
			</div>
			<div class="panel panel-default">
				<%@ include file="/WEB-INF/jsp/readingRoom/advancedSearch.jsp" %>				
			</div>
		</div>		
	</div>
	<div class="searchToggleBar" data-menu="searchPanel">
		<span class="menuToggleIcon" data-menu="searchPanel">&raquo;</span><div class="rotateTextClockwise">&nbsp;&nbsp;Search</div>
	</div>
		
	<div id="searchResultsWrapper" class="flex">			
		<vhmml:paging-controls tableId="searchResults" searchFunction="pagingSearch" sortBy="${sortBy}" pageSize="${pageSize}" currentPage="${currentPage}" pageCategory="${pageCategory}" showResultsData="true" showJumpToPage="true" showPageSize="true" sortByOptions="getReadingRoomSortByOptions"/>

		<div id="searchResults">
			<%-- don't show the description if they already have an object selected --%>
			<c:if test="${empty selectedObjectJson}">
				<jsp:include page="/WEB-INF/jsp/readingRoom/readingRoomDescription.jsp"></jsp:include>
			</c:if>					
		</div>
			
		<vhmml:paging-controls tableId="searchResults" searchFunction="pagingSearch" sortBy="${sortBy}" pageSize="${pageSize}" currentPage="${currentPage}" pageCategory="${pageCategory}" showJumpToPage="true" showPageSize="true" sortByOptions="getReadingRoomSortByOptions"/>
	</div>
		
	<div id="viewer" class="flex"></div>
	<div id="thumbnail-viewer" class="flex text-center" onmousedown="return false"></div>
	
	<div class="searchToggleBar metaDataPanel" data-menu="metaDataPanel">
		<span class="menuToggleIcon" data-menu="metaDataPanel">&laquo;</span><div class="rotateTextClockwise">&nbsp;&nbsp;Description</div>
	</div>
	<div id="metaDataPanel" class="slideOutMenu slideOutMenuRight" data-width="575px">
		<span class="menuToggleIcon reading-room-link" style="top: 38px; left: 12px;" data-menu="metaDataPanel"><span class="menuCloseIcon">&raquo;</span></span>
		<!-- <span class="glyphicon glyphicon-chevron-right menuToggleIcon left" data-menu="metaDataPanel"></span> -->
		
		<ul class="nav nav-tabs" role="tablist">
			<li role="presentation" class="active overview">
				<a href="#overview" aria-controls="overview" role="tab" data-toggle="tab">Overview</a>
			</li>
			<li role="presentation" class="object">
				<a href="#object" aria-controls="object" role="tab" data-toggle="tab">Object</a>
			</li>
			<li role="presentation" class="contents">
				<a href="#contents" aria-controls="contents" role="tab" data-toggle="tab">Contents</a>
			</li>
			<li role="presentation" class="description">
				<a href="#description" aria-controls="description" role="tab" data-toggle="tab">Full Description</a>
			</li>
			
			<li class="backToResults"><button type="button" class="btn btn-backToResults reading-room"><small><span class="glyphicon glyphicon-share-alt flipHorizontal"></span>Results</small></button></li>
		</ul>
		
		<div class="tab-content">
			<div id="overview" role="tabpanel" class="tab-pane active"></div>
			<div id="object" role="tabpanel" class="tab-pane"></div>
			<div id="contents" role="tabpanel" class="tab-pane"></div>
			<div id="description" role="tabpanel" class="tab-pane"></div>
		</div>	
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/search-common-templates.jsp"></jsp:include>

<vhmml:corrections-form correctionType="READING_ROOM" categories="Bibliography,Contents,Image(s),Physical Description,Other"/>
<%@ include file="/WEB-INF/jsp/readingRoom/usageAgreement.jsp" %>
<%@ include file="/WEB-INF/jsp/readingRoom/mustRegister.jsp" %>

<!-- mirador has to come before bootstrap-slider because there is a conflict between the two due to the fact that mirador includes jquery-ui -->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.5/handlebars.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/corrections.js?version=${applicationVersion}"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/mirador/mirador.js?version=${applicationVersion}"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/bootstrap-slider.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/vhmml-search-common.js?version=${applicationVersion}"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/reading-room.js?version=${applicationVersion}"></script>

<script id="no-search-results-template" type="text/x-handlebars-template">
	<div class="noResults"> 
		<h2>Your search returned 0 results</h2>
		<p>Tips:</p>
		<ul>
			<li>Not all of <a href="${pageContext.request.contextPath}/readingRoom/collections">our collections</a> have been imported into Reading Room. Try searching the <a href="http://www.hmml.org/oliver.html" target="blank">Legacy Catalog</a>.</li>
			<li>Check your spelling</li>
			<li>Try a broader search or a different search field</li>
			<li>If searching for a title, try an alternative or shorter form</li>
			<li> Move the Date Range sliders out to the extremes  to include all dates </li>
		    <li>When using an exact search by putting double quotes around multiple words, try the same search without the quote marks or try spelling the words differently (e.g. plurals)</li>
			<li>If searching with native script, try again without it</li>
			<li>Try Advanced Search for more faceted search options</li>
		</ul>
		<p>You can find more information on our <a href="${pageContext.request.contextPath}/readingRoom/help#SEARCHING">Help Page</a>.</p>
		Still having issues with search? <a href="${pageContext.request.contextPath}/contact"> Contact us</a>.
	</div>
</script>

<script id="overview-tab-template" type="text/x-handlebars-template">
	{{> editObjectControlsTemplate}}
	{{> overviewTemplate}}
</script>

<!--  overview-template is used by both the overview-tab-template and the full-description-template -->
<script id="overview-template" type="text/x-handlebars-template">
	<table>		
		{{render 'meta-data-field-search-link-template' vhmmlObject=this label="Country" linkText=country.name searchProp="name" fields="country" lcUri=country.authorityUriLC viafUri=country.authorityUriVIAF}}					
		{{render 'meta-data-field-search-link-template' vhmmlObject=this label="City" linkText=city.name searchProp="name" fields="country,city" lcUri=city.authorityUriLC viafUri=city.authorityUriVIAF}}							
		{{render 'meta-data-field-template' label="Holding Institution" value=holdingInstitution.name lcUri=holdingInstitution.authorityUriLC viafUri=holdingInstitution.authorityUriVIAF}}
		{{render 'meta-data-field-search-link-template' vhmmlObject=this label="Repository" linkText=repository.name searchProp="name" fields="country,city,repository" lcUri=repository.authorityUriLC viafUri=repository.authorityUriVIAF}}

		{{#if archivalData}}
			{{render 'meta-data-field-template' label="Collection" value=archivalData.collectionFond}}
			{{render 'meta-data-field-template' label="Series" value=archivalData.series}}
			{{render 'meta-data-field-template' label="Sub-Series" value=archivalData.subSeries}}
			{{render 'meta-data-field-template' label="Sub-Sub-Series" value=archivalData.subSubSeries}}
		{{/if}}

		{{render 'meta-data-field-template' label="Shelfmark" value=shelfMark}}
		{{render 'meta-data-field-template' label="Common Name" value=commonName}}
		{{render 'meta-data-field-template' label="Current Status" value=currentStatus.displayName}}		
		{{render 'meta-data-field-template' label="Type of Record" value=recordTypeName class="hideForFullDescription"}}

		{{#if archivalData}}
			{{render 'meta-data-field-template' label="Container" value=archivalData.container class="hideForFullDescription"}}
		{{/if}}

		{{render 'meta-data-field-template' label="Extent" value=extentList class="hideForFullDescription"}}
		
		{{! centuries shows for manuscript/print, but not for archival because archival shows it in the object description section }}		
		{{render 'meta-data-field-template' label="Century(ies)" value=centuryDisplay}}

		{{render 'meta-data-field-search-multiple-link-template' label="Language(s)" values=languageDisplay fieldName="language"}}
	
		{{#if archivalData}}
			{{render 'meta-data-field-template' label="Creator" value=archivalData.creator}}
			{{render 'meta-data-field-template' label="Title" value=archivalData.title}}
			{{render 'meta-data-field-template' label="Scope and Contents" value=archivalData.scopeContent}}
			{{render 'meta-data-field-template' label="Historical Note" value=archivalData.historicalNote}}
			{{render 'meta-data-field-template' label="Custodial History" value=archivalData.custodialHistory}}

			{{#each archivalData.associatedNames}}
				{{render 'meta-data-field-template' label="Associated Name" value=contributor.populatedName note=type.displayName lcUri=contributor.authorityUriLC viafUri=contributor.authorityUriVIAF}}
			{{/each}}
		{{/if}}

		{{#each subjects}}
			{{render 'meta-data-field-template' label="Subject" value=name lcUri=authorityUriLC viafUri=authorityUriVIAF}}	
		{{/each}}
	
		{{#each genres}}
			{{render 'meta-data-field-search-link-template' vhmmlObject=.. label="Genre" linkText=name searchProp="name" fields="genres" index=@index}}			
		{{/each}}
	
		{{#each features}}
			{{render 'meta-data-field-search-link-template' vhmmlObject=.. label="Feature" linkText=name searchProp="name" fields="features" index=@index}}
		{{/each}}
	
		{{render 'meta-data-field-template' label="Notes" value=notes}}	
		{{render 'meta-data-field-template' label="Bibliography" value=bibliography}}
	
		{{#each externalBibliographyUrls}}
			{{render 'meta-data-link-template' label="External Bibliography" link=url linkText=linkText newWindow="true"}}
		{{/each}}
	
		{{#each externalFacsimileUrls}}
			{{render 'meta-data-link-template' label="External Facsimile" link=url linkText=url newWindow="true"}}
		{{/each}}
	
		{{#each alternateSurrogates}}
			{{render 'meta-data-field-template' label="Alternate Surrogate" value=name class="wrap-overflow"}}
		{{/each}}
	
		{{render 'meta-data-field-template' label="HMML Proj. Num." value=hmmlProjectNumber}}
		{{render 'permalink-template' label="Permanent Link" link="https://${configValues['permalink.url'].value}/readingRoom/view" id=id}}
		{{#if publicManifest}}
			{{render 'permalink-template' label="IIIF Manifest URL" link="https://${configValues['permalink.url'].value}/image/manifest" id=id image="iiif-logo.png" imageClass="iiif-logo"}}
		{{/if}}
		{{render 'meta-data-field-template' label="Processed By" value=processedBy}}
		{{render 'meta-data-field-template' label="Acknowledgments" value=acknowledgments}}
		{{render 'meta-data-field-template' label="Surrogate Format" value=surrogateFormat.name}}
		{{render 'meta-data-field-template' label="Capture Date" value=captureDateDisplay}}
		{{render 'meta-data-field-template' label="Reproduction Notes" value=reproductionNotes}}
	
		{{#if accessRestrictionLinkText}}
			{{render 'meta-data-order-link-template' label="Access Restrictions" link="http://www.hmml.org/manuscript-order-form.html" preLinkText=accessRestrictionPreLinkText linkText=accessRestrictionLinkText}}
		{{else}}
			{{render 'meta-data-field-template' label="Access Restrictions" value=accessRestriction.displayName}}
		{{/if}}		
		
		{{render 'meta-data-link-template' label="Rights" link="https://www.vhmml.org/terms" linkText="http://www.vhmml.org/terms"}}
	</table>
</script>

<script id="description-template" type="text/x-handlebars-template">

	<label class="heading">Object<br />Description</label>

	<table>
		{{render 'meta-data-field-template' label="Type of Record" value=recordTypeName}}

		{{#if archivalData}}
			{{render 'meta-data-field-template' label="Container" value=archivalData.container}}
		{{/if}}

		{{render 'meta-data-field-template' label="Extent" value=extentList}}
		
		{{#if_not_eq recordTypeName "Archival Material"}}
			{{render 'meta-data-field-template' label="Collation" value=collation}}
			{{render 'meta-data-field-template' label="Foliation" value=foliation}}
		{{/if_not_eq}}	
		
		{{#if archivalData}}
			{{render 'meta-data-field-template' label="Support" value=archivalData.supportDisplay}}
		{{/if}}

		{{render 'meta-data-field-template' label="Binding" value=binding}}
		{{render 'meta-data-field-template' label="Binding Dimensions" value=bindingDimensions}}
		{{render 'meta-data-field-template' label="Provenance" value=provenance}}
				
		{{#each formerOwners}}
			{{render 'meta-data-field-template' label="Former Owner" value=name lcUri=authorityUriLC viafUri=authorityUriVIAF}}
		{{/each}}
	
		{{render 'meta-data-field-template' label="Condition Notes" value=conditionNotes}}

		{{#if archivalData}}
			{{render 'meta-data-field-template' label="Decoration" value=archivalData.decoration}}
			{{render 'meta-data-field-template' label="Century(ies)" value=archivalData.centuryDisplay}}
			{{render 'meta-data-field-template' label="Year Range" value=archivalData.yearRange}}
			{{render 'meta-data-field-template' label="Date Precise" value=archivalData.datePreciseDisplay}}
			{{render 'meta-data-field-template' label="Native Date" value=archivalData.nativeDate}}
			{{render 'meta-data-field-search-multiple-link-template' label="Language(s)" values=archivalData.languagesDisplay fieldName="language" separator=","}}
			{{render 'meta-data-field-search-multiple-link-template' label="Writing System(s)" values=archivalData.writingSystem fieldName="writingSystem" separator=","}}				

			{{#if archivalData.scripts}}
				{{render 'meta-data-field-search-multiple-link-template' label="Script(s)" values=archivalData.scripts fieldName="script" separator=","}}
			{{/if}}		
		{{/if}}
	</table>
</script>

<script id="part-template" type="text/x-handlebars-template">
	<label class="heading">Part {{partNumber}}</label>
	
	<table>		
		{{render 'meta-data-field-template' label="Type" value=partTypeName}}
		{{render 'meta-data-field-template' label="Part Location" value=partLocation}}			
				
		{{#if_eq partTypeName "Printed"}}
			{{render 'meta-data-field-template' label="Place of Printing" value=placeOfOrigin}}
			{{render 'meta-data-field-template' label="Printing Statement" value=printingStatement}}
			{{render 'meta-data-field-template' label="Edition Statement" value=editionStatement}}
			{{render 'meta-data-field-template' label="Format/Signatures" value=formatStatement}}
		{{/if_eq}}
	
		{{#if_not_eq partTypeName "Printed"}}
			{{render 'meta-data-field-template' label="Place of Origin" value=placeOfOrigin}}
		{{/if_not_eq}}
				
		{{render 'meta-data-field-template' label="Century(ies)" value=centuryDisplay}}
		{{render 'meta-data-field-template' label="Year Range" value=yearRange}}
		{{render 'meta-data-field-template' label="Date Precise" value=datePreciseDisplay}}
		{{render 'meta-data-field-template' label="Native Date Precise" value=nativeDatePrecise}}
		{{render 'meta-data-field-template' label="Support" value=supportDisplay}}
		{{render 'meta-data-field-template' label="Support Dimensions" value=supportDimensions}}
				
		{{#if_not_eq partTypeName "Printed"}}
			{{render 'meta-data-field-template' label="Medium" value=medium}}
		{{/if_not_eq}}
	
		{{render 'meta-data-field-template' label="Page Layout" value=layout}}
	
		{{#if_not_eq partTypeName "Printed"}}
			{{render 'meta-data-field-template' label="Writing Space" value=writingSpace}}
		{{/if_not_eq}}
				
		{{#if catchwords}}
			{{render 'meta-data-field-template' label="Catchwords" value='Yes'}}
		{{/if}}
				
		{{#if signatures}}
			{{render 'meta-data-field-template' label="Signatures" value='Yes'}}
		{{/if}}			
	
		{{#if_eq partTypeName "Printed"}}
			{{render 'meta-data-field-template' label="Font(s)" value=font}}
		{{/if_eq}}
	
		{{#if_not_eq partTypeName "Printed"}}
			{{#if writingSystem}}
				{{render 'meta-data-field-search-multiple-link-template' label="Writing System" values=writingSystem fieldName="writingSystem" separator=","}}
			{{/if}}

			{{#if script}}
				{{render 'meta-data-field-search-multiple-link-template' label="Script(s)" values=script fieldName="script" separator=","}}
			{{/if}}			
	
			{{#each scribes}}
				{{render 'meta-data-field-template' label="Scribe" value=contributor.populatedName lcUri=contributor.authorityUriLC viafUri=contributor.authorityUriVIAF}}
				{{render 'meta-data-field-template' label="Scribe NS" value=nameNs valueClass=../languageList}}
			{{/each}}
		{{/if_not_eq}}
	
		{{#each artists}}
			{{render 'meta-data-field-template' label="Artist" value=contributor.populatedName lcUri=contributor.authorityUriLC viafUri=contributor.authorityUriVIAF}}
		{{/each}}

		{{#each associatedNames}}
			{{render 'meta-data-field-template' label="Associated Name" value=contributor.populatedName note=type.displayName lcUri=contributor.authorityUriLC viafUri=contributor.authorityUriVIAF}}
		{{/each}}
	
		{{render 'meta-data-field-template' label="Decoration" value=decoration}}
		{{render 'meta-data-field-template' label="Colophon" value=colophonPart valueClass=languageList}}
		{{render 'meta-data-field-template' label="Notes" value=partNotes}}
		{{render 'meta-data-field-template' label="Acknowledgments" value=acknowledgments}}
	</table>
</script>

<script id="object-tab-template" type="text/x-handlebars-template">
	
	{{> editObjectControlsTemplate}}
	{{> descriptionTemplate}}	
	
	{{#each parts}}
		{{> partTemplate}}
	{{/each}}
</script>

<script id="contents-tab-template" type="text/x-handlebars-template">
	
	{{> editObjectControlsTemplate}}
	
	{{#if parts}}	
		{{#each parts}}
			<label class="heading">Part {{partNumber}}</label>
			{{#each contents}}
				{{> contentTemplate}}										
			{{/each}}				
		{{/each}}
	{{/if}}
	
	{{#if archivalData}}		
		{{#each archivalData.content}}
			{{> archivalContentTemplate}}
		{{/each}}
	{{/if}}
	
</script>

<script id="full-description-tab-template" type="text/x-handlebars-template">

	<label class="heading">Overview</label>
	{{> editObjectControlsTemplate}}
	{{> overviewTemplate}}	
	{{> descriptionTemplate}}

	{{#if parts}}
		{{#each parts}}
			{{> partTemplate}}

			{{#each contents}}
				{{> contentTemplate}}
			{{/each}}
		{{/each}}
	{{/if}}

	{{#if archivalData}}		
		{{#each archivalData.content}}
			{{> archivalContentTemplate}}
		{{/each}}
	{{/if}}
</script>

<script id="content-template" type="text/x-handlebars-template">
	<table class="indent">
		{{#if itemNumber}}
			<tr>
				<td><label class="heading">Item {{itemNumber}}</label><td>
			</tr>
		{{/if}}						
		{{render 'meta-data-field-template' label="Item Location" value=itemLocation}}

		{{#each authors}}
			{{render 'meta-data-field-template' label="Author" value=contributor.populatedName lcUri=contributor.authorityUriLC viafUri=contributor.authorityUriVIAF}}			
			{{render 'meta-data-field-template' label="Author NS" value=nameNs valueClass=../languageList}}
		{{/each}}
		
		{{render 'meta-data-field-template' label="Title" value=provisionalTitle}}
		{{render 'meta-data-field-template' label="Supplied Title" value=suppliedTitle}}
		{{render 'meta-data-field-template' label="Title NS" value=titleNs valueClass=languageList}}
		{{render 'meta-data-field-template' label="Uniform Title" value=uniformTitle.name lcUri=uniformTitle.authorityUriLC viafUri=uniformTitle.authorityUriVIAF}}

		{{#each alternateTitles}}
			{{render 'meta-data-field-template' label="Alternate Title" value=this}}
		{{/each}}
		
		{{render 'meta-data-field-template' label="Running Title" value=runningTitle}}		
		{{render 'meta-data-field-template' label="Pagination/Foliation" value=paginationStatement}}				

		{{#each associatedNames}}
			{{render 'meta-data-field-template' label="Associated Name" value=contributor.populatedName note=type.displayName lcUri=contributor.authorityUriLC viafUri=contributor.authorityUriVIAF}}				
		{{/each}}
			
		{{render 'meta-data-field-search-multiple-link-template' label="Language(s)" values=languageDisplay fieldName="language"}}

		{{render 'meta-data-field-template' label="Incipit" value=incipit valueClass=languageList}}

		{{render 'meta-data-field-template' label="Explicit" value=explicit valueClass=languageList}}
		{{render 'meta-data-field-template' label="Rubric" value=rubric valueClass=languageList}}
			
		{{render 'meta-data-field-template' label="Contents Detail" value=contentsDetail}}
		{{render 'meta-data-field-template' label="Item Colophon" value=colophonText valueClass=languageList}}
		{{render 'meta-data-field-template' label="Cataloger Tags" value=catalogerTags}}
		{{render 'meta-data-field-template' label="Item Notes" value=itemNotes}}
		{{render 'meta-data-field-template' label="Item Condition" value=itemCondition}}
		{{render 'meta-data-field-template' label="Item Bibliography" value=textBibliography}}
	</table>
</script>

<script id="archival-content-template" type="text/x-handlebars-template">
	<table class="indent">
		<tr>
			<td><label class="heading">Content Item</label><td>
		</tr>		

		{{render 'meta-data-field-template' label="Folder" value=folder}}				
		{{render 'meta-data-field-template' label="Item" value=item}}				
		{{render 'meta-data-field-template' label="Item Extent" value=extent}}				
		{{render 'meta-data-field-template' label="Item Location" value=itemLocation}}
		{{render 'meta-data-field-template' label="Type" value=typeName}}
		{{render 'meta-data-field-template' label="Item(s) Description" value=description}}
		{{render 'meta-data-field-template' label="Century(ies)" value=centuryDisplay}}
		{{render 'meta-data-field-template' label="Year Range" value=yearRange}}
		{{render 'meta-data-field-template' label="Date Precise" value=datePreciseDisplay}}
		{{render 'meta-data-field-template' label="Native Date" value=nativeDate}}
		{{render 'meta-data-field-template' label="Support" value=supportDisplay}}
		{{render 'meta-data-field-template' label="Support Dimensions" value=supportDimensions}}
		{{render 'meta-data-field-template' label="Medium" value=medium}}
		{{render 'meta-data-field-template' label="Page Layout" value=pageLayout}}
		{{render 'meta-data-field-search-multiple-link-template' label="Language(s)" values=languagesDisplay fieldName="language" separator=","}}
		{{render 'meta-data-field-search-multiple-link-template' label="Writing System(s)" values=writingSystem fieldName="writingSystem" separator=","}}
		{{render 'meta-data-field-search-multiple-link-template' label="Script(s)" values=script fieldName="script" separator=","}}

		{{#each scribes}}
			{{render 'meta-data-field-template' label="Scribe" value=contributor.populatedName lcUri=contributor.authorityUriLC viafUri=contributor.authorityUriVIAF}}
			{{render 'meta-data-field-template' label="Scribe NS" value=nameNs valueClass=../languageList}}
		{{/each}}

		{{#each artists}}
			{{render 'meta-data-field-template' label="Artist" value=contributor.populatedName lcUri=contributor.authorityUriLC viafUri=contributor.authorityUriVIAF}}
		{{/each}}

		{{#each associatedNames}}
			{{render 'meta-data-field-template' label="Associated Name" value=contributor.populatedName note=type.displayName lcUri=contributor.authorityUriLC viafUri=contributor.authorityUriVIAF}}
		{{/each}}

		{{render 'meta-data-field-template' label="Item Condition" value=itemCondition}}
		{{render 'meta-data-field-template' label="Notes" value=notes}}
		{{render 'meta-data-field-template' label="Item Bibliography" value=bibliography}}		
	</table>
</script>

<script id="edit-object-controls-template" type="text/x-handlebars-template">
	<a class="pull-right correctionsLink" href="javascript:showCorrectionsDialog('{{hmmlProjectNumber}}');"><img src="${pageContext.request.contextPath}/static/img/glyphicons-conversation2.png" width="20" alt="" />&nbsp;Corrections or additions?</a>
	
	<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER,ROLE_LEVEL_III_CATALOGER,ROLE_SCHOLAR">								
		<div class="editControls">
			{{#if editable}}
				{{#if lockedByDifferentUser}}
					<a class="btn reading-room right disabled">
						<span class="glyphicon glyphicon-lock" aria-hidden="true"></span> Edit
					</a>
				{{else}}
					<a class="btn reading-room right" href="javascript:editObject({{id}});">Edit</a>
				{{/if}}
			{{/if}}

			{{#if active}}
				<span class="activeLabel right">Active <div class="activeIndicator btn-success"/></span>
			{{else}}
				<span class="activeLabel right">Pending <div class="activeIndicator btn-danger"/></span>
			{{/if}}
		</div>									
	</security:authorize>
</script>

<script type="text/javascript">
	var citiesJson;
	var repositoriesJson;
	var agreementAccepted = JSON.parse('${acceptedReadingRoomAgreement}');
	var pagingControl = null;		
	var noSearchResultsTemplate = Handlebars.compile($('#no-search-results-template').html());		
	var overviewTabTemplate = Handlebars.compile($('#overview-tab-template').html());		
	var objectTabTemplate = Handlebars.compile($('#object-tab-template').html());
	var contentsTabTemplate = Handlebars.compile($('#contents-tab-template').html());
	var fullDescriptionTabTemplate = Handlebars.compile($('#full-description-tab-template').html());
	var descriptionTemplate = Handlebars.registerPartial('descriptionTemplate', $('#description-template').html());
	var partTemplate = Handlebars.registerPartial('partTemplate', $('#part-template').html());
	var contentTemplate = Handlebars.registerPartial('contentTemplate', $('#content-template').html());
	var archivalContentTemplate = Handlebars.registerPartial('archivalContentTemplate', $('#archival-content-template').html());
	var overviewTemplate = Handlebars.registerPartial('overviewTemplate', $('#overview-template').html());
	var editObjectControlsTemplate = Handlebars.registerPartial('editObjectControlsTemplate', $('#edit-object-controls-template').html());	
	var selectedObjectJson;
	
	<c:if test="${not empty savedSearchJson}">
		savedSearch = ${savedSearchJson};
	</c:if>
	
	<c:if test="${not empty selectedObjectJson}">
		selectedObjectJson = ${selectedObjectJson};
	</c:if>
	
	$(function() {
		initSearch('${savedSearch.searchTerms.beginDate}', '${savedSearch.searchTerms.endDate}');		
		
		$('a.signIn').click(function(e) {
			e.preventDefault();
			var link = $(this).attr('href');
			
			saveSearchState(function() {
				window.location = link;
			});
		});
		
		$('a.saveSearch').click(function(e) {
			e.preventDefault();
			var link = $(this).attr('href');
			
			saveSearchState(function() {
				window.location = link;
			});
		});		
		
		if(selectedObjectJson) {
			showImageThumbnail(selectedObjectJson);
			refreshMetaData(selectedObjectJson);
			$('#metaDataPanel').show();
		}
		
		if(!agreementAccepted) {
			showUsageAgreement();	
		}
	});
	
	// reading room has it's own refreshMetaData function because it's very different from folio
	function refreshMetaData(object) {		
		window.scrollTo(0, 0);
		var $metaDataPanel = $('#metaDataPanel');
		$metaDataPanel.find('li.overview a').click();
		
		$('#overview').html(overviewTabTemplate(object));
		$('#object').html(objectTabTemplate(object));		
		
		if(object.allContent.length || (object.type === 'ARCHIVAL_OBJECT' && object.archivalData.content.length)) {
			$('#contents').html(contentsTabTemplate(object));
			$metaDataPanel.find('li.contents').removeClass('disabled');
		} else {
			$metaDataPanel.find('li.contents').addClass('disabled');
		}
		
		$('#description').html(fullDescriptionTabTemplate(object));		
	}
	
	function showImageThumbnail(object) {
		$('#searchResultsWrapper').hide();
		
		var html = '';
		var imageWidth = $('.bodyContent').width() * .8;
		var hasImages = object.iconName;
		var online = object.accessRestriction.name !== 'ON_SITE_ONLY_ORDER_SCAN' && object.accessRestriction.name !== 'ON_SITE_ONLY';
		var cssClass = 'thumb';
		var url = '';
		
		if(hasImages && online) {
			cssClass += ' responsive viewImagesLink';
			url = getIconUrl(object, true) + '/' + imageWidth;
			html = '<img src="' + url + '" class="' + cssClass + '" data-object-id="' + object.id + '" data-access="' + object.accessRestriction.name + '"/>';			
		} else {			
			html = getPlaceholderImage(object, !online, 'http://www.hmml.org/manuscript-order-form.html', true);
		}
		
		if(hasImages) {
			var label = (object.accessRestriction.name == 'REGISTERED' || object.accessRestriction.name == 'REGISTERED_ONLY') && !isAuthenticated ? 'Sign in to view image(s)' : 'View Image(s)';
			html += '<div><button class="btn ${pageCategory} viewImagesLink" data-object-id="' + object.id + '" data-access="' + object.accessRestriction.name + '">' + label + '</button></div>';				
		}
		
		// hide mirador viewer
		$('#viewer').hide();
		var $thumbViewer = $('#thumbnail-viewer');
		showSpinner({element: $thumbViewer});
		$thumbViewer.show();
		
		if(url) {
			preloadImage(url, function() {
				$thumbViewer.html(html);
			});	
		} else {
			$thumbViewer.html(html);
		}				
	}
	
	function initSearch(beginDate, endDate) {		
		
		// init page-scope paging control
		pagingControl = PagingControl.get('searchResults');
		initSearchSelectLists();		
		initDateSlider(beginDate, endDate);				
		$('button.searchButton').click(function() {
			newSearch();
		});
				
		$('span.menuToggleIcon, div.searchToggleBar').click(function() {
			var $selectedMenu = $('#' + $(this).attr('data-menu'));
			slideMenu($selectedMenu);
		});
		
		var $basicSearch = $('#basicSearch');
		var $advancedSearch = $('#advancedSearch');
		
		initSearchToggle($basicSearch, $advancedSearch);
		initSearchToggle($advancedSearch, $basicSearch);
				
		runSavedSearch();
	}
	
	// we get all select list options via ajax because some of the lists are very large, so if we render
	// the options html as part of the intial page request it makes the page load very slow. this
	// way the options are grabbed and rendered in the background
	var optionsCache = {};
	
	function initSearchSelectLists() {
		var optionsUrl = contextPath + '/options/';
		var ajaxCalls = [];
		
		$('#searchPanel select.bootstrap-select').each(function(index) {
			var $selectList = $(this);
			var listName = $selectList.attr('name');
			$selectList.selectpicker();
			
			if(optionsCache[listName]) {
				appendOptions($selectList, optionsCache[listName]);
				$selectList.selectpicker('refresh');
			} else {
				ajaxCalls.push(
					$.ajax({
						url: optionsUrl + listName,
						success: function(options) {						
							appendOptions($selectList, options);
							$selectList.selectpicker('refresh');
							optionsCache[listName] = options;
							// need to capture complete list of cities and repositories so we can reset dependent lists when country/city is deselected
							if(listName == 'city') {
								citiesJson = options;
							} else if(listName == 'repository') {
								repositoriesJson = options;
							}
						}
					})
				);
			}					
		});
		
		return ajaxCalls;		
	}
	
	function appendOptions($selectList, options) {
		var optionsHtml = '';
		var valueSelected = false;
		
		for(var i = 0; i < options.length; i++) {
			var option = options[i];
			var selected = '';
			
			if(option.selected) {
				valueSelected = true;
				selected = 'selected="selected"';
			}
			
			optionsHtml += '<option value="' + option.value + '" ' + selected + ' >' + option.name + '</option>';
		}
		
		$selectList.append(optionsHtml);
		
		if(valueSelected){
			$selectList.change();
		}
	}
	
	function getSearchTerms() {
		var $searchForm = $('#searchPanel form:visible');
		var currentYear = new Date().getFullYear();		
		var dateRange = $($searchForm.find('input.slider')[0]).val();		
		var dateRangeArray = dateRange.split(',');
		var beginDate = dateRange.length > 0 ? +dateRangeArray[0] - 5 : '';
		var endDate = dateRange.length > 0 ? +dateRangeArray[1] + 5 : '';
		
		if(beginDate <= 1 && endDate >= currentYear) {
			beginDate = '';
			endDate = '';	
		}
		
		var $country = $searchForm.find('select.country');
		var $city = $searchForm.find('select.city');
		var $repository = $searchForm.find('select.repository');
		
		return {			
			keyword: $searchForm.find('textarea.keyword').val(),
			title: $searchForm.find('textarea.title').val(),
			author: $searchForm.find('textarea.author').val(),	
			language: getMultiSelectVal($searchForm, 'select.language'),
			beginDate: beginDate,
			endDate: endDate,
			downloadable: $searchForm.find('input.downloadable').is(':checked') ? true : '', // only want to filter on downloadable if it's checked, otherwise bring back both downloadable and non
			active: $searchForm.find('input.pendingOnly').is(':checked') ? false : '',
			format: getFormatFilters($searchForm),
			country: $country.val() ? $country.children('option:selected').text() : '',
			city: $city.val() ? $city.children('option:selected').text() : '',
			repository: $repository.val() ? $repository.children('option:selected').text() : '',
			hmmlProjectNumber: $searchForm.find('input.hmmlProjectNumber').val(),
			shelfmark: $searchForm.find('input.shelfmark').val(),
			placeOfOrigin: $searchForm.find('input.placeOfOrigin').val(),
			subject: $searchForm.find('input.subject').val(),
			incipit: $searchForm.find('textarea.incipit').val(),
			objectType: $searchForm.find('select.objectType').val(),
			genres: getMultiSelectVal($searchForm, 'select.genres'),
			features: getMultiSelectVal($searchForm, 'select.features'),
			script: getMultiSelectVal($searchForm, 'select.script'),
			writingSystem: getMultiSelectVal($searchForm, 'select.writingSystem')			
		};
	}
	
	function getFormatFilters($searchForm) {
		var filters = [];
		var $filters = $searchForm.find('input.formatFilter');
		
		$filters.each(function() {
			var $filter = $(this);
			
			if($filter.is(':checked')) {
				filters.push($filter.attr('data-value'));
			}
		});
		
		// if all of the formats are checked, just return empty string because we're not filtering on format 
		return filters.length === $filters.length ? '' : filters.join();		
	}

	// TODO: put this in a Handlebars template
	function getSearchHitHtml(hit, fieldDisplayNames) {
		var overview = hit.objectOverview;
		var html = '<div class="searchHit">';		
		var iconHtml = '';
		var hasImages = overview.iconName;
		
		if(hasImages) {
			iconHtml = '<img src="' + getIconUrl(overview) + '" class="viewImagesLink" data-object-id="' + overview.id + '" data-access="' + overview.accessRestriction + '"/>';
		} else {
			var $placeholderImage = $(getPlaceholderImage(overview));
			$placeholderImage.addClass('viewDescription');
			$placeholderImage.attr('data-object-id', overview.id);
			iconHtml = $placeholderImage[0].outerHTML;
		}
		
		html += '<div class="row row-eq-height">';	
			html += '<div class="rr-thumbnail" onmousedown="return false">';
				html += iconHtml;
			html += '</div>';
			
			html += '<div class="flex">';
			
				var countryCity = overview.city === overview.country ? getSearchHitFieldsHtml([overview.country]) : getSearchHitFieldsHtml([overview.city, overview.country]);
				var projectNumber = overview.hmmlProjectNumber;
				html += '<div class="searchHitSection">';
					html += '<h4 class="section-title">' + countryCity + '</h4>';
					html += '<h4 class="section-title">' + getSearchHitFieldsHtml([overview.repository, overview.shelfmark]) + '</h4>';
					html += '<h4 class="section-title">' + getSearchHitFieldsHtml([overview.centuryDisplay]) + '</h4>';
				html += '</div>';
				html += '<div class="searchHitSection">';
					html += '<i>' + getSearchHitFieldsHtml([overview.support, overview.extent, overview.dimensions]) + '</i>';
					html += '<i>' + getSearchHitFieldsHtml([overview.languages]) + '</i>';
				html += '</div>';
				html += '<div class="searchHitSection">HMML Project Number: ' + projectNumber + '</div>';
			html += '</div>';
			
			<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER,ROLE_LEVEL_III_CATALOGER,ROLE_SCHOLAR">			
		
				html += '<div>';
					var cssClass =  overview.active ? 'btn-success' : 'btn-danger';
					var activeText =  overview.active ? 'Active ' : 'Pending ';
					html += '<span class="activeLabel">' + activeText + '<div class="activeIndicator ' + cssClass + '"/></span>';
				html += '</div>';						
				
				if(hit.editable) {
					html += '<div>';
						var cssClass = overview.lockedByDifferentUser ? 'btn ${pageCategory} disabled' : 'btn ${pageCategory}';
						
						html += '<a class="' + cssClass + '" href="javascript:editObject(' + overview.id + ');">';
						
						if(overview.lockedByDifferentUser) {
							html += '<span class="glyphicon glyphicon-lock" aria-hidden="true"></span> ';
						}
						
						html += 'Edit</a>';								
					html += '</div>';	
				}					
					
			</security:authorize>
		
		html += '</div>'; // end row
		
		if (hit.highlightFields && Object.keys(hit.highlightFields).length) {			
			var i = 0;
			
			for (var fieldName in hit.highlightFields) {
				var hidden = i > 2;
				
				html += '<div class="row searchHitText' + (hidden ? ' vhmml-hide toggleHit' : '') + '">';
					html += '<hr class="separateHighlightFields"/>';
					html += '<span class="glyphicon glyphicon-check"/>&nbsp;' + hit.highlightFields[fieldName] + '<br/>'
					html += '<small>Found in <b><i>' + fieldDisplayNames[fieldName] + '</i></b></small>';
				html += '</div>';					
				i++;
			}
			
			if(Object.keys(hit.highlightFields).length > 3) {
				html += '<div class="row">';
					html += '<hr class="separateHighlightFields"/>';
					html += '<label><a class="toggleHiddenMatches">Show additional matches...</a></label>';
				html += '</div>';					
			}
		}
	
		html += '<div class="row pull-left searchHitButtons">';
			html += '<button class="btn ${pageCategory} viewDescription" data-object-id="' + overview.id + '">Description</button>';
			if(hasImages) {
				var label = (overview.accessRestriction == 'REGISTERED' || overview.accessRestriction == 'REGISTERED_ONLY') && !isAuthenticated ? 'Sign In To View Image(s)' : 'View Image(s)';
				html += '<button class="btn ${pageCategory} viewImagesLink" data-object-id="' + overview.id + '" data-access="' + overview.accessRestriction + '">' + label + '</button>';
			}						
		html += '</div>';
		
		html += '<div class="clearfix"></div>';		
		
		html += '</div>';

		return html;
	}
	
	function getPlaceholderImage(object, largePlaceholder, link, newWindowLink) {
		var cssClass = 'text-center';
		// default to green-icon with open book (digital objects)
		var icon = '<i class="card-icon green-icon icon-open-book"></i>';
		var online = object.accessRestriction.name !== 'ON_SITE_ONLY' && object.accessRestriction.name !== 'ON_SITE_ONLY_ORDER_SCAN';
		
		// this is the placeholder image you see when clicking on the Description button for items that can only be viewed on-site or by ordering a copy
		if(largePlaceholder && !online) {			
			icon = '<img src="' + contextPath + '/static/img/microfilm-placeholder.png" class="responsive"/>';
		} else {
			switch (object.format) {
				// for microfilm we show the film reel
				case 'Microform':
				case 'Scanned Microform':
					cssClass += ' image-placeholder light-blue-bg';
					icon = '<i class="card-icon blue-icon icon-film-reel"></i>';														
					break;
				default: 
					cssClass += ' image-placeholder light-green-bg';
			}
		}
		
		var placeholderImage = '<div class="' + cssClass + '">';
		
			if(link) {
				placeholderImage += '<a href="' + link + '"';
				
				if(newWindowLink) {
					placeholderImage += 'target="_blank"'
				}
				
				placeholderImage += '>' + icon + '</a>';
			} else {
				placeholderImage += icon;	
			}
			
			placeholderImage += '</div>';
		
		if(!largePlaceholder) {
			placeholderImage = '<div class="icon-wrapper">' + placeholderImage + '</div>' 
		}
		
		return placeholderImage;
	}
	
	function getIconUrl(object, largePlaceholder) {
		var url = contextPath;
		var online = object.accessRestriction !== 'ON_SITE_ONLY_ORDER_SCAN' && object.accessRestriction !== 'ON_SITE_ONLY';		
		
		// if they've selected an icon and the images are available online
		if(object.iconName && online) {
			// the projectNumber could have html if it was the result of a search hit that's got highlight formatting
			// we may want to completely separate the search hit atts from actual object attribute values at some point
			var projectNumber = stripHtml(object.hmmlProjectNumber);
			url += '/image/thumbnail/READING_ROOM/' + projectNumber + '/' + object.iconName; 	
		} 
		
		return url;
	}

	function getSearchHitFieldsHtml(fieldValues) {
		var html = '';

		if (fieldValues) {
			
			for(var i = 0; i < fieldValues.length; i++) {
				
				var value = fieldValues[i]
				
				if(value) {
					
					if(i > 0 && html) {
						html += ', ';
					}
					
					html += value;					
				}				
			}
			
			html = '<div>' + html + '</div>';
		}

		return html;
	}
	
	function showUsageAgreement() {
		var dialog = new Dialog({
			title: '<img src="' + contextPath + '/static/img/vhmml-logo-black.png" alt="Virtual H M M L"/>Reading Room User Agreement',
			type: 'confirm',				
			message: $('#readingRoomUsageAgreement').html(),			
			buttonsCssClass: '${pageCategory}',
			showCloseIcon: false,
			closeButtonCssClass: 'btn-default',
			closeButtonFunction: function() {
				window.location = contextPath + '/';
			},
			keyboard: false,
			moveable: true,
			buttons: {
				'I Agree': function() {
					$.ajax({
						url: contextPath + '/user/acceptReadingRoomAgreement',
						method: 'PUT',
						success: function() {
							agreementAccepted = true;
							dialog.hide();							
						}
					});
				}
			},
		});
		
		dialog.show();
		$('#vhmml-dialog button:last-child').focus();
	}
	
	function saveSearchState(onComplete, postLoginUrl) {
		var pagingData = pagingControl.getPagingData();						
		var searchTerms = getSearchTerms();
		var searchType = $('#searchPanel form:visible').hasClass('advanced') ? 'advanced' : 'basic';
		var postLogin = postLoginUrl ? {postLoginDestination: postLoginUrl} : {};
		var data = $('div.searchHit').length ? $.extend({}, pagingData, {searchType: searchType, searchTerms: searchTerms}, postLogin) : {};
		
		if(!onComplete) {
			onComplete = function() {};
		}
		
		$.ajax({
			url: contextPath + '/readingRoom/saveSearchState',
			dataType: 'json',
			contentType:'application/json',
			data: data,
			complete: onComplete
		});
	}
	
	function goToLogin() {
		var onComplete = function() {
			window.location = contextPath + '/login';
		}
		var postLoginUrl = '/readingRoom';
		
		saveSearchState(onComplete, postLoginUrl);				
	}
	
	function editObject(id) {
		saveSearchState(function() {
			window.location = contextPath + '/catalogDatabase/edit?id=' + id;	
		});		
	}
	
	function initSearchToggle($menu, $otherMenu) {		
		
		$menu.on('hidden.bs.collapse', function () {
			if(!$otherMenu.is(':visible')) {
				$otherMenu.collapse('show');	
			}			
		});
		
		$menu.on('shown.bs.collapse', function () {
			if($otherMenu.is(':visible')) {
				$otherMenu.collapse('hide');
			}
		});
	}
	
	function getReadingRoomSortByOptions() {		
		var html = '';
		html += '<option value="beginDate">Date</option>';
		html += '<option value="country">Country</option>';
		html += '<option value="city">City</option>';
		html += '<option value="repository">Repository</option>';				
		html += '<option value="hmmlProjectNumber">HMML Project Number</option>';
		
		return html;
	}

</script>