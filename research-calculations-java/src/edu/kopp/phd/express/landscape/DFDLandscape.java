package edu.kopp.phd.express.landscape;

import edu.kopp.phd.express.governance.DFDGovernanceLog;
import edu.kopp.phd.express.metamodel.ModelBuilder;

public class DFDLandscape extends Landscape {

    public DFDLandscape() {
        super(new DFDGovernanceLog());
    }

    /* Conceptdraw */
    {
        /* Model of Small Traditional Production Enterprise */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Model of Small Traditional Production Enterprise")
                    .xentity(1, 1)
                    .xentity(2, 1)
                    .function(4, 2)
                    .function(7, 4)
                    .function(2, 6)
                    .function(1, 1)
                    .dstore(2, 0)
                    .dstore(0, 1)
                    .dstore(1, 1)
                    .dstore(2, 1)
                    .finish());
        }

        /* Process of Account Receivable */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Process of Account Receivable")
                    .xentity(2, 2)
                    .xentity(1, 1)
                    .function(1, 1)
                    .function(1, 2)
                    .function(1, 1)
                    .function(2, 2)
                    .function(3, 1)
                    .function(2, 1)
                    .function(1, 2)
                    .function(2, 1)
                    .dstore(2, 0)
                    .dstore(0, 1)
                    .dstore(0, 2)
                    .dstore(1, 2)
                    .dstore(1, 1)
                    .finish());
        }

        /* Customer purchase */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Customer purchase")
                    .xentity(0, 1)
                    .xentity(1, 1)
                    .function(1, 1)
                    .function(2, 2)
                    .function(2, 2)
                    .function(2, 1)
                    .dstore(1, 1)
                    .dstore(1, 1)
                    .finish());
        }
    }

    /* EDraw */
    {
        /* Selling */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Selling")
                    .xentity(0, 1)
                    .xentity(0, 1)
                    .xentity(1, 1)
                    .xentity(1, 0)
                    .xentity(0, 1)
                    .xentity(0, 1)
                    .xentity(1, 0)
                    .xentity(1, 0)
                    .function(3, 2)
                    .function(3, 3)
                    .finish());
        }

        /* Warehouse */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Warehouse")
                    .xentity(1, 1)
                    .xentity(1, 1)
                    .function(3, 2)
                    .function(3, 3)
                    .dstore(0, 1)
                    .dstore(1, 1)
                    .finish());
        }

        /* Order Processing */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Order Processing")
                    .function(2, 2)
                    .function(1, 3)
                    .function(1, 1)
                    .function(1, 2)
                    .function(1, 1)
                    .dstore(2, 1)
                    .dstore(2, 1)
                    .dstore(1, 1)
                    .finish());
        }

        /* Travel */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Travel")
                    .xentity(0, 1)
                    .xentity(1, 1)
                    .xentity(1, 0)
                    .function(2, 2)
                    .function(2, 1)
                    .function(1, 2)
                    .function(3, 2)
                    .dstore(0, 1)
                    .dstore(1, 0)
                    .dstore(0, 1)
                    .finish());
        }

        /* Ticket Booking */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Ticket Booking")
                    .xentity(0, 1)
                    .xentity(1, 1)
                    .xentity(1, 0)
                    .function(2, 2)
                    .function(1, 2)
                    .function(2, 1)
                    .function(2, 3)
                    .dstore(1, 0)
                    .dstore(0, 1)
                    .dstore(1, 0)
                    .finish());
        }

        /* Inventory */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Inventory")
                    .xentity(0, 1)
                    .xentity(1, 0)
                    .function(2, 2)
                    .function(1, 1)
                    .dstore(1, 1)
                    .dstore(1, 1)
                    .finish());
        }

        /* Sales Management */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Sales Management")
                    .function(1, 1)
                    .function(2, 2)
                    .function(2, 2)
                    .function(2, 4)
                    .dstore(0, 1)
                    .dstore(0, 1)
                    .dstore(2, 1)
                    .dstore(2, 1)
                    .finish());
        }

        /* Sales */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Sales")
                    .xentity(0, 2)
                    .function(1, 1)
                    .function(0, 2)
                    .function(1, 4)
                    .function(1, 2)
                    .function(1, 2)
                    .dstore(2, 0)
                    .dstore(3, 1)
                    .dstore(1, 2)
                    .dstore(3, 0)
                    .dstore(2, 0)
                    .dstore(1, 0)
                    .finish());
        }

        /* Reservation */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Reservation")
                    .xentity(0, 1)
                    .function(1, 1)
                    .function(1, 2)
                    .function(1, 1)
                    .function(1, 2)
                    .function(1, 0)
                    .dstore(1, 3)
                    .dstore(1, 1)
                    .dstore(1, 1)
                    .finish());
        }

        /* ATM System */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("ATM System")
                    .xentity(0, 1)
                    .function(2, 0)
                    .function(2, 3)
                    .function(1, 1)
                    .function(2, 2)
                    .function(1, 1)
                    .dstore(1, 1)
                    .dstore(1, 1)
                    .finish());
        }

        /* Registration */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Registration")
                    .xentity(0, 2)
                    .xentity(0, 1)
                    .function(1, 1)
                    .function(2, 3)
                    .function(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .function(2, 1)
                    .function(2, 2)
                    .function(1, 1)
                    .function(2, 1)
                    .dstore(3, 2)
                    .dstore(2, 1)
                    .dstore(2, 1)
                    .dstore(0, 1)
                    .finish());
        }

        /* Customers */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Customers")
                    .xentity(1, 1)
                    .xentity(0, 1)
                    .xentity(2, 1)
                    .function(2, 3)
                    .function(3, 1)
                    .function(3, 2)
                    .dstore(1, 1)
                    .dstore(1, 1)
                    .dstore(0, 3)
                    .finish());
        }

        /* Withdrawal Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Withdrawal Process")
                    .xentity(2, 2)
                    .xentity(1, 0)
                    .function(3, 2)
                    .function(1, 1)
                    .function(1, 5)
                    .dstore(2, 1)
                    .dstore(1, 0)
                    .finish());
        }

        /* Message */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Message")
                    .function(2, 2)
                    .function(3, 3)
                    .function(3, 3)
                    .function(2, 2)
                    .dstore(1, 1)
                    .dstore(3, 4)
                    .finish());
        }

        /* Library Management */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Library Management")
                    .function(1, 3)
                    .function(1, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .function(4, 2)
                    .finish());
        }
    }

    /* MyDraw */
    {
        /* Estate Agency Context */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Estate Agency Context")
                    .xentity(3, 3)
                    .function(3, 2)
                    .function(1, 2)
                    .finish());
        }

        /* Estate Agency Level 1 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Estate Agency Level 1")
                    .xentity(0, 1)
                    .xentity(0, 2)
                    .xentity(1, 1)
                    .xentity(2, 0)
                    .function(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .function(2, 1)
                    .function(2, 4)
                    .dstore(1, 1)
                    .dstore(2, 1)
                    .dstore(2, 1)
                    .finish());
        }

        /* Library Information System */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Library Information System")
                    .xentity(5, 4)
                    .xentity(1, 1)
                    .function(5, 6)
                    .finish());
        }

        /* Library Information System Level 1 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Library Information System Level 1")
                    .function(1, 2)
                    .function(2, 1)
                    .function(2, 9)
                    .function(6, 5)
                    .function(5, 4)
                    .dstore(1, 1)
                    .dstore(3, 2)
                    .finish());
        }

        /* Library System Level 1 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Library System Level 1")
                    .xentity(2, 2)
                    .xentity(1, 2)
                    .xentity(1, 0)
                    .xentity(1, 1)
                    .xentity(1, 2)
                    .function(3, 1)
                    .function(5, 3)
                    .function(6, 4)
                    .dstore(0, 1)
                    .dstore(0, 1)
                    .dstore(1, 2)
                    .dstore(1, 1)
                    .dstore(1, 2)
                    .finish());
        }

        /* Salon Management System Context */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Salon Management System Context")
                    .xentity(1, 2)
                    .xentity(4, 2)
                    .xentity(0, 1)
                    .xentity(4, 8)
                    .function(12, 9)
                    .finish());
        }

        /* Salon Management System Level 1 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Salon Management System Level 1")
                    .xentity(0, 3)
                    .xentity(4, 5)
                    .xentity(0, 1)
                    .xentity(2, 0)
                    .function(2, 2)
                    .function(4, 1)
                    .function(1, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .function(2, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .dstore(1, 0)
                    .dstore(1, 1)
                    .dstore(1, 1)
                    .dstore(1, 2)
                    .finish());
        }

        /* Video Rental Context */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Video Rental Context")
                    .function(6, 5)
                    .xentity(4, 4)
                    .xentity(2, 2)
                    .finish());
        }

        /* Video Rental Level 1 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Video Rental Level 1")
                    .xentity(0, 2)
                    .xentity(0, 4)
                    .xentity(1, 1)
                    .xentity(0, 2)
                    .xentity(2, 0)
                    .function(2, 1)
                    .function(7, 3)
                    .function(2, 3)
                    .dstore(1, 1)
                    .dstore(3, 1)
                    .finish());
        }

        /* Video Rental Level 2 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Video Rental Level 2")
                    .xentity(0, 2)
                    .xentity(1, 1)
                    .xentity(1, 1)
                    .function(3, 1)
                    .function(2, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .dstore(0, 1)
                    .dstore(2, 1)
                    .finish());
        }
    }
}
