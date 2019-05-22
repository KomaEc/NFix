package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.jimple.NopStmt;
import soot.util.Chain;

public abstract class BlockGraph implements DirectedGraph<Block> {
   protected Body mBody;
   protected Chain<Unit> mUnits;
   protected List<Block> mBlocks;
   protected List<Block> mHeads = new ArrayList();
   protected List<Block> mTails = new ArrayList();

   protected BlockGraph(UnitGraph unitGraph) {
      this.mBody = unitGraph.getBody();
      this.mUnits = this.mBody.getUnits();
      Set<Unit> leaders = this.computeLeaders(unitGraph);
      this.buildBlocks(leaders, unitGraph);
   }

   protected Set<Unit> computeLeaders(UnitGraph unitGraph) {
      Body body = unitGraph.getBody();
      if (body != this.mBody) {
         throw new RuntimeException("BlockGraph.computeLeaders() called with a UnitGraph that doesn't match its mBody.");
      } else {
         Set<Unit> leaders = new HashSet();
         Chain<Trap> traps = body.getTraps();
         Iterator unitIt = traps.iterator();

         while(unitIt.hasNext()) {
            Trap trap = (Trap)unitIt.next();
            leaders.add(trap.getHandlerUnit());
         }

         unitIt = body.getUnits().iterator();

         while(true) {
            List successors;
            int succCount;
            Unit u;
            do {
               if (!unitIt.hasNext()) {
                  return leaders;
               }

               u = (Unit)unitIt.next();
               List<Unit> predecessors = unitGraph.getPredsOf(u);
               int predCount = predecessors.size();
               successors = unitGraph.getSuccsOf(u);
               succCount = successors.size();
               if (predCount != 1) {
                  leaders.add(u);
               }
            } while(succCount <= 1 && !u.branches());

            Iterator it = successors.iterator();

            while(it.hasNext()) {
               leaders.add((Unit)it.next());
            }
         }
      }
   }

   protected Map<Unit, Block> buildBlocks(Set<Unit> leaders, UnitGraph unitGraph) {
      List<Block> blockList = new ArrayList(leaders.size());
      Map<Unit, Block> unitToBlock = new HashMap();
      Unit blockHead = null;
      int blockLength = 0;
      Iterator<Unit> unitIt = this.mUnits.iterator();
      if (unitIt.hasNext()) {
         blockHead = (Unit)unitIt.next();
         if (!leaders.contains(blockHead)) {
            throw new RuntimeException("BlockGraph: first unit not a leader!");
         }

         ++blockLength;
      }

      Unit blockTail = blockHead;

      int indexInMethod;
      for(indexInMethod = 0; unitIt.hasNext(); ++blockLength) {
         Unit u = (Unit)unitIt.next();
         if (leaders.contains(u)) {
            this.addBlock(blockHead, blockTail, indexInMethod, blockLength, blockList, unitToBlock);
            ++indexInMethod;
            blockHead = u;
            blockLength = 0;
         }

         blockTail = u;
      }

      if (blockLength > 0) {
         this.addBlock(blockHead, blockTail, indexInMethod, blockLength, blockList, unitToBlock);
      }

      Iterator blockIt = unitGraph.getHeads().iterator();

      Unit tailUnit;
      Block tailBlock;
      while(blockIt.hasNext()) {
         tailUnit = (Unit)blockIt.next();
         tailBlock = (Block)unitToBlock.get(tailUnit);
         if (tailBlock.getHead() != tailUnit) {
            throw new RuntimeException("BlockGraph(): head Unit is not the first unit in the corresponding Block!");
         }

         this.mHeads.add(tailBlock);
      }

      blockIt = unitGraph.getTails().iterator();

      while(blockIt.hasNext()) {
         tailUnit = (Unit)blockIt.next();
         tailBlock = (Block)unitToBlock.get(tailUnit);
         if (tailBlock.getTail() != tailUnit) {
            throw new RuntimeException("BlockGraph(): tail Unit is not the last unit in the corresponding Block!");
         }

         this.mTails.add(tailBlock);
      }

      blockIt = blockList.iterator();

      while(true) {
         Block block;
         label98:
         do {
            while(blockIt.hasNext()) {
               block = (Block)blockIt.next();
               List<Unit> predUnits = unitGraph.getPredsOf(block.getHead());
               List<Block> predBlocks = new ArrayList(predUnits.size());
               Iterator predIt = predUnits.iterator();

               while(predIt.hasNext()) {
                  Unit predUnit = (Unit)predIt.next();
                  Block predBlock = (Block)unitToBlock.get(predUnit);
                  if (predBlock == null) {
                     throw new RuntimeException("BlockGraph(): block head mapped to null block!");
                  }

                  predBlocks.add(predBlock);
               }

               if (predBlocks.size() == 0) {
                  block.setPreds(Collections.emptyList());
               } else {
                  block.setPreds(Collections.unmodifiableList(predBlocks));
                  if (block.getHead() == this.mUnits.getFirst()) {
                     this.mHeads.add(block);
                  }
               }

               List<Unit> succUnits = unitGraph.getSuccsOf(block.getTail());
               List<Block> succBlocks = new ArrayList(succUnits.size());
               Iterator succIt = succUnits.iterator();

               while(succIt.hasNext()) {
                  Unit succUnit = (Unit)succIt.next();
                  Block succBlock = (Block)unitToBlock.get(succUnit);
                  if (succBlock == null) {
                     throw new RuntimeException("BlockGraph(): block tail mapped to null block!");
                  }

                  succBlocks.add(succBlock);
               }

               if (succBlocks.size() == 0) {
                  block.setSuccs(Collections.emptyList());
                  continue label98;
               }

               block.setSuccs(Collections.unmodifiableList(succBlocks));
            }

            this.mBlocks = Collections.unmodifiableList(blockList);
            this.mHeads = Collections.unmodifiableList(this.mHeads);
            if (this.mTails.size() == 0) {
               this.mTails = Collections.emptyList();
            } else {
               this.mTails = Collections.unmodifiableList(this.mTails);
            }

            return unitToBlock;
         } while(this.mTails.contains(block));

         if (!block.getPreds().isEmpty() || block.getHead() != block.getTail() || !(block.getHead() instanceof NopStmt)) {
            throw new RuntimeException("Block with no successors is not a tail!: " + block.toString());
         }

         blockIt.remove();
      }
   }

   private void addBlock(Unit head, Unit tail, int index, int length, List<Block> blockList, Map<Unit, Block> unitToBlock) {
      Block block = new Block(head, tail, this.mBody, index, length, this);
      blockList.add(block);
      unitToBlock.put(tail, block);
      unitToBlock.put(head, block);
   }

   public Body getBody() {
      return this.mBody;
   }

   public List<Block> getBlocks() {
      return this.mBlocks;
   }

   public String toString() {
      Iterator<Block> it = this.mBlocks.iterator();
      StringBuffer buf = new StringBuffer();

      while(it.hasNext()) {
         Block someBlock = (Block)it.next();
         buf.append(someBlock.toString() + '\n');
      }

      return buf.toString();
   }

   public List<Block> getHeads() {
      return this.mHeads;
   }

   public List<Block> getTails() {
      return this.mTails;
   }

   public List<Block> getPredsOf(Block b) {
      return b.getPreds();
   }

   public List<Block> getSuccsOf(Block b) {
      return b.getSuccs();
   }

   public int size() {
      return this.mBlocks.size();
   }

   public Iterator<Block> iterator() {
      return this.mBlocks.iterator();
   }
}
