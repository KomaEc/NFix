package org.codehaus.groovy.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

public class PropertyPathFullBinding extends AbstractFullBinding implements PropertyChangeListener {
   Set updateObjects = new HashSet();
   BindPath[] bindPaths;
   boolean bound;

   public void bind() {
      this.updateObjects.clear();
      BindPath[] arr$ = this.bindPaths;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         BindPath bp = arr$[i$];
         bp.addAllListeners(this, bp.currentObject, this.updateObjects);
      }

      this.bound = true;
   }

   public void unbind() {
      this.updateObjects.clear();
      BindPath[] arr$ = this.bindPaths;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         BindPath path = arr$[i$];
         path.removeListeners();
      }

      this.bound = false;
   }

   public void rebind() {
      if (this.bound) {
         this.bind();
      }

   }

   public void propertyChange(PropertyChangeEvent evt) {
      if (this.updateObjects.contains(evt.getSource())) {
         BindPath[] arr$ = this.bindPaths;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            BindPath bp = arr$[i$];
            Set newUpdates = new HashSet();
            bp.updatePath(this, bp.currentObject, newUpdates);
            this.updateObjects = newUpdates;
         }
      }

      this.update();
   }
}
