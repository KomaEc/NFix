package org.codehaus.plexus.component.configurator.converters.basic;

public interface Converter {
   boolean canConvert(Class var1);

   Object fromString(String var1);

   String toString(Object var1);
}
