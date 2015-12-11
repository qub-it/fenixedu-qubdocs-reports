package org.fenixedu.qubdocs.util.reports.helpers;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import com.qubit.terra.docs.util.helpers.IDocumentHelper;

public class SortHelper implements IDocumentHelper {

    public <T> List<T> sort(List<T> collection, String properties) {
        String[] propertiesList = properties.split(" ");
        int last = propertiesList.length - 1;
        BeanComparator<T> comparator = new BeanComparator<>(propertiesList[last]);
        for (int i = last - 1; i >= 0; i--) {
            comparator = new BeanComparator<>(propertiesList[i], comparator);
        }
        Collections.sort(collection, comparator);
        return collection;
    }
}
