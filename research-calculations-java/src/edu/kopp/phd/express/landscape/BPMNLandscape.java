package edu.kopp.phd.express.landscape;

import edu.kopp.phd.express.governance.ProcessFlowGovernanceLog;
import edu.kopp.phd.express.metamodel.ModelBuilder;

public class BPMNLandscape extends Landscape {

    public BPMNLandscape() {
        super(new ProcessFlowGovernanceLog());
    }

    /* Conceptdraw */
    {
        /* A Process with Normal Flow */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("A Process with Normal Flow")
                    .event("Working group active", 0, 1)
                    .event("Friday at 6 PM Pacific time", 2, 1)
                    .function("Check status of working group", 1, 1)
                    .xor("Working group still active?", 1, 2)
                    .event(1, 0)
                    .function("Send current issue list", 1, 1)
                    .finish());
        }

        /* Application Handling and Invoicing */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Application Handling and Invoicing")
                    .event("Application", 0, 1)
                    .xor(2, 1)
                    .function("Checking for correctness", 1, 1)
                    .xor("Application correct?", 1, 2)
                    .function("Request specified data", 1, 0)
                    .function("Form a new application for shipment", 1, 1)
                    .function("Form a new application for shipment", 1, 0)
                    .event("Client invoice", 0, 0)
                    .event("Email client", 0, 1)
                    .or(1, 3)
                    .event(1, 1)
                    .event(1, 1)
                    .event("2 days", 1, 1)
                    .event("Client refusal", 1, 0)
                    .event("No answer", 1, 0)
                    .finish());
        }

        /* Booking Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Booking Process")
                    .event(0, 1)
                    .function("Receive book request", 1, 1)
                    .function("Get book status", 2, 1)
                    .xor(1, 2)
                    .function("On loan reply", 1, 1)
                    .function("Checkout book", 1, 1)
                    .function("Checkout reply", 1, 1)
                    .or(1, 3)
                    .event("Hold book", 1, 1)
                    .event("Decline hold", 1, 1)
                    .event("1 week", 1, 1)
                    .function("Request hold", 1, 1)
                    .function("Cancel request",2, 1)
                    .function("Hold reply", 1, 1)
                    .event(2, 0)
                    .finish());
        }

        /* Logistics */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Logistics")
                    .event("Start", 0, 1)
                    .function("Buy request", 1, 1)
                    .function("Check credit", 1, 1)
                    .xor("Check customer known",1, 2)
                    .function("Customer not known", 1, 1)
                    .function("Credit rating", 1, 1)
                    .xor("Evaluate credit rating", 1, 2)
                    .function("Insufficient credit", 1, 1)
                    .function("Deliver goods", 1, 1)
                    .function("Acknowledge delivery details", 1, 1)
                    .function("Buy confirmed", 1, 1)
                    .xor(2, 1)
                    .function("Account not found", 1, 1)
                    .xor(2, 1)
                    .event("End", 1, 0)
                    .finish());
        }

        /* Order Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Order Process")
                    .event(0, 1)
                    .function("Quotation handling", 1, 1)
                    .function("Approve order", 1, 1)
                    .xor(1, 2)
                    .event(1, 0)
                    .and(1, 2)
                    .function("Order handling", 1, 1)
                    .function("Order handling", 1, 1)
                    .and(2, 1)
                    .function("Review order", 1, 1)
                    .event(1, 0)
                    .finish());
        }
    }

    /* EDraw */
    {
        /* Online Books Selling Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Online Books Selling Process")
                    .event("Receiving Request", 0, 1)
                    .function("Record Order", 1, 1)
                    .xor("Check Stock", 1, 2)
                    .function("", 1, 1)
                    .xor(2, 1)
                    .and(1, 2)
                    .function("Charging Issue", 1, 1)
                    .function("Separate books of the request", 1, 1)
                    .event("Confirm Receipt", 1, 1)
                    .function("Issue the invoice", 1, 1)
                    .and(2, 1)
                    .function("Deliver books to customer", 1, 1)
                    .function("Confirm Delivery", 1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Buying Mobile Phone */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Buying Mobile Phone")
                    .event(0, 1)
                    .function("Request", 1, 1)
                    .function("Check Commodity", 1, 1)
                    .xor("Is the Phone Available?", 1, 2)
                    .function("Check Money", 1, 1)
                    .xor("Have enough money?", 1, 2)
                    .function("Pay the bill", 1, 1)
                    .function("Hand over the phone", 1, 1)
                    .event(3, 0)
                    .finish());
        }

        /* Online Shopping Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Online Shopping Process")
                    .event(0, 1)
                    .function("Choose a commodity", 1, 1)
                    .event("Submit order", 1, 0)
                    .event(0, 1)
                    .xor("Check status", 1, 2)
                    .function("Indicate stock out", 1, 1)
                    .function("Prepare the commodity", 1, 1)
                    .function("Deliver the commodity", 1, 1)
                    .event("Stock out notice", 1, 0)
                    .event("Shipment notice", 1, 0)
                    .finish());
        }

        /* Employment Application */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Employment Application")
                    .event(0, 1)
                    .function("Submit application", 1, 1)
                    .xor(1, 2)
                    .event("Rejected", 1, 0)
                    .function("Interview", 1, 2)
                    .event(1, 1)
                    .event(1, 0)
                    .function("Review offer", 1, 1)
                    .xor("Satisfied?", 1, 2)
                    .event("Get hired", 1, 0)
                    .function("Reject offer", 1, 0)
                    .event(0, 1)
                    .function("Review application", 1, 1)
                    .xor(1, 2)
                    .function("Reject application", 1, 1)
                    .event(1, 0)
                    .function("Plan interview", 1, 1)
                    .function("Interview", 1, 1)
                    .function("Judge and decide", 1, 1)
                    .xor(1, 2)
                    .function("Reject Application", 1, 0)
                    .function("Send offer", 1, 1)
                    .function("Hire candidate", 1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* ERP Management */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("ERP Management")
                    .event(0, 1)
                    .event(1, 1)
                    .function("Add new data", 1, 1)
                    .xor(1, 2)
                    .function("Confirmed by information department", 1, 1)
                    .function("Synchronize to the directory", 2, 1)
                    .xor(1, 2)
                    .function("Department manager examine", 1, 1)
                    .event("Failed", 2, 0)
                    .xor(1, 2)
                    .function("Build sub system accounts", 1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Call Complaint */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Call Complaint")
                    .event(0, 1)
                    .xor(1, 2)
                    .function("Answer calls", 1, 1)
                    .function("Receive calls", 2, 1)
                    .xor("Can the problem be solved?", 1, 2)
                    .event(1, 0)
                    .function("Record conversation", 1, 1)
                    .function("Call back", 1, 1)
                    .function("Announce", 1, 1)
                    .event(3, 0)
                    .xor("Is it customer complain?", 1, 2)
                    .function("Transfer service", 1, 1)
                    .function("Handle the compliant", 1, 1)
                    .xor("Can the problem be solved?", 1, 2)
                    .function("Terminal the case", 1, 1)
                    .function("Call back", 1, 1)
                    .and(0, 2)
                    .function("Result identification", 1, 1)
                    .function("Result confirmation", 1, 1)
                    .and(1, 1)
                    .event(2, 0)
                    .and(1, 2)
                    .function("Call back confirmation (m)", 1, 1)
                    .function("Call back confirmation (e)", 1, 1)
                    .function("Accounting (m)", 1, 1)
                    .function("Accounting (e)", 1, 1)
                    .and(2, 1)
                    .function("Supplier on site service", 1, 1)
                    .function("Feed back result", 1, 1)
                    .finish());
        }

        /* Shopping Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Shopping Process")
                    .event(0, 1)
                    .function("Order", 1, 1)
                    .function("Wait for goods", 1, 1)
                    .function("Receive the goods", 1, 1)
                    .function("Evaluation", 1, 1)
                    .xor("Satisfied?", 1, 2)
                    .function("Complain", 1, 1)
                    .xor(2, 1)
                    .event(1, 0)
                    .event(0, 1)
                    .function("Process order", 1, 1)
                    .function("Deliver the goods", 1, 0)
                    .event(0, 1)
                    .function("Process compliant", 1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Magazine Production Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Magazine Production Process")
                    .event("1st of each month", 0, 1)
                    .function("Choose manuscript", 1, 1)
                    .function("Edin manuscript", 1, 1)
                    .event("Finish editing", 1, 0)
                    .function("Magazine typesetting", 0, 1)
                    .event("Finish typesetting", 1, 0)
                    .function("Print", 0, 1)
                    .event("1st of each month", 1, 1)
                    .function("Publish", 1, 1)
                    .event("Magazine publish", 1, 0)
                    .finish());
        }

        /* Leave Request Procedure */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Leave Request Procedure")
                    .event("Start", 0, 1)
                    .function("Send leave application", 1, 1)
                    .xor("Absence duration", 1, 2)
                    .function("Department manager approval", 1, 1)
                    .function("Group leader approval", 1, 1)
                    .xor("Combine", 2, 1)
                    .and("Filing", 1, 2)
                    .function("HR confirmation", 1, 1)
                    .function("Filling document in attendance system", 1, 1)
                    .xor(2, 1)
                    .event("End",1, 0)
                    .finish());
        }

        /* Auctioning Service */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Auctioning Service")
                    .event(0, 1)
                    .function("Send auction creation request", 1, 1)
                    .xor(1, 2)
                    .event("Account creation request", 1, 1)
                    .function("Send registration info.", 1, 1)
                    .xor(2, 1)
                    .event("Auction creation confirm", 1, 1)
                    .function("Send payment", 1, 0)
                    .event(0, 1)
                    .event(1, 1)
                    .xor("Already registered", 1, 2)
                    .function("Second creation request", 1, 1)
                    .event(1, 1)
                    .xor(2, 1)
                    .event("Payment", 1, 1)
                    .function("Second creation confirmation", 1, 0)
                    .finish());
        }
    }

    /* MyDraw */
    {
        /* Auction Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Auction Process")
                    .event("Item available for sale", 0, 1)
                    .function("Register item for auction", 2, 1)
                    .xor("Auction type", 1, 2)
                    .function("Buy item now", 2, 2)
                    .function("Bid for item", 1, 2)
                    .function("Close auction", 2, 1)
                    .xor("Item sold?", 1, 2)
                    .function("Complete sale", 2, 1)
                    .function("Collect comissions", 1, 1)
                    .xor("Re-submit item", 1, 2)
                    .event("Successful auction", 1, 0)
                    .event("Item not sold", 1, 0)
                    .finish());
        }

        /* Book Lending Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Book Lending Process")
                    .event(0, 1)
                    .function("Receive book lending request", 1, 1)
                    .function("Get the book status", 2, 1)
                    .xor(1, 2)
                    .function("On Loan reply", 1, 1)
                    .function("Checkout the book", 1, 1)
                    .event("Two weeks", 0, 2)
                    .or(1, 3)
                    .function("Checkout reply", 1, 1)
                    .event("Hold book", 1, 1)
                    .event("One week", 1, 1)
                    .event("Decline hold", 1, 1)
                    .function("Request hold", 1, 1)
                    .function("Cancel request", 2, 1)
                    .function("Hold reply", 2, 0)
                    .event(2, 0)
                    .finish());
        }

        /* Discussion Cycle Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Discussion Cycle Process")
                    .event(0, 1)
                    .function("Announce Issues for Discussion", 1, 3)
                    .function("Moderate E-mail discussion", 1, 2)
                    .event("Delay 6 days from Announcement", 1, 1)
                    .function("Check Calendar for Conference Call", 1, 1)
                    .xor(2, 1)
                    .function("E-mail Discussion Deadline Warning", 1, 1)
                    .xor("Conference Call in Discussion Week", 1, 2)
                    .event("Wait until Thursday, 9am", 1, 1)
                    .function("Moderate Conference Call Discussion", 1, 1)
                    .xor(2, 1)
                    .and(3, 1)
                    .function("Evaluate Discussion Progress", 1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Issues Report Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Issues Report Process")
                    .event("Working group active", 0, 1)
                    .event("Friday at 6PM Pacific Time", 1, 1)
                    .function("Check status of working group", 2, 1)
                    .xor("Working group still active?", 1, 2)
                    .function("Send current issue list", 1, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Item Ordering Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Item Ordering Process")
                    .event(0, 1)
                    .function("Receive Item Order Request", 1, 1)
                    .xor(1, 2)
                    .function("Process Item Order", 1, 2)
                    .function("Ship Item Order", 1, 1)
                    .function("Send Invoice", 1, 1)
                    .function("Make Payment Via Paypal", 1, 1)
                    .function("Paypal Payment Accepted", 1, 1)
                    .and(2, 1)
                    .function("Close Order", 2, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Order Fulfillment Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Order Fulfillment Process")
                    .event(0, 1)
                    .function("Check Ability", 1, 1)
                    .xor(1, 2)
                    .function("Ship Article", 1, 1)
                    .function("Procurement", 1, 2)
                    .function("Inform Customer", 1, 1)
                    .function("Financial Settlement", 1, 1)
                    .function("Inform Customer", 1, 1)
                    .function("Remove article from catalogue", 1, 1)
                    .event("Payment Received", 1, 0)
                    .event("Customer Informed", 1, 0)
                    .event("Article Removed", 1, 0)
                    .finish());
        }

        /* Order Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Order Process")
                    .event(0, 2)
                    .function("Store Order", 1, 1)
                    .event(1, 0)
                    .function("Retrieve past orders", 1, 1)
                    .function("Check manufacturing capacity & if the parts are available", 1, 2)
                    .function("Reset system", 1, 1)
                    .event(1, 0)
                    .xor(1, 3)
                    .function("Collect parts", 1, 1)
                    .function("Send notice to customer about rejection of order", 1, 1)
                    .function("Order parts", 1, 1)
                    .event(3, 0)
                    .finish());
        }

        /* Payment Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Payment Process")
                    .event(0, 1)
                    .function("Identify payment method", 1, 1)
                    .xor("Payment Method?", 1, 2)
                    .function("Accept cash or check", 1, 1)
                    .function("Process credit card", 1, 1)
                    .function("Prepare package for customer", 2, 1)
                    .event(1, 0)
                    .finish());
        }

        /* Crohn’s Disease Clinical Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Crohn’s Disease Clinical Process")
                    .function("P1", 0, 1)
                    .xor("Risk", 1, 2)
                    .event("NO C.D.", 1, 0)
                    .function("P2", 1, 1)
                    .xor("Risk", 1, 2)
                    .function("Nurse's Consulting", 1, 0)
                    .function("Hospital Admission", 1, 2)
                    .function("Hospital", 1, 1)
                    .function("P3", 2, 1)
                    .xor("Positive Test", 1, 2)
                    .event("NO C.D.", 1, 0)
                    .function("P4", 1, 1)
                    .xor("Positive Test", 1, 2)
                    .event("NO C.D.", 1, 0)
                    .function("Montreal Classification", 1, 1)
                    .xor("Surgery", 1, 2)
                    .function("Operating Room", 1, 1)
                    .function("Remission", 2, 0)
                    .finish());
        }

        /* Medical Appointment Scheduling Process */
        {
            getGovernanceLog().getLandscape().add(ModelBuilder.model("Medical Appointment Scheduling Process")
                    .event("Medical appointment requisition", 0, 1)
                    .function("Assign appointment to request queue (by medical record/service", 1, 1)
                    .event("Daily", 1, 1)
                    .function("Check request queue", 1, 1)
                    .function("Select speciality", 1, 1)
                    .function("Suggest first available slot (date/time)", 1, 1)
                    .function("Check if slot within clinical time", 1, 1)
                    .xor(1, 2)
                    .function("Ask the Service Director for date/time within clinical time", 1, 1)
                    .function("Confirm date/time in the system", 2, 1)
                    .function("Set slot as scheduled", 1, 1)
                    .event(1, 0)
                    .finish());
        }
    }
}
