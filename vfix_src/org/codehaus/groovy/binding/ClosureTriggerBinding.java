package org.codehaus.groovy.binding;

import groovy.lang.Closure;
import groovy.lang.Reference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ClosureTriggerBinding implements TriggerBinding, SourceBinding {
   Map<String, TriggerBinding> syntheticBindings;
   Closure closure;

   public ClosureTriggerBinding(Map<String, TriggerBinding> syntheticBindings) {
      this.syntheticBindings = syntheticBindings;
   }

   public Closure getClosure() {
      return this.closure;
   }

   public void setClosure(Closure closure) {
      this.closure = closure;
   }

   private BindPath createBindPath(String propertyName, BindPathSnooper snooper) {
      BindPath bp = new BindPath();
      bp.propertyName = propertyName;
      bp.updateLocalSyntheticProperties(this.syntheticBindings);
      List<BindPath> childPaths = new ArrayList();
      Iterator i$ = snooper.fields.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, BindPathSnooper> entry = (Entry)i$.next();
         childPaths.add(this.createBindPath((String)entry.getKey(), (BindPathSnooper)entry.getValue()));
      }

      bp.children = (BindPath[])childPaths.toArray(new BindPath[childPaths.size()]);
      return bp;
   }

   public FullBinding createBinding(SourceBinding source, TargetBinding target) {
      if (source != this) {
         throw new RuntimeException("Source binding must the Trigger Binding as well");
      } else {
         final BindPathSnooper delegate = new BindPathSnooper();

         try {
            final Class closureClass = this.closure.getClass();
            Closure closureLocalCopy = (Closure)AccessController.doPrivileged(new PrivilegedAction<Closure>() {
               public Closure run() {
                  Constructor constructor = closureClass.getConstructors()[0];
                  int paramCount = constructor.getParameterTypes().length;
                  Object[] args = new Object[paramCount];
                  args[0] = delegate;

                  for(int i = 1; i < paramCount; ++i) {
                     args[i] = new Reference(new BindPathSnooper());
                  }

                  try {
                     boolean acc = constructor.isAccessible();
                     constructor.setAccessible(true);
                     Closure localCopy = (Closure)constructor.newInstance(args);
                     if (!acc) {
                        constructor.setAccessible(false);
                     }

                     localCopy.setResolveStrategy(3);
                     Field[] arr$ = closureClass.getDeclaredFields();
                     int len$ = arr$.length;

                     for(int i$ = 0; i$ < len$; ++i$) {
                        Field f = arr$[i$];
                        acc = f.isAccessible();
                        f.setAccessible(true);
                        if (f.getType() == Reference.class) {
                           delegate.fields.put(f.getName(), (BindPathSnooper)((Reference)f.get(localCopy)).get());
                        }

                        if (!acc) {
                           f.setAccessible(false);
                        }
                     }

                     return localCopy;
                  } catch (Exception var10) {
                     throw new RuntimeException("Error snooping closure", var10);
                  }
               }
            });

            try {
               closureLocalCopy.call();
            } catch (DeadEndException var8) {
               throw var8;
            } catch (Exception var9) {
            }
         } catch (Exception var10) {
            var10.printStackTrace(System.out);
            throw new RuntimeException("A closure expression binding could not be created because of " + var10.getClass().getName() + ":\n\t" + var10.getMessage());
         }

         List<BindPath> rootPaths = new ArrayList();
         Iterator i$ = delegate.fields.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, BindPathSnooper> entry = (Entry)i$.next();
            BindPath bp = this.createBindPath((String)entry.getKey(), (BindPathSnooper)entry.getValue());
            bp.currentObject = this.closure;
            rootPaths.add(bp);
         }

         PropertyPathFullBinding fb = new PropertyPathFullBinding();
         fb.setSourceBinding(new ClosureSourceBinding(this.closure));
         fb.setTargetBinding(target);
         fb.bindPaths = (BindPath[])rootPaths.toArray(new BindPath[rootPaths.size()]);
         return fb;
      }
   }

   public Object getSourceValue() {
      return this.closure.call();
   }
}
