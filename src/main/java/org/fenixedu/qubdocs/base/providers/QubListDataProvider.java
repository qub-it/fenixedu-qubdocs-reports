package org.fenixedu.qubdocs.base.providers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import org.fenixedu.qubdocs.preprocessors.QubListPreProcessor;
import org.odftoolkit.simple.Document;
import org.odftoolkit.simple.table.Table;
import org.odftoolkit.simple.text.Section;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class QubListDataProvider implements IReportDataProvider {

    Document document;

    List<String> listVariables;

    //GOAL - QubListPreProcessor will run after the ODTPreprocessor (which manages the lazy tables) 
    //       therefore we created this data provider which will fill the fieldsMetaData
    //       before ODTPreprocessor executes.
    public QubListDataProvider(byte[] templateContent) {
        try {
            document = Document.loadDocument(new ByteArrayInputStream(templateContent));
            listVariables = new ArrayList<String>();
            processDocument();
        } catch (Exception e) {
            throw new RuntimeException("Error in creating Odf Document. " + e.getMessage());
        }
    }

    //Remove all qubList functions from the document
    public byte[] getFinalTemplateVersion() {
        try {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            document.save(byteArray);
            return byteArray.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error in generating final version of template. " + e.getMessage());
        }
    }

    protected void processDocument() {
        for (Table table : document.getTableList()) {
            if (table.getColumnCount() == 1 && table.getRowCount() == 1
                    && table.getCellByPosition(0, 0).getDisplayText().contains(QubListPreProcessor.FUNCTION_NAME)) {
                processCellNodes(table.getCellByPosition(0, 0).getOdfElement().getChildNodes());
            }
        }

        Iterator<Section> sectionIterator = document.getSectionIterator();
        while (sectionIterator.hasNext()) {
            Section section = sectionIterator.next();
            if (section.getOdfElement().getTextContent().contains(QubListPreProcessor.FUNCTION_NAME)) {
                processCellNodes(section.getOdfElement().getChildNodes());
            }
        }
    }

    protected void processCellNodes(NodeList childNodes) {
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);

            if (child instanceof Text) {
                processCellText((Text) child);
            } else {
                processCellNodes(child.getChildNodes());
            }
        }
    }

    protected void processCellText(Text node) {
        Matcher matcher = QubListPreProcessor.PATTERN.matcher(node.getData());

        while (matcher.find()) {
            String[] includes = matcher.group(1).split(" ");
            for (String include : includes) {
                listVariables.add(include);
            }
        }
        node.setData(matcher.replaceAll(""));
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData documentFieldsData) {
        for (String listVariable : listVariables) {
            documentFieldsData.registerCollectionAsField(listVariable);
        }
    }

    @Override
    public boolean handleKey(String key) {
        return false;
    }

    @Override
    public Object valueForKey(String key) {
        return null;
    }

}
