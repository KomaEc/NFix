package org.codehaus.groovy.tools.groovydoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.groovy.groovydoc.GroovyAnnotationRef;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyConstructorDoc;
import org.codehaus.groovy.groovydoc.GroovyFieldDoc;
import org.codehaus.groovy.groovydoc.GroovyMethodDoc;
import org.codehaus.groovy.groovydoc.GroovyPackageDoc;
import org.codehaus.groovy.groovydoc.GroovyParameter;
import org.codehaus.groovy.groovydoc.GroovyRootDoc;
import org.codehaus.groovy.groovydoc.GroovyType;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class SimpleGroovyClassDoc extends SimpleGroovyAbstractableElementDoc implements GroovyClassDoc {
   public static final Pattern TAG_REGEX = Pattern.compile("(?sm)\\s*@([a-zA-Z.]+)\\s+(.*?)(?=\\s+@)");
   public static final Pattern LINK_REGEX = Pattern.compile("(?m)[{]@(link)\\s+([^}]*)}");
   public static final Pattern CODE_REGEX = Pattern.compile("(?m)[{]@(code)\\s+([^}]*)}");
   public static final Pattern REF_LABEL_REGEX = Pattern.compile("([\\w.#]*(\\(.*\\))?)(\\s(.*))?");
   public static final Pattern NAME_ARGS_REGEX = Pattern.compile("([^(]+)\\(([^)]*)\\)");
   public static final Pattern SPLIT_ARGS_REGEX = Pattern.compile(",\\s*");
   private static final List<String> PRIMITIVES = Arrays.asList("void", "boolean", "byte", "short", "char", "int", "long", "float", "double");
   private static final Map<String, String> TAG_TEXT = new HashMap();
   private final List<GroovyConstructorDoc> constructors;
   private final List<GroovyFieldDoc> fields;
   private final List<GroovyFieldDoc> properties;
   private final List<GroovyFieldDoc> enumConstants;
   private final List<GroovyMethodDoc> methods;
   private final List<String> importedClassesAndPackages;
   private final List<String> interfaceNames;
   private final List<GroovyClassDoc> interfaceClasses;
   private final List<GroovyClassDoc> nested;
   private final List<LinkArgument> links;
   private GroovyClassDoc superClass;
   private GroovyClassDoc outer;
   private String superClassName;
   private String fullPathName;
   private boolean isgroovy;
   private GroovyRootDoc savedRootDoc;

   public SimpleGroovyClassDoc(List<String> importedClassesAndPackages, String name, List<LinkArgument> links) {
      super(name);
      this.savedRootDoc = null;
      this.importedClassesAndPackages = importedClassesAndPackages;
      this.links = links;
      this.constructors = new ArrayList();
      this.fields = new ArrayList();
      this.properties = new ArrayList();
      this.enumConstants = new ArrayList();
      this.methods = new ArrayList();
      this.interfaceNames = new ArrayList();
      this.interfaceClasses = new ArrayList();
      this.nested = new ArrayList();
   }

   public SimpleGroovyClassDoc(List<String> importedClassesAndPackages, String name) {
      this(importedClassesAndPackages, name, new ArrayList());
   }

   public GroovyConstructorDoc[] constructors() {
      Collections.sort(this.constructors);
      return (GroovyConstructorDoc[])this.constructors.toArray(new GroovyConstructorDoc[this.constructors.size()]);
   }

   public boolean add(GroovyConstructorDoc constructor) {
      return this.constructors.add(constructor);
   }

   public GroovyClassDoc getOuter() {
      return this.outer;
   }

   public void setOuter(GroovyClassDoc outer) {
      this.outer = outer;
   }

   public boolean isGroovy() {
      return this.isgroovy;
   }

   public void setGroovy(boolean isgroovy) {
      this.isgroovy = isgroovy;
   }

   public GroovyClassDoc[] innerClasses() {
      Collections.sort(this.nested);
      return (GroovyClassDoc[])this.nested.toArray(new GroovyClassDoc[this.nested.size()]);
   }

   public boolean addNested(GroovyClassDoc nestedClass) {
      return this.nested.add(nestedClass);
   }

   public GroovyFieldDoc[] fields() {
      Collections.sort(this.fields);
      return (GroovyFieldDoc[])this.fields.toArray(new GroovyFieldDoc[this.fields.size()]);
   }

   public boolean add(GroovyFieldDoc field) {
      return this.fields.add(field);
   }

   public GroovyFieldDoc[] properties() {
      Collections.sort(this.properties);
      return (GroovyFieldDoc[])this.properties.toArray(new GroovyFieldDoc[this.properties.size()]);
   }

   public boolean addProperty(GroovyFieldDoc property) {
      return this.properties.add(property);
   }

   public GroovyFieldDoc[] enumConstants() {
      Collections.sort(this.enumConstants);
      return (GroovyFieldDoc[])this.enumConstants.toArray(new GroovyFieldDoc[this.enumConstants.size()]);
   }

   public boolean addEnumConstant(GroovyFieldDoc field) {
      return this.enumConstants.add(field);
   }

   public GroovyMethodDoc[] methods() {
      Collections.sort(this.methods);
      return (GroovyMethodDoc[])this.methods.toArray(new GroovyMethodDoc[this.methods.size()]);
   }

   public boolean add(GroovyMethodDoc method) {
      return this.methods.add(method);
   }

   public String getSuperClassName() {
      return this.superClassName;
   }

   public void setSuperClassName(String className) {
      this.superClassName = className;
   }

   public GroovyClassDoc superclass() {
      return this.superClass;
   }

   public void setSuperClass(GroovyClassDoc doc) {
      this.superClass = doc;
   }

   public String getFullPathName() {
      return this.fullPathName;
   }

   public void setFullPathName(String fullPathName) {
      this.fullPathName = fullPathName;
   }

   public String getRelativeRootPath() {
      StringTokenizer tokenizer = new StringTokenizer(this.fullPathName, "/");
      StringBuffer sb = new StringBuffer();
      if (tokenizer.hasMoreTokens()) {
         tokenizer.nextToken();
      }

      while(tokenizer.hasMoreTokens()) {
         tokenizer.nextToken();
         sb.append("../");
      }

      return sb.toString();
   }

   public List<GroovyClassDoc> getParentClasses() {
      List<GroovyClassDoc> result = new LinkedList();
      if (this.isInterface()) {
         return result;
      } else {
         result.add(0, this);
         Object next = this;

         while(((GroovyClassDoc)next).superclass() != null && !"java.lang.Object".equals(((GroovyClassDoc)next).qualifiedTypeName())) {
            next = ((GroovyClassDoc)next).superclass();
            result.add(0, next);
         }

         GroovyClassDoc prev = next;

         ExternalGroovyClassDoc nextDoc;
         for(Class nextClass = this.getClassOf(((GroovyClassDoc)next).qualifiedTypeName()); nextClass != null && nextClass.getSuperclass() != null && !Object.class.equals(nextClass); prev = nextDoc) {
            nextClass = nextClass.getSuperclass();
            nextDoc = new ExternalGroovyClassDoc(nextClass);
            if (prev instanceof SimpleGroovyClassDoc) {
               SimpleGroovyClassDoc parent = (SimpleGroovyClassDoc)prev;
               parent.setSuperClass(nextDoc);
            }

            result.add(0, nextDoc);
         }

         if (!((GroovyClassDoc)result.get(0)).qualifiedTypeName().equals("java.lang.Object")) {
            result.add(0, new ExternalGroovyClassDoc(Object.class));
         }

         return result;
      }
   }

   public Set<GroovyClassDoc> getParentInterfaces() {
      Set<GroovyClassDoc> result = new HashSet();
      result.add(this);
      Set<GroovyClassDoc> next = new HashSet();
      ((Set)next).addAll(Arrays.asList(this.interfaces()));

      while(((Set)next).size() > 0) {
         Set<GroovyClassDoc> temp = next;
         Set<GroovyClassDoc> next = new HashSet();
         Iterator i$ = ((Set)temp).iterator();

         while(i$.hasNext()) {
            GroovyClassDoc t = (GroovyClassDoc)i$.next();
            if (t instanceof SimpleGroovyClassDoc) {
               next.addAll(((SimpleGroovyClassDoc)t).getParentInterfaces());
            } else if (t instanceof ExternalGroovyClassDoc) {
               ExternalGroovyClassDoc d = (ExternalGroovyClassDoc)t;
               next.addAll(this.getJavaInterfaces(d));
            }
         }

         next = DefaultGroovyMethods.minus((Set)next, (Collection)result);
         result.addAll((Collection)next);
      }

      return result;
   }

   private Set<GroovyClassDoc> getJavaInterfaces(ExternalGroovyClassDoc d) {
      Set<GroovyClassDoc> result = new HashSet();
      Class[] interfaces = d.externalClass().getInterfaces();
      if (interfaces != null) {
         Class[] arr$ = interfaces;
         int len$ = interfaces.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class i = arr$[i$];
            ExternalGroovyClassDoc doc = new ExternalGroovyClassDoc(i);
            result.add(doc);
            result.addAll(this.getJavaInterfaces(doc));
         }
      }

      return result;
   }

   private Class getClassOf(String next) {
      try {
         return Class.forName(next.replace("/", "."));
      } catch (Throwable var3) {
         return null;
      }
   }

   void resolve(GroovyRootDoc rootDoc) {
      this.savedRootDoc = rootDoc;
      Map visibleClasses = rootDoc.getVisibleClasses(this.importedClassesAndPackages);
      Iterator i$ = this.constructors.iterator();

      while(i$.hasNext()) {
         GroovyConstructorDoc constructor = (GroovyConstructorDoc)i$.next();
         GroovyParameter[] arr$ = constructor.parameters();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            GroovyParameter groovyParameter = arr$[i$];
            SimpleGroovyParameter param = (SimpleGroovyParameter)groovyParameter;
            String paramTypeName = param.typeName();
            if (visibleClasses.containsKey(paramTypeName)) {
               param.setType((GroovyType)visibleClasses.get(paramTypeName));
            } else {
               GroovyClassDoc doc = this.resolveClass(rootDoc, paramTypeName);
               if (doc != null) {
                  param.setType(doc);
               }
            }
         }
      }

      i$ = this.fields.iterator();

      while(i$.hasNext()) {
         GroovyFieldDoc field = (GroovyFieldDoc)i$.next();
         SimpleGroovyFieldDoc mutableField = (SimpleGroovyFieldDoc)field;
         GroovyType fieldType = field.type();
         String typeName = fieldType.typeName();
         if (visibleClasses.containsKey(typeName)) {
            mutableField.setType((GroovyType)visibleClasses.get(typeName));
         } else {
            GroovyClassDoc doc = this.resolveClass(rootDoc, typeName);
            if (doc != null) {
               mutableField.setType(doc);
            }
         }
      }

      i$ = this.methods.iterator();

      while(i$.hasNext()) {
         GroovyMethodDoc method = (GroovyMethodDoc)i$.next();
         GroovyType returnType = method.returnType();
         String typeName = returnType.typeName();
         if (visibleClasses.containsKey(typeName)) {
            method.setReturnType((GroovyType)visibleClasses.get(typeName));
         } else {
            GroovyClassDoc doc = this.resolveClass(rootDoc, typeName);
            if (doc != null) {
               method.setReturnType(doc);
            }
         }

         GroovyParameter[] arr$ = method.parameters();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            GroovyParameter groovyParameter = arr$[i$];
            SimpleGroovyParameter param = (SimpleGroovyParameter)groovyParameter;
            String paramTypeName = param.typeName();
            if (visibleClasses.containsKey(paramTypeName)) {
               param.setType((GroovyType)visibleClasses.get(paramTypeName));
            } else {
               GroovyClassDoc doc = this.resolveClass(rootDoc, paramTypeName);
               if (doc != null) {
                  param.setType(doc);
               }
            }
         }
      }

      if (this.superClassName != null && this.superClass == null) {
         this.superClass = this.resolveClass(rootDoc, this.superClassName);
      }

      i$ = this.interfaceNames.iterator();

      while(i$.hasNext()) {
         String name = (String)i$.next();
         this.interfaceClasses.add(this.resolveClass(rootDoc, name));
      }

      GroovyAnnotationRef[] arr$ = this.annotations();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         GroovyAnnotationRef annotation = arr$[i$];
         SimpleGroovyAnnotationRef ref = (SimpleGroovyAnnotationRef)annotation;
         ref.setType(this.resolveClass(rootDoc, ref.name()));
      }

   }

   public String getDocUrl(String type) {
      return this.getDocUrl(type, false);
   }

   public String getDocUrl(String type, boolean full) {
      return getDocUrl(type, full, this.links, this.getRelativeRootPath(), this.savedRootDoc, this);
   }

   private static String resolveMethodArgs(GroovyRootDoc rootDoc, SimpleGroovyClassDoc classDoc, String type) {
      if (type.indexOf("(") < 0) {
         return type;
      } else {
         Matcher m = NAME_ARGS_REGEX.matcher(type);
         if (m.matches()) {
            String name = m.group(1);
            String args = m.group(2);
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append("(");
            String[] argParts = SPLIT_ARGS_REGEX.split(args);
            boolean first = true;
            String[] arr$ = argParts;
            int len$ = argParts.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String argPart = arr$[i$];
               if (first) {
                  first = false;
               } else {
                  sb.append(", ");
               }

               GroovyClassDoc doc = classDoc.resolveClass(rootDoc, argPart);
               sb.append(doc == null ? argPart : doc.qualifiedTypeName());
            }

            sb.append(")");
            return sb.toString();
         } else {
            return type;
         }
      }
   }

   public static String getDocUrl(String type, boolean full, List<LinkArgument> links, String relativePath, GroovyRootDoc rootDoc, SimpleGroovyClassDoc classDoc) {
      if (type == null) {
         return type;
      } else {
         type = type.trim();
         if (isPrimitiveType(type)) {
            return type;
         } else {
            if (type.equals("def")) {
               type = "java.lang.Object def";
            }

            String label = null;
            Matcher matcher = REF_LABEL_REGEX.matcher(type);
            if (matcher.find()) {
               type = matcher.group(1);
               label = matcher.group(4);
            }

            if (type.startsWith("#")) {
               return "<a href='" + resolveMethodArgs(rootDoc, classDoc, type) + "'>" + (label == null ? type.substring(1) : label) + "</a>";
            } else if (type.endsWith("[]")) {
               return label != null ? getDocUrl(type.substring(0, type.length() - 2) + " " + label, full, links, relativePath, rootDoc, classDoc) : getDocUrl(type.substring(0, type.length() - 2), full, links, relativePath, rootDoc, classDoc) + "[]";
            } else {
               String[] target;
               String shortClassName;
               if (type.indexOf(46) == -1 && classDoc != null) {
                  target = type.split("#");
                  shortClassName = target[0];
                  Class c = classDoc.resolveExternalClassFromImport(shortClassName);
                  if (c != null) {
                     type = c.getName();
                  }

                  if (target.length > 1) {
                     type = type + "#" + target[1];
                  }

                  type = resolveMethodArgs(rootDoc, classDoc, type);
               }

               if (type.indexOf(46) == -1) {
                  return type;
               } else {
                  target = type.split("#");
                  shortClassName = target[0].replaceAll(".*\\.", "");
                  shortClassName = shortClassName + (target.length > 1 ? "#" + target[1].split("\\(")[0] : "");
                  String name = (full ? target[0] : shortClassName).replaceAll("#", ".");
                  if (rootDoc != null) {
                     String slashedName = target[0].replaceAll("\\.", "/");
                     GroovyClassDoc doc = rootDoc.classNamed(slashedName);
                     if (doc != null) {
                        return buildUrl(relativePath, target, label == null ? name : label);
                     }
                  }

                  Iterator i$ = links.iterator();

                  while(i$.hasNext()) {
                     LinkArgument link = (LinkArgument)i$.next();
                     StringTokenizer tokenizer = new StringTokenizer(link.getPackages(), ", ");

                     while(tokenizer.hasMoreTokens()) {
                        String token = tokenizer.nextToken();
                        if (type.startsWith(token)) {
                           return buildUrl(link.getHref(), target, label == null ? name : label);
                        }
                     }
                  }

                  return type;
               }
            }
         }
      }
   }

   private static String buildUrl(String relativeRoot, String[] target, String shortClassName) {
      if (!relativeRoot.endsWith("/")) {
         relativeRoot = relativeRoot + "/";
      }

      String url = relativeRoot + target[0].replace('.', '/') + ".html" + (target.length > 1 ? "#" + target[1] : "");
      return "<a href='" + url + "' title='" + shortClassName + "'>" + shortClassName + "</a>";
   }

   private GroovyClassDoc resolveClass(GroovyRootDoc rootDoc, String name) {
      if (isPrimitiveType(name)) {
         return null;
      } else {
         GroovyClassDoc doc = ((SimpleGroovyRootDoc)rootDoc).classNamedExact(name);
         if (doc != null) {
            return doc;
         } else {
            int slashIndex = name.lastIndexOf("/");
            GroovyClassDoc nestedDoc;
            if (slashIndex < 1) {
               doc = this.resolveInternalClassDocFromImport(rootDoc, name);
               if (doc != null) {
                  return doc;
               }

               Iterator i$ = this.nested.iterator();

               while(i$.hasNext()) {
                  nestedDoc = (GroovyClassDoc)i$.next();
                  if (nestedDoc.name().endsWith("." + name)) {
                     return nestedDoc;
                  }
               }

               doc = rootDoc.classNamed(name);
               if (doc != null) {
                  return doc;
               }
            }

            String shortname = name;
            nestedDoc = null;
            Class c;
            if (slashIndex > 0) {
               shortname = name.substring(slashIndex + 1);
               c = this.resolveExternalFullyQualifiedClass(name);
            } else {
               c = this.resolveExternalClassFromImport(name);
            }

            if (c != null) {
               return new ExternalGroovyClassDoc(c);
            } else {
               SimpleGroovyClassDoc placeholder = new SimpleGroovyClassDoc((List)null, shortname);
               placeholder.setFullPathName(name);
               return placeholder;
            }
         }
      }
   }

   private static boolean isPrimitiveType(String name) {
      String type = name;
      if (name.endsWith("[]")) {
         type = name.substring(0, name.length() - 2);
      }

      return PRIMITIVES.contains(type);
   }

   private GroovyClassDoc resolveInternalClassDocFromImport(GroovyRootDoc rootDoc, String baseName) {
      if (isPrimitiveType(baseName)) {
         return null;
      } else {
         Iterator i$ = this.importedClassesAndPackages.iterator();

         while(i$.hasNext()) {
            String importName = (String)i$.next();
            GroovyClassDoc doc;
            if (importName.endsWith("/" + baseName)) {
               doc = ((SimpleGroovyRootDoc)rootDoc).classNamedExact(importName);
               if (doc != null) {
                  return doc;
               }
            } else if (importName.endsWith("/*")) {
               doc = ((SimpleGroovyRootDoc)rootDoc).classNamedExact(importName.substring(0, importName.length() - 2) + baseName);
               if (doc != null) {
                  return doc;
               }
            }
         }

         return null;
      }
   }

   private Class resolveExternalClassFromImport(String name) {
      if (isPrimitiveType(name)) {
         return null;
      } else {
         Iterator i$ = this.importedClassesAndPackages.iterator();

         while(i$.hasNext()) {
            String importName = (String)i$.next();
            String candidate = null;
            if (importName.endsWith("/" + name)) {
               candidate = importName.replaceAll("/", ".");
            } else if (importName.endsWith("/*")) {
               candidate = importName.substring(0, importName.length() - 2).replace('/', '.') + "." + name;
            }

            if (candidate != null) {
               try {
                  return Class.forName(candidate);
               } catch (NoClassDefFoundError var6) {
               } catch (ClassNotFoundException var7) {
               }
            }
         }

         return null;
      }
   }

   private Class resolveExternalFullyQualifiedClass(String name) {
      String candidate = name.replace('/', '.');

      try {
         return Class.forName(candidate);
      } catch (NoClassDefFoundError var4) {
      } catch (ClassNotFoundException var5) {
      }

      return null;
   }

   public GroovyConstructorDoc[] constructors(boolean filter) {
      return null;
   }

   public boolean definesSerializableFields() {
      return false;
   }

   public GroovyFieldDoc[] fields(boolean filter) {
      return null;
   }

   public GroovyClassDoc findClass(String className) {
      return null;
   }

   public GroovyClassDoc[] importedClasses() {
      return null;
   }

   public GroovyPackageDoc[] importedPackages() {
      return null;
   }

   public GroovyClassDoc[] innerClasses(boolean filter) {
      return null;
   }

   public GroovyClassDoc[] interfaces() {
      Collections.sort(this.interfaceClasses);
      return (GroovyClassDoc[])this.interfaceClasses.toArray(new GroovyClassDoc[this.interfaceClasses.size()]);
   }

   public GroovyType[] interfaceTypes() {
      return null;
   }

   public boolean isExternalizable() {
      return false;
   }

   public boolean isSerializable() {
      return false;
   }

   public GroovyMethodDoc[] methods(boolean filter) {
      return null;
   }

   public GroovyFieldDoc[] serializableFields() {
      return null;
   }

   public GroovyMethodDoc[] serializationMethods() {
      return null;
   }

   public boolean subclassOf(GroovyClassDoc gcd) {
      return false;
   }

   public GroovyType superclassType() {
      return null;
   }

   public boolean isPrimitive() {
      return false;
   }

   public String qualifiedTypeName() {
      String qtnWithSlashes = this.fullPathName.startsWith("DefaultPackage/") ? this.fullPathName.substring("DefaultPackage/".length()) : this.fullPathName;
      return qtnWithSlashes.replace('/', '.');
   }

   public String simpleTypeName() {
      String typeName = this.qualifiedTypeName();
      int lastDot = typeName.lastIndexOf(46);
      return lastDot < 0 ? typeName : typeName.substring(lastDot + 1);
   }

   public String typeName() {
      return null;
   }

   public void addInterfaceName(String className) {
      this.interfaceNames.add(className);
   }

   public String firstSentenceCommentText() {
      if (super.firstSentenceCommentText() == null) {
         this.setFirstSentenceCommentText(this.replaceTags(calculateFirstSentence(this.getRawCommentText())));
      }

      return super.firstSentenceCommentText();
   }

   public String commentText() {
      if (super.commentText() == null) {
         this.setCommentText(this.replaceTags(this.getRawCommentText()));
      }

      return super.commentText();
   }

   public String replaceTags(String comment) {
      String result = comment.replaceAll("(?m)^\\s*\\*", "");
      result = this.replaceAllTags(result, "", "", LINK_REGEX);
      result = this.replaceAllTags(result, "<TT>", "</TT>", CODE_REGEX);
      result = this.replaceAllTagsCollated(result, "<DL><DT><B>", ":</B></DT><DD>", "</DD><DD>", "</DD></DL>", TAG_REGEX);
      return decodeSpecialSymbols(result);
   }

   public String replaceAllTags(String self, String s1, String s2, Pattern regex) {
      Matcher matcher = regex.matcher(self);
      if (matcher.find()) {
         matcher.reset();
         StringBuffer sb = new StringBuffer();

         while(matcher.find()) {
            String tagname = matcher.group(1);
            if (!tagname.equals("interface")) {
               String content = encodeSpecialSymbols(matcher.group(2));
               if (tagname.equals("link")) {
                  content = this.getDocUrl(content);
               }

               matcher.appendReplacement(sb, s1 + content + s2);
            }
         }

         matcher.appendTail(sb);
         return sb.toString();
      } else {
         return self;
      }
   }

   public String replaceAllTagsCollated(String self, String preKey, String postKey, String valueSeparator, String postValues, Pattern regex) {
      Matcher matcher = regex.matcher(self + "@endMarker");
      if (!matcher.find()) {
         return self;
      } else {
         matcher.reset();
         Map<String, List<String>> savedTags = new HashMap();
         StringBuffer sb = new StringBuffer();

         while(matcher.find()) {
            String tagname = matcher.group(1);
            if (!tagname.equals("interface")) {
               String content = encodeSpecialSymbols(matcher.group(2));
               if ("see".equals(tagname)) {
                  content = this.getDocUrl(content);
               } else if ("param".equals(tagname)) {
                  int index = content.indexOf(" ");
                  if (index >= 0) {
                     content = "<code>" + content.substring(0, index) + "</code> - " + content.substring(index);
                  }
               }

               if (TAG_TEXT.containsKey(tagname)) {
                  String text = (String)TAG_TEXT.get(tagname);
                  List<String> contents = (List)savedTags.get(text);
                  if (contents == null) {
                     contents = new ArrayList();
                     savedTags.put(text, contents);
                  }

                  ((List)contents).add(content);
                  matcher.appendReplacement(sb, "");
               } else {
                  matcher.appendReplacement(sb, preKey + tagname + postKey + content + postValues);
               }
            }
         }

         matcher.appendTail(sb);
         sb = new StringBuffer(sb.substring(0, sb.length() - 10));
         Iterator i$ = savedTags.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, List<String>> e = (Entry)i$.next();
            sb.append(preKey);
            sb.append((String)e.getKey());
            sb.append(postKey);
            sb.append(DefaultGroovyMethods.join((Collection)e.getValue(), valueSeparator));
            sb.append(postValues);
         }

         return sb.toString();
      }
   }

   public static String encodeSpecialSymbols(String text) {
      return Matcher.quoteReplacement(text.replaceAll("@", "&at;"));
   }

   public static String decodeSpecialSymbols(String text) {
      return text.replaceAll("&at;", "@");
   }

   static {
      TAG_TEXT.put("see", "See Also");
      TAG_TEXT.put("param", "Parameters");
      TAG_TEXT.put("throw", "Throws");
      TAG_TEXT.put("exception", "Throws");
      TAG_TEXT.put("return", "Returns");
      TAG_TEXT.put("since", "Since");
      TAG_TEXT.put("author", "Authors");
      TAG_TEXT.put("version", "Version");
   }
}
