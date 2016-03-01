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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.treasury.IAcademicServiceRequestAndAcademicTaxTreasuryEvent;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class ServiceRequestDataProvider implements IReportDataProvider {

    protected static final String KEY = "serviceRequest";
    protected static final String KEY_HAS_PRICETAG = "hasPricetag";
    protected static final String KEY_FOR_PRICE = "serviceRequestPrice";
    protected static final String KEY_EXECUTION_YEAR = "executionYearName";
    protected static final String KEY_PREVIOUS_EXECUTION_YEAR = "previousExecutionYearName";

    protected AcademicServiceRequest serviceRequest;
    protected ExecutionYear executionYear;

    public ServiceRequestDataProvider(final AcademicServiceRequest serviceRequest, ExecutionYear executionYear) {
        this.serviceRequest = serviceRequest;
        this.executionYear = executionYear;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData documentFieldsData) {
    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key) || KEY_HAS_PRICETAG.equals(key) || KEY_FOR_PRICE.equals(key) || KEY_EXECUTION_YEAR.equals(key)
                || KEY_PREVIOUS_EXECUTION_YEAR.equals(key);
    }

    @Override
    public Object valueForKey(final String key) {

        if (KEY.equals(key)) {
            return serviceRequest;
        } else if (KEY_HAS_PRICETAG.equals(key)) {
            final IAcademicServiceRequestAndAcademicTaxTreasuryEvent academicTreasuryEvent =
                    TreasuryBridgeAPIFactory.implementation().academicTreasuryEventForAcademicServiceRequest(serviceRequest);
            return academicTreasuryEvent != null && academicTreasuryEvent.isWithDebitEntry();
        } else if (KEY_FOR_PRICE.equals(key)) {
            return TreasuryBridgeAPIFactory.implementation().academicTreasuryEventForAcademicServiceRequest(serviceRequest);
        } else if (KEY_EXECUTION_YEAR.equals(key)) {
            return executionYear.getName();
        } else if (KEY_PREVIOUS_EXECUTION_YEAR.equals(key)) {
            return executionYear.getPreviousExecutionYear().getName();
        }

        return null;
    }

}
