package soot;

import soot.baf.Baf;
import soot.baf.DoubleWordType;
import soot.baf.WordType;
import soot.baf.toolkits.base.LoadStoreOptimizer;
import soot.baf.toolkits.base.PeepholeOptimizer;
import soot.baf.toolkits.base.StoreChainOptimizer;
import soot.coffi.CONSTANT_Utf8_collector;
import soot.coffi.Double2ndHalfType;
import soot.coffi.Long2ndHalfType;
import soot.coffi.UnusuableType;
import soot.coffi.Util;
import soot.dava.Dava;
import soot.dava.DavaPrinter;
import soot.dava.DavaStaticBlockCleaner;
import soot.dava.toolkits.base.AST.ASTWalker;
import soot.dava.toolkits.base.AST.TryContentsFinder;
import soot.dava.toolkits.base.AST.UselessTryRemover;
import soot.dava.toolkits.base.AST.transformations.UselessLabelFinder;
import soot.dava.toolkits.base.AST.traversals.ClosestAbruptTargetFinder;
import soot.dava.toolkits.base.finders.AbruptEdgeFinder;
import soot.dava.toolkits.base.finders.CycleFinder;
import soot.dava.toolkits.base.finders.ExceptionFinder;
import soot.dava.toolkits.base.finders.IfFinder;
import soot.dava.toolkits.base.finders.LabeledBlockFinder;
import soot.dava.toolkits.base.finders.SequenceFinder;
import soot.dava.toolkits.base.finders.SwitchFinder;
import soot.dava.toolkits.base.finders.SynchronizedBlockFinder;
import soot.dava.toolkits.base.misc.MonitorConverter;
import soot.dava.toolkits.base.misc.PackageNamer;
import soot.dava.toolkits.base.misc.ThrowFinder;
import soot.dava.toolkits.base.misc.ThrowNullConverter;
import soot.dexpler.DalvikThrowAnalysis;
import soot.dexpler.DexFileProvider;
import soot.dexpler.DexResolver;
import soot.dexpler.TrapMinimizer;
import soot.grimp.Grimp;
import soot.grimp.toolkits.base.ConstructorFolder;
import soot.javaToJimple.InitialResolver;
import soot.javaToJimple.toolkits.CondTransformer;
import soot.jbco.jimpleTransformations.ClassRenamer;
import soot.jbco.jimpleTransformations.MethodRenamer;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.paddle.PaddleHook;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.spark.fieldrw.FieldReadTagAggregator;
import soot.jimple.spark.fieldrw.FieldTagAggregator;
import soot.jimple.spark.fieldrw.FieldTagger;
import soot.jimple.spark.fieldrw.FieldWriteTagAggregator;
import soot.jimple.spark.internal.CompleteAccessibility;
import soot.jimple.spark.internal.PublicAndProtectedAccessibility;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.sets.AllSharedHybridNodes;
import soot.jimple.spark.sets.AllSharedListNodes;
import soot.jimple.spark.sets.EmptyPointsToSet;
import soot.jimple.toolkits.annotation.AvailExprTagger;
import soot.jimple.toolkits.annotation.DominatorsTagger;
import soot.jimple.toolkits.annotation.LineNumberAdder;
import soot.jimple.toolkits.annotation.arraycheck.ArrayBoundsChecker;
import soot.jimple.toolkits.annotation.arraycheck.ClassFieldAnalysis;
import soot.jimple.toolkits.annotation.arraycheck.RectangularArrayFinder;
import soot.jimple.toolkits.annotation.callgraph.CallGraphGrapher;
import soot.jimple.toolkits.annotation.callgraph.CallGraphTagger;
import soot.jimple.toolkits.annotation.defs.ReachingDefsTagger;
import soot.jimple.toolkits.annotation.fields.UnreachableFieldsTagger;
import soot.jimple.toolkits.annotation.j5anno.AnnotationGenerator;
import soot.jimple.toolkits.annotation.liveness.LiveVarsTagger;
import soot.jimple.toolkits.annotation.logic.LoopInvariantFinder;
import soot.jimple.toolkits.annotation.methods.UnreachableMethodsTagger;
import soot.jimple.toolkits.annotation.nullcheck.NullPointerChecker;
import soot.jimple.toolkits.annotation.nullcheck.NullPointerColorer;
import soot.jimple.toolkits.annotation.parity.ParityTagger;
import soot.jimple.toolkits.annotation.profiling.ProfilingGenerator;
import soot.jimple.toolkits.annotation.purity.PurityAnalysis;
import soot.jimple.toolkits.annotation.qualifiers.TightestQualifiersTagger;
import soot.jimple.toolkits.annotation.tags.ArrayNullTagAggregator;
import soot.jimple.toolkits.base.Aggregator;
import soot.jimple.toolkits.base.RenameDuplicatedClasses;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.VirtualCalls;
import soot.jimple.toolkits.graph.CriticalEdgeRemover;
import soot.jimple.toolkits.invoke.StaticInliner;
import soot.jimple.toolkits.invoke.StaticMethodBinder;
import soot.jimple.toolkits.invoke.SynchronizerManager;
import soot.jimple.toolkits.pointer.CastCheckEliminatorDumper;
import soot.jimple.toolkits.pointer.DependenceTagAggregator;
import soot.jimple.toolkits.pointer.DumbPointerAnalysis;
import soot.jimple.toolkits.pointer.FieldRWTagger;
import soot.jimple.toolkits.pointer.FullObjectSet;
import soot.jimple.toolkits.pointer.ParameterAliasTagger;
import soot.jimple.toolkits.pointer.SideEffectTagger;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.TypeConstants;
import soot.jimple.toolkits.reflection.ConstantInvokeMethodBaseTransformer;
import soot.jimple.toolkits.scalar.CommonSubexpressionEliminator;
import soot.jimple.toolkits.scalar.ConditionalBranchFolder;
import soot.jimple.toolkits.scalar.ConstantCastEliminator;
import soot.jimple.toolkits.scalar.ConstantPropagatorAndFolder;
import soot.jimple.toolkits.scalar.CopyPropagator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.EmptySwitchEliminator;
import soot.jimple.toolkits.scalar.FieldStaticnessCorrector;
import soot.jimple.toolkits.scalar.IdentityCastEliminator;
import soot.jimple.toolkits.scalar.IdentityOperationEliminator;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.jimple.toolkits.scalar.MethodStaticnessCorrector;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.jimple.toolkits.scalar.UnconditionalBranchFolder;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.jimple.toolkits.scalar.pre.BusyCodeMotion;
import soot.jimple.toolkits.scalar.pre.LazyCodeMotion;
import soot.jimple.toolkits.thread.mhp.MhpTransformer;
import soot.jimple.toolkits.thread.synchronization.LockAllocator;
import soot.jimple.toolkits.typing.TypeAssigner;
import soot.jimple.toolkits.typing.fast.BottomType;
import soot.jimple.toolkits.typing.fast.Integer127Type;
import soot.jimple.toolkits.typing.fast.Integer1Type;
import soot.jimple.toolkits.typing.fast.Integer32767Type;
import soot.jimple.toolkits.typing.integer.ClassHierarchy;
import soot.options.Options;
import soot.shimple.Shimple;
import soot.shimple.ShimpleTransformer;
import soot.shimple.toolkits.scalar.SConstantPropagatorAndFolder;
import soot.sootify.TemplatePrinter;
import soot.tagkit.InnerClassTagAggregator;
import soot.tagkit.LineNumberTagAggregator;
import soot.tagkit.TagManager;
import soot.toDex.FastDexTrapTightener;
import soot.toDex.SynchronizedMethodTransformer;
import soot.toDex.TrapSplitter;
import soot.toolkits.exceptions.DuplicateCatchAllTrapRemover;
import soot.toolkits.exceptions.PedanticThrowAnalysis;
import soot.toolkits.exceptions.ThrowableSet;
import soot.toolkits.exceptions.TrapTightener;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.toolkits.graph.SlowPseudoTopologicalOrderer;
import soot.toolkits.graph.interaction.InteractionHandler;
import soot.toolkits.scalar.LocalPacker;
import soot.toolkits.scalar.LocalSplitter;
import soot.toolkits.scalar.SmartLocalDefsPool;
import soot.toolkits.scalar.UnusedLocalEliminator;
import soot.util.PhaseDumper;
import soot.util.SharedBitSetCache;
import soot.util.cfgcmd.AltClassLoader;
import soot.xml.XMLPrinter;

public class Singletons {
   protected Singletons.Global g = new Singletons.Global();
   private PhaseOptions instance_soot_PhaseOptions;
   private VirtualCalls instance_soot_jimple_toolkits_callgraph_VirtualCalls;
   private FieldTagger instance_soot_jimple_spark_fieldrw_FieldTagger;
   private SharedBitSetCache instance_soot_util_SharedBitSetCache;
   private Options instance_soot_options_Options;
   private CHATransformer instance_soot_jimple_toolkits_callgraph_CHATransformer;
   private SlowPseudoTopologicalOrderer instance_soot_toolkits_graph_SlowPseudoTopologicalOrderer;
   private SynchronizerManager instance_soot_jimple_toolkits_invoke_SynchronizerManager;
   private ClassHierarchy instance_soot_jimple_toolkits_typing_integer_ClassHierarchy;
   private Main instance_soot_Main;
   private TagManager instance_soot_tagkit_TagManager;
   private Environment instance_soot_jimple_toolkits_pointer_representations_Environment;
   private TypeConstants instance_soot_jimple_toolkits_pointer_representations_TypeConstants;
   private Util instance_soot_coffi_Util;
   private SourceLocator instance_soot_SourceLocator;
   private CONSTANT_Utf8_collector instance_soot_coffi_CONSTANT_Utf8_collector;
   private AbruptEdgeFinder instance_soot_dava_toolkits_base_finders_AbruptEdgeFinder;
   private Aggregator instance_soot_jimple_toolkits_base_Aggregator;
   private ArrayBoundsChecker instance_soot_jimple_toolkits_annotation_arraycheck_ArrayBoundsChecker;
   private ArrayElement instance_soot_jimple_spark_pag_ArrayElement;
   private ArrayNullTagAggregator instance_soot_jimple_toolkits_annotation_tags_ArrayNullTagAggregator;
   private ASTWalker instance_soot_dava_toolkits_base_AST_ASTWalker;
   private Baf instance_soot_baf_Baf;
   private BooleanType instance_soot_BooleanType;
   private BusyCodeMotion instance_soot_jimple_toolkits_scalar_pre_BusyCodeMotion;
   private ByteType instance_soot_ByteType;
   private CastCheckEliminatorDumper instance_soot_jimple_toolkits_pointer_CastCheckEliminatorDumper;
   private CharType instance_soot_CharType;
   private ClassFieldAnalysis instance_soot_jimple_toolkits_annotation_arraycheck_ClassFieldAnalysis;
   private CommonSubexpressionEliminator instance_soot_jimple_toolkits_scalar_CommonSubexpressionEliminator;
   private ConditionalBranchFolder instance_soot_jimple_toolkits_scalar_ConditionalBranchFolder;
   private ConstantPropagatorAndFolder instance_soot_jimple_toolkits_scalar_ConstantPropagatorAndFolder;
   private ConstructorFolder instance_soot_grimp_toolkits_base_ConstructorFolder;
   private CopyPropagator instance_soot_jimple_toolkits_scalar_CopyPropagator;
   private CriticalEdgeRemover instance_soot_jimple_toolkits_graph_CriticalEdgeRemover;
   private CycleFinder instance_soot_dava_toolkits_base_finders_CycleFinder;
   private Dava instance_soot_dava_Dava;
   private DavaPrinter instance_soot_dava_DavaPrinter;
   private XMLPrinter instance_soot_xml_XMLPrinter;
   private Printer instance_soot_Printer;
   private DeadAssignmentEliminator instance_soot_jimple_toolkits_scalar_DeadAssignmentEliminator;
   private DependenceTagAggregator instance_soot_jimple_toolkits_pointer_DependenceTagAggregator;
   private Double2ndHalfType instance_soot_coffi_Double2ndHalfType;
   private DoubleType instance_soot_DoubleType;
   private DoubleWordType instance_soot_baf_DoubleWordType;
   private DumbPointerAnalysis instance_soot_jimple_toolkits_pointer_DumbPointerAnalysis;
   private EmptyPointsToSet instance_soot_jimple_spark_sets_EmptyPointsToSet;
   private ErroneousType instance_soot_ErroneousType;
   private ExceptionFinder instance_soot_dava_toolkits_base_finders_ExceptionFinder;
   private FieldRWTagger instance_soot_jimple_toolkits_pointer_FieldRWTagger;
   private FloatType instance_soot_FloatType;
   private FullObjectSet instance_soot_jimple_toolkits_pointer_FullObjectSet;
   private Grimp instance_soot_grimp_Grimp;
   private IfFinder instance_soot_dava_toolkits_base_finders_IfFinder;
   private IntType instance_soot_IntType;
   private Jimple instance_soot_jimple_Jimple;
   private LabeledBlockFinder instance_soot_dava_toolkits_base_finders_LabeledBlockFinder;
   private LazyCodeMotion instance_soot_jimple_toolkits_scalar_pre_LazyCodeMotion;
   private LineNumberTagAggregator instance_soot_tagkit_LineNumberTagAggregator;
   private InnerClassTagAggregator instance_soot_tagkit_InnerClassTagAggregator;
   private LineNumberAdder instance_soot_jimple_toolkits_annotation_LineNumberAdder;
   private LoadStoreOptimizer instance_soot_baf_toolkits_base_LoadStoreOptimizer;
   private StoreChainOptimizer instance_soot_baf_toolkits_base_StoreChainOptimizer;
   private LocalNameStandardizer instance_soot_jimple_toolkits_scalar_LocalNameStandardizer;
   private LocalPacker instance_soot_toolkits_scalar_LocalPacker;
   private LocalSplitter instance_soot_toolkits_scalar_LocalSplitter;
   private Long2ndHalfType instance_soot_coffi_Long2ndHalfType;
   private LongType instance_soot_LongType;
   private MonitorConverter instance_soot_dava_toolkits_base_misc_MonitorConverter;
   private NopEliminator instance_soot_jimple_toolkits_scalar_NopEliminator;
   private NullConstant instance_soot_jimple_NullConstant;
   private NullPointerChecker instance_soot_jimple_toolkits_annotation_nullcheck_NullPointerChecker;
   private NullType instance_soot_NullType;
   private PackageNamer instance_soot_dava_toolkits_base_misc_PackageNamer;
   private PackManager instance_soot_PackManager;
   private PeepholeOptimizer instance_soot_baf_toolkits_base_PeepholeOptimizer;
   private ProfilingGenerator instance_soot_jimple_toolkits_annotation_profiling_ProfilingGenerator;
   private RectangularArrayFinder instance_soot_jimple_toolkits_annotation_arraycheck_RectangularArrayFinder;
   private RefType instance_soot_RefType;
   private Scene instance_soot_Scene;
   private SequenceFinder instance_soot_dava_toolkits_base_finders_SequenceFinder;
   private Shimple instance_soot_shimple_Shimple;
   private ShimpleTransformer instance_soot_shimple_ShimpleTransformer;
   private SConstantPropagatorAndFolder instance_soot_shimple_toolkits_scalar_SConstantPropagatorAndFolder;
   private ShortType instance_soot_ShortType;
   private SideEffectTagger instance_soot_jimple_toolkits_pointer_SideEffectTagger;
   private SparkTransformer instance_soot_jimple_spark_SparkTransformer;
   private StaticInliner instance_soot_jimple_toolkits_invoke_StaticInliner;
   private StaticMethodBinder instance_soot_jimple_toolkits_invoke_StaticMethodBinder;
   private StmtAddressType instance_soot_StmtAddressType;
   private SwitchFinder instance_soot_dava_toolkits_base_finders_SwitchFinder;
   private SynchronizedBlockFinder instance_soot_dava_toolkits_base_finders_SynchronizedBlockFinder;
   private ThrowFinder instance_soot_dava_toolkits_base_misc_ThrowFinder;
   private ThrowNullConverter instance_soot_dava_toolkits_base_misc_ThrowNullConverter;
   private Timers instance_soot_Timers;
   private TryContentsFinder instance_soot_dava_toolkits_base_AST_TryContentsFinder;
   private TypeAssigner instance_soot_jimple_toolkits_typing_TypeAssigner;
   private UnconditionalBranchFolder instance_soot_jimple_toolkits_scalar_UnconditionalBranchFolder;
   private ConstantCastEliminator instance_soot_jimple_toolkits_scalar_ConstantCastEliminator;
   private IdentityCastEliminator instance_soot_jimple_toolkits_scalar_IdentityCastEliminator;
   private FieldStaticnessCorrector instance_soot_jimple_toolkits_scalar_FieldStaticnessCorrector;
   private MethodStaticnessCorrector instance_soot_jimple_toolkits_scalar_MethodStaticnessCorrector;
   private IdentityOperationEliminator instance_soot_jimple_toolkits_scalar_IdentityOperationEliminator;
   private UnknownType instance_soot_UnknownType;
   private UnreachableCodeEliminator instance_soot_jimple_toolkits_scalar_UnreachableCodeEliminator;
   private UnusedLocalEliminator instance_soot_toolkits_scalar_UnusedLocalEliminator;
   private UnusuableType instance_soot_coffi_UnusuableType;
   private UselessTryRemover instance_soot_dava_toolkits_base_AST_UselessTryRemover;
   private VoidType instance_soot_VoidType;
   private WordType instance_soot_baf_WordType;
   private FieldReadTagAggregator instance_soot_jimple_spark_fieldrw_FieldReadTagAggregator;
   private FieldWriteTagAggregator instance_soot_jimple_spark_fieldrw_FieldWriteTagAggregator;
   private FieldTagAggregator instance_soot_jimple_spark_fieldrw_FieldTagAggregator;
   private EntryPoints instance_soot_EntryPoints;
   private CallGraphTagger instance_soot_jimple_toolkits_annotation_callgraph_CallGraphTagger;
   private NullPointerColorer instance_soot_jimple_toolkits_annotation_nullcheck_NullPointerColorer;
   private ParityTagger instance_soot_jimple_toolkits_annotation_parity_ParityTagger;
   private UnreachableMethodsTagger instance_soot_jimple_toolkits_annotation_methods_UnreachableMethodsTagger;
   private UnreachableFieldsTagger instance_soot_jimple_toolkits_annotation_fields_UnreachableFieldsTagger;
   private TightestQualifiersTagger instance_soot_jimple_toolkits_annotation_qualifiers_TightestQualifiersTagger;
   private ParameterAliasTagger instance_soot_jimple_toolkits_pointer_ParameterAliasTagger;
   private ReachingDefsTagger instance_soot_jimple_toolkits_annotation_defs_ReachingDefsTagger;
   private LiveVarsTagger instance_soot_jimple_toolkits_annotation_liveness_LiveVarsTagger;
   private InteractionHandler instance_soot_toolkits_graph_interaction_InteractionHandler;
   private LoopInvariantFinder instance_soot_jimple_toolkits_annotation_logic_LoopInvariantFinder;
   private AvailExprTagger instance_soot_jimple_toolkits_annotation_AvailExprTagger;
   private PhaseDumper instance_soot_util_PhaseDumper;
   private AltClassLoader instance_soot_util_cfgcmd_AltClassLoader;
   private ThrowableSet.Manager instance_soot_toolkits_exceptions_ThrowableSet_Manager;
   private UnitThrowAnalysis instance_soot_toolkits_exceptions_UnitThrowAnalysis;
   private DalvikThrowAnalysis instance_soot_dexpler_DalvikThrowAnalysis;
   private DexFileProvider instance_soot_dexpler_DexFileProvider;
   private PedanticThrowAnalysis instance_soot_toolkits_exceptions_PedanticThrowAnalysis;
   private TrapTightener instance_soot_toolkits_exceptions_TrapTightener;
   private DuplicateCatchAllTrapRemover instance_soot_toolkits_exceptions_DuplicateCatchAllTrapRemover;
   private CallGraphGrapher instance_soot_jimple_toolkits_annotation_callgraph_CallGraphGrapher;
   private SootResolver instance_soot_SootResolver;
   private InitialResolver instance_soot_javaToJimple_InitialResolver;
   private PaddleHook instance_soot_jimple_paddle_PaddleHook;
   private DominatorsTagger instance_soot_jimple_toolkits_annotation_DominatorsTagger;
   private PurityAnalysis instance_soot_jimple_toolkits_annotation_purity_PurityAnalysis;
   private CondTransformer instance_soot_javaToJimple_toolkits_CondTransformer;
   private UselessLabelFinder instance_soot_dava_toolkits_base_AST_transformations_UselessLabelFinder;
   private DavaStaticBlockCleaner instance_soot_dava_DavaStaticBlockCleaner;
   private ClosestAbruptTargetFinder instance_soot_dava_toolkits_base_AST_traversals_ClosestAbruptTargetFinder;
   private AllSharedListNodes instance_soot_jimple_spark_sets_AllSharedListNodes;
   private AllSharedHybridNodes instance_soot_jimple_spark_sets_AllSharedHybridNodes;
   private LockAllocator instance_soot_jimple_toolkits_thread_synchronization_LockAllocator;
   private MhpTransformer instance_soot_jimple_toolkits_thread_mhp_MhpTransformer;
   private JastAddInitialResolver instance_soot_JastAddInitialResolver;
   private AnnotationGenerator instance_soot_jimple_toolkits_annotation_j5anno_AnnotationGenerator;
   private TemplatePrinter instance_soot_sootify_TemplatePrinter;
   private DexResolver instance_soot_dexpler_DexResolver;
   private EmptySwitchEliminator instance_soot_jimple_toolkits_scalar_EmptySwitchEliminator;
   private SynchronizedMethodTransformer instance_soot_toDex_SynchronizedMethodTransformer;
   private TrapSplitter instance_soot_toDex_TrapSplitter;
   private FastDexTrapTightener instance_soot_toDex_FastDexTrapTightener;
   private RenameDuplicatedClasses instance_soot_jimple_toolkits_base_RenameDuplicatedClasses;
   private Integer127Type instance_soot_jimple_toolkits_typing_fast_Integer127Type;
   private Integer1Type instance_soot_jimple_toolkits_typing_fast_Integer1Type;
   private Integer32767Type instance_soot_jimple_toolkits_typing_fast_Integer32767Type;
   private BottomType instance_soot_jimple_toolkits_typing_fast_BottomType;
   private TrapMinimizer instance_soot_dexpler_TrapMinimizer;
   private SmartLocalDefsPool instance_soot_toolkits_scalar_SmartLocalDefsPool;
   private PublicAndProtectedAccessibility instance_soot_jimple_spark_internal_PublicAndProtectedAccessibility;
   private CompleteAccessibility instance_soot_jimple_spark_internal_CompleteAccessibility;
   private ConstantInvokeMethodBaseTransformer instance_soot_jimple_toolkits_reflection_ConstantInvokeMethodBaseTransformer;
   private ClassRenamer instance_soot_jbco_jimpleTransformations_ClassRenamer;
   private MethodRenamer instance_soot_jbco_jimpleTransformations_MethodRenamer;

   public PhaseOptions soot_PhaseOptions() {
      if (this.instance_soot_PhaseOptions == null) {
         synchronized(this) {
            if (this.instance_soot_PhaseOptions == null) {
               this.instance_soot_PhaseOptions = new PhaseOptions(this.g);
            }
         }
      }

      return this.instance_soot_PhaseOptions;
   }

   protected void release_soot_PhaseOptions() {
      this.instance_soot_PhaseOptions = null;
   }

   public VirtualCalls soot_jimple_toolkits_callgraph_VirtualCalls() {
      if (this.instance_soot_jimple_toolkits_callgraph_VirtualCalls == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_callgraph_VirtualCalls == null) {
               this.instance_soot_jimple_toolkits_callgraph_VirtualCalls = new VirtualCalls(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_callgraph_VirtualCalls;
   }

   protected void release_soot_jimple_toolkits_callgraph_VirtualCalls() {
      this.instance_soot_jimple_toolkits_callgraph_VirtualCalls = null;
   }

   public FieldTagger soot_jimple_spark_fieldrw_FieldTagger() {
      if (this.instance_soot_jimple_spark_fieldrw_FieldTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_spark_fieldrw_FieldTagger == null) {
               this.instance_soot_jimple_spark_fieldrw_FieldTagger = new FieldTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_spark_fieldrw_FieldTagger;
   }

   protected void release_soot_jimple_spark_fieldrw_FieldTagger() {
      this.instance_soot_jimple_spark_fieldrw_FieldTagger = null;
   }

   public SharedBitSetCache soot_util_SharedBitSetCache() {
      if (this.instance_soot_util_SharedBitSetCache == null) {
         synchronized(this) {
            if (this.instance_soot_util_SharedBitSetCache == null) {
               this.instance_soot_util_SharedBitSetCache = new SharedBitSetCache(this.g);
            }
         }
      }

      return this.instance_soot_util_SharedBitSetCache;
   }

   protected void release_soot_util_SharedBitSetCache() {
      this.instance_soot_util_SharedBitSetCache = null;
   }

   public Options soot_options_Options() {
      if (this.instance_soot_options_Options == null) {
         synchronized(this) {
            if (this.instance_soot_options_Options == null) {
               this.instance_soot_options_Options = new Options(this.g);
            }
         }
      }

      return this.instance_soot_options_Options;
   }

   protected void release_soot_options_Options() {
      this.instance_soot_options_Options = null;
   }

   public CHATransformer soot_jimple_toolkits_callgraph_CHATransformer() {
      if (this.instance_soot_jimple_toolkits_callgraph_CHATransformer == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_callgraph_CHATransformer == null) {
               this.instance_soot_jimple_toolkits_callgraph_CHATransformer = new CHATransformer(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_callgraph_CHATransformer;
   }

   protected void release_soot_jimple_toolkits_callgraph_CHATransformer() {
      this.instance_soot_jimple_toolkits_callgraph_CHATransformer = null;
   }

   public SlowPseudoTopologicalOrderer soot_toolkits_graph_SlowPseudoTopologicalOrderer() {
      if (this.instance_soot_toolkits_graph_SlowPseudoTopologicalOrderer == null) {
         synchronized(this) {
            if (this.instance_soot_toolkits_graph_SlowPseudoTopologicalOrderer == null) {
               this.instance_soot_toolkits_graph_SlowPseudoTopologicalOrderer = new SlowPseudoTopologicalOrderer(this.g);
            }
         }
      }

      return this.instance_soot_toolkits_graph_SlowPseudoTopologicalOrderer;
   }

   protected void release_soot_toolkits_graph_SlowPseudoTopologicalOrderer() {
      this.instance_soot_toolkits_graph_SlowPseudoTopologicalOrderer = null;
   }

   public SynchronizerManager soot_jimple_toolkits_invoke_SynchronizerManager() {
      if (this.instance_soot_jimple_toolkits_invoke_SynchronizerManager == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_invoke_SynchronizerManager == null) {
               this.instance_soot_jimple_toolkits_invoke_SynchronizerManager = new SynchronizerManager(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_invoke_SynchronizerManager;
   }

   protected void release_soot_jimple_toolkits_invoke_SynchronizerManager() {
      this.instance_soot_jimple_toolkits_invoke_SynchronizerManager = null;
   }

   public ClassHierarchy soot_jimple_toolkits_typing_integer_ClassHierarchy() {
      if (this.instance_soot_jimple_toolkits_typing_integer_ClassHierarchy == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_typing_integer_ClassHierarchy == null) {
               this.instance_soot_jimple_toolkits_typing_integer_ClassHierarchy = new ClassHierarchy(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_typing_integer_ClassHierarchy;
   }

   protected void release_soot_jimple_toolkits_typing_integer_ClassHierarchy() {
      this.instance_soot_jimple_toolkits_typing_integer_ClassHierarchy = null;
   }

   public Main soot_Main() {
      if (this.instance_soot_Main == null) {
         synchronized(this) {
            if (this.instance_soot_Main == null) {
               this.instance_soot_Main = new Main(this.g);
            }
         }
      }

      return this.instance_soot_Main;
   }

   protected void release_soot_Main() {
      this.instance_soot_Main = null;
   }

   public TagManager soot_tagkit_TagManager() {
      if (this.instance_soot_tagkit_TagManager == null) {
         synchronized(this) {
            if (this.instance_soot_tagkit_TagManager == null) {
               this.instance_soot_tagkit_TagManager = new TagManager(this.g);
            }
         }
      }

      return this.instance_soot_tagkit_TagManager;
   }

   protected void release_soot_tagkit_TagManager() {
      this.instance_soot_tagkit_TagManager = null;
   }

   public Environment soot_jimple_toolkits_pointer_representations_Environment() {
      if (this.instance_soot_jimple_toolkits_pointer_representations_Environment == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_pointer_representations_Environment == null) {
               this.instance_soot_jimple_toolkits_pointer_representations_Environment = new Environment(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_pointer_representations_Environment;
   }

   protected void release_soot_jimple_toolkits_pointer_representations_Environment() {
      this.instance_soot_jimple_toolkits_pointer_representations_Environment = null;
   }

   public TypeConstants soot_jimple_toolkits_pointer_representations_TypeConstants() {
      if (this.instance_soot_jimple_toolkits_pointer_representations_TypeConstants == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_pointer_representations_TypeConstants == null) {
               this.instance_soot_jimple_toolkits_pointer_representations_TypeConstants = new TypeConstants(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_pointer_representations_TypeConstants;
   }

   protected void release_soot_jimple_toolkits_pointer_representations_TypeConstants() {
      this.instance_soot_jimple_toolkits_pointer_representations_TypeConstants = null;
   }

   public Util soot_coffi_Util() {
      if (this.instance_soot_coffi_Util == null) {
         synchronized(this) {
            if (this.instance_soot_coffi_Util == null) {
               this.instance_soot_coffi_Util = new Util(this.g);
            }
         }
      }

      return this.instance_soot_coffi_Util;
   }

   protected void release_soot_coffi_Util() {
      this.instance_soot_coffi_Util = null;
   }

   public SourceLocator soot_SourceLocator() {
      if (this.instance_soot_SourceLocator == null) {
         synchronized(this) {
            if (this.instance_soot_SourceLocator == null) {
               this.instance_soot_SourceLocator = new SourceLocator(this.g);
            }
         }
      }

      return this.instance_soot_SourceLocator;
   }

   protected void release_soot_SourceLocator() {
      this.instance_soot_SourceLocator = null;
   }

   public CONSTANT_Utf8_collector soot_coffi_CONSTANT_Utf8_collector() {
      if (this.instance_soot_coffi_CONSTANT_Utf8_collector == null) {
         synchronized(this) {
            if (this.instance_soot_coffi_CONSTANT_Utf8_collector == null) {
               this.instance_soot_coffi_CONSTANT_Utf8_collector = new CONSTANT_Utf8_collector(this.g);
            }
         }
      }

      return this.instance_soot_coffi_CONSTANT_Utf8_collector;
   }

   protected void release_soot_coffi_CONSTANT_Utf8_collector() {
      this.instance_soot_coffi_CONSTANT_Utf8_collector = null;
   }

   public AbruptEdgeFinder soot_dava_toolkits_base_finders_AbruptEdgeFinder() {
      if (this.instance_soot_dava_toolkits_base_finders_AbruptEdgeFinder == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_finders_AbruptEdgeFinder == null) {
               this.instance_soot_dava_toolkits_base_finders_AbruptEdgeFinder = new AbruptEdgeFinder(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_finders_AbruptEdgeFinder;
   }

   protected void release_soot_dava_toolkits_base_finders_AbruptEdgeFinder() {
      this.instance_soot_dava_toolkits_base_finders_AbruptEdgeFinder = null;
   }

   public Aggregator soot_jimple_toolkits_base_Aggregator() {
      if (this.instance_soot_jimple_toolkits_base_Aggregator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_base_Aggregator == null) {
               this.instance_soot_jimple_toolkits_base_Aggregator = new Aggregator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_base_Aggregator;
   }

   protected void release_soot_jimple_toolkits_base_Aggregator() {
      this.instance_soot_jimple_toolkits_base_Aggregator = null;
   }

   public ArrayBoundsChecker soot_jimple_toolkits_annotation_arraycheck_ArrayBoundsChecker() {
      if (this.instance_soot_jimple_toolkits_annotation_arraycheck_ArrayBoundsChecker == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_arraycheck_ArrayBoundsChecker == null) {
               this.instance_soot_jimple_toolkits_annotation_arraycheck_ArrayBoundsChecker = new ArrayBoundsChecker(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_arraycheck_ArrayBoundsChecker;
   }

   protected void release_soot_jimple_toolkits_annotation_arraycheck_ArrayBoundsChecker() {
      this.instance_soot_jimple_toolkits_annotation_arraycheck_ArrayBoundsChecker = null;
   }

   public ArrayElement soot_jimple_spark_pag_ArrayElement() {
      if (this.instance_soot_jimple_spark_pag_ArrayElement == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_spark_pag_ArrayElement == null) {
               this.instance_soot_jimple_spark_pag_ArrayElement = new ArrayElement(this.g);
            }
         }
      }

      return this.instance_soot_jimple_spark_pag_ArrayElement;
   }

   protected void release_soot_jimple_spark_pag_ArrayElement() {
      this.instance_soot_jimple_spark_pag_ArrayElement = null;
   }

   public ArrayNullTagAggregator soot_jimple_toolkits_annotation_tags_ArrayNullTagAggregator() {
      if (this.instance_soot_jimple_toolkits_annotation_tags_ArrayNullTagAggregator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_tags_ArrayNullTagAggregator == null) {
               this.instance_soot_jimple_toolkits_annotation_tags_ArrayNullTagAggregator = new ArrayNullTagAggregator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_tags_ArrayNullTagAggregator;
   }

   protected void release_soot_jimple_toolkits_annotation_tags_ArrayNullTagAggregator() {
      this.instance_soot_jimple_toolkits_annotation_tags_ArrayNullTagAggregator = null;
   }

   public ASTWalker soot_dava_toolkits_base_AST_ASTWalker() {
      if (this.instance_soot_dava_toolkits_base_AST_ASTWalker == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_AST_ASTWalker == null) {
               this.instance_soot_dava_toolkits_base_AST_ASTWalker = new ASTWalker(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_AST_ASTWalker;
   }

   protected void release_soot_dava_toolkits_base_AST_ASTWalker() {
      this.instance_soot_dava_toolkits_base_AST_ASTWalker = null;
   }

   public Baf soot_baf_Baf() {
      if (this.instance_soot_baf_Baf == null) {
         synchronized(this) {
            if (this.instance_soot_baf_Baf == null) {
               this.instance_soot_baf_Baf = new Baf(this.g);
            }
         }
      }

      return this.instance_soot_baf_Baf;
   }

   protected void release_soot_baf_Baf() {
      this.instance_soot_baf_Baf = null;
   }

   public BooleanType soot_BooleanType() {
      if (this.instance_soot_BooleanType == null) {
         synchronized(this) {
            if (this.instance_soot_BooleanType == null) {
               this.instance_soot_BooleanType = new BooleanType(this.g);
            }
         }
      }

      return this.instance_soot_BooleanType;
   }

   protected void release_soot_BooleanType() {
      this.instance_soot_BooleanType = null;
   }

   public BusyCodeMotion soot_jimple_toolkits_scalar_pre_BusyCodeMotion() {
      if (this.instance_soot_jimple_toolkits_scalar_pre_BusyCodeMotion == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_pre_BusyCodeMotion == null) {
               this.instance_soot_jimple_toolkits_scalar_pre_BusyCodeMotion = new BusyCodeMotion(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_pre_BusyCodeMotion;
   }

   protected void release_soot_jimple_toolkits_scalar_pre_BusyCodeMotion() {
      this.instance_soot_jimple_toolkits_scalar_pre_BusyCodeMotion = null;
   }

   public ByteType soot_ByteType() {
      if (this.instance_soot_ByteType == null) {
         synchronized(this) {
            if (this.instance_soot_ByteType == null) {
               this.instance_soot_ByteType = new ByteType(this.g);
            }
         }
      }

      return this.instance_soot_ByteType;
   }

   protected void release_soot_ByteType() {
      this.instance_soot_ByteType = null;
   }

   public CastCheckEliminatorDumper soot_jimple_toolkits_pointer_CastCheckEliminatorDumper() {
      if (this.instance_soot_jimple_toolkits_pointer_CastCheckEliminatorDumper == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_pointer_CastCheckEliminatorDumper == null) {
               this.instance_soot_jimple_toolkits_pointer_CastCheckEliminatorDumper = new CastCheckEliminatorDumper(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_pointer_CastCheckEliminatorDumper;
   }

   protected void release_soot_jimple_toolkits_pointer_CastCheckEliminatorDumper() {
      this.instance_soot_jimple_toolkits_pointer_CastCheckEliminatorDumper = null;
   }

   public CharType soot_CharType() {
      if (this.instance_soot_CharType == null) {
         synchronized(this) {
            if (this.instance_soot_CharType == null) {
               this.instance_soot_CharType = new CharType(this.g);
            }
         }
      }

      return this.instance_soot_CharType;
   }

   protected void release_soot_CharType() {
      this.instance_soot_CharType = null;
   }

   public ClassFieldAnalysis soot_jimple_toolkits_annotation_arraycheck_ClassFieldAnalysis() {
      if (this.instance_soot_jimple_toolkits_annotation_arraycheck_ClassFieldAnalysis == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_arraycheck_ClassFieldAnalysis == null) {
               this.instance_soot_jimple_toolkits_annotation_arraycheck_ClassFieldAnalysis = new ClassFieldAnalysis(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_arraycheck_ClassFieldAnalysis;
   }

   protected void release_soot_jimple_toolkits_annotation_arraycheck_ClassFieldAnalysis() {
      this.instance_soot_jimple_toolkits_annotation_arraycheck_ClassFieldAnalysis = null;
   }

   public CommonSubexpressionEliminator soot_jimple_toolkits_scalar_CommonSubexpressionEliminator() {
      if (this.instance_soot_jimple_toolkits_scalar_CommonSubexpressionEliminator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_CommonSubexpressionEliminator == null) {
               this.instance_soot_jimple_toolkits_scalar_CommonSubexpressionEliminator = new CommonSubexpressionEliminator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_CommonSubexpressionEliminator;
   }

   protected void release_soot_jimple_toolkits_scalar_CommonSubexpressionEliminator() {
      this.instance_soot_jimple_toolkits_scalar_CommonSubexpressionEliminator = null;
   }

   public ConditionalBranchFolder soot_jimple_toolkits_scalar_ConditionalBranchFolder() {
      if (this.instance_soot_jimple_toolkits_scalar_ConditionalBranchFolder == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_ConditionalBranchFolder == null) {
               this.instance_soot_jimple_toolkits_scalar_ConditionalBranchFolder = new ConditionalBranchFolder(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_ConditionalBranchFolder;
   }

   protected void release_soot_jimple_toolkits_scalar_ConditionalBranchFolder() {
      this.instance_soot_jimple_toolkits_scalar_ConditionalBranchFolder = null;
   }

   public ConstantPropagatorAndFolder soot_jimple_toolkits_scalar_ConstantPropagatorAndFolder() {
      if (this.instance_soot_jimple_toolkits_scalar_ConstantPropagatorAndFolder == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_ConstantPropagatorAndFolder == null) {
               this.instance_soot_jimple_toolkits_scalar_ConstantPropagatorAndFolder = new ConstantPropagatorAndFolder(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_ConstantPropagatorAndFolder;
   }

   protected void release_soot_jimple_toolkits_scalar_ConstantPropagatorAndFolder() {
      this.instance_soot_jimple_toolkits_scalar_ConstantPropagatorAndFolder = null;
   }

   public ConstructorFolder soot_grimp_toolkits_base_ConstructorFolder() {
      if (this.instance_soot_grimp_toolkits_base_ConstructorFolder == null) {
         synchronized(this) {
            if (this.instance_soot_grimp_toolkits_base_ConstructorFolder == null) {
               this.instance_soot_grimp_toolkits_base_ConstructorFolder = new ConstructorFolder(this.g);
            }
         }
      }

      return this.instance_soot_grimp_toolkits_base_ConstructorFolder;
   }

   protected void release_soot_grimp_toolkits_base_ConstructorFolder() {
      this.instance_soot_grimp_toolkits_base_ConstructorFolder = null;
   }

   public CopyPropagator soot_jimple_toolkits_scalar_CopyPropagator() {
      if (this.instance_soot_jimple_toolkits_scalar_CopyPropagator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_CopyPropagator == null) {
               this.instance_soot_jimple_toolkits_scalar_CopyPropagator = new CopyPropagator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_CopyPropagator;
   }

   protected void release_soot_jimple_toolkits_scalar_CopyPropagator() {
      this.instance_soot_jimple_toolkits_scalar_CopyPropagator = null;
   }

   public CriticalEdgeRemover soot_jimple_toolkits_graph_CriticalEdgeRemover() {
      if (this.instance_soot_jimple_toolkits_graph_CriticalEdgeRemover == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_graph_CriticalEdgeRemover == null) {
               this.instance_soot_jimple_toolkits_graph_CriticalEdgeRemover = new CriticalEdgeRemover(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_graph_CriticalEdgeRemover;
   }

   protected void release_soot_jimple_toolkits_graph_CriticalEdgeRemover() {
      this.instance_soot_jimple_toolkits_graph_CriticalEdgeRemover = null;
   }

   public CycleFinder soot_dava_toolkits_base_finders_CycleFinder() {
      if (this.instance_soot_dava_toolkits_base_finders_CycleFinder == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_finders_CycleFinder == null) {
               this.instance_soot_dava_toolkits_base_finders_CycleFinder = new CycleFinder(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_finders_CycleFinder;
   }

   protected void release_soot_dava_toolkits_base_finders_CycleFinder() {
      this.instance_soot_dava_toolkits_base_finders_CycleFinder = null;
   }

   public Dava soot_dava_Dava() {
      if (this.instance_soot_dava_Dava == null) {
         synchronized(this) {
            if (this.instance_soot_dava_Dava == null) {
               this.instance_soot_dava_Dava = new Dava(this.g);
            }
         }
      }

      return this.instance_soot_dava_Dava;
   }

   protected void release_soot_dava_Dava() {
      this.instance_soot_dava_Dava = null;
   }

   public DavaPrinter soot_dava_DavaPrinter() {
      if (this.instance_soot_dava_DavaPrinter == null) {
         synchronized(this) {
            if (this.instance_soot_dava_DavaPrinter == null) {
               this.instance_soot_dava_DavaPrinter = new DavaPrinter(this.g);
            }
         }
      }

      return this.instance_soot_dava_DavaPrinter;
   }

   protected void release_soot_dava_DavaPrinter() {
      this.instance_soot_dava_DavaPrinter = null;
   }

   public XMLPrinter soot_xml_XMLPrinter() {
      if (this.instance_soot_xml_XMLPrinter == null) {
         synchronized(this) {
            if (this.instance_soot_xml_XMLPrinter == null) {
               this.instance_soot_xml_XMLPrinter = new XMLPrinter(this.g);
            }
         }
      }

      return this.instance_soot_xml_XMLPrinter;
   }

   protected void release_soot_xml_XMLPrinter() {
      this.instance_soot_xml_XMLPrinter = null;
   }

   public Printer soot_Printer() {
      if (this.instance_soot_Printer == null) {
         synchronized(this) {
            if (this.instance_soot_Printer == null) {
               this.instance_soot_Printer = new Printer(this.g);
            }
         }
      }

      return this.instance_soot_Printer;
   }

   protected void release_soot_Printer() {
      this.instance_soot_Printer = null;
   }

   public DeadAssignmentEliminator soot_jimple_toolkits_scalar_DeadAssignmentEliminator() {
      if (this.instance_soot_jimple_toolkits_scalar_DeadAssignmentEliminator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_DeadAssignmentEliminator == null) {
               this.instance_soot_jimple_toolkits_scalar_DeadAssignmentEliminator = new DeadAssignmentEliminator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_DeadAssignmentEliminator;
   }

   protected void release_soot_jimple_toolkits_scalar_DeadAssignmentEliminator() {
      this.instance_soot_jimple_toolkits_scalar_DeadAssignmentEliminator = null;
   }

   public DependenceTagAggregator soot_jimple_toolkits_pointer_DependenceTagAggregator() {
      if (this.instance_soot_jimple_toolkits_pointer_DependenceTagAggregator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_pointer_DependenceTagAggregator == null) {
               this.instance_soot_jimple_toolkits_pointer_DependenceTagAggregator = new DependenceTagAggregator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_pointer_DependenceTagAggregator;
   }

   protected void release_soot_jimple_toolkits_pointer_DependenceTagAggregator() {
      this.instance_soot_jimple_toolkits_pointer_DependenceTagAggregator = null;
   }

   public Double2ndHalfType soot_coffi_Double2ndHalfType() {
      if (this.instance_soot_coffi_Double2ndHalfType == null) {
         synchronized(this) {
            if (this.instance_soot_coffi_Double2ndHalfType == null) {
               this.instance_soot_coffi_Double2ndHalfType = new Double2ndHalfType(this.g);
            }
         }
      }

      return this.instance_soot_coffi_Double2ndHalfType;
   }

   protected void release_soot_coffi_Double2ndHalfType() {
      this.instance_soot_coffi_Double2ndHalfType = null;
   }

   public DoubleType soot_DoubleType() {
      if (this.instance_soot_DoubleType == null) {
         synchronized(this) {
            if (this.instance_soot_DoubleType == null) {
               this.instance_soot_DoubleType = new DoubleType(this.g);
            }
         }
      }

      return this.instance_soot_DoubleType;
   }

   protected void release_soot_DoubleType() {
      this.instance_soot_DoubleType = null;
   }

   public DoubleWordType soot_baf_DoubleWordType() {
      if (this.instance_soot_baf_DoubleWordType == null) {
         synchronized(this) {
            if (this.instance_soot_baf_DoubleWordType == null) {
               this.instance_soot_baf_DoubleWordType = new DoubleWordType(this.g);
            }
         }
      }

      return this.instance_soot_baf_DoubleWordType;
   }

   protected void release_soot_baf_DoubleWordType() {
      this.instance_soot_baf_DoubleWordType = null;
   }

   public DumbPointerAnalysis soot_jimple_toolkits_pointer_DumbPointerAnalysis() {
      if (this.instance_soot_jimple_toolkits_pointer_DumbPointerAnalysis == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_pointer_DumbPointerAnalysis == null) {
               this.instance_soot_jimple_toolkits_pointer_DumbPointerAnalysis = new DumbPointerAnalysis(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_pointer_DumbPointerAnalysis;
   }

   protected void release_soot_jimple_toolkits_pointer_DumbPointerAnalysis() {
      this.instance_soot_jimple_toolkits_pointer_DumbPointerAnalysis = null;
   }

   public EmptyPointsToSet soot_jimple_spark_sets_EmptyPointsToSet() {
      if (this.instance_soot_jimple_spark_sets_EmptyPointsToSet == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_spark_sets_EmptyPointsToSet == null) {
               this.instance_soot_jimple_spark_sets_EmptyPointsToSet = new EmptyPointsToSet(this.g);
            }
         }
      }

      return this.instance_soot_jimple_spark_sets_EmptyPointsToSet;
   }

   protected void release_soot_jimple_spark_sets_EmptyPointsToSet() {
      this.instance_soot_jimple_spark_sets_EmptyPointsToSet = null;
   }

   public ErroneousType soot_ErroneousType() {
      if (this.instance_soot_ErroneousType == null) {
         synchronized(this) {
            if (this.instance_soot_ErroneousType == null) {
               this.instance_soot_ErroneousType = new ErroneousType(this.g);
            }
         }
      }

      return this.instance_soot_ErroneousType;
   }

   protected void release_soot_ErroneousType() {
      this.instance_soot_ErroneousType = null;
   }

   public ExceptionFinder soot_dava_toolkits_base_finders_ExceptionFinder() {
      if (this.instance_soot_dava_toolkits_base_finders_ExceptionFinder == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_finders_ExceptionFinder == null) {
               this.instance_soot_dava_toolkits_base_finders_ExceptionFinder = new ExceptionFinder(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_finders_ExceptionFinder;
   }

   protected void release_soot_dava_toolkits_base_finders_ExceptionFinder() {
      this.instance_soot_dava_toolkits_base_finders_ExceptionFinder = null;
   }

   public FieldRWTagger soot_jimple_toolkits_pointer_FieldRWTagger() {
      if (this.instance_soot_jimple_toolkits_pointer_FieldRWTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_pointer_FieldRWTagger == null) {
               this.instance_soot_jimple_toolkits_pointer_FieldRWTagger = new FieldRWTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_pointer_FieldRWTagger;
   }

   protected void release_soot_jimple_toolkits_pointer_FieldRWTagger() {
      this.instance_soot_jimple_toolkits_pointer_FieldRWTagger = null;
   }

   public FloatType soot_FloatType() {
      if (this.instance_soot_FloatType == null) {
         synchronized(this) {
            if (this.instance_soot_FloatType == null) {
               this.instance_soot_FloatType = new FloatType(this.g);
            }
         }
      }

      return this.instance_soot_FloatType;
   }

   protected void release_soot_FloatType() {
      this.instance_soot_FloatType = null;
   }

   public FullObjectSet soot_jimple_toolkits_pointer_FullObjectSet() {
      if (this.instance_soot_jimple_toolkits_pointer_FullObjectSet == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_pointer_FullObjectSet == null) {
               this.instance_soot_jimple_toolkits_pointer_FullObjectSet = new FullObjectSet(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_pointer_FullObjectSet;
   }

   protected void release_soot_jimple_toolkits_pointer_FullObjectSet() {
      this.instance_soot_jimple_toolkits_pointer_FullObjectSet = null;
   }

   public Grimp soot_grimp_Grimp() {
      if (this.instance_soot_grimp_Grimp == null) {
         synchronized(this) {
            if (this.instance_soot_grimp_Grimp == null) {
               this.instance_soot_grimp_Grimp = new Grimp(this.g);
            }
         }
      }

      return this.instance_soot_grimp_Grimp;
   }

   protected void release_soot_grimp_Grimp() {
      this.instance_soot_grimp_Grimp = null;
   }

   public IfFinder soot_dava_toolkits_base_finders_IfFinder() {
      if (this.instance_soot_dava_toolkits_base_finders_IfFinder == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_finders_IfFinder == null) {
               this.instance_soot_dava_toolkits_base_finders_IfFinder = new IfFinder(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_finders_IfFinder;
   }

   protected void release_soot_dava_toolkits_base_finders_IfFinder() {
      this.instance_soot_dava_toolkits_base_finders_IfFinder = null;
   }

   public IntType soot_IntType() {
      if (this.instance_soot_IntType == null) {
         synchronized(this) {
            if (this.instance_soot_IntType == null) {
               this.instance_soot_IntType = new IntType(this.g);
            }
         }
      }

      return this.instance_soot_IntType;
   }

   protected void release_soot_IntType() {
      this.instance_soot_IntType = null;
   }

   public Jimple soot_jimple_Jimple() {
      if (this.instance_soot_jimple_Jimple == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_Jimple == null) {
               this.instance_soot_jimple_Jimple = new Jimple(this.g);
            }
         }
      }

      return this.instance_soot_jimple_Jimple;
   }

   protected void release_soot_jimple_Jimple() {
      this.instance_soot_jimple_Jimple = null;
   }

   public LabeledBlockFinder soot_dava_toolkits_base_finders_LabeledBlockFinder() {
      if (this.instance_soot_dava_toolkits_base_finders_LabeledBlockFinder == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_finders_LabeledBlockFinder == null) {
               this.instance_soot_dava_toolkits_base_finders_LabeledBlockFinder = new LabeledBlockFinder(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_finders_LabeledBlockFinder;
   }

   protected void release_soot_dava_toolkits_base_finders_LabeledBlockFinder() {
      this.instance_soot_dava_toolkits_base_finders_LabeledBlockFinder = null;
   }

   public LazyCodeMotion soot_jimple_toolkits_scalar_pre_LazyCodeMotion() {
      if (this.instance_soot_jimple_toolkits_scalar_pre_LazyCodeMotion == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_pre_LazyCodeMotion == null) {
               this.instance_soot_jimple_toolkits_scalar_pre_LazyCodeMotion = new LazyCodeMotion(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_pre_LazyCodeMotion;
   }

   protected void release_soot_jimple_toolkits_scalar_pre_LazyCodeMotion() {
      this.instance_soot_jimple_toolkits_scalar_pre_LazyCodeMotion = null;
   }

   public LineNumberTagAggregator soot_tagkit_LineNumberTagAggregator() {
      if (this.instance_soot_tagkit_LineNumberTagAggregator == null) {
         synchronized(this) {
            if (this.instance_soot_tagkit_LineNumberTagAggregator == null) {
               this.instance_soot_tagkit_LineNumberTagAggregator = new LineNumberTagAggregator(this.g);
            }
         }
      }

      return this.instance_soot_tagkit_LineNumberTagAggregator;
   }

   protected void release_soot_tagkit_LineNumberTagAggregator() {
      this.instance_soot_tagkit_LineNumberTagAggregator = null;
   }

   public InnerClassTagAggregator soot_tagkit_InnerClassTagAggregator() {
      if (this.instance_soot_tagkit_InnerClassTagAggregator == null) {
         synchronized(this) {
            if (this.instance_soot_tagkit_InnerClassTagAggregator == null) {
               this.instance_soot_tagkit_InnerClassTagAggregator = new InnerClassTagAggregator(this.g);
            }
         }
      }

      return this.instance_soot_tagkit_InnerClassTagAggregator;
   }

   protected void release_soot_tagkit_InnerClassTagAggregator() {
      this.instance_soot_tagkit_InnerClassTagAggregator = null;
   }

   public LineNumberAdder soot_jimple_toolkits_annotation_LineNumberAdder() {
      if (this.instance_soot_jimple_toolkits_annotation_LineNumberAdder == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_LineNumberAdder == null) {
               this.instance_soot_jimple_toolkits_annotation_LineNumberAdder = new LineNumberAdder(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_LineNumberAdder;
   }

   protected void release_soot_jimple_toolkits_annotation_LineNumberAdder() {
      this.instance_soot_jimple_toolkits_annotation_LineNumberAdder = null;
   }

   public LoadStoreOptimizer soot_baf_toolkits_base_LoadStoreOptimizer() {
      if (this.instance_soot_baf_toolkits_base_LoadStoreOptimizer == null) {
         synchronized(this) {
            if (this.instance_soot_baf_toolkits_base_LoadStoreOptimizer == null) {
               this.instance_soot_baf_toolkits_base_LoadStoreOptimizer = new LoadStoreOptimizer(this.g);
            }
         }
      }

      return this.instance_soot_baf_toolkits_base_LoadStoreOptimizer;
   }

   protected void release_soot_baf_toolkits_base_LoadStoreOptimizer() {
      this.instance_soot_baf_toolkits_base_LoadStoreOptimizer = null;
   }

   public StoreChainOptimizer soot_baf_toolkits_base_StoreChainOptimizer() {
      if (this.instance_soot_baf_toolkits_base_StoreChainOptimizer == null) {
         synchronized(this) {
            if (this.instance_soot_baf_toolkits_base_StoreChainOptimizer == null) {
               this.instance_soot_baf_toolkits_base_StoreChainOptimizer = new StoreChainOptimizer(this.g);
            }
         }
      }

      return this.instance_soot_baf_toolkits_base_StoreChainOptimizer;
   }

   protected void release_soot_baf_toolkits_base_StoreChainOptimizer() {
      this.instance_soot_baf_toolkits_base_StoreChainOptimizer = null;
   }

   public LocalNameStandardizer soot_jimple_toolkits_scalar_LocalNameStandardizer() {
      if (this.instance_soot_jimple_toolkits_scalar_LocalNameStandardizer == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_LocalNameStandardizer == null) {
               this.instance_soot_jimple_toolkits_scalar_LocalNameStandardizer = new LocalNameStandardizer(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_LocalNameStandardizer;
   }

   protected void release_soot_jimple_toolkits_scalar_LocalNameStandardizer() {
      this.instance_soot_jimple_toolkits_scalar_LocalNameStandardizer = null;
   }

   public LocalPacker soot_toolkits_scalar_LocalPacker() {
      if (this.instance_soot_toolkits_scalar_LocalPacker == null) {
         synchronized(this) {
            if (this.instance_soot_toolkits_scalar_LocalPacker == null) {
               this.instance_soot_toolkits_scalar_LocalPacker = new LocalPacker(this.g);
            }
         }
      }

      return this.instance_soot_toolkits_scalar_LocalPacker;
   }

   protected void release_soot_toolkits_scalar_LocalPacker() {
      this.instance_soot_toolkits_scalar_LocalPacker = null;
   }

   public LocalSplitter soot_toolkits_scalar_LocalSplitter() {
      if (this.instance_soot_toolkits_scalar_LocalSplitter == null) {
         synchronized(this) {
            if (this.instance_soot_toolkits_scalar_LocalSplitter == null) {
               this.instance_soot_toolkits_scalar_LocalSplitter = new LocalSplitter(this.g);
            }
         }
      }

      return this.instance_soot_toolkits_scalar_LocalSplitter;
   }

   protected void release_soot_toolkits_scalar_LocalSplitter() {
      this.instance_soot_toolkits_scalar_LocalSplitter = null;
   }

   public Long2ndHalfType soot_coffi_Long2ndHalfType() {
      if (this.instance_soot_coffi_Long2ndHalfType == null) {
         synchronized(this) {
            if (this.instance_soot_coffi_Long2ndHalfType == null) {
               this.instance_soot_coffi_Long2ndHalfType = new Long2ndHalfType(this.g);
            }
         }
      }

      return this.instance_soot_coffi_Long2ndHalfType;
   }

   protected void release_soot_coffi_Long2ndHalfType() {
      this.instance_soot_coffi_Long2ndHalfType = null;
   }

   public LongType soot_LongType() {
      if (this.instance_soot_LongType == null) {
         synchronized(this) {
            if (this.instance_soot_LongType == null) {
               this.instance_soot_LongType = new LongType(this.g);
            }
         }
      }

      return this.instance_soot_LongType;
   }

   protected void release_soot_LongType() {
      this.instance_soot_LongType = null;
   }

   public MonitorConverter soot_dava_toolkits_base_misc_MonitorConverter() {
      if (this.instance_soot_dava_toolkits_base_misc_MonitorConverter == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_misc_MonitorConverter == null) {
               this.instance_soot_dava_toolkits_base_misc_MonitorConverter = new MonitorConverter(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_misc_MonitorConverter;
   }

   protected void release_soot_dava_toolkits_base_misc_MonitorConverter() {
      this.instance_soot_dava_toolkits_base_misc_MonitorConverter = null;
   }

   public NopEliminator soot_jimple_toolkits_scalar_NopEliminator() {
      if (this.instance_soot_jimple_toolkits_scalar_NopEliminator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_NopEliminator == null) {
               this.instance_soot_jimple_toolkits_scalar_NopEliminator = new NopEliminator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_NopEliminator;
   }

   protected void release_soot_jimple_toolkits_scalar_NopEliminator() {
      this.instance_soot_jimple_toolkits_scalar_NopEliminator = null;
   }

   public NullConstant soot_jimple_NullConstant() {
      if (this.instance_soot_jimple_NullConstant == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_NullConstant == null) {
               this.instance_soot_jimple_NullConstant = new NullConstant(this.g);
            }
         }
      }

      return this.instance_soot_jimple_NullConstant;
   }

   protected void release_soot_jimple_NullConstant() {
      this.instance_soot_jimple_NullConstant = null;
   }

   public NullPointerChecker soot_jimple_toolkits_annotation_nullcheck_NullPointerChecker() {
      if (this.instance_soot_jimple_toolkits_annotation_nullcheck_NullPointerChecker == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_nullcheck_NullPointerChecker == null) {
               this.instance_soot_jimple_toolkits_annotation_nullcheck_NullPointerChecker = new NullPointerChecker(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_nullcheck_NullPointerChecker;
   }

   protected void release_soot_jimple_toolkits_annotation_nullcheck_NullPointerChecker() {
      this.instance_soot_jimple_toolkits_annotation_nullcheck_NullPointerChecker = null;
   }

   public NullType soot_NullType() {
      if (this.instance_soot_NullType == null) {
         synchronized(this) {
            if (this.instance_soot_NullType == null) {
               this.instance_soot_NullType = new NullType(this.g);
            }
         }
      }

      return this.instance_soot_NullType;
   }

   protected void release_soot_NullType() {
      this.instance_soot_NullType = null;
   }

   public PackageNamer soot_dava_toolkits_base_misc_PackageNamer() {
      if (this.instance_soot_dava_toolkits_base_misc_PackageNamer == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_misc_PackageNamer == null) {
               this.instance_soot_dava_toolkits_base_misc_PackageNamer = new PackageNamer(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_misc_PackageNamer;
   }

   protected void release_soot_dava_toolkits_base_misc_PackageNamer() {
      this.instance_soot_dava_toolkits_base_misc_PackageNamer = null;
   }

   public PackManager soot_PackManager() {
      if (this.instance_soot_PackManager == null) {
         synchronized(this) {
            if (this.instance_soot_PackManager == null) {
               this.instance_soot_PackManager = new PackManager(this.g);
            }
         }
      }

      return this.instance_soot_PackManager;
   }

   protected void release_soot_PackManager() {
      this.instance_soot_PackManager = null;
   }

   public PeepholeOptimizer soot_baf_toolkits_base_PeepholeOptimizer() {
      if (this.instance_soot_baf_toolkits_base_PeepholeOptimizer == null) {
         synchronized(this) {
            if (this.instance_soot_baf_toolkits_base_PeepholeOptimizer == null) {
               this.instance_soot_baf_toolkits_base_PeepholeOptimizer = new PeepholeOptimizer(this.g);
            }
         }
      }

      return this.instance_soot_baf_toolkits_base_PeepholeOptimizer;
   }

   protected void release_soot_baf_toolkits_base_PeepholeOptimizer() {
      this.instance_soot_baf_toolkits_base_PeepholeOptimizer = null;
   }

   public ProfilingGenerator soot_jimple_toolkits_annotation_profiling_ProfilingGenerator() {
      if (this.instance_soot_jimple_toolkits_annotation_profiling_ProfilingGenerator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_profiling_ProfilingGenerator == null) {
               this.instance_soot_jimple_toolkits_annotation_profiling_ProfilingGenerator = new ProfilingGenerator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_profiling_ProfilingGenerator;
   }

   protected void release_soot_jimple_toolkits_annotation_profiling_ProfilingGenerator() {
      this.instance_soot_jimple_toolkits_annotation_profiling_ProfilingGenerator = null;
   }

   public RectangularArrayFinder soot_jimple_toolkits_annotation_arraycheck_RectangularArrayFinder() {
      if (this.instance_soot_jimple_toolkits_annotation_arraycheck_RectangularArrayFinder == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_arraycheck_RectangularArrayFinder == null) {
               this.instance_soot_jimple_toolkits_annotation_arraycheck_RectangularArrayFinder = new RectangularArrayFinder(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_arraycheck_RectangularArrayFinder;
   }

   protected void release_soot_jimple_toolkits_annotation_arraycheck_RectangularArrayFinder() {
      this.instance_soot_jimple_toolkits_annotation_arraycheck_RectangularArrayFinder = null;
   }

   public RefType soot_RefType() {
      if (this.instance_soot_RefType == null) {
         synchronized(this) {
            if (this.instance_soot_RefType == null) {
               this.instance_soot_RefType = new RefType(this.g);
            }
         }
      }

      return this.instance_soot_RefType;
   }

   protected void release_soot_RefType() {
      this.instance_soot_RefType = null;
   }

   public Scene soot_Scene() {
      if (this.instance_soot_Scene == null) {
         synchronized(this) {
            if (this.instance_soot_Scene == null) {
               this.instance_soot_Scene = new Scene(this.g);
            }
         }
      }

      return this.instance_soot_Scene;
   }

   protected void release_soot_Scene() {
      this.instance_soot_Scene = null;
   }

   public SequenceFinder soot_dava_toolkits_base_finders_SequenceFinder() {
      if (this.instance_soot_dava_toolkits_base_finders_SequenceFinder == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_finders_SequenceFinder == null) {
               this.instance_soot_dava_toolkits_base_finders_SequenceFinder = new SequenceFinder(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_finders_SequenceFinder;
   }

   protected void release_soot_dava_toolkits_base_finders_SequenceFinder() {
      this.instance_soot_dava_toolkits_base_finders_SequenceFinder = null;
   }

   public Shimple soot_shimple_Shimple() {
      if (this.instance_soot_shimple_Shimple == null) {
         synchronized(this) {
            if (this.instance_soot_shimple_Shimple == null) {
               this.instance_soot_shimple_Shimple = new Shimple(this.g);
            }
         }
      }

      return this.instance_soot_shimple_Shimple;
   }

   protected void release_soot_shimple_Shimple() {
      this.instance_soot_shimple_Shimple = null;
   }

   public ShimpleTransformer soot_shimple_ShimpleTransformer() {
      if (this.instance_soot_shimple_ShimpleTransformer == null) {
         synchronized(this) {
            if (this.instance_soot_shimple_ShimpleTransformer == null) {
               this.instance_soot_shimple_ShimpleTransformer = new ShimpleTransformer(this.g);
            }
         }
      }

      return this.instance_soot_shimple_ShimpleTransformer;
   }

   protected void release_soot_shimple_ShimpleTransformer() {
      this.instance_soot_shimple_ShimpleTransformer = null;
   }

   public SConstantPropagatorAndFolder soot_shimple_toolkits_scalar_SConstantPropagatorAndFolder() {
      if (this.instance_soot_shimple_toolkits_scalar_SConstantPropagatorAndFolder == null) {
         synchronized(this) {
            if (this.instance_soot_shimple_toolkits_scalar_SConstantPropagatorAndFolder == null) {
               this.instance_soot_shimple_toolkits_scalar_SConstantPropagatorAndFolder = new SConstantPropagatorAndFolder(this.g);
            }
         }
      }

      return this.instance_soot_shimple_toolkits_scalar_SConstantPropagatorAndFolder;
   }

   protected void release_soot_shimple_toolkits_scalar_SConstantPropagatorAndFolder() {
      this.instance_soot_shimple_toolkits_scalar_SConstantPropagatorAndFolder = null;
   }

   public ShortType soot_ShortType() {
      if (this.instance_soot_ShortType == null) {
         synchronized(this) {
            if (this.instance_soot_ShortType == null) {
               this.instance_soot_ShortType = new ShortType(this.g);
            }
         }
      }

      return this.instance_soot_ShortType;
   }

   protected void release_soot_ShortType() {
      this.instance_soot_ShortType = null;
   }

   public SideEffectTagger soot_jimple_toolkits_pointer_SideEffectTagger() {
      if (this.instance_soot_jimple_toolkits_pointer_SideEffectTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_pointer_SideEffectTagger == null) {
               this.instance_soot_jimple_toolkits_pointer_SideEffectTagger = new SideEffectTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_pointer_SideEffectTagger;
   }

   protected void release_soot_jimple_toolkits_pointer_SideEffectTagger() {
      this.instance_soot_jimple_toolkits_pointer_SideEffectTagger = null;
   }

   public SparkTransformer soot_jimple_spark_SparkTransformer() {
      if (this.instance_soot_jimple_spark_SparkTransformer == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_spark_SparkTransformer == null) {
               this.instance_soot_jimple_spark_SparkTransformer = new SparkTransformer(this.g);
            }
         }
      }

      return this.instance_soot_jimple_spark_SparkTransformer;
   }

   protected void release_soot_jimple_spark_SparkTransformer() {
      this.instance_soot_jimple_spark_SparkTransformer = null;
   }

   public StaticInliner soot_jimple_toolkits_invoke_StaticInliner() {
      if (this.instance_soot_jimple_toolkits_invoke_StaticInliner == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_invoke_StaticInliner == null) {
               this.instance_soot_jimple_toolkits_invoke_StaticInliner = new StaticInliner(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_invoke_StaticInliner;
   }

   protected void release_soot_jimple_toolkits_invoke_StaticInliner() {
      this.instance_soot_jimple_toolkits_invoke_StaticInliner = null;
   }

   public StaticMethodBinder soot_jimple_toolkits_invoke_StaticMethodBinder() {
      if (this.instance_soot_jimple_toolkits_invoke_StaticMethodBinder == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_invoke_StaticMethodBinder == null) {
               this.instance_soot_jimple_toolkits_invoke_StaticMethodBinder = new StaticMethodBinder(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_invoke_StaticMethodBinder;
   }

   protected void release_soot_jimple_toolkits_invoke_StaticMethodBinder() {
      this.instance_soot_jimple_toolkits_invoke_StaticMethodBinder = null;
   }

   public StmtAddressType soot_StmtAddressType() {
      if (this.instance_soot_StmtAddressType == null) {
         synchronized(this) {
            if (this.instance_soot_StmtAddressType == null) {
               this.instance_soot_StmtAddressType = new StmtAddressType(this.g);
            }
         }
      }

      return this.instance_soot_StmtAddressType;
   }

   protected void release_soot_StmtAddressType() {
      this.instance_soot_StmtAddressType = null;
   }

   public SwitchFinder soot_dava_toolkits_base_finders_SwitchFinder() {
      if (this.instance_soot_dava_toolkits_base_finders_SwitchFinder == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_finders_SwitchFinder == null) {
               this.instance_soot_dava_toolkits_base_finders_SwitchFinder = new SwitchFinder(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_finders_SwitchFinder;
   }

   protected void release_soot_dava_toolkits_base_finders_SwitchFinder() {
      this.instance_soot_dava_toolkits_base_finders_SwitchFinder = null;
   }

   public SynchronizedBlockFinder soot_dava_toolkits_base_finders_SynchronizedBlockFinder() {
      if (this.instance_soot_dava_toolkits_base_finders_SynchronizedBlockFinder == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_finders_SynchronizedBlockFinder == null) {
               this.instance_soot_dava_toolkits_base_finders_SynchronizedBlockFinder = new SynchronizedBlockFinder(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_finders_SynchronizedBlockFinder;
   }

   protected void release_soot_dava_toolkits_base_finders_SynchronizedBlockFinder() {
      this.instance_soot_dava_toolkits_base_finders_SynchronizedBlockFinder = null;
   }

   public ThrowFinder soot_dava_toolkits_base_misc_ThrowFinder() {
      if (this.instance_soot_dava_toolkits_base_misc_ThrowFinder == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_misc_ThrowFinder == null) {
               this.instance_soot_dava_toolkits_base_misc_ThrowFinder = new ThrowFinder(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_misc_ThrowFinder;
   }

   protected void release_soot_dava_toolkits_base_misc_ThrowFinder() {
      this.instance_soot_dava_toolkits_base_misc_ThrowFinder = null;
   }

   public ThrowNullConverter soot_dava_toolkits_base_misc_ThrowNullConverter() {
      if (this.instance_soot_dava_toolkits_base_misc_ThrowNullConverter == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_misc_ThrowNullConverter == null) {
               this.instance_soot_dava_toolkits_base_misc_ThrowNullConverter = new ThrowNullConverter(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_misc_ThrowNullConverter;
   }

   protected void release_soot_dava_toolkits_base_misc_ThrowNullConverter() {
      this.instance_soot_dava_toolkits_base_misc_ThrowNullConverter = null;
   }

   public Timers soot_Timers() {
      if (this.instance_soot_Timers == null) {
         synchronized(this) {
            if (this.instance_soot_Timers == null) {
               this.instance_soot_Timers = new Timers(this.g);
            }
         }
      }

      return this.instance_soot_Timers;
   }

   protected void release_soot_Timers() {
      this.instance_soot_Timers = null;
   }

   public TryContentsFinder soot_dava_toolkits_base_AST_TryContentsFinder() {
      if (this.instance_soot_dava_toolkits_base_AST_TryContentsFinder == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_AST_TryContentsFinder == null) {
               this.instance_soot_dava_toolkits_base_AST_TryContentsFinder = new TryContentsFinder(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_AST_TryContentsFinder;
   }

   protected void release_soot_dava_toolkits_base_AST_TryContentsFinder() {
      this.instance_soot_dava_toolkits_base_AST_TryContentsFinder = null;
   }

   public TypeAssigner soot_jimple_toolkits_typing_TypeAssigner() {
      if (this.instance_soot_jimple_toolkits_typing_TypeAssigner == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_typing_TypeAssigner == null) {
               this.instance_soot_jimple_toolkits_typing_TypeAssigner = new TypeAssigner(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_typing_TypeAssigner;
   }

   protected void release_soot_jimple_toolkits_typing_TypeAssigner() {
      this.instance_soot_jimple_toolkits_typing_TypeAssigner = null;
   }

   public UnconditionalBranchFolder soot_jimple_toolkits_scalar_UnconditionalBranchFolder() {
      if (this.instance_soot_jimple_toolkits_scalar_UnconditionalBranchFolder == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_UnconditionalBranchFolder == null) {
               this.instance_soot_jimple_toolkits_scalar_UnconditionalBranchFolder = new UnconditionalBranchFolder(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_UnconditionalBranchFolder;
   }

   protected void release_soot_jimple_toolkits_scalar_UnconditionalBranchFolder() {
      this.instance_soot_jimple_toolkits_scalar_UnconditionalBranchFolder = null;
   }

   public ConstantCastEliminator soot_jimple_toolkits_scalar_ConstantCastEliminator() {
      if (this.instance_soot_jimple_toolkits_scalar_ConstantCastEliminator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_ConstantCastEliminator == null) {
               this.instance_soot_jimple_toolkits_scalar_ConstantCastEliminator = new ConstantCastEliminator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_ConstantCastEliminator;
   }

   protected void release_soot_jimple_toolkits_scalar_ConstantCastEliminator() {
      this.instance_soot_jimple_toolkits_scalar_ConstantCastEliminator = null;
   }

   public IdentityCastEliminator soot_jimple_toolkits_scalar_IdentityCastEliminator() {
      if (this.instance_soot_jimple_toolkits_scalar_IdentityCastEliminator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_IdentityCastEliminator == null) {
               this.instance_soot_jimple_toolkits_scalar_IdentityCastEliminator = new IdentityCastEliminator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_IdentityCastEliminator;
   }

   protected void release_soot_jimple_toolkits_scalar_IdentityCastEliminator() {
      this.instance_soot_jimple_toolkits_scalar_IdentityCastEliminator = null;
   }

   public FieldStaticnessCorrector soot_jimple_toolkits_scalar_FieldStaticnessCorrector() {
      if (this.instance_soot_jimple_toolkits_scalar_FieldStaticnessCorrector == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_FieldStaticnessCorrector == null) {
               this.instance_soot_jimple_toolkits_scalar_FieldStaticnessCorrector = new FieldStaticnessCorrector(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_FieldStaticnessCorrector;
   }

   protected void release_soot_jimple_toolkits_scalar_FieldStaticnessCorrector() {
      this.instance_soot_jimple_toolkits_scalar_FieldStaticnessCorrector = null;
   }

   public MethodStaticnessCorrector soot_jimple_toolkits_scalar_MethodStaticnessCorrector() {
      if (this.instance_soot_jimple_toolkits_scalar_MethodStaticnessCorrector == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_MethodStaticnessCorrector == null) {
               this.instance_soot_jimple_toolkits_scalar_MethodStaticnessCorrector = new MethodStaticnessCorrector(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_MethodStaticnessCorrector;
   }

   protected void release_soot_jimple_toolkits_scalar_MethodStaticnessCorrector() {
      this.instance_soot_jimple_toolkits_scalar_MethodStaticnessCorrector = null;
   }

   public IdentityOperationEliminator soot_jimple_toolkits_scalar_IdentityOperationEliminator() {
      if (this.instance_soot_jimple_toolkits_scalar_IdentityOperationEliminator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_IdentityOperationEliminator == null) {
               this.instance_soot_jimple_toolkits_scalar_IdentityOperationEliminator = new IdentityOperationEliminator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_IdentityOperationEliminator;
   }

   protected void release_soot_jimple_toolkits_scalar_IdentityOperationEliminator() {
      this.instance_soot_jimple_toolkits_scalar_IdentityOperationEliminator = null;
   }

   public UnknownType soot_UnknownType() {
      if (this.instance_soot_UnknownType == null) {
         synchronized(this) {
            if (this.instance_soot_UnknownType == null) {
               this.instance_soot_UnknownType = new UnknownType(this.g);
            }
         }
      }

      return this.instance_soot_UnknownType;
   }

   protected void release_soot_UnknownType() {
      this.instance_soot_UnknownType = null;
   }

   public UnreachableCodeEliminator soot_jimple_toolkits_scalar_UnreachableCodeEliminator() {
      if (this.instance_soot_jimple_toolkits_scalar_UnreachableCodeEliminator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_UnreachableCodeEliminator == null) {
               this.instance_soot_jimple_toolkits_scalar_UnreachableCodeEliminator = new UnreachableCodeEliminator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_UnreachableCodeEliminator;
   }

   protected void release_soot_jimple_toolkits_scalar_UnreachableCodeEliminator() {
      this.instance_soot_jimple_toolkits_scalar_UnreachableCodeEliminator = null;
   }

   public UnusedLocalEliminator soot_toolkits_scalar_UnusedLocalEliminator() {
      if (this.instance_soot_toolkits_scalar_UnusedLocalEliminator == null) {
         synchronized(this) {
            if (this.instance_soot_toolkits_scalar_UnusedLocalEliminator == null) {
               this.instance_soot_toolkits_scalar_UnusedLocalEliminator = new UnusedLocalEliminator(this.g);
            }
         }
      }

      return this.instance_soot_toolkits_scalar_UnusedLocalEliminator;
   }

   protected void release_soot_toolkits_scalar_UnusedLocalEliminator() {
      this.instance_soot_toolkits_scalar_UnusedLocalEliminator = null;
   }

   public UnusuableType soot_coffi_UnusuableType() {
      if (this.instance_soot_coffi_UnusuableType == null) {
         synchronized(this) {
            if (this.instance_soot_coffi_UnusuableType == null) {
               this.instance_soot_coffi_UnusuableType = new UnusuableType(this.g);
            }
         }
      }

      return this.instance_soot_coffi_UnusuableType;
   }

   protected void release_soot_coffi_UnusuableType() {
      this.instance_soot_coffi_UnusuableType = null;
   }

   public UselessTryRemover soot_dava_toolkits_base_AST_UselessTryRemover() {
      if (this.instance_soot_dava_toolkits_base_AST_UselessTryRemover == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_AST_UselessTryRemover == null) {
               this.instance_soot_dava_toolkits_base_AST_UselessTryRemover = new UselessTryRemover(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_AST_UselessTryRemover;
   }

   protected void release_soot_dava_toolkits_base_AST_UselessTryRemover() {
      this.instance_soot_dava_toolkits_base_AST_UselessTryRemover = null;
   }

   public VoidType soot_VoidType() {
      if (this.instance_soot_VoidType == null) {
         synchronized(this) {
            if (this.instance_soot_VoidType == null) {
               this.instance_soot_VoidType = new VoidType(this.g);
            }
         }
      }

      return this.instance_soot_VoidType;
   }

   protected void release_soot_VoidType() {
      this.instance_soot_VoidType = null;
   }

   public WordType soot_baf_WordType() {
      if (this.instance_soot_baf_WordType == null) {
         synchronized(this) {
            if (this.instance_soot_baf_WordType == null) {
               this.instance_soot_baf_WordType = new WordType(this.g);
            }
         }
      }

      return this.instance_soot_baf_WordType;
   }

   protected void release_soot_baf_WordType() {
      this.instance_soot_baf_WordType = null;
   }

   public FieldReadTagAggregator soot_jimple_spark_fieldrw_FieldReadTagAggregator() {
      if (this.instance_soot_jimple_spark_fieldrw_FieldReadTagAggregator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_spark_fieldrw_FieldReadTagAggregator == null) {
               this.instance_soot_jimple_spark_fieldrw_FieldReadTagAggregator = new FieldReadTagAggregator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_spark_fieldrw_FieldReadTagAggregator;
   }

   protected void release_soot_jimple_spark_fieldrw_FieldReadTagAggregator() {
      this.instance_soot_jimple_spark_fieldrw_FieldReadTagAggregator = null;
   }

   public FieldWriteTagAggregator soot_jimple_spark_fieldrw_FieldWriteTagAggregator() {
      if (this.instance_soot_jimple_spark_fieldrw_FieldWriteTagAggregator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_spark_fieldrw_FieldWriteTagAggregator == null) {
               this.instance_soot_jimple_spark_fieldrw_FieldWriteTagAggregator = new FieldWriteTagAggregator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_spark_fieldrw_FieldWriteTagAggregator;
   }

   protected void release_soot_jimple_spark_fieldrw_FieldWriteTagAggregator() {
      this.instance_soot_jimple_spark_fieldrw_FieldWriteTagAggregator = null;
   }

   public FieldTagAggregator soot_jimple_spark_fieldrw_FieldTagAggregator() {
      if (this.instance_soot_jimple_spark_fieldrw_FieldTagAggregator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_spark_fieldrw_FieldTagAggregator == null) {
               this.instance_soot_jimple_spark_fieldrw_FieldTagAggregator = new FieldTagAggregator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_spark_fieldrw_FieldTagAggregator;
   }

   protected void release_soot_jimple_spark_fieldrw_FieldTagAggregator() {
      this.instance_soot_jimple_spark_fieldrw_FieldTagAggregator = null;
   }

   public EntryPoints soot_EntryPoints() {
      if (this.instance_soot_EntryPoints == null) {
         synchronized(this) {
            if (this.instance_soot_EntryPoints == null) {
               this.instance_soot_EntryPoints = new EntryPoints(this.g);
            }
         }
      }

      return this.instance_soot_EntryPoints;
   }

   protected void release_soot_EntryPoints() {
      this.instance_soot_EntryPoints = null;
   }

   public CallGraphTagger soot_jimple_toolkits_annotation_callgraph_CallGraphTagger() {
      if (this.instance_soot_jimple_toolkits_annotation_callgraph_CallGraphTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_callgraph_CallGraphTagger == null) {
               this.instance_soot_jimple_toolkits_annotation_callgraph_CallGraphTagger = new CallGraphTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_callgraph_CallGraphTagger;
   }

   protected void release_soot_jimple_toolkits_annotation_callgraph_CallGraphTagger() {
      this.instance_soot_jimple_toolkits_annotation_callgraph_CallGraphTagger = null;
   }

   public NullPointerColorer soot_jimple_toolkits_annotation_nullcheck_NullPointerColorer() {
      if (this.instance_soot_jimple_toolkits_annotation_nullcheck_NullPointerColorer == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_nullcheck_NullPointerColorer == null) {
               this.instance_soot_jimple_toolkits_annotation_nullcheck_NullPointerColorer = new NullPointerColorer(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_nullcheck_NullPointerColorer;
   }

   protected void release_soot_jimple_toolkits_annotation_nullcheck_NullPointerColorer() {
      this.instance_soot_jimple_toolkits_annotation_nullcheck_NullPointerColorer = null;
   }

   public ParityTagger soot_jimple_toolkits_annotation_parity_ParityTagger() {
      if (this.instance_soot_jimple_toolkits_annotation_parity_ParityTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_parity_ParityTagger == null) {
               this.instance_soot_jimple_toolkits_annotation_parity_ParityTagger = new ParityTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_parity_ParityTagger;
   }

   protected void release_soot_jimple_toolkits_annotation_parity_ParityTagger() {
      this.instance_soot_jimple_toolkits_annotation_parity_ParityTagger = null;
   }

   public UnreachableMethodsTagger soot_jimple_toolkits_annotation_methods_UnreachableMethodsTagger() {
      if (this.instance_soot_jimple_toolkits_annotation_methods_UnreachableMethodsTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_methods_UnreachableMethodsTagger == null) {
               this.instance_soot_jimple_toolkits_annotation_methods_UnreachableMethodsTagger = new UnreachableMethodsTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_methods_UnreachableMethodsTagger;
   }

   protected void release_soot_jimple_toolkits_annotation_methods_UnreachableMethodsTagger() {
      this.instance_soot_jimple_toolkits_annotation_methods_UnreachableMethodsTagger = null;
   }

   public UnreachableFieldsTagger soot_jimple_toolkits_annotation_fields_UnreachableFieldsTagger() {
      if (this.instance_soot_jimple_toolkits_annotation_fields_UnreachableFieldsTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_fields_UnreachableFieldsTagger == null) {
               this.instance_soot_jimple_toolkits_annotation_fields_UnreachableFieldsTagger = new UnreachableFieldsTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_fields_UnreachableFieldsTagger;
   }

   protected void release_soot_jimple_toolkits_annotation_fields_UnreachableFieldsTagger() {
      this.instance_soot_jimple_toolkits_annotation_fields_UnreachableFieldsTagger = null;
   }

   public TightestQualifiersTagger soot_jimple_toolkits_annotation_qualifiers_TightestQualifiersTagger() {
      if (this.instance_soot_jimple_toolkits_annotation_qualifiers_TightestQualifiersTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_qualifiers_TightestQualifiersTagger == null) {
               this.instance_soot_jimple_toolkits_annotation_qualifiers_TightestQualifiersTagger = new TightestQualifiersTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_qualifiers_TightestQualifiersTagger;
   }

   protected void release_soot_jimple_toolkits_annotation_qualifiers_TightestQualifiersTagger() {
      this.instance_soot_jimple_toolkits_annotation_qualifiers_TightestQualifiersTagger = null;
   }

   public ParameterAliasTagger soot_jimple_toolkits_pointer_ParameterAliasTagger() {
      if (this.instance_soot_jimple_toolkits_pointer_ParameterAliasTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_pointer_ParameterAliasTagger == null) {
               this.instance_soot_jimple_toolkits_pointer_ParameterAliasTagger = new ParameterAliasTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_pointer_ParameterAliasTagger;
   }

   protected void release_soot_jimple_toolkits_pointer_ParameterAliasTagger() {
      this.instance_soot_jimple_toolkits_pointer_ParameterAliasTagger = null;
   }

   public ReachingDefsTagger soot_jimple_toolkits_annotation_defs_ReachingDefsTagger() {
      if (this.instance_soot_jimple_toolkits_annotation_defs_ReachingDefsTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_defs_ReachingDefsTagger == null) {
               this.instance_soot_jimple_toolkits_annotation_defs_ReachingDefsTagger = new ReachingDefsTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_defs_ReachingDefsTagger;
   }

   protected void release_soot_jimple_toolkits_annotation_defs_ReachingDefsTagger() {
      this.instance_soot_jimple_toolkits_annotation_defs_ReachingDefsTagger = null;
   }

   public LiveVarsTagger soot_jimple_toolkits_annotation_liveness_LiveVarsTagger() {
      if (this.instance_soot_jimple_toolkits_annotation_liveness_LiveVarsTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_liveness_LiveVarsTagger == null) {
               this.instance_soot_jimple_toolkits_annotation_liveness_LiveVarsTagger = new LiveVarsTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_liveness_LiveVarsTagger;
   }

   protected void release_soot_jimple_toolkits_annotation_liveness_LiveVarsTagger() {
      this.instance_soot_jimple_toolkits_annotation_liveness_LiveVarsTagger = null;
   }

   public InteractionHandler soot_toolkits_graph_interaction_InteractionHandler() {
      if (this.instance_soot_toolkits_graph_interaction_InteractionHandler == null) {
         synchronized(this) {
            if (this.instance_soot_toolkits_graph_interaction_InteractionHandler == null) {
               this.instance_soot_toolkits_graph_interaction_InteractionHandler = new InteractionHandler(this.g);
            }
         }
      }

      return this.instance_soot_toolkits_graph_interaction_InteractionHandler;
   }

   protected void release_soot_toolkits_graph_interaction_InteractionHandler() {
      this.instance_soot_toolkits_graph_interaction_InteractionHandler = null;
   }

   public LoopInvariantFinder soot_jimple_toolkits_annotation_logic_LoopInvariantFinder() {
      if (this.instance_soot_jimple_toolkits_annotation_logic_LoopInvariantFinder == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_logic_LoopInvariantFinder == null) {
               this.instance_soot_jimple_toolkits_annotation_logic_LoopInvariantFinder = new LoopInvariantFinder(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_logic_LoopInvariantFinder;
   }

   protected void release_soot_jimple_toolkits_annotation_logic_LoopInvariantFinder() {
      this.instance_soot_jimple_toolkits_annotation_logic_LoopInvariantFinder = null;
   }

   public AvailExprTagger soot_jimple_toolkits_annotation_AvailExprTagger() {
      if (this.instance_soot_jimple_toolkits_annotation_AvailExprTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_AvailExprTagger == null) {
               this.instance_soot_jimple_toolkits_annotation_AvailExprTagger = new AvailExprTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_AvailExprTagger;
   }

   protected void release_soot_jimple_toolkits_annotation_AvailExprTagger() {
      this.instance_soot_jimple_toolkits_annotation_AvailExprTagger = null;
   }

   public PhaseDumper soot_util_PhaseDumper() {
      if (this.instance_soot_util_PhaseDumper == null) {
         synchronized(this) {
            if (this.instance_soot_util_PhaseDumper == null) {
               this.instance_soot_util_PhaseDumper = new PhaseDumper(this.g);
            }
         }
      }

      return this.instance_soot_util_PhaseDumper;
   }

   protected void release_soot_util_PhaseDumper() {
      this.instance_soot_util_PhaseDumper = null;
   }

   public AltClassLoader soot_util_cfgcmd_AltClassLoader() {
      if (this.instance_soot_util_cfgcmd_AltClassLoader == null) {
         synchronized(this) {
            if (this.instance_soot_util_cfgcmd_AltClassLoader == null) {
               this.instance_soot_util_cfgcmd_AltClassLoader = new AltClassLoader(this.g);
            }
         }
      }

      return this.instance_soot_util_cfgcmd_AltClassLoader;
   }

   protected void release_soot_util_cfgcmd_AltClassLoader() {
      this.instance_soot_util_cfgcmd_AltClassLoader = null;
   }

   public ThrowableSet.Manager soot_toolkits_exceptions_ThrowableSet_Manager() {
      if (this.instance_soot_toolkits_exceptions_ThrowableSet_Manager == null) {
         synchronized(this) {
            if (this.instance_soot_toolkits_exceptions_ThrowableSet_Manager == null) {
               this.instance_soot_toolkits_exceptions_ThrowableSet_Manager = new ThrowableSet.Manager(this.g);
            }
         }
      }

      return this.instance_soot_toolkits_exceptions_ThrowableSet_Manager;
   }

   protected void release_soot_toolkits_exceptions_ThrowableSet_Manager() {
      this.instance_soot_toolkits_exceptions_ThrowableSet_Manager = null;
   }

   public UnitThrowAnalysis soot_toolkits_exceptions_UnitThrowAnalysis() {
      if (this.instance_soot_toolkits_exceptions_UnitThrowAnalysis == null) {
         synchronized(this) {
            if (this.instance_soot_toolkits_exceptions_UnitThrowAnalysis == null) {
               this.instance_soot_toolkits_exceptions_UnitThrowAnalysis = new UnitThrowAnalysis(this.g);
            }
         }
      }

      return this.instance_soot_toolkits_exceptions_UnitThrowAnalysis;
   }

   protected void release_soot_toolkits_exceptions_UnitThrowAnalysis() {
      this.instance_soot_toolkits_exceptions_UnitThrowAnalysis = null;
   }

   public DalvikThrowAnalysis soot_dexpler_DalvikThrowAnalysis() {
      if (this.instance_soot_dexpler_DalvikThrowAnalysis == null) {
         synchronized(this) {
            if (this.instance_soot_dexpler_DalvikThrowAnalysis == null) {
               this.instance_soot_dexpler_DalvikThrowAnalysis = new DalvikThrowAnalysis(this.g);
            }
         }
      }

      return this.instance_soot_dexpler_DalvikThrowAnalysis;
   }

   protected void release_soot_dexpler_DalvikThrowAnalysis() {
      this.instance_soot_dexpler_DalvikThrowAnalysis = null;
   }

   public DexFileProvider soot_dexpler_DexFileProvider() {
      if (this.instance_soot_dexpler_DexFileProvider == null) {
         synchronized(this) {
            if (this.instance_soot_dexpler_DexFileProvider == null) {
               this.instance_soot_dexpler_DexFileProvider = new DexFileProvider(this.g);
            }
         }
      }

      return this.instance_soot_dexpler_DexFileProvider;
   }

   protected void release_soot_dexpler_DexFileProvider() {
      this.instance_soot_dexpler_DexFileProvider = null;
   }

   public PedanticThrowAnalysis soot_toolkits_exceptions_PedanticThrowAnalysis() {
      if (this.instance_soot_toolkits_exceptions_PedanticThrowAnalysis == null) {
         synchronized(this) {
            if (this.instance_soot_toolkits_exceptions_PedanticThrowAnalysis == null) {
               this.instance_soot_toolkits_exceptions_PedanticThrowAnalysis = new PedanticThrowAnalysis(this.g);
            }
         }
      }

      return this.instance_soot_toolkits_exceptions_PedanticThrowAnalysis;
   }

   protected void release_soot_toolkits_exceptions_PedanticThrowAnalysis() {
      this.instance_soot_toolkits_exceptions_PedanticThrowAnalysis = null;
   }

   public TrapTightener soot_toolkits_exceptions_TrapTightener() {
      if (this.instance_soot_toolkits_exceptions_TrapTightener == null) {
         synchronized(this) {
            if (this.instance_soot_toolkits_exceptions_TrapTightener == null) {
               this.instance_soot_toolkits_exceptions_TrapTightener = new TrapTightener(this.g);
            }
         }
      }

      return this.instance_soot_toolkits_exceptions_TrapTightener;
   }

   protected void release_soot_toolkits_exceptions_TrapTightener() {
      this.instance_soot_toolkits_exceptions_TrapTightener = null;
   }

   public DuplicateCatchAllTrapRemover soot_toolkits_exceptions_DuplicateCatchAllTrapRemover() {
      if (this.instance_soot_toolkits_exceptions_DuplicateCatchAllTrapRemover == null) {
         synchronized(this) {
            if (this.instance_soot_toolkits_exceptions_DuplicateCatchAllTrapRemover == null) {
               this.instance_soot_toolkits_exceptions_DuplicateCatchAllTrapRemover = new DuplicateCatchAllTrapRemover(this.g);
            }
         }
      }

      return this.instance_soot_toolkits_exceptions_DuplicateCatchAllTrapRemover;
   }

   protected void release_soot_toolkits_exceptions_DuplicateCatchAllTrapRemover() {
      this.instance_soot_toolkits_exceptions_DuplicateCatchAllTrapRemover = null;
   }

   public CallGraphGrapher soot_jimple_toolkits_annotation_callgraph_CallGraphGrapher() {
      if (this.instance_soot_jimple_toolkits_annotation_callgraph_CallGraphGrapher == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_callgraph_CallGraphGrapher == null) {
               this.instance_soot_jimple_toolkits_annotation_callgraph_CallGraphGrapher = new CallGraphGrapher(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_callgraph_CallGraphGrapher;
   }

   protected void release_soot_jimple_toolkits_annotation_callgraph_CallGraphGrapher() {
      this.instance_soot_jimple_toolkits_annotation_callgraph_CallGraphGrapher = null;
   }

   public SootResolver soot_SootResolver() {
      if (this.instance_soot_SootResolver == null) {
         synchronized(this) {
            if (this.instance_soot_SootResolver == null) {
               this.instance_soot_SootResolver = new SootResolver(this.g);
            }
         }
      }

      return this.instance_soot_SootResolver;
   }

   protected void release_soot_SootResolver() {
      this.instance_soot_SootResolver = null;
   }

   public InitialResolver soot_javaToJimple_InitialResolver() {
      if (this.instance_soot_javaToJimple_InitialResolver == null) {
         synchronized(this) {
            if (this.instance_soot_javaToJimple_InitialResolver == null) {
               this.instance_soot_javaToJimple_InitialResolver = new InitialResolver(this.g);
            }
         }
      }

      return this.instance_soot_javaToJimple_InitialResolver;
   }

   protected void release_soot_javaToJimple_InitialResolver() {
      this.instance_soot_javaToJimple_InitialResolver = null;
   }

   public PaddleHook soot_jimple_paddle_PaddleHook() {
      if (this.instance_soot_jimple_paddle_PaddleHook == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_paddle_PaddleHook == null) {
               this.instance_soot_jimple_paddle_PaddleHook = new PaddleHook(this.g);
            }
         }
      }

      return this.instance_soot_jimple_paddle_PaddleHook;
   }

   protected void release_soot_jimple_paddle_PaddleHook() {
      this.instance_soot_jimple_paddle_PaddleHook = null;
   }

   public DominatorsTagger soot_jimple_toolkits_annotation_DominatorsTagger() {
      if (this.instance_soot_jimple_toolkits_annotation_DominatorsTagger == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_DominatorsTagger == null) {
               this.instance_soot_jimple_toolkits_annotation_DominatorsTagger = new DominatorsTagger(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_DominatorsTagger;
   }

   protected void release_soot_jimple_toolkits_annotation_DominatorsTagger() {
      this.instance_soot_jimple_toolkits_annotation_DominatorsTagger = null;
   }

   public PurityAnalysis soot_jimple_toolkits_annotation_purity_PurityAnalysis() {
      if (this.instance_soot_jimple_toolkits_annotation_purity_PurityAnalysis == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_purity_PurityAnalysis == null) {
               this.instance_soot_jimple_toolkits_annotation_purity_PurityAnalysis = new PurityAnalysis(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_purity_PurityAnalysis;
   }

   protected void release_soot_jimple_toolkits_annotation_purity_PurityAnalysis() {
      this.instance_soot_jimple_toolkits_annotation_purity_PurityAnalysis = null;
   }

   public CondTransformer soot_javaToJimple_toolkits_CondTransformer() {
      if (this.instance_soot_javaToJimple_toolkits_CondTransformer == null) {
         synchronized(this) {
            if (this.instance_soot_javaToJimple_toolkits_CondTransformer == null) {
               this.instance_soot_javaToJimple_toolkits_CondTransformer = new CondTransformer(this.g);
            }
         }
      }

      return this.instance_soot_javaToJimple_toolkits_CondTransformer;
   }

   protected void release_soot_javaToJimple_toolkits_CondTransformer() {
      this.instance_soot_javaToJimple_toolkits_CondTransformer = null;
   }

   public UselessLabelFinder soot_dava_toolkits_base_AST_transformations_UselessLabelFinder() {
      if (this.instance_soot_dava_toolkits_base_AST_transformations_UselessLabelFinder == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_AST_transformations_UselessLabelFinder == null) {
               this.instance_soot_dava_toolkits_base_AST_transformations_UselessLabelFinder = new UselessLabelFinder(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_AST_transformations_UselessLabelFinder;
   }

   protected void release_soot_dava_toolkits_base_AST_transformations_UselessLabelFinder() {
      this.instance_soot_dava_toolkits_base_AST_transformations_UselessLabelFinder = null;
   }

   public DavaStaticBlockCleaner soot_dava_DavaStaticBlockCleaner() {
      if (this.instance_soot_dava_DavaStaticBlockCleaner == null) {
         synchronized(this) {
            if (this.instance_soot_dava_DavaStaticBlockCleaner == null) {
               this.instance_soot_dava_DavaStaticBlockCleaner = new DavaStaticBlockCleaner(this.g);
            }
         }
      }

      return this.instance_soot_dava_DavaStaticBlockCleaner;
   }

   protected void release_soot_dava_DavaStaticBlockCleaner() {
      this.instance_soot_dava_DavaStaticBlockCleaner = null;
   }

   public ClosestAbruptTargetFinder soot_dava_toolkits_base_AST_traversals_ClosestAbruptTargetFinder() {
      if (this.instance_soot_dava_toolkits_base_AST_traversals_ClosestAbruptTargetFinder == null) {
         synchronized(this) {
            if (this.instance_soot_dava_toolkits_base_AST_traversals_ClosestAbruptTargetFinder == null) {
               this.instance_soot_dava_toolkits_base_AST_traversals_ClosestAbruptTargetFinder = new ClosestAbruptTargetFinder(this.g);
            }
         }
      }

      return this.instance_soot_dava_toolkits_base_AST_traversals_ClosestAbruptTargetFinder;
   }

   protected void release_soot_dava_toolkits_base_AST_traversals_ClosestAbruptTargetFinder() {
      this.instance_soot_dava_toolkits_base_AST_traversals_ClosestAbruptTargetFinder = null;
   }

   public AllSharedListNodes soot_jimple_spark_sets_AllSharedListNodes() {
      if (this.instance_soot_jimple_spark_sets_AllSharedListNodes == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_spark_sets_AllSharedListNodes == null) {
               this.instance_soot_jimple_spark_sets_AllSharedListNodes = new AllSharedListNodes(this.g);
            }
         }
      }

      return this.instance_soot_jimple_spark_sets_AllSharedListNodes;
   }

   protected void release_soot_jimple_spark_sets_AllSharedListNodes() {
      this.instance_soot_jimple_spark_sets_AllSharedListNodes = null;
   }

   public AllSharedHybridNodes soot_jimple_spark_sets_AllSharedHybridNodes() {
      if (this.instance_soot_jimple_spark_sets_AllSharedHybridNodes == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_spark_sets_AllSharedHybridNodes == null) {
               this.instance_soot_jimple_spark_sets_AllSharedHybridNodes = new AllSharedHybridNodes(this.g);
            }
         }
      }

      return this.instance_soot_jimple_spark_sets_AllSharedHybridNodes;
   }

   protected void release_soot_jimple_spark_sets_AllSharedHybridNodes() {
      this.instance_soot_jimple_spark_sets_AllSharedHybridNodes = null;
   }

   public LockAllocator soot_jimple_toolkits_thread_synchronization_LockAllocator() {
      if (this.instance_soot_jimple_toolkits_thread_synchronization_LockAllocator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_thread_synchronization_LockAllocator == null) {
               this.instance_soot_jimple_toolkits_thread_synchronization_LockAllocator = new LockAllocator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_thread_synchronization_LockAllocator;
   }

   protected void release_soot_jimple_toolkits_thread_synchronization_LockAllocator() {
      this.instance_soot_jimple_toolkits_thread_synchronization_LockAllocator = null;
   }

   public MhpTransformer soot_jimple_toolkits_thread_mhp_MhpTransformer() {
      if (this.instance_soot_jimple_toolkits_thread_mhp_MhpTransformer == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_thread_mhp_MhpTransformer == null) {
               this.instance_soot_jimple_toolkits_thread_mhp_MhpTransformer = new MhpTransformer(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_thread_mhp_MhpTransformer;
   }

   protected void release_soot_jimple_toolkits_thread_mhp_MhpTransformer() {
      this.instance_soot_jimple_toolkits_thread_mhp_MhpTransformer = null;
   }

   public JastAddInitialResolver soot_JastAddInitialResolver() {
      if (this.instance_soot_JastAddInitialResolver == null) {
         synchronized(this) {
            if (this.instance_soot_JastAddInitialResolver == null) {
               this.instance_soot_JastAddInitialResolver = new JastAddInitialResolver(this.g);
            }
         }
      }

      return this.instance_soot_JastAddInitialResolver;
   }

   protected void release_soot_JastAddInitialResolver() {
      this.instance_soot_JastAddInitialResolver = null;
   }

   public AnnotationGenerator soot_jimple_toolkits_annotation_j5anno_AnnotationGenerator() {
      if (this.instance_soot_jimple_toolkits_annotation_j5anno_AnnotationGenerator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_annotation_j5anno_AnnotationGenerator == null) {
               this.instance_soot_jimple_toolkits_annotation_j5anno_AnnotationGenerator = new AnnotationGenerator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_annotation_j5anno_AnnotationGenerator;
   }

   protected void release_soot_jimple_toolkits_annotation_j5anno_AnnotationGenerator() {
      this.instance_soot_jimple_toolkits_annotation_j5anno_AnnotationGenerator = null;
   }

   public TemplatePrinter soot_sootify_TemplatePrinter() {
      if (this.instance_soot_sootify_TemplatePrinter == null) {
         synchronized(this) {
            if (this.instance_soot_sootify_TemplatePrinter == null) {
               this.instance_soot_sootify_TemplatePrinter = new TemplatePrinter(this.g);
            }
         }
      }

      return this.instance_soot_sootify_TemplatePrinter;
   }

   protected void release_soot_sootify_TemplatePrinter() {
      this.instance_soot_sootify_TemplatePrinter = null;
   }

   public DexResolver soot_dexpler_DexResolver() {
      if (this.instance_soot_dexpler_DexResolver == null) {
         synchronized(this) {
            if (this.instance_soot_dexpler_DexResolver == null) {
               this.instance_soot_dexpler_DexResolver = new DexResolver(this.g);
            }
         }
      }

      return this.instance_soot_dexpler_DexResolver;
   }

   protected void release_soot_dexpler_DexResolver() {
      this.instance_soot_dexpler_DexResolver = null;
   }

   public EmptySwitchEliminator soot_jimple_toolkits_scalar_EmptySwitchEliminator() {
      if (this.instance_soot_jimple_toolkits_scalar_EmptySwitchEliminator == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_scalar_EmptySwitchEliminator == null) {
               this.instance_soot_jimple_toolkits_scalar_EmptySwitchEliminator = new EmptySwitchEliminator(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_scalar_EmptySwitchEliminator;
   }

   protected void release_soot_jimple_toolkits_scalar_EmptySwitchEliminator() {
      this.instance_soot_jimple_toolkits_scalar_EmptySwitchEliminator = null;
   }

   public SynchronizedMethodTransformer soot_toDex_SynchronizedMethodTransformer() {
      if (this.instance_soot_toDex_SynchronizedMethodTransformer == null) {
         synchronized(this) {
            if (this.instance_soot_toDex_SynchronizedMethodTransformer == null) {
               this.instance_soot_toDex_SynchronizedMethodTransformer = new SynchronizedMethodTransformer(this.g);
            }
         }
      }

      return this.instance_soot_toDex_SynchronizedMethodTransformer;
   }

   protected void release_soot_toDex_SynchronizedMethodTransformer() {
      this.instance_soot_toDex_SynchronizedMethodTransformer = null;
   }

   public TrapSplitter soot_toDex_TrapSplitter() {
      if (this.instance_soot_toDex_TrapSplitter == null) {
         synchronized(this) {
            if (this.instance_soot_toDex_TrapSplitter == null) {
               this.instance_soot_toDex_TrapSplitter = new TrapSplitter(this.g);
            }
         }
      }

      return this.instance_soot_toDex_TrapSplitter;
   }

   protected void release_soot_toDex_TrapSplitter() {
      this.instance_soot_toDex_TrapSplitter = null;
   }

   public FastDexTrapTightener soot_toDex_FastDexTrapTightener() {
      if (this.instance_soot_toDex_FastDexTrapTightener == null) {
         synchronized(this) {
            if (this.instance_soot_toDex_FastDexTrapTightener == null) {
               this.instance_soot_toDex_FastDexTrapTightener = new FastDexTrapTightener(this.g);
            }
         }
      }

      return this.instance_soot_toDex_FastDexTrapTightener;
   }

   protected void release_soot_toDex_FastDexTrapTightener() {
      this.instance_soot_toDex_FastDexTrapTightener = null;
   }

   public RenameDuplicatedClasses soot_jimple_toolkits_base_RenameDuplicatedClasses() {
      if (this.instance_soot_jimple_toolkits_base_RenameDuplicatedClasses == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_base_RenameDuplicatedClasses == null) {
               this.instance_soot_jimple_toolkits_base_RenameDuplicatedClasses = new RenameDuplicatedClasses(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_base_RenameDuplicatedClasses;
   }

   protected void release_soot_jimple_toolkits_base_RenameDuplicatedClasses() {
      this.instance_soot_jimple_toolkits_base_RenameDuplicatedClasses = null;
   }

   public Integer127Type soot_jimple_toolkits_typing_fast_Integer127Type() {
      if (this.instance_soot_jimple_toolkits_typing_fast_Integer127Type == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_typing_fast_Integer127Type == null) {
               this.instance_soot_jimple_toolkits_typing_fast_Integer127Type = new Integer127Type(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_typing_fast_Integer127Type;
   }

   protected void release_soot_jimple_toolkits_typing_fast_Integer127Type() {
      this.instance_soot_jimple_toolkits_typing_fast_Integer127Type = null;
   }

   public Integer1Type soot_jimple_toolkits_typing_fast_Integer1Type() {
      if (this.instance_soot_jimple_toolkits_typing_fast_Integer1Type == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_typing_fast_Integer1Type == null) {
               this.instance_soot_jimple_toolkits_typing_fast_Integer1Type = new Integer1Type(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_typing_fast_Integer1Type;
   }

   protected void release_soot_jimple_toolkits_typing_fast_Integer1Type() {
      this.instance_soot_jimple_toolkits_typing_fast_Integer1Type = null;
   }

   public Integer32767Type soot_jimple_toolkits_typing_fast_Integer32767Type() {
      if (this.instance_soot_jimple_toolkits_typing_fast_Integer32767Type == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_typing_fast_Integer32767Type == null) {
               this.instance_soot_jimple_toolkits_typing_fast_Integer32767Type = new Integer32767Type(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_typing_fast_Integer32767Type;
   }

   protected void release_soot_jimple_toolkits_typing_fast_Integer32767Type() {
      this.instance_soot_jimple_toolkits_typing_fast_Integer32767Type = null;
   }

   public BottomType soot_jimple_toolkits_typing_fast_BottomType() {
      if (this.instance_soot_jimple_toolkits_typing_fast_BottomType == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_typing_fast_BottomType == null) {
               this.instance_soot_jimple_toolkits_typing_fast_BottomType = new BottomType(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_typing_fast_BottomType;
   }

   protected void release_soot_jimple_toolkits_typing_fast_BottomType() {
      this.instance_soot_jimple_toolkits_typing_fast_BottomType = null;
   }

   public TrapMinimizer soot_dexpler_TrapMinimizer() {
      if (this.instance_soot_dexpler_TrapMinimizer == null) {
         synchronized(this) {
            if (this.instance_soot_dexpler_TrapMinimizer == null) {
               this.instance_soot_dexpler_TrapMinimizer = new TrapMinimizer(this.g);
            }
         }
      }

      return this.instance_soot_dexpler_TrapMinimizer;
   }

   protected void release_soot_dexpler_TrapMinimizer() {
      this.instance_soot_dexpler_TrapMinimizer = null;
   }

   public SmartLocalDefsPool soot_toolkits_scalar_SmartLocalDefsPool() {
      if (this.instance_soot_toolkits_scalar_SmartLocalDefsPool == null) {
         synchronized(this) {
            if (this.instance_soot_toolkits_scalar_SmartLocalDefsPool == null) {
               this.instance_soot_toolkits_scalar_SmartLocalDefsPool = new SmartLocalDefsPool(this.g);
            }
         }
      }

      return this.instance_soot_toolkits_scalar_SmartLocalDefsPool;
   }

   protected void release_soot_toolkits_scalar_SmartLocalDefsPool() {
      this.instance_soot_toolkits_scalar_SmartLocalDefsPool = null;
   }

   public PublicAndProtectedAccessibility soot_jimple_spark_internal_PublicAndProtectedAccessibility() {
      if (this.instance_soot_jimple_spark_internal_PublicAndProtectedAccessibility == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_spark_internal_PublicAndProtectedAccessibility == null) {
               this.instance_soot_jimple_spark_internal_PublicAndProtectedAccessibility = new PublicAndProtectedAccessibility(this.g);
            }
         }
      }

      return this.instance_soot_jimple_spark_internal_PublicAndProtectedAccessibility;
   }

   protected void release_soot_jimple_spark_internal_PublicAndProtectedAccessibility() {
      this.instance_soot_jimple_spark_internal_PublicAndProtectedAccessibility = null;
   }

   public CompleteAccessibility soot_jimple_spark_internal_CompleteAccessibility() {
      if (this.instance_soot_jimple_spark_internal_CompleteAccessibility == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_spark_internal_CompleteAccessibility == null) {
               this.instance_soot_jimple_spark_internal_CompleteAccessibility = new CompleteAccessibility(this.g);
            }
         }
      }

      return this.instance_soot_jimple_spark_internal_CompleteAccessibility;
   }

   protected void release_soot_jimple_spark_internal_CompleteAccessibility() {
      this.instance_soot_jimple_spark_internal_CompleteAccessibility = null;
   }

   public ConstantInvokeMethodBaseTransformer soot_jimple_toolkits_reflection_ConstantInvokeMethodBaseTransformer() {
      if (this.instance_soot_jimple_toolkits_reflection_ConstantInvokeMethodBaseTransformer == null) {
         synchronized(this) {
            if (this.instance_soot_jimple_toolkits_reflection_ConstantInvokeMethodBaseTransformer == null) {
               this.instance_soot_jimple_toolkits_reflection_ConstantInvokeMethodBaseTransformer = new ConstantInvokeMethodBaseTransformer(this.g);
            }
         }
      }

      return this.instance_soot_jimple_toolkits_reflection_ConstantInvokeMethodBaseTransformer;
   }

   protected void release_soot_jimple_toolkits_reflection_ConstantInvokeMethodBaseTransformer() {
      this.instance_soot_jimple_toolkits_reflection_ConstantInvokeMethodBaseTransformer = null;
   }

   public ClassRenamer soot_jbco_jimpleTransformations_ClassRenamer() {
      if (this.instance_soot_jbco_jimpleTransformations_ClassRenamer == null) {
         synchronized(this) {
            if (this.instance_soot_jbco_jimpleTransformations_ClassRenamer == null) {
               this.instance_soot_jbco_jimpleTransformations_ClassRenamer = new ClassRenamer(this.g);
            }
         }
      }

      return this.instance_soot_jbco_jimpleTransformations_ClassRenamer;
   }

   protected void release_soot_jbco_jimpleTransformations_ClassRenamer() {
      this.instance_soot_jbco_jimpleTransformations_ClassRenamer = null;
   }

   public MethodRenamer soot_jbco_jimpleTransformations_MethodRenamer() {
      if (this.instance_soot_jbco_jimpleTransformations_MethodRenamer == null) {
         synchronized(this) {
            if (this.instance_soot_jbco_jimpleTransformations_MethodRenamer == null) {
               this.instance_soot_jbco_jimpleTransformations_MethodRenamer = new MethodRenamer(this.g);
            }
         }
      }

      return this.instance_soot_jbco_jimpleTransformations_MethodRenamer;
   }

   protected void release_soot_jbco_jimpleTransformations_MethodRenamer() {
      this.instance_soot_jbco_jimpleTransformations_MethodRenamer = null;
   }

   public final class Global {
      private Global() {
      }

      // $FF: synthetic method
      Global(Object x1) {
         this();
      }
   }
}
