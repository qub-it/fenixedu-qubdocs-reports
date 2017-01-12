package org.fenixedu.qubdocs.preprocessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fenixedu.bennu.FenixeduQubdocsReportsSpringConfiguration;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.qubit.terra.docs.util.ReportGeneratorPreProcessor;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.odt.ODTConstants;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class QubListPreProcessor extends ReportGeneratorPreProcessor {

    public static final String FUNCTION_NAME = "qubList";

    public static final Pattern PATTERN = Pattern.compile("\\s*" + FUNCTION_NAME + "\\s*\\(\\s*\"(.+)\"\\s*\\)");
    public static final List<String> METADATA_NODE_NAMES = Arrays.asList("office:document-content", "office:body", "office:text");
    public static final List<String> TABLE_NODE_NAMES =
            Arrays.asList("table:table", "table:table-column", "table:table-row", "table:table-cell");
    public static final String SECTION_NODE_NAME = "text:section";
    public static final String COLUMN_NODE_REPEATED_ATT = "table:number-columns-repeated";

    public QubListPreProcessor() {
    }

    @Override
    public String getEntryName() {
        return ODTConstants.CONTENT_XML_ENTRY;
    }

    @Override
    protected void visit(Document document, String entryName, FieldsMetadata fieldsMetadata, IDocumentFormatter formatter,
            Map<String, Object> sharedContext) throws XDocReportException {
        Node textNode = document;
        //find textNode which contains all the text of the document
        for (String nodeName : METADATA_NODE_NAMES) {
            textNode = getNodeByName(textNode.getChildNodes(), nodeName);
        }

        //find all tables to process
        List<Node> tableList = new ArrayList<Node>();
        List<Node> sectionList = new ArrayList<Node>();
        findTableNodes(tableList, sectionList, textNode);

        //process Cell Nodes
        List<Node> cellNodes = getCellNodes(tableList);
        processCellNodes(cellNodes, fieldsMetadata);

        //process Section Nodes
        //TODOJN improve
        processCellNodes(sectionList, fieldsMetadata);
    }

    protected void findTableNodes(List<Node> tableList, List<Node> sectionList, Node node) {
        //TODOJN: maybe improve by searching for all tables in all levels
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            if (child.getNodeName().equals(TABLE_NODE_NAMES.get(0))) {
                tableList.add(child);
            }
            if (child.getNodeName().equals(SECTION_NODE_NAME)) {
                sectionList.add(child);
                findTableNodes(tableList, sectionList, child);
            }
        }

    }

    protected Node getNodeByName(NodeList childrenList, String nodeName) {

        for (int i = 0; i < childrenList.getLength(); i++) {
            Node node = childrenList.item(i);
            if (node.getNodeName().equals(nodeName)) {
                return node;
            }
        }

        throw new RuntimeException(
                BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "error.unexpected.document.format"));
    }

    protected List<Node> getCellNodes(List<Node> tableList) {
        List<Node> result = new ArrayList<Node>();

        for (Node tableNode : tableList) {
            if (tableNode.getChildNodes().getLength() != 2) {
                continue;
            }

            Node columnNode = tableNode.getChildNodes().item(0);
            if (!columnNode.getNodeName().equals(TABLE_NODE_NAMES.get(1)) || hasAttribute(columnNode, COLUMN_NODE_REPEATED_ATT)) {
                continue;
            }

            Node rowNode = tableNode.getChildNodes().item(1);
            if (!rowNode.getNodeName().equals(TABLE_NODE_NAMES.get(2)) || rowNode.getChildNodes().getLength() != 1) {
                continue;
            }

            Node cellNode = rowNode.getChildNodes().item(0);
            if (!cellNode.getNodeName().equals(TABLE_NODE_NAMES.get(3))) {
                continue;
            }

            result.add(cellNode);
        }

        return result;
    }

    protected boolean hasAttribute(Node node, String attribute) {
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            Node attributeNode = node.getAttributes().item(i);
            if (attributeNode.getNodeName().equals(attribute)) {
                return true;
            }
        }
        return false;
    }

    protected void processCellNodes(List<Node> cellNodes, FieldsMetadata fieldsMetadata) {
        for (Node cellNode : cellNodes) {
            if (cellNode instanceof Text) {
                processCellText((Text) cellNode, fieldsMetadata);
            } else {
                processCellNodes(cellNode.getChildNodes(), fieldsMetadata);
            }
        }
    }

    protected void processCellNodes(NodeList childNodes, FieldsMetadata fieldsMetadata) {
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);

            if (child instanceof Text) {
                processCellText((Text) child, fieldsMetadata);
            } else {
                processCellNodes(child.getChildNodes(), fieldsMetadata);
            }
        }
    }

    protected void processCellText(Text node, FieldsMetadata fieldsMetadata) {
        Matcher matcher = PATTERN.matcher(node.getData());

        while (matcher.find()) {
            String[] includes = matcher.group(1).split(" ");
            for (String include : includes) {
                fieldsMetadata.addFieldAsList(include);
            }
        }
        node.setData(matcher.replaceAll(""));
    }

}
