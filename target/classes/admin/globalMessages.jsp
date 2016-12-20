<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h3>Site-Wide Alert Messages</h3>
<button id="addMessageButton" class="btn btn-success">Add Message</button>

<c:if test="${vhmmlGlobalMessages != null}">
	<table id="messagesTable" class="table table-striped table-hover">	
		<caption class="small text-left">
			<em>Messages currently being displayed</em>
		</caption>		
		<thead>
			<tr>
				<th class="col-md-10">Message</th>
				<th class="col-md-1">Severity</th>
				<th class="col-md-1 text-center">Actions</th>				
			</tr>
		</thead>
		<tbody></tbody>		
	</table>
</c:if>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/admin/global-messages.js?version=${applicationVersion}"></script>

<script type="text/javascript">
	$(function() {
		new GlobalMessagesAdmin();
	});
</script>