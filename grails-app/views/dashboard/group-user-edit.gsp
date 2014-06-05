<!DOCTYPE html>
<%-- by Paolo Ciccarese --%>
  <head>
	<meta name="layout" content="administrator-dashboard" />
	
    <g:javascript library="jquery" plugin="jquery"/>

  </head>

	<body>
		<table>
			<tr>
				<td valign="top" width="400px">
					<div class="title">
						<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'user.png')}"/> User - id: ${usergroup.user.id}
					</div>
					<g:form method="post" >
						<div class="csc-lens-container">
							<br/>
							<g:hiddenField name="id" value="${usergroup.group.id}" /> 
							<g:hiddenField name="user" value="${usergroup.user.id}" /> 
							<g:render plugin="cs-groups"  template="/groups/groupUserEdit" />
							<br/>
						</div>
						<div class="buttons">
							<span class="button">
								<g:actionSubmit class="edit" action="updateUserInGroup" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Update User')}" />
							</span>
							<span class="button">
								<g:actionSubmit class="list" action="listGroupUsers" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'List Users')}" />
							</span>
						</div>
					</g:form>
				</td>
			</tr>
		</table>
	</body>
</html>