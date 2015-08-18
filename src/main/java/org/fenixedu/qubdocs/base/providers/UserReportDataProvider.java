package org.fenixedu.qubdocs.base.providers;

import org.fenixedu.bennu.core.security.Authenticate;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class UserReportDataProvider implements IReportDataProvider {

    protected static final String KEY = "user";

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key);
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData arg0) {
    }

    @Override
    public Object valueForKey(final String key) {
        if (KEY.equals(key)) {
            return Authenticate.getUser().getPerson();
        }
        return null;
    }

}
