<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<c:set var="lessonIndex" scope="request">
	<%@ include file="lessonIndex.jsp" %>
</c:set>

<tiles:insertTemplate template="/WEB-INF/jsp/school/lesson-template.jsp">
	<tiles:putAttribute name="lessonName" value="Basics:"/>
	<tiles:putAttribute name="sectionName" value="Section 2: "/>		
	<tiles:putAttribute name="previousSection" expression="purposes"/>	
	<tiles:putAttribute name="nextSection" expression="exercise"/>	
	<tiles:putAttribute name="lessonIndex" value="${lessonIndex}"/>
	
	<tiles:putAttribute name="lessonContent">
		<div class="row">
			<div class="col-lg-12">
				<h4 class="lessonSubheading">PLACEHOLDER</h4>
				<p>
					<strong>add HTML markup here</strong> 
				</p>	
				<p>&nbsp;</p>
			</div>
		</div>
	</tiles:putAttribute>
</tiles:insertTemplate>
	