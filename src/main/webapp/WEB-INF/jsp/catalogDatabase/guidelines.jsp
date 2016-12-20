<div class="row">
	<div class="col-lg-12">
		<h2>
			<span class="redtext italicstext">v</span>HMML Cataloger Information: Guidelines and Metadata Dictionaries
		</h2>
		<small>This page offers the following topics: <br />
				<a href="#AUTHORITIES">Authority Lists </a>&nbsp;|&nbsp;
				<a href="#TECHNICAL">System Requirements</a>&nbsp;|&nbsp;
				<a href="#BESTPRACTICES">Best Practices</a>&nbsp;|&nbsp;
				<a href="#NOTLEVEL1">Level II and III catalogers</a>&nbsp;|&nbsp;
				<a href="#LEVEL1">Level I catalogers</a>&nbsp;|&nbsp;
				<a href="#RECOMMENDATIONS"> Recommendations</a>&nbsp;|&nbsp;
				<a href="#DICTIONARIESAIDS">Dictionaries and Aids</a>&nbsp;|&nbsp;
				<a href="#KEEPINMIND"> Keep in Mind</a>
		</small>
		<hr />
		<h3 id="AUTHORITIES">Authority Lists - Google docs</h3>
		<ul class="unorderedListNoBullets">
		  <li><a href="https://docs.google.com/spreadsheets/d/1cQtv_FsVvjgzZ0Tf6FFHE6UG2iq7TjMCgdlzLjxi174/edit?usp=sharing" target="_blank"><span class="glyphicon glyphicon-new-window"></span> Uniform name and display form and authorities</a></li>
		  <li><a href="https://docs.google.com/spreadsheets/d/1eOdkN6coJHtD5BBI31Wc6H0gxk1gtQSmWCV_8V-_Qaw/edit?usp=sharing" target="_blank"><span class="glyphicon glyphicon-new-window"></span> Eastern Christian liturgical title and authorities</a></li>
		  <li><a href="https://docs.google.com/spreadsheets/d/1uM6e5_KVV1nyXII_SY11q0icdq-Ug9zx3xyYXh2riIU/edit?usp=sharing" target="_blank"><span class="glyphicon glyphicon-new-window"></span> Uniform titles and authorities</a></li>
		  <li><a href="https://docs.google.com/spreadsheets/d/1DVbBlItUdDq2fvSt0ID8V2PbTm_GluGTpkWEVq6pNDo/edit?usp=sharing" target="_blank"><span class="glyphicon glyphicon-new-window"></span> Genre/Form authorities</a></li>
		  <li><a href="https://docs.google.com/spreadsheets/d/1FZbPVbFKq_omXklMbJwxC9_85aarcJYFkt5A_FLUayU/edit?usp=sharing" target="_blank"><span class="glyphicon glyphicon-new-window"></span> Features</a></li>
		</ul>
		<h3 id="TECHNICAL">Technical: Minimum system requirements</h3>
		<ul>
			<li>Screen size of at least iPad mini: 200 x 134.7 <abbr title="millimeters">mm</abbr> (7.87 x 5.30 <abbr title="inches">in</abbr>)</li>
			<li>Up-to-date browser (Google Chrome, Mozilla Firefox, Safari, or equivalent) with JavaScript enabled--note that we recommend Google Chrome for cataloging</li>
			<li>If running a Macintosh, OS X 10.9 or later</li>
		</ul>
		<h3 id="BESTPRACTICES">Best Practices</h3>
		<ul>
			<li>Before adding or editing catalog records, update your browser and clear your browser cache </li>
			<li>We recommend saving your data in a separate local application (e.g. Excel) and then copying and pasting into <span class="redtext italicstext">v</span>HMML rather than typing new records directly into the online form. Otherwise, if network conductivity is interrupted your record may not be saved.</li>
		</ul>
		<p>
			The catalog database has three levels of users. Your user access is
			set by <a href="http://www.hmml.org/" target="_blank">HMML</a>. A
			list of user role functions can be found here.
		</p>
		<h3 id="NOTLEVEL1">Level II and III catalogers</h3>
		Level II and III catalogers are limited to specific records for
		editing.

		<p>For example, no authority records can be created or edited (LC
			or VIAF).</p>

		<p>For Manuscript Objects, the following fields are not editable
			by level II or III catalogers
		<ul>
			<li>Subject</li>
			<li>Genre/Form and associated URI field</li>
			<li>Uniform Title plus LC and VIAF fields</li>
			<li>Author Display</li>
		</ul>
		</p>
		<div class="row">
			<div class="col-lg-12">
				<p class="small">
					<strong><i> Tip:</i> To edit a record, first search and
						retrieve it using the search panel on the left of the cataloging
						page. </strong>
				</p>
				<h3 id="LEVEL1">Level I catalogers</h3>
				<ul>
					<li>Catalogers with level I privileges are able to edit and delete any record. </li>
					<li>Remember: once a display name has been entered for someone (e.g. author), it can be changed but it can't be removed/erased </li>
				</ul>
				<p class="small">
					<strong><i> Tip:</i> Images are linked to the project
						number.</strong>
				</p>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12">
				<h3 id="RECOMMENDATIONS">Recommendations</h3>
				<ul>
					<li>Use Google Chrome browser for cataloging rather than Mozilla Firefox, Internet Explorer 11, or the Microsoft Edge browser.</li>
					
				</ul>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12">
				<h3 id="DICTIONARIESAIDS">Metadata Dictionaries and Aids</h3>
				<b>Manuscripts and printed books</b> <a href="http://www.hmml.org/" target="_blank">HMML</a> has prepared a manual for cataloging manuscript and printed 
				material, whether bound separately or together in the same codex.  
				<p><b>Archival materials</b> HMML distinguishes between manuscripts and archival material. Archival material has a distinct set of rules for cataloging. 
				<a href="${pageContext.request.contextPath}/catalogDatabase/contact" class="catalogerContact">Contact the curator</a> of the collection to determine which type of 
				catalog record you should use.</p>  <%-- A complete description of the catalog terms and use can be found here.--%>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12"> 
			<h3 id="KEEPINMIND">Keep in Mind the Following</h3>
				<ul>
					<li>Permanent link URLs only will work in the PRODUCTION environment, not on DEVELOPMENT nor on TEST.</li>
					<li>A catalog manuscript part may have no items, 1 item, or more than 1 item. A printed object can have no more than 1 item.</li>
					<li>If a rich text cataloging CKeditor field stops responding to mouse or keyboard input, save and close the record. Then open it again for editing.</li>
					<li>Records imported with century information usually display correctly in RR multi-search results. However, if a record already has an imported century and then a part is added or edited, the original imported century data MUST be manually entered or else the century(ies) will not display in Reading Room.</li>
					<li>When a record is created as type Manuscript and/or Print, it cannot later be changed to type Archival  Material. The only recourse in such a situation would be to create a new Archival record and then copy and paste the data from the old record before deleting it. </li>
					<li>Collections names are not stored as a separate field, but rather are the initial part of the HMML Project Number field.</li> 
					<li> HMML project numbers should be chosen so as to not be on the list of stop words. Otherwise finding the project number will only be available in upper case. e.g. CET rather than cet. </li>
					<li> After adding a part to a manuscript record, the Add an item button appears to be duplicated. Either button will work equally well. The reason there are two buttons is so that later on there will be one on the top and one on the bottom of the part fields for ease of access. <img src="${pageContext.request.contextPath}/static/img/add_part_double_buttons.png" alt="two green Add item buttons offset stacked"/><br /></li>
					<li> Press and hold the <kbd>Ctrl</kbd> button (Command on Macintosh) while clicking with the mouse to select more than one Support. </li>
					<li>When cataloging Folio entries, if more than one language is equally represented, please separate  these languages with the semicolon rather than a comma, e.g. Latin; Spanish</ul>
			</div>
		</div>
		<p>&nbsp;</p>
		<button class="btn catalog-database btn-sm" type="button" onclick="goBack()">
			<span aria-hidden="true" class="glyphicon glyphicon-arrow-left"></span>
			Go Back
		</button>
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
	</div>
</div>