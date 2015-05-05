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

package org.fenixedu.qubdocs.academic.documentRequests.providers;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class DocumentRequestSignatureDataProvider implements IReportDataProvider {

    protected static String DOCUMENT_REQUEST_SIGNATURE_KEY = "DOCUMENT_REQUEST_SIGNATURE_KEY";
    
    @Override
    public boolean handleKey(final String key) {
        return key.equals(DOCUMENT_REQUEST_SIGNATURE_KEY);
    }

    @Override
    public void registerFieldsAndImages(final IDocumentFieldsData arg0) {

    }

    @Override
    public Object valueForKey(final String key) {
        if(handleKey(DOCUMENT_REQUEST_SIGNATURE_KEY)) {
            // return DocumentRequestSignature.find(AdministrativeOffice);
        }
        
        return null;
    }

}
