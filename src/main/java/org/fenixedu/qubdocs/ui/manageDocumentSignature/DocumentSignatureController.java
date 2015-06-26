package org.fenixedu.qubdocs.ui.manageDocumentSignature;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.qubdocs.domain.DocumentSignature;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsBaseController;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.qubdocs.ui.manageDocumentSignature") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = FenixeduQubdocsReportsController.class, title = "label.title.manageDocumentSignature",
        accessGroup = "anyone")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value = FenixeduQubdocsReportsController.class)
@RequestMapping("/qubdocs/managedocumentsignature/documentsignature")
public class DocumentSignatureController extends FenixeduQubdocsReportsBaseController {

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:/qubdocs/managedocumentsignature/documentsignature/";
    }

    private DocumentSignature getDocumentSignature(Model m) {
        return (DocumentSignature) m.asMap().get("documentSignature");
    }

    private void setDocumentSignature(DocumentSignature documentSignature, Model m) {
        m.addAttribute("documentSignature", documentSignature);
    }

    @Atomic
    public void deleteDocumentSignature(DocumentSignature documentSignature) {
        // CHANGE_ME: Do the processing for deleting the documentSignature
        // Do not catch any exception here

        //documentSignature.delete();
    }

//				
    @RequestMapping(value = "/")
    public String search(
            @RequestParam(value = "responsiblename", required = false) java.lang.String responsibleName,
            @RequestParam(value = "responsiblefunction", required = false) org.fenixedu.commons.i18n.LocalizedString responsibleFunction,
            @RequestParam(value = "responsibleunit", required = false) org.fenixedu.commons.i18n.LocalizedString responsibleUnit,
            Model model) {
        List<DocumentSignature> searchdocumentsignatureResultsDataSet =
                filterSearchDocumentSignature(responsibleName, responsibleFunction, responsibleUnit);

        //add the results dataSet to the model
        model.addAttribute("searchdocumentsignatureResultsDataSet", searchdocumentsignatureResultsDataSet);
        return "qubdocs/managedocumentsignature/documentsignature/search";
    }

    private List<DocumentSignature> getSearchUniverseSearchDocumentSignatureDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return new ArrayList<DocumentSignature>(DocumentSignature.readAll());

    }

    private List<DocumentSignature> filterSearchDocumentSignature(java.lang.String responsibleName,
            org.fenixedu.commons.i18n.LocalizedString responsibleFunction,
            org.fenixedu.commons.i18n.LocalizedString responsibleUnit) {

        return getSearchUniverseSearchDocumentSignatureDataSet()
                .stream()
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
    public String processSearchToViewAction(@PathVariable("oid") DocumentSignature documentSignature, Model model) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return "redirect:/qubdocs/managedocumentsignature/documentsignature/read" + "/" + documentSignature.getExternalId();
    }

//				
    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") DocumentSignature documentSignature, Model model) {
        setDocumentSignature(documentSignature, model);
        return "qubdocs/managedocumentsignature/documentsignature/read";
    }

//
    @RequestMapping(value = "/delete/{oid}")
    public String delete(@PathVariable("oid") DocumentSignature documentSignature, Model model) {

        setDocumentSignature(documentSignature, model);
        try {
            //call the Atomic delete function
            deleteDocumentSignature(documentSignature);

            addInfoMessage("Sucess deleting DocumentSignature ...", model);
            return "redirect:/qubdocs/managedocumentsignature/documentsignature/";
        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage("Error deleting the DocumentSignature due to " + ex.getMessage(), model);
        }

        //The default mapping is the same Read View
        return "qubdocs/managedocumentsignature/documentsignature/read/" + getDocumentSignature(model).getExternalId();
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "qubdocs/managedocumentsignature/documentsignature/create";
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(
            @RequestParam(value = "responsiblename", required = false) java.lang.String responsibleName,
            @RequestParam(value = "responsiblefunction", required = false) org.fenixedu.commons.i18n.LocalizedString responsibleFunction,
            @RequestParam(value = "responsibleunit", required = false) org.fenixedu.commons.i18n.LocalizedString responsibleUnit,
            Model model) {
        /*
        *  Creation Logic
        *	
        	do something();
        *    		
        */

        DocumentSignature documentSignature = createDocumentSignature(responsibleName, responsibleFunction, responsibleUnit);

        /*
         * Success Validation
         */

        //Add the bean to be used in the View
        model.addAttribute("documentSignature", documentSignature);

        return "redirect:/qubdocs/managedocumentsignature/documentsignature/read/" + getDocumentSignature(model).getExternalId();

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
    public DocumentSignature createDocumentSignature(java.lang.String responsibleName,
            org.fenixedu.commons.i18n.LocalizedString responsibleFunction,
            org.fenixedu.commons.i18n.LocalizedString responsibleUnit) {
        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         */

        return DocumentSignature.create(AdministrativeOffice.readDegreeAdministrativeOffice(), responsibleName,
                responsibleFunction, responsibleUnit);
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") DocumentSignature documentSignature, Model model) {
        setDocumentSignature(documentSignature, model);
        return "qubdocs/managedocumentsignature/documentsignature/update";
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(
            @PathVariable("oid") DocumentSignature documentSignature,
            @RequestParam(value = "responsiblename", required = false) java.lang.String responsibleName,
            @RequestParam(value = "responsiblefunction", required = false) org.fenixedu.commons.i18n.LocalizedString responsibleFunction,
            @RequestParam(value = "responsibleunit", required = false) org.fenixedu.commons.i18n.LocalizedString responsibleUnit,
            Model model) {

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
        updateDocumentSignature(responsibleName, responsibleFunction, responsibleUnit, model);

        return "redirect:/qubdocs/managedocumentsignature/documentsignature/read/" + getDocumentSignature(model).getExternalId();

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
            org.fenixedu.commons.i18n.LocalizedString responsibleUnit, Model m) {
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */
        getDocumentSignature(m).edit(responsibleName, responsibleFunction, responsibleUnit);

    }

}
