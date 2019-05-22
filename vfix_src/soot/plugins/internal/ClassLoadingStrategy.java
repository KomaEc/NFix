package soot.plugins.internal;

public interface ClassLoadingStrategy {
   Object create(String var1) throws ClassNotFoundException, InstantiationException;
}
