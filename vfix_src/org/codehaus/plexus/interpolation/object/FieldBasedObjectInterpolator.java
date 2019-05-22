package org.codehaus.plexus.interpolation.object;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.interpolation.Interpolator;
import org.codehaus.plexus.interpolation.RecursionInterceptor;
import org.codehaus.plexus.interpolation.SimpleRecursionInterceptor;

public class FieldBasedObjectInterpolator implements ObjectInterpolator {
   public static final Set DEFAULT_BLACKLISTED_FIELD_NAMES;
   public static final Set DEFAULT_BLACKLISTED_PACKAGE_PREFIXES;
   private static final Map fieldsByClass = new WeakHashMap();
   private static final Map fieldIsPrimitiveByClass = new WeakHashMap();
   private Set blacklistedFieldNames;
   private Set blacklistedPackagePrefixes;
   private List warnings = new ArrayList();

   public static void clearCaches() {
      fieldsByClass.clear();
      fieldIsPrimitiveByClass.clear();
   }

   public FieldBasedObjectInterpolator() {
      this.blacklistedFieldNames = DEFAULT_BLACKLISTED_FIELD_NAMES;
      this.blacklistedPackagePrefixes = DEFAULT_BLACKLISTED_PACKAGE_PREFIXES;
   }

   public FieldBasedObjectInterpolator(Set blacklistedFieldNames, Set blacklistedPackagePrefixes) {
      this.blacklistedFieldNames = blacklistedFieldNames;
      this.blacklistedPackagePrefixes = blacklistedPackagePrefixes;
   }

   public boolean hasWarnings() {
      return this.warnings != null && !this.warnings.isEmpty();
   }

   public List getWarnings() {
      return new ArrayList(this.warnings);
   }

   public void interpolate(Object target, Interpolator interpolator) throws InterpolationException {
      this.interpolate(target, interpolator, new SimpleRecursionInterceptor());
   }

   public void interpolate(Object target, Interpolator interpolator, RecursionInterceptor recursionInterceptor) throws InterpolationException {
      this.warnings.clear();
      FieldBasedObjectInterpolator.InterpolateObjectAction action = new FieldBasedObjectInterpolator.InterpolateObjectAction(target, interpolator, recursionInterceptor, this.blacklistedFieldNames, this.blacklistedPackagePrefixes, this.warnings);
      InterpolationException error = (InterpolationException)AccessController.doPrivileged(action);
      if (error != null) {
         throw error;
      }
   }

   static {
      Set blacklistedFields = new HashSet();
      blacklistedFields.add("parent");
      DEFAULT_BLACKLISTED_FIELD_NAMES = Collections.unmodifiableSet(blacklistedFields);
      Set blacklistedPackages = new HashSet();
      blacklistedPackages.add("java");
      DEFAULT_BLACKLISTED_PACKAGE_PREFIXES = Collections.unmodifiableSet(blacklistedPackages);
   }

   private static final class InterpolationTarget {
      private Object value;
      private String path;

      private InterpolationTarget(Object value, String path) {
         this.value = value;
         this.path = path;
      }

      // $FF: synthetic method
      InterpolationTarget(Object x0, String x1, Object x2) {
         this(x0, x1);
      }
   }

   private static final class InterpolateObjectAction implements PrivilegedAction {
      private final LinkedList interpolationTargets;
      private final Interpolator interpolator;
      private final Set blacklistedFieldNames;
      private final String[] blacklistedPackagePrefixes;
      private final List warningCollector;
      private final RecursionInterceptor recursionInterceptor;

      public InterpolateObjectAction(Object target, Interpolator interpolator, RecursionInterceptor recursionInterceptor, Set blacklistedFieldNames, Set blacklistedPackagePrefixes, List warningCollector) {
         this.recursionInterceptor = recursionInterceptor;
         this.blacklistedFieldNames = blacklistedFieldNames;
         this.warningCollector = warningCollector;
         this.blacklistedPackagePrefixes = (String[])((String[])blacklistedPackagePrefixes.toArray(new String[0]));
         this.interpolationTargets = new LinkedList();
         this.interpolationTargets.add(new FieldBasedObjectInterpolator.InterpolationTarget(target, ""));
         this.interpolator = interpolator;
      }

      public Object run() {
         while(!this.interpolationTargets.isEmpty()) {
            FieldBasedObjectInterpolator.InterpolationTarget target = (FieldBasedObjectInterpolator.InterpolationTarget)this.interpolationTargets.removeFirst();

            try {
               this.traverseObjectWithParents(target.value.getClass(), target);
            } catch (InterpolationException var3) {
               return var3;
            }
         }

         return null;
      }

      private void traverseObjectWithParents(Class cls, FieldBasedObjectInterpolator.InterpolationTarget target) throws InterpolationException {
         Object obj = target.value;
         String basePath = target.path;
         if (cls != null) {
            if (cls.isArray()) {
               this.evaluateArray(obj, basePath);
            } else if (this.isQualifiedForInterpolation(cls)) {
               Field[] fields = (Field[])((Field[])FieldBasedObjectInterpolator.fieldsByClass.get(cls));
               if (fields == null) {
                  fields = cls.getDeclaredFields();
                  FieldBasedObjectInterpolator.fieldsByClass.put(cls, fields);
               }

               for(int i = 0; i < fields.length; ++i) {
                  Class type = fields[i].getType();
                  if (this.isQualifiedForInterpolation(fields[i], type)) {
                     boolean isAccessible = fields[i].isAccessible();
                     fields[i].setAccessible(true);

                     try {
                        if (String.class == type) {
                           String value = (String)fields[i].get(obj);
                           if (value != null) {
                              String interpolated = this.interpolator.interpolate(value, this.recursionInterceptor);
                              if (!interpolated.equals(value)) {
                                 fields[i].set(obj, interpolated);
                              }
                           }
                        } else {
                           Object value;
                           String interpolated;
                           if (Collection.class.isAssignableFrom(type)) {
                              Collection c = (Collection)fields[i].get(obj);
                              if (c != null && !c.isEmpty()) {
                                 ArrayList originalValues = new ArrayList(c);

                                 try {
                                    c.clear();
                                 } catch (UnsupportedOperationException var22) {
                                    this.warningCollector.add(new ObjectInterpolationWarning("Field is an unmodifiable collection. Skipping interpolation.", basePath + "." + fields[i].getName(), var22));
                                    continue;
                                 }

                                 Iterator it = originalValues.iterator();

                                 while(it.hasNext()) {
                                    value = it.next();
                                    if (value != null) {
                                       if (String.class == value.getClass()) {
                                          interpolated = this.interpolator.interpolate((String)value, this.recursionInterceptor);
                                          if (!interpolated.equals(value)) {
                                             c.add(interpolated);
                                          } else {
                                             c.add(value);
                                          }
                                       } else {
                                          c.add(value);
                                          if (value.getClass().isArray()) {
                                             this.evaluateArray(value, basePath + "." + fields[i].getName());
                                          } else {
                                             this.interpolationTargets.add(new FieldBasedObjectInterpolator.InterpolationTarget(value, basePath + "." + fields[i].getName()));
                                          }
                                       }
                                    } else {
                                       c.add(value);
                                    }
                                 }
                              }
                           } else if (Map.class.isAssignableFrom(type)) {
                              Map m = (Map)fields[i].get(obj);
                              if (m != null && !m.isEmpty()) {
                                 Iterator it = m.entrySet().iterator();

                                 while(it.hasNext()) {
                                    Entry entry = (Entry)it.next();
                                    value = entry.getValue();
                                    if (value != null) {
                                       if (String.class == value.getClass()) {
                                          interpolated = this.interpolator.interpolate((String)value, this.recursionInterceptor);
                                          if (!interpolated.equals(value)) {
                                             try {
                                                entry.setValue(interpolated);
                                             } catch (UnsupportedOperationException var21) {
                                                this.warningCollector.add(new ObjectInterpolationWarning("Field is an unmodifiable collection. Skipping interpolation.", basePath + "." + fields[i].getName(), var21));
                                             }
                                          }
                                       } else if (value.getClass().isArray()) {
                                          this.evaluateArray(value, basePath + "." + fields[i].getName());
                                       } else {
                                          this.interpolationTargets.add(new FieldBasedObjectInterpolator.InterpolationTarget(value, basePath + "." + fields[i].getName()));
                                       }
                                    }
                                 }
                              }
                           } else {
                              Object value = fields[i].get(obj);
                              if (value != null) {
                                 if (fields[i].getType().isArray()) {
                                    this.evaluateArray(value, basePath + "." + fields[i].getName());
                                 } else {
                                    this.interpolationTargets.add(new FieldBasedObjectInterpolator.InterpolationTarget(value, basePath + "." + fields[i].getName()));
                                 }
                              }
                           }
                        }
                     } catch (IllegalArgumentException var23) {
                        this.warningCollector.add(new ObjectInterpolationWarning("Failed to interpolate field. Skipping.", basePath + "." + fields[i].getName(), var23));
                     } catch (IllegalAccessException var24) {
                        this.warningCollector.add(new ObjectInterpolationWarning("Failed to interpolate field. Skipping.", basePath + "." + fields[i].getName(), var24));
                     } finally {
                        fields[i].setAccessible(isAccessible);
                     }
                  }
               }

               this.traverseObjectWithParents(cls.getSuperclass(), target);
            }

         }
      }

      private boolean isQualifiedForInterpolation(Class cls) {
         String pkgName = cls.getPackage().getName();

         for(int i = 0; i < this.blacklistedPackagePrefixes.length; ++i) {
            String prefix = this.blacklistedPackagePrefixes[i];
            if (pkgName.startsWith(prefix)) {
               return false;
            }
         }

         return true;
      }

      private boolean isQualifiedForInterpolation(Field field, Class fieldType) {
         if (!FieldBasedObjectInterpolator.fieldIsPrimitiveByClass.containsKey(fieldType)) {
            FieldBasedObjectInterpolator.fieldIsPrimitiveByClass.put(fieldType, fieldType.isPrimitive());
         }

         if ((Boolean)FieldBasedObjectInterpolator.fieldIsPrimitiveByClass.get(fieldType)) {
            return false;
         } else {
            return !this.blacklistedFieldNames.contains(field.getName());
         }
      }

      private void evaluateArray(Object target, String basePath) throws InterpolationException {
         int len = Array.getLength(target);

         for(int i = 0; i < len; ++i) {
            Object value = Array.get(target, i);
            if (value != null) {
               if (String.class == value.getClass()) {
                  String interpolated = this.interpolator.interpolate((String)value, this.recursionInterceptor);
                  if (!interpolated.equals(value)) {
                     Array.set(target, i, interpolated);
                  }
               } else {
                  this.interpolationTargets.add(new FieldBasedObjectInterpolator.InterpolationTarget(value, basePath + "[" + i + "]"));
               }
            }
         }

      }
   }
}
