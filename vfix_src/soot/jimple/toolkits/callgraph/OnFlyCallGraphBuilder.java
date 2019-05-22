package soot.jimple.toolkits.callgraph;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.Context;
import soot.DoubleType;
import soot.EntryPoints;
import soot.FastHierarchy;
import soot.FloatType;
import soot.IntType;
import soot.Kind;
import soot.Local;
import soot.LongType;
import soot.MethodContext;
import soot.MethodOrMethodContext;
import soot.NullType;
import soot.PackManager;
import soot.PhaseOptions;
import soot.PrimType;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.ShortType;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Transform;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.AssignStmt;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.FieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.toolkits.annotation.nullcheck.NullnessAnalysis;
import soot.jimple.toolkits.reflection.ReflectionTraceInfo;
import soot.options.CGOptions;
import soot.options.Options;
import soot.options.SparkOptions;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.util.HashMultiMap;
import soot.util.LargeNumberedMap;
import soot.util.MultiMap;
import soot.util.NumberedString;
import soot.util.SmallNumberedMap;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;

public final class OnFlyCallGraphBuilder {
   private static final Logger logger = LoggerFactory.getLogger(OnFlyCallGraphBuilder.class);
   private static final PrimType[] CHAR_NARROWINGS = new PrimType[]{CharType.v()};
   private static final PrimType[] INT_NARROWINGS = new PrimType[]{IntType.v(), CharType.v(), ShortType.v(), ByteType.v(), ShortType.v()};
   private static final PrimType[] SHORT_NARROWINGS = new PrimType[]{ShortType.v(), ByteType.v()};
   private static final PrimType[] LONG_NARROWINGS = new PrimType[]{LongType.v(), IntType.v(), CharType.v(), ShortType.v(), ByteType.v(), ShortType.v()};
   private static final ByteType[] BYTE_NARROWINGS = new ByteType[]{ByteType.v()};
   private static final PrimType[] FLOAT_NARROWINGS = new PrimType[]{FloatType.v(), LongType.v(), IntType.v(), CharType.v(), ShortType.v(), ByteType.v(), ShortType.v()};
   private static final PrimType[] BOOLEAN_NARROWINGS = new PrimType[]{BooleanType.v()};
   private static final PrimType[] DOUBLE_NARROWINGS = new PrimType[]{DoubleType.v(), FloatType.v(), LongType.v(), IntType.v(), CharType.v(), ShortType.v(), ByteType.v(), ShortType.v()};
   protected final NumberedString sigFinalize;
   protected final NumberedString sigInit;
   protected final NumberedString sigStart;
   protected final NumberedString sigRun;
   protected final NumberedString sigExecute;
   protected final NumberedString sigExecutorExecute;
   protected final NumberedString sigHandlerPost;
   protected final NumberedString sigHandlerPostAtFrontOfQueue;
   protected final NumberedString sigHandlerPostAtTime;
   protected final NumberedString sigHandlerPostAtTimeWithToken;
   protected final NumberedString sigHandlerPostDelayed;
   protected final NumberedString sigHandlerSendEmptyMessage;
   protected final NumberedString sigHandlerSendEmptyMessageAtTime;
   protected final NumberedString sigHandlerSendEmptyMessageDelayed;
   protected final NumberedString sigHandlerSendMessage;
   protected final NumberedString sigHandlerSendMessageAtFrontOfQueue;
   protected final NumberedString sigHandlerSendMessageAtTime;
   protected final NumberedString sigHandlerSendMessageDelayed;
   protected final NumberedString sigHandlerHandleMessage;
   protected final NumberedString sigObjRun;
   protected final NumberedString sigDoInBackground;
   protected final NumberedString sigForName;
   protected final RefType clRunnable;
   protected final RefType clAsyncTask;
   protected final RefType clHandler;
   private final CallGraph cicg;
   private final HashSet<SootMethod> analyzedMethods;
   private final LargeNumberedMap<Local, List<VirtualCallSite>> receiverToSites;
   private final LargeNumberedMap<SootMethod, List<Local>> methodToReceivers;
   private final LargeNumberedMap<SootMethod, List<Local>> methodToInvokeBases;
   private final LargeNumberedMap<SootMethod, List<Local>> methodToInvokeArgs;
   private final MultiMap<Local, InvokeCallSite> baseToInvokeSite;
   private final MultiMap<Local, InvokeCallSite> invokeArgsToInvokeSite;
   private final Map<Local, BitSet> invokeArgsToSize;
   private final MultiMap<AllocDotField, Local> allocDotFieldToLocal;
   private final MultiMap<Local, Type> reachingArgTypes;
   private final MultiMap<Local, Type> reachingBaseTypes;
   private final SmallNumberedMap<List<VirtualCallSite>> stringConstToSites;
   private final LargeNumberedMap<SootMethod, List<Local>> methodToStringConstants;
   private final ChunkedQueue<SootMethod> targetsQueue;
   private final QueueReader<SootMethod> targets;
   ReflectionModel reflectionModel;
   private CGOptions options;
   private boolean appOnly;
   private ReachableMethods rm;
   private QueueReader<MethodOrMethodContext> worklist;
   private ContextManager cm;
   private FastHierarchy fh;
   private NullnessAnalysis nullnessCache;
   private ConstantArrayAnalysis arrayCache;
   private SootMethod analysisKey;

   public OnFlyCallGraphBuilder(ContextManager cm, ReachableMethods rm) {
      this.sigFinalize = Scene.v().getSubSigNumberer().findOrAdd("void finalize()");
      this.sigInit = Scene.v().getSubSigNumberer().findOrAdd("void <init>()");
      this.sigStart = Scene.v().getSubSigNumberer().findOrAdd("void start()");
      this.sigRun = Scene.v().getSubSigNumberer().findOrAdd("void run()");
      this.sigExecute = Scene.v().getSubSigNumberer().findOrAdd("android.os.AsyncTask execute(java.lang.Object[])");
      this.sigExecutorExecute = Scene.v().getSubSigNumberer().findOrAdd("void execute(java.lang.Runnable)");
      this.sigHandlerPost = Scene.v().getSubSigNumberer().findOrAdd("boolean post(java.lang.Runnable)");
      this.sigHandlerPostAtFrontOfQueue = Scene.v().getSubSigNumberer().findOrAdd("boolean postAtFrontOfQueue(java.lang.Runnable)");
      this.sigHandlerPostAtTime = Scene.v().getSubSigNumberer().findOrAdd("boolean postAtTime(java.lang.Runnable,long)");
      this.sigHandlerPostAtTimeWithToken = Scene.v().getSubSigNumberer().findOrAdd("boolean postAtTime(java.lang.Runnable,java.lang.Object,long)");
      this.sigHandlerPostDelayed = Scene.v().getSubSigNumberer().findOrAdd("boolean postDelayed(java.lang.Runnable,long)");
      this.sigHandlerSendEmptyMessage = Scene.v().getSubSigNumberer().findOrAdd("boolean sendEmptyMessage(int)");
      this.sigHandlerSendEmptyMessageAtTime = Scene.v().getSubSigNumberer().findOrAdd("boolean sendEmptyMessageAtTime(int,long)");
      this.sigHandlerSendEmptyMessageDelayed = Scene.v().getSubSigNumberer().findOrAdd("boolean sendEmptyMessageDelayed(int,long)");
      this.sigHandlerSendMessage = Scene.v().getSubSigNumberer().findOrAdd("boolean postAtTime(java.lang.Runnable,long)");
      this.sigHandlerSendMessageAtFrontOfQueue = Scene.v().getSubSigNumberer().findOrAdd("boolean sendMessageAtFrontOfQueue(android.os.Message)");
      this.sigHandlerSendMessageAtTime = Scene.v().getSubSigNumberer().findOrAdd("boolean sendMessageAtTime(android.os.Message,long)");
      this.sigHandlerSendMessageDelayed = Scene.v().getSubSigNumberer().findOrAdd("boolean sendMessageDelayed(android.os.Message,long)");
      this.sigHandlerHandleMessage = Scene.v().getSubSigNumberer().findOrAdd("void handleMessage(android.os.Message)");
      this.sigObjRun = Scene.v().getSubSigNumberer().findOrAdd("java.lang.Object run()");
      this.sigDoInBackground = Scene.v().getSubSigNumberer().findOrAdd("java.lang.Object doInBackground(java.lang.Object[])");
      this.sigForName = Scene.v().getSubSigNumberer().findOrAdd("java.lang.Class forName(java.lang.String)");
      this.clRunnable = RefType.v("java.lang.Runnable");
      this.clAsyncTask = RefType.v("android.os.AsyncTask");
      this.clHandler = RefType.v("android.os.Handler");
      this.cicg = Scene.v().internalMakeCallGraph();
      this.analyzedMethods = new HashSet();
      this.receiverToSites = new LargeNumberedMap(Scene.v().getLocalNumberer());
      this.methodToReceivers = new LargeNumberedMap(Scene.v().getMethodNumberer());
      this.methodToInvokeBases = new LargeNumberedMap(Scene.v().getMethodNumberer());
      this.methodToInvokeArgs = new LargeNumberedMap(Scene.v().getMethodNumberer());
      this.baseToInvokeSite = new HashMultiMap();
      this.invokeArgsToInvokeSite = new HashMultiMap();
      this.invokeArgsToSize = new IdentityHashMap();
      this.allocDotFieldToLocal = new HashMultiMap();
      this.reachingArgTypes = new HashMultiMap();
      this.reachingBaseTypes = new HashMultiMap();
      this.stringConstToSites = new SmallNumberedMap();
      this.methodToStringConstants = new LargeNumberedMap(Scene.v().getMethodNumberer());
      this.targetsQueue = new ChunkedQueue();
      this.targets = this.targetsQueue.reader();
      this.nullnessCache = null;
      this.arrayCache = null;
      this.analysisKey = null;
      this.cm = cm;
      this.rm = rm;
      this.worklist = rm.listener();
      this.options = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));
      if (!this.options.verbose()) {
         logger.debug("[Call Graph] For information on where the call graph may be incomplete, use the verbose option to the cg phase.");
      }

      if (this.options.reflection_log() != null && this.options.reflection_log().length() != 0) {
         this.reflectionModel = new OnFlyCallGraphBuilder.TraceBasedReflectionModel();
      } else if (this.options.types_for_invoke() && (new SparkOptions(PhaseOptions.v().getPhaseOptions("cg.spark"))).enabled()) {
         this.reflectionModel = new OnFlyCallGraphBuilder.TypeBasedReflectionModel();
      } else {
         this.reflectionModel = new OnFlyCallGraphBuilder.DefaultReflectionModel();
      }

      this.fh = Scene.v().getOrMakeFastHierarchy();
   }

   public OnFlyCallGraphBuilder(ContextManager cm, ReachableMethods rm, boolean appOnly) {
      this(cm, rm);
      this.appOnly = appOnly;
   }

   public LargeNumberedMap<SootMethod, List<Local>> methodToReceivers() {
      return this.methodToReceivers;
   }

   public LargeNumberedMap<SootMethod, List<Local>> methodToInvokeArgs() {
      return this.methodToInvokeArgs;
   }

   public LargeNumberedMap<SootMethod, List<Local>> methodToInvokeBases() {
      return this.methodToInvokeBases;
   }

   public LargeNumberedMap<SootMethod, List<Local>> methodToStringConstants() {
      return this.methodToStringConstants;
   }

   public void processReachables() {
      while(true) {
         if (!this.worklist.hasNext()) {
            this.rm.update();
            if (!this.worklist.hasNext()) {
               return;
            }
         }

         MethodOrMethodContext momc = (MethodOrMethodContext)this.worklist.next();
         SootMethod m = momc.method();
         if (!this.appOnly || m.getDeclaringClass().isApplicationClass()) {
            if (this.analyzedMethods.add(m)) {
               this.processNewMethod(m);
            }

            this.processNewMethodContext(momc);
         }
      }
   }

   public boolean wantTypes(Local receiver) {
      return this.receiverToSites.get(receiver) != null || this.baseToInvokeSite.get(receiver) != null;
   }

   public void addBaseType(Local base, Context context, Type ty) {
      assert context == null;

      Set<InvokeCallSite> invokeSites = this.baseToInvokeSite.get(base);
      if (invokeSites != null && this.reachingBaseTypes.put(base, ty)) {
         this.resolveInvoke(invokeSites);
      }

   }

   public void addInvokeArgType(Local argArray, Context context, Type t) {
      assert context == null;

      Set<InvokeCallSite> invokeSites = this.invokeArgsToInvokeSite.get(argArray);
      if (invokeSites != null && this.reachingArgTypes.put(argArray, t)) {
         this.resolveInvoke(invokeSites);
      }

   }

   public void setArgArrayNonDetSize(Local argArray, Context context) {
      assert context == null;

      Set<InvokeCallSite> invokeSites = this.invokeArgsToInvokeSite.get(argArray);
      if (invokeSites != null) {
         if (this.invokeArgsToSize.containsKey(argArray)) {
            return;
         }

         this.invokeArgsToSize.put(argArray, (Object)null);
         this.resolveInvoke(invokeSites);
      }

   }

   public void addPossibleArgArraySize(Local argArray, int value, Context context) {
      assert context == null;

      Set<InvokeCallSite> invokeSites = this.invokeArgsToInvokeSite.get(argArray);
      if (invokeSites != null) {
         BitSet sizeSet = (BitSet)this.invokeArgsToSize.get(argArray);
         if (sizeSet != null && sizeSet.isEmpty()) {
            return;
         }

         if (sizeSet == null) {
            this.invokeArgsToSize.put(argArray, sizeSet = new BitSet());
         }

         if (!sizeSet.get(value)) {
            sizeSet.set(value);
            this.resolveInvoke(invokeSites);
         }
      }

   }

   private void resolveInvoke(Collection<InvokeCallSite> list) {
      Iterator var2 = list.iterator();

      while(true) {
         while(true) {
            label91:
            while(true) {
               InvokeCallSite ics;
               Set s;
               do {
                  do {
                     if (!var2.hasNext()) {
                        return;
                     }

                     ics = (InvokeCallSite)var2.next();
                     s = this.reachingBaseTypes.get(ics.base());
                  } while(s == null);
               } while(s.isEmpty());

               if (ics.reachingTypes() == null) {
                  boolean mustNotBeNull = ics.nullnessCode() == 1;
                  boolean mustBeNull = ics.nullnessCode() == 0;
                  if (!mustBeNull && (ics.nullnessCode() != -1 || this.invokeArgsToSize.containsKey(ics.argArray()) && this.reachingArgTypes.containsKey(ics.argArray()))) {
                     Set<Type> reachingTypes = this.reachingArgTypes.get(ics.argArray());
                     if (reachingTypes != null && this.invokeArgsToSize.containsKey(ics.argArray())) {
                        assert reachingTypes != null && this.invokeArgsToSize.containsKey(ics.argArray());

                        BitSet methodSizes = (BitSet)this.invokeArgsToSize.get(ics.argArray());
                        Iterator var16 = s.iterator();

                        while(true) {
                           Type bType;
                           do {
                              do {
                                 if (!var16.hasNext()) {
                                    continue label91;
                                 }

                                 bType = (Type)var16.next();

                                 assert bType instanceof RefLikeType;
                              } while(bType instanceof NullType);
                           } while(bType instanceof ArrayType);

                           SootClass baseClass = ((RefType)bType).getSootClass();
                           Iterator mIt = this.getPublicMethodIterator(baseClass, reachingTypes, methodSizes, mustNotBeNull);

                           while(mIt.hasNext()) {
                              SootMethod sm = (SootMethod)mIt.next();
                              this.cm.addVirtualEdge(ics.container(), ics.stmt(), sm, Kind.REFL_INVOKE, (Context)null);
                           }
                        }
                     }

                     assert ics.nullnessCode() == 1 : ics;

                     return;
                  } else {
                     Iterator var7 = s.iterator();

                     while(var7.hasNext()) {
                        Type bType = (Type)var7.next();

                        assert bType instanceof RefType;

                        SootClass baseClass = ((RefType)bType).getSootClass();

                        assert !baseClass.isInterface();

                        Iterator mIt = this.getPublicNullaryMethodIterator(baseClass);

                        while(mIt.hasNext()) {
                           SootMethod sm = (SootMethod)mIt.next();
                           this.cm.addVirtualEdge(ics.container(), ics.stmt(), sm, Kind.REFL_INVOKE, (Context)null);
                        }
                     }
                  }
               } else {
                  assert ics.nullnessCode() != 0;

                  this.resolveStaticTypes(s, ics);
               }
            }
         }
      }
   }

   private void resolveStaticTypes(Set<Type> s, InvokeCallSite ics) {
      ConstantArrayAnalysis.ArrayTypes at = ics.reachingTypes();
      Iterator var4 = s.iterator();

      while(var4.hasNext()) {
         Type bType = (Type)var4.next();
         SootClass baseClass = ((RefType)bType).getSootClass();
         Iterator mIt = this.getPublicMethodIterator(baseClass, at);

         while(mIt.hasNext()) {
            SootMethod sm = (SootMethod)mIt.next();
            this.cm.addVirtualEdge(ics.container(), ics.stmt(), sm, Kind.REFL_INVOKE, (Context)null);
         }
      }

   }

   private Iterator<SootMethod> getPublicMethodIterator(SootClass baseClass, final ConstantArrayAnalysis.ArrayTypes at) {
      return new OnFlyCallGraphBuilder.AbstractMethodIterator(baseClass) {
         protected boolean acceptMethod(SootMethod m) {
            if (!at.possibleSizes.contains(m.getParameterCount())) {
               return false;
            } else {
               for(int i = 0; i < m.getParameterCount(); ++i) {
                  if (!at.possibleTypes[i].isEmpty() && !OnFlyCallGraphBuilder.this.isReflectionCompatible(m.getParameterType(i), at.possibleTypes[i])) {
                     return false;
                  }
               }

               return true;
            }
         }
      };
   }

   private PrimType[] narrowings(PrimType f) {
      if (f instanceof IntType) {
         return INT_NARROWINGS;
      } else if (f instanceof ShortType) {
         return SHORT_NARROWINGS;
      } else if (f instanceof LongType) {
         return LONG_NARROWINGS;
      } else if (f instanceof ByteType) {
         return BYTE_NARROWINGS;
      } else if (f instanceof FloatType) {
         return FLOAT_NARROWINGS;
      } else if (f instanceof BooleanType) {
         return BOOLEAN_NARROWINGS;
      } else if (f instanceof DoubleType) {
         return DOUBLE_NARROWINGS;
      } else if (f instanceof CharType) {
         return CHAR_NARROWINGS;
      } else {
         throw new RuntimeException("Unexpected primitive type: " + f);
      }
   }

   private boolean isReflectionCompatible(Type paramType, Set<Type> reachingTypes) {
      if (reachingTypes.contains(NullType.v())) {
         return true;
      } else if (paramType instanceof RefLikeType) {
         Iterator var8 = reachingTypes.iterator();

         Type rType;
         do {
            if (!var8.hasNext()) {
               return false;
            }

            rType = (Type)var8.next();
         } while(!this.fh.canStoreType(paramType, rType));

         return true;
      } else if (paramType instanceof PrimType) {
         PrimType primType = (PrimType)paramType;
         PrimType[] var4 = this.narrowings(primType);
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PrimType narrowings = var4[var6];
            if (reachingTypes.contains(narrowings.boxedType())) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private Iterator<SootMethod> getPublicMethodIterator(SootClass baseClass, final Set<Type> reachingTypes, final BitSet methodSizes, final boolean mustNotBeNull) {
      return (Iterator)(baseClass.isPhantom() ? Collections.emptyIterator() : new OnFlyCallGraphBuilder.AbstractMethodIterator(baseClass) {
         protected boolean acceptMethod(SootMethod n) {
            int nParams = n.getParameterCount();
            if (methodSizes != null) {
               boolean compatibleSize = methodSizes.get(nParams) || !mustNotBeNull && nParams == 0;
               if (!compatibleSize) {
                  return false;
               }
            }

            List<Type> t = n.getParameterTypes();
            Iterator var4 = t.iterator();

            Type pTy;
            do {
               if (!var4.hasNext()) {
                  return true;
               }

               pTy = (Type)var4.next();
            } while(OnFlyCallGraphBuilder.this.isReflectionCompatible(pTy, reachingTypes));

            return false;
         }
      });
   }

   private Iterator<SootMethod> getPublicNullaryMethodIterator(SootClass baseClass) {
      return (Iterator)(baseClass.isPhantom() ? Collections.emptyIterator() : new OnFlyCallGraphBuilder.AbstractMethodIterator(baseClass) {
         protected boolean acceptMethod(SootMethod n) {
            int nParams = n.getParameterCount();
            return nParams == 0;
         }
      });
   }

   public void addType(Local receiver, Context srcContext, Type type, Context typeContext) {
      FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
      if (this.receiverToSites.get(receiver) != null) {
         Iterator siteIt = ((List)this.receiverToSites.get(receiver)).iterator();

         label74:
         while(true) {
            VirtualCallSite site;
            do {
               do {
                  do {
                     do {
                        if (!siteIt.hasNext()) {
                           break label74;
                        }

                        site = (VirtualCallSite)siteIt.next();
                     } while(site.kind() == Kind.THREAD && !fh.canStoreType(type, this.clRunnable));
                  } while(site.kind() == Kind.EXECUTOR && !fh.canStoreType(type, this.clRunnable));
               } while(site.kind() == Kind.ASYNCTASK && !fh.canStoreType(type, this.clAsyncTask));
            } while(site.kind() == Kind.HANDLER && !fh.canStoreType(type, this.clHandler));

            SootMethod target;
            if (site.iie() instanceof SpecialInvokeExpr && site.kind != Kind.THREAD && site.kind != Kind.EXECUTOR && site.kind != Kind.ASYNCTASK) {
               target = VirtualCalls.v().resolveSpecial((SpecialInvokeExpr)site.iie(), site.subSig(), site.container(), this.appOnly);
               if (target != null) {
                  this.targetsQueue.add(target);
               }
            } else {
               VirtualCalls.v().resolve(type, receiver.getType(), site.subSig(), site.container(), this.targetsQueue, this.appOnly);
            }

            while(this.targets.hasNext()) {
               target = (SootMethod)this.targets.next();
               this.cm.addVirtualEdge(MethodContext.v(site.container(), srcContext), site.stmt(), target, site.kind(), typeContext);
            }
         }
      }

      if (this.baseToInvokeSite.get(receiver) != null) {
         this.addBaseType(receiver, srcContext, type);
      }

   }

   public boolean wantStringConstants(Local stringConst) {
      return this.stringConstToSites.get(stringConst) != null;
   }

   public void addStringConstant(Local l, Context srcContext, String constant) {
      Iterator siteIt = ((List)this.stringConstToSites.get(l)).iterator();

      while(true) {
         while(siteIt.hasNext()) {
            VirtualCallSite site = (VirtualCallSite)siteIt.next();
            if (constant == null) {
               if (this.options.verbose()) {
                  logger.debug("Warning: Method " + site.container() + " is reachable, and calls Class.forName on a non-constant String; graph will be incomplete! Use safe-forname option for a conservative result.");
               }
            } else {
               if (constant.length() > 0 && constant.charAt(0) == '[') {
                  if (constant.length() <= 1 || constant.charAt(1) != 'L' || constant.charAt(constant.length() - 1) != ';') {
                     continue;
                  }

                  constant = constant.substring(2, constant.length() - 1);
               }

               if (!Scene.v().containsClass(constant)) {
                  if (this.options.verbose()) {
                     logger.debug("Warning: Class " + constant + " is a dynamic class, and you did not specify it as such; graph will be incomplete!");
                  }
               } else {
                  SootClass sootcls = Scene.v().getSootClass(constant);
                  if (!sootcls.isApplicationClass() && !sootcls.isPhantom()) {
                     sootcls.setLibraryClass();
                  }

                  Iterator var7 = EntryPoints.v().clinitsOf(sootcls).iterator();

                  while(var7.hasNext()) {
                     SootMethod clinit = (SootMethod)var7.next();
                     this.cm.addStaticEdge(MethodContext.v(site.container(), srcContext), site.stmt(), clinit, Kind.CLINIT);
                  }
               }
            }
         }

         return;
      }
   }

   public boolean wantArrayField(AllocDotField df) {
      return this.allocDotFieldToLocal.containsKey(df);
   }

   public void addInvokeArgType(AllocDotField df, Context context, Type type) {
      if (this.allocDotFieldToLocal.containsKey(df)) {
         Iterator var4 = this.allocDotFieldToLocal.get(df).iterator();

         while(var4.hasNext()) {
            Local l = (Local)var4.next();
            this.addInvokeArgType(l, context, type);
         }

      }
   }

   public boolean wantInvokeArg(Local receiver) {
      return this.invokeArgsToInvokeSite.containsKey(receiver);
   }

   public void addInvokeArgDotField(Local receiver, AllocDotField dot) {
      this.allocDotFieldToLocal.put(dot, receiver);
   }

   private void addInvokeCallSite(Stmt s, SootMethod container, InstanceInvokeExpr d) {
      Local l = (Local)d.getArg(0);
      Value argArray = d.getArg(1);
      InvokeCallSite ics;
      if (argArray instanceof NullConstant) {
         ics = new InvokeCallSite(s, container, d, l);
      } else {
         if (this.analysisKey != container) {
            ExceptionalUnitGraph graph = new ExceptionalUnitGraph(container.getActiveBody());
            this.nullnessCache = new NullnessAnalysis(graph);
            this.arrayCache = new ConstantArrayAnalysis(graph, container.getActiveBody());
            this.analysisKey = container;
         }

         Local argLocal = (Local)argArray;
         byte nullnessCode;
         if (this.nullnessCache.isAlwaysNonNullBefore(s, argLocal)) {
            nullnessCode = 1;
         } else if (this.nullnessCache.isAlwaysNullBefore(s, argLocal)) {
            nullnessCode = 0;
         } else {
            nullnessCode = -1;
         }

         if (nullnessCode != 0 && this.arrayCache.isConstantBefore(s, argLocal)) {
            ConstantArrayAnalysis.ArrayTypes reachingArgTypes = this.arrayCache.getArrayTypesBefore(s, argLocal);
            if (nullnessCode == -1) {
               reachingArgTypes.possibleSizes.add(0);
            }

            ics = new InvokeCallSite(s, container, d, l, reachingArgTypes, nullnessCode);
         } else {
            ics = new InvokeCallSite(s, container, d, l, argLocal, nullnessCode);
            this.invokeArgsToInvokeSite.put(argLocal, ics);
         }
      }

      this.baseToInvokeSite.put(l, ics);
   }

   private void addVirtualCallSite(Stmt s, SootMethod m, Local receiver, InstanceInvokeExpr iie, NumberedString subSig, Kind kind) {
      List<VirtualCallSite> sites = (List)this.receiverToSites.get(receiver);
      if (sites == null) {
         this.receiverToSites.put(receiver, sites = new ArrayList());
         List<Local> receivers = (List)this.methodToReceivers.get(m);
         if (receivers == null) {
            this.methodToReceivers.put(m, receivers = new ArrayList());
         }

         ((List)receivers).add(receiver);
      }

      ((List)sites).add(new VirtualCallSite(s, m, iie, subSig, kind));
   }

   private void processNewMethod(SootMethod m) {
      if (!m.isNative() && !m.isPhantom()) {
         Body b = m.retrieveActiveBody();
         this.getImplicitTargets(m);
         this.findReceivers(m, b);
      }
   }

   private void findReceivers(SootMethod m, Body b) {
      Iterator var3 = b.getUnits().iterator();

      while(true) {
         while(true) {
            while(true) {
               Stmt s;
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  Unit u = (Unit)var3.next();
                  s = (Stmt)u;
               } while(!s.containsInvokeExpr());

               InvokeExpr ie = s.getInvokeExpr();
               if (ie instanceof InstanceInvokeExpr) {
                  InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
                  Local receiver = (Local)iie.getBase();
                  NumberedString subSig = iie.getMethodRef().getSubSignature();
                  this.addVirtualCallSite(s, m, receiver, iie, subSig, Edge.ieToKind(iie));
                  if (subSig == this.sigStart) {
                     this.addVirtualCallSite(s, m, receiver, iie, this.sigRun, Kind.THREAD);
                  } else if (subSig != this.sigExecutorExecute && subSig != this.sigHandlerPost && subSig != this.sigHandlerPostAtFrontOfQueue && subSig != this.sigHandlerPostAtTime && subSig != this.sigHandlerPostAtTimeWithToken && subSig != this.sigHandlerPostDelayed) {
                     if (subSig != this.sigHandlerSendEmptyMessage && subSig != this.sigHandlerSendEmptyMessageAtTime && subSig != this.sigHandlerSendEmptyMessageDelayed && subSig != this.sigHandlerSendMessage && subSig != this.sigHandlerSendMessageAtFrontOfQueue && subSig != this.sigHandlerSendMessageAtTime && subSig != this.sigHandlerSendMessageDelayed) {
                        if (subSig == this.sigExecute) {
                           this.addVirtualCallSite(s, m, receiver, iie, this.sigDoInBackground, Kind.ASYNCTASK);
                        }
                     } else {
                        this.addVirtualCallSite(s, m, receiver, iie, this.sigHandlerHandleMessage, Kind.HANDLER);
                     }
                  } else if (iie.getArgCount() > 0) {
                     Value runnable = iie.getArg(0);
                     if (runnable instanceof Local) {
                        this.addVirtualCallSite(s, m, (Local)runnable, iie, this.sigRun, Kind.EXECUTOR);
                     }
                  }
               } else if (ie instanceof DynamicInvokeExpr) {
                  if (this.options.verbose()) {
                     logger.debug("WARNING: InvokeDynamic to " + ie + " not resolved during call-graph construction.");
                  }
               } else {
                  SootMethod tgt = ie.getMethod();
                  if (tgt != null) {
                     this.addEdge(m, s, tgt);
                     String signature = tgt.getSignature();
                     if (signature.equals("<java.security.AccessController: java.lang.Object doPrivileged(java.security.PrivilegedAction)>") || signature.equals("<java.security.AccessController: java.lang.Object doPrivileged(java.security.PrivilegedExceptionAction)>") || signature.equals("<java.security.AccessController: java.lang.Object doPrivileged(java.security.PrivilegedAction,java.security.AccessControlContext)>") || signature.equals("<java.security.AccessController: java.lang.Object doPrivileged(java.security.PrivilegedExceptionAction,java.security.AccessControlContext)>")) {
                        Local receiver = (Local)ie.getArg(0);
                        this.addVirtualCallSite(s, m, receiver, (InstanceInvokeExpr)null, this.sigObjRun, Kind.PRIVILEGED);
                     }
                  } else if (!Options.v().ignore_resolution_errors()) {
                     throw new InternalError("Unresolved target " + ie.getMethod() + ". Resolution error should have occured earlier.");
                  }
               }
            }
         }
      }
   }

   private void getImplicitTargets(SootMethod source) {
      SootClass scl = source.getDeclaringClass();
      if (!source.isNative() && !source.isPhantom()) {
         if (source.getSubSignature().indexOf("<init>") >= 0) {
            this.handleInit(source, scl);
         }

         Body b = source.retrieveActiveBody();
         Iterator var4 = b.getUnits().iterator();

         while(true) {
            while(true) {
               Stmt s;
               SootMethod clinit;
               SootClass cl;
               Iterator var19;
               do {
                  if (!var4.hasNext()) {
                     return;
                  }

                  Unit u = (Unit)var4.next();
                  s = (Stmt)u;
                  if (s.containsInvokeExpr()) {
                     InvokeExpr ie = s.getInvokeExpr();
                     SootMethodRef methodRef = ie.getMethodRef();
                     String var9 = methodRef.declaringClass().getName();
                     byte var10 = -1;
                     switch(var9.hashCode()) {
                     case -600300427:
                        if (var9.equals("java.lang.reflect.Constructor")) {
                           var10 = 2;
                        }
                        break;
                     case -530663260:
                        if (var9.equals("java.lang.Class")) {
                           var10 = 1;
                        }
                        break;
                     case 253453190:
                        if (var9.equals("java.lang.reflect.Method")) {
                           var10 = 0;
                        }
                     }

                     switch(var10) {
                     case 0:
                        if (methodRef.getSubSignature().getString().equals("java.lang.Object invoke(java.lang.Object,java.lang.Object[])")) {
                           this.reflectionModel.methodInvoke(source, s);
                        }
                        break;
                     case 1:
                        if (methodRef.getSubSignature().getString().equals("java.lang.Object newInstance()")) {
                           this.reflectionModel.classNewInstance(source, s);
                        }
                        break;
                     case 2:
                        if (methodRef.getSubSignature().getString().equals("java.lang.Object newInstance(java.lang.Object[]))")) {
                           this.reflectionModel.contructorNewInstance(source, s);
                        }
                     }

                     if (methodRef.getSubSignature() == this.sigForName) {
                        this.reflectionModel.classForName(source, s);
                     }

                     if (ie instanceof StaticInvokeExpr) {
                        cl = ie.getMethodRef().declaringClass();
                        var19 = EntryPoints.v().clinitsOf(cl).iterator();

                        while(var19.hasNext()) {
                           clinit = (SootMethod)var19.next();
                           this.addEdge(source, s, clinit, Kind.CLINIT);
                        }
                     }
                  }

                  if (s.containsFieldRef()) {
                     FieldRef fr = s.getFieldRef();
                     if (fr instanceof StaticFieldRef) {
                        SootClass cl = fr.getFieldRef().declaringClass();
                        Iterator var17 = EntryPoints.v().clinitsOf(cl).iterator();

                        while(var17.hasNext()) {
                           SootMethod clinit = (SootMethod)var17.next();
                           this.addEdge(source, s, clinit, Kind.CLINIT);
                        }
                     }
                  }
               } while(!(s instanceof AssignStmt));

               Value rhs = ((AssignStmt)s).getRightOp();
               if (rhs instanceof NewExpr) {
                  NewExpr r = (NewExpr)rhs;
                  cl = r.getBaseType().getSootClass();
                  var19 = EntryPoints.v().clinitsOf(cl).iterator();

                  while(var19.hasNext()) {
                     clinit = (SootMethod)var19.next();
                     this.addEdge(source, s, clinit, Kind.CLINIT);
                  }
               } else if (rhs instanceof NewArrayExpr || rhs instanceof NewMultiArrayExpr) {
                  Type t = rhs.getType();
                  if (t instanceof ArrayType) {
                     t = ((ArrayType)t).baseType;
                  }

                  if (t instanceof RefType) {
                     cl = ((RefType)t).getSootClass();
                     var19 = EntryPoints.v().clinitsOf(cl).iterator();

                     while(var19.hasNext()) {
                        clinit = (SootMethod)var19.next();
                        this.addEdge(source, s, clinit, Kind.CLINIT);
                     }
                  }
               }
            }
         }
      }
   }

   private void processNewMethodContext(MethodOrMethodContext momc) {
      SootMethod m = momc.method();
      Iterator it = this.cicg.edgesOutOf((MethodOrMethodContext)m);

      while(it.hasNext()) {
         Edge e = (Edge)it.next();
         this.cm.addStaticEdge(momc, e.srcUnit(), e.tgt(), e.kind());
      }

   }

   private void handleInit(SootMethod source, SootClass scl) {
      this.addEdge(source, (Stmt)null, scl, this.sigFinalize, Kind.FINALIZE);
   }

   private void constantForName(String cls, SootMethod src, Stmt srcUnit) {
      if (cls.length() > 0 && cls.charAt(0) == '[') {
         if (cls.length() > 1 && cls.charAt(1) == 'L' && cls.charAt(cls.length() - 1) == ';') {
            cls = cls.substring(2, cls.length() - 1);
            this.constantForName(cls, src, srcUnit);
         }
      } else if (!Scene.v().containsClass(cls)) {
         if (this.options.verbose()) {
            logger.warn("Class " + cls + " is a dynamic class, and you did not specify it as such; graph will be incomplete!");
         }
      } else {
         SootClass sootcls = Scene.v().getSootClass(cls);
         if (!sootcls.isPhantomClass()) {
            if (!sootcls.isApplicationClass()) {
               sootcls.setLibraryClass();
            }

            Iterator var5 = EntryPoints.v().clinitsOf(sootcls).iterator();

            while(var5.hasNext()) {
               SootMethod clinit = (SootMethod)var5.next();
               this.addEdge(src, srcUnit, clinit, Kind.CLINIT);
            }
         }
      }

   }

   private void addEdge(SootMethod src, Stmt stmt, SootMethod tgt, Kind kind) {
      this.cicg.addEdge(new Edge(src, stmt, tgt, kind));
   }

   private void addEdge(SootMethod src, Stmt stmt, SootClass cls, NumberedString methodSubSig, Kind kind) {
      SootMethod sm = cls.getMethodUnsafe(methodSubSig);
      if (sm != null) {
         this.addEdge(src, stmt, sm, kind);
      }

   }

   private void addEdge(SootMethod src, Stmt stmt, SootMethod tgt) {
      InvokeExpr ie = stmt.getInvokeExpr();
      this.addEdge(src, stmt, tgt, Edge.ieToKind(ie));
   }

   private abstract class AbstractMethodIterator implements Iterator<SootMethod> {
      private SootMethod next;
      private SootClass currClass;
      private Iterator<SootMethod> methodIterator;

      AbstractMethodIterator(SootClass baseClass) {
         this.currClass = baseClass;
         this.next = null;
         this.methodIterator = baseClass.methodIterator();
         this.findNextMethod();
      }

      protected void findNextMethod() {
         this.next = null;
         if (this.methodIterator != null) {
            while(true) {
               while(this.methodIterator.hasNext()) {
                  SootMethod n = (SootMethod)this.methodIterator.next();
                  if (n.isPublic() && !n.isStatic() && !n.isConstructor() && !n.isStaticInitializer() && n.isConcrete() && this.acceptMethod(n)) {
                     this.next = n;
                     return;
                  }
               }

               if (!this.currClass.hasSuperclass() || this.currClass.getSuperclass().isPhantom() || this.currClass.getSuperclass().getName().equals("java.lang.Object")) {
                  this.methodIterator = null;
                  return;
               }

               this.currClass = this.currClass.getSuperclass();
               this.methodIterator = this.currClass.methodIterator();
            }
         }
      }

      public boolean hasNext() {
         return this.next != null;
      }

      public SootMethod next() {
         SootMethod toRet = this.next;
         this.findNextMethod();
         return toRet;
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      protected abstract boolean acceptMethod(SootMethod var1);
   }

   public class TraceBasedReflectionModel implements ReflectionModel {
      protected Set<OnFlyCallGraphBuilder.TraceBasedReflectionModel.Guard> guards;
      protected ReflectionTraceInfo reflectionInfo;
      private boolean registeredTransformation;

      private TraceBasedReflectionModel() {
         this.registeredTransformation = false;
         this.guards = new HashSet();
         String logFile = OnFlyCallGraphBuilder.this.options.reflection_log();
         if (logFile == null) {
            throw new InternalError("Trace based refection model enabled but no trace file given!?");
         } else {
            this.reflectionInfo = new ReflectionTraceInfo(logFile);
         }
      }

      public void classForName(SootMethod container, Stmt forNameInvokeStmt) {
         Set<String> classNames = this.reflectionInfo.classForNameClassNames(container);
         if (classNames != null && !classNames.isEmpty()) {
            Iterator var4 = classNames.iterator();

            while(var4.hasNext()) {
               String clsName = (String)var4.next();
               OnFlyCallGraphBuilder.this.constantForName(clsName, container, forNameInvokeStmt);
            }
         } else {
            this.registerGuard(container, forNameInvokeStmt, "Class.forName() call site; Soot did not expect this site to be reached");
         }

      }

      public void classNewInstance(SootMethod container, Stmt newInstanceInvokeStmt) {
         Set<String> classNames = this.reflectionInfo.classNewInstanceClassNames(container);
         if (classNames != null && !classNames.isEmpty()) {
            Iterator var4 = classNames.iterator();

            while(var4.hasNext()) {
               String clsName = (String)var4.next();
               SootClass cls = Scene.v().getSootClass(clsName);
               SootMethod constructor = cls.getMethodUnsafe(OnFlyCallGraphBuilder.this.sigInit);
               if (constructor != null) {
                  OnFlyCallGraphBuilder.this.addEdge(container, newInstanceInvokeStmt, constructor, Kind.REFL_CLASS_NEWINSTANCE);
               }
            }
         } else {
            this.registerGuard(container, newInstanceInvokeStmt, "Class.newInstance() call site; Soot did not expect this site to be reached");
         }

      }

      public void contructorNewInstance(SootMethod container, Stmt newInstanceInvokeStmt) {
         Set<String> constructorSignatures = this.reflectionInfo.constructorNewInstanceSignatures(container);
         if (constructorSignatures != null && !constructorSignatures.isEmpty()) {
            Iterator var4 = constructorSignatures.iterator();

            while(var4.hasNext()) {
               String constructorSignature = (String)var4.next();
               SootMethod constructor = Scene.v().getMethod(constructorSignature);
               OnFlyCallGraphBuilder.this.addEdge(container, newInstanceInvokeStmt, constructor, Kind.REFL_CONSTR_NEWINSTANCE);
            }
         } else {
            this.registerGuard(container, newInstanceInvokeStmt, "Constructor.newInstance(..) call site; Soot did not expect this site to be reached");
         }

      }

      public void methodInvoke(SootMethod container, Stmt invokeStmt) {
         Set<String> methodSignatures = this.reflectionInfo.methodInvokeSignatures(container);
         if (methodSignatures != null && !methodSignatures.isEmpty()) {
            Iterator var4 = methodSignatures.iterator();

            while(var4.hasNext()) {
               String methodSignature = (String)var4.next();
               SootMethod method = Scene.v().getMethod(methodSignature);
               OnFlyCallGraphBuilder.this.addEdge(container, invokeStmt, method, Kind.REFL_INVOKE);
            }
         } else {
            this.registerGuard(container, invokeStmt, "Method.invoke(..) call site; Soot did not expect this site to be reached");
         }

      }

      private void registerGuard(SootMethod container, Stmt stmt, String string) {
         this.guards.add(new OnFlyCallGraphBuilder.TraceBasedReflectionModel.Guard(container, stmt, string));
         if (OnFlyCallGraphBuilder.this.options.verbose()) {
            OnFlyCallGraphBuilder.logger.debug("Incomplete trace file: Class.forName() is called in method '" + container + "' but trace contains no information about the receiver class of this call.");
            if (OnFlyCallGraphBuilder.this.options.guards().equals("ignore")) {
               OnFlyCallGraphBuilder.logger.debug("Guarding strategy is set to 'ignore'. Will ignore this problem.");
            } else if (OnFlyCallGraphBuilder.this.options.guards().equals("print")) {
               OnFlyCallGraphBuilder.logger.debug("Guarding strategy is set to 'print'. Program will print a stack trace if this location is reached during execution.");
            } else {
               if (!OnFlyCallGraphBuilder.this.options.guards().equals("throw")) {
                  throw new RuntimeException("Invalid value for phase option (guarding): " + OnFlyCallGraphBuilder.this.options.guards());
               }

               OnFlyCallGraphBuilder.logger.debug("Guarding strategy is set to 'throw'. Program will throw an Error if this location is reached during execution.");
            }
         }

         if (!this.registeredTransformation) {
            this.registeredTransformation = true;
            PackManager.v().getPack("wjap").add(new Transform("wjap.guards", new SceneTransformer() {
               protected void internalTransform(String phaseName, Map<String, String> options) {
                  Iterator var3 = TraceBasedReflectionModel.this.guards.iterator();

                  while(var3.hasNext()) {
                     OnFlyCallGraphBuilder.TraceBasedReflectionModel.Guard g = (OnFlyCallGraphBuilder.TraceBasedReflectionModel.Guard)var3.next();
                     TraceBasedReflectionModel.this.insertGuard(g);
                  }

               }
            }));
            PhaseOptions.v().setPhaseOption("wjap.guards", "enabled");
         }

      }

      private void insertGuard(OnFlyCallGraphBuilder.TraceBasedReflectionModel.Guard guard) {
         if (!OnFlyCallGraphBuilder.this.options.guards().equals("ignore")) {
            SootMethod container = guard.container;
            Stmt insertionPoint = guard.stmt;
            if (!container.hasActiveBody()) {
               OnFlyCallGraphBuilder.logger.warn("Tried to insert guard into " + container + " but couldn't because method has no body.");
            } else {
               Body body = container.getActiveBody();
               RefType runtimeExceptionType = RefType.v("java.lang.Error");
               NewExpr newExpr = Jimple.v().newNewExpr(runtimeExceptionType);
               LocalGenerator lg = new LocalGenerator(body);
               Local exceptionLocal = lg.generateLocal(runtimeExceptionType);
               AssignStmt assignStmt = Jimple.v().newAssignStmt(exceptionLocal, newExpr);
               body.getUnits().insertBefore((Unit)assignStmt, (Unit)insertionPoint);
               SootMethodRef cref = runtimeExceptionType.getSootClass().getMethod("<init>", Collections.singletonList(RefType.v("java.lang.String"))).makeRef();
               SpecialInvokeExpr constructorInvokeExpr = Jimple.v().newSpecialInvokeExpr(exceptionLocal, cref, (Value)StringConstant.v(guard.message));
               InvokeStmt initStmt = Jimple.v().newInvokeStmt(constructorInvokeExpr);
               body.getUnits().insertAfter((Unit)initStmt, (Unit)assignStmt);
               if (OnFlyCallGraphBuilder.this.options.guards().equals("print")) {
                  VirtualInvokeExpr printStackTraceExpr = Jimple.v().newVirtualInvokeExpr(exceptionLocal, Scene.v().getSootClass("java.lang.Throwable").getMethod("printStackTrace", Collections.emptyList()).makeRef());
                  InvokeStmt printStackTraceStmt = Jimple.v().newInvokeStmt(printStackTraceExpr);
                  body.getUnits().insertAfter((Unit)printStackTraceStmt, (Unit)initStmt);
               } else {
                  if (!OnFlyCallGraphBuilder.this.options.guards().equals("throw")) {
                     throw new RuntimeException("Invalid value for phase option (guarding): " + OnFlyCallGraphBuilder.this.options.guards());
                  }

                  body.getUnits().insertAfter((Unit)Jimple.v().newThrowStmt(exceptionLocal), (Unit)initStmt);
               }
            }

         }
      }

      // $FF: synthetic method
      TraceBasedReflectionModel(Object x1) {
         this();
      }

      class Guard {
         final SootMethod container;
         final Stmt stmt;
         final String message;

         public Guard(SootMethod container, Stmt stmt, String message) {
            this.container = container;
            this.stmt = stmt;
            this.message = message;
         }
      }
   }

   public class TypeBasedReflectionModel extends OnFlyCallGraphBuilder.DefaultReflectionModel {
      public TypeBasedReflectionModel() {
         super();
      }

      public void methodInvoke(SootMethod container, Stmt invokeStmt) {
         if (container.getDeclaringClass().isJavaLibraryClass()) {
            super.methodInvoke(container, invokeStmt);
         } else {
            InstanceInvokeExpr d = (InstanceInvokeExpr)invokeStmt.getInvokeExpr();
            Value base = d.getArg(0);
            if (!(base instanceof Local)) {
               super.methodInvoke(container, invokeStmt);
            } else {
               OnFlyCallGraphBuilder.this.addInvokeCallSite(invokeStmt, container, d);
            }
         }
      }
   }

   public class DefaultReflectionModel implements ReflectionModel {
      protected CGOptions options = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));
      protected HashSet<SootMethod> warnedAlready = new HashSet();

      public void classForName(SootMethod source, Stmt s) {
         List<Local> stringConstants = (List)OnFlyCallGraphBuilder.this.methodToStringConstants.get(source);
         if (stringConstants == null) {
            OnFlyCallGraphBuilder.this.methodToStringConstants.put(source, stringConstants = new ArrayList());
         }

         InvokeExpr ie = s.getInvokeExpr();
         Value className = ie.getArg(0);
         if (className instanceof StringConstant) {
            String clsx = ((StringConstant)className).value;
            OnFlyCallGraphBuilder.this.constantForName(clsx, source, s);
         } else if (className instanceof Local) {
            Local constant = (Local)className;
            Iterator var7;
            if (this.options.safe_forname()) {
               var7 = EntryPoints.v().clinits().iterator();

               while(var7.hasNext()) {
                  SootMethod tgt = (SootMethod)var7.next();
                  OnFlyCallGraphBuilder.this.addEdge(source, s, tgt, Kind.CLINIT);
               }
            } else {
               var7 = Scene.v().dynamicClasses().iterator();

               while(var7.hasNext()) {
                  SootClass cls = (SootClass)var7.next();
                  Iterator var9 = EntryPoints.v().clinitsOf(cls).iterator();

                  while(var9.hasNext()) {
                     SootMethod clinit = (SootMethod)var9.next();
                     OnFlyCallGraphBuilder.this.addEdge(source, s, clinit, Kind.CLINIT);
                  }
               }

               VirtualCallSite site = new VirtualCallSite(s, source, (InstanceInvokeExpr)null, (NumberedString)null, Kind.CLINIT);
               List<VirtualCallSite> sites = (List)OnFlyCallGraphBuilder.this.stringConstToSites.get(constant);
               if (sites == null) {
                  OnFlyCallGraphBuilder.this.stringConstToSites.put(constant, sites = new ArrayList());
                  ((List)stringConstants).add(constant);
               }

               ((List)sites).add(site);
            }
         }

      }

      public void classNewInstance(SootMethod source, Stmt s) {
         Iterator var3;
         if (this.options.safe_newinstance()) {
            var3 = EntryPoints.v().inits().iterator();

            while(var3.hasNext()) {
               SootMethod tgt = (SootMethod)var3.next();
               OnFlyCallGraphBuilder.this.addEdge(source, s, tgt, Kind.NEWINSTANCE);
            }
         } else {
            var3 = Scene.v().dynamicClasses().iterator();

            while(var3.hasNext()) {
               SootClass cls = (SootClass)var3.next();
               SootMethod sm = cls.getMethodUnsafe(OnFlyCallGraphBuilder.this.sigInit);
               if (sm != null) {
                  OnFlyCallGraphBuilder.this.addEdge(source, s, sm, Kind.NEWINSTANCE);
               }
            }

            if (this.options.verbose()) {
               OnFlyCallGraphBuilder.logger.warn("Method " + source + " is reachable, and calls Class.newInstance; graph will be incomplete! Use safe-newinstance option for a conservative result.");
            }
         }

      }

      public void contructorNewInstance(SootMethod source, Stmt s) {
         Iterator var3;
         if (this.options.safe_newinstance()) {
            var3 = EntryPoints.v().allInits().iterator();

            while(var3.hasNext()) {
               SootMethod tgt = (SootMethod)var3.next();
               OnFlyCallGraphBuilder.this.addEdge(source, s, tgt, Kind.NEWINSTANCE);
            }
         } else {
            var3 = Scene.v().dynamicClasses().iterator();

            while(var3.hasNext()) {
               SootClass cls = (SootClass)var3.next();
               Iterator var5 = cls.getMethods().iterator();

               while(var5.hasNext()) {
                  SootMethod m = (SootMethod)var5.next();
                  if (m.getName().equals("<init>")) {
                     OnFlyCallGraphBuilder.this.addEdge(source, s, m, Kind.NEWINSTANCE);
                  }
               }
            }

            if (this.options.verbose()) {
               OnFlyCallGraphBuilder.logger.warn("Method " + source + " is reachable, and calls Constructor.newInstance; graph will be incomplete! Use safe-newinstance option for a conservative result.");
            }
         }

      }

      public void methodInvoke(SootMethod container, Stmt invokeStmt) {
         if (!this.warnedAlready(container)) {
            if (this.options.verbose()) {
               OnFlyCallGraphBuilder.logger.warn("call to java.lang.reflect.Method: invoke() from " + container + "; graph will be incomplete!");
            }

            this.markWarned(container);
         }

      }

      private void markWarned(SootMethod m) {
         this.warnedAlready.add(m);
      }

      private boolean warnedAlready(SootMethod m) {
         return this.warnedAlready.contains(m);
      }
   }
}
