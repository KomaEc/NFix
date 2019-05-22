package org.jboss.util.property;

import java.util.EventListener;

public interface PropertyListener extends EventListener {
   void propertyAdded(PropertyEvent var1);

   void propertyRemoved(PropertyEvent var1);

   void propertyChanged(PropertyEvent var1);
}
