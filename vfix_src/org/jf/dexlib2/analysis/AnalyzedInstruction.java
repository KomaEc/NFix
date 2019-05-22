package org.jf.dexlib2.analysis;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.FiveRegisterInstruction;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.RegisterRangeInstruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.util.ExceptionWithContext;

public class AnalyzedInstruction implements Comparable<AnalyzedInstruction> {
   @Nonnull
   protected final MethodAnalyzer methodAnalyzer;
   @Nonnull
   protected Instruction instruction;
   protected final int instructionIndex;
   @Nonnull
   protected final TreeSet<AnalyzedInstruction> predecessors = new TreeSet();
   @Nonnull
   protected final LinkedList<AnalyzedInstruction> successors = new LinkedList();
   @Nonnull
   protected final RegisterType[] preRegisterMap;
   @Nonnull
   protected final RegisterType[] postRegisterMap;
   @Nullable
   protected Map<AnalyzedInstruction.PredecessorOverrideKey, RegisterType> predecessorRegisterOverrides = null;
   protected final Instruction originalInstruction;

   public AnalyzedInstruction(@Nonnull MethodAnalyzer methodAnalyzer, @Nonnull Instruction instruction, int instructionIndex, int registerCount) {
      this.methodAnalyzer = methodAnalyzer;
      this.instruction = instruction;
      this.originalInstruction = instruction;
      this.instructionIndex = instructionIndex;
      this.postRegisterMap = new RegisterType[registerCount];
      this.preRegisterMap = new RegisterType[registerCount];
      RegisterType unknown = RegisterType.getRegisterType((byte)0, (TypeProto)null);

      for(int i = 0; i < registerCount; ++i) {
         this.preRegisterMap[i] = unknown;
         this.postRegisterMap[i] = unknown;
      }

   }

   public int getInstructionIndex() {
      return this.instructionIndex;
   }

   public int getPredecessorCount() {
      return this.predecessors.size();
   }

   public SortedSet<AnalyzedInstruction> getPredecessors() {
      return Collections.unmodifiableSortedSet(this.predecessors);
   }

   public RegisterType getPredecessorRegisterType(@Nonnull AnalyzedInstruction predecessor, int registerNumber) {
      if (this.predecessorRegisterOverrides != null) {
         RegisterType override = (RegisterType)this.predecessorRegisterOverrides.get(new AnalyzedInstruction.PredecessorOverrideKey(predecessor, registerNumber));
         if (override != null) {
            return override;
         }
      }

      return predecessor.postRegisterMap[registerNumber];
   }

   protected boolean addPredecessor(AnalyzedInstruction predecessor) {
      return this.predecessors.add(predecessor);
   }

   protected void addSuccessor(AnalyzedInstruction successor) {
      this.successors.add(successor);
   }

   protected void setDeodexedInstruction(Instruction instruction) {
      assert this.originalInstruction.getOpcode().odexOnly();

      this.instruction = instruction;
   }

   protected void restoreOdexedInstruction() {
      assert this.originalInstruction.getOpcode().odexOnly();

      this.instruction = this.originalInstruction;
   }

   @Nonnull
   public List<AnalyzedInstruction> getSuccessors() {
      return Collections.unmodifiableList(this.successors);
   }

   @Nonnull
   public Instruction getInstruction() {
      return this.instruction;
   }

   @Nonnull
   public Instruction getOriginalInstruction() {
      return this.originalInstruction;
   }

   public boolean isBeginningInstruction() {
      if (this.predecessors.size() == 0) {
         return false;
      } else {
         return ((AnalyzedInstruction)this.predecessors.first()).instructionIndex == -1;
      }
   }

   protected boolean mergeRegister(int registerNumber, RegisterType registerType, BitSet verifiedInstructions, boolean override) {
      assert registerNumber >= 0 && registerNumber < this.postRegisterMap.length;

      assert registerType != null;

      RegisterType oldRegisterType = this.preRegisterMap[registerNumber];
      RegisterType mergedRegisterType;
      if (override) {
         mergedRegisterType = this.getMergedPreRegisterTypeFromPredecessors(registerNumber);
      } else {
         mergedRegisterType = oldRegisterType.merge(registerType);
      }

      if (mergedRegisterType.equals(oldRegisterType)) {
         return false;
      } else {
         this.preRegisterMap[registerNumber] = mergedRegisterType;
         verifiedInstructions.clear(this.instructionIndex);
         if (!this.setsRegister(registerNumber)) {
            this.postRegisterMap[registerNumber] = mergedRegisterType;
            return true;
         } else {
            return false;
         }
      }
   }

   @Nonnull
   protected RegisterType getMergedPreRegisterTypeFromPredecessors(int registerNumber) {
      RegisterType mergedRegisterType = null;
      Iterator var3 = this.predecessors.iterator();

      while(var3.hasNext()) {
         AnalyzedInstruction predecessor = (AnalyzedInstruction)var3.next();
         RegisterType predecessorRegisterType = this.getPredecessorRegisterType(predecessor, registerNumber);
         if (predecessorRegisterType != null) {
            if (mergedRegisterType == null) {
               mergedRegisterType = predecessorRegisterType;
            } else {
               mergedRegisterType = predecessorRegisterType.merge(mergedRegisterType);
            }
         }
      }

      if (mergedRegisterType == null) {
         throw new IllegalStateException();
      } else {
         return mergedRegisterType;
      }
   }

   protected boolean setPostRegisterType(int registerNumber, RegisterType registerType) {
      assert registerNumber >= 0 && registerNumber < this.postRegisterMap.length;

      assert registerType != null;

      RegisterType oldRegisterType = this.postRegisterMap[registerNumber];
      if (oldRegisterType.equals(registerType)) {
         return false;
      } else {
         this.postRegisterMap[registerNumber] = registerType;
         return true;
      }
   }

   protected boolean overridePredecessorRegisterType(@Nonnull AnalyzedInstruction predecessor, int registerNumber, @Nonnull RegisterType registerType, BitSet verifiedInstructions) {
      if (this.predecessorRegisterOverrides == null) {
         this.predecessorRegisterOverrides = Maps.newHashMap();
      }

      this.predecessorRegisterOverrides.put(new AnalyzedInstruction.PredecessorOverrideKey(predecessor, registerNumber), registerType);
      RegisterType mergedType = this.getMergedPreRegisterTypeFromPredecessors(registerNumber);
      if (this.preRegisterMap[registerNumber].equals(mergedType)) {
         return false;
      } else {
         this.preRegisterMap[registerNumber] = mergedType;
         verifiedInstructions.clear(this.instructionIndex);
         if (!this.setsRegister(registerNumber) && !this.postRegisterMap[registerNumber].equals(mergedType)) {
            this.postRegisterMap[registerNumber] = mergedType;
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean isInvokeInit() {
      if (!this.instruction.getOpcode().canInitializeReference()) {
         return false;
      } else {
         ReferenceInstruction instruction = (ReferenceInstruction)this.instruction;
         Reference reference = instruction.getReference();
         return reference instanceof MethodReference ? ((MethodReference)reference).getName().equals("<init>") : false;
      }
   }

   public boolean setsRegister(int registerNumber) {
      int destinationRegister;
      if (this.isInvokeInit()) {
         if (this.instruction instanceof FiveRegisterInstruction) {
            assert ((FiveRegisterInstruction)this.instruction).getRegisterCount() > 0;

            destinationRegister = ((FiveRegisterInstruction)this.instruction).getRegisterC();
         } else {
            assert this.instruction instanceof RegisterRangeInstruction;

            RegisterRangeInstruction rangeInstruction = (RegisterRangeInstruction)this.instruction;

            assert rangeInstruction.getRegisterCount() > 0;

            destinationRegister = rangeInstruction.getStartRegister();
         }

         RegisterType preInstructionDestRegisterType = this.getPreInstructionRegisterType(destinationRegister);
         if (preInstructionDestRegisterType.category == 0) {
            RegisterType preInstructionRegisterType = this.getPreInstructionRegisterType(registerNumber);
            if (preInstructionRegisterType.category == 16 || preInstructionRegisterType.category == 17) {
               return true;
            }
         }

         if (preInstructionDestRegisterType.category != 16 && preInstructionDestRegisterType.category != 17) {
            return false;
         } else if (registerNumber == destinationRegister) {
            return true;
         } else {
            return preInstructionDestRegisterType.equals(this.getPreInstructionRegisterType(registerNumber));
         }
      } else {
         if (this.instructionIndex > 0 && this.methodAnalyzer.getClassPath().isArt() && this.getPredecessorCount() == 1 && (this.instruction.getOpcode() == Opcode.IF_EQZ || this.instruction.getOpcode() == Opcode.IF_NEZ)) {
            AnalyzedInstruction prevInstruction = (AnalyzedInstruction)this.predecessors.first();
            if (prevInstruction.instruction.getOpcode() == Opcode.INSTANCE_OF && MethodAnalyzer.canPropagateTypeAfterInstanceOf(prevInstruction, this, this.methodAnalyzer.getClassPath())) {
               Instruction22c instanceOfInstruction = (Instruction22c)prevInstruction.instruction;
               if (registerNumber == instanceOfInstruction.getRegisterB()) {
                  return true;
               }

               if (this.instructionIndex > 1) {
                  int originalSourceRegister = -1;
                  RegisterType newType = null;
                  Iterator var6 = prevInstruction.predecessors.iterator();

                  while(var6.hasNext()) {
                     AnalyzedInstruction prevPrevAnalyzedInstruction = (AnalyzedInstruction)var6.next();
                     Opcode opcode = prevPrevAnalyzedInstruction.instruction.getOpcode();
                     if (opcode != Opcode.MOVE_OBJECT && opcode != Opcode.MOVE_OBJECT_16 && opcode != Opcode.MOVE_OBJECT_FROM16) {
                        originalSourceRegister = -1;
                        break;
                     }

                     TwoRegisterInstruction moveInstruction = (TwoRegisterInstruction)prevPrevAnalyzedInstruction.instruction;
                     RegisterType originalType = prevPrevAnalyzedInstruction.getPostInstructionRegisterType(moveInstruction.getRegisterB());
                     if (moveInstruction.getRegisterA() != instanceOfInstruction.getRegisterB()) {
                        originalSourceRegister = -1;
                        break;
                     }

                     if (originalType.type == null) {
                        originalSourceRegister = -1;
                        break;
                     }

                     if (newType == null) {
                        newType = RegisterType.getRegisterType(this.methodAnalyzer.getClassPath(), (TypeReference)instanceOfInstruction.getReference());
                     }

                     if (MethodAnalyzer.isNotWideningConversion(originalType, newType)) {
                        if (originalSourceRegister != -1) {
                           if (originalSourceRegister != moveInstruction.getRegisterB()) {
                              originalSourceRegister = -1;
                              break;
                           }
                        } else {
                           originalSourceRegister = moveInstruction.getRegisterB();
                        }
                     }
                  }

                  if (originalSourceRegister != -1 && registerNumber == originalSourceRegister) {
                     return true;
                  }
               }
            }
         }

         if (!this.instruction.getOpcode().setsRegister()) {
            return false;
         } else {
            destinationRegister = this.getDestinationRegister();
            if (registerNumber == destinationRegister) {
               return true;
            } else {
               return this.instruction.getOpcode().setsWideRegister() && registerNumber == destinationRegister + 1;
            }
         }
      }
   }

   public List<Integer> getSetRegisters() {
      List<Integer> setRegisters = Lists.newArrayList();
      if (this.instruction.getOpcode().setsRegister()) {
         setRegisters.add(this.getDestinationRegister());
      }

      if (this.instruction.getOpcode().setsWideRegister()) {
         setRegisters.add(this.getDestinationRegister() + 1);
      }

      int originalSourceRegister;
      RegisterType newType;
      if (this.isInvokeInit()) {
         int destinationRegister;
         if (this.instruction instanceof FiveRegisterInstruction) {
            destinationRegister = ((FiveRegisterInstruction)this.instruction).getRegisterC();

            assert ((FiveRegisterInstruction)this.instruction).getRegisterCount() > 0;
         } else {
            assert this.instruction instanceof RegisterRangeInstruction;

            RegisterRangeInstruction rangeInstruction = (RegisterRangeInstruction)this.instruction;

            assert rangeInstruction.getRegisterCount() > 0;

            destinationRegister = rangeInstruction.getStartRegister();
         }

         RegisterType preInstructionDestRegisterType = this.getPreInstructionRegisterType(destinationRegister);
         if (preInstructionDestRegisterType.category != 16 && preInstructionDestRegisterType.category != 17) {
            if (preInstructionDestRegisterType.category == 0) {
               for(originalSourceRegister = 0; originalSourceRegister < this.preRegisterMap.length; ++originalSourceRegister) {
                  newType = this.preRegisterMap[originalSourceRegister];
                  if (newType.category == 16 || newType.category == 17) {
                     setRegisters.add(originalSourceRegister);
                  }
               }
            }
         } else {
            setRegisters.add(destinationRegister);
            RegisterType objectRegisterType = this.preRegisterMap[destinationRegister];

            for(int i = 0; i < this.preRegisterMap.length; ++i) {
               if (i != destinationRegister) {
                  RegisterType preInstructionRegisterType = this.preRegisterMap[i];
                  if (preInstructionRegisterType.equals(objectRegisterType)) {
                     setRegisters.add(i);
                  } else if (preInstructionRegisterType.category == 16 || preInstructionRegisterType.category == 17) {
                     RegisterType postInstructionRegisterType = this.postRegisterMap[i];
                     if (postInstructionRegisterType.category == 0) {
                        setRegisters.add(i);
                     }
                  }
               }
            }
         }
      }

      if (this.instructionIndex > 0 && this.methodAnalyzer.getClassPath().isArt() && this.getPredecessorCount() == 1 && (this.instruction.getOpcode() == Opcode.IF_EQZ || this.instruction.getOpcode() == Opcode.IF_NEZ)) {
         AnalyzedInstruction prevInstruction = (AnalyzedInstruction)this.predecessors.first();
         if (prevInstruction.instruction.getOpcode() == Opcode.INSTANCE_OF && MethodAnalyzer.canPropagateTypeAfterInstanceOf(prevInstruction, this, this.methodAnalyzer.getClassPath())) {
            Instruction22c instanceOfInstruction = (Instruction22c)prevInstruction.instruction;
            setRegisters.add(instanceOfInstruction.getRegisterB());
            if (this.instructionIndex > 1) {
               originalSourceRegister = -1;
               newType = null;
               Iterator var16 = prevInstruction.predecessors.iterator();

               while(var16.hasNext()) {
                  AnalyzedInstruction prevPrevAnalyzedInstruction = (AnalyzedInstruction)var16.next();
                  Opcode opcode = prevPrevAnalyzedInstruction.instruction.getOpcode();
                  if (opcode != Opcode.MOVE_OBJECT && opcode != Opcode.MOVE_OBJECT_16 && opcode != Opcode.MOVE_OBJECT_FROM16) {
                     originalSourceRegister = -1;
                     break;
                  }

                  TwoRegisterInstruction moveInstruction = (TwoRegisterInstruction)prevPrevAnalyzedInstruction.instruction;
                  RegisterType originalType = prevPrevAnalyzedInstruction.getPostInstructionRegisterType(moveInstruction.getRegisterB());
                  if (moveInstruction.getRegisterA() != instanceOfInstruction.getRegisterB()) {
                     originalSourceRegister = -1;
                     break;
                  }

                  if (originalType.type == null) {
                     originalSourceRegister = -1;
                     break;
                  }

                  if (newType == null) {
                     newType = RegisterType.getRegisterType(this.methodAnalyzer.getClassPath(), (TypeReference)instanceOfInstruction.getReference());
                  }

                  if (MethodAnalyzer.isNotWideningConversion(originalType, newType)) {
                     if (originalSourceRegister != -1) {
                        if (originalSourceRegister != moveInstruction.getRegisterB()) {
                           originalSourceRegister = -1;
                           break;
                        }
                     } else {
                        originalSourceRegister = moveInstruction.getRegisterB();
                     }
                  }
               }

               if (originalSourceRegister != -1) {
                  setRegisters.add(originalSourceRegister);
               }
            }
         }
      }

      return setRegisters;
   }

   public int getDestinationRegister() {
      if (!this.instruction.getOpcode().setsRegister()) {
         throw new ExceptionWithContext("Cannot call getDestinationRegister() for an instruction that doesn't store a value", new Object[0]);
      } else {
         return ((OneRegisterInstruction)this.instruction).getRegisterA();
      }
   }

   public int getRegisterCount() {
      return this.postRegisterMap.length;
   }

   @Nonnull
   public RegisterType getPostInstructionRegisterType(int registerNumber) {
      return this.postRegisterMap[registerNumber];
   }

   @Nonnull
   public RegisterType getPreInstructionRegisterType(int registerNumber) {
      return this.preRegisterMap[registerNumber];
   }

   public int compareTo(@Nonnull AnalyzedInstruction analyzedInstruction) {
      if (this.instructionIndex < analyzedInstruction.instructionIndex) {
         return -1;
      } else {
         return this.instructionIndex == analyzedInstruction.instructionIndex ? 0 : 1;
      }
   }

   private static class PredecessorOverrideKey {
      public final AnalyzedInstruction analyzedInstruction;
      public final int registerNumber;

      public PredecessorOverrideKey(AnalyzedInstruction analyzedInstruction, int registerNumber) {
         this.analyzedInstruction = analyzedInstruction;
         this.registerNumber = registerNumber;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            AnalyzedInstruction.PredecessorOverrideKey that = (AnalyzedInstruction.PredecessorOverrideKey)o;
            return Objects.equal(this.registerNumber, that.registerNumber) && Objects.equal(this.analyzedInstruction, that.analyzedInstruction);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.analyzedInstruction, this.registerNumber);
      }
   }
}
