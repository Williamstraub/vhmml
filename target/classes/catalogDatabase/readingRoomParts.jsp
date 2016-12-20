<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="vhmmlfn" uri="VhmmlFunctions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<h3 class="objectParts">
	<div class="catalog-database-icon formSectionToggle"><span class="glyphicon glyphicon-triangle-right hidden"></span> Object Parts</div>
	<div class="btn-group">
		<button type="button" class="btn btn-success dropdown-toggle addPart add" data-toggle="dropdown" aria-expanded="false">Add <span class="caret"></span></button>
		<ul class="dropdown-menu" role="menu">
			<li data-part-type="MANUSCRIPT" class="MANUSCRIPT MANUSCRIPT_PRINT"><a>Manuscript Part</a></li>
			<li data-part-type="PRINTED" class="PRINT MANUSCRIPT_PRINT"><a>Printed Part</a></li>
			<li data-part-type="ARCHIVAL_OBJECT" class="ARCHIVAL_OBJECT"><a>Archival Material</a></li>
		</ul>			
	</div>
</h3>	
	
<div class="formSection objectParts">
	<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.parts)}" varStatus="loopStatus">
		<c:set var="part" value="${readingRoomObjectForm.parts[loopStatus.index]}"/>
		<c:set var="partType" value="UNKNOWN"/>
		<%-- if there are no parts, we still render one set of part fields so the user can add one, the type defaults to unknown --%>
		<c:if test="${part != null}">
			<c:set var="partType" value="${part.type}"/>
		</c:if>			
								
		<div class="objectPart ${partType} contentItemContainer" data-part-index="${loopStatus.index}" data-part-id="${part.id}">
			<form:hidden path="parts[${loopStatus.index}].hasContent" cssClass="hasContent" data-list-name="parts"/>
			<h3>
				<div href="#" class="catalog-database-icon formSectionToggle">
					<span class="glyphicon glyphicon-triangle-bottom"></span><span class="partLabel"> Part ${readingRoomObjectForm.parts[loopStatus.index].partNumber} Description</span>
					<button type="button" class="btn btn-danger delete">Delete Part and Items</button>
				</div>
			</h3>
			<div class="formSection">
				<vhmml:input hidden="true" fieldName="readingRoomObjectId" listName="parts" listIndex="${loopStatus.index}"/>					
				<vhmml:input label="Part Number" fieldName="partNumber" listName="parts" listIndex="${loopStatus.index}"  helpText="Sequential numbers assigned to distinct texts within an object.  Use only Arabic numerals." helpIconClass="catalog-database-icon" cssClass="partNumber digitsOnly"/>
				<vhmml:select label="Type" options="${partTypes}" fieldName="type" listName="parts" listIndex="${loopStatus.index}" optionLabelProperty="name" cssClass="partType" />					
				<vhmml:input fieldName="partLocation" label="Part Location" listName="parts" listIndex="${loopStatus.index}" helpText="The position of a part within a codex expressed in folios or pages. Example: fol. 1r-123v. Example: pages 3-122." helpIconClass="catalog-database-icon"/>
				<vhmml:input fieldName="countryOfOrigin" listName="parts" listIndex="${loopStatus.index}" label="Country or Place of Origin" printedLabel="Country or Place of Printing"/>
				<vhmml:checkbox fieldName="countryOfOriginUncertain" listName="parts" listIndex="${loopStatus.index}" label="Country or Place of Origin Provisional" printedLabel="Country or Place of Printing Provisional"/>
	<%-- 					<vhmml:input fieldName="cardinalOfOrigin" listName="parts" listIndex="${loopStatus.index}" label="Cardinal of Origin" printedLabel="Cardinal of Printing"/>
						<vhmml:input fieldName="regionOfOrigin" listName="parts" listIndex="${loopStatus.index}" label="Region of Origin" printedLabel="Region of Printing"/> --%>
				<vhmml:input fieldName="cardinalOfOrigin" listName="parts" listIndex="${loopStatus.index}" label="Cardinal of Origin" cssClass="hideForPrinted"/>
				<vhmml:input fieldName="regionOfOrigin" listName="parts" listIndex="${loopStatus.index}" label="Region of Origin" cssClass="hideForPrinted"/>
				<vhmml:input fieldName="cityOfOrigin" listName="parts" listIndex="${loopStatus.index}" label="City of Origin" printedLabel="City of Printing"/>
				<vhmml:checkbox fieldName="cityOfOriginUncertain" listName="parts" listIndex="${loopStatus.index}" label="City of Origin Provisional" printedLabel="City of Printing Provisional"/>					
				<vhmml:input label="Edition Statement" fieldName="editionStatement" listName="parts" listIndex="${loopStatus.index}" helpText="Used to distinguish between copies of the same title, issued by the same printer or publisher, with differing content, or with a different type setting. Different editions may be numbered (e.g., 3th edition), named (Revised edition, Updated), or implied (with different pagination)." helpIconClass="catalog-database-icon" cssClass="hideForManuscript"/>
				<vhmml:input label="Printing Statement (Transcribe)" fieldName="printingStatement" listName="parts" listIndex="${loopStatus.index}" textarea="true" helpText="Records the place of printing/publication, the name of the person or organization responsible for printing/publication, and the year (including month and day, if available) in which the book was issued transcribed as it appears on the title page and/or in the colophon of a book." helpIconClass="catalog-database-icon" cssClass="hideForManuscript"/>
				<vhmml:input label="Signatures Statement" fieldName="formatStatement" listName="parts" listIndex="${loopStatus.index}" textarea="true" helpText="The format refers to the relationship of the page to the original sheet - folio (2o), quarto (4to), octavo (8vo), duodecimo (12mo), etc. -i.e., how many pages were printed on each side of a sheet, which was then folded into a gathering; the signature statement records the physical structure of the book according to the formation, number, and naming of its gatherings, based on the signatures printed at the bottom of certain pages." helpIconClass="catalog-database-icon" cssClass="hideForManuscript ckEditor"/>
				
				<vhmml:date label="Date Precise" fieldName="datePrecise" listName="parts" listIndex="${loopStatus.index}" helpText="Exact date information found within an object. If the year is not known, leave the Year field blank." helpIconClass="catalog-database-icon" importedValue="ymdDateImported"/>					
				
				<div class="form-group">						
					<label class="col-sm-3 control-label">Year Range</label>
					<div class="col-sm-9">
						<label for="parts[${loopStatus.index}].beginDate" class="inlineLabel">Begin</label>							
						<form:input path="parts[${loopStatus.index}].beginDate" cssClass="form-control dateYear integersOnly" maxlength="5" data-list-name="parts"/>
						
						<label for="parts[${loopStatus.index}].endDate" class="inlineLabel">End</label>							
						<form:input path="parts[${loopStatus.index}].endDate" class="form-control dateYear integersOnly" maxlength="5" data-list-name="parts"/>
						
						<i class="glyphicon glyphicon-question-sign catalog-database-icon" data-toggle="tooltip" title="For objects with a single exact year, enter the same year in both Begin and End fields 
						(Begin: 1475 End: 1475). For objects with known or estimated range of years, use the earliest and last year (Begin: 1525 End: 1625). For objects with a known or estimated century or range of centuries, but not a specific year, use the beginning and end of the century range 
						(Example to represent 15th-16th century use: Begin: 1400 End: 1600)." data-placement="right" data-trigger="click hover"></i>							
					</div>
				</div>
							
				<vhmml:select label="Date Century(ies)" fieldName="centuries" listName="parts" listIndex="${loopStatus.index}" options="${centuries}" optionValueProperty="key" optionLabelProperty="value" 
					multiSelect="true" cssClass="groupbox" helpText="You can multi-select centuries for manuscripts with broader date ranges. If you select a Date Century you must also select a Year Range."  helpIconClass="catalog-database-icon" importedValue="centuryImported"/>
				<vhmml:checkbox label="Century Provisional" fieldName="centuryUncertain" listName="parts" listIndex="${loopStatus.index}" helpText="Leave unchecked if the century date is known/verified." helpIconClass="catalog-database-icon"/>
				<vhmml:input label="Native Date" fieldName="nativeDatePrecise" listName="parts" listIndex="${loopStatus.index}"/>
				<vhmml:select label="Support(s)" fieldName="supports" listName="parts" listIndex="${loopStatus.index}" options="${supports}" optionLabelProperty="name" multiSelect="true" importedValue="supportImported"/>
				
				<vhmml:dimension-input label="Support Dimensions" fieldName="supportDimensions" listName="parts" listIndex="${loopStatus.index}" helpText="The outer dimensions of the support measured in centimeters. Round to next closest tenth of a centimeter. Example: 32.2 x 25.7 cm." helpIconClass="catalog-database-icon" cssClass="positiveDecimalsOnly" importedValue="supportDimensionsImported"/>										
				<vhmml:input label="Medium" fieldName="medium" listName="parts" listIndex="${loopStatus.index}" cssClass="hideForPrinted"/>
				<vhmml:input label="Page Layout" fieldName="layout" listName="parts" listIndex="${loopStatus.index}" textarea="true" helpIconClass="catalog-database-icon" helpText="The layout of a page; the relationship of written space to the ruling pattern and to the blank space on the page; the number of columns of text; and the arrangement of glosses, commentaries, initials, and decoration in relation to the main text."/>					
				<vhmml:dimension-input label="Writing Space" fieldName="writingSpace" listName="parts" listIndex="${loopStatus.index}" helpText="The measured size of the area on the support used to contain the text in centimeters. If double column layout, describe layout style in Page Layout. Round up height and width to next closest tenth of a centimeter. Example: 33.3 x 24.5 cm." helpIconClass="catalog-database-icon" cssClass="hideForPrinted positiveDecimalsOnly" importedValue="writingSpaceImported"/>
				<vhmml:checkbox label="Catchwords" fieldName="catchwords" listName="parts" listIndex="${loopStatus.index}"/>
				<vhmml:checkbox label="Signatures" fieldName="signatures" listName="parts" listIndex="${loopStatus.index}"/>
				<vhmml:select label="Font(s)" fieldName="fonts" listName="parts" listIndex="${loopStatus.index}" options="${fonts}" optionLabelProperty="name" optionValueProperty="name" multiSelect="true" cssClass="hideForManuscript" formGroupClass="hideForManuscript"/>										
				
				<div class="hideForPrinted">
					<div id="parts[${loopStatus.index}].scribes" class="repeatableField">
						<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.parts[loopStatus.index].scribes)}" varStatus="scribesLoop">
							<div class="repeatableFieldGroup groupedFields authorityField">
								<vhmml:input label="Scribe" fieldName="contributor.name" listName="parts[${loopStatus.index}].scribes" listIndex="${scribesLoop.index}" repeatable="true" cssClass="hideForPrinted contributor"/>
								<%-- note that nameNs is an attribute of the partContributor relationship (not the contributor)  because the same author could have a native script name in Greek on one content item but Arabic on another, etc. --%>
								<vhmml:input label="Scribe Native Script" fieldName="nameNs" listName="parts[${loopStatus.index}].scribes" listIndex="${scribesLoop.index}" cssClass="hideForPrinted nameNs"/>
								<%--Hide LC and VIAF URL text fields since rarely used 
								<vhmml:input label="Scribe LC URL" fieldName="contributor.authorityUriLC" listName="parts[${loopStatus.index}].scribes" listIndex="${scribesLoop.index}" cssClass="hideForPrinted lcUri" disabled="true"/>
								<vhmml:input label="Scribe VIAF URL" fieldName="contributor.authorityUriVIAF" listName="parts[${loopStatus.index}].scribes" listIndex="${scribesLoop.index}" cssClass="hideForPrinted viafUri" disabled="true"/>
								--%>
							</div>
						</c:forEach>
					</div>
										
					<vhmml:select label="Writing System(s)" cssClass="groupbox" fieldName="writingSystems" listName="parts" listIndex="${loopStatus.index}" options="${writingSystems}" optionLabelProperty="name" optionValueProperty="name" multiSelect="true" addButtonType="WRITING_SYSTEM" addButtonLabel="Writing System" importedValue="writingSystemImported"/>						
					<vhmml:select label="Script(s)" cssClass="groupbox" fieldName="scripts" listName="parts" listIndex="${loopStatus.index}" options="${scripts}" optionLabelProperty="name" optionValueProperty="name" multiSelect="true" addButtonType="SCRIPT" addButtonLabel="Script" importedValue="scriptImported"/>
				</div>
					
				<div id="parts[${loopStatus.index}].artists" class="repeatableField">
					<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.parts[loopStatus.index].artists)}" varStatus="artistsLoop">
						<div class="repeatableFieldGroup authorityField">							
							<vhmml:input label="Artist" fieldName="contributor.name" listName="parts[${loopStatus.index}].artists" listIndex="${artistsLoop.index}" repeatable="true" cssClass="contributor"/>
								<%--Hide LC and VIAF URL text fields since rarely used
								<vhmml:input label="Artist LC URL" fieldName="contributor.authorityUriLC" listName="parts[${loopStatus.index}].artists" listIndex="${artistsLoop.index}" cssClass="lcUri" disabled="true"/>
								<vhmml:input label="Artist VIAF URL" fieldName="contributor.authorityUriVIAF" listName="parts[${loopStatus.index}].artists" listIndex="${artistsLoop.index}" cssClass="viafUri" disabled="true"/>
								--%>
						</div>
					</c:forEach>
				</div>
				
				<div id="parts[${loopStatus.index}].associatedNames" class="repeatableField">	
					<c:set var="associatedNameCount" value="0"/>
					<c:set var="associatedNames" value="${readingRoomObjectForm.parts[loopStatus.index].associatedNames}"/>
					<c:if test="${associatedNames != null && associatedNames.size() > 0}">
						<c:set var="associatedNameCount" value="${associatedNames.size() - 1}"/>
					</c:if>
					<c:forEach begin="0" end="${associatedNameCount}" varStatus="associatedNameLoop">
						<div class="repeatableFieldGroup groupedFields authorityField">
							<vhmml:input-select-combo 
								label="Associated Name"  
								listName="parts[${loopStatus.index}].associatedNames" 
								listIndex="${associatedNameLoop.index}" 
								options="${associatedNameTypes}" 
								optionLabelProperty="displayName"
								countFieldName="contributor.name"
								countFieldCssClass="contributor"
								typeFieldName="type"
								repeatable="true" />
							<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER">
								<vhmml:input label="Associated Name Display" fieldName="contributor.displayName" listName="parts[${loopStatus.index}].associatedNames" listIndex="${associatedNameLoop.index}" cssClass="displayName"/>
							</security:authorize>
							<vhmml:input label="Associated Name LC URL" fieldName="contributor.authorityUriLC" listName="parts[${loopStatus.index}].associatedNames" listIndex="${associatedNameLoop.index}" cssClass="lcUri" disabled="true"/>
							<vhmml:input label="Associated Name VIAF URL" fieldName="contributor.authorityUriVIAF" listName="parts[${loopStatus.index}].associatedNames" listIndex="${associatedNameLoop.index}" cssClass="viafUri" disabled="true"/>
						</div>										
					</c:forEach>
				</div>
							
				<vhmml:input fieldName="decoration" listName="parts" listIndex="${loopStatus.index}" label="Decoration/Illustration" textarea="true" cssClass="ckEditor"/>					
				<vhmml:input fieldName="colophonPart" listName="parts" listIndex="${loopStatus.index}" label="Colophon" textarea="true"/>	
				
				<vhmml:input fieldName="partNotes" listName="parts" listIndex="${loopStatus.index}" label="Notes" textarea="true" cssClass="ckEditor"/>
				
				<h3 class="partContents contentHeading">
					<div href="#" class="catalog-database-icon formSectionToggle">
						<span class="glyphicon glyphicon-triangle-right hidden"></span>
						<span class="partItemsLabel"> Part ${loopStatus.index + 1} Items</span>
					</div>
					<button type="button" class="btn btn-success addContentItem add">Add Item</button>
				</h3>					
				
				<div class="formSection partContents contentSection">									
					<c:set var="contentCount" value="0"/>	
					<c:set var="part" value="${readingRoomObjectForm.parts[loopStatus.index]}"/>					
						
					<c:if test="${part.contents != null && part.contents.size() > 0}">
						<c:set var="contentCount" value="${part.contents.size() - 1}"/>
					</c:if>
					<c:forEach begin="0" end="${contentCount}" varStatus="contentLoopStatus">
						<c:set var="contentItem" value="${part.contents[contentLoopStatus.index]}"/>							
						<div class="contentItem" data-item-id="${contentItem.id}">
							<h3>
								<div href="#" class="catalog-database-icon formSectionToggle">
									<span class="contentLabel"> Item ${contentItem.itemNumber} Description</span>
									<button type="button" class="btn btn-danger delete">Delete Item</button>
								</div>
							</h3>
							<vhmml:input hidden="true" fieldName="readingRoomObjectPartId" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}"/>					
							<vhmml:input label="Item Number" fieldName="itemNumber" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" cssClass="itemNumber digitsOnly" helpText="Sequential numbers assigned to distinct texts within a part.  Use only Arabic numerals." helpIconClass="catalog-database-icon" />																
							<vhmml:input label="Item Location" fieldName="itemLocation" textarea="true" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" 
							helpText="The position of an item within a part expressed in folios or pages. Example: fol. 1r-125v.  Example: pages 1-126." helpIconClass="catalog-database-icon"/>
							
							<div id="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].authors" class="repeatableField">	
								<c:set var="authorCount" value="0"/>
								<c:set var="authors" value="${readingRoomObjectForm.parts[loopStatus.index].contents[contentLoopStatus.index].authors}"/>
								<c:if test="${authors != null && authors.size() > 0}">
									<c:set var="authorCount" value="${authors.size() - 1}"/>
								</c:if>
								<c:forEach begin="0" end="${authorCount}" varStatus="authorsLoop">
									<div class="repeatableFieldGroup groupedFields authorityField">							
										<vhmml:input label="Author" fieldName="contributor.name" listName="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].authors" listIndex="${authorsLoop.index}" repeatable="true" cssClass="contributor"/>
										<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER">											
											<vhmml:input label="Author Display" printedLabel="Author Name Display" fieldName="contributor.displayName" listName="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].authors" listIndex="${authorsLoop.index}" cssClass="displayName"/>
										</security:authorize>
										<vhmml:input label="Author LC URL" fieldName="contributor.authorityUriLC" listName="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].authors" listIndex="${authorsLoop.index}" cssClass="lcUri" disabled="true"/>
										<vhmml:input label="Author VIAF URL" fieldName="contributor.authorityUriVIAF" listName="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].authors" listIndex="${authorsLoop.index}" cssClass="viafUri" disabled="true"/>
										<%-- note that nameNs is an attribute of the content contributor relationship (not the contributor) because the same author could have a native script name in Greek on one content item but Arabic on another, etc. --%>
										<vhmml:input label="Author Native Script" fieldName="nameNs" listName="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].authors" listIndex="${authorsLoop.index}" cssClass="nameNs"/>
									</div>										
								</c:forEach>
							</div>
							
							<vhmml:input label="Title" fieldName="provisionalTitle" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" textarea="true"/>								
							<vhmml:input label="Title Native Script" fieldName="titleNs" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}"/>
							<div class="groupedFields">
								<vhmml:input label="Uniform Title" fieldName="uniformTitle.name" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}"/>								
								<vhmml:input label="Uniform Title LC URL" fieldName="uniformTitle.authorityUriLC" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" cssClass="lcUri" disabled="true"/>
								<vhmml:input label="Uniform Title VIAF URL" fieldName="uniformTitle.authorityUriVIAF" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" cssClass="viafUri" disabled="true"/>
							</div>
							<c:set var="altTitleCount" value="0"/>
							<c:set var="altTitles" value="${readingRoomObjectForm.parts[loopStatus.index].contents[contentLoopStatus.index].alternateTitles}"/>
							<c:if test="${altTitles != null && altTitles.size() > 0}">
								<c:set var="altTitleCount" value="${altTitles.size() - 1}"/>
							</c:if>
							
							<div id="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].alternateTitles" class="repeatableField">									
								<c:forEach begin="0" end="${altTitleCount}" varStatus="altTitleLoop">
									<div class="repeatableFieldGroup">														
										<vhmml:input label="Alternate Title" listName="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].alternateTitles" listIndex="${altTitleLoop.index}" repeatable="true"/>
									</div>
								</c:forEach>									
							</div>
							
							<%-- <vhmml:input label="Running Title" fieldName="runningTitle" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" cssClass="hideForManuscript"/> --%>								
							<vhmml:input label="Pagination/Foliation Statement" fieldName="paginationStatement" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" textarea="true" helpText="The number of pages (pagination) or leaves (foliation) of a volume, given according to how this extent is recorded on the piece." helpIconClass="catalog-database-icon" cssClass="hideForManuscript"/>																
							
							<%-- TODO: move the associated name logic into a tag to remove duplication since it's in different locations based on the part type --%>
							<div id="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].associatedNames" class="repeatableField hideForPrinted">	
								<c:set var="associatedNameCount" value="0"/>
								<c:set var="associatedNames" value="${readingRoomObjectForm.parts[loopStatus.index].contents[contentLoopStatus.index].associatedNames}"/>
								<c:if test="${associatedNames != null && associatedNames.size() > 0}">
									<c:set var="associatedNameCount" value="${associatedNames.size() - 1}"/>
								</c:if>
								<c:forEach begin="0" end="${associatedNameCount}" varStatus="associatedNameLoop">
									<div class="repeatableFieldGroup groupedFields authorityField">
										<vhmml:input-select-combo 
											label="Associated Name"  
											listName="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].associatedNames" 
											listIndex="${associatedNameLoop.index}" 
											options="${associatedNameTypes}" 
											optionLabelProperty="displayName" 
											cssClass="hideForPrinted"
											countFieldName="contributor.name"
											countFieldCssClass="hideForPrinted contributor"
											typeFieldName="type"
											repeatable="true" />
										<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER">
											<vhmml:input label="Associated Name Display" fieldName="contributor.displayName" listName="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].associatedNames" listIndex="${associatedNameLoop.index}" cssClass="hideForPrinted displayName"/>
										</security:authorize>
										<vhmml:input label="Associated Name LC URL" fieldName="contributor.authorityUriLC" listName="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].associatedNames" listIndex="${associatedNameLoop.index}" cssClass="hideForPrinted lcUri" disabled="true"/>
										<vhmml:input label="Associated Name VIAF URL" fieldName="contributor.authorityUriVIAF" listName="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].associatedNames" listIndex="${associatedNameLoop.index}" cssClass="hideForPrinted viafUri" disabled="true"/>
									</div>										
								</c:forEach>
							</div>						
							
							<vhmml:select label="Language(s)" fieldName="languageIds" cssClass="groupbox" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" options="${languages}" optionLabelProperty="name" optionValueProperty="id" multiSelect="true" />								
							<vhmml:input label="Incipit" fieldName="incipit" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" textarea="true" />								
							<vhmml:input label="Explicit" fieldName="explicit" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" textarea="true" />
							<vhmml:input label="Rubric" fieldName="rubric" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}"  helpText="A heading or instruction in a manuscript that is not part of the main text; normally in red ink." helpIconClass="catalog-database-icon" textarea="true" cssClass="hideForPrinted"/>
																							
							<vhmml:input label="Contents Detail" fieldName="contentsDetail" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" textarea="true" 
								helpText="For printed books, a description of titles and authors not included in the main title preceded by the page or folio location. For manuscripts, a description of the contents preceded by location. Do not create an item for each Psalm in a Psalter or part of the mass in a Missal. Example: fol. 1r-2v, Psalm 1; fol. 3r-4r, Psalm 2; fol. 4r-5v, Psalm 3." helpIconClass="catalog-database-icon" cssClass="ckEditor"/>								
														
							<c:set var="disabled" value="false"/>
							<c:set var="itemBibClass" value="ckEditor"/>
							
							<security:authorize ifNotGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER">
								<c:set var="disabled" value="true"/>
								<form:hidden path="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].catalogerTags"/>
								<form:hidden path="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].itemCondition"/>
								<form:hidden path="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].textBibliography"/>
								<c:set var="itemBibClass" value=""/>
							</security:authorize>
							
							<vhmml:input label="Cataloger Tags" fieldName="catalogerTags" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" textarea="true" deprecated="true" 
								helpText="Deprecated field--do not use. Formerly held subject tags by catalogers. Data needs to be moved to new Subject or Genre/Form fields in Object Data." helpIconClass="catalog-database-icon" disabled="${disabled}"/>														
							
							<vhmml:input label="Item Condition" printedLabel="Condition" fieldName="itemCondition" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" deprecated="true"
								helpText="Deprecated field--do not use. Condition data regarding the item. Data needs to be moved to Condition Notes in Object Physical Description, noting location in object." 
								helpIconClass="catalog-database-icon" textarea="true" disabled="${disabled}"/>
							
							<vhmml:input label="Item Notes" printedLabel="Notes" fieldName="itemNotes" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" 
								textarea="true" cssClass="ckEditor" />
							
							<vhmml:input label="Item Bibliography" printedLabel="Bibliography" fieldName="textBibliography" listName="parts[${loopStatus.index}].contents" listIndex="${contentLoopStatus.index}" 
								textarea="true" cssClass="${itemBibClass}" deprecated="true" disabled="${disabled}"/>
						</div>
						<hr/>												
						
						<!--  content images data can't be edited at the moment -->
						<c:forEach var="image" items="${readingRoomObjectForm.parts[loopStatus.index].contents[contentLoopStatus.index].images}" varStatus="imageLoopStatus">
							<form:hidden path="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].images[${imageLoopStatus.index}].caption"/>
							<form:hidden path="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].images[${imageLoopStatus.index}].folioNumber"/>
							<form:hidden path="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].images[${imageLoopStatus.index}].iconClass"/>
							<form:hidden path="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].images[${imageLoopStatus.index}].imgId"/>
							<form:hidden path="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].images[${imageLoopStatus.index}].notesToPhotographer"/>
							<form:hidden path="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].images[${imageLoopStatus.index}].revisit"/>
							<form:hidden path="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].images[${imageLoopStatus.index}].seq"/>
							<form:hidden path="parts[${loopStatus.index}].contents[${contentLoopStatus.index}].images[${imageLoopStatus.index}].url1"/>
						</c:forEach>													
					</c:forEach>											
				</div>
				
				<button type="button" class="btn btn-success addContentItem add">Add Item</button>	
			</div>
		</div>							
	</c:forEach>
</div>