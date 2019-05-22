package com.gzoltar.shaded.org.pitest.coverage.analysis;

import com.gzoltar.shaded.org.pitest.reloc.asm.tree.AbstractInsnNode;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.FrameNode;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.InsnList;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.JumpInsnNode;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.LabelNode;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.LineNumberNode;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.LookupSwitchInsnNode;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.MethodNode;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.TableSwitchInsnNode;
import com.gzoltar.shaded.org.pitest.reloc.asm.tree.TryCatchBlockNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class ControlFlowAnalyser {
   private static final int LIKELY_NUMBER_OF_LINES_PER_BLOCK = 7;

   public List<Block> analyze(MethodNode mn) {
      List<Block> blocks = new ArrayList(mn.instructions.size());
      Set<AbstractInsnNode> jumpTargets = this.findJumpTargets(mn.instructions);
      this.addtryCatchBoundaries(mn, jumpTargets);
      Set<Integer> blockLines = smallSet();
      int lastLine = Integer.MIN_VALUE;
      int lastInstruction = mn.instructions.size() - 1;
      int blockStart = 0;

      for(int i = 0; i != mn.instructions.size(); ++i) {
         AbstractInsnNode ins = mn.instructions.get(i);
         if (ins instanceof LineNumberNode) {
            LineNumberNode lnn = (LineNumberNode)ins;
            blockLines.add(lnn.line);
            lastLine = lnn.line;
         } else if (jumpTargets.contains(ins) && blockStart != i) {
            blocks.add(new Block(blockStart, i - 1, blockLines));
            blockStart = i;
            blockLines = smallSet();
         } else if (this.endsBlock(ins)) {
            blocks.add(new Block(blockStart, i, blockLines));
            blockStart = i + 1;
            blockLines = smallSet();
         } else if (lastLine != Integer.MIN_VALUE && this.isInstruction(ins)) {
            blockLines.add(lastLine);
         }
      }

      if (blockStart != lastInstruction) {
         blocks.add(new Block(blockStart, lastInstruction, blockLines));
      }

      return blocks;
   }

   private static HashSet<Integer> smallSet() {
      return new HashSet(7);
   }

   private boolean isInstruction(AbstractInsnNode ins) {
      return !(ins instanceof LabelNode) && !(ins instanceof FrameNode);
   }

   private void addtryCatchBoundaries(MethodNode mn, Set<AbstractInsnNode> jumpTargets) {
      Iterator i$ = mn.tryCatchBlocks.iterator();

      while(i$.hasNext()) {
         Object each = i$.next();
         TryCatchBlockNode tcb = (TryCatchBlockNode)each;
         jumpTargets.add(tcb.handler);
      }

   }

   private boolean endsBlock(AbstractInsnNode ins) {
      return ins instanceof JumpInsnNode || this.isReturn(ins);
   }

   private boolean isReturn(AbstractInsnNode ins) {
      int opcode = ins.getOpcode();
      switch(opcode) {
      case 172:
      case 173:
      case 174:
      case 175:
      case 176:
      case 177:
      case 191:
         return true;
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
         return false;
      }
   }

   private Set<AbstractInsnNode> findJumpTargets(InsnList instructions) {
      Set<AbstractInsnNode> jumpTargets = new HashSet();
      ListIterator it = instructions.iterator();

      while(it.hasNext()) {
         Object o = it.next();
         if (o instanceof JumpInsnNode) {
            jumpTargets.add(((JumpInsnNode)o).label);
         } else if (o instanceof TableSwitchInsnNode) {
            TableSwitchInsnNode twn = (TableSwitchInsnNode)o;
            jumpTargets.add(twn.dflt);
            jumpTargets.addAll(twn.labels);
         } else if (o instanceof LookupSwitchInsnNode) {
            LookupSwitchInsnNode lsn = (LookupSwitchInsnNode)o;
            jumpTargets.add(lsn.dflt);
            jumpTargets.addAll(lsn.labels);
         }
      }

      return jumpTargets;
   }
}
