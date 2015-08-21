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
package org.fenixedu.qubdocs.ui.documentpurposetypes;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.joda.time.DateTime;

import java.util.stream.Collectors;

import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.stereotype.Component;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.FenixeduQubdocsReportsSpringConfiguration;

import pt.ist.fenixframework.Atomic;

import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsBaseController;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsController;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeTypeInstance;

//@Component("org.fenixedu.qubdocs.ui.documentPurposeTypes") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = FenixeduQubdocsReportsController.class, title = "label.title.documentPurposeTypes",
        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value=FenixeduQubdocsReportsController.class) 
@RequestMapping(DocumentPurposeTypeInstanceController.CONTROLLER_URL)
public class DocumentPurposeTypeInstanceController extends FenixeduQubdocsReportsBaseController {

    public static final String CONTROLLER_URL = "/qubdocsreports/documentpurposetypes/documentpurposetypeinstance";

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:" + CONTROLLER_URL + "/";
    }

    // @formatter: off

    /*
    * This should be used when using AngularJS in the JSP
    */

    //private DocumentPurposeTypeInstanceBean getDocumentPurposeTypeInstanceBean(Model model)
    //{
    //	return (DocumentPurposeTypeInstanceBean)model.asMap().get("documentPurposeTypeInstanceBean");
    //}
    //				
    //private void setDocumentPurposeTypeInstanceBean (DocumentPurposeTypeInstanceBean bean, Model model)
    //{
    //	model.addAttribute("documentPurposeTypeInstanceBeanJson", getBeanJson(bean));
    //	model.addAttribute("documentPurposeTypeInstanceBean", bean);
    //}

    // @formatter: on

    private DocumentPurposeTypeInstance getDocumentPurposeTypeInstance(Model model) {
        return (DocumentPurposeTypeInstance) model.asMap().get("documentPurposeTypeInstance");
    }

    private void setDocumentPurposeTypeInstance(DocumentPurposeTypeInstance documentPurposeTypeInstance, Model model) {
        model.addAttribute("documentPurposeTypeInstance", documentPurposeTypeInstance);
    }

    @Atomic
    public void deleteDocumentPurposeTypeInstance(DocumentPurposeTypeInstance documentPurposeTypeInstance) {
        // CHANGE_ME: Do the processing for deleting the documentPurposeTypeInstance
        // Do not catch any exception here

        documentPurposeTypeInstance.delete();
    }

//				
    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(
            @RequestParam(value = "code", required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name,
            @RequestParam(value = "documentpurposetype", required = false) org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType documentPurposeType,
            @RequestParam(value = "active", required = false) java.lang.Boolean active, Model model) {
        List<DocumentPurposeTypeInstance> searchdocumentpurposetypeinstanceResultsDataSet =
                filterSearchDocumentPurposeTypeInstance(code, name, documentPurposeType, active);

        //add the results dataSet to the model
        model.addAttribute("searchdocumentpurposetypeinstanceResultsDataSet", searchdocumentpurposetypeinstanceResultsDataSet);
        model.addAttribute("documentPurposeTypeValues",
                org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType.values());
        return "qubdocsreports/documentpurposetypes/documentpurposetypeinstance/search";
    }

    private Stream<DocumentPurposeTypeInstance> getSearchUniverseSearchDocumentPurposeTypeInstanceDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return DocumentPurposeTypeInstance.findAll();
    }

    private List<DocumentPurposeTypeInstance> filterSearchDocumentPurposeTypeInstance(java.lang.String code,
            org.fenixedu.commons.i18n.LocalizedString name,
            org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType documentPurposeType,
            java.lang.Boolean active) {

        return getSearchUniverseSearchDocumentPurposeTypeInstanceDataSet()
                .filter(documentPurposeTypeInstance -> code == null
                        || code.length() == 0
                        || (documentPurposeTypeInstance.getCode() != null && documentPurposeTypeInstance.getCode().length() > 0 && documentPurposeTypeInstance
                                .getCode().toLowerCase().contains(code.toLowerCase())))
                .filter(documentPurposeTypeInstance -> name == null
                        || name.isEmpty()
                        || name.getLocales()
                                .stream()
                                .allMatch(
                                        locale -> documentPurposeTypeInstance.getName().getContent(locale) != null
                                                && documentPurposeTypeInstance.getName().getContent(locale).toLowerCase()
                                                        .contains(name.getContent(locale).toLowerCase())))
                .filter(documentPurposeTypeInstance -> documentPurposeType == null
                        || documentPurposeType.equals(documentPurposeTypeInstance.getDocumentPurposeType()))
                .filter(documentPurposeTypeInstance -> active == null || active.equals(documentPurposeTypeInstance.getActive()))
                .collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") DocumentPurposeTypeInstance documentPurposeTypeInstance,
            Model model, RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/qubdocsreports/documentpurposetypes/documentpurposetypeinstance/read" + "/"
                + documentPurposeTypeInstance.getExternalId(), model, redirectAttributes);
    }

//				
    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") DocumentPurposeTypeInstance documentPurposeTypeInstance, Model model) {
        setDocumentPurposeTypeInstance(documentPurposeTypeInstance, model);
        return "qubdocsreports/documentpurposetypes/documentpurposetypeinstance/read";
    }

//
    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") DocumentPurposeTypeInstance documentPurposeTypeInstance, Model model,
            RedirectAttributes redirectAttributes) {

        setDocumentPurposeTypeInstance(documentPurposeTypeInstance, model);
        try {
            String code = documentPurposeTypeInstance.getCode();
            //call the Atomic delete function
            deleteDocumentPurposeTypeInstance(documentPurposeTypeInstance);
            addInfoMessage(BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE,
                    "label.info.documentPurposeTypes.successfulDelete", code), model);
            return redirect("/qubdocsreports/documentpurposetypes/documentpurposetypeinstance/", model, redirectAttributes);
        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(
                    BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "label.error.delete")
                            + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return "qubdocsreports/documentpurposetypes/documentpurposetypeinstance/read/"
                + getDocumentPurposeTypeInstance(model).getExternalId();
    }

//				
    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("documentPurposeTypeValues",
                org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType.values());
        model.addAttribute(
                "DocumentPurposeTypeInstance_serviceRequestTypes_options",
                Stream.concat(ServiceRequestType.findDeclarations(), ServiceRequestType.findCertificates()).collect(
                        Collectors.toList()));

        //IF ANGULAR, initialize the Bean
        //DocumentPurposeTypeInstanceBean bean = new DocumentPurposeTypeInstanceBean();
        //this.setDocumentPurposeTypeInstanceBean(bean, model);

        return "qubdocsreports/documentpurposetypes/documentpurposetypeinstance/create";
    }

//
//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _CREATEPOSTBACK_URI ="/createpostback";
//				public static final String  CREATEPOSTBACK_URL = CONTROLLER_URL + _CREATEPOSTBACK_URI;
//    			@RequestMapping(value = _CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String createpostback(@RequestParam(value = "bean", required = false) DocumentPurposeTypeInstanceBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//                  //if (something_wrong){
//                  //                 return new ResponseEntity<String>(<MESSAGE_FROM_BUNDLE>,HttpStatus.BAD_REQUEST);
//                  //}
//        			this.setDocumentPurposeTypeInstanceBean(bean, model);
//					return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
//    			}
//    			
//    			@RequestMapping(value = CREATE, method = RequestMethod.POST)
//  			  	public String create(@RequestParam(value = "bean", required = false) DocumentPurposeTypeInstanceBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//
//					/*
//					*  Creation Logic
//					*/
//					
//					try
//					{
//
//				     	DocumentPurposeTypeInstance documentPurposeTypeInstance = createDocumentPurposeTypeInstance(... get properties from bean ...,model);
//				    	
//					//Success Validation
//				     //Add the bean to be used in the View
//					model.addAttribute("documentPurposeTypeInstance",documentPurposeTypeInstance);
//				    return redirect("/qubdocs/documentpurposetypes/documentpurposetypeinstance/read/" + getDocumentPurposeTypeInstance(model).getExternalId(), model, redirectAttributes);
//					}
//					catch (Exception de)
//					{
//
//						/*
//						 * If there is any error in validation 
//					     *
//					     * Add a error / warning message
//					     * 
//					     * addErrorMessage(BundleUtil.getString(QubdocsSpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
//					     * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
//						
//						addErrorMessage(BundleUtil.getString(QubdocsSpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
//						this.setDocumentPurposeTypeInstanceBean(bean, model);				
//						return "qubdocs/documentpurposetypes/documentpurposetypeinstance/create";
//                      
//					}
//    			}
//						// @formatter: on

//				
    @RequestMapping(value = _CREATE_URI, method = RequestMethod.POST)
    public String create(
            @RequestParam(value = "code", required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name,
            @RequestParam(value = "documentpurposetype", required = false) org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType documentPurposeType,
            @RequestParam(value = "active", required = false) java.lang.Boolean active, @RequestParam(
                    value = "serviceRequestTypes", required = false) List<ServiceRequestType> serviceRequestTypes, Model model,
            RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        */

        try {

            DocumentPurposeTypeInstance documentPurposeTypeInstance =
                    createDocumentPurposeTypeInstance(code, name, documentPurposeType, active, serviceRequestTypes);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("documentPurposeTypeInstance", documentPurposeTypeInstance);
            return redirect("/qubdocsreports/documentpurposetypes/documentpurposetypeinstance/read/"
                    + getDocumentPurposeTypeInstance(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            // @formatter: off
            /*
             * If there is any error in validation 
             *
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(QubdocsSpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
            // @formatter: on

            addErrorMessage(
                    BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "label.error.create")
                            + de.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public DocumentPurposeTypeInstance createDocumentPurposeTypeInstance(java.lang.String code,
            org.fenixedu.commons.i18n.LocalizedString name,
            org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType documentPurposeType,
            java.lang.Boolean active, List<ServiceRequestType> serviceRequestTypes) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         * 
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        //DocumentPurposeTypeInstance documentPurposeTypeInstance = documentPurposeTypeInstance.create(fields_to_create);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        DocumentPurposeTypeInstance documentPurposeTypeInstance =
                DocumentPurposeTypeInstance.create(code, name, documentPurposeType);
        documentPurposeTypeInstance.setActive(active);
        for (ServiceRequestType serviceRequestType : serviceRequestTypes) {
            documentPurposeTypeInstance.addServiceRequestTypes(serviceRequestType);
        }

        return documentPurposeTypeInstance;
    }

//				
    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") DocumentPurposeTypeInstance documentPurposeTypeInstance, Model model) {
        model.addAttribute("documentPurposeTypeValues",
                org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType.values());
        model.addAttribute(
                "DocumentPurposeTypeInstance_serviceRequestTypes_options",
                Stream.concat(ServiceRequestType.findDeclarations(), ServiceRequestType.findCertificates()).collect(
                        Collectors.toList()));
        setDocumentPurposeTypeInstance(documentPurposeTypeInstance, model);

        //IF ANGULAR, initialize the Bean
        //DocumentPurposeTypeInstanceBean bean = new DocumentPurposeTypeInstanceBean(documentPurposeTypeInstance);
        //this.setDocumentPurposeTypeInstanceBean(bean, model);

        return "qubdocsreports/documentpurposetypes/documentpurposetypeinstance/update";

    }

//

//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _UPDATEPOSTBACK_URI ="/updatepostback/";
//				public static final String  UPDATEPOSTBACK_URL = CONTROLLER_URL + _UPDATEPOSTBACK_URI;
//    			@RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody ResponseEntity<String> updatepostback(@PathVariable("oid") DocumentPurposeTypeInstance documentPurposeTypeInstance, @RequestParam(value = "bean", required = false) DocumentPurposeTypeInstanceBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//                  //if (something_wrong){
//                  //                 return new ResponseEntity<String>(<MESSAGE_FROM_BUNDLE>,HttpStatus.BAD_REQUEST);
//                  //}
//        			this.setDocumentPurposeTypeInstanceBean(bean, model);
//					return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
//    			} 
//    			
//    			@RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
//  			  	public String update(@PathVariable("oid") DocumentPurposeTypeInstance documentPurposeTypeInstance, @RequestParam(value = "bean", required = false) DocumentPurposeTypeInstanceBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//					setDocumentPurposeTypeInstance(documentPurposeTypeInstance,model);
//
//				     try
//				     {
//					/*
//					*  UpdateLogic here
//					*/
//				    		
//						updateDocumentPurposeTypeInstance( .. get fields from bean..., model);
//
//					/*Succes Update */
//
//				    return redirect("/qubdocs/documentpurposetypes/documentpurposetypeinstance/read/" + getDocumentPurposeTypeInstance(model).getExternalId(), model, redirectAttributes);
//					}
//					catch (Exception de) 
//					{
//				
//						/*
//					 	* If there is any error in validation 
//				     	*
//				     	* Add a error / warning message
//				     	* 
//				     	* addErrorMessage(BundleUtil.getString(QubdocsSpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
//				     	* addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
//				     	*/
//										     
//				     	addErrorMessage(BundleUtil.getString(QubdocsSpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
//						setDocumentPurposeTypeInstance(documentPurposeTypeInstance, model);
//						this.setDocumentPurposeTypeInstanceBean(bean, model);
//
//						return "qubdocs/documentpurposetypes/documentpurposetypeinstance/update";
//					}
//				}
//						// @formatter: on    			
//				
    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(
            @PathVariable("oid") DocumentPurposeTypeInstance documentPurposeTypeInstance,
            @RequestParam(value = "code", required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name,
            @RequestParam(value = "documentpurposetype", required = false) org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType documentPurposeType,
            @RequestParam(value = "active", required = false) java.lang.Boolean active, @RequestParam(
                    value = "serviceRequestTypes", required = false) List<ServiceRequestType> serviceRequestTypes, Model model,
            RedirectAttributes redirectAttributes) {

        setDocumentPurposeTypeInstance(documentPurposeTypeInstance, model);

        try {
            /*
            *  UpdateLogic here
            */

            updateDocumentPurposeTypeInstance(code, name, documentPurposeType, active, serviceRequestTypes, model);

            /*Succes Update */

            return redirect("/qubdocsreports/documentpurposetypes/documentpurposetypeinstance/read/"
                    + getDocumentPurposeTypeInstance(model).getExternalId(), model, redirectAttributes);
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
            return update(documentPurposeTypeInstance, model);

        }
    }

    @Atomic
    public void updateDocumentPurposeTypeInstance(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name,
            org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType documentPurposeType,
            java.lang.Boolean active, List<ServiceRequestType> serviceRequestTypes, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getDocumentPurposeTypeInstance(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getDocumentPurposeTypeInstance(model).edit(code, name, documentPurposeType, active, serviceRequestTypes);
    }

}
