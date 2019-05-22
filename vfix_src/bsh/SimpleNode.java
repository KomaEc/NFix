package bsh;

class SimpleNode implements Node {
   public static SimpleNode JAVACODE = new SimpleNode(-1) {
      public String getSourceFile() {
         return "<Called from Java Code>";
      }

      public int getLineNumber() {
         return -1;
      }

      public String getText() {
         return "<Compiled Java Code>";
      }
   };
   protected Node parent;
   protected Node[] children;
   protected int id;
   Token firstToken;
   Token lastToken;
   String sourceFile;

   public SimpleNode(int var1) {
      this.id = var1;
   }

   public void jjtOpen() {
   }

   public void jjtClose() {
   }

   public void jjtSetParent(Node var1) {
      this.parent = var1;
   }

   public Node jjtGetParent() {
      return this.parent;
   }

   public void jjtAddChild(Node var1, int var2) {
      if (this.children == null) {
         this.children = new Node[var2 + 1];
      } else if (var2 >= this.children.length) {
         Node[] var3 = new Node[var2 + 1];
         System.arraycopy(this.children, 0, var3, 0, this.children.length);
         this.children = var3;
      }

      this.children[var2] = var1;
   }

   public Node jjtGetChild(int var1) {
      return this.children[var1];
   }

   public SimpleNode getChild(int var1) {
      return (SimpleNode)this.jjtGetChild(var1);
   }

   public int jjtGetNumChildren() {
      return this.children == null ? 0 : this.children.length;
   }

   public String toString() {
      return ParserTreeConstants.jjtNodeName[this.id];
   }

   public String toString(String var1) {
      return var1 + this.toString();
   }

   public void dump(String var1) {
      System.out.println(this.toString(var1));
      if (this.children != null) {
         for(int var2 = 0; var2 < this.children.length; ++var2) {
            SimpleNode var3 = (SimpleNode)this.children[var2];
            if (var3 != null) {
               var3.dump(var1 + " ");
            }
         }
      }

   }

   public void prune() {
      this.jjtSetParent((Node)null);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      throw new InterpreterError("Unimplemented or inappropriate for " + this.getClass().getName());
   }

   public void setSourceFile(String var1) {
      this.sourceFile = var1;
   }

   public String getSourceFile() {
      if (this.sourceFile == null) {
         return this.parent != null ? ((SimpleNode)this.parent).getSourceFile() : "<unknown file>";
      } else {
         return this.sourceFile;
      }
   }

   public int getLineNumber() {
      return this.firstToken.beginLine;
   }

   public String getText() {
      StringBuffer var1 = new StringBuffer();

      for(Token var2 = this.firstToken; var2 != null; var2 = var2.next) {
         var1.append(var2.image);
         if (!var2.image.equals(".")) {
            var1.append(" ");
         }

         if (var2 == this.lastToken || var2.image.equals("{") || var2.image.equals(";")) {
            break;
         }
      }

      return var1.toString();
   }
}
