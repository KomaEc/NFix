package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import soot.Immediate;
import soot.Local;
import soot.Type;
import soot.Value;
import soot.jimple.ConcreteRef;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.StringConstant;

public class ASTNode<T extends ASTNode> extends Symbol implements Cloneable, Iterable<T> {
   protected static final String PRIMITIVE_PACKAGE_NAME = "@primitive";
   public static final boolean generatedWithCircularEnabled = true;
   public static final boolean generatedWithCacheCycle = false;
   public static final boolean generatedWithComponentCheck = false;
   protected static ASTNode$State state = new ASTNode$State();
   public boolean in$Circle = false;
   public boolean is$Final = false;
   private int childIndex;
   protected int numChildren;
   protected ASTNode parent;
   protected ASTNode[] children;

   public void flushCache() {
   }

   public void flushCollectionCache() {
   }

   public ASTNode<T> clone() throws CloneNotSupportedException {
      ASTNode node = (ASTNode)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ASTNode<T> copy() {
      try {
         ASTNode node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ASTNode<T> fullCopy() {
      ASTNode tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            ASTNode child = this.children[i];
            if (child != null) {
               child = child.fullCopy();
               tree.setChild(child, i);
            }
         }
      }

      return tree;
   }

   public void accessControl() {
   }

   protected void collectExceptions(Collection c, ASTNode target) {
      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).collectExceptions(c, target);
      }

   }

   public void collectBranches(Collection c) {
      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).collectBranches(c);
      }

   }

   public Stmt branchTarget(Stmt branchStmt) {
      return this.getParent() != null ? this.getParent().branchTarget(branchStmt) : null;
   }

   public void collectFinally(Stmt branchStmt, ArrayList list) {
      if (this.getParent() != null) {
         this.getParent().collectFinally(branchStmt, list);
      }

   }

   public int varChildIndex(Block b) {
      ASTNode node;
      for(node = this; node.getParent().getParent() != b; node = node.getParent()) {
      }

      return b.getStmtListNoTransform().getIndexOfChild(node);
   }

   public int varChildIndex(TypeDecl t) {
      ASTNode node;
      for(node = this; node != null && node.getParent() != null && node.getParent().getParent() != t; node = node.getParent()) {
      }

      return node == null ? -1 : t.getBodyDeclListNoTransform().getIndexOfChild(node);
   }

   public void definiteAssignment() {
   }

   protected boolean checkDUeverywhere(Variable v) {
      for(int i = 0; i < this.getNumChild(); ++i) {
         if (!this.getChild(i).checkDUeverywhere(v)) {
            return false;
         }
      }

      return true;
   }

   protected boolean isDescendantTo(ASTNode node) {
      if (this == node) {
         return true;
      } else {
         return this.getParent() == null ? false : this.getParent().isDescendantTo(node);
      }
   }

   protected String sourceFile() {
      ASTNode node;
      for(node = this; node != null && !(node instanceof CompilationUnit); node = node.getParent()) {
      }

      if (node == null) {
         return "Unknown file";
      } else {
         CompilationUnit u = (CompilationUnit)node;
         return u.relativeName();
      }
   }

   public ASTNode setLocation(ASTNode node) {
      this.setStart(node.getStart());
      this.setEnd(node.getEnd());
      return this;
   }

   public ASTNode setStart(int i) {
      this.start = i;
      return this;
   }

   public int start() {
      return this.start;
   }

   public ASTNode setEnd(int i) {
      this.end = i;
      return this;
   }

   public int end() {
      return this.end;
   }

   public String location() {
      return "" + this.lineNumber();
   }

   public String errorPrefix() {
      return this.sourceFile() + ":" + this.location() + ":\n  *** Semantic Error: ";
   }

   public String warningPrefix() {
      return this.sourceFile() + ":" + this.location() + ":\n  *** WARNING: ";
   }

   public void error(String s) {
      ASTNode node;
      for(node = this; node != null && !(node instanceof CompilationUnit); node = node.getParent()) {
      }

      CompilationUnit cu = (CompilationUnit)node;
      if (this.getNumChild() == 0 && this.getStart() != 0 && this.getEnd() != 0) {
         int line = getLine(this.getStart());
         int column = getColumn(this.getStart());
         int endLine = getLine(this.getEnd());
         int endColumn = getColumn(this.getEnd());
         cu.errors.add(new Problem(this.sourceFile(), s, line, column, endLine, endColumn, Problem.Severity.ERROR, Problem.Kind.SEMANTIC));
      } else {
         cu.errors.add(new Problem(this.sourceFile(), s, this.lineNumber(), Problem.Severity.ERROR, Problem.Kind.SEMANTIC));
      }

   }

   public void warning(String s) {
      ASTNode node;
      for(node = this; node != null && !(node instanceof CompilationUnit); node = node.getParent()) {
      }

      CompilationUnit cu = (CompilationUnit)node;
      cu.warnings.add(new Problem(this.sourceFile(), "WARNING: " + s, this.lineNumber(), Problem.Severity.WARNING));
   }

   public void exceptionHandling() {
   }

   protected boolean reachedException(TypeDecl type) {
      for(int i = 0; i < this.getNumChild(); ++i) {
         if (this.getChild(i).reachedException(type)) {
            return true;
         }
      }

      return false;
   }

   public static Collection removeInstanceMethods(Collection c) {
      Collection c = new LinkedList(c);
      Iterator iter = c.iterator();

      while(iter.hasNext()) {
         MethodDecl m = (MethodDecl)iter.next();
         if (!m.isStatic()) {
            iter.remove();
         }
      }

      return c;
   }

   protected static void putSimpleSetElement(HashMap map, Object key, Object value) {
      SimpleSet set = (SimpleSet)map.get(key);
      if (set == null) {
         set = SimpleSet.emptySet;
      }

      map.put(key, set.add(value));
   }

   public SimpleSet removeInstanceVariables(SimpleSet oldSet) {
      SimpleSet newSet = SimpleSet.emptySet;
      Iterator iter = oldSet.iterator();

      while(iter.hasNext()) {
         Variable v = (Variable)iter.next();
         if (!v.isInstanceVariable()) {
            newSet = newSet.add(v);
         }
      }

      return newSet;
   }

   void checkModifiers() {
   }

   public void nameCheck() {
   }

   public TypeDecl extractSingleType(SimpleSet c) {
      return c.size() != 1 ? null : (TypeDecl)c.iterator().next();
   }

   public Options options() {
      return this.state().options;
   }

   public String toString() {
      StringBuffer s = new StringBuffer();
      this.toString(s);
      return s.toString().trim();
   }

   public void toString(StringBuffer s) {
      throw new Error("Operation toString(StringBuffer s) not implemented for " + this.getClass().getName());
   }

   public String dumpTree() {
      StringBuffer s = new StringBuffer();
      this.dumpTree(s, 0);
      return s.toString();
   }

   public void dumpTree(StringBuffer s, int j) {
      int i;
      for(i = 0; i < j; ++i) {
         s.append("  ");
      }

      s.append(this.dumpString() + "\n");

      for(i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).dumpTree(s, j + 1);
      }

   }

   public String dumpTreeNoRewrite() {
      StringBuffer s = new StringBuffer();
      this.dumpTreeNoRewrite(s, 0);
      return s.toString();
   }

   protected void dumpTreeNoRewrite(StringBuffer s, int indent) {
      int i;
      for(i = 0; i < indent; ++i) {
         s.append("  ");
      }

      s.append(this.dumpString());
      s.append("\n");

      for(i = 0; i < this.getNumChildNoTransform(); ++i) {
         this.getChildNoTransform(i).dumpTreeNoRewrite(s, indent + 1);
      }

   }

   public void typeCheck() {
   }

   void checkUnreachableStmt() {
   }

   public void clearLocations() {
      this.setStart(0);
      this.setEnd(0);

      for(int i = 0; i < this.getNumChildNoTransform(); ++i) {
         this.getChildNoTransform(i).clearLocations();
      }

   }

   protected void transformEnumConstructors() {
      for(int i = 0; i < this.getNumChildNoTransform(); ++i) {
         ASTNode child = this.getChildNoTransform(i);
         if (child != null) {
            child.transformEnumConstructors();
         }
      }

   }

   protected void checkEnum(EnumDecl enumDecl) {
      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).checkEnum(enumDecl);
      }

   }

   public void flushCaches() {
      this.flushCache();

      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).flushCaches();
      }

   }

   public void collectEnclosingVariables(HashSet set, TypeDecl typeDecl) {
      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).collectEnclosingVariables(set, typeDecl);
      }

   }

   public void transformation() {
      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).transformation();
      }

   }

   protected ASTNode replace(ASTNode node) {
      this.state().replacePos = node.getParent().getIndexOfChild(node);
      node.getParent().in$Circle(true);
      return node.getParent();
   }

   protected ASTNode with(ASTNode node) {
      this.setChild(node, this.state().replacePos);
      this.in$Circle(false);
      return node;
   }

   public void jimplify1phase1() {
      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).jimplify1phase1();
      }

   }

   public void jimplify1phase2() {
      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).jimplify1phase2();
      }

   }

   public void jimplify2() {
      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).jimplify2();
      }

   }

   public void jimplify2(Body b) {
      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).jimplify2(b);
      }

   }

   public Immediate asImmediate(Body b, Value v) {
      return (Immediate)(v instanceof Immediate ? (Immediate)v : b.newTemp(v));
   }

   public Local asLocal(Body b, Value v) {
      return v instanceof Local ? (Local)v : b.newTemp(v);
   }

   public Local asLocal(Body b, Value v, Type t) {
      if (v instanceof Local) {
         return (Local)v;
      } else {
         Local local = b.newTemp(t);
         b.add(b.newAssignStmt(local, v, (ASTNode)null));
         b.copyLocation(v, local);
         return local;
      }
   }

   public Value asRValue(Body b, Value v) {
      if (v instanceof Local) {
         return v;
      } else if (v instanceof soot.jimple.Constant) {
         return v;
      } else if (v instanceof ConcreteRef) {
         return v;
      } else if (v instanceof soot.jimple.Expr) {
         return v;
      } else {
         throw new Error("Need to convert " + v.getClass().getName() + " to RValue");
      }
   }

   protected soot.jimple.Stmt newLabel() {
      return Jimple.v().newNopStmt();
   }

   public void addAttributes() {
   }

   public static Value emitConstant(Constant constant) {
      if (constant instanceof Constant.ConstantInt) {
         return IntType.emitConstant(constant.intValue());
      } else if (constant instanceof Constant.ConstantLong) {
         return LongConstant.v(constant.longValue());
      } else if (constant instanceof Constant.ConstantFloat) {
         return FloatConstant.v(constant.floatValue());
      } else if (constant instanceof Constant.ConstantDouble) {
         return DoubleConstant.v(constant.doubleValue());
      } else if (constant instanceof Constant.ConstantChar) {
         return IntType.emitConstant(constant.intValue());
      } else if (constant instanceof Constant.ConstantBoolean) {
         return BooleanType.emitConstant(constant.booleanValue());
      } else if (constant instanceof Constant.ConstantString) {
         return StringConstant.v(constant.stringValue());
      } else {
         throw new Error("Unexpected constant");
      }
   }

   public void endExceptionRange(Body b, ArrayList list) {
      if (list != null) {
         soot.jimple.Stmt label = this.newLabel();
         b.addLabel(label);
         list.add(label);
      }

   }

   public void beginExceptionRange(Body b, ArrayList list) {
      if (list != null) {
         b.addNextStmt(list);
      }

   }

   public ASTNode cloneSubtree() {
      try {
         ASTNode tree = this.clone();
         tree.setParent((ASTNode)null);
         if (this.children != null) {
            tree.children = new ASTNode[this.children.length];

            for(int i = 0; i < this.children.length; ++i) {
               if (this.children[i] == null) {
                  tree.children[i] = null;
               } else {
                  tree.children[i] = this.children[i].cloneSubtree();
                  tree.children[i].setParent(tree);
               }
            }
         }

         return tree;
      } catch (CloneNotSupportedException var3) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public void checkUncheckedConversion(TypeDecl source, TypeDecl dest) {
      if (source.isUncheckedConversionTo(dest)) {
         this.warning("unchecked conversion from raw type " + source.typeName() + " to generic type " + dest.typeName());
      }

   }

   public void checkWarnings() {
   }

   public void collectTypesToHierarchy(Collection<Type> set) {
      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).collectTypesToHierarchy(set);
      }

   }

   public void collectTypesToSignatures(Collection<Type> set) {
      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).collectTypesToSignatures(set);
      }

   }

   public ASTNode() {
      this.init$Children();
   }

   public void init$Children() {
   }

   public final ASTNode$State state() {
      return state;
   }

   public boolean in$Circle() {
      return this.in$Circle;
   }

   public void in$Circle(boolean b) {
      this.in$Circle = b;
   }

   public boolean is$Final() {
      return this.is$Final;
   }

   public void is$Final(boolean b) {
      this.is$Final = b;
   }

   public T getChild(int i) {
      ASTNode node = this.getChildNoTransform(i);
      if (node == null) {
         return null;
      } else if (node.is$Final()) {
         return node;
      } else if (!node.mayHaveRewrite()) {
         node.is$Final(this.is$Final());
         return node;
      } else {
         if (!node.in$Circle()) {
            int num = this.state().boundariesCrossed;

            int rewriteState;
            do {
               this.state().push(1);
               ASTNode oldNode = node;
               node.in$Circle(true);
               node = node.rewriteTo();
               if (node != oldNode) {
                  this.setChild(node, i);
               }

               oldNode.in$Circle(false);
               rewriteState = this.state().pop();
            } while(rewriteState == 1);

            if (rewriteState == 2 && this.is$Final()) {
               node.is$Final(true);
               this.state().boundariesCrossed = num;
            }
         } else if (this.is$Final() != node.is$Final()) {
            ++this.state().boundariesCrossed;
         }

         return node;
      }
   }

   public int getIndexOfChild(ASTNode node) {
      if (node != null && node.childIndex < this.getNumChildNoTransform() && node == this.getChildNoTransform(node.childIndex)) {
         return node.childIndex;
      } else {
         for(int i = 0; i < this.getNumChildNoTransform(); ++i) {
            if (this.getChildNoTransform(i) == node) {
               node.childIndex = i;
               return i;
            }
         }

         return -1;
      }
   }

   public void addChild(T node) {
      this.setChild(node, this.getNumChildNoTransform());
   }

   public final T getChildNoTransform(int i) {
      return this.children != null ? this.children[i] : null;
   }

   protected int numChildren() {
      return this.numChildren;
   }

   public int getNumChild() {
      return this.numChildren();
   }

   public final int getNumChildNoTransform() {
      return this.numChildren();
   }

   public void setChild(ASTNode node, int i) {
      if (this.children == null) {
         this.children = new ASTNode[i + 1 > 4 ? i + 1 : 4];
      } else if (i >= this.children.length) {
         ASTNode[] c = new ASTNode[i << 1];
         System.arraycopy(this.children, 0, c, 0, this.children.length);
         this.children = c;
      }

      this.children[i] = node;
      if (i >= this.numChildren) {
         this.numChildren = i + 1;
      }

      if (node != null) {
         node.setParent(this);
         node.childIndex = i;
      }

   }

   public void insertChild(ASTNode node, int i) {
      if (this.children == null) {
         this.children = new ASTNode[i + 1 > 4 ? i + 1 : 4];
         this.children[i] = node;
      } else {
         ASTNode[] c = new ASTNode[this.children.length + 1];
         System.arraycopy(this.children, 0, c, 0, i);
         c[i] = node;
         if (i < this.children.length) {
            System.arraycopy(this.children, i, c, i + 1, this.children.length - i);

            for(int j = i + 1; j < c.length; ++j) {
               if (c[j] != null) {
                  c[j].childIndex = j;
               }
            }
         }

         this.children = c;
      }

      ++this.numChildren;
      if (node != null) {
         node.setParent(this);
         node.childIndex = i;
      }

   }

   public void removeChild(int i) {
      if (this.children != null) {
         ASTNode child = this.children[i];
         if (child != null) {
            child.parent = null;
            child.childIndex = -1;
         }

         if (!(this instanceof List) && !(this instanceof Opt)) {
            this.children[i] = null;
         } else {
            System.arraycopy(this.children, i + 1, this.children, i, this.children.length - i - 1);
            this.children[this.children.length - 1] = null;
            --this.numChildren;

            for(int j = i; j < this.numChildren; ++j) {
               if (this.children[j] != null) {
                  child = this.children[j];
                  child.childIndex = j;
               }
            }
         }
      }

   }

   public ASTNode getParent() {
      if (this.parent != null && this.parent.is$Final() != this.is$Final()) {
         ++this.state().boundariesCrossed;
      }

      return this.parent;
   }

   public void setParent(ASTNode node) {
      this.parent = node;
   }

   protected boolean duringImplicitConstructor() {
      if (this.state().duringImplicitConstructor == 0) {
         return false;
      } else {
         this.state().pop();
         this.state().push(3);
         return true;
      }
   }

   protected boolean duringBoundNames() {
      if (this.state().duringBoundNames == 0) {
         return false;
      } else {
         this.state().pop();
         this.state().push(3);
         return true;
      }
   }

   protected boolean duringNameResolution() {
      if (this.state().duringNameResolution == 0) {
         return false;
      } else {
         this.state().pop();
         this.state().push(3);
         return true;
      }
   }

   protected boolean duringSyntacticClassification() {
      if (this.state().duringSyntacticClassification == 0) {
         return false;
      } else {
         this.state().pop();
         this.state().push(3);
         return true;
      }
   }

   protected boolean duringAnonymousClasses() {
      if (this.state().duringAnonymousClasses == 0) {
         return false;
      } else {
         this.state().pop();
         this.state().push(3);
         return true;
      }
   }

   protected boolean duringVariableDeclarationTransformation() {
      if (this.state().duringVariableDeclarationTransformation == 0) {
         return false;
      } else {
         this.state().pop();
         this.state().push(3);
         return true;
      }
   }

   protected boolean duringLiterals() {
      if (this.state().duringLiterals == 0) {
         return false;
      } else {
         this.state().pop();
         this.state().push(3);
         return true;
      }
   }

   protected boolean duringDU() {
      if (this.state().duringDU == 0) {
         return false;
      } else {
         this.state().pop();
         this.state().push(3);
         return true;
      }
   }

   protected boolean duringAnnotations() {
      if (this.state().duringAnnotations == 0) {
         return false;
      } else {
         this.state().pop();
         this.state().push(3);
         return true;
      }
   }

   protected boolean duringEnums() {
      if (this.state().duringEnums == 0) {
         return false;
      } else {
         this.state().pop();
         this.state().push(3);
         return true;
      }
   }

   protected boolean duringGenericTypeVariables() {
      if (this.state().duringGenericTypeVariables == 0) {
         return false;
      } else {
         this.state().pop();
         this.state().push(3);
         return true;
      }
   }

   public Iterator<T> iterator() {
      return new Iterator<T>() {
         private int counter = 0;

         public boolean hasNext() {
            return this.counter < ASTNode.this.getNumChild();
         }

         public T next() {
            return this.hasNext() ? ASTNode.this.getChild(this.counter++) : null;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void collectErrors() {
      this.nameCheck();
      this.typeCheck();
      this.accessControl();
      this.exceptionHandling();
      this.checkUnreachableStmt();
      this.definiteAssignment();
      this.checkModifiers();
      this.checkWarnings();

      for(int i = 0; i < this.getNumChild(); ++i) {
         this.getChild(i).collectErrors();
      }

   }

   public boolean unassignedEverywhere(Variable v, TryStmt stmt) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumChild(); ++i) {
         if (!this.getChild(i).unassignedEverywhere(v, stmt)) {
            return false;
         }
      }

      return true;
   }

   public int lineNumber() {
      ASTNode$State state = this.state();

      ASTNode n;
      for(n = this; n.getParent() != null && n.getStart() == 0; n = n.getParent()) {
      }

      return getLine(n.getStart());
   }

   public String indent() {
      ASTNode$State state = this.state();
      String indent = this.extractIndent();
      return indent.startsWith("\n") ? indent : "\n" + indent;
   }

   public String extractIndent() {
      ASTNode$State state = this.state();
      if (this.getParent() == null) {
         return "";
      } else {
         String indent = this.getParent().extractIndent();
         if (this.getParent().addsIndentationLevel()) {
            indent = indent + "  ";
         }

         return indent;
      }
   }

   public boolean addsIndentationLevel() {
      ASTNode$State state = this.state();
      return false;
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName();
   }

   public boolean usesTypeVariable() {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumChild(); ++i) {
         if (this.getChild(i).usesTypeVariable()) {
            return true;
         }
      }

      return false;
   }

   public boolean isStringAdd() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean definesLabel() {
      ASTNode$State state = this.state();
      return false;
   }

   public CompilationUnit compilationUnit() {
      ASTNode$State state = this.state();
      CompilationUnit compilationUnit_value = this.getParent().Define_CompilationUnit_compilationUnit(this, (ASTNode)null);
      return compilationUnit_value;
   }

   public ASTNode rewriteTo() {
      if (this.state().peek() == 1) {
         this.state().pop();
         this.state().push(2);
      }

      return this;
   }

   public TypeDecl Define_TypeDecl_superType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_superType(this, caller);
   }

   public ConstructorDecl Define_ConstructorDecl_constructorDecl(ASTNode caller, ASTNode child) {
      return this.getParent().Define_ConstructorDecl_constructorDecl(this, caller);
   }

   public TypeDecl Define_TypeDecl_componentType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_componentType(this, caller);
   }

   public LabeledStmt Define_LabeledStmt_lookupLabel(ASTNode caller, ASTNode child, String name) {
      return this.getParent().Define_LabeledStmt_lookupLabel(this, caller, name);
   }

   public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isDest(this, caller);
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isSource(this, caller);
   }

   public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isIncOrDec(this, caller);
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      return this.getParent().Define_boolean_isDUbefore(this, caller, v);
   }

   public TypeDecl Define_TypeDecl_typeException(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeException(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeRuntimeException(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeRuntimeException(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeError(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeError(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeNullPointerException(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeNullPointerException(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeThrowable(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeThrowable(this, caller);
   }

   public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
      return this.getParent().Define_boolean_handlesException(this, caller, exceptionType);
   }

   public Collection Define_Collection_lookupConstructor(ASTNode caller, ASTNode child) {
      return this.getParent().Define_Collection_lookupConstructor(this, caller);
   }

   public Collection Define_Collection_lookupSuperConstructor(ASTNode caller, ASTNode child) {
      return this.getParent().Define_Collection_lookupSuperConstructor(this, caller);
   }

   public Expr Define_Expr_nestedScope(ASTNode caller, ASTNode child) {
      return this.getParent().Define_Expr_nestedScope(this, caller);
   }

   public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
      return this.getParent().Define_Collection_lookupMethod(this, caller, name);
   }

   public TypeDecl Define_TypeDecl_typeObject(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeObject(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeCloneable(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeCloneable(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeSerializable(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeSerializable(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeBoolean(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeBoolean(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeByte(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeByte(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeShort(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeShort(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeChar(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeChar(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeInt(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeInt(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeLong(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeLong(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeFloat(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeFloat(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeDouble(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeDouble(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeString(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeString(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeVoid(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeVoid(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeNull(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeNull(this, caller);
   }

   public TypeDecl Define_TypeDecl_unknownType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_unknownType(this, caller);
   }

   public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
      return this.getParent().Define_boolean_hasPackage(this, caller, packageName);
   }

   public TypeDecl Define_TypeDecl_lookupType(ASTNode caller, ASTNode child, String packageName, String typeName) {
      return this.getParent().Define_TypeDecl_lookupType(this, caller, packageName, typeName);
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      return this.getParent().Define_SimpleSet_lookupType(this, caller, name);
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      return this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
   }

   public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_mayBePublic(this, caller);
   }

   public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_mayBeProtected(this, caller);
   }

   public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_mayBePrivate(this, caller);
   }

   public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_mayBeStatic(this, caller);
   }

   public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_mayBeFinal(this, caller);
   }

   public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_mayBeAbstract(this, caller);
   }

   public boolean Define_boolean_mayBeVolatile(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_mayBeVolatile(this, caller);
   }

   public boolean Define_boolean_mayBeTransient(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_mayBeTransient(this, caller);
   }

   public boolean Define_boolean_mayBeStrictfp(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_mayBeStrictfp(this, caller);
   }

   public boolean Define_boolean_mayBeSynchronized(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_mayBeSynchronized(this, caller);
   }

   public boolean Define_boolean_mayBeNative(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_mayBeNative(this, caller);
   }

   public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
      return this.getParent().Define_ASTNode_enclosingBlock(this, caller);
   }

   public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
      return this.getParent().Define_VariableScope_outerScope(this, caller);
   }

   public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_insideLoop(this, caller);
   }

   public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_insideSwitch(this, caller);
   }

   public Case Define_Case_bind(ASTNode caller, ASTNode child, Case c) {
      return this.getParent().Define_Case_bind(this, caller, c);
   }

   public String Define_String_typeDeclIndent(ASTNode caller, ASTNode child) {
      return this.getParent().Define_String_typeDeclIndent(this, caller);
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_NameType_nameType(this, caller);
   }

   public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isAnonymous(this, caller);
   }

   public Variable Define_Variable_unknownField(ASTNode caller, ASTNode child) {
      return this.getParent().Define_Variable_unknownField(this, caller);
   }

   public MethodDecl Define_MethodDecl_unknownMethod(ASTNode caller, ASTNode child) {
      return this.getParent().Define_MethodDecl_unknownMethod(this, caller);
   }

   public ConstructorDecl Define_ConstructorDecl_unknownConstructor(ASTNode caller, ASTNode child) {
      return this.getParent().Define_ConstructorDecl_unknownConstructor(this, caller);
   }

   public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_declType(this, caller);
   }

   public BodyDecl Define_BodyDecl_enclosingBodyDecl(ASTNode caller, ASTNode child) {
      return this.getParent().Define_BodyDecl_enclosingBodyDecl(this, caller);
   }

   public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isMemberType(this, caller);
   }

   public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_hostType(this, caller);
   }

   public TypeDecl Define_TypeDecl_switchType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_switchType(this, caller);
   }

   public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_returnType(this, caller);
   }

   public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_enclosingInstance(this, caller);
   }

   public String Define_String_methodHost(ASTNode caller, ASTNode child) {
      return this.getParent().Define_String_methodHost(this, caller);
   }

   public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_inExplicitConstructorInvocation(this, caller);
   }

   public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_inStaticContext(this, caller);
   }

   public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_reportUnreachable(this, caller);
   }

   public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isMethodParameter(this, caller);
   }

   public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isConstructorParameter(this, caller);
   }

   public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isExceptionHandlerParameter(this, caller);
   }

   public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
      return this.getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
   }

   public ElementValue Define_ElementValue_lookupElementTypeValue(ASTNode caller, ASTNode child, String name) {
      return this.getParent().Define_ElementValue_lookupElementTypeValue(this, caller, name);
   }

   public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String s) {
      return this.getParent().Define_boolean_withinSuppressWarnings(this, caller, s);
   }

   public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_withinDeprecatedAnnotation(this, caller);
   }

   public Annotation Define_Annotation_lookupAnnotation(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
      return this.getParent().Define_Annotation_lookupAnnotation(this, caller, typeDecl);
   }

   public TypeDecl Define_TypeDecl_enclosingAnnotationDecl(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_enclosingAnnotationDecl(this, caller);
   }

   public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_assignConvertedType(this, caller);
   }

   public boolean Define_boolean_inExtendsOrImplements(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_inExtendsOrImplements(this, caller);
   }

   public TypeDecl Define_TypeDecl_typeWildcard(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_typeWildcard(this, caller);
   }

   public TypeDecl Define_TypeDecl_lookupWildcardExtends(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
      return this.getParent().Define_TypeDecl_lookupWildcardExtends(this, caller, typeDecl);
   }

   public TypeDecl Define_TypeDecl_lookupWildcardSuper(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
      return this.getParent().Define_TypeDecl_lookupWildcardSuper(this, caller, typeDecl);
   }

   public LUBType Define_LUBType_lookupLUBType(ASTNode caller, ASTNode child, Collection bounds) {
      return this.getParent().Define_LUBType_lookupLUBType(this, caller, bounds);
   }

   public GLBType Define_GLBType_lookupGLBType(ASTNode caller, ASTNode child, ArrayList bounds) {
      return this.getParent().Define_GLBType_lookupGLBType(this, caller, bounds);
   }

   public TypeDecl Define_TypeDecl_genericDecl(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_genericDecl(this, caller);
   }

   public boolean Define_boolean_variableArityValid(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_variableArityValid(this, caller);
   }

   public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_expectedType(this, caller);
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
      return this.getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
      return this.getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
   }

   public int Define_int_localNum(ASTNode caller, ASTNode child) {
      return this.getParent().Define_int_localNum(this, caller);
   }

   public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_enclosedByExceptionHandler(this, caller);
   }

   public ArrayList Define_ArrayList_exceptionRanges(ASTNode caller, ASTNode child) {
      return this.getParent().Define_ArrayList_exceptionRanges(this, caller);
   }

   public boolean Define_boolean_isCatchParam(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isCatchParam(this, caller);
   }

   public CatchClause Define_CatchClause_catchClause(ASTNode caller, ASTNode child) {
      return this.getParent().Define_CatchClause_catchClause(this, caller);
   }

   public boolean Define_boolean_resourcePreviouslyDeclared(ASTNode caller, ASTNode child, String name) {
      return this.getParent().Define_boolean_resourcePreviouslyDeclared(this, caller, name);
   }

   public ClassInstanceExpr Define_ClassInstanceExpr_getClassInstanceExpr(ASTNode caller, ASTNode child) {
      return this.getParent().Define_ClassInstanceExpr_getClassInstanceExpr(this, caller);
   }

   public boolean Define_boolean_isAnonymousDecl(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isAnonymousDecl(this, caller);
   }

   public boolean Define_boolean_isExplicitGenericConstructorAccess(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isExplicitGenericConstructorAccess(this, caller);
   }

   public CompilationUnit Define_CompilationUnit_compilationUnit(ASTNode caller, ASTNode child) {
      return this.getParent().Define_CompilationUnit_compilationUnit(this, caller);
   }

   public SimpleSet Define_SimpleSet_allImportedTypes(ASTNode caller, ASTNode child, String name) {
      return this.getParent().Define_SimpleSet_allImportedTypes(this, caller, name);
   }

   public String Define_String_packageName(ASTNode caller, ASTNode child) {
      return this.getParent().Define_String_packageName(this, caller);
   }

   public TypeDecl Define_TypeDecl_enclosingType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_TypeDecl_enclosingType(this, caller);
   }

   public boolean Define_boolean_isNestedType(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isNestedType(this, caller);
   }

   public boolean Define_boolean_isLocalClass(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_isLocalClass(this, caller);
   }

   public String Define_String_hostPackage(ASTNode caller, ASTNode child) {
      return this.getParent().Define_String_hostPackage(this, caller);
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      return this.getParent().Define_boolean_reachable(this, caller);
   }

   public boolean Define_boolean_inhModifiedInScope(ASTNode caller, ASTNode child, Variable var) {
      return this.getParent().Define_boolean_inhModifiedInScope(this, caller, var);
   }

   public boolean Define_boolean_reachableCatchClause(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
      return this.getParent().Define_boolean_reachableCatchClause(this, caller, exceptionType);
   }

   public Collection<TypeDecl> Define_Collection_TypeDecl__caughtExceptions(ASTNode caller, ASTNode child) {
      return this.getParent().Define_Collection_TypeDecl__caughtExceptions(this, caller);
   }
}
