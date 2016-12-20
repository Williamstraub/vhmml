<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/lexicon.css?version=${applicationVersion}"/>

<h1>${term.term}</h1>

<div class="lexicon-tabs" role="tabpanel">

	<!-- Nav tabs -->
	<ul class="nav nav-tabs" role="tablist">
		<li role="presentation" class="active">
			<a href="#full-definition" aria-controls="full-definition" role="tab" data-toggle="tab">Full Definition</a>
		</li>
		
		<%-- data-toggle attribute has to be removed for tabs to be disabled with Twitter Bootstrap --%>
		<c:set var="dataToggleAtt" value="tab"/>
		<c:set var="relatedTermsClass" value=""/>
		<c:if test="${term.relatedTerms == null || term.relatedTerms.size() <= 0}">
			<c:set var="dataToggleAtt" value=""/>
			<c:set var="relatedTermsClass" value="disabled"/>			
		</c:if>
		<li role="presentation" class="${relatedTermsClass}">
			<a href="#related-terms" aria-controls="related-terms" role="tab" data-toggle="${dataToggleAtt}">Related Terms</a>
		</li>
		
		<c:set var="dataToggleAtt" value="tab"/>
		<c:set var="otherLangClass" value=""/>
		<c:if test="${term.otherLanguages == null || term.otherLanguages.size() <= 0}">
			<c:set var="dataToggleAtt" value=""/>	
			<c:set var="otherLangClass" value="disabled"/>
		</c:if>
		<li role="presentation" class="${otherLangClass}">
			<a href="#other-languages" aria-controls="other-languages" role="tab" data-toggle="${dataToggleAtt}">Other Languages</a>
		</li>
		
		<c:set var="dataToggleAtt" value="tab"/>
		<c:set var="bibliographyClass" value=""/>
		<c:if test="${term.bibliography == null || term.bibliography.length() <= 0}">
			<c:set var="dataToggleAtt" value=""/>
			<c:set var="bibliographyClass" value="disabled"/>
		</c:if>
		<li role="presentation" class="${bibliographyClass}">
			<a href="#bibliography"aria-controls="#bibliography" role="tab" data-toggle="${dataToggleAtt}">Bibliography</a>
		</li>
	</ul>

	<!-- Tab panes -->
	<div class="tab-content">
		<div id="full-definition" role="tabpanel" class="tab-pane active">
			<p>${term.fullDefinition}</p>
						
			<div>
				<c:set var="permUrl" value="http://${configValues['permalink.url'].value}/lexicon/definition/${term.id}"/>
				<label>URL to this definition:&nbsp;&nbsp;</label><a href="${permUrl}">${permUrl}</a>
			</div>
			<br/>			
			
			<c:if test="${term.contributors.size() > 0}">
				<label>Contributor(s): </label>
				<c:forEach var="contributor" items="${term.contributors}" varStatus="loopStatus">
					<c:if test="${loopStatus.index > 0}">,</c:if>
					${contributor.name}					
				</c:forEach>
			</c:if>
			<br/>
		</div>		
				
		<div id="related-terms" role="tabpanel" class="tab-pane">
			<ul>
				<c:forEach var="relatedTerm" items="${term.relatedTerms}">
					<li><a href="${pageContext.request.contextPath}/lexicon/definition/${relatedTerm.id}">${relatedTerm.term}</a></li>
				</c:forEach>
			</ul>
		</div>			
		
		
		<div id="other-languages" role="tabpanel" class="tab-pane">
			<ul class="otherLanguages">
				<c:forEach items="${term.otherLanguages}" var="lang">
					<li><label>${lang.key}</label>: ${lang.value}</li>
				</c:forEach>
			</ul>			
		</div>		
		
		<div id="bibliography" role="tabpanel" class="tab-pane">${term.bibliography}</div>
		
	</div>

</div>

<div class="buttonRow">
	<a href="${pageContext.request.contextPath}/lexicon?searchText=${searchText}&startsWithLetter=${startsWithLetter}&selectedPage=${selectedPage}">
		<button class="btn lexicon">Close</button>
	</a>
</div>
