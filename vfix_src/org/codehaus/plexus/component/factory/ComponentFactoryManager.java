package org.codehaus.plexus.component.factory;

public interface ComponentFactoryManager {
   ComponentFactory findComponentFactory(String var1) throws UndefinedComponentFactoryException;

   ComponentFactory getDefaultComponentFactory() throws UndefinedComponentFactoryException;
}
