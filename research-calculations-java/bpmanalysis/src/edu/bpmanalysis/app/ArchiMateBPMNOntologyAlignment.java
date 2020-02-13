package edu.bpmanalysis.app;

import org.apache.jena.rdf.model.*;

import java.util.*;

public class ArchiMateBPMNOntologyAlignment {

    public static void BFS(Model model, RDFNode start) {
        Set<RDFNode> visited = new HashSet<>();
        Queue<RDFNode> queue = new LinkedList<>();
        Set<Statement> queried = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            RDFNode node = queue.poll();
            System.out.println(node.toString());

            StmtIterator iterator = model.listStatements((Resource) node, null, (String) null);

            while (iterator.hasNext()) {
                Statement statement = iterator.nextStatement();

                if (!queried.contains(statement)) {
                    queried.add(statement);
                    System.out.println(statement);

                    if (!visited.contains(statement.getObject())) {
                        queue.add(statement.getObject());
                        visited.add(statement.getObject());
                    }
                }
            }

            iterator = model.listStatements(null, null, node);

            while (iterator.hasNext()) {
                Statement statement = iterator.nextStatement();

                if (!queried.contains(statement)) {
                    queried.add(statement);
                    System.out.println(statement);

                    if (!visited.contains(statement.getSubject())) {
                        queue.add(statement.getSubject());
                        visited.add(statement.getSubject());
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        {
            Model archiMateModel = ModelFactory.createDefaultModel();

            Resource actor = archiMateModel.createResource("Business Actor");
            Resource service = archiMateModel.createResource("Business Service");
            Resource role = archiMateModel.createResource("Business Role");
            Resource process = archiMateModel.createResource("Business Process");
            Resource event = archiMateModel.createResource("Business Event");
            Resource function = archiMateModel.createResource("Business Function");
            Resource object = archiMateModel.createResource("Business Object");

            archiMateModel.add(actor, archiMateModel.createProperty("uses"), service);
            archiMateModel.add(actor, archiMateModel.createProperty("assigned to"), role);
            archiMateModel.add(role, archiMateModel.createProperty("assigned to"), process);
            archiMateModel.add(process, archiMateModel.createProperty("realizes"), service);
            archiMateModel.add(process, archiMateModel.createProperty("aggregates"), function);
            archiMateModel.add(function, archiMateModel.createProperty("triggers"), function);
            archiMateModel.add(event, archiMateModel.createProperty("triggers"), function);
            archiMateModel.add(function, archiMateModel.createProperty("accesses"), object);

            System.out.println("BFS ArchiMate");
            BFS(archiMateModel, event);
        }

        {
            Model bpmnModel = ModelFactory.createDefaultModel();

            Resource message = bpmnModel.createResource("Message");
            Resource pool = bpmnModel.createResource("Pool");
            Resource lane = bpmnModel.createResource("Lane");
            Resource activity = bpmnModel.createResource("Activity");
            Resource event = bpmnModel.createResource("Event");
            Resource gateway = bpmnModel.createResource("Gateway");
            Resource dataObject = bpmnModel.createResource("Data Object");

            bpmnModel.add(message, bpmnModel.createProperty("flows between"), pool);
            bpmnModel.add(lane, bpmnModel.createProperty("sub-divides"), pool);
            bpmnModel.add(pool, bpmnModel.createProperty("contains"), activity);
            bpmnModel.add(activity, bpmnModel.createProperty("sequences"), activity);
            bpmnModel.add(event, bpmnModel.createProperty("triggers"), activity);
            bpmnModel.add(activity, bpmnModel.createProperty("uses"), dataObject);
            bpmnModel.add(gateway, bpmnModel.createProperty("controls"), activity);

            System.out.println("BFS BPMN");
            BFS(bpmnModel, event);
        }
    }

}
