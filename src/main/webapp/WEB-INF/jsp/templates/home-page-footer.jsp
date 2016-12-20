<footer class="main-footer">
	<div class="footercontainer">
		<div class="row">
			<div class="col-lg-12 text-center">
				<jsp:include page="footer-links.jsp"></jsp:include>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-8 col-sm-5 col-lg-3">
				<small>Funded in part by</small><br /> <img
					id="supporterslogos_withhotspots"
					src="${pageContext.request.contextPath}/static/img/supporterslogos_200.png"
					width="200" height="126" style="padding-bottom: 10px"
					usemap="#m_supporterslogos_withhotspots"
					alt="vHMML financial support provided in part by the following: Arcadia, The Henry Luce Foundation, and Institute of Museum and Library Services" />
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
				</map>

				<br /> <a href="${pageContext.request.contextPath}/about"><small>About
						<em><span class="redtext">v</span></em>HMML
				</small></a>

			</div>

			<div class="col-xs-2 col-sm-3 col-md-3 col-lg-6">
				<a href="http://www.hmml.org/" target="_blank"><img
					src="${pageContext.request.contextPath}/static/img/HMML2015RoundLogo72.png"
					width="140" height="140" style="margin-top: 14px"
					alt="Hill Museum &amp; Manuscript Library" class="center-block"></a>
			</div>

			<div class="col-xs-3 col-sm-4 col-md-4 col-lg-3 text-right">

				<a href="${pageContext.request.contextPath}/terms"><small><em><span class="redtext">v</span></em>HMML Terms
						of Use</small></a> <br />
				<br /> <a href="http://csbsju.edu/" target="_blank"> <img
					src="${pageContext.request.contextPath}/static/img/SJUHorizR1x.png"
					width="140" alt="Saint John's University" />
				</a>

				<p>
					<a href="${pageContext.request.contextPath}/privacy"><small><em><span class="redtext">v</span></em>HMML Privacy
							Policy</small></a> <br /> <a
						href="${pageContext.request.contextPath}/contact"><small>Contact
							<span class="redtext italicstext">v</span>HMML
					</small></a>
				</p>
			</div>
		</div>
	</div>
</footer>