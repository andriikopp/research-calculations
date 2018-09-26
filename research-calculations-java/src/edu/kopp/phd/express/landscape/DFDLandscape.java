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
                    .externalEntity(1, 1)
                    .externalEntity(2, 1)
                    .function(4, 2)
                    .function(7, 4)
                    .function(2, 6)
                    .function(1, 1)
                    .dataStore(2, 0)
                    .dataStore(0, 1)
                    .dataStore(1, 1)
                    .dataStore(2, 1)
                    .finish());
        }

        /* Process of Account Receivable */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Process of Account Receivable")
                    .externalEntity(2, 2)
                    .externalEntity(1, 1)
                    .function(1, 1)
                    .function(1, 2)
                    .function(1, 1)
                    .function(2, 2)
                    .function(3, 1)
                    .function(2, 1)
                    .function(1, 2)
                    .function(2, 1)
                    .dataStore(2, 0)
                    .dataStore(0, 1)
                    .dataStore(0, 2)
                    .dataStore(1, 2)
                    .dataStore(1, 1)
                    .finish());
        }

        /* Customer purchase */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Customer purchase")
                    .externalEntity(0, 1)
                    .externalEntity(1, 1)
                    .function(1, 1)
                    .function(2, 2)
                    .function(2, 2)
                    .function(2, 1)
                    .dataStore(1, 1)
                    .dataStore(1, 1)
                    .finish());
        }
    }

    /* EDraw */
    {
        /* Selling */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Selling")
                    .externalEntity(0, 1)
                    .externalEntity(0, 1)
                    .externalEntity(1, 1)
                    .externalEntity(1, 0)
                    .externalEntity(0, 1)
                    .externalEntity(0, 1)
                    .externalEntity(1, 0)
                    .externalEntity(1, 0)
                    .function(3, 2)
                    .function(3, 3)
                    .finish());
        }

        /* Warehouse */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Warehouse")
                    .externalEntity(1, 1)
                    .externalEntity(1, 1)
                    .function(3, 2)
                    .function(3, 3)
                    .dataStore(0, 1)
                    .dataStore(1, 1)
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
                    .dataStore(2, 1)
                    .dataStore(2, 1)
                    .dataStore(1, 1)
                    .finish());
        }

        /* Travel */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Travel")
                    .externalEntity(0, 1)
                    .externalEntity(1, 1)
                    .externalEntity(1, 0)
                    .function(2, 2)
                    .function(2, 1)
                    .function(1, 2)
                    .function(3, 2)
                    .dataStore(0, 1)
                    .dataStore(1, 0)
                    .dataStore(0, 1)
                    .finish());
        }

        /* Ticket Booking */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Ticket Booking")
                    .externalEntity(0, 1)
                    .externalEntity(1, 1)
                    .externalEntity(1, 0)
                    .function(2, 2)
                    .function(1, 2)
                    .function(2, 1)
                    .function(2, 3)
                    .dataStore(1, 0)
                    .dataStore(0, 1)
                    .dataStore(1, 0)
                    .finish());
        }

        /* Inventory */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Inventory")
                    .externalEntity(0, 1)
                    .externalEntity(1, 0)
                    .function(2, 2)
                    .function(1, 1)
                    .dataStore(1, 1)
                    .dataStore(1, 1)
                    .finish());
        }

        /* Sales Management */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Sales Management")
                    .function(1, 1)
                    .function(2, 2)
                    .function(2, 2)
                    .function(2, 4)
                    .dataStore(0, 1)
                    .dataStore(0, 1)
                    .dataStore(2, 1)
                    .dataStore(2, 1)
                    .finish());
        }

        /* Sales */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Sales")
                    .externalEntity(0, 2)
                    .function(1, 1)
                    .function(0, 2)
                    .function(1, 4)
                    .function(1, 2)
                    .function(1, 2)
                    .dataStore(2, 0)
                    .dataStore(3, 1)
                    .dataStore(1, 2)
                    .dataStore(3, 0)
                    .dataStore(2, 0)
                    .dataStore(1, 0)
                    .finish());
        }

        /* Reservation */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Reservation")
                    .externalEntity(0, 1)
                    .function(1, 1)
                    .function(1, 2)
                    .function(1, 1)
                    .function(1, 2)
                    .function(1, 0)
                    .dataStore(1, 3)
                    .dataStore(1, 1)
                    .dataStore(1, 1)
                    .finish());
        }

        /* ATM System */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("ATM System")
                    .externalEntity(0, 1)
                    .function(2, 0)
                    .function(2, 3)
                    .function(1, 1)
                    .function(2, 2)
                    .function(1, 1)
                    .dataStore(1, 1)
                    .dataStore(1, 1)
                    .finish());
        }

        /* Registration */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Registration")
                    .externalEntity(0, 2)
                    .externalEntity(0, 1)
                    .function(1, 1)
                    .function(2, 3)
                    .function(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .function(2, 1)
                    .function(2, 2)
                    .function(1, 1)
                    .function(2, 1)
                    .dataStore(3, 2)
                    .dataStore(2, 1)
                    .dataStore(2, 1)
                    .dataStore(0, 1)
                    .finish());
        }

        /* Customers */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Customers")
                    .externalEntity(1, 1)
                    .externalEntity(0, 1)
                    .externalEntity(2, 1)
                    .function(2, 3)
                    .function(3, 1)
                    .function(3, 2)
                    .dataStore(1, 1)
                    .dataStore(1, 1)
                    .dataStore(0, 3)
                    .finish());
        }

        /* Withdrawal Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Withdrawal Process")
                    .externalEntity(2, 2)
                    .externalEntity(1, 0)
                    .function(3, 2)
                    .function(1, 1)
                    .function(1, 5)
                    .dataStore(2, 1)
                    .dataStore(1, 0)
                    .finish());
        }

        /* Message */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Message")
                    .function(2, 2)
                    .function(3, 3)
                    .function(3, 3)
                    .function(2, 2)
                    .dataStore(1, 1)
                    .dataStore(3, 4)
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
                    .externalEntity(3, 3)
                    .function(3, 2)
                    .function(1, 2)
                    .finish());
        }

        /* Estate Agency Level 1 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Estate Agency Level 1")
                    .externalEntity(0, 1)
                    .externalEntity(0, 2)
                    .externalEntity(1, 1)
                    .externalEntity(2, 0)
                    .function(1, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .function(2, 1)
                    .function(2, 4)
                    .dataStore(1, 1)
                    .dataStore(2, 1)
                    .dataStore(2, 1)
                    .finish());
        }

        /* Library Information System */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Library Information System")
                    .externalEntity(5, 4)
                    .externalEntity(1, 1)
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
                    .dataStore(1, 1)
                    .dataStore(3, 2)
                    .finish());
        }

        /* Library System Level 1 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Library System Level 1")
                    .externalEntity(2, 2)
                    .externalEntity(1, 2)
                    .externalEntity(1, 0)
                    .externalEntity(1, 1)
                    .externalEntity(1, 2)
                    .function(3, 1)
                    .function(5, 3)
                    .function(6, 4)
                    .dataStore(0, 1)
                    .dataStore(0, 1)
                    .dataStore(1, 2)
                    .dataStore(1, 1)
                    .dataStore(1, 2)
                    .finish());
        }

        /* Salon Management System Context */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Salon Management System Context")
                    .externalEntity(1, 2)
                    .externalEntity(4, 2)
                    .externalEntity(0, 1)
                    .externalEntity(4, 8)
                    .function(12, 9)
                    .finish());
        }

        /* Salon Management System Level 1 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Salon Management System Level 1")
                    .externalEntity(0, 3)
                    .externalEntity(4, 5)
                    .externalEntity(0, 1)
                    .externalEntity(2, 0)
                    .function(2, 2)
                    .function(4, 1)
                    .function(1, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .function(2, 1)
                    .function(1, 1)
                    .function(1, 1)
                    .dataStore(1, 0)
                    .dataStore(1, 1)
                    .dataStore(1, 1)
                    .dataStore(1, 2)
                    .finish());
        }

        /* Video Rental Context */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Video Rental Context")
                    .function(6, 5)
                    .externalEntity(4, 4)
                    .externalEntity(2, 2)
                    .finish());
        }

        /* Video Rental Level 1 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Video Rental Level 1")
                    .externalEntity(0, 2)
                    .externalEntity(0, 4)
                    .externalEntity(1, 1)
                    .externalEntity(0, 2)
                    .externalEntity(2, 0)
                    .function(2, 1)
                    .function(7, 3)
                    .function(2, 3)
                    .dataStore(1, 1)
                    .dataStore(3, 1)
                    .finish());
        }

        /* Video Rental Level 2 */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Video Rental Level 2")
                    .externalEntity(0, 2)
                    .externalEntity(1, 1)
                    .externalEntity(1, 1)
                    .function(3, 1)
                    .function(2, 2)
                    .function(1, 1)
                    .function(1, 1)
                    .dataStore(0, 1)
                    .dataStore(2, 1)
                    .finish());
        }
    }
}
