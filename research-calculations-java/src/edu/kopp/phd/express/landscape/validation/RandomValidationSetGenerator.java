package edu.kopp.phd.express.landscape.validation;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.metamodel.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomValidationSetGenerator {
    public static final int NUMBER_OF_MODELS = 100;
    public static final int SIZE_OF_MODELS = 20;

    public static final int NUMBER_OF_FAULT_MODELS = 60;

    public static final Node[] CF_DEFECTS = {
            new Function("MSE", 0, 1),
            new Function("MEE", 1, 0),
            new Function("MSG", 1, 2),
            new Function("MJG", 2, 1),

            new Event("CF-ERR-1", 0, 0),
            new Event("CF-ERR-2", 1, 2),
            new Event("CF-ERR-3", 2, 1),
            new Event("CF-ERR-4", 2, 2),

            new Connector("CF-ERR-5", 1, 1, null),
            new Connector("CF-ERR-6", 0, 1, null),
            new Connector("CF-ERR-7", 1, 0, null),
            new Connector("CF-ERR-8", 2, 2, null),

            new ProcessInterface("CF-ERR-9", 1, 1),
            new ProcessInterface("CF-ERR-10", 0, 0),

            /* Extra */
            new Event("XCF-ERR-1", 0, 2),
            new Event("XCF-ERR-2", 2, 0),

            new Connector("XCF-ERR-3", 0, 2, null),
            new Connector("XCF-ERR-4", 2, 0, null),

            new ProcessInterface("XCF-ERR-5", 2, 2)
    };

    public static final Node[] DF_DEFECTS = {
            new Function("MS", 0, 1),
            new Function("BHS", 1, 0),
            new Function("GHS", 1, 2),

            new DataStore("DF-ERR-1", 0, 1),
            new DataStore("DF-ERR-2", 1, 0),
            new DataStore("DF-ERR-3", 0, 0),

            new ExternalEntity("DF-ERR-4", 0, 0),
            new ExternalEntity("DF-ERR-5", 1, 1),

            /* Extra */
            new DataStore("XDF-ERR-1", 0, 2),
            new DataStore("XDF-ERR-2", 2, 0),

            new ExternalEntity("XDF-ERR-3", 2, 2)
    };

    public static final Function[] HR_DEFECTS = {
            new Function("UR", 1, 1, 0, 1, 1, 1, 1),
            new Function("RA", 1, 1, 2, 1, 1, 1, 1)
    };

    public static final Function[] IO_DEFECTS = {
            new Function("UIO-1", 1, 1, 1, 0, 1, 1, 1),
            new Function("UIO-2", 1, 1, 1, 1, 0, 1, 1),
            new Function("UIO-3", 1, 1, 1, 1, 1, 0, 1)
    };

    public static final Function[] NHR_DEFECTS = {
            new Function("UT", 1, 1, 1, 1, 1, 1, 0),
            new Function("AA", 1, 1, 1, 1, 1, 1, 2)
    };

    public static final Function[] NO_DEFECTS = {
            new Function("NDF-1", 1, 1, 1, 1, 1, 1, 1),
            new Function("NDF-2", 1, 1, 1, 2, 1, 1, 1),
            new Function("NDF-3", 1, 1, 1, 1, 1, 2, 1),
            new Function("NDF-4", 1, 1, 1, 1, 2, 1, 1)
    };

    private static final Random RANDOM = new Random();

    private static int test_num = 1;

    public static List<Model> generateModelsWithDefects(Node[] possibleDefects) {
        List<Model> models = new ArrayList<>();

        for (int number = 1; number <= NUMBER_OF_MODELS; number++) {
            Model model = new Model("RTest-" + test_num);

            int size = 2 + RANDOM.nextInt(SIZE_OF_MODELS - 2);

            for (int task = 1; task <= size - 1; task++) {
                model.getNodes().add(NO_DEFECTS[RANDOM.nextInt(NO_DEFECTS.length)]);
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
