/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: anil.mamede@qub-it.com
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

package org.fenixedu.qubdocs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.fenixedu.qubdocs.domain.DocumentTemplate;
import org.fenixedu.qubdocs.util.reports.helpers.DateHelper;
import org.fenixedu.qubdocs.util.reports.helpers.EnumerationHelper;
import org.fenixedu.qubdocs.util.reports.helpers.LanguageHelper;
import org.fenixedu.qubdocs.util.reports.helpers.MoneyHelper;
import org.fenixedu.qubdocs.util.reports.helpers.NumbersHelper;
import org.fenixedu.qubdocs.util.reports.helpers.SortHelper;
import org.fenixedu.qubdocs.util.reports.helpers.StringsHelper;

import com.qubit.terra.docs.core.DocumentTemplateEngine;
import com.qubit.terra.docs.util.IReportDataProvider;
import com.qubit.terra.docs.util.ReportGenerationException;
import com.qubit.terra.docs.util.ReportGenerator;

public class FenixEduDocumentGenerator extends ReportGenerator {

    protected FenixEduDocumentGenerator(final DocumentTemplate documentTemplate, final String mimeType) {
        this(documentTemplate.getDocumentTemplateFile().getContent(), mimeType);
    }

    protected FenixEduDocumentGenerator(final byte[] template, final String mimeType) {
        super(template, DocumentTemplateEngine.getServiceImplementation().getFontsPath(), mimeType);
        registerHelpers();
    }

    private void registerHelpers() {
        registerHelper("dates", new DateHelper());
        registerHelper("lang", new LanguageHelper());
        registerHelper("order", new SortHelper());
        registerHelper("numbers", new NumbersHelper());
        registerHelper("enumeration", new EnumerationHelper());
        registerHelper("strings", new StringsHelper());
        registerHelper("money", new MoneyHelper());
    }

    public FenixEduDocumentGenerator registerDataProviders(final Collection<? extends IReportDataProvider> providers) {
        for (IReportDataProvider provider : providers) {
            registerDataProvider(provider);
        }

        return this;
    }

    public static FenixEduDocumentGenerator create(final byte[] template, final String mimeType) {
        return new FenixEduDocumentGenerator(template, mimeType);
    }

    public static FenixEduDocumentGenerator create(final DocumentTemplate documentTemplate, final String mimeType) {
        return new FenixEduDocumentGenerator(documentTemplate, mimeType);
    }

    public static FenixEduDocumentGenerator create(final String template, final String mimeType) {
        try {
            return new FenixEduDocumentGenerator(FileUtils.readFileToByteArray(new File(template)), mimeType);
        } catch (FileNotFoundException e) {
            throw new ReportGenerationException(
                    "Template file was not found. Maybe this soft refence is inconsistent, try re-upload this template.", e);
        } catch (IOException e) {
            throw new ReportGenerationException("Error retrieving the template.", e);
        }
    }
}
