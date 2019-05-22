package org.jf.dexlib2.analysis;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.instruction.FiveRegisterInstruction;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.NarrowLiteralInstruction;
import org.jf.dexlib2.iface.instruction.OffsetInstruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.RegisterRangeInstruction;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.SwitchPayload;
import org.jf.dexlib2.iface.instruction.ThreeRegisterInstruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction10x;
import org.jf.dexlib2.iface.instruction.formats.Instruction21t;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.instruction.formats.Instruction22cs;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.instruction.formats.Instruction35mi;
import org.jf.dexlib2.iface.instruction.formats.Instruction35ms;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rmi;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rms;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction10x;
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction21c;
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction22c;
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction35c;
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction3rc;
import org.jf.dexlib2.immutable.reference.ImmutableFieldReference;
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference;
import org.jf.dexlib2.util.MethodUtil;
import org.jf.dexlib2.util.ReferenceUtil;
import org.jf.dexlib2.util.TypeUtils;
import org.jf.dexlib2.writer.util.TryListBuilder;
import org.jf.util.BitSetUtils;
import org.jf.util.ExceptionWithContext;
import org.jf.util.SparseArray;

public class MethodAnalyzer {
   @Nonnull
   private final Method method;
   @Nonnull
   private final MethodImplementation methodImpl;
   private final boolean normalizeVirtualMethods;
   private final int paramRegisterCount;
   @Nonnull
   private final ClassPath classPath;
   @Nullable
   private final InlineMethodResolver inlineResolver;
   @Nonnull
   private final SparseArray<AnalyzedInstruction> analyzedInstructions = new SparseArray(0);
   @Nonnull
   private final BitSet analyzedState;
   @Nullable
   private AnalysisException analysisException = null;
   private final AnalyzedInstruction startOfMethod;
   private static final BitSet Primitive32BitCategories = BitSetUtils.bitSetOfIndexes(2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
   private static final BitSet WideLowCategories = BitSetUtils.bitSetOfIndexes(12, 14);
   private static final BitSet WideHighCategories = BitSetUtils.bitSetOfIndexes(13, 15);
   private static final BitSet ReferenceOrUninitCategories = BitSetUtils.bitSetOfIndexes(2, 16, 17, 18);
   private static final BitSet BooleanCategories = BitSetUtils.bitSetOfIndexes(2, 3, 4);

   public MethodAnalyzer(@Nonnull ClassPath classPath, @Nonnull Method method, @Nullable InlineMethodResolver inlineResolver, boolean normalizeVirtualMethods) {
      this.classPath = classPath;
      this.inlineResolver = inlineResolver;
      this.normalizeVirtualMethods = normalizeVirtualMethods;
      this.method = method;
      MethodImplementation methodImpl = method.getImplementation();
      if (methodImpl == null) {
         throw new IllegalArgumentException("The method has no implementation");
      } else {
         this.methodImpl = methodImpl;
         this.startOfMethod = new AnalyzedInstruction(this, new ImmutableInstruction10x(Opcode.NOP), -1, methodImpl.getRegisterCount()) {
            protected boolean addPredecessor(AnalyzedInstruction predecessor) {
               throw new UnsupportedOperationException();
            }

            @Nonnull
            public RegisterType getPredecessorRegisterType(@Nonnull AnalyzedInstruction predecessor, int registerNumber) {
               throw new UnsupportedOperationException();
            }
         };
         this.buildInstructionList();
         this.analyzedState = new BitSet(this.analyzedInstructions.size());
         this.paramRegisterCount = MethodUtil.getParameterRegisterCount(method);
         this.analyze();
      }
   }

   @Nonnull
   public ClassPath getClassPath() {
      return this.classPath;
   }

   private void analyze() {
      Method method = this.method;
      MethodImplementation methodImpl = this.methodImpl;
      int totalRegisters = methodImpl.getRegisterCount();
      int parameterRegisters = this.paramRegisterCount;
      int nonParameterRegisters = totalRegisters - parameterRegisters;
      if (!MethodUtil.isStatic(method)) {
         int thisRegister = totalRegisters - parameterRegisters;
         if (MethodUtil.isConstructor(method)) {
            this.setPostRegisterTypeAndPropagateChanges(this.startOfMethod, thisRegister, RegisterType.getRegisterType((byte)17, this.classPath.getClass(method.getDefiningClass())));
         } else {
            this.setPostRegisterTypeAndPropagateChanges(this.startOfMethod, thisRegister, RegisterType.getRegisterType((byte)18, this.classPath.getClass(method.getDefiningClass())));
         }

         this.propagateParameterTypes(totalRegisters - parameterRegisters + 1);
      } else {
         this.propagateParameterTypes(totalRegisters - parameterRegisters);
      }

      RegisterType uninit = RegisterType.getRegisterType((byte)1, (TypeProto)null);

      for(int i = 0; i < nonParameterRegisters; ++i) {
         this.setPostRegisterTypeAndPropagateChanges(this.startOfMethod, i, uninit);
      }

      BitSet instructionsToAnalyze = new BitSet(this.analyzedInstructions.size());
      Iterator var8 = this.startOfMethod.successors.iterator();

      while(var8.hasNext()) {
         AnalyzedInstruction successor = (AnalyzedInstruction)var8.next();
         instructionsToAnalyze.set(successor.instructionIndex);
      }

      BitSet undeodexedInstructions = new BitSet(this.analyzedInstructions.size());

      while(true) {
         int i;
         do {
            boolean didSomething = false;

            while(!instructionsToAnalyze.isEmpty()) {
               for(i = instructionsToAnalyze.nextSetBit(0); i >= 0; i = instructionsToAnalyze.nextSetBit(i + 1)) {
                  instructionsToAnalyze.clear(i);
                  if (!this.analyzedState.get(i)) {
                     AnalyzedInstruction instructionToAnalyze = (AnalyzedInstruction)this.analyzedInstructions.valueAt(i);

                     try {
                        if (instructionToAnalyze.originalInstruction.getOpcode().odexOnly()) {
                           instructionToAnalyze.restoreOdexedInstruction();
                        }

                        if (!this.analyzeInstruction(instructionToAnalyze)) {
                           undeodexedInstructions.set(i);
                           continue;
                        }

                        didSomething = true;
                        undeodexedInstructions.clear(i);
                     } catch (AnalysisException var14) {
                        this.analysisException = var14;
                        int codeAddress = this.getInstructionAddress(instructionToAnalyze);
                        var14.codeAddress = codeAddress;
                        var14.addContext(String.format("opcode: %s", instructionToAnalyze.instruction.getOpcode().name));
                        var14.addContext(String.format("code address: %d", codeAddress));
                        var14.addContext(String.format("method: %s", ReferenceUtil.getReferenceString(method)));
                        break;
                     }

                     this.analyzedState.set(instructionToAnalyze.getInstructionIndex());
                     Iterator var12 = instructionToAnalyze.successors.iterator();

                     while(var12.hasNext()) {
                        AnalyzedInstruction successor = (AnalyzedInstruction)var12.next();
                        instructionsToAnalyze.set(successor.getInstructionIndex());
                     }
                  }
               }

               if (this.analysisException != null) {
                  break;
               }
            }

            if (!didSomething) {
               for(int i = 0; i < this.analyzedInstructions.size(); ++i) {
                  AnalyzedInstruction analyzedInstruction = (AnalyzedInstruction)this.analyzedInstructions.valueAt(i);
                  Instruction instruction = analyzedInstruction.getInstruction();
                  if (instruction.getOpcode().odexOnly()) {
                     int objectRegisterNumber;
                     switch(instruction.getOpcode().format) {
                     case Format10x:
                        this.analyzeOdexReturnVoid(analyzedInstruction, false);
                        continue;
                     case Format21c:
                     case Format22c:
                        this.analyzePutGetVolatile(analyzedInstruction, false);
                        continue;
                     case Format35c:
                        this.analyzeInvokeDirectEmpty(analyzedInstruction, false);
                        continue;
                     case Format3rc:
                        this.analyzeInvokeObjectInitRange(analyzedInstruction, false);
                        continue;
                     case Format22cs:
                        objectRegisterNumber = ((Instruction22cs)instruction).getRegisterB();
                        break;
                     case Format35mi:
                     case Format35ms:
                        objectRegisterNumber = ((FiveRegisterInstruction)instruction).getRegisterC();
                        break;
                     case Format3rmi:
                     case Format3rms:
                        objectRegisterNumber = ((RegisterRangeInstruction)instruction).getStartRegister();
                        break;
                     default:
                        continue;
                     }

                     analyzedInstruction.setDeodexedInstruction(new UnresolvedOdexInstruction(instruction, objectRegisterNumber));
                  }
               }

               return;
            }
         } while(undeodexedInstructions.isEmpty());

         for(i = undeodexedInstructions.nextSetBit(0); i >= 0; i = undeodexedInstructions.nextSetBit(i + 1)) {
            instructionsToAnalyze.set(i);
         }
      }
   }

   private void propagateParameterTypes(int parameterStartRegister) {
      int i = 0;
      Iterator var3 = this.method.getParameters().iterator();

      while(var3.hasNext()) {
         MethodParameter parameter = (MethodParameter)var3.next();
         if (TypeUtils.isWideType((TypeReference)parameter)) {
            this.setPostRegisterTypeAndPropagateChanges(this.startOfMethod, parameterStartRegister + i++, RegisterType.getWideRegisterType(parameter, true));
            this.setPostRegisterTypeAndPropagateChanges(this.startOfMethod, parameterStartRegister + i++, RegisterType.getWideRegisterType(parameter, false));
         } else {
            this.setPostRegisterTypeAndPropagateChanges(this.startOfMethod, parameterStartRegister + i++, RegisterType.getRegisterType(this.classPath, parameter));
         }
      }

   }

   public List<AnalyzedInstruction> getAnalyzedInstructions() {
      return this.analyzedInstructions.getValues();
   }

   public List<Instruction> getInstructions() {
      return Lists.transform(this.analyzedInstructions.getValues(), new Function<AnalyzedInstruction, Instruction>() {
         @Nullable
         public Instruction apply(@Nullable AnalyzedInstruction input) {
            return input == null ? null : input.instruction;
         }
      });
   }

   @Nullable
   public AnalysisException getAnalysisException() {
      return this.analysisException;
   }

   public int getParamRegisterCount() {
      return this.paramRegisterCount;
   }

   public int getInstructionAddress(@Nonnull AnalyzedInstruction instruction) {
      return this.analyzedInstructions.keyAt(instruction.instructionIndex);
   }

   private void setDestinationRegisterTypeAndPropagateChanges(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType registerType) {
      this.setPostRegisterTypeAndPropagateChanges(analyzedInstruction, analyzedInstruction.getDestinationRegister(), registerType);
   }

   private void propagateChanges(@Nonnull BitSet changedInstructions, int registerNumber, boolean override) {
      label16:
      while(true) {
         if (!changedInstructions.isEmpty()) {
            int instructionIndex = changedInstructions.nextSetBit(0);

            while(true) {
               if (instructionIndex < 0) {
                  continue label16;
               }

               changedInstructions.clear(instructionIndex);
               this.propagateRegisterToSuccessors((AnalyzedInstruction)this.analyzedInstructions.valueAt(instructionIndex), registerNumber, changedInstructions, override);
               instructionIndex = changedInstructions.nextSetBit(instructionIndex + 1);
            }
         }

         return;
      }
   }

   private void overridePredecessorRegisterTypeAndPropagateChanges(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull AnalyzedInstruction predecessor, int registerNumber, @Nonnull RegisterType registerType) {
      BitSet changedInstructions = new BitSet(this.analyzedInstructions.size());
      if (analyzedInstruction.overridePredecessorRegisterType(predecessor, registerNumber, registerType, this.analyzedState)) {
         changedInstructions.set(analyzedInstruction.instructionIndex);
         this.propagateChanges(changedInstructions, registerNumber, true);
         if (registerType.category == 12) {
            checkWidePair(registerNumber, analyzedInstruction);
            this.overridePredecessorRegisterTypeAndPropagateChanges(analyzedInstruction, predecessor, registerNumber + 1, RegisterType.LONG_HI_TYPE);
         } else if (registerType.category == 14) {
            checkWidePair(registerNumber, analyzedInstruction);
            this.overridePredecessorRegisterTypeAndPropagateChanges(analyzedInstruction, predecessor, registerNumber + 1, RegisterType.DOUBLE_HI_TYPE);
         }

      }
   }

   private void initializeRefAndPropagateChanges(@Nonnull AnalyzedInstruction analyzedInstruction, int registerNumber, @Nonnull RegisterType registerType) {
      BitSet changedInstructions = new BitSet(this.analyzedInstructions.size());
      if (analyzedInstruction.setPostRegisterType(registerNumber, registerType)) {
         this.propagateRegisterToSuccessors(analyzedInstruction, registerNumber, changedInstructions, false);
         this.propagateChanges(changedInstructions, registerNumber, false);
         if (registerType.category == 12) {
            checkWidePair(registerNumber, analyzedInstruction);
            this.setPostRegisterTypeAndPropagateChanges(analyzedInstruction, registerNumber + 1, RegisterType.LONG_HI_TYPE);
         } else if (registerType.category == 14) {
            checkWidePair(registerNumber, analyzedInstruction);
            this.setPostRegisterTypeAndPropagateChanges(analyzedInstruction, registerNumber + 1, RegisterType.DOUBLE_HI_TYPE);
         }

      }
   }

   private void setPostRegisterTypeAndPropagateChanges(@Nonnull AnalyzedInstruction analyzedInstruction, int registerNumber, @Nonnull RegisterType registerType) {
      BitSet changedInstructions = new BitSet(this.analyzedInstructions.size());
      if (analyzedInstruction.setPostRegisterType(registerNumber, registerType)) {
         this.propagateRegisterToSuccessors(analyzedInstruction, registerNumber, changedInstructions, false);
         this.propagateChanges(changedInstructions, registerNumber, false);
         if (registerType.category == 12) {
            checkWidePair(registerNumber, analyzedInstruction);
            this.setPostRegisterTypeAndPropagateChanges(analyzedInstruction, registerNumber + 1, RegisterType.LONG_HI_TYPE);
         } else if (registerType.category == 14) {
            checkWidePair(registerNumber, analyzedInstruction);
            this.setPostRegisterTypeAndPropagateChanges(analyzedInstruction, registerNumber + 1, RegisterType.DOUBLE_HI_TYPE);
         }

      }
   }

   private void propagateRegisterToSuccessors(@Nonnull AnalyzedInstruction instruction, int registerNumber, @Nonnull BitSet changedInstructions, boolean override) {
      RegisterType postRegisterType = instruction.getPostInstructionRegisterType(registerNumber);
      Iterator var6 = instruction.successors.iterator();

      while(var6.hasNext()) {
         AnalyzedInstruction successor = (AnalyzedInstruction)var6.next();
         if (successor.mergeRegister(registerNumber, postRegisterType, this.analyzedState, override)) {
            changedInstructions.set(successor.instructionIndex);
         }
      }

   }

   private void buildInstructionList() {
      int registerCount = this.methodImpl.getRegisterCount();
      ImmutableList<Instruction> instructions = ImmutableList.copyOf(this.methodImpl.getInstructions());
      this.analyzedInstructions.ensureCapacity(instructions.size());
      int currentCodeAddress = 0;

      for(int i = 0; i < instructions.size(); ++i) {
         Instruction instruction = (Instruction)instructions.get(i);
         this.analyzedInstructions.append(currentCodeAddress, new AnalyzedInstruction(this, instruction, i, registerCount));

         assert this.analyzedInstructions.indexOfKey(currentCodeAddress) == i;

         currentCodeAddress += instruction.getCodeUnits();
      }

      List<? extends TryBlock<? extends ExceptionHandler>> tries = this.methodImpl.getTryBlocks();
      tries = TryListBuilder.massageTryBlocks(tries);
      int triesIndex = 0;
      TryBlock currentTry = null;
      AnalyzedInstruction[] currentExceptionHandlers = null;
      AnalyzedInstruction[][] exceptionHandlers = new AnalyzedInstruction[instructions.size()][];
      if (tries != null) {
         for(int i = 0; i < this.analyzedInstructions.size(); ++i) {
            AnalyzedInstruction instruction = (AnalyzedInstruction)this.analyzedInstructions.valueAt(i);
            Opcode instructionOpcode = instruction.instruction.getOpcode();
            currentCodeAddress = this.getInstructionAddress(instruction);
            if (currentTry != null && currentTry.getStartCodeAddress() + currentTry.getCodeUnitCount() <= currentCodeAddress) {
               currentTry = null;
               ++triesIndex;
            }

            if (currentTry == null && triesIndex < tries.size()) {
               TryBlock<? extends ExceptionHandler> tryBlock = (TryBlock)tries.get(triesIndex);
               if (tryBlock.getStartCodeAddress() <= currentCodeAddress) {
                  assert tryBlock.getStartCodeAddress() + tryBlock.getCodeUnitCount() > currentCodeAddress;

                  currentTry = tryBlock;
                  currentExceptionHandlers = this.buildExceptionHandlerArray(tryBlock);
               }
            }

            if (currentTry != null && instructionOpcode.canThrow()) {
               exceptionHandlers[i] = currentExceptionHandlers;
            }
         }
      }

      assert this.analyzedInstructions.size() > 0;

      BitSet instructionsToProcess = new BitSet(instructions.size());
      this.addPredecessorSuccessor(this.startOfMethod, (AnalyzedInstruction)this.analyzedInstructions.valueAt(0), exceptionHandlers, instructionsToProcess);

      while(true) {
         while(true) {
            int instructionCodeAddress;
            AnalyzedInstruction instruction;
            Opcode instructionOpcode;
            do {
               if (instructionsToProcess.isEmpty()) {
                  return;
               }

               int currentInstructionIndex = instructionsToProcess.nextSetBit(0);
               instructionsToProcess.clear(currentInstructionIndex);
               instruction = (AnalyzedInstruction)this.analyzedInstructions.valueAt(currentInstructionIndex);
               instructionOpcode = instruction.instruction.getOpcode();
               instructionCodeAddress = this.getInstructionAddress(instruction);
               if (instruction.instruction.getOpcode().canContinue()) {
                  if (currentInstructionIndex == this.analyzedInstructions.size() - 1) {
                     throw new AnalysisException("Execution can continue past the last instruction", new Object[0]);
                  }

                  AnalyzedInstruction nextInstruction = (AnalyzedInstruction)this.analyzedInstructions.valueAt(currentInstructionIndex + 1);
                  this.addPredecessorSuccessor(instruction, nextInstruction, exceptionHandlers, instructionsToProcess);
               }
            } while(!(instruction.instruction instanceof OffsetInstruction));

            OffsetInstruction offsetInstruction = (OffsetInstruction)instruction.instruction;
            if (instructionOpcode != Opcode.PACKED_SWITCH && instructionOpcode != Opcode.SPARSE_SWITCH) {
               if (instructionOpcode != Opcode.FILL_ARRAY_DATA) {
                  int targetAddressOffset = offsetInstruction.getCodeOffset();
                  AnalyzedInstruction targetInstruction = (AnalyzedInstruction)this.analyzedInstructions.get(instructionCodeAddress + targetAddressOffset);
                  this.addPredecessorSuccessor(instruction, targetInstruction, exceptionHandlers, instructionsToProcess);
               }
            } else {
               AnalyzedInstruction analyzedSwitchPayload = (AnalyzedInstruction)this.analyzedInstructions.get(instructionCodeAddress + offsetInstruction.getCodeOffset());
               if (analyzedSwitchPayload == null) {
                  throw new AnalysisException("Invalid switch payload offset", new Object[0]);
               }

               SwitchPayload switchPayload = (SwitchPayload)analyzedSwitchPayload.instruction;
               Iterator var17 = switchPayload.getSwitchElements().iterator();

               while(var17.hasNext()) {
                  SwitchElement switchElement = (SwitchElement)var17.next();
                  AnalyzedInstruction targetInstruction = (AnalyzedInstruction)this.analyzedInstructions.get(instructionCodeAddress + switchElement.getOffset());
                  if (targetInstruction == null) {
                     throw new AnalysisException("Invalid switch target offset", new Object[0]);
                  }

                  this.addPredecessorSuccessor(instruction, targetInstruction, exceptionHandlers, instructionsToProcess);
               }
            }
         }
      }
   }

   private void addPredecessorSuccessor(@Nonnull AnalyzedInstruction predecessor, @Nonnull AnalyzedInstruction successor, @Nonnull AnalyzedInstruction[][] exceptionHandlers, @Nonnull BitSet instructionsToProcess) {
      this.addPredecessorSuccessor(predecessor, successor, exceptionHandlers, instructionsToProcess, false);
   }

   private void addPredecessorSuccessor(@Nonnull AnalyzedInstruction predecessor, @Nonnull AnalyzedInstruction successor, @Nonnull AnalyzedInstruction[][] exceptionHandlers, @Nonnull BitSet instructionsToProcess, boolean allowMoveException) {
      if (!allowMoveException && successor.instruction.getOpcode() == Opcode.MOVE_EXCEPTION) {
         throw new AnalysisException("Execution can pass from the " + predecessor.instruction.getOpcode().name + " instruction at code address 0x" + Integer.toHexString(this.getInstructionAddress(predecessor)) + " to the move-exception instruction at address 0x" + Integer.toHexString(this.getInstructionAddress(successor)), new Object[0]);
      } else if (successor.addPredecessor(predecessor)) {
         predecessor.addSuccessor(successor);
         instructionsToProcess.set(successor.getInstructionIndex());
         AnalyzedInstruction[] exceptionHandlersForSuccessor = exceptionHandlers[successor.instructionIndex];
         if (exceptionHandlersForSuccessor != null) {
            assert successor.instruction.getOpcode().canThrow();

            AnalyzedInstruction[] var7 = exceptionHandlersForSuccessor;
            int var8 = exceptionHandlersForSuccessor.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               AnalyzedInstruction exceptionHandler = var7[var9];
               this.addPredecessorSuccessor(predecessor, exceptionHandler, exceptionHandlers, instructionsToProcess, true);
            }
         }

      }
   }

   @Nonnull
   private AnalyzedInstruction[] buildExceptionHandlerArray(@Nonnull TryBlock<? extends ExceptionHandler> tryBlock) {
      List<? extends ExceptionHandler> exceptionHandlers = tryBlock.getExceptionHandlers();
      AnalyzedInstruction[] handlerInstructions = new AnalyzedInstruction[exceptionHandlers.size()];

      for(int i = 0; i < exceptionHandlers.size(); ++i) {
         handlerInstructions[i] = (AnalyzedInstruction)this.analyzedInstructions.get(((ExceptionHandler)exceptionHandlers.get(i)).getHandlerCodeAddress());
      }

      return handlerInstructions;
   }

   private boolean analyzeInstruction(@Nonnull AnalyzedInstruction analyzedInstruction) {
      Instruction instruction = analyzedInstruction.instruction;
      switch(instruction.getOpcode()) {
      case NOP:
         return true;
      case MOVE:
      case MOVE_FROM16:
      case MOVE_16:
      case MOVE_WIDE:
      case MOVE_WIDE_FROM16:
      case MOVE_WIDE_16:
      case MOVE_OBJECT:
      case MOVE_OBJECT_FROM16:
      case MOVE_OBJECT_16:
         this.analyzeMove(analyzedInstruction);
         return true;
      case MOVE_RESULT:
      case MOVE_RESULT_WIDE:
      case MOVE_RESULT_OBJECT:
         this.analyzeMoveResult(analyzedInstruction);
         return true;
      case MOVE_EXCEPTION:
         this.analyzeMoveException(analyzedInstruction);
         return true;
      case RETURN_VOID:
      case RETURN:
      case RETURN_WIDE:
      case RETURN_OBJECT:
         return true;
      case RETURN_VOID_BARRIER:
      case RETURN_VOID_NO_BARRIER:
         this.analyzeOdexReturnVoid(analyzedInstruction);
         return true;
      case CONST_4:
      case CONST_16:
      case CONST:
      case CONST_HIGH16:
         this.analyzeConst(analyzedInstruction);
         return true;
      case CONST_WIDE_16:
      case CONST_WIDE_32:
      case CONST_WIDE:
      case CONST_WIDE_HIGH16:
         this.analyzeWideConst(analyzedInstruction);
         return true;
      case CONST_STRING:
      case CONST_STRING_JUMBO:
         this.analyzeConstString(analyzedInstruction);
         return true;
      case CONST_CLASS:
         this.analyzeConstClass(analyzedInstruction);
         return true;
      case MONITOR_ENTER:
      case MONITOR_EXIT:
         return true;
      case CHECK_CAST:
         this.analyzeCheckCast(analyzedInstruction);
         return true;
      case INSTANCE_OF:
         this.analyzeInstanceOf(analyzedInstruction);
         return true;
      case ARRAY_LENGTH:
         this.analyzeArrayLength(analyzedInstruction);
         return true;
      case NEW_INSTANCE:
         this.analyzeNewInstance(analyzedInstruction);
         return true;
      case NEW_ARRAY:
         this.analyzeNewArray(analyzedInstruction);
         return true;
      case FILLED_NEW_ARRAY:
      case FILLED_NEW_ARRAY_RANGE:
         return true;
      case FILL_ARRAY_DATA:
         return true;
      case THROW:
      case GOTO:
      case GOTO_16:
      case GOTO_32:
         return true;
      case PACKED_SWITCH:
      case SPARSE_SWITCH:
         return true;
      case CMPL_FLOAT:
      case CMPG_FLOAT:
      case CMPL_DOUBLE:
      case CMPG_DOUBLE:
      case CMP_LONG:
         this.analyzeFloatWideCmp(analyzedInstruction);
         return true;
      case IF_EQ:
      case IF_NE:
      case IF_LT:
      case IF_GE:
      case IF_GT:
      case IF_LE:
      case IF_LTZ:
      case IF_GEZ:
      case IF_GTZ:
      case IF_LEZ:
         return true;
      case IF_EQZ:
      case IF_NEZ:
         this.analyzeIfEqzNez(analyzedInstruction);
         return true;
      case AGET:
         this.analyze32BitPrimitiveAget(analyzedInstruction, RegisterType.INTEGER_TYPE);
         return true;
      case AGET_BOOLEAN:
         this.analyze32BitPrimitiveAget(analyzedInstruction, RegisterType.BOOLEAN_TYPE);
         return true;
      case AGET_BYTE:
         this.analyze32BitPrimitiveAget(analyzedInstruction, RegisterType.BYTE_TYPE);
         return true;
      case AGET_CHAR:
         this.analyze32BitPrimitiveAget(analyzedInstruction, RegisterType.CHAR_TYPE);
         return true;
      case AGET_SHORT:
         this.analyze32BitPrimitiveAget(analyzedInstruction, RegisterType.SHORT_TYPE);
         return true;
      case AGET_WIDE:
         this.analyzeAgetWide(analyzedInstruction);
         return true;
      case AGET_OBJECT:
         this.analyzeAgetObject(analyzedInstruction);
         return true;
      case APUT:
      case APUT_BOOLEAN:
      case APUT_BYTE:
      case APUT_CHAR:
      case APUT_SHORT:
      case APUT_WIDE:
      case APUT_OBJECT:
         return true;
      case IGET:
         this.analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.INTEGER_TYPE);
         return true;
      case IGET_BOOLEAN:
         this.analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.BOOLEAN_TYPE);
         return true;
      case IGET_BYTE:
         this.analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.BYTE_TYPE);
         return true;
      case IGET_CHAR:
         this.analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.CHAR_TYPE);
         return true;
      case IGET_SHORT:
         this.analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.SHORT_TYPE);
         return true;
      case IGET_WIDE:
      case IGET_OBJECT:
         this.analyzeIgetSgetWideObject(analyzedInstruction);
         return true;
      case IPUT:
      case IPUT_BOOLEAN:
      case IPUT_BYTE:
      case IPUT_CHAR:
      case IPUT_SHORT:
      case IPUT_WIDE:
      case IPUT_OBJECT:
         return true;
      case SGET:
         this.analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.INTEGER_TYPE);
         return true;
      case SGET_BOOLEAN:
         this.analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.BOOLEAN_TYPE);
         return true;
      case SGET_BYTE:
         this.analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.BYTE_TYPE);
         return true;
      case SGET_CHAR:
         this.analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.CHAR_TYPE);
         return true;
      case SGET_SHORT:
         this.analyze32BitPrimitiveIgetSget(analyzedInstruction, RegisterType.SHORT_TYPE);
         return true;
      case SGET_WIDE:
      case SGET_OBJECT:
         this.analyzeIgetSgetWideObject(analyzedInstruction);
         return true;
      case SPUT:
      case SPUT_BOOLEAN:
      case SPUT_BYTE:
      case SPUT_CHAR:
      case SPUT_SHORT:
      case SPUT_WIDE:
      case SPUT_OBJECT:
         return true;
      case INVOKE_VIRTUAL:
         this.analyzeInvokeVirtual(analyzedInstruction, false);
         return true;
      case INVOKE_SUPER:
         this.analyzeInvokeVirtual(analyzedInstruction, false);
         return true;
      case INVOKE_DIRECT:
         this.analyzeInvokeDirect(analyzedInstruction);
         return true;
      case INVOKE_STATIC:
         return true;
      case INVOKE_INTERFACE:
         return true;
      case INVOKE_VIRTUAL_RANGE:
         this.analyzeInvokeVirtual(analyzedInstruction, true);
         return true;
      case INVOKE_SUPER_RANGE:
         this.analyzeInvokeVirtual(analyzedInstruction, true);
         return true;
      case INVOKE_DIRECT_RANGE:
         this.analyzeInvokeDirectRange(analyzedInstruction);
         return true;
      case INVOKE_STATIC_RANGE:
         return true;
      case INVOKE_INTERFACE_RANGE:
         return true;
      case NEG_INT:
      case NOT_INT:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE);
         return true;
      case NEG_LONG:
      case NOT_LONG:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.LONG_LO_TYPE);
         return true;
      case NEG_FLOAT:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.FLOAT_TYPE);
         return true;
      case NEG_DOUBLE:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE);
         return true;
      case INT_TO_LONG:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.LONG_LO_TYPE);
         return true;
      case INT_TO_FLOAT:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.FLOAT_TYPE);
         return true;
      case INT_TO_DOUBLE:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE);
         return true;
      case LONG_TO_INT:
      case DOUBLE_TO_INT:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE);
         return true;
      case LONG_TO_FLOAT:
      case DOUBLE_TO_FLOAT:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.FLOAT_TYPE);
         return true;
      case LONG_TO_DOUBLE:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE);
         return true;
      case FLOAT_TO_INT:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE);
         return true;
      case FLOAT_TO_LONG:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.LONG_LO_TYPE);
         return true;
      case FLOAT_TO_DOUBLE:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE);
         return true;
      case DOUBLE_TO_LONG:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.LONG_LO_TYPE);
         return true;
      case INT_TO_BYTE:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.BYTE_TYPE);
         return true;
      case INT_TO_CHAR:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.CHAR_TYPE);
         return true;
      case INT_TO_SHORT:
         this.analyzeUnaryOp(analyzedInstruction, RegisterType.SHORT_TYPE);
         return true;
      case ADD_INT:
      case SUB_INT:
      case MUL_INT:
      case DIV_INT:
      case REM_INT:
      case SHL_INT:
      case SHR_INT:
      case USHR_INT:
         this.analyzeBinaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE, false);
         return true;
      case AND_INT:
      case OR_INT:
      case XOR_INT:
         this.analyzeBinaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE, true);
         return true;
      case ADD_LONG:
      case SUB_LONG:
      case MUL_LONG:
      case DIV_LONG:
      case REM_LONG:
      case AND_LONG:
      case OR_LONG:
      case XOR_LONG:
      case SHL_LONG:
      case SHR_LONG:
      case USHR_LONG:
         this.analyzeBinaryOp(analyzedInstruction, RegisterType.LONG_LO_TYPE, false);
         return true;
      case ADD_FLOAT:
      case SUB_FLOAT:
      case MUL_FLOAT:
      case DIV_FLOAT:
      case REM_FLOAT:
         this.analyzeBinaryOp(analyzedInstruction, RegisterType.FLOAT_TYPE, false);
         return true;
      case ADD_DOUBLE:
      case SUB_DOUBLE:
      case MUL_DOUBLE:
      case DIV_DOUBLE:
      case REM_DOUBLE:
         this.analyzeBinaryOp(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE, false);
         return true;
      case ADD_INT_2ADDR:
      case SUB_INT_2ADDR:
      case MUL_INT_2ADDR:
      case DIV_INT_2ADDR:
      case REM_INT_2ADDR:
      case SHL_INT_2ADDR:
      case SHR_INT_2ADDR:
      case USHR_INT_2ADDR:
         this.analyzeBinary2AddrOp(analyzedInstruction, RegisterType.INTEGER_TYPE, false);
         return true;
      case AND_INT_2ADDR:
      case OR_INT_2ADDR:
      case XOR_INT_2ADDR:
         this.analyzeBinary2AddrOp(analyzedInstruction, RegisterType.INTEGER_TYPE, true);
         return true;
      case ADD_LONG_2ADDR:
      case SUB_LONG_2ADDR:
      case MUL_LONG_2ADDR:
      case DIV_LONG_2ADDR:
      case REM_LONG_2ADDR:
      case AND_LONG_2ADDR:
      case OR_LONG_2ADDR:
      case XOR_LONG_2ADDR:
      case SHL_LONG_2ADDR:
      case SHR_LONG_2ADDR:
      case USHR_LONG_2ADDR:
         this.analyzeBinary2AddrOp(analyzedInstruction, RegisterType.LONG_LO_TYPE, false);
         return true;
      case ADD_FLOAT_2ADDR:
      case SUB_FLOAT_2ADDR:
      case MUL_FLOAT_2ADDR:
      case DIV_FLOAT_2ADDR:
      case REM_FLOAT_2ADDR:
         this.analyzeBinary2AddrOp(analyzedInstruction, RegisterType.FLOAT_TYPE, false);
         return true;
      case ADD_DOUBLE_2ADDR:
      case SUB_DOUBLE_2ADDR:
      case MUL_DOUBLE_2ADDR:
      case DIV_DOUBLE_2ADDR:
      case REM_DOUBLE_2ADDR:
         this.analyzeBinary2AddrOp(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE, false);
         return true;
      case ADD_INT_LIT16:
      case RSUB_INT:
      case MUL_INT_LIT16:
      case DIV_INT_LIT16:
      case REM_INT_LIT16:
         this.analyzeLiteralBinaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE, false);
         return true;
      case AND_INT_LIT16:
      case OR_INT_LIT16:
      case XOR_INT_LIT16:
         this.analyzeLiteralBinaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE, true);
         return true;
      case ADD_INT_LIT8:
      case RSUB_INT_LIT8:
      case MUL_INT_LIT8:
      case DIV_INT_LIT8:
      case REM_INT_LIT8:
      case SHL_INT_LIT8:
         this.analyzeLiteralBinaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE, false);
         return true;
      case AND_INT_LIT8:
      case OR_INT_LIT8:
      case XOR_INT_LIT8:
         this.analyzeLiteralBinaryOp(analyzedInstruction, RegisterType.INTEGER_TYPE, true);
         return true;
      case SHR_INT_LIT8:
         this.analyzeLiteralBinaryOp(analyzedInstruction, this.getDestTypeForLiteralShiftRight(analyzedInstruction, true), false);
         return true;
      case USHR_INT_LIT8:
         this.analyzeLiteralBinaryOp(analyzedInstruction, this.getDestTypeForLiteralShiftRight(analyzedInstruction, false), false);
         return true;
      case IGET_VOLATILE:
      case IPUT_VOLATILE:
      case SGET_VOLATILE:
      case SPUT_VOLATILE:
      case IGET_OBJECT_VOLATILE:
      case IGET_WIDE_VOLATILE:
      case IPUT_WIDE_VOLATILE:
      case SGET_WIDE_VOLATILE:
      case SPUT_WIDE_VOLATILE:
         this.analyzePutGetVolatile(analyzedInstruction);
         return true;
      case THROW_VERIFICATION_ERROR:
         return true;
      case EXECUTE_INLINE:
         this.analyzeExecuteInline(analyzedInstruction);
         return true;
      case EXECUTE_INLINE_RANGE:
         this.analyzeExecuteInlineRange(analyzedInstruction);
         return true;
      case INVOKE_DIRECT_EMPTY:
         this.analyzeInvokeDirectEmpty(analyzedInstruction);
         return true;
      case INVOKE_OBJECT_INIT_RANGE:
         this.analyzeInvokeObjectInitRange(analyzedInstruction);
         return true;
      case IGET_QUICK:
      case IGET_WIDE_QUICK:
      case IGET_OBJECT_QUICK:
      case IPUT_QUICK:
      case IPUT_WIDE_QUICK:
      case IPUT_OBJECT_QUICK:
      case IPUT_BOOLEAN_QUICK:
      case IPUT_BYTE_QUICK:
      case IPUT_CHAR_QUICK:
      case IPUT_SHORT_QUICK:
      case IGET_BOOLEAN_QUICK:
      case IGET_BYTE_QUICK:
      case IGET_CHAR_QUICK:
      case IGET_SHORT_QUICK:
         return this.analyzeIputIgetQuick(analyzedInstruction);
      case INVOKE_VIRTUAL_QUICK:
         return this.analyzeInvokeVirtualQuick(analyzedInstruction, false, false);
      case INVOKE_SUPER_QUICK:
         return this.analyzeInvokeVirtualQuick(analyzedInstruction, true, false);
      case INVOKE_VIRTUAL_QUICK_RANGE:
         return this.analyzeInvokeVirtualQuick(analyzedInstruction, false, true);
      case INVOKE_SUPER_QUICK_RANGE:
         return this.analyzeInvokeVirtualQuick(analyzedInstruction, true, true);
      case IPUT_OBJECT_VOLATILE:
      case SGET_OBJECT_VOLATILE:
      case SPUT_OBJECT_VOLATILE:
         this.analyzePutGetVolatile(analyzedInstruction);
         return true;
      default:
         assert false;

         return true;
      }
   }

   private void analyzeMove(@Nonnull AnalyzedInstruction analyzedInstruction) {
      TwoRegisterInstruction instruction = (TwoRegisterInstruction)analyzedInstruction.instruction;
      RegisterType sourceRegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterB());
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, sourceRegisterType);
   }

   private void analyzeMoveResult(@Nonnull AnalyzedInstruction analyzedInstruction) {
      AnalyzedInstruction previousInstruction = null;
      if (analyzedInstruction.instructionIndex > 0) {
         previousInstruction = (AnalyzedInstruction)this.analyzedInstructions.valueAt(analyzedInstruction.instructionIndex - 1);
      }

      if (previousInstruction != null && previousInstruction.instruction.getOpcode().setsResult()) {
         ReferenceInstruction invokeInstruction = (ReferenceInstruction)previousInstruction.instruction;
         Reference reference = invokeInstruction.getReference();
         RegisterType resultRegisterType;
         if (reference instanceof MethodReference) {
            resultRegisterType = RegisterType.getRegisterType(this.classPath, ((MethodReference)reference).getReturnType());
         } else {
            resultRegisterType = RegisterType.getRegisterType(this.classPath, (TypeReference)reference);
         }

         this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, resultRegisterType);
      } else {
         throw new AnalysisException(analyzedInstruction.instruction.getOpcode().name + " must occur after an invoke-*/fill-new-array instruction", new Object[0]);
      }
   }

   private void analyzeMoveException(@Nonnull AnalyzedInstruction analyzedInstruction) {
      int instructionAddress = this.getInstructionAddress(analyzedInstruction);
      RegisterType exceptionType = RegisterType.UNKNOWN_TYPE;
      Iterator var4 = this.methodImpl.getTryBlocks().iterator();

      while(var4.hasNext()) {
         TryBlock<? extends ExceptionHandler> tryBlock = (TryBlock)var4.next();
         Iterator var6 = tryBlock.getExceptionHandlers().iterator();

         while(var6.hasNext()) {
            ExceptionHandler handler = (ExceptionHandler)var6.next();
            if (handler.getHandlerCodeAddress() == instructionAddress) {
               String type = handler.getExceptionType();
               if (type == null) {
                  exceptionType = RegisterType.getRegisterType((byte)18, this.classPath.getClass("Ljava/lang/Throwable;"));
               } else {
                  exceptionType = RegisterType.getRegisterType((byte)18, this.classPath.getClass(type)).merge(exceptionType);
               }
            }
         }
      }

      if (exceptionType.category == 0) {
         throw new AnalysisException("move-exception must be the first instruction in an exception handler block", new Object[0]);
      } else {
         this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, exceptionType);
      }
   }

   private void analyzeOdexReturnVoid(AnalyzedInstruction analyzedInstruction) {
      this.analyzeOdexReturnVoid(analyzedInstruction, true);
   }

   private void analyzeOdexReturnVoid(@Nonnull AnalyzedInstruction analyzedInstruction, boolean analyzeResult) {
      Instruction10x deodexedInstruction = new ImmutableInstruction10x(Opcode.RETURN_VOID);
      analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
      if (analyzeResult) {
         this.analyzeInstruction(analyzedInstruction);
      }

   }

   private void analyzeConst(@Nonnull AnalyzedInstruction analyzedInstruction) {
      NarrowLiteralInstruction instruction = (NarrowLiteralInstruction)analyzedInstruction.instruction;
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.getRegisterTypeForLiteral(instruction.getNarrowLiteral()));
   }

   private void analyzeWideConst(@Nonnull AnalyzedInstruction analyzedInstruction) {
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.LONG_LO_TYPE);
   }

   private void analyzeConstString(@Nonnull AnalyzedInstruction analyzedInstruction) {
      TypeProto stringClass = this.classPath.getClass("Ljava/lang/String;");
      RegisterType stringType = RegisterType.getRegisterType((byte)18, stringClass);
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, stringType);
   }

   private void analyzeConstClass(@Nonnull AnalyzedInstruction analyzedInstruction) {
      TypeProto classClass = this.classPath.getClass("Ljava/lang/Class;");
      RegisterType classType = RegisterType.getRegisterType((byte)18, classClass);
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, classType);
   }

   private void analyzeCheckCast(@Nonnull AnalyzedInstruction analyzedInstruction) {
      ReferenceInstruction instruction = (ReferenceInstruction)analyzedInstruction.instruction;
      TypeReference reference = (TypeReference)instruction.getReference();
      RegisterType castRegisterType = RegisterType.getRegisterType(this.classPath, reference);
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, castRegisterType);
   }

   public static boolean isNotWideningConversion(RegisterType originalType, RegisterType newType) {
      if (originalType.type != null && newType.type != null) {
         if (originalType.type.isInterface()) {
            return newType.type.implementsInterface(originalType.type.getType());
         } else {
            TypeProto commonSuperclass = newType.type.getCommonSuperclass(originalType.type);
            if (commonSuperclass.getType().equals(originalType.type.getType())) {
               return true;
            } else {
               return !commonSuperclass.getType().equals(newType.type.getType());
            }
         }
      } else {
         return true;
      }
   }

   static boolean canPropagateTypeAfterInstanceOf(AnalyzedInstruction analyzedInstanceOfInstruction, AnalyzedInstruction analyzedIfInstruction, ClassPath classPath) {
      if (!classPath.isArt()) {
         return false;
      } else {
         Instruction ifInstruction = analyzedIfInstruction.instruction;
         if (((Instruction21t)ifInstruction).getRegisterA() == analyzedInstanceOfInstruction.getDestinationRegister()) {
            Reference reference = ((Instruction22c)analyzedInstanceOfInstruction.getInstruction()).getReference();
            RegisterType registerType = RegisterType.getRegisterType(classPath, (TypeReference)reference);

            try {
               if (registerType.type != null && !registerType.type.isInterface()) {
                  int objectRegister = ((TwoRegisterInstruction)analyzedInstanceOfInstruction.getInstruction()).getRegisterB();
                  RegisterType originalType = analyzedIfInstruction.getPreInstructionRegisterType(objectRegister);
                  return isNotWideningConversion(originalType, registerType);
               }
            } catch (UnresolvedClassException var8) {
               return false;
            }
         }

         return false;
      }
   }

   private void analyzeIfEqzNez(@Nonnull AnalyzedInstruction analyzedInstruction) {
      if (this.classPath.isArt()) {
         int instructionIndex = analyzedInstruction.getInstructionIndex();
         if (instructionIndex > 0) {
            if (analyzedInstruction.getPredecessorCount() != 1) {
               return;
            }

            AnalyzedInstruction prevAnalyzedInstruction = (AnalyzedInstruction)analyzedInstruction.getPredecessors().first();
            if (prevAnalyzedInstruction.instruction.getOpcode() == Opcode.INSTANCE_OF) {
               AnalyzedInstruction fallthroughInstruction = (AnalyzedInstruction)this.analyzedInstructions.valueAt(analyzedInstruction.getInstructionIndex() + 1);
               int nextAddress = this.getInstructionAddress(analyzedInstruction) + ((Instruction21t)analyzedInstruction.instruction).getCodeOffset();
               AnalyzedInstruction branchInstruction = (AnalyzedInstruction)this.analyzedInstructions.get(nextAddress);
               int narrowingRegister = ((Instruction22c)prevAnalyzedInstruction.instruction).getRegisterB();
               RegisterType originalType = analyzedInstruction.getPreInstructionRegisterType(narrowingRegister);
               Instruction22c instanceOfInstruction = (Instruction22c)prevAnalyzedInstruction.instruction;
               RegisterType newType = RegisterType.getRegisterType(this.classPath, (TypeReference)instanceOfInstruction.getReference());
               Iterator var11 = analyzedInstruction.getSetRegisters().iterator();

               while(var11.hasNext()) {
                  int register = (Integer)var11.next();
                  if (analyzedInstruction.instruction.getOpcode() == Opcode.IF_EQZ) {
                     this.overridePredecessorRegisterTypeAndPropagateChanges(fallthroughInstruction, analyzedInstruction, register, newType);
                     this.overridePredecessorRegisterTypeAndPropagateChanges(branchInstruction, analyzedInstruction, register, originalType);
                  } else {
                     this.overridePredecessorRegisterTypeAndPropagateChanges(fallthroughInstruction, analyzedInstruction, register, originalType);
                     this.overridePredecessorRegisterTypeAndPropagateChanges(branchInstruction, analyzedInstruction, register, newType);
                  }
               }
            }
         }
      }

   }

   private void analyzeInstanceOf(@Nonnull AnalyzedInstruction analyzedInstruction) {
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.BOOLEAN_TYPE);
   }

   private void analyzeArrayLength(@Nonnull AnalyzedInstruction analyzedInstruction) {
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.INTEGER_TYPE);
   }

   private void analyzeNewInstance(@Nonnull AnalyzedInstruction analyzedInstruction) {
      ReferenceInstruction instruction = (ReferenceInstruction)analyzedInstruction.instruction;
      int register = ((OneRegisterInstruction)analyzedInstruction.instruction).getRegisterA();
      RegisterType destRegisterType = analyzedInstruction.getPostInstructionRegisterType(register);
      if (destRegisterType.category != 0) {
         assert destRegisterType.category == 16;

      } else {
         TypeReference typeReference = (TypeReference)instruction.getReference();
         RegisterType classType = RegisterType.getRegisterType(this.classPath, typeReference);
         this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.getRegisterType((byte)16, classType.type));
      }
   }

   private void analyzeNewArray(@Nonnull AnalyzedInstruction analyzedInstruction) {
      ReferenceInstruction instruction = (ReferenceInstruction)analyzedInstruction.instruction;
      TypeReference type = (TypeReference)instruction.getReference();
      if (type.getType().charAt(0) != '[') {
         throw new AnalysisException("new-array used with non-array type", new Object[0]);
      } else {
         RegisterType arrayType = RegisterType.getRegisterType(this.classPath, type);
         this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, arrayType);
      }
   }

   private void analyzeFloatWideCmp(@Nonnull AnalyzedInstruction analyzedInstruction) {
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.BYTE_TYPE);
   }

   private void analyze32BitPrimitiveAget(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType registerType) {
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, registerType);
   }

   private void analyzeAgetWide(@Nonnull AnalyzedInstruction analyzedInstruction) {
      ThreeRegisterInstruction instruction = (ThreeRegisterInstruction)analyzedInstruction.instruction;
      RegisterType arrayRegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterB());
      if (arrayRegisterType.category != 2) {
         if (arrayRegisterType.category != 18 || !(arrayRegisterType.type instanceof ArrayProto)) {
            throw new AnalysisException("aget-wide used with non-array register: %s", new Object[]{arrayRegisterType.toString()});
         }

         ArrayProto arrayProto = (ArrayProto)arrayRegisterType.type;
         if (arrayProto.dimensions != 1) {
            throw new AnalysisException("aget-wide used with multi-dimensional array: %s", new Object[]{arrayRegisterType.toString()});
         }

         char arrayBaseType = arrayProto.getElementType().charAt(0);
         if (arrayBaseType == 'J') {
            this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.LONG_LO_TYPE);
         } else {
            if (arrayBaseType != 'D') {
               throw new AnalysisException("aget-wide used with narrow array: %s", new Object[]{arrayRegisterType});
            }

            this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.DOUBLE_LO_TYPE);
         }
      } else {
         this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.LONG_LO_TYPE);
      }

   }

   private void analyzeAgetObject(@Nonnull AnalyzedInstruction analyzedInstruction) {
      ThreeRegisterInstruction instruction = (ThreeRegisterInstruction)analyzedInstruction.instruction;
      RegisterType arrayRegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterB());
      if (arrayRegisterType.category != 2) {
         if (arrayRegisterType.category != 18 || !(arrayRegisterType.type instanceof ArrayProto)) {
            throw new AnalysisException("aget-object used with non-array register: %s", new Object[]{arrayRegisterType.toString()});
         }

         ArrayProto arrayProto = (ArrayProto)arrayRegisterType.type;
         String elementType = arrayProto.getImmediateElementType();
         this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.getRegisterType((byte)18, this.classPath.getClass(elementType)));
      } else {
         this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, RegisterType.NULL_TYPE);
      }

   }

   private void analyze32BitPrimitiveIgetSget(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType registerType) {
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, registerType);
   }

   private void analyzeIgetSgetWideObject(@Nonnull AnalyzedInstruction analyzedInstruction) {
      ReferenceInstruction referenceInstruction = (ReferenceInstruction)analyzedInstruction.instruction;
      FieldReference fieldReference = (FieldReference)referenceInstruction.getReference();
      RegisterType fieldType = RegisterType.getRegisterType(this.classPath, fieldReference.getType());
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, fieldType);
   }

   private void analyzeInvokeDirect(@Nonnull AnalyzedInstruction analyzedInstruction) {
      FiveRegisterInstruction instruction = (FiveRegisterInstruction)analyzedInstruction.instruction;
      this.analyzeInvokeDirectCommon(analyzedInstruction, instruction.getRegisterC());
   }

   private void analyzeInvokeDirectRange(@Nonnull AnalyzedInstruction analyzedInstruction) {
      RegisterRangeInstruction instruction = (RegisterRangeInstruction)analyzedInstruction.instruction;
      this.analyzeInvokeDirectCommon(analyzedInstruction, instruction.getStartRegister());
   }

   private void analyzeInvokeDirectCommon(@Nonnull AnalyzedInstruction analyzedInstruction, int objectRegister) {
      if (analyzedInstruction.isInvokeInit()) {
         RegisterType uninitRef = analyzedInstruction.getPreInstructionRegisterType(objectRegister);
         if (uninitRef.category != 16 && uninitRef.category != 17) {
            assert analyzedInstruction.getSetRegisters().isEmpty();

            return;
         }

         RegisterType initRef = RegisterType.getRegisterType((byte)18, uninitRef.type);
         Iterator var5 = analyzedInstruction.getSetRegisters().iterator();

         while(var5.hasNext()) {
            int register = (Integer)var5.next();
            RegisterType registerType = analyzedInstruction.getPreInstructionRegisterType(register);
            if (registerType == uninitRef) {
               this.setPostRegisterTypeAndPropagateChanges(analyzedInstruction, register, initRef);
            } else {
               this.setPostRegisterTypeAndPropagateChanges(analyzedInstruction, register, registerType);
            }
         }
      }

   }

   private void analyzeUnaryOp(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType destRegisterType) {
      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, destRegisterType);
   }

   private void analyzeBinaryOp(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType destRegisterType, boolean checkForBoolean) {
      if (checkForBoolean) {
         ThreeRegisterInstruction instruction = (ThreeRegisterInstruction)analyzedInstruction.instruction;
         RegisterType source1RegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterB());
         RegisterType source2RegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterC());
         if (BooleanCategories.get(source1RegisterType.category) && BooleanCategories.get(source2RegisterType.category)) {
            destRegisterType = RegisterType.BOOLEAN_TYPE;
         }
      }

      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, destRegisterType);
   }

   private void analyzeBinary2AddrOp(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType destRegisterType, boolean checkForBoolean) {
      if (checkForBoolean) {
         TwoRegisterInstruction instruction = (TwoRegisterInstruction)analyzedInstruction.instruction;
         RegisterType source1RegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterA());
         RegisterType source2RegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterB());
         if (BooleanCategories.get(source1RegisterType.category) && BooleanCategories.get(source2RegisterType.category)) {
            destRegisterType = RegisterType.BOOLEAN_TYPE;
         }
      }

      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, destRegisterType);
   }

   private void analyzeLiteralBinaryOp(@Nonnull AnalyzedInstruction analyzedInstruction, @Nonnull RegisterType destRegisterType, boolean checkForBoolean) {
      if (checkForBoolean) {
         TwoRegisterInstruction instruction = (TwoRegisterInstruction)analyzedInstruction.instruction;
         RegisterType sourceRegisterType = analyzedInstruction.getPreInstructionRegisterType(instruction.getRegisterB());
         if (BooleanCategories.get(sourceRegisterType.category)) {
            int literal = ((NarrowLiteralInstruction)analyzedInstruction.instruction).getNarrowLiteral();
            if (literal == 0 || literal == 1) {
               destRegisterType = RegisterType.BOOLEAN_TYPE;
            }
         }
      }

      this.setDestinationRegisterTypeAndPropagateChanges(analyzedInstruction, destRegisterType);
   }

   private RegisterType getDestTypeForLiteralShiftRight(@Nonnull AnalyzedInstruction analyzedInstruction, boolean signedShift) {
      TwoRegisterInstruction instruction = (TwoRegisterInstruction)analyzedInstruction.instruction;
      RegisterType sourceRegisterType = getAndCheckSourceRegister(analyzedInstruction, instruction.getRegisterB(), Primitive32BitCategories);
      long literalShift = (long)((NarrowLiteralInstruction)analyzedInstruction.instruction).getNarrowLiteral();
      if (literalShift == 0L) {
         return sourceRegisterType;
      } else {
         RegisterType destRegisterType;
         if (!signedShift) {
            destRegisterType = RegisterType.INTEGER_TYPE;
         } else {
            destRegisterType = sourceRegisterType;
         }

         literalShift &= 31L;
         switch(sourceRegisterType.category) {
         case 2:
         case 3:
         case 4:
            return RegisterType.NULL_TYPE;
         case 5:
            break;
         case 6:
            return RegisterType.POS_BYTE_TYPE;
         case 7:
            if (signedShift && literalShift >= 8L) {
               return RegisterType.BYTE_TYPE;
            }
            break;
         case 8:
            if (literalShift >= 8L) {
               return RegisterType.POS_BYTE_TYPE;
            }
            break;
         case 9:
            if (literalShift > 8L) {
               return RegisterType.POS_BYTE_TYPE;
            }
            break;
         case 10:
         case 11:
            if (!signedShift) {
               if (literalShift > 24L) {
                  return RegisterType.POS_BYTE_TYPE;
               }

               if (literalShift >= 16L) {
                  return RegisterType.CHAR_TYPE;
               }
            } else {
               if (literalShift >= 24L) {
                  return RegisterType.BYTE_TYPE;
               }

               if (literalShift >= 16L) {
                  return RegisterType.SHORT_TYPE;
               }
            }
            break;
         default:
            assert false;
         }

         return destRegisterType;
      }
   }

   private void analyzeExecuteInline(@Nonnull AnalyzedInstruction analyzedInstruction) {
      if (this.inlineResolver == null) {
         throw new AnalysisException("Cannot analyze an odexed instruction unless we are deodexing", new Object[0]);
      } else {
         Instruction35mi instruction = (Instruction35mi)analyzedInstruction.instruction;
         Method resolvedMethod = this.inlineResolver.resolveExecuteInline(analyzedInstruction);
         int acccessFlags = resolvedMethod.getAccessFlags();
         Opcode deodexedOpcode;
         if (AccessFlags.STATIC.isSet(acccessFlags)) {
            deodexedOpcode = Opcode.INVOKE_STATIC;
         } else if (AccessFlags.PRIVATE.isSet(acccessFlags)) {
            deodexedOpcode = Opcode.INVOKE_DIRECT;
         } else {
            deodexedOpcode = Opcode.INVOKE_VIRTUAL;
         }

         Instruction35c deodexedInstruction = new ImmutableInstruction35c(deodexedOpcode, instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), resolvedMethod);
         analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
         this.analyzeInstruction(analyzedInstruction);
      }
   }

   private void analyzeExecuteInlineRange(@Nonnull AnalyzedInstruction analyzedInstruction) {
      if (this.inlineResolver == null) {
         throw new AnalysisException("Cannot analyze an odexed instruction unless we are deodexing", new Object[0]);
      } else {
         Instruction3rmi instruction = (Instruction3rmi)analyzedInstruction.instruction;
         Method resolvedMethod = this.inlineResolver.resolveExecuteInline(analyzedInstruction);
         int acccessFlags = resolvedMethod.getAccessFlags();
         Opcode deodexedOpcode;
         if (AccessFlags.STATIC.isSet(acccessFlags)) {
            deodexedOpcode = Opcode.INVOKE_STATIC_RANGE;
         } else if (AccessFlags.PRIVATE.isSet(acccessFlags)) {
            deodexedOpcode = Opcode.INVOKE_DIRECT_RANGE;
         } else {
            deodexedOpcode = Opcode.INVOKE_VIRTUAL_RANGE;
         }

         Instruction3rc deodexedInstruction = new ImmutableInstruction3rc(deodexedOpcode, instruction.getStartRegister(), instruction.getRegisterCount(), resolvedMethod);
         analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
         this.analyzeInstruction(analyzedInstruction);
      }
   }

   private void analyzeInvokeDirectEmpty(@Nonnull AnalyzedInstruction analyzedInstruction) {
      this.analyzeInvokeDirectEmpty(analyzedInstruction, true);
   }

   private void analyzeInvokeDirectEmpty(@Nonnull AnalyzedInstruction analyzedInstruction, boolean analyzeResult) {
      Instruction35c instruction = (Instruction35c)analyzedInstruction.instruction;
      Instruction35c deodexedInstruction = new ImmutableInstruction35c(Opcode.INVOKE_DIRECT, instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getReference());
      analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
      if (analyzeResult) {
         this.analyzeInstruction(analyzedInstruction);
      }

   }

   private void analyzeInvokeObjectInitRange(@Nonnull AnalyzedInstruction analyzedInstruction) {
      this.analyzeInvokeObjectInitRange(analyzedInstruction, true);
   }

   private void analyzeInvokeObjectInitRange(@Nonnull AnalyzedInstruction analyzedInstruction, boolean analyzeResult) {
      Instruction3rc instruction = (Instruction3rc)analyzedInstruction.instruction;
      int startRegister = instruction.getStartRegister();
      int registerCount = 1;
      Object deodexedInstruction;
      if (startRegister < 16) {
         deodexedInstruction = new ImmutableInstruction35c(Opcode.INVOKE_DIRECT, registerCount, startRegister, 0, 0, 0, 0, instruction.getReference());
      } else {
         deodexedInstruction = new ImmutableInstruction3rc(Opcode.INVOKE_DIRECT_RANGE, startRegister, registerCount, instruction.getReference());
      }

      analyzedInstruction.setDeodexedInstruction((Instruction)deodexedInstruction);
      if (analyzeResult) {
         this.analyzeInstruction(analyzedInstruction);
      }

   }

   private boolean analyzeIputIgetQuick(@Nonnull AnalyzedInstruction analyzedInstruction) {
      Instruction22cs instruction = (Instruction22cs)analyzedInstruction.instruction;
      int fieldOffset = instruction.getFieldOffset();
      RegisterType objectRegisterType = getAndCheckSourceRegister(analyzedInstruction, instruction.getRegisterB(), ReferenceOrUninitCategories);
      if (objectRegisterType.category == 2) {
         return false;
      } else {
         TypeProto objectRegisterTypeProto = objectRegisterType.type;

         assert objectRegisterTypeProto != null;

         TypeProto classTypeProto = this.classPath.getClass(objectRegisterTypeProto.getType());
         FieldReference resolvedField = classTypeProto.getFieldByOffset(fieldOffset);
         if (resolvedField == null) {
            throw new AnalysisException("Could not resolve the field in class %s at offset %d", new Object[]{objectRegisterType.type.getType(), fieldOffset});
         } else {
            ClassDef thisClass = this.classPath.getClassDef(this.method.getDefiningClass());
            if (!TypeUtils.canAccessClass(thisClass.getType(), this.classPath.getClassDef(((FieldReference)resolvedField).getDefiningClass()))) {
               ClassDef fieldClass;
               String superclass;
               for(fieldClass = this.classPath.getClassDef(objectRegisterTypeProto.getType()); !TypeUtils.canAccessClass(thisClass.getType(), fieldClass); fieldClass = this.classPath.getClassDef(superclass)) {
                  superclass = fieldClass.getSuperclass();
                  if (superclass == null) {
                     throw new ExceptionWithContext("Couldn't find accessible class while resolving field %s", new Object[]{ReferenceUtil.getShortFieldDescriptor((FieldReference)resolvedField)});
                  }
               }

               FieldReference newResolvedField = this.classPath.getClass(fieldClass.getType()).getFieldByOffset(fieldOffset);
               if (newResolvedField == null) {
                  throw new ExceptionWithContext("Couldn't find accessible class while resolving field %s", new Object[]{ReferenceUtil.getShortFieldDescriptor((FieldReference)resolvedField)});
               }

               resolvedField = new ImmutableFieldReference(fieldClass.getType(), newResolvedField.getName(), newResolvedField.getType());
            }

            String fieldType = ((FieldReference)resolvedField).getType();
            Opcode opcode = this.classPath.getFieldInstructionMapper().getAndCheckDeodexedOpcode(fieldType, instruction.getOpcode());
            Instruction22c deodexedInstruction = new ImmutableInstruction22c(opcode, (byte)instruction.getRegisterA(), (byte)instruction.getRegisterB(), (Reference)resolvedField);
            analyzedInstruction.setDeodexedInstruction(deodexedInstruction);
            this.analyzeInstruction(analyzedInstruction);
            return true;
         }
      }
   }

   private boolean analyzeInvokeVirtual(@Nonnull AnalyzedInstruction analyzedInstruction, boolean isRange) {
      if (!this.normalizeVirtualMethods) {
         return true;
      } else {
         MethodReference targetMethod;
         if (isRange) {
            Instruction3rc instruction = (Instruction3rc)analyzedInstruction.instruction;
            targetMethod = (MethodReference)instruction.getReference();
         } else {
            Instruction35c instruction = (Instruction35c)analyzedInstruction.instruction;
            targetMethod = (MethodReference)instruction.getReference();
         }

         MethodReference replacementMethod = this.normalizeMethodReference(targetMethod);
         if (replacementMethod != null && !replacementMethod.equals(targetMethod)) {
            Object deodexedInstruction;
            if (isRange) {
               Instruction3rc instruction = (Instruction3rc)analyzedInstruction.instruction;
               deodexedInstruction = new ImmutableInstruction3rc(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), replacementMethod);
            } else {
               Instruction35c instruction = (Instruction35c)analyzedInstruction.instruction;
               deodexedInstruction = new ImmutableInstruction35c(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), replacementMethod);
            }

            analyzedInstruction.setDeodexedInstruction((Instruction)deodexedInstruction);
            return true;
         } else {
            return true;
         }
      }
   }

   private boolean analyzeInvokeVirtualQuick(@Nonnull AnalyzedInstruction analyzedInstruction, boolean isSuper, boolean isRange) {
      int methodIndex;
      int objectRegister;
      if (isRange) {
         Instruction3rms instruction = (Instruction3rms)analyzedInstruction.instruction;
         methodIndex = instruction.getVtableIndex();
         objectRegister = instruction.getStartRegister();
      } else {
         Instruction35ms instruction = (Instruction35ms)analyzedInstruction.instruction;
         methodIndex = instruction.getVtableIndex();
         objectRegister = instruction.getRegisterC();
      }

      RegisterType objectRegisterType = getAndCheckSourceRegister(analyzedInstruction, objectRegister, ReferenceOrUninitCategories);
      TypeProto objectRegisterTypeProto = objectRegisterType.type;
      if (objectRegisterType.category == 2) {
         return false;
      } else {
         assert objectRegisterTypeProto != null;

         Object resolvedMethod;
         String superclass;
         if (isSuper) {
            TypeProto typeProto = this.classPath.getClass(this.method.getDefiningClass());
            superclass = typeProto.getSuperclass();
            TypeProto superType;
            if (superclass != null) {
               superType = this.classPath.getClass(superclass);
            } else {
               superType = typeProto;
            }

            resolvedMethod = superType.getMethodByVtableIndex(methodIndex);
         } else {
            resolvedMethod = objectRegisterTypeProto.getMethodByVtableIndex(methodIndex);
         }

         if (resolvedMethod == null) {
            throw new AnalysisException("Could not resolve the method in class %s at index %d", new Object[]{objectRegisterType.type.getType(), methodIndex});
         } else {
            ClassDef thisClass = this.classPath.getClassDef(this.method.getDefiningClass());
            if (this.classPath.getClass(((MethodReference)resolvedMethod).getDefiningClass()).isInterface()) {
               resolvedMethod = new MethodAnalyzer.ReparentedMethodReference((MethodReference)resolvedMethod, objectRegisterTypeProto.getType());
            } else if (!isSuper && !TypeUtils.canAccessClass(thisClass.getType(), this.classPath.getClassDef(((MethodReference)resolvedMethod).getDefiningClass()))) {
               ClassDef methodClass;
               for(methodClass = this.classPath.getClassDef(objectRegisterTypeProto.getType()); !TypeUtils.canAccessClass(thisClass.getType(), methodClass); methodClass = this.classPath.getClassDef(superclass)) {
                  superclass = methodClass.getSuperclass();
                  if (superclass == null) {
                     throw new ExceptionWithContext("Couldn't find accessible class while resolving method %s", new Object[]{ReferenceUtil.getMethodDescriptor((MethodReference)resolvedMethod, true)});
                  }
               }

               MethodReference newResolvedMethod = this.classPath.getClass(methodClass.getType()).getMethodByVtableIndex(methodIndex);
               if (newResolvedMethod == null) {
                  throw new ExceptionWithContext("Couldn't find accessible class while resolving method %s", new Object[]{ReferenceUtil.getMethodDescriptor((MethodReference)resolvedMethod, true)});
               }

               resolvedMethod = new ImmutableMethodReference(methodClass.getType(), newResolvedMethod.getName(), newResolvedMethod.getParameterTypes(), newResolvedMethod.getReturnType());
            }

            if (this.normalizeVirtualMethods) {
               MethodReference replacementMethod = this.normalizeMethodReference((MethodReference)resolvedMethod);
               if (replacementMethod != null) {
                  resolvedMethod = replacementMethod;
               }
            }

            Opcode opcode;
            Object deodexedInstruction;
            if (isRange) {
               Instruction3rms instruction = (Instruction3rms)analyzedInstruction.instruction;
               if (isSuper) {
                  opcode = Opcode.INVOKE_SUPER_RANGE;
               } else {
                  opcode = Opcode.INVOKE_VIRTUAL_RANGE;
               }

               deodexedInstruction = new ImmutableInstruction3rc(opcode, instruction.getStartRegister(), instruction.getRegisterCount(), (Reference)resolvedMethod);
            } else {
               Instruction35ms instruction = (Instruction35ms)analyzedInstruction.instruction;
               if (isSuper) {
                  opcode = Opcode.INVOKE_SUPER;
               } else {
                  opcode = Opcode.INVOKE_VIRTUAL;
               }

               deodexedInstruction = new ImmutableInstruction35c(opcode, instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), (Reference)resolvedMethod);
            }

            analyzedInstruction.setDeodexedInstruction((Instruction)deodexedInstruction);
            this.analyzeInstruction(analyzedInstruction);
            return true;
         }
      }
   }

   private boolean analyzePutGetVolatile(@Nonnull AnalyzedInstruction analyzedInstruction) {
      return this.analyzePutGetVolatile(analyzedInstruction, true);
   }

   private boolean analyzePutGetVolatile(@Nonnull AnalyzedInstruction analyzedInstruction, boolean analyzeResult) {
      FieldReference field = (FieldReference)((ReferenceInstruction)analyzedInstruction.instruction).getReference();
      String fieldType = field.getType();
      Opcode originalOpcode = analyzedInstruction.instruction.getOpcode();
      Opcode opcode = this.classPath.getFieldInstructionMapper().getAndCheckDeodexedOpcode(fieldType, originalOpcode);
      Object deodexedInstruction;
      if (originalOpcode.isStaticFieldAccessor()) {
         OneRegisterInstruction instruction = (OneRegisterInstruction)analyzedInstruction.instruction;
         deodexedInstruction = new ImmutableInstruction21c(opcode, instruction.getRegisterA(), field);
      } else {
         TwoRegisterInstruction instruction = (TwoRegisterInstruction)analyzedInstruction.instruction;
         deodexedInstruction = new ImmutableInstruction22c(opcode, instruction.getRegisterA(), instruction.getRegisterB(), field);
      }

      analyzedInstruction.setDeodexedInstruction((Instruction)deodexedInstruction);
      if (analyzeResult) {
         this.analyzeInstruction(analyzedInstruction);
      }

      return true;
   }

   @Nonnull
   private static RegisterType getAndCheckSourceRegister(@Nonnull AnalyzedInstruction analyzedInstruction, int registerNumber, BitSet validCategories) {
      assert registerNumber >= 0 && registerNumber < analyzedInstruction.postRegisterMap.length;

      RegisterType registerType = analyzedInstruction.getPreInstructionRegisterType(registerNumber);
      checkRegister(registerType, registerNumber, validCategories);
      if (validCategories == WideLowCategories) {
         checkRegister(registerType, registerNumber, WideLowCategories);
         checkWidePair(registerNumber, analyzedInstruction);
         RegisterType secondRegisterType = analyzedInstruction.getPreInstructionRegisterType(registerNumber + 1);
         checkRegister(secondRegisterType, registerNumber + 1, WideHighCategories);
      }

      return registerType;
   }

   private static void checkRegister(RegisterType registerType, int registerNumber, BitSet validCategories) {
      if (!validCategories.get(registerType.category)) {
         throw new AnalysisException(String.format("Invalid register type %s for register v%d.", registerType.toString(), registerNumber), new Object[0]);
      }
   }

   private static void checkWidePair(int registerNumber, AnalyzedInstruction analyzedInstruction) {
      if (registerNumber + 1 >= analyzedInstruction.postRegisterMap.length) {
         throw new AnalysisException(String.format("v%d cannot be used as the first register in a wide registerpair because it is the last register.", registerNumber), new Object[0]);
      }
   }

   @Nullable
   private MethodReference normalizeMethodReference(@Nonnull MethodReference methodRef) {
      TypeProto typeProto = this.classPath.getClass(methodRef.getDefiningClass());

      int methodIndex;
      try {
         methodIndex = typeProto.findMethodIndexInVtable(methodRef);
      } catch (UnresolvedClassException var8) {
         return null;
      }

      if (methodIndex < 0) {
         return null;
      } else {
         ClassProto thisClass = (ClassProto)this.classPath.getClass(this.method.getDefiningClass());
         Method replacementMethod = typeProto.getMethodByVtableIndex(methodIndex);

         assert replacementMethod != null;

         while(true) {
            String superType = typeProto.getSuperclass();
            if (superType == null) {
               break;
            }

            typeProto = this.classPath.getClass(superType);
            Method resolvedMethod = typeProto.getMethodByVtableIndex(methodIndex);
            if (resolvedMethod == null) {
               break;
            }

            if (!resolvedMethod.equals(replacementMethod) && AnalyzedMethodUtil.canAccess(thisClass, resolvedMethod, false, false, true)) {
               replacementMethod = resolvedMethod;
            }
         }

         return replacementMethod;
      }
   }

   private static class ReparentedMethodReference extends BaseMethodReference {
      private final MethodReference baseReference;
      private final String definingClass;

      public ReparentedMethodReference(MethodReference baseReference, String definingClass) {
         this.baseReference = baseReference;
         this.definingClass = definingClass;
      }

      @Nonnull
      public String getName() {
         return this.baseReference.getName();
      }

      @Nonnull
      public List<? extends CharSequence> getParameterTypes() {
         return this.baseReference.getParameterTypes();
      }

      @Nonnull
      public String getReturnType() {
         return this.baseReference.getReturnType();
      }

      @Nonnull
      public String getDefiningClass() {
         return this.definingClass;
      }
   }
}
