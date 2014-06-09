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
						<g:link class="edit" controller="dashboard" action="editSystem" id="${system.id}" style="text-decoration: none;">${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Edit System')}</g:link>
					</span>
					<%-- 
					<span class="button">
						<g:actionSubmit class="edit" action="editSystem" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Edit System')}" />
					</span>
					--%>
					
					<%-- 
					<span class="button">
						<g:actionSubmit class="list" action="listSystems" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'List Systems')}" />
					</span>
					<span class="button">
						<g:actionSubmit class="edit" action="showSystem" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Manage Administrators')}" />
					</span>
					--%>
					<span class="button">
						<g:link class="edit" controller="dashboard" action="manageSystemAdministrators" id="${system.id}" style="text-decoration: none;">${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Manage Administrators')}</g:link>
					</span>
					<g:if test="${system.enabled!=true}">
						<span class="button">
							<g:link class="enable" controller="dashboard" action="enableSystem" id="${system.id}" style="text-decoration: none;">${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Enable')}</g:link>
						</span>
					</g:if>
					<g:elseif test="${system.enabled==true}">
						<span class="button">
							<g:link class="disable" controller="dashboard" action="disableSystem" id="${system.id}" style="text-decoration: none;" 
								onclick="return confirm('${message(code: 'default.button.disable.account.confirm.message', default: 'Are you sure you want to disable the system: '+system.shortName+' ?')}');" >${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Disable')}</g:link>	
						</span>
					</g:elseif>
				
						<span class="button">
						<g:link class="delete" controller="dashboard" action="deleteSystem" id="${system.id}" style="text-decoration: none;" 
							onclick="return confirm('${message(code: 'default.button.disable.account.confirm.message', default: 'Are you sure you want to delate the system: '+system.shortName+' ?')}');" >${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Delete')}</g:link>	
					</span>
					
					<span class="button">
						<span class="button">
							<g:link class="reload" controller="dashboard" action="regenerateSystemKey" id="${system.id}" style="text-decoration: none;">${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Regenerate API Key')}</g:link>
						</span>
					</span>
					<g:if test="${grailsApplication.config.org.commonsemantics.grails.systems.model.field.secretkey!='hide'}">
						<span class="button">
							<g:link class="reload" controller="dashboard" action="regenerateSystemSecretKey" id="${system.id}" style="text-decoration: none;">${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Regenerate Secret Key')}</g:link>
						</span>
					</g:if>
				</div>
			</g:form>
		</div>
	</body>
</html>
