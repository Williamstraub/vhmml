<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<c:set var="lessonIndex" scope="request">
	<%@ include file="lessonIndex.jsp"%>
</c:set>

<tiles:insertTemplate template="/WEB-INF/jsp/school/lesson-template.jsp">
	<tiles:putAttribute name="lessonName" value="Basics: Paleography" />
	<tiles:putAttribute name="sectionName"
		value="Section 1: Overview" />
	<%-- <tiles:putAttribute name="sectionDescription" value="OVERVIEW" />--%>
	<tiles:putAttribute name="nextSection" value="placeholder2" />
	<tiles:putAttribute name="lessonIndex" value="${lessonIndex}" />
	<tiles:putAttribute name="lessonContent">
	 
		<div class="row">
			<div class="col-lg-11">
				<h4 class="lessonSubheading">OVERVIEW</h4>
				<p>This lesson introduces the...</p><br />
				
				Placeholder
			</div>
		</div>
		<div class="row">Lake Tahoe 2015
			<div class="col-lg-12 lesson-image">
				<div id="openseadragon1"
					style="width: 900px; height: 500px; background-color: #666; border-radius: 1%;"></div>
				<script type="text/javascript">
					var viewer = OpenSeadragon({
						id : "openseadragon1",
						prefixUrl : "${pageContext.request.contextPath}/static/js/openseadragon/images/",
						defaultZoomLevel : "2", // normally defaults to 0
						minZoomLevel : "0.5",
						maxZoomLevel : "10",
						zoomPerClick: "1.4", showRotationControl : "true",
						tileSources : "${pageContext.request.contextPath}/static/js/openseadragon/images/tahoe-wstraub/GeneratedImages/dzc_output.xml"
					});
				</script>

				<noscript>
					JavaScript needs to be enabled in order to utilize the zoom viewer.
					A static image of the manuscript may be seen: <img
						src="${pageContext.request.contextPath}/static/js/openseadragon/images/tahoe-wstraub/GeneratedImages/dzc_output_files/9/0_0.jpg"
						alt="manuscript page from St. Gallen 1394">
				</noscript>
				<small><em>photo by William Straub
				</em></small>

			</div>
		</div>
		<div class="row">
			<div class="col-lg-11">

				<h4 class="lessonSubheading">second section of placeholder text</h4>
				<p>blah blah blah
					</p>


			</div>
		</div><br />
		<div class="row">
			
			<div class="col-sm-6 col-md-7 col-lg-3 lesson-image">
				 Use the Microsoft Expression free Deep Zoom Composer to create openseadragon zoomable images as type Seadragon Ajax
				<div id="openseadragon2"
					style="width: 265px; height: 400px; background-color: #666; border-radius: 1%;"></div>
				<script type="text/javascript">
					var viewer = OpenSeadragon({
						id : "openseadragon2",
						prefixUrl : "${pageContext.request.contextPath}/static/js/openseadragon/images/",
						//defaultZoomLevel : "2", // normally defaults to 0
						minZoomLevel : "0.8",
						maxZoomLevel : "10",
						zoomPerClick: "1.4", showRotationControl : "true",
						tileSources : "${pageContext.request.contextPath}/static/js/openseadragon/images/___/GeneratedImages/dzc_output.xml"
					});
				</script>
				<noscript>
					JavaScript needs to be enabled in order to utilize the zoom viewer.
					A static image of the manuscript may be seen: <img
						src="${pageContext.request.contextPath}/static/js/openseadragon/images/____/GeneratedImages/dzc_output_files/9/0_0.jpg"
						alt="description from">
				</noscript>

				<small><em>some citation <br />(some link)
				</em></small> 
				<br />
			</div>
			
		</div><p>&nbsp;</p>
	</tiles:putAttribute>
</tiles:insertTemplate>