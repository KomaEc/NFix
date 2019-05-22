package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.G;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.dava.CorruptASTException;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.internal.javaRep.DNewInvokeExpr;
import soot.dava.internal.javaRep.DSpecialInvokeExpr;
import soot.dava.internal.javaRep.DStaticInvokeExpr;
import soot.dava.internal.javaRep.DVariableDeclarationStmt;
import soot.dava.internal.javaRep.DVirtualInvokeExpr;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.ASTParentNodeFinder;
import soot.dava.toolkits.base.AST.traversals.ASTUsesAndDefs;
import soot.dava.toolkits.base.AST.traversals.AllDefinitionsFinder;
import soot.grimp.internal.GAssignStmt;
import soot.grimp.internal.GCastExpr;
import soot.grimp.internal.GInvokeStmt;
import soot.grimp.internal.GReturnStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JimpleLocal;

public class SuperFirstStmtHandler extends DepthFirstAdapter {
   public final boolean DEBUG = false;
   ASTMethodNode originalASTMethod;
   DavaBody originalDavaBody;
   Unit originalConstructorUnit;
   InstanceInvokeExpr originalConstructorExpr;
   SootMethod originalSootMethod;
   SootClass originalSootClass;
   Map originalPMap;
   List argsOneTypes = null;
   List argsOneValues = null;
   List argsTwoValues = null;
   List argsTwoTypes = null;
   SootMethod newSootPreInitMethod = null;
   DavaBody newPreInitDavaBody = null;
   ASTMethodNode newASTPreInitMethod = null;
   SootMethod newConstructor = null;
   DavaBody newConstructorDavaBody = null;
   ASTMethodNode newASTConstructorMethod = null;
   List<Local> mustInitialize;
   int mustInitializeIndex = 0;

   public SuperFirstStmtHandler(ASTMethodNode AST) {
      this.originalASTMethod = AST;
      this.initialize();
   }

   public SuperFirstStmtHandler(boolean verbose, ASTMethodNode AST) {
      super(verbose);
      this.originalASTMethod = AST;
      this.initialize();
   }

   public void initialize() {
      this.originalDavaBody = this.originalASTMethod.getDavaBody();
      this.originalConstructorUnit = this.originalDavaBody.get_ConstructorUnit();
      this.originalConstructorExpr = this.originalDavaBody.get_ConstructorExpr();
      Iterator typeIt;
      Type t;
      if (this.originalConstructorExpr != null) {
         this.argsTwoValues = this.originalConstructorExpr.getArgs();
         this.argsTwoTypes = new ArrayList();
         typeIt = this.argsTwoValues.iterator();

         while(typeIt.hasNext()) {
            Value val = (Value)typeIt.next();
            t = val.getType();
            this.argsTwoTypes.add(t);
         }
      }

      this.originalSootMethod = this.originalDavaBody.getMethod();
      this.originalSootClass = this.originalSootMethod.getDeclaringClass();
      this.originalPMap = this.originalDavaBody.get_ParamMap();
      this.argsOneTypes = this.originalSootMethod.getParameterTypes();
      this.argsOneValues = new ArrayList();
      typeIt = this.argsOneTypes.iterator();

      for(int count = 0; typeIt.hasNext(); ++count) {
         t = (Type)typeIt.next();
         this.argsOneValues.add(this.originalPMap.get(new Integer(count)));
      }

   }

   public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
      Iterator var2 = node.getStatements().iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Unit u = as.get_Stmt();
         if (u == this.originalConstructorUnit) {
            ASTParentNodeFinder parentFinder = new ASTParentNodeFinder();
            this.originalASTMethod.apply(parentFinder);
            Object tempParent = parentFinder.getParentOf(node);
            if (tempParent != this.originalASTMethod) {
               this.removeInit();
               return;
            }

            this.createSootPreInitMethod();
            this.createNewASTPreInitMethod(node);
            if (this.newASTPreInitMethod == null) {
               this.removeInit();
               return;
            }

            if (!this.finalizePreInitMethod()) {
               this.removeInit();
               return;
            }

            this.createNewConstructor();
            this.createNewASTConstructor(node);
            if (!this.createCallToSuper()) {
               this.removeInit();
               return;
            }

            this.finalizeConstructor();
            if (this.changeOriginalAST()) {
               this.debug("SuperFirstStmtHandler....inASTStatementSeuqneNode", "Added PreInit");
               G.v().SootMethodAddedByDava = true;
               G.v().SootMethodsAdded.add(this.newSootPreInitMethod);
               G.v().SootMethodsAdded.add(this.newConstructor);
               G.v().SootClassNeedsDavaSuperHandlerClass.add(this.originalSootClass);
            }
         }
      }

   }

   public void removeInit() {
      List<Object> newBody = new ArrayList();
      List<Object> subBody = this.originalASTMethod.get_SubBodies();
      if (subBody.size() == 1) {
         List oldBody = (List)subBody.get(0);
         Iterator oldIt = oldBody.iterator();

         while(true) {
            while(oldIt.hasNext()) {
               ASTNode node = (ASTNode)oldIt.next();
               if (!(node instanceof ASTStatementSequenceNode)) {
                  newBody.add(node);
               } else {
                  ASTStatementSequenceNode seqNode = (ASTStatementSequenceNode)node;
                  List<AugmentedStmt> newStmtList = new ArrayList();
                  Iterator var8 = seqNode.getStatements().iterator();

                  while(var8.hasNext()) {
                     AugmentedStmt augStmt = (AugmentedStmt)var8.next();
                     Stmt stmtTemp = augStmt.get_Stmt();
                     if (stmtTemp != this.originalConstructorUnit) {
                        newStmtList.add(augStmt);
                     }
                  }

                  if (newStmtList.size() != 0) {
                     newBody.add(new ASTStatementSequenceNode(newStmtList));
                  }
               }
            }

            this.originalASTMethod.replaceBody(newBody);
            return;
         }
      }
   }

   public boolean changeOriginalAST() {
      if (this.originalConstructorExpr == null) {
         return false;
      } else {
         List thisArgList = new ArrayList();
         thisArgList.addAll(this.argsOneValues);
         DStaticInvokeExpr newInvokeExpr = new DStaticInvokeExpr(this.newSootPreInitMethod.makeRef(), this.argsOneValues);
         thisArgList.add(newInvokeExpr);
         InstanceInvokeExpr tempExpr = new DSpecialInvokeExpr(this.originalConstructorExpr.getBase(), this.newConstructor.makeRef(), thisArgList);
         this.originalDavaBody.set_ConstructorExpr(tempExpr);
         GInvokeStmt s = new GInvokeStmt(tempExpr);
         this.originalDavaBody.set_ConstructorUnit(s);
         this.originalASTMethod.setDeclarations(new ASTStatementSequenceNode(new ArrayList()));
         this.originalASTMethod.replaceBody(new ArrayList());
         return true;
      }
   }

   private SootMethodRef makeMethodRef(String methodName, ArrayList args) {
      SootMethod method = Scene.v().makeSootMethod(methodName, args, RefType.v("java.lang.Object"));
      method.setDeclaringClass(new SootClass("DavaSuperHandler"));
      return method.makeRef();
   }

   private boolean createCallToSuper() {
      if (this.originalConstructorExpr == null) {
         return false;
      } else {
         SootClass parentClass = this.originalSootClass.getSuperclass();
         if (!parentClass.declaresMethod("<init>", this.argsTwoTypes)) {
            return false;
         } else {
            SootMethod superConstructor = parentClass.getMethod("<init>", this.argsTwoTypes);
            List argsForConstructor = new ArrayList();
            int count = 0;
            RefType type = (new SootClass("DavaSuperHandler")).getType();
            Local jimpleLocal = new JimpleLocal("handler", type);
            ArrayList tempList = new ArrayList();
            tempList.add(IntType.v());
            SootMethodRef getMethodRef = this.makeMethodRef("get", tempList);
            List tempArgList = null;
            Iterator typeIt = this.argsTwoTypes.iterator();

            while(typeIt.hasNext()) {
               Type tempType = (Type)typeIt.next();
               DIntConstant arg = DIntConstant.v(count, IntType.v());
               ++count;
               tempArgList = new ArrayList();
               tempArgList.add(arg);
               DVirtualInvokeExpr tempInvokeExpr = new DVirtualInvokeExpr(jimpleLocal, getMethodRef, tempArgList, new HashSet());
               Value toAddExpr = this.getProperCasting(tempType, tempInvokeExpr);
               if (toAddExpr == null) {
                  throw new DecompilationException("UNABLE TO CREATE TOADDEXPR:" + tempType);
               }

               argsForConstructor.add(toAddExpr);
            }

            this.mustInitializeIndex = count;
            DVirtualInvokeExpr virtualInvoke = new DVirtualInvokeExpr(this.originalConstructorExpr.getBase(), superConstructor.makeRef(), argsForConstructor, new HashSet());
            this.newConstructorDavaBody.set_ConstructorExpr(virtualInvoke);
            GInvokeStmt s = new GInvokeStmt(virtualInvoke);
            this.newConstructorDavaBody.set_ConstructorUnit(s);
            return true;
         }
      }
   }

   public Value getProperCasting(Type tempType, DVirtualInvokeExpr tempInvokeExpr) {
      if (tempType instanceof RefType) {
         return new GCastExpr(tempInvokeExpr, tempType);
      } else if (tempType instanceof PrimType) {
         PrimType t = (PrimType)tempType;
         GCastExpr tempExpr;
         SootMethod tempMethod;
         SootMethodRef tempMethodRef;
         if (t == BooleanType.v()) {
            tempExpr = new GCastExpr(tempInvokeExpr, RefType.v("java.lang.Boolean"));
            tempMethod = Scene.v().makeSootMethod("booleanValue", new ArrayList(), BooleanType.v());
            tempMethod.setDeclaringClass(new SootClass("java.lang.Boolean"));
            tempMethodRef = tempMethod.makeRef();
            return new DVirtualInvokeExpr(tempExpr, tempMethodRef, new ArrayList(), new HashSet());
         } else if (t == ByteType.v()) {
            tempExpr = new GCastExpr(tempInvokeExpr, RefType.v("java.lang.Byte"));
            tempMethod = Scene.v().makeSootMethod("byteValue", new ArrayList(), ByteType.v());
            tempMethod.setDeclaringClass(new SootClass("java.lang.Byte"));
            tempMethodRef = tempMethod.makeRef();
            return new DVirtualInvokeExpr(tempExpr, tempMethodRef, new ArrayList(), new HashSet());
         } else if (t == CharType.v()) {
            tempExpr = new GCastExpr(tempInvokeExpr, RefType.v("java.lang.Character"));
            tempMethod = Scene.v().makeSootMethod("charValue", new ArrayList(), CharType.v());
            tempMethod.setDeclaringClass(new SootClass("java.lang.Character"));
            tempMethodRef = tempMethod.makeRef();
            return new DVirtualInvokeExpr(tempExpr, tempMethodRef, new ArrayList(), new HashSet());
         } else if (t == DoubleType.v()) {
            tempExpr = new GCastExpr(tempInvokeExpr, RefType.v("java.lang.Double"));
            tempMethod = Scene.v().makeSootMethod("doubleValue", new ArrayList(), DoubleType.v());
            tempMethod.setDeclaringClass(new SootClass("java.lang.Double"));
            tempMethodRef = tempMethod.makeRef();
            return new DVirtualInvokeExpr(tempExpr, tempMethodRef, new ArrayList(), new HashSet());
         } else if (t == FloatType.v()) {
            tempExpr = new GCastExpr(tempInvokeExpr, RefType.v("java.lang.Float"));
            tempMethod = Scene.v().makeSootMethod("floatValue", new ArrayList(), FloatType.v());
            tempMethod.setDeclaringClass(new SootClass("java.lang.Float"));
            tempMethodRef = tempMethod.makeRef();
            return new DVirtualInvokeExpr(tempExpr, tempMethodRef, new ArrayList(), new HashSet());
         } else if (t == IntType.v()) {
            tempExpr = new GCastExpr(tempInvokeExpr, RefType.v("java.lang.Integer"));
            tempMethod = Scene.v().makeSootMethod("intValue", new ArrayList(), IntType.v());
            tempMethod.setDeclaringClass(new SootClass("java.lang.Integer"));
            tempMethodRef = tempMethod.makeRef();
            return new DVirtualInvokeExpr(tempExpr, tempMethodRef, new ArrayList(), new HashSet());
         } else if (t == LongType.v()) {
            tempExpr = new GCastExpr(tempInvokeExpr, RefType.v("java.lang.Long"));
            tempMethod = Scene.v().makeSootMethod("longValue", new ArrayList(), LongType.v());
            tempMethod.setDeclaringClass(new SootClass("java.lang.Long"));
            tempMethodRef = tempMethod.makeRef();
            return new DVirtualInvokeExpr(tempExpr, tempMethodRef, new ArrayList(), new HashSet());
         } else if (t == ShortType.v()) {
            tempExpr = new GCastExpr(tempInvokeExpr, RefType.v("java.lang.Short"));
            tempMethod = Scene.v().makeSootMethod("shortValue", new ArrayList(), ShortType.v());
            tempMethod.setDeclaringClass(new SootClass("java.lang.Short"));
            tempMethodRef = tempMethod.makeRef();
            return new DVirtualInvokeExpr(tempExpr, tempMethodRef, new ArrayList(), new HashSet());
         } else {
            throw new DecompilationException("Unhandle primType:" + tempType);
         }
      } else {
         throw new DecompilationException("The type:" + tempType + " was not a reftye or primtype. PLEASE REPORT.");
      }
   }

   private void finalizeConstructor() {
      this.newASTConstructorMethod.setDavaBody(this.newConstructorDavaBody);
      this.newConstructorDavaBody.getUnits().clear();
      this.newConstructorDavaBody.getUnits().addLast((Unit)this.newASTConstructorMethod);
      System.out.println("Setting declaring class of method" + this.newConstructor.getSubSignature());
      this.newConstructor.setDeclaringClass(this.originalSootClass);
   }

   private boolean finalizePreInitMethod() {
      this.newASTPreInitMethod.setDavaBody(this.newPreInitDavaBody);
      this.newPreInitDavaBody.getUnits().clear();
      this.newPreInitDavaBody.getUnits().addLast((Unit)this.newASTPreInitMethod);
      List<Object> subBodies = this.newASTPreInitMethod.get_SubBodies();
      if (subBodies.size() != 1) {
         return false;
      } else {
         List body = (List)subBodies.get(0);
         Iterator it = body.iterator();
         boolean empty = true;

         while(it.hasNext()) {
            ASTNode tempNode = (ASTNode)it.next();
            if (!(tempNode instanceof ASTStatementSequenceNode)) {
               empty = false;
               break;
            }

            List<AugmentedStmt> stmts = ((ASTStatementSequenceNode)tempNode).getStatements();
            Iterator var7 = stmts.iterator();

            while(var7.hasNext()) {
               AugmentedStmt as = (AugmentedStmt)var7.next();
               Stmt s = as.get_Stmt();
               if (!(s instanceof DVariableDeclarationStmt)) {
                  empty = false;
                  break;
               }
            }

            if (!empty) {
               break;
            }
         }

         if (empty) {
            return false;
         } else {
            this.createDavaStoreStmts();
            return true;
         }
      }
   }

   public void createNewASTConstructor(ASTStatementSequenceNode initNode) {
      List<Object> newConstructorBody = new ArrayList();
      List<AugmentedStmt> newStmts = new ArrayList();
      RefType type = (new SootClass("DavaSuperHandler")).getType();
      Local jimpleLocal = new JimpleLocal("handler", type);
      ArrayList tempList = new ArrayList();
      tempList.add(IntType.v());
      SootMethodRef getMethodRef = this.makeMethodRef("get", tempList);
      Iterator it;
      if (this.mustInitialize != null) {
         it = this.mustInitialize.iterator();

         while(it.hasNext()) {
            Local initLocal = (Local)it.next();
            Type tempType = initLocal.getType();
            DIntConstant arg = DIntConstant.v(this.mustInitializeIndex, IntType.v());
            ++this.mustInitializeIndex;
            ArrayList tempArgList = new ArrayList();
            tempArgList.add(arg);
            DVirtualInvokeExpr tempInvokeExpr = new DVirtualInvokeExpr(jimpleLocal, getMethodRef, tempArgList, new HashSet());
            Value toAddExpr = this.getProperCasting(tempType, tempInvokeExpr);
            if (toAddExpr == null) {
               throw new DecompilationException("UNABLE TO CREATE TOADDEXPR:" + tempType);
            }

            GAssignStmt assign = new GAssignStmt(initLocal, toAddExpr);
            newStmts.add(new AugmentedStmt(assign));
         }
      }

      it = initNode.getStatements().iterator();

      while(it.hasNext()) {
         AugmentedStmt augStmt = (AugmentedStmt)it.next();
         Stmt stmtTemp = augStmt.get_Stmt();
         if (stmtTemp == this.originalConstructorUnit) {
            break;
         }
      }

      while(it.hasNext()) {
         newStmts.add(it.next());
      }

      if (newStmts.size() > 0) {
         newConstructorBody.add(new ASTStatementSequenceNode(newStmts));
      }

      List<Object> originalASTMethodSubBodies = this.originalASTMethod.get_SubBodies();
      if (originalASTMethodSubBodies.size() != 1) {
         throw new CorruptASTException("size of ASTMethodNode subBody not 1");
      } else {
         List<Object> oldASTBody = (List)originalASTMethodSubBodies.get(0);
         Iterator<Object> itOld = oldASTBody.iterator();
         boolean sanity = false;

         while(itOld.hasNext()) {
            ASTNode tempNode = (ASTNode)itOld.next();
            if (tempNode instanceof ASTStatementSequenceNode && ((ASTStatementSequenceNode)tempNode).getStatements().equals(initNode.getStatements())) {
               sanity = true;
               break;
            }
         }

         if (!sanity) {
            throw new DecompilationException("never found the init node");
         } else {
            while(itOld.hasNext()) {
               newConstructorBody.add(itOld.next());
            }

            List<AugmentedStmt> newConstructorDeclarations = new ArrayList();
            Iterator var25 = this.originalASTMethod.getDeclarations().getStatements().iterator();

            while(var25.hasNext()) {
               AugmentedStmt as = (AugmentedStmt)var25.next();
               DVariableDeclarationStmt varDecStmt = (DVariableDeclarationStmt)as.get_Stmt();
               newConstructorDeclarations.add(new AugmentedStmt((DVariableDeclarationStmt)varDecStmt.clone()));
            }

            ASTStatementSequenceNode newDecs = new ASTStatementSequenceNode(new ArrayList());
            if (newConstructorDeclarations.size() > 0) {
               newDecs = new ASTStatementSequenceNode(newConstructorDeclarations);
               newConstructorBody.add(0, newDecs);
            }

            this.newASTConstructorMethod = new ASTMethodNode(newConstructorBody);
            this.newASTConstructorMethod.setDeclarations(newDecs);
         }
      }
   }

   private void createNewConstructor() {
      String uniqueName = "<init>";
      List args = new ArrayList();
      args.addAll(this.argsOneTypes);
      RefType type = (new SootClass("DavaSuperHandler")).getType();
      args.add(type);
      this.newConstructor = Scene.v().makeSootMethod(uniqueName, args, IntType.v());
      this.newConstructor.setDeclaringClass(this.originalSootClass);
      this.newConstructor.setModifiers(1);
      this.newConstructorDavaBody = Dava.v().newBody(this.newConstructor);
      Map tempMap = new HashMap();
      Iterator typeIt = this.argsOneTypes.iterator();

      for(int count = 0; typeIt.hasNext(); ++count) {
         Type t = (Type)typeIt.next();
         tempMap.put(new Integer(count), this.originalPMap.get(new Integer(count)));
      }

      tempMap.put(new Integer(this.argsOneTypes.size()), "handler");
      this.newConstructorDavaBody.set_ParamMap(tempMap);
      this.newConstructor.setActiveBody(this.newConstructorDavaBody);
   }

   private void createNewASTPreInitMethod(ASTStatementSequenceNode initNode) {
      List<Object> newPreinitBody = new ArrayList();
      List<Object> originalASTMethodSubBodies = this.originalASTMethod.get_SubBodies();
      if (originalASTMethodSubBodies.size() != 1) {
         throw new CorruptASTException("size of ASTMethodNode subBody not 1");
      } else {
         List<Object> oldASTBody = (List)originalASTMethodSubBodies.get(0);
         Iterator<Object> it = oldASTBody.iterator();
         boolean sanity = false;

         while(it.hasNext()) {
            ASTNode tempNode = (ASTNode)it.next();
            if (tempNode instanceof ASTStatementSequenceNode) {
               if (((ASTStatementSequenceNode)tempNode).getStatements().equals(initNode.getStatements())) {
                  sanity = true;
                  break;
               }

               newPreinitBody.add(tempNode);
            } else {
               newPreinitBody.add(tempNode);
            }
         }

         if (!sanity) {
            throw new DecompilationException("never found the init node");
         } else {
            List<AugmentedStmt> newStmts = new ArrayList();
            Iterator var8 = initNode.getStatements().iterator();

            while(var8.hasNext()) {
               AugmentedStmt augStmt = (AugmentedStmt)var8.next();
               Stmt stmtTemp = augStmt.get_Stmt();
               if (stmtTemp == this.originalConstructorUnit) {
                  break;
               }

               newStmts.add(augStmt);
            }

            if (newStmts.size() > 0) {
               newPreinitBody.add(new ASTStatementSequenceNode(newStmts));
            }

            List<AugmentedStmt> newPreinitDeclarations = new ArrayList();
            Iterator var14 = this.originalASTMethod.getDeclarations().getStatements().iterator();

            while(var14.hasNext()) {
               AugmentedStmt as = (AugmentedStmt)var14.next();
               DVariableDeclarationStmt varDecStmt = (DVariableDeclarationStmt)as.get_Stmt();
               newPreinitDeclarations.add(new AugmentedStmt((DVariableDeclarationStmt)varDecStmt.clone()));
            }

            ASTStatementSequenceNode newDecs = new ASTStatementSequenceNode(new ArrayList());
            if (newPreinitDeclarations.size() > 0) {
               newDecs = new ASTStatementSequenceNode(newPreinitDeclarations);
               newPreinitBody.remove(0);
               newPreinitBody.add(0, newDecs);
            }

            if (newPreinitBody.size() < 1) {
               this.newASTPreInitMethod = null;
            } else {
               this.newASTPreInitMethod = new ASTMethodNode(newPreinitBody);
               this.newASTPreInitMethod.setDeclarations(newDecs);
            }
         }
      }
   }

   private void createSootPreInitMethod() {
      String uniqueName = this.getUniqueName();
      this.newSootPreInitMethod = Scene.v().makeSootMethod(uniqueName, this.argsOneTypes, (new SootClass("DavaSuperHandler")).getType());
      this.newSootPreInitMethod.setDeclaringClass(this.originalSootClass);
      this.newSootPreInitMethod.setModifiers(10);
      this.newPreInitDavaBody = Dava.v().newBody(this.newSootPreInitMethod);
      this.newPreInitDavaBody.set_ParamMap(this.originalPMap);
      this.newSootPreInitMethod.setActiveBody(this.newPreInitDavaBody);
   }

   private String getUniqueName() {
      String toReturn = "preInit";
      int counter = 0;
      List methodList = this.originalSootClass.getMethods();
      boolean done = false;

      while(true) {
         while(!done) {
            done = true;
            Iterator it = methodList.iterator();

            while(it.hasNext()) {
               Object temp = it.next();
               if (!(temp instanceof SootMethod)) {
                  throw new DecompilationException("SootClass returned a non SootMethod method");
               }

               SootMethod method = (SootMethod)temp;
               String name = method.getName();
               if (toReturn.compareTo(name) == 0) {
                  ++counter;
                  toReturn = "preInit" + counter;
                  done = false;
                  break;
               }
            }

            it = G.v().SootMethodsAdded.iterator();

            while(it.hasNext()) {
               SootMethod method = (SootMethod)it.next();
               String name = method.getName();
               if (toReturn.compareTo(name) == 0) {
                  ++counter;
                  toReturn = "preInit" + counter;
                  done = false;
                  break;
               }
            }
         }

         return toReturn;
      }
   }

   private void createDavaStoreStmts() {
      List<AugmentedStmt> davaHandlerStmts = new ArrayList();
      SootClass sootClass = new SootClass("DavaSuperHandler");
      Type localType = sootClass.getType();
      Local newLocal = new JimpleLocal("handler", localType);
      DVariableDeclarationStmt varStmt = null;
      varStmt = new DVariableDeclarationStmt(localType, this.newPreInitDavaBody);
      varStmt.addLocal(newLocal);
      AugmentedStmt as = new AugmentedStmt(varStmt);
      davaHandlerStmts.add(as);
      DNewInvokeExpr invokeExpr = new DNewInvokeExpr(RefType.v(sootClass), this.makeMethodRef("DavaSuperHandler", new ArrayList()), new ArrayList());
      GAssignStmt initialization = new GAssignStmt(newLocal, invokeExpr);
      davaHandlerStmts.add(new AugmentedStmt(initialization));
      Iterator typeIt = this.argsTwoTypes.iterator();
      Iterator valIt = this.argsTwoValues.iterator();
      ArrayList tempList = new ArrayList();
      tempList.add(RefType.v("java.lang.Object"));
      SootMethod method = Scene.v().makeSootMethod("store", tempList, VoidType.v());
      method.setDeclaringClass(sootClass);
      SootMethodRef getMethodRef = method.makeRef();

      while(typeIt.hasNext() && valIt.hasNext()) {
         Type tempType = (Type)typeIt.next();
         Value tempVal = (Value)valIt.next();
         AugmentedStmt toAdd = this.createStmtAccordingToType(tempType, tempVal, newLocal, getMethodRef);
         davaHandlerStmts.add(toAdd);
      }

      if (!typeIt.hasNext() && !valIt.hasNext()) {
         List<Local> uniqueLocals = this.addDefsToLiveVariables();
         Iterator localIt = uniqueLocals.iterator();

         while(localIt.hasNext()) {
            Local local = (Local)localIt.next();
            AugmentedStmt toAdd = this.createStmtAccordingToType(local.getType(), local, newLocal, getMethodRef);
            davaHandlerStmts.add(toAdd);
         }

         this.mustInitialize = uniqueLocals;
         GReturnStmt returnStmt = new GReturnStmt(newLocal);
         davaHandlerStmts.add(new AugmentedStmt(returnStmt));
         ASTStatementSequenceNode addedNode = new ASTStatementSequenceNode(davaHandlerStmts);
         List<Object> subBodies = this.newASTPreInitMethod.get_SubBodies();
         if (subBodies.size() != 1) {
            throw new CorruptASTException("ASTMethodNode does not have one subBody");
         } else {
            List<Object> body = (List)subBodies.get(0);
            body.add(addedNode);
            this.newASTPreInitMethod.replaceBody(body);
         }
      } else {
         throw new DecompilationException("Error creating DavaHandler stmts");
      }
   }

   public AugmentedStmt createStmtAccordingToType(Type tempType, Value tempVal, Local newLocal, SootMethodRef getMethodRef) {
      if (tempType instanceof RefType) {
         return this.createAugmentedStmtToAdd(newLocal, getMethodRef, tempVal);
      } else if (tempType instanceof PrimType) {
         PrimType t = (PrimType)tempType;
         ArrayList argList = new ArrayList();
         argList.add(tempVal);
         ArrayList typeList;
         DNewInvokeExpr argForStore;
         if (t == BooleanType.v()) {
            typeList = new ArrayList();
            typeList.add(IntType.v());
            argForStore = new DNewInvokeExpr(RefType.v("java.lang.Boolean"), this.makeMethodRef("Boolean", typeList), argList);
            return this.createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore);
         } else if (t == ByteType.v()) {
            typeList = new ArrayList();
            typeList.add(ByteType.v());
            argForStore = new DNewInvokeExpr(RefType.v("java.lang.Byte"), this.makeMethodRef("Byte", typeList), argList);
            return this.createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore);
         } else if (t == CharType.v()) {
            typeList = new ArrayList();
            typeList.add(CharType.v());
            argForStore = new DNewInvokeExpr(RefType.v("java.lang.Character"), this.makeMethodRef("Character", typeList), argList);
            return this.createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore);
         } else if (t == DoubleType.v()) {
            typeList = new ArrayList();
            typeList.add(DoubleType.v());
            argForStore = new DNewInvokeExpr(RefType.v("java.lang.Double"), this.makeMethodRef("Double", typeList), argList);
            return this.createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore);
         } else if (t == FloatType.v()) {
            typeList = new ArrayList();
            typeList.add(FloatType.v());
            argForStore = new DNewInvokeExpr(RefType.v("java.lang.Float"), this.makeMethodRef("Float", typeList), argList);
            return this.createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore);
         } else if (t == IntType.v()) {
            typeList = new ArrayList();
            typeList.add(IntType.v());
            argForStore = new DNewInvokeExpr(RefType.v("java.lang.Integer"), this.makeMethodRef("Integer", typeList), argList);
            return this.createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore);
         } else if (t == LongType.v()) {
            typeList = new ArrayList();
            typeList.add(LongType.v());
            argForStore = new DNewInvokeExpr(RefType.v("java.lang.Long"), this.makeMethodRef("Long", typeList), argList);
            return this.createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore);
         } else if (t == ShortType.v()) {
            typeList = new ArrayList();
            typeList.add(ShortType.v());
            argForStore = new DNewInvokeExpr(RefType.v("java.lang.Short"), this.makeMethodRef("Short", typeList), argList);
            return this.createAugmentedStmtToAdd(newLocal, getMethodRef, argForStore);
         } else {
            throw new DecompilationException("UNHANDLED PRIMTYPE:" + tempType);
         }
      } else {
         throw new DecompilationException("The type:" + tempType + " is neither a reftype or a primtype");
      }
   }

   private List<Local> addDefsToLiveVariables() {
      AllDefinitionsFinder finder = new AllDefinitionsFinder();
      this.newASTPreInitMethod.apply(finder);
      List<DefinitionStmt> allDefs = finder.getAllDefs();
      List<Local> uniqueLocals = new ArrayList();
      List<DefinitionStmt> uniqueLocalDefs = new ArrayList();
      Iterator it = allDefs.iterator();

      while(it.hasNext()) {
         DefinitionStmt s = (DefinitionStmt)it.next();
         Value left = s.getLeftOp();
         if (left instanceof Local) {
            if (uniqueLocals.contains(left)) {
               int index = uniqueLocals.indexOf(left);
               uniqueLocals.remove(index);
               uniqueLocalDefs.remove(index);
            } else {
               uniqueLocals.add((Local)left);
               uniqueLocalDefs.add(s);
            }
         }
      }

      ASTParentNodeFinder parentFinder = new ASTParentNodeFinder();
      this.newASTPreInitMethod.apply(parentFinder);
      List<DefinitionStmt> toRemoveDefs = new ArrayList();
      it = uniqueLocalDefs.iterator();

      while(true) {
         Object grandParent;
         DefinitionStmt s;
         do {
            if (!it.hasNext()) {
               it = toRemoveDefs.iterator();

               while(it.hasNext()) {
                  s = (DefinitionStmt)it.next();
                  int index = uniqueLocalDefs.indexOf(s);
                  uniqueLocals.remove(index);
                  uniqueLocalDefs.remove(index);
               }

               toRemoveDefs = new ArrayList();
               ASTUsesAndDefs uDdU = new ASTUsesAndDefs(this.originalASTMethod);
               this.originalASTMethod.apply(uDdU);
               it = uniqueLocalDefs.iterator();

               while(true) {
                  DefinitionStmt s;
                  while(it.hasNext()) {
                     s = (DefinitionStmt)it.next();
                     Object temp = uDdU.getDUChain(s);
                     if (temp == null) {
                        toRemoveDefs.add(s);
                     } else {
                        ArrayList uses = (ArrayList)temp;
                        if (uses.size() == 0) {
                           toRemoveDefs.add(s);
                        }

                        Iterator useIt = uses.iterator();
                        boolean onlyInConstructorUnit = true;

                        while(useIt.hasNext()) {
                           Object tempUse = useIt.next();
                           if (tempUse != this.originalConstructorUnit) {
                              onlyInConstructorUnit = false;
                           }
                        }

                        if (onlyInConstructorUnit) {
                           toRemoveDefs.add(s);
                        }
                     }
                  }

                  it = toRemoveDefs.iterator();

                  while(it.hasNext()) {
                     s = (DefinitionStmt)it.next();
                     int index = uniqueLocalDefs.indexOf(s);
                     uniqueLocals.remove(index);
                     uniqueLocalDefs.remove(index);
                  }

                  return uniqueLocals;
               }
            }

            s = (DefinitionStmt)it.next();
            Object parent = parentFinder.getParentOf(s);
            if (parent == null || !(parent instanceof ASTStatementSequenceNode)) {
               toRemoveDefs.add(s);
            }

            grandParent = parentFinder.getParentOf(parent);
         } while(grandParent != null && grandParent instanceof ASTMethodNode);

         toRemoveDefs.add(s);
      }
   }

   private AugmentedStmt createAugmentedStmtToAdd(Local newLocal, SootMethodRef getMethodRef, Value tempVal) {
      ArrayList tempArgList = new ArrayList();
      tempArgList.add(tempVal);
      DVirtualInvokeExpr tempInvokeExpr = new DVirtualInvokeExpr(newLocal, getMethodRef, tempArgList, new HashSet());
      GInvokeStmt s = new GInvokeStmt(tempInvokeExpr);
      return new AugmentedStmt(s);
   }

   public void debug(String methodName, String debug) {
   }
}
