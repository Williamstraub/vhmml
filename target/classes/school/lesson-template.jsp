<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/school.css?version=${applicationVersion}"/>

<script src="${pageContext.request.contextPath}/static/js/openseadragon/openseadragon.min.js"> </script>

<a href="#contentOfLesson" class="sr-only sr-only-focusable">Skip to main content</a>

<div id="contentOfLesson">
	<div class="row">    
		<div class="col-lg-9">
			<h2 class="lessonSubheading"><span class="school-underline"><tiles:insertAttribute name="lessonName" /></span></h2>       
			<h3 class="lessonSubheading"><tiles:insertAttribute name="sectionName" /></h3>
			<h4 class="sectionDescription"><tiles:insertAttribute name="sectionDescription" defaultValue=""/></h4>
		</div>
    
		<div class="col-lg-3">
			<c:if test="${not empty lessonIndex}">
				<div class="lessonIndexWrapper school">
					<label for="lessonIndex">Lesson Index</label>
					<div id="lessonIndex" class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
							<%-- selectedIndexItem is set in lesson-index-item.tag --%>
							<span class="selected"><tiles:insertAttribute name="selectedIndexItem" defaultValue=""/></span><span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">					
							<tiles:insertAttribute name="lessonIndex"/>
						</ul>					
					</div>
					<a href="${pageContext.request.contextPath}/school" class="allLessons"><span class="glyphicon glyphicon-share-alt flipHorizontal"></span>&nbsp;all lessons</a>
				</div>
			</c:if>			
		</div>
	</div>
	
	<tiles:insertAttribute name="lessonContent" />	
	
	<div class="row">
		<c:set var="previousSection">
			<tiles:insertAttribute name="previousSection" defaultValue=""/>
		</c:set>
		
		<c:set var="nextSection">
			<tiles:insertAttribute name="nextSection" defaultValue=""/>
		</c:set>
		
		<div class="col-lg-12">
			<c:if test="${previousSection != ''}">
				<a href="${pageContext.request.contextPath}/school/lesson/${lessonPath}/${previousSection}"><button class="btn school navButton"><span  class="glyphicon glyphicon-arrow-left"></span> Previous</button></a>
			</c:if>		
			
			<c:if test="${nextSection != ''}">
				<a href="${pageContext.request.contextPath}/school/lesson/${lessonPath}/${nextSection}"><button class="btn school navButton">Next <span class="glyphicon glyphicon-arrow-right"></span></button></a>
			</c:if>	
			
			<c:if test="${nextSection == ''}">
				<div class="row buttonRow">
					<a href="${pageContext.request.contextPath}/school">
						<button class="btn left school"><span class="glyphicon glyphicon-share-alt flipHorizontal"></span> Return to Lessons Menu</button>
					</a>					
				</div>
			</c:if>		
		</div>
	</div>
</div>

<div id="answerDialog" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="showAnswer" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h1 class="modal-title" id="myModalLabel">The correct answer is</h1>
			</div>
			<div class="modal-body"></div>
            <div class="modal-footer">
                <button type="button" class="btn button right school" data-dismiss="modal">Close</button>	               
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function() {
		
		var $answerDialog = $('#answerDialog');
		
		$('body').on('click', 'button.showAnswer', function() {
			/**
				Answer markup needs to go immediately after the button to show the answer
				and it can be in one of two forms:
				1) <span class="exerciseAnswer">Answer HTML here</span>
				2) <span class="exerciseAnswer">
				       <span class="answerTitle">Answer dialog title</span>
				       <span class="answer">Answer HTML here</span>
				   </span> 
			**/			
			
			var $answer = $(this).next('.exerciseAnswer');
			var title = $answer.find('.answerTitle').length ? $answer.find('.answerTitle').html() : 'The correct answer is'; 
			var answerHtml = $answer.find('.answer').length ? $answer.find('.answer').html() : $answer.html();
			$answerDialog.find('h1.modal-title').html(title);
			
			$answerDialog.find('div.modal-body').html(answerHtml);
			
			$answerDialog.find('.modal-content').on('mousemove', function(){ // Update containment each time it's dragged
			    $(this).draggable({
			        greedy: true, 
			        handle: '.modal-header',	
			        containment: // Set containment to current viewport
			        [
			         	$(document).scrollLeft(),
			         	$(document).scrollTop(),
			         	$(document).scrollLeft()+$(window).width()-$(this).outerWidth(),
			         	$(document).scrollTop()+$(window).height()-$(this).outerHeight()
			         ]
			    });
			}).draggable();						
						
			// always re-center the dialog since they can drag it way off screen if they get really crazy
			centerDialog($answerDialog);
			$answerDialog.modal({show: true, backdrop: false});
			$('div.modal-backdrop').css('opacity', 0);			
		});		
		
		$answerDialog.on('hidden.bs.modal', function (e) {
			centerDialog($answerDialog);
		});
	});		
	
	function centerDialog($dialog) {
		$dialog.css('top', '0px');
		$dialog.css('left', '0px');
	}
</script>