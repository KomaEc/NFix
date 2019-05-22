package org.jf.dexlib2.builder;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.debug.BuilderEndLocal;
import org.jf.dexlib2.builder.debug.BuilderEpilogueBegin;
import org.jf.dexlib2.builder.debug.BuilderLineNumber;
import org.jf.dexlib2.builder.debug.BuilderPrologueEnd;
import org.jf.dexlib2.builder.debug.BuilderRestartLocal;
import org.jf.dexlib2.builder.debug.BuilderSetSourceFile;
import org.jf.dexlib2.builder.debug.BuilderStartLocal;
import org.jf.dexlib2.builder.instruction.BuilderArrayPayload;
import org.jf.dexlib2.builder.instruction.BuilderInstruction10t;
import org.jf.dexlib2.builder.instruction.BuilderInstruction10x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction11n;
import org.jf.dexlib2.builder.instruction.BuilderInstruction11x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction12x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction20bc;
import org.jf.dexlib2.builder.instruction.BuilderInstruction20t;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21c;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21ih;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21lh;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21s;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21t;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22b;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22c;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22cs;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22s;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22t;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction23x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction30t;
import org.jf.dexlib2.builder.instruction.BuilderInstruction31c;
import org.jf.dexlib2.builder.instruction.BuilderInstruction31i;
import org.jf.dexlib2.builder.instruction.BuilderInstruction31t;
import org.jf.dexlib2.builder.instruction.BuilderInstruction32x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction35c;
import org.jf.dexlib2.builder.instruction.BuilderInstruction35mi;
import org.jf.dexlib2.builder.instruction.BuilderInstruction35ms;
import org.jf.dexlib2.builder.instruction.BuilderInstruction3rc;
import org.jf.dexlib2.builder.instruction.BuilderInstruction3rmi;
import org.jf.dexlib2.builder.instruction.BuilderInstruction3rms;
import org.jf.dexlib2.builder.instruction.BuilderInstruction51l;
import org.jf.dexlib2.builder.instruction.BuilderPackedSwitchPayload;
import org.jf.dexlib2.builder.instruction.BuilderSparseSwitchPayload;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.debug.EndLocal;
import org.jf.dexlib2.iface.debug.LineNumber;
import org.jf.dexlib2.iface.debug.RestartLocal;
import org.jf.dexlib2.iface.debug.SetSourceFile;
import org.jf.dexlib2.iface.debug.StartLocal;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload;
import org.jf.dexlib2.iface.instruction.formats.Instruction10t;
import org.jf.dexlib2.iface.instruction.formats.Instruction10x;
import org.jf.dexlib2.iface.instruction.formats.Instruction11n;
import org.jf.dexlib2.iface.instruction.formats.Instruction11x;
import org.jf.dexlib2.iface.instruction.formats.Instruction12x;
import org.jf.dexlib2.iface.instruction.formats.Instruction20bc;
import org.jf.dexlib2.iface.instruction.formats.Instruction20t;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.instruction.formats.Instruction21ih;
import org.jf.dexlib2.iface.instruction.formats.Instruction21lh;
import org.jf.dexlib2.iface.instruction.formats.Instruction21s;
import org.jf.dexlib2.iface.instruction.formats.Instruction21t;
import org.jf.dexlib2.iface.instruction.formats.Instruction22b;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.instruction.formats.Instruction22cs;
import org.jf.dexlib2.iface.instruction.formats.Instruction22s;
import org.jf.dexlib2.iface.instruction.formats.Instruction22t;
import org.jf.dexlib2.iface.instruction.formats.Instruction22x;
import org.jf.dexlib2.iface.instruction.formats.Instruction23x;
import org.jf.dexlib2.iface.instruction.formats.Instruction30t;
import org.jf.dexlib2.iface.instruction.formats.Instruction31c;
import org.jf.dexlib2.iface.instruction.formats.Instruction31i;
import org.jf.dexlib2.iface.instruction.formats.Instruction31t;
import org.jf.dexlib2.iface.instruction.formats.Instruction32x;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.instruction.formats.Instruction35mi;
import org.jf.dexlib2.iface.instruction.formats.Instruction35ms;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rmi;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rms;
import org.jf.dexlib2.iface.instruction.formats.Instruction51l;
import org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.util.ExceptionWithContext;

public class MutableMethodImplementation implements MethodImplementation {
   private final int registerCount;
   final ArrayList<MethodLocation> instructionList = Lists.newArrayList((Object[])(new MethodLocation((BuilderInstruction)null, 0, 0)));
   private final ArrayList<BuilderTryBlock> tryBlocks = Lists.newArrayList();
   private boolean fixInstructions = true;

   public MutableMethodImplementation(@Nonnull MethodImplementation methodImplementation) {
      this.registerCount = methodImplementation.getRegisterCount();
      int codeAddress = 0;
      int index = 0;
      Iterator var4 = methodImplementation.getInstructions().iterator();

      while(var4.hasNext()) {
         Instruction instruction = (Instruction)var4.next();
         codeAddress += instruction.getCodeUnits();
         ++index;
         this.instructionList.add(new MethodLocation((BuilderInstruction)null, codeAddress, index));
      }

      final int[] codeAddressToIndex = new int[codeAddress + 1];
      Arrays.fill(codeAddressToIndex, -1);

      for(int i = 0; i < this.instructionList.size(); codeAddressToIndex[((MethodLocation)this.instructionList.get(i)).codeAddress] = i++) {
      }

      List<MutableMethodImplementation.Task> switchPayloadTasks = Lists.newArrayList();
      index = 0;

      Iterator var6;
      for(var6 = methodImplementation.getInstructions().iterator(); var6.hasNext(); ++index) {
         final Instruction instruction = (Instruction)var6.next();
         final MethodLocation location = (MethodLocation)this.instructionList.get(index);
         Opcode opcode = instruction.getOpcode();
         if (opcode != Opcode.PACKED_SWITCH_PAYLOAD && opcode != Opcode.SPARSE_SWITCH_PAYLOAD) {
            this.convertAndSetInstruction(location, codeAddressToIndex, instruction);
         } else {
            switchPayloadTasks.add(new MutableMethodImplementation.Task() {
               public void perform() {
                  MutableMethodImplementation.this.convertAndSetInstruction(location, codeAddressToIndex, instruction);
               }
            });
         }
      }

      var6 = switchPayloadTasks.iterator();

      while(var6.hasNext()) {
         MutableMethodImplementation.Task switchPayloadTask = (MutableMethodImplementation.Task)var6.next();
         switchPayloadTask.perform();
      }

      MethodLocation debugLocation;
      BuilderDebugItem builderDebugItem;
      for(var6 = methodImplementation.getDebugItems().iterator(); var6.hasNext(); builderDebugItem.location = debugLocation) {
         DebugItem debugItem = (DebugItem)var6.next();
         int debugCodeAddress = debugItem.getCodeAddress();
         int locationIndex = this.mapCodeAddressToIndex(codeAddressToIndex, debugCodeAddress);
         debugLocation = (MethodLocation)this.instructionList.get(locationIndex);
         builderDebugItem = this.convertDebugItem(debugItem);
         debugLocation.getDebugItems().add(builderDebugItem);
      }

      var6 = methodImplementation.getTryBlocks().iterator();

      while(var6.hasNext()) {
         TryBlock<? extends ExceptionHandler> tryBlock = (TryBlock)var6.next();
         Label startLabel = this.newLabel(codeAddressToIndex, tryBlock.getStartCodeAddress());
         Label endLabel = this.newLabel(codeAddressToIndex, tryBlock.getStartCodeAddress() + tryBlock.getCodeUnitCount());
         Iterator var22 = tryBlock.getExceptionHandlers().iterator();

         while(var22.hasNext()) {
            ExceptionHandler exceptionHandler = (ExceptionHandler)var22.next();
            this.tryBlocks.add(new BuilderTryBlock(startLabel, endLabel, exceptionHandler.getExceptionTypeReference(), this.newLabel(codeAddressToIndex, exceptionHandler.getHandlerCodeAddress())));
         }
      }

   }

   public MutableMethodImplementation(int registerCount) {
      this.registerCount = registerCount;
   }

   public int getRegisterCount() {
      return this.registerCount;
   }

   @Nonnull
   public List<BuilderInstruction> getInstructions() {
      if (this.fixInstructions) {
         this.fixInstructions();
      }

      return new AbstractList<BuilderInstruction>() {
         public BuilderInstruction get(int i) {
            if (i >= this.size()) {
               throw new IndexOutOfBoundsException();
            } else {
               if (MutableMethodImplementation.this.fixInstructions) {
                  MutableMethodImplementation.this.fixInstructions();
               }

               return ((MethodLocation)MutableMethodImplementation.this.instructionList.get(i)).instruction;
            }
         }

         public int size() {
            if (MutableMethodImplementation.this.fixInstructions) {
               MutableMethodImplementation.this.fixInstructions();
            }

            return MutableMethodImplementation.this.instructionList.size() - 1;
         }
      };
   }

   @Nonnull
   public List<BuilderTryBlock> getTryBlocks() {
      if (this.fixInstructions) {
         this.fixInstructions();
      }

      return Collections.unmodifiableList(this.tryBlocks);
   }

   @Nonnull
   public Iterable<? extends DebugItem> getDebugItems() {
      if (this.fixInstructions) {
         this.fixInstructions();
      }

      return Iterables.concat(Iterables.transform(this.instructionList, new Function<MethodLocation, Iterable<? extends DebugItem>>() {
         @Nullable
         public Iterable<? extends DebugItem> apply(@Nullable MethodLocation input) {
            assert input != null;

            if (MutableMethodImplementation.this.fixInstructions) {
               throw new IllegalStateException("This iterator was invalidated by a change to this MutableMethodImplementation.");
            } else {
               return input.getDebugItems();
            }
         }
      }));
   }

   public void addCatch(@Nullable TypeReference type, @Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
      this.tryBlocks.add(new BuilderTryBlock(from, to, type, handler));
   }

   public void addCatch(@Nullable String type, @Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
      this.tryBlocks.add(new BuilderTryBlock(from, to, type, handler));
   }

   public void addCatch(@Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
      this.tryBlocks.add(new BuilderTryBlock(from, to, handler));
   }

   public void addInstruction(int index, BuilderInstruction instruction) {
      if (index >= this.instructionList.size()) {
         throw new IndexOutOfBoundsException();
      } else if (index == this.instructionList.size() - 1) {
         this.addInstruction(instruction);
      } else {
         int codeAddress = ((MethodLocation)this.instructionList.get(index)).getCodeAddress();
         MethodLocation newLoc = new MethodLocation(instruction, codeAddress, index);
         this.instructionList.add(index, newLoc);
         instruction.location = newLoc;
         codeAddress += instruction.getCodeUnits();

         for(int i = index + 1; i < this.instructionList.size(); ++i) {
            MethodLocation location = (MethodLocation)this.instructionList.get(i);
            ++location.index;
            location.codeAddress = codeAddress;
            if (location.instruction != null) {
               codeAddress += location.instruction.getCodeUnits();
            } else {
               assert i == this.instructionList.size() - 1;
            }
         }

         this.fixInstructions = true;
      }
   }

   public void addInstruction(@Nonnull BuilderInstruction instruction) {
      MethodLocation last = (MethodLocation)this.instructionList.get(this.instructionList.size() - 1);
      last.instruction = instruction;
      instruction.location = last;
      int nextCodeAddress = last.codeAddress + instruction.getCodeUnits();
      this.instructionList.add(new MethodLocation((BuilderInstruction)null, nextCodeAddress, this.instructionList.size()));
      this.fixInstructions = true;
   }

   public void replaceInstruction(int index, @Nonnull BuilderInstruction replacementInstruction) {
      if (index >= this.instructionList.size() - 1) {
         throw new IndexOutOfBoundsException();
      } else {
         MethodLocation replaceLocation = (MethodLocation)this.instructionList.get(index);
         replacementInstruction.location = replaceLocation;
         BuilderInstruction old = replaceLocation.instruction;

         assert old != null;

         old.location = null;
         replaceLocation.instruction = replacementInstruction;
         int codeAddress = replaceLocation.codeAddress + replaceLocation.instruction.getCodeUnits();

         for(int i = index + 1; i < this.instructionList.size(); ++i) {
            MethodLocation location = (MethodLocation)this.instructionList.get(i);
            location.codeAddress = codeAddress;
            Instruction instruction = location.getInstruction();
            if (instruction != null) {
               codeAddress += instruction.getCodeUnits();
            } else {
               assert i == this.instructionList.size() - 1;
            }
         }

         this.fixInstructions = true;
      }
   }

   public void removeInstruction(int index) {
      if (index >= this.instructionList.size() - 1) {
         throw new IndexOutOfBoundsException();
      } else {
         MethodLocation toRemove = (MethodLocation)this.instructionList.get(index);
         toRemove.instruction = null;
         MethodLocation next = (MethodLocation)this.instructionList.get(index + 1);
         toRemove.mergeInto(next);
         this.instructionList.remove(index);
         int codeAddress = toRemove.codeAddress;

         for(int i = index; i < this.instructionList.size(); ++i) {
            MethodLocation location = (MethodLocation)this.instructionList.get(i);
            location.index = i;
            location.codeAddress = codeAddress;
            Instruction instruction = location.getInstruction();
            if (instruction != null) {
               codeAddress += instruction.getCodeUnits();
            } else {
               assert i == this.instructionList.size() - 1;
            }
         }

         this.fixInstructions = true;
      }
   }

   public void swapInstructions(int index1, int index2) {
      if (index1 < this.instructionList.size() - 1 && index2 < this.instructionList.size() - 1) {
         MethodLocation first = (MethodLocation)this.instructionList.get(index1);
         MethodLocation second = (MethodLocation)this.instructionList.get(index2);

         assert first.instruction != null;

         assert second.instruction != null;

         first.instruction.location = second;
         second.instruction.location = first;
         BuilderInstruction tmp = second.instruction;
         second.instruction = first.instruction;
         first.instruction = tmp;
         int codeAddress;
         if (index2 < index1) {
            codeAddress = index2;
            index2 = index1;
            index1 = codeAddress;
         }

         codeAddress = first.codeAddress + first.instruction.getCodeUnits();

         for(int i = index1 + 1; i <= index2; ++i) {
            MethodLocation location = (MethodLocation)this.instructionList.get(i);
            location.codeAddress = codeAddress;
            Instruction instruction = location.instruction;

            assert instruction != null;

            codeAddress += location.instruction.getCodeUnits();
         }

         this.fixInstructions = true;
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   @Nullable
   private BuilderInstruction getFirstNonNop(int startIndex) {
      for(int i = startIndex; i < this.instructionList.size() - 1; ++i) {
         BuilderInstruction instruction = ((MethodLocation)this.instructionList.get(i)).instruction;

         assert instruction != null;

         if (instruction.getOpcode() != Opcode.NOP) {
            return instruction;
         }
      }

      return null;
   }

   private void fixInstructions() {
      HashSet<MethodLocation> payloadLocations = Sets.newHashSet();
      Iterator var2 = this.instructionList.iterator();

      while(true) {
         while(true) {
            MethodLocation location;
            BuilderInstruction instruction;
            do {
               if (!var2.hasNext()) {
                  boolean madeChanges;
                  do {
                     madeChanges = false;

                     for(int index = 0; index < this.instructionList.size(); ++index) {
                        MethodLocation location = (MethodLocation)this.instructionList.get(index);
                        BuilderInstruction instruction = location.instruction;
                        if (instruction != null) {
                           int previousIndex;
                           switch(instruction.getOpcode()) {
                           case GOTO:
                              previousIndex = ((BuilderOffsetInstruction)instruction).internalGetCodeOffset();
                              if (previousIndex >= -128 && previousIndex <= 127) {
                                 break;
                              }

                              Object replacement;
                              if (previousIndex >= -32768 && previousIndex <= 32767) {
                                 replacement = new BuilderInstruction20t(Opcode.GOTO_16, ((BuilderOffsetInstruction)instruction).getTarget());
                              } else {
                                 replacement = new BuilderInstruction30t(Opcode.GOTO_32, ((BuilderOffsetInstruction)instruction).getTarget());
                              }

                              this.replaceInstruction(location.index, (BuilderInstruction)replacement);
                              madeChanges = true;
                              break;
                           case GOTO_16:
                              previousIndex = ((BuilderOffsetInstruction)instruction).internalGetCodeOffset();
                              if (previousIndex < -32768 || previousIndex > 32767) {
                                 BuilderOffsetInstruction replacement = new BuilderInstruction30t(Opcode.GOTO_32, ((BuilderOffsetInstruction)instruction).getTarget());
                                 this.replaceInstruction(location.index, replacement);
                                 madeChanges = true;
                              }
                              break;
                           case SPARSE_SWITCH_PAYLOAD:
                           case PACKED_SWITCH_PAYLOAD:
                              if (((BuilderSwitchPayload)instruction).referrer == null) {
                                 this.removeInstruction(index);
                                 --index;
                                 madeChanges = true;
                                 break;
                              }
                           case ARRAY_PAYLOAD:
                              if ((location.codeAddress & 1) != 0) {
                                 previousIndex = location.index - 1;
                                 MethodLocation previousLocation = (MethodLocation)this.instructionList.get(previousIndex);
                                 Instruction previousInstruction = previousLocation.instruction;

                                 assert previousInstruction != null;

                                 if (previousInstruction.getOpcode() == Opcode.NOP) {
                                    this.removeInstruction(previousIndex);
                                    --index;
                                 } else {
                                    this.addInstruction(location.index, new BuilderInstruction10x(Opcode.NOP));
                                    ++index;
                                 }

                                 madeChanges = true;
                              }
                           }
                        }
                     }
                  } while(madeChanges);

                  this.fixInstructions = false;
                  return;
               }

               location = (MethodLocation)var2.next();
               instruction = location.instruction;
            } while(instruction == null);

            switch(instruction.getOpcode()) {
            case SPARSE_SWITCH:
            case PACKED_SWITCH:
               MethodLocation targetLocation = ((BuilderOffsetInstruction)instruction).getTarget().getLocation();
               BuilderInstruction targetInstruction = targetLocation.instruction;
               if (targetInstruction == null) {
                  throw new IllegalStateException(String.format("Switch instruction at address/index 0x%x/%d points to the end of the method.", location.codeAddress, location.index));
               }

               if (targetInstruction.getOpcode() == Opcode.NOP) {
                  targetInstruction = this.getFirstNonNop(targetLocation.index + 1);
               }

               if (targetInstruction != null && targetInstruction instanceof BuilderSwitchPayload) {
                  if ((instruction.opcode != Opcode.PACKED_SWITCH || targetInstruction.getOpcode() == Opcode.PACKED_SWITCH_PAYLOAD) && (instruction.opcode != Opcode.SPARSE_SWITCH || targetInstruction.getOpcode() == Opcode.SPARSE_SWITCH_PAYLOAD)) {
                     if (!payloadLocations.add(targetLocation)) {
                        throw new IllegalStateException("Multiple switch instructions refer to the same payload. This is not currently supported. Please file a bug :)");
                     }

                     ((BuilderSwitchPayload)targetInstruction).referrer = location;
                     continue;
                  }

                  throw new IllegalStateException(String.format("Switch instruction at address/index 0x%x/%d refers to the wrong type of payload instruction.", location.codeAddress, location.index));
               }

               throw new IllegalStateException(String.format("Switch instruction at address/index 0x%x/%d does not refer to a payload instruction.", location.codeAddress, location.index));
            }
         }
      }
   }

   private int mapCodeAddressToIndex(@Nonnull int[] codeAddressToIndex, int codeAddress) {
      while(true) {
         int index = codeAddressToIndex[codeAddress];
         if (index >= 0) {
            return index;
         }

         --codeAddress;
      }
   }

   private int mapCodeAddressToIndex(int codeAddress) {
      float avgCodeUnitsPerInstruction = 1.9F;
      int index = (int)((float)codeAddress / avgCodeUnitsPerInstruction);
      if (index >= this.instructionList.size()) {
         index = this.instructionList.size() - 1;
      }

      MethodLocation guessedLocation = (MethodLocation)this.instructionList.get(index);
      if (guessedLocation.codeAddress == codeAddress) {
         return index;
      } else if (guessedLocation.codeAddress > codeAddress) {
         do {
            --index;
         } while(((MethodLocation)this.instructionList.get(index)).codeAddress > codeAddress);

         return index;
      } else {
         do {
            ++index;
         } while(index < this.instructionList.size() && ((MethodLocation)this.instructionList.get(index)).codeAddress <= codeAddress);

         return index - 1;
      }
   }

   @Nonnull
   public Label newLabelForAddress(int codeAddress) {
      if (codeAddress >= 0 && codeAddress <= ((MethodLocation)this.instructionList.get(this.instructionList.size() - 1)).codeAddress) {
         MethodLocation referent = (MethodLocation)this.instructionList.get(this.mapCodeAddressToIndex(codeAddress));
         return referent.addNewLabel();
      } else {
         throw new IndexOutOfBoundsException(String.format("codeAddress %d out of bounds", codeAddress));
      }
   }

   @Nonnull
   public Label newLabelForIndex(int instructionIndex) {
      if (instructionIndex >= 0 && instructionIndex < this.instructionList.size()) {
         MethodLocation referent = (MethodLocation)this.instructionList.get(instructionIndex);
         return referent.addNewLabel();
      } else {
         throw new IndexOutOfBoundsException(String.format("instruction index %d out of bounds", instructionIndex));
      }
   }

   @Nonnull
   private Label newLabel(@Nonnull int[] codeAddressToIndex, int codeAddress) {
      MethodLocation referent = (MethodLocation)this.instructionList.get(this.mapCodeAddressToIndex(codeAddressToIndex, codeAddress));
      return referent.addNewLabel();
   }

   @Nonnull
   public Label newSwitchPayloadReferenceLabel(@Nonnull MethodLocation switchLocation, @Nonnull int[] codeAddressToIndex, int codeAddress) {
      MethodLocation referent = (MethodLocation)this.instructionList.get(this.mapCodeAddressToIndex(codeAddressToIndex, codeAddress));
      MutableMethodImplementation.SwitchPayloadReferenceLabel label = new MutableMethodImplementation.SwitchPayloadReferenceLabel();
      label.switchLocation = switchLocation;
      referent.getLabels().add(label);
      return label;
   }

   private void setInstruction(@Nonnull MethodLocation location, @Nonnull BuilderInstruction instruction) {
      location.instruction = instruction;
      instruction.location = location;
   }

   private void convertAndSetInstruction(@Nonnull MethodLocation location, int[] codeAddressToIndex, @Nonnull Instruction instruction) {
      switch(instruction.getOpcode().format) {
      case Format10t:
         this.setInstruction(location, this.newBuilderInstruction10t(location.codeAddress, codeAddressToIndex, (Instruction10t)instruction));
         return;
      case Format10x:
         this.setInstruction(location, this.newBuilderInstruction10x((Instruction10x)instruction));
         return;
      case Format11n:
         this.setInstruction(location, this.newBuilderInstruction11n((Instruction11n)instruction));
         return;
      case Format11x:
         this.setInstruction(location, this.newBuilderInstruction11x((Instruction11x)instruction));
         return;
      case Format12x:
         this.setInstruction(location, this.newBuilderInstruction12x((Instruction12x)instruction));
         return;
      case Format20bc:
         this.setInstruction(location, this.newBuilderInstruction20bc((Instruction20bc)instruction));
         return;
      case Format20t:
         this.setInstruction(location, this.newBuilderInstruction20t(location.codeAddress, codeAddressToIndex, (Instruction20t)instruction));
         return;
      case Format21c:
         this.setInstruction(location, this.newBuilderInstruction21c((Instruction21c)instruction));
         return;
      case Format21ih:
         this.setInstruction(location, this.newBuilderInstruction21ih((Instruction21ih)instruction));
         return;
      case Format21lh:
         this.setInstruction(location, this.newBuilderInstruction21lh((Instruction21lh)instruction));
         return;
      case Format21s:
         this.setInstruction(location, this.newBuilderInstruction21s((Instruction21s)instruction));
         return;
      case Format21t:
         this.setInstruction(location, this.newBuilderInstruction21t(location.codeAddress, codeAddressToIndex, (Instruction21t)instruction));
         return;
      case Format22b:
         this.setInstruction(location, this.newBuilderInstruction22b((Instruction22b)instruction));
         return;
      case Format22c:
         this.setInstruction(location, this.newBuilderInstruction22c((Instruction22c)instruction));
         return;
      case Format22cs:
         this.setInstruction(location, this.newBuilderInstruction22cs((Instruction22cs)instruction));
         return;
      case Format22s:
         this.setInstruction(location, this.newBuilderInstruction22s((Instruction22s)instruction));
         return;
      case Format22t:
         this.setInstruction(location, this.newBuilderInstruction22t(location.codeAddress, codeAddressToIndex, (Instruction22t)instruction));
         return;
      case Format22x:
         this.setInstruction(location, this.newBuilderInstruction22x((Instruction22x)instruction));
         return;
      case Format23x:
         this.setInstruction(location, this.newBuilderInstruction23x((Instruction23x)instruction));
         return;
      case Format30t:
         this.setInstruction(location, this.newBuilderInstruction30t(location.codeAddress, codeAddressToIndex, (Instruction30t)instruction));
         return;
      case Format31c:
         this.setInstruction(location, this.newBuilderInstruction31c((Instruction31c)instruction));
         return;
      case Format31i:
         this.setInstruction(location, this.newBuilderInstruction31i((Instruction31i)instruction));
         return;
      case Format31t:
         this.setInstruction(location, this.newBuilderInstruction31t(location, codeAddressToIndex, (Instruction31t)instruction));
         return;
      case Format32x:
         this.setInstruction(location, this.newBuilderInstruction32x((Instruction32x)instruction));
         return;
      case Format35c:
         this.setInstruction(location, this.newBuilderInstruction35c((Instruction35c)instruction));
         return;
      case Format35mi:
         this.setInstruction(location, this.newBuilderInstruction35mi((Instruction35mi)instruction));
         return;
      case Format35ms:
         this.setInstruction(location, this.newBuilderInstruction35ms((Instruction35ms)instruction));
         return;
      case Format3rc:
         this.setInstruction(location, this.newBuilderInstruction3rc((Instruction3rc)instruction));
         return;
      case Format3rmi:
         this.setInstruction(location, this.newBuilderInstruction3rmi((Instruction3rmi)instruction));
         return;
      case Format3rms:
         this.setInstruction(location, this.newBuilderInstruction3rms((Instruction3rms)instruction));
         return;
      case Format51l:
         this.setInstruction(location, this.newBuilderInstruction51l((Instruction51l)instruction));
         return;
      case PackedSwitchPayload:
         this.setInstruction(location, this.newBuilderPackedSwitchPayload(location, codeAddressToIndex, (PackedSwitchPayload)instruction));
         return;
      case SparseSwitchPayload:
         this.setInstruction(location, this.newBuilderSparseSwitchPayload(location, codeAddressToIndex, (SparseSwitchPayload)instruction));
         return;
      case ArrayPayload:
         this.setInstruction(location, this.newBuilderArrayPayload((ArrayPayload)instruction));
         return;
      default:
         throw new ExceptionWithContext("Instruction format %s not supported", new Object[]{instruction.getOpcode().format});
      }
   }

   @Nonnull
   private BuilderInstruction10t newBuilderInstruction10t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction10t instruction) {
      return new BuilderInstruction10t(instruction.getOpcode(), this.newLabel(codeAddressToIndex, codeAddress + instruction.getCodeOffset()));
   }

   @Nonnull
   private BuilderInstruction10x newBuilderInstruction10x(@Nonnull Instruction10x instruction) {
      return new BuilderInstruction10x(instruction.getOpcode());
   }

   @Nonnull
   private BuilderInstruction11n newBuilderInstruction11n(@Nonnull Instruction11n instruction) {
      return new BuilderInstruction11n(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
   }

   @Nonnull
   private BuilderInstruction11x newBuilderInstruction11x(@Nonnull Instruction11x instruction) {
      return new BuilderInstruction11x(instruction.getOpcode(), instruction.getRegisterA());
   }

   @Nonnull
   private BuilderInstruction12x newBuilderInstruction12x(@Nonnull Instruction12x instruction) {
      return new BuilderInstruction12x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
   }

   @Nonnull
   private BuilderInstruction20bc newBuilderInstruction20bc(@Nonnull Instruction20bc instruction) {
      return new BuilderInstruction20bc(instruction.getOpcode(), instruction.getVerificationError(), instruction.getReference());
   }

   @Nonnull
   private BuilderInstruction20t newBuilderInstruction20t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction20t instruction) {
      return new BuilderInstruction20t(instruction.getOpcode(), this.newLabel(codeAddressToIndex, codeAddress + instruction.getCodeOffset()));
   }

   @Nonnull
   private BuilderInstruction21c newBuilderInstruction21c(@Nonnull Instruction21c instruction) {
      return new BuilderInstruction21c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getReference());
   }

   @Nonnull
   private BuilderInstruction21ih newBuilderInstruction21ih(@Nonnull Instruction21ih instruction) {
      return new BuilderInstruction21ih(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
   }

   @Nonnull
   private BuilderInstruction21lh newBuilderInstruction21lh(@Nonnull Instruction21lh instruction) {
      return new BuilderInstruction21lh(instruction.getOpcode(), instruction.getRegisterA(), instruction.getWideLiteral());
   }

   @Nonnull
   private BuilderInstruction21s newBuilderInstruction21s(@Nonnull Instruction21s instruction) {
      return new BuilderInstruction21s(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
   }

   @Nonnull
   private BuilderInstruction21t newBuilderInstruction21t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction21t instruction) {
      return new BuilderInstruction21t(instruction.getOpcode(), instruction.getRegisterA(), this.newLabel(codeAddressToIndex, codeAddress + instruction.getCodeOffset()));
   }

   @Nonnull
   private BuilderInstruction22b newBuilderInstruction22b(@Nonnull Instruction22b instruction) {
      return new BuilderInstruction22b(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getNarrowLiteral());
   }

   @Nonnull
   private BuilderInstruction22c newBuilderInstruction22c(@Nonnull Instruction22c instruction) {
      return new BuilderInstruction22c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getReference());
   }

   @Nonnull
   private BuilderInstruction22cs newBuilderInstruction22cs(@Nonnull Instruction22cs instruction) {
      return new BuilderInstruction22cs(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getFieldOffset());
   }

   @Nonnull
   private BuilderInstruction22s newBuilderInstruction22s(@Nonnull Instruction22s instruction) {
      return new BuilderInstruction22s(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getNarrowLiteral());
   }

   @Nonnull
   private BuilderInstruction22t newBuilderInstruction22t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction22t instruction) {
      return new BuilderInstruction22t(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), this.newLabel(codeAddressToIndex, codeAddress + instruction.getCodeOffset()));
   }

   @Nonnull
   private BuilderInstruction22x newBuilderInstruction22x(@Nonnull Instruction22x instruction) {
      return new BuilderInstruction22x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
   }

   @Nonnull
   private BuilderInstruction23x newBuilderInstruction23x(@Nonnull Instruction23x instruction) {
      return new BuilderInstruction23x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getRegisterC());
   }

   @Nonnull
   private BuilderInstruction30t newBuilderInstruction30t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction30t instruction) {
      return new BuilderInstruction30t(instruction.getOpcode(), this.newLabel(codeAddressToIndex, codeAddress + instruction.getCodeOffset()));
   }

   @Nonnull
   private BuilderInstruction31c newBuilderInstruction31c(@Nonnull Instruction31c instruction) {
      return new BuilderInstruction31c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getReference());
   }

   @Nonnull
   private BuilderInstruction31i newBuilderInstruction31i(@Nonnull Instruction31i instruction) {
      return new BuilderInstruction31i(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
   }

   @Nonnull
   private BuilderInstruction31t newBuilderInstruction31t(@Nonnull MethodLocation location, int[] codeAddressToIndex, @Nonnull Instruction31t instruction) {
      int codeAddress = location.getCodeAddress();
      Label newLabel;
      if (instruction.getOpcode() != Opcode.FILL_ARRAY_DATA) {
         newLabel = this.newSwitchPayloadReferenceLabel(location, codeAddressToIndex, codeAddress + instruction.getCodeOffset());
      } else {
         newLabel = this.newLabel(codeAddressToIndex, codeAddress + instruction.getCodeOffset());
      }

      return new BuilderInstruction31t(instruction.getOpcode(), instruction.getRegisterA(), newLabel);
   }

   @Nonnull
   private BuilderInstruction32x newBuilderInstruction32x(@Nonnull Instruction32x instruction) {
      return new BuilderInstruction32x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
   }

   @Nonnull
   private BuilderInstruction35c newBuilderInstruction35c(@Nonnull Instruction35c instruction) {
      return new BuilderInstruction35c(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getReference());
   }

   @Nonnull
   private BuilderInstruction35mi newBuilderInstruction35mi(@Nonnull Instruction35mi instruction) {
      return new BuilderInstruction35mi(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getInlineIndex());
   }

   @Nonnull
   private BuilderInstruction35ms newBuilderInstruction35ms(@Nonnull Instruction35ms instruction) {
      return new BuilderInstruction35ms(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getVtableIndex());
   }

   @Nonnull
   private BuilderInstruction3rc newBuilderInstruction3rc(@Nonnull Instruction3rc instruction) {
      return new BuilderInstruction3rc(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getReference());
   }

   @Nonnull
   private BuilderInstruction3rmi newBuilderInstruction3rmi(@Nonnull Instruction3rmi instruction) {
      return new BuilderInstruction3rmi(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getInlineIndex());
   }

   @Nonnull
   private BuilderInstruction3rms newBuilderInstruction3rms(@Nonnull Instruction3rms instruction) {
      return new BuilderInstruction3rms(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getVtableIndex());
   }

   @Nonnull
   private BuilderInstruction51l newBuilderInstruction51l(@Nonnull Instruction51l instruction) {
      return new BuilderInstruction51l(instruction.getOpcode(), instruction.getRegisterA(), instruction.getWideLiteral());
   }

   @Nullable
   private MethodLocation findSwitchForPayload(@Nonnull MethodLocation payloadLocation) {
      MethodLocation location = payloadLocation;
      MethodLocation switchLocation = null;

      do {
         Iterator var4 = location.getLabels().iterator();

         while(var4.hasNext()) {
            Label label = (Label)var4.next();
            if (label instanceof MutableMethodImplementation.SwitchPayloadReferenceLabel) {
               if (switchLocation != null) {
                  throw new IllegalStateException("Multiple switch instructions refer to the same payload. This is not currently supported. Please file a bug :)");
               }

               switchLocation = ((MutableMethodImplementation.SwitchPayloadReferenceLabel)label).switchLocation;
            }
         }

         if (location.index == 0) {
            return switchLocation;
         }

         location = (MethodLocation)this.instructionList.get(location.index - 1);
      } while(location.instruction != null && location.instruction.getOpcode() == Opcode.NOP);

      return switchLocation;
   }

   @Nonnull
   private BuilderPackedSwitchPayload newBuilderPackedSwitchPayload(@Nonnull MethodLocation location, @Nonnull int[] codeAddressToIndex, @Nonnull PackedSwitchPayload instruction) {
      List<? extends SwitchElement> switchElements = instruction.getSwitchElements();
      if (switchElements.size() == 0) {
         return new BuilderPackedSwitchPayload(0, (List)null);
      } else {
         MethodLocation switchLocation = this.findSwitchForPayload(location);
         int baseAddress;
         if (switchLocation == null) {
            baseAddress = 0;
         } else {
            baseAddress = switchLocation.codeAddress;
         }

         List<Label> labels = Lists.newArrayList();
         Iterator var8 = switchElements.iterator();

         while(var8.hasNext()) {
            SwitchElement element = (SwitchElement)var8.next();
            labels.add(this.newLabel(codeAddressToIndex, element.getOffset() + baseAddress));
         }

         return new BuilderPackedSwitchPayload(((SwitchElement)switchElements.get(0)).getKey(), labels);
      }
   }

   @Nonnull
   private BuilderSparseSwitchPayload newBuilderSparseSwitchPayload(@Nonnull MethodLocation location, @Nonnull int[] codeAddressToIndex, @Nonnull SparseSwitchPayload instruction) {
      List<? extends SwitchElement> switchElements = instruction.getSwitchElements();
      if (switchElements.size() == 0) {
         return new BuilderSparseSwitchPayload((List)null);
      } else {
         MethodLocation switchLocation = this.findSwitchForPayload(location);
         int baseAddress;
         if (switchLocation == null) {
            baseAddress = 0;
         } else {
            baseAddress = switchLocation.codeAddress;
         }

         List<SwitchLabelElement> labelElements = Lists.newArrayList();
         Iterator var8 = switchElements.iterator();

         while(var8.hasNext()) {
            SwitchElement element = (SwitchElement)var8.next();
            labelElements.add(new SwitchLabelElement(element.getKey(), this.newLabel(codeAddressToIndex, element.getOffset() + baseAddress)));
         }

         return new BuilderSparseSwitchPayload(labelElements);
      }
   }

   @Nonnull
   private BuilderArrayPayload newBuilderArrayPayload(@Nonnull ArrayPayload instruction) {
      return new BuilderArrayPayload(instruction.getElementWidth(), instruction.getArrayElements());
   }

   @Nonnull
   private BuilderDebugItem convertDebugItem(@Nonnull DebugItem debugItem) {
      switch(debugItem.getDebugItemType()) {
      case 3:
         StartLocal startLocal = (StartLocal)debugItem;
         return new BuilderStartLocal(startLocal.getRegister(), startLocal.getNameReference(), startLocal.getTypeReference(), startLocal.getSignatureReference());
      case 4:
      default:
         throw new ExceptionWithContext("Invalid debug item type: " + debugItem.getDebugItemType(), new Object[0]);
      case 5:
         EndLocal endLocal = (EndLocal)debugItem;
         return new BuilderEndLocal(endLocal.getRegister());
      case 6:
         RestartLocal restartLocal = (RestartLocal)debugItem;
         return new BuilderRestartLocal(restartLocal.getRegister());
      case 7:
         return new BuilderPrologueEnd();
      case 8:
         return new BuilderEpilogueBegin();
      case 9:
         SetSourceFile setSourceFile = (SetSourceFile)debugItem;
         return new BuilderSetSourceFile(setSourceFile.getSourceFileReference());
      case 10:
         LineNumber lineNumber = (LineNumber)debugItem;
         return new BuilderLineNumber(lineNumber.getLineNumber());
      }
   }

   private static class SwitchPayloadReferenceLabel extends Label {
      @Nonnull
      public MethodLocation switchLocation;

      private SwitchPayloadReferenceLabel() {
      }

      // $FF: synthetic method
      SwitchPayloadReferenceLabel(Object x0) {
         this();
      }
   }

   private interface Task {
      void perform();
   }
}
