<!DOCTYPE html>
<%-- by Paolo Ciccarese --%>
  <head>
	<meta name="layout" content="administrator-dashboard" />
	
    <g:javascript library="jquery" plugin="jquery"/>
    
    <script type="text/javascript">
	function setDefaultValue() {
		var eResults = document.getElementById('results');
		eResults.style.display="none"
		var eAjaxIcon = document.getElementById('ajaxIcon');
		eAjaxIcon.style.display="inline"
	}
    
	function addResults(response) {  
		var eAjaxIcon = document.getElementById('ajaxIcon');
		eAjaxIcon.style.display="none"

		var eContent = document.getElementById('content');
		while(eContent.firstChild) {
			eContent.removeChild(eContent.firstChild);
		}
			
		var eResults = document.getElementById('results');
		if (eResults.style.display=="none") eResults.style.display="block"

		for(var i=0; i< response.users.length; i++) {
			var eTr = document.createElement('tr');
			var eUsername = document.createElement('td');
			var eLink = document.createElement('a');
			eLink.href = "showUser/" + response.users[i].id;
			eLink.innerHTML = response.users[i].username;
			eUsername.appendChild(eLink);
			eTr.appendChild(eUsername);
			var displayName = ""
			if(response.users[i].displayName!=null && response.users[i].displayName.trim().length>0) {
				displayName = " ("+response.users[i].displayName+")";
			}
			var eName = document.createElement('td');
			eName.innerHTML = response.users[i].name + displayName;
			eTr.appendChild(eName);
			var eAdmin = document.createElement('td');
			eAdmin.innerHTML = response.users[i].isAdmin;
			eTr.appendChild(eAdmin);
			var eMgr = document.createElement('td');
			eMgr.innerHTML = response.users[i].isManager;
			eTr.appendChild(eMgr);
			var eUsr = document.createElement('td');
			eUsr.innerHTML = response.users[i].isUser;
			eTr.appendChild(eUsr);
			var eCreation = document.createElement('td');
			eCreation.innerHTML = response.users[i].dateCreated;
			eTr.appendChild(eCreation);
			
			var eStatus = document.createElement('td');
			eStatus.innerHTML = response.users[i].status;
			eTr.appendChild(eStatus);

			var eStatus = document.createElement('td');
			eStatus.innerHTML = "<a href=''>Enroll</a>";
			eTr.appendChild(eStatus);
			
			eContent.appendChild(eTr);
		}
	}
	</script>
  </head>

	<body>
		<div class="title">
						<img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'groups.png')}"/> Enroll Users in Group - id: ${group.id}
					</div>
		<div style="padding-top:5px; font-size: 15px;"> <img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'search.png')}" width="16px"/> Search users</div>
		<g:render plugin="cs-users"  template="/users/usersSearchForm" />
		<br/>
		<div style="padding-top:5px; font-size: 15px;"> <img style="display: inline; vertical-align: middle;" src="${resource(dir:'images/dashboard',file:'add_group.png')}"/>  Enroll users</div>
		<g:render plugin="cs-groups"  template="/groups/addUserToGroupResults" />
	</body>
</html>