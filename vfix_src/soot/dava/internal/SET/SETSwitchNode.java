package soot.dava.internal.SET;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import soot.Value;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.finders.SwitchNode;
import soot.util.IterableSet;

public class SETSwitchNode extends SETDagNode {
   private List<SwitchNode> switchNodeList;
   private Value key;

   public SETSwitchNode(AugmentedStmt characterizingStmt, Value key, IterableSet body, List<SwitchNode> switchNodeList, IterableSet junkBody) {
      super(characterizingStmt, body);
      this.key = key;
      this.switchNodeList = switchNodeList;
      Iterator it = switchNodeList.iterator();

      while(it.hasNext()) {
         this.add_SubBody(((SwitchNode)it.next()).get_Body());
      }

      this.add_SubBody(junkBody);
   }

   public IterableSet get_NaturalExits() {
      return new IterableSet();
   }

   public ASTNode emit_AST() {
      LinkedList<Object> indexList = new LinkedList();
      Map<Object, List<Object>> index2ASTBody = new HashMap();
      Iterator it = this.switchNodeList.iterator();

      while(it.hasNext()) {
         SwitchNode sn = (SwitchNode)it.next();
         Object lastIndex = sn.get_IndexSet().last();
         Iterator iit = sn.get_IndexSet().iterator();

         while(iit.hasNext()) {
            Object index = iit.next();
            indexList.addLast(index);
            if (index != lastIndex) {
               index2ASTBody.put(index, (Object)null);
            } else {
               index2ASTBody.put(index, this.emit_ASTBody((IterableSet)this.get_Body2ChildChain().get(sn.get_Body())));
            }
         }
      }

      return new ASTSwitchNode(this.get_Label(), this.key, indexList, index2ASTBody);
   }

   public AugmentedStmt get_EntryStmt() {
      return this.get_CharacterizingStmt();
   }
}
