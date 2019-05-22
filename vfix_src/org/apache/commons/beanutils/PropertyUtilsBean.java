package org.apache.commons.beanutils;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertyUtilsBean {
   private FastHashMap descriptorsCache = null;
   private FastHashMap mappedDescriptorsCache = null;
   private Log log;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$PropertyUtils;

   protected static PropertyUtilsBean getInstance() {
      return BeanUtilsBean.getInstance().getPropertyUtils();
   }

   public PropertyUtilsBean() {
      this.log = LogFactory.getLog(class$org$apache$commons$beanutils$PropertyUtils == null ? (class$org$apache$commons$beanutils$PropertyUtils = class$("org.apache.commons.beanutils.PropertyUtils")) : class$org$apache$commons$beanutils$PropertyUtils);
      this.descriptorsCache = new FastHashMap();
      this.descriptorsCache.setFast(true);
      this.mappedDescriptorsCache = new FastHashMap();
      this.mappedDescriptorsCache.setFast(true);
   }

   public void clearDescriptors() {
      this.descriptorsCache.clear();
      this.mappedDescriptorsCache.clear();
      Introspector.flushCaches();
   }

   public void copyProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (dest == null) {
         throw new IllegalArgumentException("No destination bean specified");
      } else if (orig == null) {
         throw new IllegalArgumentException("No origin bean specified");
      } else {
         int i;
         String name;
         Object value;
         if (orig instanceof DynaBean) {
            DynaProperty[] origDescriptors = ((DynaBean)orig).getDynaClass().getDynaProperties();

            for(i = 0; i < origDescriptors.length; ++i) {
               name = origDescriptors[i].getName();
               if (dest instanceof DynaBean) {
                  if (this.isWriteable(dest, name)) {
                     value = ((DynaBean)orig).get(name);
                     ((DynaBean)dest).set(name, value);
                  }
               } else if (this.isWriteable(dest, name)) {
                  value = ((DynaBean)orig).get(name);
                  this.setSimpleProperty(dest, name, value);
               }
            }
         } else if (orig instanceof Map) {
            Iterator names = ((Map)orig).keySet().iterator();

            while(names.hasNext()) {
               String name = (String)names.next();
               Object value;
               if (dest instanceof DynaBean) {
                  if (this.isWriteable(dest, name)) {
                     value = ((Map)orig).get(name);
                     ((DynaBean)dest).set(name, value);
                  }
               } else if (this.isWriteable(dest, name)) {
                  value = ((Map)orig).get(name);
                  this.setSimpleProperty(dest, name, value);
               }
            }
         } else {
            PropertyDescriptor[] origDescriptors = this.getPropertyDescriptors(orig);

            for(i = 0; i < origDescriptors.length; ++i) {
               name = origDescriptors[i].getName();
               if (this.isReadable(orig, name)) {
                  if (dest instanceof DynaBean) {
                     if (this.isWriteable(dest, name)) {
                        value = this.getSimpleProperty(orig, name);
                        ((DynaBean)dest).set(name, value);
                     }
                  } else if (this.isWriteable(dest, name)) {
                     value = this.getSimpleProperty(orig, name);
                     this.setSimpleProperty(dest, name, value);
                  }
               }
            }
         }

      }
   }

   public Map describe(Object bean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else {
         Map description = new HashMap();
         int i;
         String name;
         if (bean instanceof DynaBean) {
            DynaProperty[] descriptors = ((DynaBean)bean).getDynaClass().getDynaProperties();

            for(i = 0; i < descriptors.length; ++i) {
               name = descriptors[i].getName();
               description.put(name, this.getProperty(bean, name));
            }
         } else {
            PropertyDescriptor[] descriptors = this.getPropertyDescriptors(bean);

            for(i = 0; i < descriptors.length; ++i) {
               name = descriptors[i].getName();
               if (descriptors[i].getReadMethod() != null) {
                  description.put(name, this.getProperty(bean, name));
               }
            }
         }

         return description;
      }
   }

   public Object getIndexedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else {
         int delim = name.indexOf(91);
         int delim2 = name.indexOf(93);
         if (delim >= 0 && delim2 > delim) {
            boolean var5 = true;

            int index;
            try {
               String subscript = name.substring(delim + 1, delim2);
               index = Integer.parseInt(subscript);
            } catch (NumberFormatException var7) {
               throw new IllegalArgumentException("Invalid indexed property '" + name + "'");
            }

            name = name.substring(0, delim);
            return this.getIndexedProperty(bean, name, index);
         } else {
            throw new IllegalArgumentException("Invalid indexed property '" + name + "'");
         }
      }
   }

   public Object getIndexedProperty(Object bean, String name, int index) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else if (bean instanceof DynaBean) {
         DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
         if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" + name + "'");
         } else {
            return ((DynaBean)bean).get(name, index);
         }
      } else {
         PropertyDescriptor descriptor = this.getPropertyDescriptor(bean, name);
         if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" + name + "'");
         } else {
            Method readMethod;
            if (descriptor instanceof IndexedPropertyDescriptor) {
               readMethod = ((IndexedPropertyDescriptor)descriptor).getIndexedReadMethod();
               if (readMethod != null) {
                  Object[] subscript = new Object[]{new Integer(index)};

                  try {
                     return this.invokeMethod(readMethod, bean, subscript);
                  } catch (InvocationTargetException var8) {
                     if (var8.getTargetException() instanceof ArrayIndexOutOfBoundsException) {
                        throw (ArrayIndexOutOfBoundsException)var8.getTargetException();
                     }

                     throw var8;
                  }
               }
            }

            readMethod = this.getReadMethod(descriptor);
            if (readMethod == null) {
               throw new NoSuchMethodException("Property '" + name + "' has no getter method");
            } else {
               Object value = this.invokeMethod(readMethod, bean, new Object[0]);
               if (!value.getClass().isArray()) {
                  if (!(value instanceof List)) {
                     throw new IllegalArgumentException("Property '" + name + "' is not indexed");
                  } else {
                     return ((List)value).get(index);
                  }
               } else {
                  return Array.get(value, index);
               }
            }
         }
      }
   }

   public Object getMappedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else {
         int delim = name.indexOf(40);
         int delim2 = name.indexOf(41);
         if (delim >= 0 && delim2 > delim) {
            String key = name.substring(delim + 1, delim2);
            name = name.substring(0, delim);
            return this.getMappedProperty(bean, name, key);
         } else {
            throw new IllegalArgumentException("Invalid mapped property '" + name + "'");
         }
      }
   }

   public Object getMappedProperty(Object bean, String name, String key) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else if (key == null) {
         throw new IllegalArgumentException("No key specified");
      } else if (bean instanceof DynaBean) {
         DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
         if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" + name + "'");
         } else {
            return ((DynaBean)bean).get(name, key);
         }
      } else {
         Object result = null;
         PropertyDescriptor descriptor = this.getPropertyDescriptor(bean, name);
         if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" + name + "'");
         } else {
            Method readMethod;
            if (descriptor instanceof MappedPropertyDescriptor) {
               readMethod = ((MappedPropertyDescriptor)descriptor).getMappedReadMethod();
               if (readMethod == null) {
                  throw new NoSuchMethodException("Property '" + name + "' has no mapped getter method");
               }

               Object[] keyArray = new Object[]{key};
               result = this.invokeMethod(readMethod, bean, keyArray);
            } else {
               readMethod = descriptor.getReadMethod();
               if (readMethod == null) {
                  throw new NoSuchMethodException("Property '" + name + "' has no mapped getter method");
               }

               Object invokeResult = this.invokeMethod(readMethod, bean, new Object[0]);
               if (invokeResult instanceof Map) {
                  result = ((Map)invokeResult).get(key);
               }
            }

            return result;
         }
      }
   }

   /** @deprecated */
   public FastHashMap getMappedPropertyDescriptors(Class beanClass) {
      return beanClass == null ? null : (FastHashMap)this.mappedDescriptorsCache.get(beanClass);
   }

   /** @deprecated */
   public FastHashMap getMappedPropertyDescriptors(Object bean) {
      return bean == null ? null : this.getMappedPropertyDescriptors(bean.getClass());
   }

   public Object getNestedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else {
         int indexOfINDEXED_DELIM = true;
         int indexOfMAPPED_DELIM = true;
         int indexOfMAPPED_DELIM2 = true;
         boolean var6 = true;

         while(true) {
            int indexOfNESTED_DELIM = name.indexOf(46);
            int indexOfMAPPED_DELIM = name.indexOf(40);
            int indexOfMAPPED_DELIM2 = name.indexOf(41);
            if (indexOfMAPPED_DELIM2 < 0 || indexOfMAPPED_DELIM < 0 || indexOfNESTED_DELIM >= 0 && indexOfNESTED_DELIM <= indexOfMAPPED_DELIM) {
               indexOfNESTED_DELIM = name.indexOf(46);
            } else {
               indexOfNESTED_DELIM = name.indexOf(46, indexOfMAPPED_DELIM2);
            }

            int indexOfINDEXED_DELIM;
            if (indexOfNESTED_DELIM < 0) {
               indexOfINDEXED_DELIM = name.indexOf(91);
               indexOfMAPPED_DELIM = name.indexOf(40);
               if (bean instanceof Map) {
                  bean = ((Map)bean).get(name);
               } else if (indexOfMAPPED_DELIM >= 0) {
                  bean = this.getMappedProperty(bean, name);
               } else if (indexOfINDEXED_DELIM >= 0) {
                  bean = this.getIndexedProperty(bean, name);
               } else {
                  bean = this.getSimpleProperty(bean, name);
               }

               return bean;
            }

            String next = name.substring(0, indexOfNESTED_DELIM);
            indexOfINDEXED_DELIM = next.indexOf(91);
            indexOfMAPPED_DELIM = next.indexOf(40);
            if (bean instanceof Map) {
               bean = ((Map)bean).get(next);
            } else if (indexOfMAPPED_DELIM >= 0) {
               bean = this.getMappedProperty(bean, next);
            } else if (indexOfINDEXED_DELIM >= 0) {
               bean = this.getIndexedProperty(bean, next);
            } else {
               bean = this.getSimpleProperty(bean, next);
            }

            if (bean == null) {
               throw new NestedNullException("Null property value for '" + name.substring(0, indexOfNESTED_DELIM) + "'");
            }

            name = name.substring(indexOfNESTED_DELIM + 1);
         }
      }
   }

   public Object getProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return this.getNestedProperty(bean, name);
   }

   public PropertyDescriptor getPropertyDescriptor(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else {
         while(true) {
            int period = this.findNextNestedIndex(name);
            int i;
            if (period < 0) {
               period = name.indexOf(91);
               if (period >= 0) {
                  name = name.substring(0, period);
               }

               period = name.indexOf(40);
               if (period >= 0) {
                  name = name.substring(0, period);
               }

               if (bean != null && name != null) {
                  PropertyDescriptor[] descriptors = this.getPropertyDescriptors(bean);
                  if (descriptors != null) {
                     for(i = 0; i < descriptors.length; ++i) {
                        if (name.equals(descriptors[i].getName())) {
                           return descriptors[i];
                        }
                     }
                  }

                  PropertyDescriptor result = null;
                  FastHashMap mappedDescriptors = this.getMappedPropertyDescriptors(bean);
                  if (mappedDescriptors == null) {
                     mappedDescriptors = new FastHashMap();
                     mappedDescriptors.setFast(true);
                     this.mappedDescriptorsCache.put(bean.getClass(), mappedDescriptors);
                  }

                  result = (PropertyDescriptor)mappedDescriptors.get(name);
                  if (result == null) {
                     try {
                        result = new MappedPropertyDescriptor(name, bean.getClass());
                     } catch (IntrospectionException var8) {
                     }

                     if (result != null) {
                        mappedDescriptors.put(name, result);
                     }
                  }

                  return (PropertyDescriptor)result;
               }

               return null;
            }

            String next = name.substring(0, period);
            i = next.indexOf(91);
            int indexOfMAPPED_DELIM = next.indexOf(40);
            if (indexOfMAPPED_DELIM >= 0 && (i < 0 || indexOfMAPPED_DELIM < i)) {
               bean = this.getMappedProperty(bean, next);
            } else if (i >= 0) {
               bean = this.getIndexedProperty(bean, next);
            } else {
               bean = this.getSimpleProperty(bean, next);
            }

            if (bean == null) {
               throw new IllegalArgumentException("Null property value for '" + name.substring(0, period) + "'");
            }

            name = name.substring(period + 1);
         }
      }
   }

   private int findNextNestedIndex(String expression) {
      int bracketCount = 0;
      int i = 0;

      for(int size = expression.length(); i < size; ++i) {
         char at = expression.charAt(i);
         switch(at) {
         case '(':
         case '[':
            ++bracketCount;
            break;
         case ')':
         case ']':
            --bracketCount;
            break;
         case '.':
            if (bracketCount < 1) {
               return i;
            }
         }
      }

      return -1;
   }

   public PropertyDescriptor[] getPropertyDescriptors(Class beanClass) {
      if (beanClass == null) {
         throw new IllegalArgumentException("No bean class specified");
      } else {
         PropertyDescriptor[] descriptors = null;
         descriptors = (PropertyDescriptor[])this.descriptorsCache.get(beanClass);
         if (descriptors != null) {
            return descriptors;
         } else {
            BeanInfo beanInfo = null;

            try {
               beanInfo = Introspector.getBeanInfo(beanClass);
            } catch (IntrospectionException var5) {
               return new PropertyDescriptor[0];
            }

            descriptors = beanInfo.getPropertyDescriptors();
            if (descriptors == null) {
               descriptors = new PropertyDescriptor[0];
            }

            this.descriptorsCache.put(beanClass, descriptors);
            return descriptors;
         }
      }
   }

   public PropertyDescriptor[] getPropertyDescriptors(Object bean) {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else {
         return this.getPropertyDescriptors(bean.getClass());
      }
   }

   public Class getPropertyEditorClass(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else {
         PropertyDescriptor descriptor = this.getPropertyDescriptor(bean, name);
         return descriptor != null ? descriptor.getPropertyEditorClass() : null;
      }
   }

   public Class getPropertyType(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else if (bean instanceof DynaBean) {
         DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
         if (descriptor == null) {
            return null;
         } else {
            Class type = descriptor.getType();
            if (type == null) {
               return null;
            } else {
               return type.isArray() ? type.getComponentType() : type;
            }
         }
      } else {
         PropertyDescriptor descriptor = this.getPropertyDescriptor(bean, name);
         if (descriptor == null) {
            return null;
         } else if (descriptor instanceof IndexedPropertyDescriptor) {
            return ((IndexedPropertyDescriptor)descriptor).getIndexedPropertyType();
         } else {
            return descriptor instanceof MappedPropertyDescriptor ? ((MappedPropertyDescriptor)descriptor).getMappedPropertyType() : descriptor.getPropertyType();
         }
      }
   }

   public Method getReadMethod(PropertyDescriptor descriptor) {
      return MethodUtils.getAccessibleMethod(descriptor.getReadMethod());
   }

   public Object getSimpleProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else if (name.indexOf(46) >= 0) {
         throw new IllegalArgumentException("Nested property names are not allowed");
      } else if (name.indexOf(91) >= 0) {
         throw new IllegalArgumentException("Indexed property names are not allowed");
      } else if (name.indexOf(40) >= 0) {
         throw new IllegalArgumentException("Mapped property names are not allowed");
      } else if (bean instanceof DynaBean) {
         DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
         if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" + name + "'");
         } else {
            return ((DynaBean)bean).get(name);
         }
      } else {
         PropertyDescriptor descriptor = this.getPropertyDescriptor(bean, name);
         if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" + name + "'");
         } else {
            Method readMethod = this.getReadMethod(descriptor);
            if (readMethod == null) {
               throw new NoSuchMethodException("Property '" + name + "' has no getter method");
            } else {
               Object value = this.invokeMethod(readMethod, bean, new Object[0]);
               return value;
            }
         }
      }
   }

   public Method getWriteMethod(PropertyDescriptor descriptor) {
      return MethodUtils.getAccessibleMethod(descriptor.getWriteMethod());
   }

   public boolean isReadable(Object bean, String name) {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else if (bean instanceof DynaBean) {
         return ((DynaBean)bean).getDynaClass().getDynaProperty(name) != null;
      } else {
         try {
            PropertyDescriptor desc = this.getPropertyDescriptor(bean, name);
            if (desc != null) {
               Method readMethod = desc.getReadMethod();
               if (readMethod == null && desc instanceof IndexedPropertyDescriptor) {
                  readMethod = ((IndexedPropertyDescriptor)desc).getIndexedReadMethod();
               }

               return readMethod != null;
            } else {
               return false;
            }
         } catch (IllegalAccessException var6) {
            return false;
         } catch (InvocationTargetException var7) {
            return false;
         } catch (NoSuchMethodException var8) {
            return false;
         }
      }
   }

   public boolean isWriteable(Object bean, String name) {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else if (bean instanceof DynaBean) {
         return ((DynaBean)bean).getDynaClass().getDynaProperty(name) != null;
      } else {
         try {
            PropertyDescriptor desc = this.getPropertyDescriptor(bean, name);
            if (desc != null) {
               Method writeMethod = desc.getWriteMethod();
               if (writeMethod == null && desc instanceof IndexedPropertyDescriptor) {
                  writeMethod = ((IndexedPropertyDescriptor)desc).getIndexedWriteMethod();
               }

               return writeMethod != null;
            } else {
               return false;
            }
         } catch (IllegalAccessException var6) {
            return false;
         } catch (InvocationTargetException var7) {
            return false;
         } catch (NoSuchMethodException var8) {
            return false;
         }
      }
   }

   public void setIndexedProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else {
         int delim = name.indexOf(91);
         int delim2 = name.indexOf(93);
         if (delim >= 0 && delim2 > delim) {
            boolean var6 = true;

            int index;
            try {
               String subscript = name.substring(delim + 1, delim2);
               index = Integer.parseInt(subscript);
            } catch (NumberFormatException var8) {
               throw new IllegalArgumentException("Invalid indexed property '" + name + "'");
            }

            name = name.substring(0, delim);
            this.setIndexedProperty(bean, name, index, value);
         } else {
            throw new IllegalArgumentException("Invalid indexed property '" + name + "'");
         }
      }
   }

   public void setIndexedProperty(Object bean, String name, int index, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else if (bean instanceof DynaBean) {
         DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
         if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" + name + "'");
         } else {
            ((DynaBean)bean).set(name, index, value);
         }
      } else {
         PropertyDescriptor descriptor = this.getPropertyDescriptor(bean, name);
         if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" + name + "'");
         } else {
            Method writeMethod;
            if (descriptor instanceof IndexedPropertyDescriptor) {
               writeMethod = ((IndexedPropertyDescriptor)descriptor).getIndexedWriteMethod();
               if (writeMethod != null) {
                  Object[] subscript = new Object[]{new Integer(index), value};

                  try {
                     if (this.log.isTraceEnabled()) {
                        String valueClassName = value == null ? "<null>" : value.getClass().getName();
                        this.log.trace("setSimpleProperty: Invoking method " + writeMethod + " with index=" + index + ", value=" + value + " (class " + valueClassName + ")");
                     }

                     this.invokeMethod(writeMethod, bean, subscript);
                     return;
                  } catch (InvocationTargetException var9) {
                     if (var9.getTargetException() instanceof ArrayIndexOutOfBoundsException) {
                        throw (ArrayIndexOutOfBoundsException)var9.getTargetException();
                     }

                     throw var9;
                  }
               }
            }

            writeMethod = descriptor.getReadMethod();
            if (writeMethod == null) {
               throw new NoSuchMethodException("Property '" + name + "' has no getter method");
            } else {
               Object array = this.invokeMethod(writeMethod, bean, new Object[0]);
               if (!array.getClass().isArray()) {
                  if (!(array instanceof List)) {
                     throw new IllegalArgumentException("Property '" + name + "' is not indexed");
                  }

                  ((List)array).set(index, value);
               } else {
                  Array.set(array, index, value);
               }

            }
         }
      }
   }

   public void setMappedProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else {
         int delim = name.indexOf(40);
         int delim2 = name.indexOf(41);
         if (delim >= 0 && delim2 > delim) {
            String key = name.substring(delim + 1, delim2);
            name = name.substring(0, delim);
            this.setMappedProperty(bean, name, key, value);
         } else {
            throw new IllegalArgumentException("Invalid mapped property '" + name + "'");
         }
      }
   }

   public void setMappedProperty(Object bean, String name, String key, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else if (key == null) {
         throw new IllegalArgumentException("No key specified");
      } else if (bean instanceof DynaBean) {
         DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
         if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" + name + "'");
         } else {
            ((DynaBean)bean).set(name, key, value);
         }
      } else {
         PropertyDescriptor descriptor = this.getPropertyDescriptor(bean, name);
         if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" + name + "'");
         } else {
            Method mappedWriteMethod;
            if (descriptor instanceof MappedPropertyDescriptor) {
               mappedWriteMethod = ((MappedPropertyDescriptor)descriptor).getMappedWriteMethod();
               if (mappedWriteMethod == null) {
                  throw new NoSuchMethodException("Property '" + name + "' has no mapped setter method");
               }

               Object[] params = new Object[]{key, value};
               if (this.log.isTraceEnabled()) {
                  String valueClassName = value == null ? "<null>" : value.getClass().getName();
                  this.log.trace("setSimpleProperty: Invoking method " + mappedWriteMethod + " with key=" + key + ", value=" + value + " (class " + valueClassName + ")");
               }

               this.invokeMethod(mappedWriteMethod, bean, params);
            } else {
               mappedWriteMethod = descriptor.getReadMethod();
               if (mappedWriteMethod == null) {
                  throw new NoSuchMethodException("Property '" + name + "' has no mapped getter method");
               }

               Object invokeResult = this.invokeMethod(mappedWriteMethod, bean, new Object[0]);
               if (invokeResult instanceof Map) {
                  ((Map)invokeResult).put(key, value);
               }
            }

         }
      }
   }

   public void setNestedProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else {
         int indexOfINDEXED_DELIM = true;
         boolean var5 = true;

         while(true) {
            int delim = name.indexOf(46);
            int indexOfINDEXED_DELIM;
            int indexOfMAPPED_DELIM;
            if (delim < 0) {
               indexOfINDEXED_DELIM = name.indexOf(91);
               indexOfMAPPED_DELIM = name.indexOf(40);
               if (bean instanceof Map) {
                  PropertyDescriptor descriptor = this.getPropertyDescriptor(bean, name);
                  if (descriptor == null) {
                     ((Map)bean).put(name, value);
                  } else {
                     this.setSimpleProperty(bean, name, value);
                  }
               } else if (indexOfMAPPED_DELIM >= 0) {
                  this.setMappedProperty(bean, name, value);
               } else if (indexOfINDEXED_DELIM >= 0) {
                  this.setIndexedProperty(bean, name, value);
               } else {
                  this.setSimpleProperty(bean, name, value);
               }

               return;
            }

            String next = name.substring(0, delim);
            indexOfINDEXED_DELIM = next.indexOf(91);
            indexOfMAPPED_DELIM = next.indexOf(40);
            if (bean instanceof Map) {
               bean = ((Map)bean).get(next);
            } else if (indexOfMAPPED_DELIM >= 0) {
               bean = this.getMappedProperty(bean, next);
            } else if (indexOfINDEXED_DELIM >= 0) {
               bean = this.getIndexedProperty(bean, next);
            } else {
               bean = this.getSimpleProperty(bean, next);
            }

            if (bean == null) {
               throw new IllegalArgumentException("Null property value for '" + name.substring(0, delim) + "'");
            }

            name = name.substring(delim + 1);
         }
      }
   }

   public void setProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      this.setNestedProperty(bean, name, value);
   }

   public void setSimpleProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         throw new IllegalArgumentException("No bean specified");
      } else if (name == null) {
         throw new IllegalArgumentException("No name specified");
      } else if (name.indexOf(46) >= 0) {
         throw new IllegalArgumentException("Nested property names are not allowed");
      } else if (name.indexOf(91) >= 0) {
         throw new IllegalArgumentException("Indexed property names are not allowed");
      } else if (name.indexOf(40) >= 0) {
         throw new IllegalArgumentException("Mapped property names are not allowed");
      } else if (bean instanceof DynaBean) {
         DynaProperty descriptor = ((DynaBean)bean).getDynaClass().getDynaProperty(name);
         if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" + name + "'");
         } else {
            ((DynaBean)bean).set(name, value);
         }
      } else {
         PropertyDescriptor descriptor = this.getPropertyDescriptor(bean, name);
         if (descriptor == null) {
            throw new NoSuchMethodException("Unknown property '" + name + "'");
         } else {
            Method writeMethod = this.getWriteMethod(descriptor);
            if (writeMethod == null) {
               throw new NoSuchMethodException("Property '" + name + "' has no setter method");
            } else {
               Object[] values = new Object[]{value};
               if (this.log.isTraceEnabled()) {
                  String valueClassName = value == null ? "<null>" : value.getClass().getName();
                  this.log.trace("setSimpleProperty: Invoking method " + writeMethod + " with value " + value + " (class " + valueClassName + ")");
               }

               this.invokeMethod(writeMethod, bean, values);
            }
         }
      }
   }

   private Object invokeMethod(Method method, Object bean, Object[] values) throws IllegalAccessException, InvocationTargetException {
      try {
         return method.invoke(bean, values);
      } catch (IllegalArgumentException var5) {
         this.log.error("Method invocation failed.", var5);
         throw new IllegalArgumentException("Cannot invoke " + method.getDeclaringClass().getName() + "." + method.getName() + " - " + var5.getMessage());
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
