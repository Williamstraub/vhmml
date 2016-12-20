<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="vhmmlfn" uri="VhmmlFunctions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-switch.min.css"/>	
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/jquery-ui-plugins-groupbox.css"/>	
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/catalog-database.css?version=${applicationVersion}"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/typeahead.css"/>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/bootstrap-switch.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/arrive.min.js"></script>
<script src="https://cdn.ckeditor.com/4.4.7/full/ckeditor.js"></script>

<div id="savingMessage">Saving Progress...</div>

<form:form commandName="readingRoomObjectForm" cssClass="form-horizontal" data-object-type="READING_ROOM" method="POST" action="${pageContext.request.contextPath}/catalogDatabase/save" data-validation-rule-url="/catalogDatabase/readingRoomObject/validationRules">
	<c:set var="mainHeading" value="Add a new catalog record"/>
	<c:if test="${readingRoomObjectForm.readingRoomObjectId != null}">
		<c:set var="mainHeading" value="Edit ${readingRoomObjectForm.recordType.displayName} Record"/>
	</c:if>
	
	<h2>
		<span class="mainHeading">${mainHeading} </span><button id="toggleAll" type="button" class="btn catalog-database"><label>Collapse All</label> &nbsp;<span class="glyphicon glyphicon-triangle-bottom"></span></button>
	</h2>
		
	<vhmml:select label="Type of Record" fieldName="recordType" options="${objectTypes}" optionValueProperty="name" optionLabelProperty="displayName" required="true" labelColumns="2" fieldColumns="10" hideEmptyFirstOption="true"/>
	
	<h3 class="objectData"><div class="catalog-database-icon formSectionToggle"><span class="glyphicon glyphicon-triangle-bottom"></span><span class="accessDataHeading"> Object Access Data</span></div></h3>
	<div class="formSection objectData">	
		<form:hidden path="readingRoomObjectId" class="objectId"/>
		<%-- we need this flag because spring will send up a list containing one empty part object even if the user didn't add any --%>
		<form:hidden path="hasParts" cssClass="hasParts"/>
		<form:hidden path="iconName"/>
		<div class="row">
			<div class="col-sm-3 objectIcon">
				<div class="iconPlaceholder rr-thumbnail" onmousedown="return false">
					<div class="icon-wrapper">
						<div class="text-center image-placeholder light-green-bg"><i class="card-icon green-icon icon-open-book"></i></div>
					</div>
				</div>
				
				<img id="iconImage" class="iconImage" src="${pageContext.request.contextPath}${readingRoomObjectForm.iconUrl}" data-image-name="${readingRoomObjectForm.iconName}"/>								
			</div>
			<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER">
				<div class="col-sm-3">
					<button id="selectIconButton" type="button" class="btn catalog-database">Select Icon</button>
					<button id="clearIconButton" type="button" class="btn catalog-database">Clear Icon</button>
				</div>
				<div class="col-sm-6">
					<div><b>HMML Project Number: ${readingRoomObjectForm.hmmlProjectNumber}</b></div>
					<div>Image Server: ${readingRoomObjectForm.imageServer}</div>
					<div>Images Directory: ${readingRoomObjectForm.imagesLocation == null ? 'Not Found' : readingRoomObjectForm.imagesLocation}</div>
					<div>Object ID: <span class="objectId">${readingRoomObjectForm.readingRoomObjectId}</span></div>
					<div>Permanent Link: http://${configValues['permalink.url'].value}/readingRoom/view/${readingRoomObjectForm.readingRoomObjectId}</div>				
				</div>
			</security:authorize>
		</div>
					
		<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER">
			<vhmml:checkbox label="Status" fieldName="active" cssClass="switch"/>
		</security:authorize>
		
		<security:authorize ifAnyGranted="ROLE_ADMIN">
			<vhmml:checkbox label="Public IIIF Manifest" fieldName="publicManifest" />
		</security:authorize>
		
		<c:set var="inputterEditDisabled" value="true"/>
		
		<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER">
			<c:set var="inputterEditDisabled" value="false"/>
		</security:authorize>
		
		<vhmml:input label="Record Creator" fieldName="inputter" helpText="The person who created the record. Information auto-populated at sign in or after import." helpIconClass="catalog-database-icon" disabled="${inputterEditDisabled}"/>
		
		<c:if test="${not empty readingRoomObjectForm.lastUpdatedBy}">
			<div class="form-group">
				<label class="col-sm-3 control-label">Last Edited By</label>
				<div class="col-sm-9">
					<span class="non-editable">${readingRoomObjectForm.lastUpdatedBy} on <fmt:formatDate value="${readingRoomObjectForm.lastUpdate}" pattern="yyyy/MM/dd 'at' hh:mm aaa z"/></span>			
				</div>
			</div>
		</c:if>				
		
		<security:authorize ifAnyGranted="ROLE_ADMIN">
			<vhmml:select label="Assigned Scholar" options="${scholars}" fieldName="assignedScholar" optionLabelProperty="username" optionValueProperty="id" />
		</security:authorize>
		
		<vhmml:input label="Processed by Institution" fieldName="processedBy" required="true" />
		<vhmml:select label="Surrogate Format" options="${formats}" fieldName="surrogateFormatId" optionLabelProperty="name" optionValueProperty="id" required="true"/>
		<vhmml:date label="Capture Date" fieldName="captureDate" />
		<div id="alternateSurrogateFormats" class="repeatableField">
			<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.alternateSurrogateFormats)}" varStatus="loopStatus">						
				<div class="repeatableFieldGroup">
					<vhmml:input label="Alternate Surrogate" listName="alternateSurrogateFormats" listIndex="${loopStatus.index}" repeatable="true"/>		
				</div>
			</c:forEach>		
		</div>
	
		<vhmml:input label="Reproduction Notes" fieldName="reproductionNotes" textarea="true" />
		<vhmml:select label="Access Restrictions" options="${accessRestrictions}" fieldName="accessRestriction" optionLabelProperty="displayName" required="true" hideEmptyFirstOption="true"/>
		<%-- <vhmml:checkbox label="Images Viewable Online" fieldName="viewableOnline" /> --%>		
		<vhmml:select label="Downloadable" options="${downloadOptions}" fieldName="downloadOption" optionLabelProperty="displayName" hideEmptyFirstOption="true"/>
		<vhmml:checkbox label="Right-to-Left" fieldName="rightToLeft" />
		
		<div id="externalFacsimileUrls" class="repeatableField">
			<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.externalFacsimileUrls)}" varStatus="loopStatus">
				<div class="repeatableFieldGroup groupedFields">
					<vhmml:input label="External Facsimile URL" listName="externalFacsimileUrls" listIndex="${loopStatus.index}" fieldName="url" repeatable="true"/>
				</div>			
			</c:forEach>
		</div>	
		
		<div id="externalBibliographyUrls" class="repeatableField">
			<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.externalBibliographyUrls)}" varStatus="loopStatus">
				<div class="repeatableFieldGroup groupedFields">
					<vhmml:input label="External Bibliography" listName="externalBibliographyUrls" listIndex="${loopStatus.index}" fieldName="linkText" textarea="true" cssClass="ckEditor" repeatable="true"/>
					<vhmml:input label="External Bibliography URL" listName="externalBibliographyUrls" listIndex="${loopStatus.index}" fieldName="url" />												
				</div>			
			</c:forEach>
		</div>		
	</div>	
	
	<h3 class="objectData"><div class="catalog-database-icon formSectionToggle"><span class="glyphicon glyphicon-triangle-bottom"></span> <span class="objectDataHeading">Object Data</span></div></h3>
	<div class="formSection objectData">
	
		<vhmml:input label="HMML Project Number" fieldName="hmmlProjectNumber" cssClass="projectNumber" required="true" helpText="The code prefix and item number of a digital object or the main series number assigned to a microform by HMML. Do not include underscores. Example: CCM 00401." helpIconClass="catalog-database-icon"/>		
		<vhmml:select label="Country" options="${countries}" fieldName="countryId" cssClass="country updateAuthorityFields" addButtonType="COUNTRY" optionLabelProperty="name" optionValueProperty="id" required="true" isAuthorityListField="true"/>				
		<vhmml:select label="City" options="${citiesByCountry.get(readingRoomObjectForm.countryId)}" fieldName="cityId" cssClass="city updateAuthorityFields" optionValueProperty="id" optionLabelProperty="name" addButtonType="CITY" required="true" isAuthorityListField="true"/>
		<vhmml:select label="Holding Institution" options="${institutions}" fieldName="holdingInstitutionId" optionValueProperty="id" optionLabelProperty="name" addButtonType="INSTITUTION" helpText="An institution responsible for more than one repository." helpIconClass="catalog-database-icon"/>
		<vhmml:select label="Repository" options="${repositoriesByCity.get(readingRoomObjectForm.cityId)}" fieldName="repositoryId" cssClass="repository updateAuthorityFields" optionValueProperty="id" optionLabelProperty="name" addButtonType="REPOSITORY" required="true" isAuthorityListField="true"/>
		
		<vhmml:input label="Archival Collection/Fonds" fieldName="archivalData.collectionFond" cssClass="archivalField"/>
		<vhmml:input label="Archival Series" fieldName="archivalData.series" cssClass="archivalField"/>
		<vhmml:input label="Archival Sub-series" fieldName="archivalData.subSeries" cssClass="archivalField"/>		
		<vhmml:input label="Archival Sub-sub-series" fieldName="archivalData.subSubSeries" cssClass="archivalField"/>		
				
		<vhmml:input label="Shelfmark" fieldName="shelfMark"/>
		<vhmml:input label="Common Name" fieldName="commonName"/><%-- helpText="This field was previously labeled Nickname." helpIconClass="catalog-database-icon" --%>
		<vhmml:select label="Current Status" options="${statuses}" fieldName="currentStatus" optionLabelProperty="displayName" required="true"/>
		<vhmml:input-select-combo label="Container" options="${archivalContainers}" typeFieldName="archivalData.containerType" countFieldName="archivalData.containerCount" optionValueProperty="name" optionLabelProperty="name" cssClass="archivalField" countFieldCssClass="digitsOnly" addButtonType="CONTAINER" addButtonLabel="Container"/>
		
		<div id="extents" class="repeatableField">
			<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.extents)}" varStatus="loopStatus">						
				<div class="repeatableFieldGroup">					
					<vhmml:input-select-combo label="Extent"  listName="extents" listIndex="${loopStatus.index}" options="${extentTypes}" optionLabelProperty="displayName" repeatable="true" importedValue="folioImported" countFieldCssClass="digitsOnly"/>
				</div>
			</c:forEach>									
		</div>
		
		<vhmml:input label="Creator" fieldName="archivalData.creator" cssClass="archivalField"/>
		<vhmml:input label="Title" fieldName="archivalData.title" textarea="true" cssClass="archivalField ckEditor"/>
		<vhmml:input label="Scope and Contents" fieldName="archivalData.scopeContent" textarea="true" cssClass="archivalField ckEditor"/>
		<vhmml:input label="Historical Note" fieldName="archivalData.historicalNote" textarea="true" cssClass="archivalField ckEditor"/>
		<vhmml:input label="Custodial History" fieldName="archivalData.custodialHistory" textarea="true" cssClass="archivalField ckEditor"/>
		
		<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER">		
			<div id="subjects" class="repeatableField">
				<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.subjects)}" varStatus="loopStatus">
					<div class="repeatableFieldGroup groupedFields">
						<vhmml:input label="Subject" listName="subjects" listIndex="${loopStatus.index}" fieldName="name" repeatable="true"/>
						<vhmml:input label="Subject LC URL" listName="subjects" listIndex="${loopStatus.index}" fieldName="authorityUriLC" cssClass="lcUri" disabled="true"/>
						<vhmml:input label="Subject VIAF URL" listName="subjects" listIndex="${loopStatus.index}" fieldName="authorityUriVIAF" cssClass="viafUri" disabled="true"/>
					</div>
				</c:forEach>			
			</div>
		</security:authorize>
		
		<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER">
			<div id="genres" class="repeatableField">
				<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.genres)}" varStatus="loopStatus">
					<div class="repeatableFieldGroup groupedFields">
						<vhmml:select label="Genre/Form" listName="genres" listIndex="${loopStatus.index}" fieldName="id" options="${genres}" optionLabelProperty="name" optionValueProperty="id" repeatable="true" hasAssociatedUri="true"/>
						<vhmml:input label="Genre/Form URL" listName="genres" listIndex="${loopStatus.index}" fieldName="uri" disabled="true" cssClass="associatedUri"/>
					</div>			
				</c:forEach>
			</div>
		</security:authorize>
		
		<!-- ASSOCIATED NAMES AT THE OBJECT LEVEL FOR ARCHIVAL OBJECTS ONLY -->
		<div id="archivalData.associatedNames" class="archivalField repeatableField">	
			<c:set var="associatedNameCount" value="0"/>
			<c:set var="associatedNames" value="${readingRoomObjectForm.archivalData.associatedNames}"/>
			<c:if test="${associatedNames != null && associatedNames.size() > 0}">
				<c:set var="associatedNameCount" value="${associatedNames.size() - 1}"/>
			</c:if>
			<c:forEach begin="0" end="${associatedNameCount}" varStatus="associatedNameLoop">
				<div class="repeatableFieldGroup groupedFields authorityField">
					
					<vhmml:input-select-combo 
						label="Associated Name"  
						listName="archivalData.associatedNames" 
						listIndex="${associatedNameLoop.index}" 
						options="${associatedNameTypes}" 
						optionLabelProperty="displayName"
						countFieldName="contributor.name"
						countFieldCssClass="contributor"
						typeFieldName="type"
						repeatable="true" />
					
					<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER">
						<vhmml:input label="Associated Name Display" fieldName="contributor.displayName" listName="archivalData.associatedNames" listIndex="${associatedNameLoop.index}" cssClass="displayName"/>
					</security:authorize>
					<vhmml:input label="Associated Name LC URL" fieldName="contributor.authorityUriLC" listName="archivalData.associatedNames" listIndex="${associatedNameLoop.index}" cssClass="lcUri" disabled="true"/>
					<vhmml:input label="Associated Name VIAF URL" fieldName="contributor.authorityUriVIAF" listName="archivalData.associatedNames" listIndex="${associatedNameLoop.index}" cssClass="viafUri" disabled="true"/>
					<vhmml:input label="Associated Name Native Script" fieldName="nameNs" listName="archivalData.associatedNames" listIndex="${associatedNameLoop.index}"/>					
				</div>
			</c:forEach>
		</div>
			
		<vhmml:select label="Features" cssClass="groupbox" options="${features}" fieldName="featureIds" optionLabelProperty="name" optionValueProperty="id" multiSelect="true" addButtonType="FEATURE" addButtonLabel="Feature" importedValue="featuresImported"/>
		<vhmml:date label="Date Precise" fieldName="archivalData.datePrecise" cssClass="archivalField" helpText="Exact date information found within an object. If the year is not known, leave the Year field blank." helpIconClass="catalog-database-icon"/>
		
		<div class="form-group archivalField">
			<label class="col-sm-3 control-label">Year Range</label>
			<div class="col-sm-9">
				<label for="archivalData.beginDate" class="inlineLabel">Begin</label>							
				<form:input path="archivalData.beginDate" cssClass="form-control dateYear integersOnly" maxlength="5"/>
				
				<label for="archivalData.endDate" class="inlineLabel">End</label>							
				<form:input path="archivalData.endDate" class="form-control dateYear integersOnly" maxlength="5"/>
				
				<i class="glyphicon glyphicon-question-sign catalog-database-icon" data-toggle="tooltip" title="For objects with a single exact year, enter the same year in both Begin and End fields 
				(Begin: 1475 End: 1475). For objects with known or estimated range of years, use the earliest and last year (Begin: 1525 End: 1625). For objects with a known or estimated century or range of centuries, but not a specific year, use the beginning and end of the century range 
				(Example to represent 15th-16th century use: Begin: 1400 End: 1600)." data-placement="right" data-trigger="click hover"></i>							
			</div>
		</div>
		
 		<vhmml:select label="Date Century(ies)" fieldName="archivalData.centuries" cssClass="groupbox" formGroupClass="archivalField" options="${centuries}" optionValueProperty="key" optionLabelProperty="value" 
			multiSelect="true" helpText="You can multi-select centuries for manuscripts with broader date ranges. If you select a Date Century you must also select a Year Range."  helpIconClass="catalog-database-icon" note="Imported Value: ${readingRoomObjectForm.archivalData.centuryImported}" noteClass="importedValue"/>
			
		<div class="archivalField">
			<vhmml:checkbox label="Century Provisional" fieldName="archivalData.centuryUncertain" cssClass="archivalField" helpText="Leave unchecked if the century date is known/verified." helpIconClass="catalog-database-icon"/>
		</div>		
		
		<vhmml:input label="Native Date" fieldName="archivalData.nativeDate" cssClass="archivalField"/>
		
		<vhmml:select label="Support(s)" fieldName="archivalData.supportList" formGroupClass="archivalField" options="${supports}" optionLabelProperty="name" multiSelect="true" />
		<vhmml:select label="Language(s)" fieldName="archivalData.languageIds" cssClass="groupbox" formGroupClass="archivalField" options="${languages}" optionLabelProperty="name" optionValueProperty="id" multiSelect="true" />
		<vhmml:select label="Writing System(s)" cssClass="groupbox" fieldName="archivalData.writingSystemList" options="${writingSystems}" formGroupClass="archivalField" optionLabelProperty="name" optionValueProperty="name" multiSelect="true" addButtonType="WRITING_SYSTEM" addButtonLabel="Writing System"/>
		
		<vhmml:input label="General Notes" fieldName="notes" textarea="true" cssClass="ckEditor"/>	
		<vhmml:input label="Bibliography" fieldName="bibliography" textarea="true" cssClass="ckEditor"/>
		<vhmml:input label="Acknowledgments" fieldName="acknowledgments" textarea="true"/>
	</div>
	
	<h3 class="objectData"><div class="catalog-database-icon formSectionToggle"><span class="glyphicon glyphicon-triangle-bottom"></span> <span class="physicalDescriptionHeading">Object Physical Description</span></div></h3>
	<div class="formSection objectData">
		<vhmml:input label="Collation" fieldName="collation" textarea="true" cssClass="ckEditor hideForArchival"/>		
		<vhmml:input label="Foliation" fieldName="foliation" cssClass="hideForArchival" />
		<vhmml:input label="Binding" fieldName="binding" textarea="true" cssClass="ckEditor"/>
		<vhmml:dimension-input label="Binding Dimensions" fieldName="binding" helpText="The outer dimensions of the binding measured in centimeters. Round to next closest tenth of a cm. Example: 32.2 x 25 x 3.7 cm." helpIconClass="catalog-database-icon" includeDepth="true" cssClass="positiveDecimalsOnly" importedValue="bindingDimensionsImported"/>		
		<vhmml:input label="Provenance" fieldName="provenance" textarea="true" cssClass="ckEditor"/>
		
		<div id="formerOwners" class="repeatableField">
			<c:forEach begin="0" end="${vhmmlfn:getFieldCount(readingRoomObjectForm.formerOwners)}" varStatus="loopStatus">
				<div class="authorityField repeatableFieldGroup">
					<vhmml:input label="Former Owner" listName="formerOwners" listIndex="${loopStatus.index}" fieldName="name" repeatable="true" cssClass="contributor"/>				
					<%--Hide LC and VIAF URL since rarely used & also remove classes for highlighting from div above for: repeatableFieldGroup groupedFields 
					<vhmml:input label="Former Owner LC URL" listName="formerOwners" listIndex="${loopStatus.index}" fieldName="authorityUriLC" cssClass="lcUri" disabled="true"/>				
					<vhmml:input label="Former Owner VIAF URL" listName="formerOwners" listIndex="${loopStatus.index}" fieldName="authorityUriVIAF" cssClass="viafUri" disabled="true"/>
					 --%>
				</div>
			</c:forEach>
		</div>
		
		<vhmml:input label="Condition Notes" fieldName="conditionNotes" textarea="true" cssClass="ckEditor"/>
		<vhmml:input fieldName="archivalData.decoration" label="Decoration/Illustration" textarea="true" cssClass="ckEditor archivalField"/>
	</div>
	
	<!-- archival content is a mix of object parts & object content rolled up so we just put it in a separate page so we can
		show/hide objectParts or archivalContent based on what the user selects for record type, rather than maintaining a bunch
		of rules and CSS class to show/hide specific fields based on record type -->	
	<jsp:include page="readingRoomParts.jsp"></jsp:include>			
	<jsp:include page="archivalContent.jsp"></jsp:include>
	
	<p>&nbsp;</p>	
	<div class="buttonRow">
		<c:set var="deleteButtonClass" value=""/>
		
		<c:if test="${readingRoomObjectForm.readingRoomObjectId == null}">
			<c:set var="deleteButtonClass" value="hidden"/>		
		</c:if>
		
		<button type="button" class="btn btn-danger delete ${deleteButtonClass}" data-delete-url="${pageContext.request.contextPath}/catalogDatabase/delete">Delete</button>		
		<button type="button" class="btn btn-warning cancel" data-cancel-url="/catalogDatabase">Cancel</button>
		<button type="button" class="btn btn-success save saveAndClose" data-close-url="/catalogDatabase" data-home-page="Catalog Database">Save and Close</button>
		<button type="button" class="btn btn-success save">Save</button>
	</div>
  <p>&nbsp;</p>		
</form:form>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/handlebars-v4.0.4.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/typeahead.bundle.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery-ui-plugins-groupbox.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/reading-room.js?version=${applicationVersion}"></script>

<script id="add-list-option-dialog-template" type="text/x-handlebars-template">
	<form id="addListOptionForm" name="addListOptionForm" class="horizontal-form row">
		<div class="form-group">
			<label class="col-sm-4 control-label">Name</label>
			<div class="col-sm-8">
				<input type="text" name="name" class="form-control"/>
			</div>			
		</div>
		
		<div class="form-group authorityUri">
			<label class="col-sm-4 control-label">Authority LC URL</label>
			<div class="col-sm-8">
				<input type="text" name="authorityUriLC" class="form-control"/>
			</div>
		</div>

		<div class="form-group authorityUri">
			<label class="col-sm-4 control-label">Authority VIAF URL</label>
			<div class="col-sm-8">
				<input type="text" name="authorityUriVIAF" class="form-control"/>
			</div>
		</div>
	</form>
</script>

<script type="text/javascript">
	var editing = ${readingRoomObjectForm.readingRoomObjectId != null};
	var authorityListPermission = false;
	
	<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER">
		$(function() {
			$('input.lcUri, input.viafUri').removeAttr('disabled');
			authorityListPermission = true;
		});
	</security:authorize>
	
	<security:authorize ifNotGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER">
		$('input.lcUri, input.viafUri').addClass('alwaysDisabled');
	</security:authorize>
</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/catalog-db-common.js?version=${applicationVersion}"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/catalog-db-reading-room.js?version=${applicationVersion}"></script>