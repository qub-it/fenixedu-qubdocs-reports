/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: xpto@qub-it.com
 *
 * 
 * This file is part of FenixEdu Qubdocs.
 *
 * FenixEdu Qubdocs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Qubdocs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Qubdocs.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fenixedu.qubdocs.dto.documenttemplates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.qubdocs.domain.DocumentTemplateFile;
import org.fenixedu.qubdocs.domain.serviceRequests.AcademicServiceRequestTemplate;

public class AcademicServiceRequestTemplateBean implements IBean {

    private ServiceRequestType serviceRequestType;
    private List<TupleDataSourceBean> serviceRequestTypeDataSource;
    private DegreeType degreeType;
    private List<TupleDataSourceBean> degreeTypeDataSource;
    private ProgramConclusion programConclusion;
    private List<TupleDataSourceBean> programConclusionDataSource;
    private Degree degree;
    private List<TupleDataSourceBean> degreeDataSource;
    private User creator;
    private List<TupleDataSourceBean> creatorDataSource;
    private DocumentTemplateFile documentTemplateFile;
    private List<TupleDataSourceBean> documentTemplateFileDataSource;
    private User updater;
    private List<TupleDataSourceBean> updaterDataSource;
    private java.util.Locale language;
    private List<TupleDataSourceBean> languageDataSource;
    private java.lang.Boolean custom;
    private org.fenixedu.commons.i18n.LocalizedString name;
    private org.fenixedu.commons.i18n.LocalizedString description;
    private org.joda.time.DateTime creationDate;
    private org.joda.time.DateTime updateDate;
    private java.lang.Boolean active;

    public ServiceRequestType getServiceRequestType() {
        return serviceRequestType;
    }

    public void setServiceRequestType(ServiceRequestType value) {
        serviceRequestType = value;
    }

    public List<TupleDataSourceBean> getServiceRequestTypeDataSource() {
        return serviceRequestTypeDataSource;
    }

    public void setServiceRequestTypeDataSource(List<ServiceRequestType> value) {
        this.serviceRequestTypeDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getName().getContent());
            return tuple;
        }).collect(Collectors.toList());
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType value) {
        degreeType = value;
    }

    public List<TupleDataSourceBean> getDegreeTypeDataSource() {
        return degreeTypeDataSource;
    }

    public void setDegreeTypeDataSource(List<DegreeType> value) {
        this.degreeTypeDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getName().getContent());
            return tuple;
        }).collect(Collectors.toList());
    }

    public ProgramConclusion getProgramConclusion() {
        return programConclusion;
    }

    public void setProgramConclusion(ProgramConclusion value) {
        programConclusion = value;
    }

    public List<TupleDataSourceBean> getProgramConclusionDataSource() {
        return programConclusionDataSource;
    }

    public void setProgramConclusionDataSource(List<ProgramConclusion> value) {
        this.programConclusionDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getName().getContent() + " - " + x.getDescription().getContent());
            return tuple;
        }).collect(Collectors.toList());
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree value) {
        degree = value;
    }

    public List<TupleDataSourceBean> getDegreeDataSource() {
        return degreeDataSource;
    }

    public void setDegreeDataSource(List<Degree> value) {
        this.degreeDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getPresentationNameI18N().getContent());
            return tuple;
        }).collect(Collectors.toList());
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User value) {
        creator = value;
    }

    public List<TupleDataSourceBean> getCreatorDataSource() {
        return creatorDataSource;
    }

    public void setCreatorDataSource(List<User> value) {
        this.creatorDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getUsername());
            return tuple;
        }).collect(Collectors.toList());
    }

    public DocumentTemplateFile getDocumentTemplateFile() {
        return documentTemplateFile;
    }

    public void setDocumentTemplateFile(DocumentTemplateFile value) {
        documentTemplateFile = value;
    }

    public List<TupleDataSourceBean> getDocumentTemplateFileDataSource() {
        return documentTemplateFileDataSource;
    }

    public void setDocumentTemplateFileDataSource(List<DocumentTemplateFile> value) {
        this.documentTemplateFileDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getDisplayName());
            return tuple;
        }).collect(Collectors.toList());
    }

    public User getUpdater() {
        return updater;
    }

    public void setUpdater(User value) {
        updater = value;
    }

    public List<TupleDataSourceBean> getUpdaterDataSource() {
        return updaterDataSource;
    }

    public void setUpdaterDataSource(List<User> value) {
        this.updaterDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId());
            tuple.setText(x.getUsername());
            return tuple;
        }).collect(Collectors.toList());
    }

    public java.util.Locale getLanguage() {
        return language;
    }

    public void setLanguage(java.util.Locale value) {
        language = value;
    }

    public List<TupleDataSourceBean> getLanguageDataSource() {
        return languageDataSource;
    }

    public void setLanguageDataSource(List<Locale> value) {
        this.languageDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getLanguage() + "_" + x.getCountry());
            tuple.setText(x.getDisplayLanguage());
            return tuple;
        }).collect(Collectors.toList());
    }

    public java.lang.Boolean getCustom() {
        return custom;
    }

    public void setCustom(java.lang.Boolean value) {
        custom = value;
    }

    public org.fenixedu.commons.i18n.LocalizedString getName() {
        return name;
    }

    public void setName(org.fenixedu.commons.i18n.LocalizedString value) {
        name = value;
    }

    public org.fenixedu.commons.i18n.LocalizedString getDescription() {
        return description;
    }

    public void setDescription(org.fenixedu.commons.i18n.LocalizedString value) {
        description = value;
    }

    public org.joda.time.DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(org.joda.time.DateTime value) {
        creationDate = value;
    }

    public org.joda.time.DateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(org.joda.time.DateTime value) {
        updateDate = value;
    }

    public java.lang.Boolean getActive() {
        return active;
    }

    public void setActive(java.lang.Boolean value) {
        active = value;
    }

    public AcademicServiceRequestTemplateBean() {

    }

    public AcademicServiceRequestTemplateBean(AcademicServiceRequestTemplate academicServiceRequestTemplate) {
        this.setServiceRequestType(academicServiceRequestTemplate.getServiceRequestType());
        this.setDegreeType(academicServiceRequestTemplate.getDegreeType());
        this.setProgramConclusion(academicServiceRequestTemplate.getProgramConclusion());
        this.setDegree(academicServiceRequestTemplate.getDegree());
        this.setCreator(academicServiceRequestTemplate.getCreator());
        this.setDocumentTemplateFile(academicServiceRequestTemplate.getDocumentTemplateFile());
        this.setUpdater(academicServiceRequestTemplate.getUpdater());
        this.setLanguage(academicServiceRequestTemplate.getLanguage());
        this.setCustom(academicServiceRequestTemplate.getCustom());
        this.setName(academicServiceRequestTemplate.getName());
        this.setDescription(academicServiceRequestTemplate.getDescription());
        this.setCreationDate(academicServiceRequestTemplate.getCreationDate());
        this.setUpdateDate(academicServiceRequestTemplate.getUpdateDate());
        this.setActive(academicServiceRequestTemplate.getActive());
        this.setLanguage(academicServiceRequestTemplate.getLanguage());
        this.setCustom(academicServiceRequestTemplate.getCustom());
        this.setName(academicServiceRequestTemplate.getName());
        this.setDescription(academicServiceRequestTemplate.getDescription());
        this.setCreationDate(academicServiceRequestTemplate.getCreationDate());
        this.setUpdateDate(academicServiceRequestTemplate.getUpdateDate());
        this.setActive(academicServiceRequestTemplate.getActive());
    }

}
