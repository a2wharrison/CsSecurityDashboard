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
						<g:actionSubmit class="edit" action="editGroup" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Edit Group')}" />
					</span>
					<span class="button">
						<g:actionSubmit class="list" action="listGroups" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'List Groups')}" />
					</span>
				</div>
			</g:form>
		</div>
	</body>
</html>