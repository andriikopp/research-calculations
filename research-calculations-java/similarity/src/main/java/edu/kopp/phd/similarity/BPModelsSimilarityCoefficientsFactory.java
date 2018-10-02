package main.java.edu.kopp.phd.similarity;

import main.java.edu.kopp.phd.repository.domain.model.GenericModel;
import main.java.edu.kopp.phd.repository.domain.model.notation.ARISeEPCModel;
import main.java.edu.kopp.phd.repository.domain.model.notation.BPMNModel;
import main.java.edu.kopp.phd.repository.domain.model.notation.DFDModel;
import main.java.edu.kopp.phd.repository.domain.model.notation.IDEF0Model;
import main.java.edu.kopp.phd.similarity.beans.BPModelsSimilarityCoefficients;

public final class BPModelsSimilarityCoefficientsFactory {

    public static BPModelsSimilarityCoefficients getCoefficients(GenericModel first, GenericModel second) {
        if (first instanceof BPMNModel && second instanceof BPMNModel) {
            return new BPModelsSimilarityCoefficients(0.5, 0.5, 0, 0, 0, 0);
        }

        if ((first instanceof BPMNModel && second instanceof DFDModel) ||
                (first instanceof DFDModel && second instanceof BPMNModel)) {
            return new BPModelsSimilarityCoefficients(1, 0, 0, 0, 0, 0);
        }

        if ((first instanceof BPMNModel && second instanceof ARISeEPCModel) ||
                (first instanceof ARISeEPCModel && second instanceof BPMNModel)) {
            return new BPModelsSimilarityCoefficients(0.5, 0.5, 0, 0, 0, 0);
        }

        if ((first instanceof BPMNModel && second instanceof IDEF0Model) ||
                (first instanceof IDEF0Model && second instanceof BPMNModel)) {
            return new BPModelsSimilarityCoefficients(1, 0, 0, 0, 0, 0);
        }

        if (first instanceof DFDModel && second instanceof DFDModel) {
            return new BPModelsSimilarityCoefficients(0.5, 0, 0, 0, 0.5, 0);
        }

        if ((first instanceof DFDModel && second instanceof ARISeEPCModel) ||
                (first instanceof ARISeEPCModel && second instanceof DFDModel)) {
            return new BPModelsSimilarityCoefficients(0.5, 0, 0, 0, 0.5, 0);
        }

        if ((first instanceof DFDModel && second instanceof IDEF0Model) ||
                (first instanceof IDEF0Model && second instanceof DFDModel)) {
            return new BPModelsSimilarityCoefficients(0.5, 0, 0, 0, 0.5, 0);
        }

        if (first instanceof ARISeEPCModel && second instanceof ARISeEPCModel) {
            return new BPModelsSimilarityCoefficients(0.2, 0.2, 0.2, 0.2, 0.2, 0);
        }

        if ((first instanceof ARISeEPCModel && second instanceof IDEF0Model) ||
                (first instanceof IDEF0Model && second instanceof ARISeEPCModel)) {
            return new BPModelsSimilarityCoefficients(0.25, 0, 0.25, 0.25, 0.25, 0);
        }

        return new BPModelsSimilarityCoefficients(0.2, 0, 0.2, 0.2, 0.2, 0.2);
    }
}
