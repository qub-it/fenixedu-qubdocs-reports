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
	<h1><spring:message code="label.documentPurposeTypes.updateDocumentPurposeTypeInstance" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/qubdocsreports/documentpurposetypes/documentpurposetypeinstance/read/${documentPurposeTypeInstance.externalId}" ><spring:message code="label.event.back" /></a>
|&nbsp;&nbsp;</div>
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

<form method="post" class="form-horizontal">

<div class="panel panel-default">
  <div class="panel-body">
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DocumentPurposeTypeInstance.code"/></div> 

<div class="col-sm-10">
	<input id="documentPurposeTypeInstance_code" class="form-control" type="text" name="code"  value='<c:out value='${not empty param.code ? param.code : documentPurposeTypeInstance.code }'/>'  required />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DocumentPurposeTypeInstance.name"/></div> 

<div class="col-sm-10">
	<input id="documentPurposeTypeInstance_name" class="form-control" type="text" name="name"  bennu-localized-string value='${not empty param.name ? param.name : documentPurposeTypeInstance.name.json() } ' required /> 
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DocumentPurposeTypeInstance.documentPurposeType"/></div> 

<div class="col-sm-4">
	<select id="documentPurposeTypeInstance_documentPurposeType" class="form-control" name="documentpurposetype">
		<option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME--%>
		<c:forEach items="${documentPurposeTypeValues}" var="field">
			<option value='<c:out value='${field}'/>'><c:out value='${field}'/></option>
		</c:forEach>
	</select>
	<script>
		$("#documentPurposeTypeInstance_documentPurposeType").val('<c:out value='${not empty param.documentpurposetype ? param.documentpurposetype : documentPurposeTypeInstance.documentPurposeType }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DocumentPurposeTypeInstance.active"/></div> 

<div class="col-sm-2">
<select id="documentPurposeTypeInstance_active" name="active" class="form-control">
<option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#documentPurposeTypeInstance_active").val('<c:out value='${not empty param.active ? param.active : documentPurposeTypeInstance.active }'/>');
	</script>	
</div>
</div>

<div class="form-group row">
	<div class="col-sm-2 control-label">
		<spring:message code="label.DocumentPurposeTypeInstance.serviceRequestTypes" />
	</div>

	<div class="col-sm-2">
		<select id="documentPurposeTypeInstance_serviceRequestTypes" class="js-example-basic-single" name="serviceRequestTypes" multiple="multiple">
			<option value="">&nbsp;</option>
			<%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
		</select>
		<script>
		<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
			var service_request_type_options = [
				<c:forEach items="${DocumentPurposeTypeInstance_serviceRequestTypes_options}" var="element">   // THIS _FIELD_NAME__options must be added in the Controller.java 
					{
						text :"<c:out value='${element.name.content}'/>",  //Format the Output for the HTML Option
						id : "<c:out value='${element.externalId}'/>" //Define the ID for the HTML Option
					},
				</c:forEach>
			];
			
			var selectedOptions = [
				<c:forEach items="${documentPurposeTypeInstance.serviceRequestTypes}" var="element" varStatus="loop"><c:out value='${element.externalId}'/>    ${!loop.last ? ',' : ''}	</c:forEach>
			];
			//Init Select2Options
			initSelect2Multiple("#documentPurposeTypeInstance_serviceRequestTypes",service_request_type_options, selectedOptions); //
		</script>
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


	});
</script>
