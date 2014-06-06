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
				<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'groups.png')}"/> Edit Group - id: ${group.id}
			</div>
			
			<g:form method="post" >
				<div class="csc-lens-container">
					<g:render plugin="cs-groups" template="/groups/groupEdit" />
					<br/>
				</div>
				<tr>
					<td valign="top" colspan="2" >
						<div class="buttons">
							<span class="button">
								<g:actionSubmit class="save" action="updateGroup" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Update Group')}" />
							</span>
							<span class="button">
								<g:actionSubmit class="disable" action="showGroup" value="${message(code: 'org.commonsemantics.grails.general.cancel', default: 'Cancel')}" />
							</span>
						</div>
					</td>
				</tr>
			</g:form>
		</div>
	</body>
</html>