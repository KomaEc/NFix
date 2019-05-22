package org.codehaus.groovy.ast;

import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.MetaClass;
import groovy.lang.Range;
import groovy.lang.Reference;
import groovy.lang.Script;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.vmplugin.VMPluginFactory;

public class ClassHelper {
   private static final Class[] classes;
   private static final String[] primitiveClassNames;
   public static final ClassNode DYNAMIC_TYPE;
   public static final ClassNode OBJECT_TYPE;
   public static final ClassNode VOID_TYPE;
   public static final ClassNode CLOSURE_TYPE;
   public static final ClassNode GSTRING_TYPE;
   public static final ClassNode LIST_TYPE;
   public static final ClassNode MAP_TYPE;
   public static final ClassNode RANGE_TYPE;
   public static final ClassNode PATTERN_TYPE;
   public static final ClassNode STRING_TYPE;
   public static final ClassNode SCRIPT_TYPE;
   public static final ClassNode REFERENCE_TYPE;
   public static final ClassNode boolean_TYPE;
   public static final ClassNode char_TYPE;
   public static final ClassNode byte_TYPE;
   public static final ClassNode int_TYPE;
   public static final ClassNode long_TYPE;
   public static final ClassNode short_TYPE;
   public static final ClassNode double_TYPE;
   public static final ClassNode float_TYPE;
   public static final ClassNode Byte_TYPE;
   public static final ClassNode Short_TYPE;
   public static final ClassNode Integer_TYPE;
   public static final ClassNode Long_TYPE;
   public static final ClassNode Character_TYPE;
   public static final ClassNode Float_TYPE;
   public static final ClassNode Double_TYPE;
   public static final ClassNode Boolean_TYPE;
   public static final ClassNode BigInteger_TYPE;
   public static final ClassNode BigDecimal_TYPE;
   public static final ClassNode void_WRAPPER_TYPE;
   public static final ClassNode CLASS_Type;
   public static final ClassNode METACLASS_TYPE;
   public static final ClassNode GENERATED_CLOSURE_Type;
   public static final ClassNode Enum_Type;
   public static final ClassNode Annotation_TYPE;
   public static final ClassNode ELEMENT_TYPE_TYPE;
   private static ClassNode[] types;
   private static ClassNode[] numbers;
   protected static final ClassNode[] EMPTY_TYPE_ARRAY;
   public static final String OBJECT = "java.lang.Object";

   public static ClassNode makeCached(Class c) {
      SoftReference<ClassNode> classNodeSoftReference = (SoftReference)ClassHelper.ClassHelperCache.classCache.get(c);
      ClassNode classNode;
      if (classNodeSoftReference == null || (classNode = (ClassNode)classNodeSoftReference.get()) == null) {
         classNode = new ClassNode(c);
         ClassHelper.ClassHelperCache.classCache.put(c, new SoftReference(classNode));
         VMPluginFactory.getPlugin().setAdditionalClassInformation(classNode);
      }

      return classNode;
   }

   public static ClassNode[] make(Class[] classes) {
      ClassNode[] cns = new ClassNode[classes.length];

      for(int i = 0; i < cns.length; ++i) {
         cns[i] = make(classes[i]);
      }

      return cns;
   }

   public static ClassNode make(Class c) {
      return make(c, true);
   }

   public static ClassNode make(Class c, boolean includeGenerics) {
      for(int i = 0; i < classes.length; ++i) {
         if (c == classes[i]) {
            return types[i];
         }
      }

      if (c.isArray()) {
         ClassNode cn = make(c.getComponentType(), includeGenerics);
         return cn.makeArray();
      } else {
         return makeWithoutCaching(c, includeGenerics);
      }
   }

   public static ClassNode makeWithoutCaching(Class c) {
      return makeWithoutCaching(c, true);
   }

   public static ClassNode makeWithoutCaching(Class c, boolean includeGenerics) {
      ClassNode cached;
      if (c.isArray()) {
         cached = makeWithoutCaching(c.getComponentType(), includeGenerics);
         return cached.makeArray();
      } else {
         cached = makeCached(c);
         if (includeGenerics) {
            return cached;
         } else {
            ClassNode t = makeWithoutCaching(c.getName());
            t.setRedirect(cached);
            return t;
         }
      }
   }

   public static ClassNode makeWithoutCaching(String name) {
      ClassNode cn = new ClassNode(name, 1, OBJECT_TYPE);
      cn.isPrimaryNode = false;
      return cn;
   }

   public static ClassNode make(String name) {
      if (name != null && name.length() != 0) {
         int i;
         for(i = 0; i < primitiveClassNames.length; ++i) {
            if (primitiveClassNames[i].equals(name)) {
               return types[i];
            }
         }

         for(i = 0; i < classes.length; ++i) {
            String cname = classes[i].getName();
            if (name.equals(cname)) {
               return types[i];
            }
         }

         return makeWithoutCaching(name);
      } else {
         return DYNAMIC_TYPE;
      }
   }

   public static ClassNode getWrapper(ClassNode cn) {
      cn = cn.redirect();
      if (!isPrimitiveType(cn)) {
         return cn;
      } else if (cn == boolean_TYPE) {
         return Boolean_TYPE;
      } else if (cn == byte_TYPE) {
         return Byte_TYPE;
      } else if (cn == char_TYPE) {
         return Character_TYPE;
      } else if (cn == short_TYPE) {
         return Short_TYPE;
      } else if (cn == int_TYPE) {
         return Integer_TYPE;
      } else if (cn == long_TYPE) {
         return Long_TYPE;
      } else if (cn == float_TYPE) {
         return Float_TYPE;
      } else if (cn == double_TYPE) {
         return Double_TYPE;
      } else {
         return cn == VOID_TYPE ? void_WRAPPER_TYPE : cn;
      }
   }

   public static ClassNode getUnwrapper(ClassNode cn) {
      cn = cn.redirect();
      if (isPrimitiveType(cn)) {
         return cn;
      } else if (cn == Boolean_TYPE) {
         return boolean_TYPE;
      } else if (cn == Byte_TYPE) {
         return byte_TYPE;
      } else if (cn == Character_TYPE) {
         return char_TYPE;
      } else if (cn == Short_TYPE) {
         return short_TYPE;
      } else if (cn == Integer_TYPE) {
         return int_TYPE;
      } else if (cn == Long_TYPE) {
         return long_TYPE;
      } else if (cn == Float_TYPE) {
         return float_TYPE;
      } else {
         return cn == Double_TYPE ? double_TYPE : cn;
      }
   }

   public static boolean isPrimitiveType(ClassNode cn) {
      return cn == boolean_TYPE || cn == char_TYPE || cn == byte_TYPE || cn == short_TYPE || cn == int_TYPE || cn == long_TYPE || cn == float_TYPE || cn == double_TYPE || cn == VOID_TYPE;
   }

   public static boolean isNumberType(ClassNode cn) {
      return cn == Byte_TYPE || cn == Short_TYPE || cn == Integer_TYPE || cn == Long_TYPE || cn == Float_TYPE || cn == Double_TYPE || cn == byte_TYPE || cn == short_TYPE || cn == int_TYPE || cn == long_TYPE || cn == float_TYPE || cn == double_TYPE;
   }

   public static ClassNode makeReference() {
      return make(Reference.class);
   }

   public static boolean isCachedType(ClassNode type) {
      ClassNode[] arr$ = types;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ClassNode cachedType = arr$[i$];
         if (cachedType == type) {
            return true;
         }
      }

      return false;
   }

   static {
      classes = new Class[]{Object.class, Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Double.TYPE, Float.TYPE, Void.TYPE, Closure.class, GString.class, List.class, Map.class, Range.class, Pattern.class, Script.class, String.class, Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Double.class, Float.class, BigDecimal.class, BigInteger.class, Void.class, Reference.class, Class.class, MetaClass.class};
      primitiveClassNames = new String[]{"", "boolean", "char", "byte", "short", "int", "long", "double", "float", "void"};
      DYNAMIC_TYPE = makeCached(Object.class);
      OBJECT_TYPE = DYNAMIC_TYPE;
      VOID_TYPE = makeCached(Void.TYPE);
      CLOSURE_TYPE = makeCached(Closure.class);
      GSTRING_TYPE = makeCached(GString.class);
      LIST_TYPE = makeWithoutCaching(List.class);
      MAP_TYPE = makeWithoutCaching(Map.class);
      RANGE_TYPE = makeCached(Range.class);
      PATTERN_TYPE = makeCached(Pattern.class);
      STRING_TYPE = makeCached(String.class);
      SCRIPT_TYPE = makeCached(Script.class);
      REFERENCE_TYPE = makeWithoutCaching(Reference.class);
      boolean_TYPE = makeCached(Boolean.TYPE);
      char_TYPE = makeCached(Character.TYPE);
      byte_TYPE = makeCached(Byte.TYPE);
      int_TYPE = makeCached(Integer.TYPE);
      long_TYPE = makeCached(Long.TYPE);
      short_TYPE = makeCached(Short.TYPE);
      double_TYPE = makeCached(Double.TYPE);
      float_TYPE = makeCached(Float.TYPE);
      Byte_TYPE = makeCached(Byte.class);
      Short_TYPE = makeCached(Short.class);
      Integer_TYPE = makeCached(Integer.class);
      Long_TYPE = makeCached(Long.class);
      Character_TYPE = makeCached(Character.class);
      Float_TYPE = makeCached(Float.class);
      Double_TYPE = makeCached(Double.class);
      Boolean_TYPE = makeCached(Boolean.class);
      BigInteger_TYPE = makeCached(BigInteger.class);
      BigDecimal_TYPE = makeCached(BigDecimal.class);
      void_WRAPPER_TYPE = makeCached(Void.class);
      CLASS_Type = makeWithoutCaching(Class.class);
      METACLASS_TYPE = makeCached(MetaClass.class);
      GENERATED_CLOSURE_Type = makeCached(GeneratedClosure.class);
      Enum_Type = new ClassNode("java.lang.Enum", 0, OBJECT_TYPE);
      Annotation_TYPE = new ClassNode("java.lang.annotation.Annotation", 0, OBJECT_TYPE);
      ELEMENT_TYPE_TYPE = new ClassNode("java.lang.annotation.ElementType", 0, Enum_Type);
      Enum_Type.isPrimaryNode = false;
      Annotation_TYPE.isPrimaryNode = false;
      types = new ClassNode[]{OBJECT_TYPE, boolean_TYPE, char_TYPE, byte_TYPE, short_TYPE, int_TYPE, long_TYPE, double_TYPE, float_TYPE, VOID_TYPE, CLOSURE_TYPE, GSTRING_TYPE, LIST_TYPE, MAP_TYPE, RANGE_TYPE, PATTERN_TYPE, SCRIPT_TYPE, STRING_TYPE, Boolean_TYPE, Character_TYPE, Byte_TYPE, Short_TYPE, Integer_TYPE, Long_TYPE, Double_TYPE, Float_TYPE, BigDecimal_TYPE, BigInteger_TYPE, void_WRAPPER_TYPE, REFERENCE_TYPE, CLASS_Type, METACLASS_TYPE, GENERATED_CLOSURE_Type, Enum_Type, Annotation_TYPE};
      numbers = new ClassNode[]{char_TYPE, byte_TYPE, short_TYPE, int_TYPE, long_TYPE, double_TYPE, float_TYPE, Short_TYPE, Byte_TYPE, Character_TYPE, Integer_TYPE, Float_TYPE, Long_TYPE, Double_TYPE, BigInteger_TYPE, BigDecimal_TYPE};
      EMPTY_TYPE_ARRAY = new ClassNode[0];
   }

   static class ClassHelperCache {
      static Map<Class, SoftReference<ClassNode>> classCache = new WeakHashMap();
   }
}
