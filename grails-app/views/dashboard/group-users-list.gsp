<!DOCTYPE html>
<%-- by Paolo Ciccarese --%>

<html>
  	<head>
    	<meta name="layout" content="administrator-dashboard" />
    	<title>Users for Group</title>
  	</head>
  	<body>
  		<div class="title">
  			<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'groups.png',plugin:'af-security')}"/> 
  				Users for Group '${group.name}' - total# ${usersTotal}
  		</div>
  		<br/>
  		<%--
  		<h3>Group</h3>
  		<div class="csc-lens-container">
			<g:render plugin="cs-groups" template="/groups/groupShow" />
		</div>
		<br/>
		<h3>Group users (${userGroups.size()})</h3>
		 --%>
		<g:render plugin="cs-groups" template="/groups/groupUsersList" />	
  	</body>
</html>