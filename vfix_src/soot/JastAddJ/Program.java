package soot.JastAddJ;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Program extends ASTNode<ASTNode> implements Cloneable {
   protected BytecodeReader bytecodeReader;
   protected JavaParser javaParser;
   private boolean pathsInitialized = false;
   private ArrayList classPath;
   private ArrayList sourcePath;
   private FileNamesPart sourceFiles = new FileNamesPart(this);
   public int classFileReadTime;
   public static final int SRC_PREC_JAVA = 1;
   public static final int SRC_PREC_CLASS = 2;
   public static final int SRC_PREC_ONLY_CLASS = 3;
   private int srcPrec = 0;
   private HashMap loadedCompilationUnit = new HashMap();
   protected boolean typeObject_computed = false;
   protected TypeDecl typeObject_value;
   protected boolean typeCloneable_computed = false;
   protected TypeDecl typeCloneable_value;
   protected boolean typeSerializable_computed = false;
   protected TypeDecl typeSerializable_value;
   protected boolean typeBoolean_computed = false;
   protected TypeDecl typeBoolean_value;
   protected boolean typeByte_computed = false;
   protected TypeDecl typeByte_value;
   protected boolean typeShort_computed = false;
   protected TypeDecl typeShort_value;
   protected boolean typeChar_computed = false;
   protected TypeDecl typeChar_value;
   protected boolean typeInt_computed = false;
   protected TypeDecl typeInt_value;
   protected boolean typeLong_computed = false;
   protected TypeDecl typeLong_value;
   protected boolean typeFloat_computed = false;
   protected TypeDecl typeFloat_value;
   protected boolean typeDouble_computed = false;
   protected TypeDecl typeDouble_value;
   protected boolean typeString_computed = false;
   protected TypeDecl typeString_value;
   protected boolean typeVoid_computed = false;
   protected TypeDecl typeVoid_value;
   protected boolean typeNull_computed = false;
   protected TypeDecl typeNull_value;
   protected boolean unknownType_computed = false;
   protected TypeDecl unknownType_value;
   protected Map hasPackage_String_values;
   protected Map lookupType_String_String_values;
   protected Map lookupLibType_String_String_values;
   protected Map getLibCompilationUnit_String_values;
   protected List getLibCompilationUnit_String_list;
   protected boolean getPrimitiveCompilationUnit_computed = false;
   protected PrimitiveCompilationUnit getPrimitiveCompilationUnit_value;
   protected boolean unknownConstructor_computed = false;
   protected ConstructorDecl unknownConstructor_value;
   protected boolean wildcards_computed = false;
   protected WildcardsCompilationUnit wildcards_value;

   public void flushCache() {
      super.flushCache();
      this.typeObject_computed = false;
      this.typeObject_value = null;
      this.typeCloneable_computed = false;
      this.typeCloneable_value = null;
      this.typeSerializable_computed = false;
      this.typeSerializable_value = null;
      this.typeBoolean_computed = false;
      this.typeBoolean_value = null;
      this.typeByte_computed = false;
      this.typeByte_value = null;
      this.typeShort_computed = false;
      this.typeShort_value = null;
      this.typeChar_computed = false;
      this.typeChar_value = null;
      this.typeInt_computed = false;
      this.typeInt_value = null;
      this.typeLong_computed = false;
      this.typeLong_value = null;
      this.typeFloat_computed = false;
      this.typeFloat_value = null;
      this.typeDouble_computed = false;
      this.typeDouble_value = null;
      this.typeString_computed = false;
      this.typeString_value = null;
      this.typeVoid_computed = false;
      this.typeVoid_value = null;
      this.typeNull_computed = false;
      this.typeNull_value = null;
      this.unknownType_computed = false;
      this.unknownType_value = null;
      this.hasPackage_String_values = null;
      this.lookupType_String_String_values = null;
      this.lookupLibType_String_String_values = null;
      this.getLibCompilationUnit_String_values = null;
      this.getLibCompilationUnit_String_list = null;
      this.getPrimitiveCompilationUnit_computed = false;
      this.getPrimitiveCompilationUnit_value = null;
      this.unknownConstructor_computed = false;
      this.unknownConstructor_value = null;
      this.wildcards_computed = false;
      this.wildcards_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Program clone() throws CloneNotSupportedException {
      Program node = (Program)super.clone();
      node.typeObject_computed = false;
      node.typeObject_value = null;
      node.typeCloneable_computed = false;
      node.typeCloneable_value = null;
      node.typeSerializable_computed = false;
      node.typeSerializable_value = null;
      node.typeBoolean_computed = false;
      node.typeBoolean_value = null;
      node.typeByte_computed = false;
      node.typeByte_value = null;
      node.typeShort_computed = false;
      node.typeShort_value = null;
      node.typeChar_computed = false;
      node.typeChar_value = null;
      node.typeInt_computed = false;
      node.typeInt_value = null;
      node.typeLong_computed = false;
      node.typeLong_value = null;
      node.typeFloat_computed = false;
      node.typeFloat_value = null;
      node.typeDouble_computed = false;
      node.typeDouble_value = null;
      node.typeString_computed = false;
      node.typeString_value = null;
      node.typeVoid_computed = false;
      node.typeVoid_value = null;
      node.typeNull_computed = false;
      node.typeNull_value = null;
      node.unknownType_computed = false;
      node.unknownType_value = null;
      node.hasPackage_String_values = null;
      node.lookupType_String_String_values = null;
      node.lookupLibType_String_String_values = null;
      node.getLibCompilationUnit_String_values = null;
      node.getLibCompilationUnit_String_list = null;
      node.getPrimitiveCompilationUnit_computed = false;
      node.getPrimitiveCompilationUnit_value = null;
      node.unknownConstructor_computed = false;
      node.unknownConstructor_value = null;
      node.wildcards_computed = false;
      node.wildcards_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public Program copy() {
      try {
         Program node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public Program fullCopy() {
      Program tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            ASTNode child = this.children[i];
            if (child != null) {
               child = child.fullCopy();
               tree.setChild(child, i);
            }
         }
      }

      return tree;
   }

   public void initBytecodeReader(BytecodeReader r) {
      this.bytecodeReader = r;
   }

   public void initJavaParser(JavaParser p) {
      this.javaParser = p;
   }

   public CompilationUnit addSourceFile(String name) {
      return this.sourceFiles.addSourceFile(name);
   }

   public Iterator compilationUnitIterator() {
      this.initPaths();
      return new Iterator() {
         int index = 0;

         public boolean hasNext() {
            return this.index < Program.this.getNumCompilationUnit() || !Program.this.sourceFiles.isEmpty();
         }

         public Object next() {
            if (Program.this.getNumCompilationUnit() == this.index) {
               String typename = (String)Program.this.sourceFiles.keySet().iterator().next();
               CompilationUnit u = Program.this.getCompilationUnit(typename);
               if (u == null) {
                  throw new Error("File " + typename + " not found");
               }

               Program.this.addCompilationUnit(u);
               Program.this.getCompilationUnit(Program.this.getNumCompilationUnit() - 1);
            }

            return Program.this.getCompilationUnit(this.index++);
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public InputStream getInputStream(String name) {
      this.initPaths();

      try {
         Iterator iter = this.classPath.iterator();

         while(iter.hasNext()) {
            PathPart part = (PathPart)iter.next();
            if (part.selectCompilationUnit(name)) {
               return part.getInputStream();
            }
         }
      } catch (IOException var4) {
      }

      throw new Error("Could not find nested type " + name);
   }

   public boolean isPackage(String name) {
      if (this.sourceFiles.hasPackage(name)) {
         return true;
      } else {
         Iterator iter = this.classPath.iterator();

         PathPart part;
         do {
            if (!iter.hasNext()) {
               iter = this.sourcePath.iterator();

               do {
                  if (!iter.hasNext()) {
                     return false;
                  }

                  part = (PathPart)iter.next();
               } while(!part.hasPackage(name));

               return true;
            }

            part = (PathPart)iter.next();
         } while(!part.hasPackage(name));

         return true;
      }
   }

   public void pushClassPath(String name) {
      PathPart part = PathPart.createSourcePath(name, this);
      if (part != null) {
         this.sourcePath.add(part);
         System.out.println("Pushing source path " + name);
         part = PathPart.createClassPath(name, this);
         if (part != null) {
            this.classPath.add(part);
            System.out.println("Pushing class path " + name);
         }

      } else {
         throw new Error("Could not push source path " + name);
      }
   }

   public void popClassPath() {
      if (this.sourcePath.size() > 0) {
         this.sourcePath.remove(this.sourcePath.size() - 1);
      }

      if (this.classPath.size() > 0) {
         this.classPath.remove(this.classPath.size() - 1);
      }

   }

   public void initPaths() {
      if (!this.pathsInitialized) {
         this.pathsInitialized = true;
         ArrayList classPaths = new ArrayList();
         ArrayList sourcePaths = new ArrayList();
         String[] bootclasspaths;
         if (this.options().hasValueForOption("-bootclasspath")) {
            bootclasspaths = this.options().getValueForOption("-bootclasspath").split(File.pathSeparator);
         } else {
            bootclasspaths = System.getProperty("sun.boot.class.path").split(File.pathSeparator);
         }

         for(int i = 0; i < bootclasspaths.length; ++i) {
            classPaths.add(bootclasspaths[i]);
         }

         String[] extdirs;
         if (this.options().hasValueForOption("-extdirs")) {
            extdirs = this.options().getValueForOption("-extdirs").split(File.pathSeparator);
         } else {
            extdirs = System.getProperty("java.ext.dirs").split(File.pathSeparator);
         }

         for(int i = 0; i < extdirs.length; ++i) {
            classPaths.add(extdirs[i]);
         }

         String[] userClasses = null;
         if (this.options().hasValueForOption("-classpath")) {
            userClasses = this.options().getValueForOption("-classpath").split(File.pathSeparator);
         } else if (this.options().hasValueForOption("-cp")) {
            userClasses = this.options().getValueForOption("-cp").split(File.pathSeparator);
         } else {
            userClasses = ".".split(File.pathSeparator);
         }

         int i;
         if (!this.options().hasValueForOption("-sourcepath")) {
            for(i = 0; i < userClasses.length; ++i) {
               classPaths.add(userClasses[i]);
               sourcePaths.add(userClasses[i]);
            }
         } else {
            for(i = 0; i < userClasses.length; ++i) {
               classPaths.add(userClasses[i]);
            }

            userClasses = this.options().getValueForOption("-sourcepath").split(File.pathSeparator);

            for(i = 0; i < userClasses.length; ++i) {
               sourcePaths.add(userClasses[i]);
            }
         }

         this.classPath = new ArrayList();
         this.sourcePath = new ArrayList();
         Iterator iter = classPaths.iterator();

         String s;
         PathPart part;
         while(iter.hasNext()) {
            s = (String)iter.next();
            part = PathPart.createClassPath(s, this);
            if (part != null) {
               this.classPath.add(part);
            } else if (this.options().verbose()) {
               System.out.println("Warning: Could not use " + s + " as class path");
            }
         }

         iter = sourcePaths.iterator();

         while(iter.hasNext()) {
            s = (String)iter.next();
            part = PathPart.createSourcePath(s, this);
            if (part != null) {
               this.sourcePath.add(part);
            } else if (this.options().verbose()) {
               System.out.println("Warning: Could not use " + s + " as source path");
            }
         }
      }

   }

   public void addClassPath(PathPart pathPart) {
      this.classPath.add(pathPart);
      pathPart.setProgram(this);
   }

   public void addSourcePath(PathPart pathPart) {
      this.sourcePath.add(pathPart);
      pathPart.setProgram(this);
   }

   public void simpleReset() {
      this.lookupType_String_String_values = new HashMap();
      this.hasPackage_String_values = new HashMap();
      List list = new List();

      for(int i = 0; i < this.getNumCompilationUnit(); ++i) {
         CompilationUnit unit = this.getCompilationUnit(i);
         if (!unit.fromSource()) {
            list.add(unit);
         }
      }

      this.setCompilationUnitList(list);
   }

   public void errorCheck(Collection collection) {
      Iterator iter = this.compilationUnitIterator();

      while(iter.hasNext()) {
         CompilationUnit cu = (CompilationUnit)iter.next();
         if (cu.fromSource()) {
            cu.collectErrors();
            collection.addAll(cu.errors);
         }
      }

   }

   public void errorCheck(Collection collection, Collection warn) {
      Iterator iter = this.compilationUnitIterator();

      while(iter.hasNext()) {
         CompilationUnit cu = (CompilationUnit)iter.next();
         if (cu.fromSource()) {
            cu.collectErrors();
            collection.addAll(cu.errors);
            warn.addAll(cu.warnings);
         }
      }

   }

   public boolean errorCheck() {
      Collection collection = new LinkedList();
      this.errorCheck(collection);
      if (collection.isEmpty()) {
         return false;
      } else {
         System.out.println("Errors:");
         Iterator iter = collection.iterator();

         while(iter.hasNext()) {
            String s = (String)iter.next();
            System.out.println(s);
         }

         return true;
      }
   }

   public void toString(StringBuffer s) {
      Iterator iter = this.compilationUnitIterator();

      while(iter.hasNext()) {
         CompilationUnit cu = (CompilationUnit)iter.next();
         if (cu.fromSource()) {
            cu.toString(s);
         }
      }

   }

   public String dumpTree() {
      StringBuffer s = new StringBuffer();
      Iterator iter = this.compilationUnitIterator();

      while(iter.hasNext()) {
         CompilationUnit cu = (CompilationUnit)iter.next();
         if (cu.fromSource()) {
            s.append(cu.dumpTree());
         }
      }

      return s.toString();
   }

   public void jimplify1() {
      Iterator iter = this.compilationUnitIterator();

      CompilationUnit u;
      while(iter.hasNext()) {
         u = (CompilationUnit)iter.next();
         if (u.fromSource()) {
            u.jimplify1phase1();
         }
      }

      iter = this.compilationUnitIterator();

      while(iter.hasNext()) {
         u = (CompilationUnit)iter.next();
         if (u.fromSource()) {
            u.jimplify1phase2();
         }
      }

   }

   public void jimplify2() {
      Iterator iter = this.compilationUnitIterator();

      while(iter.hasNext()) {
         CompilationUnit u = (CompilationUnit)iter.next();
         if (u.fromSource()) {
            u.jimplify2();
         }
      }

   }

   public void setSrcPrec(int i) {
      this.srcPrec = i;
   }

   public boolean hasLoadedCompilationUnit(String fileName) {
      return this.loadedCompilationUnit.containsKey(fileName);
   }

   public CompilationUnit getCachedOrLoadCompilationUnit(String fileName) {
      if (this.loadedCompilationUnit.containsKey(fileName)) {
         return (CompilationUnit)this.loadedCompilationUnit.get(fileName);
      } else {
         this.addSourceFile(fileName);
         return (CompilationUnit)this.loadedCompilationUnit.get(fileName);
      }
   }

   public void releaseCompilationUnitForFile(String fileName) {
      this.lookupType_String_String_values = new HashMap();
      this.hasPackage_String_values = new HashMap();
      this.loadedCompilationUnit.remove(fileName);
      List<CompilationUnit> newList = new List();
      Iterator var3 = this.getCompilationUnits().iterator();

      while(var3.hasNext()) {
         CompilationUnit cu = (CompilationUnit)var3.next();
         boolean dontAdd = false;
         if (cu.fromSource()) {
            String pathName = cu.pathName();
            if (pathName.equals(fileName)) {
               dontAdd = true;
            }
         }

         if (!dontAdd) {
            newList.add(cu);
         }
      }

      this.setCompilationUnitList(newList);
   }

   public Program() {
      this.is$Final(true);
   }

   public void init$Children() {
      this.children = new ASTNode[1];
      this.setChild(new List(), 0);
   }

   public Program(List<CompilationUnit> p0) {
      this.setChild(p0, 0);
      this.is$Final(true);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setCompilationUnitList(List<CompilationUnit> list) {
      this.setChild(list, 0);
   }

   public int getNumCompilationUnit() {
      return this.getCompilationUnitList().getNumChild();
   }

   public int getNumCompilationUnitNoTransform() {
      return this.getCompilationUnitListNoTransform().getNumChildNoTransform();
   }

   public CompilationUnit getCompilationUnit(int i) {
      return (CompilationUnit)this.getCompilationUnitList().getChild(i);
   }

   public void refined__Program_addCompilationUnit(CompilationUnit node) {
      List<CompilationUnit> list = this.parent != null && state != null ? this.getCompilationUnitList() : this.getCompilationUnitListNoTransform();
      list.addChild(node);
   }

   public void addCompilationUnitNoTransform(CompilationUnit node) {
      List<CompilationUnit> list = this.getCompilationUnitListNoTransform();
      list.addChild(node);
   }

   public void setCompilationUnit(CompilationUnit node, int i) {
      List<CompilationUnit> list = this.getCompilationUnitList();
      list.setChild(node, i);
   }

   public List<CompilationUnit> getCompilationUnits() {
      return this.getCompilationUnitList();
   }

   public List<CompilationUnit> getCompilationUnitsNoTransform() {
      return this.getCompilationUnitListNoTransform();
   }

   public List<CompilationUnit> refined__Program_getCompilationUnitList() {
      List<CompilationUnit> list = (List)this.getChild(0);
      list.getNumChild();
      return list;
   }

   public List<CompilationUnit> getCompilationUnitListNoTransform() {
      return (List)this.getChildNoTransform(0);
   }

   public CompilationUnit getCompilationUnit(String name) {
      this.initPaths();

      try {
         if (this.sourceFiles.selectCompilationUnit(name)) {
            return this.sourceFiles.getCompilationUnit();
         } else {
            PathPart sourcePart = null;
            PathPart classPart = null;
            Iterator iter = this.sourcePath.iterator();

            PathPart part;
            while(iter.hasNext() && sourcePart == null) {
               part = (PathPart)iter.next();
               if (part.selectCompilationUnit(name)) {
                  sourcePart = part;
               }
            }

            iter = this.classPath.iterator();

            while(iter.hasNext() && classPart == null) {
               part = (PathPart)iter.next();
               if (part.selectCompilationUnit(name)) {
                  classPart = part;
               }
            }

            String pkgName;
            CompilationUnit unit;
            int index;
            if (sourcePart != null && this.srcPrec == 1) {
               unit = this.getCachedOrLoadCompilationUnit((new File(sourcePart.pathName)).getCanonicalPath());
               index = name.lastIndexOf(46);
               if (index == -1) {
                  return unit;
               }

               pkgName = name.substring(0, index);
               if (pkgName.equals(unit.getPackageDecl())) {
                  return unit;
               }
            }

            if (classPart != null && this.srcPrec == 2) {
               unit = classPart.getCompilationUnit();
               index = name.lastIndexOf(46);
               if (index == -1) {
                  return unit;
               }

               pkgName = name.substring(0, index);
               if (pkgName.equals(unit.getPackageDecl())) {
                  return unit;
               }
            }

            if (this.srcPrec == 3) {
               if (classPart != null) {
                  unit = classPart.getCompilationUnit();
                  index = name.lastIndexOf(46);
                  if (index == -1) {
                     return unit;
                  }

                  pkgName = name.substring(0, index);
                  if (pkgName.equals(unit.getPackageDecl())) {
                     return unit;
                  }
               }
            } else if (sourcePart != null && (classPart == null || classPart.getAge() <= sourcePart.getAge())) {
               unit = this.getCachedOrLoadCompilationUnit((new File(sourcePart.pathName)).getCanonicalPath());
               index = name.lastIndexOf(46);
               if (index == -1) {
                  return unit;
               }

               pkgName = name.substring(0, index);
               if (pkgName.equals(unit.getPackageDecl())) {
                  return unit;
               }
            } else if (classPart != null) {
               unit = classPart.getCompilationUnit();
               index = name.lastIndexOf(46);
               if (index == -1) {
                  return unit;
               }

               pkgName = name.substring(0, index);
               if (pkgName.equals(unit.getPackageDecl())) {
                  return unit;
               }
            }

            return null;
         }
      } catch (IOException var7) {
         return null;
      }
   }

   void addCompilationUnit(CompilationUnit unit) {
      try {
         if (unit.pathName() != null) {
            String fileName = (new File(unit.pathName())).getCanonicalPath();
            this.loadedCompilationUnit.put(fileName, unit);
         }
      } catch (IOException var3) {
      }

      this.refined__Program_addCompilationUnit(unit);
   }

   public List getCompilationUnitList() {
      this.initPaths();
      return this.refined__Program_getCompilationUnitList();
   }

   public TypeDecl typeObject() {
      if (this.typeObject_computed) {
         return this.typeObject_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeObject_value = this.typeObject_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeObject_computed = true;
         }

         return this.typeObject_value;
      }
   }

   private TypeDecl typeObject_compute() {
      return this.lookupType("java.lang", "Object");
   }

   public TypeDecl typeCloneable() {
      if (this.typeCloneable_computed) {
         return this.typeCloneable_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeCloneable_value = this.typeCloneable_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeCloneable_computed = true;
         }

         return this.typeCloneable_value;
      }
   }

   private TypeDecl typeCloneable_compute() {
      return this.lookupType("java.lang", "Cloneable");
   }

   public TypeDecl typeSerializable() {
      if (this.typeSerializable_computed) {
         return this.typeSerializable_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeSerializable_value = this.typeSerializable_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeSerializable_computed = true;
         }

         return this.typeSerializable_value;
      }
   }

   private TypeDecl typeSerializable_compute() {
      return this.lookupType("java.io", "Serializable");
   }

   public TypeDecl typeBoolean() {
      if (this.typeBoolean_computed) {
         return this.typeBoolean_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeBoolean_value = this.typeBoolean_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeBoolean_computed = true;
         }

         return this.typeBoolean_value;
      }
   }

   private TypeDecl typeBoolean_compute() {
      return this.lookupType("@primitive", "boolean");
   }

   public TypeDecl typeByte() {
      if (this.typeByte_computed) {
         return this.typeByte_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeByte_value = this.typeByte_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeByte_computed = true;
         }

         return this.typeByte_value;
      }
   }

   private TypeDecl typeByte_compute() {
      return this.lookupType("@primitive", "byte");
   }

   public TypeDecl typeShort() {
      if (this.typeShort_computed) {
         return this.typeShort_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeShort_value = this.typeShort_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeShort_computed = true;
         }

         return this.typeShort_value;
      }
   }

   private TypeDecl typeShort_compute() {
      return this.lookupType("@primitive", "short");
   }

   public TypeDecl typeChar() {
      if (this.typeChar_computed) {
         return this.typeChar_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeChar_value = this.typeChar_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeChar_computed = true;
         }

         return this.typeChar_value;
      }
   }

   private TypeDecl typeChar_compute() {
      return this.lookupType("@primitive", "char");
   }

   public TypeDecl typeInt() {
      if (this.typeInt_computed) {
         return this.typeInt_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeInt_value = this.typeInt_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeInt_computed = true;
         }

         return this.typeInt_value;
      }
   }

   private TypeDecl typeInt_compute() {
      return this.lookupType("@primitive", "int");
   }

   public TypeDecl typeLong() {
      if (this.typeLong_computed) {
         return this.typeLong_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeLong_value = this.typeLong_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeLong_computed = true;
         }

         return this.typeLong_value;
      }
   }

   private TypeDecl typeLong_compute() {
      return this.lookupType("@primitive", "long");
   }

   public TypeDecl typeFloat() {
      if (this.typeFloat_computed) {
         return this.typeFloat_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeFloat_value = this.typeFloat_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeFloat_computed = true;
         }

         return this.typeFloat_value;
      }
   }

   private TypeDecl typeFloat_compute() {
      return this.lookupType("@primitive", "float");
   }

   public TypeDecl typeDouble() {
      if (this.typeDouble_computed) {
         return this.typeDouble_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeDouble_value = this.typeDouble_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeDouble_computed = true;
         }

         return this.typeDouble_value;
      }
   }

   private TypeDecl typeDouble_compute() {
      return this.lookupType("@primitive", "double");
   }

   public TypeDecl typeString() {
      if (this.typeString_computed) {
         return this.typeString_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeString_value = this.typeString_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeString_computed = true;
         }

         return this.typeString_value;
      }
   }

   private TypeDecl typeString_compute() {
      return this.lookupType("java.lang", "String");
   }

   public TypeDecl typeVoid() {
      if (this.typeVoid_computed) {
         return this.typeVoid_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeVoid_value = this.typeVoid_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeVoid_computed = true;
         }

         return this.typeVoid_value;
      }
   }

   private TypeDecl typeVoid_compute() {
      return this.lookupType("@primitive", "void");
   }

   public TypeDecl typeNull() {
      if (this.typeNull_computed) {
         return this.typeNull_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeNull_value = this.typeNull_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeNull_computed = true;
         }

         return this.typeNull_value;
      }
   }

   private TypeDecl typeNull_compute() {
      return this.lookupType("@primitive", "null");
   }

   public TypeDecl unknownType() {
      if (this.unknownType_computed) {
         return this.unknownType_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.unknownType_value = this.unknownType_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.unknownType_computed = true;
         }

         return this.unknownType_value;
      }
   }

   private TypeDecl unknownType_compute() {
      return this.lookupType("@primitive", "Unknown");
   }

   public boolean hasPackage(String packageName) {
      if (this.hasPackage_String_values == null) {
         this.hasPackage_String_values = new HashMap(4);
      }

      if (this.hasPackage_String_values.containsKey(packageName)) {
         return (Boolean)this.hasPackage_String_values.get(packageName);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean hasPackage_String_value = this.hasPackage_compute(packageName);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.hasPackage_String_values.put(packageName, hasPackage_String_value);
         }

         return hasPackage_String_value;
      }
   }

   private boolean hasPackage_compute(String packageName) {
      return this.isPackage(packageName);
   }

   public TypeDecl lookupType(String packageName, String typeName) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(packageName);
      _parameters.add(typeName);
      if (this.lookupType_String_String_values == null) {
         this.lookupType_String_String_values = new HashMap(4);
      }

      if (this.lookupType_String_String_values.containsKey(_parameters)) {
         return (TypeDecl)this.lookupType_String_String_values.get(_parameters);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         TypeDecl lookupType_String_String_value = this.lookupType_compute(packageName, typeName);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lookupType_String_String_values.put(_parameters, lookupType_String_String_value);
         }

         return lookupType_String_String_value;
      }
   }

   private TypeDecl lookupType_compute(String packageName, String typeName) {
      String fullName = packageName.equals("") ? typeName : packageName + "." + typeName;

      for(int i = 0; i < this.getNumCompilationUnit(); ++i) {
         for(int j = 0; j < this.getCompilationUnit(i).getNumTypeDecl(); ++j) {
            TypeDecl type = this.getCompilationUnit(i).getTypeDecl(j);
            if (type.fullName().equals(fullName)) {
               return type;
            }
         }
      }

      return this.lookupLibType(packageName, typeName);
   }

   public TypeDecl lookupLibType(String packageName, String typeName) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(packageName);
      _parameters.add(typeName);
      if (this.lookupLibType_String_String_values == null) {
         this.lookupLibType_String_String_values = new HashMap(4);
      }

      if (this.lookupLibType_String_String_values.containsKey(_parameters)) {
         return (TypeDecl)this.lookupLibType_String_String_values.get(_parameters);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         TypeDecl lookupLibType_String_String_value = this.lookupLibType_compute(packageName, typeName);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lookupLibType_String_String_values.put(_parameters, lookupLibType_String_String_value);
         }

         return lookupLibType_String_String_value;
      }
   }

   private TypeDecl lookupLibType_compute(String packageName, String typeName) {
      String fullName = packageName.equals("") ? typeName : packageName + "." + typeName;
      if (packageName.equals("@primitive")) {
         PrimitiveCompilationUnit unit = this.getPrimitiveCompilationUnit();
         if (typeName.equals("boolean")) {
            return unit.typeBoolean();
         }

         if (typeName.equals("byte")) {
            return unit.typeByte();
         }

         if (typeName.equals("short")) {
            return unit.typeShort();
         }

         if (typeName.equals("char")) {
            return unit.typeChar();
         }

         if (typeName.equals("int")) {
            return unit.typeInt();
         }

         if (typeName.equals("long")) {
            return unit.typeLong();
         }

         if (typeName.equals("float")) {
            return unit.typeFloat();
         }

         if (typeName.equals("double")) {
            return unit.typeDouble();
         }

         if (typeName.equals("null")) {
            return unit.typeNull();
         }

         if (typeName.equals("void")) {
            return unit.typeVoid();
         }

         if (typeName.equals("Unknown")) {
            return unit.unknownType();
         }
      }

      CompilationUnit libUnit = this.getLibCompilationUnit(fullName);
      if (libUnit != null) {
         for(int j = 0; j < libUnit.getNumTypeDecl(); ++j) {
            TypeDecl type = libUnit.getTypeDecl(j);
            if (type.fullName().equals(fullName)) {
               return type;
            }
         }
      }

      return null;
   }

   public CompilationUnit getLibCompilationUnit(String fullName) {
      if (this.getLibCompilationUnit_String_values == null) {
         this.getLibCompilationUnit_String_values = new HashMap(4);
      }

      if (this.getLibCompilationUnit_String_values.containsKey(fullName)) {
         return (CompilationUnit)this.getLibCompilationUnit_String_values.get(fullName);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         CompilationUnit getLibCompilationUnit_String_value = this.getLibCompilationUnit_compute(fullName);
         if (this.getLibCompilationUnit_String_list == null) {
            this.getLibCompilationUnit_String_list = new List();
            this.getLibCompilationUnit_String_list.is$Final = true;
            this.getLibCompilationUnit_String_list.setParent(this);
         }

         this.getLibCompilationUnit_String_list.add(getLibCompilationUnit_String_value);
         if (getLibCompilationUnit_String_value != null) {
            getLibCompilationUnit_String_value.is$Final = true;
         }

         this.getLibCompilationUnit_String_values.put(fullName, getLibCompilationUnit_String_value);
         return getLibCompilationUnit_String_value;
      }
   }

   private CompilationUnit getLibCompilationUnit_compute(String fullName) {
      return this.getCompilationUnit(fullName);
   }

   public PrimitiveCompilationUnit getPrimitiveCompilationUnit() {
      if (this.getPrimitiveCompilationUnit_computed) {
         return this.getPrimitiveCompilationUnit_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.getPrimitiveCompilationUnit_value = this.getPrimitiveCompilationUnit_compute();
         this.getPrimitiveCompilationUnit_value.setParent(this);
         this.getPrimitiveCompilationUnit_value.is$Final = true;
         this.getPrimitiveCompilationUnit_computed = true;
         return this.getPrimitiveCompilationUnit_value;
      }
   }

   private PrimitiveCompilationUnit getPrimitiveCompilationUnit_compute() {
      PrimitiveCompilationUnit u = new PrimitiveCompilationUnit();
      u.setPackageDecl("@primitive");
      return u;
   }

   public ConstructorDecl unknownConstructor() {
      if (this.unknownConstructor_computed) {
         return this.unknownConstructor_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.unknownConstructor_value = this.unknownConstructor_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.unknownConstructor_computed = true;
         }

         return this.unknownConstructor_value;
      }
   }

   private ConstructorDecl unknownConstructor_compute() {
      return (ConstructorDecl)this.unknownType().constructors().iterator().next();
   }

   public WildcardsCompilationUnit wildcards() {
      if (this.wildcards_computed) {
         return this.wildcards_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.wildcards_value = this.wildcards_compute();
         this.wildcards_value.setParent(this);
         this.wildcards_value.is$Final = true;
         this.wildcards_computed = true;
         return this.wildcards_value;
      }
   }

   private WildcardsCompilationUnit wildcards_compute() {
      return new WildcardsCompilationUnit("wildcards", new List(), new List());
   }

   public TypeDecl Define_TypeDecl_superType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public ConstructorDecl Define_ConstructorDecl_constructorDecl(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public TypeDecl Define_TypeDecl_componentType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.unknownType();
   }

   public LabeledStmt Define_LabeledStmt_lookupLabel(ASTNode caller, ASTNode child, String name) {
      this.getIndexOfChild(caller);
      return null;
   }

   public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return true;
   }

   public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      this.getIndexOfChild(caller);
      return true;
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      this.getIndexOfChild(caller);
      return true;
   }

   public TypeDecl Define_TypeDecl_typeException(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.lookupType("java.lang", "Exception");
   }

   public TypeDecl Define_TypeDecl_typeRuntimeException(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.lookupType("java.lang", "RuntimeException");
   }

   public TypeDecl Define_TypeDecl_typeError(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.lookupType("java.lang", "Error");
   }

   public TypeDecl Define_TypeDecl_typeNullPointerException(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.lookupType("java.lang", "NullPointerException");
   }

   public TypeDecl Define_TypeDecl_typeThrowable(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.lookupType("java.lang", "Throwable");
   }

   public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
      this.getIndexOfChild(caller);
      throw new Error("Operation handlesException not supported");
   }

   public Collection Define_Collection_lookupConstructor(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return Collections.EMPTY_LIST;
   }

   public Collection Define_Collection_lookupSuperConstructor(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return Collections.EMPTY_LIST;
   }

   public Expr Define_Expr_nestedScope(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      throw new UnsupportedOperationException();
   }

   public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
      this.getIndexOfChild(caller);
      return Collections.EMPTY_LIST;
   }

   public TypeDecl Define_TypeDecl_typeObject(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeObject();
   }

   public TypeDecl Define_TypeDecl_typeCloneable(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeCloneable();
   }

   public TypeDecl Define_TypeDecl_typeSerializable(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeSerializable();
   }

   public TypeDecl Define_TypeDecl_typeBoolean(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeBoolean();
   }

   public TypeDecl Define_TypeDecl_typeByte(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeByte();
   }

   public TypeDecl Define_TypeDecl_typeShort(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeShort();
   }

   public TypeDecl Define_TypeDecl_typeChar(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeChar();
   }

   public TypeDecl Define_TypeDecl_typeInt(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeInt();
   }

   public TypeDecl Define_TypeDecl_typeLong(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeLong();
   }

   public TypeDecl Define_TypeDecl_typeFloat(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeFloat();
   }

   public TypeDecl Define_TypeDecl_typeDouble(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeDouble();
   }

   public TypeDecl Define_TypeDecl_typeString(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeString();
   }

   public TypeDecl Define_TypeDecl_typeVoid(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeVoid();
   }

   public TypeDecl Define_TypeDecl_typeNull(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeNull();
   }

   public TypeDecl Define_TypeDecl_unknownType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.unknownType();
   }

   public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
      this.getIndexOfChild(caller);
      return this.hasPackage(packageName);
   }

   public TypeDecl Define_TypeDecl_lookupType(ASTNode caller, ASTNode child, String packageName, String typeName) {
      this.getIndexOfChild(caller);
      return this.lookupType(packageName, typeName);
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      this.getIndexOfChild(caller);
      return SimpleSet.emptySet;
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      this.getIndexOfChild(caller);
      return SimpleSet.emptySet;
   }

   public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_mayBeVolatile(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_mayBeTransient(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_mayBeStrictfp(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_mayBeSynchronized(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_mayBeNative(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      throw new UnsupportedOperationException("outerScope() not defined");
   }

   public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public Case Define_Case_bind(ASTNode caller, ASTNode child, Case c) {
      this.getIndexOfChild(caller);
      return null;
   }

   public String Define_String_typeDeclIndent(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return "";
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return NameType.NO_NAME;
   }

   public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public Variable Define_Variable_unknownField(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.unknownType().findSingleVariable("unknown");
   }

   public MethodDecl Define_MethodDecl_unknownMethod(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      Iterator iter = this.unknownType().memberMethods("unknown").iterator();
      if (iter.hasNext()) {
         MethodDecl m = (MethodDecl)iter.next();
         return m;
      } else {
         throw new Error("Could not find method unknown in type Unknown");
      }
   }

   public ConstructorDecl Define_ConstructorDecl_unknownConstructor(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.unknownConstructor();
   }

   public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public BodyDecl Define_BodyDecl_enclosingBodyDecl(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public TypeDecl Define_TypeDecl_switchType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.unknownType();
   }

   public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeVoid();
   }

   public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public String Define_String_methodHost(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      throw new Error("Needs extra equation for methodHost()");
   }

   public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return true;
   }

   public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
      this.getIndexOfChild(caller);
      return false;
   }

   public ElementValue Define_ElementValue_lookupElementTypeValue(ASTNode caller, ASTNode child, String name) {
      this.getIndexOfChild(caller);
      return null;
   }

   public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String s) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public Annotation Define_Annotation_lookupAnnotation(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
      this.getIndexOfChild(caller);
      return null;
   }

   public TypeDecl Define_TypeDecl_enclosingAnnotationDecl(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.unknownType();
   }

   public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.typeNull();
   }

   public boolean Define_boolean_inExtendsOrImplements(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public TypeDecl Define_TypeDecl_typeWildcard(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.wildcards().typeWildcard();
   }

   public TypeDecl Define_TypeDecl_lookupWildcardExtends(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
      this.getIndexOfChild(caller);
      return this.wildcards().lookupWildcardExtends(typeDecl);
   }

   public TypeDecl Define_TypeDecl_lookupWildcardSuper(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
      this.getIndexOfChild(caller);
      return this.wildcards().lookupWildcardSuper(typeDecl);
   }

   public LUBType Define_LUBType_lookupLUBType(ASTNode caller, ASTNode child, Collection bounds) {
      this.getIndexOfChild(caller);
      return this.wildcards().lookupLUBType(bounds);
   }

   public GLBType Define_GLBType_lookupGLBType(ASTNode caller, ASTNode child, ArrayList bounds) {
      this.getIndexOfChild(caller);
      return this.wildcards().lookupGLBType(bounds);
   }

   public TypeDecl Define_TypeDecl_genericDecl(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public boolean Define_boolean_variableArityValid(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
      if (caller == this.getCompilationUnitListNoTransform()) {
         caller.getIndexOfChild(child);
         throw new Error("condition_false_label not implemented");
      } else {
         return this.getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
      }
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
      if (caller == this.getCompilationUnitListNoTransform()) {
         caller.getIndexOfChild(child);
         throw new Error("condition_true_label not implemented");
      } else {
         return this.getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
      }
   }

   public int Define_int_localNum(ASTNode caller, ASTNode child) {
      if (caller == this.getCompilationUnitListNoTransform()) {
         caller.getIndexOfChild(child);
         return 0;
      } else {
         return this.getParent().Define_int_localNum(this, caller);
      }
   }

   public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public ArrayList Define_ArrayList_exceptionRanges(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public boolean Define_boolean_isCatchParam(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public CatchClause Define_CatchClause_catchClause(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      throw new IllegalStateException("Could not find parent catch clause");
   }

   public boolean Define_boolean_resourcePreviouslyDeclared(ASTNode caller, ASTNode child, String name) {
      this.getIndexOfChild(caller);
      return false;
   }

   public ClassInstanceExpr Define_ClassInstanceExpr_getClassInstanceExpr(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public boolean Define_boolean_isAnonymousDecl(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public boolean Define_boolean_isExplicitGenericConstructorAccess(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return false;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
