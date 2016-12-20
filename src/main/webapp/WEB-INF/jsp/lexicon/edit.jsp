<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script src="https://cdn.ckeditor.com/4.4.7/full/ckeditor.js"></script>
 
<script src="${pageContext.request.contextPath}/static/js/jquery.tagsinput.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/jquery.tagsinput.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin.css?version=${applicationVersion}" />

<style type="text/css">
	.tags {
		width: 400px;
		height: 50px;
	}
	
	button.lexicon, button.lexicon:hover {
		display: inline;
		margin-right: 10px;
	}
	
	#editTermForm label {
		text-align: left;
	}
</style>

<div class="buttonRow">
	<button class="btn button btn-default cancelButton">Cancel/Return</button>
	<button class="btn button lexicon saveButton">Update</button>
</div>

<form id="editTermForm" name="editTermForm" method="post" action="${pageContext.request.contextPath}/lexicon/admin/save">
	<input type="hidden" name="id" value="${lexiconTerm.id}"/>
	<input type="hidden" name="searchText" value="${searchText}"/>
	<input type="hidden" name="startsWithLetter" value="${startsWithLetter}"/>
	<input type="hidden" name="selectedPage" value="${selectedPage}"/>
	
	<label for="term">Term</label>
	<textarea id="term" name="term" class="ckEditor">${lexiconTerm.term}</textarea>
	
	<label for="shortDefinition">Short Definition</label>
	<textarea id="shortDefinition" name="shortDefinition" class="ckEditor">${lexiconTerm.shortDefinition}</textarea>
	
	<label for="fullDefinition">Full Definition</label>
	<textarea id="fullDefinition" name="fullDefinition" class="ckEditor">${lexiconTerm.fullDefinition}</textarea>
	
	<label for="bibliography">Bibliography</label>
	<textarea id="bibliography" name="bibliography" class="ckEditor">${lexiconTerm.bibliography}</textarea>
	
	<label for="notes">Notes</label>
	<textarea id="notes" name="notes" class="ckEditor">${lexiconTerm.notes}</textarea>	
	
	<p><em>Press <kbd>Enter</kbd> after each input for multiple Related Terms or Contributors below.</em></p>
	<label>Related Terms</label>
	<input id="relatedTerms" name="relatedTerms" value="${lexiconTerm.relatedTermsString}"/>
	
	<label>Contributors</label>
	<input id="contributors" name="contributors" value="${lexiconTerm.contributorsString}"/>
		
	<p><em>Separate multiple terms below with a semicolon ( <kbd>;</kbd> ).</em></p>
	<label for="arabicTerms">Arabic Terms</label>
	<textarea id="arabicTerms" name="arabicTerms" class="ckEditor">${lexiconTerm.arabicTerms}</textarea>
	
	<label for="armenianTerms">Armenian</label>
	<textarea id="armenianTerms" name="armenianTerms" class="ckEditor">${lexiconTerm.armenianTerms}</textarea>
	
	<label for="frenchTerms">French</label>
	<textarea id="frenchTerms" name="frenchTerms" class="ckEditor">${lexiconTerm.frenchTerms}</textarea>
	
	<label for="amharicTerms">G&#x04d9;&#x02bf;&#x04d9;z/Amharic</label>
	<textarea id="amharicTerms" name="amharicTerms" class="ckEditor">${lexiconTerm.amharicTerms}</textarea>

	<label for="germanTerms">German</label>
	<textarea id="germanTerms" name="germanTerms" class="ckEditor">${lexiconTerm.germanTerms}</textarea>
	
	<label for="italianTerms">Italian</label>
	<textarea id="italianTerms" name="italianTerms" class="ckEditor">${lexiconTerm.italianTerms}</textarea>
	
	<label for="latinTerms">Latin</label>
	<textarea id="latinTerms" name="latinTerms" class="ckEditor">${lexiconTerm.latinTerms}</textarea>
	
	<label for="portugueseTerms">Portuguese</label>
	<textarea id="portugueseTerms" name="portugueseTerms" class="ckEditor">${lexiconTerm.portugueseTerms}</textarea>
	
	<label for="spanishTerms">Spanish</label>
	<textarea id="spanishTerms" name="spanishTerms" class="ckEditor">${lexiconTerm.spanishTerms}</textarea>
	
	<label for="syriacTerms">Syriac</label>
	<textarea id="syriacTerms" name="syriacTerms" class="ckEditor">${lexiconTerm.syriacTerms}</textarea>	
</form>

<em>Separate multiple terms above  with a semicolon ( <kbd>;</kbd> ).</em>

<div class="buttonRow">
	<button class="btn button btn-default cancelButton">Cancel/Return</button>
	<button class="btn button lexicon saveButton">Update</button>
</div>

<script type="text/javascript">
	var searchText = '${searchText}';
	var startsWithLetter = '${startsWithLetter}';
	var selectedPage = '${selectedPage}';
</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/edit-lexicon.js?version=${applicationVersion}"></script>

