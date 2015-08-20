package org.fenixedu.qubdocs.ui.documenttemplates;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.qubdocs.dto.documenttemplates.AcademicServiceRequestBean;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsBaseController;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.ist.fenixframework.Atomic;

@BennuSpringController(value = FenixeduQubdocsReportsController.class)
@RequestMapping(AcademicServiceRequestController.CONTROLLER_URL)
public class AcademicServiceRequestController extends FenixeduQubdocsReportsBaseController {

    public static final String CONTROLLER_URL = "/qubdocsreports/documenttemplates/academicservicerequest";

    private AcademicServiceRequestBean getAcademicServiceRequestBean(Model model) {
        return (AcademicServiceRequestBean) model.asMap().get("academicServiceRequestBean");
    }

    private void setAcademicServiceRequestBean(AcademicServiceRequestBean bean, Model model) {
        model.addAttribute("academicServiceRequestBeanJson", getBeanJson(bean));
        model.addAttribute("academicServiceRequestBean", bean);
    }

    private static final String _GET_CUSTOM_TEMPLATES_URI = "/getcustomtemplates";
    public static final String GET_CUSTOM_TEMPLATES_URL = CONTROLLER_URL + _GET_CUSTOM_TEMPLATES_URI;

    @RequestMapping(value = _GET_CUSTOM_TEMPLATES_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> getCustomTemplates(
            @RequestParam(value = "documentRequest", required = true) DocumentRequest documentRequest, Model model) {

        AcademicServiceRequestBean bean = new AcademicServiceRequestBean(documentRequest);
        setAcademicServiceRequestBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    private static final String _PRINT_DOCUMENT_REQUEST_URI = "/printdocumentrequest";
    public static final String PRINT_DOCUMENT_REQUEST_URL = CONTROLLER_URL + _PRINT_DOCUMENT_REQUEST_URI;

    @RequestMapping(value = _PRINT_DOCUMENT_REQUEST_URI, method = RequestMethod.POST)
    public void printDocumentRequest(@RequestParam(value = "bean", required = false) AcademicServiceRequestBean bean,
            @RequestParam(value = "documentRequest", required = true) DocumentRequest documentRequest, Model model,
            HttpServletResponse response) {
        try {
            byte[] document = generateDocument(bean, documentRequest);
            response.setContentType("application/pdf");
            String filename =
                    URLEncoder.encode(StringNormalizer.normalizePreservingCapitalizedLetters(documentRequest.getReportFileName())
                            .replaceAll("\\s", "_"), "UTF-8")
                            + ".pdf";
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.getOutputStream().write(document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Atomic
    public byte[] generateDocument(AcademicServiceRequestBean bean, DocumentRequest documentRequest) {
        documentRequest.setDocumentSigner(bean.getDocumentSigner());
        documentRequest.setAcademicServiceRequestTemplate(bean.getAcademicServiceRequestTemplate());
        return documentRequest.generateDocument();
    }

}
