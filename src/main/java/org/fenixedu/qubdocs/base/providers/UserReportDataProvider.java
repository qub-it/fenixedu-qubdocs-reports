package org.fenixedu.qubdocs.base.providers;

import org.fenixedu.bennu.core.security.Authenticate;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;
import com.qubit.terra.docs.util.IReportFieldsProvider;

public class UserReportDataProvider implements IReportDataProvider {

    protected static final String KEY = "user";

    public static final IReportFieldsProvider fields = new IReportFieldsProvider() {

        @Override
        public void registerFieldsMetadata(IFieldsExporter exporter) {
            exporter.registerSimpleField("user.name", "Nome do Utilizador");
            exporter.registerSimpleField("user.username", "Username");
            exporter.registerSimpleField("user.female", "Se é do sexo feminino");
            exporter.registerSimpleField("user.male", "Se é do sexo masculino");
            exporter.registerSimpleField("user.user.email", "Email");
        }

    };

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
