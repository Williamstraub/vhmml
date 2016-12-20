<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
	#importsTable {
		display: none;
	}
</style>

<form method="POST" enctype="multipart/form-data">
	Import File: <input id="importFile" type="file" name="importFile">
	<div id="importButtonWrapper">		
		<p>
			<a href="${pageContext.request.contextPath}/catalogDatabase" role="button" class="btn button  btn-warning">Cancel/Return</a>
			<button id="importButton" class="btn btn-success">Import</button>
		</p>
	</div> 	
</form>

<div class="table-responsive">
	<table id="importsTable" class="table table-striped table-hover">
		<caption>Previous Imports</caption>		
		<thead>		
			<tr>
				<th class="col-md-3">File</th>
				<th class="col-md-2">Date</th>
				<th class="col-md-2">Status</th>
				<th class="col-md-4">Error</th>
				<th class="col-md-1"></th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
</div>

<script type="text/javascript">

$(function() {	
	
	var messageDialog = new Dialog();
	var imports = null;
	
	<c:if test="${not empty imports}">
		imports = ${imports};
	</c:if>
	
	if(imports) {
		refreshImportsTable(imports);	
	}
	
	$('#importButtonWrapper').on('click', '#importButton', function() {
		if($('#importFile').val()) {
			var $importButtonWrapper = $('#importButtonWrapper');
			var importWrapperHtml = $importButtonWrapper.html();
			var formData = new FormData();
			formData.append('importFile', $('#importFile')[0].files[0]);
			
			$.ajax({
				url: contextPath + '/catalogDatabase/import',
				data: formData,
				method: 'POST',
				contentType: false,
				processData: false,
				beforeSend: function() {
					Messages.removeAll();
					showSpinner({
						element: $importButtonWrapper,
						css: {'height': 'auto', 'display': 'inline-block'} 
					});
				},
				complete: function(response) {
					$importButtonWrapper.html(importWrapperHtml);
					Messages.showResponseMessage(response);		
					refreshImportsTable(JSON.parse(response.responseText).imports);
				}
			});
		} else {
			messageDialog.show({
				title: 'No Import File',
				message: 'Please select a file to import.',
				closeButtonCssClass: 'catalog-database'
			});
		}
		
		return false;
	});
	
	$('body').on('click', 'button.deleteImport', function() {
		var $button = $(this);
		var id = $button.attr('data-import-id');
		
		$.ajax({
			url: contextPath + '/catalogDatabase/import/delete/' + id,
			beforeSend: function() {
				Messages.removeAll();
				showSpinner({
					element: $button.parent('td'),
					css: {'height': 'auto', 'display': 'inline-block'} 
				});
			},
			complete: function(response) {
				Messages.showResponseMessage(response);		
				refreshImportsTable(JSON.parse(response.responseText).imports);
			}
		});
	});
});

function refreshImportsTable(imports) {
	if(imports && imports.length) {
		var html = '';
		
		for(var i = 0; i < imports.length; i++) {		
			var importData = imports[i];
			
			html += '<tr>';
				html += '<td>' + importData.importFile + '</td>';
				html += '<td>' + importData.displayDate + '</td>';
				html += '<td>' + importData.status + '</td>';								
				html += '<td>' + (importData.errorMessage ? importData.errorMessage : '') + '</td>';
				html += '<td>'; 
					if(importData.status == 'COMPLETED') {
						html += '<button class="btn btn-danger deleteImport" data-import-id="' + importData.id + '">Delete</button>';	
					}					
				html += '</td>';
								
			html += '</tr>';
		}
		
		$('#importsTable tbody').html(html);
		$('#importsTable').show();
	}
}

</script>