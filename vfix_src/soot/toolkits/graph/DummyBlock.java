package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Unit;

class DummyBlock extends Block {
   DummyBlock(Body body, int indexInMethod) {
      super((Unit)null, (Unit)null, body, indexInMethod, 0, (BlockGraph)null);
   }

   void makeHeadBlock(List<Block> oldHeads) {
      this.setPreds(new ArrayList());
      this.setSuccs(new ArrayList(oldHeads));

      Block oldHead;
      ArrayList newPreds;
      for(Iterator headsIt = oldHeads.iterator(); headsIt.hasNext(); oldHead.setPreds(newPreds)) {
         oldHead = (Block)headsIt.next();
         newPreds = new ArrayList();
         newPreds.add(this);
         List<Block> oldPreds = oldHead.getPreds();
         if (oldPreds != null) {
            newPreds.addAll(oldPreds);
         }
      }

   }

   void makeTailBlock(List<Block> oldTails) {
      this.setSuccs(new ArrayList());
      this.setPreds(new ArrayList(oldTails));

      Block oldTail;
      ArrayList newSuccs;
      for(Iterator tailsIt = oldTails.iterator(); tailsIt.hasNext(); oldTail.setSuccs(newSuccs)) {
         oldTail = (Block)tailsIt.next();
         newSuccs = new ArrayList();
         newSuccs.add(this);
         List<Block> oldSuccs = oldTail.getSuccs();
         if (oldSuccs != null) {
            newSuccs.addAll(oldSuccs);
         }
      }

   }

   public Iterator<Unit> iterator() {
      List<Unit> s = Collections.emptyList();
      return s.iterator();
   }
}
