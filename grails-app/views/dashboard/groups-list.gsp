<!DOCTYPE html>
<%-- by Paolo Ciccarese --%>

<html>
  	<head>
    	<meta name="layout" content="administrator-dashboard" />
    	<title>All Groups List - - total# ${groups.size()} :: ${grailsApplication.config.af.shared.title}</title>
  	</head>
  	<body>
  		<div class="title">
  			<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'groups.png',plugin:'af-security')}"/> 
  				Groups List - total# ${groups.size()}
  		</div>
		<sec:access expression="hasRole('ROLE_ADMIN')">
			<g:render template="/groups/groupsList" plugin="cs-groups"/>
		</sec:access>
		<sec:ifNotGranted roles="ROLE_ADMIN">
			<sec:access expression="hasRole('ROLE_MANAGER')">
				<g:render template="/groups/groupsList" />
			</sec:access>
		</sec:ifNotGranted>
		<sec:ifNotGranted roles="ROLE_ADMIN, ROLE_MANAGER">
			---
		</sec:ifNotGranted>
  	</body>
</html>