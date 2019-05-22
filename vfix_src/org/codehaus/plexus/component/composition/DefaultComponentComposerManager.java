package org.codehaus.plexus.component.composition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;

public class DefaultComponentComposerManager implements ComponentComposerManager {
   private Map composerMap = new HashMap();
   private List componentComposers;
   private String defaultComponentComposerId = "field";

   public void assembleComponent(Object component, ComponentDescriptor componentDescriptor, PlexusContainer container) throws UndefinedComponentComposerException, CompositionException {
      if (componentDescriptor.getRequirements().size() != 0) {
         String componentComposerId = componentDescriptor.getComponentComposer();
         if (componentComposerId == null || componentComposerId.trim().length() == 0) {
            componentComposerId = this.defaultComponentComposerId;
         }

         ComponentComposer componentComposer = this.getComponentComposer(componentComposerId);
         componentComposer.assembleComponent(component, componentDescriptor, container);
      }
   }

   protected ComponentComposer getComponentComposer(String id) throws UndefinedComponentComposerException {
      ComponentComposer retValue = null;
      if (this.composerMap.containsKey(id)) {
         retValue = (ComponentComposer)this.composerMap.get(id);
      } else {
         retValue = this.findComponentComposer(id);
      }

      if (retValue == null) {
         throw new UndefinedComponentComposerException("Specified component composer cannot be found: " + id);
      } else {
         return retValue;
      }
   }

   private ComponentComposer findComponentComposer(String id) {
      ComponentComposer retValue = null;
      Iterator iterator = this.componentComposers.iterator();

      while(iterator.hasNext()) {
         ComponentComposer componentComposer = (ComponentComposer)iterator.next();
         if (componentComposer.getId().equals(id)) {
            retValue = componentComposer;
            break;
         }
      }

      return retValue;
   }
}
