package soot.toolkits.graph;

import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Unit;
import soot.util.Chain;

public class Block implements Iterable<Unit> {
   private static final Logger logger = LoggerFactory.getLogger(Block.class);
   private Unit mHead;
   private Unit mTail;
   private final Body mBody;
   private List<Block> mPreds;
   private List<Block> mSuccessors;
   private int mBlockLength = 0;
   private int mIndexInMethod = 0;

   public Block(Unit aHead, Unit aTail, Body aBody, int aIndexInMethod, int aBlockLength, BlockGraph aBlockGraph) {
      this.mHead = aHead;
      this.mTail = aTail;
      this.mBody = aBody;
      this.mIndexInMethod = aIndexInMethod;
      this.mBlockLength = aBlockLength;
   }

   public Body getBody() {
      return this.mBody;
   }

   public Iterator<Unit> iterator() {
      if (this.mBody != null) {
         Chain<Unit> units = this.mBody.getUnits();
         return units.iterator(this.mHead, this.mTail);
      } else {
         return null;
      }
   }

   public void insertBefore(Unit toInsert, Unit point) {
      if (point == this.mHead) {
         this.mHead = toInsert;
      }

      Chain<Unit> methodBody = this.mBody.getUnits();
      methodBody.insertBefore((Object)toInsert, point);
   }

   public void insertAfter(Unit toInsert, Unit point) {
      if (point == this.mTail) {
         this.mTail = toInsert;
      }

      Chain<Unit> methodBody = this.mBody.getUnits();
      methodBody.insertAfter((Object)toInsert, point);
   }

   public boolean remove(Unit item) {
      Chain<Unit> methodBody = this.mBody.getUnits();
      if (item == this.mHead) {
         this.mHead = (Unit)methodBody.getSuccOf(item);
      } else if (item == this.mTail) {
         this.mTail = (Unit)methodBody.getPredOf(item);
      }

      return methodBody.remove(item);
   }

   public Unit getSuccOf(Unit aItem) {
      Chain<Unit> methodBody = this.mBody.getUnits();
      return aItem != this.mTail ? (Unit)methodBody.getSuccOf(aItem) : null;
   }

   public Unit getPredOf(Unit aItem) {
      Chain<Unit> methodBody = this.mBody.getUnits();
      return aItem != this.mHead ? (Unit)methodBody.getPredOf(aItem) : null;
   }

   public void setIndexInMethod(int aIndexInMethod) {
      this.mIndexInMethod = aIndexInMethod;
   }

   public int getIndexInMethod() {
      return this.mIndexInMethod;
   }

   public Unit getHead() {
      return this.mHead;
   }

   public Unit getTail() {
      return this.mTail;
   }

   public void setPreds(List<Block> preds) {
      this.mPreds = preds;
   }

   public List<Block> getPreds() {
      return this.mPreds;
   }

   public void setSuccs(List<Block> succs) {
      this.mSuccessors = succs;
   }

   public List<Block> getSuccs() {
      return this.mSuccessors;
   }

   public String toShortString() {
      return "Block #" + this.mIndexInMethod;
   }

   public String toString() {
      StringBuffer strBuf = new StringBuffer();
      strBuf.append("Block " + this.mIndexInMethod + ":" + System.getProperty("line.separator"));
      strBuf.append("[preds: ");
      Iterator it;
      if (this.mPreds != null) {
         it = this.mPreds.iterator();

         while(it.hasNext()) {
            strBuf.append(((Block)it.next()).getIndexInMethod() + " ");
         }
      }

      strBuf.append("] [succs: ");
      if (this.mSuccessors != null) {
         it = this.mSuccessors.iterator();

         while(it.hasNext()) {
            strBuf.append(((Block)it.next()).getIndexInMethod() + " ");
         }
      }

      strBuf.append("]" + System.getProperty("line.separator"));
      Chain<Unit> methodUnits = this.mBody.getUnits();
      Iterator<Unit> basicBlockIt = methodUnits.iterator(this.mHead, this.mTail);
      if (basicBlockIt.hasNext()) {
         Unit someUnit = (Unit)basicBlockIt.next();
         strBuf.append(someUnit.toString() + ";" + System.getProperty("line.separator"));

         while(basicBlockIt.hasNext()) {
            someUnit = (Unit)basicBlockIt.next();
            if (someUnit == this.mTail) {
               break;
            }

            strBuf.append(someUnit.toString() + ";" + System.getProperty("line.separator"));
         }

         someUnit = this.mTail;
         if (this.mTail == null) {
            strBuf.append("error: null tail found; block length: " + this.mBlockLength + "" + System.getProperty("line.separator"));
         } else if (this.mHead != this.mTail) {
            strBuf.append(someUnit.toString() + ";" + System.getProperty("line.separator"));
         }
      }

      return strBuf.toString();
   }
}
