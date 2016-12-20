<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ attribute name="sectionPageName" required="true" rtexprvalue="true" %>
<%@ attribute name="label" required="true" rtexprvalue="true" %>

<c:set var="sectionUrl" value="/school/lesson/${lessonPath}/${sectionPageName}"/>

<c:if test="${selectedSectionUrl ==  sectionUrl}">
	<tiles:putAttribute name="selectedIndexItem" value="${label}" cascade="true"/>
</c:if>

<li><a href="${pageContext.request.contextPath}${sectionUrl}">${label}</a></li>