package soot.dexpler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jf.dexlib2.analysis.ClassPath;
import org.jf.dexlib2.analysis.ClassPathResolver;
import org.jf.dexlib2.analysis.ClassProvider;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.immutable.debug.ImmutableLineNumber;
import org.jf.dexlib2.util.MethodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.DoubleType;
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.NullType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.dexpler.instructions.DanglingInstruction;
import soot.dexpler.instructions.DeferableInstruction;
import soot.dexpler.instructions.DexlibAbstractInstruction;
import soot.dexpler.instructions.InstructionFactory;
import soot.dexpler.instructions.MoveExceptionInstruction;
import soot.dexpler.instructions.OdexInstruction;
import soot.dexpler.instructions.PseudoInstruction;
import soot.dexpler.instructions.RetypeableInstruction;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NullConstant;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.toolkits.base.Aggregator;
import soot.jimple.toolkits.scalar.ConditionalBranchFolder;
import soot.jimple.toolkits.scalar.ConstantCastEliminator;
import soot.jimple.toolkits.scalar.CopyPropagator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.FieldStaticnessCorrector;
import soot.jimple.toolkits.scalar.IdentityCastEliminator;
import soot.jimple.toolkits.scalar.IdentityOperationEliminator;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.jimple.toolkits.scalar.MethodStaticnessCorrector;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.jimple.toolkits.typing.TypeAssigner;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceLineNumberTag;
import soot.toolkits.exceptions.TrapTightener;
import soot.toolkits.scalar.LocalPacker;
import soot.toolkits.scalar.LocalSplitter;
import soot.toolkits.scalar.UnusedLocalEliminator;

public class DexBody {
   private static final Logger logger = LoggerFactory.getLogger(DexBody.class);
   private List<DexlibAbstractInstruction> instructions;
   private Local[] registerLocals;
   private Local storeResultLocal;
   private Map<Integer, DexlibAbstractInstruction> instructionAtAddress;
   private List<DeferableInstruction> deferredInstructions;
   private Set<RetypeableInstruction> instructionsToRetype;
   private DanglingInstruction dangling;
   private int numRegisters;
   private int numParameterRegisters;
   private final List<Type> parameterTypes;
   private boolean isStatic;
   private JimpleBody jBody;
   private List<? extends TryBlock<? extends ExceptionHandler>> tries;
   private RefType declaringClassType;
   private final DexFile dexFile;
   private final Method method;
   private ArrayList<PseudoInstruction> pseudoInstructionData = new ArrayList();
   private LocalSplitter localSplitter = null;
   private UnreachableCodeEliminator unreachableCodeEliminator = null;
   private CopyPropagator copyPropagator = null;

   PseudoInstruction isAddressInData(int a) {
      Iterator var2 = this.pseudoInstructionData.iterator();

      PseudoInstruction pi;
      int fb;
      int lb;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         pi = (PseudoInstruction)var2.next();
         fb = pi.getDataFirstByte();
         lb = pi.getDataLastByte();
      } while(fb > a || a > lb);

      return pi;
   }

   DexBody(DexFile dexFile, Method method, RefType declaringClassType) {
      MethodImplementation code = method.getImplementation();
      if (code == null) {
         throw new RuntimeException("error: no code for method " + method.getName());
      } else {
         this.declaringClassType = declaringClassType;
         this.tries = code.getTryBlocks();
         List<? extends CharSequence> paramTypes = method.getParameterTypes();
         if (paramTypes != null) {
            this.parameterTypes = new ArrayList();
            Iterator var6 = paramTypes.iterator();

            while(var6.hasNext()) {
               CharSequence type = (CharSequence)var6.next();
               this.parameterTypes.add(DexType.toSoot(type.toString()));
            }
         } else {
            this.parameterTypes = Collections.emptyList();
         }

         this.isStatic = Modifier.isStatic(method.getAccessFlags());
         this.numRegisters = code.getRegisterCount();
         this.numParameterRegisters = MethodUtil.getParameterRegisterCount(method);
         if (!this.isStatic) {
            --this.numParameterRegisters;
         }

         this.instructions = new ArrayList();
         this.instructionAtAddress = new HashMap();
         this.registerLocals = new Local[this.numRegisters];
         int address = 0;

         Instruction instruction;
         Iterator var12;
         for(var12 = code.getInstructions().iterator(); var12.hasNext(); address += instruction.getCodeUnits()) {
            instruction = (Instruction)var12.next();
            DexlibAbstractInstruction dexInstruction = InstructionFactory.fromInstruction(instruction, address);
            this.instructions.add(dexInstruction);
            this.instructionAtAddress.put(address, dexInstruction);
         }

         if (this.numParameterRegisters > this.numRegisters) {
            throw new RuntimeException("Malformed dex file: insSize (" + this.numParameterRegisters + ") > registersSize (" + this.numRegisters + ")");
         } else {
            var12 = code.getDebugItems().iterator();

            while(var12.hasNext()) {
               DebugItem di = (DebugItem)var12.next();
               if (di instanceof ImmutableLineNumber) {
                  ImmutableLineNumber ln = (ImmutableLineNumber)di;
                  DexlibAbstractInstruction ins = this.instructionAtAddress(ln.getCodeAddress());
                  if (ins != null) {
                     ins.setLineNumber(ln.getLineNumber());
                  }
               }
            }

            this.dexFile = dexFile;
            this.method = method;
         }
      }
   }

   public Set<Type> usedTypes() {
      Set<Type> types = new HashSet();
      Iterator var2 = this.instructions.iterator();

      while(var2.hasNext()) {
         DexlibAbstractInstruction i = (DexlibAbstractInstruction)var2.next();
         types.addAll(i.introducedTypes());
      }

      if (this.tries != null) {
         var2 = this.tries.iterator();

         while(var2.hasNext()) {
            TryBlock<? extends ExceptionHandler> tryItem = (TryBlock)var2.next();
            List<? extends ExceptionHandler> hList = tryItem.getExceptionHandlers();
            Iterator var5 = hList.iterator();

            while(var5.hasNext()) {
               ExceptionHandler handler = (ExceptionHandler)var5.next();
               String exType = handler.getExceptionType();
               if (exType != null) {
                  types.add(DexType.toSoot(exType));
               }
            }
         }
      }

      return types;
   }

   public void add(Unit u) {
      this.getBody().getUnits().add(u);
   }

   public void addDeferredJimplification(DeferableInstruction i) {
      this.deferredInstructions.add(i);
   }

   public void addRetype(RetypeableInstruction i) {
      this.instructionsToRetype.add(i);
   }

   public Body getBody() {
      if (this.jBody == null) {
         throw new RuntimeException("No jimplification happened yet, no body available.");
      } else {
         return this.jBody;
      }
   }

   public Local[] getRegisterLocals() {
      return this.registerLocals;
   }

   public Local getRegisterLocal(int num) throws InvalidDalvikBytecodeException {
      int totalRegisters = this.registerLocals.length;
      if (num > totalRegisters) {
         throw new InvalidDalvikBytecodeException("Trying to access register " + num + " but only " + totalRegisters + " is/are available.");
      } else {
         return this.registerLocals[num];
      }
   }

   public Local getStoreResultLocal() {
      return this.storeResultLocal;
   }

   public DexlibAbstractInstruction instructionAtAddress(int address) {
      DexlibAbstractInstruction i;
      for(i = null; i == null && address >= 0; --address) {
         i = (DexlibAbstractInstruction)this.instructionAtAddress.get(address);
      }

      return i;
   }

   public Body jimplify(Body b, SootMethod m) {
      Jimple jimple = Jimple.v();
      UnknownType unknownType = UnknownType.v();
      NullConstant nullConstant = NullConstant.v();
      Options options = Options.v();
      this.jBody = (JimpleBody)b;
      this.deferredInstructions = new ArrayList();
      this.instructionsToRetype = new HashSet();
      List<Local> paramLocals = new LinkedList();
      int i;
      if (!this.isStatic) {
         i = this.numRegisters - this.numParameterRegisters - 1;
         Local thisLocal = jimple.newLocal("$u" + i, unknownType);
         this.jBody.getLocals().add(thisLocal);
         this.registerLocals[i] = thisLocal;
         JIdentityStmt idStmt = (JIdentityStmt)jimple.newIdentityStmt(thisLocal, jimple.newThisRef(this.declaringClassType));
         this.add(idStmt);
         paramLocals.add(thisLocal);
      }

      i = 0;
      int parameterRegister = this.numRegisters - this.numParameterRegisters;

      for(Iterator var22 = this.parameterTypes.iterator(); var22.hasNext(); ++parameterRegister) {
         Type t = (Type)var22.next();
         Local gen = jimple.newLocal("$u" + parameterRegister, unknownType);
         this.jBody.getLocals().add(gen);
         this.registerLocals[parameterRegister] = gen;
         JIdentityStmt idStmt = (JIdentityStmt)jimple.newIdentityStmt(gen, jimple.newParameterRef(t, i++));
         this.add(idStmt);
         paramLocals.add(gen);
         if (t instanceof LongType || t instanceof DoubleType) {
            ++parameterRegister;
            Local g = jimple.newLocal("$u" + parameterRegister, unknownType);
            this.jBody.getLocals().add(g);
            this.registerLocals[parameterRegister] = g;
         }
      }

      for(i = 0; i < this.numRegisters - this.numParameterRegisters - (this.isStatic ? 0 : 1); ++i) {
         this.registerLocals[i] = jimple.newLocal("$u" + i, unknownType);
         this.jBody.getLocals().add(this.registerLocals[i]);
      }

      this.storeResultLocal = jimple.newLocal("$u-1", unknownType);
      this.jBody.getLocals().add(this.storeResultLocal);
      boolean isOdex = this.dexFile instanceof DexBackedDexFile ? ((DexBackedDexFile)this.dexFile).isOdexFile() : false;
      ClassPath cp = null;
      if (isOdex) {
         String[] sootClasspath = options.soot_classpath().split(File.pathSeparator);
         List<String> classpathList = new ArrayList();
         String[] var28 = sootClasspath;
         int var32 = sootClasspath.length;

         for(int var36 = 0; var36 < var32; ++var36) {
            String str = var28[var36];
            classpathList.add(str);
         }

         try {
            ClassPathResolver resolver = new ClassPathResolver(classpathList, classpathList, classpathList, this.dexFile);
            cp = new ClassPath((ClassProvider[])resolver.getResolvedClassProviders().toArray(new ClassProvider[0]));
         } catch (IOException var19) {
            throw new RuntimeException(var19);
         }
      }

      int prevLineNumber = -1;
      Iterator var27 = this.instructions.iterator();

      while(var27.hasNext()) {
         DexlibAbstractInstruction instruction = (DexlibAbstractInstruction)var27.next();
         if (isOdex && instruction instanceof OdexInstruction) {
            ((OdexInstruction)instruction).deOdex(this.dexFile, this.method, cp);
         }

         if (this.dangling != null) {
            this.dangling.finalize(this, instruction);
            this.dangling = null;
         }

         instruction.jimplify(this);
         if (instruction.getLineNumber() > 0) {
            prevLineNumber = instruction.getLineNumber();
         } else {
            instruction.setLineNumber(prevLineNumber);
         }
      }

      var27 = this.deferredInstructions.iterator();

      while(var27.hasNext()) {
         DeferableInstruction instruction = (DeferableInstruction)var27.next();
         instruction.deferredJimplify(this);
      }

      if (this.tries != null) {
         this.addTraps();
      }

      int prevLn = -1;
      boolean keepLineNumber = options.keep_line_number();
      Iterator var35 = this.instructions.iterator();

      while(true) {
         Unit u;
         while(var35.hasNext()) {
            DexlibAbstractInstruction instruction = (DexlibAbstractInstruction)var35.next();
            u = instruction.getUnit();
            int lineNumber = u.getJavaSourceStartLineNumber();
            if (keepLineNumber && lineNumber < 0) {
               if (prevLn >= 0) {
                  u.addTag(new LineNumberTag(prevLn));
                  u.addTag(new SourceLineNumberTag(prevLn));
               }
            } else {
               prevLn = lineNumber;
            }
         }

         this.instructions = null;
         this.instructionAtAddress.clear();
         this.deferredInstructions = null;
         this.dangling = null;
         this.tries = null;
         DexTrapStackFixer.v().transform(this.jBody);
         DexJumpChainShortener.v().transform(this.jBody);
         DexReturnInliner.v().transform(this.jBody);
         DexArrayInitReducer.v().transform(this.jBody);
         this.getLocalSplitter().transform(this.jBody);
         this.getUnreachableCodeEliminator().transform(this.jBody);
         DeadAssignmentEliminator.v().transform(this.jBody);
         UnusedLocalEliminator.v().transform(this.jBody);
         var35 = this.instructionsToRetype.iterator();

         while(var35.hasNext()) {
            RetypeableInstruction i = (RetypeableInstruction)var35.next();
            i.retype(this.jBody);
         }

         DexNumTransformer.v().transform(this.jBody);
         DexReturnValuePropagator.v().transform(this.jBody);
         this.getCopyPopagator().transform(this.jBody);
         DexNullThrowTransformer.v().transform(this.jBody);
         DexNullTransformer.v().transform(this.jBody);
         DexIfTransformer.v().transform(this.jBody);
         DeadAssignmentEliminator.v().transform(this.jBody);
         UnusedLocalEliminator.v().transform(this.jBody);
         DexNullArrayRefTransformer.v().transform(this.jBody);
         DexNullInstanceofTransformer.v().transform(this.jBody);
         TypeAssigner.v().transform(this.jBody);
         RefType objectType = RefType.v("java.lang.Object");
         LocalPacker.v().transform(this.jBody);
         UnusedLocalEliminator.v().transform(this.jBody);
         LocalNameStandardizer.v().transform(this.jBody);
         if (options.wrong_staticness() == 3) {
            FieldStaticnessCorrector.v().transform(this.jBody);
            MethodStaticnessCorrector.v().transform(this.jBody);
         }

         TrapTightener.v().transform(this.jBody);
         TrapMinimizer.v().transform(this.jBody);
         Aggregator.v().transform(this.jBody);
         ConditionalBranchFolder.v().transform(this.jBody);
         ConstantCastEliminator.v().transform(this.jBody);
         IdentityCastEliminator.v().transform(this.jBody);
         IdentityOperationEliminator.v().transform(this.jBody);
         UnreachableCodeEliminator.v().transform(this.jBody);
         DeadAssignmentEliminator.v().transform(this.jBody);
         UnusedLocalEliminator.v().transform(this.jBody);
         NopEliminator.v().transform(this.jBody);
         DexReturnPacker.v().transform(this.jBody);
         Iterator var41 = this.jBody.getUnits().iterator();

         while(var41.hasNext()) {
            u = (Unit)var41.next();
            if (u instanceof AssignStmt) {
               AssignStmt ass = (AssignStmt)u;
               if (ass.getRightOp() instanceof CastExpr) {
                  CastExpr c = (CastExpr)ass.getRightOp();
                  if (c.getType() instanceof NullType) {
                     ass.setRightOp(nullConstant);
                  }
               }
            }

            if (u instanceof DefinitionStmt) {
               DefinitionStmt def = (DefinitionStmt)u;
               if (def.getLeftOp() instanceof Local && def.getRightOp() instanceof CaughtExceptionRef) {
                  Type t = def.getLeftOp().getType();
                  if (t instanceof RefType) {
                     RefType rt = (RefType)t;
                     if (rt.getSootClass().isPhantom() && !rt.getSootClass().hasSuperclass() && !rt.getSootClass().getName().equals("java.lang.Throwable")) {
                        rt.getSootClass().setSuperclass(Scene.v().getSootClass("java.lang.Throwable"));
                     }
                  }
               }
            }
         }

         var41 = this.jBody.getLocals().iterator();

         while(var41.hasNext()) {
            Local l = (Local)var41.next();
            Type t = l.getType();
            if (t instanceof NullType) {
               l.setType(objectType);
            }
         }

         return this.jBody;
      }
   }

   protected LocalSplitter getLocalSplitter() {
      if (this.localSplitter == null) {
         this.localSplitter = new LocalSplitter(DalvikThrowAnalysis.v());
      }

      return this.localSplitter;
   }

   protected UnreachableCodeEliminator getUnreachableCodeEliminator() {
      if (this.unreachableCodeEliminator == null) {
         this.unreachableCodeEliminator = new UnreachableCodeEliminator(DalvikThrowAnalysis.v());
      }

      return this.unreachableCodeEliminator;
   }

   protected CopyPropagator getCopyPopagator() {
      if (this.copyPropagator == null) {
         this.copyPropagator = new CopyPropagator(DalvikThrowAnalysis.v(), false);
      }

      return this.copyPropagator;
   }

   public void setDanglingInstruction(DanglingInstruction i) {
      this.dangling = i;
   }

   public List<DexlibAbstractInstruction> instructionsAfter(DexlibAbstractInstruction instruction) {
      int i = this.instructions.indexOf(instruction);
      if (i == -1) {
         throw new IllegalArgumentException("Instruction" + instruction + "not part of this body.");
      } else {
         return this.instructions.subList(i + 1, this.instructions.size());
      }
   }

   public List<DexlibAbstractInstruction> instructionsBefore(DexlibAbstractInstruction instruction) {
      int i = this.instructions.indexOf(instruction);
      if (i == -1) {
         throw new IllegalArgumentException("Instruction " + instruction + " not part of this body.");
      } else {
         List<DexlibAbstractInstruction> l = new ArrayList();
         l.addAll(this.instructions.subList(0, i));
         Collections.reverse(l);
         return l;
      }
   }

   private void addTraps() {
      Jimple jimple = Jimple.v();
      Iterator var2 = this.tries.iterator();

      while(var2.hasNext()) {
         TryBlock<? extends ExceptionHandler> tryItem = (TryBlock)var2.next();
         int startAddress = tryItem.getStartCodeAddress();
         int length = tryItem.getCodeUnitCount();
         int endAddress = startAddress + length;
         Unit beginStmt = this.instructionAtAddress(startAddress).getUnit();
         Unit endStmt = this.instructionAtAddress(endAddress).getUnit();
         if (this.jBody.getUnits().getLast() == endStmt && this.instructionAtAddress(endAddress - 1).getUnit() == endStmt) {
            Unit nop = jimple.newNopStmt();
            this.jBody.getUnits().insertAfter((Unit)nop, (Unit)endStmt);
            endStmt = nop;
         }

         List<? extends ExceptionHandler> hList = tryItem.getExceptionHandlers();
         Iterator var10 = hList.iterator();

         while(var10.hasNext()) {
            ExceptionHandler handler = (ExceptionHandler)var10.next();
            String exceptionType = handler.getExceptionType();
            if (exceptionType == null) {
               exceptionType = "Ljava/lang/Throwable;";
            }

            Type t = DexType.toSoot(exceptionType);
            if (t instanceof RefType) {
               SootClass exception = ((RefType)t).getSootClass();
               DexlibAbstractInstruction instruction = this.instructionAtAddress(handler.getHandlerCodeAddress());
               if (!(instruction instanceof MoveExceptionInstruction)) {
                  logger.debug("" + String.format("First instruction of trap handler unit not MoveException but %s", instruction.getClass().getName()));
               } else {
                  ((MoveExceptionInstruction)instruction).setRealType(this, exception.getType());
               }

               Trap trap = jimple.newTrap(exception, (Unit)beginStmt, (Unit)endStmt, (Unit)instruction.getUnit());
               this.jBody.getTraps().add(trap);
            }
         }
      }

   }
}
