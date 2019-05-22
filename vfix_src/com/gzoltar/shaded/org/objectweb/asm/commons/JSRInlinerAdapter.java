package com.gzoltar.shaded.org.objectweb.asm.commons;

import com.gzoltar.shaded.org.objectweb.asm.Label;
import com.gzoltar.shaded.org.objectweb.asm.MethodVisitor;
import com.gzoltar.shaded.org.objectweb.asm.Opcodes;
import com.gzoltar.shaded.org.objectweb.asm.tree.AbstractInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.InsnList;
import com.gzoltar.shaded.org.objectweb.asm.tree.InsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.JumpInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.LabelNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.LocalVariableNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.LookupSwitchInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.MethodNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.TableSwitchInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.TryCatchBlockNode;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class JSRInlinerAdapter extends MethodNode implements Opcodes {
   private static final boolean LOGGING = false;
   private final Map<LabelNode, BitSet> subroutineHeads;
   private final BitSet mainSubroutine;
   final BitSet dualCitizens;

   public JSRInlinerAdapter(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
      this(327680, mv, access, name, desc, signature, exceptions);
      if (this.getClass() != JSRInlinerAdapter.class) {
         throw new IllegalStateException();
      }
   }

   protected JSRInlinerAdapter(int api, MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
      super(api, access, name, desc, signature, exceptions);
      this.subroutineHeads = new HashMap();
      this.mainSubroutine = new BitSet();
      this.dualCitizens = new BitSet();
      this.mv = mv;
   }

   public void visitJumpInsn(int opcode, Label lbl) {
      super.visitJumpInsn(opcode, lbl);
      LabelNode ln = ((JumpInsnNode)this.instructions.getLast()).label;
      if (opcode == 168 && !this.subroutineHeads.containsKey(ln)) {
         this.subroutineHeads.put(ln, new BitSet());
      }

   }

   public void visitEnd() {
      if (!this.subroutineHeads.isEmpty()) {
         this.markSubroutines();
         this.emitCode();
      }

      if (this.mv != null) {
         this.accept(this.mv);
      }

   }

   private void markSubroutines() {
      BitSet anyvisited = new BitSet();
      this.markSubroutineWalk(this.mainSubroutine, 0, anyvisited);
      Iterator it = this.subroutineHeads.entrySet().iterator();

      while(it.hasNext()) {
         Entry<LabelNode, BitSet> entry = (Entry)it.next();
         LabelNode lab = (LabelNode)entry.getKey();
         BitSet sub = (BitSet)entry.getValue();
         int index = this.instructions.indexOf(lab);
         this.markSubroutineWalk(sub, index, anyvisited);
      }

   }

   private void markSubroutineWalk(BitSet sub, int index, BitSet anyvisited) {
      this.markSubroutineWalkDFS(sub, index, anyvisited);
      boolean loop = true;

      while(loop) {
         loop = false;
         Iterator it = this.tryCatchBlocks.iterator();

         while(it.hasNext()) {
            TryCatchBlockNode trycatch = (TryCatchBlockNode)it.next();
            int handlerindex = this.instructions.indexOf(trycatch.handler);
            if (!sub.get(handlerindex)) {
               int startindex = this.instructions.indexOf(trycatch.start);
               int endindex = this.instructions.indexOf(trycatch.end);
               int nextbit = sub.nextSetBit(startindex);
               if (nextbit != -1 && nextbit < endindex) {
                  this.markSubroutineWalkDFS(sub, handlerindex, anyvisited);
                  loop = true;
               }
            }
         }
      }

   }

   private void markSubroutineWalkDFS(BitSet sub, int index, BitSet anyvisited) {
      while(true) {
         AbstractInsnNode node = this.instructions.get(index);
         if (sub.get(index)) {
            return;
         }

         sub.set(index);
         if (anyvisited.get(index)) {
            this.dualCitizens.set(index);
         }

         anyvisited.set(index);
         int destidx;
         if (node.getType() == 7 && node.getOpcode() != 168) {
            JumpInsnNode jnode = (JumpInsnNode)node;
            destidx = this.instructions.indexOf(jnode.label);
            this.markSubroutineWalkDFS(sub, destidx, anyvisited);
         }

         int i;
         LabelNode l;
         if (node.getType() == 11) {
            TableSwitchInsnNode tsnode = (TableSwitchInsnNode)node;
            destidx = this.instructions.indexOf(tsnode.dflt);
            this.markSubroutineWalkDFS(sub, destidx, anyvisited);

            for(i = tsnode.labels.size() - 1; i >= 0; --i) {
               l = (LabelNode)tsnode.labels.get(i);
               destidx = this.instructions.indexOf(l);
               this.markSubroutineWalkDFS(sub, destidx, anyvisited);
            }
         }

         if (node.getType() == 12) {
            LookupSwitchInsnNode lsnode = (LookupSwitchInsnNode)node;
            destidx = this.instructions.indexOf(lsnode.dflt);
            this.markSubroutineWalkDFS(sub, destidx, anyvisited);

            for(i = lsnode.labels.size() - 1; i >= 0; --i) {
               l = (LabelNode)lsnode.labels.get(i);
               destidx = this.instructions.indexOf(l);
               this.markSubroutineWalkDFS(sub, destidx, anyvisited);
            }
         }

         switch(this.instructions.get(index).getOpcode()) {
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
         case 191:
            return;
         case 168:
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
         default:
            ++index;
            if (index >= this.instructions.size()) {
               return;
            }
         }
      }
   }

   private void emitCode() {
      LinkedList<JSRInlinerAdapter.Instantiation> worklist = new LinkedList();
      worklist.add(new JSRInlinerAdapter.Instantiation((JSRInlinerAdapter.Instantiation)null, this.mainSubroutine));
      InsnList newInstructions = new InsnList();
      List<TryCatchBlockNode> newTryCatchBlocks = new ArrayList();
      ArrayList newLocalVariables = new ArrayList();

      while(!worklist.isEmpty()) {
         JSRInlinerAdapter.Instantiation inst = (JSRInlinerAdapter.Instantiation)worklist.removeFirst();
         this.emitSubroutine(inst, worklist, newInstructions, newTryCatchBlocks, newLocalVariables);
      }

      this.instructions = newInstructions;
      this.tryCatchBlocks = newTryCatchBlocks;
      this.localVariables = newLocalVariables;
   }

   private void emitSubroutine(JSRInlinerAdapter.Instantiation instant, List<JSRInlinerAdapter.Instantiation> worklist, InsnList newInstructions, List<TryCatchBlockNode> newTryCatchBlocks, List<LocalVariableNode> newLocalVariables) {
      LabelNode duplbl = null;
      int i = 0;

      LabelNode retlabel;
      for(int c = this.instructions.size(); i < c; ++i) {
         AbstractInsnNode insn = this.instructions.get(i);
         JSRInlinerAdapter.Instantiation owner = instant.findOwner(i);
         if (insn.getType() == 8) {
            retlabel = (LabelNode)insn;
            LabelNode remap = instant.rangeLabel(retlabel);
            if (remap != duplbl) {
               newInstructions.add((AbstractInsnNode)remap);
               duplbl = remap;
            }
         } else if (owner == instant) {
            if (insn.getOpcode() != 169) {
               if (insn.getOpcode() == 168) {
                  retlabel = ((JumpInsnNode)insn).label;
                  BitSet sub = (BitSet)this.subroutineHeads.get(retlabel);
                  JSRInlinerAdapter.Instantiation newinst = new JSRInlinerAdapter.Instantiation(instant, sub);
                  LabelNode startlbl = newinst.gotoLabel(retlabel);
                  newInstructions.add((AbstractInsnNode)(new InsnNode(1)));
                  newInstructions.add((AbstractInsnNode)(new JumpInsnNode(167, startlbl)));
                  newInstructions.add((AbstractInsnNode)newinst.returnLabel);
                  worklist.add(newinst);
               } else {
                  newInstructions.add(insn.clone(instant));
               }
            } else {
               retlabel = null;

               for(JSRInlinerAdapter.Instantiation p = instant; p != null; p = p.previous) {
                  if (p.subroutine.get(i)) {
                     retlabel = p.returnLabel;
                  }
               }

               if (retlabel == null) {
                  throw new RuntimeException("Instruction #" + i + " is a RET not owned by any subroutine");
               }

               newInstructions.add((AbstractInsnNode)(new JumpInsnNode(167, retlabel)));
            }
         }
      }

      Iterator it = this.tryCatchBlocks.iterator();

      while(true) {
         TryCatchBlockNode trycatch;
         LabelNode start;
         LabelNode end;
         do {
            if (!it.hasNext()) {
               it = this.localVariables.iterator();

               while(it.hasNext()) {
                  LocalVariableNode lvnode = (LocalVariableNode)it.next();
                  start = instant.rangeLabel(lvnode.start);
                  end = instant.rangeLabel(lvnode.end);
                  if (start != end) {
                     newLocalVariables.add(new LocalVariableNode(lvnode.name, lvnode.desc, lvnode.signature, start, end, lvnode.index));
                  }
               }

               return;
            }

            trycatch = (TryCatchBlockNode)it.next();
            start = instant.rangeLabel(trycatch.start);
            end = instant.rangeLabel(trycatch.end);
         } while(start == end);

         retlabel = instant.gotoLabel(trycatch.handler);
         if (start == null || end == null || retlabel == null) {
            throw new RuntimeException("Internal error!");
         }

         newTryCatchBlocks.add(new TryCatchBlockNode(start, end, retlabel, trycatch.type));
      }
   }

   private static void log(String str) {
      System.err.println(str);
   }

   private class Instantiation extends AbstractMap<LabelNode, LabelNode> {
      final JSRInlinerAdapter.Instantiation previous;
      public final BitSet subroutine;
      public final Map<LabelNode, LabelNode> rangeTable = new HashMap();
      public final LabelNode returnLabel;

      Instantiation(JSRInlinerAdapter.Instantiation prev, BitSet sub) {
         this.previous = prev;
         this.subroutine = sub;

         for(JSRInlinerAdapter.Instantiation p = prev; p != null; p = p.previous) {
            if (p.subroutine == sub) {
               throw new RuntimeException("Recursive invocation of " + sub);
            }
         }

         if (prev != null) {
            this.returnLabel = new LabelNode();
         } else {
            this.returnLabel = null;
         }

         LabelNode duplbl = null;
         int i = 0;

         for(int c = JSRInlinerAdapter.this.instructions.size(); i < c; ++i) {
            AbstractInsnNode insn = JSRInlinerAdapter.this.instructions.get(i);
            if (insn.getType() == 8) {
               LabelNode ilbl = (LabelNode)insn;
               if (duplbl == null) {
                  duplbl = new LabelNode();
               }

               this.rangeTable.put(ilbl, duplbl);
            } else if (this.findOwner(i) == this) {
               duplbl = null;
            }
         }

      }

      public JSRInlinerAdapter.Instantiation findOwner(int i) {
         if (!this.subroutine.get(i)) {
            return null;
         } else if (!JSRInlinerAdapter.this.dualCitizens.get(i)) {
            return this;
         } else {
            JSRInlinerAdapter.Instantiation own = this;

            for(JSRInlinerAdapter.Instantiation p = this.previous; p != null; p = p.previous) {
               if (p.subroutine.get(i)) {
                  own = p;
               }
            }

            return own;
         }
      }

      public LabelNode gotoLabel(LabelNode l) {
         JSRInlinerAdapter.Instantiation owner = this.findOwner(JSRInlinerAdapter.this.instructions.indexOf(l));
         return (LabelNode)owner.rangeTable.get(l);
      }

      public LabelNode rangeLabel(LabelNode l) {
         return (LabelNode)this.rangeTable.get(l);
      }

      public Set<Entry<LabelNode, LabelNode>> entrySet() {
         return null;
      }

      public LabelNode get(Object o) {
         return this.gotoLabel((LabelNode)o);
      }
   }
}
