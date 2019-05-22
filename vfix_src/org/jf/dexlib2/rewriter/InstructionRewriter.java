package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction20bc;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.instruction.formats.Instruction31c;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.TypeReference;

public class InstructionRewriter implements Rewriter<Instruction> {
   @Nonnull
   protected final Rewriters rewriters;

   public InstructionRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public Instruction rewrite(@Nonnull Instruction instruction) {
      if (instruction instanceof ReferenceInstruction) {
         switch(instruction.getOpcode().format) {
         case Format20bc:
            return new InstructionRewriter.RewrittenInstruction20bc((Instruction20bc)instruction);
         case Format21c:
            return new InstructionRewriter.RewrittenInstruction21c((Instruction21c)instruction);
         case Format22c:
            return new InstructionRewriter.RewrittenInstruction22c((Instruction22c)instruction);
         case Format31c:
            return new InstructionRewriter.RewrittenInstruction31c((Instruction31c)instruction);
         case Format35c:
            return new InstructionRewriter.RewrittenInstruction35c((Instruction35c)instruction);
         case Format3rc:
            return new InstructionRewriter.RewrittenInstruction3rc((Instruction3rc)instruction);
         default:
            throw new IllegalArgumentException();
         }
      } else {
         return instruction;
      }
   }

   protected class RewrittenInstruction3rc extends InstructionRewriter.BaseRewrittenReferenceInstruction<Instruction3rc> implements Instruction3rc {
      public RewrittenInstruction3rc(@Nonnull Instruction3rc instruction) {
         super(instruction);
      }

      public int getStartRegister() {
         return ((Instruction3rc)this.instruction).getStartRegister();
      }

      public int getRegisterCount() {
         return ((Instruction3rc)this.instruction).getRegisterCount();
      }
   }

   protected class RewrittenInstruction35c extends InstructionRewriter.BaseRewrittenReferenceInstruction<Instruction35c> implements Instruction35c {
      public RewrittenInstruction35c(@Nonnull Instruction35c instruction) {
         super(instruction);
      }

      public int getRegisterC() {
         return ((Instruction35c)this.instruction).getRegisterC();
      }

      public int getRegisterE() {
         return ((Instruction35c)this.instruction).getRegisterE();
      }

      public int getRegisterG() {
         return ((Instruction35c)this.instruction).getRegisterG();
      }

      public int getRegisterCount() {
         return ((Instruction35c)this.instruction).getRegisterCount();
      }

      public int getRegisterD() {
         return ((Instruction35c)this.instruction).getRegisterD();
      }

      public int getRegisterF() {
         return ((Instruction35c)this.instruction).getRegisterF();
      }
   }

   protected class RewrittenInstruction31c extends InstructionRewriter.BaseRewrittenReferenceInstruction<Instruction31c> implements Instruction31c {
      public RewrittenInstruction31c(@Nonnull Instruction31c instruction) {
         super(instruction);
      }

      public int getRegisterA() {
         return ((Instruction31c)this.instruction).getRegisterA();
      }
   }

   protected class RewrittenInstruction22c extends InstructionRewriter.BaseRewrittenReferenceInstruction<Instruction22c> implements Instruction22c {
      public RewrittenInstruction22c(@Nonnull Instruction22c instruction) {
         super(instruction);
      }

      public int getRegisterA() {
         return ((Instruction22c)this.instruction).getRegisterA();
      }

      public int getRegisterB() {
         return ((Instruction22c)this.instruction).getRegisterB();
      }
   }

   protected class RewrittenInstruction21c extends InstructionRewriter.BaseRewrittenReferenceInstruction<Instruction21c> implements Instruction21c {
      public RewrittenInstruction21c(@Nonnull Instruction21c instruction) {
         super(instruction);
      }

      public int getRegisterA() {
         return ((Instruction21c)this.instruction).getRegisterA();
      }
   }

   protected class RewrittenInstruction20bc extends InstructionRewriter.BaseRewrittenReferenceInstruction<Instruction20bc> implements Instruction20bc {
      public RewrittenInstruction20bc(@Nonnull Instruction20bc instruction) {
         super(instruction);
      }

      public int getVerificationError() {
         return ((Instruction20bc)this.instruction).getVerificationError();
      }
   }

   protected class BaseRewrittenReferenceInstruction<T extends ReferenceInstruction> implements ReferenceInstruction {
      @Nonnull
      protected T instruction;

      protected BaseRewrittenReferenceInstruction(@Nonnull T instruction) {
         this.instruction = instruction;
      }

      @Nonnull
      public Reference getReference() {
         switch(this.getReferenceType()) {
         case 0:
            return this.instruction.getReference();
         case 1:
            return RewriterUtils.rewriteTypeReference(InstructionRewriter.this.rewriters.getTypeRewriter(), (TypeReference)this.instruction.getReference());
         case 2:
            return (Reference)InstructionRewriter.this.rewriters.getFieldReferenceRewriter().rewrite((FieldReference)this.instruction.getReference());
         case 3:
            return (Reference)InstructionRewriter.this.rewriters.getMethodReferenceRewriter().rewrite((MethodReference)this.instruction.getReference());
         default:
            throw new IllegalArgumentException();
         }
      }

      public int getReferenceType() {
         return this.instruction.getReferenceType();
      }

      public Opcode getOpcode() {
         return this.instruction.getOpcode();
      }

      public int getCodeUnits() {
         return this.instruction.getCodeUnits();
      }
   }
}
