package com.google.inject.internal;

import com.google.inject.TypeLiteral;
import com.google.inject.internal.util.SourceProvider;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeConverter;
import com.google.inject.spi.TypeConverterBinding;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

final class TypeConverterBindingProcessor extends AbstractProcessor {
   TypeConverterBindingProcessor(Errors errors) {
      super(errors);
   }

   static void prepareBuiltInConverters(InjectorImpl injector) {
      convertToPrimitiveType(injector, Integer.TYPE, Integer.class);
      convertToPrimitiveType(injector, Long.TYPE, Long.class);
      convertToPrimitiveType(injector, Boolean.TYPE, Boolean.class);
      convertToPrimitiveType(injector, Byte.TYPE, Byte.class);
      convertToPrimitiveType(injector, Short.TYPE, Short.class);
      convertToPrimitiveType(injector, Float.TYPE, Float.class);
      convertToPrimitiveType(injector, Double.TYPE, Double.class);
      convertToClass(injector, Character.class, new TypeConverter() {
         public Object convert(String value, TypeLiteral<?> toType) {
            value = value.trim();
            if (value.length() != 1) {
               throw new RuntimeException("Length != 1.");
            } else {
               return value.charAt(0);
            }
         }

         public String toString() {
            return "TypeConverter<Character>";
         }
      });
      convertToClasses(injector, Matchers.subclassesOf(Enum.class), new TypeConverter() {
         public Object convert(String value, TypeLiteral<?> toType) {
            return Enum.valueOf(toType.getRawType(), value);
         }

         public String toString() {
            return "TypeConverter<E extends Enum<E>>";
         }
      });
      internalConvertToTypes(injector, new AbstractMatcher<TypeLiteral<?>>() {
         public boolean matches(TypeLiteral<?> typeLiteral) {
            return typeLiteral.getRawType() == Class.class;
         }

         public String toString() {
            return "Class<?>";
         }
      }, new TypeConverter() {
         public Object convert(String value, TypeLiteral<?> toType) {
            try {
               return Class.forName(value);
            } catch (ClassNotFoundException var4) {
               throw new RuntimeException(var4.getMessage());
            }
         }

         public String toString() {
            return "TypeConverter<Class<?>>";
         }
      });
   }

   private static <T> void convertToPrimitiveType(InjectorImpl injector, Class<T> primitiveType, final Class<T> wrapperType) {
      try {
         String var10002 = String.valueOf(capitalize(primitiveType.getName()));
         String var10001;
         if (var10002.length() != 0) {
            var10001 = "parse".concat(var10002);
         } else {
            String var10003 = new String;
            var10001 = var10003;
            var10003.<init>("parse");
         }

         final Method parser = wrapperType.getMethod(var10001, String.class);
         TypeConverter typeConverter = new TypeConverter() {
            public Object convert(String value, TypeLiteral<?> toType) {
               try {
                  return parser.invoke((Object)null, value);
               } catch (IllegalAccessException var4) {
                  throw new AssertionError(var4);
               } catch (InvocationTargetException var5) {
                  throw new RuntimeException(var5.getTargetException().getMessage());
               }
            }

            public String toString() {
               String var1 = String.valueOf(String.valueOf(wrapperType.getSimpleName()));
               return (new StringBuilder(15 + var1.length())).append("TypeConverter<").append(var1).append(">").toString();
            }
         };
         convertToClass(injector, wrapperType, typeConverter);
      } catch (NoSuchMethodException var5) {
         throw new AssertionError(var5);
      }
   }

   private static <T> void convertToClass(InjectorImpl injector, Class<T> type, TypeConverter converter) {
      convertToClasses(injector, Matchers.identicalTo(type), converter);
   }

   private static void convertToClasses(InjectorImpl injector, final Matcher<? super Class<?>> typeMatcher, TypeConverter converter) {
      internalConvertToTypes(injector, new AbstractMatcher<TypeLiteral<?>>() {
         public boolean matches(TypeLiteral<?> typeLiteral) {
            Type type = typeLiteral.getType();
            if (!(type instanceof Class)) {
               return false;
            } else {
               Class<?> clazz = (Class)type;
               return typeMatcher.matches(clazz);
            }
         }

         public String toString() {
            return typeMatcher.toString();
         }
      }, converter);
   }

   private static void internalConvertToTypes(InjectorImpl injector, Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter converter) {
      injector.state.addConverter(new TypeConverterBinding(SourceProvider.UNKNOWN_SOURCE, typeMatcher, converter));
   }

   public Boolean visit(TypeConverterBinding command) {
      this.injector.state.addConverter(new TypeConverterBinding(command.getSource(), command.getTypeMatcher(), command.getTypeConverter()));
      return true;
   }

   private static String capitalize(String s) {
      if (s.length() == 0) {
         return s;
      } else {
         char first = s.charAt(0);
         char capitalized = Character.toUpperCase(first);
         String var10000;
         if (first == capitalized) {
            var10000 = s;
         } else {
            String var4 = String.valueOf(String.valueOf(s.substring(1)));
            var10000 = (new StringBuilder(1 + var4.length())).append(capitalized).append(var4).toString();
         }

         return var10000;
      }
   }
}
