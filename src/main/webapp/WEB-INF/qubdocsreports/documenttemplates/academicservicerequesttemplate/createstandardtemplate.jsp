<!--
 /**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: diogo.simoe@qub-it.com 
 *
 * 
 * This file is part of FenixEdu Specifications.
 *
 * FenixEdu Specifications is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Specifications is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Specifications.  If not, see <http://www.gnu.org/licenses/>.
 */
 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js"/>
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link rel="stylesheet" href="${datatablesCssUrl}"/>
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<link href="${pageContext.request.contextPath}/static/qubdocsreports/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/qubdocsreports/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>						
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js" ></script>
<script src="${pageContext.request.contextPath}/static/qubdocsreports/js/omnis.js"></script>



<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.documentTemplates.createStandardTemplate" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/qubdocsreports/documenttemplates/academicservicerequesttemplate/searchtemplates"  ><spring:message code="label.event.back" /></a></div>
	<c:if test="${not empty infoMessages}">
				<div class="alert alert-info" role="alert">
					
					<c:forEach items="${infoMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>
			<c:if test="${not empty warningMessages}">
				<div class="alert alert-warning" role="alert">
					
					<c:forEach items="${warningMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>
			<c:if test="${not empty errorMessages}">
				<div class="alert alert-danger" role="alert">
					
					<c:forEach items="${errorMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>

<form method="post" class="form-horizontal" enctype="multipart/form-data">
<div class="panel panel-default">
  <div class="panel-body">
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.name"/></div> 

<div class="col-sm-10">
	<input id="academicServiceRequestTemplate_name" class="form-control" type="text" name="name"  bennu-localized-string value='${not empty param.name ? param.name : "{}" } '/> 
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.description"/></div> 

<div class="col-sm-10">
	<input id="academicServiceRequestTemplate_description" class="form-control" type="text" name="description"  bennu-localized-string value='${not empty param.description ? param.description : "{}" } '/> 
</div>
</div>
<!--
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.language"/></div>
	<div class="col-sm-10">
		<input id="academicServiceRequestTemplate_language" class="form-control" type="text" name="language"  value='<c:out value='${not empty param.language ? param.language : academicServiceRequestTemplate.language }'/>'  required />
	</div>	
</div>
-->
<div class="form-group row">
	<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.language"/></div> 
	<div class="col-sm-2">
		<select id="academicServiceRequestTemplate_language" class="form-control" name="language">
			<c:forEach items="${AcademicServiceRequestTemplate_language_options}" var="element">
				<option value="${element.language}_${element.country}">${element.displayLanguage}</option>
			</c:forEach>
		</select>
	</div>
</div>
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.serviceRequestType"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="academicServiceRequestTemplate_serviceRequestType" class="js-example-basic-single" name="servicerequesttype">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.degreeType"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="academicServiceRequestTemplate_degreeType" class="js-example-basic-single" name="degreetype">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.degree"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="academicServiceRequestTemplate_degree" class="js-example-basic-single" name="degree">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
	<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.programConclusion"/></div>
	<div class="col-sm-4">
		<%-- Relation to side 1 drop down rendered in input --%>
		<select id="academicServiceRequestTemplate_programConclusion" class="js-example-basic-single" name="programconclusion">
			<option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
	</div>
</div>
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.documentTemplateFile"/></div>
	<div class="col-sm-4">
		<input type="file" name="documentTemplateFile" accept=".odt, application/vnd.oasis.opendocument.text" required />
	</div>	
</div>

  </div>
  <div class="panel-footer">
		<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />"/>
	</div>
</div>
</form>

<script>
$(document).ready(function() {

	<%-- Block for providing serviceRequestType options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	serviceRequestType_options = [
		<c:forEach items="${AcademicServiceRequestTemplate_serviceRequestType_options}" var="element"> 
			{
				text : "<c:out value='${element.name.content}'/>",  
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#academicServiceRequestTemplate_serviceRequestType").select2(
		{
			data : serviceRequestType_options,
		}	  
    );
    
    
    
    $("#academicServiceRequestTemplate_serviceRequestType").select2().select2('val', '<c:out value='${param.servicerequesttype}'/>');

	<%-- End block for providing serviceRequestType options --%>
	<%-- Block for providing degreeType options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	degreeType_options = [
		<c:forEach items="${AcademicServiceRequestTemplate_degreeType_options}" var="element"> 
			{
				text : "<c:out value='${element}'/>",  
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#academicServiceRequestTemplate_degreeType").select2(
		{
			data : degreeType_options,
		}	  
    );
    
    
    
    $("#academicServiceRequestTemplate_degreeType").select2().select2('val', '<c:out value='${param.degreetype}'/>');

	<%-- End block for providing degreeType options --%>
	<%-- Block for providing degree options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	degree_options = [
		<c:forEach items="${AcademicServiceRequestTemplate_degree_options}" var="element"> 
			{
				text : "<c:out value='${element}'/>",  
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#academicServiceRequestTemplate_degree").select2(
		{
			data : degree_options,
		}	  
    );
    
    
    
    $("#academicServiceRequestTemplate_degree").select2().select2('val', '<c:out value='${param.degree}'/>');

	<%-- End block for providing degree options --%>
	<%-- Block for providing programConclusion options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	programConclusion_options = [
		<c:forEach items="${AcademicServiceRequestTemplate_programConclusion_options}" var="element"> 
			{
				text : "<c:out value='${element}'/>",  
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#academicServiceRequestTemplate_programConclusion").select2(
		{
			data : programConclusion_options,
		}	  
    );
    
    
    
    $("#academicServiceRequestTemplate_programConclusion").select2().select2('val', '<c:out value='${param.programconclusion}'/>');

	<%-- End block for providing programConclusion options --%>

	});
</script>
