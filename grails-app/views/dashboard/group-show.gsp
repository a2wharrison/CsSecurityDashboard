<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>

<!doctype html>
<html>
	<head>
		<meta name="layout" content="administrator-dashboard"/>
		<title>${grailsApplication.metadata['app.name']}.${label}</title>
	</head>
	<body>
		<div class="csc-main">
			<div class="title">
				<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'groups.png')}"/> Group - id: ${group.id}
			</div>
		
			<g:form method="post" >
				<div class="csc-lens-container">
					<br/>
					<g:hiddenField name="id" value="${group.id}" /> 
					<g:render plugin="cs-groups" template="/groups/groupShow" />
					<br/>
				</div>
				<div class="buttons">
					<span class="button">
						<g:link class="edit" controller="dashboard" action="editGroup" id="${group.id}" style="text-decoration: none;">${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Edit Group')}</g:link>
					</span>
					<span class="button">
						<g:link class="list" controller="dashboard" action="listGroups" id="${group.id}" style="text-decoration: none;">${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'List Groups')}</g:link>
					</span>
					<span class="button">
						<g:link class="add" controller="dashboard" action="enrollUsersInGroup" id="${group.id}" style="text-decoration: none;">Enroll More Users</g:link>
					</span>
				</div>
			</g:form>
		</div>
	</body>
</html>