package edu.kopp.phd.express.landscape.validation;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.metamodel.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomValidationSetGenerator {
    public static final int NUMBER_OF_MODELS = 2000;
    public static final int SIZE_OF_MODELS = 100;

    public static final int NUMBER_OF_FAULT_MODELS = 200;

    public static final Node[] CF_DEFECTS = {
            new Function("MSE", 0, 1),
            new Function("MEE", 1, 0),
            new Function("MSG", 1, 2),
            new Function("MJG", 2, 1),

            // Extra
            new Event("CF-ERR-1", 0, 0),
            new Event("CF-ERR-2", 1, 2),
            new Event("CF-ERR-3", 2, 1),
            new Event("CF-ERR-4", 2, 2),

            new Connector("CF-ERR-5", 1, 1, Connector.XOR),
            new Connector("CF-ERR-6", 0, 1, Connector.XOR),
            new Connector("CF-ERR-7", 1, 0, Connector.XOR),
            new Connector("CF-ERR-8", 2, 2, Connector.XOR),

            new Connector("CF-ERR-5", 1, 1, Connector.OR),
            new Connector("CF-ERR-6", 0, 1, Connector.OR),
            new Connector("CF-ERR-7", 1, 0, Connector.OR),
            new Connector("CF-ERR-8", 2, 2, Connector.OR),

            new Connector("CF-ERR-5", 1, 1, Connector.AND),
            new Connector("CF-ERR-6", 0, 1, Connector.AND),
            new Connector("CF-ERR-7", 1, 0, Connector.AND),
            new Connector("CF-ERR-8", 2, 2, Connector.AND)
    };

    public static final Node[] DF_DEFECTS = {
            new Function("MS", 0, 1),
            new Function("BHS", 1, 0),
            new Function("GHS", 1, 2)
    };

    public static final Node[] ENV_DEFECTS = {
            new Function("UR", 1, 1, 0, 1, 1, 1, 1),
            new Function("UI", 1, 1, 1, 0, 1, 1, 1),
            new Function("UC", 1, 1, 1, 1, 0, 1, 1),
            new Function("UO", 1, 1, 1, 1, 1, 0, 1),
            new Function("UT", 1, 1, 1, 1, 1, 1, 0),
    };

    public static final Node[] NO_DEFECTS_FUNCTIONS = {
            new Function("NDF-0", 1, 1, 2, 1, 1, 1, 1),
            new Function("NDF-1", 1, 1, 1, 2, 1, 1, 1),
            new Function("NDF-2", 1, 1, 1, 1, 2, 1, 1),
            new Function("NDF-3", 1, 1, 1, 1, 1, 2, 1),
            new Function("NDF-4", 1, 1, 1, 1, 1, 1, 2)
    };

    public static final Node[] NO_DEFECTS_REMAINING = {
            new Event("NDF-5", 0, 1),
            new Event("NDF-6", 1, 0),
            new Event("NDF-7", 1, 1),

            new Connector("NDF-8", 1, 2, Connector.XOR),
            new Connector("NDF-9", 2, 1, Connector.XOR),

            new Connector("NDF-8", 1, 2, Connector.OR),
            new Connector("NDF-9", 2, 1, Connector.OR),

            new Connector("NDF-8", 1, 2, Connector.AND),
            new Connector("NDF-9", 2, 1, Connector.AND),

            /*
            new DataStore("NDF-13", 1, 1),

            new ExternalEntity("NDF-14", 0, 1),
            new ExternalEntity("NDF-15", 1, 0)
            */
    };

    private static final Random RANDOM = new Random();

    private static int test_num = 1;

    public static List<Model> generateModelsWithDefects(Node[] possibleDefects) {
        List<Model> models = new ArrayList<>();

        for (int number = 1; number <= NUMBER_OF_MODELS; number++) {
            Model model = new Model("RTest-" + test_num);

            int size = (2 + RANDOM.nextInt(SIZE_OF_MODELS - 2)) / 2;

            for (int task = 1; task <= size - 1; task++) {
                model.getNodes().add(NO_DEFECTS_FUNCTIONS[RANDOM.nextInt(NO_DEFECTS_FUNCTIONS.length)]);
                model.getNodes().add(NO_DEFECTS_REMAINING[RANDOM.nextInt(NO_DEFECTS_REMAINING.length)]);
            }

            if (number <= NUMBER_OF_FAULT_MODELS) {
                model.getNodes().add(possibleDefects[RANDOM.nextInt(possibleDefects.length)]);

                model.issues();
            }

            models.add(model);

            test_num++;
        }

        return models;
    }
}
