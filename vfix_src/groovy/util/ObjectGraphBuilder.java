package groovy.util;

import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.MetaProperty;
import groovy.lang.MissingPropertyException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.codehaus.groovy.runtime.InvokerHelper;

public class ObjectGraphBuilder extends FactoryBuilderSupport {
   public static final String NODE_CLASS = "_NODE_CLASS_";
   public static final String NODE_NAME = "_NODE_NAME_";
   public static final String OBJECT_ID = "_OBJECT_ID_";
   public static final String LAZY_REF = "_LAZY_REF_";
   public static final String CLASSNAME_RESOLVER_KEY = "name";
   public static final String CLASSNAME_RESOLVER_REFLECTION = "reflection";
   public static final String CLASSNAME_RESOLVER_REFLECTION_ROOT = "root";
   private static final Pattern PLURAL_IES_PATTERN = Pattern.compile(".*[^aeiouy]y", 2);
   private ObjectGraphBuilder.ChildPropertySetter childPropertySetter = new ObjectGraphBuilder.DefaultChildPropertySetter();
   private ObjectGraphBuilder.ClassNameResolver classNameResolver = new ObjectGraphBuilder.DefaultClassNameResolver();
   private ObjectGraphBuilder.IdentifierResolver identifierResolver = new ObjectGraphBuilder.DefaultIdentifierResolver();
   private ObjectGraphBuilder.NewInstanceResolver newInstanceResolver = new ObjectGraphBuilder.DefaultNewInstanceResolver();
   private ObjectGraphBuilder.ObjectFactory objectFactory = new ObjectGraphBuilder.ObjectFactory();
   private ObjectGraphBuilder.ObjectBeanFactory objectBeanFactory = new ObjectGraphBuilder.ObjectBeanFactory();
   private ObjectGraphBuilder.ObjectRefFactory objectRefFactory = new ObjectGraphBuilder.ObjectRefFactory();
   private ObjectGraphBuilder.ReferenceResolver referenceResolver = new ObjectGraphBuilder.DefaultReferenceResolver();
   private ObjectGraphBuilder.RelationNameResolver relationNameResolver = new ObjectGraphBuilder.DefaultRelationNameResolver();
   private Map<String, Class> resolvedClasses = new HashMap();
   private ClassLoader classLoader;
   private boolean lazyReferencesAllowed = true;
   private List<ObjectGraphBuilder.NodeReference> lazyReferences = new ArrayList();
   private String beanFactoryName = "bean";

   public ObjectGraphBuilder() {
      this.addPostNodeCompletionDelegate(new Closure(this, this) {
         public void doCall(ObjectGraphBuilder builder, Object parent, Object node) {
            if (parent == null) {
               builder.resolveLazyReferences();
               builder.dispose();
            }

         }
      });
   }

   public String getBeanFactoryName() {
      return this.beanFactoryName;
   }

   public ObjectGraphBuilder.ChildPropertySetter getChildPropertySetter() {
      return this.childPropertySetter;
   }

   public ClassLoader getClassLoader() {
      return this.classLoader;
   }

   public ObjectGraphBuilder.ClassNameResolver getClassNameResolver() {
      return this.classNameResolver;
   }

   public ObjectGraphBuilder.NewInstanceResolver getNewInstanceResolver() {
      return this.newInstanceResolver;
   }

   public ObjectGraphBuilder.RelationNameResolver getRelationNameResolver() {
      return this.relationNameResolver;
   }

   public boolean isLazyReferencesAllowed() {
      return this.lazyReferencesAllowed;
   }

   public void setBeanFactoryName(String beanFactoryName) {
      this.beanFactoryName = beanFactoryName;
   }

   public void setChildPropertySetter(final Object childPropertySetter) {
      if (childPropertySetter instanceof ObjectGraphBuilder.ChildPropertySetter) {
         this.childPropertySetter = (ObjectGraphBuilder.ChildPropertySetter)childPropertySetter;
      } else if (childPropertySetter instanceof Closure) {
         this.childPropertySetter = new ObjectGraphBuilder.ChildPropertySetter() {
            public void setChild(Object parent, Object child, String parentName, String propertyName) {
               Closure cls = (Closure)childPropertySetter;
               cls.setDelegate(ObjectGraphBuilder.this);
               cls.call(new Object[]{parent, child, parentName, propertyName});
            }
         };
      } else {
         this.childPropertySetter = new ObjectGraphBuilder.DefaultChildPropertySetter();
      }

   }

   public void setClassLoader(ClassLoader classLoader) {
      this.classLoader = classLoader;
   }

   public void setClassNameResolver(final Object classNameResolver) {
      if (classNameResolver instanceof ObjectGraphBuilder.ClassNameResolver) {
         this.classNameResolver = (ObjectGraphBuilder.ClassNameResolver)classNameResolver;
      } else if (classNameResolver instanceof String) {
         this.classNameResolver = new ObjectGraphBuilder.ClassNameResolver() {
            public String resolveClassname(String classname) {
               return ObjectGraphBuilder.makeClassName((String)classNameResolver, classname);
            }
         };
      } else if (classNameResolver instanceof Closure) {
         this.classNameResolver = new ObjectGraphBuilder.ClassNameResolver() {
            public String resolveClassname(String classname) {
               Closure cls = (Closure)classNameResolver;
               cls.setDelegate(ObjectGraphBuilder.this);
               return (String)cls.call(new Object[]{classname});
            }
         };
      } else if (classNameResolver instanceof Map) {
         Map classNameResolverOptions = (Map)classNameResolver;
         String resolverName = (String)classNameResolverOptions.get("name");
         if (resolverName == null) {
            throw new RuntimeException("key 'name' not defined");
         }

         if (!"reflection".equals(resolverName)) {
            throw new RuntimeException("unknown class name resolver " + resolverName);
         }

         String root = (String)classNameResolverOptions.get("root");
         if (root == null) {
            throw new RuntimeException("key 'root' not defined");
         }

         this.classNameResolver = new ObjectGraphBuilder.ReflectionClassNameResolver(root);
      } else {
         this.classNameResolver = new ObjectGraphBuilder.DefaultClassNameResolver();
      }

   }

   public void setIdentifierResolver(final Object identifierResolver) {
      if (identifierResolver instanceof ObjectGraphBuilder.IdentifierResolver) {
         this.identifierResolver = (ObjectGraphBuilder.IdentifierResolver)identifierResolver;
      } else if (identifierResolver instanceof String) {
         this.identifierResolver = new ObjectGraphBuilder.IdentifierResolver() {
            public String getIdentifierFor(String nodeName) {
               return (String)identifierResolver;
            }
         };
      } else if (identifierResolver instanceof Closure) {
         this.identifierResolver = new ObjectGraphBuilder.IdentifierResolver() {
            public String getIdentifierFor(String nodeName) {
               Closure cls = (Closure)identifierResolver;
               cls.setDelegate(ObjectGraphBuilder.this);
               return (String)cls.call(new Object[]{nodeName});
            }
         };
      } else {
         this.identifierResolver = new ObjectGraphBuilder.DefaultIdentifierResolver();
      }

   }

   public void setLazyReferencesAllowed(boolean lazyReferencesAllowed) {
      this.lazyReferencesAllowed = lazyReferencesAllowed;
   }

   public void setNewInstanceResolver(final Object newInstanceResolver) {
      if (newInstanceResolver instanceof ObjectGraphBuilder.NewInstanceResolver) {
         this.newInstanceResolver = (ObjectGraphBuilder.NewInstanceResolver)newInstanceResolver;
      } else if (newInstanceResolver instanceof Closure) {
         this.newInstanceResolver = new ObjectGraphBuilder.NewInstanceResolver() {
            public Object newInstance(Class klass, Map attributes) throws InstantiationException, IllegalAccessException {
               Closure cls = (Closure)newInstanceResolver;
               cls.setDelegate(ObjectGraphBuilder.this);
               return cls.call(new Object[]{klass, attributes});
            }
         };
      } else {
         this.newInstanceResolver = new ObjectGraphBuilder.DefaultNewInstanceResolver();
      }

   }

   public void setReferenceResolver(final Object referenceResolver) {
      if (referenceResolver instanceof ObjectGraphBuilder.ReferenceResolver) {
         this.referenceResolver = (ObjectGraphBuilder.ReferenceResolver)referenceResolver;
      } else if (referenceResolver instanceof String) {
         this.referenceResolver = new ObjectGraphBuilder.ReferenceResolver() {
            public String getReferenceFor(String nodeName) {
               return (String)referenceResolver;
            }
         };
      } else if (referenceResolver instanceof Closure) {
         this.referenceResolver = new ObjectGraphBuilder.ReferenceResolver() {
            public String getReferenceFor(String nodeName) {
               Closure cls = (Closure)referenceResolver;
               cls.setDelegate(ObjectGraphBuilder.this);
               return (String)cls.call(new Object[]{nodeName});
            }
         };
      } else {
         this.referenceResolver = new ObjectGraphBuilder.DefaultReferenceResolver();
      }

   }

   public void setRelationNameResolver(ObjectGraphBuilder.RelationNameResolver relationNameResolver) {
      this.relationNameResolver = (ObjectGraphBuilder.RelationNameResolver)(relationNameResolver != null ? relationNameResolver : new ObjectGraphBuilder.DefaultRelationNameResolver());
   }

   protected void postInstantiate(Object name, Map attributes, Object node) {
      super.postInstantiate(name, attributes, node);
      Map context = this.getContext();
      String objectId = (String)context.get("_OBJECT_ID_");
      if (objectId != null && node != null) {
         this.setVariable(objectId, node);
      }

   }

   protected void preInstantiate(Object name, Map attributes, Object value) {
      super.preInstantiate(name, attributes, value);
      Map context = this.getContext();
      context.put("_OBJECT_ID_", attributes.remove(this.identifierResolver.getIdentifierFor((String)name)));
   }

   protected Factory resolveFactory(Object name, Map attributes, Object value) {
      Factory factory = super.resolveFactory(name, attributes, value);
      if (factory != null) {
         return factory;
      } else if (attributes.get(this.referenceResolver.getReferenceFor((String)name)) != null) {
         return this.objectRefFactory;
      } else {
         return (Factory)(this.beanFactoryName != null && this.beanFactoryName.equals((String)name) ? this.objectBeanFactory : this.objectFactory);
      }
   }

   private void resolveLazyReferences() {
      if (this.lazyReferencesAllowed) {
         Iterator i$ = this.lazyReferences.iterator();

         while(i$.hasNext()) {
            ObjectGraphBuilder.NodeReference ref = (ObjectGraphBuilder.NodeReference)i$.next();
            if (ref.parent != null) {
               Object child = null;

               try {
                  child = this.getProperty(ref.refId);
               } catch (MissingPropertyException var6) {
               }

               if (child == null) {
                  throw new IllegalArgumentException("There is no valid node for reference " + ref.parentName + "." + ref.childName + "=" + ref.refId);
               }

               this.childPropertySetter.setChild(ref.parent, child, ref.parentName, this.relationNameResolver.resolveChildRelationName(ref.parentName, ref.parent, ref.childName, child));
               String propertyName = this.relationNameResolver.resolveParentRelationName(ref.parentName, ref.parent, ref.childName, child);
               MetaProperty metaProperty = InvokerHelper.getMetaClass(child).hasProperty(child, propertyName);
               if (metaProperty != null) {
                  metaProperty.setProperty(child, ref.parent);
               }
            }
         }

      }
   }

   private static String makeClassName(String root, String name) {
      return root + "." + name.substring(0, 1).toUpperCase() + name.substring(1);
   }

   private static class NodeReference {
      private final Object parent;
      private final String parentName;
      private final String childName;
      private final String refId;

      private NodeReference(Object parent, String parentName, String childName, String refId) {
         this.parent = parent;
         this.parentName = parentName;
         this.childName = childName;
         this.refId = refId;
      }

      public String toString() {
         return "[parentName=" + this.parentName + ", childName=" + this.childName + ", refId=" + this.refId + "]";
      }

      // $FF: synthetic method
      NodeReference(Object x0, String x1, String x2, String x3, Object x4) {
         this(x0, x1, x2, x3);
      }
   }

   private static class ObjectRefFactory extends ObjectGraphBuilder.ObjectFactory {
      private ObjectRefFactory() {
         super(null);
      }

      public boolean isLeaf() {
         return true;
      }

      public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
         ObjectGraphBuilder ogbuilder = (ObjectGraphBuilder)builder;
         String refProperty = ogbuilder.referenceResolver.getReferenceFor((String)name);
         Object refId = properties.remove(refProperty);
         Object object = null;
         Boolean lazy = Boolean.FALSE;
         if (refId instanceof String) {
            try {
               object = ogbuilder.getProperty((String)refId);
            } catch (MissingPropertyException var15) {
            }

            if (object == null) {
               if (!ogbuilder.isLazyReferencesAllowed()) {
                  throw new IllegalArgumentException("There is no previous node with " + ogbuilder.identifierResolver.getIdentifierFor((String)name) + "=" + refId);
               }

               lazy = Boolean.TRUE;
            }
         } else {
            object = refId;
         }

         if (!properties.isEmpty()) {
            throw new IllegalArgumentException("You can not modify the properties of a referenced object.");
         } else {
            Map context = ogbuilder.getContext();
            context.put("_NODE_NAME_", name);
            context.put("_LAZY_REF_", lazy);
            if (lazy) {
               Map parentContext = ogbuilder.getParentContext();
               Object parent = null;
               String parentName = null;
               String childName = (String)name;
               if (parentContext != null) {
                  parent = context.get("_CURRENT_NODE_");
                  parentName = (String)parentContext.get("_NODE_NAME_");
               }

               ogbuilder.lazyReferences.add(new ObjectGraphBuilder.NodeReference(parent, parentName, childName, (String)refId));
            } else {
               context.put("_NODE_CLASS_", object.getClass());
            }

            return object;
         }
      }

      public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
         Boolean lazy = (Boolean)builder.getContext().get("_LAZY_REF_");
         if (!lazy) {
            super.setChild(builder, parent, child);
         }

      }

      public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
         Boolean lazy = (Boolean)builder.getContext().get("_LAZY_REF_");
         if (!lazy) {
            super.setParent(builder, parent, child);
         }

      }

      // $FF: synthetic method
      ObjectRefFactory(Object x0) {
         this();
      }
   }

   private static class ObjectBeanFactory extends ObjectGraphBuilder.ObjectFactory {
      private ObjectBeanFactory() {
         super(null);
      }

      public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
         if (value == null) {
            return super.newInstance(builder, name, value, properties);
         } else {
            Object bean = null;
            Class klass = null;
            Map context = builder.getContext();
            if (!(value instanceof String) && !(value instanceof GString)) {
               if (value instanceof Class) {
                  klass = (Class)value;
                  bean = this.resolveInstance(builder, name, value, klass, properties);
               } else {
                  klass = value.getClass();
                  bean = value;
               }

               String nodename = klass.getSimpleName();
               if (nodename.length() > 1) {
                  nodename = nodename.substring(0, 1).toLowerCase() + nodename.substring(1);
               } else {
                  nodename = nodename.toLowerCase();
               }

               context.put("_NODE_NAME_", nodename);
               context.put("_NODE_CLASS_", klass);
               return bean;
            } else {
               throw new IllegalArgumentException("ObjectGraphBuilder." + ((ObjectGraphBuilder)builder).getBeanFactoryName() + "() does not accept String nor GString as value.");
            }
         }
      }

      // $FF: synthetic method
      ObjectBeanFactory(Object x0) {
         this();
      }
   }

   private static class ObjectFactory extends AbstractFactory {
      private ObjectFactory() {
      }

      public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
         ObjectGraphBuilder ogbuilder = (ObjectGraphBuilder)builder;
         String classname = ogbuilder.classNameResolver.resolveClassname((String)name);
         Class klass = this.resolveClass(builder, classname, name, value, properties);
         Map context = builder.getContext();
         context.put("_NODE_NAME_", name);
         context.put("_NODE_CLASS_", klass);
         return this.resolveInstance(builder, name, value, klass, properties);
      }

      protected Class resolveClass(FactoryBuilderSupport builder, String classname, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
         ObjectGraphBuilder ogbuilder = (ObjectGraphBuilder)builder;
         Class klass = (Class)ogbuilder.resolvedClasses.get(classname);
         if (klass == null) {
            klass = this.loadClass(ogbuilder.classLoader, classname);
            if (klass == null) {
               klass = this.loadClass(ogbuilder.getClass().getClassLoader(), classname);
            }

            if (klass == null) {
               try {
                  klass = Class.forName(classname);
               } catch (ClassNotFoundException var9) {
               }
            }

            if (klass == null) {
               klass = this.loadClass(Thread.currentThread().getContextClassLoader(), classname);
            }

            if (klass == null) {
               throw new RuntimeException(new ClassNotFoundException(classname));
            }

            ogbuilder.resolvedClasses.put(classname, klass);
         }

         return klass;
      }

      protected Object resolveInstance(FactoryBuilderSupport builder, Object name, Object value, Class klass, Map properties) throws InstantiationException, IllegalAccessException {
         ObjectGraphBuilder ogbuilder = (ObjectGraphBuilder)builder;
         return value != null && klass.isAssignableFrom(value.getClass()) ? value : ogbuilder.newInstanceResolver.newInstance(klass, properties);
      }

      public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
         if (child != null) {
            ObjectGraphBuilder ogbuilder = (ObjectGraphBuilder)builder;
            if (parent != null) {
               Map context = ogbuilder.getContext();
               Map parentContext = ogbuilder.getParentContext();
               String parentName = null;
               String childName = (String)context.get("_NODE_NAME_");
               if (parentContext != null) {
                  parentName = (String)parentContext.get("_NODE_NAME_");
               }

               String propertyName = ogbuilder.relationNameResolver.resolveParentRelationName(parentName, parent, childName, child);
               MetaProperty metaProperty = InvokerHelper.getMetaClass(child).hasProperty(child, propertyName);
               if (metaProperty != null) {
                  metaProperty.setProperty(child, parent);
               }
            }

         }
      }

      public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
         if (child != null) {
            ObjectGraphBuilder ogbuilder = (ObjectGraphBuilder)builder;
            if (parent != null) {
               Map context = ogbuilder.getContext();
               Map parentContext = ogbuilder.getParentContext();
               String parentName = null;
               String childName = (String)context.get("_NODE_NAME_");
               if (parentContext != null) {
                  parentName = (String)parentContext.get("_NODE_NAME_");
               }

               ogbuilder.childPropertySetter.setChild(parent, child, parentName, ogbuilder.relationNameResolver.resolveChildRelationName(parentName, parent, childName, child));
            }

         }
      }

      protected Class loadClass(ClassLoader classLoader, String classname) {
         if (classLoader != null && classname != null) {
            try {
               return classLoader.loadClass(classname);
            } catch (ClassNotFoundException var4) {
               return null;
            }
         } else {
            return null;
         }
      }

      // $FF: synthetic method
      ObjectFactory(Object x0) {
         this();
      }
   }

   public interface RelationNameResolver {
      String resolveChildRelationName(String var1, Object var2, String var3, Object var4);

      String resolveParentRelationName(String var1, Object var2, String var3, Object var4);
   }

   public interface ReferenceResolver {
      String getReferenceFor(String var1);
   }

   public interface NewInstanceResolver {
      Object newInstance(Class var1, Map var2) throws InstantiationException, IllegalAccessException;
   }

   public interface IdentifierResolver {
      String getIdentifierFor(String var1);
   }

   public static class DefaultRelationNameResolver implements ObjectGraphBuilder.RelationNameResolver {
      public String resolveChildRelationName(String parentName, Object parent, String childName, Object child) {
         boolean matchesIESRule = ObjectGraphBuilder.PLURAL_IES_PATTERN.matcher(childName).matches();
         String childNamePlural = matchesIESRule ? childName.substring(0, childName.length() - 1) + "ies" : childName + "s";
         MetaProperty metaProperty = InvokerHelper.getMetaClass(parent).hasProperty(parent, childNamePlural);
         return metaProperty != null ? childNamePlural : childName;
      }

      public String resolveParentRelationName(String parentName, Object parent, String childName, Object child) {
         return parentName;
      }
   }

   public static class DefaultReferenceResolver implements ObjectGraphBuilder.ReferenceResolver {
      public String getReferenceFor(String nodeName) {
         return "refId";
      }
   }

   public static class DefaultNewInstanceResolver implements ObjectGraphBuilder.NewInstanceResolver {
      public Object newInstance(Class klass, Map attributes) throws InstantiationException, IllegalAccessException {
         return klass.newInstance();
      }
   }

   public static class DefaultIdentifierResolver implements ObjectGraphBuilder.IdentifierResolver {
      public String getIdentifierFor(String nodeName) {
         return "id";
      }
   }

   public class ReflectionClassNameResolver implements ObjectGraphBuilder.ClassNameResolver {
      private final String root;

      public ReflectionClassNameResolver(String root) {
         this.root = root;
      }

      public String resolveClassname(String classname) {
         Object currentNode = ObjectGraphBuilder.this.getContext().get("_CURRENT_NODE_");
         if (currentNode == null) {
            return ObjectGraphBuilder.makeClassName(this.root, classname);
         } else {
            try {
               Class klass = currentNode.getClass().getDeclaredField(classname).getType();
               if (Collection.class.isAssignableFrom(klass)) {
                  Type type = currentNode.getClass().getDeclaredField(classname).getGenericType();
                  if (!(type instanceof ParameterizedType)) {
                     throw new RuntimeException("collection field " + classname + " must be genericised");
                  }

                  ParameterizedType ptype = (ParameterizedType)type;
                  Type[] actualTypeArguments = ptype.getActualTypeArguments();
                  if (actualTypeArguments.length != 1) {
                     throw new RuntimeException("can't determine class name for collection field " + classname + " with multiple generics");
                  }

                  Type typeArgument = actualTypeArguments[0];
                  if (!(typeArgument instanceof Class)) {
                     throw new RuntimeException("can't instantiate collection field " + classname + " elements as they aren't a class");
                  }

                  klass = (Class)actualTypeArguments[0];
               }

               return klass.getName();
            } catch (NoSuchFieldException var8) {
               throw new RuntimeException("can't find field " + classname + " for node class " + currentNode.getClass().getName(), var8);
            }
         }
      }
   }

   public static class DefaultClassNameResolver implements ObjectGraphBuilder.ClassNameResolver {
      public String resolveClassname(String classname) {
         return classname.length() == 1 ? classname.toUpperCase() : classname.substring(0, 1).toUpperCase() + classname.substring(1);
      }
   }

   public static class DefaultChildPropertySetter implements ObjectGraphBuilder.ChildPropertySetter {
      public void setChild(Object parent, Object child, String parentName, String propertyName) {
         try {
            Object property = InvokerHelper.getProperty(parent, propertyName);
            if (property != null && Collection.class.isAssignableFrom(property.getClass())) {
               ((Collection)property).add(child);
            } else {
               InvokerHelper.setProperty(parent, propertyName, child);
            }
         } catch (MissingPropertyException var6) {
         }

      }
   }

   public interface ClassNameResolver {
      String resolveClassname(String var1);
   }

   public interface ChildPropertySetter {
      void setChild(Object var1, Object var2, String var3, String var4);
   }
}
