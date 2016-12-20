<style  type="text/css">
a.descriptionFolioLink, a.descriptionFolioLink:visited {
	text-decoration: none;
	color: #428bca;;
}
a.descriptionFolioLink:hover {
	color: #72bc94;
	text-decoration: underline;
}
</style>

<div class="content">
	<div class="page-banner folio">				
		<div class="wrapper">
			<h3 class="section-title text-left">What You&#x2019;ll Find in Folio</h3>			
			<p>
				<span class="redtext italicstext">v</span>HMML Folio offers images of manuscript pages from various traditions across the globe and across the ages. The
				collection is shaped to highlight the changing history of scripts in these traditions. In other words, it is a sort of
				paleography album, but of a type rarely seen: it will soon be possible to find examples of Syriac, Latin, G&#x04d9;&#x02bf;&#x04d9;z, Arabic,
				Georgian, and other manuscripts in the same place. Each image has been annotated by an expert scholar to show the
				notable paleographic features and provide a brief historical and codicological description. There are also full
				transcriptions of each page and select bibliography so that you can pursue further research.
			</p>			
		</div>
	</div>
	
	<div class="container">
		<div class="row">
			<div class="col-lg-6">
				<div class="image-card">
					<div class="carousel-image active">
						<img src="${pageContext.request.contextPath}/image/FOLIO/CAROUSEL IMAGES/FOLIO 00002.jpg/full/425,/0/default.jpg"/>
						<div class="carousel-caption">						
							<a href="https://w3id.org/vhmml/folio/view/2"><button>VIEW IN FOLIO</button></a>
						</div>
					</div>
					<div class="carousel-image">
						<img src="${pageContext.request.contextPath}/image/FOLIO/CAROUSEL IMAGES/FOLIO 00006.jpg/full/425,/0/default.jpg">
						<div class="carousel-caption">						
							<a href="https://w3id.org/vhmml/folio/view/6"><button>VIEW IN FOLIO</button></a>
						</div>
					</div>
					<div class="carousel-image">
						<img src="${pageContext.request.contextPath}/image/FOLIO/CAROUSEL IMAGES/FOLIO 00015.jpg/full/425,/0/default.jpg">
						<div class="carousel-caption">						
							<a href="https://w3id.org/vhmml/folio/view/15"><button>VIEW IN FOLIO</button></a>
						</div>
					</div>
					<div class="carousel-image">
						<img src="${pageContext.request.contextPath}/image/FOLIO/CAROUSEL IMAGES/FOLIO 00022.jpg/full/425,/0/default.jpg">
						<div class="carousel-caption">						
							<a href="https://w3id.org/vhmml/folio/view/22"><button>VIEW IN FOLIO</button></a>
						</div>
					</div>
					<div class="carousel-image">
						<img src="${pageContext.request.contextPath}/image/FOLIO/CAROUSEL IMAGES/FOLIO 00029.jpg/full/425,/0/default.jpg">
						<div class="carousel-caption">						
							<a href="https://w3id.org/vhmml/folio/view/29"><button>VIEW IN FOLIO</button></a>
						</div>
					</div>
					<div class="carousel-image">
						<img src="${pageContext.request.contextPath}/image/FOLIO/CAROUSEL IMAGES/FOLIO 00030.jpg/full/425,/0/default.jpg">
						<div class="carousel-caption">						
							<a href="https://w3id.org/vhmml/folio/view/30"><button>VIEW IN FOLIO</button></a>
						</div>
					</div>					
				</div>
			</div>
			
			<div class="col-lg-6">
				<div class="card folio-card">
					<h4 class="section-title">Languages, Writing Systems &amp; Scripts</h4>
					<hr/>
					<p>
						Folio allows you to find images by language, writing system (e.g., the Roman alphabet), and script (style of writing).
					</p>
					<p>
						<label>Writing System:</label>
						 In Folio you can learn how a single writing system can be used for many languages, just as the 
						 Roman writing system is used for hundreds of languages today. For example, the Ethiopic writing system, 
						 devised to write the ancient G&#x04d9;&#x02bf;&#x04d9;z language, is now used for modern languages such as Amharic. Arabic 
						 letters are used for Persian, Urdu, and many other kinds of manuscripts. The Syriac writing system 
						 was used for manuscripts in the Syriac language as well as when Syriac Christians wrote in Arabic, 
						 a phenomenon known as Garshuni.
					</p>
					
					<p>
						<label>Script:</label>
						Every writing system has different expressions that vary geographically and historically. In <span class="redtext">v</span>HMML  
						<a href="${pageContext.request.contextPath}/school" class="descriptionFolioLink">School</a> you can learn the various scripts used in the principal writing systems. Scholars use differences between scripts to locate manuscripts in a particular time and place.				
					</p>				
				</div>			
			</div>	
		</div>		
		
		<div class="row">
			<div class="col-lg-12">
				<div class="card card-full folio-card">
					<h4 class="section-title">Using Reference, Lexicon and School</h4>
					<hr/>
					<div class="col-lg-12">
						<p>
							We suggest that you visit <span class="redtext">v</span>HMML <a href="${pageContext.request.contextPath}/reference" class="descriptionFolioLink">Reference</a> to find bibliography helpful for your research. <a href="${pageContext.request.contextPath}/reference" class="descriptionFolioLink">Reference</a> is always just one
							click away from Folio via the menu in the upper right corner of any Folio page. From the menu you can also visit <span class="redtext">v</span>HMML
							<a href="${pageContext.request.contextPath}/lexicon" class="descriptionFolioLink">Lexicon</a> for explanations of the terminology used by paleographers and codicologists. You can also try finding a term from
							<a href="${pageContext.request.contextPath}/lexicon" class="descriptionFolioLink">Lexicon</a> in Folio with the Keyword search. If you have paleographic ambitions, explore <span class="redtext">v</span>HMML <a href="${pageContext.request.contextPath}/school" class="descriptionFolioLink">School</a>, where you will find
							lessons and exercises in various manuscript traditions and scripts.
						</p>
					</div>					
					<div class="clearfix"></div>
				</div>
			</div>
		</div>		
	</div>
</div>

<script type="text/javascript">	
	var $carouselImages = $('div.image-card div.carousel-image');
	var $imageCard = $('div.image-card');
	
	$(function() {
		var $firstImage = $imageCard.children('div.carousel-image.active');
		
		preloadImage($firstImage.find('img').attr('src'), function() {
			$firstImage.fadeIn(800);
			setTimeout(changeImage, 5000);
		});		
	});
	
	function changeImage() {
		var $activeImage = $imageCard.find('div.carousel-image.active');
		
		$activeImage.fadeOut(800, function() {
			$activeImage.removeClass('active');
			var $nextImage = $activeImage.next('div.carousel-image');
			$nextImage = $nextImage.length ? $nextImage : $imageCard.children('div.carousel-image').first(); 
			$nextImage.addClass('active').fadeIn(800);
			setTimeout(changeImage, 5000);
		});
	}
</script>