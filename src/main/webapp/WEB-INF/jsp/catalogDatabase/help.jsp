<h2><span class="redtext italicstext">v</span>HMML Catalog Database Help </h2>
	<p class="small">
					<strong><i> Tip:</i> To edit a record, first search and
						retrieve it using the search panel on the left of the cataloging
						page. </strong>
	</p>
<p class="small">
					<strong><i> Remember:</i> Images are linked to the project
						number.</strong>
				</p>
	<p> There is no way to delete every listed instance of a field (e.g. there will always be at least one Subject field and its associated LC and VIAF URL fields). If the data is erroneous, and you want to get rid of it, delete the data using <kbd>Backspace</kbd> or <kbd>Delete</kbd>.</p>
<h3>Cataloging tips</h3>
<ul>
	<li>You may choose more than one item in the multi-select drop-down lists, indicated by (s) at the end of the label. For example, Language(s).</li>		
	<li>Check the Provisional box if the date or place of origin is less than certain. Otherwise leave it unchecked.</li>	
	<%-- <li>To select more than one item or to deselect in a Search multi-select drop-down list, in Windows: press <kbd>Ctrl</kbd> and mouse click; Mac OS X: press <kbd>Command</kbd> and mouse click.</li>--%>
	<li>When cataloging archival material, make sure that the date range is populated at the top object level. Otherwise users who utilize the date range slider in the search panel won't be able to find that object.</li>
	<li> HMML Project Numbers should <i>never</i> have underscores in them--otherwise images will not show up correctly in Mirador. Spaces are acceptable in HMML Project numbers. </li>
	<li> Utilize centimeters for dimensions, with decimal places if necessary. Do <i>not</i> use millimeters.</li>
	<li> If the Access Restrictions is set to <i>On-site only</i>, then choosing thumbnails is irrelevant as the images will not display. </li>
	<li> Note that there are issues with some browsers working better than others. Currently we recommend using Google Chrome. </li>
</ul>	
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
			<li>Because of the high-resolution of the images used in <span
				class="redtext italicstext">v</span>HMML, users with slow internet
			connections may experience a delay in image loading.</li>
			<li> If your browser scrolling freezes while using the image zoom on a tablet or other mobile device, just refresh the page to engage the scrolling again.
		</ul>
		<h3>A special note about Syriac</h3>
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

<p>&nbsp;</p>
<a href="${pageContext.request.contextPath}/contact"> Contact us </a>
<p>&nbsp;</p>
<button class="btn catalog-database btn-sm" type="button" onclick="goBack()"><span aria-hidden="true" class="glyphicon glyphicon-arrow-left"></span>
			Go Back</button>
		<br />
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
		