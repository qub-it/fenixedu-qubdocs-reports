package org.fenixedu.bennu;

import java.util.Comparator;

public class TupleDataSourceBean implements IBean {

    public static final Comparator<TupleDataSourceBean> COMPARE_BY_TEXT = new Comparator<TupleDataSourceBean>() {

        @Override
        public int compare(final TupleDataSourceBean o1, final TupleDataSourceBean o2) {
            int c = o1.getText().compareTo(o2.getText());
            return c != 0 ? c : o1.getId().compareTo(o2.getId());
        }
    };

    private String id;
    private String text;

    public TupleDataSourceBean() {

    }

    public TupleDataSourceBean(final String id, final String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}