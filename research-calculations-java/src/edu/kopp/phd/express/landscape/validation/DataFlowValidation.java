package edu.kopp.phd.express.landscape.validation;

import edu.kopp.phd.express.governance.DFDGovernanceLog;
import edu.kopp.phd.express.landscape.Landscape;
import edu.kopp.phd.express.metamodel.ModelBuilder;

public class DataFlowValidation extends Landscape {

    public DataFlowValidation() {
        super(new DFDGovernanceLog());
    }

    /* Incorrect */
    {
        /* Test 15 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 15")
                    .externalEntity(0, 1)
                    .function(2, 0)
                    .dataStore(1, 2)
                    .function(1, 2)
                    .externalEntity(1, 0)
                    .finish().issues());
        }

        /* Test 16 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 16")
                    .externalEntity(1, 0)
                    .function(0, 2)
                    .dataStore(2, 1)
                    .function(1, 2)
                    .externalEntity(1, 0)
                    .finish().issues());
        }

        /* Test 17 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 17")
                    .externalEntity(0, 1)
                    .function(1, 2)
                    .dataStore(1, 0)
                    .externalEntity(1, 0)
                    .finish().issues());
        }

        /* Test 18 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 18")
                    .externalEntity(0, 1)
                    .function(2, 1)
                    .dataStore(0, 1)
                    .externalEntity(1, 0)
                    .finish().issues());
        }

        /* Test 19 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 19")
                    .externalEntity(0, 1)
                    .function(2, 0)
                    .dataStore(0, 1)
                    .externalEntity(0, 0)
                    .finish().issues());
        }

        /* Test 20 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 20")
                    .externalEntity(0, 1)
                    .dataFlowBetweenOtherEntities()
                    .dataStore(2, 1)
                    .function(1, 1)
                    .function(1, 2)
                    .externalEntity(1, 0)
                    .finish().issues());
        }

        /* Test 21 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 21")
                    .externalEntity(0, 1)
                    .function(2, 1)
                    .dataStore(1, 1)
                    .dataStore(1, 2)
                    .dataFlowBetweenOtherEntities()
                    .externalEntity(1, 0)
                    .finish().issues());
        }

        /* Test 22 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 22")
                    .externalEntity(0, 1)
                    .function(1, 1)
                    .externalEntity(1, 1)
                    .dataFlowBetweenOtherEntities()
                    .externalEntity(1, 0)
                    .finish().issues());
        }
    }

    /* Correct */
    {
        /* Test 47 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 47")
                    .externalEntity(0, 1)
                    .function(2, 1)
                    .dataStore(2, 2)
                    .function(1, 2)
                    .externalEntity(1, 0)
                    .finish());
        }

        /* Test 48 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 48")
                    .externalEntity(1, 0)
                    .function(1, 2)
                    .dataStore(2, 2)
                    .function(1, 2)
                    .externalEntity(1, 0)
                    .finish());
        }

        /* Test 49 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 49")
                    .externalEntity(0, 1)
                    .function(2, 2)
                    .dataStore(1, 1)
                    .externalEntity(1, 0)
                    .finish());
        }

        /* Test 50 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 50")
                    .externalEntity(0, 1)
                    .function(2, 2)
                    .dataStore(1, 1)
                    .externalEntity(1, 0)
                    .finish());
        }

        /* Test 51 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 51")
                    .externalEntity(0, 1)
                    .function(2, 2)
                    .dataStore(1, 1)
                    .externalEntity(1, 0)
                    .finish());
        }

        /* Test 52 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 52")
                    .externalEntity(0, 1)
                    .dataStore(2, 1)
                    .function(2, 1)
                    .function(1, 2)
                    .externalEntity(1, 0)
                    .finish());
        }

        /* Test 53 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 53")
                    .externalEntity(0, 1)
                    .function(2, 2)
                    .dataStore(1, 1)
                    .dataStore(1, 2)
                    .externalEntity(1, 0)
                    .finish());
        }

        /* Test 54 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 54")
                    .externalEntity(0, 1)
                    .function(1, 1)
                    .externalEntity(1, 1)
                    .function(1, 1)
                    .externalEntity(1, 0)
                    .finish());
        }
    }
}
