package com.gzoltar.shaded.org.objectweb.asm.commons;

import com.gzoltar.shaded.org.objectweb.asm.Handle;
import com.gzoltar.shaded.org.objectweb.asm.Label;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import com.gzoltar.shaded.org.objectweb.asm.Opcodes;
import com.gzoltar.shaded.org.objectweb.asm.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AdviceAdapter extends GeneratorAdapter implements Opcodes {
   private static final Object THIS = new Object();
   private static final Object OTHER = new Object();
   protected int methodAccess;
   protected String methodDesc;
   private boolean constructor;
   private boolean superInitialized;
   private List<Object> stackFrame;
   private Map<Label, List<Object>> branches;

   protected AdviceAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
      super(api, mv, access, name, desc);
      this.methodAccess = access;
      this.methodDesc = desc;
      this.constructor = "<init>".equals(name);
   }

   public void visitCode() {
      this.mv.visitCode();
      if (this.constructor) {
         this.stackFrame = new ArrayList();
         this.branches = new HashMap();
      } else {
         this.superInitialized = true;
         this.onMethodEnter();
      }

   }

   public void visitLabel(Label label) {
      this.mv.visitLabel(label);
      if (this.constructor && this.branches != null) {
         List<Object> frame = (List)this.branches.get(label);
         if (frame != null) {
            this.stackFrame = frame;
            this.branches.remove(label);
         }
      }

   }

   public void visitInsn(int opcode) {
      if (this.constructor) {
         int s;
         switch(opcode) {
         case 0:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
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
         case 47:
         case 49:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 116:
         case 117:
         case 118:
         case 119:
         case 132:
         case 134:
         case 138:
         case 139:
         case 143:
         case 145:
         case 146:
         case 147:
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
         case 167:
         case 168:
         case 169:
         case 170:
         case 171:
         case 178:
         case 179:
         case 180:
         case 181:
         case 182:
         case 183:
         case 184:
         case 185:
         case 186:
         case 187:
         case 188:
         case 189:
         case 190:
         case 192:
         case 193:
         default:
            break;
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 11:
         case 12:
         case 13:
         case 133:
         case 135:
         case 140:
         case 141:
            this.pushValue(OTHER);
            break;
         case 9:
         case 10:
         case 14:
         case 15:
            this.pushValue(OTHER);
            this.pushValue(OTHER);
            break;
         case 46:
         case 48:
         case 50:
         case 51:
         case 52:
         case 53:
         case 87:
         case 96:
         case 98:
         case 100:
         case 102:
         case 104:
         case 106:
         case 108:
         case 110:
         case 112:
         case 114:
         case 120:
         case 121:
         case 122:
         case 123:
         case 124:
         case 125:
         case 126:
         case 128:
         case 130:
         case 136:
         case 137:
         case 142:
         case 144:
         case 149:
         case 150:
         case 194:
         case 195:
            this.popValue();
            break;
         case 79:
         case 81:
         case 83:
         case 84:
         case 85:
         case 86:
         case 148:
         case 151:
         case 152:
            this.popValue();
            this.popValue();
            this.popValue();
            break;
         case 80:
         case 82:
            this.popValue();
            this.popValue();
            this.popValue();
            this.popValue();
            break;
         case 88:
         case 97:
         case 99:
         case 101:
         case 103:
         case 105:
         case 107:
         case 109:
         case 111:
         case 113:
         case 115:
         case 127:
         case 129:
         case 131:
            this.popValue();
            this.popValue();
            break;
         case 89:
            this.pushValue(this.peekValue());
            break;
         case 90:
            s = this.stackFrame.size();
            this.stackFrame.add(s - 2, this.stackFrame.get(s - 1));
            break;
         case 91:
            s = this.stackFrame.size();
            this.stackFrame.add(s - 3, this.stackFrame.get(s - 1));
            break;
         case 92:
            s = this.stackFrame.size();
            this.stackFrame.add(s - 2, this.stackFrame.get(s - 1));
            this.stackFrame.add(s - 2, this.stackFrame.get(s - 1));
            break;
         case 93:
            s = this.stackFrame.size();
            this.stackFrame.add(s - 3, this.stackFrame.get(s - 1));
            this.stackFrame.add(s - 3, this.stackFrame.get(s - 1));
            break;
         case 94:
            s = this.stackFrame.size();
            this.stackFrame.add(s - 4, this.stackFrame.get(s - 1));
            this.stackFrame.add(s - 4, this.stackFrame.get(s - 1));
            break;
         case 95:
            s = this.stackFrame.size();
            this.stackFrame.add(s - 2, this.stackFrame.get(s - 1));
            this.stackFrame.remove(s);
            break;
         case 172:
         case 174:
         case 176:
         case 191:
            this.popValue();
            this.onMethodExit(opcode);
            break;
         case 173:
         case 175:
            this.popValue();
            this.popValue();
            this.onMethodExit(opcode);
            break;
         case 177:
            this.onMethodExit(opcode);
         }
      } else {
         switch(opcode) {
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
         case 177:
         case 191:
            this.onMethodExit(opcode);
         case 178:
         case 179:
         case 180:
         case 181:
         case 182:
         case 183:
         case 184:
         case 185:
         case 186:
         case 187:
         case 188:
         case 189:
         case 190:
         }
      }

      this.mv.visitInsn(opcode);
   }

   public void visitVarInsn(int opcode, int var) {
      super.visitVarInsn(opcode, var);
      if (this.constructor) {
         switch(opcode) {
         case 21:
         case 23:
            this.pushValue(OTHER);
            break;
         case 22:
         case 24:
            this.pushValue(OTHER);
            this.pushValue(OTHER);
            break;
         case 25:
            this.pushValue(var == 0 ? THIS : OTHER);
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
            break;
         case 54:
         case 56:
         case 58:
            this.popValue();
            break;
         case 55:
         case 57:
            this.popValue();
            this.popValue();
         }
      }

   }

   public void visitFieldInsn(int opcode, String owner, String name, String desc) {
      this.mv.visitFieldInsn(opcode, owner, name, desc);
      if (this.constructor) {
         char c = desc.charAt(0);
         boolean longOrDouble = c == 'J' || c == 'D';
         switch(opcode) {
         case 178:
            this.pushValue(OTHER);
            if (longOrDouble) {
               this.pushValue(OTHER);
            }
            break;
         case 179:
            this.popValue();
            if (longOrDouble) {
               this.popValue();
            }
            break;
         case 180:
         default:
            if (longOrDouble) {
               this.pushValue(OTHER);
            }
            break;
         case 181:
            this.popValue();
            this.popValue();
            if (longOrDouble) {
               this.popValue();
            }
         }
      }

   }

   public void visitIntInsn(int opcode, int operand) {
      this.mv.visitIntInsn(opcode, operand);
      if (this.constructor && opcode != 188) {
         this.pushValue(OTHER);
      }

   }

   public void visitLdcInsn(Object cst) {
      this.mv.visitLdcInsn(cst);
      if (this.constructor) {
         this.pushValue(OTHER);
         if (cst instanceof Double || cst instanceof Long) {
            this.pushValue(OTHER);
         }
      }

   }

   public void visitMultiANewArrayInsn(String desc, int dims) {
      this.mv.visitMultiANewArrayInsn(desc, dims);
      if (this.constructor) {
         for(int i = 0; i < dims; ++i) {
            this.popValue();
         }

         this.pushValue(OTHER);
      }

   }

   public void visitTypeInsn(int opcode, String type) {
      this.mv.visitTypeInsn(opcode, type);
      if (this.constructor && opcode == 187) {
         this.pushValue(OTHER);
      }

   }

   /** @deprecated */
   @Deprecated
   public void visitMethodInsn(int opcode, String owner, String name, String desc) {
      if (this.api >= 327680) {
         super.visitMethodInsn(opcode, owner, name, desc);
      } else {
         this.doVisitMethodInsn(opcode, owner, name, desc, opcode == 185);
      }
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      if (this.api < 327680) {
         super.visitMethodInsn(opcode, owner, name, desc, itf);
      } else {
         this.doVisitMethodInsn(opcode, owner, name, desc, itf);
      }
   }

   private void doVisitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
      if (this.constructor) {
         Type[] types = Type.getArgumentTypes(desc);

         for(int i = 0; i < types.length; ++i) {
            this.popValue();
            if (types[i].getSize() == 2) {
               this.popValue();
            }
         }

         switch(opcode) {
         case 182:
         case 185:
            this.popValue();
            break;
         case 183:
            Object type = this.popValue();
            if (type == THIS && !this.superInitialized) {
               this.onMethodEnter();
               this.superInitialized = true;
               this.constructor = false;
            }
         case 184:
         }

         Type returnType = Type.getReturnType(desc);
         if (returnType != Type.VOID_TYPE) {
            this.pushValue(OTHER);
            if (returnType.getSize() == 2) {
               this.pushValue(OTHER);
            }
         }
      }

   }

   public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
      this.mv.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
      if (this.constructor) {
         Type[] types = Type.getArgumentTypes(desc);

         for(int i = 0; i < types.length; ++i) {
            this.popValue();
            if (types[i].getSize() == 2) {
               this.popValue();
            }
         }

         Type returnType = Type.getReturnType(desc);
         if (returnType != Type.VOID_TYPE) {
            this.pushValue(OTHER);
            if (returnType.getSize() == 2) {
               this.pushValue(OTHER);
            }
         }
      }

   }

   public void visitJumpInsn(int opcode, Label label) {
      this.mv.visitJumpInsn(opcode, label);
      if (this.constructor) {
         switch(opcode) {
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
         case 198:
         case 199:
            this.popValue();
            break;
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
            this.popValue();
            this.popValue();
         case 167:
         case 169:
         case 170:
         case 171:
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
         case 177:
         case 178:
         case 179:
         case 180:
         case 181:
         case 182:
         case 183:
         case 184:
         case 185:
         case 186:
         case 187:
         case 188:
         case 189:
         case 190:
         case 191:
         case 192:
         case 193:
         case 194:
         case 195:
         case 196:
         case 197:
         default:
            break;
         case 168:
            this.pushValue(OTHER);
         }

         this.addBranch(label);
      }

   }

   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
      this.mv.visitLookupSwitchInsn(dflt, keys, labels);
      if (this.constructor) {
         this.popValue();
         this.addBranches(dflt, labels);
      }

   }

   public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
      this.mv.visitTableSwitchInsn(min, max, dflt, labels);
      if (this.constructor) {
         this.popValue();
         this.addBranches(dflt, labels);
      }

   }

   public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
      super.visitTryCatchBlock(start, end, handler, type);
      if (this.constructor && !this.branches.containsKey(handler)) {
         List<Object> stackFrame = new ArrayList();
         stackFrame.add(OTHER);
         this.branches.put(handler, stackFrame);
      }

   }

   private void addBranches(Label dflt, Label[] labels) {
      this.addBranch(dflt);

      for(int i = 0; i < labels.length; ++i) {
         this.addBranch(labels[i]);
      }

   }

   private void addBranch(Label label) {
      if (!this.branches.containsKey(label)) {
         this.branches.put(label, new ArrayList(this.stackFrame));
      }
   }

   private Object popValue() {
      return this.stackFrame.remove(this.stackFrame.size() - 1);
   }

   private Object peekValue() {
      return this.stackFrame.get(this.stackFrame.size() - 1);
   }

   private void pushValue(Object o) {
      this.stackFrame.add(o);
   }

   protected void onMethodEnter() {
   }

   protected void onMethodExit(int opcode) {
   }
}
