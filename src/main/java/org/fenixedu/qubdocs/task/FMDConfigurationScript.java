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

package org.fenixedu.qubdocs.task;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeTypeInstance;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentSigner;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.commons.i18n.LocalizedString;

public class FMDConfigurationScript extends CustomTask {

    final private static Locale pt = new Locale("pt", "PT");
    final private static Locale en = new Locale("en", "GB");

    @Override
    public void runTask() throws Exception {
        Map<String, LocalizedString> documentPurposeTypes = new HashMap<String, LocalizedString>();
        documentPurposeTypes.put("FAMILY_ALLOWANCE", new LocalizedString(pt, "Abono de Família").with(en, "Family Allowance"));
        documentPurposeTypes.put("MILITARY", new LocalizedString(pt, "Fins Militares").with(en, "Military Purposes"));
        documentPurposeTypes.put("PUBLIC_TRANSPORTS",
                new LocalizedString(pt, "Transportes Públicos").with(en, "Public Transports"));
        documentPurposeTypes.put("STUDY_SCHOLARSHIP", new LocalizedString(pt, "Bolsa de Estudo").with(en, "Scholarship"));
        documentPurposeTypes.put("PROFESSIONAL", new LocalizedString(pt, "Fins Profissionais").with(en, "Professional purposes"));
        documentPurposeTypes.put("PPRE", new LocalizedString(pt, "PPRE").with(en, "PPRE"));
        documentPurposeTypes.put("IMMIGRATION", new LocalizedString(pt, "SEF").with(en, "Immigration"));
        documentPurposeTypes.put("OTHER", new LocalizedString(pt, "Outra").with(en, "Other"));

        for (Entry<String, LocalizedString> entry : documentPurposeTypes.entrySet()) {
            DocumentPurposeTypeInstance dpti;
            if (DocumentPurposeTypeInstance.findUnique(entry.getKey()) == null) {
                // Enum availability must be tested through Exception handling unfortunetely
                try {
                    DocumentPurposeType documentPurposeType = DocumentPurposeType.valueOf(entry.getKey());
                    dpti = DocumentPurposeTypeInstance.create(entry.getKey(), entry.getValue(), documentPurposeType);
                } catch (IllegalArgumentException iae) {
                    dpti = DocumentPurposeTypeInstance.create(entry.getKey(), entry.getValue());
                }
            } else {
                dpti = DocumentPurposeTypeInstance.findUnique(entry.getKey());
                dpti.setName(entry.getValue());
            }
            dpti.setActive(true);
            for (ServiceRequestType srt : dpti.getServiceRequestTypesSet()) {
                dpti.removeServiceRequestTypes(srt);
            }
            dpti.addServiceRequestTypes(ServiceRequestType.findUniqueByCode("SCHOOL_REGISTRATION_CERTIFICATE").get());
            dpti.addServiceRequestTypes(ServiceRequestType.findUniqueByCode("SCHOOL_REGISTRATION_DECLARATION").get());
            dpti.addServiceRequestTypes(ServiceRequestType.findUniqueByCode("ENROLMENT_CERTIFICATE").get());
            dpti.addServiceRequestTypes(ServiceRequestType.findUniqueByCode("ENROLMENT_DECLARATION").get());
        }
        DocumentPurposeTypeInstance.findActives().filter(dpti -> !documentPurposeTypes.containsKey(dpti.getCode()))
                .forEach(dpti -> dpti.setActive(false));

        /* DocumentSigners */
        DocumentSigner.create(AdministrativeOffice.readDegreeAdministrativeOffice(), "Conceição Manso", "Conceição Manso",
                new LocalizedString(pt, "Responsável dos Serviços Académicos"), new LocalizedString(pt,
                        "Faculdade de Medicina Dentária"), Gender.FEMALE);
        DocumentSigner.create(AdministrativeOffice.readDegreeAdministrativeOffice(), "Eduardo Brunheta", "Eduardo Brunheta",
                new LocalizedString(pt, "Assistente Técnico"), new LocalizedString(pt, "Faculdade de Medicina Dentária"),
                Gender.MALE);
        DocumentSigner.create(AdministrativeOffice.readDegreeAdministrativeOffice(), "Cristina Fernandes", "Cristina Fernandes",
                new LocalizedString(pt, "Diretora Executiva"), new LocalizedString(pt, "Faculdade de Medicina Dentária"),
                Gender.FEMALE);
    }

}