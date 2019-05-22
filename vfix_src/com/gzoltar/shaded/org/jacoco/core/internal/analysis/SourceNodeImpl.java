package com.gzoltar.shaded.org.jacoco.core.internal.analysis;

import com.gzoltar.shaded.org.jacoco.core.analysis.CoverageNodeImpl;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICounter;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.analysis.ILine;
import com.gzoltar.shaded.org.jacoco.core.analysis.ISourceNode;

public class SourceNodeImpl extends CoverageNodeImpl implements ISourceNode {
   private LineImpl[] lines = null;
   private int offset = -1;

   public SourceNodeImpl(ICoverageNode.ElementType elementType, String name) {
      super(elementType, name);
   }

   public void ensureCapacity(int first, int last) {
      if (first != -1 && last != -1) {
         if (this.lines == null) {
            this.offset = first;
            this.lines = new LineImpl[last - first + 1];
         } else {
            int newFirst = Math.min(this.getFirstLine(), first);
            int newLast = Math.max(this.getLastLine(), last);
            int newLength = newLast - newFirst + 1;
            if (newLength > this.lines.length) {
               LineImpl[] newLines = new LineImpl[newLength];
               System.arraycopy(this.lines, 0, newLines, this.offset - newFirst, this.lines.length);
               this.offset = newFirst;
               this.lines = newLines;
            }
         }

      }
   }

   public void increment(ISourceNode child) {
      this.instructionCounter = this.instructionCounter.increment(child.getInstructionCounter());
      this.branchCounter = this.branchCounter.increment(child.getBranchCounter());
      this.complexityCounter = this.complexityCounter.increment(child.getComplexityCounter());
      this.methodCounter = this.methodCounter.increment(child.getMethodCounter());
      this.classCounter = this.classCounter.increment(child.getClassCounter());
      int firstLine = child.getFirstLine();
      if (firstLine != -1) {
         int lastLine = child.getLastLine();
         this.ensureCapacity(firstLine, lastLine);

         for(int i = firstLine; i <= lastLine; ++i) {
            ILine line = child.getLine(i);
            this.incrementLine(line.getInstructionCounter(), line.getBranchCounter(), i);
         }
      }

   }

   public void increment(ICounter instructions, ICounter branches, int line) {
      if (line != -1) {
         this.incrementLine(instructions, branches, line);
      }

      this.instructionCounter = this.instructionCounter.increment(instructions);
      this.branchCounter = this.branchCounter.increment(branches);
   }

   private void incrementLine(ICounter instructions, ICounter branches, int line) {
      this.ensureCapacity(line, line);
      LineImpl l = this.getLine(line);
      int oldTotal = l.getInstructionCounter().getTotalCount();
      int oldCovered = l.getInstructionCounter().getCoveredCount();
      this.lines[line - this.offset] = l.increment(instructions, branches);
      if (instructions.getTotalCount() > 0) {
         if (instructions.getCoveredCount() == 0) {
            if (oldTotal == 0) {
               this.lineCounter = this.lineCounter.increment(CounterImpl.COUNTER_1_0);
            }
         } else if (oldTotal == 0) {
            this.lineCounter = this.lineCounter.increment(CounterImpl.COUNTER_0_1);
         } else if (oldCovered == 0) {
            this.lineCounter = this.lineCounter.increment(-1, 1);
         }
      }

   }

   public int getFirstLine() {
      return this.offset;
   }

   public int getLastLine() {
      return this.lines == null ? -1 : this.offset + this.lines.length - 1;
   }

   public LineImpl getLine(int nr) {
      if (this.lines != null && nr >= this.getFirstLine() && nr <= this.getLastLine()) {
         LineImpl line = this.lines[nr - this.offset];
         return line == null ? LineImpl.EMPTY : line;
      } else {
         return LineImpl.EMPTY;
      }
   }
}
