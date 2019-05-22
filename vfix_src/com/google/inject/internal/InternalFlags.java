package com.google.inject.internal;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.logging.Logger;

public class InternalFlags {
   private static final Logger logger = Logger.getLogger(InternalFlags.class.getName());
   private static final InternalFlags.IncludeStackTraceOption INCLUDE_STACK_TRACES = parseIncludeStackTraceOption();
   private static final InternalFlags.CustomClassLoadingOption CUSTOM_CLASS_LOADING = parseCustomClassLoadingOption();
   private static final InternalFlags.NullableProvidesOption NULLABLE_PROVIDES;

   public static InternalFlags.IncludeStackTraceOption getIncludeStackTraceOption() {
      return INCLUDE_STACK_TRACES;
   }

   public static InternalFlags.CustomClassLoadingOption getCustomClassLoadingOption() {
      return CUSTOM_CLASS_LOADING;
   }

   public static InternalFlags.NullableProvidesOption getNullableProvidesOption() {
      return NULLABLE_PROVIDES;
   }

   private static InternalFlags.IncludeStackTraceOption parseIncludeStackTraceOption() {
      return (InternalFlags.IncludeStackTraceOption)getSystemOption("guice_include_stack_traces", InternalFlags.IncludeStackTraceOption.ONLY_FOR_DECLARING_SOURCE);
   }

   private static InternalFlags.CustomClassLoadingOption parseCustomClassLoadingOption() {
      return (InternalFlags.CustomClassLoadingOption)getSystemOption("guice_custom_class_loading", InternalFlags.CustomClassLoadingOption.BRIDGE, InternalFlags.CustomClassLoadingOption.OFF);
   }

   private static InternalFlags.NullableProvidesOption parseNullableProvidesOption(InternalFlags.NullableProvidesOption defaultValue) {
      return (InternalFlags.NullableProvidesOption)getSystemOption("guice_check_nullable_provides_params", defaultValue);
   }

   private static <T extends Enum<T>> T getSystemOption(String name, T defaultValue) {
      return getSystemOption(name, defaultValue, defaultValue);
   }

   private static <T extends Enum<T>> T getSystemOption(final String name, T defaultValue, T secureValue) {
      Class<T> enumType = defaultValue.getDeclaringClass();
      String value = null;

      try {
         value = (String)AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
               return System.getProperty(name);
            }
         });
         return value != null && value.length() > 0 ? Enum.valueOf(enumType, value) : defaultValue;
      } catch (SecurityException var9) {
         return secureValue;
      } catch (IllegalArgumentException var10) {
         Logger var10000 = logger;
         String var6 = String.valueOf(String.valueOf(value));
         String var7 = String.valueOf(String.valueOf(name));
         String var8 = String.valueOf(String.valueOf(Arrays.asList(enumType.getEnumConstants())));
         var10000.warning((new StringBuilder(56 + var6.length() + var7.length() + var8.length())).append(var6).append(" is not a valid flag value for ").append(var7).append(". ").append(" Values must be one of ").append(var8).toString());
         return defaultValue;
      }
   }

   static {
      NULLABLE_PROVIDES = parseNullableProvidesOption(InternalFlags.NullableProvidesOption.ERROR);
   }

   public static enum NullableProvidesOption {
      IGNORE,
      WARN,
      ERROR;
   }

   public static enum CustomClassLoadingOption {
      OFF,
      BRIDGE;
   }

   public static enum IncludeStackTraceOption {
      OFF,
      ONLY_FOR_DECLARING_SOURCE,
      COMPLETE;
   }
}
