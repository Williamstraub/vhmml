<style type="text/css">
	.export-controls {
		margin: 20px 0;
	}
	
	.exportButtonWrapper {
		display: inline;
		margin-right: 8px;
	}
</style>

<div class="export-controls">
	<label class="control-label">Collection Name</label>
	<input id="collectionName" type="text" name="collectionName"/>
</div>
		
<div id="exportButtons">
	<div class="exportButtonWrapper">
		<button class="btn catalog-database exportButton exportObjects">Export Reading Room Objects</button>
	</div>
	
	<div class="exportButtonWrapper">
		<button class="btn catalog-database exportButton exportLinks">Export Permanent Links</button>
	</div>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/reading-room-export.js"></script>