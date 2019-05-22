package soot.dava.toolkits.base.finders;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import soot.dava.internal.asg.AugmentedStmt;
import soot.util.IterableSet;

public class SwitchNode implements Comparable {
   private final LinkedList preds;
   private final LinkedList succs;
   private AugmentedStmt as;
   private int score;
   private TreeSet<Object> indexSet;
   private IterableSet body;

   public SwitchNode(AugmentedStmt as, TreeSet<Object> indexSet, IterableSet body) {
      this.as = as;
      this.indexSet = indexSet;
      this.body = body;
      this.preds = new LinkedList();
      this.succs = new LinkedList();
      this.score = -1;
   }

   public int get_Score() {
      if (this.score == -1) {
         this.score = 0;
         if (this.preds.size() < 2) {
            Iterator sit = this.succs.iterator();

            while(sit.hasNext()) {
               SwitchNode ssn = (SwitchNode)sit.next();
               int curScore = ssn.get_Score();
               if (this.score < curScore) {
                  this.score = curScore;
               }
            }

            ++this.score;
         }
      }

      return this.score;
   }

   public List get_Preds() {
      return this.preds;
   }

   public List get_Succs() {
      return this.succs;
   }

   public AugmentedStmt get_AugStmt() {
      return this.as;
   }

   public TreeSet<Object> get_IndexSet() {
      return new TreeSet(this.indexSet);
   }

   public IterableSet get_Body() {
      return this.body;
   }

   public SwitchNode reset() {
      this.preds.clear();
      this.succs.clear();
      return this;
   }

   public void setup_Graph(HashMap<AugmentedStmt, SwitchNode> binding) {
      Iterator rit = ((AugmentedStmt)this.as.bsuccs.get(0)).get_Reachers().iterator();

      while(rit.hasNext()) {
         SwitchNode pred = (SwitchNode)binding.get(rit.next());
         if (pred != null) {
            if (!this.preds.contains(pred)) {
               this.preds.add(pred);
            }

            if (!pred.succs.contains(this)) {
               pred.succs.add(this);
            }
         }
      }

   }

   public int compareTo(Object o) {
      if (o == this) {
         return 0;
      } else if (this.indexSet.last() instanceof String) {
         return 1;
      } else if (o instanceof String) {
         return -1;
      } else if (o instanceof Integer) {
         return (Integer)this.indexSet.last() - (Integer)o;
      } else if (o instanceof TreeSet) {
         TreeSet other = (TreeSet)o;
         return other.last() instanceof String ? -1 : (Integer)this.indexSet.last() - (Integer)other.last();
      } else {
         SwitchNode other = (SwitchNode)o;
         return other.indexSet.last() instanceof String ? -1 : (Integer)this.indexSet.last() - (Integer)other.indexSet.last();
      }
   }
}
