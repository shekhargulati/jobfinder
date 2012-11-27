<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@ page session="false"%>

<p>
	Welcome,<c:out value="${account.firstName}" />!
</p>

<p class="lead">Recommended Jobs</p>
<table class="table table-striped" id="recommendedJobs" title="Recommended Jobs">
	<tr>
		<th>Job Title</th>
		<th>Company Name</th>
		<th>Skills</th>
		<th>Address</th>
		<th>Distance</th>
		<th>Duration</th>
	</tr>
	<c:forEach items="${recommendedJobs}" var="recommendedJob">
	<tr>
		<td>${recommendedJob.jobTitle}</td>
		<td>${recommendedJob.companyName}</td>
		<td><c:forEach items="${recommendedJob.skills}" var="skill">
		<dd><c:out value="${skill}"/></dd>
	</c:forEach></td>
		<td>${recommendedJob.address}</td>
		<td>${recommendedJob.distance}</td>
		<td>${recommendedJob.duration}</td>
		<td>
		<form id="applyJob" action="jobs/apply/${recommendedJob.jobId}" method="POST">
			<button class="btn btn-large btn-success" type="submit">Apply Job</button>
		</form>
		</td>
	</tr>

	</c:forEach>
</table>

<hr>

<p class="lead">Jobs Applied By You</p>
<table class="table table-striped" id="appliedJobs" title="Applied Jobs">
	<tr>
		<th>Job Title</th>
		<th>Company Name</th>
		<th>Skills</th>
		<th>Address</th>
		<th>Distance</th>
		<th>Duration</th>
	</tr>
	<c:forEach items="${appliedJobs}" var="appliedJob">
	<tr>
		<td>${appliedJob.jobTitle}</td>
		<td>${appliedJob.companyName}</td>
		<td><c:forEach items="${appliedJob.skills}" var="skill">
		<dd><c:out value="${skill}"/></dd>
	</c:forEach></td>
		<td>${appliedJob.address}</td>
		<td>${appliedJob.distance}</td>
		<td>${appliedJob.duration}</td>
	</tr>

	</c:forEach>
</table>

<hr>
