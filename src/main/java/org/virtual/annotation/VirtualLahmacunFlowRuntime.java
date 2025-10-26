package org.virtual.annotation;

import org.virtual.core.Assert;
import org.virtual.core.FlowExecutorWithChef;
import org.virtual.core.VirtualLahmacunChef;
import org.virtual.core.VirtualThreadOven;
import org.virtual.dag.FlowGraph;
import org.virtual.model.TaskLahmacunResult;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class VirtualLahmacunFlowRuntime {

    private final VirtualThreadOven oven;
    private final VirtualLahmacunChef chef;

    public VirtualLahmacunFlowRuntime() {
        this.oven = new VirtualThreadOven();
        this.chef = new VirtualLahmacunChef(oven);
    }

    public void startAllFlows(String basePackage) throws Exception {
        Assert.nonNull(basePackage, "Base package must not be null");
        List<Class<?>> classes = scanPackage(basePackage);
        Map<String, TaskLahmacunResult<?>> allResults = new LinkedHashMap<>();
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotation() || clazz.isInterface()){
                continue;
            }
            Object instance = clazz.getDeclaredConstructor().newInstance();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(VirtualFlow.class)) {
                    method.setAccessible(true);
                    Object result = method.invoke(instance);
                    if (!(result instanceof FlowGraph graph)) {
                        throw new IllegalStateException("@" + VirtualFlow.class.getSimpleName()
                                + " method must return FlowGraph: " + method.getName());
                    }
                    FlowExecutorWithChef executor = new FlowExecutorWithChef(graph, chef);
                    allResults.putAll(executor.execute());
                }
            }
        }
        System.out.println("\n--- VirtualFlow Runtime Results ---");
        allResults.forEach((id, r) -> System.out.println(id + " -> " + r.status() + " | " + r.result() + " | " + r.durationMillis()));
        oven.close();
        AtomicLong sum = new AtomicLong();
        allResults.forEach((k,v) ->  sum.addAndGet(v.durationMillis()));
        System.out.println("Completed Flows -> "  + sum+" ms");
    }

    private List<Class<?>> scanPackage(String basePackage) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = basePackage.replace('.', '/');
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File dir = new File(resource.getFile());
            if (dir.exists()) {
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    if (file.getName().endsWith(".class")) {
                        String className = basePackage + '.' + file.getName().replace(".class", "");
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
        return classes;
    }
}