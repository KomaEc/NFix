package org.apache.tools.ant;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.tools.ant.taskdefs.PreSetDef;
import org.apache.tools.ant.types.EnumeratedAttribute;

public final class IntrospectionHelper {
   private static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap(0));
   private static final Map HELPERS = new Hashtable();
   private static final Map PRIMITIVE_TYPE_MAP = new HashMap(8);
   private static final int MAX_REPORT_NESTED_TEXT = 20;
   private static final String ELLIPSIS = "...";
   private Hashtable attributeTypes = new Hashtable();
   private Hashtable attributeSetters = new Hashtable();
   private Hashtable nestedTypes = new Hashtable();
   private Hashtable nestedCreators = new Hashtable();
   private List addTypeMethods = new ArrayList();
   private Method addText = null;
   private Class bean;
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$java$lang$Byte;
   // $FF: synthetic field
   static Class class$java$lang$Character;
   // $FF: synthetic field
   static Class class$java$lang$Short;
   // $FF: synthetic field
   static Class class$java$lang$Integer;
   // $FF: synthetic field
   static Class class$java$lang$Long;
   // $FF: synthetic field
   static Class class$java$lang$Float;
   // $FF: synthetic field
   static Class class$java$lang$Double;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$ProjectComponent;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Task;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Project;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Location;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$DynamicElement;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$DynamicElementNS;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$TaskContainer;
   // $FF: synthetic field
   static Class class$java$lang$Class;
   // $FF: synthetic field
   static Class class$java$io$File;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$types$EnumeratedAttribute;

   private IntrospectionHelper(Class bean) {
      this.bean = bean;
      Method[] methods = bean.getMethods();

      for(int i = 0; i < methods.length; ++i) {
         Method m = methods[i];
         String name = m.getName();
         Class returnType = m.getReturnType();
         Class[] args = m.getParameterTypes();
         if (args.length == 1 && Void.TYPE.equals(returnType) && ("add".equals(name) || "addConfigured".equals(name))) {
            this.insertAddTypeMethod(m);
         } else if ((!(class$org$apache$tools$ant$ProjectComponent == null ? (class$org$apache$tools$ant$ProjectComponent = class$("org.apache.tools.ant.ProjectComponent")) : class$org$apache$tools$ant$ProjectComponent).isAssignableFrom(bean) || args.length != 1 || !this.isHiddenSetMethod(name, args[0])) && (!this.isContainer() || args.length != 1 || !"addTask".equals(name) || !(class$org$apache$tools$ant$Task == null ? (class$org$apache$tools$ant$Task = class$("org.apache.tools.ant.Task")) : class$org$apache$tools$ant$Task).equals(args[0]))) {
            if ("addText".equals(name) && Void.TYPE.equals(returnType) && args.length == 1 && (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String).equals(args[0])) {
               this.addText = methods[i];
            } else {
               String propName;
               if (name.startsWith("set") && Void.TYPE.equals(returnType) && args.length == 1 && !args[0].isArray()) {
                  propName = this.getPropertyName(name, "set");
                  if (this.attributeSetters.get(propName) == null || !(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String).equals(args[0])) {
                     IntrospectionHelper.AttributeSetter as = this.createAttributeSetter(m, args[0], propName);
                     if (as != null) {
                        this.attributeTypes.put(propName, args[0]);
                        this.attributeSetters.put(propName, as);
                     }
                  }
               } else if (name.startsWith("create") && !returnType.isArray() && !returnType.isPrimitive() && args.length == 0) {
                  propName = this.getPropertyName(name, "create");
                  if (this.nestedCreators.get(propName) == null) {
                     this.nestedTypes.put(propName, returnType);
                     this.nestedCreators.put(propName, new IntrospectionHelper.CreateNestedCreator(m));
                  }
               } else {
                  Constructor constructor;
                  String propName;
                  if (name.startsWith("addConfigured") && Void.TYPE.equals(returnType) && args.length == 1 && !(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String).equals(args[0]) && !args[0].isArray() && !args[0].isPrimitive()) {
                     try {
                        constructor = null;

                        try {
                           constructor = args[0].getConstructor();
                        } catch (NoSuchMethodException var10) {
                           constructor = args[0].getConstructor(class$org$apache$tools$ant$Project == null ? (class$org$apache$tools$ant$Project = class$("org.apache.tools.ant.Project")) : class$org$apache$tools$ant$Project);
                        }

                        propName = this.getPropertyName(name, "addConfigured");
                        this.nestedTypes.put(propName, args[0]);
                        this.nestedCreators.put(propName, new IntrospectionHelper.AddNestedCreator(m, constructor, 2));
                     } catch (NoSuchMethodException var11) {
                     }
                  } else if (name.startsWith("add") && Void.TYPE.equals(returnType) && args.length == 1 && !(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String).equals(args[0]) && !args[0].isArray() && !args[0].isPrimitive()) {
                     try {
                        constructor = null;

                        try {
                           constructor = args[0].getConstructor();
                        } catch (NoSuchMethodException var12) {
                           constructor = args[0].getConstructor(class$org$apache$tools$ant$Project == null ? (class$org$apache$tools$ant$Project = class$("org.apache.tools.ant.Project")) : class$org$apache$tools$ant$Project);
                        }

                        propName = this.getPropertyName(name, "add");
                        if (this.nestedTypes.get(propName) == null) {
                           this.nestedTypes.put(propName, args[0]);
                           this.nestedCreators.put(propName, new IntrospectionHelper.AddNestedCreator(m, constructor, 1));
                        }
                     } catch (NoSuchMethodException var13) {
                     }
                  }
               }
            }
         }
      }

   }

   private boolean isHiddenSetMethod(String name, Class type) {
      if ("setLocation".equals(name) && (class$org$apache$tools$ant$Location == null ? (class$org$apache$tools$ant$Location = class$("org.apache.tools.ant.Location")) : class$org$apache$tools$ant$Location).equals(type)) {
         return true;
      } else {
         return "setTaskType".equals(name) && (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String).equals(type);
      }
   }

   public static synchronized IntrospectionHelper getHelper(Class c) {
      return getHelper((Project)null, c);
   }

   public static IntrospectionHelper getHelper(Project p, Class c) {
      IntrospectionHelper ih = (IntrospectionHelper)HELPERS.get(c.getName());
      if (ih == null || ih.bean != c) {
         ih = new IntrospectionHelper(c);
         if (p != null) {
            HELPERS.put(c.getName(), ih);
         }
      }

      return ih;
   }

   public void setAttribute(Project p, Object element, String attributeName, String value) throws BuildException {
      IntrospectionHelper.AttributeSetter as = (IntrospectionHelper.AttributeSetter)this.attributeSetters.get(attributeName.toLowerCase(Locale.US));
      if (as == null) {
         if (element instanceof DynamicAttributeNS) {
            DynamicAttributeNS dc = (DynamicAttributeNS)element;
            String uriPlusPrefix = ProjectHelper.extractUriFromComponentName(attributeName);
            String uri = ProjectHelper.extractUriFromComponentName(uriPlusPrefix);
            String localName = ProjectHelper.extractNameFromComponentName(attributeName);
            String qName = "".equals(uri) ? localName : uri + ":" + localName;
            dc.setDynamicAttribute(uri, localName, qName, value);
         } else if (element instanceof DynamicAttribute) {
            DynamicAttribute dc = (DynamicAttribute)element;
            dc.setDynamicAttribute(attributeName.toLowerCase(Locale.US), value);
         } else if (attributeName.indexOf(58) == -1) {
            String msg = this.getElementName(p, element) + " doesn't support the \"" + attributeName + "\" attribute.";
            throw new UnsupportedAttributeException(msg, attributeName);
         }
      } else {
         try {
            as.set(p, element, value);
         } catch (IllegalAccessException var11) {
            throw new BuildException(var11);
         } catch (InvocationTargetException var12) {
            Throwable t = var12.getTargetException();
            if (t instanceof BuildException) {
               throw (BuildException)t;
            } else {
               throw new BuildException(t);
            }
         }
      }
   }

   public void addText(Project project, Object element, String text) throws BuildException {
      if (this.addText == null) {
         text = text.trim();
         if (text.length() != 0) {
            String msg = project.getElementName(element) + " doesn't support nested text data (\"" + this.condenseText(text) + "\").";
            throw new BuildException(msg);
         }
      } else {
         try {
            this.addText.invoke(element, text);
         } catch (IllegalAccessException var6) {
            throw new BuildException(var6);
         } catch (InvocationTargetException var7) {
            Throwable t = var7.getTargetException();
            if (t instanceof BuildException) {
               throw (BuildException)t;
            } else {
               throw new BuildException(t);
            }
         }
      }
   }

   public void throwNotSupported(Project project, Object parent, String elementName) {
      String msg = project.getElementName(parent) + " doesn't support the nested \"" + elementName + "\" element.";
      throw new UnsupportedElementException(msg, elementName);
   }

   private IntrospectionHelper.NestedCreator getNestedCreator(Project project, String parentUri, Object parent, String elementName, UnknownElement child) throws BuildException {
      String uri = ProjectHelper.extractUriFromComponentName(elementName);
      String name = ProjectHelper.extractNameFromComponentName(elementName);
      if (uri.equals("antlib:org.apache.tools.ant")) {
         uri = "";
      }

      if (parentUri.equals("antlib:org.apache.tools.ant")) {
         parentUri = "";
      }

      IntrospectionHelper.NestedCreator nc = null;
      if (uri.equals(parentUri) || uri.equals("")) {
         nc = (IntrospectionHelper.NestedCreator)this.nestedCreators.get(name.toLowerCase(Locale.US));
      }

      if (nc == null) {
         nc = this.createAddTypeCreator(project, parent, elementName);
      }

      if (nc == null && parent instanceof DynamicElementNS) {
         DynamicElementNS dc = (DynamicElementNS)parent;
         String qName = child == null ? name : child.getQName();
         final Object nestedElement = dc.createDynamicElement(child == null ? "" : child.getNamespace(), name, qName);
         if (nestedElement != null) {
            nc = new IntrospectionHelper.NestedCreator((Method)null) {
               Object create(Project project, Object parent, Object ignore) {
                  return nestedElement;
               }
            };
         }
      }

      if (nc == null && parent instanceof DynamicElement) {
         DynamicElement dc = (DynamicElement)parent;
         final Object nestedElement = dc.createDynamicElement(name.toLowerCase(Locale.US));
         if (nestedElement != null) {
            nc = new IntrospectionHelper.NestedCreator((Method)null) {
               Object create(Project project, Object parent, Object ignore) {
                  return nestedElement;
               }
            };
         }
      }

      if (nc == null) {
         this.throwNotSupported(project, parent, elementName);
      }

      return nc;
   }

   /** @deprecated */
   public Object createElement(Project project, Object parent, String elementName) throws BuildException {
      IntrospectionHelper.NestedCreator nc = this.getNestedCreator(project, "", parent, elementName, (UnknownElement)null);

      try {
         Object nestedElement = nc.create(project, parent, (Object)null);
         if (project != null) {
            project.setProjectReference(nestedElement);
         }

         return nestedElement;
      } catch (IllegalAccessException var7) {
         throw new BuildException(var7);
      } catch (InstantiationException var8) {
         throw new BuildException(var8);
      } catch (InvocationTargetException var9) {
         Throwable t = var9.getTargetException();
         if (t instanceof BuildException) {
            throw (BuildException)t;
         } else {
            throw new BuildException(t);
         }
      }
   }

   public IntrospectionHelper.Creator getElementCreator(Project project, String parentUri, Object parent, String elementName, UnknownElement ue) {
      IntrospectionHelper.NestedCreator nc = this.getNestedCreator(project, parentUri, parent, elementName, ue);
      return new IntrospectionHelper.Creator(project, parent, nc);
   }

   public boolean isDynamic() {
      return (class$org$apache$tools$ant$DynamicElement == null ? (class$org$apache$tools$ant$DynamicElement = class$("org.apache.tools.ant.DynamicElement")) : class$org$apache$tools$ant$DynamicElement).isAssignableFrom(this.bean) || (class$org$apache$tools$ant$DynamicElementNS == null ? (class$org$apache$tools$ant$DynamicElementNS = class$("org.apache.tools.ant.DynamicElementNS")) : class$org$apache$tools$ant$DynamicElementNS).isAssignableFrom(this.bean);
   }

   public boolean isContainer() {
      return (class$org$apache$tools$ant$TaskContainer == null ? (class$org$apache$tools$ant$TaskContainer = class$("org.apache.tools.ant.TaskContainer")) : class$org$apache$tools$ant$TaskContainer).isAssignableFrom(this.bean);
   }

   public boolean supportsNestedElement(String elementName) {
      return this.nestedCreators.containsKey(elementName.toLowerCase(Locale.US)) || this.isDynamic() || this.addTypeMethods.size() != 0;
   }

   public boolean supportsNestedElement(String parentUri, String elementName) {
      if (parentUri.equals("antlib:org.apache.tools.ant")) {
         parentUri = "";
      }

      String uri = ProjectHelper.extractUriFromComponentName(elementName);
      if (uri.equals("antlib:org.apache.tools.ant")) {
         uri = "";
      }

      String name = ProjectHelper.extractNameFromComponentName(elementName);
      return this.nestedCreators.containsKey(name.toLowerCase(Locale.US)) && (uri.equals(parentUri) || "".equals(uri)) || this.isDynamic() || this.addTypeMethods.size() != 0;
   }

   public void storeElement(Project project, Object parent, Object child, String elementName) throws BuildException {
      if (elementName != null) {
         IntrospectionHelper.NestedCreator ns = (IntrospectionHelper.NestedCreator)this.nestedCreators.get(elementName.toLowerCase(Locale.US));
         if (ns != null) {
            try {
               ns.store(parent, child);
            } catch (IllegalAccessException var8) {
               throw new BuildException(var8);
            } catch (InstantiationException var9) {
               throw new BuildException(var9);
            } catch (InvocationTargetException var10) {
               Throwable t = var10.getTargetException();
               if (t instanceof BuildException) {
                  throw (BuildException)t;
               } else {
                  throw new BuildException(t);
               }
            }
         }
      }
   }

   public Class getElementType(String elementName) throws BuildException {
      Class nt = (Class)this.nestedTypes.get(elementName);
      if (nt == null) {
         throw new UnsupportedElementException("Class " + this.bean.getName() + " doesn't support the nested \"" + elementName + "\" element.", elementName);
      } else {
         return nt;
      }
   }

   public Class getAttributeType(String attributeName) throws BuildException {
      Class at = (Class)this.attributeTypes.get(attributeName);
      if (at == null) {
         throw new UnsupportedAttributeException("Class " + this.bean.getName() + " doesn't support the \"" + attributeName + "\" attribute.", attributeName);
      } else {
         return at;
      }
   }

   public Method getAddTextMethod() throws BuildException {
      if (!this.supportsCharacters()) {
         throw new BuildException("Class " + this.bean.getName() + " doesn't support nested text data.");
      } else {
         return this.addText;
      }
   }

   public Method getElementMethod(String elementName) throws BuildException {
      Object creator = this.nestedCreators.get(elementName);
      if (creator == null) {
         throw new UnsupportedElementException("Class " + this.bean.getName() + " doesn't support the nested \"" + elementName + "\" element.", elementName);
      } else {
         return ((IntrospectionHelper.NestedCreator)creator).method;
      }
   }

   public Method getAttributeMethod(String attributeName) throws BuildException {
      Object setter = this.attributeSetters.get(attributeName);
      if (setter == null) {
         throw new UnsupportedAttributeException("Class " + this.bean.getName() + " doesn't support the \"" + attributeName + "\" attribute.", attributeName);
      } else {
         return ((IntrospectionHelper.AttributeSetter)setter).method;
      }
   }

   public boolean supportsCharacters() {
      return this.addText != null;
   }

   public Enumeration getAttributes() {
      return this.attributeSetters.keys();
   }

   public Map getAttributeMap() {
      return this.attributeTypes.size() < 1 ? EMPTY_MAP : Collections.unmodifiableMap(this.attributeTypes);
   }

   public Enumeration getNestedElements() {
      return this.nestedTypes.keys();
   }

   public Map getNestedElementMap() {
      return this.nestedTypes.size() < 1 ? EMPTY_MAP : Collections.unmodifiableMap(this.nestedTypes);
   }

   public List getExtensionPoints() {
      return this.addTypeMethods.size() < 1 ? Collections.EMPTY_LIST : Collections.unmodifiableList(this.addTypeMethods);
   }

   private IntrospectionHelper.AttributeSetter createAttributeSetter(final Method m, Class arg, final String attrName) {
      final Class reflectedArg = PRIMITIVE_TYPE_MAP.containsKey(arg) ? (Class)PRIMITIVE_TYPE_MAP.get(arg) : arg;
      if ((class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String).equals(reflectedArg)) {
         return new IntrospectionHelper.AttributeSetter(m) {
            public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException {
               m.invoke(parent, (Object[])(new String[]{value}));
            }
         };
      } else if ((class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character).equals(reflectedArg)) {
         return new IntrospectionHelper.AttributeSetter(m) {
            public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException {
               if (value.length() == 0) {
                  throw new BuildException("The value \"\" is not a legal value for attribute \"" + attrName + "\"");
               } else {
                  m.invoke(parent, (Object[])(new Character[]{new Character(value.charAt(0))}));
               }
            }
         };
      } else if ((class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean).equals(reflectedArg)) {
         return new IntrospectionHelper.AttributeSetter(m) {
            public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException {
               m.invoke(parent, (Object[])(new Boolean[]{Project.toBoolean(value) ? Boolean.TRUE : Boolean.FALSE}));
            }
         };
      } else if ((class$java$lang$Class == null ? (class$java$lang$Class = class$("java.lang.Class")) : class$java$lang$Class).equals(reflectedArg)) {
         return new IntrospectionHelper.AttributeSetter(m) {
            public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException {
               try {
                  m.invoke(parent, Class.forName(value));
               } catch (ClassNotFoundException var5) {
                  throw new BuildException(var5);
               }
            }
         };
      } else if ((class$java$io$File == null ? (class$java$io$File = class$("java.io.File")) : class$java$io$File).equals(reflectedArg)) {
         return new IntrospectionHelper.AttributeSetter(m) {
            public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException {
               m.invoke(parent, p.resolveFile(value));
            }
         };
      } else if ((class$org$apache$tools$ant$types$EnumeratedAttribute == null ? (class$org$apache$tools$ant$types$EnumeratedAttribute = class$("org.apache.tools.ant.types.EnumeratedAttribute")) : class$org$apache$tools$ant$types$EnumeratedAttribute).isAssignableFrom(reflectedArg)) {
         return new IntrospectionHelper.AttributeSetter(m) {
            public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException {
               try {
                  EnumeratedAttribute ea = (EnumeratedAttribute)reflectedArg.newInstance();
                  ea.setValue(value);
                  m.invoke(parent, ea);
               } catch (InstantiationException var5) {
                  throw new BuildException(var5);
               }
            }
         };
      } else if (reflectedArg.getSuperclass() != null && reflectedArg.getSuperclass().getName().equals("java.lang.Enum")) {
         return new IntrospectionHelper.AttributeSetter(m) {
            public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException {
               try {
                  m.invoke(parent, reflectedArg.getMethod("valueOf", IntrospectionHelper.class$java$lang$String == null ? (IntrospectionHelper.class$java$lang$String = IntrospectionHelper.class$("java.lang.String")) : IntrospectionHelper.class$java$lang$String).invoke((Object)null, value));
               } catch (InvocationTargetException var5) {
                  if (var5.getTargetException() instanceof IllegalArgumentException) {
                     throw new BuildException("'" + value + "' is not a permitted value for " + reflectedArg.getName());
                  } else {
                     throw new BuildException(var5.getTargetException());
                  }
               } catch (Exception var6) {
                  throw new BuildException(var6);
               }
            }
         };
      } else {
         final Constructor c;
         final boolean includeProject;
         try {
            c = reflectedArg.getConstructor(class$org$apache$tools$ant$Project == null ? (class$org$apache$tools$ant$Project = class$("org.apache.tools.ant.Project")) : class$org$apache$tools$ant$Project, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            includeProject = true;
         } catch (NoSuchMethodException var10) {
            try {
               c = reflectedArg.getConstructor(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               includeProject = false;
            } catch (NoSuchMethodException var9) {
               return null;
            }
         }

         return new IntrospectionHelper.AttributeSetter(m) {
            public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException {
               try {
                  Object[] args = includeProject ? new Object[]{p, value} : new Object[]{value};
                  Object attribute = c.newInstance(args);
                  if (p != null) {
                     p.setProjectReference(attribute);
                  }

                  m.invoke(parent, attribute);
               } catch (InstantiationException var6) {
                  throw new BuildException(var6);
               }
            }
         };
      }
   }

   protected String getElementName(Project project, Object element) {
      return project.getElementName(element);
   }

   private String getPropertyName(String methodName, String prefix) {
      return methodName.substring(prefix.length()).toLowerCase(Locale.US);
   }

   public static void clearCache() {
      HELPERS.clear();
   }

   private IntrospectionHelper.NestedCreator createAddTypeCreator(Project project, Object parent, String elementName) throws BuildException {
      if (this.addTypeMethods.size() == 0) {
         return null;
      } else {
         ComponentHelper helper = ComponentHelper.getComponentHelper(project);
         final Object addedObject = null;
         Method addMethod = null;
         Class clazz = helper.getComponentClass(elementName);
         if (clazz == null) {
            return null;
         } else {
            addMethod = this.findMatchingMethod(clazz, this.addTypeMethods);
            if (addMethod == null) {
               return null;
            } else {
               addedObject = helper.createComponent(elementName);
               if (addedObject == null) {
                  return null;
               } else {
                  final Object rObject = addedObject;
                  if (addedObject instanceof PreSetDef.PreSetDefinition) {
                     rObject = ((PreSetDef.PreSetDefinition)addedObject).createObject(project);
                  }

                  return new IntrospectionHelper.NestedCreator(addMethod) {
                     Object create(Project project, Object parent, Object ignore) throws InvocationTargetException, IllegalAccessException {
                        if (!this.getMethod().getName().endsWith("Configured")) {
                           this.getMethod().invoke(parent, rObject);
                        }

                        return addedObject;
                     }

                     Object getRealObject() {
                        return rObject;
                     }

                     void store(Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
                        if (this.getMethod().getName().endsWith("Configured")) {
                           this.getMethod().invoke(parent, rObject);
                        }

                     }
                  };
               }
            }
         }
      }
   }

   private void insertAddTypeMethod(Method method) {
      Class argClass = method.getParameterTypes()[0];

      for(int c = 0; c < this.addTypeMethods.size(); ++c) {
         Method current = (Method)this.addTypeMethods.get(c);
         if (current.getParameterTypes()[0].equals(argClass)) {
            if (method.getName().equals("addConfigured")) {
               this.addTypeMethods.set(c, method);
            }

            return;
         }

         if (current.getParameterTypes()[0].isAssignableFrom(argClass)) {
            this.addTypeMethods.add(c, method);
            return;
         }
      }

      this.addTypeMethods.add(method);
   }

   private Method findMatchingMethod(Class paramClass, List methods) {
      Class matchedClass = null;
      Method matchedMethod = null;

      for(int i = 0; i < methods.size(); ++i) {
         Method method = (Method)methods.get(i);
         Class methodClass = method.getParameterTypes()[0];
         if (methodClass.isAssignableFrom(paramClass)) {
            if (matchedClass == null) {
               matchedClass = methodClass;
               matchedMethod = method;
            } else if (!methodClass.isAssignableFrom(matchedClass)) {
               throw new BuildException("ambiguous: types " + matchedClass.getName() + " and " + methodClass.getName() + " match " + paramClass.getName());
            }
         }
      }

      return matchedMethod;
   }

   private String condenseText(String text) {
      if (text.length() <= 20) {
         return text;
      } else {
         int ends = (20 - "...".length()) / 2;
         return (new StringBuffer(text)).replace(ends, text.length() - ends, "...").toString();
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

   static {
      Class[] primitives = new Class[]{Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};
      Class[] wrappers = new Class[]{class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean, class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte, class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character, class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short, class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer, class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long, class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float, class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double};

      for(int i = 0; i < primitives.length; ++i) {
         PRIMITIVE_TYPE_MAP.put(primitives[i], wrappers[i]);
      }

   }

   private abstract static class AttributeSetter {
      private Method method;

      AttributeSetter(Method m) {
         this.method = m;
      }

      abstract void set(Project var1, Object var2, String var3) throws InvocationTargetException, IllegalAccessException, BuildException;
   }

   private class AddNestedCreator extends IntrospectionHelper.NestedCreator {
      static final int ADD = 1;
      static final int ADD_CONFIGURED = 2;
      private Constructor constructor;
      private int behavior;

      AddNestedCreator(Method m, Constructor c, int behavior) {
         super(m);
         this.constructor = c;
         this.behavior = behavior;
      }

      boolean isPolyMorphic() {
         return true;
      }

      Object create(Project project, Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
         if (child == null) {
            child = this.constructor.newInstance(this.constructor.getParameterTypes().length == 0 ? new Object[0] : new Object[]{project});
         }

         if (child instanceof PreSetDef.PreSetDefinition) {
            child = ((PreSetDef.PreSetDefinition)child).createObject(project);
         }

         if (this.behavior == 1) {
            this.istore(parent, child);
         }

         return child;
      }

      void store(Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
         if (this.behavior == 2) {
            this.istore(parent, child);
         }

      }

      private void istore(Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
         this.getMethod().invoke(parent, child);
      }
   }

   private class CreateNestedCreator extends IntrospectionHelper.NestedCreator {
      CreateNestedCreator(Method m) {
         super(m);
      }

      Object create(Project project, Object parent, Object ignore) throws InvocationTargetException, IllegalAccessException {
         return this.getMethod().invoke(parent);
      }
   }

   private abstract static class NestedCreator {
      private Method method;

      NestedCreator(Method m) {
         this.method = m;
      }

      Method getMethod() {
         return this.method;
      }

      boolean isPolyMorphic() {
         return false;
      }

      Object getRealObject() {
         return null;
      }

      abstract Object create(Project var1, Object var2, Object var3) throws InvocationTargetException, IllegalAccessException, InstantiationException;

      void store(Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
      }
   }

   public static final class Creator {
      private IntrospectionHelper.NestedCreator nestedCreator;
      private Object parent;
      private Project project;
      private Object nestedObject;
      private String polyType;

      private Creator(Project project, Object parent, IntrospectionHelper.NestedCreator nestedCreator) {
         this.project = project;
         this.parent = parent;
         this.nestedCreator = nestedCreator;
      }

      public void setPolyType(String polyType) {
         this.polyType = polyType;
      }

      public Object create() {
         if (this.polyType != null) {
            if (!this.nestedCreator.isPolyMorphic()) {
               throw new BuildException("Not allowed to use the polymorphic form for this element");
            }

            ComponentHelper helper = ComponentHelper.getComponentHelper(this.project);
            this.nestedObject = helper.createComponent(this.polyType);
            if (this.nestedObject == null) {
               throw new BuildException("Unable to create object of type " + this.polyType);
            }
         }

         try {
            this.nestedObject = this.nestedCreator.create(this.project, this.parent, this.nestedObject);
            if (this.project != null) {
               this.project.setProjectReference(this.nestedObject);
            }

            return this.nestedObject;
         } catch (IllegalAccessException var3) {
            throw new BuildException(var3);
         } catch (InstantiationException var4) {
            throw new BuildException(var4);
         } catch (IllegalArgumentException var5) {
            if (this.polyType != null) {
               throw new BuildException("Invalid type used " + this.polyType);
            } else {
               throw var5;
            }
         } catch (InvocationTargetException var6) {
            Throwable t = var6.getTargetException();
            if (t instanceof BuildException) {
               throw (BuildException)t;
            } else {
               throw new BuildException(t);
            }
         }
      }

      public Object getRealObject() {
         return this.nestedCreator.getRealObject();
      }

      public void store() {
         try {
            this.nestedCreator.store(this.parent, this.nestedObject);
         } catch (IllegalAccessException var3) {
            throw new BuildException(var3);
         } catch (InstantiationException var4) {
            throw new BuildException(var4);
         } catch (IllegalArgumentException var5) {
            if (this.polyType != null) {
               throw new BuildException("Invalid type used " + this.polyType);
            } else {
               throw var5;
            }
         } catch (InvocationTargetException var6) {
            Throwable t = var6.getTargetException();
            if (t instanceof BuildException) {
               throw (BuildException)t;
            } else {
               throw new BuildException(t);
            }
         }
      }

      // $FF: synthetic method
      Creator(Project x0, Object x1, IntrospectionHelper.NestedCreator x2, Object x3) {
         this(x0, x1, x2);
      }
   }
}
