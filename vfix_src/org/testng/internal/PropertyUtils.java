package org.testng.internal;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.testng.log4testng.Logger;

public class PropertyUtils {
   private static final Logger LOGGER = Logger.getLogger(PropertyUtils.class);

   public static void setProperty(Object instance, String name, String value) {
      if (instance == null) {
         LOGGER.warn("Cannot set property " + name + " with value " + value + ". The target instance is null");
      } else {
         Class propClass = getPropertyType(instance.getClass(), name);
         if (propClass == null) {
            LOGGER.warn("Cannot set property " + name + " with value " + value + ". Property class could not be found");
         } else {
            Object realValue = Parameters.convertType(propClass, value, name);
            setPropertyRealValue(instance, name, realValue);
         }
      }
   }

   public static Class getPropertyType(Class instanceClass, String propertyName) {
      if (instanceClass == null) {
         LOGGER.warn("Cannot retrieve property class for " + propertyName + ". Target instance class is null");
      }

      PropertyDescriptor propDesc = getPropertyDescriptor(instanceClass, propertyName);
      return propDesc.getPropertyType();
   }

   private static PropertyDescriptor getPropertyDescriptor(Class targetClass, String propertyName) {
      PropertyDescriptor result = null;
      if (targetClass == null) {
         LOGGER.warn("Cannot retrieve property " + propertyName + ". Class is null");
      } else {
         try {
            BeanInfo beanInfo = Introspector.getBeanInfo(targetClass);
            PropertyDescriptor[] propDescriptors = beanInfo.getPropertyDescriptors();
            PropertyDescriptor[] arr$ = propDescriptors;
            int len$ = propDescriptors.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               PropertyDescriptor propDesc = arr$[i$];
               if (propDesc.getName().equals(propertyName)) {
                  result = propDesc;
                  break;
               }
            }
         } catch (IntrospectionException var9) {
            LOGGER.warn("Cannot retrieve property " + propertyName + ". Cause is: " + var9);
         }
      }

      return result;
   }

   public static void setPropertyRealValue(Object instance, String name, Object value) {
      if (instance == null) {
         LOGGER.warn("Cannot set property " + name + " with value " + value + ". Targe instance is null");
      } else {
         PropertyDescriptor propDesc = getPropertyDescriptor(instance.getClass(), name);
         if (propDesc == null) {
            LOGGER.warn("Cannot set property " + name + " with value " + value + ". Property does not exist");
         } else {
            Method method = propDesc.getWriteMethod();

            try {
               method.invoke(instance, value);
            } catch (InvocationTargetException | IllegalAccessException var6) {
               LOGGER.warn("Cannot set property " + name + " with value " + value + ". Cause " + var6);
            }

         }
      }
   }
}
