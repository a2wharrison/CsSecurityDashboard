<!DOCTYPE html>
<%-- by Paolo Ciccarese --%>

<html>
    <head>
		<meta name="layout" content="administrator-dashboard" /> 
		<title>Create Group :: ${grailsApplication.config.af.shared.title}</title>
    </head>
	<body>
		<div class="title">Group Creation </div>
		<sec:access expression="hasRole('ROLE_ADMIN')">
			<g:render template="/groups/groupCreate" plugin="cs-group"/>
			<%-- <g:render template="/administrator/createGroup" /> --%>
		</sec:access>
		<sec:ifNotGranted roles="ROLE_ADMIN">
			<sec:access expression="hasRole('ROLE_MANAGER')">
				<g:render template="/manager/createGroup" />
			</sec:access>
		</sec:ifNotGranted>
		<sec:ifNotGranted roles="ROLE_ADMIN, ROLE_MANAGER">
			---
		</sec:ifNotGranted>
	</body>
</html>