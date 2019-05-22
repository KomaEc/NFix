package soot.javaToJimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import polyglot.ast.ArrayAccess;
import polyglot.ast.ArrayInit;
import polyglot.ast.Assert;
import polyglot.ast.Assign;
import polyglot.ast.Binary;
import polyglot.ast.Block;
import polyglot.ast.BooleanLit;
import polyglot.ast.Branch;
import polyglot.ast.Call;
import polyglot.ast.Case;
import polyglot.ast.Cast;
import polyglot.ast.Catch;
import polyglot.ast.CharLit;
import polyglot.ast.ClassLit;
import polyglot.ast.Conditional;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Do;
import polyglot.ast.Empty;
import polyglot.ast.Eval;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldDecl;
import polyglot.ast.FloatLit;
import polyglot.ast.For;
import polyglot.ast.Formal;
import polyglot.ast.If;
import polyglot.ast.Instanceof;
import polyglot.ast.IntLit;
import polyglot.ast.Labeled;
import polyglot.ast.Lit;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.LocalDecl;
import polyglot.ast.New;
import polyglot.ast.NewArray;
import polyglot.ast.Node;
import polyglot.ast.NullLit;
import polyglot.ast.ProcedureCall;
import polyglot.ast.Receiver;
import polyglot.ast.Return;
import polyglot.ast.Special;
import polyglot.ast.StringLit;
import polyglot.ast.Switch;
import polyglot.ast.SwitchBlock;
import polyglot.ast.Synchronized;
import polyglot.ast.Throw;
import polyglot.ast.Try;
import polyglot.ast.TypeNode;
import polyglot.ast.Unary;
import polyglot.ast.While;
import polyglot.types.ArrayType;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.LocalInstance;
import polyglot.types.MemberInstance;
import polyglot.types.MethodInstance;
import polyglot.types.NullType;
import polyglot.types.PrimitiveType;
import polyglot.util.IdentityKey;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FastHierarchy;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.MethodSource;
import soot.Modifier;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ConditionExpr;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.EqExpr;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GotoStmt;
import soot.jimple.GtExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.LtExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import soot.tagkit.EnclosingTag;
import soot.tagkit.Host;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.SyntheticTag;
import soot.tagkit.ThrowCreatedByCompilerTag;

public class JimpleBodyBuilder extends AbstractJimpleBodyBuilder {
   protected List<List<Stmt>> beforeReturn;
   protected List<List<Stmt>> afterReturn;
   protected ArrayList<Trap> exceptionTable;
   protected Stack<Stmt> endControlNoop = new Stack();
   protected Stack<Stmt> condControlNoop = new Stack();
   protected Stack<Value> monitorStack;
   protected Stack<Try> tryStack;
   protected Stack<Try> catchStack;
   protected Stack<Stmt> trueNoop = new Stack();
   protected Stack<Stmt> falseNoop = new Stack();
   protected HashMap<String, Stmt> labelBreakMap;
   protected HashMap<String, Stmt> labelContinueMap;
   protected HashMap<polyglot.ast.Stmt, Stmt> labelMap;
   protected HashMap<IdentityKey, Local> localsMap = new HashMap();
   protected HashMap getThisMap = new HashMap();
   protected Local specialThisLocal;
   protected Local outerClassParamLocal;
   protected int paramRefCount = 0;
   protected LocalGenerator lg;
   int inLeftOr = 0;

   public JimpleBody createJimpleBody(Block block, List formals, SootMethod sootMethod) {
      this.createBody(sootMethod);
      this.lg = new LocalGenerator(this.body);
      if (!Modifier.isStatic(sootMethod.getModifiers())) {
         RefType type = sootMethod.getDeclaringClass().getType();
         this.specialThisLocal = Jimple.v().newLocal("this", type);
         this.body.getLocals().add(this.specialThisLocal);
         ThisRef thisRef = Jimple.v().newThisRef(type);
         Stmt thisStmt = Jimple.v().newIdentityStmt(this.specialThisLocal, thisRef);
         this.body.getUnits().add((Unit)thisStmt);
      }

      int formalsCounter = 0;
      int outerIndex = sootMethod.getDeclaringClass().getName().lastIndexOf("$");
      ParameterRef paramRef;
      IdentityStmt stmt;
      if (outerIndex != -1 && sootMethod.getName().equals("<init>")) {
         SootField this0Field = sootMethod.getDeclaringClass().getFieldByNameUnsafe("this$0");
         if (this0Field != null) {
            SootClass outerClass = ((RefType)this0Field.getType()).getSootClass();
            Local outerLocal = this.lg.generateLocal(outerClass.getType());
            paramRef = Jimple.v().newParameterRef(outerClass.getType(), formalsCounter);
            ++this.paramRefCount;
            stmt = Jimple.v().newIdentityStmt(outerLocal, paramRef);
            stmt.addTag(new EnclosingTag());
            this.body.getUnits().add((Unit)stmt);
            ((PolyglotMethodSource)sootMethod.getSource()).setOuterClassThisInit(outerLocal);
            this.outerClassParamLocal = outerLocal;
            ++formalsCounter;
         }
      }

      Iterator finalsIt;
      if (formals != null) {
         String[] formalNames = new String[formals.size()];

         for(finalsIt = formals.iterator(); finalsIt.hasNext(); ++formalsCounter) {
            Formal formal = (Formal)finalsIt.next();
            this.createFormal(formal, formalsCounter);
            formalNames[formalsCounter] = formal.name();
         }

         this.body.getMethod().addTag(new ParamNamesTag(formalNames));
      }

      ArrayList<SootField> finalsList = ((PolyglotMethodSource)this.body.getMethod().getSource()).getFinalsList();
      if (finalsList != null) {
         for(finalsIt = finalsList.iterator(); finalsIt.hasNext(); ++formalsCounter) {
            SootField sf = (SootField)finalsIt.next();
            paramRef = Jimple.v().newParameterRef(sf.getType(), formalsCounter);
            ++this.paramRefCount;
            stmt = Jimple.v().newIdentityStmt(this.lg.generateLocal(sf.getType()), paramRef);
            this.body.getUnits().add((Unit)stmt);
         }
      }

      this.createBlock(block);
      if (sootMethod.getName().equals("<clinit>")) {
         this.handleAssert(sootMethod);
         this.handleStaticFieldInits(sootMethod);
         this.handleStaticInitializerBlocks(sootMethod);
      }

      boolean hasReturn = false;
      if (block != null) {
         Iterator it = block.statements().iterator();

         while(it.hasNext()) {
            Object next = it.next();
            if (next instanceof Return) {
               hasReturn = true;
            }
         }
      }

      Type retType = this.body.getMethod().getReturnType();
      if (!hasReturn && retType instanceof VoidType) {
         Stmt retStmt = Jimple.v().newReturnVoidStmt();
         this.body.getUnits().add((Unit)retStmt);
      }

      if (this.exceptionTable != null) {
         Iterator trapsIt = this.exceptionTable.iterator();

         while(trapsIt.hasNext()) {
            this.body.getTraps().add(trapsIt.next());
         }
      }

      return this.body;
   }

   private void handleAssert(SootMethod sootMethod) {
      if (((PolyglotMethodSource)sootMethod.getSource()).hasAssert()) {
         ((PolyglotMethodSource)sootMethod.getSource()).addAssertInits(this.body);
      }
   }

   private void handleFieldInits(SootMethod sootMethod) {
      ArrayList<FieldDecl> fieldInits = ((PolyglotMethodSource)sootMethod.getSource()).getFieldInits();
      if (fieldInits != null) {
         this.handleFieldInits(fieldInits);
      }

   }

   protected void handleFieldInits(ArrayList<FieldDecl> fieldInits) {
      Iterator fieldInitsIt = fieldInits.iterator();

      while(fieldInitsIt.hasNext()) {
         FieldDecl field = (FieldDecl)fieldInitsIt.next();
         String fieldName = field.name();
         Expr initExpr = field.init();
         SootClass currentClass = this.body.getMethod().getDeclaringClass();
         SootFieldRef sootField = Scene.v().makeFieldRef(currentClass, fieldName, Util.getSootType(field.type().type()), field.flags().isStatic());
         Local base = this.specialThisLocal;
         FieldRef fieldRef = Jimple.v().newInstanceFieldRef(base, sootField);
         Object sootExpr;
         if (initExpr instanceof ArrayInit) {
            sootExpr = this.getArrayInitLocal((ArrayInit)initExpr, field.type().type());
         } else {
            sootExpr = this.base().createAggressiveExpr(initExpr, false, false);
         }

         if (sootExpr instanceof ConditionExpr) {
            sootExpr = this.handleCondBinExpr((ConditionExpr)sootExpr);
         }

         AssignStmt assign;
         if (sootExpr instanceof Local) {
            assign = Jimple.v().newAssignStmt(fieldRef, (Value)sootExpr);
         } else {
            if (!(sootExpr instanceof Constant)) {
               throw new RuntimeException("fields must assign to local or constant only");
            }

            assign = Jimple.v().newAssignStmt(fieldRef, (Value)sootExpr);
         }

         this.body.getUnits().add((Unit)assign);
         Util.addLnPosTags(assign, initExpr.position());
         Util.addLnPosTags(assign.getRightOpBox(), initExpr.position());
      }

   }

   private void handleOuterClassThisInit(SootMethod sootMethod) {
      SootField this0Field = this.body.getMethod().getDeclaringClass().getFieldByNameUnsafe("this$0");
      if (this0Field != null) {
         FieldRef fieldRef = Jimple.v().newInstanceFieldRef(this.specialThisLocal, this0Field.makeRef());
         AssignStmt stmt = Jimple.v().newAssignStmt(fieldRef, this.outerClassParamLocal);
         this.body.getUnits().add((Unit)stmt);
      }

   }

   private void handleStaticFieldInits(SootMethod sootMethod) {
      ArrayList<FieldDecl> staticFieldInits = ((PolyglotMethodSource)sootMethod.getSource()).getStaticFieldInits();
      if (staticFieldInits != null) {
         Iterator staticFieldInitsIt = staticFieldInits.iterator();

         while(staticFieldInitsIt.hasNext()) {
            FieldDecl field = (FieldDecl)staticFieldInitsIt.next();
            String fieldName = field.name();
            Expr initExpr = field.init();
            SootClass currentClass = this.body.getMethod().getDeclaringClass();
            SootFieldRef sootField = Scene.v().makeFieldRef(currentClass, fieldName, Util.getSootType(field.type().type()), field.flags().isStatic());
            FieldRef fieldRef = Jimple.v().newStaticFieldRef(sootField);
            Object sootExpr;
            if (initExpr instanceof ArrayInit) {
               sootExpr = this.getArrayInitLocal((ArrayInit)initExpr, field.type().type());
            } else {
               sootExpr = this.base().createAggressiveExpr(initExpr, false, false);
               if (sootExpr instanceof ConditionExpr) {
                  sootExpr = this.handleCondBinExpr((ConditionExpr)sootExpr);
               }
            }

            Stmt assign = Jimple.v().newAssignStmt(fieldRef, (Value)sootExpr);
            this.body.getUnits().add((Unit)assign);
            Util.addLnPosTags(assign, initExpr.position());
         }
      }

   }

   private void handleInitializerBlocks(SootMethod sootMethod) {
      ArrayList<Block> initializerBlocks = ((PolyglotMethodSource)sootMethod.getSource()).getInitializerBlocks();
      if (initializerBlocks != null) {
         this.handleStaticBlocks(initializerBlocks);
      }

   }

   protected void handleStaticBlocks(ArrayList<Block> initializerBlocks) {
      Iterator initBlocksIt = initializerBlocks.iterator();

      while(initBlocksIt.hasNext()) {
         this.createBlock((Block)initBlocksIt.next());
      }

   }

   private void handleStaticInitializerBlocks(SootMethod sootMethod) {
      ArrayList<Block> staticInitializerBlocks = ((PolyglotMethodSource)sootMethod.getSource()).getStaticInitializerBlocks();
      if (staticInitializerBlocks != null) {
         Iterator staticInitBlocksIt = staticInitializerBlocks.iterator();

         while(staticInitBlocksIt.hasNext()) {
            this.createBlock((Block)staticInitBlocksIt.next());
         }
      }

   }

   private void createBody(SootMethod sootMethod) {
      this.body = Jimple.v().newBody(sootMethod);
      sootMethod.setActiveBody(this.body);
   }

   private void createBlock(Block block) {
      if (block != null) {
         Iterator it = block.statements().iterator();

         while(it.hasNext()) {
            Object next = it.next();
            if (!(next instanceof polyglot.ast.Stmt)) {
               throw new RuntimeException("Unexpected - Unhandled Node");
            }

            this.createStmt((polyglot.ast.Stmt)next);
         }

      }
   }

   private Local createCatchFormal(Formal formal) {
      Type sootType = Util.getSootType(formal.type().type());
      Local formalLocal = this.createLocal(formal.localInstance());
      CaughtExceptionRef exceptRef = Jimple.v().newCaughtExceptionRef();
      Stmt stmt = Jimple.v().newIdentityStmt(formalLocal, exceptRef);
      this.body.getUnits().add((Unit)stmt);
      Util.addLnPosTags(stmt, formal.position());
      Util.addLnPosTags(((IdentityStmt)stmt).getRightOpBox(), formal.position());
      String[] names = new String[]{formal.name()};
      stmt.addTag(new ParamNamesTag(names));
      return formalLocal;
   }

   private void createFormal(Formal formal, int counter) {
      Type sootType = Util.getSootType(formal.type().type());
      Local formalLocal = this.createLocal(formal.localInstance());
      ParameterRef paramRef = Jimple.v().newParameterRef(sootType, counter);
      ++this.paramRefCount;
      Stmt stmt = Jimple.v().newIdentityStmt(formalLocal, paramRef);
      this.body.getUnits().add((Unit)stmt);
      Util.addLnPosTags(((IdentityStmt)stmt).getRightOpBox(), formal.position());
      Util.addLnPosTags(stmt, formal.position());
   }

   private Value createLiteral(Lit lit) {
      if (lit instanceof IntLit) {
         IntLit intLit = (IntLit)lit;
         long litValue = intLit.value();
         return (Value)(intLit.kind() == IntLit.INT ? IntConstant.v((int)litValue) : LongConstant.v(litValue));
      } else if (lit instanceof StringLit) {
         String litValue = ((StringLit)lit).value();
         return StringConstant.v(litValue);
      } else if (lit instanceof NullLit) {
         return NullConstant.v();
      } else if (lit instanceof FloatLit) {
         FloatLit floatLit = (FloatLit)lit;
         double litValue = floatLit.value();
         return (Value)(floatLit.kind() == FloatLit.DOUBLE ? DoubleConstant.v(floatLit.value()) : FloatConstant.v((float)floatLit.value()));
      } else if (lit instanceof CharLit) {
         char litValue = ((CharLit)lit).value();
         return IntConstant.v(litValue);
      } else if (lit instanceof BooleanLit) {
         boolean litValue = ((BooleanLit)lit).value();
         return litValue ? IntConstant.v(1) : IntConstant.v(0);
      } else if (lit instanceof ClassLit) {
         return this.getSpecialClassLitLocal((ClassLit)lit);
      } else {
         throw new RuntimeException("Unknown Literal - Unhandled: " + lit.getClass());
      }
   }

   private Local createLocal(LocalInstance localInst) {
      Type sootType = Util.getSootType(localInst.type());
      String name = localInst.name();
      Local sootLocal = this.createLocal(name, sootType);
      this.localsMap.put(new IdentityKey(localInst), sootLocal);
      return sootLocal;
   }

   private Local createLocal(String name, Type sootType) {
      Local sootLocal = Jimple.v().newLocal(name, sootType);
      this.body.getLocals().add(sootLocal);
      return sootLocal;
   }

   private Local getLocal(polyglot.ast.Local local) {
      return this.getLocal(local.localInstance());
   }

   private Local getLocal(LocalInstance li) {
      Local fieldLocal;
      if (this.localsMap.containsKey(new IdentityKey(li))) {
         fieldLocal = (Local)this.localsMap.get(new IdentityKey(li));
         return fieldLocal;
      } else if (this.body.getMethod().getDeclaringClass().declaresField("val$" + li.name(), Util.getSootType(li.type()))) {
         fieldLocal = this.generateLocal(li.type());
         SootFieldRef field = Scene.v().makeFieldRef(this.body.getMethod().getDeclaringClass(), "val$" + li.name(), Util.getSootType(li.type()), false);
         FieldRef fieldRef = Jimple.v().newInstanceFieldRef(this.specialThisLocal, field);
         AssignStmt assign = Jimple.v().newAssignStmt(fieldLocal, fieldRef);
         this.body.getUnits().add((Unit)assign);
         return fieldLocal;
      } else {
         SootClass currentClass = this.body.getMethod().getDeclaringClass();

         SootClass outerClass;
         for(boolean fieldFound = false; !fieldFound; currentClass = outerClass) {
            if (!currentClass.declaresFieldByName("this$0")) {
               throw new RuntimeException("Trying to get field val$" + li.name() + " from some outer class but can't access the outer class of: " + currentClass.getName() + "! current class contains fields: " + currentClass.getFields());
            }

            outerClass = ((RefType)currentClass.getFieldByName("this$0").getType()).getSootClass();
            if (outerClass.declaresField("val$" + li.name(), Util.getSootType(li.type()))) {
               fieldFound = true;
            }
         }

         SootMethod methToInvoke = this.makeLiFieldAccessMethod(currentClass, li);
         ArrayList methParams = new ArrayList();
         methParams.add(this.getThis(currentClass.getType()));
         Local res = Util.getPrivateAccessFieldInvoke(methToInvoke.makeRef(), methParams, this.body, this.lg);
         return res;
      }
   }

   private SootMethod makeLiFieldAccessMethod(SootClass classToInvoke, LocalInstance li) {
      String name = "access$" + InitialResolver.v().getNextPrivateAccessCounter() + "00";
      ArrayList paramTypes = new ArrayList();
      paramTypes.add(classToInvoke.getType());
      SootMethod meth = Scene.v().makeSootMethod(name, paramTypes, Util.getSootType(li.type()), 8);
      classToInvoke.addMethod(meth);
      PrivateFieldAccMethodSource src = new PrivateFieldAccMethodSource(Util.getSootType(li.type()), "val$" + li.name(), false, classToInvoke);
      meth.setActiveBody(src.getBody(meth, (String)null));
      meth.addTag(new SyntheticTag());
      return meth;
   }

   protected void createStmt(polyglot.ast.Stmt stmt) {
      if (stmt instanceof Eval) {
         this.base().createAggressiveExpr(((Eval)stmt).expr(), false, false);
      } else if (stmt instanceof If) {
         this.createIf2((If)stmt);
      } else if (stmt instanceof LocalDecl) {
         this.createLocalDecl((LocalDecl)stmt);
      } else if (stmt instanceof Block) {
         this.createBlock((Block)stmt);
      } else if (stmt instanceof While) {
         this.createWhile2((While)stmt);
      } else if (stmt instanceof Do) {
         this.createDo2((Do)stmt);
      } else if (stmt instanceof For) {
         this.createForLoop2((For)stmt);
      } else if (stmt instanceof Switch) {
         this.createSwitch((Switch)stmt);
      } else if (stmt instanceof Return) {
         this.createReturn((Return)stmt);
      } else if (stmt instanceof Branch) {
         this.createBranch((Branch)stmt);
      } else if (stmt instanceof ConstructorCall) {
         this.createConstructorCall((ConstructorCall)stmt);
      } else if (!(stmt instanceof Empty)) {
         if (stmt instanceof Throw) {
            this.createThrow((Throw)stmt);
         } else if (stmt instanceof Try) {
            this.createTry((Try)stmt);
         } else if (stmt instanceof Labeled) {
            this.createLabeled((Labeled)stmt);
         } else if (stmt instanceof Synchronized) {
            this.createSynchronized((Synchronized)stmt);
         } else if (stmt instanceof Assert) {
            this.createAssert((Assert)stmt);
         } else {
            if (!(stmt instanceof LocalClassDecl)) {
               throw new RuntimeException("Unhandled Stmt: " + stmt.getClass());
            }

            this.createLocalClassDecl((LocalClassDecl)stmt);
         }
      }

   }

   private boolean needSootIf(Value sootCond) {
      return !(sootCond instanceof IntConstant) || ((IntConstant)sootCond).value != 1;
   }

   private void createIf2(If ifExpr) {
      NopStmt endTgt = Jimple.v().newNopStmt();
      NopStmt brchTgt = Jimple.v().newNopStmt();
      Expr condition = ifExpr.cond();
      this.createBranchingExpr(condition, brchTgt, false);
      polyglot.ast.Stmt consequence = ifExpr.consequent();
      this.createStmt(consequence);
      Stmt goto1 = Jimple.v().newGotoStmt((Unit)endTgt);
      this.body.getUnits().add((Unit)goto1);
      this.body.getUnits().add((Unit)brchTgt);
      polyglot.ast.Stmt alternative = ifExpr.alternative();
      if (alternative != null) {
         this.createStmt(alternative);
      }

      this.body.getUnits().add((Unit)endTgt);
   }

   private void createBranchingExpr(Expr expr, Stmt tgt, boolean boto) {
      NopStmt t1;
      Binary cond_or;
      if (expr instanceof Binary && ((Binary)expr).operator() == Binary.COND_AND) {
         cond_or = (Binary)expr;
         if (boto) {
            t1 = Jimple.v().newNopStmt();
            this.createBranchingExpr(cond_or.left(), t1, false);
            this.createBranchingExpr(cond_or.right(), tgt, true);
            this.body.getUnits().add((Unit)t1);
         } else {
            this.createBranchingExpr(cond_or.left(), tgt, false);
            this.createBranchingExpr(cond_or.right(), tgt, false);
         }
      } else if (expr instanceof Binary && ((Binary)expr).operator() == Binary.COND_OR) {
         cond_or = (Binary)expr;
         if (boto) {
            this.createBranchingExpr(cond_or.left(), tgt, true);
            this.createBranchingExpr(cond_or.right(), tgt, true);
         } else {
            t1 = Jimple.v().newNopStmt();
            this.createBranchingExpr(cond_or.left(), t1, true);
            this.createBranchingExpr(cond_or.right(), tgt, false);
            this.body.getUnits().add((Unit)t1);
         }
      } else if (expr instanceof Unary && ((Unary)expr).operator() == Unary.NOT) {
         Unary not = (Unary)expr;
         this.createBranchingExpr(not.expr(), tgt, !boto);
      } else {
         Value sootCond = this.base().createAggressiveExpr(expr, false, false);
         boolean needIf = this.needSootIf(sootCond);
         if (needIf) {
            Object sootCond;
            if (!(sootCond instanceof ConditionExpr)) {
               if (!boto) {
                  sootCond = Jimple.v().newEqExpr(sootCond, IntConstant.v(0));
               } else {
                  sootCond = Jimple.v().newNeExpr(sootCond, IntConstant.v(0));
               }
            } else {
               sootCond = this.handleDFLCond((ConditionExpr)sootCond);
               if (!boto) {
                  sootCond = this.reverseCondition((ConditionExpr)sootCond);
               }
            }

            IfStmt ifStmt = Jimple.v().newIfStmt((Value)sootCond, (Unit)tgt);
            this.body.getUnits().add((Unit)ifStmt);
            Util.addLnPosTags(ifStmt.getConditionBox(), expr.position());
            Util.addLnPosTags(ifStmt, expr.position());
         } else if (sootCond instanceof IntConstant && ((IntConstant)sootCond).value == 1 == boto) {
            GotoStmt gotoStmt = Jimple.v().newGotoStmt((Unit)tgt);
            this.body.getUnits().add((Unit)gotoStmt);
            Util.addLnPosTags(gotoStmt, expr.position());
         }
      }

   }

   private void createWhile2(While whileStmt) {
      Stmt brchTgt = Jimple.v().newNopStmt();
      Stmt beginTgt = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)beginTgt);
      this.endControlNoop.push(Jimple.v().newNopStmt());
      this.condControlNoop.push(Jimple.v().newNopStmt());
      Stmt continueStmt = (Stmt)this.condControlNoop.pop();
      this.body.getUnits().add((Unit)continueStmt);
      this.condControlNoop.push(continueStmt);
      Expr condition = whileStmt.cond();
      this.createBranchingExpr(condition, brchTgt, false);
      this.createStmt(whileStmt.body());
      GotoStmt gotoLoop = Jimple.v().newGotoStmt((Unit)beginTgt);
      this.body.getUnits().add((Unit)gotoLoop);
      this.body.getUnits().add((Unit)this.endControlNoop.pop());
      this.body.getUnits().add((Unit)brchTgt);
      this.condControlNoop.pop();
   }

   private void createDo2(Do doStmt) {
      Stmt noop1 = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)noop1);
      this.endControlNoop.push(Jimple.v().newNopStmt());
      this.condControlNoop.push(Jimple.v().newNopStmt());
      this.createStmt(doStmt.body());
      Stmt continueStmt = (Stmt)this.condControlNoop.pop();
      this.body.getUnits().add((Unit)continueStmt);
      this.condControlNoop.push(continueStmt);
      if (this.labelMap != null && this.labelMap.containsKey(doStmt)) {
         this.body.getUnits().add((Unit)this.labelMap.get(doStmt));
      }

      Expr condition = doStmt.cond();
      this.createBranchingExpr(condition, noop1, true);
      this.body.getUnits().add((Unit)this.endControlNoop.pop());
      this.condControlNoop.pop();
   }

   private void createForLoop2(For forStmt) {
      this.endControlNoop.push(Jimple.v().newNopStmt());
      this.condControlNoop.push(Jimple.v().newNopStmt());
      Iterator initsIt = forStmt.inits().iterator();

      while(initsIt.hasNext()) {
         this.createStmt((polyglot.ast.Stmt)initsIt.next());
      }

      Stmt noop1 = Jimple.v().newNopStmt();
      Stmt noop2 = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)noop2);
      Expr condition = forStmt.cond();
      if (condition != null) {
         this.createBranchingExpr(condition, noop1, false);
      }

      this.createStmt(forStmt.body());
      this.body.getUnits().add((Unit)this.condControlNoop.pop());
      if (this.labelMap != null && this.labelMap.containsKey(forStmt)) {
         this.body.getUnits().add((Unit)this.labelMap.get(forStmt));
      }

      Iterator itersIt = forStmt.iters().iterator();

      while(itersIt.hasNext()) {
         this.createStmt((polyglot.ast.Stmt)itersIt.next());
      }

      Stmt goto1 = Jimple.v().newGotoStmt((Unit)noop2);
      this.body.getUnits().add((Unit)goto1);
      this.body.getUnits().add((Unit)noop1);
      this.body.getUnits().add((Unit)this.endControlNoop.pop());
   }

   private void createLocalDecl(LocalDecl localDecl) {
      String name = localDecl.name();
      LocalInstance localInst = localDecl.localInstance();
      Value lhs = this.createLocal(localInst);
      Expr expr = localDecl.init();
      if (expr != null) {
         Object rhs;
         if (expr instanceof ArrayInit) {
            rhs = this.getArrayInitLocal((ArrayInit)expr, localInst.type());
         } else {
            rhs = this.base().createAggressiveExpr(expr, false, false);
         }

         if (rhs instanceof ConditionExpr) {
            rhs = this.handleCondBinExpr((ConditionExpr)rhs);
         }

         AssignStmt stmt = Jimple.v().newAssignStmt(lhs, (Value)rhs);
         this.body.getUnits().add((Unit)stmt);
         Util.addLnPosTags(stmt, localDecl.position());
         if (localDecl.position() != null) {
            Util.addLnPosTags(stmt.getLeftOpBox(), localDecl.position().line(), localDecl.position().endLine(), localDecl.position().endColumn() - name.length(), localDecl.position().endColumn());
            if (expr != null) {
               Util.addLnPosTags(stmt, localDecl.position().line(), expr.position().endLine(), localDecl.position().column(), expr.position().endColumn());
            } else {
               Util.addLnPosTags(stmt, localDecl.position().line(), localDecl.position().endLine(), localDecl.position().column(), localDecl.position().endColumn());
            }
         }

         if (expr != null) {
            Util.addLnPosTags(stmt.getRightOpBox(), expr.position());
         }
      }

   }

   private void createSwitch(Switch switchStmt) {
      Expr value = switchStmt.expr();
      Value sootValue = this.base().createAggressiveExpr(value, false, false);
      if (switchStmt.elements().size() != 0) {
         Stmt defaultTarget = null;
         Case[] caseArray = new Case[switchStmt.elements().size()];
         Stmt[] targetsArray = new Stmt[switchStmt.elements().size()];
         ArrayList<Stmt> targets = new ArrayList();
         HashMap<Object, Stmt> targetsMap = new HashMap();
         int counter = 0;
         Iterator it = switchStmt.elements().iterator();

         while(it.hasNext()) {
            Object next = it.next();
            if (next instanceof Case) {
               Stmt noop = Jimple.v().newNopStmt();
               if (!((Case)next).isDefault()) {
                  targets.add(noop);
                  caseArray[counter] = (Case)next;
                  targetsArray[counter] = noop;
                  ++counter;
                  targetsMap.put(next, noop);
               } else {
                  defaultTarget = noop;
               }
            }
         }

         int lowIndex = false;
         int highIndex = false;

         int j;
         for(int i = 0; i < counter; ++i) {
            for(j = i + 1; j < counter; ++j) {
               if (caseArray[j].value() < caseArray[i].value()) {
                  Case tempCase = caseArray[i];
                  Stmt tempTarget = targetsArray[i];
                  caseArray[i] = caseArray[j];
                  targetsArray[i] = targetsArray[j];
                  caseArray[j] = tempCase;
                  targetsArray[j] = tempTarget;
               }
            }
         }

         ArrayList sortedTargets = new ArrayList();

         for(j = 0; j < counter; ++j) {
            sortedTargets.add(targetsArray[j]);
         }

         boolean hasDefaultTarget = true;
         if (defaultTarget == null) {
            Stmt noop = Jimple.v().newNopStmt();
            defaultTarget = noop;
            hasDefaultTarget = false;
         }

         Object sootSwitchStmt;
         if (this.isLookupSwitch(switchStmt)) {
            ArrayList values = new ArrayList();

            for(int i = 0; i < counter; ++i) {
               if (!caseArray[i].isDefault()) {
                  values.add(IntConstant.v((int)caseArray[i].value()));
               }
            }

            LookupSwitchStmt lookupStmt = Jimple.v().newLookupSwitchStmt(sootValue, values, sortedTargets, (Unit)defaultTarget);
            Util.addLnPosTags(lookupStmt.getKeyBox(), value.position());
            sootSwitchStmt = lookupStmt;
         } else {
            long lowVal = 0L;
            long highVal = 0L;
            boolean unknown = true;
            it = switchStmt.elements().iterator();

            while(it.hasNext()) {
               Object next = it.next();
               if (next instanceof Case && !((Case)next).isDefault()) {
                  long temp = ((Case)next).value();
                  if (unknown) {
                     highVal = temp;
                     lowVal = temp;
                     unknown = false;
                  }

                  if (temp > highVal) {
                     highVal = temp;
                  }

                  if (temp < lowVal) {
                     lowVal = temp;
                  }
               }
            }

            TableSwitchStmt tableStmt = Jimple.v().newTableSwitchStmt(sootValue, (int)lowVal, (int)highVal, sortedTargets, (Unit)defaultTarget);
            Util.addLnPosTags(tableStmt.getKeyBox(), value.position());
            sootSwitchStmt = tableStmt;
         }

         this.body.getUnits().add((Unit)sootSwitchStmt);
         Util.addLnPosTags((Host)sootSwitchStmt, switchStmt.position());
         this.endControlNoop.push(Jimple.v().newNopStmt());
         it = switchStmt.elements().iterator();
         Iterator var33 = targets.iterator();

         while(it.hasNext()) {
            Object next = it.next();
            if (next instanceof Case) {
               if (!((Case)next).isDefault()) {
                  this.body.getUnits().add((Unit)targetsMap.get(next));
               } else {
                  this.body.getUnits().add((Unit)defaultTarget);
               }
            } else {
               SwitchBlock blockStmt = (SwitchBlock)next;
               this.createBlock(blockStmt);
            }
         }

         if (!hasDefaultTarget) {
            this.body.getUnits().add((Unit)defaultTarget);
         }

         this.body.getUnits().add((Unit)this.endControlNoop.pop());
      }
   }

   private boolean isLookupSwitch(Switch switchStmt) {
      int lowest = 0;
      int highest = 0;
      int counter = 0;
      Iterator it = switchStmt.elements().iterator();

      while(true) {
         Case caseStmt;
         do {
            Object next;
            do {
               if (!it.hasNext()) {
                  if (counter - 1 == highest - lowest) {
                     return false;
                  }

                  return true;
               }

               next = it.next();
            } while(!(next instanceof Case));

            caseStmt = (Case)next;
         } while(caseStmt.isDefault());

         int caseValue = (int)caseStmt.value();
         if (caseValue <= lowest || counter == 0) {
            lowest = caseValue;
         }

         if (caseValue >= highest || counter == 0) {
            highest = caseValue;
         }

         ++counter;
      }
   }

   private void createBranch(Branch branchStmt) {
      Try currentTry;
      if (this.tryStack != null && !this.tryStack.isEmpty()) {
         currentTry = (Try)this.tryStack.pop();
         if (currentTry.finallyBlock() != null) {
            this.createBlock(currentTry.finallyBlock());
            this.tryStack.push(currentTry);
         } else {
            this.tryStack.push(currentTry);
         }
      }

      if (this.catchStack != null && !this.catchStack.isEmpty()) {
         currentTry = (Try)this.catchStack.pop();
         if (currentTry.finallyBlock() != null) {
            this.createBlock(currentTry.finallyBlock());
            this.catchStack.push(currentTry);
         } else {
            this.catchStack.push(currentTry);
         }
      }

      this.body.getUnits().add((Unit)Jimple.v().newNopStmt());
      Stack putBack;
      Local exitVal;
      ExitMonitorStmt emStmt;
      Stmt gotoCondNoop;
      GotoStmt gotoLabel;
      GotoStmt gotoCond;
      if (branchStmt.kind() == Branch.BREAK) {
         if (branchStmt.label() == null) {
            gotoCondNoop = (Stmt)this.endControlNoop.pop();
            if (this.monitorStack != null) {
               putBack = new Stack();

               while(!this.monitorStack.isEmpty()) {
                  exitVal = (Local)this.monitorStack.pop();
                  putBack.push(exitVal);
                  emStmt = Jimple.v().newExitMonitorStmt(exitVal);
                  this.body.getUnits().add((Unit)emStmt);
               }

               while(!putBack.isEmpty()) {
                  this.monitorStack.push(putBack.pop());
               }
            }

            gotoCond = Jimple.v().newGotoStmt((Unit)gotoCondNoop);
            this.endControlNoop.push(gotoCondNoop);
            this.body.getUnits().add((Unit)gotoCond);
            Util.addLnPosTags(gotoCond, branchStmt.position());
         } else {
            gotoLabel = Jimple.v().newGotoStmt((Unit)this.labelBreakMap.get(branchStmt.label()));
            this.body.getUnits().add((Unit)gotoLabel);
            Util.addLnPosTags(gotoLabel, branchStmt.position());
         }
      } else if (branchStmt.kind() == Branch.CONTINUE) {
         if (branchStmt.label() == null) {
            gotoCondNoop = (Stmt)this.condControlNoop.pop();
            if (this.monitorStack != null) {
               putBack = new Stack();

               while(!this.monitorStack.isEmpty()) {
                  exitVal = (Local)this.monitorStack.pop();
                  putBack.push(exitVal);
                  emStmt = Jimple.v().newExitMonitorStmt(exitVal);
                  this.body.getUnits().add((Unit)emStmt);
               }

               while(!putBack.isEmpty()) {
                  this.monitorStack.push(putBack.pop());
               }
            }

            gotoCond = Jimple.v().newGotoStmt((Unit)gotoCondNoop);
            this.condControlNoop.push(gotoCondNoop);
            this.body.getUnits().add((Unit)gotoCond);
            Util.addLnPosTags(gotoCond, branchStmt.position());
         } else {
            gotoLabel = Jimple.v().newGotoStmt((Unit)this.labelContinueMap.get(branchStmt.label()));
            this.body.getUnits().add((Unit)gotoLabel);
            Util.addLnPosTags(gotoLabel, branchStmt.position());
         }
      }

   }

   private void createLabeled(Labeled labeledStmt) {
      String label = labeledStmt.label();
      polyglot.ast.Stmt stmt = labeledStmt.statement();
      Stmt noop = Jimple.v().newNopStmt();
      if (!(stmt instanceof For) && !(stmt instanceof Do)) {
         this.body.getUnits().add((Unit)noop);
      }

      if (this.labelMap == null) {
         this.labelMap = new HashMap();
      }

      this.labelMap.put(stmt, noop);
      if (this.labelBreakMap == null) {
         this.labelBreakMap = new HashMap();
      }

      if (this.labelContinueMap == null) {
         this.labelContinueMap = new HashMap();
      }

      this.labelContinueMap.put(label, noop);
      Stmt noop2 = Jimple.v().newNopStmt();
      this.labelBreakMap.put(label, noop2);
      this.createStmt(stmt);
      this.body.getUnits().add((Unit)noop2);
   }

   private void createAssert(Assert assertStmt) {
      Local testLocal = this.lg.generateLocal(BooleanType.v());
      SootFieldRef assertField = Scene.v().makeFieldRef(this.body.getMethod().getDeclaringClass(), "$assertionsDisabled", BooleanType.v(), true);
      FieldRef assertFieldRef = Jimple.v().newStaticFieldRef(assertField);
      AssignStmt fieldAssign = Jimple.v().newAssignStmt(testLocal, assertFieldRef);
      this.body.getUnits().add((Unit)fieldAssign);
      NopStmt nop1 = Jimple.v().newNopStmt();
      ConditionExpr cond1 = Jimple.v().newNeExpr(testLocal, IntConstant.v(0));
      IfStmt testIf = Jimple.v().newIfStmt(cond1, (Unit)nop1);
      this.body.getUnits().add((Unit)testIf);
      if (!(assertStmt.cond() instanceof BooleanLit) || ((BooleanLit)assertStmt.cond()).value()) {
         Value sootCond = this.base().createAggressiveExpr(assertStmt.cond(), false, false);
         boolean needIf = this.needSootIf(sootCond);
         Object sootCond;
         if (!(sootCond instanceof ConditionExpr)) {
            sootCond = Jimple.v().newEqExpr(sootCond, IntConstant.v(1));
         } else {
            sootCond = this.handleDFLCond((ConditionExpr)sootCond);
         }

         if (needIf) {
            IfStmt ifStmt = Jimple.v().newIfStmt((Value)sootCond, (Unit)nop1);
            this.body.getUnits().add((Unit)ifStmt);
            Util.addLnPosTags(ifStmt.getConditionBox(), assertStmt.cond().position());
            Util.addLnPosTags(ifStmt, assertStmt.position());
         }
      }

      Local failureLocal = this.lg.generateLocal(RefType.v("java.lang.AssertionError"));
      NewExpr newExpr = Jimple.v().newNewExpr(RefType.v("java.lang.AssertionError"));
      AssignStmt newAssign = Jimple.v().newAssignStmt(failureLocal, newExpr);
      this.body.getUnits().add((Unit)newAssign);
      ArrayList paramTypes = new ArrayList();
      ArrayList params = new ArrayList();
      if (assertStmt.errorMessage() != null) {
         Value errorExpr = this.base().createAggressiveExpr(assertStmt.errorMessage(), false, false);
         if (errorExpr instanceof ConditionExpr) {
            errorExpr = this.handleCondBinExpr((ConditionExpr)errorExpr);
         }

         Type errorType = ((Value)errorExpr).getType();
         if (assertStmt.errorMessage().type().isChar()) {
            errorType = CharType.v();
         }

         if (errorType instanceof IntType) {
            paramTypes.add(IntType.v());
         } else if (errorType instanceof LongType) {
            paramTypes.add(LongType.v());
         } else if (errorType instanceof FloatType) {
            paramTypes.add(FloatType.v());
         } else if (errorType instanceof DoubleType) {
            paramTypes.add(DoubleType.v());
         } else if (errorType instanceof CharType) {
            paramTypes.add(CharType.v());
         } else if (errorType instanceof BooleanType) {
            paramTypes.add(BooleanType.v());
         } else if (errorType instanceof ShortType) {
            paramTypes.add(IntType.v());
         } else if (errorType instanceof ByteType) {
            paramTypes.add(IntType.v());
         } else {
            paramTypes.add(Scene.v().getSootClass("java.lang.Object").getType());
         }

         params.add(errorExpr);
      }

      SootMethodRef methToInvoke = Scene.v().makeMethodRef(Scene.v().getSootClass("java.lang.AssertionError"), "<init>", paramTypes, VoidType.v(), false);
      SpecialInvokeExpr invokeExpr = Jimple.v().newSpecialInvokeExpr(failureLocal, methToInvoke, (List)params);
      InvokeStmt invokeStmt = Jimple.v().newInvokeStmt(invokeExpr);
      this.body.getUnits().add((Unit)invokeStmt);
      if (assertStmt.errorMessage() != null) {
         Util.addLnPosTags(invokeExpr.getArgBox(0), assertStmt.errorMessage().position());
      }

      ThrowStmt throwStmt = Jimple.v().newThrowStmt(failureLocal);
      this.body.getUnits().add((Unit)throwStmt);
      this.body.getUnits().add((Unit)nop1);
   }

   private void createSynchronized(Synchronized synchStmt) {
      Value sootExpr = this.base().createAggressiveExpr(synchStmt.expr(), false, false);
      EnterMonitorStmt enterMon = Jimple.v().newEnterMonitorStmt(sootExpr);
      this.body.getUnits().add((Unit)enterMon);
      if (this.beforeReturn == null) {
         this.beforeReturn = new ArrayList();
      }

      if (this.afterReturn == null) {
         this.afterReturn = new ArrayList();
      }

      this.beforeReturn.add(new ArrayList());
      this.afterReturn.add(new ArrayList());
      if (this.monitorStack == null) {
         this.monitorStack = new Stack();
      }

      this.monitorStack.push(sootExpr);
      Util.addLnPosTags(enterMon.getOpBox(), synchStmt.expr().position());
      Util.addLnPosTags(enterMon, synchStmt.expr().position());
      Stmt startNoop = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)startNoop);
      this.createBlock(synchStmt.body());
      ExitMonitorStmt exitMon = Jimple.v().newExitMonitorStmt(sootExpr);
      this.body.getUnits().add((Unit)exitMon);
      this.monitorStack.pop();
      Util.addLnPosTags(exitMon.getOpBox(), synchStmt.expr().position());
      Util.addLnPosTags(exitMon, synchStmt.expr().position());
      Stmt endSynchNoop = Jimple.v().newNopStmt();
      Stmt gotoEnd = Jimple.v().newGotoStmt((Unit)endSynchNoop);
      Stmt endNoop = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)endNoop);
      this.body.getUnits().add((Unit)gotoEnd);
      Stmt catchAllBeforeNoop = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)catchAllBeforeNoop);
      Local formalLocal = this.lg.generateLocal(RefType.v("java.lang.Throwable"));
      CaughtExceptionRef exceptRef = Jimple.v().newCaughtExceptionRef();
      Stmt stmt = Jimple.v().newIdentityStmt(formalLocal, exceptRef);
      this.body.getUnits().add((Unit)stmt);
      Stmt catchBeforeNoop = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)catchBeforeNoop);
      Local local = this.lg.generateLocal(RefType.v("java.lang.Throwable"));
      Stmt assign = Jimple.v().newAssignStmt(local, formalLocal);
      this.body.getUnits().add((Unit)assign);
      ExitMonitorStmt catchExitMon = Jimple.v().newExitMonitorStmt(sootExpr);
      this.body.getUnits().add((Unit)catchExitMon);
      Util.addLnPosTags(catchExitMon.getOpBox(), synchStmt.expr().position());
      Stmt catchAfterNoop = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)catchAfterNoop);
      Stmt throwStmt = Jimple.v().newThrowStmt(local);
      this.body.getUnits().add((Unit)throwStmt);
      this.body.getUnits().add((Unit)endSynchNoop);
      List<Stmt> before = (List)this.beforeReturn.get(this.beforeReturn.size() - 1);
      List<Stmt> after = (List)this.afterReturn.get(this.afterReturn.size() - 1);
      if (before.size() > 0) {
         this.addToExceptionList(startNoop, (Stmt)before.get(0), catchAllBeforeNoop, Scene.v().getSootClass("java.lang.Throwable"));

         for(int i = 1; i < before.size(); ++i) {
            this.addToExceptionList((Stmt)after.get(i - 1), (Stmt)before.get(i), catchAllBeforeNoop, Scene.v().getSootClass("java.lang.Throwable"));
         }

         this.addToExceptionList((Stmt)after.get(after.size() - 1), endNoop, catchAllBeforeNoop, Scene.v().getSootClass("java.lang.Throwable"));
      } else {
         this.addToExceptionList(startNoop, endNoop, catchAllBeforeNoop, Scene.v().getSootClass("java.lang.Throwable"));
      }

      this.beforeReturn.remove(before);
      this.afterReturn.remove(after);
      this.addToExceptionList(catchBeforeNoop, catchAfterNoop, catchAllBeforeNoop, Scene.v().getSootClass("java.lang.Throwable"));
   }

   private void createReturn(Return retStmt) {
      Expr expr = retStmt.expr();
      Value sootLocal = null;
      if (expr != null) {
         sootLocal = this.base().createAggressiveExpr(expr, false, false);
      }

      if (this.monitorStack != null) {
         Stack putBack = new Stack();

         while(!this.monitorStack.isEmpty()) {
            Local exitVal = (Local)this.monitorStack.pop();
            putBack.push(exitVal);
            ExitMonitorStmt emStmt = Jimple.v().newExitMonitorStmt(exitVal);
            this.body.getUnits().add((Unit)emStmt);
         }

         while(!putBack.isEmpty()) {
            this.monitorStack.push(putBack.pop());
         }

         Stmt stopNoop = Jimple.v().newNopStmt();
         this.body.getUnits().add((Unit)stopNoop);
         if (this.beforeReturn != null) {
            Iterator var14 = this.beforeReturn.iterator();

            while(var14.hasNext()) {
               List<Stmt> v = (List)var14.next();
               v.add(stopNoop);
            }
         }
      }

      Try currentTry;
      if (this.tryStack != null && !this.tryStack.isEmpty()) {
         currentTry = (Try)this.tryStack.pop();
         if (currentTry.finallyBlock() != null) {
            this.createBlock(currentTry.finallyBlock());
            this.tryStack.push(currentTry);
         } else {
            this.tryStack.push(currentTry);
         }
      }

      if (this.catchStack != null && !this.catchStack.isEmpty()) {
         currentTry = (Try)this.catchStack.pop();
         if (currentTry.finallyBlock() != null) {
            this.createBlock(currentTry.finallyBlock());
            this.catchStack.push(currentTry);
         } else {
            this.catchStack.push(currentTry);
         }
      }

      if (expr == null) {
         Stmt retStmtVoid = Jimple.v().newReturnVoidStmt();
         this.body.getUnits().add((Unit)retStmtVoid);
         Util.addLnPosTags(retStmtVoid, retStmt.position());
      } else {
         if (sootLocal instanceof ConditionExpr) {
            sootLocal = this.handleCondBinExpr((ConditionExpr)sootLocal);
         }

         ReturnStmt retStmtLocal = Jimple.v().newReturnStmt((Value)sootLocal);
         this.body.getUnits().add((Unit)retStmtLocal);
         Util.addLnPosTags(retStmtLocal.getOpBox(), expr.position());
         Util.addLnPosTags(retStmtLocal, retStmt.position());
      }

      Stmt startNoop = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)startNoop);
      if (this.afterReturn != null) {
         Iterator var13 = this.afterReturn.iterator();

         while(var13.hasNext()) {
            List<Stmt> v = (List)var13.next();
            v.add(startNoop);
         }
      }

   }

   private void createThrow(Throw throwStmt) {
      Value toThrow = this.base().createAggressiveExpr(throwStmt.expr(), false, false);
      ThrowStmt throwSt = Jimple.v().newThrowStmt(toThrow);
      this.body.getUnits().add((Unit)throwSt);
      Util.addLnPosTags(throwSt, throwStmt.position());
      Util.addLnPosTags(throwSt.getOpBox(), throwStmt.expr().position());
   }

   private void createTry(Try tryStmt) {
      Block finallyBlock = tryStmt.finallyBlock();
      if (finallyBlock == null) {
         this.createTryCatch(tryStmt);
      } else {
         this.createTryCatchFinally(tryStmt);
      }

   }

   private void createTryCatch(Try tryStmt) {
      Block tryBlock = tryStmt.tryBlock();
      Stmt noop1 = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)noop1);
      if (this.tryStack == null) {
         this.tryStack = new Stack();
      }

      this.tryStack.push(tryStmt);
      this.createBlock(tryBlock);
      this.tryStack.pop();
      Stmt noop2 = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)noop2);
      Stmt endNoop = Jimple.v().newNopStmt();
      Stmt tryEndGoto = Jimple.v().newGotoStmt((Unit)endNoop);
      this.body.getUnits().add((Unit)tryEndGoto);
      Iterator it = tryStmt.catchBlocks().iterator();

      while(it.hasNext()) {
         Stmt noop3 = Jimple.v().newNopStmt();
         this.body.getUnits().add((Unit)noop3);
         Catch catchBlock = (Catch)it.next();
         this.createCatchFormal(catchBlock.formal());
         if (this.catchStack == null) {
            this.catchStack = new Stack();
         }

         this.catchStack.push(tryStmt);
         this.createBlock(catchBlock.body());
         this.catchStack.pop();
         Stmt catchEndGoto = Jimple.v().newGotoStmt((Unit)endNoop);
         this.body.getUnits().add((Unit)catchEndGoto);
         Type sootType = Util.getSootType(catchBlock.catchType());
         this.addToExceptionList(noop1, noop2, noop3, Scene.v().getSootClass(sootType.toString()));
      }

      this.body.getUnits().add((Unit)endNoop);
   }

   private void createTryCatchFinally(Try tryStmt) {
      HashMap<Stmt, Stmt> gotoMap = new HashMap();
      Block tryBlock = tryStmt.tryBlock();
      Stmt noop1 = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)noop1);
      if (this.tryStack == null) {
         this.tryStack = new Stack();
      }

      this.tryStack.push(tryStmt);
      this.createBlock(tryBlock);
      this.tryStack.pop();
      Stmt noop2 = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)noop2);
      Stmt endNoop = Jimple.v().newNopStmt();
      Stmt tryGotoFinallyNoop = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)tryGotoFinallyNoop);
      Stmt tryFinallyNoop = Jimple.v().newNopStmt();
      Stmt tryGotoFinally = Jimple.v().newGotoStmt((Unit)tryFinallyNoop);
      this.body.getUnits().add((Unit)tryGotoFinally);
      Stmt beforeEndGotoNoop = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)beforeEndGotoNoop);
      Stmt tryEndGoto = Jimple.v().newGotoStmt((Unit)endNoop);
      this.body.getUnits().add((Unit)tryEndGoto);
      gotoMap.put(tryFinallyNoop, beforeEndGotoNoop);
      Stmt catchAllBeforeNoop = Jimple.v().newNopStmt();
      Iterator it = tryStmt.catchBlocks().iterator();

      NopStmt catchStmtsNoop;
      while(it.hasNext()) {
         Stmt noop3 = Jimple.v().newNopStmt();
         this.body.getUnits().add((Unit)noop3);
         Catch catchBlock = (Catch)it.next();
         Stmt catchRefNoop = Jimple.v().newNopStmt();
         this.body.getUnits().add((Unit)catchRefNoop);
         this.createCatchFormal(catchBlock.formal());
         catchStmtsNoop = Jimple.v().newNopStmt();
         this.body.getUnits().add((Unit)catchStmtsNoop);
         if (this.catchStack == null) {
            this.catchStack = new Stack();
         }

         this.catchStack.push(tryStmt);
         this.createBlock(catchBlock.body());
         this.catchStack.pop();
         Stmt catchGotoFinallyNoop = Jimple.v().newNopStmt();
         this.body.getUnits().add((Unit)catchGotoFinallyNoop);
         Stmt catchFinallyNoop = Jimple.v().newNopStmt();
         Stmt catchGotoFinally = Jimple.v().newGotoStmt((Unit)catchFinallyNoop);
         this.body.getUnits().add((Unit)catchGotoFinally);
         Stmt beforeCatchEndGotoNoop = Jimple.v().newNopStmt();
         this.body.getUnits().add((Unit)beforeCatchEndGotoNoop);
         Stmt catchEndGoto = Jimple.v().newGotoStmt((Unit)endNoop);
         this.body.getUnits().add((Unit)catchEndGoto);
         gotoMap.put(catchFinallyNoop, beforeCatchEndGotoNoop);
         Type sootType = Util.getSootType(catchBlock.catchType());
         this.addToExceptionList(noop1, noop2, noop3, Scene.v().getSootClass(sootType.toString()));
         this.addToExceptionList(catchStmtsNoop, beforeCatchEndGotoNoop, catchAllBeforeNoop, Scene.v().getSootClass("java.lang.Throwable"));
      }

      Local formalLocal = this.lg.generateLocal(RefType.v("java.lang.Throwable"));
      this.body.getUnits().add((Unit)catchAllBeforeNoop);
      CaughtExceptionRef exceptRef = Jimple.v().newCaughtExceptionRef();
      Stmt stmt = Jimple.v().newIdentityStmt(formalLocal, exceptRef);
      this.body.getUnits().add((Unit)stmt);
      catchStmtsNoop = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)catchStmtsNoop);
      Local catchAllAssignLocal = this.lg.generateLocal(RefType.v("java.lang.Throwable"));
      Stmt catchAllAssign = Jimple.v().newAssignStmt(catchAllAssignLocal, formalLocal);
      this.body.getUnits().add((Unit)catchAllAssign);
      Stmt catchAllFinallyNoop = Jimple.v().newNopStmt();
      Stmt catchAllGotoFinally = Jimple.v().newGotoStmt((Unit)catchAllFinallyNoop);
      this.body.getUnits().add((Unit)catchAllGotoFinally);
      Stmt catchAllBeforeThrowNoop = Jimple.v().newNopStmt();
      this.body.getUnits().add((Unit)catchAllBeforeThrowNoop);
      Stmt throwStmt = Jimple.v().newThrowStmt(catchAllAssignLocal);
      throwStmt.addTag(new ThrowCreatedByCompilerTag());
      this.body.getUnits().add((Unit)throwStmt);
      gotoMap.put(catchAllFinallyNoop, catchAllBeforeThrowNoop);
      Stmt catchAllGotoEnd = Jimple.v().newGotoStmt((Unit)endNoop);
      this.body.getUnits().add((Unit)catchAllGotoEnd);
      this.addToExceptionList(catchStmtsNoop, catchAllBeforeThrowNoop, catchAllBeforeNoop, Scene.v().getSootClass("java.lang.Throwable"));
      Iterator finallyIt = gotoMap.keySet().iterator();

      while(finallyIt.hasNext()) {
         Stmt noopStmt = (Stmt)finallyIt.next();
         this.body.getUnits().add((Unit)noopStmt);
         this.createBlock(tryStmt.finallyBlock());
         Stmt backToStmt = (Stmt)gotoMap.get(noopStmt);
         Stmt backToGoto = Jimple.v().newGotoStmt((Unit)backToStmt);
         this.body.getUnits().add((Unit)backToGoto);
      }

      this.body.getUnits().add((Unit)endNoop);
      this.addToExceptionList(noop1, beforeEndGotoNoop, catchAllBeforeNoop, Scene.v().getSootClass("java.lang.Throwable"));
   }

   private void addToExceptionList(Stmt from, Stmt to, Stmt with, SootClass exceptionClass) {
      if (this.exceptionTable == null) {
         this.exceptionTable = new ArrayList();
      }

      Trap trap = Jimple.v().newTrap(exceptionClass, (Unit)from, (Unit)to, (Unit)with);
      this.exceptionTable.add(trap);
   }

   public Constant createConstant(Expr expr) {
      Object constantVal = expr.constantValue();
      return this.getConstant(constantVal, expr.type());
   }

   protected Value createAggressiveExpr(Expr expr, boolean reduceAggressively, boolean reverseCondIfNec) {
      if (expr.isConstant() && expr.constantValue() != null && expr.type() != null && (!(expr instanceof Binary) || !expr.type().toString().equals("java.lang.String"))) {
         return this.createConstant(expr);
      } else if (expr instanceof Assign) {
         return this.getAssignLocal((Assign)expr);
      } else if (expr instanceof Lit) {
         return this.createLiteral((Lit)expr);
      } else if (expr instanceof polyglot.ast.Local) {
         return this.getLocal((polyglot.ast.Local)expr);
      } else if (expr instanceof Binary) {
         return this.getBinaryLocal2((Binary)expr, reduceAggressively);
      } else if (expr instanceof Unary) {
         return this.getUnaryLocal((Unary)expr);
      } else if (expr instanceof Cast) {
         return this.getCastLocal((Cast)expr);
      } else if (expr instanceof ArrayAccess) {
         return this.getArrayRefLocal((ArrayAccess)expr);
      } else if (expr instanceof NewArray) {
         return this.getNewArrayLocal((NewArray)expr);
      } else if (expr instanceof Call) {
         return this.getCallLocal((Call)expr);
      } else if (expr instanceof New) {
         return this.getNewLocal((New)expr);
      } else if (expr instanceof Special) {
         return this.getSpecialLocal((Special)expr);
      } else if (expr instanceof Instanceof) {
         return this.getInstanceOfLocal((Instanceof)expr);
      } else if (expr instanceof Conditional) {
         return this.getConditionalLocal((Conditional)expr);
      } else if (expr instanceof Field) {
         return this.getFieldLocal((Field)expr);
      } else {
         throw new RuntimeException("Unhandled Expression: " + expr);
      }
   }

   protected Local handlePrivateFieldUnarySet(Unary unary) {
      Field fLeft = (Field)unary.expr();
      Value base = this.base().getBaseLocal(fLeft.target());
      Value fieldGetLocal = this.getPrivateAccessFieldLocal(fLeft, base);
      Local tmp = this.generateLocal(fLeft.type());
      AssignStmt stmt1 = Jimple.v().newAssignStmt(tmp, fieldGetLocal);
      this.body.getUnits().add((Unit)stmt1);
      Util.addLnPosTags(stmt1, unary.position());
      Value incVal = this.base().getConstant(Util.getSootType(fLeft.type()), 1);
      Object binExpr;
      if (unary.operator() != Unary.PRE_INC && unary.operator() != Unary.POST_INC) {
         binExpr = Jimple.v().newSubExpr(tmp, incVal);
      } else {
         binExpr = Jimple.v().newAddExpr(tmp, incVal);
      }

      Local tmp2 = this.generateLocal(fLeft.type());
      AssignStmt assign = Jimple.v().newAssignStmt(tmp2, (Value)binExpr);
      this.body.getUnits().add((Unit)assign);
      if (unary.operator() != Unary.PRE_INC && unary.operator() != Unary.PRE_DEC) {
         this.base().handlePrivateFieldSet(fLeft, tmp2, base);
         return tmp;
      } else {
         return this.base().handlePrivateFieldSet(fLeft, tmp2, base);
      }
   }

   protected Local handlePrivateFieldAssignSet(Assign assign) {
      Field fLeft = (Field)assign.left();
      Value fieldBase = this.base().getBaseLocal(fLeft.target());
      Object right;
      if (assign.operator() == Assign.ASSIGN) {
         right = this.base().getSimpleAssignRightLocal(assign);
      } else if (assign.operator() == Assign.ADD_ASSIGN && assign.type().toString().equals("java.lang.String")) {
         right = this.getStringConcatAssignRightLocal(assign);
      } else {
         Local leftLocal = this.getPrivateAccessFieldLocal(fLeft, fieldBase);
         right = this.base().getAssignRightLocal(assign, leftLocal);
      }

      return this.handlePrivateFieldSet(fLeft, (Value)right, fieldBase);
   }

   protected Local handlePrivateFieldSet(Expr expr, Value right, Value base) {
      Field fLeft = (Field)expr;
      SootClass containClass = ((RefType)Util.getSootType(fLeft.target().type())).getSootClass();
      SootMethod methToUse = this.addSetAccessMeth(containClass, fLeft, right);
      ArrayList params = new ArrayList();
      if (!fLeft.flags().isStatic()) {
         params.add(base);
      }

      params.add(right);
      InvokeExpr invoke = Jimple.v().newStaticInvokeExpr(methToUse.makeRef(), (List)params);
      Local retLocal = this.lg.generateLocal(right.getType());
      AssignStmt assignStmt = Jimple.v().newAssignStmt(retLocal, invoke);
      this.body.getUnits().add((Unit)assignStmt);
      return retLocal;
   }

   private SootMethod addSetAccessMeth(SootClass conClass, Field field, Value param) {
      if (InitialResolver.v().getPrivateFieldSetAccessMap() != null && InitialResolver.v().getPrivateFieldSetAccessMap().containsKey(new IdentityKey(field.fieldInstance()))) {
         return (SootMethod)InitialResolver.v().getPrivateFieldSetAccessMap().get(new IdentityKey(field.fieldInstance()));
      } else {
         String name = "access$" + InitialResolver.v().getNextPrivateAccessCounter() + "00";
         ArrayList paramTypes = new ArrayList();
         if (!field.flags().isStatic()) {
            paramTypes.add(conClass.getType());
         }

         paramTypes.add(Util.getSootType(field.type()));
         Type retType = Util.getSootType(field.type());
         SootMethod meth = Scene.v().makeSootMethod(name, paramTypes, retType, 8);
         PrivateFieldSetMethodSource pfsms = new PrivateFieldSetMethodSource(Util.getSootType(field.type()), field.name(), field.flags().isStatic());
         conClass.addMethod(meth);
         meth.setActiveBody(pfsms.getBody(meth, (String)null));
         InitialResolver.v().addToPrivateFieldSetAccessMap(field, meth);
         meth.addTag(new SyntheticTag());
         return meth;
      }
   }

   private SootMethod addGetFieldAccessMeth(SootClass conClass, Field field) {
      if (InitialResolver.v().getPrivateFieldGetAccessMap() != null && InitialResolver.v().getPrivateFieldGetAccessMap().containsKey(new IdentityKey(field.fieldInstance()))) {
         return (SootMethod)InitialResolver.v().getPrivateFieldGetAccessMap().get(new IdentityKey(field.fieldInstance()));
      } else {
         String name = "access$" + InitialResolver.v().getNextPrivateAccessCounter() + "00";
         ArrayList paramTypes = new ArrayList();
         if (!field.flags().isStatic()) {
            paramTypes.add(conClass.getType());
         }

         SootMethod meth = Scene.v().makeSootMethod(name, paramTypes, Util.getSootType(field.type()), 8);
         PrivateFieldAccMethodSource pfams = new PrivateFieldAccMethodSource(Util.getSootType(field.type()), field.name(), field.flags().isStatic(), conClass);
         conClass.addMethod(meth);
         meth.setActiveBody(pfams.getBody(meth, (String)null));
         InitialResolver.v().addToPrivateFieldGetAccessMap(field, meth);
         meth.addTag(new SyntheticTag());
         return meth;
      }
   }

   private SootMethod addGetMethodAccessMeth(SootClass conClass, Call call) {
      if (InitialResolver.v().getPrivateMethodGetAccessMap() != null && InitialResolver.v().getPrivateMethodGetAccessMap().containsKey(new IdentityKey(call.methodInstance()))) {
         return (SootMethod)InitialResolver.v().getPrivateMethodGetAccessMap().get(new IdentityKey(call.methodInstance()));
      } else {
         String name = "access$" + InitialResolver.v().getNextPrivateAccessCounter() + "00";
         ArrayList paramTypes = new ArrayList();
         if (!call.methodInstance().flags().isStatic()) {
            paramTypes.add(conClass.getType());
         }

         ArrayList sootParamsTypes = this.getSootParamsTypes(call);
         paramTypes.addAll(sootParamsTypes);
         SootMethod meth = Scene.v().makeSootMethod(name, paramTypes, Util.getSootType(call.methodInstance().returnType()), 8);
         PrivateMethodAccMethodSource pmams = new PrivateMethodAccMethodSource(call.methodInstance());
         conClass.addMethod(meth);
         meth.setActiveBody(pmams.getBody(meth, (String)null));
         InitialResolver.v().addToPrivateMethodGetAccessMap(call, meth);
         meth.addTag(new SyntheticTag());
         return meth;
      }
   }

   protected Value getAssignRightLocal(Assign assign, Local leftLocal) {
      if (assign.operator() == Assign.ASSIGN) {
         return this.base().getSimpleAssignRightLocal(assign);
      } else {
         return assign.operator() == Assign.ADD_ASSIGN && assign.type().toString().equals("java.lang.String") ? this.getStringConcatAssignRightLocal(assign) : this.getComplexAssignRightLocal(assign, leftLocal);
      }
   }

   protected Value getSimpleAssignRightLocal(Assign assign) {
      boolean repush = false;
      Stmt tNoop = null;
      Stmt fNoop = null;
      if (!this.trueNoop.empty() && !this.falseNoop.empty()) {
         tNoop = (Stmt)this.trueNoop.pop();
         fNoop = (Stmt)this.falseNoop.pop();
         repush = true;
      }

      Value right = this.base().createAggressiveExpr(assign.right(), false, false);
      if (repush) {
         this.trueNoop.push(tNoop);
         this.falseNoop.push(fNoop);
      }

      if (right instanceof ConditionExpr) {
         right = this.handleCondBinExpr((ConditionExpr)right);
      }

      return (Value)right;
   }

   private Local getStringConcatAssignRightLocal(Assign assign) {
      Local sb = this.createStringBuffer(assign);
      sb = this.generateAppends(assign.left(), sb);
      sb = this.generateAppends(assign.right(), sb);
      Local rLocal = this.createToString(sb, assign);
      return rLocal;
   }

   private Local getComplexAssignRightLocal(Assign assign, Local leftLocal) {
      Value right = this.base().createAggressiveExpr(assign.right(), false, false);
      if (right instanceof ConditionExpr) {
         right = this.handleCondBinExpr((ConditionExpr)right);
      }

      BinopExpr binop = null;
      if (assign.operator() == Assign.ADD_ASSIGN) {
         binop = Jimple.v().newAddExpr(leftLocal, (Value)right);
      } else if (assign.operator() == Assign.SUB_ASSIGN) {
         binop = Jimple.v().newSubExpr(leftLocal, (Value)right);
      } else if (assign.operator() == Assign.MUL_ASSIGN) {
         binop = Jimple.v().newMulExpr(leftLocal, (Value)right);
      } else if (assign.operator() == Assign.DIV_ASSIGN) {
         binop = Jimple.v().newDivExpr(leftLocal, (Value)right);
      } else if (assign.operator() == Assign.MOD_ASSIGN) {
         binop = Jimple.v().newRemExpr(leftLocal, (Value)right);
      } else if (assign.operator() == Assign.SHL_ASSIGN) {
         binop = Jimple.v().newShlExpr(leftLocal, (Value)right);
      } else if (assign.operator() == Assign.SHR_ASSIGN) {
         binop = Jimple.v().newShrExpr(leftLocal, (Value)right);
      } else if (assign.operator() == Assign.USHR_ASSIGN) {
         binop = Jimple.v().newUshrExpr(leftLocal, (Value)right);
      } else if (assign.operator() == Assign.BIT_AND_ASSIGN) {
         binop = Jimple.v().newAndExpr(leftLocal, (Value)right);
      } else if (assign.operator() == Assign.BIT_OR_ASSIGN) {
         binop = Jimple.v().newOrExpr(leftLocal, (Value)right);
      } else if (assign.operator() == Assign.BIT_XOR_ASSIGN) {
         binop = Jimple.v().newXorExpr(leftLocal, (Value)right);
      }

      Local retLocal = this.lg.generateLocal(leftLocal.getType());
      AssignStmt assignStmt = Jimple.v().newAssignStmt(retLocal, (Value)binop);
      this.body.getUnits().add((Unit)assignStmt);
      Util.addLnPosTags(((BinopExpr)binop).getOp1Box(), assign.left().position());
      Util.addLnPosTags(((BinopExpr)binop).getOp2Box(), assign.right().position());
      return retLocal;
   }

   private Value getSimpleAssignLocal(Assign assign) {
      Value left = this.base().createLHS(assign.left());
      Value right = this.base().getSimpleAssignRightLocal(assign);
      AssignStmt stmt = Jimple.v().newAssignStmt(left, right);
      this.body.getUnits().add((Unit)stmt);
      Util.addLnPosTags(stmt, assign.position());
      Util.addLnPosTags(stmt.getRightOpBox(), assign.right().position());
      Util.addLnPosTags(stmt.getLeftOpBox(), assign.left().position());
      return left instanceof Local ? left : right;
   }

   private Value getStrConAssignLocal(Assign assign) {
      Value left = this.base().createLHS(assign.left());
      Value right = this.getStringConcatAssignRightLocal(assign);
      AssignStmt stmt = Jimple.v().newAssignStmt(left, right);
      this.body.getUnits().add((Unit)stmt);
      Util.addLnPosTags(stmt, assign.position());
      Util.addLnPosTags(stmt.getRightOpBox(), assign.right().position());
      Util.addLnPosTags(stmt.getLeftOpBox(), assign.left().position());
      return (Value)(left instanceof Local ? left : right);
   }

   protected Value getAssignLocal(Assign assign) {
      if (this.base().needsAccessor(assign.left())) {
         return this.base().handlePrivateFieldAssignSet(assign);
      } else if (assign.operator() == Assign.ASSIGN) {
         return this.getSimpleAssignLocal(assign);
      } else if (assign.operator() == Assign.ADD_ASSIGN && assign.type().toString().equals("java.lang.String")) {
         return this.getStrConAssignLocal(assign);
      } else {
         Value left = this.base().createLHS(assign.left());
         Value left2 = (Value)left.clone();
         Local leftLocal;
         if (left instanceof Local) {
            leftLocal = (Local)left;
         } else {
            leftLocal = this.lg.generateLocal(left.getType());
            AssignStmt stmt1 = Jimple.v().newAssignStmt(leftLocal, left);
            this.body.getUnits().add((Unit)stmt1);
            Util.addLnPosTags(stmt1, assign.position());
         }

         Value right = this.base().getAssignRightLocal(assign, leftLocal);
         AssignStmt stmt2 = Jimple.v().newAssignStmt(leftLocal, right);
         this.body.getUnits().add((Unit)stmt2);
         Util.addLnPosTags(stmt2, assign.position());
         Util.addLnPosTags(stmt2.getRightOpBox(), assign.right().position());
         Util.addLnPosTags(stmt2.getLeftOpBox(), assign.left().position());
         if (!(left instanceof Local)) {
            AssignStmt stmt3 = Jimple.v().newAssignStmt(left2, leftLocal);
            this.body.getUnits().add((Unit)stmt3);
            Util.addLnPosTags(stmt3, assign.position());
            Util.addLnPosTags(stmt3.getRightOpBox(), assign.right().position());
            Util.addLnPosTags(stmt3.getLeftOpBox(), assign.left().position());
         }

         return leftLocal;
      }
   }

   private Value getFieldLocalLeft(Field field) {
      Receiver receiver = field.target();
      return (Value)(field.name().equals("length") && receiver.type() instanceof ArrayType ? this.getSpecialArrayLengthLocal(field) : this.getFieldRef(field));
   }

   private Value getFieldLocal(Field field) {
      Receiver receiver = field.target();
      PolyglotMethodSource ms = (PolyglotMethodSource)this.body.getMethod().getSource();
      if (field.name().equals("length") && receiver.type() instanceof ArrayType) {
         return this.getSpecialArrayLengthLocal(field);
      } else if (field.name().equals("class")) {
         throw new RuntimeException("Should go through ClassLit");
      } else if (this.base().needsAccessor(field)) {
         Value base = this.base().getBaseLocal(field.target());
         return this.getPrivateAccessFieldLocal(field, base);
      } else if (field.target() instanceof Special && ((Special)field.target()).kind() == Special.SUPER && ((Special)field.target()).qualifier() != null) {
         return this.getSpecialSuperQualifierLocal(field);
      } else if (this.shouldReturnConstant(field)) {
         return this.getReturnConstant(field);
      } else {
         FieldRef fieldRef = this.getFieldRef(field);
         Local baseLocal = this.generateLocal(field.type());
         AssignStmt fieldAssignStmt = Jimple.v().newAssignStmt(baseLocal, fieldRef);
         this.body.getUnits().add((Unit)fieldAssignStmt);
         Util.addLnPosTags(fieldAssignStmt, field.position());
         Util.addLnPosTags(fieldAssignStmt.getRightOpBox(), field.position());
         return baseLocal;
      }
   }

   protected boolean needsAccessor(Expr expr) {
      if (!(expr instanceof Field) && !(expr instanceof Call)) {
         return false;
      } else {
         return expr instanceof Field ? this.needsAccessor((MemberInstance)((Field)expr).fieldInstance()) : this.needsAccessor((MemberInstance)((Call)expr).methodInstance());
      }
   }

   protected boolean needsAccessor(MemberInstance inst) {
      if (inst.flags().isPrivate()) {
         if (!Util.getSootType(inst.container()).equals(this.body.getMethod().getDeclaringClass().getType())) {
            return true;
         }
      } else if (inst.flags().isProtected()) {
         if (Util.getSootType(inst.container()).equals(this.body.getMethod().getDeclaringClass().getType())) {
            return false;
         }

         SootClass currentClass = this.body.getMethod().getDeclaringClass();
         if (currentClass.getSuperclass().getType().equals(Util.getSootType(inst.container()))) {
            return false;
         }

         do {
            if (!currentClass.hasOuterClass()) {
               return false;
            }

            currentClass = currentClass.getOuterClass();
            if (Util.getSootType(inst.container()).equals(currentClass.getType())) {
               return false;
            }
         } while(!Util.getSootType(inst.container()).equals(currentClass.getSuperclass().getType()));

         return true;
      }

      return false;
   }

   private Constant getReturnConstant(Field field) {
      return this.getConstant(field.constantValue(), field.type());
   }

   private Constant getConstant(Object constVal, polyglot.types.Type type) {
      if (constVal instanceof String) {
         return StringConstant.v((String)constVal);
      } else if (constVal instanceof Boolean) {
         boolean val = (Boolean)constVal;
         return IntConstant.v(val ? 1 : 0);
      } else if (type.isChar()) {
         char val;
         if (constVal instanceof Integer) {
            val = (char)(Integer)constVal;
         } else {
            val = (Character)constVal;
         }

         return IntConstant.v(val);
      } else {
         Number num = (Number)constVal;
         num = this.createConstantCast(type, num);
         if (num instanceof Long) {
            return LongConstant.v((Long)num);
         } else if (num instanceof Double) {
            return DoubleConstant.v((Double)num);
         } else if (num instanceof Float) {
            return FloatConstant.v((Float)num);
         } else if (num instanceof Byte) {
            return IntConstant.v((Byte)num);
         } else {
            return num instanceof Short ? IntConstant.v((Short)num) : IntConstant.v((Integer)num);
         }
      }
   }

   private Number createConstantCast(polyglot.types.Type fieldType, Number constant) {
      if (constant instanceof Integer) {
         if (fieldType.isDouble()) {
            return new Double((double)(Integer)constant);
         }

         if (fieldType.isFloat()) {
            return new Float((float)(Integer)constant);
         }

         if (fieldType.isLong()) {
            return new Long((long)(Integer)constant);
         }
      }

      return constant;
   }

   private boolean shouldReturnConstant(Field field) {
      return field.isConstant() && field.constantValue() != null;
   }

   protected FieldRef getFieldRef(Field field) {
      SootClass receiverClass = ((RefType)Util.getSootType(field.target().type())).getSootClass();
      SootFieldRef receiverField = Scene.v().makeFieldRef(receiverClass, field.name(), Util.getSootType(field.type()), field.flags().isStatic());
      Object fieldRef;
      if (field.fieldInstance().flags().isStatic()) {
         fieldRef = Jimple.v().newStaticFieldRef(receiverField);
      } else {
         Local base = (Local)this.base().getBaseLocal(field.target());
         fieldRef = Jimple.v().newInstanceFieldRef(base, receiverField);
      }

      if (field.target() instanceof polyglot.ast.Local && fieldRef instanceof InstanceFieldRef) {
         Util.addLnPosTags(((InstanceFieldRef)fieldRef).getBaseBox(), field.target().position());
      }

      return (FieldRef)fieldRef;
   }

   private Local getPrivateAccessFieldLocal(Field field, Value base) {
      SootMethod toInvoke;
      if (field.fieldInstance().flags().isPrivate()) {
         toInvoke = this.addGetFieldAccessMeth(((RefType)Util.getSootType(field.fieldInstance().container())).getSootClass(), field);
         SootClass var4 = ((RefType)Util.getSootType(field.fieldInstance().container())).getSootClass();
      } else {
         if (InitialResolver.v().hierarchy() == null) {
            InitialResolver.v().hierarchy(new FastHierarchy());
         }

         SootClass containingClass = ((RefType)Util.getSootType(field.fieldInstance().container())).getSootClass();
         SootClass addToClass;
         if (this.body.getMethod().getDeclaringClass().hasOuterClass()) {
            for(addToClass = this.body.getMethod().getDeclaringClass().getOuterClass(); !InitialResolver.v().hierarchy().canStoreType(containingClass.getType(), addToClass.getType()) && addToClass.hasOuterClass(); addToClass = addToClass.getOuterClass()) {
            }
         } else {
            addToClass = containingClass;
         }

         toInvoke = this.addGetFieldAccessMeth(addToClass, field);
      }

      ArrayList params = new ArrayList();
      if (!field.fieldInstance().flags().isStatic()) {
         params.add(base);
      }

      return Util.getPrivateAccessFieldInvoke(toInvoke.makeRef(), params, this.body, this.lg);
   }

   private Local getSpecialClassLitLocal(ClassLit lit) {
      if (lit.typeNode().type().isPrimitive()) {
         PrimitiveType primType = (PrimitiveType)lit.typeNode().type();
         Local retLocal = this.lg.generateLocal(RefType.v("java.lang.Class"));
         SootFieldRef primField = null;
         if (primType.isBoolean()) {
            primField = Scene.v().makeFieldRef(Scene.v().getSootClass("java.lang.Boolean"), "TYPE", RefType.v("java.lang.Class"), true);
         } else if (primType.isByte()) {
            primField = Scene.v().makeFieldRef(Scene.v().getSootClass("java.lang.Byte"), "TYPE", RefType.v("java.lang.Class"), true);
         } else if (primType.isChar()) {
            primField = Scene.v().makeFieldRef(Scene.v().getSootClass("java.lang.Character"), "TYPE", RefType.v("java.lang.Class"), true);
         } else if (primType.isDouble()) {
            primField = Scene.v().makeFieldRef(Scene.v().getSootClass("java.lang.Double"), "TYPE", RefType.v("java.lang.Class"), true);
         } else if (primType.isFloat()) {
            primField = Scene.v().makeFieldRef(Scene.v().getSootClass("java.lang.Float"), "TYPE", RefType.v("java.lang.Class"), true);
         } else if (primType.isInt()) {
            primField = Scene.v().makeFieldRef(Scene.v().getSootClass("java.lang.Integer"), "TYPE", RefType.v("java.lang.Class"), true);
         } else if (primType.isLong()) {
            primField = Scene.v().makeFieldRef(Scene.v().getSootClass("java.lang.Long"), "TYPE", RefType.v("java.lang.Class"), true);
         } else if (primType.isShort()) {
            primField = Scene.v().makeFieldRef(Scene.v().getSootClass("java.lang.Short"), "TYPE", RefType.v("java.lang.Class"), true);
         } else if (primType.isVoid()) {
            primField = Scene.v().makeFieldRef(Scene.v().getSootClass("java.lang.Void"), "TYPE", RefType.v("java.lang.Class"), true);
         }

         StaticFieldRef fieldRef = Jimple.v().newStaticFieldRef(primField);
         AssignStmt assignStmt = Jimple.v().newAssignStmt(retLocal, fieldRef);
         this.body.getUnits().add((Unit)assignStmt);
         return retLocal;
      } else {
         SootClass thisClass = this.body.getMethod().getDeclaringClass();
         String fieldName = Util.getFieldNameForClassLit(lit.typeNode().type());
         Type fieldType = RefType.v("java.lang.Class");
         Local fieldLocal = this.lg.generateLocal(RefType.v("java.lang.Class"));
         SootFieldRef sootField = null;
         if (thisClass.isInterface()) {
            HashMap<SootClass, SootClass> specialAnonMap = InitialResolver.v().specialAnonMap();
            if (specialAnonMap == null || !specialAnonMap.containsKey(thisClass)) {
               throw new RuntimeException("Class is interface so it must have an anon class to handle class lits but its anon class cannot be found.");
            }

            SootClass specialClass = (SootClass)specialAnonMap.get(thisClass);
            sootField = Scene.v().makeFieldRef(specialClass, fieldName, fieldType, true);
         } else {
            sootField = Scene.v().makeFieldRef(thisClass, fieldName, fieldType, true);
         }

         StaticFieldRef fieldRef = Jimple.v().newStaticFieldRef(sootField);
         Stmt fieldAssign = Jimple.v().newAssignStmt(fieldLocal, fieldRef);
         this.body.getUnits().add((Unit)fieldAssign);
         Stmt noop1 = Jimple.v().newNopStmt();
         soot.jimple.Expr neExpr = Jimple.v().newNeExpr(fieldLocal, NullConstant.v());
         Stmt ifStmt = Jimple.v().newIfStmt(neExpr, (Unit)noop1);
         this.body.getUnits().add((Unit)ifStmt);
         ArrayList paramTypes = new ArrayList();
         paramTypes.add(RefType.v("java.lang.String"));
         SootMethodRef invokeMeth = null;
         if (thisClass.isInterface()) {
            HashMap<SootClass, SootClass> specialAnonMap = InitialResolver.v().specialAnonMap();
            if (specialAnonMap == null || !specialAnonMap.containsKey(thisClass)) {
               throw new RuntimeException("Class is interface so it must have an anon class to handle class lits but its anon class cannot be found.");
            }

            SootClass specialClass = (SootClass)specialAnonMap.get(thisClass);
            invokeMeth = Scene.v().makeMethodRef(specialClass, "class$", paramTypes, RefType.v("java.lang.Class"), true);
         } else {
            invokeMeth = Scene.v().makeMethodRef(thisClass, "class$", paramTypes, RefType.v("java.lang.Class"), true);
         }

         ArrayList params = new ArrayList();
         params.add(StringConstant.v(Util.getParamNameForClassLit(lit.typeNode().type())));
         soot.jimple.Expr classInvoke = Jimple.v().newStaticInvokeExpr(invokeMeth, (List)params);
         Local methLocal = this.lg.generateLocal(RefType.v("java.lang.Class"));
         Stmt invokeAssign = Jimple.v().newAssignStmt(methLocal, classInvoke);
         this.body.getUnits().add((Unit)invokeAssign);
         Stmt assignField = Jimple.v().newAssignStmt(fieldRef, methLocal);
         this.body.getUnits().add((Unit)assignField);
         Stmt noop2 = Jimple.v().newNopStmt();
         Stmt goto1 = Jimple.v().newGotoStmt((Unit)noop2);
         this.body.getUnits().add((Unit)goto1);
         this.body.getUnits().add((Unit)noop1);
         fieldAssign = Jimple.v().newAssignStmt(methLocal, fieldRef);
         this.body.getUnits().add((Unit)fieldAssign);
         this.body.getUnits().add((Unit)noop2);
         return methLocal;
      }
   }

   private Local getSpecialArrayLengthLocal(Field field) {
      Receiver receiver = field.target();
      Local localField;
      if (receiver instanceof polyglot.ast.Local) {
         localField = this.getLocal((polyglot.ast.Local)receiver);
      } else if (receiver instanceof Expr) {
         localField = (Local)this.base().createAggressiveExpr((Expr)receiver, false, false);
      } else {
         localField = this.generateLocal(receiver.type());
      }

      LengthExpr lengthExpr = Jimple.v().newLengthExpr(localField);
      Local retLocal = this.lg.generateLocal(IntType.v());
      Stmt assign = Jimple.v().newAssignStmt(retLocal, lengthExpr);
      this.body.getUnits().add((Unit)assign);
      Util.addLnPosTags(assign, field.position());
      Util.addLnPosTags(lengthExpr.getOpBox(), field.target().position());
      return retLocal;
   }

   private Value getBinaryLocal2(Binary binary, boolean reduceAggressively) {
      if (binary.operator() == Binary.COND_AND) {
         return this.createCondAnd(binary);
      } else if (binary.operator() == Binary.COND_OR) {
         return this.createCondOr(binary);
      } else if (binary.type().toString().equals("java.lang.String")) {
         if (this.areAllStringLits(binary)) {
            String result = this.createStringConstant(binary);
            return StringConstant.v(result);
         } else {
            Local sb = this.createStringBuffer(binary);
            sb = this.generateAppends(binary.left(), sb);
            sb = this.generateAppends(binary.right(), sb);
            return this.createToString(sb, binary);
         }
      } else {
         Value lVal = this.base().createAggressiveExpr(binary.left(), true, false);
         Value rVal = this.base().createAggressiveExpr(binary.right(), true, false);
         Object rhs;
         if (this.isComparisonBinary(binary.operator())) {
            rhs = this.getBinaryComparisonExpr(lVal, rVal, binary.operator());
         } else {
            rhs = this.getBinaryExpr(lVal, rVal, binary.operator());
         }

         if (rhs instanceof BinopExpr) {
            Util.addLnPosTags(((BinopExpr)rhs).getOp1Box(), binary.left().position());
            Util.addLnPosTags(((BinopExpr)rhs).getOp2Box(), binary.right().position());
         }

         if (rhs instanceof ConditionExpr && !reduceAggressively) {
            return (Value)rhs;
         } else {
            if (rhs instanceof ConditionExpr) {
               rhs = this.handleCondBinExpr((ConditionExpr)rhs, true);
            }

            Local lhs = this.generateLocal(binary.type());
            AssignStmt assignStmt = Jimple.v().newAssignStmt(lhs, (Value)rhs);
            this.body.getUnits().add((Unit)assignStmt);
            Util.addLnPosTags(assignStmt.getRightOpBox(), binary.position());
            Util.addLnPosTags(assignStmt, binary.position());
            return lhs;
         }
      }
   }

   private boolean areAllStringLits(Node node) {
      if (node instanceof StringLit) {
         return true;
      } else if (node instanceof Field) {
         return this.shouldReturnConstant((Field)node);
      } else if (node instanceof Binary) {
         return this.areAllStringLitsBinary((Binary)node);
      } else if (node instanceof Unary) {
         Unary unary = (Unary)node;
         return unary.isConstant();
      } else if (node instanceof Cast) {
         Cast cast = (Cast)node;
         return cast.isConstant();
      } else if (node instanceof Lit) {
         Lit lit = (Lit)node;
         return lit.isConstant();
      } else {
         return false;
      }
   }

   private boolean areAllStringLitsBinary(Binary binary) {
      return this.areAllStringLits(binary.left()) && this.areAllStringLits(binary.right());
   }

   private String createStringConstant(Node node) {
      String s = null;
      if (node instanceof StringLit) {
         s = ((StringLit)node).value();
      } else if (node instanceof Cast) {
         Cast cast = (Cast)node;
         if (cast.type().isChar()) {
            s = "" + (Character)cast.constantValue();
         } else {
            s = "" + cast.constantValue();
         }
      } else if (node instanceof Unary) {
         Unary unary = (Unary)node;
         s = "" + unary.constantValue();
      } else if (node instanceof CharLit) {
         s = "" + ((CharLit)node).value();
      } else if (node instanceof BooleanLit) {
         s = "" + ((BooleanLit)node).value();
      } else if (node instanceof IntLit) {
         s = "" + ((IntLit)node).value();
      } else if (node instanceof FloatLit) {
         s = "" + ((FloatLit)node).value();
      } else if (node instanceof NullLit) {
         s = "null";
      } else if (node instanceof Field) {
         Field field = (Field)node;
         if (field.fieldInstance().constantValue() instanceof String) {
            s = (String)field.constantValue();
         } else if (field.fieldInstance().constantValue() instanceof Boolean) {
            boolean val = (Boolean)field.constantValue();
            int temp = val ? 1 : 0;
            s = "" + temp;
         } else if (field.type().isChar()) {
            char val = (char)(Integer)field.constantValue();
            s = "" + val;
         } else {
            Number num = (Number)field.fieldInstance().constantValue();
            num = this.createConstantCast(field.type(), num);
            if (num instanceof Long) {
               s = "" + (Long)num;
            } else if (num instanceof Double) {
               s = "" + (Double)num;
            } else if (num instanceof Float) {
               s = "" + (Float)num;
            } else if (num instanceof Byte) {
               s = "" + (Byte)num;
            } else if (num instanceof Short) {
               s = "" + (Short)num;
            } else {
               s = "" + (Integer)num;
            }
         }
      } else {
         if (!(node instanceof Binary)) {
            throw new RuntimeException("No other string constant folding done");
         }

         s = this.createStringConstantBinary((Binary)node);
      }

      return s;
   }

   private String createStringConstantBinary(Binary binary) {
      String s = null;
      if (Util.getSootType(binary.type()).toString().equals("java.lang.String")) {
         s = this.createStringConstant(binary.left()) + this.createStringConstant(binary.right());
      } else {
         s = binary.constantValue().toString();
      }

      return s;
   }

   private boolean isComparisonBinary(Binary.Operator op) {
      return op == Binary.EQ || op == Binary.NE || op == Binary.GE || op == Binary.GT || op == Binary.LE || op == Binary.LT;
   }

   private Value getBinaryExpr(Value lVal, Value rVal, Binary.Operator operator) {
      Value rValue = null;
      if (lVal instanceof ConditionExpr) {
         lVal = this.handleCondBinExpr((ConditionExpr)lVal);
      }

      if (rVal instanceof ConditionExpr) {
         rVal = this.handleCondBinExpr((ConditionExpr)rVal);
      }

      if (operator == Binary.ADD) {
         rValue = Jimple.v().newAddExpr((Value)lVal, (Value)rVal);
      } else if (operator == Binary.SUB) {
         rValue = Jimple.v().newSubExpr((Value)lVal, (Value)rVal);
      } else if (operator == Binary.MUL) {
         rValue = Jimple.v().newMulExpr((Value)lVal, (Value)rVal);
      } else if (operator == Binary.DIV) {
         rValue = Jimple.v().newDivExpr((Value)lVal, (Value)rVal);
      } else {
         Local intVal;
         CastExpr castExpr;
         AssignStmt assignStmt;
         if (operator == Binary.SHR) {
            if (((Value)rVal).getType().equals(LongType.v())) {
               intVal = this.lg.generateLocal(IntType.v());
               castExpr = Jimple.v().newCastExpr((Value)rVal, IntType.v());
               assignStmt = Jimple.v().newAssignStmt(intVal, castExpr);
               this.body.getUnits().add((Unit)assignStmt);
               rValue = Jimple.v().newShrExpr((Value)lVal, intVal);
            } else {
               rValue = Jimple.v().newShrExpr((Value)lVal, (Value)rVal);
            }
         } else if (operator == Binary.USHR) {
            if (((Value)rVal).getType().equals(LongType.v())) {
               intVal = this.lg.generateLocal(IntType.v());
               castExpr = Jimple.v().newCastExpr((Value)rVal, IntType.v());
               assignStmt = Jimple.v().newAssignStmt(intVal, castExpr);
               this.body.getUnits().add((Unit)assignStmt);
               rValue = Jimple.v().newUshrExpr((Value)lVal, intVal);
            } else {
               rValue = Jimple.v().newUshrExpr((Value)lVal, (Value)rVal);
            }
         } else if (operator == Binary.SHL) {
            if (((Value)rVal).getType().equals(LongType.v())) {
               intVal = this.lg.generateLocal(IntType.v());
               castExpr = Jimple.v().newCastExpr((Value)rVal, IntType.v());
               assignStmt = Jimple.v().newAssignStmt(intVal, castExpr);
               this.body.getUnits().add((Unit)assignStmt);
               rValue = Jimple.v().newShlExpr((Value)lVal, intVal);
            } else {
               rValue = Jimple.v().newShlExpr((Value)lVal, (Value)rVal);
            }
         } else if (operator == Binary.BIT_AND) {
            rValue = Jimple.v().newAndExpr((Value)lVal, (Value)rVal);
         } else if (operator == Binary.BIT_OR) {
            rValue = Jimple.v().newOrExpr((Value)lVal, (Value)rVal);
         } else if (operator == Binary.BIT_XOR) {
            rValue = Jimple.v().newXorExpr((Value)lVal, (Value)rVal);
         } else {
            if (operator != Binary.MOD) {
               throw new RuntimeException("Binary not yet handled!");
            }

            rValue = Jimple.v().newRemExpr((Value)lVal, (Value)rVal);
         }
      }

      return (Value)rValue;
   }

   private Value getBinaryComparisonExpr(Value lVal, Value rVal, Binary.Operator operator) {
      Object rValue;
      if (operator == Binary.EQ) {
         rValue = Jimple.v().newEqExpr(lVal, rVal);
      } else if (operator == Binary.GE) {
         rValue = Jimple.v().newGeExpr(lVal, rVal);
      } else if (operator == Binary.GT) {
         rValue = Jimple.v().newGtExpr(lVal, rVal);
      } else if (operator == Binary.LE) {
         rValue = Jimple.v().newLeExpr(lVal, rVal);
      } else if (operator == Binary.LT) {
         rValue = Jimple.v().newLtExpr(lVal, rVal);
      } else {
         if (operator != Binary.NE) {
            throw new RuntimeException("Unknown Comparison Expr");
         }

         rValue = Jimple.v().newNeExpr(lVal, rVal);
      }

      return (Value)rValue;
   }

   private Value reverseCondition(ConditionExpr cond) {
      Object newExpr;
      if (cond instanceof EqExpr) {
         newExpr = Jimple.v().newNeExpr(cond.getOp1(), cond.getOp2());
      } else if (cond instanceof NeExpr) {
         newExpr = Jimple.v().newEqExpr(cond.getOp1(), cond.getOp2());
      } else if (cond instanceof GtExpr) {
         newExpr = Jimple.v().newLeExpr(cond.getOp1(), cond.getOp2());
      } else if (cond instanceof GeExpr) {
         newExpr = Jimple.v().newLtExpr(cond.getOp1(), cond.getOp2());
      } else if (cond instanceof LtExpr) {
         newExpr = Jimple.v().newGeExpr(cond.getOp1(), cond.getOp2());
      } else {
         if (!(cond instanceof LeExpr)) {
            throw new RuntimeException("Unknown Condition Expr");
         }

         newExpr = Jimple.v().newGtExpr(cond.getOp1(), cond.getOp2());
      }

      ((ConditionExpr)newExpr).getOp1Box().addAllTagsOf(cond.getOp1Box());
      ((ConditionExpr)newExpr).getOp2Box().addAllTagsOf(cond.getOp2Box());
      return (Value)newExpr;
   }

   private Value handleDFLCond(ConditionExpr cond) {
      Local result = this.lg.generateLocal(ByteType.v());
      soot.jimple.Expr cmExpr = null;
      if (!this.isDouble(cond.getOp1()) && !this.isDouble(cond.getOp2()) && !this.isFloat(cond.getOp1()) && !this.isFloat(cond.getOp2())) {
         if (!this.isLong(cond.getOp1()) && !this.isLong(cond.getOp2())) {
            return cond;
         }

         cmExpr = Jimple.v().newCmpExpr(cond.getOp1(), cond.getOp2());
      } else if (!(cond instanceof GeExpr) && !(cond instanceof GtExpr)) {
         cmExpr = Jimple.v().newCmplExpr(cond.getOp1(), cond.getOp2());
      } else {
         cmExpr = Jimple.v().newCmpgExpr(cond.getOp1(), cond.getOp2());
      }

      Stmt assign = Jimple.v().newAssignStmt(result, (Value)cmExpr);
      this.body.getUnits().add((Unit)assign);
      Object cond;
      if (cond instanceof EqExpr) {
         cond = Jimple.v().newEqExpr(result, IntConstant.v(0));
      } else if (cond instanceof GeExpr) {
         cond = Jimple.v().newGeExpr(result, IntConstant.v(0));
      } else if (cond instanceof GtExpr) {
         cond = Jimple.v().newGtExpr(result, IntConstant.v(0));
      } else if (cond instanceof LeExpr) {
         cond = Jimple.v().newLeExpr(result, IntConstant.v(0));
      } else if (cond instanceof LtExpr) {
         cond = Jimple.v().newLtExpr(result, IntConstant.v(0));
      } else {
         if (!(cond instanceof NeExpr)) {
            throw new RuntimeException("Unknown Comparison Expr");
         }

         cond = Jimple.v().newNeExpr(result, IntConstant.v(0));
      }

      return (Value)cond;
   }

   private boolean isDouble(Value val) {
      return val.getType() instanceof DoubleType;
   }

   private boolean isFloat(Value val) {
      return val.getType() instanceof FloatType;
   }

   private boolean isLong(Value val) {
      return val.getType() instanceof LongType;
   }

   private Value createCondAnd(Binary binary) {
      Local retLocal = this.lg.generateLocal(BooleanType.v());
      Stmt noop1 = Jimple.v().newNopStmt();
      Value lVal = this.base().createAggressiveExpr(binary.left(), false, false);
      boolean leftNeedIf = this.needSootIf(lVal);
      Object lVal;
      if (!(lVal instanceof ConditionExpr)) {
         lVal = Jimple.v().newEqExpr(lVal, IntConstant.v(0));
      } else {
         lVal = this.reverseCondition((ConditionExpr)lVal);
         lVal = this.handleDFLCond((ConditionExpr)lVal);
      }

      if (leftNeedIf) {
         IfStmt ifLeft = Jimple.v().newIfStmt((Value)lVal, (Unit)noop1);
         this.body.getUnits().add((Unit)ifLeft);
         Util.addLnPosTags(ifLeft.getConditionBox(), binary.left().position());
         Util.addLnPosTags(ifLeft, binary.left().position());
      }

      Stmt endNoop = Jimple.v().newNopStmt();
      Value rVal = this.base().createAggressiveExpr(binary.right(), false, false);
      boolean rightNeedIf = this.needSootIf(rVal);
      Object rVal;
      if (!(rVal instanceof ConditionExpr)) {
         rVal = Jimple.v().newEqExpr(rVal, IntConstant.v(0));
      } else {
         rVal = this.reverseCondition((ConditionExpr)rVal);
         rVal = this.handleDFLCond((ConditionExpr)rVal);
      }

      if (rightNeedIf) {
         IfStmt ifRight = Jimple.v().newIfStmt((Value)rVal, (Unit)noop1);
         this.body.getUnits().add((Unit)ifRight);
         Util.addLnPosTags(ifRight.getConditionBox(), binary.right().position());
         Util.addLnPosTags(ifRight, binary.right().position());
      }

      Stmt assign1 = Jimple.v().newAssignStmt(retLocal, IntConstant.v(1));
      this.body.getUnits().add((Unit)assign1);
      Stmt gotoEnd1 = Jimple.v().newGotoStmt((Unit)endNoop);
      this.body.getUnits().add((Unit)gotoEnd1);
      this.body.getUnits().add((Unit)noop1);
      Stmt assign2 = Jimple.v().newAssignStmt(retLocal, IntConstant.v(0));
      this.body.getUnits().add((Unit)assign2);
      this.body.getUnits().add((Unit)endNoop);
      Util.addLnPosTags(assign1, binary.position());
      Util.addLnPosTags(assign2, binary.position());
      return retLocal;
   }

   private Value createCondOr(Binary binary) {
      Local retLocal = this.lg.generateLocal(BooleanType.v());
      Stmt endNoop = Jimple.v().newNopStmt();
      Stmt noop1 = Jimple.v().newNopStmt();
      Stmt noop2 = Jimple.v().newNopStmt();
      Value lVal = this.base().createAggressiveExpr(binary.left(), false, false);
      boolean leftNeedIf = this.needSootIf(lVal);
      Object lVal;
      if (!(lVal instanceof ConditionExpr)) {
         lVal = Jimple.v().newNeExpr(lVal, IntConstant.v(0));
      } else {
         lVal = this.handleDFLCond((ConditionExpr)lVal);
      }

      if (leftNeedIf) {
         IfStmt ifLeft = Jimple.v().newIfStmt((Value)lVal, (Unit)noop1);
         this.body.getUnits().add((Unit)ifLeft);
         Util.addLnPosTags(ifLeft, binary.left().position());
         Util.addLnPosTags(ifLeft.getConditionBox(), binary.left().position());
      }

      Value rVal = this.base().createAggressiveExpr(binary.right(), false, false);
      boolean rightNeedIf = this.needSootIf(rVal);
      Object rVal;
      if (!(rVal instanceof ConditionExpr)) {
         rVal = Jimple.v().newEqExpr(rVal, IntConstant.v(0));
      } else {
         if (this.inLeftOr == 0) {
            rVal = this.reverseCondition((ConditionExpr)rVal);
         }

         rVal = this.handleDFLCond((ConditionExpr)rVal);
      }

      if (rightNeedIf) {
         IfStmt ifRight = Jimple.v().newIfStmt((Value)rVal, (Unit)noop2);
         this.body.getUnits().add((Unit)ifRight);
         Util.addLnPosTags(ifRight, binary.right().position());
         Util.addLnPosTags(ifRight.getConditionBox(), binary.right().position());
      }

      this.body.getUnits().add((Unit)noop1);
      Stmt assign2 = Jimple.v().newAssignStmt(retLocal, IntConstant.v(1));
      this.body.getUnits().add((Unit)assign2);
      Util.addLnPosTags(assign2, binary.position());
      Stmt gotoEnd2 = Jimple.v().newGotoStmt((Unit)endNoop);
      this.body.getUnits().add((Unit)gotoEnd2);
      this.body.getUnits().add((Unit)noop2);
      Stmt assign3 = Jimple.v().newAssignStmt(retLocal, IntConstant.v(0));
      this.body.getUnits().add((Unit)assign3);
      Util.addLnPosTags(assign3, binary.position());
      this.body.getUnits().add((Unit)endNoop);
      Util.addLnPosTags(assign2, binary.position());
      Util.addLnPosTags(assign3, binary.position());
      return retLocal;
   }

   private Local handleCondBinExpr(ConditionExpr condExpr) {
      Local boolLocal = this.lg.generateLocal(BooleanType.v());
      Stmt noop1 = Jimple.v().newNopStmt();
      Value newVal = this.reverseCondition(condExpr);
      newVal = this.handleDFLCond((ConditionExpr)newVal);
      Stmt ifStmt = Jimple.v().newIfStmt(newVal, (Unit)noop1);
      this.body.getUnits().add((Unit)ifStmt);
      this.body.getUnits().add((Unit)Jimple.v().newAssignStmt(boolLocal, IntConstant.v(1)));
      Stmt noop2 = Jimple.v().newNopStmt();
      Stmt goto1 = Jimple.v().newGotoStmt((Unit)noop2);
      this.body.getUnits().add((Unit)goto1);
      this.body.getUnits().add((Unit)noop1);
      this.body.getUnits().add((Unit)Jimple.v().newAssignStmt(boolLocal, IntConstant.v(0)));
      this.body.getUnits().add((Unit)noop2);
      return boolLocal;
   }

   private Local handleCondBinExpr(ConditionExpr condExpr, boolean reverse) {
      Local boolLocal = this.lg.generateLocal(BooleanType.v());
      Stmt noop1 = Jimple.v().newNopStmt();
      Value newVal = condExpr;
      if (reverse) {
         newVal = this.reverseCondition(condExpr);
      }

      Value newVal = this.handleDFLCond((ConditionExpr)newVal);
      Stmt ifStmt = Jimple.v().newIfStmt(newVal, (Unit)noop1);
      this.body.getUnits().add((Unit)ifStmt);
      this.body.getUnits().add((Unit)Jimple.v().newAssignStmt(boolLocal, IntConstant.v(1)));
      Stmt noop2 = Jimple.v().newNopStmt();
      Stmt goto1 = Jimple.v().newGotoStmt((Unit)noop2);
      this.body.getUnits().add((Unit)goto1);
      this.body.getUnits().add((Unit)noop1);
      this.body.getUnits().add((Unit)Jimple.v().newAssignStmt(boolLocal, IntConstant.v(0)));
      this.body.getUnits().add((Unit)noop2);
      return boolLocal;
   }

   private Local createStringBuffer(Expr expr) {
      Local local = this.lg.generateLocal(RefType.v("java.lang.StringBuffer"));
      NewExpr newExpr = Jimple.v().newNewExpr(RefType.v("java.lang.StringBuffer"));
      Stmt assign = Jimple.v().newAssignStmt(local, newExpr);
      this.body.getUnits().add((Unit)assign);
      Util.addLnPosTags(assign, expr.position());
      SootClass classToInvoke1 = Scene.v().getSootClass("java.lang.StringBuffer");
      SootMethodRef methodToInvoke1 = Scene.v().makeMethodRef(classToInvoke1, "<init>", new ArrayList(), VoidType.v(), false);
      SpecialInvokeExpr invoke = Jimple.v().newSpecialInvokeExpr(local, methodToInvoke1);
      Stmt invokeStmt = Jimple.v().newInvokeStmt(invoke);
      this.body.getUnits().add((Unit)invokeStmt);
      Util.addLnPosTags(invokeStmt, expr.position());
      return local;
   }

   private Local createToString(Local sb, Expr expr) {
      Local newString = this.lg.generateLocal(RefType.v("java.lang.String"));
      SootClass classToInvoke2 = Scene.v().getSootClass("java.lang.StringBuffer");
      SootMethodRef methodToInvoke2 = Scene.v().makeMethodRef(classToInvoke2, "toString", new ArrayList(), RefType.v("java.lang.String"), false);
      VirtualInvokeExpr toStringInvoke = Jimple.v().newVirtualInvokeExpr(sb, methodToInvoke2);
      Stmt lastAssign = Jimple.v().newAssignStmt(newString, toStringInvoke);
      this.body.getUnits().add((Unit)lastAssign);
      Util.addLnPosTags(lastAssign, expr.position());
      return newString;
   }

   private boolean isStringConcat(Expr expr) {
      if (expr instanceof Binary) {
         Binary bin = (Binary)expr;
         if (bin.operator() == Binary.ADD) {
            return bin.type().toString().equals("java.lang.String");
         } else {
            return false;
         }
      } else if (expr instanceof Assign) {
         Assign assign = (Assign)expr;
         if (assign.operator() == Assign.ADD_ASSIGN) {
            return assign.type().toString().equals("java.lang.String");
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private Local generateAppends(Expr expr, Local sb) {
      if (this.isStringConcat(expr)) {
         if (expr instanceof Binary) {
            sb = this.generateAppends(((Binary)expr).left(), sb);
            sb = this.generateAppends(((Binary)expr).right(), sb);
         } else {
            sb = this.generateAppends(((Assign)expr).left(), sb);
            sb = this.generateAppends(((Assign)expr).right(), sb);
         }
      } else {
         Value toApp = this.base().createAggressiveExpr(expr, false, false);
         Type appendType = null;
         if (toApp instanceof StringConstant) {
            appendType = RefType.v("java.lang.String");
         } else if (toApp instanceof NullConstant) {
            appendType = RefType.v("java.lang.Object");
         } else if (toApp instanceof Constant) {
            appendType = ((Value)toApp).getType();
         } else if (toApp instanceof Local) {
            if (((Local)toApp).getType() instanceof PrimType) {
               appendType = ((Local)toApp).getType();
            } else if (((Local)toApp).getType() instanceof RefType) {
               if (((Local)toApp).getType().toString().equals("java.lang.String")) {
                  appendType = RefType.v("java.lang.String");
               } else if (((Local)toApp).getType().toString().equals("java.lang.StringBuffer")) {
                  appendType = RefType.v("java.lang.StringBuffer");
               } else {
                  appendType = RefType.v("java.lang.Object");
               }
            } else {
               appendType = RefType.v("java.lang.Object");
            }
         } else if (toApp instanceof ConditionExpr) {
            toApp = this.handleCondBinExpr((ConditionExpr)toApp);
            appendType = BooleanType.v();
         }

         if (appendType instanceof ShortType || appendType instanceof ByteType) {
            Local intLocal = this.lg.generateLocal(IntType.v());
            soot.jimple.Expr cast = Jimple.v().newCastExpr((Value)toApp, IntType.v());
            Stmt castAssign = Jimple.v().newAssignStmt(intLocal, cast);
            this.body.getUnits().add((Unit)castAssign);
            toApp = intLocal;
            appendType = IntType.v();
         }

         ArrayList paramsTypes = new ArrayList();
         paramsTypes.add(appendType);
         ArrayList params = new ArrayList();
         params.add(toApp);
         SootClass classToInvoke = Scene.v().getSootClass("java.lang.StringBuffer");
         SootMethodRef methodToInvoke = Scene.v().makeMethodRef(classToInvoke, "append", paramsTypes, RefType.v("java.lang.StringBuffer"), false);
         VirtualInvokeExpr appendInvoke = Jimple.v().newVirtualInvokeExpr(sb, methodToInvoke, (List)params);
         Util.addLnPosTags(appendInvoke.getArgBox(0), expr.position());
         Local tmp = this.lg.generateLocal(RefType.v("java.lang.StringBuffer"));
         Stmt appendStmt = Jimple.v().newAssignStmt(tmp, appendInvoke);
         sb = tmp;
         this.body.getUnits().add((Unit)appendStmt);
         Util.addLnPosTags(appendStmt, expr.position());
      }

      return sb;
   }

   private Value getUnaryLocal(Unary unary) {
      Expr expr = unary.expr();
      Unary.Operator op = unary.operator();
      Value local;
      AssignStmt assign1;
      if (op != Unary.POST_INC && op != Unary.PRE_INC && op != Unary.POST_DEC && op != Unary.PRE_DEC) {
         Local retLocal;
         if (op == Unary.BIT_NOT) {
            IntConstant int1 = IntConstant.v(-1);
            retLocal = this.generateLocal(expr.type());
            Value sootExpr = this.base().createAggressiveExpr(expr, false, false);
            XorExpr xor = Jimple.v().newXorExpr(sootExpr, this.base().getConstant(sootExpr.getType(), -1));
            Util.addLnPosTags(xor.getOp1Box(), expr.position());
            Stmt assign1 = Jimple.v().newAssignStmt(retLocal, xor);
            this.body.getUnits().add((Unit)assign1);
            Util.addLnPosTags(assign1, unary.position());
            return retLocal;
         } else {
            AssignStmt assign;
            if (op == Unary.NEG) {
               Object sootExpr;
               if (expr instanceof IntLit) {
                  long longVal = ((IntLit)expr).value();
                  if (((IntLit)expr).kind() == IntLit.LONG) {
                     sootExpr = LongConstant.v(-longVal);
                  } else {
                     sootExpr = IntConstant.v(-((int)longVal));
                  }
               } else if (expr instanceof FloatLit) {
                  double doubleVal = ((FloatLit)expr).value();
                  if (((FloatLit)expr).kind() == FloatLit.DOUBLE) {
                     sootExpr = DoubleConstant.v(-doubleVal);
                  } else {
                     sootExpr = FloatConstant.v(-((float)doubleVal));
                  }
               } else {
                  local = this.base().createAggressiveExpr(expr, false, false);
                  NegExpr negExpr = Jimple.v().newNegExpr(local);
                  sootExpr = negExpr;
                  Util.addLnPosTags(negExpr.getOpBox(), expr.position());
               }

               retLocal = this.generateLocal(expr.type());
               assign = Jimple.v().newAssignStmt(retLocal, (Value)sootExpr);
               this.body.getUnits().add((Unit)assign);
               Util.addLnPosTags(assign, expr.position());
               return retLocal;
            } else if (op == Unary.POS) {
               Local retLocal = this.generateLocal(expr.type());
               local = this.base().createAggressiveExpr(expr, false, false);
               assign = Jimple.v().newAssignStmt(retLocal, local);
               this.body.getUnits().add((Unit)assign);
               Util.addLnPosTags(assign, expr.position());
               return retLocal;
            } else if (op == Unary.NOT) {
               boolean repush = false;
               Stmt tNoop = null;
               Stmt fNoop = null;
               if (!this.trueNoop.empty() && !this.falseNoop.empty()) {
                  tNoop = (Stmt)this.trueNoop.pop();
                  fNoop = (Stmt)this.falseNoop.pop();
                  repush = true;
               }

               Value local = this.base().createAggressiveExpr(expr, false, false);
               if (repush) {
                  this.trueNoop.push(tNoop);
                  this.falseNoop.push(fNoop);
               }

               if (local instanceof ConditionExpr) {
                  local = this.handleCondBinExpr((ConditionExpr)local);
               }

               NeExpr neExpr = Jimple.v().newNeExpr((Value)local, this.base().getConstant(((Value)local).getType(), 0));
               Stmt noop1 = Jimple.v().newNopStmt();
               IfStmt ifStmt;
               if (!this.falseNoop.empty()) {
                  ifStmt = Jimple.v().newIfStmt(neExpr, (Unit)((Unit)this.falseNoop.peek()));
               } else {
                  ifStmt = Jimple.v().newIfStmt(neExpr, (Unit)noop1);
               }

               this.body.getUnits().add((Unit)ifStmt);
               Util.addLnPosTags(ifStmt, expr.position());
               Util.addLnPosTags(ifStmt.getConditionBox(), expr.position());
               if (!this.falseNoop.empty()) {
                  return IntConstant.v(1);
               } else {
                  Local retLocal = this.lg.generateLocal(((Value)local).getType());
                  assign1 = Jimple.v().newAssignStmt(retLocal, this.base().getConstant(retLocal.getType(), 1));
                  this.body.getUnits().add((Unit)assign1);
                  Util.addLnPosTags(assign1, expr.position());
                  Stmt noop2 = Jimple.v().newNopStmt();
                  Stmt goto1 = Jimple.v().newGotoStmt((Unit)noop2);
                  this.body.getUnits().add((Unit)goto1);
                  this.body.getUnits().add((Unit)noop1);
                  Stmt assign2 = Jimple.v().newAssignStmt(retLocal, this.base().getConstant(retLocal.getType(), 0));
                  this.body.getUnits().add((Unit)assign2);
                  Util.addLnPosTags(assign2, expr.position());
                  this.body.getUnits().add((Unit)noop2);
                  return retLocal;
               }
            } else {
               throw new RuntimeException("Unhandled Unary Expr");
            }
         }
      } else if (this.base().needsAccessor(unary.expr())) {
         return this.base().handlePrivateFieldUnarySet(unary);
      } else {
         Value left = this.base().createLHS(unary.expr());
         local = Jimple.cloneIfNecessary(left);
         Local tmp = this.lg.generateLocal(left.getType());
         AssignStmt stmt1 = Jimple.v().newAssignStmt(tmp, left);
         this.body.getUnits().add((Unit)stmt1);
         Util.addLnPosTags(stmt1, unary.position());
         Value incVal = this.base().getConstant(left.getType(), 1);
         Object binExpr;
         if (unary.operator() != Unary.PRE_INC && unary.operator() != Unary.POST_INC) {
            binExpr = Jimple.v().newSubExpr(tmp, incVal);
         } else {
            binExpr = Jimple.v().newAddExpr(tmp, incVal);
         }

         Local tmp2 = this.lg.generateLocal(left.getType());
         AssignStmt assign = Jimple.v().newAssignStmt(tmp2, (Value)binExpr);
         this.body.getUnits().add((Unit)assign);
         assign1 = Jimple.v().newAssignStmt(local, tmp2);
         this.body.getUnits().add((Unit)assign1);
         return unary.operator() != Unary.POST_DEC && unary.operator() != Unary.POST_INC ? tmp2 : tmp;
      }
   }

   protected Constant getConstant(Type type, int val) {
      if (type instanceof DoubleType) {
         return DoubleConstant.v((double)val);
      } else if (type instanceof FloatType) {
         return FloatConstant.v((float)val);
      } else {
         return (Constant)(type instanceof LongType ? LongConstant.v((long)val) : IntConstant.v(val));
      }
   }

   private Value getCastLocal(Cast castExpr) {
      if (!castExpr.expr().type().equals(castExpr.type()) && (!castExpr.type().isClass() || !Util.getSootType(castExpr.type()).toString().equals("java.lang.Object"))) {
         Value val = this.base().createAggressiveExpr(castExpr.expr(), false, false);
         Type type = Util.getSootType(castExpr.type());
         CastExpr cast = Jimple.v().newCastExpr(val, type);
         Util.addLnPosTags(cast.getOpBox(), castExpr.expr().position());
         Local retLocal = this.lg.generateLocal(cast.getCastType());
         Stmt castAssign = Jimple.v().newAssignStmt(retLocal, cast);
         this.body.getUnits().add((Unit)castAssign);
         Util.addLnPosTags(castAssign, castExpr.position());
         return retLocal;
      } else {
         return this.base().createAggressiveExpr(castExpr.expr(), false, false);
      }
   }

   private ArrayList getSootParams(ProcedureCall call) {
      ArrayList sootParams = new ArrayList();

      Object nextExpr;
      for(Iterator it = call.arguments().iterator(); it.hasNext(); sootParams.add(nextExpr)) {
         Expr next = (Expr)it.next();
         nextExpr = this.base().createAggressiveExpr(next, false, false);
         if (nextExpr instanceof ConditionExpr) {
            nextExpr = this.handleCondBinExpr((ConditionExpr)nextExpr);
         }
      }

      return sootParams;
   }

   private ArrayList getSootParamsTypes(ProcedureCall call) {
      ArrayList sootParamsTypes = new ArrayList();
      Iterator it = call.procedureInstance().formalTypes().iterator();

      while(it.hasNext()) {
         Object next = it.next();
         sootParamsTypes.add(Util.getSootType((polyglot.types.Type)next));
      }

      return sootParamsTypes;
   }

   private SootMethodRef getMethodFromClass(SootClass sootClass, String name, ArrayList paramTypes, Type returnType, boolean isStatic) {
      SootMethodRef ref = Scene.v().makeMethodRef(sootClass, name, paramTypes, returnType, isStatic);
      return ref;
   }

   private void handleFinalLocalParams(ArrayList sootParams, ArrayList sootParamTypes, ClassType keyType) {
      HashMap<IdentityKey, AnonLocalClassInfo> finalLocalInfo = InitialResolver.v().finalLocalInfo();
      if (finalLocalInfo != null && finalLocalInfo.containsKey(new IdentityKey(keyType))) {
         AnonLocalClassInfo alci = (AnonLocalClassInfo)finalLocalInfo.get(new IdentityKey(keyType));
         ArrayList<IdentityKey> finalLocals = alci.finalLocalsUsed();
         if (finalLocals != null) {
            Iterator it = finalLocals.iterator();

            while(it.hasNext()) {
               Object next = it.next();
               LocalInstance li = (LocalInstance)((IdentityKey)next).object();
               sootParamTypes.add(Util.getSootType(li.type()));
               sootParams.add(this.getLocal(li));
            }
         }
      }

   }

   protected Local getThis(Type sootType) {
      return Util.getThis(sootType, this.body, this.getThisMap, this.lg);
   }

   protected boolean needsOuterClassRef(ClassType typeToInvoke) {
      AnonLocalClassInfo info = (AnonLocalClassInfo)InitialResolver.v().finalLocalInfo().get(new IdentityKey(typeToInvoke));
      if (InitialResolver.v().isAnonInCCall(typeToInvoke)) {
         return false;
      } else if (info != null && !info.inStaticMethod()) {
         return true;
      } else {
         return typeToInvoke.isNested() && !typeToInvoke.flags().isStatic() && !typeToInvoke.isAnonymous() && !typeToInvoke.isLocal();
      }
   }

   private void handleOuterClassParams(ArrayList sootParams, Value qVal, ArrayList sootParamsTypes, ClassType typeToInvoke) {
      ArrayList needsRef = InitialResolver.v().getHasOuterRefInInit();
      boolean addRef = this.needsOuterClassRef(typeToInvoke);
      SootClass outerClass;
      if (addRef) {
         outerClass = ((RefType)Util.getSootType(typeToInvoke.outer())).getSootClass();
         sootParamsTypes.add(outerClass.getType());
      }

      if (addRef && !typeToInvoke.isAnonymous() && qVal != null) {
         sootParams.add(qVal);
      } else if (addRef && !typeToInvoke.isAnonymous()) {
         outerClass = ((RefType)Util.getSootType(typeToInvoke.outer())).getSootClass();
         sootParams.add(this.getThis(outerClass.getType()));
      } else if (addRef && typeToInvoke.isAnonymous()) {
         outerClass = ((RefType)Util.getSootType(typeToInvoke.outer())).getSootClass();
         sootParams.add(this.getThis(outerClass.getType()));
      }

      if (typeToInvoke.isAnonymous() && qVal != null) {
         sootParamsTypes.add(qVal.getType());
         sootParams.add(qVal);
      }

   }

   private void createConstructorCall(ConstructorCall cCall) {
      ArrayList sootParams = new ArrayList();
      ArrayList sootParamsTypes = new ArrayList();
      ConstructorInstance cInst = cCall.constructorInstance();
      String containerName = null;
      if (cInst.container() instanceof ClassType) {
         containerName = ((ClassType)cInst.container()).fullName();
      }

      SootClass classToInvoke;
      if (cCall.kind() == ConstructorCall.SUPER) {
         classToInvoke = ((RefType)Util.getSootType(cInst.container())).getSootClass();
      } else {
         if (cCall.kind() != ConstructorCall.THIS) {
            throw new RuntimeException("Unknown kind of Constructor Call");
         }

         classToInvoke = this.body.getMethod().getDeclaringClass();
      }

      Local base = this.specialThisLocal;
      ClassType objType = (ClassType)cInst.container();
      Local qVal = null;
      if (cCall.qualifier() != null) {
         qVal = (Local)this.base().createAggressiveExpr(cCall.qualifier(), false, false);
      }

      this.handleOuterClassParams(sootParams, qVal, sootParamsTypes, objType);
      sootParams.addAll(this.getSootParams(cCall));
      sootParamsTypes.addAll(this.getSootParamsTypes(cCall));
      this.handleFinalLocalParams(sootParams, sootParamsTypes, (ClassType)cCall.constructorInstance().container());
      SootMethodRef methodToInvoke = this.getMethodFromClass(classToInvoke, "<init>", sootParamsTypes, VoidType.v(), false);
      SpecialInvokeExpr specialInvokeExpr = Jimple.v().newSpecialInvokeExpr(base, methodToInvoke, (List)sootParams);
      Stmt invokeStmt = Jimple.v().newInvokeStmt(specialInvokeExpr);
      this.body.getUnits().add((Unit)invokeStmt);
      Util.addLnPosTags(invokeStmt, cCall.position());
      int numParams = 0;

      for(Iterator invokeParamsIt = cCall.arguments().iterator(); invokeParamsIt.hasNext(); ++numParams) {
         Util.addLnPosTags(specialInvokeExpr.getArgBox(numParams), ((Expr)invokeParamsIt.next()).position());
      }

      if (this.body.getMethod().getName().equals("<init>") && cCall.kind() == ConstructorCall.SUPER) {
         this.handleOuterClassThisInit(this.body.getMethod());
         this.handleFinalLocalInits();
         this.handleFieldInits(this.body.getMethod());
         this.handleInitializerBlocks(this.body.getMethod());
      }

   }

   private void handleFinalLocalInits() {
      ArrayList<SootField> finalsList = ((PolyglotMethodSource)this.body.getMethod().getSource()).getFinalsList();
      if (finalsList != null) {
         int paramCount = this.paramRefCount - finalsList.size();

         for(Iterator it = finalsList.iterator(); it.hasNext(); ++paramCount) {
            SootField sf = (SootField)it.next();
            FieldRef fieldRef = Jimple.v().newInstanceFieldRef(this.specialThisLocal, sf.makeRef());
            AssignStmt stmt = Jimple.v().newAssignStmt(fieldRef, this.body.getParameterLocal(paramCount));
            this.body.getUnits().add((Unit)stmt);
         }

      }
   }

   private void createLocalClassDecl(LocalClassDecl cDecl) {
      BiMap lcMap = InitialResolver.v().getLocalClassMap();
      String name = Util.getSootType(cDecl.decl().type()).toString();
      if (!InitialResolver.v().hasClassInnerTag(this.body.getMethod().getDeclaringClass(), name)) {
         Util.addInnerClassTag(this.body.getMethod().getDeclaringClass(), name, (String)null, cDecl.decl().name(), Util.getModifier(cDecl.decl().flags()));
      }

   }

   private Local getNewLocal(New newExpr) {
      ArrayList sootParams = new ArrayList();
      ArrayList sootParamsTypes = new ArrayList();
      ClassType objType = (ClassType)newExpr.objectType().type();
      String name;
      ClassType outerType;
      if (newExpr.anonType() != null) {
         objType = newExpr.anonType();
         name = Util.getSootType((polyglot.types.Type)objType).toString();
         outerType = ((ClassType)objType).outer();
         if (!InitialResolver.v().hasClassInnerTag(this.body.getMethod().getDeclaringClass(), name)) {
            Util.addInnerClassTag(this.body.getMethod().getDeclaringClass(), name, (String)null, (String)null, outerType.flags().isInterface() ? 9 : Util.getModifier(((ClassType)objType).flags()));
         }
      } else if (!((ClassType)objType).isTopLevel()) {
         name = Util.getSootType((polyglot.types.Type)objType).toString();
         outerType = ((ClassType)objType).outer();
         if (!InitialResolver.v().hasClassInnerTag(this.body.getMethod().getDeclaringClass(), name)) {
            Util.addInnerClassTag(this.body.getMethod().getDeclaringClass(), name, Util.getSootType(outerType).toString(), ((ClassType)objType).name(), outerType.flags().isInterface() ? 9 : Util.getModifier(((ClassType)objType).flags()));
         }
      }

      RefType sootType = (RefType)Util.getSootType((polyglot.types.Type)objType);
      Local retLocal = this.lg.generateLocal(sootType);
      NewExpr sootNew = Jimple.v().newNewExpr(sootType);
      AssignStmt stmt = Jimple.v().newAssignStmt(retLocal, sootNew);
      this.body.getUnits().add((Unit)stmt);
      Util.addLnPosTags(stmt, newExpr.position());
      Util.addLnPosTags(stmt.getRightOpBox(), newExpr.position());
      SootClass classToInvoke = sootType.getSootClass();
      Value qVal = null;
      if (newExpr.qualifier() != null) {
         qVal = this.base().createAggressiveExpr(newExpr.qualifier(), false, false);
      }

      this.handleOuterClassParams(sootParams, qVal, sootParamsTypes, (ClassType)objType);
      boolean repush = false;
      Stmt tNoop = null;
      Stmt fNoop = null;
      if (!this.trueNoop.empty() && !this.falseNoop.empty()) {
         tNoop = (Stmt)this.trueNoop.pop();
         fNoop = (Stmt)this.falseNoop.pop();
         repush = true;
      }

      sootParams.addAll(this.getSootParams(newExpr));
      if (repush) {
         this.trueNoop.push(tNoop);
         this.falseNoop.push(fNoop);
      }

      sootParamsTypes.addAll(this.getSootParamsTypes(newExpr));
      this.handleFinalLocalParams(sootParams, sootParamsTypes, (ClassType)objType);
      SootMethodRef methodToInvoke = this.getMethodFromClass(classToInvoke, "<init>", sootParamsTypes, VoidType.v(), false);
      SpecialInvokeExpr specialInvokeExpr = Jimple.v().newSpecialInvokeExpr(retLocal, methodToInvoke, (List)sootParams);
      Stmt invokeStmt = Jimple.v().newInvokeStmt(specialInvokeExpr);
      this.body.getUnits().add((Unit)invokeStmt);
      Util.addLnPosTags(invokeStmt, newExpr.position());
      int numParams = 0;

      for(Iterator invokeParamsIt = newExpr.arguments().iterator(); invokeParamsIt.hasNext(); ++numParams) {
         Util.addLnPosTags(specialInvokeExpr.getArgBox(numParams), ((Expr)invokeParamsIt.next()).position());
      }

      return retLocal;
   }

   protected SootMethodRef getSootMethodRef(Call call) {
      SootClass receiverTypeClass;
      if (Util.getSootType(call.methodInstance().container()).equals(RefType.v("java.lang.Object"))) {
         Type sootRecType = RefType.v("java.lang.Object");
         receiverTypeClass = Scene.v().getSootClass("java.lang.Object");
      } else {
         Type sootRecType;
         if (call.target().type() == null) {
            sootRecType = Util.getSootType(call.methodInstance().container());
         } else {
            sootRecType = Util.getSootType(call.target().type());
         }

         if (sootRecType instanceof RefType) {
            receiverTypeClass = ((RefType)sootRecType).getSootClass();
         } else {
            if (!(sootRecType instanceof soot.ArrayType)) {
               throw new RuntimeException("call target problem: " + call);
            }

            receiverTypeClass = Scene.v().getSootClass("java.lang.Object");
         }
      }

      MethodInstance methodInstance = call.methodInstance();
      Type sootRetType = Util.getSootType(methodInstance.returnType());
      ArrayList sootParamsTypes = this.getSootParamsTypes(call);
      SootMethodRef callMethod = Scene.v().makeMethodRef(receiverTypeClass, methodInstance.name(), sootParamsTypes, sootRetType, methodInstance.flags().isStatic());
      return callMethod;
   }

   private Local getCallLocal(Call call) {
      String name = call.name();
      Receiver receiver = call.target();
      Local baseLocal;
      if (receiver instanceof Special && ((Special)receiver).kind() == Special.SUPER && ((Special)receiver).qualifier() != null) {
         baseLocal = this.getSpecialSuperQualifierLocal(call);
         return baseLocal;
      } else {
         baseLocal = (Local)this.base().getBaseLocal(receiver);
         boolean repush = false;
         Stmt tNoop = null;
         Stmt fNoop = null;
         if (!this.trueNoop.empty() && !this.falseNoop.empty()) {
            tNoop = (Stmt)this.trueNoop.pop();
            fNoop = (Stmt)this.falseNoop.pop();
            repush = true;
         }

         ArrayList sootParams = this.getSootParams(call);
         if (repush) {
            this.trueNoop.push(tNoop);
            this.falseNoop.push(fNoop);
         }

         SootMethodRef callMethod = this.base().getSootMethodRef(call);
         SootClass receiverTypeClass;
         if (Util.getSootType(call.methodInstance().container()).equals(RefType.v("java.lang.Object"))) {
            Type sootRecType = RefType.v("java.lang.Object");
            receiverTypeClass = Scene.v().getSootClass("java.lang.Object");
         } else {
            Type sootRecType;
            if (call.target().type() == null) {
               sootRecType = Util.getSootType(call.methodInstance().container());
            } else {
               sootRecType = Util.getSootType(call.target().type());
            }

            if (sootRecType instanceof RefType) {
               receiverTypeClass = ((RefType)sootRecType).getSootClass();
            } else {
               if (!(sootRecType instanceof soot.ArrayType)) {
                  throw new RuntimeException("call target problem: " + call);
               }

               receiverTypeClass = Scene.v().getSootClass("java.lang.Object");
            }
         }

         MethodInstance methodInstance = call.methodInstance();
         boolean isPrivateAccess = false;
         if (this.needsAccessor((Expr)call)) {
            SootClass containingClass = ((RefType)Util.getSootType(call.methodInstance().container())).getSootClass();
            SootClass classToAddMethTo = containingClass;
            if (call.methodInstance().flags().isProtected()) {
               if (InitialResolver.v().hierarchy() == null) {
                  InitialResolver.v().hierarchy(new FastHierarchy());
               }

               SootClass addToClass;
               if (this.body.getMethod().getDeclaringClass().hasOuterClass()) {
                  for(addToClass = this.body.getMethod().getDeclaringClass().getOuterClass(); !InitialResolver.v().hierarchy().canStoreType(containingClass.getType(), addToClass.getType()) && addToClass.hasOuterClass(); addToClass = addToClass.getOuterClass()) {
                  }
               } else {
                  addToClass = containingClass;
               }

               classToAddMethTo = addToClass;
            }

            callMethod = this.addGetMethodAccessMeth(classToAddMethTo, call).makeRef();
            if (!call.methodInstance().flags().isStatic()) {
               if (call.target() instanceof Expr) {
                  sootParams.add(0, baseLocal);
               } else if (this.body.getMethod().getDeclaringClass().declaresFieldByName("this$0")) {
                  sootParams.add(0, this.getThis(Util.getSootType(call.methodInstance().container())));
               } else {
                  sootParams.add(0, baseLocal);
               }
            }

            isPrivateAccess = true;
         }

         Object invokeExpr;
         if (isPrivateAccess) {
            invokeExpr = Jimple.v().newStaticInvokeExpr(callMethod, (List)sootParams);
         } else if (Modifier.isInterface(receiverTypeClass.getModifiers()) && methodInstance.flags().isAbstract()) {
            invokeExpr = Jimple.v().newInterfaceInvokeExpr(baseLocal, callMethod, (List)sootParams);
         } else if (methodInstance.flags().isStatic()) {
            invokeExpr = Jimple.v().newStaticInvokeExpr(callMethod, (List)sootParams);
         } else if (methodInstance.flags().isPrivate()) {
            invokeExpr = Jimple.v().newSpecialInvokeExpr(baseLocal, callMethod, (List)sootParams);
         } else if (receiver instanceof Special && ((Special)receiver).kind() == Special.SUPER) {
            invokeExpr = Jimple.v().newSpecialInvokeExpr(baseLocal, callMethod, (List)sootParams);
         } else {
            invokeExpr = Jimple.v().newVirtualInvokeExpr(baseLocal, callMethod, (List)sootParams);
         }

         int numParams = 0;

         for(Iterator callParamsIt = call.arguments().iterator(); callParamsIt.hasNext(); ++numParams) {
            Util.addLnPosTags(((InvokeExpr)invokeExpr).getArgBox(numParams), ((Expr)callParamsIt.next()).position());
         }

         if (invokeExpr instanceof InstanceInvokeExpr) {
            Util.addLnPosTags(((InstanceInvokeExpr)invokeExpr).getBaseBox(), call.target().position());
         }

         if (((InvokeExpr)invokeExpr).getMethodRef().returnType().equals(VoidType.v())) {
            Stmt invoke = Jimple.v().newInvokeStmt((Value)invokeExpr);
            this.body.getUnits().add((Unit)invoke);
            Util.addLnPosTags(invoke, call.position());
            return null;
         } else {
            Local retLocal = this.lg.generateLocal(((InvokeExpr)invokeExpr).getMethodRef().returnType());
            Stmt assignStmt = Jimple.v().newAssignStmt(retLocal, (Value)invokeExpr);
            this.body.getUnits().add((Unit)assignStmt);
            Util.addLnPosTags(assignStmt, call.position());
            return retLocal;
         }
      }
   }

   protected Value getBaseLocal(Receiver receiver) {
      if (receiver instanceof TypeNode) {
         return this.generateLocal(((TypeNode)receiver).type());
      } else {
         Value val = this.base().createAggressiveExpr((Expr)receiver, false, false);
         if (val instanceof Constant) {
            Local retLocal = this.lg.generateLocal(val.getType());
            AssignStmt stmt = Jimple.v().newAssignStmt(retLocal, val);
            this.body.getUnits().add((Unit)stmt);
            return retLocal;
         } else {
            return val;
         }
      }
   }

   private Local getNewArrayLocal(NewArray newArrExpr) {
      Type sootType = Util.getSootType(newArrExpr.type());
      Object expr;
      if (newArrExpr.numDims() == 1) {
         Object dimLocal;
         if (newArrExpr.additionalDims() == 1) {
            dimLocal = IntConstant.v(1);
         } else {
            dimLocal = this.base().createAggressiveExpr((Expr)newArrExpr.dims().get(0), false, false);
         }

         NewArrayExpr newArrayExpr = Jimple.v().newNewArrayExpr(((soot.ArrayType)sootType).getElementType(), (Value)dimLocal);
         expr = newArrayExpr;
         if (newArrExpr.additionalDims() != 1) {
            Util.addLnPosTags(newArrayExpr.getSizeBox(), ((Expr)newArrExpr.dims().get(0)).position());
         }
      } else {
         ArrayList valuesList = new ArrayList();
         Iterator it = newArrExpr.dims().iterator();

         while(it.hasNext()) {
            valuesList.add(this.base().createAggressiveExpr((Expr)it.next(), false, false));
         }

         if (newArrExpr.additionalDims() != 0) {
            valuesList.add(IntConstant.v(newArrExpr.additionalDims()));
         }

         NewMultiArrayExpr newMultiArrayExpr = Jimple.v().newNewMultiArrayExpr((soot.ArrayType)sootType, valuesList);
         expr = newMultiArrayExpr;
         Iterator sizeBoxIt = newArrExpr.dims().iterator();

         for(int counter = 0; sizeBoxIt.hasNext(); ++counter) {
            Util.addLnPosTags(newMultiArrayExpr.getSizeBox(counter), ((Expr)sizeBoxIt.next()).position());
         }
      }

      Local retLocal = this.lg.generateLocal(sootType);
      AssignStmt stmt = Jimple.v().newAssignStmt(retLocal, (Value)expr);
      this.body.getUnits().add((Unit)stmt);
      Util.addLnPosTags(stmt, newArrExpr.position());
      Util.addLnPosTags(stmt.getRightOpBox(), newArrExpr.position());
      if (newArrExpr.init() != null) {
         Value initVal = this.getArrayInitLocal(newArrExpr.init(), newArrExpr.type());
         AssignStmt initStmt = Jimple.v().newAssignStmt(retLocal, initVal);
         this.body.getUnits().add((Unit)initStmt);
      }

      return retLocal;
   }

   private Local getArrayInitLocal(ArrayInit arrInit, polyglot.types.Type lhsType) {
      Local local = this.generateLocal(lhsType);
      NewArrayExpr arrExpr = Jimple.v().newNewArrayExpr(((soot.ArrayType)local.getType()).getElementType(), IntConstant.v(arrInit.elements().size()));
      Stmt assign = Jimple.v().newAssignStmt(local, arrExpr);
      this.body.getUnits().add((Unit)assign);
      Util.addLnPosTags(assign, arrInit.position());
      Iterator it = arrInit.elements().iterator();

      for(int index = 0; it.hasNext(); ++index) {
         Expr elemExpr = (Expr)it.next();
         Object elem;
         if (elemExpr instanceof ArrayInit) {
            if (((ArrayInit)elemExpr).type() instanceof NullType) {
               if (lhsType instanceof ArrayType) {
                  elem = this.getArrayInitLocal((ArrayInit)elemExpr, ((ArrayType)lhsType).base());
               } else {
                  elem = this.getArrayInitLocal((ArrayInit)elemExpr, lhsType);
               }
            } else {
               elem = this.getArrayInitLocal((ArrayInit)elemExpr, ((ArrayType)lhsType).base());
            }
         } else {
            elem = this.base().createAggressiveExpr(elemExpr, false, false);
         }

         ArrayRef arrRef = Jimple.v().newArrayRef(local, IntConstant.v(index));
         AssignStmt elemAssign = Jimple.v().newAssignStmt(arrRef, (Value)elem);
         this.body.getUnits().add((Unit)elemAssign);
         Util.addLnPosTags(elemAssign, elemExpr.position());
         Util.addLnPosTags(elemAssign.getRightOpBox(), elemExpr.position());
      }

      return local;
   }

   protected Value createLHS(Expr expr) {
      if (expr instanceof polyglot.ast.Local) {
         return this.getLocal((polyglot.ast.Local)expr);
      } else if (expr instanceof ArrayAccess) {
         return this.getArrayRefLocalLeft((ArrayAccess)expr);
      } else if (expr instanceof Field) {
         return this.getFieldLocalLeft((Field)expr);
      } else {
         throw new RuntimeException("Unhandled LHS");
      }
   }

   private Value getArrayRefLocalLeft(ArrayAccess arrayRefExpr) {
      Expr array = arrayRefExpr.array();
      Expr access = arrayRefExpr.index();
      Local arrLocal = (Local)this.base().createAggressiveExpr(array, false, false);
      Value arrAccess = this.base().createAggressiveExpr(access, false, false);
      this.generateLocal(arrayRefExpr.type());
      ArrayRef ref = Jimple.v().newArrayRef(arrLocal, arrAccess);
      Util.addLnPosTags(ref.getBaseBox(), arrayRefExpr.array().position());
      Util.addLnPosTags(ref.getIndexBox(), arrayRefExpr.index().position());
      return ref;
   }

   private Value getArrayRefLocal(ArrayAccess arrayRefExpr) {
      Expr array = arrayRefExpr.array();
      Expr access = arrayRefExpr.index();
      Local arrLocal = (Local)this.base().createAggressiveExpr(array, false, false);
      Value arrAccess = this.base().createAggressiveExpr(access, false, false);
      Local retLocal = this.generateLocal(arrayRefExpr.type());
      ArrayRef ref = Jimple.v().newArrayRef(arrLocal, arrAccess);
      Util.addLnPosTags(ref.getBaseBox(), arrayRefExpr.array().position());
      Util.addLnPosTags(ref.getIndexBox(), arrayRefExpr.index().position());
      Stmt stmt = Jimple.v().newAssignStmt(retLocal, ref);
      this.body.getUnits().add((Unit)stmt);
      Util.addLnPosTags(stmt, arrayRefExpr.position());
      return retLocal;
   }

   private Local getSpecialSuperQualifierLocal(Expr expr) {
      ArrayList methodParams = new ArrayList();
      SootClass classToInvoke;
      Special target;
      if (expr instanceof Call) {
         target = (Special)((Call)expr).target();
         classToInvoke = ((RefType)Util.getSootType(target.qualifier().type())).getSootClass();
         methodParams = this.getSootParams((Call)expr);
      } else {
         if (!(expr instanceof Field)) {
            throw new RuntimeException("Trying to create special super qualifier for: " + expr + " which is not a field or call");
         }

         target = (Special)((Field)expr).target();
         classToInvoke = ((RefType)Util.getSootType(target.qualifier().type())).getSootClass();
      }

      SootMethod methToInvoke = this.makeSuperAccessMethod(classToInvoke, expr);
      Local classToInvokeLocal = Util.getThis(classToInvoke.getType(), this.body, this.getThisMap, this.lg);
      methodParams.add(0, classToInvokeLocal);
      InvokeExpr invokeExpr = Jimple.v().newStaticInvokeExpr(methToInvoke.makeRef(), (List)methodParams);
      if (!methToInvoke.getReturnType().equals(VoidType.v())) {
         Local retLocal = this.lg.generateLocal(methToInvoke.getReturnType());
         AssignStmt stmt = Jimple.v().newAssignStmt(retLocal, invokeExpr);
         this.body.getUnits().add((Unit)stmt);
         return retLocal;
      } else {
         this.body.getUnits().add((Unit)Jimple.v().newInvokeStmt(invokeExpr));
         return null;
      }
   }

   private Local getSpecialLocal(Special specialExpr) {
      if (specialExpr.kind() == Special.SUPER) {
         return specialExpr.qualifier() == null ? this.specialThisLocal : this.getThis(Util.getSootType(specialExpr.qualifier().type()));
      } else if (specialExpr.kind() == Special.THIS) {
         return specialExpr.qualifier() == null ? this.specialThisLocal : this.getThis(Util.getSootType(specialExpr.qualifier().type()));
      } else {
         throw new RuntimeException("Unknown Special");
      }
   }

   private SootMethod makeSuperAccessMethod(SootClass classToInvoke, Object memberToAccess) {
      String name = "access$" + InitialResolver.v().getNextPrivateAccessCounter() + "00";
      ArrayList paramTypes = new ArrayList();
      paramTypes.add(classToInvoke.getType());
      SootMethod meth;
      Object src;
      if (memberToAccess instanceof Field) {
         Field fieldToAccess = (Field)memberToAccess;
         meth = Scene.v().makeSootMethod(name, paramTypes, Util.getSootType(fieldToAccess.type()), 8);
         PrivateFieldAccMethodSource fSrc = new PrivateFieldAccMethodSource(Util.getSootType(fieldToAccess.type()), fieldToAccess.name(), fieldToAccess.flags().isStatic(), ((RefType)Util.getSootType(fieldToAccess.target().type())).getSootClass());
         src = fSrc;
      } else {
         if (!(memberToAccess instanceof Call)) {
            throw new RuntimeException("trying to access unhandled member type: " + memberToAccess);
         }

         Call methToAccess = (Call)memberToAccess;
         paramTypes.addAll(this.getSootParamsTypes(methToAccess));
         meth = Scene.v().makeSootMethod(name, paramTypes, Util.getSootType(methToAccess.methodInstance().returnType()), 8);
         PrivateMethodAccMethodSource mSrc = new PrivateMethodAccMethodSource(methToAccess.methodInstance());
         src = mSrc;
      }

      classToInvoke.addMethod(meth);
      meth.setActiveBody(((MethodSource)src).getBody(meth, (String)null));
      meth.addTag(new SyntheticTag());
      return meth;
   }

   private Local getInstanceOfLocal(Instanceof instExpr) {
      Type sootType = Util.getSootType(instExpr.compareType().type());
      Value local = this.base().createAggressiveExpr(instExpr.expr(), false, false);
      InstanceOfExpr instOfExpr = Jimple.v().newInstanceOfExpr(local, sootType);
      Local lhs = this.lg.generateLocal(BooleanType.v());
      AssignStmt instAssign = Jimple.v().newAssignStmt(lhs, instOfExpr);
      this.body.getUnits().add((Unit)instAssign);
      Util.addLnPosTags(instAssign, instExpr.position());
      Util.addLnPosTags(instAssign.getRightOpBox(), instExpr.position());
      Util.addLnPosTags(instOfExpr.getOpBox(), instExpr.expr().position());
      return lhs;
   }

   private Local getConditionalLocal(Conditional condExpr) {
      Stmt noop1 = Jimple.v().newNopStmt();
      Expr condition = condExpr.cond();
      this.createBranchingExpr(condition, noop1, false);
      Local retLocal = this.generateLocal(condExpr.type());
      Expr consequence = condExpr.consequent();
      Value conseqVal = this.base().createAggressiveExpr(consequence, false, false);
      if (conseqVal instanceof ConditionExpr) {
         conseqVal = this.handleCondBinExpr((ConditionExpr)conseqVal);
      }

      AssignStmt conseqAssignStmt = Jimple.v().newAssignStmt(retLocal, (Value)conseqVal);
      this.body.getUnits().add((Unit)conseqAssignStmt);
      Util.addLnPosTags(conseqAssignStmt, condExpr.position());
      Util.addLnPosTags(conseqAssignStmt.getRightOpBox(), consequence.position());
      Stmt noop2 = Jimple.v().newNopStmt();
      Stmt goto1 = Jimple.v().newGotoStmt((Unit)noop2);
      this.body.getUnits().add((Unit)goto1);
      this.body.getUnits().add((Unit)noop1);
      Expr alternative = condExpr.alternative();
      if (alternative != null) {
         Value altVal = this.base().createAggressiveExpr(alternative, false, false);
         if (altVal instanceof ConditionExpr) {
            altVal = this.handleCondBinExpr((ConditionExpr)altVal);
         }

         AssignStmt altAssignStmt = Jimple.v().newAssignStmt(retLocal, (Value)altVal);
         this.body.getUnits().add((Unit)altAssignStmt);
         Util.addLnPosTags(altAssignStmt, condExpr.position());
         Util.addLnPosTags(altAssignStmt, alternative.position());
         Util.addLnPosTags(altAssignStmt.getRightOpBox(), alternative.position());
      }

      this.body.getUnits().add((Unit)noop2);
      return retLocal;
   }

   protected Local generateLocal(polyglot.types.Type polyglotType) {
      Type type = Util.getSootType(polyglotType);
      return this.lg.generateLocal(type);
   }

   protected Local generateLocal(Type sootType) {
      return this.lg.generateLocal(sootType);
   }
}
