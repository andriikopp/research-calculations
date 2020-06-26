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

    public static void run(String name, List<String> process) {
        System.out.println(name);

        for (String task : process) {
            System.out.println(task);
        }
    }

    public static void main(String[] args) {
        run("Dispatch of Goods",
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
