package soot.dava.internal.SET;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.dava.DavaBody;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.finders.AbruptEdgeFinder;
import soot.dava.toolkits.base.finders.LabeledBlockFinder;
import soot.dava.toolkits.base.finders.SequenceFinder;
import soot.util.IterableSet;
import soot.util.UnmodifiableIterableSet;

public abstract class SETNode {
   private static final Logger logger = LoggerFactory.getLogger(SETNode.class);
   private IterableSet<AugmentedStmt> body;
   private final SETNodeLabel label;
   protected SETNode parent;
   protected AugmentedStmt entryStmt;
   protected IterableSet predecessors;
   protected IterableSet successors;
   protected LinkedList<IterableSet> subBodies;
   protected Map<IterableSet, IterableSet> body2childChain;

   public abstract IterableSet get_NaturalExits();

   public abstract ASTNode emit_AST();

   public abstract AugmentedStmt get_EntryStmt();

   protected abstract boolean resolve(SETNode var1);

   public SETNode(IterableSet<AugmentedStmt> body) {
      this.body = body;
      this.parent = null;
      this.label = new SETNodeLabel();
      this.subBodies = new LinkedList();
      this.body2childChain = new HashMap();
      this.predecessors = new IterableSet();
      this.successors = new IterableSet();
   }

   public void add_SubBody(IterableSet body) {
      this.subBodies.add(body);
      this.body2childChain.put(body, new IterableSet());
   }

   public Map<IterableSet, IterableSet> get_Body2ChildChain() {
      return this.body2childChain;
   }

   public List<IterableSet> get_SubBodies() {
      return this.subBodies;
   }

   public IterableSet<AugmentedStmt> get_Body() {
      return this.body;
   }

   public SETNodeLabel get_Label() {
      return this.label;
   }

   public SETNode get_Parent() {
      return this.parent;
   }

   public boolean contains(Object o) {
      return this.body.contains(o);
   }

   public IterableSet get_Successors() {
      return this.successors;
   }

   public IterableSet get_Predecessors() {
      return this.predecessors;
   }

   public boolean add_Child(SETNode child, IterableSet children) {
      if (this != child && !children.contains(child)) {
         children.add(child);
         child.parent = this;
         return true;
      } else {
         return false;
      }
   }

   public boolean remove_Child(SETNode child, IterableSet children) {
      if (this != child && children.contains(child)) {
         children.remove(child);
         child.parent = null;
         return true;
      } else {
         return false;
      }
   }

   public boolean insert_ChildBefore(SETNode child, SETNode point, IterableSet children) {
      if (this != child && this != point && children.contains(point)) {
         children.insertBefore(child, point);
         child.parent = this;
         return true;
      } else {
         return false;
      }
   }

   public List<Object> emit_ASTBody(IterableSet children) {
      LinkedList<Object> l = new LinkedList();
      Iterator cit = children.iterator();

      while(cit.hasNext()) {
         ASTNode astNode = ((SETNode)cit.next()).emit_AST();
         if (astNode != null) {
            l.addLast(astNode);
         }
      }

      return l;
   }

   public IterableSet<AugmentedStmt> get_IntersectionWith(SETNode other) {
      return this.body.intersection(other.get_Body());
   }

   public boolean has_IntersectionWith(SETNode other) {
      Iterator var2 = other.get_Body().iterator();

      AugmentedStmt as;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         as = (AugmentedStmt)var2.next();
      } while(!this.body.contains(as));

      return true;
   }

   public boolean is_SupersetOf(SETNode other) {
      return this.body.isSupersetOf(other.get_Body());
   }

   public boolean is_StrictSupersetOf(SETNode other) {
      return this.body.isStrictSubsetOf(other.get_Body());
   }

   public void find_SmallestSETNode(AugmentedStmt as) {
      Iterator sbit = this.subBodies.iterator();

      while(sbit.hasNext()) {
         Iterator it = ((IterableSet)this.body2childChain.get(sbit.next())).iterator();

         while(it.hasNext()) {
            SETNode child = (SETNode)it.next();
            if (child.contains(as)) {
               child.find_SmallestSETNode(as);
               return;
            }
         }
      }

      as.myNode = this;
   }

   public void find_LabeledBlocks(LabeledBlockFinder lbf) {
      Iterator sbit = this.subBodies.iterator();

      while(sbit.hasNext()) {
         Iterator cit = ((IterableSet)this.body2childChain.get(sbit.next())).iterator();

         while(cit.hasNext()) {
            ((SETNode)cit.next()).find_LabeledBlocks(lbf);
         }
      }

      lbf.perform_ChildOrder(this);
      lbf.find_LabeledBlocks(this);
   }

   public void find_StatementSequences(SequenceFinder sf, DavaBody davaBody) {
      Iterator sbit = this.subBodies.iterator();

      while(sbit.hasNext()) {
         IterableSet body = (IterableSet)sbit.next();
         IterableSet children = (IterableSet)this.body2childChain.get(body);
         HashSet<AugmentedStmt> childUnion = new HashSet();
         Iterator cit = children.iterator();

         while(cit.hasNext()) {
            SETNode child = (SETNode)cit.next();
            child.find_StatementSequences(sf, davaBody);
            childUnion.addAll(child.get_Body());
         }

         sf.find_StatementSequences(this, body, childUnion, davaBody);
      }

   }

   public void find_AbruptEdges(AbruptEdgeFinder aef) {
      Iterator sbit = this.subBodies.iterator();

      IterableSet children;
      while(sbit.hasNext()) {
         children = (IterableSet)sbit.next();
         IterableSet children = (IterableSet)this.body2childChain.get(children);
         Iterator cit = children.iterator();

         while(cit.hasNext()) {
            ((SETNode)cit.next()).find_AbruptEdges(aef);
         }

         aef.find_Continues(this, children, children);
      }

      sbit = this.subBodies.iterator();

      while(true) {
         Iterator cit;
         do {
            if (!sbit.hasNext()) {
               return;
            }

            children = (IterableSet)this.body2childChain.get(sbit.next());
            cit = children.iterator();
         } while(!cit.hasNext());

         SETNode cur = (SETNode)cit.next();
         SETNode prev = null;

         while(cit.hasNext()) {
            prev = cur;
            cur = (SETNode)cit.next();
            aef.find_Breaks(prev, cur);
         }
      }
   }

   protected void remove_AugmentedStmt(AugmentedStmt as) {
      IterableSet childChain = (IterableSet)this.body2childChain.remove(this.body);
      if (this.body instanceof UnmodifiableIterableSet) {
         ((UnmodifiableIterableSet)this.body).forceRemove(as);
      } else {
         this.body.remove(as);
      }

      if (childChain != null) {
         this.body2childChain.put(this.body, childChain);
      }

      Iterator var3 = this.subBodies.iterator();

      IterableSet subBody;
      do {
         if (!var3.hasNext()) {
            return;
         }

         subBody = (IterableSet)var3.next();
      } while(!subBody.contains(as));

      childChain = (IterableSet)this.body2childChain.remove(subBody);
      if (subBody instanceof UnmodifiableIterableSet) {
         ((UnmodifiableIterableSet)subBody).forceRemove(as);
      } else {
         subBody.remove(as);
      }

      if (childChain != null) {
         this.body2childChain.put(subBody, childChain);
      }

   }

   public boolean nest(SETNode other) {
      if (!other.resolve(this)) {
         return false;
      } else {
         IterableSet otherBody = other.get_Body();
         Iterator sbit = this.subBodies.iterator();

         label51:
         while(true) {
            IterableSet subBody;
            do {
               if (!sbit.hasNext()) {
                  return true;
               }

               subBody = (IterableSet)sbit.next();
            } while(!subBody.intersects(otherBody));

            IterableSet childChain = (IterableSet)this.body2childChain.get(subBody);
            Iterator ccit = childChain.snapshotIterator();

            while(true) {
               while(true) {
                  SETNode curChild;
                  IterableSet childBody;
                  do {
                     if (!ccit.hasNext()) {
                        this.add_Child(other, childChain);
                        continue label51;
                     }

                     curChild = (SETNode)ccit.next();
                     childBody = curChild.get_Body();
                  } while(!childBody.intersects(otherBody));

                  if (childBody.isSupersetOf(otherBody)) {
                     return curChild.nest(other);
                  }

                  this.remove_Child(curChild, childChain);
                  Iterator osbit = other.subBodies.iterator();

                  while(osbit.hasNext()) {
                     IterableSet otherSubBody = (IterableSet)osbit.next();
                     if (otherSubBody.isSupersetOf(childBody)) {
                        other.add_Child(curChild, (IterableSet)other.get_Body2ChildChain().get(otherSubBody));
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   public void dump() {
      this.dump(G.v().out);
   }

   public void dump(PrintStream out) {
      this.dump(out, "");
   }

   private void dump(PrintStream out, String indentation) {
      String TOP = ".---";
      String TAB = "|  ";
      String MID = "+---";
      String BOT = "`---";
      out.println(indentation);
      out.println(indentation + TOP);
      out.println(indentation + TAB + this.getClass());
      out.println(indentation + TAB);
      Iterator it = this.body.iterator();

      while(it.hasNext()) {
         out.println(indentation + TAB + ((AugmentedStmt)it.next()).toString());
      }

      Iterator sbit = this.subBodies.iterator();

      while(sbit.hasNext()) {
         IterableSet subBody = (IterableSet)sbit.next();
         out.println(indentation + MID);
         Iterator bit = subBody.iterator();

         while(bit.hasNext()) {
            out.println(indentation + TAB + ((AugmentedStmt)bit.next()).toString());
         }

         out.println(indentation + TAB);
         Iterator cit = ((IterableSet)this.body2childChain.get(subBody)).iterator();

         while(cit.hasNext()) {
            ((SETNode)cit.next()).dump(out, TAB + indentation);
         }
      }

      out.println(indentation + BOT);
   }

   public void verify() {
      Iterator sbit = this.subBodies.iterator();

      while(sbit.hasNext()) {
         IterableSet body = (IterableSet)sbit.next();
         Iterator bit = body.iterator();

         while(bit.hasNext()) {
            if (!(bit.next() instanceof AugmentedStmt)) {
               logger.debug("Error in body: " + this.getClass());
            }
         }

         Iterator cit = ((IterableSet)this.body2childChain.get(body)).iterator();

         while(cit.hasNext()) {
            ((SETNode)cit.next()).verify();
         }
      }

   }

   public boolean equals(Object other) {
      if (!(other instanceof SETNode)) {
         return false;
      } else {
         SETNode typed_other = (SETNode)other;
         if (!this.body.equals(typed_other.body)) {
            return false;
         } else if (!this.subBodies.equals(typed_other.subBodies)) {
            return false;
         } else {
            return this.body2childChain.equals(typed_other.body2childChain);
         }
      }
   }

   public int hashCode() {
      return 1;
   }
}
