package soot.dava.internal.SET;

import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.util.IterableSet;

public class SETBasicBlock implements Comparable {
   private static final Logger logger = LoggerFactory.getLogger(SETBasicBlock.class);
   private SETNode entryNode;
   private SETNode exitNode;
   private final IterableSet predecessors = new IterableSet();
   private final IterableSet successors = new IterableSet();
   private final IterableSet body = new IterableSet();
   private int priority;

   public SETBasicBlock() {
      this.entryNode = this.exitNode = null;
      this.priority = -1;
   }

   public int compareTo(Object o) {
      if (o == this) {
         return 0;
      } else {
         SETBasicBlock other = (SETBasicBlock)o;
         int difference = other.get_Priority() - this.get_Priority();
         if (difference == 0) {
            difference = 1;
         }

         return difference;
      }
   }

   private int get_Priority() {
      if (this.priority == -1) {
         this.priority = 0;
         if (this.predecessors.size() == 1) {
            Iterator sit = this.successors.iterator();

            while(sit.hasNext()) {
               int sucScore = ((SETBasicBlock)sit.next()).get_Priority();
               if (sucScore > this.priority) {
                  this.priority = sucScore;
               }
            }

            ++this.priority;
         }
      }

      return this.priority;
   }

   public void add(SETNode sn) {
      if (this.body.isEmpty()) {
         this.entryNode = sn;
      }

      this.body.add(sn);
      G.v().SETBasicBlock_binding.put(sn, this);
      this.exitNode = sn;
   }

   public SETNode get_EntryNode() {
      return this.entryNode;
   }

   public SETNode get_ExitNode() {
      return this.exitNode;
   }

   public IterableSet get_Predecessors() {
      return this.predecessors;
   }

   public IterableSet get_Successors() {
      return this.successors;
   }

   public IterableSet get_Body() {
      return this.body;
   }

   public static SETBasicBlock get_SETBasicBlock(SETNode o) {
      return (SETBasicBlock)G.v().SETBasicBlock_binding.get(o);
   }

   public void printSig() {
      Iterator it = this.body.iterator();

      while(it.hasNext()) {
         ((SETNode)it.next()).dump();
      }

   }

   public void dump() {
      this.printSig();
      logger.debug("=== preds ===");
      Iterator it = this.predecessors.iterator();

      while(it.hasNext()) {
         ((SETBasicBlock)it.next()).printSig();
      }

      logger.debug("=== succs ===");
      it = this.successors.iterator();

      while(it.hasNext()) {
         ((SETBasicBlock)it.next()).printSig();
      }

   }
}
