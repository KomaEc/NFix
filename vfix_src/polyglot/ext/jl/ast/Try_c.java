package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Catch;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.Try;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;
import polyglot.util.TypedList;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;

public class Try_c extends Stmt_c implements Try {
   protected Block tryBlock;
   protected List catchBlocks;
   protected Block finallyBlock;
   // $FF: synthetic field
   static Class class$polyglot$ast$Catch;

   public Try_c(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock) {
      super(pos);
      this.tryBlock = tryBlock;
      this.catchBlocks = TypedList.copyAndCheck(catchBlocks, class$polyglot$ast$Catch == null ? (class$polyglot$ast$Catch = class$("polyglot.ast.Catch")) : class$polyglot$ast$Catch, true);
      this.finallyBlock = finallyBlock;
   }

   public Block tryBlock() {
      return this.tryBlock;
   }

   public Try tryBlock(Block tryBlock) {
      Try_c n = (Try_c)this.copy();
      n.tryBlock = tryBlock;
      return n;
   }

   public List catchBlocks() {
      return Collections.unmodifiableList(this.catchBlocks);
   }

   public Try catchBlocks(List catchBlocks) {
      Try_c n = (Try_c)this.copy();
      n.catchBlocks = TypedList.copyAndCheck(catchBlocks, class$polyglot$ast$Catch == null ? (class$polyglot$ast$Catch = class$("polyglot.ast.Catch")) : class$polyglot$ast$Catch, true);
      return n;
   }

   public Block finallyBlock() {
      return this.finallyBlock;
   }

   public Try finallyBlock(Block finallyBlock) {
      Try_c n = (Try_c)this.copy();
      n.finallyBlock = finallyBlock;
      return n;
   }

   protected Try_c reconstruct(Block tryBlock, List catchBlocks, Block finallyBlock) {
      if (tryBlock == this.tryBlock && CollectionUtil.equals(catchBlocks, this.catchBlocks) && finallyBlock == this.finallyBlock) {
         return this;
      } else {
         Try_c n = (Try_c)this.copy();
         n.tryBlock = tryBlock;
         n.catchBlocks = TypedList.copyAndCheck(catchBlocks, class$polyglot$ast$Catch == null ? (class$polyglot$ast$Catch = class$("polyglot.ast.Catch")) : class$polyglot$ast$Catch, true);
         n.finallyBlock = finallyBlock;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Block tryBlock = (Block)this.visitChild(this.tryBlock, v);
      List catchBlocks = this.visitList(this.catchBlocks, v);
      Block finallyBlock = (Block)this.visitChild(this.finallyBlock, v);
      return this.reconstruct(tryBlock, catchBlocks, finallyBlock);
   }

   public NodeVisitor exceptionCheckEnter(ExceptionChecker ec) throws SemanticException {
      ec = (ExceptionChecker)super.exceptionCheckEnter(ec);
      return ec.bypassChildren(this);
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      TypeSystem ts = ec.typeSystem();
      ec = ec.push();
      Block tryBlock = (Block)this.visitChild(this.tryBlock, ec);
      SubtypeSet thrown = ec.throwsSet();
      SubtypeSet caught = new SubtypeSet(ts.Throwable());
      ec = ec.pop();
      thrown.addAll(ts.uncheckedExceptions());
      Iterator i = this.catchBlocks.iterator();

      while(i.hasNext()) {
         Catch cb = (Catch)i.next();
         Type catchType = cb.catchType();
         boolean match = false;
         Iterator j = thrown.iterator();

         label49: {
            Type ex;
            do {
               if (!j.hasNext()) {
                  break label49;
               }

               ex = (Type)j.next();
            } while(!ts.isSubtype(ex, catchType) && !ts.isSubtype(catchType, ex));

            match = true;
         }

         if (!match) {
            throw new SemanticException("The exception \"" + catchType + "\" is not thrown in the try block.", cb.position());
         }

         if (caught.contains(catchType)) {
            throw new SemanticException("The exception \"" + catchType + "\" has been caught by an earlier catch block.", cb.position());
         }

         caught.add(catchType);
      }

      thrown.removeAll(caught);
      List catchBlocks = new ArrayList(this.catchBlocks.size());

      for(Iterator i = this.catchBlocks.iterator(); i.hasNext(); ec = ec.pop()) {
         Catch cb = (Catch)i.next();
         ec = ec.push();
         cb = (Catch)this.visitChild(cb, ec);
         catchBlocks.add(cb);
         thrown.addAll(ec.throwsSet());
      }

      Block finallyBlock = null;
      if (this.finallyBlock != null) {
         ec = ec.push();
         finallyBlock = (Block)this.visitChild(this.finallyBlock, ec);
         if (!this.finallyBlock.reachable()) {
            thrown.clear();
         }

         thrown.addAll(ec.throwsSet());
         ec = ec.pop();
      }

      ec.throwsSet().addAll(thrown);
      return this.reconstruct(tryBlock, catchBlocks, finallyBlock).exceptions(ec.throwsSet());
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("try ");
      sb.append(this.tryBlock.toString());
      int count = 0;
      Iterator it = this.catchBlocks.iterator();

      while(it.hasNext()) {
         Catch cb = (Catch)it.next();
         if (count++ > 2) {
            sb.append("...");
            break;
         }

         sb.append(" ");
         sb.append(cb.toString());
      }

      if (this.finallyBlock != null) {
         sb.append(" finally ");
         sb.append(this.finallyBlock.toString());
      }

      return sb.toString();
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("try");
      this.printSubStmt(this.tryBlock, w, tr);
      Iterator it = this.catchBlocks.iterator();

      while(it.hasNext()) {
         Catch cb = (Catch)it.next();
         w.newline(0);
         this.printBlock(cb, w, tr);
      }

      if (this.finallyBlock != null) {
         w.newline(0);
         w.write("finally");
         this.printSubStmt(this.finallyBlock, w, tr);
      }

   }

   public Term entry() {
      return this.tryBlock.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      TypeSystem ts = v.typeSystem();
      CFGBuilder v1 = v.push(this, false);
      CFGBuilder v2 = v.push(this, true);
      Iterator i = ts.uncheckedExceptions().iterator();

      while(i.hasNext()) {
         Type type = (Type)i.next();
         v1.visitThrow(this.tryBlock.entry(), type);
      }

      Object next;
      if (this.finallyBlock == null) {
         next = this;
      } else {
         next = this.finallyBlock.entry();
         v.visitCFG(this.finallyBlock, (Term)this);
      }

      v1.visitCFG(this.tryBlock, (Term)next);
      Iterator it = this.catchBlocks.iterator();

      while(it.hasNext()) {
         Catch cb = (Catch)it.next();
         v2.visitCFG(cb, (Term)next);
      }

      return succs;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
