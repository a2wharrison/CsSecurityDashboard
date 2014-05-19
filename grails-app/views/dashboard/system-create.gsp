<!DOCTYPE html>
<%-- by Paolo Ciccarese --%>

<html>
    <head>
		<meta name="layout" content="administrator-dashboard" /> 
		<title>Create System :: ${grailsApplication.config.af.shared.title}</title>
    </head>
	<body>
		<div class="title">System Creation </div>
		<sec:access expression="hasRole('ROLE_ADMIN')">
			<g:render plugin="cs-systems" template="/systems/systemCreate" />
		</sec:access>
		<sec:ifNotGranted roles="ROLE_ADMIN">
			<sec:access expression="hasRole('ROLE_MANAGER')">
				<g:render template="/manager/createSystem" />
			</sec:access>
		</sec:ifNotGranted>
		<sec:ifNotGranted roles="ROLE_ADMIN, ROLE_MANAGER">
			---
		</sec:ifNotGranted>
	</body>
</html>