package org.jboss.util.property;

public interface BoundPropertyListener extends PropertyListener {
   String getPropertyName();

   void propertyBound(PropertyMap var1);

   void propertyUnbound(PropertyMap var1);
}
