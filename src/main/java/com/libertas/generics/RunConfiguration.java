package com.libertas.generics;

public class RunConfiguration {
    private static RunConfiguration instance = new RunConfiguration();

    private RunConfiguration() {}

    public static RunConfiguration getInstance() {
        return instance;
    }

    public RunMode mode = RunMode.TERMINAL;
}
