<div id="carousel" class="carousel slide" data-ride="carousel">
	<ol class="carousel-indicators">
		<li data-target="#carousel" data-slide-to="0" class="active"></li>
		<li data-target="#carousel" data-slide-to="1"></li>
		<li data-target="#carousel" data-slide-to="2"></li>
		<li data-target="#carousel" data-slide-to="3"></li>
		<li data-target="#carousel" data-slide-to="4"></li>
		<li data-target="#carousel" data-slide-to="5"></li>
		<li data-target="#carousel" data-slide-to="6"></li>
		<li data-target="#carousel" data-slide-to="7"></li>
	</ol>
 
	<div class="carousel-inner responsive" role="listbox">	
	
		<div class="item active">
			<img src="${pageContext.request.contextPath}/static/img/placeholder.jpg">
			<div class="carousel-caption">
				<h3 class="section-title">Image1<br /><br /> description of image one</h3>
				<a href=""><button>VIEW IN READING ROOM</button></a>
			</div>
		</div>

		<div class="item">
			<img src="${pageContext.request.contextPath}/static/img/placeholder.jpg">
			<div class="carousel-caption">
				<h3 class="section-title">image number two<br /><br /> </h3>
				<a href="https://w3id.org/vhmml/readingRoom/"><button>VIEW IN READING ROOM</button></a>
			</div>						
		</div>

		<div class="item">
			<img src="${pageContext.request.contextPath}/static/img/placeholder.jpg">
			<div class="carousel-caption">
				<h3 class="section-title">Image4<br /><br /> image description</h3>
				<a href="https://w3id.org/vhmml/readingRoom/view/"><button>VIEW IN READING ROOM</button></a>
			</div>			
		</div>
		
		<div class="item">
			<img src="${pageContext.request.contextPath}/static/img/placeholder.jpg">
			<div class="carousel-caption">
				<h3 class="section-title">image5<br /><br /></h3>
				<a href="https://w3id.org/vhmml/readingRoom/view/"><button>VIEW IN READING ROOM</button></a>
			</div>									
		</div>		
		
		<div class="item">
			<img src="${pageContext.request.contextPath}/static/img/placeholder.jpg">
			<div class="carousel-caption">
				<h3 class="section-title">image6<br /><br /> image six description</h3>
				<a href="https://w3id.org/vhmml/readingRoom"><button>COMING SOON TO READING ROOM</button></a>
			</div>		
		</div>
		
		<div class="item">
			<img src="${pageContext.request.contextPath}/static/img/placeholder.jpg">
			<div class="carousel-caption">
				<h3 class="section-title">image<br /><br /> image description</h3>
				<a href="https://w3id.org/vhmml/readingRoom/view/"><button>VIEW IN READING ROOM</button></a>
			</div>						
		</div>
		
		<div class="item">
			<img src="${pageContext.request.contextPath}/static/img/placeholder.jpg">
			<div class="carousel-caption">
				<h3 class="section-title"><br /><br /></h3>
				<a href="https://w3id.org/vhmml/readingRoom"><button>COMING SOON TO READING ROOM</button></a>
			</div>		
		</div>
		
		<div class="item">
			<img src="${pageContext.request.contextPath}/static/img/placeholder.jpg">
			<div class="carousel-caption">
				<h3 class="section-title">image #<br /><br /> description</h3>
				<a href="https://w3id.org/vhmml/readingRoom"><button>COMING SOON TO READING ROOM</button></a>
			</div>		
		</div>
	</div>

	<a class="left carousel-control" href="#carousel" role="button" data-slide="prev">
		<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
		<span class="sr-only">Previous</span>
	</a>
	<a class="right carousel-control" href="#carousel" role="button" data-slide="next">
		<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
		<span class="sr-only">Next</span>
	</a>
</div>

<script type="text/javascript">
	var $carousel;
	var cycling = true;
	var $arrows = $('.carousel-control .glyphicon');
	
	$(function() {
		$carousel = $('#carousel');
		
		$carousel.on('click', 'div.item, .carousel-indicators li', function() {
			toggleCarousel();
		})
		
		$('.carousel-control').hover(
			function() {
				$arrows.show();
			},
			function() {
				$arrows.hide();
			}
		);
	});
	
	function toggleCarousel() {
		if(cycling) {			
			$carousel.carousel('pause');
			cycling = false;
		} else {
			$carousel.carousel('cycle');
			cycling = true;
		}
	}
</script>