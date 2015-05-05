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

package org.fenixedu.qubdocs.domain;

import java.util.Collection;
import java.util.Set;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.io.domain.FileStorageConfiguration;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.qubdocs.FenixEduDocumentGenerator;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Predicate;
import com.qubit.terra.docs.core.DocumentTemplateEngine;
import com.qubit.terra.docs.core.IDocumentTemplate;
import com.qubit.terra.docs.util.IReportDataProvider;
import com.qubit.terra.docs.util.ReportGenerator;

public class DocumentTemplate extends DocumentTemplate_Base implements IDocumentTemplate {
    
	protected DocumentTemplate() {
        super();
        setBennu(Bennu.getInstance());
        setCreationDate(new DateTime());
        setUpdateDate(new DateTime());
        setCreator(Authenticate.getUser());
        setUpdater(Authenticate.getUser());
        setActive(true);
    }

    protected DocumentTemplate(final LocalizedString name, final LocalizedString description) {
        this();
        init(name, description);
    }

    protected void init(final LocalizedString name, final LocalizedString description) {
        setName(name);
        setDescription(description);

        checkRules();
    }

    protected void checkRules() {
        check(getName(), "error.DocumentTemplate.name.required");
        check(getDescription(), "error.DocumentTemplate.remarks.required");
        check(getCreationDate(), "error.DocumentTemplate.creationDate.required");
        check(getUpdateDate(), "error.DocumentTemplate.updateDate.required");
        check(getCreator(), "error.DocumentTemplate.creationResponsible.required");
        check(getUpdater(), "error.DocumentTemplate.updateResponsible.required");
    }
    
    private static <T extends Object> void check(T subject, String exceptionCause) {
    	if (subject == null) {
    		throw new DomainException(exceptionCause);
    	}
    }

    @Override
    public boolean isActive() {
        return getActive() != null && getActive();
    }

    @Atomic
    public void edit(final LocalizedString name, final LocalizedString description) {
        setName(name);
        setDescription(description);
        setUpdateDate(new DateTime());
        setUpdater(Authenticate.getUser());

        checkRules();
    }

    @Override
    @Atomic
    public void deactivateDocument() {
        setActive(false);
        checkRules();
    }

    @Override
    @Atomic
    public void activateDocument() {
        setActive(true);
    }

    public Set<? extends DocumentTemplate> readAllDocuments() {
        return (Set<? extends DocumentTemplate>) DocumentTemplateEngine.getServiceImplementation().readAllDocuments();
    }

    public Set<? extends DocumentTemplate> readActiveDocuments() {
        return (Set<? extends DocumentTemplate>) DocumentTemplateEngine.getServiceImplementation().readActiveDocuments();
    }

    public DocumentTemplateVersion createVersion(final String filename, final byte[] content) {
        return DocumentTemplateVersion.create(this, filename, content);
    }

    public FenixEduDocumentGenerator createGeneratorForDocx(final Collection<? extends IReportDataProvider> dataProviders) {
        return createGenerator(dataProviders, ReportGenerator.DOCX);
    }

    public FenixEduDocumentGenerator createGeneratorForPdf(final Collection<? extends IReportDataProvider> dataProviders) {
        return createGenerator(dataProviders, ReportGenerator.PDF);
    }

    public FenixEduDocumentGenerator createGenerator(final Collection<? extends IReportDataProvider> dataProviders,
            final String mimeTypeFormat) {
    	FenixEduDocumentGenerator reportGenerator = FenixEduDocumentGenerator.create(this, mimeTypeFormat);
        reportGenerator.registerDataProviders(dataProviders);
        return reportGenerator;
    }

    public static final boolean isStorageDefined() {
        return FileStorageConfiguration.readFileStorageByFileType(DocumentTemplate.class.getName()) != null;
    }

    /*
     * FILTER PREDICATES
     * 
     */

    public static Filters filters = new Filters();

    public static class Filters {

        public Predicate<DocumentTemplate> active(final Boolean value) {
            return new Predicate<DocumentTemplate>() {
                @Override
                public boolean apply(final DocumentTemplate template) {
                    return template.getActive().equals(value);
                }
            };
        }

    }
    
}
