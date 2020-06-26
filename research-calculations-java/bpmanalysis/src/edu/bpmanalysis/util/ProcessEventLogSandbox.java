package edu.bpmanalysis.util;

import java.util.*;

public class ProcessEventLogSandbox {
    private static Random randomGenerator = new Random();

    public static List<String> parallel(List<String>... sequences) {
        List<List<String>> list = Arrays.asList(sequences);
        Collections.shuffle(list);
        List<String> output = new ArrayList<>();

        for (List<String> tasks : sequences) {
            output.addAll(tasks);
        }

        return output;
    }

    public static List<String> exclusive(List<String>... sequences) {
        return sequences[randomGenerator.nextInt(sequences.length)];
    }

    public static List<String> sequence(List<String>... sequences) {
        List<String> output = new ArrayList<>();

        for (List<String> tasks : sequences) {
            output.addAll(tasks);
        }

        return output;
    }

    public static List<String> task(String label) {
        return Arrays.asList(label);
    }

    public static void run(int instance, List<String> process) {
        for (String task : process) {
            System.out.println(String.format("%d\t%s\t%s", instance, task, System.nanoTime()));
        }
    }

    public static void main(String[] args) {

        for (int instance = 1; instance <= 100; instance++)
            run(instance,
                    sequence(
                            exclusive(
                                    sequence(
                                            parallel(
                                                    task("Request Bid Shipping Company 1"),
                                                    task("Request Bid Shipping Company 2"),
                                                    task("Request Bid Shipping Company 3")
                                            ),
                                            task("Choose Company")
                                    ),
                                    task("Normal Post")
                            ),
                            task("Package Label"),
                            task("Determine Insurance"),
                            exclusive(
                                    task("Insurance Taken"),
                                    task("Insurance Not Taken")
                            ),
                            task("Prepare for pickup")
                    )
            );
    }
}
