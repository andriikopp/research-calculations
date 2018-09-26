package edu.kopp.phd.express.landscape.validation;

import edu.kopp.phd.express.governance.ARISGovernanceLog;
import edu.kopp.phd.express.landscape.Landscape;
import edu.kopp.phd.express.metamodel.ModelBuilder;

public class ControlFlowValidation extends Landscape {

    public ControlFlowValidation() {
        super(new ARISGovernanceLog());
    }

    /* Incorrect */
    {
        /* Test 1 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 1")
                    .event(0, 1)
                    .xor(1, 2)
                    .xor(2, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Test 2 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 2")
                    .function(0, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Test 3 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 3")
                    .event(0, 1)
                    .function(1, 1)
                    .function(1, 0)
                    .finish());
        }

        /* Test 4 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 4")
                    .event(0, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .event(2, 0)
                    .finish());
        }

        /* Test 5 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 5")
                    .event(0, 1)
                    .function(2, 1)
                    .xor(1, 2)
                    .event(1, 0)
                    .finish());
        }

        /* Test 6 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 6")
                    .event(0, 1)
                    .function(1, 2)
                    .function(1, 0)
                    .event(1, 0)
                    .finish());
        }

        /* Test 7 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 7")
                    .event(0, 1)
                    .xor(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Test 8 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 8")
                    .event(0, 1)
                    .event(0, 1)
                    .xor(2, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .event(1, 0)
                    .finish());
        }

        /* Test 9 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 9")
                    .event(0, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .event(0, 1)
                    .function(1, 1)
                    .event(0, 1)
                    .finish());
        }

        /* Test 10 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 10")
                    .event(0, 1)
                    .function(1, 1)
                    .processInterface(1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Test 11 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 11")
                    .event(0, 1)
                    .and(1, 2)
                    .function(1, 1)
                    .processInterface(2, 0)
                    .finish());
        }

        /* Test 12 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 12")
                    .eventMakesDecision(0, 1)
                    .or(1, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .processInterface(1, 0)
                    .processInterface(1, 0)
                    .finish());
        }

        /* Test 13 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 13")
                    .eventMakesDecision(0, 1)
                    .xor(1, 2)
                    .function(1, 1)
                    .processInterface(1, 0)
                    .event(1, 0)
                    .finish());
        }

        /* Test 14 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 14")
                    .eventPrecedesEvent(0, 1)
                    .event(1, 1)
                    .processInterface(1, 1)
                    .event(1, 0)
                    .finish());
        }
    }

    /* Correct */
    {
        /* Test 33 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 33")
                    .event(0, 1)
                    .xor(1, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .xor(2, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Test 34 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 34")
                    .event(0, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Test 35 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 35")
                    .event(0, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Test 36 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 36")
                    .event(0, 1)
                    .and(1, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .and(2, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Test 37 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 37")
                    .event(0, 1)
                    .xor(2, 1)
                    .function(1, 1)
                    .xor(1, 2)
                    .event(1, 0)
                    .finish());
        }

        /* Test 38 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 38")
                    .event(0, 1)
                    .function(1, 1)
                    .xor(1, 2)
                    .function(1, 1)
                    .event(1, 0)
                    .event(1, 0)
                    .finish());
        }

        /* Test 39 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 39")
                    .event(0, 1)
                    .xor(1, 2)
                    .event(1, 0)
                    .function(1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Test 40 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 40")
                    .event(0, 1)
                    .event(0, 1)
                    .xor(2, 1)
                    .xor(1, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .event(1, 0)
                    .finish());
        }

        /* Test 41 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 41")
                    .event(0, 1)
                    .function(1, 1)
                    .xor(1, 2)
                    .function(1, 1)
                    .event(1, 0)
                    .event(1, 1)
                    .function(1, 1)
                    .event(0, 1)
                    .finish());
        }

        /* Test 42 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 42")
                    .event(0, 1)
                    .function(1, 1)
                    .event(1, 1)
                    .processInterface(1, 0)
                    .finish());
        }

        /* Test 43 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 43")
                    .event(0, 1)
                    .and(1, 2)
                    .function(1, 1)
                    .and(2, 1)
                    .processInterface(1, 0)
                    .finish());
        }

        /* Test 44 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 44")
                    .event(0, 1)
                    .function(1, 1)
                    .or(1, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .processInterface(1, 0)
                    .processInterface(1, 0)
                    .finish());
        }

        /* Test 45 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 45")
                    .event(0, 1)
                    .function(1, 1)
                    .xor(1, 2)
                    .function(1, 1)
                    .processInterface(1, 0)
                    .event(1, 0)
                    .finish());
        }

        /* Test 46 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Test 46")
                    .event(0, 1)
                    .function(1, 1)
                    .event(1, 1)
                    .processInterface(1, 0)
                    .finish());
        }
    }
}
