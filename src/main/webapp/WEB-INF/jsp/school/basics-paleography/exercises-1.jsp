<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<c:set var="lessonIndex" scope="request">
	<%@ include file="lessonIndex.jsp"%>
</c:set>

<tiles:insertTemplate template="/WEB-INF/jsp/school/lesson-template.jsp">
	<tiles:putAttribute name="lessonName" value="Basics: Paleography" />
	<tiles:putAttribute name="sectionName" value="Review exercise" />
	
	<tiles:putAttribute name="previousSection" value="placeholder2" />

	<tiles:putAttribute name="lessonIndex" value="${lessonIndex}" />
	<tiles:putAttribute name="lessonContent">
	<style type= "text/css" media= "all">
		ol li {list-style-type: lower-alpha;} 
	</style>
		<div class="row">
			<div class="col-lg-11">
				<h4 class="lessonSubheading">Exercise 1: </h4>
				<p>Fish swim in the...</p>
				
				<ol>
					<li>trees</li>
					<li>prairie</li>
					<li>sea</li>
				</ol>
				
				<div class="row">
					<div class="col-lg-12">
						<button class="btn left school showAnswer">Display the
							Answer</button>
						<span class="exerciseAnswer">
							<p>
								c. sea
							</p>
						</span>
					</div>
				</div>
				<hr />
				<h4 class="lessonSubheading">Exercise 2: </h4>

				<p>Which one of these matches the image?</p>

			</div>
			<div class="row">
				<div class="col-lg-4 col-md-6  lesson-image">
					&nbsp;a.
					<img
							src="${pageContext.request.contextPath}/static/img/school/basics-paleography/placeholder.jpg"
							alt="placeholder text description for assistive devices ">
					
					<br /><small><em>citation
					</em></small>

				</div>
				<div class="col-lg-4 col-md-6 lesson-image">
					&nbsp;b.
					 <img
							src="${pageContext.request.contextPath}/static/img/school/basics-paleography/placeholder.png"
							alt="image description">
					
					<br /><small><em>citation number two<a
							href="https://creativecommons.org/licenses/by-nc-sa/3.0/"
							target="_blank">CC BY-SA license</a>.
					</em></small>
				</div>
				<div class="col-lg-4 col-md-6 lesson-image">
					&nbsp;c.
					  <img src="${pageContext.request.contextPath}/static/img/school/basics-paleography/placeholder.png"
					  alt="image description">
					
					<br /><small><em>citation #3
					</em></small>
					<p></p>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<button class="btn left school showAnswer">Display the
						Answer</button>
					<span class="exerciseAnswer">
						<p>
							b. <em>description of the image and explanation of why this is the correct answer
						</p>
						<hr />
					</span>
				</div>
			</div>
			<p>&nbsp;</p>
		</div>
	</tiles:putAttribute>
</tiles:insertTemplate>
