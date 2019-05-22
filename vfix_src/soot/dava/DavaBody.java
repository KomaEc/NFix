package soot.dava;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.G;
import soot.IntType;
import soot.Local;
import soot.PatchingChain;
import soot.PhaseOptions;
import soot.RefType;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.SET.SETTopNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.dava.internal.javaRep.DCmpExpr;
import soot.dava.internal.javaRep.DCmpgExpr;
import soot.dava.internal.javaRep.DCmplExpr;
import soot.dava.internal.javaRep.DInstanceFieldRef;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.internal.javaRep.DInterfaceInvokeExpr;
import soot.dava.internal.javaRep.DLengthExpr;
import soot.dava.internal.javaRep.DNegExpr;
import soot.dava.internal.javaRep.DNewArrayExpr;
import soot.dava.internal.javaRep.DNewInvokeExpr;
import soot.dava.internal.javaRep.DNewMultiArrayExpr;
import soot.dava.internal.javaRep.DSpecialInvokeExpr;
import soot.dava.internal.javaRep.DStaticFieldRef;
import soot.dava.internal.javaRep.DStaticInvokeExpr;
import soot.dava.internal.javaRep.DThisRef;
import soot.dava.internal.javaRep.DVirtualInvokeExpr;
import soot.dava.toolkits.base.AST.UselessTryRemover;
import soot.dava.toolkits.base.AST.transformations.ASTCleaner;
import soot.dava.toolkits.base.AST.transformations.ASTCleanerTwo;
import soot.dava.toolkits.base.AST.transformations.AndAggregator;
import soot.dava.toolkits.base.AST.transformations.BooleanConditionSimplification;
import soot.dava.toolkits.base.AST.transformations.DeInliningFinalFields;
import soot.dava.toolkits.base.AST.transformations.DecrementIncrementStmtCreation;
import soot.dava.toolkits.base.AST.transformations.FinalFieldDefinition;
import soot.dava.toolkits.base.AST.transformations.ForLoopCreator;
import soot.dava.toolkits.base.AST.transformations.IfElseSplitter;
import soot.dava.toolkits.base.AST.transformations.LocalVariableCleaner;
import soot.dava.toolkits.base.AST.transformations.LoopStrengthener;
import soot.dava.toolkits.base.AST.transformations.NewStringBufferSimplification;
import soot.dava.toolkits.base.AST.transformations.OrAggregatorFour;
import soot.dava.toolkits.base.AST.transformations.OrAggregatorOne;
import soot.dava.toolkits.base.AST.transformations.OrAggregatorTwo;
import soot.dava.toolkits.base.AST.transformations.PushLabeledBlockIn;
import soot.dava.toolkits.base.AST.transformations.ShortcutArrayInit;
import soot.dava.toolkits.base.AST.transformations.ShortcutIfGenerator;
import soot.dava.toolkits.base.AST.transformations.SuperFirstStmtHandler;
import soot.dava.toolkits.base.AST.transformations.TypeCastingError;
import soot.dava.toolkits.base.AST.transformations.UselessAbruptStmtRemover;
import soot.dava.toolkits.base.AST.transformations.UselessLabeledBlockRemover;
import soot.dava.toolkits.base.AST.traversals.ClosestAbruptTargetFinder;
import soot.dava.toolkits.base.AST.traversals.CopyPropagation;
import soot.dava.toolkits.base.finders.AbruptEdgeFinder;
import soot.dava.toolkits.base.finders.CycleFinder;
import soot.dava.toolkits.base.finders.ExceptionFinder;
import soot.dava.toolkits.base.finders.ExceptionNode;
import soot.dava.toolkits.base.finders.IfFinder;
import soot.dava.toolkits.base.finders.LabeledBlockFinder;
import soot.dava.toolkits.base.finders.SequenceFinder;
import soot.dava.toolkits.base.finders.SwitchFinder;
import soot.dava.toolkits.base.finders.SynchronizedBlockFinder;
import soot.dava.toolkits.base.misc.MonitorConverter;
import soot.dava.toolkits.base.misc.ThrowNullConverter;
import soot.grimp.GrimpBody;
import soot.grimp.NewInvokeExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.Expr;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.MonitorStmt;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.ReturnStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.internal.JGotoStmt;
import soot.jimple.internal.JimpleLocal;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.TrapUnitGraph;
import soot.util.IterableSet;
import soot.util.Switchable;

public class DavaBody extends Body {
   public boolean DEBUG;
   private Map<Integer, Value> pMap;
   private HashSet<Object> consumedConditions;
   private HashSet<Object> thisLocals;
   private IterableSet<ExceptionNode> synchronizedBlockFacts;
   private IterableSet<ExceptionNode> exceptionFacts;
   private IterableSet<AugmentedStmt> monitorFacts;
   private IterableSet<String> importList;
   private Local controlLocal;
   private InstanceInvokeExpr constructorExpr;
   private Unit constructorUnit;
   private List<CaughtExceptionRef> caughtrefs;

   DavaBody(SootMethod m) {
      super(m);
      this.DEBUG = false;
      this.pMap = new HashMap();
      this.consumedConditions = new HashSet();
      this.thisLocals = new HashSet();
      this.synchronizedBlockFacts = new IterableSet();
      this.exceptionFacts = new IterableSet();
      this.monitorFacts = new IterableSet();
      this.importList = new IterableSet();
      this.caughtrefs = new LinkedList();
      this.controlLocal = null;
      this.constructorExpr = null;
   }

   public Unit get_ConstructorUnit() {
      return this.constructorUnit;
   }

   public List<CaughtExceptionRef> get_CaughtRefs() {
      return this.caughtrefs;
   }

   public InstanceInvokeExpr get_ConstructorExpr() {
      return this.constructorExpr;
   }

   public void set_ConstructorExpr(InstanceInvokeExpr expr) {
      this.constructorExpr = expr;
   }

   public void set_ConstructorUnit(Unit s) {
      this.constructorUnit = s;
   }

   public Map<Integer, Value> get_ParamMap() {
      return this.pMap;
   }

   public void set_ParamMap(Map<Integer, Value> map) {
      this.pMap = map;
   }

   public HashSet<Object> get_ThisLocals() {
      return this.thisLocals;
   }

   public Local get_ControlLocal() {
      if (this.controlLocal == null) {
         this.controlLocal = new JimpleLocal("controlLocal", IntType.v());
         this.getLocals().add(this.controlLocal);
      }

      return this.controlLocal;
   }

   public Set<Object> get_ConsumedConditions() {
      return this.consumedConditions;
   }

   public void consume_Condition(AugmentedStmt as) {
      this.consumedConditions.add(as);
   }

   public Object clone() {
      Body b = Dava.v().newBody(this.getMethod());
      b.importBodyContentsFrom(this);
      return b;
   }

   public IterableSet<ExceptionNode> get_SynchronizedBlockFacts() {
      return this.synchronizedBlockFacts;
   }

   public IterableSet<ExceptionNode> get_ExceptionFacts() {
      return this.exceptionFacts;
   }

   public IterableSet<AugmentedStmt> get_MonitorFacts() {
      return this.monitorFacts;
   }

   public IterableSet<String> getImportList() {
      return this.importList;
   }

   DavaBody(Body body) {
      this(body.getMethod());
      this.debug("DavaBody", "creating DavaBody for" + body.getMethod().toString());
      Dava.v().log("\nstart method " + body.getMethod().toString());
      if (this.DEBUG && body.getMethod().getExceptions().size() != 0) {
         this.debug("DavaBody", "printing NON EMPTY exception list for " + body.getMethod().toString() + " " + body.getMethod().getExceptions().toString());
      }

      this.copy_Body(body);
      AugmentedStmtGraph asg = new AugmentedStmtGraph(new BriefUnitGraph(this), new TrapUnitGraph(this));
      ExceptionFinder.v().preprocess(this, asg);
      SETTopNode SET = new SETTopNode(asg.get_ChainView());

      while(true) {
         try {
            CycleFinder.v().find(this, asg, SET);
            IfFinder.v().find(this, asg, SET);
            SwitchFinder.v().find(this, asg, SET);
            SynchronizedBlockFinder.v().find(this, asg, SET);
            ExceptionFinder.v().find(this, asg, SET);
            SequenceFinder.v().find(this, asg, SET);
            LabeledBlockFinder.v().find(this, asg, SET);
            AbruptEdgeFinder.v().find(this, asg, SET);
            break;
         } catch (RetriggerAnalysisException var7) {
            SET = new SETTopNode(asg.get_ChainView());
            this.consumedConditions = new HashSet();
         }
      }

      MonitorConverter.v().convert(this);
      ThrowNullConverter.v().convert(this);
      ASTNode AST = SET.emit_AST();
      this.getTraps().clear();
      this.getUnits().clear();
      this.getUnits().addLast((Unit)AST);

      do {
         G.v().ASTAnalysis_modified = false;
         AST.perform_Analysis(UselessTryRemover.v());
      } while(G.v().ASTAnalysis_modified);

      if (AST instanceof ASTMethodNode) {
         ((ASTMethodNode)AST).storeLocals(this);
         Map options = PhaseOptions.v().getPhaseOptions("db.force-recompile");
         boolean force = PhaseOptions.getBoolean(options, "enabled");
         if (force) {
            AST.apply(new SuperFirstStmtHandler((ASTMethodNode)AST));
         }

         this.debug("DavaBody", "PreInit booleans is" + G.v().SootMethodAddedByDava);
      }

      Dava.v().log("end method " + body.getMethod().toString());
   }

   public void applyBugFixes() {
      ASTNode AST = (ASTNode)this.getUnits().getFirst();
      this.debug("applyBugFixes", "Applying AST analyzes for method" + this.getMethod().toString());
      AST.apply(new ShortcutIfGenerator());
      this.debug("applyBugFixes", "after ShortcutIfGenerator" + G.v().ASTTransformations_modified);
      AST.apply(new TypeCastingError());
      this.debug("applyBugFixes", "after TypeCastingError" + G.v().ASTTransformations_modified);
   }

   public void analyzeAST() {
      ASTNode AST = (ASTNode)this.getUnits().getFirst();
      this.debug("analyzeAST", "Applying AST analyzes for method" + this.getMethod().toString());
      this.applyASTAnalyses(AST);
      this.debug("analyzeAST", "Applying structure analysis" + this.getMethod().toString());
      this.applyStructuralAnalyses(AST);
      this.debug("analyzeAST", "Applying structure analysis DONE" + this.getMethod().toString());
   }

   private void applyASTAnalyses(ASTNode AST) {
      this.debug("applyASTAnalyses", "initial one time analyses started");
      AST.apply(new BooleanConditionSimplification());
      AST.apply(new DecrementIncrementStmtCreation());
      this.debug("applyASTAnalyses", "initial one time analyses completed");
      boolean flag = true;
      G.v().ASTTransformations_modified = false;
      G.v().ASTIfElseFlipped = false;
      int countFlipping = 0;
      if (flag) {
         do {
            this.debug("applyASTAnalyses", "ITERATION");
            G.v().ASTTransformations_modified = false;
            AST.apply(new AndAggregator());
            this.debug("applyASTAnalyses", "after AndAggregator" + G.v().ASTTransformations_modified);
            AST.apply(new OrAggregatorOne());
            this.debug("applyASTAnalyses", "after OraggregatorOne" + G.v().ASTTransformations_modified);
            AST.apply(new OrAggregatorTwo());
            this.debug("applyASTAnalyses", "after OraggregatorTwo" + G.v().ASTTransformations_modified);
            this.debug("applyASTAnalyses", "after OraggregatorTwo ifElseFlipped is" + G.v().ASTIfElseFlipped);
            AST.apply(new OrAggregatorFour());
            this.debug("applyASTAnalyses", "after OraggregatorFour" + G.v().ASTTransformations_modified);
            AST.apply(new ASTCleaner());
            this.debug("applyASTAnalyses", "after ASTCleaner" + G.v().ASTTransformations_modified);
            AST.apply(new PushLabeledBlockIn());
            this.debug("applyASTAnalyses", "after PushLabeledBlockIn" + G.v().ASTTransformations_modified);
            AST.apply(new LoopStrengthener());
            this.debug("applyASTAnalyses", "after LoopStrengthener" + G.v().ASTTransformations_modified);
            AST.apply(new ASTCleanerTwo());
            this.debug("applyASTAnalyses", "after ASTCleanerTwo" + G.v().ASTTransformations_modified);
            AST.apply(new ForLoopCreator());
            this.debug("applyASTAnalyses", "after ForLoopCreator" + G.v().ASTTransformations_modified);
            AST.apply(new NewStringBufferSimplification());
            this.debug("applyASTAnalyses", "after NewStringBufferSimplification" + G.v().ASTTransformations_modified);
            AST.apply(new ShortcutArrayInit());
            this.debug("applyASTAnalyses", "after ShortcutArrayInit" + G.v().ASTTransformations_modified);
            AST.apply(new UselessLabeledBlockRemover());
            this.debug("applyASTAnalyses", "after UselessLabeledBlockRemover" + G.v().ASTTransformations_modified);
            if (!G.v().ASTTransformations_modified) {
               AST.apply(new IfElseSplitter());
               this.debug("applyASTAnalyses", "after IfElseSplitter" + G.v().ASTTransformations_modified);
            }

            if (!G.v().ASTTransformations_modified) {
               AST.apply(new UselessAbruptStmtRemover());
               this.debug("applyASTAnalyses", "after UselessAbruptStmtRemover" + G.v().ASTTransformations_modified);
            }

            AST.apply(new ShortcutIfGenerator());
            this.debug("applyASTAnalyses", "after ShortcutIfGenerator" + G.v().ASTTransformations_modified);
            AST.apply(new TypeCastingError());
            this.debug("applyASTAnalyses", "after TypeCastingError" + G.v().ASTTransformations_modified);
            if (G.v().ASTTransformations_modified) {
               G.v().ASTIfElseFlipped = false;
               countFlipping = 0;
               this.debug("applyASTanalyses", "Transformation modified was true hence will reiterate. set flipped to false");
            } else if (G.v().ASTIfElseFlipped) {
               this.debug("", "ifelseflipped and transformations NOT modified");
               if (countFlipping == 0) {
                  this.debug("", "ifelseflipped and transformations NOT modified count is 0");
                  G.v().ASTIfElseFlipped = false;
                  ++countFlipping;
                  G.v().ASTTransformations_modified = true;
               } else {
                  this.debug("", "ifelseflipped and transformations NOT modified count is not 0 TERMINATE");
               }
            }
         } while(G.v().ASTTransformations_modified);
      }

      AST.apply(ClosestAbruptTargetFinder.v());
      this.debug("applyASTAnalyses", "after ClosestAbruptTargetFinder" + G.v().ASTTransformations_modified);
      Map<String, String> options = PhaseOptions.v().getPhaseOptions("db.force-recompile");
      boolean force = PhaseOptions.getBoolean(options, "enabled");
      if (force) {
         this.debug("applyASTAnalyses", "before FinalFieldDefinition" + G.v().ASTTransformations_modified);
         new FinalFieldDefinition((ASTMethodNode)AST);
         this.debug("applyASTAnalyses", "after FinalFieldDefinition" + G.v().ASTTransformations_modified);
      }

      AST.apply(new DeInliningFinalFields());
      this.debug("applyASTAnalyses", "end applyASTAnlayses" + G.v().ASTTransformations_modified);
   }

   private void applyStructuralAnalyses(ASTNode AST) {
      this.debug("applyStructureAnalyses", "invoking copy propagation");
      CopyPropagation prop = new CopyPropagation(AST);
      AST.apply(prop);
      this.debug("applyStructureAnalyses", "invoking copy propagation DONE");
      this.debug("applyStructureAnalyses", "Local Variable Cleaner started");
      AST.apply(new LocalVariableCleaner(AST));
      this.debug("applyStructureAnalyses", "Local Variable Cleaner DONE");
   }

   private void copy_Body(Body body) {
      if (!(body instanceof GrimpBody)) {
         throw new RuntimeException("You can only create a DavaBody from a GrimpBody!");
      } else {
         GrimpBody grimpBody = (GrimpBody)body;
         HashMap<Switchable, Switchable> bindings = new HashMap();
         HashMap<Unit, Unit> reverse_binding = new HashMap();
         Iterator var5 = grimpBody.getUnits().iterator();

         Unit u;
         Unit newObject;
         while(var5.hasNext()) {
            u = (Unit)var5.next();
            newObject = (Unit)u.clone();
            this.getUnits().addLast(newObject);
            bindings.put(u, newObject);
            reverse_binding.put(newObject, u);
         }

         var5 = this.getUnits().iterator();

         while(true) {
            int i;
            Stmt s;
            do {
               if (!var5.hasNext()) {
                  var5 = grimpBody.getLocals().iterator();

                  while(var5.hasNext()) {
                     Local original = (Local)var5.next();
                     Local copy = Dava.v().newLocal(original.getName(), original.getType());
                     this.getLocals().add(copy);
                     bindings.put(original, copy);
                  }

                  var5 = this.getAllUnitBoxes().iterator();

                  Unit handlerUnit;
                  while(var5.hasNext()) {
                     UnitBox box = (UnitBox)var5.next();
                     handlerUnit = box.getUnit();
                     if ((newObject = (Unit)bindings.get(handlerUnit)) != null) {
                        box.setUnit(newObject);
                     }
                  }

                  var5 = this.getUseAndDefBoxes().iterator();

                  while(var5.hasNext()) {
                     ValueBox vb = (ValueBox)var5.next();
                     if (vb.getValue() instanceof Local) {
                        vb.setValue((Value)bindings.get(vb.getValue()));
                     }
                  }

                  var5 = grimpBody.getTraps().iterator();

                  Trap t;
                  while(var5.hasNext()) {
                     t = (Trap)var5.next();
                     Trap cloneTrap = (Trap)t.clone();
                     handlerUnit = (Unit)bindings.get(t.getHandlerUnit());
                     cloneTrap.setHandlerUnit(handlerUnit);
                     cloneTrap.setBeginUnit((Unit)bindings.get(t.getBeginUnit()));
                     cloneTrap.setEndUnit((Unit)bindings.get(t.getEndUnit()));
                     this.getTraps().add(cloneTrap);
                  }

                  PatchingChain<Unit> units = this.getUnits();
                  Iterator it = units.snapshotIterator();

                  while(true) {
                     while(it.hasNext()) {
                        Unit u = (Unit)it.next();
                        Stmt s = (Stmt)u;
                        JGotoStmt jgs;
                        JGotoStmt jgs;
                        if (s instanceof IfStmt) {
                           IfStmt ifs = (IfStmt)s;
                           jgs = new JGotoStmt(units.getSuccOf(u));
                           units.insertAfter((Unit)jgs, (Unit)u);
                           jgs = new JGotoStmt(ifs.getTarget());
                           units.insertAfter((Unit)jgs, (Unit)jgs);
                           ifs.setTarget(jgs);
                        } else {
                           int i;
                           if (s instanceof TableSwitchStmt) {
                              TableSwitchStmt tss = (TableSwitchStmt)s;
                              i = tss.getHighIndex() - tss.getLowIndex() + 1;

                              for(int i = 0; i < i; ++i) {
                                 JGotoStmt jgs = new JGotoStmt(tss.getTarget(i));
                                 units.insertAfter((Unit)jgs, (Unit)tss);
                                 tss.setTarget(i, jgs);
                              }

                              jgs = new JGotoStmt(tss.getDefaultTarget());
                              units.insertAfter((Unit)jgs, (Unit)tss);
                              tss.setDefaultTarget(jgs);
                           } else if (s instanceof LookupSwitchStmt) {
                              LookupSwitchStmt lss = (LookupSwitchStmt)s;

                              for(i = 0; i < lss.getTargetCount(); ++i) {
                                 jgs = new JGotoStmt(lss.getTarget(i));
                                 units.insertAfter((Unit)jgs, (Unit)lss);
                                 lss.setTarget(i, jgs);
                              }

                              jgs = new JGotoStmt(lss.getDefaultTarget());
                              units.insertAfter((Unit)jgs, (Unit)lss);
                              lss.setDefaultTarget(jgs);
                           }
                        }
                     }

                     var5 = this.getTraps().iterator();

                     while(var5.hasNext()) {
                        t = (Trap)var5.next();
                        JGotoStmt jgs = new JGotoStmt(t.getHandlerUnit());
                        units.addLast((Unit)jgs);
                        t.setHandlerUnit(jgs);
                     }

                     Iterator var14 = this.getLocals().iterator();

                     while(var14.hasNext()) {
                        Local l = (Local)var14.next();
                        Type t = l.getType();
                        if (t instanceof RefType) {
                           RefType rt = (RefType)t;
                           String className = rt.getSootClass().toString();
                           String packageName = rt.getSootClass().getJavaPackageName();
                           String classPackageName = packageName;
                           if (className.lastIndexOf(46) > 0) {
                              classPackageName = className.substring(0, className.lastIndexOf(46));
                           }

                           if (!packageName.equals(classPackageName)) {
                              throw new DecompilationException("Unable to retrieve package name for identifier. Please report to developer.");
                           }

                           this.addToImportList(className);
                        }
                     }

                     var14 = this.getUnits().iterator();

                     Unit u;
                     Stmt s;
                     DefinitionStmt ds;
                     while(var14.hasNext()) {
                        u = (Unit)var14.next();
                        s = (Stmt)u;
                        if (s instanceof IfStmt) {
                           this.javafy(((IfStmt)s).getConditionBox());
                        } else if (s instanceof ThrowStmt) {
                           this.javafy(((ThrowStmt)s).getOpBox());
                        } else if (s instanceof TableSwitchStmt) {
                           this.javafy(((TableSwitchStmt)s).getKeyBox());
                        } else if (s instanceof LookupSwitchStmt) {
                           this.javafy(((LookupSwitchStmt)s).getKeyBox());
                        } else if (s instanceof MonitorStmt) {
                           this.javafy(((MonitorStmt)s).getOpBox());
                        } else if (s instanceof DefinitionStmt) {
                           ds = (DefinitionStmt)s;
                           this.javafy(ds.getRightOpBox());
                           this.javafy(ds.getLeftOpBox());
                           if (ds.getRightOp() instanceof IntConstant) {
                              ds.getRightOpBox().setValue(DIntConstant.v(((IntConstant)ds.getRightOp()).value, ds.getLeftOp().getType()));
                           }
                        } else if (s instanceof ReturnStmt) {
                           ReturnStmt rs = (ReturnStmt)s;
                           if (rs.getOp() instanceof IntConstant) {
                              rs.getOpBox().setValue(DIntConstant.v(((IntConstant)rs.getOp()).value, body.getMethod().getReturnType()));
                           } else {
                              this.javafy(rs.getOpBox());
                           }
                        } else if (s instanceof InvokeStmt) {
                           this.javafy(((InvokeStmt)s).getInvokeExprBox());
                        }
                     }

                     var14 = this.getUnits().iterator();

                     while(var14.hasNext()) {
                        u = (Unit)var14.next();
                        s = (Stmt)u;
                        Value rightOp;
                        if (s instanceof IdentityStmt) {
                           IdentityStmt ids = (IdentityStmt)s;
                           rightOp = ids.getRightOp();
                           Value ids_leftOp = ids.getLeftOp();
                           if (ids_leftOp instanceof Local && rightOp instanceof ThisRef) {
                              Local thisLocal = (Local)ids_leftOp;
                              this.thisLocals.add(thisLocal);
                              thisLocal.setName("this");
                           }
                        }

                        if (s instanceof DefinitionStmt) {
                           ds = (DefinitionStmt)s;
                           rightOp = ds.getRightOp();
                           if (rightOp instanceof ParameterRef) {
                              this.pMap.put(((ParameterRef)rightOp).getIndex(), ds.getLeftOp());
                           }

                           if (rightOp instanceof CaughtExceptionRef) {
                              this.caughtrefs.add((CaughtExceptionRef)rightOp);
                           }
                        }
                     }

                     var14 = this.getUnits().iterator();

                     while(true) {
                        InstanceInvokeExpr iie;
                        String name;
                        do {
                           Value base;
                           do {
                              do {
                                 InvokeExpr ie;
                                 do {
                                    do {
                                       if (!var14.hasNext()) {
                                          return;
                                       }

                                       u = (Unit)var14.next();
                                       s = (Stmt)u;
                                    } while(!(s instanceof InvokeStmt));

                                    InvokeStmt ivs = (InvokeStmt)s;
                                    ie = ivs.getInvokeExpr();
                                 } while(!(ie instanceof InstanceInvokeExpr));

                                 iie = (InstanceInvokeExpr)ie;
                                 base = iie.getBase();
                              } while(!(base instanceof Local));
                           } while(!((Local)base).getName().equals("this"));

                           SootMethodRef m = iie.getMethodRef();
                           name = m.name();
                        } while(!name.equals("<init>") && !name.equals("<clinit>"));

                        if (this.constructorUnit != null) {
                           throw new RuntimeException("More than one candidate for constructor found.");
                        }

                        this.constructorExpr = iie;
                        this.constructorUnit = s;
                     }
                  }
               }

               u = (Unit)var5.next();
               s = (Stmt)u;
               if (s instanceof TableSwitchStmt) {
                  TableSwitchStmt ts = (TableSwitchStmt)s;
                  TableSwitchStmt original_switch = (TableSwitchStmt)reverse_binding.get(u);
                  ts.setDefaultTarget((Unit)bindings.get(original_switch.getDefaultTarget()));
                  LinkedList<Unit> new_target_list = new LinkedList();
                  i = ts.getHighIndex() - ts.getLowIndex() + 1;

                  for(int i = 0; i < i; ++i) {
                     new_target_list.add((Unit)bindings.get(original_switch.getTarget(i)));
                  }

                  ts.setTargets(new_target_list);
               }
            } while(!(s instanceof LookupSwitchStmt));

            LookupSwitchStmt ls = (LookupSwitchStmt)s;
            LookupSwitchStmt original_switch = (LookupSwitchStmt)reverse_binding.get(u);
            ls.setDefaultTarget((Unit)bindings.get(original_switch.getDefaultTarget()));
            Unit[] new_target_list = new Unit[original_switch.getTargetCount()];

            for(i = 0; i < original_switch.getTargetCount(); ++i) {
               new_target_list[i] = (Unit)((Unit)bindings.get(original_switch.getTarget(i)));
            }

            ls.setTargets(new_target_list);
            ls.setLookupValues(original_switch.getLookupValues());
         }
      }
   }

   private void javafy(ValueBox vb) {
      Value v = vb.getValue();
      if (v instanceof Expr) {
         this.javafy_expr(vb);
      } else if (v instanceof Ref) {
         this.javafy_ref(vb);
      } else if (v instanceof Local) {
         this.javafy_local(vb);
      } else if (v instanceof Constant) {
         this.javafy_constant(vb);
      }

   }

   private void javafy_expr(ValueBox vb) {
      Expr e = (Expr)vb.getValue();
      if (e instanceof BinopExpr) {
         this.javafy_binop_expr(vb);
      } else if (e instanceof UnopExpr) {
         this.javafy_unop_expr(vb);
      } else if (e instanceof CastExpr) {
         this.javafy_cast_expr(vb);
      } else if (e instanceof NewArrayExpr) {
         this.javafy_newarray_expr(vb);
      } else if (e instanceof NewMultiArrayExpr) {
         this.javafy_newmultiarray_expr(vb);
      } else if (e instanceof InstanceOfExpr) {
         this.javafy_instanceof_expr(vb);
      } else if (e instanceof InvokeExpr) {
         this.javafy_invoke_expr(vb);
      } else if (e instanceof NewExpr) {
         this.javafy_new_expr(vb);
      }

   }

   private void javafy_ref(ValueBox vb) {
      Ref r = (Ref)vb.getValue();
      if (r instanceof StaticFieldRef) {
         SootFieldRef fieldRef = ((StaticFieldRef)r).getFieldRef();
         String className = fieldRef.declaringClass().toString();
         String packageName = fieldRef.declaringClass().getJavaPackageName();
         String classPackageName = packageName;
         if (className.lastIndexOf(46) > 0) {
            classPackageName = className.substring(0, className.lastIndexOf(46));
         }

         if (!packageName.equals(classPackageName)) {
            throw new DecompilationException("Unable to retrieve package name for identifier. Please report to developer.");
         }

         this.addToImportList(className);
         vb.setValue(new DStaticFieldRef(fieldRef, this.getMethod().getDeclaringClass().getName()));
      } else if (r instanceof ArrayRef) {
         ArrayRef ar = (ArrayRef)r;
         this.javafy(ar.getBaseBox());
         this.javafy(ar.getIndexBox());
      } else if (r instanceof InstanceFieldRef) {
         InstanceFieldRef ifr = (InstanceFieldRef)r;
         this.javafy(ifr.getBaseBox());
         vb.setValue(new DInstanceFieldRef(ifr.getBase(), ifr.getFieldRef(), this.thisLocals));
      } else if (r instanceof ThisRef) {
         ThisRef tr = (ThisRef)r;
         vb.setValue(new DThisRef((RefType)tr.getType()));
      }

   }

   private void javafy_local(ValueBox vb) {
   }

   private void javafy_constant(ValueBox vb) {
   }

   private void javafy_binop_expr(ValueBox vb) {
      BinopExpr boe = (BinopExpr)vb.getValue();
      ValueBox leftOpBox = boe.getOp1Box();
      ValueBox rightOpBox = boe.getOp2Box();
      Value leftOp = leftOpBox.getValue();
      Value rightOp = rightOpBox.getValue();
      if (rightOp instanceof IntConstant) {
         if (!(leftOp instanceof IntConstant)) {
            this.javafy(leftOpBox);
            leftOp = leftOpBox.getValue();
            if (boe instanceof ConditionExpr) {
               rightOpBox.setValue(DIntConstant.v(((IntConstant)rightOp).value, leftOp.getType()));
            } else {
               rightOpBox.setValue(DIntConstant.v(((IntConstant)rightOp).value, (Type)null));
            }
         }
      } else if (leftOp instanceof IntConstant) {
         this.javafy(rightOpBox);
         rightOp = rightOpBox.getValue();
         if (boe instanceof ConditionExpr) {
            leftOpBox.setValue(DIntConstant.v(((IntConstant)leftOp).value, rightOp.getType()));
         } else {
            leftOpBox.setValue(DIntConstant.v(((IntConstant)leftOp).value, (Type)null));
         }
      } else {
         this.javafy(rightOpBox);
         rightOp = rightOpBox.getValue();
         this.javafy(leftOpBox);
         leftOp = leftOpBox.getValue();
      }

      if (boe instanceof CmpExpr) {
         vb.setValue(new DCmpExpr(leftOp, rightOp));
      } else if (boe instanceof CmplExpr) {
         vb.setValue(new DCmplExpr(leftOp, rightOp));
      } else if (boe instanceof CmpgExpr) {
         vb.setValue(new DCmpgExpr(leftOp, rightOp));
      }

   }

   private void javafy_unop_expr(ValueBox vb) {
      UnopExpr uoe = (UnopExpr)vb.getValue();
      this.javafy(uoe.getOpBox());
      if (uoe instanceof LengthExpr) {
         vb.setValue(new DLengthExpr(((LengthExpr)uoe).getOp()));
      } else if (uoe instanceof NegExpr) {
         vb.setValue(new DNegExpr(((NegExpr)uoe).getOp()));
      }

   }

   private void javafy_cast_expr(ValueBox vb) {
      CastExpr ce = (CastExpr)vb.getValue();
      this.javafy(ce.getOpBox());
   }

   private void javafy_newarray_expr(ValueBox vb) {
      NewArrayExpr nae = (NewArrayExpr)vb.getValue();
      this.javafy(nae.getSizeBox());
      vb.setValue(new DNewArrayExpr(nae.getBaseType(), nae.getSize()));
   }

   private void javafy_newmultiarray_expr(ValueBox vb) {
      NewMultiArrayExpr nmae = (NewMultiArrayExpr)vb.getValue();

      for(int i = 0; i < nmae.getSizeCount(); ++i) {
         this.javafy(nmae.getSizeBox(i));
      }

      vb.setValue(new DNewMultiArrayExpr(nmae.getBaseType(), nmae.getSizes()));
   }

   private void javafy_instanceof_expr(ValueBox vb) {
      InstanceOfExpr ioe = (InstanceOfExpr)vb.getValue();
      this.javafy(ioe.getOpBox());
   }

   private void javafy_invoke_expr(ValueBox vb) {
      InvokeExpr ie = (InvokeExpr)vb.getValue();
      String className = ie.getMethodRef().declaringClass().toString();
      String packageName = ie.getMethodRef().declaringClass().getJavaPackageName();
      String classPackageName = packageName;
      if (className.lastIndexOf(46) > 0) {
         classPackageName = className.substring(0, className.lastIndexOf(46));
      }

      if (!packageName.equals(classPackageName)) {
         throw new DecompilationException("Unable to retrieve package name for identifier. Please report to developer.");
      } else {
         this.addToImportList(className);

         for(int i = 0; i < ie.getArgCount(); ++i) {
            Value arg = ie.getArg(i);
            if (arg instanceof IntConstant) {
               ie.getArgBox(i).setValue(DIntConstant.v(((IntConstant)arg).value, ie.getMethodRef().parameterType(i)));
            } else {
               this.javafy(ie.getArgBox(i));
            }
         }

         if (ie instanceof InstanceInvokeExpr) {
            this.javafy(((InstanceInvokeExpr)ie).getBaseBox());
            if (ie instanceof VirtualInvokeExpr) {
               VirtualInvokeExpr vie = (VirtualInvokeExpr)ie;
               vb.setValue(new DVirtualInvokeExpr(vie.getBase(), vie.getMethodRef(), vie.getArgs(), this.thisLocals));
            } else if (ie instanceof SpecialInvokeExpr) {
               SpecialInvokeExpr sie = (SpecialInvokeExpr)ie;
               vb.setValue(new DSpecialInvokeExpr(sie.getBase(), sie.getMethodRef(), sie.getArgs()));
            } else {
               if (!(ie instanceof InterfaceInvokeExpr)) {
                  throw new RuntimeException("InstanceInvokeExpr " + ie + " not javafied correctly");
               }

               InterfaceInvokeExpr iie = (InterfaceInvokeExpr)ie;
               vb.setValue(new DInterfaceInvokeExpr(iie.getBase(), iie.getMethodRef(), iie.getArgs()));
            }
         } else {
            if (!(ie instanceof StaticInvokeExpr)) {
               throw new RuntimeException("InvokeExpr " + ie + " not javafied correctly");
            }

            StaticInvokeExpr sie = (StaticInvokeExpr)ie;
            if (sie instanceof NewInvokeExpr) {
               NewInvokeExpr nie = (NewInvokeExpr)sie;
               RefType rt = nie.getBaseType();
               className = rt.getSootClass().toString();
               packageName = rt.getSootClass().getJavaPackageName();
               classPackageName = packageName;
               if (className.lastIndexOf(46) > 0) {
                  classPackageName = className.substring(0, className.lastIndexOf(46));
               }

               if (!packageName.equals(classPackageName)) {
                  throw new DecompilationException("Unable to retrieve package name for identifier. Please report to developer.");
               }

               this.addToImportList(className);
               vb.setValue(new DNewInvokeExpr((RefType)nie.getType(), nie.getMethodRef(), nie.getArgs()));
            } else {
               SootMethodRef methodRef = sie.getMethodRef();
               className = methodRef.declaringClass().toString();
               packageName = methodRef.declaringClass().getJavaPackageName();
               classPackageName = packageName;
               if (className.lastIndexOf(46) > 0) {
                  classPackageName = className.substring(0, className.lastIndexOf(46));
               }

               if (!packageName.equals(classPackageName)) {
                  throw new DecompilationException("Unable to retrieve package name for identifier. Please report to developer.");
               }

               this.addToImportList(className);
               vb.setValue(new DStaticInvokeExpr(methodRef, sie.getArgs()));
            }
         }

      }
   }

   private void javafy_new_expr(ValueBox vb) {
      NewExpr ne = (NewExpr)vb.getValue();
      String className = ne.getBaseType().getSootClass().toString();
      String packageName = ne.getBaseType().getSootClass().getJavaPackageName();
      String classPackageName = packageName;
      if (className.lastIndexOf(46) > 0) {
         classPackageName = className.substring(0, className.lastIndexOf(46));
      }

      if (!packageName.equals(classPackageName)) {
         throw new DecompilationException("Unable to retrieve package name for identifier. Please report to developer.");
      } else {
         this.addToImportList(className);
      }
   }

   public void addToImportList(String className) {
      if (!className.isEmpty()) {
         this.importList.add(className);
      }

   }

   public void debug(String methodName, String debug) {
      if (this.DEBUG) {
         System.out.println(methodName + "    DEBUG: " + debug);
      }

   }
}
