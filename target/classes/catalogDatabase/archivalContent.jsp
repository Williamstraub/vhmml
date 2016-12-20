<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="vhmmlfn" uri="VhmmlFunctions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<h3 class="archivalContent">
	<div class="catalog-database-icon formSectionToggle"><span class="glyphicon glyphicon-triangle-right hidden"></span> Archival Contents</div>
	<div class="btn-group">
		<button type="button" class="btn btn-success dropdown-toggle addArchivalItem add" data-toggle="dropdown" aria-expanded="false">Add <span class="caret"></span></button>
		<ul class="dropdown-menu" role="menu">
			<li data-part-type="ARCHIVAL_OBJECT" class="ARCHIVAL_OBJECT"><a>Archival Material</a></li>
		</ul>			
	</div>
</h3>

<div class="formSection contentSection archivalContent contentItemContainer">
	<form:hidden path="archivalData.hasArchivalContent" cssClass="hasArchivalContent" />
	
	<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.archivalData.content)}" varStatus="loopStatus">
		<c:set var="contentItem" value="${readingRoomObjectForm.archivalData.content[loopStatus.index]}"/>	
		<%-- if there are no contents, we still render one set of content fields so the user can add one --%>							
		<div class="contentItem" data-item-index="${loopStatus.index}" data-item-id="${contentItem.id}">
			
			<h3 class="contentHeading">
				<div href="#" class="catalog-database-icon formSectionToggle">
					<span class="glyphicon glyphicon-triangle-bottom"></span> Folder <span class="archivalFolder">${contentItem.folder}</span> Item <span class="archivalItem">${contentItem.item}</span>
					<button type="button" class="btn btn-danger delete">Delete Content</button>
				</div>
			</h3>
			<div class="formSection">
				<vhmml:select label="Type" options="${archivalItemTypes}" fieldName="type" listName="archivalData.content" listIndex="${loopStatus.index}" optionLabelProperty="name" required="true" cssClass="itemType" hideEmptyFirstOption="true"/>
				<vhmml:input label="Folder" fieldName="folder" listName="archivalData.content" cssClass="archivalFolder digitsOnly" listIndex="${loopStatus.index}" helpText="If an item is a volume, then leave this Folder field blank" helpIconClass="catalog-database-icon" />
				<vhmml:input label="Item" fieldName="item" listName="archivalData.content" cssClass="archivalItem digitsOnly" listIndex="${loopStatus.index}" />
				
				<div class="groupedFields">
					<vhmml:input label="Item extent, if in a folder" fieldName="extent" listName="archivalData.content" listIndex="${loopStatus.index}" />	
					<vhmml:input label="Item location, if in a volume" fieldName="itemLocation" listName="archivalData.content" listIndex="${loopStatus.index}" />	
				</div>
				
				<vhmml:input label="Item(s) Description" fieldName="description" listName="archivalData.content" listIndex="${loopStatus.index}" textarea="true" cssClass="ckEditor"/>
				<vhmml:date label="Date Precise" fieldName="datePrecise" listName="archivalData.content" listIndex="${loopStatus.index}" helpText="Exact date information found within an item. If the year is not known, leave the Year field blank." helpIconClass="catalog-database-icon"/>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">Year Range</label>
					<div class="col-sm-9">
						<label for="archivalData.content[${loopStatus.index}].beginDate" class="inlineLabel">Begin</label>							
						<form:input path="archivalData.content[${loopStatus.index}].beginDate" cssClass="form-control dateYear integersOnly" maxlength="5"/>
						
						<label for="archivalData.content[${loopStatus.index}].endDate" class="inlineLabel">End</label>							
						<form:input path="archivalData.content[${loopStatus.index}].endDate" class="form-control dateYear integersOnly" maxlength="5"/>
						
						<i class="glyphicon glyphicon-question-sign catalog-database-icon" data-toggle="tooltip" title="For objects with a single exact year, enter the same year in both Begin and End fields 
						(Begin: 1475 End: 1475). For objects with known or estimated range of years, use the earliest and last year (Begin: 1525 End: 1625). For objects with a known or estimated century or range of centuries, but not a specific year, use the beginning and end of the century range 
						(Example to represent 15th-16th century use: Begin: 1400 End: 1600)." data-placement="right" data-trigger="click hover"></i>							
					</div>
				</div>
				
				<vhmml:select label="Date Century(ies)" fieldName="centuries" listName="archivalData.content" listIndex="${loopStatus.index}" 
					cssClass="groupbox" formGroupClass="archivalField" options="${centuries}" optionValueProperty="key" optionLabelProperty="value" 
					multiSelect="true" helpText="You can multi-select centuries for manuscripts with broader date ranges. If you select a Date Century you must also select a Year Range."  helpIconClass="catalog-database-icon" />
				
				<vhmml:checkbox label="Century Provisional" fieldName="centuryUncertain" listName="archivalData.content" listIndex="${loopStatus.index}" helpText="Leave unchecked if the century date is known/verified." helpIconClass="catalog-database-icon"/>
				<vhmml:input label="Native Date" fieldName="nativeDate" listName="archivalData.content" listIndex="${loopStatus.index}" />
					
				<vhmml:select label="Support(s)" fieldName="supportList" listName="archivalData.content" listIndex="${loopStatus.index}" options="${supports}" optionLabelProperty="name" multiSelect="true" />
				<vhmml:dimension-input label="Support Dimensions" fieldName="supportDimensions" listName="archivalData.content" listIndex="${loopStatus.index}" helpText="The outer dimensions of the support measured in centimeters. Round to next closest tenth of a centimeter. Example: 32.2 x 25.7 cm." helpIconClass="catalog-database-icon" cssClass="positiveDecimalsOnly"/>
				<vhmml:input label="Medium" fieldName="medium" listName="archivalData.content" listIndex="${loopStatus.index}" />
				<vhmml:input label="Page Layout" fieldName="pageLayout" listName="archivalData.content" listIndex="${loopStatus.index}" textarea="true" helpIconClass="catalog-database-icon" helpText="The layout of a page; the relationship of written space to the ruling pattern and to the blank space on the page; the number of columns of text; and the arrangement of glosses, commentaries, initials, and decoration in relation to the main text."/>
				
				<div id="archivalData.content[${loopStatus.index}].scribes" class="repeatableField">
					<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.archivalData.content[loopStatus.index].scribes)}" varStatus="scribesLoop">
						<div class="repeatableFieldGroup groupedFields authorityField">
							<vhmml:input label="Scribe" fieldName="contributor.name" listName="archivalData.content[${loopStatus.index}].scribes" listIndex="${scribesLoop.index}" repeatable="true" cssClass="contributor"/>							
							<%-- note that nameNs is an attribute of the partContributor relationship (not the contributor)  because the same author could have a native script name in Greek on one content item but Arabic on another, etc. --%>
							<vhmml:input label="Scribe Native Script" fieldName="nameNs" listName="archivalData.content[${loopStatus.index}].scribes" listIndex="${scribesLoop.index}" cssClass="hideForPrinted nameNs"/>
							<%-- hidden as per Daniel by William November 10, 2016
							<vhmml:input label="Scribe LC URL" fieldName="contributor.authorityUriLC" listName="archivalData.content[${loopStatus.index}].scribes" listIndex="${scribesLoop.index}" cssClass="lcUri" disabled="true"/>
							<vhmml:input label="Scribe VIAF URL" fieldName="contributor.authorityUriVIAF" listName="archivalData.content[${loopStatus.index}].scribes" listIndex="${scribesLoop.index}" cssClass="viafUri" disabled="true"/>
							--%>
						</div>
					</c:forEach>
				</div>
				
				<vhmml:select label="Language(s)" fieldName="languageIds" cssClass="groupbox" listName="archivalData.content" listIndex="${loopStatus.index}" options="${languages}" optionLabelProperty="name" optionValueProperty="id" multiSelect="true" />
				<vhmml:select label="Writing System(s)" cssClass="groupbox" fieldName="writingSystemList" listName="archivalData.content" listIndex="${loopStatus.index}" options="${writingSystems}" formGroupClass="archivalField" optionLabelProperty="name" optionValueProperty="name" multiSelect="true" addButtonType="WRITING_SYSTEM" addButtonLabel="Writing System"/>				
				<vhmml:select label="Script(s)" fieldName="scriptList" cssClass="groupbox" listName="archivalData.content" listIndex="${loopStatus.index}" options="${scripts}" optionLabelProperty="name" optionValueProperty="name" multiSelect="true" addButtonType="SCRIPT" addButtonLabel="Script"/>
				
				<div id="archivalData.content[${loopStatus.index}].artists" class="repeatableField">
					<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.archivalData.content[loopStatus.index].artists)}" varStatus="artistLoop">
						<div class="repeatableFieldGroup groupedFields authorityField">
							<vhmml:input label="Artist" fieldName="contributor.name" listName="archivalData.content[${loopStatus.index}].artists" listIndex="${artistLoop.index}" repeatable="true" cssClass="contributor"/>																	
							<vhmml:input label="Artist LC URL" fieldName="contributor.authorityUriLC" listName="archivalData.content[${loopStatus.index}].artists" listIndex="${artistLoop.index}" cssClass="lcUri" disabled="true"/>
							<vhmml:input label="Artist VIAF URL" fieldName="contributor.authorityUriVIAF" listName="archivalData.content[${loopStatus.index}].artists" listIndex="${artistLoop.index}" cssClass="viafUri" disabled="true"/>
						</div>
					</c:forEach>
				</div>
				
				<div id="archivalData.content[${loopStatus.index}].associatedNames" class="repeatableField">	
					<c:set var="associatedNameCount" value="0"/>
					<c:set var="associatedNames" value="${readingRoomObjectForm.archivalData.content[loopStatus.index].associatedNames}"/>
					<c:if test="${associatedNames != null && associatedNames.size() > 0}">
						<c:set var="associatedNameCount" value="${associatedNames.size() - 1}"/>
					</c:if>
					<c:forEach begin="0" end="${associatedNameCount}" varStatus="associatedNameLoop">
						<div class="repeatableFieldGroup groupedFields authorityField">
							<vhmml:input-select-combo 
								label="Associated Name"  
								listName="archivalData.content[${loopStatus.index}].associatedNames" 
								listIndex="${associatedNameLoop.index}" 
								options="${associatedNameTypes}" 
								optionLabelProperty="displayName"
								countFieldName="contributor.name"
								countFieldCssClass="contributor"
								typeFieldName="type"
								repeatable="true" />
							<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER">
								<vhmml:input label="Associated Name Display" fieldName="contributor.displayName" listName="archivalData.content[${loopStatus.index}].associatedNames" listIndex="${associatedNameLoop.index}" cssClass="displayName"/>
							</security:authorize>
							<vhmml:input label="Associated Name LC URL" fieldName="contributor.authorityUriLC" listName="archivalData.content[${loopStatus.index}].associatedNames" listIndex="${associatedNameLoop.index}" cssClass="lcUri" disabled="true"/>
							<vhmml:input label="Associated Name VIAF URL" fieldName="contributor.authorityUriVIAF" listName="archivalData.content[${loopStatus.index}].associatedNames" listIndex="${associatedNameLoop.index}" cssClass="viafUri" disabled="true"/>
							
						</div>										
					</c:forEach>
				</div>
				
				<c:set var="disabled" value="false"/>
				<c:set var="itemBibClass" value="ckEditor"/>
				
				<security:authorize ifNotGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER">
					<c:set var="disabled" value="true"/>
					<form:hidden path="archivalData.content[${loopStatus.index}].itemCondition"/>					
					<form:hidden path="archivalData.content[${loopStatus.index}].bibliography"/>	
					<c:set var="itemBibClass" value=""/>			
				</security:authorize>
				
				<vhmml:input label="Item Condition" fieldName="itemCondition" listName="archivalData.content" listIndex="${loopStatus.index}" deprecated="true"
					helpText="Deprecated field--do not use. Condition data regarding the item. Data needs to be moved to Condition Notes in Object Physical Description, noting location in object." 
					helpIconClass="catalog-database-icon" textarea="true" disabled="${disabled}"/>
				
				<vhmml:input label="Item Notes" fieldName="notes" listName="archivalData.content" listIndex="${loopStatus.index}" textarea="true" cssClass="ckEditor"/>
				<vhmml:input label="Decoration/Illustration" fieldName="decoration" listName="archivalData.content" listIndex="${loopStatus.index}" textarea="true" cssClass="ckEditor"/>								
				<vhmml:input label="Item Bibliography" fieldName="bibliography" listName="archivalData.content" listIndex="${loopStatus.index}" 
					textarea="true" cssClass="${itemBibClass}" deprecated="true" disabled="${disabled}"/>
				 
			</div>			
		</div>							
	</c:forEach>
	
	<div class="btn-group">
		<button type="button" class="btn btn-success dropdown-toggle addArchivalItem add" data-toggle="dropdown" aria-expanded="false">Add <span class="caret"></span></button>
		<ul class="dropdown-menu" role="menu">
			<li data-part-type="ARCHIVAL_OBJECT" class="ARCHIVAL_OBJECT"><a>Archival Material</a></li>
		</ul>			
	</div>
</div>