/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: anil.mamede@qub-it.com
 *               diogo.simoes@qub-it.com
 *
 * 
 * This file is part of FenixEdu QubDocs.
 *
 * FenixEdu QubDocs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu QubDocs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu QubDocs.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fenixedu.academic.util.report;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.ExtraCurricularCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.StandaloneEnrolmentCertificateRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.report.academicAdministrativeOffice.AdministrativeOfficeDocument;
import org.fenixedu.academic.report.academicAdministrativeOffice.DocumentRequestReader;
import org.fenixedu.qubdocs.FenixEduDocumentGenerator;
import org.fenixedu.qubdocs.academic.documentRequests.providers.ApprovementCertificateCurriculumEntries;
import org.fenixedu.qubdocs.academic.documentRequests.providers.ConclusionInformationDataProvider;
import org.fenixedu.qubdocs.academic.documentRequests.providers.CurriculumEntriesDataProvider;
import org.fenixedu.qubdocs.academic.documentRequests.providers.CurriculumEntryRemarksDataProvider;
import org.fenixedu.qubdocs.academic.documentRequests.providers.CurriculumInformationDataProvider;
import org.fenixedu.qubdocs.academic.documentRequests.providers.DegreeCurricularPlanInformationDataProvider;
import org.fenixedu.qubdocs.academic.documentRequests.providers.DocumentSignerDataProvider;
import org.fenixedu.qubdocs.academic.documentRequests.providers.EnrolmentsDataProvider;
import org.fenixedu.qubdocs.academic.documentRequests.providers.ExtraCurricularCoursesDataProvider;
import org.fenixedu.qubdocs.academic.documentRequests.providers.LocalizedDatesProvider;
import org.fenixedu.qubdocs.academic.documentRequests.providers.RegistrationDataProvider;
import org.fenixedu.qubdocs.academic.documentRequests.providers.ServiceRequestDataProvider;
import org.fenixedu.qubdocs.academic.documentRequests.providers.StandaloneCurriculumEntriesDataProvider;
import org.fenixedu.qubdocs.base.providers.PersonReportDataProvider;
import org.fenixedu.qubdocs.base.providers.UserReportDataProvider;
import org.fenixedu.qubdocs.domain.DocumentPrinterConfiguration;
import org.fenixedu.qubdocs.domain.serviceRequests.AcademicServiceRequestTemplate;
import org.joda.time.LocalDate;

import com.qubit.terra.docs.core.DocumentTemplateEngine;
import com.qubit.terra.docs.core.IDocumentTemplateService;

public class DocumentPrinter implements ReportPrinter {

    @Override
    public ReportResult printReports(ReportDescription... reports) throws Exception {
        ReportDescription reportDescription = reports[0];

        if (reportDescription instanceof AdministrativeOfficeDocument) {
            final AdministrativeOfficeDocument document = (AdministrativeOfficeDocument) reportDescription;

            final DocumentRequest documentRequest = DocumentRequestReader.toDocumentRequest(document);
            final ExecutionYear executionYear = documentRequest.getExecutionYear();
            final Registration registration = documentRequest.getRegistration();
            final CycleType requestedCycle = requestedCycle(registration, documentRequest);
            final ProgramConclusion programConclusion = programConclusion(registration, documentRequest);

            final FenixEduDocumentGenerator generator =
                    FenixEduDocumentGenerator.create(AcademicServiceRequestTemplate.findTemplateFor(
                            documentRequest.getLanguage(), documentRequest.getServiceRequestType(),
                            documentRequest.getDegreeType(), programConclusion, documentRequest.getDegree()),
                            FenixEduDocumentGenerator.PDF);

            generator.registerDataProvider(new PersonReportDataProvider(documentRequest.getStudent().getPerson()));
            generator.registerDataProvider(new RegistrationDataProvider(registration));
            generator.registerDataProvider(new LocalizedDatesProvider());
            generator.registerDataProvider(new ServiceRequestDataProvider(DocumentRequestReader.toDocumentRequest(document),
                    executionYear));
            generator.registerDataProvider(new DegreeCurricularPlanInformationDataProvider(registration, requestedCycle,
                    executionYear));
            generator
                    .registerDataProvider(new EnrolmentsDataProvider(registration, executionYear, documentRequest.getLanguage()));

            generator.registerDataProvider(new DocumentSignerDataProvider(documentRequest));

            generator.registerDataProvider(new ConclusionInformationDataProvider(registration, programConclusion));

            generator.registerDataProvider(new CurriculumEntriesDataProvider(registration, requestedCycle,
                    new CurriculumEntryRemarksDataProvider(registration), documentRequest.getLanguage()));

            generator.registerDataProvider(new UserReportDataProvider());

            generator.registerDataProvider(new CurriculumInformationDataProvider(registration, executionYear));

            if (documentRequest instanceof ExtraCurricularCertificateRequest) {
                final ExtraCurricularCertificateRequest extraCurricularCertificateRequest =
                        (ExtraCurricularCertificateRequest) documentRequest;

                generator
                        .registerDataProvider(new ExtraCurricularCoursesDataProvider(extraCurricularCertificateRequest
                                .getEnrolmentsSet(), documentRequest.getLanguage(), new CurriculumEntryRemarksDataProvider(
                                registration)));
            }

            if (documentRequest instanceof StandaloneEnrolmentCertificateRequest) {
                final StandaloneEnrolmentCertificateRequest standaloneEnrolmentCertificateRequest =
                        (StandaloneEnrolmentCertificateRequest) documentRequest;

                generator.registerDataProvider(new StandaloneCurriculumEntriesDataProvider(registration,
                        standaloneEnrolmentCertificateRequest.getEnrolmentsSet(), new CurriculumEntryRemarksDataProvider(
                                registration), documentRequest.getLanguage(), new LocalDate()));
            }

//            if (documentRequest instanceof ApprovementMobilityCertificateRequest) {
//                final ApprovementMobilityCertificateRequest approvementRequest =
//                        (ApprovementMobilityCertificateRequest) documentRequest;
//
//                final ApprovementCertificateCurriculumEntries entriesDataProvider =
//                        new ApprovementCertificateCurriculumEntries(approvementRequest, registration, requestedCycle,
//                                new CurriculumEntryRemarksDataProvider(registration), approvementRequest.getLanguage());
//
//                generator.registerDataProvider(entriesDataProvider);
//            }

            generator.registerDataProvider(new ApprovementCertificateCurriculumEntries(documentRequest, registration,
                    requestedCycle, new CurriculumEntryRemarksDataProvider(registration), documentRequest.getLanguage()));

            if (documentRequest instanceof DegreeFinalizationCertificateRequest) {
                generator.registerDataProvider(new CurriculumEntriesDataProvider(registration, requestedCycle,
                        new CurriculumEntryRemarksDataProvider(registration), documentRequest.getLanguage()));
            }

            final byte[] report = generator.generateReport();

            return new ReportResult(report, "application/PDF", "pdf");
        }

        return null;
    }

    private ProgramConclusion programConclusion(final Registration registration, final DocumentRequest documentRequest) {
        if (documentRequest instanceof RegistryDiplomaRequest) {
            return ((RegistryDiplomaRequest) documentRequest).getProgramConclusion();
        } else if (documentRequest instanceof DiplomaRequest) {
            return ((DiplomaRequest) documentRequest).getProgramConclusion();
        } else if (documentRequest instanceof DegreeFinalizationCertificateRequest) {
            return ((DegreeFinalizationCertificateRequest) documentRequest).getProgramConclusion();
        }

        return null;
    }

    public static synchronized void registerService() {
        ReportsUtils.setPrinter(new DocumentPrinter());
        IDocumentTemplateService service = DocumentPrinterConfiguration.getInstance();
        DocumentTemplateEngine.registerServiceImplementations(service);
    }

    private CycleType requestedCycle(final Registration registration, final DocumentRequest documentRequest) {
        if (registration.isBolonha() && registration.getDegreeType().hasExactlyOneCycleType()) {
            return registration.getDegreeType().getCycleType();
        }

        return null;
    }
}
