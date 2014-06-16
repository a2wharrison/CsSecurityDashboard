<!DOCTYPE html>
<%-- by Paolo Ciccarese --%>
<%@ page import="org.commonsemantics.grails.users.model.User" %>
<%@ page import="org.commonsemantics.grails.users.model.UserRole" %>

<html>
<head>
	<meta name="layout" content="administrator-dashboard" />
	<title>All Users List<g:if test="${role!=null}">(with Role: ${role.label})</g:if> - total# ${usersTotal} :: ${grailsApplication.config.af.shared.title}</title>
</head>
<body>
	<div class="title">
		<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'user.png')}"/> Users <g:if test="${role!=null}">(with Role: ${role.label})</g:if> List - total# ${usersTotal}
	</div>
	<sec:access expression="hasRole('ROLE_ADMIN')">
		<%-- <g:render template="/dashboard/administrator/listUsers" /> --%>
		<g:render template="/users/usersList" plugin="cs-users"/>
	</sec:access>
	<sec:ifNotGranted roles="ROLE_ADMIN">
		<sec:access expression="hasRole('ROLE_MANAGER')">
			---
		</sec:access>
	</sec:ifNotGranted>
	<sec:ifNotGranted roles="ROLE_ADMIN, ROLE_MANAGER">
		---
	</sec:ifNotGranted>
</body>
</html>