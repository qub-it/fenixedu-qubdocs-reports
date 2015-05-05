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

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;

import com.qubit.terra.docs.core.IDocumentTemplateVersion;

public class DocumentTemplateVersion extends DocumentTemplateVersion_Base implements IDocumentTemplateVersion {

	
	private static final String ROOT_DIR = "DocumentTemplate";

    protected DocumentTemplateVersion() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected DocumentTemplateVersion(final DocumentTemplate documentTemplate, final String filename, final byte[] content) {
        this();
        init(filename, filename, content);
        setDocumentTemplate(documentTemplate);
        setUploader(Authenticate.getUser());

        checkRules();
    }

    protected void checkRules() {
        if (getDocumentTemplate() == null && getHistoricDocumentTemplate() == null) {
            throw new DomainException("error.DocumentTemplateVersion.documentTemplate.required");
        }
    }
    
    @Override
	public boolean isAccessible(User user) {
		//TODO: Should check instead if the user has authorization to deal with documents
		return user == getUploader();
	}

    @Atomic
    public void markAsHistoric() {
        setHistoricDocumentTemplate(getDocumentTemplate());
        setDocumentTemplate(null);

        checkRules();
    }

    @Atomic
    public static DocumentTemplateVersion create(final DocumentTemplate documentTemplate, final String filename,
            final byte[] content) {
        return new DocumentTemplateVersion(documentTemplate, filename, content);
    }
}
