package org.codehaus.groovy.classgen;

import groovyjarjarasm.asm.Label;
import groovyjarjarasm.asm.MethodVisitor;
import groovyjarjarasm.asm.Opcodes;
import groovyjarjarasm.asm.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class BytecodeHelper implements Opcodes {
   private MethodVisitor mv;

   public MethodVisitor getMethodVisitor() {
      return this.mv;
   }

   public BytecodeHelper(MethodVisitor mv) {
      this.mv = mv;
   }

   public void quickBoxIfNecessary(ClassNode type) {
      String descr = getTypeDescription(type);
      if (type == ClassHelper.boolean_TYPE) {
         this.boxBoolean();
      } else if (ClassHelper.isPrimitiveType(type) && type != ClassHelper.VOID_TYPE) {
         ClassNode wrapper = ClassHelper.getWrapper(type);
         String internName = getClassInternalName(wrapper);
         this.mv.visitTypeInsn(187, internName);
         this.mv.visitInsn(89);
         if (type != ClassHelper.double_TYPE && type != ClassHelper.long_TYPE) {
            this.mv.visitInsn(93);
            this.mv.visitInsn(88);
         } else {
            this.mv.visitInsn(94);
            this.mv.visitInsn(88);
         }

         this.mv.visitMethodInsn(183, internName, "<init>", "(" + descr + ")V");
      }

   }

   public void quickUnboxIfNecessary(ClassNode type) {
      if (ClassHelper.isPrimitiveType(type) && type != ClassHelper.VOID_TYPE) {
         ClassNode wrapper = ClassHelper.getWrapper(type);
         String internName = getClassInternalName(wrapper);
         if (type == ClassHelper.boolean_TYPE) {
            this.mv.visitTypeInsn(192, internName);
            this.mv.visitMethodInsn(182, internName, type.getName() + "Value", "()" + getTypeDescription(type));
         } else {
            this.mv.visitTypeInsn(192, "java/lang/Number");
            this.mv.visitMethodInsn(182, "java/lang/Number", type.getName() + "Value", "()" + getTypeDescription(type));
         }
      }

   }

   public void box(Class type) {
      if (ReflectionCache.getCachedClass(type).isPrimitive && type != Void.TYPE) {
         String returnString = "(" + getTypeDescription(type) + ")Ljava/lang/Object;";
         this.mv.visitMethodInsn(184, getClassInternalName(DefaultTypeTransformation.class.getName()), "box", returnString);
      }

   }

   public void box(ClassNode type) {
      if (!type.isPrimaryClassNode()) {
         this.box(type.getTypeClass());
      }
   }

   public void unbox(Class type) {
      if (type.isPrimitive() && type != Void.TYPE) {
         String returnString = "(Ljava/lang/Object;)" + getTypeDescription(type);
         this.mv.visitMethodInsn(184, getClassInternalName(DefaultTypeTransformation.class.getName()), type.getName() + "Unbox", returnString);
      }

   }

   public void unbox(ClassNode type) {
      if (!type.isPrimaryClassNode()) {
         this.unbox(type.getTypeClass());
      }
   }

   public static String getClassInternalName(ClassNode t) {
      return t.isPrimaryClassNode() ? getClassInternalName(t.getName()) : getClassInternalName(t.getTypeClass());
   }

   public static String getClassInternalName(Class t) {
      return Type.getInternalName(t);
   }

   public static String getClassInternalName(String name) {
      return name.replace('.', '/');
   }

   public static String getMethodDescriptor(ClassNode returnType, Parameter[] parameters) {
      StringBuffer buffer = new StringBuffer("(");

      for(int i = 0; i < parameters.length; ++i) {
         buffer.append(getTypeDescription(parameters[i].getType()));
      }

      buffer.append(")");
      buffer.append(getTypeDescription(returnType));
      return buffer.toString();
   }

   public static String getMethodDescriptor(Class returnType, Class[] paramTypes) {
      StringBuffer buffer = new StringBuffer("(");

      for(int i = 0; i < paramTypes.length; ++i) {
         buffer.append(getTypeDescription(paramTypes[i]));
      }

      buffer.append(")");
      buffer.append(getTypeDescription(returnType));
      return buffer.toString();
   }

   public static String getTypeDescription(Class c) {
      return Type.getDescriptor(c);
   }

   public static String getClassLoadingTypeDescription(ClassNode c) {
      StringBuffer buf = new StringBuffer();

      boolean array;
      for(array = false; c.isArray(); array = true) {
         buf.append('[');
         c = c.getComponentType();
      }

      if (ClassHelper.isPrimitiveType(c)) {
         buf.append(getTypeDescription(c));
      } else {
         if (array) {
            buf.append('L');
         }

         buf.append(c.getName());
         if (array) {
            buf.append(';');
         }
      }

      return buf.toString();
   }

   public static String getTypeDescription(ClassNode c) {
      return getTypeDescription(c, true);
   }

   private static String getTypeDescription(ClassNode c, boolean end) {
      StringBuffer buf = new StringBuffer();

      ClassNode d;
      for(d = c; !ClassHelper.isPrimitiveType(d); d = d.getComponentType()) {
         if (!d.isArray()) {
            buf.append('L');
            String name = d.getName();
            int len = name.length();

            for(int i = 0; i < len; ++i) {
               char car = name.charAt(i);
               buf.append(car == '.' ? '/' : car);
            }

            if (end) {
               buf.append(';');
            }

            return buf.toString();
         }

         buf.append('[');
      }

      char car;
      if (d == ClassHelper.int_TYPE) {
         car = 'I';
      } else if (d == ClassHelper.VOID_TYPE) {
         car = 'V';
      } else if (d == ClassHelper.boolean_TYPE) {
         car = 'Z';
      } else if (d == ClassHelper.byte_TYPE) {
         car = 'B';
      } else if (d == ClassHelper.char_TYPE) {
         car = 'C';
      } else if (d == ClassHelper.short_TYPE) {
         car = 'S';
      } else if (d == ClassHelper.double_TYPE) {
         car = 'D';
      } else if (d == ClassHelper.float_TYPE) {
         car = 'F';
      } else {
         car = 'J';
      }

      buf.append(car);
      return buf.toString();
   }

   public static String[] getClassInternalNames(ClassNode[] names) {
      int size = names.length;
      String[] answer = new String[size];

      for(int i = 0; i < size; ++i) {
         answer[i] = getClassInternalName(names[i]);
      }

      return answer;
   }

   protected void pushConstant(boolean value) {
      if (value) {
         this.mv.visitInsn(4);
      } else {
         this.mv.visitInsn(3);
      }

   }

   public void pushConstant(int value) {
      switch(value) {
      case 0:
         this.mv.visitInsn(3);
         break;
      case 1:
         this.mv.visitInsn(4);
         break;
      case 2:
         this.mv.visitInsn(5);
         break;
      case 3:
         this.mv.visitInsn(6);
         break;
      case 4:
         this.mv.visitInsn(7);
         break;
      case 5:
         this.mv.visitInsn(8);
         break;
      default:
         if (value >= -128 && value <= 127) {
            this.mv.visitIntInsn(16, value);
         } else if (value >= -32768 && value <= 32767) {
            this.mv.visitIntInsn(17, value);
         } else {
            this.mv.visitLdcInsn(value);
         }
      }

   }

   public void doCast(Class type) {
      if (type != Object.class) {
         if (type.isPrimitive() && type != Void.TYPE) {
            this.unbox(type);
         } else {
            this.mv.visitTypeInsn(192, type.isArray() ? getTypeDescription(type) : getClassInternalName(type.getName()));
         }
      }

   }

   public void doCast(ClassNode type) {
      if (type != ClassHelper.OBJECT_TYPE) {
         if (ClassHelper.isPrimitiveType(type) && type != ClassHelper.VOID_TYPE) {
            this.unbox(type);
         } else {
            this.mv.visitTypeInsn(192, type.isArray() ? getTypeDescription(type) : getClassInternalName(type));
         }

      }
   }

   public void load(ClassNode type, int idx) {
      if (type == ClassHelper.double_TYPE) {
         this.mv.visitVarInsn(24, idx);
      } else if (type == ClassHelper.float_TYPE) {
         this.mv.visitVarInsn(23, idx);
      } else if (type == ClassHelper.long_TYPE) {
         this.mv.visitVarInsn(22, idx);
      } else if (type != ClassHelper.boolean_TYPE && type != ClassHelper.char_TYPE && type != ClassHelper.byte_TYPE && type != ClassHelper.int_TYPE && type != ClassHelper.short_TYPE) {
         this.mv.visitVarInsn(25, idx);
      } else {
         this.mv.visitVarInsn(21, idx);
      }

   }

   public void load(Variable v) {
      this.load(v.getType(), v.getIndex());
   }

   public void store(Variable v, boolean markStart) {
      ClassNode type = v.getType();
      this.unbox(type);
      int idx = v.getIndex();
      if (type == ClassHelper.double_TYPE) {
         this.mv.visitVarInsn(57, idx);
      } else if (type == ClassHelper.float_TYPE) {
         this.mv.visitVarInsn(56, idx);
      } else if (type == ClassHelper.long_TYPE) {
         this.mv.visitVarInsn(55, idx);
      } else if (type != ClassHelper.boolean_TYPE && type != ClassHelper.char_TYPE && type != ClassHelper.byte_TYPE && type != ClassHelper.int_TYPE && type != ClassHelper.short_TYPE) {
         this.mv.visitVarInsn(58, idx);
      } else {
         this.mv.visitVarInsn(54, idx);
      }

   }

   public void store(Variable v) {
      this.store(v, false);
   }

   void loadConstant(Object value) {
      if (value == null) {
         this.mv.visitInsn(1);
      } else if (value instanceof String) {
         this.mv.visitLdcInsn(value);
      } else {
         String className;
         if (value instanceof Character) {
            String className = "java/lang/Character";
            this.mv.visitTypeInsn(187, className);
            this.mv.visitInsn(89);
            this.mv.visitLdcInsn(value);
            className = "(C)V";
            this.mv.visitMethodInsn(183, className, "<init>", className);
         } else if (value instanceof Number) {
            Number n = (Number)value;
            className = getClassInternalName(value.getClass().getName());
            if (!(n instanceof BigDecimal) && !(n instanceof BigInteger)) {
               String methodType;
               if (n instanceof Integer) {
                  this.mv.visitLdcInsn(n);
                  methodType = "(I)Ljava/lang/Integer;";
               } else if (n instanceof Double) {
                  this.mv.visitLdcInsn(n);
                  methodType = "(D)Ljava/lang/Double;";
               } else if (n instanceof Float) {
                  this.mv.visitLdcInsn(n);
                  methodType = "(F)Ljava/lang/Float;";
               } else if (n instanceof Long) {
                  this.mv.visitLdcInsn(n);
                  methodType = "(J)Ljava/lang/Long;";
               } else if (n instanceof Short) {
                  this.mv.visitLdcInsn(n);
                  methodType = "(S)Ljava/lang/Short;";
               } else {
                  if (!(n instanceof Byte)) {
                     throw new ClassGeneratorException("Cannot generate bytecode for constant: " + value + " of type: " + value.getClass().getName() + ".  Numeric constant type not supported.");
                  }

                  this.mv.visitLdcInsn(n);
                  methodType = "(B)Ljava/lang/Byte;";
               }

               this.mv.visitMethodInsn(184, className, "valueOf", methodType);
            } else {
               this.mv.visitTypeInsn(187, className);
               this.mv.visitInsn(89);
               this.mv.visitLdcInsn(n.toString());
               this.mv.visitMethodInsn(183, className, "<init>", "(Ljava/lang/String;)V");
            }
         } else if (value instanceof Boolean) {
            Boolean bool = (Boolean)value;
            className = bool ? "TRUE" : "FALSE";
            this.mv.visitFieldInsn(178, "java/lang/Boolean", className, "Ljava/lang/Boolean;");
         } else {
            if (!(value instanceof Class)) {
               throw new ClassGeneratorException("Cannot generate bytecode for constant: " + value + " of type: " + value.getClass().getName());
            }

            Class vc = (Class)value;
            if (!vc.getName().equals("java.lang.Void")) {
               throw new ClassGeneratorException("Cannot generate bytecode for constant: " + value + " of type: " + value.getClass().getName());
            }
         }
      }

   }

   /** @deprecated */
   public void loadVar(Variable variable) {
      int index = variable.getIndex();
      if (variable.isHolder()) {
         this.mv.visitVarInsn(25, index);
         this.mv.visitMethodInsn(182, "groovy/lang/Reference", "get", "()Ljava/lang/Object;");
      } else {
         this.load(variable);
         if (variable != Variable.THIS_VARIABLE && variable != Variable.SUPER_VARIABLE) {
            this.box(variable.getType());
         }
      }

   }

   public void loadVar(Variable variable, boolean useReferenceDirectly) {
      int index = variable.getIndex();
      if (variable.isHolder()) {
         this.mv.visitVarInsn(25, index);
         if (!useReferenceDirectly) {
            this.mv.visitMethodInsn(182, "groovy/lang/Reference", "get", "()Ljava/lang/Object;");
         }
      } else {
         this.load(variable);
         if (variable != Variable.THIS_VARIABLE && variable != Variable.SUPER_VARIABLE) {
            this.box(variable.getType());
         }
      }

   }

   public void storeVar(Variable variable) {
      int index = variable.getIndex();
      if (variable.isHolder()) {
         this.mv.visitVarInsn(25, index);
         this.mv.visitInsn(95);
         this.mv.visitMethodInsn(182, "groovy/lang/Reference", "set", "(Ljava/lang/Object;)V");
      } else {
         this.store(variable, false);
      }

   }

   public void putField(FieldNode fld) {
      this.putField(fld, getClassInternalName(fld.getOwner()));
   }

   public void putField(FieldNode fld, String ownerName) {
      this.mv.visitFieldInsn(181, ownerName, fld.getName(), getTypeDescription(fld.getType()));
   }

   public void swapObjectWith(ClassNode type) {
      if (type != ClassHelper.long_TYPE && type != ClassHelper.double_TYPE) {
         this.mv.visitInsn(95);
      } else {
         this.mv.visitInsn(91);
         this.mv.visitInsn(87);
      }

   }

   public void swapWithObject(ClassNode type) {
      if (type != ClassHelper.long_TYPE && type != ClassHelper.double_TYPE) {
         this.mv.visitInsn(95);
      } else {
         this.mv.visitInsn(93);
         this.mv.visitInsn(88);
      }

   }

   public static ClassNode boxOnPrimitive(ClassNode type) {
      return !type.isArray() ? ClassHelper.getWrapper(type) : boxOnPrimitive(type.getComponentType()).makeArray();
   }

   public void boxBoolean() {
      Label l0 = new Label();
      this.mv.visitJumpInsn(153, l0);
      this.mv.visitFieldInsn(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
      Label l1 = new Label();
      this.mv.visitJumpInsn(167, l1);
      this.mv.visitLabel(l0);
      this.mv.visitFieldInsn(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
      this.mv.visitLabel(l1);
   }

   public void negateBoolean() {
      Label endLabel = new Label();
      Label falseLabel = new Label();
      this.mv.visitJumpInsn(154, falseLabel);
      this.mv.visitInsn(4);
      this.mv.visitJumpInsn(167, endLabel);
      this.mv.visitLabel(falseLabel);
      this.mv.visitInsn(3);
      this.mv.visitLabel(endLabel);
   }

   public void mark(String msg) {
      this.mv.visitLdcInsn(msg);
      this.mv.visitInsn(87);
   }

   public static String formatNameForClassLoading(String name) {
      if (!name.equals("int") && !name.equals("long") && !name.equals("short") && !name.equals("float") && !name.equals("double") && !name.equals("byte") && !name.equals("char") && !name.equals("boolean") && !name.equals("void")) {
         if (name == null) {
            return "java.lang.Object;";
         } else if (name.startsWith("[")) {
            return name.replace('/', '.');
         } else if (name.startsWith("L")) {
            name = name.substring(1);
            if (name.endsWith(";")) {
               name = name.substring(0, name.length() - 1);
            }

            return name.replace('/', '.');
         } else {
            String prefix = "";
            if (name.endsWith("[]")) {
               prefix = "[";
               name = name.substring(0, name.length() - 2);
               if (name.equals("int")) {
                  return prefix + "I";
               } else if (name.equals("long")) {
                  return prefix + "J";
               } else if (name.equals("short")) {
                  return prefix + "S";
               } else if (name.equals("float")) {
                  return prefix + "F";
               } else if (name.equals("double")) {
                  return prefix + "D";
               } else if (name.equals("byte")) {
                  return prefix + "B";
               } else if (name.equals("char")) {
                  return prefix + "C";
               } else {
                  return name.equals("boolean") ? prefix + "Z" : prefix + "L" + name.replace('/', '.') + ";";
               }
            } else {
               return name.replace('/', '.');
            }
         }
      } else {
         return name;
      }
   }

   public void dup() {
      this.mv.visitInsn(89);
   }

   public void doReturn(ClassNode returnType) {
      if (returnType == ClassHelper.double_TYPE) {
         this.mv.visitInsn(175);
      } else if (returnType == ClassHelper.float_TYPE) {
         this.mv.visitInsn(174);
      } else if (returnType == ClassHelper.long_TYPE) {
         this.mv.visitInsn(173);
      } else if (returnType != ClassHelper.boolean_TYPE && returnType != ClassHelper.char_TYPE && returnType != ClassHelper.byte_TYPE && returnType != ClassHelper.int_TYPE && returnType != ClassHelper.short_TYPE) {
         if (returnType == ClassHelper.VOID_TYPE) {
            this.mv.visitInsn(177);
         } else {
            this.mv.visitInsn(176);
         }
      } else {
         this.mv.visitInsn(172);
      }

   }

   private static boolean hasGenerics(Parameter[] param) {
      if (param.length == 0) {
         return false;
      } else {
         for(int i = 0; i < param.length; ++i) {
            ClassNode type = param[i].getType();
            if (hasGenerics(type)) {
               return true;
            }
         }

         return false;
      }
   }

   private static boolean hasGenerics(ClassNode type) {
      return type.isArray() ? hasGenerics(type.getComponentType()) : type.getGenericsTypes() != null;
   }

   public static String getGenericsMethodSignature(MethodNode node) {
      GenericsType[] generics = node.getGenericsTypes();
      Parameter[] param = node.getParameters();
      ClassNode returnType = node.getReturnType();
      if (generics == null && !hasGenerics(param) && !hasGenerics(returnType)) {
         return null;
      } else {
         StringBuffer ret = new StringBuffer(100);
         getGenericsTypeSpec(ret, generics);
         GenericsType[] paramTypes = new GenericsType[param.length];

         for(int i = 0; i < param.length; ++i) {
            ClassNode pType = param[i].getType();
            if (pType.getGenericsTypes() != null && pType.isGenericsPlaceHolder()) {
               paramTypes[i] = pType.getGenericsTypes()[0];
            } else {
               paramTypes[i] = new GenericsType(pType);
            }
         }

         addSubTypes(ret, paramTypes, "(", ")");
         addSubTypes(ret, new GenericsType[]{new GenericsType(returnType)}, "", "");
         return ret.toString();
      }
   }

   private static boolean usesGenericsInClassSignature(ClassNode node) {
      if (!node.isUsingGenerics()) {
         return false;
      } else if (hasGenerics(node)) {
         return true;
      } else {
         ClassNode sclass = node.getUnresolvedSuperClass(false);
         if (sclass.isUsingGenerics()) {
            return true;
         } else {
            ClassNode[] interfaces = node.getInterfaces();
            if (interfaces != null) {
               for(int i = 0; i < interfaces.length; ++i) {
                  if (interfaces[i].isUsingGenerics()) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   public static String getGenericsSignature(ClassNode node) {
      if (!usesGenericsInClassSignature(node)) {
         return null;
      } else {
         GenericsType[] genericsTypes = node.getGenericsTypes();
         StringBuffer ret = new StringBuffer(100);
         getGenericsTypeSpec(ret, genericsTypes);
         GenericsType extendsPart = new GenericsType(node.getUnresolvedSuperClass(false));
         writeGenericsBounds(ret, extendsPart, true);
         ClassNode[] interfaces = node.getInterfaces();

         for(int i = 0; i < interfaces.length; ++i) {
            GenericsType interfacePart = new GenericsType(interfaces[i]);
            writeGenericsBounds(ret, interfacePart, false);
         }

         return ret.toString();
      }
   }

   private static void getGenericsTypeSpec(StringBuffer ret, GenericsType[] genericsTypes) {
      if (genericsTypes != null) {
         ret.append('<');

         for(int i = 0; i < genericsTypes.length; ++i) {
            String name = genericsTypes[i].getName();
            ret.append(name);
            ret.append(':');
            writeGenericsBounds(ret, genericsTypes[i], true);
         }

         ret.append('>');
      }
   }

   public static String getGenericsBounds(ClassNode type) {
      GenericsType[] genericsTypes = type.getGenericsTypes();
      if (genericsTypes == null) {
         return null;
      } else {
         StringBuffer ret = new StringBuffer(100);
         if (type.isGenericsPlaceHolder()) {
            addSubTypes(ret, type.getGenericsTypes(), "", "");
         } else {
            GenericsType gt = new GenericsType(type);
            writeGenericsBounds(ret, gt, false);
         }

         return ret.toString();
      }
   }

   private static void writeGenericsBoundType(StringBuffer ret, ClassNode printType, boolean writeInterfaceMarker) {
      if (writeInterfaceMarker && printType.isInterface()) {
         ret.append(":");
      }

      if (printType.equals(ClassHelper.OBJECT_TYPE) && printType.getGenericsTypes() != null) {
         ret.append("T");
         ret.append(printType.getGenericsTypes()[0].getName());
         ret.append(";");
      } else {
         ret.append(getTypeDescription(printType, false));
         addSubTypes(ret, printType.getGenericsTypes(), "<", ">");
         if (!ClassHelper.isPrimitiveType(printType)) {
            ret.append(";");
         }
      }

   }

   private static void writeGenericsBounds(StringBuffer ret, GenericsType type, boolean writeInterfaceMarker) {
      if (type.getUpperBounds() != null) {
         ClassNode[] bounds = type.getUpperBounds();

         for(int i = 0; i < bounds.length; ++i) {
            writeGenericsBoundType(ret, bounds[i], writeInterfaceMarker);
         }
      } else if (type.getLowerBound() != null) {
         writeGenericsBoundType(ret, type.getLowerBound(), writeInterfaceMarker);
      } else {
         writeGenericsBoundType(ret, type.getType(), writeInterfaceMarker);
      }

   }

   private static void addSubTypes(StringBuffer ret, GenericsType[] types, String start, String end) {
      if (types != null) {
         ret.append(start);

         for(int i = 0; i < types.length; ++i) {
            if (types[i].getType().isArray()) {
               ret.append("[");
               addSubTypes(ret, new GenericsType[]{new GenericsType(types[i].getType().getComponentType())}, "", "");
            } else if (types[i].isPlaceholder()) {
               ret.append('T');
               String name = types[i].getName();
               ret.append(name);
               ret.append(';');
            } else if (types[i].isWildcard()) {
               if (types[i].getUpperBounds() != null) {
                  ret.append('+');
                  writeGenericsBounds(ret, types[i], false);
               } else if (types[i].getLowerBound() != null) {
                  ret.append('-');
                  writeGenericsBounds(ret, types[i], false);
               } else {
                  ret.append('*');
               }
            } else {
               writeGenericsBounds(ret, types[i], false);
            }
         }

         ret.append(end);
      }
   }
}
