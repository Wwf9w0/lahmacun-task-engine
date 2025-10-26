package org.virtual;

import org.virtual.core.VirtualLahmacunFlowRuntime;

public class Main {
    public static void main(String[] args) throws Exception {
        VirtualLahmacunFlowRuntime runtime = new VirtualLahmacunFlowRuntime();
        runtime.startAllFlows("org.virtual.annotation");
    }
}
