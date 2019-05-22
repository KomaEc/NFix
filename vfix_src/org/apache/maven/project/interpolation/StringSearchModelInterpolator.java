package org.apache.maven.project.interpolation;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import org.apache.maven.model.Model;
import org.apache.maven.project.ProjectBuilderConfiguration;
import org.apache.maven.project.path.PathTranslator;
import org.codehaus.plexus.interpolation.InterpolationPostProcessor;
import org.codehaus.plexus.interpolation.Interpolator;
import org.codehaus.plexus.interpolation.StringSearchInterpolator;
import org.codehaus.plexus.interpolation.ValueSource;
import org.codehaus.plexus.logging.Logger;

public class StringSearchModelInterpolator extends AbstractStringBasedModelInterpolator {
   private static final Map<Class<?>, Field[]> fieldsByClass = new WeakHashMap();
   private static final Map<Class<?>, Boolean> fieldIsPrimitiveByClass = new WeakHashMap();

   public StringSearchModelInterpolator() {
   }

   public StringSearchModelInterpolator(PathTranslator pathTranslator) {
      super(pathTranslator);
   }

   public Model interpolate(Model model, File projectDir, ProjectBuilderConfiguration config, boolean debugEnabled) throws ModelInterpolationException {
      this.interpolateObject(model, model, projectDir, config, debugEnabled);
      return model;
   }

   protected void interpolateObject(Object obj, Model model, File projectDir, ProjectBuilderConfiguration config, boolean debugEnabled) throws ModelInterpolationException {
      try {
         List<ValueSource> valueSources = this.createValueSources(model, projectDir, config);
         List<InterpolationPostProcessor> postProcessors = this.createPostProcessors(model, projectDir, config);
         StringSearchModelInterpolator.InterpolateObjectAction action = new StringSearchModelInterpolator.InterpolateObjectAction(obj, valueSources, postProcessors, debugEnabled, this, this.getLogger());
         ModelInterpolationException error = (ModelInterpolationException)AccessController.doPrivileged(action);
         if (error != null) {
            throw error;
         }
      } finally {
         this.getInterpolator().clearAnswers();
      }

   }

   protected Interpolator createInterpolator() {
      StringSearchInterpolator interpolator = new StringSearchInterpolator();
      interpolator.setCacheAnswers(true);
      return interpolator;
   }

   private static final class InterpolateObjectAction implements PrivilegedAction<ModelInterpolationException> {
      private final boolean debugEnabled;
      private final LinkedList<Object> interpolationTargets;
      private final StringSearchModelInterpolator modelInterpolator;
      private final Logger logger;
      private final List<ValueSource> valueSources;
      private final List<InterpolationPostProcessor> postProcessors;

      public InterpolateObjectAction(Object target, List<ValueSource> valueSources, List<InterpolationPostProcessor> postProcessors, boolean debugEnabled, StringSearchModelInterpolator modelInterpolator, Logger logger) {
         this.valueSources = valueSources;
         this.postProcessors = postProcessors;
         this.debugEnabled = debugEnabled;
         this.interpolationTargets = new LinkedList();
         this.interpolationTargets.add(target);
         this.modelInterpolator = modelInterpolator;
         this.logger = logger;
      }

      public ModelInterpolationException run() {
         while(!this.interpolationTargets.isEmpty()) {
            Object obj = this.interpolationTargets.removeFirst();

            try {
               this.traverseObjectWithParents(obj.getClass(), obj);
            } catch (ModelInterpolationException var3) {
               return var3;
            }
         }

         return null;
      }

      private void traverseObjectWithParents(Class<?> cls, Object target) throws ModelInterpolationException {
         if (cls != null) {
            if (cls.isArray()) {
               this.evaluateArray(target);
            } else if (this.isQualifiedForInterpolation(cls)) {
               Field[] fields = (Field[])((Field[])StringSearchModelInterpolator.fieldsByClass.get(cls));
               if (fields == null) {
                  fields = cls.getDeclaredFields();
                  StringSearchModelInterpolator.fieldsByClass.put(cls, fields);
               }

               label302:
               for(int i = 0; i < fields.length; ++i) {
                  Class<?> type = fields[i].getType();
                  if (this.isQualifiedForInterpolation(fields[i], type)) {
                     boolean isAccessible = fields[i].isAccessible();
                     fields[i].setAccessible(true);

                     try {
                        if (String.class == type) {
                           String value = (String)fields[i].get(target);
                           if (value != null) {
                              String interpolated = this.modelInterpolator.interpolateInternal(value, this.valueSources, this.postProcessors, this.debugEnabled);
                              if (!interpolated.equals(value)) {
                                 fields[i].set(target, interpolated);
                              }
                           }
                        } else {
                           Object value;
                           String interpolated;
                           if (Collection.class.isAssignableFrom(type)) {
                              Collection<Object> c = (Collection)fields[i].get(target);
                              if (c != null && !c.isEmpty()) {
                                 ArrayList originalValues = new ArrayList(c);

                                 try {
                                    c.clear();
                                 } catch (UnsupportedOperationException var19) {
                                    if (this.debugEnabled && this.logger != null) {
                                       this.logger.debug("Skipping interpolation of field: " + fields[i] + " in: " + cls.getName() + "; it is an unmodifiable collection.");
                                    }
                                    continue;
                                 }

                                 Iterator i$ = originalValues.iterator();

                                 while(i$.hasNext()) {
                                    value = i$.next();
                                    if (value != null) {
                                       if (String.class == value.getClass()) {
                                          interpolated = this.modelInterpolator.interpolateInternal((String)value, this.valueSources, this.postProcessors, this.debugEnabled);
                                          if (!interpolated.equals(value)) {
                                             c.add(interpolated);
                                          } else {
                                             c.add(value);
                                          }
                                       } else {
                                          c.add(value);
                                          if (value.getClass().isArray()) {
                                             this.evaluateArray(value);
                                          } else {
                                             this.interpolationTargets.add(value);
                                          }
                                       }
                                    } else {
                                       c.add(value);
                                    }
                                 }
                              }
                           } else if (!Map.class.isAssignableFrom(type)) {
                              Object value = fields[i].get(target);
                              if (value != null) {
                                 if (fields[i].getType().isArray()) {
                                    this.evaluateArray(value);
                                 } else {
                                    this.interpolationTargets.add(value);
                                 }
                              }
                           } else {
                              Map<Object, Object> m = (Map)fields[i].get(target);
                              if (m != null && !m.isEmpty()) {
                                 Iterator i$ = m.entrySet().iterator();

                                 while(true) {
                                    Entry entry;
                                    do {
                                       while(true) {
                                          do {
                                             if (!i$.hasNext()) {
                                                continue label302;
                                             }

                                             entry = (Entry)i$.next();
                                             value = entry.getValue();
                                          } while(value == null);

                                          if (String.class == value.getClass()) {
                                             interpolated = this.modelInterpolator.interpolateInternal((String)value, this.valueSources, this.postProcessors, this.debugEnabled);
                                             break;
                                          }

                                          if (value.getClass().isArray()) {
                                             this.evaluateArray(value);
                                          } else {
                                             this.interpolationTargets.add(value);
                                          }
                                       }
                                    } while(interpolated.equals(value));

                                    try {
                                       entry.setValue(interpolated);
                                    } catch (UnsupportedOperationException var20) {
                                       if (this.debugEnabled && this.logger != null) {
                                          this.logger.debug("Skipping interpolation of field: " + fields[i] + " (key: " + entry.getKey() + ") in: " + cls.getName() + "; it is an unmodifiable collection.");
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     } catch (IllegalArgumentException var21) {
                        throw new ModelInterpolationException("Failed to interpolate field: " + fields[i] + " on class: " + cls.getName(), var21);
                     } catch (IllegalAccessException var22) {
                        throw new ModelInterpolationException("Failed to interpolate field: " + fields[i] + " on class: " + cls.getName(), var22);
                     } finally {
                        fields[i].setAccessible(isAccessible);
                     }
                  }
               }

               this.traverseObjectWithParents(cls.getSuperclass(), target);
            }

         }
      }

      private boolean isQualifiedForInterpolation(Class<?> cls) {
         return !cls.getPackage().getName().startsWith("java");
      }

      private boolean isQualifiedForInterpolation(Field field, Class<?> fieldType) {
         if (!StringSearchModelInterpolator.fieldIsPrimitiveByClass.containsKey(fieldType)) {
            StringSearchModelInterpolator.fieldIsPrimitiveByClass.put(fieldType, fieldType.isPrimitive());
         }

         if ((Boolean)StringSearchModelInterpolator.fieldIsPrimitiveByClass.get(fieldType)) {
            return false;
         } else {
            return !"parent".equals(field.getName());
         }
      }

      private void evaluateArray(Object target) throws ModelInterpolationException {
         int len = Array.getLength(target);

         for(int i = 0; i < len; ++i) {
            Object value = Array.get(target, i);
            if (value != null) {
               if (String.class == value.getClass()) {
                  String interpolated = this.modelInterpolator.interpolateInternal((String)value, this.valueSources, this.postProcessors, this.debugEnabled);
                  if (!interpolated.equals(value)) {
                     Array.set(target, i, interpolated);
                  }
               } else {
                  this.interpolationTargets.add(value);
               }
            }
         }

      }
   }
}
