<div class="row">
	<div class="col-lg-12">
		<h2>
			Gen. Help
		</h2>
		<h3>
			This software is optimized for the
			following browsers and platforms
		</h3>

		<ul>
			<li>Chrome running on both Windows and Mac OS</li>

			<li>Firefox running on both Windows and Mac OS</li>

			<%-- <li>Internet Explorer 11 running on Windows</li>--%>

			<li>Safari running on Mac OS X 10.9 (Mavericks) and later, and iOS 9 (iPad mini and larger
				screens)</li>
		</ul>
		<h3>Technical: Minimum system requirements</h3>
		<ul>
			<li>Screen size of at least iPad mini: 200 x 134.7 <abbr title="millimeters">mm</abbr> (7.87 x 5.30 <abbr title="inches">in</abbr>)</li>
			<li>Up-to-date browser (Google Chrome, Mozilla Firefox, Safari, or equivalent) with JavaScript enabled</li>
			<li>If running a Macintosh, OS X 10.9 or later</li>
		</ul>
		<h3 class="rrHelp">Known issues</h3>
		<ul>
			<li>Note: this software has <em>not</em>
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
		<br />
		<h3>Need more help?</h3>
		<p>
			Please feel free to <a
				href="${pageContext.request.contextPath}/contact">contact us</a>
			with suggestions or improvements.
		</p>
		<p>&nbsp;</p>
		<button class="btn home btn-sm" type="button" type="button" onclick="goBack()"><span aria-hidden="true" class="glyphicon glyphicon-arrow-left"></span>
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