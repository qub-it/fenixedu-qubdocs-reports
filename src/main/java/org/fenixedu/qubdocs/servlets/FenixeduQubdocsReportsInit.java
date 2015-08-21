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
