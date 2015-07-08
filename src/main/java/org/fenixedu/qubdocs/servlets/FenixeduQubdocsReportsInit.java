package org.fenixedu.qubdocs.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentPurposeTypeInstance;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

@WebServlet(value = "/FenixeduQubdocsReportsInitServlet", name = "FenixeduQubdocsReportsInitServlet", loadOnStartup = 6)
public class FenixeduQubdocsReportsInit extends HttpServlet {

    static final private String PLUGIN_NAME = "FenixeduQubdocsReports";
    static final public String BUNDLE = "resources/" + PLUGIN_NAME + "Resources";

    public String getPluginName() {
        return PLUGIN_NAME;
    }

    private static final long serialVersionUID = 1L;

    @Atomic(mode = Atomic.TxMode.READ, flattenNested = false)
    @Override
    public void init() throws ServletException {
        super.init();
        /*
         * Populate all Initial set of DocumentPurposeTypeInstance <-> DocumentPurposeType
         */
        for (DocumentPurposeType type : DocumentPurposeType.values()) {
            if (DocumentPurposeTypeInstance.findUnique(type) == null) {
                createTypeInstance(type);
            }
        }
    }

    @Atomic
    private void createTypeInstance(DocumentPurposeType type) {
        DocumentPurposeTypeInstance typeInstance =
                DocumentPurposeTypeInstance.create(type.name(),
                        BundleUtil.getLocalizedString("resources/EnumerationResources", type.name()), type);
        typeInstance.setActive(true);
    }
}
