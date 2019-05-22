package soot;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.coffi.Utf8_Enumeration;
import soot.dava.internal.SET.SETBasicBlock;
import soot.dava.internal.SET.SETNode;
import soot.dexpler.DalvikThrowAnalysis;
import soot.jimple.spark.pag.MethodPAG;
import soot.jimple.spark.pag.Parm;
import soot.jimple.spark.sets.P2SetFactory;
import soot.jimple.toolkits.annotation.arraycheck.Array2ndDimensionSymbol;
import soot.jimple.toolkits.pointer.UnionFactory;
import soot.jimple.toolkits.pointer.util.NativeHelper;
import soot.jimple.toolkits.typing.ClassHierarchy;
import soot.toolkits.astmetrics.ClassData;
import soot.toolkits.scalar.Pair;

public class G extends Singletons {
   private static G.GlobalObjectGetter objectGetter = new G.GlobalObjectGetter() {
      private G instance = new G();

      public G getG() {
         return this.instance;
      }

      public void reset() {
         this.instance = new G();
      }
   };
   /** @deprecated */
   @Deprecated
   public PrintStream out;
   public long coffi_BasicBlock_ids;
   public Utf8_Enumeration coffi_CONSTANT_Utf8_info_e1;
   public Utf8_Enumeration coffi_CONSTANT_Utf8_info_e2;
   public int SETNodeLabel_uniqueId;
   public HashMap<SETNode, SETBasicBlock> SETBasicBlock_binding;
   public boolean ASTAnalysis_modified;
   public NativeHelper NativeHelper_helper;
   public P2SetFactory newSetFactory;
   public P2SetFactory oldSetFactory;
   public Map<Pair<SootMethod, Integer>, Parm> Parm_pairToElement;
   public int SparkNativeHelper_tempVar;
   public int PaddleNativeHelper_tempVar;
   public boolean PointsToSetInternal_warnedAlready;
   public HashMap<SootMethod, MethodPAG> MethodPAG_methodToPag;
   public Set MethodRWSet_allGlobals;
   public Set MethodRWSet_allFields;
   public int GeneralConstObject_counter;
   public UnionFactory Union_factory;
   public HashMap<Object, Array2ndDimensionSymbol> Array2ndDimensionSymbol_pool;
   public List<Timer> Timer_outstandingTimers;
   public boolean Timer_isGarbageCollecting;
   public Timer Timer_forcedGarbageCollectionTimer;
   public int Timer_count;
   public final Map<Scene, ClassHierarchy> ClassHierarchy_classHierarchyMap;
   public final Map<MethodContext, MethodContext> MethodContext_map;
   public DalvikThrowAnalysis interproceduralDalvikThrowAnalysis;
   public boolean ASTTransformations_modified;
   public boolean ASTIfElseFlipped;
   public boolean SootMethodAddedByDava;
   public ArrayList<SootClass> SootClassNeedsDavaSuperHandlerClass;
   public ArrayList<SootMethod> SootMethodsAdded;
   public ArrayList<ClassData> ASTMetricsData;

   public G() {
      this.out = System.out;
      this.coffi_BasicBlock_ids = 0L;
      this.coffi_CONSTANT_Utf8_info_e1 = new Utf8_Enumeration();
      this.coffi_CONSTANT_Utf8_info_e2 = new Utf8_Enumeration();
      this.SETNodeLabel_uniqueId = 0;
      this.SETBasicBlock_binding = new HashMap();
      this.NativeHelper_helper = null;
      this.Parm_pairToElement = new HashMap();
      this.SparkNativeHelper_tempVar = 0;
      this.PaddleNativeHelper_tempVar = 0;
      this.PointsToSetInternal_warnedAlready = false;
      this.MethodPAG_methodToPag = new HashMap();
      this.MethodRWSet_allGlobals = new HashSet();
      this.MethodRWSet_allFields = new HashSet();
      this.GeneralConstObject_counter = 0;
      this.Union_factory = null;
      this.Array2ndDimensionSymbol_pool = new HashMap();
      this.Timer_outstandingTimers = new ArrayList();
      this.Timer_forcedGarbageCollectionTimer = new Timer("gc");
      this.ClassHierarchy_classHierarchyMap = new HashMap();
      this.MethodContext_map = new HashMap();
      this.interproceduralDalvikThrowAnalysis = null;
      this.SootClassNeedsDavaSuperHandlerClass = new ArrayList();
      this.SootMethodsAdded = new ArrayList();
      this.ASTMetricsData = new ArrayList();
   }

   public static G v() {
      return objectGetter.getG();
   }

   public static void reset() {
      objectGetter.reset();
   }

   public static void setGlobalObjectGetter(G.GlobalObjectGetter newGetter) {
      objectGetter = newGetter;
   }

   public DalvikThrowAnalysis interproceduralDalvikThrowAnalysis() {
      if (this.interproceduralDalvikThrowAnalysis == null) {
         this.interproceduralDalvikThrowAnalysis = new DalvikThrowAnalysis(this.g, true);
      }

      return this.interproceduralDalvikThrowAnalysis;
   }

   public void resetSpark() {
      Method[] var1 = this.getClass().getSuperclass().getDeclaredMethods();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Method m = var1[var3];
         if (m.getName().startsWith("release_soot_jimple_spark_")) {
            try {
               m.invoke(this);
            } catch (IllegalAccessException var6) {
               throw new RuntimeException(var6);
            } catch (IllegalArgumentException var7) {
               throw new RuntimeException(var7);
            } catch (InvocationTargetException var8) {
               throw new RuntimeException(var8);
            }
         }
      }

      this.MethodPAG_methodToPag.clear();
      this.MethodRWSet_allFields.clear();
      this.MethodRWSet_allGlobals.clear();
      this.newSetFactory = null;
      this.oldSetFactory = null;
      this.Parm_pairToElement.clear();
      this.release_soot_jimple_toolkits_callgraph_VirtualCalls();
   }

   public class Global {
   }

   public interface GlobalObjectGetter {
      G getG();

      void reset();
   }
}
