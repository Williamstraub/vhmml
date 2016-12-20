<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${configValues['hide.reading.room'].value == 'false'}">
	<a href="${pageContext.request.contextPath}/readingRoom" class="reading-room-hover">Reading Room</a>&nbsp;|&nbsp;
</c:if>
<a href="http://www.hmml.org/oliver.html" class="oliver-hover" target="_blank">Legacy Catalog</a>&nbsp;|&nbsp;
<a href="${pageContext.request.contextPath}/school" class="school-hover">School</a>&nbsp;|&nbsp;

<c:if test="${configValues['hide.folio'].value == 'false'}">
	<a href="${pageContext.request.contextPath}/folio" class="folio-hover">Folio</a>&nbsp;|&nbsp;
</c:if>
<a href="${pageContext.request.contextPath}/lexicon" class="lexicon-hover">Lexicon</a>&nbsp;|&nbsp;
<a href="${pageContext.request.contextPath}/reference" class="reference-hover">Reference</a>
<%-- &nbsp;|&nbsp;<a href="${pageContext.request.contextPath}/scriptorium" class="scriptorium-hover">Scriptorium</a>--%>
<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER,ROLE_LEVEL_III_CATALOGER">
	&nbsp;|&nbsp;<a href="${pageContext.request.contextPath}/catalogDatabase" class="catalog-database-hover">Catalog Database</a>
</security:authorize>