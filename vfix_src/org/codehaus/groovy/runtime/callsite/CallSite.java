package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyObject;
import java.util.concurrent.atomic.AtomicInteger;

public interface CallSite {
   CallSiteArray getArray();

   int getIndex();

   String getName();

   AtomicInteger getUsage();

   Object getProperty(Object var1) throws Throwable;

   Object callGetPropertySafe(Object var1) throws Throwable;

   Object callGetProperty(Object var1) throws Throwable;

   Object callGroovyObjectGetProperty(Object var1) throws Throwable;

   Object callGroovyObjectGetPropertySafe(Object var1) throws Throwable;

   Object call(Object var1, Object[] var2) throws Throwable;

   Object call(Object var1) throws Throwable;

   Object call(Object var1, Object var2) throws Throwable;

   Object call(Object var1, Object var2, Object var3) throws Throwable;

   Object call(Object var1, Object var2, Object var3, Object var4) throws Throwable;

   Object call(Object var1, Object var2, Object var3, Object var4, Object var5) throws Throwable;

   Object callSafe(Object var1, Object[] var2) throws Throwable;

   Object callSafe(Object var1) throws Throwable;

   Object callSafe(Object var1, Object var2) throws Throwable;

   Object callSafe(Object var1, Object var2, Object var3) throws Throwable;

   Object callSafe(Object var1, Object var2, Object var3, Object var4) throws Throwable;

   Object callSafe(Object var1, Object var2, Object var3, Object var4, Object var5) throws Throwable;

   Object callCurrent(GroovyObject var1, Object[] var2) throws Throwable;

   Object callCurrent(GroovyObject var1) throws Throwable;

   Object callCurrent(GroovyObject var1, Object var2) throws Throwable;

   Object callCurrent(GroovyObject var1, Object var2, Object var3) throws Throwable;

   Object callCurrent(GroovyObject var1, Object var2, Object var3, Object var4) throws Throwable;

   Object callCurrent(GroovyObject var1, Object var2, Object var3, Object var4, Object var5) throws Throwable;

   Object callStatic(Class var1, Object[] var2) throws Throwable;

   Object callStatic(Class var1) throws Throwable;

   Object callStatic(Class var1, Object var2) throws Throwable;

   Object callStatic(Class var1, Object var2, Object var3) throws Throwable;

   Object callStatic(Class var1, Object var2, Object var3, Object var4) throws Throwable;

   Object callStatic(Class var1, Object var2, Object var3, Object var4, Object var5) throws Throwable;

   Object callConstructor(Object var1, Object[] var2) throws Throwable;

   Object callConstructor(Object var1) throws Throwable;

   Object callConstructor(Object var1, Object var2) throws Throwable;

   Object callConstructor(Object var1, Object var2, Object var3) throws Throwable;

   Object callConstructor(Object var1, Object var2, Object var3, Object var4) throws Throwable;

   Object callConstructor(Object var1, Object var2, Object var3, Object var4, Object var5) throws Throwable;
}
