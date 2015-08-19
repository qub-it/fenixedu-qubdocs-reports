/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: xpto@qub-it.com
 *
 * 
 * This file is part of FenixEdu Qubdocs.
 *
 * FenixEdu Qubdocs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Qubdocs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Qubdocs.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.qubdocs.ui.documenttemplates;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.bennu.FenixeduQubdocsReportsSpringConfiguration;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.qubdocs.domain.DocumentTemplateFile;
import org.fenixedu.qubdocs.domain.serviceRequests.AcademicServiceRequestTemplate;
import org.fenixedu.qubdocs.dto.documenttemplates.AcademicServiceRequestTemplateBean;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsBaseController;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.qubdocs.ui.documentTemplates") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = FenixeduQubdocsReportsController.class, title = "label.title.documentTemplates",
        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value = FenixeduQubdocsReportsController.class)
@RequestMapping(AcademicServiceRequestTemplateController.CONTROLLER_URL)
public class AcademicServiceRequestTemplateController extends FenixeduQubdocsReportsBaseController {

    public static final String CONTROLLER_URL = "/qubdocsreports/documenttemplates/academicservicerequesttemplate";

    //

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:" + CONTROLLER_URL + _SEARCHTEMPLATES_URI;
    }

    // @formatter: off

    /*
    * This should be used when using AngularJS in the JSP
    */

    private AcademicServiceRequestTemplateBean getAcademicServiceRequestTemplateBean(Model model) {
        return (AcademicServiceRequestTemplateBean) model.asMap().get("academicServiceRequestTemplateBean");
    }

    private void setAcademicServiceRequestTemplateBean(AcademicServiceRequestTemplateBean bean, Model model) {
        model.addAttribute("academicServiceRequestTemplateBeanJson", getBeanJson(bean));
        model.addAttribute("academicServiceRequestTemplateBean", bean);
    }

    // @formatter: on

    private AcademicServiceRequestTemplate getAcademicServiceRequestTemplate(Model model) {
        return (AcademicServiceRequestTemplate) model.asMap().get("academicServiceRequestTemplate");
    }

    private void setAcademicServiceRequestTemplate(AcademicServiceRequestTemplate academicServiceRequestTemplate, Model model) {
        model.addAttribute("academicServiceRequestTemplate", academicServiceRequestTemplate);
    }

    @Atomic
    public void deleteAcademicServiceRequestTemplate(AcademicServiceRequestTemplate academicServiceRequestTemplate) {
        academicServiceRequestTemplate.delete();
    }

//                  
    private static final String _SEARCHTEMPLATES_URI = "/searchtemplates";
    public static final String SEARCHTEMPLATES_URL = CONTROLLER_URL + _SEARCHTEMPLATES_URI;

    @RequestMapping(value = _SEARCHTEMPLATES_URI)
    public String searchTemplates(
            @RequestParam(value = "active", required = false) java.lang.Boolean active,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name,
            @RequestParam(value = "servicerequesttype", required = false) org.fenixedu.academic.domain.serviceRequests.ServiceRequestType serviceRequestType,
            @RequestParam(value = "custom", required = false) java.lang.Boolean custom, Model model) {
        List<AcademicServiceRequestTemplate> searchtemplatesResultsDataSet =
                filterSearchTemplates(active, name, serviceRequestType, custom);

        //add the results dataSet to the model
        model.addAttribute("searchtemplatesResultsDataSet", searchtemplatesResultsDataSet);
        model.addAttribute(
                "AcademicServiceRequestTemplate_serviceRequestType_options",
                org.fenixedu.academic.domain.serviceRequests.ServiceRequestType.findActive()
                        .sorted(Comparator.comparing(ServiceRequestType::getName)).collect(Collectors.toList()));
        return "qubdocsreports/documenttemplates/academicservicerequesttemplate/searchtemplates";
    }

    private Stream<AcademicServiceRequestTemplate> getSearchUniverseSearchTemplatesDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return AcademicServiceRequestTemplate.findAll();
        //return new ArrayList<AcademicServiceRequestTemplate>().stream();
    }

    private List<AcademicServiceRequestTemplate> filterSearchTemplates(java.lang.Boolean active,
            org.fenixedu.commons.i18n.LocalizedString name,
            org.fenixedu.academic.domain.serviceRequests.ServiceRequestType serviceRequestType, java.lang.Boolean custom) {

        return getSearchUniverseSearchTemplatesDataSet()
                .filter(academicServiceRequestTemplate -> active == null
                        || active.equals(academicServiceRequestTemplate.getActive()))
                .filter(academicServiceRequestTemplate -> name == null
                        || name.isEmpty()
                        || name.getLocales()
                                .stream()
                                .allMatch(
                                        locale -> academicServiceRequestTemplate.getName().getContent(locale) != null
                                                && academicServiceRequestTemplate.getName().getContent(locale).toLowerCase()
                                                        .contains(name.getContent(locale).toLowerCase())))
                .filter(academicServiceRequestTemplate -> serviceRequestType == null
                        || serviceRequestType == academicServiceRequestTemplate.getServiceRequestType())
                .filter(academicServiceRequestTemplate -> custom == null
                        || custom.equals(academicServiceRequestTemplate.getCustom())).collect(Collectors.toList());
    }

    private static final String _SEARCHTEMPLATES_TO_VIEW_ACTION_URI = "/searchtemplates/view/";
    public static final String SEARCHTEMPLATES_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCHTEMPLATES_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCHTEMPLATES_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchTemplatesToViewAction(
            @PathVariable("oid") AcademicServiceRequestTemplate academicServiceRequestTemplate, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below    
        return redirect("/qubdocsreports/documenttemplates/academicservicerequesttemplate/readtemplate" + "/"
                + academicServiceRequestTemplate.getExternalId(), model, redirectAttributes);
    }

    //

    //
    // This is the EventcreateCustomTemplate Method for Screen searchtemplates
    //
    private static final String _SEARCHTEMPLATES_TO_CREATECUSTOMTEMPLATE_URI = "/searchtemplates/createcustomtemplate";
    public static final String SEARCHTEMPLATES_TO_CREATECUSTOMTEMPLATE_URL = CONTROLLER_URL
            + _SEARCHTEMPLATES_TO_CREATECUSTOMTEMPLATE_URI;

    @RequestMapping(value = _SEARCHTEMPLATES_TO_CREATECUSTOMTEMPLATE_URI)
    public String processSearchtemplatesCreateCustomTemplate(Model model, RedirectAttributes redirectAttributes) {
        //
        /* Put here the logic for processing Event createCustomTemplate     */
        //doSomething();

        // Now choose what is the Exit Screen    
        return redirect("/qubdocsreports/documenttemplates/academicservicerequesttemplate/createcustomtemplate", model,
                redirectAttributes);
    }

//                  
    private static final String _CREATESTANDARDTEMPLATE_URI = "/createstandardtemplate";
    public static final String CREATESTANDARDTEMPLATE_URL = CONTROLLER_URL + _CREATESTANDARDTEMPLATE_URI;

    @RequestMapping(value = _CREATESTANDARDTEMPLATE_URI, method = RequestMethod.GET)
    public String createstandardtemplate(Model model) {
        model.addAttribute("AcademicServiceRequestTemplate_language_options", CoreConfiguration.supportedLocales());
        model.addAttribute(
                "AcademicServiceRequestTemplate_serviceRequestType_options",
                org.fenixedu.academic.domain.serviceRequests.ServiceRequestType.findActive()
                        .sorted(Comparator.comparing(ServiceRequestType::getName)).collect(Collectors.toList()));
        model.addAttribute("AcademicServiceRequestTemplate_degreeType_options", DegreeType.all().collect(Collectors.toList()));
        model.addAttribute("AcademicServiceRequestTemplate_degree_options", new ArrayList<org.fenixedu.academic.domain.Degree>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("AcademicServiceRequestTemplate_degree_options", org.fenixedu.academic.domain.Degree.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("AcademicServiceRequestTemplate_programConclusion_options",
                new ArrayList<org.fenixedu.academic.domain.degreeStructure.ProgramConclusion>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("AcademicServiceRequestTemplate_programConclusion_options", org.fenixedu.academic.domain.degreeStructure.ProgramConclusion.findAll()); // CHANGE_ME - MUST DEFINE RELATION

        //IF ANGULAR, initialize the Bean
        AcademicServiceRequestTemplateBean bean = new AcademicServiceRequestTemplateBean();
        bean.setLanguageDataSource(new ArrayList<Locale>(CoreConfiguration.supportedLocales()));
        bean.setServiceRequestTypeDataSource(ServiceRequestType.findActive()
                .sorted(Comparator.comparing(ServiceRequestType::getName)).collect(Collectors.toList()));
        bean.setDegreeTypeDataSource(DegreeType.all().sorted().collect(Collectors.toList()));
        this.setAcademicServiceRequestTemplateBean(bean, model);

        //return "qubdocsreports/documenttemplates/academicservicerequesttemplate/createstandardtemplate";
        return "qubdocsreports/documenttemplates/academicservicerequesttemplate/angularcreatestandardtemplate";
    }

    //
//                   THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
    //
//                          // @formatter: off
//              
    private static final String _CREATESTANDARDTEMPLATEPOSTBACK_URI = "/createstandardtemplatepostback";
    public static final String CREATESTANDARDTEMPLATEPOSTBACK_URL = CONTROLLER_URL + _CREATESTANDARDTEMPLATEPOSTBACK_URI;

    @RequestMapping(value = _CREATESTANDARDTEMPLATEPOSTBACK_URI, method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> createstandardtemplatepostback(
            @RequestParam(value = "bean", required = false) AcademicServiceRequestTemplateBean bean, Model model) {

        DegreeType degreeType = bean.getDegreeType();
        if (degreeType != null) {
            bean.setDegreeDataSource(new ArrayList<Degree>(degreeType.getDegreeSet()));
            bean.setProgramConclusionDataSource(new ArrayList<ProgramConclusion>(Bennu.getInstance().getProgramConclusionSet()));
        }

        // Do validation logic ?!?!
        //if (something_wrong){
        //                 return new ResponseEntity<String>(<MESSAGE_FROM_BUNDLE>,HttpStatus.BAD_REQUEST);
        //}
        this.setAcademicServiceRequestTemplateBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _CREATESTANDARDTEMPLATE_URI, method = RequestMethod.POST)
    public String createstandardtemplate(@RequestParam(value = "bean", required = false) AcademicServiceRequestTemplateBean bean,
            @RequestParam(value = "documentTemplateFile", required = true) MultipartFile documentTemplateFile, Model model,
            RedirectAttributes redirectAttributes) {

        /*
        *  Creation Logic
        */

        try {

            AcademicServiceRequestTemplate academicServiceRequestTemplate =
                    createAcademicServiceRequestTemplate(bean.getName(), bean.getDescription(), bean.getLanguage(),
                            bean.getServiceRequestType(), bean.getDegreeType(), bean.getDegree(), bean.getProgramConclusion(),
                            documentTemplateFile);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("academicServiceRequestTemplate", academicServiceRequestTemplate);
            return redirect("/qubdocsreports/documenttemplates/academicservicerequesttemplate/readtemplate/"
                    + getAcademicServiceRequestTemplate(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            /*
             * If there is any error in validation 
             *
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(QubdocsSpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */

            addErrorMessage(
                    BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "label.error.create")
                            + de.getLocalizedMessage(), model);
            this.setAcademicServiceRequestTemplateBean(bean, model);
            return "qubdocsreports/documenttemplates/academicservicerequesttemplate/angularcreatestandardtemplate";
        }
    }

//                          // @formatter: on

//                  
//    @RequestMapping(value = _CREATESTANDARDTEMPLATE_URI, method = RequestMethod.POST)
//    public String createstandardtemplate(
//            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name,
//            @RequestParam(value = "description", required = false) org.fenixedu.commons.i18n.LocalizedString description,
//            @RequestParam(value = "language", required = false) java.util.Locale language,
//            @RequestParam(value = "servicerequesttype", required = false) org.fenixedu.academic.domain.serviceRequests.ServiceRequestType serviceRequestType,
//            @RequestParam(value = "degreetype", required = false) org.fenixedu.academic.domain.degree.DegreeType degreeType,
//            @RequestParam(value = "degree", required = false) org.fenixedu.academic.domain.Degree degree,
//            @RequestParam(value = "programconclusion", required = false) org.fenixedu.academic.domain.degreeStructure.ProgramConclusion programConclusion,
//            @RequestParam(value = "documentTemplateFile", required = true) MultipartFile documentTemplateFile, Model model,
//            RedirectAttributes redirectAttributes) {
//        /*
//        *  Creation Logic
//        */
//
//        try {
//
//            AcademicServiceRequestTemplate academicServiceRequestTemplate =
//                    createAcademicServiceRequestTemplate(name, description, language, serviceRequestType, degreeType, degree,
//                            programConclusion, documentTemplateFile);
//
//            //Success Validation
//            //Add the bean to be used in the View
//            model.addAttribute("academicServiceRequestTemplate", academicServiceRequestTemplate);
//            return redirect("/qubdocsreports/documenttemplates/academicservicerequesttemplate/readtemplate/"
//                    + getAcademicServiceRequestTemplate(model).getExternalId(), model, redirectAttributes);
//        } catch (Exception de) {
//
//            // @formatter: off
//            /*
//             * If there is any error in validation 
//             *
//             * Add a error / warning message
//             * 
//             * addErrorMessage(BundleUtil.getString(QubdocsSpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
//             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
//            // @formatter: on
//
//            addErrorMessage(
//                    BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "label.error.create")
//                            + de.getLocalizedMessage(), model);
//            return createstandardtemplate(model);
//        }
//    }

    @Atomic
    public AcademicServiceRequestTemplate createAcademicServiceRequestTemplate(org.fenixedu.commons.i18n.LocalizedString name,
            org.fenixedu.commons.i18n.LocalizedString description, java.util.Locale language,
            org.fenixedu.academic.domain.serviceRequests.ServiceRequestType serviceRequestType,
            org.fenixedu.academic.domain.degree.DegreeType degreeType, org.fenixedu.academic.domain.Degree degree,
            org.fenixedu.academic.domain.degreeStructure.ProgramConclusion programConclusion, MultipartFile documentTemplateFile)
            throws IOException {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         * 
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        //AcademicServiceRequestTemplate academicServiceRequestTemplate = academicServiceRequestTemplate.create(fields_to_create);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        AcademicServiceRequestTemplate academicServiceRequestTemplate =
                AcademicServiceRequestTemplate.create(name, description, language, serviceRequestType, degreeType,
                        programConclusion, degree);
        DocumentTemplateFile.create(academicServiceRequestTemplate, documentTemplateFile.getOriginalFilename(),
                documentTemplateFile.getBytes());
        return academicServiceRequestTemplate;
    }

//                  
    private static final String _CREATECUSTOMTEMPLATE_URI = "/createcustomtemplate";
    public static final String CREATECUSTOMTEMPLATE_URL = CONTROLLER_URL + _CREATECUSTOMTEMPLATE_URI;

    @RequestMapping(value = _CREATECUSTOMTEMPLATE_URI, method = RequestMethod.GET)
    public String createcustomtemplate(Model model) {
        model.addAttribute("AcademicServiceRequestTemplate_language_options", CoreConfiguration.supportedLocales());
        model.addAttribute(
                "AcademicServiceRequestTemplate_serviceRequestType_options",
                org.fenixedu.academic.domain.serviceRequests.ServiceRequestType.findActive()
                        .sorted(Comparator.comparing(ServiceRequestType::getName)).collect(Collectors.toList()));

        return "qubdocsreports/documenttemplates/academicservicerequesttemplate/createcustomtemplate";
    }

    @RequestMapping(value = _CREATECUSTOMTEMPLATE_URI, method = RequestMethod.POST)
    public String createcustomtemplate(@RequestParam(value = "name", required = true) LocalizedString name, @RequestParam(
            value = "description", required = true) LocalizedString description, @RequestParam(value = "language",
            required = true) Locale language,
            @RequestParam(value = "servicerequesttype", required = true) ServiceRequestType serviceRequestType, @RequestParam(
                    value = "documentTemplateFile", required = true) MultipartFile documentTemplateFile, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            AcademicServiceRequestTemplate academicServiceRequestTemplate =
                    createAcademicServiceRequestTemplate(name, description, language, serviceRequestType, documentTemplateFile);

            model.addAttribute("academicServiceRequestTemplate", academicServiceRequestTemplate);
            return redirect("/qubdocsreports/documenttemplates/academicservicerequesttemplate/readtemplate/"
                    + getAcademicServiceRequestTemplate(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {
            addErrorMessage(
                    BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "label.error.create")
                            + de.getLocalizedMessage(), model);
        }
        return createcustomtemplate(model);
    }

    @Atomic
    public AcademicServiceRequestTemplate createAcademicServiceRequestTemplate(LocalizedString name, LocalizedString description,
            java.util.Locale language, ServiceRequestType serviceRequestType, MultipartFile documentTemplateFile)
            throws IOException {
        AcademicServiceRequestTemplate academicServiceRequestTemplate =
                AcademicServiceRequestTemplate.createCustom(name, description, language, serviceRequestType);
        DocumentTemplateFile.create(academicServiceRequestTemplate, documentTemplateFile.getOriginalFilename(),
                documentTemplateFile.getBytes());
        return academicServiceRequestTemplate;
    }

//                  
    private static final String _READTEMPLATE_URI = "/readtemplate/";
    public static final String READTEMPLATE_URL = CONTROLLER_URL + _READTEMPLATE_URI;

    @RequestMapping(value = _READTEMPLATE_URI + "{oid}")
    public String readtemplate(@PathVariable("oid") AcademicServiceRequestTemplate academicServiceRequestTemplate, Model model) {
        setAcademicServiceRequestTemplate(academicServiceRequestTemplate, model);
        return "qubdocsreports/documenttemplates/academicservicerequesttemplate/readtemplate";
    }

    //
    private static final String _DELETETEMPLATE_URI = "/deletetemplate/";
    public static final String DELETETEMPLATE_URL = CONTROLLER_URL + _DELETETEMPLATE_URI;

    @RequestMapping(value = _DELETETEMPLATE_URI + "{oid}", method = RequestMethod.POST)
    public String deletetemplate(@PathVariable("oid") AcademicServiceRequestTemplate academicServiceRequestTemplate, Model model,
            RedirectAttributes redirectAttributes) {

        setAcademicServiceRequestTemplate(academicServiceRequestTemplate, model);
        try {
            String templateName = academicServiceRequestTemplate.getName().getContent();
            deleteAcademicServiceRequestTemplate(academicServiceRequestTemplate);

            addInfoMessage(BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE,
                    "label.info.documentTemplates.successfulDelete", templateName), model);
            return redirect("/qubdocsreports/documenttemplates/academicservicerequesttemplate/searchtemplates", model,
                    redirectAttributes);
        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(
                    BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "label.error.delete")
                            + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return "qubdocsreports/documenttemplates/academicservicerequesttemplate/readtemplate/"
                + getAcademicServiceRequestTemplate(model).getExternalId();
    }

//                  
    private static final String _UPDATETEMPLATE_URI = "/updatetemplate/";
    public static final String UPDATETEMPLATE_URL = CONTROLLER_URL + _UPDATETEMPLATE_URI;

    @RequestMapping(value = _UPDATETEMPLATE_URI + "{oid}", method = RequestMethod.GET)
    public String updatetemplate(@PathVariable("oid") AcademicServiceRequestTemplate academicServiceRequestTemplate, Model model) {
        setAcademicServiceRequestTemplate(academicServiceRequestTemplate, model);

        //IF ANGULAR, initialize the Bean
        //AcademicServiceRequestTemplateBean bean = new AcademicServiceRequestTemplateBean(academicServiceRequestTemplate);
        //this.setAcademicServiceRequestTemplateBean(bean, model);

        return "qubdocsreports/documenttemplates/academicservicerequesttemplate/updatetemplate";

    }

    //

//                   THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
    //
//                          // @formatter: off
//              
//                  private static final String _UPDATETEMPLATEPOSTBACK_URI ="/updatetemplatepostback/";
//                  public static final String  UPDATETEMPLATEPOSTBACK_URL = CONTROLLER_URL + _UPDATETEMPLATEPOSTBACK_URI;
//                  @RequestMapping(value = _UPDATETEMPLATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//                      public @ResponseBody ResponseEntity<String> updatetemplatepostback(@PathVariable("oid") AcademicServiceRequestTemplate academicServiceRequestTemplate, @RequestParam(value = "bean", required = false) AcademicServiceRequestTemplateBean bean,
//                      Model model) {
    //
//                      // Do validation logic ?!?!
//                      //if (something_wrong){
//                      //                 return new ResponseEntity<String>(<MESSAGE_FROM_BUNDLE>,HttpStatus.BAD_REQUEST);
//                      //}
//                      this.setAcademicServiceRequestTemplateBean(bean, model);
//                      return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
//                  } 
//                  
//                  @RequestMapping(value = _UPDATETEMPLATE_URI + "{oid}", method = RequestMethod.POST)
//                      public String updatetemplate(@PathVariable("oid") AcademicServiceRequestTemplate academicServiceRequestTemplate, @RequestParam(value = "bean", required = false) AcademicServiceRequestTemplateBean bean,
//                      Model model, RedirectAttributes redirectAttributes ) {
//                      setAcademicServiceRequestTemplate(academicServiceRequestTemplate,model);
    //
//                       try
//                       {
//                      /*
//                      *  UpdateLogic here
//                      */
//                              
//                          updateAcademicServiceRequestTemplate( .. get fields from bean..., model);
    //
//                      /*Succes Update */
    //
//                      return redirect("/qubdocsreports/documenttemplates/academicservicerequesttemplate/updatetemplate/" + getAcademicServiceRequestTemplate(model).getExternalId(), model, redirectAttributes);
//                      }
//                      catch (Exception de) 
//                      {
//                  
//                          /*
//                          * If there is any error in validation 
//                          *
//                          * Add a error / warning message
//                          * 
//                          * addErrorMessage(BundleUtil.getString(QubdocsSpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
//                          * addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
//                          */
//                                               
//                          addErrorMessage(BundleUtil.getString(QubdocsSpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
//                          setAcademicServiceRequestTemplate(academicServiceRequestTemplate, model);
//                          this.setAcademicServiceRequestTemplateBean(bean, model);
    //
//                          return "qubdocsreports/documenttemplates/academicservicerequesttemplate/updatetemplate";
//                      }
//                  }
//                          // @formatter: on               
//                  
    @RequestMapping(value = _UPDATETEMPLATE_URI + "{oid}", method = RequestMethod.POST)
    public String updatetemplate(@PathVariable("oid") AcademicServiceRequestTemplate academicServiceRequestTemplate,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, @RequestParam(
                    value = "description", required = false) org.fenixedu.commons.i18n.LocalizedString description,
            @RequestParam(value = "active", required = false) java.lang.Boolean active, Model model,
            RedirectAttributes redirectAttributes) {

        setAcademicServiceRequestTemplate(academicServiceRequestTemplate, model);

        try {
            /*
            *  UpdateLogic here
            */

            updateAcademicServiceRequestTemplate(name, description, active, model);

            /*Succes Update */

            return redirect("/qubdocsreports/documenttemplates/academicservicerequesttemplate/updatetemplate/"
                    + getAcademicServiceRequestTemplate(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {
            // @formatter: off

            /*
            * If there is any error in validation 
            *
            * Add a error / warning message
            * 
            * addErrorMessage(BundleUtil.getString(QubdocsSpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
            * addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
            */
            // @formatter: on

            addErrorMessage(
                    BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "label.error.update")
                            + de.getLocalizedMessage(), model);
            return updatetemplate(academicServiceRequestTemplate, model);

        }
    }

    @Atomic
    public void updateAcademicServiceRequestTemplate(org.fenixedu.commons.i18n.LocalizedString name,
            org.fenixedu.commons.i18n.LocalizedString description, java.lang.Boolean active, Model model) {

        // @formatter: off              
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getAcademicServiceRequestTemplate(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getAcademicServiceRequestTemplate(model).setName(name);
        getAcademicServiceRequestTemplate(model).setDescription(description);
        getAcademicServiceRequestTemplate(model).setActive(active);
    }

    @RequestMapping(value = "/search/download/{documentTemplateFileId}", method = RequestMethod.GET)
    public void processSearchToDownloadAction(@PathVariable("documentTemplateFileId") DocumentTemplateFile documentTemplateFile,
            HttpServletResponse response) {
        try {
            response.setContentType(documentTemplateFile.getContentType());
            String filename =
                    URLEncoder.encode(StringNormalizer.normalizePreservingCapitalizedLetters(documentTemplateFile.getFilename())
                            .replaceAll("\\s", "_"), "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.getOutputStream().write(documentTemplateFile.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}