<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>

<h3>Your Profile</h3>


<p>Hello, <c:out value="${profile.account.fullName}"/>!</p>
<img class="img-polaroid" src="<c:out value="${profile.linkedInProfile.profilePictureUrl}"/>"/>
<address>
<c:out value="${profile.account.address}"/>
</address>

<c:if test="${isConnected}">
<dl class="dl-horizontal">
	<dt>LinkedIn ID:</dt>
	<dd><a href="<c:out value="${profile.linkedInProfile.publicProfileUrl}"/>" target="_blank"><c:out value="${profile.linkedInProfile.id}"/></a></dd>
	<dt>Headline:</dt>
	<dd><c:out value="${profile.linkedInProfile.headline}"/></dd>
	<dt>Industry:</dt>
	<dd><c:out value="${profile.linkedInProfile.industry}"/></dd>
	<dt>Summary:</dt>
	<dd><c:out value="${profile.linkedInProfile.summary}"/></dd>
</dl>

<c:url value="/connect/linkedin" var="disconnectUrl"/>
<form id="disconnect" action="${disconnectUrl}" method="post">
	<button class="btn btn-large btn-success" type="submit">Disconnect from LinkedIn</button>	
	<input type="hidden" name="_method" value="delete" />
</form>
</c:if>

<c:if test="${empty isConnected}">
<h3>Connect to LinkedIn</h3>

<form action="<c:url value="/connect/linkedin" />" method="POST">
	<div class="formInfo">
		<p>
			You haven't created any connections with LinkedIn yet. Click the button to connect LocalJobs with your LinkedIn account. 
			(You'll be redirected to LinkedIn where you'll be asked to authorize the connection.)
		</p>
	</div>
	<p><button class="btn btn-primary" type="submit">Connect with LinkedIn</button></p>
</form>
</c:if>
