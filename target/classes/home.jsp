<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/home-page.css?version=${applicationVersion}"/>

<style type="text/css">
	.container {
		padding: 0;
		margin: 0;
		width: 100%;
		text-align: center;
	}
</style>

<jsp:include page="carousel.jsp"></jsp:include>

<div class="center container">	
	<h4 class="page-banner section-title">Resources and Tools for Manuscript Studies</h4>
 	<div class="cards">
		<div class="card" style="display: inline-block;">
			<img src="${pageContext.request.contextPath}/static/img/placeholder.jpg"/>
			<div class="card-content">
  				<h4 class="section-title">Search Collections</h4>
  				<span class="redtext">v</span>HMML Reading Room provides access to thousands of items in HMML&#x2019;s collections. In Reading Room you will also find improved descriptions and multiple ways to search. While collections are being added to Reading Room, the Legacy Catalog (OLIVER) and other finding aids are available to help your search. 				
			</div>  
			<div class="link-wrapper">
				<c:if test="${configValues['hide.reading.room'].value == 'false'}">
					<span class="card-link">
						<a href="${pageContext.request.contextPath}/readingRoom">READING ROOM</a>
					</span>
					
					<span class="link-spacer">|</span>
				</c:if>
				<span class="card-link">
					<a href="http://www.hmml.org/oliver.html" target="_blank">LEGACY CATALOG</a>
				</span>
			</div>
		</div>
		
		<div class="card" style="display: inline-block;">
			<img src="${pageContext.request.contextPath}/static/img/placeholder.jpg"/>
			<div class="card-content">
  				<h4 class="section-title">Learn About Manuscripts</h4>				
				School has lessons in paleography and codicology, as well as transcription exercises.
				<p>Folio features pages selected from a variety of manuscripts to illustrate scripts and layouts. Each example has detailed commentary and a full transcription.</p>
			</div>
			<div class="link-wrapper">					
				<span class="card-link">
					<a href="${pageContext.request.contextPath}/school">SCHOOL</a>
				</span>
				<c:if test="${configValues['hide.folio'].value == 'false'}">
					<span class="link-spacer">|</span>
					
					<span class="card-link">
						<a href="${pageContext.request.contextPath}/folio">FOLIO</a>
					</span>
				</c:if>	
			</div>
		</div>
		
		<div class="card" style="display: inline-block;">
			<img src="${pageContext.request.contextPath}/static/img/placeholder.jpg"/>
			<div class="card-content">
  				<h4 class="section-title">Tools For Research</h4>
				Lexicon features hundreds of terms used in manuscript studies, including those used in other modern languages. 

				<p>Reference has a growing bibliography of works about manuscripts, with links to digitized versions when available.</p>

			</div>
			<div class="link-wrapper">					
				<span class="card-link">
					<a href="${pageContext.request.contextPath}/lexicon">LEXICON</a>
				</span>
				
				<span class="link-spacer">|</span>
				
				<span class="card-link">
					<a href="${pageContext.request.contextPath}/reference">REFERENCE</a>
				</span>
			</div>
		</div>
	</div>
</div>