package org.apache.tools.ant;

public interface DynamicElement {
   Object createDynamicElement(String var1) throws BuildException;
}
