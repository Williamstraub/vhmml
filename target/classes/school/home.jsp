<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/school.css?version=${applicationVersion}" />

<div class="row">
	<div class="col-lg-3">	
		<div class="block-image-wrapper">
			<div>
				<img src="${pageContext.request.contextPath}/static/img/placeholder.png" class="block-image" />
			</div>
			<div class="info-icon-wrapper">
				<span class="info-icon">
					<i class="glyphicon glyphicon-info-sign pointer" aria-hidden="true" data-toggle="tooltip" title="Image from placeholder" data-placement="right" data-trigger="hover"></i>
				</span>
			</div>			
		</div>			
	</div>
	<div class="col-lg-9">

		<p>School provides a place for HTML-based lessons</p>
		
		
	</div>

</div>

<div class="row">
	<div class="col-lg-12">
		
		<h3 class="lessonTitleIntro">
			<span class="school-underline">Section 1</span>
		</h3>

		<!--Collapsing curtain accordion from http://api.jqueryui.com/accordion/-->
		<div class="accordion">
			<h4 class="lessonHeading">Sample lesson A</h4>
			<div class="lessons">
				<a
					href="${pageContext.request.contextPath}/school/lesson/basics-paleography/overview"
					class="lessonLink"><img
					src="${pageContext.request.contextPath}/static/img/school/placeholder.jpg"
					width="121" height="90"
					alt="Portion of manuscript showing something">&nbsp;Basics</a>
				<div class="lessonDescription">Basic Introduction to </div>
				<hr />

				
				
			</div>
			<h4 class="lessonHeading">another section</h4>
			<div class="lessons">
				<%--<a
					href="${pageContext.request.contextPath}/school/lesson/.../introduction"
					class="lessonLink"> <img
					src="${pageContext.request.contextPath}/static/img/school/placeholder.png"
					width="121" alt="Portion of manuscript showing"> 
				</a> --%>
				<div class="lessonDescription"></div>

				<hr />
				
				


			</div>
			<h4 class="lessonHeading">third section(coming soon)</h4>
			<div>
				<p><img src="${pageContext.request.contextPath}/static/img/school/placeholder.jpg"
					width="121" height="90"
					alt="Portion of manuscript showing x being developed."></p>
			</div>


		</div>
		<!-- end of Accordion -->

		<h3 class="lessonTitleIntro">
			<%-- Transcription Lessons --%>
			<span class="school-underline">Section 2=</span>
		</h3>

		<div class="accordion">
			<h4 class="lessonHeading">more about X </h4>
			<div class="lessons">
				<a
					href="${pageContext.request.contextPath}/school/lesson/basics-transcription/purposes"
					class="lessonLink"><img
					src="${pageContext.request.contextPath}/static/img/school/placeholder.jpg"
					width="121" height="90"
					alt="Portion of ___ showing  x">&nbsp;Basics</a>
				<div class="lessonDescription">How and why to ...</div>
				<hr />

				
			</div>
		</div>

		<div id="backCover1"
			style="width: 5000px; height: 3000px; position: fixed; background: black; top: 0; left: 0; filter: alpha(Opacity = 50); opacity: 0.5; z-index: 9; display: none">
			&nbsp;</div>
	</div>

	<script type="text/javascript">
		$(function() {
			$('.accordion').accordion({
				heightStyle : 'content',
				active : false,
				collapsible : true
			});
		});
	</script>