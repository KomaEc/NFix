package groovyjarjarasm.asm.commons;

import groovyjarjarasm.asm.Label;
import groovyjarjarasm.asm.MethodAdapter;
import groovyjarjarasm.asm.MethodVisitor;
import groovyjarjarasm.asm.Opcodes;
import groovyjarjarasm.asm.Type;

public class LocalVariablesSorter extends MethodAdapter {
   private static final Type OBJECT_TYPE = Type.getObjectType("java/lang/Object");
   private int[] mapping = new int[40];
   private Object[] newLocals = new Object[20];
   protected final int firstLocal;
   protected int nextLocal;
   private boolean changed;

   public LocalVariablesSorter(int var1, String var2, MethodVisitor var3) {
      super(var3);
      Type[] var4 = Type.getArgumentTypes(var2);
      this.nextLocal = (8 & var1) == 0 ? 1 : 0;

      for(int var5 = 0; var5 < var4.length; ++var5) {
         this.nextLocal += var4[var5].getSize();
      }

      this.firstLocal = this.nextLocal;
   }

   public void visitVarInsn(int var1, int var2) {
      Type var3;
      switch(var1) {
      case 21:
      case 54:
         var3 = Type.INT_TYPE;
         break;
      case 22:
      case 55:
         var3 = Type.LONG_TYPE;
         break;
      case 23:
      case 56:
         var3 = Type.FLOAT_TYPE;
         break;
      case 24:
      case 57:
         var3 = Type.DOUBLE_TYPE;
         break;
      case 25:
      case 58:
         var3 = OBJECT_TYPE;
         break;
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 37:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      default:
         var3 = Type.VOID_TYPE;
      }

      this.mv.visitVarInsn(var1, this.remap(var2, var3));
   }

   public void visitIincInsn(int var1, int var2) {
      this.mv.visitIincInsn(this.remap(var1, Type.INT_TYPE), var2);
   }

   public void visitMaxs(int var1, int var2) {
      this.mv.visitMaxs(var1, this.nextLocal);
   }

   public void visitLocalVariable(String var1, String var2, String var3, Label var4, Label var5, int var6) {
      int var7 = this.remap(var6, Type.getType(var2));
      this.mv.visitLocalVariable(var1, var2, var3, var4, var5, var7);
   }

   public void visitFrame(int var1, int var2, Object[] var3, int var4, Object[] var5) {
      if (var1 != -1) {
         throw new IllegalStateException("ClassReader.accept() should be called with EXPAND_FRAMES flag");
      } else if (!this.changed) {
         this.mv.visitFrame(var1, var2, var3, var4, var5);
      } else {
         Object[] var6 = new Object[this.newLocals.length];
         System.arraycopy(this.newLocals, 0, var6, 0, var6.length);
         int var7 = 0;

         int var8;
         for(var8 = 0; var8 < var2; ++var8) {
            Object var9 = var3[var8];
            int var10 = var9 != Opcodes.LONG && var9 != Opcodes.DOUBLE ? 1 : 2;
            if (var9 != Opcodes.TOP) {
               this.setFrameLocal(this.remap(var7, var10), var9);
            }

            var7 += var10;
         }

         var7 = 0;
         var8 = 0;

         for(int var11 = 0; var7 < this.newLocals.length; ++var11) {
            Object var12 = this.newLocals[var7++];
            if (var12 != null && var12 != Opcodes.TOP) {
               this.newLocals[var11] = var12;
               var8 = var11 + 1;
               if (var12 == Opcodes.LONG || var12 == Opcodes.DOUBLE) {
                  ++var7;
               }
            } else {
               this.newLocals[var11] = Opcodes.TOP;
            }
         }

         this.mv.visitFrame(var1, var8, this.newLocals, var4, var5);
         this.newLocals = var6;
      }
   }

   public int newLocal(Type var1) {
      Object var2;
      switch(var1.getSort()) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
         var2 = Opcodes.INTEGER;
         break;
      case 6:
         var2 = Opcodes.FLOAT;
         break;
      case 7:
         var2 = Opcodes.LONG;
         break;
      case 8:
         var2 = Opcodes.DOUBLE;
         break;
      case 9:
         var2 = var1.getDescriptor();
         break;
      default:
         var2 = var1.getInternalName();
      }

      int var3 = this.nextLocal;
      this.nextLocal += var1.getSize();
      this.setLocalType(var3, var1);
      this.setFrameLocal(var3, var2);
      return var3;
   }

   protected void setLocalType(int var1, Type var2) {
   }

   private void setFrameLocal(int var1, Object var2) {
      int var3 = this.newLocals.length;
      if (var1 >= var3) {
         Object[] var4 = new Object[Math.max(2 * var3, var1 + 1)];
         System.arraycopy(this.newLocals, 0, var4, 0, var3);
         this.newLocals = var4;
      }

      this.newLocals[var1] = var2;
   }

   private int remap(int var1, Type var2) {
      if (var1 < this.firstLocal) {
         return var1;
      } else {
         int var3 = 2 * var1 + var2.getSize() - 1;
         int var4 = this.mapping.length;
         if (var3 >= var4) {
            int[] var5 = new int[Math.max(2 * var4, var3 + 1)];
            System.arraycopy(this.mapping, 0, var5, 0, var4);
            this.mapping = var5;
         }

         int var6 = this.mapping[var3];
         if (var6 == 0) {
            var6 = this.newLocalMapping(var2);
            this.setLocalType(var6, var2);
            this.mapping[var3] = var6 + 1;
         } else {
            --var6;
         }

         if (var6 != var1) {
            this.changed = true;
         }

         return var6;
      }
   }

   protected int newLocalMapping(Type var1) {
      int var2 = this.nextLocal;
      this.nextLocal += var1.getSize();
      return var2;
   }

   private int remap(int var1, int var2) {
      if (var1 >= this.firstLocal && this.changed) {
         int var3 = 2 * var1 + var2 - 1;
         int var4 = var3 < this.mapping.length ? this.mapping[var3] : 0;
         if (var4 == 0) {
            throw new IllegalStateException("Unknown local variable " + var1);
         } else {
            return var4 - 1;
         }
      } else {
         return var1;
      }
   }
}
