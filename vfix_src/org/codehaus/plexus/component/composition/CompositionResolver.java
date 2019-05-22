package org.codehaus.plexus.component.composition;

import java.util.List;
import org.codehaus.plexus.component.repository.ComponentDescriptor;

public interface CompositionResolver {
   void addComponentDescriptor(ComponentDescriptor var1) throws CompositionException;

   List getRequirements(String var1);

   List findRequirements(String var1);
}
