<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="deleteButtonClass" value=""/>

<c:if test="${readingRoomObjectForm.readingRoomObjectId == null}">
	<c:set var="deleteButtonClass" value="hidden"/>		
</c:if>

<button type="button" class="btn btn-danger delete ${deleteButtonClass}" data-delete-url="${pageContext.request.contextPath}/catalogDatabase/delete">Delete</button>		
<button type="button" class="btn btn-warning cancel" data-cancel-url="/catalogDatabase">Cancel</button>
<button type="button" class="btn btn-success save saveAndClose" data-close-url="/catalogDatabase" data-home-page="Catalog Database">Save and Close</button>
<button type="button" class="btn btn-success save">Save</button>