package org.codehaus.plexus.component.composition;

import java.util.Iterator;
import java.util.List;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentRequirement;
import org.codehaus.plexus.util.dag.CycleDetectedException;
import org.codehaus.plexus.util.dag.DAG;

public class DefaultCompositionResolver implements CompositionResolver {
   private DAG dag = new DAG();

   public void addComponentDescriptor(ComponentDescriptor componentDescriptor) throws CompositionException {
      String componentKey = componentDescriptor.getComponentKey();
      List requirements = componentDescriptor.getRequirements();
      Iterator iterator = requirements.iterator();

      while(iterator.hasNext()) {
         ComponentRequirement requirement = (ComponentRequirement)iterator.next();

         try {
            this.dag.addEdge(componentKey, requirement.getRole());
         } catch (CycleDetectedException var7) {
            throw new CompositionException("Cyclic requirement detected", var7);
         }
      }

   }

   public List getRequirements(String componentKey) {
      return this.dag.getChildLabels(componentKey);
   }

   public List findRequirements(String componentKey) {
      return this.dag.getParentLabels(componentKey);
   }
}
