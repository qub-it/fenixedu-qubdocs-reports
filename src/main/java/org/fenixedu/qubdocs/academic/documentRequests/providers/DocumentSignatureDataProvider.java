package org.fenixedu.qubdocs.academic.documentRequests.providers;

import org.fenixedu.qubdocs.domain.DocumentSignature;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class DocumentSignatureDataProvider implements IReportDataProvider {

    private static final String KEY = "documentSignature"; 
    
    @Override
    public boolean handleKey(String key) {
        // TODO Auto-generated method stub
        return KEY.equals(key);
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object valueForKey(String key) {
        
        if (handleKey(key)){
            return DocumentSignature.readAll().iterator().next();
        }
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public void registerFieldsMetadata(IFieldsExporter exporter) {
		// TODO Auto-generated method stub
		
	}

}
