/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: diogo.simoes@qub-it.com 
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

package org.fenixedu.qubdocs.ui.manageDocumentSignature;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentSigner;
import org.fenixedu.bennu.FenixeduQubdocsReportsSpringConfiguration;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.commons.i18n.LocalizedString;
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
@RequestMapping(DocumentSignatureController.CONTROLLER_URL)
public class DocumentSignatureController extends FenixeduQubdocsReportsBaseController {

    public static final String CONTROLLER_URL = "/qubdocsreports/managedocumentsignature/documentsignature";

    @RequestMapping
    public String home(Model model) {
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

    private static final String SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + SEARCH_URI;

    @RequestMapping(value = SEARCH_URI)
    public String search(@RequestParam(value = "responsiblename", required = false) String responsibleName, @RequestParam(
            value = "responsiblefunction", required = false) LocalizedString responsibleFunction, @RequestParam(
            value = "responsibleunit", required = false) LocalizedString responsibleUnit, Model model) {
        List<DocumentSigner> searchdocumentsignatureResultsDataSet =
                filterSearchDocumentSignature(responsibleName, responsibleFunction, responsibleUnit);

        model.addAttribute("searchdocumentsignatureResultsDataSet", searchdocumentsignatureResultsDataSet);
        return "qubdocsreports/managedocumentsignature/documentsignature/search";
    }

    private Stream<DocumentSigner> getSearchUniverseSearchDocumentSignatureDataSet() {
        return DocumentSigner.findAll();
    }

    private List<DocumentSigner> filterSearchDocumentSignature(String responsibleName, LocalizedString responsibleFunction,
            LocalizedString responsibleUnit) {
        return getSearchUniverseSearchDocumentSignatureDataSet()
                .filter(documentSignature -> responsibleName == null
                        || responsibleName.length() == 0
                        || (documentSignature.getResponsibleName() != null && documentSignature.getResponsibleName().length() > 0 && containsName(
                                documentSignature.getResponsibleName(), responsibleName)))
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

    private boolean containsName(String fullName, String searchName) {
        String[] splittedSearchName = StringNormalizer.normalizeAndRemoveAccents(searchName.toLowerCase().trim()).split("\\s+");
        String fullNameNormalized = StringNormalizer.normalizeAndRemoveAccents(fullName.toLowerCase());
        for (String name : splittedSearchName) {
            if (!fullNameNormalized.contains(name)) {
                return false;
            }
        }
        return true;
    }

    private static final String SEARCH_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_VIEW_ACTION_URL = CONTROLLER_URL + SEARCH_VIEW_ACTION_URI;

    @RequestMapping(value = SEARCH_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") DocumentSigner documentSignature, Model model,
            RedirectAttributes redirectAttributes) {
        return redirect(READ_URL + documentSignature.getExternalId(), model, redirectAttributes);
    }

    private static final String READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + READ_URI;

    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") DocumentSigner documentSignature, Model model) {
        setDocumentSignature(documentSignature, model);
        return "qubdocsreports/managedocumentsignature/documentsignature/read";
    }

    private static final String DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + DELETE_URI;

    @RequestMapping(value = DELETE_URI + "{oid}")
    public String delete(@PathVariable("oid") DocumentSigner documentSignature, Model model, RedirectAttributes redirectAttributes) {

        setDocumentSignature(documentSignature, model);
        try {
            String responsibleName = documentSignature.getResponsibleName();
            deleteDocumentSignature(documentSignature);
            addInfoMessage(BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE,
                    "label.info.documentSignature.successfulDelete", responsibleName), model);
            return redirect(SEARCH_URL, model, redirectAttributes);
        } catch (DomainException ex) {
            addErrorMessage(
                    BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "label.error.delete")
                            + ex.getLocalizedMessage(), model);
        }
        return redirect(READ_URL + getDocumentSignature(model).getExternalId(), model, redirectAttributes);
    }

    private static final String CREATE_URI = "/create/";
    public static final String CREATE_URL = CONTROLLER_URL + CREATE_URI;

    @RequestMapping(value = CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("responsibleGenderValues", Gender.values());
        return "qubdocsreports/managedocumentsignature/documentsignature/create";
    }

    @RequestMapping(value = CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "responsiblename", required = true) String responsibleName, @RequestParam(
            value = "responsibleshortname", required = false) String responsibleShortName, @RequestParam(
            value = "responsiblefunction", required = false) LocalizedString responsibleFunction, @RequestParam(
            value = "responsibleunit", required = false) LocalizedString responsibleUnit, @RequestParam(
            value = "responsiblegender", required = false) Gender responsibleGender, Model model,
            RedirectAttributes redirectAttributes) {
        if (responsibleName.trim().isEmpty()) {
            addErrorMessage(
                    BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "error.responsible.name.invalid"),
                    model);
            return create(model);
        }
        if (responsibleFunction == null || responsibleFunction.getContent(new Locale("pt")) == null
                || responsibleFunction.getContent(new Locale("pt")).trim().isEmpty()) {
            addErrorMessage(
                    BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "error.responsible.function.empty"),
                    model);
            return create(model);
        }

        DocumentSigner documentSignature =
                createDocumentSignature(responsibleName, responsibleShortName, responsibleFunction, responsibleUnit,
                        responsibleGender);

        model.addAttribute("documentSignature", documentSignature);

        return redirect(READ_URL + getDocumentSignature(model).getExternalId(), model, redirectAttributes);
    }

    @Atomic
    public DocumentSigner createDocumentSignature(String responsibleName, String responsibleShortName,
            LocalizedString responsibleFunction, LocalizedString responsibleUnit, Gender responsibleGender) {
        return DocumentSigner.create(AdministrativeOffice.readDegreeAdministrativeOffice(), responsibleName,
                responsibleShortName, responsibleFunction, responsibleUnit, responsibleGender);
    }

    private static final String UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + UPDATE_URI;

    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") DocumentSigner documentSignature, Model model) {
        model.addAttribute("responsibleGenderValues", Gender.values());
        setDocumentSignature(documentSignature, model);
        return "qubdocsreports/managedocumentsignature/documentsignature/update";
    }

    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") DocumentSigner documentSignature, @RequestParam(value = "responsiblename",
            required = false) String responsibleName,
            @RequestParam(value = "responsibleshortname", required = false) String responsibleShortName, @RequestParam(
                    value = "responsiblefunction", required = false) LocalizedString responsibleFunction, @RequestParam(
                    value = "responsibleunit", required = false) LocalizedString responsibleUnit, @RequestParam(
                    value = "responsiblegender", required = false) Gender responsibleGender, @RequestParam(
                    value = "responsibledefault", required = false) boolean responsibleDefault, Model model,
            RedirectAttributes redirectAttributes) {

        setDocumentSignature(documentSignature, model);

        updateDocumentSignature(responsibleName, responsibleShortName, responsibleFunction, responsibleUnit, responsibleGender,
                responsibleDefault, model);

        return redirect(READ_URL + getDocumentSignature(model).getExternalId(), model, redirectAttributes);
    }

    @Atomic
    public void updateDocumentSignature(String responsibleName, String responsibleShortName, LocalizedString responsibleFunction,
            LocalizedString responsibleUnit, Gender responsibleGender, boolean responsibleDefault, Model m) {
        getDocumentSignature(m).edit(responsibleName, responsibleShortName, responsibleFunction, responsibleUnit,
                responsibleGender, responsibleDefault);
    }

}