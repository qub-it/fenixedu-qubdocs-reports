package org.fenixedu.academic.util.report;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.qubdocs.base.providers.PersonReportDataProvider;
import org.fenixedu.qubdocs.base.providers.UserReportDataProvider;

import com.qubit.terra.docs.util.FieldsExporter;
import com.qubit.terra.docs.util.IReportFieldsProvider;

public class DocumentFieldsExporter {

    public static final byte[] exportFields() {
        final List<IReportFieldsProvider> fieldsProviders = new ArrayList<IReportFieldsProvider>();
        fieldsProviders.add(PersonReportDataProvider.fields);
        fieldsProviders.add(UserReportDataProvider.fields);
        //fieldsProviders.add(CurricularPlanInformationReportDataProvider.fields);

        FieldsExporter exporter = new FieldsExporter();
        for (IReportFieldsProvider provider : fieldsProviders) {
            provider.registerFieldsMetadata(exporter);
        }
        return exporter.exportFields();
    }

}
