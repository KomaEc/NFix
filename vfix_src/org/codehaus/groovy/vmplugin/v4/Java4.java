package org.codehaus.groovy.vmplugin.v4;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.vmplugin.VMPlugin;

public class Java4 implements VMPlugin {
   private static Class[] EMPTY_CLASS_ARRAY = new Class[0];

   public void setAdditionalClassInformation(ClassNode c) {
   }

   public Class[] getPluginDefaultGroovyMethods() {
      return EMPTY_CLASS_ARRAY;
   }

   public void configureAnnotation(AnnotationNode an) {
   }

   public void configureClassNode(CompileUnit compileUnit, ClassNode classNode) {
      Class clazz = classNode.getTypeClass();
      Field[] fields = clazz.getDeclaredFields();

      for(int i = 0; i < fields.length; ++i) {
         classNode.addField(fields[i].getName(), fields[i].getModifiers(), classNode, (Expression)null);
      }

      Method[] methods = clazz.getDeclaredMethods();

      for(int i = 0; i < methods.length; ++i) {
         Method m = methods[i];
         MethodNode mn = new MethodNode(m.getName(), m.getModifiers(), ClassHelper.make(m.getReturnType()), this.createParameters(m.getParameterTypes()), ClassHelper.make(m.getExceptionTypes()), (Statement)null);
         classNode.addMethod(mn);
      }

      Constructor[] constructors = clazz.getDeclaredConstructors();

      for(int i = 0; i < constructors.length; ++i) {
         Constructor ctor = constructors[i];
         classNode.addConstructor(ctor.getModifiers(), this.createParameters(ctor.getParameterTypes()), ClassHelper.make(ctor.getExceptionTypes()), (Statement)null);
      }

      Class sc = clazz.getSuperclass();
      if (sc != null) {
         classNode.setUnresolvedSuperClass(this.getPrimaryClassNode(compileUnit, sc));
      }

      this.buildInterfaceTypes(compileUnit, classNode, clazz);
   }

   private ClassNode getPrimaryClassNode(CompileUnit compileUnit, Class clazz) {
      ClassNode result = null;
      if (compileUnit != null) {
         result = compileUnit.getClass(clazz.getName());
      }

      if (result == null) {
         result = ClassHelper.make(clazz);
      }

      return result;
   }

   private void buildInterfaceTypes(CompileUnit compileUnit, ClassNode classNode, Class c) {
      Class[] interfaces = c.getInterfaces();
      ClassNode[] ret = new ClassNode[interfaces.length];

      for(int i = 0; i < interfaces.length; ++i) {
         ret[i] = this.getPrimaryClassNode(compileUnit, interfaces[i]);
      }

      classNode.setInterfaces(ret);
   }

   private Parameter[] createParameters(Class[] types) {
      Parameter[] parameters = Parameter.EMPTY_ARRAY;
      int size = types.length;
      if (size > 0) {
         parameters = new Parameter[size];

         for(int i = 0; i < size; ++i) {
            parameters[i] = this.createParameter(types[i], i);
         }
      }

      return parameters;
   }

   private Parameter createParameter(Class parameterType, int idx) {
      return new Parameter(ClassHelper.make(parameterType), "param" + idx);
   }
}
