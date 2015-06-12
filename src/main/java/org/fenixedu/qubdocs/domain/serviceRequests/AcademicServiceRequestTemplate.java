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

package org.fenixedu.qubdocs.domain.serviceRequests;

import java.util.Locale;

import org.fenixedu.commons.i18n.LocalizedString;

public class AcademicServiceRequestTemplate extends AcademicServiceRequestTemplate_Base {
	
	protected AcademicServiceRequestTemplate(final LocalizedString name, final LocalizedString description) {
        super();
        init(name, description);
    }
    
    protected AcademicServiceRequestTemplate(final LocalizedString name, final LocalizedString description, final Locale language, final boolean custom) {
    	this(name, description);
    	setLanguage(language);
    	setCustom(custom);
    }
    
    public static AcademicServiceRequestTemplate createTemplate(final LocalizedString name, final LocalizedString description, final Locale language, final boolean custom) {
    	return new AcademicServiceRequestTemplate(name, description, language, custom);
    }
}
