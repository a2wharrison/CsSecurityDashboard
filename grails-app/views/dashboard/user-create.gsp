<!DOCTYPE html>
<%-- by Paolo Ciccarese --%>

<html>
    <head>
		<meta name="layout" content="administrator-dashboard" /> 
		<title>Create user :: ${grailsApplication.config.af.shared.title}</title>
    </head>
	<body>
		<div class="title">User Creation </div>
		<g:form method="post" >
			<g:render template="/users/userCreate" plugin="cs-user"/>
			<br/>
			<tr>
				<td valign="top" colspan="2" >
					<div class="buttons">
						<span class="button">
							<g:actionSubmit class="save" action="saveUser" value="${message(code: 'org.commonsemantics.grails.users.profile.create', default: 'Create User')}" />
						</span>
						<span class="button">
							<g:actionSubmit class="cancel" action="showUser" value="${message(code: 'org.commonsemantics.grails.general.cancel', default: 'Cancel')}" />
						</span>
					</div>
				</td>
			</tr>
		</g:form>
	</body>
</html>