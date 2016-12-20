<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<c:set var="lessonIndex" scope="request">
	<%@ include file="lessonIndex.jsp"%>
</c:set>

<tiles:insertTemplate template="/WEB-INF/jsp/school/lesson-template.jsp">
	<tiles:putAttribute name="lessonName" value="Basics:  Paleography" />
	<tiles:putAttribute name="sectionName"
		value="Section 2:  Talking About " />
	<tiles:putAttribute name="previousSection" value="overview" />
	<tiles:putAttribute name="nextSection" value="exercises-1" />
	<tiles:putAttribute name="lessonIndex" value="${lessonIndex}" />
	<tiles:putAttribute name="lessonContent">
		<div class="row">
			<div class="col-lg-11">
				<h4 class="lessonSubheading">TALKING ABOUT x</h4>
				<p>
				 Use the Microsoft Expression free Deep Zoom Composer to create openseadragon zoomable images as type Seadragon Ajax	
				</p>


				<div id="openseadragon1"
					style="width: 900px; height: 700px; background-color: #666; border-radius: 1%;"></div>
				<script type="text/javascript">
					var viewer = OpenSeadragon({
						id : "openseadragon1",
						prefixUrl : "${pageContext.request.contextPath}/static/js/openseadragon/images/",
						defaultZoomLevel : "2", // normally defaults to 0
						minZoomLevel : "0.8",
						maxZoomLevel : "10",
						zoomPerClick: "1.4", showRotationControl : "true",
						tileSources : "${pageContext.request.contextPath}/static/js/openseadragon/images/tahoe-wstraub/GeneratedImages/dzc_output.xml"
					});
				</script>

				<noscript>
					JavaScript needs to be enabled in order to utilize the zoom viewer.
					A static image of the manuscript may be seen: <img
						src="${pageContext.request.contextPath}/static/js/openseadragon/images/tahoe-wstraub/GeneratedImages/dzc_output_files/9/0_0.jpg"
						alt="description of Lake Tahoe6">
				</noscript>
				<small><em>Lake Tahoe zoomable image by William  Straub
				</em></small>
				<p>&nbsp;</p>

				<p>&nbsp;</p>
				
			</div>
		</div>
	</tiles:putAttribute>
</tiles:insertTemplate>
