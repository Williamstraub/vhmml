<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<c:set var="lessonIndex" scope="request">
	<%@ include file="lessonIndex.jsp" %>
</c:set>

<tiles:insertTemplate template="/WEB-INF/jsp/school/lesson-template.jsp">
	<tiles:putAttribute name="lessonName" value="Basics: "/>
	<tiles:putAttribute name="sectionName" value="Section 3: Exercise"/>	
	<tiles:putAttribute name="sectionDescription" value="REVIEW PRINCIPLES OF "/>	
	<tiles:putAttribute name="previousSection" expression="principles-conventions"/>	
	<tiles:putAttribute name="lessonIndex" value="${lessonIndex}"/>
	
	<tiles:putAttribute name="lessonContent">
		<div class="row">
			<div class="col-lg-12">	
				<p>Which one of these things should you do when meeting somebody</p>
				<ol class="orderedlistalpha">
					<li>smile</li>
					<li>introduce yourself</li>
					<li>give your name</li>
					<li>all of the above</li>
				</ol>
			</div>
		</div>
		
		<div class="row">
			<div class="col-lg-12">
				<button class="btn left school showAnswer">Display the Answer</button>
				<span class="exerciseAnswer">
					<span class="answerTitle">The correct answer is ...</span>
					<span class="answer">
						You should ...
						<p>d. &nbsp;<span class="macronTitulus"> all of the above</span></p>
					</span>
				</span>
				<p>&nbsp;</p>
			</div>
		</div>    
		<p>&nbsp;</p>
	</tiles:putAttribute>
</tiles:insertTemplate>
	