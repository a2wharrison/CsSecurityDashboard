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
				<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'groups.png')}"/> Edit System - id: ${system.id}
			</div>
			
			<g:form method="post" >
				<div class="csc-lens-container">
					<g:render plugin="cs-systems" template="/systems/systemEditFields" />
				</div>
				<tr>
					<td valign="top" colspan="2" >
						<div class="buttons">
							<span class="button">
								<g:actionSubmit class="save" action="updateSystem" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Update System')}" />
							</span>
							<%-- 
							<span class="button">
								<g:actionSubmit class="cancel" action="showSystem" value="${message(code: 'org.commonsemantics.grails.general.cancel', default: 'Cancel')}" />
							</span>
							--%>
							<span class="button">
								<g:link class="cancel" controller="dashboard" action="showSystem" id="${system.id}" style="text-decoration: none;">${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Cancel')}</g:link>
							</span>
						</div>
					</td>
				</tr>
			</g:form>
		</div>
	</body>
</html>