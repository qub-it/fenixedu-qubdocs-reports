package org.fenixedu.qubdocs.academic.documentRequests.providers;

import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.qubdocs.domain.serviceRequests.documentRequests.DocumentSigner;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class DocumentSignerDataProvider implements IReportDataProvider {
    protected static final String KEY = "documentSignature";
    protected static final String KEY_MALE = "documentSignerIsMale";
    protected static final String KEY_FEMALE = "documentSignerIsFemale";

    protected AcademicServiceRequest academicServiceRequest;

    public DocumentSignerDataProvider(AcademicServiceRequest academicServiceRequest) {
        this.academicServiceRequest = academicServiceRequest;
    }

    @Override
    public boolean handleKey(String key) {
        return KEY.equals(key) || KEY_MALE.equals(key) || KEY_FEMALE.equals(key);
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object valueForKey(String key) {
        if (KEY.equals(key)) {
            return getSigner();
        } else if (KEY_MALE.equals(key)) {
            return getSigner().getResponsibleGender() == Gender.MALE;
        } else if (KEY_FEMALE.equals(key)) {
            return getSigner().getResponsibleGender() == Gender.FEMALE;
        }
        return null;
    }

    private DocumentSigner getSigner() {
        if (academicServiceRequest.getDocumentSigner() != null) {
            return academicServiceRequest.getDocumentSigner();
        }
        return DocumentSigner.findDefaultDocumentSignature();
    }

    @Override
    public void registerFieldsMetadata(IFieldsExporter exporter) {
        // TODO Auto-generated method stub

    }

}
