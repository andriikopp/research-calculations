package edu.kopp.phd.express.landscape;

import edu.kopp.phd.express.governance.ARISGovernanceLog;
import edu.kopp.phd.express.metamodel.ModelBuilder;

public class ARISLandscape extends Landscape {

    public ARISLandscape() {
        super(new ARISGovernanceLog());
    }

    /* Conceptdraw */
    {
        /* Image Processing */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Image Processing (Conceptdraw)")
                    .event(0, 1)
                    .function(1, 1)
                    .xor(2, 1)
                    .xor(1, 3)
                    .and(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .xor(1, 2)
                    .and(2, 1)
                    .xor(1, 2)
                    .event(1, 1)
                    .function(1, 1)
                    .or(3, 1)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .event(1, 0)
                    .finish());
        }

        /* Login and Registration */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Login and Registration (Conceptdraw)")
                    .event(0, 1)
                    .function(1, 1)
                    .xor(1, 2)
                    .event(1, 0)
                    .event(1, 1)
                    .function(1, 1)
                    .xor(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .xor(2, 1)
                    .function(1, 1)
                    .xor(3, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1, 0, 0, 0, 0, 1)
                    .xor(1, 2)
                    .event(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .event(1, 1)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 2, 0, 0, 0, 0, 1)
                    .xor(1, 1)
                    .event(1, 1)
                    .and(1, 2)
                    .function(1, 1, 0, 0, 0, 0, 0)
                    .event(1, 0)
                    .function(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .event(1, 1)
                    .finish());
        }

        /* Main Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Main Process (Conceptdraw)")
                    .event(0, 1)
                    .function(1, 1, 1, 0, 0, 0, 1)
                    .xor(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .xor(2, 1)
                    .event(1, 1)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .event(1, 1)
                    .pinterface(1, 0)
                    .finish());
        }

        /* Order Processing */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Order Processing (Conceptdraw)")
                    .event(0, 1)
                    .function(1, 1, 1, 1, 0, 0, 1)
                    .event(1, 1)
                    .function(1, 1, 2, 0, 0, 1, 1)
                    .event(1, 1)
                    .function(1, 1, 5, 2, 0, 1, 1)
                    .event(1, 1)
                    .function(1, 1, 4, 1, 0, 0, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Wikipedia Editing */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Wikipedia Editing (Conceptdraw)")
                    .event(0, 1)
                    .function(1, 1)
                    .xor(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .xor(2, 1)
                    .xor(2, 1)
                    .function(1, 1)
                    .and(1, 2)
                    .event(1, 0)
                    .event(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .event(1, 1)
                    .event(1, 1)
                    .and(2, 1)
                    .function(1, 1)
                    .event(1, 1)
                    .finish());
        }
    }

    /* EDraw */
    {
        /* Asset Management */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Asset Management (EDraw)")
                    .event(0, 1)
                    .event(0, 1)
                    .function(1, 1, 1, 5, 0, 1, 0)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .or(2, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .or(2, 1)
                    .event(1, 1)
                    .function(1, 1, 1, 0, 0, 1, 0)
                    .event(1, 0)
                    .finish());
        }

        /* Online Sales */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Online Sales (EDraw)")
                    .function(0, 1, 1, 0, 0, 0, 0)
                    .event(1, 1)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .event(1, 1)
                    .or(1, 2)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .function(1, 0, 1, 0, 0, 0, 0)
                    .function(1, 0, 0, 0, 0, 0, 1)
                    .finish());
        }

        /* Order Book */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Order Book (EDraw)")
                    .event(0, 1)
                    .function(1, 1, 1, 0, 0, 3, 0)
                    .event(1, 1)
                    .function(1, 1, 0, 1, 0, 0, 1)
                    .event(1, 1)
                    .and(1, 2)
                    .function(1, 1, 0, 0, 0, 0, 1)
                    .function(1, 1, 0, 0, 0, 0, 1)
                    .event(1, 1)
                    .and(2, 1)
                    .event(1, 1)
                    .xor(1, 2)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .event(1, 1)
                    .xor(1, 1)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .event(1, 1, true, false)
                    .event(2, 0)
                    .finish());
        }

        /* Product Development */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Product Development (EDraw)")
                    .event(0, 1)
                    .function(1, 1, 1, 1, 0, 0, 0)
                    .event(1, 1)
                    .function(1, 1, 1, 0, 0, 1, 0)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .function(2, 1)
                    .xor(1, 2)
                    .event(1, 1, true, false)
                    .event(1, 1)
                    .event(1, 1, true, false)
                    .event(1, 0)
                    .finish());
        }

        /* Product Order */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Product Order (EDraw)")
                    .event(0, 1)
                    .function(1, 1, 0, 1, 0, 0, 0)
                    .xor(1, 2)
                    .or(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1, 0, 1, 0, 0, 0)
                    .function(1, 1, 0, 1, 0, 0, 0)
                    .event(1, 1)
                    .event(1, 1)
                    .or(2, 1)
                    .function(2, 1, 0, 1, 0, 0, 0)
                    .or(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .pinterface(1, 1)
                    .event(1, 1)
                    .event(1, 1)
                    .or(2, 1)
                    .function(1, 1, 0, 1, 0, 0, 0)
                    .event(1, 0)
                    .finish());
        }

        /* Repair */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Repair (EDraw)")
                    .event(0, 1)
                    .function(1, 1)
                    .xor(1, 3)
                    .event(1, 1)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .and(1, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .or(1, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .or(2, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .or(2, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .event(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Stocktaking */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Stocktaking (EDraw)")
                    .event(0, 1, false, true)
                    .xor(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1, 0, 0, 0, 1, 0)
                    .function(1, 1, 1, 0, 0, 1, 0)
                    .function(2, 1, 1, 0, 0, 1, 0)
                    .event(2, 1)
                    .function(1, 1, 1, 0, 0, 1, 0)
                    .xor(1, 2)
                    .event(1, 1, true, false)
                    .event(1, 1)
                    .function(1, 1, 1, 0, 0, 1, 0)
                    .event(1, 0)
                    .finish());
        }
    }

    /* MyDraw */
    {
        /* Asset Management */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Asset Management (MyDraw)")
                    .event(0, 1)
                    .event(0, 1)
                    .function(1, 1, 1, 5, 0, 1, 0)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .or(2, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .or(2, 1)
                    .event(1, 1)
                    .function(1, 1, 1, 0, 0, 1, 0)
                    .event(1, 0)
                    .finish());
        }

        /* Loan Application */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Loan Application (MyDraw)")
                    .event(0, 1, false, true)
                    .xor(1, 2)
                    .event(1, 1, false, true)
                    .event(1, 1)
                    .xor(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .pinterface(1, 1)
                    .xor(1, 1)
                    .xor(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .xor(3, 1)
                    .pinterface(1, 1)
                    .pinterface(1, 1)
                    .event(1, 0)
                    .event(1, 0)
                    .finish());
        }

        /* Login and Registration */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Login and Registration (MyDraw)")
                    .event(0, 1)
                    .function(1, 1)
                    .xor(1, 2)
                    .event(1, 0)
                    .event(1, 1)
                    .function(1, 1)
                    .xor(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .xor(2, 1)
                    .function(1, 1)
                    .xor(3, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1, 0, 0, 0, 0, 1)
                    .xor(1, 2)
                    .event(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .event(1, 1)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 2, 0, 0, 0, 0, 1)
                    .xor(1, 1)
                    .event(1, 1)
                    .and(1, 2)
                    .function(1, 1, 0, 0, 0, 0, 0)
                    .event(1, 0)
                    .function(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .event(1, 1)
                    .finish());
        }

        /* Order Book */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Order Book (MyDraw)")
                    .event(0, 1)
                    .function(1, 1, 1, 0, 0, 3, 0)
                    .event(1, 1)
                    .function(1, 1, 0, 1, 0, 0, 1)
                    .event(1, 1)
                    .and(1, 2)
                    .function(1, 1, 0, 0, 0, 0, 1)
                    .function(1, 1, 0, 0, 0, 0, 1)
                    .event(1, 1)
                    .and(2, 1)
                    .event(1, 1)
                    .xor(1, 2)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .event(1, 1)
                    .xor(1, 1)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .event(1, 1, true, false)
                    .event(2, 0)
                    .finish());
        }

        /* Product Development */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Product Development (MyDraw)")
                    .event(0, 1)
                    .function(1, 1, 1, 1, 0, 0, 0)
                    .event(1, 1)
                    .function(1, 1, 1, 0, 0, 1, 0)
                    .function(1, 1, 1, 0, 0, 0, 0)
                    .function(2, 1)
                    .xor(1, 2)
                    .event(1, 1, true, false)
                    .event(1, 1)
                    .event(1, 1, true, false)
                    .event(1, 0)
                    .finish());
        }

        /* Product Order */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Product Order (MyDraw)")
                    .event(0, 1)
                    .function(1, 1, 0, 1, 0, 0, 0)
                    .xor(1, 2)
                    .or(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1, 0, 1, 0, 0, 0)
                    .function(1, 1, 0, 1, 0, 0, 0)
                    .event(1, 1)
                    .event(1, 1)
                    .or(2, 1)
                    .function(2, 1, 0, 1, 0, 0, 0)
                    .or(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .event(1, 1)
                    .event(1, 1)
                    .or(2, 1)
                    .function(1, 1, 0, 1, 0, 0, 0)
                    .event(1, 0)
                    .finish());
        }

        /* Repair */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Repair (MyDraw)")
                    .event(0, 1)
                    .function(1, 1)
                    .xor(1, 3)
                    .event(1, 1)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .and(1, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .or(1, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .or(2, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .or(2, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .event(1, 1)
                    .function(1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Stocktaking */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Stocktaking (MyDraw)")
                    .event(0, 1, false, true)
                    .xor(1, 2)
                    .event(1, 1)
                    .event(1, 1)
                    .function(1, 1, 0, 0, 0, 1, 0)
                    .function(1, 1, 1, 0, 0, 1, 0)
                    .function(2, 1, 1, 0, 0, 1, 0)
                    .event(2, 1)
                    .function(1, 1, 1, 0, 0, 1, 0)
                    .xor(1, 2)
                    .event(1, 1, true, false)
                    .event(1, 1)
                    .function(1, 1, 1, 0, 0, 1, 0)
                    .event(1, 0)
                    .finish());
        }
    }
}
