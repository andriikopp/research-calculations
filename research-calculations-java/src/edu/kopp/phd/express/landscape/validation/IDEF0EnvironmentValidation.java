package edu.kopp.phd.express.landscape.validation;

import edu.kopp.phd.express.governance.ARISGovernanceLog;
import edu.kopp.phd.express.landscape.Landscape;
import edu.kopp.phd.express.metamodel.ModelBuilder;

public class IDEF0EnvironmentValidation extends Landscape {

    public IDEF0EnvironmentValidation() {
        super(new ARISGovernanceLog());
    }

    /* Incorrect */
    {
        /* Test 29 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 29")
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .function(ModelBuilder.environment(0, 1, 1, 1, 0))
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .finish());
        }

        /* Test 30 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 30")
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 0, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .finish());
        }

        /* Test 31 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 31")
                    .function(ModelBuilder.environment(1, 1, 1, 0, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .finish());
        }

        /* Test 32 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 32")
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 0, 1, 1))
                    .finish());
        }

        /* Test 66 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 66")
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .finish());
        }

        /* Test 67 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 67")
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 1, 1))
                    .finish());
        }
    }

    /* Correct */
    {
        /* Test 61 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 61")
                    .function(ModelBuilder.environment(1, 2, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 3, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 2, 1))
                    .finish());
        }

        /* Test 62 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 62")
                    .function(ModelBuilder.environment(1, 1, 1, 2, 1))
                    .function(ModelBuilder.environment(1, 1, 3, 1, 1))
                    .function(ModelBuilder.environment(1, 2, 3, 1, 1))
                    .finish());
        }

        /* Test 63 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 63")
                    .function(ModelBuilder.environment(1, 1, 3, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 2, 2, 1))
                    .function(ModelBuilder.environment(1, 2, 2, 1, 1))
                    .finish());
        }

        /* Test 64 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 64")
                    .function(ModelBuilder.environment(1, 3, 1, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 2, 1, 1))
                    .function(ModelBuilder.environment(1, 1, 1, 2, 1))
                    .finish());
        }
    }
}
