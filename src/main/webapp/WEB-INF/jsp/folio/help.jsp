<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/vhmml-search-page.css?version=${applicationVersion}"/>
<style type="text/css">
	.main-content {
		padding: 140px 40px 40px 40px;
	}
</style>
<h2><span class="redtext italicstext">v</span>HMML Folio Help </h2>
<p>
	<a href="#OVERVIEW">OVERVIEW</a>&nbsp;|&nbsp;
	<a href="#SEARCHING">SEARCHING</a>&nbsp;|&nbsp;
	<a href="#SYRIAC">SYRIAC</a>&nbsp;|&nbsp;
	<a href="#TECHNICAL">TECHNICAL</a>
 </p>
 <h3 id="OVERVIEW" class="rrHelp jumptarget-breadcrumbs">Overview</h3>
<span class="redtext italicstext">v</span>HMML Folio offers a unique subset of <a href="${pageContext.request.contextPath}/readingRoom">Reading Room</a> manuscripts,
richly described and many with transcriptions. Besides reading paleographic descriptions and viewing some beautiful manuscript examples, Folio is designed to be useful for 
comparing manuscripts so that one in a user's hands may be compared with detailed Folio entries to ascertain script type and approximate dates. Signing in or registration is not required to view any of the Folio records or images. 

<h3 id="SEARCHING" class="rrHelp jumptarget-breadcrumbs">Searching <span class="redtext italicstext">v</span>HMML Folio</h3>
To explore Folio, we suggest that you begin by clicking Search button without any specific search parameters. Using the filters in the &#8220;Search&#8221; panel, you may assemble your own exhibit of images, according to your interests and curiosity. Clicking on the thumbnail images in your filtered search results will take you to the image and paleographic description. 
<ul>
	<li><span class="rrHelpSubheading">Keyword (BETA)</span>This field works sort of like a Google search. Capitalized and lowercase characters are treated equally. Wildcard searching is allowed using an * (asterisk) to represent zero or more unknown characters. Exact phrase searching using quotation marks/double quotes ( "" ) will limit results by finding words and spaces together. Words must be spelled exactly or else they won't be found if using double quotes.</li>
	<li><span class="rrHelpSubheading">Note that Keyword search</span> is designed to work with at least 3 entered characters. Searches with fewer than 3 letters will probably yield suboptimal results. Keyword search does <i>not</i> currently support Boolean searching (using AND/OR/NOT operators).</li>
	<li><span class="rrHelpSubheading">Date Range</span> If you use the <i>Date Range</i> slider, set it with generous room on either side of your target date. The Date Range slider is set to be plus or minus 5 years.</li>
	<%-- <i>Note: once you activate the</i> Date Range <i>slider, manuscripts with no date, or no cataloger-supplied date, will be excluded from the results.</i> For undated manuscripts  materials a cataloger often suggests an approximate century, but some records have no date whatsoever. 
	<li>Adjusting the Date Range slider, even slightly, will eliminate finding any objects that are not dated. Move the Date Range slider handles out to the extremes to include any date or unknown date. </li>--%>
	<li>After viewing a record description, please use the Back to Results <img src="${pageContext.request.contextPath}/static/img/back_to_results-Folio.png" width="62px"  alt="Back to Results button" /> button rather than the back button on your browser.</li>
</ul>
We hope that you will contribute to Folio by telling us what you see using the  &#8220;Additions or corrections?&#8221; link at the upper right corner of each record.
<h3 id="SYRIAC" class="rrHelp jumptarget-breadcrumbs">A special note about Syriac</h3>	
	If you see a series of small boxes where you expect to see Syriac characters, here are suggestions for various platforms:
 			<ul>
 				<li>iOS devices: iPads or other iOS devices will not display
				Syriac fonts until you install a third-party application. You might
				consider this free app or ones like it that will render Syriac
				Unicode: <a
				href="https://www.apptopia.com/apps/itunes_connect/980346488/about"
				target="_blank">https://www.apptopia.com/apps/itunes_connect/980346488/about</a>
				</li>
				<li> Mac OS X computers: if Syriac fonts need to be installed, we
				recommend following these directions: <a
				href="http://www.bethmardutho.org/index.php/syriac-mac.html"
				target="_blank">http://www.bethmardutho.org/index.php/syriac-mac.html</a>
				</li>
				<li>On a Windows PC: one solution is to  
				<a href="http://www.bethmardutho.org/index.php/resources/fonts.html" target="_blank">download the free Meltho Fonts.</a>
				After unzipping the melthofonts-xxx.zip file, open the folder where the fonts were unzipped, select all of the .OTF files, right-click and then click Install.
				If the fonts were installed correctly, instead of the above rectangles you should now see Syriac characters.<br />
				<br />
				Microsoft Internet Explorer 11 and Edge browsers do not render Syriac plurals correctly. If users wish to view Syriac records with 
				native script, we recommend utilizing a different browser other than IE 11 or Microsoft Edge.
				</li>
			</ul>	
			<img src="${pageContext.request.contextPath}/static/img/missing_syriac_fonts.png"	alt="series of rectangles where Syriac characters should be seen" />
			
<h3 id="TECHNICAL" class="rrHelp jumptarget-breadcrumbs">Technical: Minimum system requirements</h3>
<ul>
	<li>Screen size of at least iPad mini: 200 x 134.7 <abbr title="millimeters">mm</abbr> (7.87 x 5.30 <abbr title="inches">in</abbr>)</li>
	<li>Up-to-date browser (Google Chrome, Mozilla Firefox, Safari, or equivalent) with JavaScript enabled</li>
	<li>If running a Macintosh, OS X 10.9 or later</li>
</ul>
<span class="redtext italicstext">v</span>HMML Folio is the Java-based successor to the Meteor-based Folio version 1.0.
<h3 class="rrHelp">Known issues</h3>
		<ul>
			<li>Note: <span class="redtext italicstext">v</span>HMML has <em>not</em>
				been optimized for smaller devices such as iPhones or Android
				phones.
			</li>
			<li>Older Mac OS X systems prior to version 10.9 may appear to run, but specific features such as search and feedback corrections or additions may be inoperable.</li>
			<li>Because of the high-resolution of the images used in <span class="redtext italicstext">v</span>HMML, users with slow internet connections may experience a delay in image loading.</li>
		</ul>
<h3 class="rrHelp"> Still having problems? </h3>
<a href="${pageContext.request.contextPath}/contact">Please contact us </a>
<p>&nbsp;</p>
<button class="btn folio btn-sm" onclick="goBack()"><span aria-hidden="true" class="glyphicon glyphicon-arrow-left"></span>
			Go Back</button><br />
		<script>
			function goBack() {
				window.open( document.referrer,"_self");
			}
		</script>
		<noscript>
			<p>&nbsp;Use the back button on your browser to return to the
				previous page</p>
		</noscript>
		<p>&nbsp;</p>