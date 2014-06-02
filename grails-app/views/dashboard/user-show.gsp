<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>

<!doctype html>
<html>
	<head>
		<meta name="layout" content="administrator-dashboard"/>
		<title>${grailsApplication.metadata['app.name']}.${label}</title>
	</head>
	<body>
		<script type="text/javascript">
		  	$(document).ready(function() {
		  		var dataToSend = { id: '${user.id}' };
		  		$.ajax({
			  	  	url: "${appBaseUrl}/ajaxDashboard/userGroups",
			  	  	context: $("#groupsContent"),
			  	  	data: dataToSend
		  		})
		  	   .done(function(data){
		  			$("#groupsSpinner").css("display","none");
		  			var label = data.length == 1 ? data.length + ' Group' : data.length + ' Groups';
		  			$("#groupsTitle").html("<b>"+label+"</b>");
		  			$.each(data, function(i,item){
			  			var roles ="";
						for(var i=0; i<item.roles.length; i++) {
							roles+=item.roles[i].label
						}
		  				$('#groupsTable').append('<tr><td><a href="../showGroup/' + 
			  				item.group.id + '">' + item.group.name + '</a></td><td>' + 
			  				item.dateCreated + '</td><td>'+ roles +
			  				'</td><td> '+ item.status.label + '</td></tr>');
		  		    });
		  					  			
			  	})
			  	.fail(function( jqXHR, textStatus ) {
			  		$("#groupsSpinner").css("display","none");
			  		$("#groupsTitle").html("<b>! Failed to load the list of groups</b>");
			  		$("#userGroupsComponent").css("display","none");
				});
		  	});
		</script>
	
		<table>
			<tr>
				<td valign="top" width="400px">
					<div class="title">
						<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'user.png')}"/> User - id: ${user.id}
					</div>
					<g:form method="post" >
						<div class="csc-lens-container">
							<br/>
							<g:hiddenField name="id" value="${user.id}" /> 
							<g:render plugin="cs-users" template="/users/userShow" />
							<br/>
						</div>
						<div class="buttons">
							<span class="button">
								<g:actionSubmit class="edit" action="editUser" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Edit User')}" />
							</span>
							<span class="button">
								<g:actionSubmit class="list" action="listUsers" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'List Users')}" />
							</span>
						</div>
					</g:form>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<div class="title">
						<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'groups.png')}"/> Groups
					</div>
					<g:render template="/groups/ajaxShowUserGroups" />
				</td>
			</tr>
		</table>
	</body>
</html>