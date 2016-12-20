<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="referenceGroups" required="true" rtexprvalue="true" type="java.util.List" %>
<%@ attribute name="onSelectNode" required="false" rtexprvalue="true" %>
<%@ attribute name="showImportedCount" required="false" rtexprvalue="true" %>
<%@ attribute name="hideEmptyCollections" required="false" rtexprvalue="true" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jstree.min.js"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/js-tree/style.min.css"/>

<div id="referenceTree">
	<ul>
		<li id="rootNode">Reference Library
			<ul>
				<c:forEach var="group" items="${referenceGroups}">
					<li id="${group.id}" class="group">${group.name}
						<c:if test="${group.collections.size() > 0}">
							<ul>
						</c:if>
							<c:forEach var="collection" items="${group.collections}">
								<c:if test="${!hideEmptyCollections || collection.importedItemCount > 0}">
									<li id="${collection.key}" class="collection" data-item-count="${collection.importedItemCount}">
										${collection.name}
										<c:if test="${showImportedCount}">
											&nbsp;(${collection.importedItemCount})
										</c:if>
									</li>
								</c:if>								
							</c:forEach>
						<c:if test="${group.collections.size() > 0}">
							</ul>
						</c:if>
					</li>
				</c:forEach>
			</ul>
		</li>		
	</ul>
</div>

<script type="text/javascript">	

	$(function() {
		
		var selectNodeCallback = eval('${onSelectNode}');
		 
		$('#referenceTree').jstree({
			'plugins': ['crrm'],
			'core': {
				'check_callback' : function (operation, node, node_parent, node_position, more) {
		            // operation can be 'create_node', 'rename_node', 'delete_node', 'move_node' or 'copy_node'
		            // in case of 'rename_node' node_position is filled with the new node name
		            return true;
		        }
			}
		});
		
		$('#referenceTree').jstree('open_node', $('#rootNode'));
		
		$('#referenceTree').on('select_node.jstree', function (node, selected, event) {
			if(selectNodeCallback) {
				selectNodeCallback.call(this, node, selected, event);	
			}									
		});		
	});

</script> 