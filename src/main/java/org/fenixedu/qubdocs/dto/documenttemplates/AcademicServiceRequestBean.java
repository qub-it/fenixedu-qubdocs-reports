package org.fenixedu.qubdocs.dto.documenttemplates;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentSigner;
import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.qubdocs.domain.serviceRequests.AcademicServiceRequestTemplate;

public class AcademicServiceRequestBean implements IBean {

    private DocumentSigner documentSigner;
    private List<TupleDataSourceBean> documentSignerDataSource;
    private AcademicServiceRequestTemplate academicServiceRequestTemplate;
    private List<TupleDataSourceBean> academicServiceRequestTemplateDataSource;

    public DocumentSigner getDocumentSigner() {
        return documentSigner;
    }

    public void setDocumentSigner(DocumentSigner value) {
        documentSigner = value;
    }

    public List<TupleDataSourceBean> getDocumentSignerDataSource() {
        return documentSignerDataSource;
    }

    public void setDocumentSignerDataSource(List<DocumentSigner> value) {
        this.documentSignerDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getResponsibleName() + " - " + x.getResponsibleFunction().getContent());
            return tuple;
        }).collect(Collectors.toList());
    }

    public AcademicServiceRequestTemplate getAcademicServiceRequestTemplate() {
        return academicServiceRequestTemplate;
    }

    public void setAcademicServiceRequestTemplate(AcademicServiceRequestTemplate value) {
        academicServiceRequestTemplate = value;
    }

    public List<TupleDataSourceBean> getAcademicServiceRequestTemplateDataSource() {
        return academicServiceRequestTemplateDataSource;
    }

    public void setAcademicServiceRequestTemplateDataSource(Set<AcademicServiceRequestTemplate> value) {
        this.academicServiceRequestTemplateDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getName().getContent());
            return tuple;
        }).collect(Collectors.toList());
    }

    public AcademicServiceRequestBean() {
        List<DocumentSigner> documentSignatures =
                DocumentSigner.findAll()
                        .filter(ds -> ds.getAdministrativeOffice() == AdministrativeOffice.readDegreeAdministrativeOffice())
                        .sorted(DocumentSigner.DEFAULT_COMPARATOR).collect(Collectors.toList());
        setDocumentSignerDataSource(documentSignatures);
        setDocumentSigner(documentSignatures.get(0));
    }

    public AcademicServiceRequestBean(DocumentRequest documentRequest) {
        this();
        AcademicServiceRequestTemplate standardTemplate =
                AcademicServiceRequestTemplate.findTemplateFor(documentRequest.getLanguage(),
                        documentRequest.getServiceRequestType(), documentRequest.getDegreeType(), null,
                        documentRequest.getDegree());
        Set<AcademicServiceRequestTemplate> templates =
                AcademicServiceRequestTemplate.readCustomTemplatesFor(documentRequest.getLanguage(),
                        documentRequest.getServiceRequestType());
        if (standardTemplate != null) {
            templates.add(standardTemplate);
        }
        setAcademicServiceRequestTemplateDataSource(templates);
        setAcademicServiceRequestTemplate(standardTemplate);
    }
}
