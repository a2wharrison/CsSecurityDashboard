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
				<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'computer.png')}"/> System - id: ${system.id}
			</div>
		
			<g:form method="post" >
				<div class="csc-lens-container">
					<br/>
					<g:hiddenField name="id" value="${system.id}" /> 
					<g:render plugin="cs-groups" template="/systems/systemShow" />
					<br/>
				</div>
				<div class="buttons">
					<span class="button">
						<g:actionSubmit class="edit" action="editSystem" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Edit System')}" />
					</span>
					<span class="button">
						<g:actionSubmit class="list" action="listSystems" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'List Systems')}" />
					</span>
					<span class="button">
						<g:actionSubmit class="reload" action="regenerateSystemKey" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Regenerate API Key')}" />
					</span>
					<g:if test="${grailsApplication.config.org.commonsemantics.grails.systems.model.field.secretkey!='hide'}">
						<span class="button">
							<g:actionSubmit class="reload" action="regenerateSystemSecretKey" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Regenerate Secret Key')}" />
						</span>
					</g:if>
				</div>
			</g:form>
		</div>
	</body>
</html>