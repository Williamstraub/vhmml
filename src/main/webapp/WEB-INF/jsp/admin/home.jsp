<!-- 
	applicationName & applicationVersion come from the manifest file, they are inserted into the manifest by Jenkins
	as part of the build process. The values from the manifest are put on the servletContext in AppStartupListener 
-->
<h1><span class="redtext">v</span>HMML System Administration</h1>
<h3>Version: ${applicationVersion}</h3>
<h3>Active Sessions: ${activeSessions}</h3>