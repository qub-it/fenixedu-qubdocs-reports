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
<%@page import="org.fenixedu.qubdocs.ui.manageDocumentSignature.DocumentSignatureController"%>
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

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message
            code="label.manageDocumentSignature.searchDocumentSignature" />
        <small></small>
    </h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}<%=DocumentSignatureController.CREATE_URL%>"><spring:message
            code="label.event.create" /></a>
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



<div class="panel panel-default">
    <form method="get" class="form-horizontal">
        <div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.DocumentSignature.responsibleName" />
                </div>

                <div class="col-sm-10">
                    <input id="documentSignature_responsibleName"
                        class="form-control" type="text"
                        name="responsiblename"
                        value='<c:out value='${not empty param.responsiblename ? param.responsiblename : documentSignature.responsibleName }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.DocumentSignature.responsibleFunction" />
                </div>

                <div class="col-sm-10">
                    <input id="documentSignature_responsibleFunction"
                        class="form-control" type="text"
                        name="responsiblefunction"
                        bennu-localized-string
                        value='${not empty param.responsiblefunction ? param.responsiblefunction : "{}" } ' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.DocumentSignature.responsibleUnit" />
                </div>

                <div class="col-sm-10">
                    <input id="documentSignature_responsibleUnit"
                        class="form-control" type="text"
                        name="responsibleunit" bennu-localized-string
                        value='${not empty param.responsibleunit ? param.responsibleunit : "{}" } ' />
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
    <c:when test="${not empty searchdocumentsignatureResultsDataSet}">
        <table id="searchdocumentsignatureTable"
            class="table responsive table-bordered table-hover">
            <thead>
                <tr>
                    <%--!!!  Field names here --%>
                    <th><spring:message
                            code="label.DocumentSignature.responsibleName" /></th>
                    <th><spring:message
                            code="label.DocumentSignature.responsibleFunction" /></th>
                    <th><spring:message
                            code="label.DocumentSignature.responsibleUnit" /></th>
                    <th><spring:message
                            code="label.DocumentSignature.responsibleGender" /></th>
                    <th><spring:message
                            code="label.DocumentSignature.responsibleDefault" /></th>
                    <%-- Operations Column --%>
                    <th></th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info" role="alert">

            <spring:message code="label.noResultsFound" />

        </div>

    </c:otherwise>
</c:choose>

<script>
	var searchdocumentsignatureDataSet = [
			<c:forEach items="${searchdocumentsignatureResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
"responsiblename" : "<c:out value='${searchResult.responsibleName}'/>",
"responsiblefunction" : "<c:out value='${searchResult.responsibleFunction.content}'/>",
"responsibleunit" : "<c:out value='${searchResult.responsibleUnit.content}'/>",
"responsiblegender" : "<c:out value='${searchResult.responsibleGender.localizedName}'/>",
"responsibledefault" : "<c:if test="${searchResult.defaultSignature}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.defaultSignature}"><spring:message code="label.false" /></c:if>",
"actions" :
" <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}<%= DocumentSignatureController.SEARCH_VIEW_ACTION_URL %>${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" },
            </c:forEach>
    ];
	
	$(document).ready(function() {
	
		var table = $('#searchdocumentsignatureTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'responsiblename' },
			{ data: 'responsiblefunction' },
			{ data: 'responsibleunit' },
			{ data: 'responsiblegender' },
			{ data: 'responsibledefault' },
			{ data: 'actions' }
			
		],
		"data" : searchdocumentsignatureDataSet,
		//Documentation: https://datatables.net/reference/option/dom
//"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
//"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "//cdn.datatables.net/tabletools/2.2.3/swf/copy_csv_xls_pdf.swf"
        }
		});
		table.columns.adjust().draw();
		
		  $('#searchdocumentsignatureTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>
