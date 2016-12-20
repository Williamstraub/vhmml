<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags" %>

<style type="text/css">
	#groupCollections {
		display: none;
	}
	
	#referenceTree {
		overflow-x: scroll;
		border: 1px solid #000;
		margin: 10px 0;
		min-height: 400px
	}
	
	#importCollection {
		display: block;
	}	
</style>

<div class="row">
			
	<div class="col-lg-3">
		<h3>Zotero Citation Import</h3>
		<button id="importCollection" class="btn reference disabled">Import items from selected collection</button>
		<vhmml:zotero-items-tree referenceGroups="${referenceGroups}" onSelectNode="selectReferenceItem"/>
	</div>		
	
	<div class="col-lg-9 table-list">						
		<vhmml:paging-controls tableId="itemsTable" searchFunction="getCollectionItems" pageCategory="reference" sortBy="${sortBy}" pageSize="${pageSize}" currentPage="${currentPage}" />			
		<table id="itemsTable" class="table table-striped"></table>
		<vhmml:paging-controls tableId="itemsTable" searchFunction="getCollectionItems" pageCategory="reference" sortBy="${sortBy}" pageSize="${pageSize}" currentPage="${currentPage}" />
	</div>		
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/reference-import.js?version=${applicationVersion}"></script>
