<%@page import="org.fenixedu.qubdocs.ui.institutionconfiguration.InstitutionConfigurationController"%>
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

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>						
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js" ></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.institutionReportConfiguration.read" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;
    <a class="" href="${pageContext.request.contextPath}<%= InstitutionConfigurationController.UPDATE_URL %>"  >
        <spring:message code="label.event.update" />
    </a>
    &nbsp;
</div>

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

<div class="panel panel-primary">
    <div class="panel-heading">
	   <h3 class="panel-title"><spring:message code="label.details"/></h3>
	</div>
	<div class="panel-body">
        <table class="table">
		    <tbody>
                <tr>
	                <th scope="row" class="col-xs-3"><spring:message code="label.InstitutionReportConfiguration.name"/></th> 
	                <td>
		                <c:out value='${institutionReportConfiguration.name.content}'/>
	                </td> 
                </tr>
                <tr>
                    <th scope="row" class="col-xs-3"><spring:message code="label.InstitutionReportConfiguration.shortName"/></th> 
                    <td>
                        <c:out value='${institutionReportConfiguration.shortName.content}'/>
                    </td> 
                </tr>
                <tr>
                    <th scope="row" class="col-xs-3"><spring:message code="label.InstitutionReportConfiguration.address"/></th> 
                    <td>
                        <c:out value='${institutionReportConfiguration.address}'/>
                    </td> 
                </tr>
                <tr>
                    <th scope="row" class="col-xs-3"><spring:message code="label.InstitutionReportConfiguration.site"/></th> 
                    <td>
                        <c:out value='${institutionReportConfiguration.site}'/>
                    </td> 
                </tr>
                <tr>
	               <th scope="row" class="col-xs-3"><spring:message code="label.InstitutionReportConfiguration.logo"/></th> 
	               <td>
                       <a href="${pageContext.request.contextPath}<%= InstitutionConfigurationController.DOWNLOAD_URL %>/${institutionReportConfiguration.institutionLogo.externalId}">
     		               <c:out value='${institutionReportConfiguration.institutionLogo.displayName}'/>
                       </a>
	               </td> 
                </tr>
                <tr>
                   <th scope="row" class="col-xs-3"><spring:message code="label.InstitutionReportConfiguration.letterheadLogo"/></th> 
                   <td>
                       <a href="${pageContext.request.contextPath}<%= InstitutionConfigurationController.DOWNLOAD_URL %>/${institutionReportConfiguration.letterheadInstitutionLogo.externalId}">
                           <c:out value='${institutionReportConfiguration.letterheadInstitutionLogo.displayName}'/>
                       </a>
                   </td> 
                </tr>
            </tbody>
        </table>
    </div>
</div>

<script>
$(document).ready(function() {

	
	});
</script>
