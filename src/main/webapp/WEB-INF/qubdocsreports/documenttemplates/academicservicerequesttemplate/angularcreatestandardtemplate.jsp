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
${portal.angularToolkit()} 
<%--${portal.toolkit()}--%>

<link href="${pageContext.request.contextPath}/static/qubdocsreports/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/qubdocsreports/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>						
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js" ></script>
<script src="${pageContext.request.contextPath}/static/qubdocsreports/js/omnis.js"></script>

<script src="${pageContext.request.contextPath}/webjars/angular-sanitize/1.3.11/angular-sanitize.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.css" />
<script src="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.js"></script>


<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.documentTemplates.createStandardTemplate" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/qubdocsreports/documenttemplates/academicservicerequesttemplate/searchtemplates"  ><spring:message code="label.event.back" /></a>
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

<script>

angular.module('angularAppAcademicServiceRequestTemplate', ['ngSanitize', 'ui.select', 'bennuToolkit']).controller('AcademicServiceRequestTemplateController', ['$scope', function($scope) {
	$scope.booleanvalues= [
		                    {name: '<spring:message code="label.no"/>',    value: false},
		                    {name: '<spring:message code="label.yes"/>',        value: true}
		                  ];

 	$scope.object=angular.fromJson('${academicServiceRequestTemplateBeanJson}');
	$scope.postBack = createAngularPostbackFunction($scope); 

	$scope.onDegreeTypeChange = function(degreeType, model) {
        $scope.object.degree = undefined;
        $scope.object.programConclusion = undefined;
        $scope.postBack(model);
    };
 	
}]);
</script>

<form name='form' method="POST" class="form-horizontal" enctype="multipart/form-data"
	ng-app="angularAppAcademicServiceRequestTemplate" ng-controller="AcademicServiceRequestTemplateController"
	action='${pageContext.request.contextPath}/qubdocsreports/documenttemplates/academicservicerequesttemplate/createstandardtemplate'>

	<input type="hidden" name="postback"
		value='${pageContext.request.contextPath}/qubdocsreports/documenttemplates/academicservicerequesttemplate/createstandardtemplatepostback' />
		
	<input name="bean" type="hidden" value="{{ object }}" />
<div class="panel panel-default">
  <div class="panel-body">
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.name"/></div> 

<div class="col-sm-10">
	<input id="academicServiceRequestTemplate_name" class="form-control" type="text" name="name"  ng-localized-string="object.name" required="required" ng-required="true"/>
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.description"/></div> 

<div class="col-sm-10">
	<input id="academicServiceRequestTemplate_description" class="form-control" type="text" name="description"  ng-localized-string="object.description"/>
</div>
</div>		
<div class="form-group row">
	<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.language"/></div> 
	<div class="col-sm-10">
		<select id="academicServiceRequestTemplate_language" name="language" ng-model="object.language" required="required" ng-required="true" >
			<option ng-repeat="language in object.languageDataSource" value="{{language.id}}">{{language.text}}</option>
		</select>
	</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.serviceRequestType"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		<ui-select id="academicServiceRequestTemplate_serviceRequestType" class="" name="servicerequesttype" ng-model="$parent.object.serviceRequestType" theme="bootstrap" ng-disabled="disabled" required>
    						<ui-select-match >{{$select.selected.text}}</ui-select-match>
    						<ui-select-choices repeat="serviceRequestType.id as serviceRequestType in object.serviceRequestTypeDataSource | filter: $select.search">
      							<span ng-bind-html="serviceRequestType.text | highlight: $select.search"></span>
    						</ui-select-choices>
  						</ui-select>				
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.degreeType"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		<ui-select id="academicServiceRequestTemplate_degreeType" class="" name="degreetype" ng-model="$parent.object.degreeType" on-select="onDegreeTypeChange($item, $model)" theme="bootstrap" ng-disabled="disabled" >
    						<ui-select-match >{{$select.selected.text}}</ui-select-match>
    						<ui-select-choices repeat="degreeType.id as degreeType in object.degreeTypeDataSource | filter: $select.search">
      							<span ng-bind-html="degreeType.text | highlight: $select.search"></span>
    						</ui-select-choices>
  						</ui-select>				
				</div>
</div>
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.degree"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		<ui-select id="academicServiceRequestTemplate_degree" class="" name="degree" ng-model="$parent.object.degree" theme="bootstrap" ng-disabled="disabled" >
    						<ui-select-match >{{$select.selected.text}}</ui-select-match>
    						<ui-select-choices repeat="degree.id as degree in object.degreeDataSource | filter: $select.search">
      							<span ng-bind-html="degree.text | highlight: $select.search"></span>
    						</ui-select-choices>
  						</ui-select>				
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.AcademicServiceRequestTemplate.programConclusion"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		<ui-select id="academicServiceRequestTemplate_programConclusion" class="" name="programconclusion" ng-model="$parent.object.programConclusion" theme="bootstrap" ng-disabled="disabled" >
    						<ui-select-match >{{$select.selected.text}}</ui-select-match>
    						<ui-select-choices repeat="programConclusion.id as programConclusion in object.programConclusionDataSource | filter: $select.search">
      							<span ng-bind-html="programConclusion.text | highlight: $select.search"></span>
    						</ui-select-choices>
  						</ui-select>				
				</div>
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

// Put here the initializing code for page
	});
</script>
