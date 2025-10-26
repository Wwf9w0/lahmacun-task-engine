package org.virtual;


import org.virtual.annotation.VirtualLahmacunFlowRuntime;

public class Main {
    public static void main(String[] args) throws Exception {
        VirtualLahmacunFlowRuntime runtime = new VirtualLahmacunFlowRuntime();
        runtime.startAllFlows("org.virtual.annotation");
    }
}
