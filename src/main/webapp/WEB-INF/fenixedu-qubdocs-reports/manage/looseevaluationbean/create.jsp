<%--
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: anil.mamede@qub-it.com
 *
 * 
 * This file is part of FenixEdu QubDocs.
 *
 * FenixEdu QubDocs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu QubDocs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu QubDocs.  If not, see <http://www.gnu.org/licenses/>.
 --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<spring:url var="datatablesUrl"
	value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js" />
<spring:url var="datatablesBootstrapJsUrl"
	value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl"
	value="/CSS/dataTables/dataTables.bootstrap.min.css" />
<link rel="stylesheet" href="${datatablesCssUrl}" />
<spring:url var="datatablesI18NUrl"
	value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json" />

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css" />

<link
	href="//cdn.datatables.net/responsive/1.0.4/css/dataTables.responsive.css"
	rel="stylesheet" />
<script
	src="//cdn.datatables.net/responsive/1.0.4/js/dataTables.responsive.js"></script>
<link
	href="//cdn.datatables.net/tabletools/2.2.3/css/dataTables.tableTools.css"
	rel="stylesheet" />
<script
	src="//cdn.datatables.net/tabletools/2.2.3/js/dataTables.tableTools.min.js"></script>
<link
	href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/css/select2.min.css"
	rel="stylesheet" />
<script
	src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/js/select2.min.js"></script>

${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message code="label.manage.createLooseEvaluationBean" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<a class=""
		href="${pageContext.request.contextPath}/looseevaluationbean/${studentCurricularPlan.externalId}"><span
		class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<spring:message
			code="label.event.back" /></a> |&nbsp;&nbsp;
</div>
<c:if test="${not empty infoMessages}">
	<div class="alert alert-info" role="alert">

		<c:forEach items="${infoMessages}" var="message">
			<p>${message}</p>
		</c:forEach>

	</div>
</c:if>
<c:if test="${not empty warningMessages}">
	<div class="alert alert-warning" role="alert">

		<c:forEach items="${warningMessages}" var="message">
			<p>${message}</p>
		</c:forEach>

	</div>
</c:if>
<c:if test="${not empty errorMessages}">
	<div class="alert alert-danger" role="alert">

		<c:forEach items="${errorMessages}" var="message">
			<p>${message}</p>
		</c:forEach>

	</div>
</c:if>

<form method="post" class="form-horizontal">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.LooseEvaluationBean.enrolment" />
				</div>

				<div class="col-sm-4">
					<%-- Relation to side 1 drop down rendered in input --%>
					<select id="looseEvaluationBean_enrolment"
						class="js-example-basic-single" name="enrolment">
						<option value=""></option>
						<%-- empty option remove it if you don't want to have it or give it a label --%>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.LooseEvaluationBean.availableDate" />
				</div>

				<div class="col-sm-10">
					<input id="looseEvaluationBean_availableDate" class="form-control"
						type="text" name="availabledate" bennu-date
						value="${not empty param.availabledate ? param.availabledate : looseEvaluationBean.availableDate }" />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.LooseEvaluationBean.examDate" />
				</div>

				<div class="col-sm-10">
					<input id="looseEvaluationBean_examDate" class="form-control"
						type="text" name="examdate" bennu-date
						value="${not empty param.examdate ? param.examdate : looseEvaluationBean.examDate }" />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.LooseEvaluationBean.grade" />
				</div>

				<div class="col-sm-10">
					<input id="looseEvaluationBean_grade" class="form-control"
						type="text" name="grade"
						value="${not empty param.grade ? param.grade : looseEvaluationBean.grade }" />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.LooseEvaluationBean.type" />
				</div>

				<div class="col-sm-4">
					<select id="looseEvaluationBean_type" class="form-control"
						name="type">
						<option value=""></option>
						<%-- empty option remove it if you don't want to have it or give it a label --%>
						<c:forEach items="${typeValues}" var="field">
							<option value="${field.externalId}">${field.name.content}</option>
						</c:forEach>
					</select>
					<script>
		$("#looseEvaluationBean_type").val("${not empty param.type ? param.type : looseEvaluationBean.type }");
	</script>
				</div>
			</div>
		</div>
		<div class="panel-footer">
			<input type="submit" class="btn btn-default" role="button"
				value="<spring:message code="label.submit" />" />
		</div>
	</div>
</form>

<script>
$(document).ready(function() {

		<%-- Block for providing enrolment options --%>
		enrolment_options = [
			<c:forEach items="${LooseEvaluationBean_enrolment_options}" var="element"> 
				{
					text : "${element.name}",
					id : "${element.externalId}"
				},
			</c:forEach>
		];
		
		$("#looseEvaluationBean_enrolment").select2(
			{
				data : enrolment_options,
			}	  
	    );
	    
	    
	    
	    $("#looseEvaluationBean_enrolment").select2().select2('val', "${param.enrolment}");
	
		<%-- End block for providing enrolment options --%>
	
	
	});
</script>
