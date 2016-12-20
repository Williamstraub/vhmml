<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<c:set var="lessonIndex" scope="request">
	<%@ include file="lessonIndex.jsp"%>
</c:set>

<tiles:insertTemplate template="/WEB-INF/jsp/school/lesson-template.jsp">
	<tiles:putAttribute name="lessonName" value="Basics: Latin Paleography" />
	<tiles:putAttribute name="sectionName"
		value="Section 3: Classes of Script; How Script Looks and How It Is Executed" />
	<tiles:putAttribute name="previousSection" value="scripts-letterforms" />
	<tiles:putAttribute name="nextSection" value="exercises-1" />
	<tiles:putAttribute name="lessonIndex" value="${lessonIndex}" />
	<tiles:putAttribute name="lessonContent">
		<div class="row">
			<div class="col-lg-11">
				<h4 class="lessonSubheading">CLASSES OF SCRIPT: MAJUSCULE vs. MINUSCULE</h4>
				<p>
					Paleographers classify all scripts as either <b>majuscule</b> or <b>minuscule</b>.
				</p>
				<p>The technical definitions of these terms are different from
					our &#8220;upper case&#8221; and &#8220;lower case&#8221;, which are terms derived from
					typography.</p>
				<p>
					<b>Majuscule script:</b> A majuscule script is one in which <b>all
						letters are written as if between two imaginary lines.</b> That is,
					nothing sticks up above minim-height and nothing hangs down below
					the baseline. All letters are the same height. (There may be one or
					two letters with parts that poke up or down a tiny bit, but a
					script in which the overwhelming majority of letters fit between
					two notional lines is classified as a majuscule.) Here is an
					ancient example of a majuscule script:
				</p>
				
				<%-- <div id="openseadragon1"
					style="width: 900px; height: 400px; background-color: #666; border-radius: 1%;"></div>
				<script type="text/javascript">
					var viewer = OpenSeadragon({
						id : "openseadragon1",
						prefixUrl : "${pageContext.request.contextPath}/static/js/openseadragon/images/",
						defaultZoomLevel : "2",
						zoomPerClick: "1.4", showRotationControl : "true",
						tileSources : "${pageContext.request.contextPath}/static/js/openseadragon/images/stgallen1394/GeneratedImages/dzc_output.xml"
					});
				</script>
				--%>
				 <img
						src="${pageContext.request.contextPath}/static/img/school/basics-paleography/StGallen-CodSang1394-p12cropped.jpg" width="1000"
						alt="Latin parchment manuscript page from Saint Gallen, 1394">
				
				<br /><small><em>St. Gallen, Stiftsbibliothek, Cod. Sang.
						1394, p. 12. (<a href="http://www.e-codices.unifr.ch"
						target="_blank">www.e&#8209;codices.unifr.ch</a>)
				</em></small>

				<p>The script above, which we will study in the next unit, gives
					us our upper-case letters, but here we are concerned with why it
					qualifies as a majuscule script. Notice that, although <b>F</b> and <b>L</b>
					stick up slightly above minim-height and the tail of <b>Q</b> dips very
					slightly below the baseline, the overwhelming majority of letters
					fit between two notional lines. A glance at the page tells you that
					this is so.</p>
				<p>
					<b>Minuscule script:</b> A minuscule script, by contrast, is one
					that has ascenders and descenders, like our lower-case alphabet. A
					minuscule script can be defined as a script written between four
					imaginary lines: most letters sit on the baseline and reach only up
					to minim-height, but ascenders on some letters reach up to an
					imaginary line above minim height, and descenders reach down to an
					imaginary line below the baseline. Here is a minuscule script:
				</p>
			
						<img src="${pageContext.request.contextPath}/static/img/school/basics-paleography/StGallen-CodSang116-p3crop2.jpg" width="1000" alt="manuscript page from St. Gall  116" >
				
			
				<br /><small><em>St. Gallen, Stiftsbibliothek, Cod. Sang.
						116, p. 3. (<a href="http://www.e-codices.unifr.ch"
						target="_blank">www.e&#8209;codices.unifr.ch</a>)
				</em></small>
				<p>&nbsp;</p>
				<p>On this page, the top line is written in a majuscule script
					and the rest of the text is written in a minuscule script:</p>

				<%-- <div id="openseadragon3"
					style="width: 900px; height: 700px; background-color: #666; border-radius: 1%;"></div>
				<script type="text/javascript">
					var viewer = OpenSeadragon({
						id : "openseadragon3",
						prefixUrl : "${pageContext.request.contextPath}/static/js/openseadragon/images/",
						defaultZoomLevel : "2", 
						minZoomLevel : "0.8",
						maxZoomLevel : "10",
						zoomPerClick: "1.4", showRotationControl : "true",
						tileSources : "${pageContext.request.contextPath}/static/js/openseadragon/images/stgallen-codsang116-p3/GeneratedImages/dzc_output.xml"
					});
				</script>

				<noscript>
					JavaScript needs to be enabled in order to utilize the zoom viewer.
					A static image of the manuscript may be seen: <img
						src="${pageContext.request.contextPath}/static/js/openseadragon/images/stgallen-codsang116-p3/GeneratedImages/dzc_output_files/9/0_0.jpg"
						alt="Latin parchment manuscript page from Saint Gallen, 116">
				</noscript>
				--%>
				<img src="${pageContext.request.contextPath}/static/img/school/basics-paleography/StGallen-CodSang152-p3cropped.jpg" alt=" manuscript page from St. Gall  152" >
				
				<br /><small><em>St. Gallen, Stiftsbibliothek, Cod. Sang.
						152, p. 3. (<a href="http://www.e-codices.unifr.ch"
						target="_blank">www.e&#8209;codices.unifr.ch</a>)
				</em></small>
				<hr />
				<h4 class="lessonSubheading">HOW SCRIPT LOOKS AND HOW IT IS EXECUTED</h4>
				<p>
					<b>Aspect:</b> A script's aspect is its <b>general appearance</b>.
					The vocabulary of aspect is highly subjective and not at all
					standardized. A script may be spiky, cramped, spacious, rounded,
					squiggly &#8212; you name it. Even though descriptions of aspect are
					highly personal, it is helpful to evaluate aspect when you
					encounter a new script. See how it appears to you, especially in
					comparison to other scripts. A sense of its overall &#8220;look&#8221; will
					help you identify that script in future. You should evaluate aspect
					when looking at a page of script as a whole, rather than when you
					are looking close up at individual letters and their parts.
				</p>
				<p>
					<b>Ductus:</b> A script's ductus, by contrast, is a way of
					describing <b>what the scribe did as he was forming the letters</b>.
					The ductus consists of the number of strokes in a letter and the
					order of execution of those strokes. The concept of ductus may thus
					be used to describe how a letter <b>o</b> in one script may be made in a
					single, circular stroke of the pen, whereas in another it may be
					made of six individual angular strokes. We will not give a great
					deal of attention to ductus in this course, but it is a useful
					concept to bear in mind when you are more advanced in your work
					with manuscripts and you come to evaluate how the approaches of individual scribes
				 to a script differ from one another, or how the use of
					different kinds of pens affects the execution of letters.
				</p>
				<p>
					A note about the term <b>cursive</b>: We are familiar with the idea
					of cursive as meaning writing in which all the letters are joined
					together. That notion gets at the same general point as the
					technical definition of cursive in paleography. <b>A cursive
						script is one that is made with comparatively few lifts of the
						pen.</b>
				</p>
				<p>That may or may not mean that the letters are joined to one
					another. An example of a cursive way of forming letters is the
					example just given, in which an <b>o</b> is made in a single stroke
					instead of six separate strokes. You will see paleographers refer
					to cursive scripts as having a &#8220;simplified ductus&#8221;, which just
					means that their letters are made of fewer separate strokes.
					Cursive scripts are, in general, less time-consuming for the scribe
					to write than non-cursive scripts, though some cursives are so
					elaborate that they must have been as time-consuming as
					non-cursives.</p>
				<p>Paleographers have, over the centuries, tended to apply the
					term cursive to any script that appears hastily written. While
					this is not the technical definition of a cursive, it has led to
					the term &#8220;cursive&#8221; sticking to some scripts that we might not
					describe as cursive if we were planning a system of nomenclature
					from scratch. It is worth bearing in mind the technical definition
					of cursive while being prepared to learn the standard names for
					some scripts that have traditionally been known as &#8220;cursives.&#8221; We
					will encounter these at the very beginning and the very end of our
					course, in the units on Classical Antiquity and on Gothic Cursiva.</p>
				<hr />
				<p>Ready to test your grasp of the terms for classes of script
					and parts of letters? Go on to a short set of review exercises.</p>



			</div>
		</div>
		<p>&nbsp;</p>
	</tiles:putAttribute>
</tiles:insertTemplate>
