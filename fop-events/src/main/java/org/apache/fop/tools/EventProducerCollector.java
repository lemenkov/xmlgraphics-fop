/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package org.apache.fop.tools;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.fop.events.EventProducer;
import org.apache.fop.events.model.EventMethodModel;
import org.apache.fop.events.model.EventModel;
import org.apache.fop.events.model.EventProducerModel;
import org.apache.fop.events.model.EventSeverity;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;

/**
 * Finds EventProducer interfaces and builds the event model for them.
 */
class EventProducerCollector {

    private static final String CLASSNAME_EVENT_PRODUCER = EventProducer.class.getName();
    private static final Map<String, Class<?>> PRIMITIVE_MAP;

    static {
        Map<String, Class<?>> m = new java.util.HashMap<String, Class<?>>();
        m.put("boolean", Boolean.class);
        m.put("byte", Byte.class);
        m.put("char", Character.class);
        m.put("short", Short.class);
        m.put("int", Integer.class);
        m.put("long", Long.class);
        m.put("float", Float.class);
        m.put("double", Double.class);
        PRIMITIVE_MAP = Collections.unmodifiableMap(m);
    }

    private List<EventModel> models = new java.util.ArrayList<EventModel>();

    /**
     * Creates a new EventProducerCollector.
     */
    EventProducerCollector() {
    }

    /**
     * Scans a file and processes it if it extends the {@link EventProducer} interface.
     * @param src the source file (a Java source file)
     * @return true if the file contained an EventProducer interface
     * @throws IOException if an I/O error occurs
     * @throws EventConventionException if the EventProducer conventions are violated
     * @throws ClassNotFoundException if a required class cannot be found
     */
    public boolean scanFile(File src)
            throws IOException, EventConventionException, ClassNotFoundException {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSource(src);
        Collection<JavaClass> classes = builder.getClasses();
        boolean eventProducerFound = false;
        for (JavaClass clazz : classes) {
            if (clazz.isInterface() && implementsInterface(clazz, CLASSNAME_EVENT_PRODUCER)) {
                processEventProducerInterface(clazz);
                eventProducerFound = true;
            }
        }
        return eventProducerFound;
    }

    private boolean implementsInterface(JavaClass clazz, String intf) {
        List<JavaClass> classes = clazz.getInterfaces();
        for (JavaClass cl : classes) {
            if (cl.getFullyQualifiedName().equals(intf)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Processes an EventProducer interface and creates an EventProducerModel from it.
     * @param clazz the EventProducer interface
     * @throws EventConventionException if the event producer conventions are violated
     * @throws ClassNotFoundException if a required class cannot be found
     */
    protected void processEventProducerInterface(JavaClass clazz)
                throws EventConventionException, ClassNotFoundException {
        EventProducerModel prodMeta = new EventProducerModel(clazz.getFullyQualifiedName());
        List<JavaMethod> methods = clazz.getMethods(true);
        for (JavaMethod method : methods) {
            EventMethodModel methodMeta = createMethodModel(method);
            prodMeta.addMethod(methodMeta);
        }
        EventModel model = new EventModel();
        model.addProducer(prodMeta);
        models.add(model);
    }

    private EventMethodModel createMethodModel(JavaMethod method)
            throws EventConventionException, ClassNotFoundException {
        JavaClass clazz = method.getDeclaringClass();
        //Check EventProducer conventions
        if (!method.getReturns().isVoid()) {
            throw new EventConventionException("All methods of interface "
                    + clazz.getFullyQualifiedName() + " must have return type 'void'!");
        }
        String methodSig = clazz.getFullyQualifiedName() + "." + method.getCallSignature();
        List<JavaParameter> params = method.getParameters();
        if (params.size() < 1) {
            throw new EventConventionException("The method " + methodSig
                    + " must have at least one parameter: 'Object source'!");
        }
        JavaClass firstType = params.get(0).getJavaClass();
        if (firstType.isPrimitive() || !"source".equals(params.get(0).getName())) {
            throw new EventConventionException("The first parameter of the method " + methodSig
                    + " must be: 'Object source'!");
        }

        //build method model
        DocletTag tag = method.getTagByName("event.severity");
        EventSeverity severity;
        if (tag != null) {
            severity = EventSeverity.valueOf(tag.getValue());
        } else {
            severity = EventSeverity.INFO;
        }
        EventMethodModel methodMeta = new EventMethodModel(
                method.getName(), severity);
        if (params.size() > 1) {
            for (int j = 1, cj = params.size(); j < cj; j++) {
                JavaParameter p = params.get(j);
                Class<?> type;
                JavaClass pClass = p.getJavaClass();
                if (pClass.isPrimitive()) {
                    type = PRIMITIVE_MAP.get(pClass.getName());
                    if (type == null) {
                        throw new UnsupportedOperationException(
                                "Primitive datatype not supported: " + pClass.getName());
                    }
                } else {
                    String className = pClass.getFullyQualifiedName();
                    type = Class.forName(className);
                }
                methodMeta.addParameter(type, p.getName());
            }
        }
        List<JavaClass> exceptions = method.getExceptions();
        if (exceptions != null && exceptions.size() > 0) {
            //We only use the first declared exception because that is always thrown
            JavaClass cl = exceptions.get(0);
            methodMeta.setExceptionClass(cl.getFullyQualifiedName());
            methodMeta.setSeverity(EventSeverity.FATAL); //In case it's not set in the comments
        }
        return methodMeta;
    }

    /**
     * Returns the event model that has been accumulated.
     * @return the event model.
     */
    public List<EventModel> getModels() {
        return this.models;
    }

}
