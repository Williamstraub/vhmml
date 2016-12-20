<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="tableId" required="true" rtexprvalue="true" %>
<%@ attribute name="searchFunction" required="true" rtexprvalue="true" %>
<%@ attribute name="searchParams" required="false" rtexprvalue="true" %>
<%@ attribute name="sortBy" required="false" rtexprvalue="true" %>
<%@ attribute name="pageSize" required="false" rtexprvalue="true" %>
<%@ attribute name="currentPage" required="false" rtexprvalue="true" %>
<%@ attribute name="onPrevButton" required="false" rtexprvalue="true" %>
<%@ attribute name="onNextButton" required="false" rtexprvalue="true" %>
<%@ attribute name="pageCategory" required="false" rtexprvalue="true" %>
<%@ attribute name="showResultsData" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="pageSizes" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="showJumpToPage" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="showPageSize" required="false" rtexprvalue="true" type="java.lang.Boolean" %>
<%@ attribute name="sortByOptions" required="false" rtexprvalue="true" %>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/paging.css"/>	

<div class="paging-controls ${tableId}">

	<c:if test="${showResultsData}">
		<div class="searchResultsData">
			<span class="total"></span>			
			<span class="sortByWrapper">					
				<label>Sort by</label>
				<select name="sortBy" class="sortBy bootstrap-select" title="Relevance">
					<option value="_score">Relevance</option>					
				</select>									
				<button class="sortDir iconButton btn ${pageCategory}" title="Sort order: ascending / descending"><span class="glyphicon glyphicon-chevron-down"></span></button>
			</span>				
			<span class="displayed"></span>			
			<div class="clearfix"></div>
		</div>
	</c:if>
	
	<hr class="thick"/>			
	
	<nav class="pagingButtons">
		<span class="jumpToPageWrapper left">										
			<c:if test="${showJumpToPage}">
				<label>Jump to page </label>
				<input type="text" class="jumpToPage digitsOnly" />
				<button class="iconButton btn btn-go glyphicon glyphicon-triangle-right ${pageCategory}"></button>
			</c:if>
		</span>
		<span class="right">
			<c:if test="${showPageSize}">
				<span class="pageSizeWrapper">
					<label>VIEW</label> 
					<select name="pageSize" class="pageSize bootstrap-select">
						<option>10</option>
						<option selected="selected">25</option>
						<option>50</option>
					</select>
				</span>
			</c:if>			
			<a href="#" class="previous"><span aria-hidden="true" class="glyphicon glyphicon-triangle-left"></span> PREV</a>
			<span class="currentPage"></span>			
			<span>of <span class="pageCount"></span></span>			
			<a href="#" class="next">NEXT <span aria-hidden="true"  class="glyphicon glyphicon-triangle-right"></span></a>
		</span>
		<div class="clearfix"></div>
	</nav>
	
	<hr class="thick ${pageCategory}"/>
</div>

<script type="text/javascript">

	$(function() {		
		new PagingControl({
			tableId: '${tableId}',
			searchFunction: eval('${searchFunction}'),
			sortBy: '${sortBy}',
			pageSize: '${not empty pageSize ? pageSize : 25}',
			currentPage: '${currentPage}',
			searchParams: window['${searchParams}'],
			onPrevButton: window['${onPrevButton}'],
			onNextButton: window['${onNextButton}'],
			sortByOptions: window['${sortByOptions}'],
		});
	});
	
</script>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/paging.js"></script>