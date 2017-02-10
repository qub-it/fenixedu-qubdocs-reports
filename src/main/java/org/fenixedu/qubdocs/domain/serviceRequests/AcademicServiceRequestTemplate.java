/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: anil.mamede@qub-it.com
 *               diogo.simoes@qub-it.com
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

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

public class AcademicServiceRequestTemplate extends AcademicServiceRequestTemplate_Base {

    protected AcademicServiceRequestTemplate(final LocalizedString name, final LocalizedString description, final User creator) {
        super();
        setCreator(creator);
        setUpdater(creator);
        init(name, description);
    }

    protected AcademicServiceRequestTemplate(final LocalizedString name, final LocalizedString description, final Locale language,
            final boolean custom, final User creator) {
        this(name, description, creator);
        setLanguage(language);
        setCustom(custom);
    }

    public void delete() {
        getDocumentTemplateFile().delete();

        setDegree(null);
        setProgramConclusion(null);
        setDegreeType(null);
        setServiceRequestType(null);
        setUpdater(null);
        setCreator(null);
        setBennu(null);

        deleteDomainObject();
    }

    public static Stream<AcademicServiceRequestTemplate> findAll() {
        return Bennu.getInstance().getDocumentTemplatesSet().stream().filter(dt -> dt instanceof AcademicServiceRequestTemplate)
                .map(AcademicServiceRequestTemplate.class::cast);
    }

    /*
     * This model can be tricky. 1 ServiceType -> 1 Standard Active Template + n Custom Active Template
     * Association between ServiceType and StandardTemplate is 1to1 when fully specified.
     * If not, a best effort strategy is applied as follows (from more general to more specific):
     * ||||||||||ServiceRequestType
     *   ||||||||ServiceRequestType+DegreeType
     *     ||||||ServiceRequestType+DegreeType+ProgramConclusion
     *       ||||ServiceRequestType+DegreeType+Degree
     *         ||ServiceRequestType+DegreeType+Degree+ProgramConclusion
     *
     * Custom Templates have no specificity and are just arranged by ServiceType.
     */

    public static AcademicServiceRequestTemplate matchTemplateFor(final Locale language,
            final ServiceRequestType serviceRequestType, final DegreeType degreeType, final ProgramConclusion programConclusion,
            final Degree degree) {
        if (serviceRequestType != null) {
            for (AcademicServiceRequestTemplate template : serviceRequestType.getAcademicServiceRequestTemplatesSet()) {
                if (!template.getActive()) {
                    continue;
                }
                if (template.getCustom()) {
                    continue;
                }
                if (!template.getLanguage().equals(language)) {
                    continue;
                }
                if (template.getDegreeType() != degreeType) {
                    continue;
                }
                if (template.getDegree() != degree) {
                    continue;
                }
                if (template.getProgramConclusion() != programConclusion) {
                    continue;
                }
                return template;
            }
        }
        return null;
    }

    public static AcademicServiceRequestTemplate findTemplateFor(final Locale language,
            final ServiceRequestType serviceRequestType, final DegreeType degreeType, final ProgramConclusion programConclusion,
            final Degree degree) {
        AcademicServiceRequestTemplate matchedTemplate =
                matchTemplateFor(language, serviceRequestType, degreeType, programConclusion, degree);
        if (matchedTemplate != null) {
            return matchedTemplate;
        }

        matchedTemplate = matchTemplateFor(language, serviceRequestType, degreeType, null, degree);
        if (matchedTemplate != null) {
            return matchedTemplate;
        }

        matchedTemplate = matchTemplateFor(language, serviceRequestType, degreeType, programConclusion, null);
        if (matchedTemplate != null) {
            return matchedTemplate;
        }

        matchedTemplate = matchTemplateFor(language, serviceRequestType, degreeType, null, null);
        if (matchedTemplate != null) {
            return matchedTemplate;
        }

        matchedTemplate = matchTemplateFor(language, serviceRequestType, null, null, null);
        return matchedTemplate;
    }

    public static Set<AcademicServiceRequestTemplate> readCustomTemplatesFor(final Locale language,
            final ServiceRequestType serviceRequestType) {
        Set<AcademicServiceRequestTemplate> customTemplates = new HashSet<>();
        for (AcademicServiceRequestTemplate template : serviceRequestType.getAcademicServiceRequestTemplatesSet()) {
            if (template.getCustom() && template.getActive() && template.getLanguage().equals(language)) {
                customTemplates.add(template);
            }
        }
        return customTemplates;
    }

    @Atomic
    public static AcademicServiceRequestTemplate create(final LocalizedString name, final LocalizedString description,
            final Locale language, final ServiceRequestType serviceRequestType, final DegreeType degreeType,
            final ProgramConclusion programConclusion, final Degree degree, final User creator) {
        AcademicServiceRequestTemplate oldTemplate =
                matchTemplateFor(language, serviceRequestType, degreeType, programConclusion, degree);
        if (oldTemplate != null) {
            oldTemplate.setActive(false);
        }
        AcademicServiceRequestTemplate template = new AcademicServiceRequestTemplate(
                name == null || name.isEmpty() ? serviceRequestType.getName() : name, description == null
                        || description.isEmpty() ? buildTemplateDescription(degreeType, programConclusion, degree) : description,
                language, false, creator);
        template.setServiceRequestType(serviceRequestType);
        template.setDegreeType(degreeType);
        template.setProgramConclusion(programConclusion);
        template.setDegree(degree);
        return template;
    }

    @Atomic
    public static AcademicServiceRequestTemplate create(final LocalizedString name, final LocalizedString description,
            final Locale language, final ServiceRequestType serviceRequestType, final DegreeType degreeType,
            final ProgramConclusion programConclusion, final Degree degree) {
        return create(name, description, language, serviceRequestType, degreeType, programConclusion, degree,
                Authenticate.getUser());
    }

    @Atomic
    public static AcademicServiceRequestTemplate createCustom(final LocalizedString name, final LocalizedString description,
            final Locale language, final ServiceRequestType serviceRequestType) {
        AcademicServiceRequestTemplate template =
                new AcademicServiceRequestTemplate(name == null || name.isEmpty() ? serviceRequestType.getName() : name,
                        description == null || description.isEmpty() ? new LocalizedString() : description, language, true,
                        Authenticate.getUser());
        template.setServiceRequestType(serviceRequestType);
        return template;
    }

    private static LocalizedString buildTemplateDescription(final DegreeType degreeType,
            final ProgramConclusion programConclusion, final Degree degree) {
        LocalizedString description = new LocalizedString();
        if (programConclusion != null) {
            description.append(programConclusion.getName());
            description.append(" - ");
        }
        if (degreeType != null) {
            description.append(degreeType.getName());
            description.append(" ");
        }
        if (degree != null) {
            description.append(degree.getPresentationNameI18N());
        }
        return description;
    }

}
