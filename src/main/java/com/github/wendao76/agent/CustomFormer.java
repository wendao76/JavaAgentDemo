package com.github.wendao76.agent;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class CustomFormer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] transformed = null;
        System.out.println("Transforming " + className);
        ClassPool classPool = ClassPool.getDefault();
        CtClass cl = null;

        try {
            cl = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            if(cl.isInterface() == false) {
                CtBehavior[] methods = cl.getDeclaredBehaviors();
                for (int i=0; i<methods.length; i++) {
                    doMethod(methods[i]);
                }
                transformed = cl.toBytecode();
            }
        } catch (IOException e) {
            System.out.println("Could not instrument  " + className + ", exception:" + e.getMessage());
        } catch (CannotCompileException e) {
           if(cl != null) {
               cl.detach();
           }
        }
        return transformed;
    }

    private void doMethod(CtBehavior method) throws CannotCompileException {
        method.insertBefore("long stime = System.nanoTime();");
        method.insertAfter("System.out.Printlon(\" leave" + method.getName() + "and time: \" +(System.nanoTime()-stime))");
    }
}
