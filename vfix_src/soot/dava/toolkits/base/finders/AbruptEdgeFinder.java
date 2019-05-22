package soot.dava.toolkits.base.finders;

import java.util.Iterator;
import soot.G;
import soot.Singletons;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETCycleNode;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.util.IterableSet;

public class AbruptEdgeFinder implements FactFinder {
   public AbruptEdgeFinder(Singletons.Global g) {
   }

   public static AbruptEdgeFinder v() {
      return G.v().soot_dava_toolkits_base_finders_AbruptEdgeFinder();
   }

   public void find(DavaBody body, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
      Dava.v().log("AbruptEdgeFinder::find()");
      SET.find_AbruptEdges(this);
   }

   public void find_Continues(SETNode SETParent, IterableSet body, IterableSet children) {
      if (SETParent instanceof SETCycleNode) {
         SETCycleNode scn = (SETCycleNode)SETParent;
         IterableSet naturalPreds = ((SETNode)children.getLast()).get_NaturalExits();
         Iterator pit = scn.get_CharacterizingStmt().bpreds.iterator();

         while(pit.hasNext()) {
            AugmentedStmt pas = (AugmentedStmt)pit.next();
            if (body.contains(pas) && !naturalPreds.contains(pas)) {
               ((SETStatementSequenceNode)pas.myNode).insert_AbruptStmt(new DAbruptStmt("continue", scn.get_Label()));
            }
         }

      }
   }

   public void find_Breaks(SETNode prev, SETNode cur) {
      IterableSet naturalPreds = prev.get_NaturalExits();
      Iterator pit = cur.get_EntryStmt().bpreds.iterator();

      while(pit.hasNext()) {
         AugmentedStmt pas = (AugmentedStmt)pit.next();
         if (prev.get_Body().contains(pas) && !naturalPreds.contains(pas)) {
            Object temp = pas.myNode;
            ((SETStatementSequenceNode)temp).insert_AbruptStmt(new DAbruptStmt("break", prev.get_Label()));
         }
      }

   }
}
