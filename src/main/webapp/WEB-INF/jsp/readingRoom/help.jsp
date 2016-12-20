<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/vhmml-search-page.css?version=${applicationVersion}"/>

<style type="text/css">
	.main-content {
		padding: 140px 40px 40px 40px;
	}
</style>

<h2><span class="redtext italicstext">v</span>HMML Reading Room Help </h2>
<p>
	<a href="#OVERVIEW">OVERVIEW</a>&nbsp;|&nbsp;
	<a href="#SEARCHING">SEARCHING</a>&nbsp;|&nbsp;
	<a href="#IMAGES">IMAGES</a>&nbsp;|&nbsp;
	<a href="#SIGNIN">SIGN IN</a>&nbsp;|&nbsp;
	<a href="#SYRIAC">SYRIAC</a>&nbsp;|&nbsp;
	<a href="#TECHNICAL">TECHNICAL</a>					

	<h3 id="OVERVIEW" class="rrHelp jumptarget-breadcrumbs">Overview</h3>
In <span class="redtext italicstext">v</span>HMML Reading Room you can search <a href="http://www.hmml.org/" target="_blank">HMML</a>&#8217;s extensive microform and digital collections. For many items you can also view high-resolution color images or scans of black and white microfilm. Eventually <span class="redtext italicstext">v</span>HMML Reading Room will contain all manuscript and printed items digitized by HMML since 2003 as well as an extensive selection of scanned films from earlier projects.  
<span class="redtext italicstext">v</span>HMML Reading Room will also contain many rare books (both manuscript and printed) from HMML&#8217;s own holdings.


<h3 id="SEARCHING" class="rrHelp jumptarget-breadcrumbs">Searching <span class="redtext italicstext">v</span>HMML Reading Room</h3>
<span class="redtext italicstext">v</span>HMML Reading Room is a successor to HMML&#8217;s previous online catalog, <a href="http://www.hmml.org/oliver.html" target="_blank">OLIVER</a>, and image database, <i><a href="http://cdm.csbsju.edu/" target="_blank">Vivarium</a></i>. <span class="redtext italicstext">v</span>HMML Reading Room 
presents each manuscript or printed object at three levels of description: 
<ul class="listWithinParagraph">
  <li>as a whole physical <i>object</i> with identifiable characteristics (binding, dimensions, shelfmark, etc.)</li>
  <li>as consisting of a <i>part</i> or <i>parts</i> (usually only one, but there are many multi-part objects in the collection)</li>
  <li>as containing textual <i>items</i> within a <i>part</i></li>
</ul>
  As you view the results of your search, you will see this underlying structure expressed most clearly in the tabs for <i>Object Description</i> and <i>Contents</i>.

<p><em><span class="rrHelpReminderHeading">REMINDER</span>: Reading Room does not yet contain all of HMML&#8217;s collections. You can view a 
</em><a href="${pageContext.request.contextPath}/readingRoom/collections">list of collections</a> <em>already in Reading Room. For others, you will need to consult 
the</em> <a href="http://www.hmml.org/oliver.html" target="_blank">Legacy Catalog</a> <em>or see the</em> <a href="${pageContext.request.contextPath}/readingRoom/uncatalogedCollections">list of uncataloged collections</a>.</p> 

<p>Unlike traditional printed catalogs, <span class="redtext italicstext">v</span>HMML Reading Room is an ever-expanding and ever-improving work in progress. The metadata has been reviewed and conformed to new standards, but you will find that the degree of detail in cataloging varies by collection. Some objects are richly described, while others contain only very basic metadata.
As a result, your search criteria will be applied to collections that may or may not have a level of description sufficient to support a highly-detailed search. Most entries have a <i>Genre/Form</i> indication and author&#8217;s names have been standardized and are consistent across collections.  We hope that you will contribute to the cataloging effort by telling us what you see using the &#8220;Additions or corrections?&#8221; link at the upper right corner of each record.</p> 
<ul>
  <li><span class="rrHelpSubheading">Keyword</span> This field works sort of like a Google search, in that various fields are searched EXCEPT for HMML project number. Capitalized and lowercase characters are treated equally. Wildcard searching is allowed using an * (asterisk) to represent zero or more unknown characters. Exact phrase searching using quotation marks/double quotes ( "" ) will limit results by finding words and spaces together. Words must be spelled exactly or else they won't be found if using double quotes.</li>
  <%--The ? character (question mark) represents an unknown character. --%>
  <li><span class="rrHelpSubheading">Keyword (BETA)</span> This  release does not yet support Boolean searching (using AND/OR/NOT operators). </li>
  <li><span class="rrHelpSubheading">Name/Title</span> <i>Name</i> searches any personal name associated with the object or its contents, including authors, scribes, artists, owners, and other associated names. <i>Title</i> searches several title-related fields. Because there is great variation in titles, you may need to try different forms.</li>
  <li><span class="rrHelpSubheading">Filters</span> Checkboxes and dropdowns will narrow and focus your search, as will the <i>Date Range</i> slider (see below).</li>
  <li><span class="rrHelpSubheading">Image availability</span> The checkboxes at the bottom of the search panels can be used to restrict your search to manuscripts or printed materials that have images available in <span class="redtext italicstext">v</span>HMML Reading Room. They can also be used to limit search results by type of images and level of access.</li>
  <li><span class="rrHelpSubheading">Date Range</span> <i>Note: once you activate the</i> Date Range <i>slider, manuscripts or printed materials with no date, or no cataloger-supplied date, will be excluded from the results.</i> For undated manuscripts or printed materials, a cataloger often suggests an approximate century, but some records have no date whatsoever. If you use the <i>Date Range</i> slider, set it with generous room on either side of your target date.</li>
  <li><span class="rrHelpSubheading">Native script</span> You can use Unicode characters for native script searching of those fields likely to contain native script entries, such as <i>Name</i>, <i>Title</i>, and <i>Incipits</i>. <i>Keyword</i> searches will also find native script entries. Note: the base language of <span class="redtext italicstext">v</span>HMML Reading Room is English. Native script is not used in every field, but only where useful for describing the manuscript or printed object.</li>
  <li><span class="rrHelpSubheading">Country/City/Repository</span> These fields are linked and nested. If you choose a <i>Country</i> from the dropdown menu, only the cities in that country will appear in the <i>City</i> dropdown. If you then choose a <i>City</i>, the <i>Repository</i> dropdown will show only the repositories from the selected city.</li>
  <li><span class="rrHelpSubheading">Genre/Form</span> These controlled vocabulary terms are taken from the standard thesauri used by special collections librarians and archivists: the <a href="http://www.getty.edu/research/tools/vocabularies/aat/" target="_blank">Art &amp; Architecture Thesaurus (AAT)</a>, and the <a href="http://rbms.info/vocabularies/index.shtml" target="_blank">RBMS Controlled Vocabularies</a> from the Bibliographic Standards Committee of the Rare Books and Manuscripts Section of the Association of College and Research Libraries/American Library Association. 
  You can <a href="javascript:showGenres();">see a current list of our genre/form terms here</a>.</li>
  <li><span class="rrHelpSubheading">Features</span> HMML uses a list of controlled vocabulary for features. You can <a href="javascript:showFeatures();">see a current list of our features here</a>.</li>
  <li><span class="rrHelpSubheading">Record type</span> <i>Manuscript</i> corresponds to records that only have been cataloged as manuscripts in a repository, whether a bound volume or simply as loose leaves. <i>Print</i> indicates printed works, whether books or broadsheets. <i>Manuscript &amp; Print</i> identifies records that include both manuscript and printed material, such as a bound volume that contains both a printed work and manuscript. <i>Archival Material</i> pertains specifically to objects that have been cataloged according to modern archival standards within private, church, or state archives. In some instances, archival material is housed as a manuscript or print within a repository, and as such should be searched under manuscript or print.</li>
</ul>

<h3  class="rrHelp">Viewing search results</h3>
<ul class="withHeading">
  <li><span class="rrHelpSubheading">Narrowing your results</span> You can use other search panels and filters to narrow your initial results; you do not need to start over. <span class="redtext italicstext">v</span>HMML Reading Room  offers <i>Sort by</i> with several options ascending and descending  at the top of the results screen.</li>
  <li><span class="rrHelpSubheading">Image status</span> The image thumbnail will tell you immediately if the manuscript or printed item is available in <span class="redtext italicstext">v</span>HMML Reading Room in color digital form or as a scanned black and white microform. The icon of a microfilm reel indicates a film that has not been scanned; you can <a href="http://www.hmml.org/manuscript-order-form.html" target="_blank">order a scanned copy</a> by clicking on the link next to the icon. </li>
  <li><span class="rrHelpSubheading">Viewing Options</span> You can view the results in several display formats: Overview, Object Description, Contents. </li>
  <li><span class="rrHelpSubheading">External authorities</span> When standardized external authority records are available for authors, titles, subjects, countries, cities, or repositories, you will see a clickable link for the corresponding record at the <a href="http://id.loc.gov/authorities/names"  target="_blank">Library of Congress &nbsp;<img src="${pageContext.request.contextPath}/static/img/library_of_congress.png" width="22" height="19" alt="Library of Congress Authorities" /></a>&nbsp;and/or 
	the  <a href="http://viaf.org/" target="_blank">Virtual International Authority File &nbsp;<span class="viafLink">VIAF</span></a>. These links will open in a separate browser tab. </li>
  <li><span class="rrHelpSubheading">Foliation/Pagination</span> In some cases the foliation/pagination indicated on the image will not correspond exactly to that contained in the cataloging. The discrepancy will never be very great.</li>
</ul>

<h3 class="rrHelp">Some search examples</h3>
	<ul>
			<li>Keyword search: searching Keyword for CCM 00020 returns zero results. Instead search the HMML Project Number field for CCM 00020.</li>
			<li>Keyword search for homilies NOT prayers does not work as expected. Boolean searching using  NOT hasn't been implemented yet. Instead search for homilies. </li>
			<li>Keyword search for 242 leaf(ves) doesn't show expected results. Keyword does <i>not</i> search all fields; in this case specifically, extent is not searched. Try searching other keywords instead. </li>
			<li>If user is unsure as to how the system has spelled John Chrysostom. Conduct a keyword search using the asterisk wildcard for: chr*om.</li>
			<li>Using double quotes: searching for "spiritual exercises" won't find "spiritual exercise", but it will find "Spiritual Exercises".</li>
	</ul>
<h3 class="rrHelp">Search tips</h3>
	<ul>
			<li>Adjusting the Date Range slider, even slightly, will eliminate finding any objects that are not dated. Move the Date Range slider handles out to the extremes to include any date or unknown date. </li>
			<li>After viewing a record description, please use the Back to Results <img src="${pageContext.request.contextPath}/static/img/back_to_results.png" width="62px"  alt="Back to Results button" /> button rather than the back button on your browser.</li>
			<li>Keyword search is designed to work with at least 3 entered characters. Searches with fewer than 3 letters will probably yield suboptimal results.</li>
			<li>Keyword search is currently the only field that works with asterisk (*) wildcard character. </li>
			<li>The Date Range slider is set to be plus or minus 5 years, in part due to the possibly inexact dates of our catalog records.</li>
			<li>Dimensions of objects are always listed in order: height <i>x</i> width (and optionally <i>x</i> thickness)  in centimeters (cm). Keyword search does not search the Dimensions nor Condition Notes fields.</li>
			<li>If you would like to browse, leave the keyword field blank and select a Language or Genre/Form from the drop-down menu and click the Search button. You may also browse by other fields as well.</li>
	</ul>
	
<h3 id="IMAGES" class="rrHelp jumptarget-breadcrumbs">Viewing images</h3>
<ul class="withHeading">
  <li><span class="rrHelpSubheading">Non-registered versus registered access</span>/ Most of our partners require that you register with <span class="redtext italicstext">v</span>HMML before viewing images of their manuscripts or printed items. This is to make you aware of certain rights reserved by the owning institutions. <a href="${pageContext.request.contextPath}/registration">Registration</a> is one-time and no-cost. Once your registration has been confirmed by HMML, you will be able to browse the entire digital collection by logging in with your email address and password.</li>
  
  <li><span class="rrHelpSubheading">Restricted access</span> Most of HMML's pre-2003 microfilms have not been scanned, and we do not have permission to host images of them in <span class="redtext italicstext">v</span>HMML Reading Room. Nor can we host scans of microfilms purchased from other libraries. The major exception is the EMML Collection of microfilmed Ethiopian manuscripts, many of which have been scanned and will become available in Reading Room. All materials, digital and analog, can be used on-site when <a href="http://www.hmml.org/visit-hmml.html" target="_blank">visiting HMML</a>.</li> 
  
  <li><span class="rrHelpSubheading">Image presentation</span> The images in <span class="redtext italicstext">v</span>HMML Reading Room have not been cropped. The ruler, white balance bar, and manuscript shelfmark will be visible in the vast majority of the digital images. Sometimes you will also see the implements used to hold down the pages during the photographic process. Seeing a smudged shelfmark strip or ruler positioned a bit askew is a reminder that many of these images were captured under difficult working conditions.</li>
  <li><span class="rrHelpSubheading">IIIF</span> If a record has a field for IIIF Manifest URL, including a logo <img src="${pageContext.request.contextPath}/static/img/iiif-logo.png" width="20" alt="blue and red triple I f logo"> for <a href="http://iiif.io/" target="_blank"> the IIIF (International Image Interoperability Framework)</a> , the manifest is available for copying into any IIIF compliant viewer that allows access to secure HTTPS connections. In other words, the IIIF logo on of <span class="redtext italicstext">v</span>HMML record indicates that the originating library has allowed the image to be viewable from other repositories, and is not limited to viewing solely in <span class="redtext italicstext">v</span>HMML Reading Room.</li> 
</ul>
<p>While viewing images of a document, use the controls in the lower right for zooming (magnifying) in and out, and for panning around
 the image after zooming. Note that the panning arrows have little or no effect unless the image has been magnified with zoom. The 
 little house button <img src="${pageContext.request.contextPath}/static/img/mirador_house.png" alt="Mirador house button" width="40px" /> returns the image to the default magnification after zooming.  </p>
 <img src="${pageContext.request.contextPath}/static/img/key_mirador_zoom_controls.png" alt="Mirador zooming and panning buttons explained" />

<p>While viewing images, the upper right controls enable switching between different views, rotating the image 90&#xb0;, enlarging the image to fill the browser screen, or closing out of the image view and returning to Reading Room Description and Search.</p>
<img src="${pageContext.request.contextPath}/static/img/mirador_view_options_key.png" alt="Mirador view options buttons explained" />

<h3 id="SIGNIN" class="rrHelp jumptarget-breadcrumbs">Having trouble signing in?</h3>
<ul class="withHeading">
  <li>Make sure that you are <a href="${pageContext.request.contextPath}/registration">registered</a>.</li> 
  <li>Try closing your browser (quit) and then restarting it.</li> 
  <li>Try to <a href="${pageContext.request.contextPath}/login">sign in</a> again.</li>
  <li>If you have forgotten your password, you can <a href="${pageContext.request.contextPath}/user/forgotPassword">reset it</a>.</li>
</ul>


	
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

<h3 class="rrHelp">Known issues</h3>
		<ul>
			<li>Note: <span class="redtext italicstext">v</span>HMML has <em>not</em>
				been optimized for smaller devices such as iPhones or Android
				phones.
			</li>
			<li>Older Mac OS X systems prior to version 10.9 may appear to run, but specific features such as Reading Room search and feedback corrections or additions may be inoperable.</li>
			<li>Because of the high-resolution of the images used in <span class="redtext italicstext">v</span>HMML, users with slow internet connections may experience a delay in image loading.</li>
			<li>The Full Screen button &nbsp;&nbsp;<span class="glyphicon glyphicon-resize-full"></span>&nbsp;&nbsp; in Mirador view does <i>not</i> work on iOS devices to maximize images. This is apparently an issue with the Mirador code utilized by <span class="redtext italicstext">v</span>HMML.</li>
		</ul>
<h3 class="rrHelp"> Still having problems? </h3>
<a href="${pageContext.request.contextPath}/contact">Please contact us </a>
<p>&nbsp;</p>
<button class="btn reading-room btn-sm" type="button" onclick="goBack()">
				<span aria-hidden="true" class="glyphicon glyphicon-arrow-left"></span>
				Go Back
</button>
<br />
<script>
	var genresHtml;
	var featuresHtml;
	
	function goBack() {
		window.open( document.referrer,"_self");
	}
	
	function openGenresDialog() {
		new Dialog({
			title: 'Genre/Form Complete List',
			showCloseIcon: true,
			body: genresHtml,
			closeButtonLabel: 'Close',
			closeButtonCssClass: 'reading-room',
		}).show();		
	}
	
	function showGenres() {
		
		if(!genresHtml) {
			$.ajax({
				url: contextPath + '/readingRoom/options/genres',
				success: function(options) {
					genresHtml = '<ul>';
					
					for(var i = 0; i < options.length; i++) {
						genresHtml += '<li>' + options[i].name + '</li>';	
					}
					
					genresHtml += '</ul>';
					openGenresDialog();
				}
			});
		} else {
			openGenresDialog();
		}	
	}
	
	function openFeaturesDialog() {
		new Dialog({
			title: 'Features Terms List',
			showCloseIcon: true,
			body: featuresHtml,
			closeButtonLabel: 'Close',
			closeButtonCssClass: 'reading-room',
		}).show();		
	}
	
	function showFeatures() {
		
		if(!featuresHtml) {
			$.ajax({
				url: contextPath + '/readingRoom/options/features',
				success: function(options) {
					featuresHtml = '<ul>';
					
					for(var i = 0; i < options.length; i++) {
						featuresHtml += '<li>' + options[i].name + '</li>';	
					}
					
					featuresHtml += '</ul>';
					openFeaturesDialog();
				}
			});
		} else {
			openFeaturesDialog();
		}
	}		
</script>
<noscript>
	<p>&nbsp;Use the back button on your browser to return to the
		previous page</p>
</noscript>

<br />		
<p>&nbsp;</p>

		