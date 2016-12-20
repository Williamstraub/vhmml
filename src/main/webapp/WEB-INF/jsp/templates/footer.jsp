<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<footer class="main-footer">	
	<tiles:insertAttribute name="footerHelp" />
	
	<div class="footercontainer">		
		<div class="row">
			<div class="col-lg-2">
				<a href="http://www.hmml.org/"  target="_blank">
					<img src="${pageContext.request.contextPath}/static/img/HMML2015RoundLogo72.png" width="175px;" alt="Hill Museum &amp; Manuscript Library">
				</a>
			</div>
			
			<div class="col-lg-2">
				<ul>
					<c:if test="${configValues['hide.reading.room'].value == 'false'}">
						<li><a href="${pageContext.request.contextPath}/readingRoom" >READING ROOM</a></li>						
					</c:if>
					
					<li><a href="http://www.hmml.org/oliver.html" target="_blank">LEGACY CATALOG</a></li>
					<li><a href="${pageContext.request.contextPath}/school">SCHOOL</a></li>
				</ul>				
			</div>
			
			<div class="col-lg-2">
				<ul>
					<c:if test="${configValues['hide.folio'].value == 'false'}">
						<li><a href="${pageContext.request.contextPath}/folio">FOLIO</a></li>						
					</c:if>
					
					<li><a href="${pageContext.request.contextPath}/lexicon">LEXICON</a></li>
					<li><a href="${pageContext.request.contextPath}/reference">REFERENCE</a></li>
				</ul>				
			</div>
			
			<div class="col-lg-6 text-right">
				<span class="funded-by-text">Funded in part by</span>
				<br/> 
				<img
					id="m_supporterslogos_withhotspots"
					src="${pageContext.request.contextPath}/static/img/supporterslogos_200.png" 
					<%--src="${pageContext.request.contextPath}/static/img/supporterslogos-3b.png"--%>
					width="200" style="padding-bottom: 10px"
					usemap="#m_supporterslogos_withhotspots"
					alt="vHMML financial support provided in part by the following: Arcadia, The Henry Luce Foundation, IMLS (Institute of Museum and Library Services), and the Andrew W. Mellon Foundation" />
				<map name="m_supporterslogos_withhotspots"
					id="m_supporterslogos_withhotspots">
					<area shape="poly" coords="5,6,143,6,143,52,5,52,5,6"
						href="http://www.arcadiafund.org.uk/about-arcadia/about-arcadia.aspx"
						target="_blank" alt="Arcadia Fund" />
					<area shape="poly" coords="5,57,143,57,143,119,5,119,5,57"
						href="https://www.imls.gov/" target="_blank"
						alt="Institute of Museum and Library Services" />
					<area shape="poly" coords="148,6,193,6,193,119,148,119,148,6"
						href="http://www.hluce.org/" target="_blank"
						alt="The Henry Luce Foundation, Inc" />
					<area shape="poly" coords="26,120,26,159,175,159,175,120"
						href="https://mellon.org/" target="_blank" 
						alt="The Andrew W Mellon Foundation" />
				</map>
			</div>
		</div>
	</div>
	
	<div class="footercontainer">
		<div class="row">						
			<div class="col-lg-10 footer-bottom-links">
				<a href="${pageContext.request.contextPath}/about" class="saveSearch">About <span class="redtext">v</span>HMML</a>
				<a href="${pageContext.request.contextPath}/terms" class="saveSearch">Terms of Use</a>
				<a href="${pageContext.request.contextPath}/privacy" class="saveSearch">Privacy Policy</a>
				<a href="${pageContext.request.contextPath}/contact" class="saveSearch">Contact</a>
				<a href="http://www.hmml.org/support-our-mission.html" target="_blank">Support&nbsp;Our&nbsp;Mission</a>
			</div>
			
			<div class="col-lg-2 text-right">
				<a href="http://csbsju.edu/" target="_blank">
					<img src="${pageContext.request.contextPath}/static/img/SJUHorizR1x.png" alt="Saint John's University" />
				</a>
			</div>
		</div>
	</div>
</footer>
