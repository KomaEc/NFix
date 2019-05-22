package soot.javaToJimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polyglot.ast.Block;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassLit;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.FieldDecl;
import polyglot.ast.Formal;
import polyglot.ast.Initializer;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.ast.SourceFile;
import polyglot.ast.TypeNode;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Flags;
import polyglot.types.LocalInstance;
import polyglot.util.IdentityKey;
import soot.BooleanType;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.VoidType;
import soot.options.Options;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.EnclosingTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.QualifyingTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.SyntheticTag;

public class ClassResolver {
   private static final Logger logger = LoggerFactory.getLogger(ClassResolver.class);
   private ArrayList<FieldDecl> staticFieldInits;
   private ArrayList<FieldDecl> fieldInits;
   private ArrayList<Block> initializerBlocks;
   private ArrayList<Block> staticInitializerBlocks;
   private final SootClass sootClass;
   private final Collection<Type> references;

   protected void addSourceFileTag(SootClass sc) {
      SourceFileTag tag = null;
      if (sc.hasTag("SourceFileTag")) {
         tag = (SourceFileTag)sc.getTag("SourceFileTag");
      } else {
         tag = new SourceFileTag();
         sc.addTag(tag);
      }

      String name = Util.getSourceFileOfClass(sc);
      if (InitialResolver.v().classToSourceMap() != null && InitialResolver.v().classToSourceMap().containsKey(name)) {
         name = (String)InitialResolver.v().classToSourceMap().get(name);
      }

      int slashIndex = name.lastIndexOf("/");
      if (slashIndex != -1) {
         name = name.substring(slashIndex + 1);
      }

      tag.setSourceFile(name);
   }

   private void createClassDecl(ClassDecl cDecl) {
      if (!cDecl.type().isTopLevel()) {
         SootClass outerClass = ((RefType)Util.getSootType(cDecl.type().outer())).getSootClass();
         if (InitialResolver.v().getInnerClassInfoMap() == null) {
            InitialResolver.v().setInnerClassInfoMap(new HashMap());
         }

         InitialResolver.v().getInnerClassInfoMap().put(this.sootClass, new InnerClassInfo(outerClass, cDecl.name(), 0));
         this.sootClass.setOuterClass(outerClass);
      }

      Flags flags = cDecl.flags();
      this.addModifiers(flags, cDecl);
      if (cDecl.superClass() == null) {
         SootClass superClass = Scene.v().getSootClass("java.lang.Object");
         this.sootClass.setSuperclass(superClass);
      } else {
         this.sootClass.setSuperclass(((RefType)Util.getSootType(cDecl.superClass().type())).getSootClass());
         if (((ClassType)cDecl.superClass().type()).isNested()) {
            ClassType superType = (ClassType)cDecl.superClass().type();
            Util.addInnerClassTag(this.sootClass, this.sootClass.getName(), ((RefType)Util.getSootType(superType.outer())).toString(), superType.name(), Util.getModifier(superType.flags()));
         }
      }

      Iterator interfacesIt = cDecl.interfaces().iterator();

      while(interfacesIt.hasNext()) {
         TypeNode next = (TypeNode)interfacesIt.next();
         this.sootClass.addInterface(((RefType)Util.getSootType(next.type())).getSootClass());
      }

      this.findReferences(cDecl);
      this.createClassBody(cDecl.body());
      this.handleFieldInits();
      if (this.staticFieldInits != null || this.staticInitializerBlocks != null) {
         SootMethod clinitMethod;
         if (!this.sootClass.declaresMethod("<clinit>", new ArrayList(), VoidType.v())) {
            clinitMethod = Scene.v().makeSootMethod("<clinit>", new ArrayList(), VoidType.v(), 8, new ArrayList());
            this.sootClass.addMethod(clinitMethod);
            PolyglotMethodSource mSource = new PolyglotMethodSource();
            mSource.setJBB(InitialResolver.v().getJBBFactory().createJimpleBodyBuilder());
            clinitMethod.setSource(mSource);
         } else {
            clinitMethod = this.sootClass.getMethod("<clinit>", new ArrayList(), VoidType.v());
         }

         ((PolyglotMethodSource)clinitMethod.getSource()).setStaticFieldInits(this.staticFieldInits);
         ((PolyglotMethodSource)clinitMethod.getSource()).setStaticInitializerBlocks(this.staticInitializerBlocks);
      }

      if (cDecl.type().isLocal()) {
         AnonLocalClassInfo info = (AnonLocalClassInfo)InitialResolver.v().finalLocalInfo().get(new IdentityKey(cDecl.type()));
         ArrayList<SootField> finalsList = this.addFinalLocals(cDecl.body(), info.finalLocalsAvail(), cDecl.type(), info);
         Iterator it = this.sootClass.getMethods().iterator();

         while(it.hasNext()) {
            SootMethod meth = (SootMethod)it.next();
            if (meth.getName().equals("<init>")) {
               ((PolyglotMethodSource)meth.getSource()).setFinalsList(finalsList);
            }
         }

         if (!info.inStaticMethod()) {
            ClassType outerType = cDecl.type().outer();
            this.addOuterClassThisRefToInit(outerType);
            this.addOuterClassThisRefField(outerType);
         }
      } else if (cDecl.type().isNested() && !cDecl.flags().isStatic()) {
         ClassType outerType = cDecl.type().outer();
         this.addOuterClassThisRefToInit(outerType);
         this.addOuterClassThisRefField(outerType);
      }

      Util.addLnPosTags(this.sootClass, cDecl.position());
   }

   private void findReferences(Node node) {
      TypeListBuilder typeListBuilder = new TypeListBuilder();
      node.visit(typeListBuilder);
      Iterator var3 = typeListBuilder.getList().iterator();

      while(var3.hasNext()) {
         polyglot.types.Type type = (polyglot.types.Type)var3.next();
         if (!type.isPrimitive() && type.isClass()) {
            ClassType classType = (ClassType)type;
            Type sootClassType = Util.getSootType(classType);
            this.references.add(sootClassType);
         }
      }

   }

   private void createClassBody(ClassBody classBody) {
      this.staticFieldInits = null;
      this.fieldInits = null;
      this.initializerBlocks = null;
      this.staticInitializerBlocks = null;
      Iterator it = classBody.members().iterator();

      while(it.hasNext()) {
         Object next = it.next();
         if (next instanceof MethodDecl) {
            this.createMethodDecl((MethodDecl)next);
         } else if (next instanceof FieldDecl) {
            this.createFieldDecl((FieldDecl)next);
         } else if (next instanceof ConstructorDecl) {
            this.createConstructorDecl((ConstructorDecl)next);
         } else if (next instanceof ClassDecl) {
            Util.addInnerClassTag(this.sootClass, Util.getSootType(((ClassDecl)next).type()).toString(), this.sootClass.getName(), ((ClassDecl)next).name().toString(), Util.getModifier(((ClassDecl)next).flags()));
         } else if (next instanceof Initializer) {
            this.createInitializer((Initializer)next);
         } else if (Options.v().verbose()) {
            logger.debug("Class Body Member not implemented for type " + next.getClass().getName());
         }
      }

      this.handleInnerClassTags(classBody);
      this.handleClassLiteral(classBody);
      this.handleAssert(classBody);
   }

   private void addOuterClassThisRefField(polyglot.types.Type outerType) {
      Type outerSootType = Util.getSootType(outerType);
      SootField field = Scene.v().makeSootField("this$0", outerSootType, 18);
      this.sootClass.addField(field);
      field.addTag(new SyntheticTag());
   }

   private void addOuterClassThisRefToInit(polyglot.types.Type outerType) {
      Type outerSootType = Util.getSootType(outerType);
      Iterator it = this.sootClass.getMethods().iterator();

      while(it.hasNext()) {
         SootMethod meth = (SootMethod)it.next();
         if (meth.getName().equals("<init>")) {
            List newParams = new ArrayList();
            newParams.add(outerSootType);
            newParams.addAll(meth.getParameterTypes());
            meth.setParameterTypes(newParams);
            meth.addTag(new EnclosingTag());
            if (InitialResolver.v().getHasOuterRefInInit() == null) {
               InitialResolver.v().setHasOuterRefInInit(new ArrayList());
            }

            InitialResolver.v().getHasOuterRefInInit().add(meth.getDeclaringClass().getType());
         }
      }

   }

   private void addFinals(LocalInstance li, ArrayList<SootField> finalFields) {
      Iterator var3 = this.sootClass.getMethods().iterator();

      while(var3.hasNext()) {
         SootMethod meth = (SootMethod)var3.next();
         if (meth.getName().equals("<init>")) {
            List<Type> newParams = new ArrayList();
            newParams.addAll(meth.getParameterTypes());
            newParams.add(Util.getSootType(li.type()));
            meth.setParameterTypes(newParams);
         }
      }

      SootField sf = Scene.v().makeSootField("val$" + li.name(), Util.getSootType(li.type()), 18);
      this.sootClass.addField(sf);
      finalFields.add(sf);
      sf.addTag(new SyntheticTag());
   }

   private ArrayList<SootField> addFinalLocals(ClassBody cBody, ArrayList<IdentityKey> finalLocalsAvail, ClassType nodeKeyType, AnonLocalClassInfo info) {
      ArrayList<SootField> finalFields = new ArrayList();
      LocalUsesChecker luc = new LocalUsesChecker();
      cBody.visit(luc);
      ArrayList<IdentityKey> localsUsed = new ArrayList();
      Iterator fieldsNeededIt = finalLocalsAvail.iterator();

      while(fieldsNeededIt.hasNext()) {
         LocalInstance li = (LocalInstance)((IdentityKey)fieldsNeededIt.next()).object();
         if (!luc.getLocalDecls().contains(new IdentityKey(li))) {
            localsUsed.add(new IdentityKey(li));
            this.addFinals(li, finalFields);
         }
      }

      Iterator newsIt = luc.getNews().iterator();

      while(true) {
         ClassType tempNewType;
         do {
            if (!newsIt.hasNext()) {
               for(ClassType superType = (ClassType)nodeKeyType.superType(); !Util.getSootType(superType).equals(Scene.v().getSootClass("java.lang.Object").getType()); superType = (ClassType)superType.superType()) {
                  if (InitialResolver.v().finalLocalInfo().containsKey(new IdentityKey(superType))) {
                     AnonLocalClassInfo lInfo = (AnonLocalClassInfo)InitialResolver.v().finalLocalInfo().get(new IdentityKey(superType));
                     Iterator it = lInfo.finalLocalsAvail().iterator();

                     while(it.hasNext()) {
                        LocalInstance li2 = (LocalInstance)((IdentityKey)it.next()).object();
                        if (!this.sootClass.declaresField("val$" + li2.name(), Util.getSootType(li2.type())) && !luc.getLocalDecls().contains(new IdentityKey(li2))) {
                           this.addFinals(li2, finalFields);
                           localsUsed.add(new IdentityKey(li2));
                        }
                     }
                  }
               }

               info.finalLocalsUsed(localsUsed);
               InitialResolver.v().finalLocalInfo().put(new IdentityKey(nodeKeyType), info);
               return finalFields;
            }

            New tempNew = (New)newsIt.next();
            tempNewType = (ClassType)tempNew.objectType().type();
         } while(!InitialResolver.v().finalLocalInfo().containsKey(new IdentityKey(tempNewType)));

         AnonLocalClassInfo lInfo = (AnonLocalClassInfo)InitialResolver.v().finalLocalInfo().get(new IdentityKey(tempNewType));
         Iterator it = lInfo.finalLocalsAvail().iterator();

         while(it.hasNext()) {
            LocalInstance li2 = (LocalInstance)((IdentityKey)it.next()).object();
            if (!this.sootClass.declaresField("val$" + li2.name(), Util.getSootType(li2.type())) && !luc.getLocalDecls().contains(new IdentityKey(li2))) {
               this.addFinals(li2, finalFields);
               localsUsed.add(new IdentityKey(li2));
            }
         }
      }
   }

   private void createAnonClassDecl(New aNew) {
      SootClass outerClass = ((RefType)Util.getSootType(aNew.anonType().outer())).getSootClass();
      if (InitialResolver.v().getInnerClassInfoMap() == null) {
         InitialResolver.v().setInnerClassInfoMap(new HashMap());
      }

      InitialResolver.v().getInnerClassInfoMap().put(this.sootClass, new InnerClassInfo(outerClass, "0", 3));
      this.sootClass.setOuterClass(outerClass);
      SootClass typeClass = ((RefType)Util.getSootType(aNew.objectType().type())).getSootClass();
      if (((ClassType)aNew.objectType().type()).flags().isInterface()) {
         this.sootClass.addInterface(typeClass);
         this.sootClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
      } else {
         this.sootClass.setSuperclass(typeClass);
         if (((ClassType)aNew.objectType().type()).isNested()) {
            ClassType superType = (ClassType)aNew.objectType().type();
            Util.addInnerClassTag(this.sootClass, typeClass.getName(), ((RefType)Util.getSootType(superType.outer())).toString(), superType.name(), Util.getModifier(superType.flags()));
         }
      }

      ArrayList params = new ArrayList();
      SootMethod method;
      if (((ClassType)aNew.objectType().type()).flags().isInterface()) {
         method = Scene.v().makeSootMethod("<init>", params, VoidType.v());
      } else {
         if (!aNew.arguments().isEmpty()) {
            ConstructorInstance ci = InitialResolver.v().getConstructorForAnon(aNew);
            Iterator aIt = ci.formalTypes().iterator();

            while(aIt.hasNext()) {
               polyglot.types.Type pType = (polyglot.types.Type)aIt.next();
               params.add(Util.getSootType(pType));
            }
         }

         method = Scene.v().makeSootMethod("<init>", params, VoidType.v());
      }

      AnonClassInitMethodSource src = new AnonClassInitMethodSource();
      method.setSource(src);
      this.sootClass.addMethod(method);
      AnonLocalClassInfo info = (AnonLocalClassInfo)InitialResolver.v().finalLocalInfo().get(new IdentityKey(aNew.anonType()));
      if (aNew.qualifier() != null) {
         this.addQualifierRefToInit(aNew.qualifier().type());
         src.hasQualifier(true);
      }

      if (info != null) {
         src.inStaticMethod(info.inStaticMethod());
         if (!info.inStaticMethod() && !InitialResolver.v().isAnonInCCall(aNew.anonType())) {
            this.addOuterClassThisRefToInit(aNew.anonType().outer());
            this.addOuterClassThisRefField(aNew.anonType().outer());
            src.thisOuterType(Util.getSootType(aNew.anonType().outer()));
            src.hasOuterRef(true);
         }
      }

      src.polyglotType((ClassType)aNew.anonType().superType());
      src.anonType(aNew.anonType());
      if (info != null) {
         src.setFinalsList(this.addFinalLocals(aNew.body(), info.finalLocalsAvail(), aNew.anonType(), info));
      }

      src.outerClassType(Util.getSootType(aNew.anonType().outer()));
      if (((ClassType)aNew.objectType().type()).isNested()) {
         src.superOuterType(Util.getSootType(((ClassType)aNew.objectType().type()).outer()));
         src.isSubType(Util.isSubType(aNew.anonType().outer(), ((ClassType)aNew.objectType().type()).outer()));
      }

      Util.addLnPosTags(this.sootClass, aNew.position().line(), aNew.body().position().endLine(), aNew.position().column(), aNew.body().position().endColumn());
   }

   public int getModifiers(Flags flags) {
      return Util.getModifier(flags);
   }

   private void addModifiers(Flags flags, ClassDecl cDecl) {
      int modifiers = 0;
      if (cDecl.type().isNested()) {
         if (flags.isPublic() || flags.isProtected() || flags.isPrivate()) {
            modifiers = 1;
         }

         if (flags.isInterface()) {
            modifiers |= 512;
         }

         if (flags.isAbstract()) {
            modifiers |= 1024;
         }

         if (cDecl.type().outer().flags().isInterface()) {
            modifiers |= 1;
         }
      } else {
         modifiers = this.getModifiers(flags);
      }

      this.sootClass.setModifiers(modifiers);
   }

   private SootClass getSpecialInterfaceAnonClass(SootClass addToClass) {
      if (InitialResolver.v().specialAnonMap() != null && InitialResolver.v().specialAnonMap().containsKey(addToClass)) {
         return (SootClass)InitialResolver.v().specialAnonMap().get(addToClass);
      } else {
         String specialClassName = addToClass.getName() + "$" + InitialResolver.v().getNextAnonNum();
         SootClass specialClass = new SootClass(specialClassName);
         Scene.v().addClass(specialClass);
         specialClass.setApplicationClass();
         specialClass.addTag(new SyntheticTag());
         specialClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
         Util.addInnerClassTag(addToClass, specialClass.getName(), addToClass.getName(), (String)null, 8);
         Util.addInnerClassTag(specialClass, specialClass.getName(), addToClass.getName(), (String)null, 8);
         InitialResolver.v().addNameToAST(specialClassName);
         this.references.add(RefType.v(specialClassName));
         if (InitialResolver.v().specialAnonMap() == null) {
            InitialResolver.v().setSpecialAnonMap(new HashMap());
         }

         InitialResolver.v().specialAnonMap().put(addToClass, specialClass);
         return specialClass;
      }
   }

   private void handleAssert(ClassBody cBody) {
      AssertStmtChecker asc = new AssertStmtChecker();
      cBody.visit(asc);
      if (asc.isHasAssert()) {
         String fieldName = "$assertionsDisabled";
         Type fieldType = BooleanType.v();
         if (!this.sootClass.declaresField(fieldName, fieldType)) {
            SootField assertionsDisabledField = Scene.v().makeSootField(fieldName, fieldType, 24);
            this.sootClass.addField(assertionsDisabledField);
            assertionsDisabledField.addTag(new SyntheticTag());
         }

         SootClass addToClass;
         for(addToClass = this.sootClass; InitialResolver.v().getInnerClassInfoMap() != null && InitialResolver.v().getInnerClassInfoMap().containsKey(addToClass); addToClass = ((InnerClassInfo)InitialResolver.v().getInnerClassInfoMap().get(addToClass)).getOuterClass()) {
         }

         fieldName = "class$" + addToClass.getName().replaceAll(".", "$");
         if (InitialResolver.v().getInterfacesList() != null && InitialResolver.v().getInterfacesList().contains(addToClass.getName())) {
            addToClass = this.getSpecialInterfaceAnonClass(addToClass);
         }

         Type fieldType = RefType.v("java.lang.Class");
         if (!addToClass.declaresField(fieldName, fieldType)) {
            SootField classField = Scene.v().makeSootField(fieldName, fieldType, 8);
            addToClass.addField(classField);
            classField.addTag(new SyntheticTag());
         }

         String methodName = "class$";
         Type methodRetType = RefType.v("java.lang.Class");
         ArrayList paramTypes = new ArrayList();
         paramTypes.add(RefType.v("java.lang.String"));
         SootMethod sootMethod = Scene.v().makeSootMethod(methodName, paramTypes, methodRetType, 8);
         AssertClassMethodSource assertMSrc = new AssertClassMethodSource();
         sootMethod.setSource(assertMSrc);
         if (!addToClass.declaresMethod(methodName, paramTypes, methodRetType)) {
            addToClass.addMethod(sootMethod);
            sootMethod.addTag(new SyntheticTag());
         }

         methodName = "<clinit>";
         Type methodRetType = VoidType.v();
         paramTypes = new ArrayList();
         sootMethod = Scene.v().makeSootMethod(methodName, paramTypes, methodRetType, 8);
         PolyglotMethodSource mSrc = new PolyglotMethodSource();
         mSrc.setJBB(InitialResolver.v().getJBBFactory().createJimpleBodyBuilder());
         mSrc.hasAssert(true);
         sootMethod.setSource(mSrc);
         if (!this.sootClass.declaresMethod(methodName, paramTypes, methodRetType)) {
            this.sootClass.addMethod(sootMethod);
         } else {
            ((PolyglotMethodSource)this.sootClass.getMethod(methodName, paramTypes, methodRetType).getSource()).hasAssert(true);
         }

      }
   }

   private void createConstructorDecl(ConstructorDecl constructor) {
      String name = "<init>";
      ArrayList parameters = this.createParameters(constructor);
      ArrayList<SootClass> exceptions = this.createExceptions(constructor);
      SootMethod sootMethod = this.createSootConstructor(name, constructor.flags(), parameters, exceptions);
      this.finishProcedure(constructor, sootMethod);
   }

   private void createMethodDecl(MethodDecl method) {
      String name = this.createName(method);
      ArrayList parameters = this.createParameters(method);
      ArrayList<SootClass> exceptions = this.createExceptions(method);
      SootMethod sootMethod = this.createSootMethod(name, method.flags(), method.returnType().type(), parameters, exceptions);
      this.finishProcedure(method, sootMethod);
   }

   private void finishProcedure(ProcedureDecl procedure, SootMethod sootMethod) {
      this.addProcedureToClass(sootMethod);
      if (procedure.position() != null) {
         Util.addLnPosTags(sootMethod, procedure.position());
      }

      PolyglotMethodSource mSrc = new PolyglotMethodSource(procedure.body(), procedure.formals());
      mSrc.setJBB(InitialResolver.v().getJBBFactory().createJimpleBodyBuilder());
      sootMethod.setSource(mSrc);
   }

   private void handleFieldInits() {
      if (this.fieldInits != null || this.initializerBlocks != null) {
         Iterator methodsIt = this.sootClass.getMethods().iterator();

         while(methodsIt.hasNext()) {
            SootMethod next = (SootMethod)methodsIt.next();
            if (next.getName().equals("<init>")) {
               PolyglotMethodSource src = (PolyglotMethodSource)next.getSource();
               src.setInitializerBlocks(this.initializerBlocks);
               src.setFieldInits(this.fieldInits);
            }
         }
      }

   }

   private void handleClassLiteral(ClassBody cBody) {
      ClassLiteralChecker classLitChecker = new ClassLiteralChecker();
      cBody.visit(classLitChecker);
      ArrayList<Node> classLitList = classLitChecker.getList();
      if (!classLitList.isEmpty()) {
         SootClass addToClass = this.sootClass;
         if (addToClass.isInterface()) {
            addToClass = this.getSpecialInterfaceAnonClass(addToClass);
         }

         String methodName = "class$";
         Type methodRetType = RefType.v("java.lang.Class");
         ArrayList paramTypes = new ArrayList();
         paramTypes.add(RefType.v("java.lang.String"));
         SootMethod sootMethod = Scene.v().makeSootMethod(methodName, paramTypes, methodRetType, 8);
         ClassLiteralMethodSource mSrc = new ClassLiteralMethodSource();
         sootMethod.setSource(mSrc);
         if (!addToClass.declaresMethod(methodName, paramTypes, methodRetType)) {
            addToClass.addMethod(sootMethod);
            sootMethod.addTag(new SyntheticTag());
         }

         Iterator classLitIt = classLitList.iterator();

         while(classLitIt.hasNext()) {
            ClassLit classLit = (ClassLit)classLitIt.next();
            String fieldName = Util.getFieldNameForClassLit(classLit.typeNode().type());
            Type fieldType = RefType.v("java.lang.Class");
            SootField sootField = Scene.v().makeSootField(fieldName, fieldType, 8);
            if (!addToClass.declaresField(fieldName, fieldType)) {
               addToClass.addField(sootField);
               sootField.addTag(new SyntheticTag());
            }
         }
      }

   }

   protected void createSource(SourceFile source) {
      SourceFileTag t;
      if (this.sootClass.hasTag("SourceFileTag")) {
         t = (SourceFileTag)this.sootClass.getTag("SourceFileTag");
         t.setAbsolutePath(source.source().path());
      } else {
         t = new SourceFileTag();
         t.setAbsolutePath(source.source().path());
         this.sootClass.addTag(t);
      }

      String simpleName = this.sootClass.getName();
      Iterator declsIt = source.decls().iterator();
      boolean found = false;

      while(declsIt.hasNext()) {
         Object next = declsIt.next();
         if (next instanceof ClassDecl) {
            ClassType nextType = ((ClassDecl)next).type();
            if (Util.getSootType(nextType).equals(this.sootClass.getType())) {
               this.createClassDecl((ClassDecl)next);
               found = true;
            }
         }
      }

      if (!found) {
         NestedClassListBuilder nestedClassBuilder = new NestedClassListBuilder();
         source.visit(nestedClassBuilder);
         Iterator nestedDeclsIt = nestedClassBuilder.getClassDeclsList().iterator();

         while(true) {
            while(nestedDeclsIt.hasNext() && !found) {
               ClassDecl nextDecl = (ClassDecl)nestedDeclsIt.next();
               ClassType type = nextDecl.type();
               if (type.isLocal() && !type.isAnonymous()) {
                  if (InitialResolver.v().getLocalClassMap().containsVal(simpleName)) {
                     this.createClassDecl(((LocalClassDecl)InitialResolver.v().getLocalClassMap().getKey(simpleName)).decl());
                     found = true;
                  }
               } else if (Util.getSootType(type).equals(this.sootClass.getType())) {
                  this.createClassDecl(nextDecl);
                  found = true;
               }
            }

            if (!found && InitialResolver.v().getAnonClassMap() != null && InitialResolver.v().getAnonClassMap().containsVal(simpleName)) {
               New aNew = (New)InitialResolver.v().getAnonClassMap().getKey(simpleName);
               if (aNew == null) {
                  throw new RuntimeException("Could resolve class: " + simpleName);
               }

               this.createAnonClassDecl(aNew);
               this.findReferences(aNew.body());
               this.createClassBody(aNew.body());
               this.handleFieldInits();
            }
            break;
         }
      }

   }

   private void handleInnerClassTags(ClassBody classBody) {
      if (InitialResolver.v().getInnerClassInfoMap() != null && InitialResolver.v().getInnerClassInfoMap().containsKey(this.sootClass)) {
         InnerClassInfo tag = (InnerClassInfo)InitialResolver.v().getInnerClassInfoMap().get(this.sootClass);
         Util.addInnerClassTag(this.sootClass, this.sootClass.getName(), tag.getInnerType() == 3 ? null : tag.getOuterClass().getName(), tag.getInnerType() == 3 ? null : tag.getSimpleName(), Modifier.isInterface(tag.getOuterClass().getModifiers()) ? 9 : this.sootClass.getModifiers());

         InnerClassInfo tag2;
         for(SootClass outerClass = tag.getOuterClass(); InitialResolver.v().getInnerClassInfoMap().containsKey(outerClass); outerClass = tag2.getOuterClass()) {
            tag2 = (InnerClassInfo)InitialResolver.v().getInnerClassInfoMap().get(outerClass);
            Util.addInnerClassTag(this.sootClass, outerClass.getName(), tag2.getInnerType() == 3 ? null : tag2.getOuterClass().getName(), tag2.getInnerType() == 3 ? null : tag2.getSimpleName(), tag2.getInnerType() == 3 && Modifier.isInterface(tag2.getOuterClass().getModifiers()) ? 9 : outerClass.getModifiers());
         }
      }

   }

   private void addQualifierRefToInit(polyglot.types.Type type) {
      Type sootType = Util.getSootType(type);
      Iterator it = this.sootClass.getMethods().iterator();

      while(it.hasNext()) {
         SootMethod meth = (SootMethod)it.next();
         if (meth.getName().equals("<init>")) {
            List newParams = new ArrayList();
            newParams.add(sootType);
            newParams.addAll(meth.getParameterTypes());
            meth.setParameterTypes(newParams);
            meth.addTag(new QualifyingTag());
         }
      }

   }

   private void addProcedureToClass(SootMethod method) {
      this.sootClass.addMethod(method);
   }

   private void addConstValTag(FieldDecl field, SootField sootField) {
      if (field.fieldInstance().constantValue() instanceof Integer) {
         sootField.addTag(new IntegerConstantValueTag((Integer)field.fieldInstance().constantValue()));
      } else if (field.fieldInstance().constantValue() instanceof Character) {
         sootField.addTag(new IntegerConstantValueTag((Character)field.fieldInstance().constantValue()));
      } else if (field.fieldInstance().constantValue() instanceof Short) {
         sootField.addTag(new IntegerConstantValueTag((Short)field.fieldInstance().constantValue()));
      } else if (field.fieldInstance().constantValue() instanceof Byte) {
         sootField.addTag(new IntegerConstantValueTag((Byte)field.fieldInstance().constantValue()));
      } else if (field.fieldInstance().constantValue() instanceof Boolean) {
         boolean b = (Boolean)field.fieldInstance().constantValue();
         sootField.addTag(new IntegerConstantValueTag(b ? 1 : 0));
      } else if (field.fieldInstance().constantValue() instanceof Long) {
         sootField.addTag(new LongConstantValueTag((Long)field.fieldInstance().constantValue()));
      } else if (field.fieldInstance().constantValue() instanceof Double) {
         sootField.addTag(new DoubleConstantValueTag((double)((long)(Double)field.fieldInstance().constantValue())));
         DoubleConstantValueTag var4 = (DoubleConstantValueTag)sootField.getTag("DoubleConstantValueTag");
      } else if (field.fieldInstance().constantValue() instanceof Float) {
         sootField.addTag(new FloatConstantValueTag((Float)field.fieldInstance().constantValue()));
      } else {
         if (!(field.fieldInstance().constantValue() instanceof String)) {
            throw new RuntimeException("Expecting static final field to have a constant value! For field: " + field + " of type: " + field.fieldInstance().constantValue().getClass());
         }

         sootField.addTag(new StringConstantValueTag((String)field.fieldInstance().constantValue()));
      }

   }

   private void createFieldDecl(FieldDecl field) {
      int modifiers = Util.getModifier(field.fieldInstance().flags());
      String name = field.fieldInstance().name();
      Type sootType = Util.getSootType(field.fieldInstance().type());
      SootField sootField = Scene.v().makeSootField(name, sootType, modifiers);
      this.sootClass.addField(sootField);
      if (field.fieldInstance().flags().isStatic()) {
         if (field.init() != null) {
            if (field.flags().isFinal() && (field.type().type().isPrimitive() || field.type().type().toString().equals("java.lang.String")) && field.fieldInstance().isConstant()) {
               this.addConstValTag(field, sootField);
            } else {
               if (this.staticFieldInits == null) {
                  this.staticFieldInits = new ArrayList();
               }

               this.staticFieldInits.add(field);
            }
         }
      } else if (field.init() != null) {
         if (this.fieldInits == null) {
            this.fieldInits = new ArrayList();
         }

         this.fieldInits.add(field);
      }

      Util.addLnPosTags(sootField, field.position());
   }

   public ClassResolver(SootClass sootClass, Set<Type> set) {
      this.sootClass = sootClass;
      this.references = set;
   }

   private String createName(ProcedureDecl procedure) {
      return procedure.name();
   }

   private ArrayList createParameters(ProcedureDecl procedure) {
      ArrayList parameters = new ArrayList();
      Iterator formalsIt = procedure.formals().iterator();

      while(formalsIt.hasNext()) {
         Formal next = (Formal)formalsIt.next();
         parameters.add(Util.getSootType(next.type().type()));
      }

      return parameters;
   }

   private ArrayList<SootClass> createExceptions(ProcedureDecl procedure) {
      ArrayList<SootClass> exceptions = new ArrayList();
      Iterator throwsIt = procedure.throwTypes().iterator();

      while(throwsIt.hasNext()) {
         polyglot.types.Type throwType = ((TypeNode)throwsIt.next()).type();
         exceptions.add(((RefType)Util.getSootType(throwType)).getSootClass());
      }

      return exceptions;
   }

   private SootMethod createSootMethod(String name, Flags flags, polyglot.types.Type returnType, ArrayList parameters, ArrayList<SootClass> exceptions) {
      int modifier = Util.getModifier(flags);
      Type sootReturnType = Util.getSootType(returnType);
      SootMethod method = Scene.v().makeSootMethod(name, parameters, sootReturnType, modifier, exceptions);
      return method;
   }

   private void createInitializer(Initializer initializer) {
      if (initializer.flags().isStatic()) {
         if (this.staticInitializerBlocks == null) {
            this.staticInitializerBlocks = new ArrayList();
         }

         this.staticInitializerBlocks.add(initializer.body());
      } else {
         if (this.initializerBlocks == null) {
            this.initializerBlocks = new ArrayList();
         }

         this.initializerBlocks.add(initializer.body());
      }

   }

   private SootMethod createSootConstructor(String name, Flags flags, ArrayList parameters, ArrayList<SootClass> exceptions) {
      int modifier = Util.getModifier(flags);
      SootMethod method = Scene.v().makeSootMethod(name, parameters, VoidType.v(), modifier, exceptions);
      return method;
   }
}
