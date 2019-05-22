package org.jf.dexlib2.dexbacked.raw;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.VerificationError;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.instruction.DexBackedInstruction;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.iface.instruction.FieldOffsetInstruction;
import org.jf.dexlib2.iface.instruction.InlineIndexInstruction;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.NarrowLiteralInstruction;
import org.jf.dexlib2.iface.instruction.OffsetInstruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.ThreeRegisterInstruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import org.jf.dexlib2.iface.instruction.VerificationErrorInstruction;
import org.jf.dexlib2.iface.instruction.VtableIndexInstruction;
import org.jf.dexlib2.iface.instruction.WideLiteralInstruction;
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import org.jf.dexlib2.util.AnnotatedBytes;
import org.jf.dexlib2.util.ReferenceUtil;
import org.jf.util.ExceptionWithContext;
import org.jf.util.NumberUtils;

public class CodeItem {
   public static final int REGISTERS_OFFSET = 0;
   public static final int INS_OFFSET = 2;
   public static final int OUTS_OFFSET = 4;
   public static final int TRIES_SIZE_OFFSET = 6;
   public static final int DEBUG_INFO_OFFSET = 8;
   public static final int INSTRUCTION_COUNT_OFFSET = 12;
   public static final int INSTRUCTION_START_OFFSET = 16;

   @Nonnull
   public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
      return new SectionAnnotator(annotator, mapItem) {
         private SectionAnnotator debugInfoAnnotator = null;

         public void annotateSection(@Nonnull AnnotatedBytes out) {
            this.debugInfoAnnotator = this.annotator.getAnnotator(8195);
            super.annotateSection(out);
         }

         @Nonnull
         public String getItemName() {
            return "code_item";
         }

         public int getItemAlignment() {
            return 4;
         }

         public void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            try {
               DexReader reader = this.dexFile.readerAt(out.getCursor());
               int registers = reader.readUshort();
               out.annotate(2, "registers_size = %d", registers);
               int inSize = reader.readUshort();
               out.annotate(2, "ins_size = %d", inSize);
               int outSize = reader.readUshort();
               out.annotate(2, "outs_size = %d", outSize);
               int triesCount = reader.readUshort();
               out.annotate(2, "tries_size = %d", triesCount);
               int debugInfoOffset = reader.readInt();
               out.annotate(4, "debug_info_off = 0x%x", debugInfoOffset);
               if (debugInfoOffset > 0) {
                  this.addDebugInfoIdentity(debugInfoOffset, itemIdentity);
               }

               int instructionSize = reader.readSmallUint();
               out.annotate(4, "insns_size = 0x%x", instructionSize);
               out.annotate(0, "instructions:");
               out.indent();
               out.setLimit(out.getCursor(), out.getCursor() + instructionSize * 2);
               int end = reader.getOffset() + instructionSize * 2;

               try {
                  while(reader.getOffset() < end) {
                     Instruction instruction = DexBackedInstruction.readFrom(reader);
                     if (reader.getOffset() > end) {
                        out.annotateTo(end, "truncated instruction");
                        reader.setOffset(end);
                     } else {
                        switch(instruction.getOpcode().format) {
                        case Format10x:
                           this.annotateInstruction10x(out, instruction);
                           break;
                        case Format35c:
                           this.annotateInstruction35c(out, (Instruction35c)instruction);
                           break;
                        case Format3rc:
                           this.annotateInstruction3rc(out, (Instruction3rc)instruction);
                           break;
                        case ArrayPayload:
                           this.annotateArrayPayload(out, (ArrayPayload)instruction);
                           break;
                        case PackedSwitchPayload:
                           this.annotatePackedSwitchPayload(out, (PackedSwitchPayload)instruction);
                           break;
                        case SparseSwitchPayload:
                           this.annotateSparseSwitchPayload(out, (SparseSwitchPayload)instruction);
                           break;
                        default:
                           this.annotateDefaultInstruction(out, instruction);
                        }
                     }

                     assert reader.getOffset() == out.getCursor();
                  }
               } catch (ExceptionWithContext var92) {
                  var92.printStackTrace(System.err);
                  out.annotate(0, "annotation error: %s", var92.getMessage());
                  out.moveTo(end);
                  reader.setOffset(end);
               } finally {
                  out.clearLimit();
                  out.deindent();
               }

               if (triesCount > 0) {
                  if (reader.getOffset() % 4 != 0) {
                     reader.readUshort();
                     out.annotate(2, "padding");
                  }

                  out.annotate(0, "try_items:");
                  out.indent();

                  int i;
                  int handlerCount;
                  int handlerListCount;
                  try {
                     for(handlerListCount = 0; handlerListCount < triesCount; ++handlerListCount) {
                        out.annotate(0, "try_item[%d]:", handlerListCount);
                        out.indent();

                        try {
                           i = reader.readSmallUint();
                           out.annotate(4, "start_addr = 0x%x", i);
                           handlerCount = reader.readUshort();
                           out.annotate(2, "insn_count = 0x%x", handlerCount);
                           int handlerOffset = reader.readUshort();
                           out.annotate(2, "handler_off = 0x%x", handlerOffset);
                        } finally {
                           out.deindent();
                        }
                     }
                  } finally {
                     out.deindent();
                  }

                  handlerListCount = reader.readSmallUleb128();
                  out.annotate(0, "encoded_catch_handler_list:");
                  out.annotateTo(reader.getOffset(), "size = %d", handlerListCount);
                  out.indent();

                  try {
                     for(i = 0; i < handlerListCount; ++i) {
                        out.annotate(0, "encoded_catch_handler[%d]", i);
                        out.indent();

                        try {
                           handlerCount = reader.readSleb128();
                           out.annotateTo(reader.getOffset(), "size = %d", handlerCount);
                           boolean hasCatchAll = handlerCount <= 0;
                           handlerCount = Math.abs(handlerCount);
                           int j;
                           if (handlerCount != 0) {
                              out.annotate(0, "handlers:");
                              out.indent();

                              try {
                                 for(j = 0; j < handlerCount; ++j) {
                                    out.annotate(0, "encoded_type_addr_pair[%d]", i);
                                    out.indent();

                                    try {
                                       int typeIndex = reader.readSmallUleb128();
                                       out.annotateTo(reader.getOffset(), TypeIdItem.getReferenceAnnotation(this.dexFile, typeIndex));
                                       int handlerAddress = reader.readSmallUleb128();
                                       out.annotateTo(reader.getOffset(), "addr = 0x%x", handlerAddress);
                                    } finally {
                                       out.deindent();
                                    }
                                 }
                              } finally {
                                 out.deindent();
                              }
                           }

                           if (hasCatchAll) {
                              j = reader.readSmallUleb128();
                              out.annotateTo(reader.getOffset(), "catch_all_addr = 0x%x", j);
                           }
                        } finally {
                           out.deindent();
                        }
                     }
                  } finally {
                     out.deindent();
                  }
               }
            } catch (ExceptionWithContext var94) {
               out.annotate(0, "annotation error: %s", var94.getMessage());
            }

         }

         private String formatRegister(int registerNum) {
            return String.format("v%d", registerNum);
         }

         private void annotateInstruction10x(@Nonnull AnnotatedBytes out, @Nonnull Instruction instruction) {
            out.annotate(2, instruction.getOpcode().name);
         }

         private void annotateInstruction35c(@Nonnull AnnotatedBytes out, @Nonnull Instruction35c instruction) {
            List<String> args = Lists.newArrayList();
            int registerCount = instruction.getRegisterCount();
            if (registerCount == 1) {
               args.add(this.formatRegister(instruction.getRegisterC()));
            } else if (registerCount == 2) {
               args.add(this.formatRegister(instruction.getRegisterC()));
               args.add(this.formatRegister(instruction.getRegisterD()));
            } else if (registerCount == 3) {
               args.add(this.formatRegister(instruction.getRegisterC()));
               args.add(this.formatRegister(instruction.getRegisterD()));
               args.add(this.formatRegister(instruction.getRegisterE()));
            } else if (registerCount == 4) {
               args.add(this.formatRegister(instruction.getRegisterC()));
               args.add(this.formatRegister(instruction.getRegisterD()));
               args.add(this.formatRegister(instruction.getRegisterE()));
               args.add(this.formatRegister(instruction.getRegisterF()));
            } else if (registerCount == 5) {
               args.add(this.formatRegister(instruction.getRegisterC()));
               args.add(this.formatRegister(instruction.getRegisterD()));
               args.add(this.formatRegister(instruction.getRegisterE()));
               args.add(this.formatRegister(instruction.getRegisterF()));
               args.add(this.formatRegister(instruction.getRegisterG()));
            }

            String reference = ReferenceUtil.getReferenceString(instruction.getReference());
            out.annotate(6, String.format("%s {%s}, %s", instruction.getOpcode().name, Joiner.on(", ").join((Iterable)args), reference));
         }

         private void annotateInstruction3rc(@Nonnull AnnotatedBytes out, @Nonnull Instruction3rc instruction) {
            int startRegister = instruction.getStartRegister();
            int endRegister = startRegister + instruction.getRegisterCount() - 1;
            String reference = ReferenceUtil.getReferenceString(instruction.getReference());
            out.annotate(6, String.format("%s {%s .. %s}, %s", instruction.getOpcode().name, this.formatRegister(startRegister), this.formatRegister(endRegister), reference));
         }

         private void annotateDefaultInstruction(@Nonnull AnnotatedBytes out, @Nonnull Instruction instruction) {
            List<String> args = Lists.newArrayList();
            if (instruction instanceof OneRegisterInstruction) {
               args.add(this.formatRegister(((OneRegisterInstruction)instruction).getRegisterA()));
               if (instruction instanceof TwoRegisterInstruction) {
                  args.add(this.formatRegister(((TwoRegisterInstruction)instruction).getRegisterB()));
                  if (instruction instanceof ThreeRegisterInstruction) {
                     args.add(this.formatRegister(((ThreeRegisterInstruction)instruction).getRegisterC()));
                  }
               }
            } else if (instruction instanceof VerificationErrorInstruction) {
               String verificationError = VerificationError.getVerificationErrorName(((VerificationErrorInstruction)instruction).getVerificationError());
               if (verificationError != null) {
                  args.add(verificationError);
               } else {
                  args.add("invalid verification error type");
               }
            }

            if (instruction instanceof ReferenceInstruction) {
               args.add(ReferenceUtil.getReferenceString(((ReferenceInstruction)instruction).getReference()));
            } else {
               int inlineIndex;
               if (instruction instanceof OffsetInstruction) {
                  inlineIndex = ((OffsetInstruction)instruction).getCodeOffset();
                  String sign = inlineIndex >= 0 ? "+" : "-";
                  args.add(String.format("%s0x%x", sign, Math.abs(inlineIndex)));
               } else if (instruction instanceof NarrowLiteralInstruction) {
                  inlineIndex = ((NarrowLiteralInstruction)instruction).getNarrowLiteral();
                  if (NumberUtils.isLikelyFloat(inlineIndex)) {
                     args.add(String.format("%d # %f", inlineIndex, Float.intBitsToFloat(inlineIndex)));
                  } else {
                     args.add(String.format("%d", inlineIndex));
                  }
               } else if (instruction instanceof WideLiteralInstruction) {
                  long value = ((WideLiteralInstruction)instruction).getWideLiteral();
                  if (NumberUtils.isLikelyDouble(value)) {
                     args.add(String.format("%d # %f", value, Double.longBitsToDouble(value)));
                  } else {
                     args.add(String.format("%d", value));
                  }
               } else if (instruction instanceof FieldOffsetInstruction) {
                  inlineIndex = ((FieldOffsetInstruction)instruction).getFieldOffset();
                  args.add(String.format("field@0x%x", inlineIndex));
               } else if (instruction instanceof VtableIndexInstruction) {
                  inlineIndex = ((VtableIndexInstruction)instruction).getVtableIndex();
                  args.add(String.format("vtable@%d", inlineIndex));
               } else if (instruction instanceof InlineIndexInstruction) {
                  inlineIndex = ((InlineIndexInstruction)instruction).getInlineIndex();
                  args.add(String.format("inline@%d", inlineIndex));
               }
            }

            out.annotate(instruction.getCodeUnits() * 2, "%s %s", instruction.getOpcode().name, Joiner.on(", ").join((Iterable)args));
         }

         private void annotateArrayPayload(@Nonnull AnnotatedBytes out, @Nonnull ArrayPayload instruction) {
            List<Number> elements = instruction.getArrayElements();
            int elementWidth = instruction.getElementWidth();
            out.annotate(2, instruction.getOpcode().name);
            out.indent();
            out.annotate(2, "element_width = %d", elementWidth);
            out.annotate(4, "size = %d", elements.size());
            out.annotate(0, "elements:");
            out.indent();

            for(int i = 0; i < elements.size(); ++i) {
               if (elementWidth == 8) {
                  long value = ((Number)elements.get(i)).longValue();
                  if (NumberUtils.isLikelyDouble(value)) {
                     out.annotate(elementWidth, "element[%d] = %d # %f", i, value, Double.longBitsToDouble(value));
                  } else {
                     out.annotate(elementWidth, "element[%d] = %d", i, value);
                  }
               } else {
                  int valuex = ((Number)elements.get(i)).intValue();
                  if (NumberUtils.isLikelyFloat(valuex)) {
                     out.annotate(elementWidth, "element[%d] = %d # %f", i, valuex, Float.intBitsToFloat(valuex));
                  } else {
                     out.annotate(elementWidth, "element[%d] = %d", i, valuex);
                  }
               }
            }

            if (out.getCursor() % 2 != 0) {
               out.annotate(1, "padding");
            }

            out.deindent();
            out.deindent();
         }

         private void annotatePackedSwitchPayload(@Nonnull AnnotatedBytes out, @Nonnull PackedSwitchPayload instruction) {
            List<? extends SwitchElement> elements = instruction.getSwitchElements();
            out.annotate(2, instruction.getOpcode().name);
            out.indent();
            out.annotate(2, "size = %d", elements.size());
            if (elements.size() == 0) {
               out.annotate(4, "first_key");
            } else {
               out.annotate(4, "first_key = %d", ((SwitchElement)elements.get(0)).getKey());
               out.annotate(0, "targets:");
               out.indent();

               for(int i = 0; i < elements.size(); ++i) {
                  out.annotate(4, "target[%d] = %d", i, ((SwitchElement)elements.get(i)).getOffset());
               }

               out.deindent();
            }

            out.deindent();
         }

         private void annotateSparseSwitchPayload(@Nonnull AnnotatedBytes out, @Nonnull SparseSwitchPayload instruction) {
            List<? extends SwitchElement> elements = instruction.getSwitchElements();
            out.annotate(2, instruction.getOpcode().name);
            out.indent();
            out.annotate(2, "size = %d", elements.size());
            if (elements.size() > 0) {
               out.annotate(0, "keys:");
               out.indent();

               int i;
               for(i = 0; i < elements.size(); ++i) {
                  out.annotate(4, "key[%d] = %d", i, ((SwitchElement)elements.get(i)).getKey());
               }

               out.deindent();
               out.annotate(0, "targets:");
               out.indent();

               for(i = 0; i < elements.size(); ++i) {
                  out.annotate(4, "target[%d] = %d", i, ((SwitchElement)elements.get(i)).getOffset());
               }

               out.deindent();
            }

            out.deindent();
         }

         private void addDebugInfoIdentity(int debugInfoOffset, String methodString) {
            if (this.debugInfoAnnotator != null) {
               this.debugInfoAnnotator.setItemIdentity(debugInfoOffset, methodString);
            }

         }
      };
   }

   public static class TryItem {
      public static final int ITEM_SIZE = 8;
      public static final int START_ADDRESS_OFFSET = 0;
      public static final int CODE_UNIT_COUNT_OFFSET = 4;
      public static final int HANDLER_OFFSET = 6;
   }
}
