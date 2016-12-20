<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<c:set var="lessonIndex" scope="request">
	<%@ include file="lessonIndex.jsp" %>
</c:set>

<tiles:insertTemplate template="/WEB-INF/jsp/school/lesson-template.jsp">
	<tiles:putAttribute name="lessonName" value="Basics: "/>
	<tiles:putAttribute name="sectionName" value="Section 1: The Purposes of "/>	
	<tiles:putAttribute name="nextSection" expression="principles-conventions"/>	
	<tiles:putAttribute name="lessonIndex" value="${lessonIndex}"/>
	
	<tiles:putAttribute name="lessonContent">
		<div class="row">
			<div class="col-lg-12">    	
				<p>enter HTML text and code here
				</p>
				<p>&nbsp;</p>
			</div>
		</div>
	</tiles:putAttribute>
</tiles:insertTemplate>
	