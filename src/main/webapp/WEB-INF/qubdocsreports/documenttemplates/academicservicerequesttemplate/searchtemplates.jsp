<!--
 /**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Servi�os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright � 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright � 2015 Universidade de Lisboa (after any Go-Live phase)
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
    href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css"
    rel="stylesheet" />
<script
    src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link
    href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css"
    rel="stylesheet" />
<script
    src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link
    href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css"
    rel="stylesheet" />
<script
    src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript"
    src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script
    src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}


<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message code="label.documentTemplates.searchTemplates" />
        <small></small>
    </h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
    &nbsp; <a class=""
        href="${pageContext.request.contextPath}/qubdocsreports/documenttemplates/academicservicerequesttemplate/createstandardtemplate"><spring:message
            code="label.event.create" /></a> |&nbsp;&nbsp; <span
        class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
    &nbsp; <a class=""
        href="${pageContext.request.contextPath}/qubdocsreports/documenttemplates/academicservicerequesttemplate/createcustomtemplate"><spring:message
            code="label.event.documentTemplates.createCustomTemplate" /></a>
</div>
<c:if test="${not empty infoMessages}">
    <div class="alert alert-info" role="alert">

        <c:forEach items="${infoMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon glyphicon-ok-sign"
                    aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>

    </div>
</c:if>
<c:if test="${not empty warningMessages}">
    <div class="alert alert-warning" role="alert">

        <c:forEach items="${warningMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon-exclamation-sign"
                    aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>

    </div>
</c:if>
<c:if test="${not empty errorMessages}">
    <div class="alert alert-danger" role="alert">

        <c:forEach items="${errorMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon-exclamation-sign"
                    aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>

    </div>
</c:if>


<script type="text/javascript">
	  function processDelete(externalId) {
	    url = "${pageContext.request.contextPath}/qubdocsreports/documenttemplates/academicservicerequesttemplate/searchtemplatessearchTemplates/delete/" + externalId;
	    $("#deleteForm").attr("action", url);
	    $('#deleteModal').modal('toggle')
	  }
</script>


<div class="modal fade" id="deleteModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="deleteForm" action="#" method="POST">
                <div class="modal-header">
                    <button type="button" class="close"
                        data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">
                        <spring:message code="label.confirmation" />
                    </h4>
                </div>
                <div class="modal-body">
                    <p>
                        <spring:message
                            code="label.documentTemplates.searchTemplates.confirmDelete" />
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                        data-dismiss="modal">
                        <spring:message code="label.close" />
                    </button>
                    <button id="deleteButton" class="btn btn-danger"
                        type="submit">
                        <spring:message code="label.delete" />
                    </button>
                </div>
            </form>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<div class="panel panel-default">
    <form method="get" class="form-horizontal">
        <div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.AcademicServiceRequestTemplate.active" />
                </div>

                <div class="col-sm-2">
                    <select id="academicServiceRequestTemplate_active"
                        name="active" class="form-control">
                        <option value=""></option>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                        <option value="false"><spring:message
                                code="label.no" /></option>
                        <option value="true"><spring:message
                                code="label.yes" /></option>
                    </select>
                    <script>
		$("#academicServiceRequestTemplate_active").val('<c:out value='${ param.active }'/>');
	</script>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.AcademicServiceRequestTemplate.name" />
                </div>

                <div class="col-sm-10">
                    <input id="academicServiceRequestTemplate_name"
                        class="form-control" type="text" name="name"
                        bennu-localized-string
                        value='${not empty param.name ? param.name : "{}" } ' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.AcademicServiceRequestTemplate.serviceRequestType" />
                </div>

                <div class="col-sm-4">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <select
                        id="academicServiceRequestTemplate_serviceRequestType"
                        class="js-example-basic-single"
                        name="servicerequesttype">
                        <option value=""></option>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.AcademicServiceRequestTemplate.custom" />
                </div>

                <div class="col-sm-2">
                    <select id="academicServiceRequestTemplate_custom"
                        name="custom" class="form-control">
                        <option value=""></option>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                        <option value="false"><spring:message
                                code="label.no" /></option>
                        <option value="true"><spring:message
                                code="label.yes" /></option>
                    </select>
                    <script>
                        $("#academicServiceRequestTemplate_custom").val('<c:out value='${ param.custom }'/>');
                    </script>
                </div>
            </div>
        </div>
        <div class="panel-footer">
            <input type="submit" class="btn btn-default" role="button"
                value="<spring:message code="label.search" />" />
        </div>
    </form>
</div>


<c:choose>
    <c:when test="${not empty searchtemplatesResultsDataSet}">
        <table id="searchtemplatesTable"
            class="table responsive table-bordered table-hover">
            <thead>
                <tr>
                    <%--!!!  Field names here --%>
                    <th><spring:message
                            code="label.AcademicServiceRequestTemplate.active" /></th>
                    <th><spring:message
                            code="label.AcademicServiceRequestTemplate.name" /></th>
                    <th><spring:message
                            code="label.AcademicServiceRequestTemplate.serviceRequestType" /></th>
                    <th><spring:message
                            code="label.AcademicServiceRequestTemplate.custom" /></th>
                    <th><spring:message
                            code="label.AcademicServiceRequestTemplate.degreeType" /></th>
                    <th><spring:message
                            code="label.AcademicServiceRequestTemplate.degree" /></th>
                    <th><spring:message
                            code="label.AcademicServiceRequestTemplate.programConclusion" /></th>
                    <%-- Operations Column --%>
                    <th></th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <div class="alert alert-warning" role="alert">

            <p>
                <span class="glyphicon glyphicon-exclamation-sign"
                    aria-hidden="true">&nbsp;</span>
                <spring:message code="label.noResultsFound" />
            </p>

        </div>

    </c:otherwise>
</c:choose>

<script>
	var searchtemplatesDataSet = [
			<c:forEach items="${searchtemplatesResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
"active" : "<c:if test="${searchResult.active}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.active}"><spring:message code="label.false" /></c:if>",
"name" : "<c:out value='${searchResult.name.content}'/>",
"servicerequesttype" : "<c:out value='${searchResult.serviceRequestType.name.content}'/>",
"custom" : "<c:if test="${searchResult.custom}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.custom}"><spring:message code="label.false" /></c:if>",
"degreetype" : "<c:out value='${searchResult.degreeType.name.content}'/>",
"degree" : "<c:out value='${searchResult.degree.presentationNameI18N.content}'/>",
"programconclusion" : "<c:out value='${searchResult.programConclusion.name.content}'/>",
"actions" :
" <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/qubdocsreports/documenttemplates/academicservicerequesttemplate/searchtemplates/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" 
			},
            </c:forEach>
    ];
	
	$(document).ready(function() {

	<%-- Block for providing serviceRequestType options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	serviceRequestType_options = [
        <c:forEach items="${AcademicServiceRequestTemplate_serviceRequestType_options}" var="element">
            <c:set var="elementText" value="${fn:substring(element.name.content, 0, 160)}" />
            {
                text :"<c:out value='${elementText}'/>",
                id : "<c:out value='${element.externalId}'/>"
            },
        </c:forEach>
	];
	
	$("#academicServiceRequestTemplate_serviceRequestType").select2(
		{
			data : serviceRequestType_options,
		}	  
		    );

	<%-- If it's not from parameter change param.serviceRequestType to whatever you need (it's the externalId already) --%>
		    $("#academicServiceRequestTemplate_serviceRequestType").select2().select2('val', '<c:out value='${selectedServiceRequestType.externalId}'/>');
	<%-- End block for providing serviceRequestType options --%>
	


		var table = $('#searchtemplatesTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'active' },
			{ data: 'name' },
			{ data: 'servicerequesttype' },
			{ data: 'custom' },
			{ data: 'degreetype' },
			{ data: 'degree' },
			{ data: 'programconclusion' },
			{ data: 'actions' }
			
		],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 7 } 
		             ],
		"data" : searchtemplatesDataSet,
		//Documentation: https://datatables.net/reference/option/dom
//"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
//"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/swf/copy_csv_xls_pdf.swf"        	
        }
		});
		table.columns.adjust().draw();
		
		  $('#searchtemplatesTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>

