<div style="background: black;">
	<div class="container">
		<sec:access expression="hasRole('ROLE_ADMIN')">
			<div style="height: 20px; display: block; color: white;">
				<div id="adminBar">
					<p class="f_left">
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
						<img style="display: inline; vertical-align: middle;"
							src="${resource(dir:'images/dashboard',file:'pie.png')}" />
						<g:link controller="crunch" action="pulse">Pulse</g:link>
						&nbsp;&nbsp; <img style="display: inline; vertical-align: middle;"
							src="${resource(dir:'images/dashboard',file:'danger.png')}" />
						<g:link controller="crunch" action="index">Danger Zone</g:link>
					</p>
				</div>
			</div>
		</sec:access>
	</div>
</div>