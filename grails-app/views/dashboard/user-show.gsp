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
				<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'user.png')}"/> User - id: ${user.id}
			</div>
			<g:form method="post" >
				<div class="csc-lens-container">
					<g:hiddenField name="id" value="${user.id}" /> 
					<g:render plugin="cs-users" template="/users/userShow" />
				</div>
				<br/>
				<div class="buttons">
					<span class="button">
						<g:actionSubmit class="edit" action="editUser" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Edit User')}" />
					</span>
					<span class="button">
						<g:actionSubmit class="list" action="listUsers" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'List Users')}" />
					</span>
				</div>
			</g:form>
		</div>
	</body>
</html>