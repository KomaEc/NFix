package org.codehaus.plexus.component.repository;

import java.util.List;
import java.util.Map;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.repository.exception.ComponentImplementationNotFoundException;
import org.codehaus.plexus.component.repository.exception.ComponentRepositoryException;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public interface ComponentRepository {
   void configure(PlexusConfiguration var1);

   void initialize() throws ComponentRepositoryException;

   boolean hasComponent(String var1);

   boolean hasComponent(String var1, String var2);

   void addComponentDescriptor(ComponentDescriptor var1) throws ComponentRepositoryException;

   void addComponentDescriptor(PlexusConfiguration var1) throws ComponentRepositoryException;

   ComponentDescriptor getComponentDescriptor(String var1);

   Map getComponentDescriptorMap(String var1);

   List getComponentDependencies(ComponentDescriptor var1);

   void validateComponentDescriptor(ComponentDescriptor var1) throws ComponentImplementationNotFoundException;

   void setClassRealm(ClassRealm var1);
}
