package edu.kopp.phd.express.landscape.validation;

import edu.kopp.phd.express.governance.ARISGovernanceLog;
import edu.kopp.phd.express.landscape.Landscape;
import edu.kopp.phd.express.metamodel.ModelBuilder;

public class ARISEnvironmentValidation extends Landscape {

    public ARISEnvironmentValidation() {
        super(new ARISGovernanceLog());
    }

    /* Incorrect */
    {
        /* Test 23 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 23")
                    .event(0, 1)
                    .function(1, 1, ModelBuilder.environment(0, 1, 0, 1, 1))
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 1, 1))
                    .event(0, 1)
                    .finish());
        }

        /* Test 24 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 24")
                    .event(0, 1)
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 1, 1))
                    .function(1, 1, ModelBuilder.environment(2, 1, 0, 1, 1))
                    .event(0, 1)
                    .finish());
        }

        /* Test 25 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 25")
                    .event(0, 1)
                    .function(1, 1, ModelBuilder.environment(1, 0, 0, 1, 1))
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 1, 1))
                    .event(0, 1)
                    .finish());
        }

        /* Test 26 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 26")
                    .event(0, 1)
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 1, 1))
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 0, 1))
                    .event(0, 1)
                    .finish());
        }

        /* Test 27 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 27")
                    .event(0, 1)
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 1, 1))
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 1, 0))
                    .event(0, 1)
                    .finish());
        }

        /* Test 28 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 28")
                    .event(0, 1)
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 1, 1))
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 1, 2))
                    .event(0, 1)
                    .finish());
        }
    }

    /* Correct */
    {
        /* Test 55 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 55")
                    .event(0, 1)
                    .function(1, 1, ModelBuilder.environment(1, 2, 0, 1, 1))
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 1, 1))
                    .event(0, 1)
                    .finish());
        }

        /* Test 56 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 56")
                    .event(0, 1)
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 3, 1))
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 1, 1))
                    .event(0, 1)
                    .finish());
        }

        /* Test 57 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 57")
                    .event(0, 1)
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 2, 1))
                    .function(1, 1, ModelBuilder.environment(1, 2, 0, 1, 1))
                    .event(0, 1)
                    .finish());
        }

        /* Test 58 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 58")
                    .event(0, 1)
                    .function(1, 1, ModelBuilder.environment(1, 4, 0, 1, 1))
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 2, 1))
                    .event(0, 1)
                    .finish());
        }

        /* Test 59 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 59")
                    .event(0, 1)
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 2, 1))
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 1, 1))
                    .event(0, 1)
                    .finish());
        }

        /* Test 60 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 60")
                    .event(0, 1)
                    .function(1, 1, ModelBuilder.environment(1, 1, 0, 1, 1))
                    .function(1, 1, ModelBuilder.environment(1, 5, 0, 1, 1))
                    .event(0, 1)
                    .finish());
        }
    }
}
