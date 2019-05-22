package org.codehaus.plexus.component.configurator.converters;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.util.ReflectionUtils;

public class ComponentValueSetter {
   private Object object;
   private String fieldName;
   private ConverterLookup lookup;
   private Method setter;
   private Class setterParamType;
   private ConfigurationConverter setterTypeConverter;
   private Field field;
   private Class fieldType;
   private ConfigurationConverter fieldTypeConverter;
   private ConfigurationListener listener;

   public ComponentValueSetter(String fieldName, Object object, ConverterLookup lookup) throws ComponentConfigurationException {
      this(fieldName, object, lookup, (ConfigurationListener)null);
   }

   public ComponentValueSetter(String fieldName, Object object, ConverterLookup lookup, ConfigurationListener listener) throws ComponentConfigurationException {
      this.fieldName = fieldName;
      this.object = object;
      this.lookup = lookup;
      this.listener = listener;
      if (object == null) {
         throw new ComponentConfigurationException("Component is null");
      } else {
         this.initSetter();
         this.initField();
         if (this.setter == null && this.field == null) {
            throw new ComponentConfigurationException("Cannot find setter nor field in " + object.getClass().getName() + " for '" + fieldName + "'");
         } else if (this.setterTypeConverter == null && this.fieldTypeConverter == null) {
            throw new ComponentConfigurationException("Cannot find converter for " + this.setterParamType.getName() + (this.fieldType != null && !this.fieldType.equals(this.setterParamType) ? " or " + this.fieldType.getName() : ""));
         }
      }
   }

   private void initSetter() {
      this.setter = ReflectionUtils.getSetter(this.fieldName, this.object.getClass());
      if (this.setter != null) {
         this.setterParamType = this.setter.getParameterTypes()[0];

         try {
            this.setterTypeConverter = this.lookup.lookupConverterForType(this.setterParamType);
         } catch (ComponentConfigurationException var2) {
         }

      }
   }

   private void initField() {
      this.field = ReflectionUtils.getFieldByNameIncludingSuperclasses(this.fieldName, this.object.getClass());
      if (this.field != null) {
         this.fieldType = this.field.getType();

         try {
            this.fieldTypeConverter = this.lookup.lookupConverterForType(this.fieldType);
         } catch (ComponentConfigurationException var2) {
         }

      }
   }

   private void setValueUsingField(Object value) throws ComponentConfigurationException {
      String exceptionInfo = this.object.getClass().getName() + "." + this.field.getName() + "; type: " + value.getClass().getName();

      try {
         boolean wasAccessible = this.field.isAccessible();
         if (!wasAccessible) {
            this.field.setAccessible(true);
         }

         if (this.listener != null) {
            this.listener.notifyFieldChangeUsingReflection(this.fieldName, value, this.object);
         }

         this.field.set(this.object, value);
         if (!wasAccessible) {
            this.field.setAccessible(false);
         }

      } catch (IllegalAccessException var4) {
         throw new ComponentConfigurationException("Cannot access field: " + exceptionInfo, var4);
      } catch (IllegalArgumentException var5) {
         throw new ComponentConfigurationException("Cannot assign value '" + value + "' to field: " + exceptionInfo, var5);
      }
   }

   private void setValueUsingSetter(Object value) throws ComponentConfigurationException {
      if (this.setterParamType != null && this.setter != null) {
         String exceptionInfo = this.object.getClass().getName() + "." + this.setter.getName() + "( " + this.setterParamType.getClass().getName() + " )";
         if (this.listener != null) {
            this.listener.notifyFieldChangeUsingSetter(this.fieldName, value, this.object);
         }

         try {
            this.setter.invoke(this.object, value);
         } catch (IllegalAccessException var4) {
            throw new ComponentConfigurationException("Cannot access method: " + exceptionInfo, var4);
         } catch (IllegalArgumentException var5) {
            throw new ComponentConfigurationException("Invalid parameter supplied while setting '" + value + "' to " + exceptionInfo, var5);
         } catch (InvocationTargetException var6) {
            throw new ComponentConfigurationException("Setter " + exceptionInfo + " threw exception when called with parameter '" + value + "': " + var6.getTargetException().getMessage(), var6);
         }
      } else {
         throw new ComponentConfigurationException("No setter found");
      }
   }

   public void configure(PlexusConfiguration config, ClassLoader cl, ExpressionEvaluator evaluator) throws ComponentConfigurationException {
      Object value = null;
      if (this.setterTypeConverter != null) {
         try {
            value = this.setterTypeConverter.fromConfiguration(this.lookup, config, this.setterParamType, this.object.getClass(), cl, evaluator, this.listener);
            if (value != null) {
               this.setValueUsingSetter(value);
               return;
            }
         } catch (ComponentConfigurationException var8) {
            if (this.fieldTypeConverter == null || this.fieldTypeConverter.getClass().equals(this.setterTypeConverter.getClass())) {
               throw var8;
            }
         }
      }

      ComponentConfigurationException savedEx = null;
      if (value != null) {
         try {
            this.setValueUsingField(value);
            return;
         } catch (ComponentConfigurationException var7) {
            savedEx = var7;
         }
      }

      value = this.fieldTypeConverter.fromConfiguration(this.lookup, config, this.fieldType, this.object.getClass(), cl, evaluator, this.listener);
      if (value != null) {
         this.setValueUsingField(value);
      } else if (savedEx != null) {
         throw savedEx;
      }

   }
}
