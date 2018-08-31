package edu.kopp.phd.express.standards;

import edu.kopp.phd.express.metamodel.Model;
import edu.kopp.phd.express.standards.api.Validator;

public class ProcessEnvironmentValidator implements Validator {
    private double org;
    private double in;
    private double reg;
    private double out;
    private double app;

    public ProcessEnvironmentValidator() {
        this.org = 0.83;
        this.in = 1.00;
        this.reg = 1.00;
        this.out = 1.00;
        this.app = 0.67;
    }

    @Override
    public double validate(Model model) {
        return org * (countFunctions(model) - countValidOrgFunctions(model)) +
                in * (countFunctions(model) - countValidInFunctions(model)) +
                reg * (countFunctions(model) - countValidRegFunctions(model)) +
                out * (countFunctions(model) - countValidOutFunctions(model)) +
                app * (countFunctions(model) - countValidAppFunctions(model));
    }

    @Override
    public void ignoreRegulations() {
        reg = 0;
    }
}
