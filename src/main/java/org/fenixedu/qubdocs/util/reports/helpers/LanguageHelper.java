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

package org.fenixedu.qubdocs.util.reports.helpers;

import java.util.Locale;

import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.qubdocs.FenixEduDocumentGenerator;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.qubit.terra.docs.util.helpers.IDocumentHelper;

public class LanguageHelper implements IDocumentHelper {

    private Locale locale;

    public LanguageHelper() {

    }

    public LanguageHelper(Locale locale) {
        this.locale = locale;
    }

    public String i18n(final String localizedStringJson) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(localizedStringJson);
        LocalizedString i18nString = LocalizedString.fromJson(jsonElement);
        return i18n(i18nString);
    }

    public String i18n(final LocalizedString i18nString) {
        if (i18nString == null || locale == null) {
            return FenixEduDocumentGenerator.DASH;
        }

        String message = i18nString.getContent(locale);
        return message != null ? message : "";
    }

    public String i18n(final MultiLanguageString i18nString) {
        if (i18nString == null || locale == null) {
            return FenixEduDocumentGenerator.DASH;
        }

        String message = i18nString.getContent(locale);
        return message != null ? message : "";
    }

    public String pt(final LocalizedString i18nString) {
        if (i18nString == null) {
            return FenixEduDocumentGenerator.DASH;
        }

        String message = i18nString.getContent(new Locale("pt", "PT"));
        return message != null ? message : "";
    }

    public String pt(final MultiLanguageString i18nString) {
        if (i18nString == null) {
            return FenixEduDocumentGenerator.DASH;
        }

        String message = i18nString.getContent(new Locale("pt", "PT"));
        return message != null ? message : "";
    }

    public String en(final LocalizedString i18nString) {
        if (i18nString == null) {
            return FenixEduDocumentGenerator.DASH;
        }

        String message = i18nString.getContent(new Locale("en", "GB"));
        return message != null ? message : "";
    }

    public String en(final MultiLanguageString i18nString) {
        if (i18nString == null) {
            return FenixEduDocumentGenerator.DASH;
        }

        String message = i18nString.getContent(new Locale("en", "GB"));
        return message != null ? message : "";
    }

}
