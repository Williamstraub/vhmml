<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css?version=${applicationVersion}"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/js/datepicker/css/bootstrap-datepicker3.standalone.min.css"/>

<div class="reportControls">
	<form id="reportForm" name="reportForm">
		<label>${reportTypeDisplayName} Report</label>
		
		<span class="reportParam object">
			<input id="hmmlProjectNumber" name="hmmlProjectNumber" type="text"/>
		</span>
		
		<span class="reportParam user cataloger">
			<input id="username" name="username" type="text"/>
		</span>
		
		<span class="reportParam popularity">
			<select id="aggregationField" name="aggregationField">
				<option value="country">Country</option>
				<option value="city">City</option>
				<option value="repository">Repository</option>
				<option value="collection">Collection</option>
				<option value="hmmlProjectNumber">Object</option>
			</select>
		</span>
		
		<span class="reportParam popularity">
			<label class="popularity">Show Top</label>
			<input id="aggregationMax" name="aggregationMax" type="text" class="popularity digitsOnly" value="100">
		</span>
		
		<span class="reportParam object user popularity cataloger">
			<label>From</label>
			<input id="startDate" name="startDate" type="text" class="date"/>
		</span>
		
		<span class="reportParam object user popularity cataloger">
			<label>To</label>
			<input id="endDate" name="endDate" type="text" class="date"/>
		</span>
		
		<button id="runReport" type="button" class="btn admin">Go</button>
		<button id="clearButton" type="reset" class="btn btn-default">Clear</button>
	</form>	
</div>

<div class="row">
	<vhmml:paging-controls tableId="reportTable" searchFunction="runReport" sortBy="${sortBy}" pageSize="${pageSize}" currentPage="${currentPage}" pageCategory="admin" showJumpToPage="true"/>
	
	<div id="noResultsMessage" class="alert alert-warning" role="alert"><span class="glyphicon glyphicon-exclamation-sign"></span>No results found for search</div>		
	
	<div class="table-responsive">		
		<table id="reportTable" class="table table-striped table-hover"></table>			
	</div>		
	
	<vhmml:paging-controls tableId="reportTable" searchFunction="runReport" sortBy="${sortBy}" pageSize="${pageSize}" currentPage="${currentPage}" pageCategory="admin" showJumpToPage="true"/>
	
</div>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.5/handlebars.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/datepicker/js/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/admin/reports.js"></script>

<script type="text/javascript">
	var reportType = '${reportType}';
</script>

<script id="object-report-template" type="text/x-handlebars-template">
	<thead>
		<tr>
			<th class="col-lg-4"><a href="javascript:sort('hmmlProjectNumber');">Object</a></th>
			<th class="col-lg-4"><a href="javascript:sort('username');">Username</a></th>
			<th class="col-lg-4"><a href="javascript:sort('date');">Date Accessed</a></th>			
			<th class="col-lg-6 text-right"><button class="btn admin export">Export CSV</button></th>
		</tr>
	</thead>
	<tbody>
		{{#each data}}
			<tr id="{{id}}">			
				<td>{{hmmlProjectNumber}}</td>
				<td>{{username}}</td>	
				<td>{{date}}</td>			
				<td></td>
			</tr>		
		{{/each}}
	</tbody>	
</script>

<script id="user-report-template" type="text/x-handlebars-template">
	<thead>
		<tr>			
			<th class="col-lg-4"><a href="javascript:sort('username');">Username</a></th>
			<th class="col-lg-4"><a href="javascript:sort('hmmlProjectNumber');">Object</a></th>
			<th class="col-lg-4"><a href="javascript:sort('date');">{{dateLabel}}</a></th>
			<th class="col-lg-6 text-right"><button class="btn admin export">Export CSV</button></th>
		</tr>
	</thead>
	<tbody>
		{{#each data}}
			<tr id="{{id}}">				
				<td>{{username}}</td>
				<td>{{hmmlProjectNumber}}</td>
				<td>{{date}}</td>
				<td></td>
			</tr>		
		{{/each}}
	</tbody>
</script>

<script id="popularity-report-template" type="text/x-handlebars-template">
	<thead>
		<tr>			
			<th class="col-lg-6"><a href="javascript:sort('_term');">{{aggregationFieldLabel}}</a></th>
			<th class="col-lg-6"><a href="javascript:sort('_count');">Number of Hits</a></th>
			<th class="col-lg-6 text-right"><button class="btn admin export">Export CSV</button></th>
		</tr>
	</thead>
	<tbody>
		{{#each data}}
			<tr id="{{id}}">
				<td>{{key}}</td>
				<td>{{count}}</td>				
				<td></td>			
			</tr>		
		{{/each}}
	</tbody>
</script>
<!-- 
Object
	- Object
	- Username
	- Date Accessed
Cataloger
	- Cataloger
	- Object
	- Date added/edited
Popularity
	- Country/City/Repo/Collection/Object
	- Number of hits
	- From Date
	- To Date
User
	- username
	- object
	- date accessed -->