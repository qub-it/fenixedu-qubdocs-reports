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

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import com.qubit.terra.docs.util.helpers.IDocumentHelper;

public class DateHelper implements IDocumentHelper {

    private Locale locale;

    public DateHelper() {
    }

    public DateHelper(final Locale locale) {
        this.locale = locale;
    }

    public String numericDate(final LocalDate localDate) {
        return localDate.toString("dd/MM/yyyy");
    }

    public String numericDate(final YearMonthDay yearMonthDay) {
        return numericDate(yearMonthDay.toLocalDate());
    }

    public String numericDateTime(final DateTime dateTime) {
        return dateTime.toString("dd/MM/yyyy HH:mm");
    }

    public LocalizedString extendedDate(final LocalDate localDate) {
        LocalizedString i18NString = new LocalizedString();
        for (Locale locale : CoreConfiguration.supportedLocales()) {
            String month = localDate.toString("MMMM", locale);
            if (locale.getLanguage().equals("pt")) {
                month = month.toLowerCase(); // Java does not follow the Portuguese Language Orthographic Agreement of 1990
            }
            String day = String.valueOf(localDate.getDayOfMonth());
            if (locale.getLanguage().equals("en")) {
                day += getDayOfMonthOrdinalInEn(localDate.getDayOfMonth());
            }

            String message = BundleUtil.getString("resources.FenixeduQubdocsReportsResources", locale,
                    "message.DateHelper.extendedDate", day, month, localDate.toString("yyyy", locale));
            i18NString = i18NString.with(locale, message);
        }
        return i18NString;
    }

    public LocalizedString extendedDate(final YearMonthDay yearMonthDay) {
        return extendedDate(yearMonthDay.toLocalDate());
    }

    public String monthYear(final Partial partial) {
        return partial.toString("MM/yyyy");
    }

    public String monthYear(final LocalDate localDate) {
        return localDate.toString("MM/yyyy");
    }

    public String date(final DateTime dateTime) {
        return dateTime.toString("dd/MM/yyyy");
    }

    public String date(final LocalDate localDate) {
        return localDate.toString("dd/MM/yyyy");
    }

    public String date(final YearMonthDay yearMonthDay) {
        return yearMonthDay.toString("dd/MM/yyyy");
    }

    public String freeMarkerDate(final LocalDate localDate) {
        return localDate.toString("MM/dd/yyyy");
    }

    public String freeMarkerDate(final YearMonthDay yearMonthDay) {
        return yearMonthDay.toString("MM/dd/yyyy");
    }

    public String now() {
        return numericDateTime(new DateTime());
    }

    public String today() {
        return date(new DateTime());
    }

    public LocalizedString extendedToday() {
        return extendedDate(new LocalDate());
    }

    public String getDayOfMonthOrdinal(final int day, final boolean isMale, final boolean isPlural) {
        if (locale == null || locale.getLanguage().equals("pt")) {
            return getDayOfMonthOrdinalInPt(day, isMale, isPlural);
        } else if (locale.getLanguage().equals("en")) {
            return getDayOfMonthOrdinalInEn(day);
        }
        return "Unknown Language";
    }

    private String getDayOfMonthOrdinalInPt(final int day, final boolean isMale, final boolean isPlural) {
        if (isMale) {
            if (isPlural) {
                return "\u1D52\u02E2";
            } else {
                return "\u00BA";
            }
        } else {
            if (isPlural) {
                return "\u1D43\u02E2";
            } else {
                return "\u00AA";
            }
        }
    }

    private String getDayOfMonthOrdinalInEn(final int day) {
        if (day >= 11 && day <= 13) {
            return "\u1D57\u02B0";
        }
        switch (day % 10) {
        case 1:
            return "\u02E2\u1D57";
        case 2:
            return "\u207F\u1D48";
        case 3:
            return "\u02B3\u1D48";
        default:
            return "\u1D57\u02B0";
        }
    }

}
