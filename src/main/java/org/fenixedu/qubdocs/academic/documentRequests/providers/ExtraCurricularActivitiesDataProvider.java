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

import java.util.Comparator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.curriculum.ExtraCurricularActivity;
import org.joda.time.Interval;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class ExtraCurricularActivitiesDataProvider implements IReportDataProvider {

    protected static final String KEY = "extraCurricularActivities";
    protected static final String KEY_FOR_LIST = "extraCurricularActivitiesList";
    
    protected final Comparator<ExtraCurricularActivity> EXTRA_CURRICULAR_ACTIVITY_COMPARATOR = new Comparator<ExtraCurricularActivity>() {

        @Override
        public int compare(ExtraCurricularActivity o1, ExtraCurricularActivity o2) {
            int resultByType = o1.getType().getName().getContent(locale).compareTo(o2.getType().getName().getContent(locale));
            
            if(resultByType != 0) {
                return resultByType;
            }
            
            return o1.getStart().compareTo(o2.getStart());
        }
        
    };
    
    protected Student student;
    protected TreeSet<ExtraCurricularActivity> activities;
    protected Interval interval;
    protected Locale locale;
    
    public ExtraCurricularActivitiesDataProvider(final Student student, final Interval interval, final Locale locale) {
        this.student = student;
        this.interval = interval;
        this.locale = locale;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData documentFieldsData) {
        documentFieldsData.registerCollectionAsField(KEY_FOR_LIST);
    }

    @Override
    public boolean handleKey(final String key) {
        return KEY.equals(key) || KEY_FOR_LIST.equals(key);
    }

    @Override
    public Object valueForKey(String key) {
        if(KEY.equals(key)) {
            return this;
        } else if(KEY_FOR_LIST.equals(key)) {
            return this.getActivities();
        }
        
        return null;
    }
    
    public boolean isEmpty() {
        return getActivities().isEmpty();
    }

    public Set<ExtraCurricularActivity> getActivities() {
        if(activities == null) {
            TreeSet<ExtraCurricularActivity> result = Sets.newTreeSet(EXTRA_CURRICULAR_ACTIVITY_COMPARATOR);
            
            for (ExtraCurricularActivity extraCurricularActivity : student.getExtraCurricularActivitySet()) {
                if(!interval.overlaps(extraCurricularActivity.getActivityInterval())) {
                    continue;
                }
                
                result.add(extraCurricularActivity);
            }
            
            activities = result;
        }
        
        return activities;
    }

	@Override
	public void registerFieldsMetadata(IFieldsExporter exporter) {
		// TODO Auto-generated method stub
		
	}

}
