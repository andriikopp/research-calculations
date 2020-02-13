package edu.bpmanalysis.web.bpmq.util;

import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static edu.bpmanalysis.web.bpmq.util.BPMOntologyUtil.MODEL;

public class BPMEALandscapeUtil {
    public static final Map<String, String> EA_LANDSCAPE = new HashMap<>();

    public static Map<String, Map<String, Double>> generateEAMatrix() {
        Map<String, Map<String, Double>> matrix = new LinkedHashMap<>();
        String[] dimensions = {"hasInput", "hasOutput", "hasActivity", "happenLocation",
                "hasStakeholder", "happenTime", "hasMotivation"};

        for (String dim : dimensions) {
            Map<String, Double> artifactDegrees = new HashMap<>();
            Selector selector = new SimpleSelector(null, MODEL.createProperty(dim), (Object) null);
            StmtIterator iterator = MODEL.listStatements(selector);

            while (iterator.hasNext()) {
                Statement statement = iterator.nextStatement();
                String object = statement.getObject().asLiteral().getString();
                double degree = artifactDegrees.getOrDefault(object, 0.0);
                artifactDegrees.put(object, degree + 1.0);
            }

            matrix.put(dim, artifactDegrees);
        }

        return matrix;
    }

    public static String generateArchiMateEALandscape() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<model xmlns=\"http://www.opengroup.org/xsd/archimate\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xsi:schemaLocation=\"http://www.opengroup.org/xsd/archimate " +
                "http://www.opengroup.org/xsd/archimate/archimate_v2p1.xsd\" " +
                "identifier=\"id-" + UUID.randomUUID().toString().replace("-", "")
                .substring(0, 9) + "\">\n" +
                "<name xml:lang=\"ru\">EA_Test_01</name>\n" +
                "<elements>";

        for (Map.Entry<String, String> entry : EA_LANDSCAPE.entrySet()) {
            xml += "<element identifier=\"id-" + UUID.randomUUID().toString().replace("-", "")
                    .substring(0, 9) + "\" " +
                    "xsi:type=\"" + entry.getValue() + "\">\n" +
                    "   <label xml:lang=\"ru\">" + entry.getKey() + "</label>\n" +
                    "</element>";
        }

        xml += "</elements>\n" +
                "<relationships />\n" +
                "   <propertydefs>\n" +
                "       <propertydef identifier=\"propid-junctionType\" " +
                "name=\"JunctionType\" type=\"string\" />\n" +
                "   </propertydefs>\n" +
                "</model>";

        return xml;
    }
}
