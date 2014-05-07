<!DOCTYPE html>
<%-- by Paolo Ciccarese --%>

<html>
  	<head>
    	<meta name="layout" content="administrator-dashboard" />
    	<title>All Systems List - total# ${systems.size()} :: ${grailsApplication.config.af.shared.title}</title>
  	</head>
  	<body>
  		<div class="title">
  			<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'computer.png',plugin:'users-module')}"/> 
  				System List - total# ${systems.size()}
  		</div>
  		<sec:access expression="hasRole('ROLE_ADMIN')">
			<%--<g:render template="/systems/systemsList" plugin="cs-system"/>--%>
			<%--<g:render template="/administrator/listSystems" /> --%>
		</sec:access>
		<sec:ifNotGranted roles="ROLE_ADMIN">
			<sec:access expression="hasRole('ROLE_MANAGER')">
				<g:render template="/manager/listSystems" />
			</sec:access>
		</sec:ifNotGranted>
		<sec:ifNotGranted roles="ROLE_ADMIN, ROLE_MANAGER">
			---
		</sec:ifNotGranted>
  	</body>
</html>