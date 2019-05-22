package org.codehaus.plexus.personality.plexus.lifecycle.phase;

import java.util.List;
import java.util.Map;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

public interface ServiceLocator {
   Object lookup(String var1) throws ComponentLookupException;

   Object lookup(String var1, String var2) throws ComponentLookupException;

   Map lookupMap(String var1) throws ComponentLookupException;

   List lookupList(String var1) throws ComponentLookupException;

   void release(Object var1) throws ComponentLifecycleException;

   void releaseAll(Map var1) throws ComponentLifecycleException;

   void releaseAll(List var1) throws ComponentLifecycleException;

   boolean hasComponent(String var1);

   boolean hasComponent(String var1, String var2);
}
