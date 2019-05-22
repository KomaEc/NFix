package polyglot.visit;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import polyglot.ast.Block;
import polyglot.ast.Branch;
import polyglot.ast.Labeled;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Return;
import polyglot.ast.Stmt;
import polyglot.ast.SwitchBlock;
import polyglot.ast.Throw;

public class CodeCleaner extends NodeVisitor {
   protected NodeFactory nf;
   protected AlphaRenamer alphaRen;

   public CodeCleaner(NodeFactory nf) {
      this.nf = nf;
      this.alphaRen = new AlphaRenamer(nf);
   }

   public Node leave(Node old, Node n, NodeVisitor v) {
      if (!(n instanceof Block) && !(n instanceof Labeled)) {
         return n;
      } else if (n instanceof Labeled) {
         Labeled l = (Labeled)n;
         if (!(l.statement() instanceof Block)) {
            return n;
         } else {
            Block b = (Block)l.statement();
            if (b.statements().size() != 1) {
               return (Node)(this.labelRefs(b).contains(l.label()) ? n : this.nf.Block(b.position(), this.clean(this.flattenBlock(b))));
            } else {
               b = (Block)b.visit(this.alphaRen);
               return this.nf.Labeled(l.position(), l.label(), (Stmt)b.statements().get(0));
            }
         }
      } else {
         Block b = (Block)n;
         List stmtList = this.clean(this.flattenBlock(b));
         return (Node)(b instanceof SwitchBlock ? this.nf.SwitchBlock(b.position(), stmtList) : this.nf.Block(b.position(), stmtList));
      }
   }

   protected List flattenBlock(Block b) {
      List stmtList = new LinkedList();
      Iterator it = b.statements().iterator();

      while(it.hasNext()) {
         Stmt stmt = (Stmt)it.next();
         if (stmt instanceof Block) {
            stmt = (Stmt)stmt.visit(this.alphaRen);
            stmtList.addAll(((Block)stmt).statements());
         } else {
            stmtList.add(stmt);
         }
      }

      return stmtList;
   }

   protected List clean(List l) {
      List stmtList = new LinkedList();
      Iterator it = l.iterator();

      Stmt stmt;
      do {
         if (!it.hasNext()) {
            return l;
         }

         stmt = (Stmt)it.next();
         stmtList.add(stmt);
      } while(!(stmt instanceof Branch) && !(stmt instanceof Return) && !(stmt instanceof Throw));

      return stmtList;
   }

   protected Set labelRefs(Block b) {
      final Set result = new HashSet();
      b.visit(new NodeVisitor() {
         public Node leave(Node old, Node n, NodeVisitor v) {
            if (n instanceof Branch) {
               result.add(((Branch)n).label());
            }

            return n;
         }
      });
      return result;
   }
}
