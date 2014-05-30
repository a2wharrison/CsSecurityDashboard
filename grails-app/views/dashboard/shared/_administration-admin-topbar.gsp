<div class="wrapper col0">
	<div id="adminBar" style="font-size:14px;">
		<p class="f_left">
			<img style="display: inline; vertical-align: middle;" width="16px"
				src="${resource(dir:'images/dashboard',file:'left-grey.png')}" />
			<g:link controller="secure" action="index">Home</g:link>
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
			<img style="display:inline;" src="${resource(dir:'images/dashboard',file:'exit.png')}" title="Logout"  width="16px" />
			<g:link controller="logout" action="index">Logout</g:link>
		</p>
	</div>
</div>