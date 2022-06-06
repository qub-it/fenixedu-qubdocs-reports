package org.fenixedu.qubdocs.academic.documentRequests.providers;

import java.util.Collection;

import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.commons.i18n.LocalizedString;

public interface ICreditsTransferRemarksCollection {

    public String getRemarkIdsFor(ICurriculumEntry entry);

    public LocalizedString getFormattedRemarks(String separator);

    public Collection<String> getRemarkIds();

    public LocalizedString getRemarkTextForId(String remarkId);

}
