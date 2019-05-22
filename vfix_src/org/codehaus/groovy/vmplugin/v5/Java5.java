package org.codehaus.groovy.vmplugin.v5;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.PackageNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.vmplugin.VMPlugin;

public class Java5 implements VMPlugin {
   private static final Class[] PLUGIN_DGM = new Class[]{PluginDefaultGroovyMethods.class};

   public void setAdditionalClassInformation(ClassNode cn) {
      this.setGenericsTypes(cn);
   }

   private void setGenericsTypes(ClassNode cn) {
      TypeVariable[] tvs = cn.getTypeClass().getTypeParameters();
      GenericsType[] gts = this.configureTypeVariable(tvs);
      cn.setGenericsTypes(gts);
   }

   private GenericsType[] configureTypeVariable(TypeVariable[] tvs) {
      if (tvs.length == 0) {
         return null;
      } else {
         GenericsType[] gts = new GenericsType[tvs.length];

         for(int i = 0; i < tvs.length; ++i) {
            gts[i] = this.configureTypeVariableDefinition(tvs[i]);
         }

         return gts;
      }
   }

   private GenericsType configureTypeVariableDefinition(TypeVariable tv) {
      ClassNode base = this.configureTypeVariableReference(tv);
      ClassNode redirect = base.redirect();
      base.setRedirect((ClassNode)null);
      Type[] tBounds = tv.getBounds();
      GenericsType gt;
      if (tBounds.length == 0) {
         gt = new GenericsType(base);
      } else {
         ClassNode[] cBounds = this.configureTypes(tBounds);
         gt = new GenericsType(base, cBounds, (ClassNode)null);
         gt.setName(base.getName());
         gt.setPlaceholder(true);
      }

      base.setRedirect(redirect);
      return gt;
   }

   private ClassNode[] configureTypes(Type[] types) {
      if (types.length == 0) {
         return null;
      } else {
         ClassNode[] nodes = new ClassNode[types.length];

         for(int i = 0; i < types.length; ++i) {
            nodes[i] = this.configureType(types[i]);
         }

         return nodes;
      }
   }

   private ClassNode configureType(Type type) {
      if (type instanceof WildcardType) {
         return this.configureWildcardType((WildcardType)type);
      } else if (type instanceof ParameterizedType) {
         return this.configureParameterizedType((ParameterizedType)type);
      } else if (type instanceof GenericArrayType) {
         return this.configureGenericArray((GenericArrayType)type);
      } else if (type instanceof TypeVariable) {
         return this.configureTypeVariableReference((TypeVariable)type);
      } else if (type instanceof Class) {
         return this.configureClass((Class)type);
      } else {
         throw new GroovyBugError("unknown type: " + type + " := " + type.getClass());
      }
   }

   private ClassNode configureClass(Class c) {
      return c.isPrimitive() ? ClassHelper.make(c) : ClassHelper.makeWithoutCaching(c, false);
   }

   private ClassNode configureGenericArray(GenericArrayType genericArrayType) {
      Type component = genericArrayType.getGenericComponentType();
      ClassNode node = this.configureType(component);
      return node.makeArray();
   }

   private ClassNode configureWildcardType(WildcardType wildcardType) {
      ClassNode base = ClassHelper.makeWithoutCaching("?");
      base.setRedirect(ClassHelper.OBJECT_TYPE);
      ClassNode[] lowers = this.configureTypes(wildcardType.getLowerBounds());
      ClassNode lower = null;
      if (lower != null) {
         lower = lowers[0];
      }

      ClassNode[] upper = this.configureTypes(wildcardType.getUpperBounds());
      GenericsType t = new GenericsType(base, upper, lower);
      t.setWildcard(true);
      ClassNode ref = ClassHelper.makeWithoutCaching(Object.class, false);
      ref.setGenericsTypes(new GenericsType[]{t});
      return ref;
   }

   private ClassNode configureParameterizedType(ParameterizedType parameterizedType) {
      ClassNode base = this.configureType(parameterizedType.getRawType());
      GenericsType[] gts = this.configureTypeArguments(parameterizedType.getActualTypeArguments());
      base.setGenericsTypes(gts);
      return base;
   }

   private ClassNode configureTypeVariableReference(TypeVariable tv) {
      ClassNode cn = ClassHelper.makeWithoutCaching(tv.getName());
      cn.setGenericsPlaceHolder(true);
      ClassNode cn2 = ClassHelper.makeWithoutCaching(tv.getName());
      cn2.setGenericsPlaceHolder(true);
      GenericsType[] gts = new GenericsType[]{new GenericsType(cn2)};
      cn.setGenericsTypes(gts);
      cn.setRedirect(ClassHelper.OBJECT_TYPE);
      return cn;
   }

   private GenericsType[] configureTypeArguments(Type[] ta) {
      if (ta.length == 0) {
         return null;
      } else {
         GenericsType[] gts = new GenericsType[ta.length];

         for(int i = 0; i < ta.length; ++i) {
            ClassNode t = this.configureType(ta[i]);
            if (ta[i] instanceof WildcardType) {
               GenericsType[] gen = t.getGenericsTypes();
               gts[i] = gen[0];
            } else {
               gts[i] = new GenericsType(t);
            }
         }

         return gts;
      }
   }

   public Class[] getPluginDefaultGroovyMethods() {
      return PLUGIN_DGM;
   }

   private void setAnnotationMetaData(Annotation[] annotations, AnnotatedNode an) {
      Annotation[] arr$ = annotations;
      int len$ = annotations.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Annotation annotation = arr$[i$];
         AnnotationNode node = new AnnotationNode(ClassHelper.make(annotation.annotationType()));
         this.configureAnnotation(node, annotation);
         an.addAnnotation(node);
      }

   }

   private void configureAnnotationFromDefinition(AnnotationNode definition, AnnotationNode root) {
      ClassNode type = definition.getClassNode();
      if (type.isResolved()) {
         Class clazz = type.getTypeClass();
         Expression exp;
         if (clazz == Retention.class) {
            exp = definition.getMember("value");
            if (!(exp instanceof PropertyExpression)) {
               return;
            }

            PropertyExpression pe = (PropertyExpression)exp;
            String name = pe.getPropertyAsString();
            RetentionPolicy policy = RetentionPolicy.valueOf(name);
            this.setRetentionPolicy(policy, root);
         } else if (clazz == Target.class) {
            exp = definition.getMember("value");
            if (!(exp instanceof ListExpression)) {
               return;
            }

            ListExpression le = (ListExpression)exp;
            int bitmap = 0;

            ElementType value;
            for(Iterator i$ = le.getExpressions().iterator(); i$.hasNext(); bitmap |= this.getElementCode(value)) {
               Expression e = (Expression)i$.next();
               PropertyExpression element = (PropertyExpression)e;
               String name = element.getPropertyAsString();
               value = ElementType.valueOf(name);
            }

            root.setAllowedTargets(bitmap);
         }

      }
   }

   public void configureAnnotation(AnnotationNode node) {
      ClassNode type = node.getClassNode();
      List<AnnotationNode> annotations = type.getAnnotations();
      Iterator i$ = annotations.iterator();

      while(i$.hasNext()) {
         AnnotationNode an = (AnnotationNode)i$.next();
         this.configureAnnotationFromDefinition(an, node);
      }

      this.configureAnnotationFromDefinition(node, node);
   }

   private void configureAnnotation(AnnotationNode node, Annotation annotation) {
      Class type = annotation.annotationType();
      if (type == Retention.class) {
         Retention r = (Retention)annotation;
         RetentionPolicy value = r.value();
         this.setRetentionPolicy(value, node);
         node.setMember("value", new PropertyExpression(new ClassExpression(ClassHelper.makeWithoutCaching(RetentionPolicy.class, false)), value.toString()));
      } else if (type == Target.class) {
         Target t = (Target)annotation;
         ElementType[] elements = t.value();
         ListExpression elementExprs = new ListExpression();
         ElementType[] arr$ = elements;
         int len$ = elements.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ElementType element = arr$[i$];
            elementExprs.addExpression(new PropertyExpression(new ClassExpression(ClassHelper.ELEMENT_TYPE_TYPE), element.name()));
         }

         node.setMember("value", elementExprs);
      } else {
         Method[] declaredMethods = type.getDeclaredMethods();

         for(int i = 0; i < declaredMethods.length; ++i) {
            Method declaredMethod = declaredMethods[i];

            try {
               Object value = declaredMethod.invoke(annotation);
               Expression valueExpression = this.annotationValueToExpression(value);
               if (valueExpression != null) {
                  node.setMember(declaredMethod.getName(), valueExpression);
               }
            } catch (IllegalAccessException var11) {
            } catch (InvocationTargetException var12) {
            }
         }
      }

   }

   private Expression annotationValueToExpression(Object value) {
      if (value != null && !(value instanceof String) && !(value instanceof Number) && !(value instanceof Character) && !(value instanceof Boolean)) {
         if (value instanceof Class) {
            return new ClassExpression(ClassHelper.makeWithoutCaching((Class)value));
         } else if (!value.getClass().isArray()) {
            return null;
         } else {
            ListExpression elementExprs = new ListExpression();
            int len = Array.getLength(value);

            for(int i = 0; i != len; ++i) {
               elementExprs.addExpression(this.annotationValueToExpression(Array.get(value, i)));
            }

            return elementExprs;
         }
      } else {
         return new ConstantExpression(value);
      }
   }

   private void setRetentionPolicy(RetentionPolicy value, AnnotationNode node) {
      switch(value) {
      case RUNTIME:
         node.setRuntimeRetention(true);
         break;
      case SOURCE:
         node.setSourceRetention(true);
         break;
      case CLASS:
         node.setClassRetention(true);
         break;
      default:
         throw new GroovyBugError("unsupported Retention " + value);
      }

   }

   private int getElementCode(ElementType value) {
      switch(value) {
      case TYPE:
         return 1;
      case CONSTRUCTOR:
         return 2;
      case METHOD:
         return 4;
      case FIELD:
         return 8;
      case PARAMETER:
         return 16;
      case LOCAL_VARIABLE:
         return 32;
      case ANNOTATION_TYPE:
         return 64;
      case PACKAGE:
         return 128;
      default:
         throw new GroovyBugError("unsupported Target " + value);
      }
   }

   private void setMethodDefaultValue(MethodNode mn, Method m) {
      Object defaultValue = m.getDefaultValue();
      mn.setCode(new ReturnStatement(new ConstantExpression(defaultValue)));
      mn.setAnnotationDefault(true);
   }

   public void configureClassNode(CompileUnit compileUnit, ClassNode classNode) {
      Class clazz = classNode.getTypeClass();
      Field[] fields = clazz.getDeclaredFields();
      Field[] arr$ = fields;
      int len$ = fields.length;

      int len$;
      for(len$ = 0; len$ < len$; ++len$) {
         Field f = arr$[len$];
         ClassNode ret = this.makeClassNode(compileUnit, f.getGenericType(), f.getType());
         classNode.addField(f.getName(), f.getModifiers(), ret, (Expression)null);
      }

      Method[] methods = clazz.getDeclaredMethods();
      Method[] arr$ = methods;
      len$ = methods.length;

      Parameter[] params;
      ClassNode[] exceptions;
      int len$;
      for(len$ = 0; len$ < len$; ++len$) {
         Method m = arr$[len$];
         ClassNode ret = this.makeClassNode(compileUnit, m.getGenericReturnType(), m.getReturnType());
         params = this.makeParameters(compileUnit, m.getGenericParameterTypes(), m.getParameterTypes());
         exceptions = this.makeClassNodes(compileUnit, m.getGenericExceptionTypes(), m.getExceptionTypes());
         MethodNode mn = new MethodNode(m.getName(), m.getModifiers(), ret, params, exceptions, (Statement)null);
         this.setMethodDefaultValue(mn, m);
         this.setAnnotationMetaData(m.getAnnotations(), mn);
         mn.setGenericsTypes(this.configureTypeVariable(m.getTypeParameters()));
         classNode.addMethod(mn);
      }

      Constructor[] constructors = clazz.getDeclaredConstructors();
      Constructor[] arr$ = constructors;
      len$ = constructors.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Constructor ctor = arr$[i$];
         params = this.makeParameters(compileUnit, ctor.getGenericParameterTypes(), ctor.getParameterTypes());
         exceptions = this.makeClassNodes(compileUnit, ctor.getGenericExceptionTypes(), ctor.getExceptionTypes());
         classNode.addConstructor(ctor.getModifiers(), params, exceptions, (Statement)null);
      }

      Class sc = clazz.getSuperclass();
      if (sc != null) {
         classNode.setUnresolvedSuperClass(this.makeClassNode(compileUnit, clazz.getGenericSuperclass(), sc));
      }

      this.makeInterfaceTypes(compileUnit, classNode, clazz);
      this.setAnnotationMetaData(classNode.getTypeClass().getAnnotations(), classNode);
      PackageNode packageNode = classNode.getPackage();
      if (packageNode != null) {
         this.setAnnotationMetaData(classNode.getTypeClass().getPackage().getAnnotations(), packageNode);
      }

   }

   private void makeInterfaceTypes(CompileUnit cu, ClassNode classNode, Class clazz) {
      Type[] interfaceTypes = clazz.getGenericInterfaces();
      if (interfaceTypes.length == 0) {
         classNode.setInterfaces(ClassNode.EMPTY_ARRAY);
      } else {
         Class[] interfaceClasses = clazz.getInterfaces();
         ClassNode[] ret = new ClassNode[interfaceTypes.length];

         for(int i = 0; i < interfaceTypes.length; ++i) {
            ret[i] = this.makeClassNode(cu, interfaceTypes[i], interfaceClasses[i]);
         }

         classNode.setInterfaces(ret);
      }

   }

   private ClassNode[] makeClassNodes(CompileUnit cu, Type[] types, Class[] cls) {
      ClassNode[] nodes = new ClassNode[types.length];

      for(int i = 0; i < nodes.length; ++i) {
         nodes[i] = this.makeClassNode(cu, types[i], cls[i]);
      }

      return nodes;
   }

   private ClassNode makeClassNode(CompileUnit cu, Type t, Class c) {
      ClassNode back = null;
      if (cu != null) {
         back = cu.getClass(c.getName());
      }

      if (back == null) {
         back = ClassHelper.make(c);
      }

      if (!(t instanceof Class)) {
         ClassNode front = this.configureType(t);
         front.setRedirect(back);
         return front;
      } else {
         return back;
      }
   }

   private Parameter[] makeParameters(CompileUnit cu, Type[] types, Class[] cls) {
      Parameter[] params = Parameter.EMPTY_ARRAY;
      if (types.length > 0) {
         params = new Parameter[types.length];

         for(int i = 0; i < params.length; ++i) {
            params[i] = this.makeParameter(cu, types[i], cls[i], i);
         }
      }

      return params;
   }

   private Parameter makeParameter(CompileUnit cu, Type type, Class cl, int idx) {
      ClassNode cn = this.makeClassNode(cu, type, cl);
      return new Parameter(cn, "param" + idx);
   }
}
