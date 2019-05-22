package org.codehaus.plexus.component.factory;

import java.util.List;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.factory.java.JavaComponentFactory;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.codehaus.plexus.util.StringUtils;

public class DefaultComponentFactoryManager implements ComponentFactoryManager, Contextualizable {
   private String defaultComponentFactoryId = "java";
   private ComponentFactory defaultComponentFactory = new JavaComponentFactory();
   private PlexusContainer container;
   /** @deprecated */
   private List componentFactories;

   public ComponentFactory findComponentFactory(String id) throws UndefinedComponentFactoryException {
      if (!StringUtils.isEmpty(id) && !this.defaultComponentFactoryId.equals(id)) {
         try {
            return (ComponentFactory)this.container.lookup(ComponentFactory.ROLE, id);
         } catch (ComponentLookupException var3) {
            throw new UndefinedComponentFactoryException("Specified component factory cannot be found: " + id, var3);
         }
      } else {
         return this.defaultComponentFactory;
      }
   }

   public ComponentFactory getDefaultComponentFactory() throws UndefinedComponentFactoryException {
      return this.defaultComponentFactory;
   }

   public void contextualize(Context context) throws ContextException {
      this.container = (PlexusContainer)context.get("plexus");
   }
}
