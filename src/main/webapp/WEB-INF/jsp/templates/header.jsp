<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tiles:importAttribute name="pageCategory"/>

<header class="main-header" itemscope itemtype="http://schema.org/Organization">
	<a itemprop="url" href="${pageContext.request.contextPath}/" class="logo">	
		<img itemprop="logo" src="${pageContext.request.contextPath}/static/img/vhmml-logo.png" height="48" alt="Virtual H M M L">
	</a>	
	
	<div class="right-links">		
		
		<span class="sign-in-links">
			<security:authorize access="isAuthenticated()">
				<security:authentication property="principal.firstName" var="firstName"/>			
				<a href="#" class="account-settings-link" data-menu="account-menu">Welcome <c:out value="${firstName}"/>! <span class="caret"> </span></a>					
			</security:authorize>		
				
			<security:authorize access="!isAuthenticated()">		
				<a href="${pageContext.request.contextPath}/login" class="signIn">Sign In</a><span class="separator light-gray">|</span><a href="${pageContext.request.contextPath}/registration">Register</a>
			</security:authorize>
		</span>
			
		<span class="help-link light-gray">
			<a href="${pageHelpLink}" class="saveSearch"><span class="question-icon">?</span><span class="help-link-text">  Help</span></a>		
		</span>
	</div>	
	
	<a href="#main-nav" class="hamburger-menu open-menu">&#9776;</a>
	<a href="#" class="hamburger-menu close-menu">&#9776;</a>
	
</header>

<nav class="account-menu">
	<a href="${pageContext.request.contextPath}/user/accountSettings"><span class="glyphicon glyphicon-cog"></span>&nbsp;&nbsp;My Account</a>
	<a href="${pageContext.request.contextPath}/user/changePassword"><span class="glyphicon glyphicon-lock"></span>&nbsp;&nbsp;Change Password</a>
	<a href="${pageContext.request.contextPath}/logout"><span class="glyphicon glyphicon-log-out"></span>&nbsp;&nbsp;Sign Out</a>
</nav>

<c:set var="showBreadcrumbs">
	<tiles:insertAttribute name="showBreadcrumbs"/>
</c:set>

<c:if test="${showBreadcrumbs}">
	<div class="breadcrumbs light-gray">	
		<span class="wrapper">
		<!-- TODO: rename HelpLinkFilter the breadcrumbs variable is put on the request by the HelpLink filter -->
			<c:forEach var="breadcrumb" items="${breadcrumbs}" varStatus="loopStatus">			
				<c:set var="cssClass" value=""/>				
				<c:if test="${loopStatus.index > 0}">
					<c:set var="cssClass" value="${pageCategory}-link"/>
					<span> / </span>
				</c:if>
				
				<a href="${breadcrumb.value}" class="${cssClass}">${breadcrumb.key}</a>
			</c:forEach>			
		</span>
		
		<div class="breadcrumb-buttons">
			<tiles:insertAttribute name="breadcrumbButtons"/>
		</div>
	</div>
</c:if>