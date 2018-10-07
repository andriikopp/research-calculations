package main.java;

import main.java.edu.kopp.phd.repository.domain.category.CategoryType;
import main.java.edu.kopp.phd.repository.domain.model.GenericModel;
import main.java.edu.kopp.phd.repository.domain.model.notation.ARISeEPCModel;
import main.java.edu.kopp.phd.repository.domain.model.notation.BPMNModel;
import main.java.edu.kopp.phd.repository.domain.model.notation.DFDModel;
import main.java.edu.kopp.phd.repository.domain.model.notation.IDEF0Model;
import main.java.edu.kopp.phd.repository.domain.process.GenericProcess;
import main.java.edu.kopp.phd.similarity.BPModelsSimilarity;
import org.apache.jena.rdf.model.Resource;

public class SimilarityValidation {
    private static GenericProcess order = GenericProcess.createProcess("Order", CategoryType.OPERATION);

    public static GenericModel firstSampleModelBPMN() {
        BPMNModel model = BPMNModel.createModel("BPMN Model 01", order);

        Resource outOfStock = model.createStartEvent("Out of stock");
        Resource placeOrder = model.createActivity("Place order");
        Resource orderPlaced = model.createIntermediateEvent("Order placed");
        Resource signContract = model.createActivity("Sign contract");
        Resource contractSigned = model.createIntermediateEvent("Contract signed");
        Resource receiveGoods = model.createActivity("Receive goods");
        Resource goodsReceived = model.createIntermediateEvent("Goods received");
        Resource verifyInvoice = model.createActivity("Verify invoice");
        Resource invoiceVerified = model.createEndEvent("Invoice verified");

        return model
                .start()
                .createSequenceFlow(outOfStock, placeOrder)
                .createSequenceFlow(placeOrder, orderPlaced)
                .createSequenceFlow(orderPlaced, signContract)
                .createSequenceFlow(signContract, contractSigned)
                .createSequenceFlow(contractSigned, receiveGoods)
                .createSequenceFlow(receiveGoods, goodsReceived)
                .createSequenceFlow(goodsReceived, verifyInvoice)
                .createSequenceFlow(verifyInvoice, invoiceVerified)
                .finish();
    }

    public static GenericModel firstSampleModelDFD() {
        DFDModel model = DFDModel.createModel("DFD Model 01", order);

        Resource placeOrder = model.createActivity("Place order");
        Resource signContract = model.createActivity("Sign contract");
        Resource receiveGoods = model.createActivity("Receive goods");
        Resource verifyInvoice = model.createActivity("Verify invoice");

        Resource stockLevel = model.createBusinessObject("Stock level");
        Resource order = model.createBusinessObject("Order");
        Resource contract = model.createBusinessObject("Contract");
        Resource goods = model.createBusinessObject("Goods");
        Resource invoice = model.createBusinessObject("Invoice");

        Resource inventoryPlanning = model.createExternalEntity("Inventory planning");
        Resource warehousing = model.createExternalEntity("Warehousing");

        return model
                .start()
                .createOutput(inventoryPlanning, stockLevel)
                .createInput(placeOrder, stockLevel)
                .createOutput(placeOrder, order)
                .createInput(signContract, order)
                .createOutput(signContract, contract)
                .createInput(receiveGoods, contract)
                .createOutput(receiveGoods, goods)
                .createInput(verifyInvoice, goods)
                .createOutput(verifyInvoice, invoice)
                .createInput(warehousing, invoice)
                .finish();
    }

    public static GenericModel firstSampleModelARISeEPC() {
        ARISeEPCModel model = ARISeEPCModel.createModel("ARIS eEPC Model 01", order);

        Resource outOfStock = model.createStartEvent("Out of stock");
        Resource placeOrder = model.createActivity("Place order");
        Resource orderPlaced = model.createIntermediateEvent("Order placed");
        Resource signContract = model.createActivity("Sign contract");
        Resource contractSigned = model.createIntermediateEvent("Contract signed");
        Resource receiveGoods = model.createActivity("Receive goods");
        Resource goodsReceived = model.createIntermediateEvent("Goods received");
        Resource verifyInvoice = model.createActivity("Verify invoice");
        Resource invoiceVerified = model.createEndEvent("Invoice verified");

        Resource supplyDept = model.createDepartment("Supply Dept.");
        Resource warehouse = model.createDepartment("Warehouse");
        Resource accountingDept = model.createDepartment("Accounting Dept.");

        Resource SRM = model.createApplicationSystem("SRM");
        Resource WM = model.createApplicationSystem("WM");

        return model
                .start()
                .createSequenceFlow(outOfStock, placeOrder)
                .createSequenceFlow(placeOrder, orderPlaced)
                .createSequenceFlow(orderPlaced, signContract)
                .createSequenceFlow(signContract, contractSigned)
                .createSequenceFlow(contractSigned, receiveGoods)
                .createSequenceFlow(receiveGoods, goodsReceived)
                .createSequenceFlow(goodsReceived, verifyInvoice)
                .createSequenceFlow(verifyInvoice, invoiceVerified)
                .createResponsible(placeOrder, supplyDept)
                .createResponsible(signContract, supplyDept)
                .createResponsible(receiveGoods, warehouse)
                .createResponsible(verifyInvoice, accountingDept)
                .createSupport(placeOrder, SRM)
                .createSupport(receiveGoods, SRM, WM)
                .finish();
    }

    public static GenericModel firstSampleModelIDEF0() {
        IDEF0Model model = IDEF0Model.createModel("IDEF0 Model 01", order);

        Resource placeOrder = model.createActivity("Place order");
        Resource signContract = model.createActivity("Sign contract");
        Resource receiveGoods = model.createActivity("Receive goods");
        Resource verifyInvoice = model.createActivity("Verify invoice");

        Resource supplyDept = model.createDepartment("Supply Dept.");
        Resource warehouse = model.createDepartment("Warehouse");
        Resource accountingDept = model.createDepartment("Accounting Dept.");

        Resource SRM = model.createApplicationSystem("SRM");
        Resource WM = model.createApplicationSystem("WM");

        Resource stockLevel = model.createBusinessObject("Stock level");
        Resource order = model.createBusinessObject("Order");
        Resource contract = model.createBusinessObject("Contract");
        Resource goods = model.createBusinessObject("Goods");
        Resource invoice = model.createBusinessObject("Invoice");

        return model
                .start()
                .createInput(placeOrder, stockLevel)
                .createOutput(placeOrder, order)
                .createInput(signContract, order)
                .createOutput(signContract, contract)
                .createInput(receiveGoods, contract)
                .createOutput(receiveGoods, goods)
                .createInput(verifyInvoice, goods)
                .createOutput(verifyInvoice, invoice)
                .createResponsible(placeOrder, supplyDept)
                .createResponsible(signContract, supplyDept)
                .createResponsible(receiveGoods, warehouse)
                .createResponsible(verifyInvoice, accountingDept)
                .createSupport(placeOrder, SRM)
                .createSupport(receiveGoods, SRM, WM)
                .finish();
    }

    public static GenericModel secondSampleModelBPMN() {
        BPMNModel model = BPMNModel.createModel("BPMN Model 02", order);

        Resource outOfStock = model.createStartEvent("Out of stock");
        Resource placeOrder = model.createActivity("Place order");
        Resource orderPlaced = model.createIntermediateEvent("Order placed");
        Resource receiveGoods = model.createActivity("Receive goods");
        Resource goodsReceived = model.createIntermediateEvent("Goods received");
        Resource andSplit = model.createAndSplitGateway();
        Resource storeGoods = model.createActivity("Store goods");
        Resource verifyInvoice = model.createActivity("Verify invoice");
        Resource andJoin = model.createAndJoinGateway();
        Resource goodsSupplied = model.createEndEvent("Goods supplied");

        return model
                .start()
                .createSequenceFlow(outOfStock, placeOrder)
                .createSequenceFlow(placeOrder, orderPlaced)
                .createSequenceFlow(orderPlaced, receiveGoods)
                .createSequenceFlow(receiveGoods, goodsReceived)
                .createSequenceFlow(goodsReceived, andSplit)
                .createSequenceFlow(andJoin, storeGoods, verifyInvoice)
                .createSequenceFlow(storeGoods, andJoin)
                .createSequenceFlow(verifyInvoice, andJoin)
                .createSequenceFlow(andJoin, goodsSupplied)
                .finish();
    }

    public static GenericModel secondSampleModelDFD() {
        DFDModel model = DFDModel.createModel("DFD Model 02", order);

        Resource placeOrder = model.createActivity("Place order");
        Resource receiveGoods = model.createActivity("Receive goods");
        Resource storeGoods = model.createActivity("Store goods");
        Resource verifyInvoice = model.createActivity("Verify invoice");

        Resource stockLevel = model.createBusinessObject("Stock level");
        Resource order = model.createBusinessObject("Order");
        Resource goods = model.createBusinessObject("Goods");
        Resource invoice = model.createBusinessObject("Invoice");

        Resource inventoryPlanning = model.createExternalEntity("Inventory planning");
        Resource warehousing = model.createExternalEntity("Warehousing");

        return model
                .start()
                .createOutput(inventoryPlanning, stockLevel)
                .createInput(placeOrder, stockLevel)
                .createOutput(placeOrder, order)
                .createInput(receiveGoods, order)
                .createOutput(receiveGoods, goods)
                .createInput(storeGoods, goods)
                .createInput(verifyInvoice, goods)
                .createOutput(verifyInvoice, invoice)
                .createInput(warehousing, invoice)
                .finish();
    }

    public static GenericModel secondSampleModelARISeEPC() {
        ARISeEPCModel model = ARISeEPCModel.createModel("ARIS eEPC Model 02", order);

        Resource outOfStock = model.createStartEvent("Out of stock");
        Resource placeOrder = model.createActivity("Place order");
        Resource orderPlaced = model.createIntermediateEvent("Order placed");
        Resource receiveGoods = model.createActivity("Receive goods");
        Resource goodsReceived = model.createIntermediateEvent("Goods received");
        Resource andSplit = model.createAndSplitGateway();
        Resource storeGoods = model.createActivity("Store goods");
        Resource verifyInvoice = model.createActivity("Verify invoice");
        Resource andJoin = model.createAndJoinGateway();
        Resource goodsSupplied = model.createEndEvent("Goods supplied");

        Resource supplyDept = model.createDepartment("Supply Dept.");
        Resource warehouse = model.createDepartment("Warehouse");
        Resource accountingDept = model.createDepartment("Accounting Dept.");

        Resource SRM = model.createApplicationSystem("SRM");
        Resource WM = model.createApplicationSystem("WM");

        return model
                .start()
                .createSequenceFlow(outOfStock, placeOrder)
                .createSequenceFlow(placeOrder, orderPlaced)
                .createSequenceFlow(orderPlaced, receiveGoods)
                .createSequenceFlow(receiveGoods, goodsReceived)
                .createSequenceFlow(goodsReceived, andSplit)
                .createSequenceFlow(andJoin, storeGoods, verifyInvoice)
                .createSequenceFlow(storeGoods, andJoin)
                .createSequenceFlow(verifyInvoice, andJoin)
                .createSequenceFlow(andJoin, goodsSupplied)
                .createResponsible(placeOrder, supplyDept)
                .createResponsible(receiveGoods, warehouse)
                .createResponsible(verifyInvoice, accountingDept)
                .createResponsible(storeGoods, warehouse)
                .createSupport(placeOrder, SRM)
                .createSupport(receiveGoods, SRM, WM)
                .createSupport(storeGoods, WM)
                .finish();
    }

    public static GenericModel secondSampleModelIDEF0() {
        IDEF0Model model = IDEF0Model.createModel("IDEF0 Model 02", order);

        Resource placeOrder = model.createActivity("Place order");
        Resource receiveGoods = model.createActivity("Receive goods");
        Resource storeGoods = model.createActivity("Store goods");
        Resource verifyInvoice = model.createActivity("Verify invoice");

        Resource supplyDept = model.createDepartment("Supply Dept.");
        Resource warehouse = model.createDepartment("Warehouse");
        Resource accountingDept = model.createDepartment("Accounting Dept.");

        Resource SRM = model.createApplicationSystem("SRM");
        Resource WM = model.createApplicationSystem("WM");

        Resource stockLevel = model.createBusinessObject("Stock level");
        Resource order = model.createBusinessObject("Order");
        Resource goods = model.createBusinessObject("Goods");
        Resource invoice = model.createBusinessObject("Invoice");

        return model
                .start()
                .createInput(placeOrder, stockLevel)
                .createOutput(placeOrder, order)
                .createInput(receiveGoods, order)
                .createOutput(receiveGoods, goods)
                .createInput(storeGoods, goods)
                .createInput(verifyInvoice, goods)
                .createOutput(verifyInvoice, invoice)
                .createResponsible(placeOrder, supplyDept)
                .createResponsible(receiveGoods, warehouse)
                .createResponsible(verifyInvoice, accountingDept)
                .createResponsible(storeGoods, warehouse)
                .createSupport(placeOrder, SRM)
                .createSupport(receiveGoods, SRM, WM)
                .createSupport(storeGoods, WM)
                .finish();
    }

    public static final void main(String[] args) {
        GenericModel[] models = {
                firstSampleModelBPMN(),
                firstSampleModelDFD(),
                firstSampleModelARISeEPC(),
                firstSampleModelIDEF0(),
                secondSampleModelBPMN(),
                secondSampleModelDFD(),
                secondSampleModelARISeEPC(),
                secondSampleModelIDEF0()
        };

        for (GenericModel first : models) {
            System.out.printf("%s\t", first.getName());

            for (GenericModel second : models) {
                double similarity = BPModelsSimilarity.similarity(first, second);

                System.out.printf("%.2f\t", similarity);
            }

            System.out.println();
        }
    }
}
