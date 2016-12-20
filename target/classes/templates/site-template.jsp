<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="vhmml" tagdir="/WEB-INF/tags" %>

<html lang="en-US">
	<head itemscope itemtype="http://schema.org/WebSite">
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<meta name="viewport" content="initial-scale=1"/>	
		
		<title itemprop="name">
			<tiles:insertAttribute name="title" />
		</title>
		<link rel="canonical" href="https://www.vhmml.org/" itemprop="url">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/static/img/favicon.ico">
		
		<link href="https://fonts.googleapis.com/css?family=Noto+Serif:400,700&amp;subset=latin,latin-ext,greek,greek-ext" rel="stylesheet" type="text/css">
		<link href="https://fonts.googleapis.com/css?family=Noto+Sans:400,700&amp;subset=latin,latin-ext,greek,greek-ext" rel="stylesheet" type="text/css">
		<link href="https://fonts.googleapis.com/css?family=Lato&amp;subset=latin,latin-ext,greek,greek-ext" rel="stylesheet"/>
		<link href="https://fonts.googleapis.com/css?family=Libre+Baskerville" rel="stylesheet">

		<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/jquery-ui.min.css"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/jquery-ui.theme.min.css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/static/fonts/vhmml/css/vhmml.css?version=${applicationVersion}"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css?version=${applicationVersion}"/>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery-1.12.1.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery-ui.min.js"></script>		
		<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/vhmml.js?version=${applicationVersion}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/validation.js?version=${applicationVersion}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/dialog.js?version=${applicationVersion}"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/messages.js?version=${applicationVersion}"></script>
				
		<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
		<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
		<!--[if lt IE 9]>
			<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
			<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
		<![endif]-->
	</head>
	
	<body ontouchstart="closeTooltipsOnTouch(event);">
		
		<div id="vhmmlMessages"></div>	
		
		<nav id="main-nav" class="main-nav">
		
			<c:if test="${configValues['hide.reading.room'].value == 'false'}">
		    	<a href="${pageContext.request.contextPath}/readingRoom">Reading Room</a>
		    </c:if>
			
			<a href="http://www.hmml.org/oliver.html" target="_blank">Legacy Catalog</a>
			
			<a href="${pageContext.request.contextPath}/school">School</a>
			
			<c:if test="${configValues['hide.folio'].value == 'false'}">
				<a href="${pageContext.request.contextPath}/folio">Folio</a>
			</c:if>
			
			<a href="${pageContext.request.contextPath}/lexicon">Lexicon</a>
		    <a href="${pageContext.request.contextPath}/reference">Reference</a>
			
			<%-- <a href="${pageContext.request.contextPath}/scriptorium">Scriptorium</a> --%>
					
			<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_LEVEL_I_CATALOGER,ROLE_LEVEL_II_CATALOGER,ROLE_LEVEL_III_CATALOGER">
				<a href="${pageContext.request.contextPath}/catalogDatabase">Catalog Database</a>
			</security:authorize>
			
			<security:authorize ifAnyGranted="ROLE_ADMIN">
				<a href="${pageContext.request.contextPath}/admin">Administration</a>
			</security:authorize>			
		</nav>
		
		<div class="container page-wrap">
			<tiles:insertAttribute name="header" />	
			<div class="main-content">
				<tiles:insertAttribute name="body" />
			</div>			
			<tiles:insertAttribute name="footer" />											
		</div>		
		
		<c:if test="${vhmmlEnvironment == 'prod'}">
			<script>
				(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function()
				{ (i[r].q=i[r].q||[]).push(arguments)}
				,i[r].l=1*new Date();a=s.createElement(o),
				m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
				})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
				ga('create', 'UA-56257562-1', 'auto');
				ga('send', 'pageview');
			</script>
		</c:if>
		
		<!-- 
			Bootstrap has to come after body content because it relies on jQuery and Mirador embeds a different version of jQuery, so Bootstrap 
			dialogs and such won't work on pages that use Mirador unless we put jQuery down here after Mirador has loaded jQuery 
		-->
		<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
		
		<!-- these includes have to come after the body content is inserted or the textinput control isn't available in the document ready function below -->
		<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery-ui-plugins-core.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery-ui-plugins-textinput.js"></script>
			
		<script type="text/javascript">
			
			var sessionTimeoutSeconds = ${pageContext.session.maxInactiveInterval};
			var scheme = '${pageContext.request.scheme}';
			
			<c:if test="${vhmmlEnvironment == 'prod' || vhmmlEnvironment == 'test'}">
				scheme = 'https';
			</c:if>
			var serverName = '${pageContext.request.serverName}';
			var serverPort = '${pageContext.request.serverPort}';
			var contextPath = '${pageContext.request.contextPath}';
			
			// all global messages includes messages the user has already seen adn closed, we need this list so the admin screen can show them
			var allGlobalMessages = '';					
			var pageMessages = '';
			var fieldValidationMessages = '';
			var isAuthenticated = false;
			
			<security:authorize ifAnyGranted="ROLE_ADMIN">
				<c:if test="${vhmmlGlobalMessages != null}">
					allGlobalMessages = JSON.parse('${vhmmlGlobalMessages}');
				</c:if>
			</security:authorize>
			
			<security:authorize access="isAuthenticated()">
				isAuthenticated = true;
			</security:authorize>
			
			<c:if test="${pageMessages != null}">
				pageMessages = JSON.parse('${pageMessages}');				
			</c:if>
			
			<c:if test="${fieldErrorMessages != null}">
				fieldValidationMessages = JSON.parse('${fieldErrorMessages}');
			</c:if>
			
			<c:if test="${popupMessage != null}">
				new Dialog().show(JSON.parse('${popupMessage}'));
			</c:if>
			
			$(function() {
				$('input.digitsOnly').textinput({'filter': 'digits'}); 
				$('input.integersOnly').textinput({'whitelist': '0123456789-'}); 
				$('input.positiveDecimalsOnly').textinput({'whitelist': '0123456789.'});
				
				if(isAuthenticated) {
					initSessionTimeout();
				}
			});
		</script>		
	</body>
</html>