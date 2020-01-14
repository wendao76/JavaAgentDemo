package com.github.wendao76.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * @author wendao76
 */
public class CustomAgent {
    private static Instrumentation inst = null;
    public static void premain(String agentArgs, Instrumentation _inst) {
        System.out.println("CustomAgent.premain is called");
        inst = _inst;
        ClassFileTransformer trans = new CustomFormer();
        System.out.println("Add a CustomFormer instance to the JVM");
        inst.addTransformer(trans);
    }

    public static void premain(String agentArgs) {
        System.out.println("test");
    }

    public static void agentmain(String agentArgs, Instrumentation _inst) {
        System.out.println("agentmain1");
    }

    public static void agentmain(String agentArgs) {
        System.out.println("agentmain2");
    }
}
