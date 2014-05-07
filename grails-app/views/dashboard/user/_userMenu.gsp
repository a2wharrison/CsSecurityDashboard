<div id="navcontainer">
<h2>User</h2>
<h3>Manage Users</h3>
<ul id="navlist">
	<g:if test="${menuitem!=null && menuitem=='searchUser'}">
		<li class="active"><g:link controller="dashboard" action="searchUser">Search Users</g:link></li>
	</g:if>
	<g:else><li><g:link controller="dashboard" action="searchUser">Search Users</g:link></li></g:else>
	<g:if test="${menuitem!=null && menuitem=='createUser'}">
		<li class="active"><g:link controller="dashboard" action="createUser">Invite User</g:link></li>
	</g:if>
	<g:else><li><g:link controller="dashboard" action="createUser">Invite User</g:link></li></g:else>
</ul>
<h3>Manage Groups</h3>
<ul id="navlist">
	<g:if test="${menuitem!=null && menuitem=='searchGroups'}">
		<li class="active"><g:link controller="administrator" action="searchGroup">Search Groups</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="searchGroup">Search Groups</g:link></li></g:else>
	<g:if test="${menuitem!=null && menuitem=='createGroup'}">
		<li class="active"><g:link controller="administrator" action="createGroup">Create Group</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="createGroup">Create Group</g:link></li></g:else>
</ul>

<h3>Manage Systems</h3>
<ul id="navlist">
	<g:if test="${menuitem!=null && menuitem=='listSystems'}">
		<li class="active"><g:link controller="administrator" action="listSystems">List Systems</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="listSystems">List Systems</g:link></li></g:else>
	<g:if test="${menuitem!=null && menuitem=='searchSystems'}">
		<li class="active"><g:link controller="administrator" action="searchSystem">Search Systems</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="searchSystem">Search Systems</g:link></li></g:else>
	<g:if test="${menuitem!=null && menuitem=='createSystem'}">
		<li class="active"><g:link controller="administrator" action="createSystem">Create System</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="createSystem">Create System</g:link></li></g:else>
</ul>

<h3>Moderation Queues</h3>
<ul id="navlist">
	<g:if test="${menuitem!=null && menuitem=='pastUserAccountRequests'}">
		<li class="active"><g:link controller="userAccuntRequest" action="pastAccountsRequests">Past Users Account Requests</g:link></li>
	</g:if>
	<g:else>
		<li><g:link controller="userAccuntRequest" action="pastAccountsRequests">Past Users Account Requests</g:link></li>
	</g:else>
	<g:if test="${menuitem!=null && menuitem=='moderateUserAccountRequests'}">
		<li class="active"><g:link controller="userAccuntRequest" action="moderateUserAccountsRequests">Moderate Users Account Requests</g:link></li>
	</g:if>
	<g:else>
		<li><g:link controller="userAccuntRequest" action="moderateUserAccountsRequests">Moderate Account Requests</g:link></li>
	</g:else>
	<%--<li><g:link controller="adminDashboard" action="activateUser">Groups Requests</g:link>--%></li>
</ul>
<%-- 
<h3>Manage Users</h3>
<ul id="navlist">
	<g:if test="${menuitem!=null && menuitem=='listUsers'}">
		<li class="active"><g:link controller="administrator" action="listUsers">List Users</g:link></li>
	</g:if>
	<g:else>
		<li><g:link controller="administrator" action="listUsers">List Users</g:link></li>
	</g:else>
	<g:if test="${menuitem!=null && menuitem=='searchUser'}">
		<li class="active"><g:link controller="administrator" action="searchUser">Search Users</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="searchUser">Search Users</g:link></li></g:else>
	<g:if test="${menuitem!=null && menuitem=='createUser'}">
		<li class="active"><g:link controller="administrator" action="createUser">Create User</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="createUser">Create User</g:link></li></g:else>
</ul>

<h3>Manage Groups</h3>
<ul id="navlist">
	<g:if test="${menuitem!=null && menuitem=='listGroups'}">
		<li class="active"><g:link controller="administrator" action="listGroups">List Groups</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="listGroups">List Groups</g:link></li></g:else>
	<g:if test="${menuitem!=null && menuitem=='searchGroups'}">
		<li class="active"><g:link controller="administrator" action="searchGroup">Search Groups</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="searchGroup">Search Groups</g:link></li></g:else>
	<g:if test="${menuitem!=null && menuitem=='createGroup'}">
		<li class="active"><g:link controller="administrator" action="createGroup">Create Group</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="createGroup">Create Group</g:link></li></g:else>
</ul>

<h3>Manage Systems</h3>
<ul id="navlist">
	<g:if test="${menuitem!=null && menuitem=='listSystems'}">
		<li class="active"><g:link controller="administrator" action="listSystems">List Systems</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="listSystems">List Systems</g:link></li></g:else>
	<g:if test="${menuitem!=null && menuitem=='searchSystems'}">
		<li class="active"><g:link controller="administrator" action="searchSystem">Search Systems</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="searchSystem">Search Systems</g:link></li></g:else>
	<g:if test="${menuitem!=null && menuitem=='createSystem'}">
		<li class="active"><g:link controller="administrator" action="createSystem">Create System</g:link></li>
	</g:if>
	<g:else><li><g:link controller="administrator" action="createSystem">Create System</g:link></li></g:else>
</ul>
--%>
<%--
<h3>Moderation Queue</h3>
<ul id="navlist">
	<li><g:link controller="adminDashboard" action="pastAccountsRequests">Past Account Requests</g:link></li>
	<li><g:link controller="adminDashboard" action="moderateAccountsRequests">Moderate Account Requests</g:link></li>
	<li><g:link controller="adminDashboard" action="activateUser">Groups Requests</g:link></li>
</ul>

<h3>Data dump</h3>
<ul id="navlist">
<li><g:link controller="dump" action="dumpAnnotation">Dump annotations</g:link></li>
<li><g:link controller="dump" action="dumpLastVersionAnnotation">Dump last versions</g:link></li>
</ul>

<h3>Node Management</h3>
<ul id="navlist">
<li><g:link controller="adminDashboard" action="generalProperties">General Properties</g:link></li>
<li><g:link controller="adminDashboard" action="sendEmails">Global notification service</g:link></li>
</ul>

<br/>
<h3>Circles Management</h3>
<ul id="navlist">
<li id="active"><a href="#" id="current">List Circles</a></li>
<li><a href="#">Find Circles</a></li>
<li><a href="#">Circles Network</a></li>
</ul>
<br/>
<h3>Event Management</h3>
<ul id="navlist">
<li id="active"><a href="#" id="current">List Events</a></li>
<li><a href="#">Find Events</a></li>
<li><a href="#">Create Event</a></li>
<li><a href="#">Events Timeline</a></li>
</ul>
<br/>
<h3>Nodes Management</h3>
<ul id="navlist">
<li><a href="#">Node Info</a></li>
<li id="active"><a href="#" id="current">List Nodes</a></li>
<li><a href="#">Nodes Network</a></li>
</ul>
<br/>
<h3>Statistics</h3>
<ul id="navlist">
<li><a href="#">Users Usage</a></li>
<li><a href="#">Annotated Resources</a></li>
<li><a href="#">Annotations Items</a></li>
</ul>

<br/>
<h3>Export Management</h3>
<ul id="navlist">
<li><a href="#">Export all</a></li>
</ul>
--%>
<br/>
<br/>
</div>