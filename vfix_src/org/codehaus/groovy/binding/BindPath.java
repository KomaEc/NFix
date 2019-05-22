package org.codehaus.groovy.binding;

import groovy.lang.MetaClass;
import groovy.lang.MissingPropertyException;
import groovy.lang.Reference;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.codehaus.groovy.runtime.InvokerHelper;

public class BindPath {
   Map<String, TriggerBinding> localSynthetics;
   Object currentObject;
   String propertyName;
   PropertyChangeListener localListener;
   PropertyChangeListener globalListener;
   BindingUpdatable syntheticFullBinding;
   BindPath[] children;
   static final Class[] NAME_PARAMS = new Class[]{String.class, PropertyChangeListener.class};
   static final Class[] GLOBAL_PARAMS = new Class[]{PropertyChangeListener.class};

   public synchronized void updatePath(PropertyChangeListener listener, Object newObject, Set updateSet) {
      if (this.currentObject != newObject) {
         this.removeListeners();
      }

      if (this.children != null && this.children.length > 0) {
         try {
            Object newValue = null;
            if (newObject != null) {
               updateSet.add(newObject);
               newValue = this.extractNewValue(newObject);
            }

            BindPath[] arr$ = this.children;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               BindPath child = arr$[i$];
               child.updatePath(listener, newValue, updateSet);
            }
         } catch (Exception var9) {
         }
      }

      if (this.currentObject != newObject) {
         this.addListeners(listener, newObject, updateSet);
      }

   }

   public void addAllListeners(PropertyChangeListener listener, Object newObject, Set updateSet) {
      this.addListeners(listener, newObject, updateSet);
      if (this.children != null && this.children.length > 0) {
         try {
            Object newValue = null;
            if (newObject != null) {
               updateSet.add(newObject);
               newValue = this.extractNewValue(newObject);
            }

            BindPath[] arr$ = this.children;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               BindPath child = arr$[i$];
               child.addAllListeners(listener, newValue, updateSet);
            }
         } catch (Exception var9) {
            var9.printStackTrace(System.out);
         }
      }

   }

   private Object extractNewValue(Object newObject) {
      Object newValue;
      try {
         newValue = InvokerHelper.getProperty(newObject, this.propertyName);
      } catch (MissingPropertyException var6) {
         try {
            newValue = InvokerHelper.getAttribute(newObject, this.propertyName);
            if (newValue instanceof Reference) {
               newValue = ((Reference)newValue).get();
            }
         } catch (Exception var5) {
            newValue = null;
         }
      }

      return newValue;
   }

   public void addListeners(PropertyChangeListener listener, Object newObject, Set updateSet) {
      this.removeListeners();
      if (newObject != null) {
         TriggerBinding syntheticTrigger = this.getSyntheticTriggerBinding(newObject);
         MetaClass mc = InvokerHelper.getMetaClass(newObject);
         if (syntheticTrigger != null) {
            PropertyBinding psb = new PropertyBinding(newObject, this.propertyName);
            PropertyChangeProxyTargetBinding proxytb = new PropertyChangeProxyTargetBinding(newObject, this.propertyName, listener);
            this.syntheticFullBinding = syntheticTrigger.createBinding(psb, proxytb);
            this.syntheticFullBinding.bind();
            updateSet.add(newObject);
         } else if (!mc.respondsTo(newObject, "addPropertyChangeListener", NAME_PARAMS).isEmpty()) {
            InvokerHelper.invokeMethod(newObject, "addPropertyChangeListener", new Object[]{this.propertyName, listener});
            this.localListener = listener;
            updateSet.add(newObject);
         } else if (!mc.respondsTo(newObject, "addPropertyChangeListener", GLOBAL_PARAMS).isEmpty()) {
            InvokerHelper.invokeMethod(newObject, "addPropertyChangeListener", listener);
            this.globalListener = listener;
            updateSet.add(newObject);
         }
      }

      this.currentObject = newObject;
   }

   public void removeListeners() {
      if (this.globalListener != null) {
         try {
            InvokerHelper.invokeMethod(this.currentObject, "removePropertyChangeListener", this.globalListener);
         } catch (Exception var3) {
         }

         this.globalListener = null;
      }

      if (this.localListener != null) {
         try {
            InvokerHelper.invokeMethod(this.currentObject, "removePropertyChangeListener", new Object[]{this.propertyName, this.localListener});
         } catch (Exception var2) {
         }

         this.localListener = null;
      }

      if (this.syntheticFullBinding != null) {
         this.syntheticFullBinding.unbind();
      }

   }

   public synchronized void updateLocalSyntheticProperties(Map<String, TriggerBinding> synthetics) {
      this.localSynthetics = null;
      String endName = "#" + this.propertyName;
      Iterator i$ = synthetics.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, TriggerBinding> synteticEntry = (Entry)i$.next();
         if (((String)synteticEntry.getKey()).endsWith(endName)) {
            if (this.localSynthetics == null) {
               this.localSynthetics = new TreeMap();
            }

            this.localSynthetics.put(synteticEntry.getKey(), synteticEntry.getValue());
         }
      }

   }

   public TriggerBinding getSyntheticTriggerBinding(Object newObject) {
      if (this.localSynthetics == null) {
         return null;
      } else {
         for(Class currentClass = newObject.getClass(); currentClass != null; currentClass = currentClass.getSuperclass()) {
            TriggerBinding trigger = (TriggerBinding)this.localSynthetics.get(currentClass.getName() + "#" + this.propertyName);
            if (trigger != null) {
               return trigger;
            }
         }

         return null;
      }
   }
}
