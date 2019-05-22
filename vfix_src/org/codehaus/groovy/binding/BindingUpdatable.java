package org.codehaus.groovy.binding;

public interface BindingUpdatable {
   void bind();

   void unbind();

   void rebind();

   void update();

   void reverseUpdate();
}
