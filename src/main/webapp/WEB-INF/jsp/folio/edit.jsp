<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/catalog-database.css?version=${applicationVersion}"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-switch.min.css"/>	

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/bootstrap-switch.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/arrive.min.js"></script>
<script src="https://cdn.ckeditor.com/4.4.7/full/ckeditor.js"></script>

<div id="savingMessage">Saving Progress...</div>

<form:form commandName="folioObject" cssClass="form-horizontal" data-object-type="FOLIO" method="POST" action="${pageContext.request.contextPath}/folio/admin/save" data-validation-rule-url="/folio/folioObject/validationRules">
	<c:set var="mainHeading" value="Add a new Folio Object"/>
	<c:if test="${folioObject.id != null}">
		<c:set var="mainHeading" value="Edit Folio Object"/>
	</c:if>
	
	<div class="buttonRow">
		<c:set var="deleteButtonClass" value=""/>
		
		<c:if test="${folioObject.id == null}">
			<c:set var="deleteButtonClass" value="hidden"/>		
		</c:if>
		
		<button type="button" class="btn btn-danger delete ${deleteButtonClass}" data-delete-url="${pageContext.request.contextPath}/folio/admin/delete">Delete</button>		
		<button type="button" class="btn btn-warning cancel" data-cancel-url="/folio">Cancel</button>
		<button type="button" class="btn btn-success save saveAndClose" data-close-url="/folio" data-home-page="Folio">Save and Close</button>		
		<button type="button" class="btn btn-success save">Save</button>
	</div>	
	
	<h3>${mainHeading}</h3>
	<div class="formSection">	
		<form:hidden path="id" class="objectId"/>		
		<form:hidden path="iconName"/>
		<div class="row">
			<div class="col-sm-3 objectIcon">
				<div class="iconPlaceholder rr-thumbnail" onmousedown="return false">
					<div class="icon-wrapper">
						<div class="text-center image-placeholder light-green-bg"><i class="card-icon green-icon icon-open-book"></i></div>
					</div>
				</div>
				
				<img id="iconImage" class="iconImage" src="${pageContext.request.contextPath}/image/thumbnail/FOLIO/${folioObject.folioObjectNumber}/${folioObject.iconName}" data-image-name="${folioObject.iconName}"/>								
			</div>
			<div class="col-sm-3">
				<button id="selectIconButton" type="button" class="btn folio">Select Icon</button>
				<button id="clearIconButton" type="button" class="btn folio">Clear Icon</button>
			</div>
			<div class="col-sm-6">
				<div>Image Server: ${imageServer}</div>
				<div>Images Directory: ${readingRoomObjectForm.imagesLocation == null ? 'Not Found' : readingRoomObjectForm.imagesLocation}</div>
				<div>Object ID: ${readingRoomObjectForm.readingRoomObjectId}</div>
				<div>Permanent link: http://${configValues['permalink.url'].value}/readingRoom/view/${readingRoomObjectForm.readingRoomObjectId}</div>
			</div>
		</div>
					
		<vhmml:checkbox label="Status" fieldName="active" cssClass="switch"/>
		
		<c:set var="inputterEditDisabled" value="true"/>
		
		<security:authorize ifAnyGranted="ROLE_ADMIN">
			<c:set var="inputterEditDisabled" value="false"/>
		</security:authorize>
				
		<c:if test="${inputterEditDisabled}">
			<input type="hidden" name="createdBy" value="${folioObject.createdBy}"/>
		</c:if>
		
		<vhmml:input label="Record Creator" fieldName="createdBy" helpText="The person who created the record. Information auto-populated at sign in or after import." helpIconClass="catalog-database-icon" disabled="${inputterEditDisabled}"/>
		
		<c:if test="${not empty folioObject.lastUpdatedBy}">
			<div class="form-group">
				<label class="col-sm-3 control-label">Last Edited By</label>
				<div class="col-sm-9">
					<span class="non-editable">${folioObject.lastUpdatedBy} on <fmt:formatDate value="${folioObject.lastUpdate}" pattern="yyyy/MM/dd 'at' hh:mm aaa z"/></span>			
				</div>
			</div>
		</c:if>				
		
		<vhmml:input label="Folio Object Number" fieldName="folioObjectNumber" cssClass="projectNumber"/>				
		<vhmml:input label="Country" fieldName="country" />				
		<vhmml:input label="City" fieldName="city"/>				
		<vhmml:input label="Repository" fieldName="repository"/>				
		<vhmml:input label="Shelf mark" fieldName="shelfMark"/>				
		<vhmml:input label="Common Name" fieldName="commonName"/>
		<vhmml:input label="Provenance" fieldName="provenance" textarea="true" cssClass="ckEditor"/>
		<vhmml:input label="Bibliography" fieldName="bibliography" textarea="true" cssClass="ckEditor"/>
		<vhmml:input label="External Facsimile URL" fieldName="externalUrl" />
		<vhmml:input label="Acknowledgements" fieldName="acknowledgements" textarea="true"/>	
		<vhmml:input label="Place of Origin" fieldName="placeOfOrigin"/>				
		<vhmml:input label="Date Precise" fieldName="datePrecise"/>
		<div class="form-group">						
			<label class="col-sm-3 control-label">Year Range</label>
			<div class="col-sm-9">
				<label for="beginDate" class="inlineLabel">Begin</label>							
				<form:input path="beginDate" cssClass="form-control dateYear integersOnly" maxlength="5" />
				
				<label for="endDate" class="inlineLabel">End</label>							
				<form:input path="endDate" class="form-control dateYear integersOnly" maxlength="5"/>										
			</div>
		</div>
		<vhmml:input label="Date Century" fieldName="dateCentury"/>
		<vhmml:input label="Language" fieldName="language"/>
		<vhmml:input label="Writing System" fieldName="writingSystem"/>
		<vhmml:input label="Script" fieldName="script"/>
		<vhmml:input label="Folio Title" fieldName="title" textarea="true" cssClass="ckEditor"/>
		<vhmml:input label="Folio Text" fieldName="text" textarea="true" cssClass="ckEditor"/>
		<vhmml:input label="Folio Description" fieldName="description" textarea="true" cssClass="ckEditor"/>				
		<vhmml:input label="Folio Paleography Features" fieldName="specialFeatures" textarea="true" cssClass="ckEditor"/>				
		<vhmml:input label="Transcription" fieldName="transcription" textarea="true" cssClass="ckEditor"/>				
	</div>			
    <p>&nbsp;</p>	
	<div class="buttonRow">
		<c:set var="deleteButtonClass" value=""/>
		
		<c:if test="${folioObject.id == null}">
			<c:set var="deleteButtonClass" value="hidden"/>		
		</c:if>
		
		<button type="button" class="btn btn-danger delete ${deleteButtonClass}" data-delete-url="${pageContext.request.contextPath}/folio/admin/delete">Delete</button>		
		<button type="button" class="btn btn-warning cancel" data-cancel-url="/folio">Cancel</button>
		<button type="button" class="btn btn-success save saveAndClose" data-close-url="/folio" data-home-page="Folio">Save and Close</button>		
		<button type="button" class="btn btn-success save">Save</button>
	</div>
	<p>&nbsp;</p>		
</form:form>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/catalog-db-common.js?version=${applicationVersion}"></script>