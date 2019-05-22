package org.jf.dexlib2.writer;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.instruction.DualReferenceInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
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
import org.jf.dexlib2.iface.instruction.formats.Instruction45cc;
import org.jf.dexlib2.iface.instruction.formats.Instruction4rcc;
import org.jf.dexlib2.iface.instruction.formats.Instruction51l;
import org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.util.ExceptionWithContext;

public class InstructionWriter<StringRef extends StringReference, TypeRef extends TypeReference, FieldRefKey extends FieldReference, MethodRefKey extends MethodReference, ProtoRefKey extends MethodProtoReference> {
   @Nonnull
   private final Opcodes opcodes;
   @Nonnull
   private final DexDataWriter writer;
   @Nonnull
   private final StringSection<?, StringRef> stringSection;
   @Nonnull
   private final TypeSection<?, ?, TypeRef> typeSection;
   @Nonnull
   private final FieldSection<?, ?, FieldRefKey, ?> fieldSection;
   @Nonnull
   private final MethodSection<?, ?, ?, MethodRefKey, ?> methodSection;
   @Nonnull
   private final ProtoSection<?, ?, ProtoRefKey, ?> protoSection;
   private final Comparator<SwitchElement> switchElementComparator = new Comparator<SwitchElement>() {
      public int compare(SwitchElement element1, SwitchElement element2) {
         return Ints.compare(element1.getKey(), element2.getKey());
      }
   };

   @Nonnull
   static <StringRef extends StringReference, TypeRef extends TypeReference, FieldRefKey extends FieldReference, MethodRefKey extends MethodReference, ProtoRefKey extends MethodProtoReference> InstructionWriter<StringRef, TypeRef, FieldRefKey, MethodRefKey, ProtoRefKey> makeInstructionWriter(@Nonnull Opcodes opcodes, @Nonnull DexDataWriter writer, @Nonnull StringSection<?, StringRef> stringSection, @Nonnull TypeSection<?, ?, TypeRef> typeSection, @Nonnull FieldSection<?, ?, FieldRefKey, ?> fieldSection, @Nonnull MethodSection<?, ?, ?, MethodRefKey, ?> methodSection, @Nonnull ProtoSection<?, ?, ProtoRefKey, ?> protoSection) {
      return new InstructionWriter(opcodes, writer, stringSection, typeSection, fieldSection, methodSection, protoSection);
   }

   InstructionWriter(@Nonnull Opcodes opcodes, @Nonnull DexDataWriter writer, @Nonnull StringSection<?, StringRef> stringSection, @Nonnull TypeSection<?, ?, TypeRef> typeSection, @Nonnull FieldSection<?, ?, FieldRefKey, ?> fieldSection, @Nonnull MethodSection<?, ?, ?, MethodRefKey, ?> methodSection, @Nonnull ProtoSection<?, ?, ProtoRefKey, ?> protoSection) {
      this.opcodes = opcodes;
      this.writer = writer;
      this.stringSection = stringSection;
      this.typeSection = typeSection;
      this.fieldSection = fieldSection;
      this.methodSection = methodSection;
      this.protoSection = protoSection;
   }

   private short getOpcodeValue(Opcode opcode) {
      Short value = this.opcodes.getOpcodeValue(opcode);
      if (value == null) {
         throw new ExceptionWithContext("Instruction %s is invalid for api %d", new Object[]{opcode.name, this.opcodes.api});
      } else {
         return value;
      }
   }

   public void write(@Nonnull Instruction10t instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getCodeOffset());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction10x instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(0);
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction11n instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(packNibbles(instruction.getRegisterA(), instruction.getNarrowLiteral()));
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction11x instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction12x instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(packNibbles(instruction.getRegisterA(), instruction.getRegisterB()));
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction20bc instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getVerificationError());
         this.writer.writeUshort(this.getReferenceIndex(instruction));
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction20t instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(0);
         this.writer.writeShort(instruction.getCodeOffset());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction21c instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
         this.writer.writeUshort(this.getReferenceIndex(instruction));
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction21ih instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
         this.writer.writeShort(instruction.getHatLiteral());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction21lh instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
         this.writer.writeShort(instruction.getHatLiteral());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction21s instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
         this.writer.writeShort(instruction.getNarrowLiteral());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction21t instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
         this.writer.writeShort(instruction.getCodeOffset());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction22b instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
         this.writer.write(instruction.getRegisterB());
         this.writer.write(instruction.getNarrowLiteral());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction22c instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(packNibbles(instruction.getRegisterA(), instruction.getRegisterB()));
         this.writer.writeUshort(this.getReferenceIndex(instruction));
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction22cs instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(packNibbles(instruction.getRegisterA(), instruction.getRegisterB()));
         this.writer.writeUshort(instruction.getFieldOffset());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction22s instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(packNibbles(instruction.getRegisterA(), instruction.getRegisterB()));
         this.writer.writeShort(instruction.getNarrowLiteral());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction22t instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(packNibbles(instruction.getRegisterA(), instruction.getRegisterB()));
         this.writer.writeShort(instruction.getCodeOffset());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction22x instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
         this.writer.writeUshort(instruction.getRegisterB());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction23x instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
         this.writer.write(instruction.getRegisterB());
         this.writer.write(instruction.getRegisterC());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction30t instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(0);
         this.writer.writeInt(instruction.getCodeOffset());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction31c instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
         this.writer.writeInt(this.getReferenceIndex(instruction));
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction31i instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
         this.writer.writeInt(instruction.getNarrowLiteral());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction31t instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
         this.writer.writeInt(instruction.getCodeOffset());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction32x instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(0);
         this.writer.writeUshort(instruction.getRegisterA());
         this.writer.writeUshort(instruction.getRegisterB());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction35c instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(packNibbles(instruction.getRegisterG(), instruction.getRegisterCount()));
         this.writer.writeUshort(this.getReferenceIndex(instruction));
         this.writer.write(packNibbles(instruction.getRegisterC(), instruction.getRegisterD()));
         this.writer.write(packNibbles(instruction.getRegisterE(), instruction.getRegisterF()));
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction35mi instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(packNibbles(instruction.getRegisterG(), instruction.getRegisterCount()));
         this.writer.writeUshort(instruction.getInlineIndex());
         this.writer.write(packNibbles(instruction.getRegisterC(), instruction.getRegisterD()));
         this.writer.write(packNibbles(instruction.getRegisterE(), instruction.getRegisterF()));
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction35ms instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(packNibbles(instruction.getRegisterG(), instruction.getRegisterCount()));
         this.writer.writeUshort(instruction.getVtableIndex());
         this.writer.write(packNibbles(instruction.getRegisterC(), instruction.getRegisterD()));
         this.writer.write(packNibbles(instruction.getRegisterE(), instruction.getRegisterF()));
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction3rc instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterCount());
         this.writer.writeUshort(this.getReferenceIndex(instruction));
         this.writer.writeUshort(instruction.getStartRegister());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction3rmi instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterCount());
         this.writer.writeUshort(instruction.getInlineIndex());
         this.writer.writeUshort(instruction.getStartRegister());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction3rms instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterCount());
         this.writer.writeUshort(instruction.getVtableIndex());
         this.writer.writeUshort(instruction.getStartRegister());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction45cc instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(packNibbles(instruction.getRegisterG(), instruction.getRegisterCount()));
         this.writer.writeUshort(this.getReferenceIndex(instruction));
         this.writer.write(packNibbles(instruction.getRegisterC(), instruction.getRegisterD()));
         this.writer.write(packNibbles(instruction.getRegisterE(), instruction.getRegisterF()));
         this.writer.writeUshort(this.getReference2Index(instruction));
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction4rcc instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterCount());
         this.writer.writeUshort(this.getReferenceIndex(instruction));
         this.writer.writeUshort(instruction.getStartRegister());
         this.writer.writeUshort(this.getReference2Index(instruction));
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull Instruction51l instruction) {
      try {
         this.writer.write(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.write(instruction.getRegisterA());
         this.writer.writeLong(instruction.getWideLiteral());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void write(@Nonnull ArrayPayload instruction) {
      try {
         this.writer.writeUshort(this.getOpcodeValue(instruction.getOpcode()));
         this.writer.writeUshort(instruction.getElementWidth());
         List<Number> elements = instruction.getArrayElements();
         this.writer.writeInt(elements.size());
         Iterator var3;
         Number element;
         label49:
         switch(instruction.getElementWidth()) {
         case 1:
            var3 = elements.iterator();

            while(true) {
               if (!var3.hasNext()) {
                  break label49;
               }

               element = (Number)var3.next();
               this.writer.write(element.byteValue());
            }
         case 2:
            var3 = elements.iterator();

            while(var3.hasNext()) {
               element = (Number)var3.next();
               this.writer.writeShort(element.shortValue());
            }
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            break;
         case 4:
            var3 = elements.iterator();

            while(true) {
               if (!var3.hasNext()) {
                  break label49;
               }

               element = (Number)var3.next();
               this.writer.writeInt(element.intValue());
            }
         case 8:
            var3 = elements.iterator();

            while(var3.hasNext()) {
               element = (Number)var3.next();
               this.writer.writeLong(element.longValue());
            }
         }

         if ((this.writer.getPosition() & 1) != 0) {
            this.writer.write(0);
         }

      } catch (IOException var5) {
         throw new RuntimeException(var5);
      }
   }

   public void write(@Nonnull SparseSwitchPayload instruction) {
      try {
         this.writer.writeUbyte(0);
         this.writer.writeUbyte(this.getOpcodeValue(instruction.getOpcode()) >> 8);
         List<? extends SwitchElement> elements = Ordering.from(this.switchElementComparator).immutableSortedCopy(instruction.getSwitchElements());
         this.writer.writeUshort(elements.size());
         Iterator var3 = elements.iterator();

         SwitchElement element;
         while(var3.hasNext()) {
            element = (SwitchElement)var3.next();
            this.writer.writeInt(element.getKey());
         }

         var3 = elements.iterator();

         while(var3.hasNext()) {
            element = (SwitchElement)var3.next();
            this.writer.writeInt(element.getOffset());
         }

      } catch (IOException var5) {
         throw new RuntimeException(var5);
      }
   }

   public void write(@Nonnull PackedSwitchPayload instruction) {
      try {
         this.writer.writeUbyte(0);
         this.writer.writeUbyte(this.getOpcodeValue(instruction.getOpcode()) >> 8);
         List<? extends SwitchElement> elements = instruction.getSwitchElements();
         this.writer.writeUshort(elements.size());
         if (elements.size() == 0) {
            this.writer.writeInt(0);
         } else {
            this.writer.writeInt(((SwitchElement)elements.get(0)).getKey());
            Iterator var3 = elements.iterator();

            while(var3.hasNext()) {
               SwitchElement element = (SwitchElement)var3.next();
               this.writer.writeInt(element.getOffset());
            }
         }

      } catch (IOException var5) {
         throw new RuntimeException(var5);
      }
   }

   private static int packNibbles(int a, int b) {
      return b << 4 | a;
   }

   private int getReferenceIndex(ReferenceInstruction referenceInstruction) {
      return this.getReferenceIndex(referenceInstruction.getReferenceType(), referenceInstruction.getReference());
   }

   private int getReference2Index(DualReferenceInstruction referenceInstruction) {
      return this.getReferenceIndex(referenceInstruction.getReferenceType2(), referenceInstruction.getReference2());
   }

   private int getReferenceIndex(int referenceType, Reference reference) {
      switch(referenceType) {
      case 0:
         return this.stringSection.getItemIndex((StringReference)reference);
      case 1:
         return this.typeSection.getItemIndex((TypeReference)reference);
      case 2:
         return this.fieldSection.getItemIndex((FieldReference)reference);
      case 3:
         return this.methodSection.getItemIndex((MethodReference)reference);
      case 4:
         return this.protoSection.getItemIndex((MethodProtoReference)reference);
      default:
         throw new ExceptionWithContext("Unknown reference type: %d", new Object[]{referenceType});
      }
   }
}
