<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/lexicon.css?version=${applicationVersion}"/>
<div class="row">
	<div class="col-lg-12">
		<h2><span class="redtext italicstext">v</span>HMML Lexicon Help</h2>
		<h3>Search tips and suggestions</h3>
		<ul>
			<li>The search engine ignores "stop words" such as articles and
				conjunctions: a, an, the, and, etc.</li>

			<li>Hyphens and upper/lower case distinctions are irrelevant: <i>anglo
					saxon</i> returns the same result as <i>Anglo-Saxon</i>.
			</li>

			<li>You do not need to use double-quotes around multiple terms (
				" " ). For example, search for tinted drawing rather than "tinted
				drawing."</li>

			<li>Wildcards such as ? or * are not accepted in this release.</li>

			<li>Both American and British spellings should return the same
				results. Please let us know if this is not the case for your search.</li>

			
		</ul>
<p>Find any glitches in the search process? Have suggestions
				for improving <span
							class="redtext italicstext">v</span>HMML Lexicon? Please let us know using the "Corrections or
				additions?" link.
			Or you may <a href="${pageContext.request.contextPath}/contact">contact us at HMML</a>.
		</p>
		<h3>Background and current state of Lexicon</h3>
		<p>
			Lexicon is based on a core set of terms found in Michelle Brown's 
			<a href="http://www.bl.uk/catalogues/illuminatedmanuscripts/glossary.asp" target="_blank">Understanding
				Illuminated Manuscripts</a> (used by permission), amplified by terms and
			definitions developed by HMML staff. Lexicon has been designed to be
			an expandable resource that will be continually enriched with
			additional terms, equivalents in other languages, and bibliography.
			If "Related Terms," "Other Languages," and/or "Bibliography" do not
			yet contain any information, those tabs are disabled. We hope that
			there will be much more there in the future: please send us your
			suggestions by using the "Corrections or additions?" link.
		</p>
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
			<img src="${pageContext.request.contextPath}/static/img/missing_syriac_fonts.png" alt="series of rectangles where Syriac characters should be seen"  />
			
		<h3>Technical: Minimum system requirements</h3>
		<ul>
			<li>Screen size of at least iPad mini: 200 x 134.7 <abbr title="millimeters">mm</abbr> (7.87 x 5.30 <abbr title="inches">in</abbr>)</li>
			<li>Up-to-date browser (Google Chrome, Mozilla Firefox, Safari, or equivalent) with JavaScript enabled</li>
			<li>If running a Macintosh, OS X 10.9 or later</li>
		</ul>
		<h3 class="rrHelp">Known issues</h3>
		<ul>
			<li>Note: <span class="redtext italicstext">v</span>HMML has <em>not</em>
				been optimized for smaller devices such as iPhones or Android
				phones.
			</li>
			<li>Older Mac OS X systems prior to version 10.9 may appear to run, but specific features such as search and feedback corrections or additions may be inoperable.</li>
		</ul>	
		<p>&nbsp;</p>
		<button class="btn lexicon btn-sm" type="button" onclick="goBack()"><span aria-hidden="true" class="glyphicon glyphicon-arrow-left"></span>
			Go Back</button>
		<br />
		<script>
			function goBack() {
				window.history.back();
			}
		</script>
		<noscript>
			<p>&nbsp;Use the back button on your browser to return to the
				previous page</p>
		</noscript>

		<p>&nbsp;</p>

	</div>
</div>