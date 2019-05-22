package com.gzoltar.shaded.org.jacoco.core.internal.analysis;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICounter;
import com.gzoltar.shaded.org.jacoco.core.analysis.ILine;

public abstract class LineImpl implements ILine {
   private static final int SINGLETON_INS_LIMIT = 8;
   private static final int SINGLETON_BRA_LIMIT = 4;
   private static final LineImpl[][][][] SINGLETONS = new LineImpl[9][][][];
   public static final LineImpl EMPTY;
   protected CounterImpl instructions;
   protected CounterImpl branches;

   private static LineImpl getInstance(CounterImpl instructions, CounterImpl branches) {
      int im = instructions.getMissedCount();
      int ic = instructions.getCoveredCount();
      int bm = branches.getMissedCount();
      int bc = branches.getCoveredCount();
      return (LineImpl)(im <= 8 && ic <= 8 && bm <= 4 && bc <= 4 ? SINGLETONS[im][ic][bm][bc] : new LineImpl.Var(instructions, branches));
   }

   private LineImpl(CounterImpl instructions, CounterImpl branches) {
      this.instructions = instructions;
      this.branches = branches;
   }

   public abstract LineImpl increment(ICounter var1, ICounter var2);

   public int getStatus() {
      return this.instructions.getStatus() | this.branches.getStatus();
   }

   public ICounter getInstructionCounter() {
      return this.instructions;
   }

   public ICounter getBranchCounter() {
      return this.branches;
   }

   public int hashCode() {
      return 23 * this.instructions.hashCode() ^ this.branches.hashCode();
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ILine)) {
         return false;
      } else {
         ILine that = (ILine)obj;
         return this.instructions.equals(that.getInstructionCounter()) && this.branches.equals(that.getBranchCounter());
      }
   }

   // $FF: synthetic method
   LineImpl(CounterImpl x0, CounterImpl x1, Object x2) {
      this(x0, x1);
   }

   static {
      for(int i = 0; i <= 8; ++i) {
         SINGLETONS[i] = new LineImpl[9][][];

         for(int j = 0; j <= 8; ++j) {
            SINGLETONS[i][j] = new LineImpl[5][];

            for(int k = 0; k <= 4; ++k) {
               SINGLETONS[i][j][k] = new LineImpl[5];

               for(int l = 0; l <= 4; ++l) {
                  SINGLETONS[i][j][k][l] = new LineImpl.Fix(i, j, k, l);
               }
            }
         }
      }

      EMPTY = SINGLETONS[0][0][0][0];
   }

   private static final class Fix extends LineImpl {
      public Fix(int im, int ic, int bm, int bc) {
         super(CounterImpl.getInstance(im, ic), CounterImpl.getInstance(bm, bc), null);
      }

      public LineImpl increment(ICounter instructions, ICounter branches) {
         return LineImpl.getInstance(this.instructions.increment(instructions), this.branches.increment(branches));
      }
   }

   private static final class Var extends LineImpl {
      Var(CounterImpl instructions, CounterImpl branches) {
         super(instructions, branches, null);
      }

      public LineImpl increment(ICounter instructions, ICounter branches) {
         this.instructions = this.instructions.increment(instructions);
         this.branches = this.branches.increment(branches);
         return this;
      }
   }
}
