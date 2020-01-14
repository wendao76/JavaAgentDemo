package com.github.wendao76.agent;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author wendao76
 */
public class CustomFormer implements ClassFileTransformer {
    public final String injectedClassName = "com.github.wendao76";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace("/", ".");
        byte[] transformed = null;
        ClassPool classPool = ClassPool.getDefault();
        CtClass cl = null;
        if(className.startsWith(injectedClassName)) {
            System.out.println("Transforming " + className);
            try {
                cl = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                if(!cl.isInterface()) {
                    CtBehavior[] methods = cl.getDeclaredBehaviors();
                    for (int i=0; i<methods.length; i++) {
                        doMethod(methods[i]);
                    }
                    transformed = cl.toBytecode();
                }
                return transformed;
            } catch (Exception e) {
                e.printStackTrace();
//                System.out.println("Could not instrument  " + className + ", exception:" + e.getMessage());
            } finally {
                if(cl != null) {
                    cl.detach();
                }
            }
        }
        return null;
    }

    private void doMethod(CtBehavior method) throws CannotCompileException {
        method.insertBefore("System.out.println(\"before:\"+\"" +method.getName()+"\");");
        method.insertAfter("System.out.println(\"after:\"+\""+method.getName()+"\");");
    }
}
