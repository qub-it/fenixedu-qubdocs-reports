package org.fenixedu.qubdocs.ui.manageDocumentSignature;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentSigner;
import org.fenixedu.bennu.FenixeduQubdocsReportsSpringConfiguration;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsBaseController;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.qubdocs.ui.manageDocumentSignature") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = FenixeduQubdocsReportsController.class, title = "label.title.manageDocumentSignature",
        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value = FenixeduQubdocsReportsController.class)
@RequestMapping(DocumentSignatureController.CONTROLLER_URL)
public class DocumentSignatureController extends FenixeduQubdocsReportsBaseController {

    public static final String CONTROLLER_URL = "/qubdocsreports/managedocumentsignature/documentsignature";

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:" + CONTROLLER_URL + "/";
    }

    private DocumentSigner getDocumentSignature(Model m) {
        return (DocumentSigner) m.asMap().get("documentSignature");
    }

    private void setDocumentSignature(DocumentSigner documentSignature, Model m) {
        m.addAttribute("documentSignature", documentSignature);
    }

    @Atomic
    public void deleteDocumentSignature(DocumentSigner documentSignature) {
        // CHANGE_ME: Do the processing for deleting the documentSignature
        // Do not catch any exception here

        documentSignature.delete();
    }

//				
    @RequestMapping(value = "/")
    public String search(
            @RequestParam(value = "responsiblename", required = false) java.lang.String responsibleName,
            @RequestParam(value = "responsiblefunction", required = false) org.fenixedu.commons.i18n.LocalizedString responsibleFunction,
            @RequestParam(value = "responsibleunit", required = false) org.fenixedu.commons.i18n.LocalizedString responsibleUnit,
            Model model) {
        List<DocumentSigner> searchdocumentsignatureResultsDataSet =
                filterSearchDocumentSignature(responsibleName, responsibleFunction, responsibleUnit);

        //add the results dataSet to the model
        model.addAttribute("searchdocumentsignatureResultsDataSet", searchdocumentsignatureResultsDataSet);
        return "qubdocsreports/managedocumentsignature/documentsignature/search";
    }

    private Stream<DocumentSigner> getSearchUniverseSearchDocumentSignatureDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return DocumentSigner.findAll();

    }

    private List<DocumentSigner> filterSearchDocumentSignature(java.lang.String responsibleName,
            org.fenixedu.commons.i18n.LocalizedString responsibleFunction,
            org.fenixedu.commons.i18n.LocalizedString responsibleUnit) {

        return getSearchUniverseSearchDocumentSignatureDataSet()
                .filter(documentSignature -> responsibleName == null
                        || responsibleName.length() == 0
                        || (documentSignature.getResponsibleName() != null && documentSignature.getResponsibleName().length() > 0 && documentSignature
                                .getResponsibleName().toLowerCase().contains(responsibleName.toLowerCase())))
                .filter(documentSignature -> responsibleFunction == null
                        || responsibleFunction.isEmpty()
                        || responsibleFunction
                                .getLocales()
                                .stream()
                                .allMatch(
                                        locale -> documentSignature.getResponsibleFunction().getContent(locale) != null
                                                && documentSignature.getResponsibleFunction().getContent(locale).toLowerCase()
                                                        .contains(responsibleFunction.getContent(locale).toLowerCase())))
                .filter(documentSignature -> responsibleUnit == null
                        || responsibleUnit.isEmpty()
                        || responsibleUnit
                                .getLocales()
                                .stream()
                                .allMatch(
                                        locale -> documentSignature.getResponsibleUnit().getContent(locale) != null
                                                && documentSignature.getResponsibleUnit().getContent(locale).toLowerCase()
                                                        .contains(responsibleUnit.getContent(locale).toLowerCase())))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") DocumentSigner documentSignature, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect(
                "/qubdocsreports/managedocumentsignature/documentsignature/read" + "/" + documentSignature.getExternalId(),
                model, redirectAttributes);
    }

//				
    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") DocumentSigner documentSignature, Model model) {
        setDocumentSignature(documentSignature, model);
        return "qubdocsreports/managedocumentsignature/documentsignature/read";
    }

//
    @RequestMapping(value = "/delete/{oid}")
    public String delete(@PathVariable("oid") DocumentSigner documentSignature, Model model, RedirectAttributes redirectAttributes) {

        setDocumentSignature(documentSignature, model);
        try {
            String responsibleName = documentSignature.getResponsibleName();
            //call the Atomic delete function
            deleteDocumentSignature(documentSignature);
            addInfoMessage(BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE,
                    "label.info.documentSignature.successfulDelete", responsibleName), model);
            return redirect("/qubdocsreports/managedocumentsignature/documentsignature/", model, redirectAttributes);
        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(
                    BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "label.error.delete")
                            + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return "qubdocsreports/managedocumentsignature/documentsignature/read/" + getDocumentSignature(model).getExternalId();
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("responsibleGenderValues", Gender.values());
        return "qubdocsreports/managedocumentsignature/documentsignature/create";
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(
            @RequestParam(value = "responsiblename", required = false) java.lang.String responsibleName,
            @RequestParam(value = "responsiblefunction", required = false) org.fenixedu.commons.i18n.LocalizedString responsibleFunction,
            @RequestParam(value = "responsibleunit", required = false) org.fenixedu.commons.i18n.LocalizedString responsibleUnit,
            @RequestParam(value = "responsiblegender", required = false) org.fenixedu.academic.domain.person.Gender responsibleGender,
            Model model, RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        *	
        	do something();
        *    		
        */

        DocumentSigner documentSignature =
                createDocumentSignature(responsibleName, responsibleFunction, responsibleUnit, responsibleGender);

        /*
         * Success Validation
         */

        //Add the bean to be used in the View
        model.addAttribute("documentSignature", documentSignature);

        return redirect("/qubdocsreports/managedocumentsignature/documentsignature/read/"
                + getDocumentSignature(model).getExternalId(), model, redirectAttributes);

        /*
         * If there is any error in validation 
         *
         * Add a error / warning message
         * 
         * addErrorMessage(" Error because ...",model);
         * addWarningMessage(" Waring becaus ...",model);
         
         
         * 
         * return create(model);
         */
    }

    @Atomic
    public DocumentSigner createDocumentSignature(java.lang.String responsibleName,
            org.fenixedu.commons.i18n.LocalizedString responsibleFunction,
            org.fenixedu.commons.i18n.LocalizedString responsibleUnit,
            org.fenixedu.academic.domain.person.Gender responsibleGender) {
        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         */

        return DocumentSigner.create(AdministrativeOffice.readDegreeAdministrativeOffice(), responsibleName, responsibleFunction,
                responsibleUnit, responsibleGender);
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") DocumentSigner documentSignature, Model model) {
        model.addAttribute("responsibleGenderValues", Gender.values());
        setDocumentSignature(documentSignature, model);
        return "qubdocsreports/managedocumentsignature/documentsignature/update";
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(
            @PathVariable("oid") DocumentSigner documentSignature,
            @RequestParam(value = "responsiblename", required = false) java.lang.String responsibleName,
            @RequestParam(value = "responsiblefunction", required = false) org.fenixedu.commons.i18n.LocalizedString responsibleFunction,
            @RequestParam(value = "responsibleunit", required = false) org.fenixedu.commons.i18n.LocalizedString responsibleUnit,
            @RequestParam(value = "responsiblegender", required = false) org.fenixedu.academic.domain.person.Gender responsibleGender,
            @RequestParam(value = "responsibledefault", required = false) boolean responsibleDefault, Model model,
            RedirectAttributes redirectAttributes) {

        setDocumentSignature(documentSignature, model);

        /*
        *  UpdateLogic here
        *	
        	do something();
        *    		
        */

        /*
         * Succes Update
         */
        updateDocumentSignature(responsibleName, responsibleFunction, responsibleUnit, responsibleGender, responsibleDefault,
                model);

        return redirect("/qubdocsreports/managedocumentsignature/documentsignature/read/"
                + getDocumentSignature(model).getExternalId(), model, redirectAttributes);

        /*
         * If there is any error in validation 
         *
         * Add a error / warning message
         * 
         * addErrorMessage(" Error because ...",model);
         * addWarningMessage(" Waring becaus ...",model);
         
         * return update(documentSignature,model);
         */
    }

    @Atomic
    public void updateDocumentSignature(java.lang.String responsibleName,
            org.fenixedu.commons.i18n.LocalizedString responsibleFunction,
            org.fenixedu.commons.i18n.LocalizedString responsibleUnit,
            org.fenixedu.academic.domain.person.Gender responsibleGender, boolean responsibleDefault, Model m) {
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */
        getDocumentSignature(m).edit(responsibleName, responsibleFunction, responsibleUnit, responsibleGender);
        getDocumentSignature(m).setDefaultSignature(responsibleDefault);

    }

}
