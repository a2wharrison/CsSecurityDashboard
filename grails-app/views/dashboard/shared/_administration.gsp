<div class="wrapper col0">
 <div id="adminBar">
     <p class="f_left">
     	<g:link controller="main" action="index">Home :: </g:link>
    	<g:link controller="dashboard" action="index">
			<sec:access expression="hasRole('ROLE_ADMIN')">
				Administration Dashboard 
			</sec:access>
			<sec:ifNotGranted roles="ROLE_ADMIN">
				<sec:access expression="hasRole('ROLE_MANAGER')">
					Manager Dashboard
				</sec:access>
			</sec:ifNotGranted>
			<sec:ifNotGranted roles="ROLE_ADMIN, ROLE_MANAGER">
				User Dashboard
			</sec:ifNotGranted>
		</g:link>
  		<%-- 
  		<sec:ifAllGranted roles="ROLE_ADMIN,ROLE_MANAGER">
  		::
  		</sec:ifAllGranted>
  		<sec:ifAnyGranted roles="ROLE_MANAGER">
  			<a href="#">Manager Dashboard </a>
  		</sec:ifAnyGranted>
  		--%>
  	</p>
  	 <p class="f_right">
  		<a href="#">My Profile</a> :: <g:link controller="logout" action="index">Logout</g:link>
  	</p>
  </div>
</div>