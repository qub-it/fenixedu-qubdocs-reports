package org.fenixedu.qubdocs.util.reports.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import com.qubit.terra.docs.util.helpers.IDocumentHelper;

public class SortHelper implements IDocumentHelper {

    public <T> List<T> sort(final List<T> collection, final String properties) {
        String[] propertiesList = properties.split(" ");
        ComparatorChain chain = new ComparatorChain();
        List<T> modCollection = new ArrayList<>(collection);
        for (String property : propertiesList) {
            BeanComparator<T> comparator = new BeanComparator<>(property);
            chain.addComparator(comparator);
        }
        Collections.sort(modCollection, chain);
        return modCollection;
    }
}
