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

package org.fenixedu.qubdocs.base.providers;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.qubdocs.util.DocsStringUtils;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;
import com.qubit.terra.docs.util.IReportFieldsProvider;

public class PersonReportDataProvider implements IReportDataProvider {

    protected static final String KEY = "person";
    protected static final String HAS_SOCIAL_SECURITY_NUMBER = "hasSocialSecurityNumber";

    public static final IReportFieldsProvider fields = new IReportFieldsProvider() {

        @Override
        public void registerFieldsMetadata(IFieldsExporter exporter) {
            exporter.registerSimpleField("person.name", "Nome do Aluno");
            exporter.registerSimpleField("person.givenNames", "Nomes próprios");
            exporter.registerSimpleField("person.familyNames", "Apelidos");
            exporter.registerSimpleField("person.female", "Se é do sexo feminino");
            exporter.registerSimpleField("person.male", "Se é do sexo masculino");
            exporter.registerSimpleField(
                    "person.dateOfBirthYearMonthDay",
                    "Data Nascimento. Data por extenso: dates.extendedDate(person.dateOfBirthYearMonthDay) Data numérica: dates.numericDate(person.dateOfBirthYearMonthDay)");
            exporter.registerSimpleField("person.idDocumentType.localizedNameI18N", "Tipo de Doc. Identificação");
            exporter.registerSimpleField("person.documentIdNumber", "N. Doc. Identificação");
            exporter.registerSimpleField(
                    "person.expirationDateOfDocumentIdYearMonthDay",
                    "Data validade do Doc. Identificação. Data por extenso: dates.extendedDate(person.expirationDateOfDocumentIdYearMonthDay) Data numérica: dates.numericDate(person.expirationDateOfDocumentIdYearMonthDay)");
            exporter.registerSimpleField("person.nameOfFather", "Nome do pai");
            exporter.registerSimpleField("person.nameOfMother", "Nome da mãe");
            exporter.registerSimpleField("person.districtOfBirth", "Naturalidade");
            exporter.registerSimpleField("person.country.countryNationality", "Nacionalidade");

//            exporter.registerCollectionField("testList_person", "Lista de itens de teste Person.");
//            exporter.registerSimpleField("testList.test_person", "Item de teste Person.");
        }

    };

    protected Person person;

    public PersonReportDataProvider(final Person person) {
        this.person = person;
    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key) || HAS_SOCIAL_SECURITY_NUMBER.equals(key);
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData arg0) {
    }

    @Override
    public Object valueForKey(final String key) {
        if (KEY.equals(key)) {
            return person;
        } else if (HAS_SOCIAL_SECURITY_NUMBER.equals(key)) {
            return !DocsStringUtils.isEmpty(person.getSocialSecurityNumber());
        }

        return null;
    }

    public void checkData() {

    }

}
