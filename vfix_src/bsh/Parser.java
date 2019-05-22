package bsh;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class Parser implements ParserTreeConstants, ParserConstants {
   protected JJTParserState jjtree = new JJTParserState();
   boolean retainComments = false;
   public ParserTokenManager token_source;
   JavaCharStream jj_input_stream;
   public Token token;
   public Token jj_nt;
   private int jj_ntk;
   private Token jj_scanpos;
   private Token jj_lastpos;
   private int jj_la;
   public boolean lookingAhead = false;
   private boolean jj_semLA;
   private final Parser.LookaheadSuccess jj_ls = new Parser.LookaheadSuccess();

   public void setRetainComments(boolean var1) {
      this.retainComments = var1;
   }

   void jjtreeOpenNodeScope(Node var1) {
      ((SimpleNode)var1).firstToken = this.getToken(1);
   }

   void jjtreeCloseNodeScope(Node var1) {
      ((SimpleNode)var1).lastToken = this.getToken(0);
   }

   void reInitInput(Reader var1) {
      this.ReInit(var1);
   }

   public SimpleNode popNode() {
      return this.jjtree.nodeArity() > 0 ? (SimpleNode)this.jjtree.popNode() : null;
   }

   void reInitTokenInput(Reader var1) {
      this.jj_input_stream.ReInit(var1, this.jj_input_stream.getEndLine(), this.jj_input_stream.getEndColumn());
   }

   public static void main(String[] var0) throws IOException, ParseException {
      boolean var1 = false;
      int var2 = 0;
      if (var0[0].equals("-p")) {
         ++var2;
         var1 = true;
      }

      while(var2 < var0.length) {
         FileReader var3 = new FileReader(var0[var2]);
         Parser var4 = new Parser(var3);
         var4.setRetainComments(true);

         while(!var4.Line()) {
            if (var1) {
               System.out.println(var4.popNode());
            }
         }

         ++var2;
      }

   }

   boolean isRegularForStatement() {
      byte var1 = 1;
      int var3 = var1 + 1;
      Token var2 = this.getToken(var1);
      if (var2.kind != 30) {
         return false;
      } else {
         var2 = this.getToken(var3++);
         if (var2.kind != 72) {
            return false;
         } else {
            while(true) {
               var2 = this.getToken(var3++);
               switch(var2.kind) {
               case 0:
                  return false;
               case 78:
                  return true;
               case 89:
                  return false;
               }
            }
         }
      }
   }

   ParseException createParseException(String var1) {
      Token var2 = this.token;
      int var3 = var2.beginLine;
      int var4 = var2.beginColumn;
      String var10000;
      if (var2.kind == 0) {
         var10000 = ParserConstants.tokenImage[0];
      } else {
         var10000 = var2.image;
      }

      return new ParseException("Parse error at line " + var3 + ", column " + var4 + " : " + var1);
   }

   public final boolean Line() throws ParseException {
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 0:
         this.jj_consume_token(0);
         Interpreter.debug("End of File!");
         return true;
      default:
         if (this.jj_2_1(1)) {
            this.BlockStatement();
            return false;
         } else {
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      }
   }

   public final Modifiers Modifiers(int var1, boolean var2) throws ParseException {
      Modifiers var3 = null;

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 10:
         case 27:
         case 39:
         case 43:
         case 44:
         case 45:
         case 48:
         case 49:
         case 51:
         case 52:
         case 58:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 10:
               this.jj_consume_token(10);
               break;
            case 27:
               this.jj_consume_token(27);
               break;
            case 39:
               this.jj_consume_token(39);
               break;
            case 43:
               this.jj_consume_token(43);
               break;
            case 44:
               this.jj_consume_token(44);
               break;
            case 45:
               this.jj_consume_token(45);
               break;
            case 48:
               this.jj_consume_token(48);
               break;
            case 49:
               this.jj_consume_token(49);
               break;
            case 51:
               this.jj_consume_token(51);
               break;
            case 52:
               this.jj_consume_token(52);
               break;
            case 58:
               this.jj_consume_token(58);
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            if (!var2) {
               try {
                  if (var3 == null) {
                     var3 = new Modifiers();
                  }

                  var3.addModifier(var1, this.getToken(0).image);
               } catch (IllegalStateException var5) {
                  throw this.createParseException(var5.getMessage());
               }
            }
            break;
         default:
            return var3;
         }
      }
   }

   public final void ClassDeclaration() throws ParseException {
      BSHClassDeclaration var1 = new BSHClassDeclaration(1);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         Modifiers var3 = this.Modifiers(0, false);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 13:
            this.jj_consume_token(13);
            break;
         case 37:
            this.jj_consume_token(37);
            var1.isInterface = true;
            break;
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }

         Token var4 = this.jj_consume_token(69);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 25:
            this.jj_consume_token(25);
            this.AmbiguousName();
            var1.extend = true;
         }

         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 33:
            this.jj_consume_token(33);
            int var5 = this.NameList();
            var1.numInterfaces = var5;
         default:
            this.Block();
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.modifiers = var3;
            var1.name = var4.image;
         }
      } catch (Throwable var11) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var11 instanceof RuntimeException) {
            throw (RuntimeException)var11;
         }

         if (var11 instanceof ParseException) {
            throw (ParseException)var11;
         }

         throw (Error)var11;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void MethodDeclaration() throws ParseException {
      BSHMethodDeclaration var1 = new BSHMethodDeclaration(2);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);
      Token var3 = null;

      try {
         Modifiers var4 = this.Modifiers(1, false);
         var1.modifiers = var4;
         if (this.jj_2_2(Integer.MAX_VALUE)) {
            var3 = this.jj_consume_token(69);
            var1.name = var3.image;
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 11:
            case 14:
            case 17:
            case 22:
            case 29:
            case 36:
            case 38:
            case 47:
            case 57:
            case 69:
               this.ReturnType();
               var3 = this.jj_consume_token(69);
               var1.name = var3.image;
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }

         this.FormalParameters();
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 54:
            this.jj_consume_token(54);
            int var5 = this.NameList();
            var1.numThrows = var5;
         }

         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 74:
            this.Block();
            break;
         case 78:
            this.jj_consume_token(78);
            break;
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      } catch (Throwable var11) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var11 instanceof RuntimeException) {
            throw (RuntimeException)var11;
         }

         if (var11 instanceof ParseException) {
            throw (ParseException)var11;
         }

         throw (Error)var11;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void PackageDeclaration() throws ParseException {
      BSHPackageDeclaration var1 = new BSHPackageDeclaration(3);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(42);
         this.AmbiguousName();
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void ImportDeclaration() throws ParseException {
      BSHImportDeclaration var1 = new BSHImportDeclaration(4);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);
      Token var3 = null;
      Token var4 = null;

      try {
         if (this.jj_2_3(3)) {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 48:
               var3 = this.jj_consume_token(48);
            default:
               this.jj_consume_token(34);
               this.AmbiguousName();
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 80:
                  var4 = this.jj_consume_token(80);
                  this.jj_consume_token(104);
               default:
                  this.jj_consume_token(78);
                  this.jjtree.closeNodeScope(var1, true);
                  var2 = false;
                  this.jjtreeCloseNodeScope(var1);
                  if (var3 != null) {
                     var1.staticImport = true;
                  }

                  if (var4 != null) {
                     var1.importPackage = true;
                  }
               }
            }
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 34:
               this.jj_consume_token(34);
               this.jj_consume_token(104);
               this.jj_consume_token(78);
               this.jjtree.closeNodeScope(var1, true);
               var2 = false;
               this.jjtreeCloseNodeScope(var1);
               var1.superImport = true;
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }
      } catch (Throwable var10) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var10 instanceof RuntimeException) {
            throw (RuntimeException)var10;
         }

         if (var10 instanceof ParseException) {
            throw (ParseException)var10;
         }

         throw (Error)var10;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void VariableDeclarator() throws ParseException {
      BSHVariableDeclarator var1 = new BSHVariableDeclarator(5);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         Token var3 = this.jj_consume_token(69);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 81:
            this.jj_consume_token(81);
            this.VariableInitializer();
         default:
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.name = var3.image;
         }
      } catch (Throwable var9) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var9 instanceof RuntimeException) {
            throw (RuntimeException)var9;
         }

         if (var9 instanceof ParseException) {
            throw (ParseException)var9;
         }

         throw (Error)var9;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void VariableInitializer() throws ParseException {
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 11:
      case 14:
      case 17:
      case 22:
      case 26:
      case 29:
      case 36:
      case 38:
      case 40:
      case 41:
      case 47:
      case 55:
      case 57:
      case 60:
      case 64:
      case 66:
      case 67:
      case 69:
      case 72:
      case 86:
      case 87:
      case 100:
      case 101:
      case 102:
      case 103:
         this.Expression();
         break;
      case 12:
      case 13:
      case 15:
      case 16:
      case 18:
      case 19:
      case 20:
      case 21:
      case 23:
      case 24:
      case 25:
      case 27:
      case 28:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 37:
      case 39:
      case 42:
      case 43:
      case 44:
      case 45:
      case 46:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 56:
      case 58:
      case 59:
      case 61:
      case 62:
      case 63:
      case 65:
      case 68:
      case 70:
      case 71:
      case 73:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
      case 80:
      case 81:
      case 82:
      case 83:
      case 84:
      case 85:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      default:
         this.jj_consume_token(-1);
         throw new ParseException();
      case 74:
         this.ArrayInitializer();
      }

   }

   public final void ArrayInitializer() throws ParseException {
      BSHArrayInitializer var1 = new BSHArrayInitializer(6);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(74);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 14:
         case 17:
         case 22:
         case 26:
         case 29:
         case 36:
         case 38:
         case 40:
         case 41:
         case 47:
         case 55:
         case 57:
         case 60:
         case 64:
         case 66:
         case 67:
         case 69:
         case 72:
         case 74:
         case 86:
         case 87:
         case 100:
         case 101:
         case 102:
         case 103:
            this.VariableInitializer();

            while(this.jj_2_4(2)) {
               this.jj_consume_token(79);
               this.VariableInitializer();
            }
         case 12:
         case 13:
         case 15:
         case 16:
         case 18:
         case 19:
         case 20:
         case 21:
         case 23:
         case 24:
         case 25:
         case 27:
         case 28:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 37:
         case 39:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 56:
         case 58:
         case 59:
         case 61:
         case 62:
         case 63:
         case 65:
         case 68:
         case 70:
         case 71:
         case 73:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         default:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 79:
               this.jj_consume_token(79);
            default:
               this.jj_consume_token(75);
            }
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void FormalParameters() throws ParseException {
      BSHFormalParameters var1 = new BSHFormalParameters(7);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(72);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 14:
         case 17:
         case 22:
         case 29:
         case 36:
         case 38:
         case 47:
         case 69:
            this.FormalParameter();

            label120:
            while(true) {
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 79:
                  this.jj_consume_token(79);
                  this.FormalParameter();
                  break;
               default:
                  break label120;
               }
            }
         default:
            this.jj_consume_token(73);
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void FormalParameter() throws ParseException {
      BSHFormalParameter var1 = new BSHFormalParameter(8);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         Token var3;
         if (this.jj_2_5(2)) {
            this.Type();
            var3 = this.jj_consume_token(69);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.name = var3.image;
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 69:
               var3 = this.jj_consume_token(69);
               this.jjtree.closeNodeScope(var1, true);
               var2 = false;
               this.jjtreeCloseNodeScope(var1);
               var1.name = var3.image;
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }
      } catch (Throwable var9) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var9 instanceof RuntimeException) {
            throw (RuntimeException)var9;
         }

         if (var9 instanceof ParseException) {
            throw (ParseException)var9;
         }

         throw (Error)var9;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void Type() throws ParseException {
      BSHType var1 = new BSHType(9);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 14:
         case 17:
         case 22:
         case 29:
         case 36:
         case 38:
         case 47:
            this.PrimitiveType();
            break;
         case 69:
            this.AmbiguousName();
            break;
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }

         while(this.jj_2_6(2)) {
            this.jj_consume_token(76);
            this.jj_consume_token(77);
            var1.addArrayDimension();
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void ReturnType() throws ParseException {
      BSHReturnType var1 = new BSHReturnType(10);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 14:
         case 17:
         case 22:
         case 29:
         case 36:
         case 38:
         case 47:
         case 69:
            this.Type();
            break;
         case 57:
            this.jj_consume_token(57);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.isVoid = true;
            break;
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void PrimitiveType() throws ParseException {
      BSHPrimitiveType var1 = new BSHPrimitiveType(11);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
            this.jj_consume_token(11);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.type = Boolean.TYPE;
            break;
         case 14:
            this.jj_consume_token(14);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.type = Byte.TYPE;
            break;
         case 17:
            this.jj_consume_token(17);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.type = Character.TYPE;
            break;
         case 22:
            this.jj_consume_token(22);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.type = Double.TYPE;
            break;
         case 29:
            this.jj_consume_token(29);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.type = Float.TYPE;
            break;
         case 36:
            this.jj_consume_token(36);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.type = Integer.TYPE;
            break;
         case 38:
            this.jj_consume_token(38);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.type = Long.TYPE;
            break;
         case 47:
            this.jj_consume_token(47);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.type = Short.TYPE;
            break;
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void AmbiguousName() throws ParseException {
      BSHAmbiguousName var1 = new BSHAmbiguousName(12);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         Token var3 = this.jj_consume_token(69);
         StringBuffer var4 = new StringBuffer(var3.image);

         while(this.jj_2_7(2)) {
            this.jj_consume_token(80);
            var3 = this.jj_consume_token(69);
            var4.append("." + var3.image);
         }

         this.jjtree.closeNodeScope(var1, true);
         var2 = false;
         this.jjtreeCloseNodeScope(var1);
         var1.text = var4.toString();
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }
   }

   public final int NameList() throws ParseException {
      byte var1 = 0;
      this.AmbiguousName();
      int var2 = var1 + 1;

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 79:
            this.jj_consume_token(79);
            this.AmbiguousName();
            ++var2;
            break;
         default:
            return var2;
         }
      }
   }

   public final void Expression() throws ParseException {
      if (this.jj_2_8(Integer.MAX_VALUE)) {
         this.Assignment();
      } else {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 14:
         case 17:
         case 22:
         case 26:
         case 29:
         case 36:
         case 38:
         case 40:
         case 41:
         case 47:
         case 55:
         case 57:
         case 60:
         case 64:
         case 66:
         case 67:
         case 69:
         case 72:
         case 86:
         case 87:
         case 100:
         case 101:
         case 102:
         case 103:
            this.ConditionalExpression();
            break;
         case 12:
         case 13:
         case 15:
         case 16:
         case 18:
         case 19:
         case 20:
         case 21:
         case 23:
         case 24:
         case 25:
         case 27:
         case 28:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 37:
         case 39:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 56:
         case 58:
         case 59:
         case 61:
         case 62:
         case 63:
         case 65:
         case 68:
         case 70:
         case 71:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      }

   }

   public final void Assignment() throws ParseException {
      BSHAssignment var1 = new BSHAssignment(13);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.PrimaryExpression();
         int var3 = this.AssignmentOperator();
         var1.operator = var3;
         this.Expression();
      } catch (Throwable var9) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var9 instanceof RuntimeException) {
            throw (RuntimeException)var9;
         }

         if (var9 instanceof ParseException) {
            throw (ParseException)var9;
         }

         throw (Error)var9;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final int AssignmentOperator() throws ParseException {
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 81:
         this.jj_consume_token(81);
         break;
      case 82:
      case 83:
      case 84:
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 109:
      case 110:
      case 111:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 123:
      case 125:
      default:
         this.jj_consume_token(-1);
         throw new ParseException();
      case 118:
         this.jj_consume_token(118);
         break;
      case 119:
         this.jj_consume_token(119);
         break;
      case 120:
         this.jj_consume_token(120);
         break;
      case 121:
         this.jj_consume_token(121);
         break;
      case 122:
         this.jj_consume_token(122);
         break;
      case 124:
         this.jj_consume_token(124);
         break;
      case 126:
         this.jj_consume_token(126);
         break;
      case 127:
         this.jj_consume_token(127);
         break;
      case 128:
         this.jj_consume_token(128);
         break;
      case 129:
         this.jj_consume_token(129);
         break;
      case 130:
         this.jj_consume_token(130);
         break;
      case 131:
         this.jj_consume_token(131);
         break;
      case 132:
         this.jj_consume_token(132);
         break;
      case 133:
         this.jj_consume_token(133);
      }

      Token var1 = this.getToken(0);
      return var1.kind;
   }

   public final void ConditionalExpression() throws ParseException {
      this.ConditionalOrExpression();
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 88:
         this.jj_consume_token(88);
         this.Expression();
         this.jj_consume_token(89);
         BSHTernaryExpression var1 = new BSHTernaryExpression(14);
         boolean var2 = true;
         this.jjtree.openNodeScope(var1);
         this.jjtreeOpenNodeScope(var1);

         try {
            this.ConditionalExpression();
         } catch (Throwable var8) {
            if (var2) {
               this.jjtree.clearNodeScope(var1);
               var2 = false;
            } else {
               this.jjtree.popNode();
            }

            if (var8 instanceof RuntimeException) {
               throw (RuntimeException)var8;
            } else {
               if (var8 instanceof ParseException) {
                  throw (ParseException)var8;
               }

               throw (Error)var8;
            }
         } finally {
            if (var2) {
               this.jjtree.closeNodeScope(var1, 3);
               this.jjtreeCloseNodeScope(var1);
            }

         }
      default:
      }
   }

   public final void ConditionalOrExpression() throws ParseException {
      Token var1 = null;
      this.ConditionalAndExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 96:
         case 97:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 96:
               var1 = this.jj_consume_token(96);
               break;
            case 97:
               var1 = this.jj_consume_token(97);
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            this.ConditionalAndExpression();
            BSHBinaryExpression var2 = new BSHBinaryExpression(15);
            boolean var3 = true;
            this.jjtree.openNodeScope(var2);
            this.jjtreeOpenNodeScope(var2);

            try {
               this.jjtree.closeNodeScope(var2, 2);
               var3 = false;
               this.jjtreeCloseNodeScope(var2);
               var2.kind = var1.kind;
               break;
            } finally {
               if (var3) {
                  this.jjtree.closeNodeScope(var2, 2);
                  this.jjtreeCloseNodeScope(var2);
               }

            }
         default:
            return;
         }
      }
   }

   public final void ConditionalAndExpression() throws ParseException {
      Token var1 = null;
      this.InclusiveOrExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 98:
         case 99:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 98:
               var1 = this.jj_consume_token(98);
               break;
            case 99:
               var1 = this.jj_consume_token(99);
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            this.InclusiveOrExpression();
            BSHBinaryExpression var2 = new BSHBinaryExpression(15);
            boolean var3 = true;
            this.jjtree.openNodeScope(var2);
            this.jjtreeOpenNodeScope(var2);

            try {
               this.jjtree.closeNodeScope(var2, 2);
               var3 = false;
               this.jjtreeCloseNodeScope(var2);
               var2.kind = var1.kind;
               break;
            } finally {
               if (var3) {
                  this.jjtree.closeNodeScope(var2, 2);
                  this.jjtreeCloseNodeScope(var2);
               }

            }
         default:
            return;
         }
      }
   }

   public final void InclusiveOrExpression() throws ParseException {
      Token var1 = null;
      this.ExclusiveOrExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 108:
         case 109:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 108:
               var1 = this.jj_consume_token(108);
               break;
            case 109:
               var1 = this.jj_consume_token(109);
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            this.ExclusiveOrExpression();
            BSHBinaryExpression var2 = new BSHBinaryExpression(15);
            boolean var3 = true;
            this.jjtree.openNodeScope(var2);
            this.jjtreeOpenNodeScope(var2);

            try {
               this.jjtree.closeNodeScope(var2, 2);
               var3 = false;
               this.jjtreeCloseNodeScope(var2);
               var2.kind = var1.kind;
               break;
            } finally {
               if (var3) {
                  this.jjtree.closeNodeScope(var2, 2);
                  this.jjtreeCloseNodeScope(var2);
               }

            }
         default:
            return;
         }
      }
   }

   public final void ExclusiveOrExpression() throws ParseException {
      Token var1 = null;
      this.AndExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 110:
            var1 = this.jj_consume_token(110);
            this.AndExpression();
            BSHBinaryExpression var2 = new BSHBinaryExpression(15);
            boolean var3 = true;
            this.jjtree.openNodeScope(var2);
            this.jjtreeOpenNodeScope(var2);

            try {
               this.jjtree.closeNodeScope(var2, 2);
               var3 = false;
               this.jjtreeCloseNodeScope(var2);
               var2.kind = var1.kind;
               break;
            } finally {
               if (var3) {
                  this.jjtree.closeNodeScope(var2, 2);
                  this.jjtreeCloseNodeScope(var2);
               }

            }
         default:
            return;
         }
      }
   }

   public final void AndExpression() throws ParseException {
      Token var1 = null;
      this.EqualityExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 106:
         case 107:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 106:
               var1 = this.jj_consume_token(106);
               break;
            case 107:
               var1 = this.jj_consume_token(107);
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            this.EqualityExpression();
            BSHBinaryExpression var2 = new BSHBinaryExpression(15);
            boolean var3 = true;
            this.jjtree.openNodeScope(var2);
            this.jjtreeOpenNodeScope(var2);

            try {
               this.jjtree.closeNodeScope(var2, 2);
               var3 = false;
               this.jjtreeCloseNodeScope(var2);
               var2.kind = var1.kind;
               break;
            } finally {
               if (var3) {
                  this.jjtree.closeNodeScope(var2, 2);
                  this.jjtreeCloseNodeScope(var2);
               }

            }
         default:
            return;
         }
      }
   }

   public final void EqualityExpression() throws ParseException {
      Token var1 = null;
      this.InstanceOfExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 90:
         case 95:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 90:
               var1 = this.jj_consume_token(90);
               break;
            case 95:
               var1 = this.jj_consume_token(95);
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            this.InstanceOfExpression();
            BSHBinaryExpression var2 = new BSHBinaryExpression(15);
            boolean var3 = true;
            this.jjtree.openNodeScope(var2);
            this.jjtreeOpenNodeScope(var2);

            try {
               this.jjtree.closeNodeScope(var2, 2);
               var3 = false;
               this.jjtreeCloseNodeScope(var2);
               var2.kind = var1.kind;
               break;
            } finally {
               if (var3) {
                  this.jjtree.closeNodeScope(var2, 2);
                  this.jjtreeCloseNodeScope(var2);
               }

            }
         default:
            return;
         }
      }
   }

   public final void InstanceOfExpression() throws ParseException {
      Token var1 = null;
      this.RelationalExpression();
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 35:
         var1 = this.jj_consume_token(35);
         this.Type();
         BSHBinaryExpression var2 = new BSHBinaryExpression(15);
         boolean var3 = true;
         this.jjtree.openNodeScope(var2);
         this.jjtreeOpenNodeScope(var2);

         try {
            this.jjtree.closeNodeScope(var2, 2);
            var3 = false;
            this.jjtreeCloseNodeScope(var2);
            var2.kind = var1.kind;
         } finally {
            if (var3) {
               this.jjtree.closeNodeScope(var2, 2);
               this.jjtreeCloseNodeScope(var2);
            }

         }
      default:
      }
   }

   public final void RelationalExpression() throws ParseException {
      Token var1 = null;
      this.ShiftExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 82:
         case 83:
         case 84:
         case 85:
         case 91:
         case 92:
         case 93:
         case 94:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 82:
               var1 = this.jj_consume_token(82);
               break;
            case 83:
               var1 = this.jj_consume_token(83);
               break;
            case 84:
               var1 = this.jj_consume_token(84);
               break;
            case 85:
               var1 = this.jj_consume_token(85);
               break;
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            case 91:
               var1 = this.jj_consume_token(91);
               break;
            case 92:
               var1 = this.jj_consume_token(92);
               break;
            case 93:
               var1 = this.jj_consume_token(93);
               break;
            case 94:
               var1 = this.jj_consume_token(94);
            }

            this.ShiftExpression();
            BSHBinaryExpression var2 = new BSHBinaryExpression(15);
            boolean var3 = true;
            this.jjtree.openNodeScope(var2);
            this.jjtreeOpenNodeScope(var2);

            try {
               this.jjtree.closeNodeScope(var2, 2);
               var3 = false;
               this.jjtreeCloseNodeScope(var2);
               var2.kind = var1.kind;
               break;
            } finally {
               if (var3) {
                  this.jjtree.closeNodeScope(var2, 2);
                  this.jjtreeCloseNodeScope(var2);
               }

            }
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         default:
            return;
         }
      }
   }

   public final void ShiftExpression() throws ParseException {
      Token var1 = null;
      this.AdditiveExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 112:
               var1 = this.jj_consume_token(112);
               break;
            case 113:
               var1 = this.jj_consume_token(113);
               break;
            case 114:
               var1 = this.jj_consume_token(114);
               break;
            case 115:
               var1 = this.jj_consume_token(115);
               break;
            case 116:
               var1 = this.jj_consume_token(116);
               break;
            case 117:
               var1 = this.jj_consume_token(117);
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            this.AdditiveExpression();
            BSHBinaryExpression var2 = new BSHBinaryExpression(15);
            boolean var3 = true;
            this.jjtree.openNodeScope(var2);
            this.jjtreeOpenNodeScope(var2);

            try {
               this.jjtree.closeNodeScope(var2, 2);
               var3 = false;
               this.jjtreeCloseNodeScope(var2);
               var2.kind = var1.kind;
               break;
            } finally {
               if (var3) {
                  this.jjtree.closeNodeScope(var2, 2);
                  this.jjtreeCloseNodeScope(var2);
               }

            }
         default:
            return;
         }
      }
   }

   public final void AdditiveExpression() throws ParseException {
      Token var1 = null;
      this.MultiplicativeExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 102:
         case 103:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 102:
               var1 = this.jj_consume_token(102);
               break;
            case 103:
               var1 = this.jj_consume_token(103);
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            this.MultiplicativeExpression();
            BSHBinaryExpression var2 = new BSHBinaryExpression(15);
            boolean var3 = true;
            this.jjtree.openNodeScope(var2);
            this.jjtreeOpenNodeScope(var2);

            try {
               this.jjtree.closeNodeScope(var2, 2);
               var3 = false;
               this.jjtreeCloseNodeScope(var2);
               var2.kind = var1.kind;
               break;
            } finally {
               if (var3) {
                  this.jjtree.closeNodeScope(var2, 2);
                  this.jjtreeCloseNodeScope(var2);
               }

            }
         default:
            return;
         }
      }
   }

   public final void MultiplicativeExpression() throws ParseException {
      Token var1 = null;
      this.UnaryExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 104:
         case 105:
         case 111:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 104:
               var1 = this.jj_consume_token(104);
               break;
            case 105:
               var1 = this.jj_consume_token(105);
               break;
            case 111:
               var1 = this.jj_consume_token(111);
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            this.UnaryExpression();
            BSHBinaryExpression var2 = new BSHBinaryExpression(15);
            boolean var3 = true;
            this.jjtree.openNodeScope(var2);
            this.jjtreeOpenNodeScope(var2);

            try {
               this.jjtree.closeNodeScope(var2, 2);
               var3 = false;
               this.jjtreeCloseNodeScope(var2);
               var2.kind = var1.kind;
               break;
            } finally {
               if (var3) {
                  this.jjtree.closeNodeScope(var2, 2);
                  this.jjtreeCloseNodeScope(var2);
               }

            }
         default:
            return;
         }
      }
   }

   public final void UnaryExpression() throws ParseException {
      Token var1 = null;
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 11:
      case 14:
      case 17:
      case 22:
      case 26:
      case 29:
      case 36:
      case 38:
      case 40:
      case 41:
      case 47:
      case 55:
      case 57:
      case 60:
      case 64:
      case 66:
      case 67:
      case 69:
      case 72:
      case 86:
      case 87:
         this.UnaryExpressionNotPlusMinus();
         break;
      case 12:
      case 13:
      case 15:
      case 16:
      case 18:
      case 19:
      case 20:
      case 21:
      case 23:
      case 24:
      case 25:
      case 27:
      case 28:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 37:
      case 39:
      case 42:
      case 43:
      case 44:
      case 45:
      case 46:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 56:
      case 58:
      case 59:
      case 61:
      case 62:
      case 63:
      case 65:
      case 68:
      case 70:
      case 71:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
      case 80:
      case 81:
      case 82:
      case 83:
      case 84:
      case 85:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      default:
         this.jj_consume_token(-1);
         throw new ParseException();
      case 100:
         this.PreIncrementExpression();
         break;
      case 101:
         this.PreDecrementExpression();
         break;
      case 102:
      case 103:
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 102:
            var1 = this.jj_consume_token(102);
            break;
         case 103:
            var1 = this.jj_consume_token(103);
            break;
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }

         this.UnaryExpression();
         BSHUnaryExpression var2 = new BSHUnaryExpression(16);
         boolean var3 = true;
         this.jjtree.openNodeScope(var2);
         this.jjtreeOpenNodeScope(var2);

         try {
            this.jjtree.closeNodeScope(var2, 1);
            var3 = false;
            this.jjtreeCloseNodeScope(var2);
            var2.kind = var1.kind;
         } finally {
            if (var3) {
               this.jjtree.closeNodeScope(var2, 1);
               this.jjtreeCloseNodeScope(var2);
            }

         }
      }

   }

   public final void PreIncrementExpression() throws ParseException {
      Token var1 = null;
      var1 = this.jj_consume_token(100);
      this.PrimaryExpression();
      BSHUnaryExpression var2 = new BSHUnaryExpression(16);
      boolean var3 = true;
      this.jjtree.openNodeScope(var2);
      this.jjtreeOpenNodeScope(var2);

      try {
         this.jjtree.closeNodeScope(var2, 1);
         var3 = false;
         this.jjtreeCloseNodeScope(var2);
         var2.kind = var1.kind;
      } finally {
         if (var3) {
            this.jjtree.closeNodeScope(var2, 1);
            this.jjtreeCloseNodeScope(var2);
         }

      }

   }

   public final void PreDecrementExpression() throws ParseException {
      Token var1 = null;
      var1 = this.jj_consume_token(101);
      this.PrimaryExpression();
      BSHUnaryExpression var2 = new BSHUnaryExpression(16);
      boolean var3 = true;
      this.jjtree.openNodeScope(var2);
      this.jjtreeOpenNodeScope(var2);

      try {
         this.jjtree.closeNodeScope(var2, 1);
         var3 = false;
         this.jjtreeCloseNodeScope(var2);
         var2.kind = var1.kind;
      } finally {
         if (var3) {
            this.jjtree.closeNodeScope(var2, 1);
            this.jjtreeCloseNodeScope(var2);
         }

      }

   }

   public final void UnaryExpressionNotPlusMinus() throws ParseException {
      Token var1 = null;
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 86:
      case 87:
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 86:
            var1 = this.jj_consume_token(86);
            break;
         case 87:
            var1 = this.jj_consume_token(87);
            break;
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }

         this.UnaryExpression();
         BSHUnaryExpression var2 = new BSHUnaryExpression(16);
         boolean var3 = true;
         this.jjtree.openNodeScope(var2);
         this.jjtreeOpenNodeScope(var2);

         try {
            this.jjtree.closeNodeScope(var2, 1);
            var3 = false;
            this.jjtreeCloseNodeScope(var2);
            var2.kind = var1.kind;
            break;
         } finally {
            if (var3) {
               this.jjtree.closeNodeScope(var2, 1);
               this.jjtreeCloseNodeScope(var2);
            }

         }
      default:
         if (this.jj_2_9(Integer.MAX_VALUE)) {
            this.CastExpression();
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 11:
            case 14:
            case 17:
            case 22:
            case 26:
            case 29:
            case 36:
            case 38:
            case 40:
            case 41:
            case 47:
            case 55:
            case 57:
            case 60:
            case 64:
            case 66:
            case 67:
            case 69:
            case 72:
               this.PostfixExpression();
               break;
            case 12:
            case 13:
            case 15:
            case 16:
            case 18:
            case 19:
            case 20:
            case 21:
            case 23:
            case 24:
            case 25:
            case 27:
            case 28:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 37:
            case 39:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 56:
            case 58:
            case 59:
            case 61:
            case 62:
            case 63:
            case 65:
            case 68:
            case 70:
            case 71:
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }
      }

   }

   public final void CastLookahead() throws ParseException {
      if (this.jj_2_10(2)) {
         this.jj_consume_token(72);
         this.PrimitiveType();
      } else if (this.jj_2_11(Integer.MAX_VALUE)) {
         this.jj_consume_token(72);
         this.AmbiguousName();
         this.jj_consume_token(76);
         this.jj_consume_token(77);
      } else {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 72:
            this.jj_consume_token(72);
            this.AmbiguousName();
            this.jj_consume_token(73);
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 26:
            case 41:
            case 55:
            case 57:
            case 60:
            case 64:
            case 66:
            case 67:
               this.Literal();
               return;
            case 40:
               this.jj_consume_token(40);
               return;
            case 69:
               this.jj_consume_token(69);
               return;
            case 72:
               this.jj_consume_token(72);
               return;
            case 86:
               this.jj_consume_token(86);
               return;
            case 87:
               this.jj_consume_token(87);
               return;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      }

   }

   public final void PostfixExpression() throws ParseException {
      Token var1 = null;
      if (this.jj_2_12(Integer.MAX_VALUE)) {
         this.PrimaryExpression();
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 100:
            var1 = this.jj_consume_token(100);
            break;
         case 101:
            var1 = this.jj_consume_token(101);
            break;
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }

         BSHUnaryExpression var2 = new BSHUnaryExpression(16);
         boolean var3 = true;
         this.jjtree.openNodeScope(var2);
         this.jjtreeOpenNodeScope(var2);

         try {
            this.jjtree.closeNodeScope(var2, 1);
            var3 = false;
            this.jjtreeCloseNodeScope(var2);
            var2.kind = var1.kind;
            var2.postfix = true;
         } finally {
            if (var3) {
               this.jjtree.closeNodeScope(var2, 1);
               this.jjtreeCloseNodeScope(var2);
            }

         }
      } else {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 14:
         case 17:
         case 22:
         case 26:
         case 29:
         case 36:
         case 38:
         case 40:
         case 41:
         case 47:
         case 55:
         case 57:
         case 60:
         case 64:
         case 66:
         case 67:
         case 69:
         case 72:
            this.PrimaryExpression();
            break;
         case 12:
         case 13:
         case 15:
         case 16:
         case 18:
         case 19:
         case 20:
         case 21:
         case 23:
         case 24:
         case 25:
         case 27:
         case 28:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 37:
         case 39:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 56:
         case 58:
         case 59:
         case 61:
         case 62:
         case 63:
         case 65:
         case 68:
         case 70:
         case 71:
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      }

   }

   public final void CastExpression() throws ParseException {
      BSHCastExpression var1 = new BSHCastExpression(17);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         if (this.jj_2_13(Integer.MAX_VALUE)) {
            this.jj_consume_token(72);
            this.Type();
            this.jj_consume_token(73);
            this.UnaryExpression();
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 72:
               this.jj_consume_token(72);
               this.Type();
               this.jj_consume_token(73);
               this.UnaryExpressionNotPlusMinus();
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void PrimaryExpression() throws ParseException {
      BSHPrimaryExpression var1 = new BSHPrimaryExpression(18);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.PrimaryPrefix();

         while(true) {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 74:
            case 76:
            case 80:
               this.PrimarySuffix();
               break;
            default:
               return;
            }
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         } else if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         } else {
            throw (Error)var8;
         }
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }
   }

   public final void MethodInvocation() throws ParseException {
      BSHMethodInvocation var1 = new BSHMethodInvocation(19);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.AmbiguousName();
         this.Arguments();
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void PrimaryPrefix() throws ParseException {
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 26:
      case 41:
      case 55:
      case 57:
      case 60:
      case 64:
      case 66:
      case 67:
         this.Literal();
         break;
      case 40:
         this.AllocationExpression();
         break;
      case 72:
         this.jj_consume_token(72);
         this.Expression();
         this.jj_consume_token(73);
         break;
      default:
         if (this.jj_2_14(Integer.MAX_VALUE)) {
            this.MethodInvocation();
         } else if (this.jj_2_15(Integer.MAX_VALUE)) {
            this.Type();
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 69:
               this.AmbiguousName();
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }
      }

   }

   public final void PrimarySuffix() throws ParseException {
      BSHPrimarySuffix var1 = new BSHPrimarySuffix(20);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);
      Token var3 = null;

      try {
         if (this.jj_2_16(2)) {
            this.jj_consume_token(80);
            this.jj_consume_token(13);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.operation = 0;
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 74:
               this.jj_consume_token(74);
               this.Expression();
               this.jj_consume_token(75);
               this.jjtree.closeNodeScope(var1, true);
               var2 = false;
               this.jjtreeCloseNodeScope(var1);
               var1.operation = 3;
               break;
            case 76:
               this.jj_consume_token(76);
               this.Expression();
               this.jj_consume_token(77);
               this.jjtree.closeNodeScope(var1, true);
               var2 = false;
               this.jjtreeCloseNodeScope(var1);
               var1.operation = 1;
               break;
            case 80:
               this.jj_consume_token(80);
               var3 = this.jj_consume_token(69);
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 72:
                  this.Arguments();
               default:
                  this.jjtree.closeNodeScope(var1, true);
                  var2 = false;
                  this.jjtreeCloseNodeScope(var1);
                  var1.operation = 2;
                  var1.field = var3.image;
                  return;
               }
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }
      } catch (Throwable var9) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var9 instanceof RuntimeException) {
            throw (RuntimeException)var9;
         }

         if (var9 instanceof ParseException) {
            throw (ParseException)var9;
         }

         throw (Error)var9;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void Literal() throws ParseException {
      BSHLiteral var1 = new BSHLiteral(21);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         Token var3;
         String var4;
         char var5;
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 26:
         case 55:
            boolean var7 = this.BooleanLiteral();
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.value = new Primitive(var7);
            break;
         case 41:
            this.NullLiteral();
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.value = Primitive.NULL;
            break;
         case 57:
            this.VoidLiteral();
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.value = Primitive.VOID;
            break;
         case 60:
            var3 = this.jj_consume_token(60);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var4 = var3.image;
            var5 = var4.charAt(var4.length() - 1);
            if (var5 != 'l' && var5 != 'L') {
               try {
                  var1.value = new Primitive(Integer.decode(var4));
               } catch (NumberFormatException var17) {
                  throw this.createParseException("Error or number too big for integer type: " + var4);
               }
            } else {
               var4 = var4.substring(0, var4.length() - 1);
               var1.value = new Primitive(new Long(var4));
            }
            break;
         case 64:
            var3 = this.jj_consume_token(64);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var4 = var3.image;
            var5 = var4.charAt(var4.length() - 1);
            if (var5 != 'f' && var5 != 'F') {
               if (var5 == 'd' || var5 == 'D') {
                  var4 = var4.substring(0, var4.length() - 1);
               }

               var1.value = new Primitive(new Double(var4));
            } else {
               var4 = var4.substring(0, var4.length() - 1);
               var1.value = new Primitive(new Float(var4));
            }
            break;
         case 66:
            var3 = this.jj_consume_token(66);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);

            try {
               var1.charSetup(var3.image.substring(1, var3.image.length() - 1));
               break;
            } catch (Exception var16) {
               throw this.createParseException("Error parsing character: " + var3.image);
            }
         case 67:
            var3 = this.jj_consume_token(67);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);

            try {
               var1.stringSetup(var3.image.substring(1, var3.image.length() - 1));
               break;
            } catch (Exception var15) {
               throw this.createParseException("Error parsing string: " + var3.image);
            }
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      } catch (Throwable var18) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var18 instanceof RuntimeException) {
            throw (RuntimeException)var18;
         }

         if (var18 instanceof ParseException) {
            throw (ParseException)var18;
         }

         throw (Error)var18;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final boolean BooleanLiteral() throws ParseException {
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 26:
         this.jj_consume_token(26);
         return false;
      case 55:
         this.jj_consume_token(55);
         return true;
      default:
         this.jj_consume_token(-1);
         throw new ParseException();
      }
   }

   public final void NullLiteral() throws ParseException {
      this.jj_consume_token(41);
   }

   public final void VoidLiteral() throws ParseException {
      this.jj_consume_token(57);
   }

   public final void Arguments() throws ParseException {
      BSHArguments var1 = new BSHArguments(22);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(72);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 14:
         case 17:
         case 22:
         case 26:
         case 29:
         case 36:
         case 38:
         case 40:
         case 41:
         case 47:
         case 55:
         case 57:
         case 60:
         case 64:
         case 66:
         case 67:
         case 69:
         case 72:
         case 86:
         case 87:
         case 100:
         case 101:
         case 102:
         case 103:
            this.ArgumentList();
         case 12:
         case 13:
         case 15:
         case 16:
         case 18:
         case 19:
         case 20:
         case 21:
         case 23:
         case 24:
         case 25:
         case 27:
         case 28:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 37:
         case 39:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 56:
         case 58:
         case 59:
         case 61:
         case 62:
         case 63:
         case 65:
         case 68:
         case 70:
         case 71:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         default:
            this.jj_consume_token(73);
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void ArgumentList() throws ParseException {
      this.Expression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 79:
            this.jj_consume_token(79);
            this.Expression();
            break;
         default:
            return;
         }
      }
   }

   public final void AllocationExpression() throws ParseException {
      BSHAllocationExpression var1 = new BSHAllocationExpression(23);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         if (this.jj_2_18(2)) {
            this.jj_consume_token(40);
            this.PrimitiveType();
            this.ArrayDimensions();
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 40:
               this.jj_consume_token(40);
               this.AmbiguousName();
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 72:
                  this.Arguments();
                  if (this.jj_2_17(2)) {
                     this.Block();
                  }

                  return;
               case 76:
                  this.ArrayDimensions();
                  return;
               default:
                  this.jj_consume_token(-1);
                  throw new ParseException();
               }
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void ArrayDimensions() throws ParseException {
      BSHArrayDimensions var1 = new BSHArrayDimensions(24);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         if (this.jj_2_21(2)) {
            while(true) {
               this.jj_consume_token(76);
               this.Expression();
               this.jj_consume_token(77);
               var1.addDefinedDimension();
               if (!this.jj_2_19(2)) {
                  while(this.jj_2_20(2)) {
                     this.jj_consume_token(76);
                     this.jj_consume_token(77);
                     var1.addUndefinedDimension();
                  }
                  break;
               }
            }
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 76:
               while(true) {
                  this.jj_consume_token(76);
                  this.jj_consume_token(77);
                  var1.addUndefinedDimension();
                  switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 76:
                     break;
                  default:
                     this.ArrayInitializer();
                     return;
                  }
               }
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void Statement() throws ParseException {
      if (this.jj_2_22(2)) {
         this.LabeledStatement();
      } else {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 14:
         case 17:
         case 22:
         case 26:
         case 29:
         case 36:
         case 38:
         case 40:
         case 41:
         case 47:
         case 55:
         case 57:
         case 60:
         case 64:
         case 66:
         case 67:
         case 69:
         case 72:
         case 86:
         case 87:
         case 100:
         case 101:
         case 102:
         case 103:
            this.StatementExpression();
            this.jj_consume_token(78);
            break;
         case 12:
         case 13:
         case 15:
         case 16:
         case 18:
         case 19:
         case 20:
         case 23:
         case 24:
         case 25:
         case 27:
         case 28:
         case 30:
         case 31:
         case 33:
         case 34:
         case 35:
         case 37:
         case 39:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 48:
         case 49:
         case 51:
         case 52:
         case 53:
         case 54:
         case 56:
         case 58:
         case 61:
         case 62:
         case 63:
         case 65:
         case 68:
         case 70:
         case 71:
         case 73:
         case 75:
         case 76:
         case 77:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         default:
            if (this.isRegularForStatement()) {
               this.ForStatement();
               break;
            } else {
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 12:
                  this.BreakStatement();
                  return;
               case 19:
                  this.ContinueStatement();
                  return;
               case 30:
                  this.EnhancedForStatement();
                  return;
               case 46:
                  this.ReturnStatement();
                  return;
               case 51:
                  this.SynchronizedStatement();
                  return;
               case 53:
                  this.ThrowStatement();
                  return;
               case 56:
                  this.TryStatement();
                  return;
               default:
                  this.jj_consume_token(-1);
                  throw new ParseException();
               }
            }
         case 21:
            this.DoStatement();
            break;
         case 32:
            this.IfStatement();
            break;
         case 50:
            this.SwitchStatement();
            break;
         case 59:
            this.WhileStatement();
            break;
         case 74:
            this.Block();
            break;
         case 78:
            this.EmptyStatement();
         }
      }

   }

   public final void LabeledStatement() throws ParseException {
      this.jj_consume_token(69);
      this.jj_consume_token(89);
      this.Statement();
   }

   public final void Block() throws ParseException {
      BSHBlock var1 = new BSHBlock(25);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(74);

         while(this.jj_2_23(1)) {
            this.BlockStatement();
         }

         this.jj_consume_token(75);
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         } else if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         } else {
            throw (Error)var8;
         }
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }
   }

   public final void BlockStatement() throws ParseException {
      if (this.jj_2_24(Integer.MAX_VALUE)) {
         this.ClassDeclaration();
      } else if (this.jj_2_25(Integer.MAX_VALUE)) {
         this.MethodDeclaration();
      } else if (this.jj_2_26(Integer.MAX_VALUE)) {
         this.MethodDeclaration();
      } else if (this.jj_2_27(Integer.MAX_VALUE)) {
         this.TypedVariableDeclaration();
         this.jj_consume_token(78);
      } else if (this.jj_2_28(1)) {
         this.Statement();
      } else {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 34:
         case 48:
            this.ImportDeclaration();
            break;
         case 42:
            this.PackageDeclaration();
            break;
         case 68:
            this.FormalComment();
            break;
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      }

   }

   public final void FormalComment() throws ParseException {
      BSHFormalComment var1 = new BSHFormalComment(26);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         Token var3 = this.jj_consume_token(68);
         this.jjtree.closeNodeScope(var1, this.retainComments);
         var2 = false;
         this.jjtreeCloseNodeScope(var1);
         var1.text = var3.image;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, this.retainComments);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void EmptyStatement() throws ParseException {
      this.jj_consume_token(78);
   }

   public final void StatementExpression() throws ParseException {
      this.Expression();
   }

   public final void SwitchStatement() throws ParseException {
      BSHSwitchStatement var1 = new BSHSwitchStatement(27);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(50);
         this.jj_consume_token(72);
         this.Expression();
         this.jj_consume_token(73);
         this.jj_consume_token(74);

         label114:
         while(true) {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 15:
            case 20:
               this.SwitchLabel();

               while(true) {
                  if (!this.jj_2_29(1)) {
                     continue label114;
                  }

                  this.BlockStatement();
               }
            default:
               this.jj_consume_token(75);
               return;
            }
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         } else if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         } else {
            throw (Error)var8;
         }
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }
   }

   public final void SwitchLabel() throws ParseException {
      BSHSwitchLabel var1 = new BSHSwitchLabel(28);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 15:
            this.jj_consume_token(15);
            this.Expression();
            this.jj_consume_token(89);
            break;
         case 20:
            this.jj_consume_token(20);
            this.jj_consume_token(89);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.isDefault = true;
            break;
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void IfStatement() throws ParseException {
      BSHIfStatement var1 = new BSHIfStatement(29);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(32);
         this.jj_consume_token(72);
         this.Expression();
         this.jj_consume_token(73);
         this.Statement();
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 23:
            this.jj_consume_token(23);
            this.Statement();
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void WhileStatement() throws ParseException {
      BSHWhileStatement var1 = new BSHWhileStatement(30);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(59);
         this.jj_consume_token(72);
         this.Expression();
         this.jj_consume_token(73);
         this.Statement();
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void DoStatement() throws ParseException {
      BSHWhileStatement var1 = new BSHWhileStatement(30);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(21);
         this.Statement();
         this.jj_consume_token(59);
         this.jj_consume_token(72);
         this.Expression();
         this.jj_consume_token(73);
         this.jj_consume_token(78);
         this.jjtree.closeNodeScope(var1, true);
         var2 = false;
         this.jjtreeCloseNodeScope(var1);
         var1.isDoStatement = true;
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void ForStatement() throws ParseException {
      BSHForStatement var1 = new BSHForStatement(31);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);
      Object var3 = null;

      try {
         this.jj_consume_token(30);
         this.jj_consume_token(72);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 10:
         case 11:
         case 14:
         case 17:
         case 22:
         case 26:
         case 27:
         case 29:
         case 36:
         case 38:
         case 39:
         case 40:
         case 41:
         case 43:
         case 44:
         case 45:
         case 47:
         case 48:
         case 49:
         case 51:
         case 52:
         case 55:
         case 57:
         case 58:
         case 60:
         case 64:
         case 66:
         case 67:
         case 69:
         case 72:
         case 86:
         case 87:
         case 100:
         case 101:
         case 102:
         case 103:
            this.ForInit();
            var1.hasForInit = true;
         case 12:
         case 13:
         case 15:
         case 16:
         case 18:
         case 19:
         case 20:
         case 21:
         case 23:
         case 24:
         case 25:
         case 28:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 37:
         case 42:
         case 46:
         case 50:
         case 53:
         case 54:
         case 56:
         case 59:
         case 61:
         case 62:
         case 63:
         case 65:
         case 68:
         case 70:
         case 71:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         }

         this.jj_consume_token(78);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 14:
         case 17:
         case 22:
         case 26:
         case 29:
         case 36:
         case 38:
         case 40:
         case 41:
         case 47:
         case 55:
         case 57:
         case 60:
         case 64:
         case 66:
         case 67:
         case 69:
         case 72:
         case 86:
         case 87:
         case 100:
         case 101:
         case 102:
         case 103:
            this.Expression();
            var1.hasExpression = true;
         case 12:
         case 13:
         case 15:
         case 16:
         case 18:
         case 19:
         case 20:
         case 21:
         case 23:
         case 24:
         case 25:
         case 27:
         case 28:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 37:
         case 39:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 56:
         case 58:
         case 59:
         case 61:
         case 62:
         case 63:
         case 65:
         case 68:
         case 70:
         case 71:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         }

         this.jj_consume_token(78);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 14:
         case 17:
         case 22:
         case 26:
         case 29:
         case 36:
         case 38:
         case 40:
         case 41:
         case 47:
         case 55:
         case 57:
         case 60:
         case 64:
         case 66:
         case 67:
         case 69:
         case 72:
         case 86:
         case 87:
         case 100:
         case 101:
         case 102:
         case 103:
            this.ForUpdate();
            var1.hasForUpdate = true;
         case 12:
         case 13:
         case 15:
         case 16:
         case 18:
         case 19:
         case 20:
         case 21:
         case 23:
         case 24:
         case 25:
         case 27:
         case 28:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 37:
         case 39:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 56:
         case 58:
         case 59:
         case 61:
         case 62:
         case 63:
         case 65:
         case 68:
         case 70:
         case 71:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         default:
            this.jj_consume_token(73);
            this.Statement();
         }
      } catch (Throwable var9) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var9 instanceof RuntimeException) {
            throw (RuntimeException)var9;
         }

         if (var9 instanceof ParseException) {
            throw (ParseException)var9;
         }

         throw (Error)var9;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void EnhancedForStatement() throws ParseException {
      BSHEnhancedForStatement var1 = new BSHEnhancedForStatement(32);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);
      Token var3 = null;

      try {
         if (this.jj_2_30(4)) {
            this.jj_consume_token(30);
            this.jj_consume_token(72);
            var3 = this.jj_consume_token(69);
            this.jj_consume_token(89);
            this.Expression();
            this.jj_consume_token(73);
            this.Statement();
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.varName = var3.image;
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 30:
               this.jj_consume_token(30);
               this.jj_consume_token(72);
               this.Type();
               var3 = this.jj_consume_token(69);
               this.jj_consume_token(89);
               this.Expression();
               this.jj_consume_token(73);
               this.Statement();
               this.jjtree.closeNodeScope(var1, true);
               var2 = false;
               this.jjtreeCloseNodeScope(var1);
               var1.varName = var3.image;
               break;
            default:
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }
      } catch (Throwable var9) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var9 instanceof RuntimeException) {
            throw (RuntimeException)var9;
         }

         if (var9 instanceof ParseException) {
            throw (ParseException)var9;
         }

         throw (Error)var9;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void ForInit() throws ParseException {
      Object var1 = null;
      if (this.jj_2_31(Integer.MAX_VALUE)) {
         this.TypedVariableDeclaration();
      } else {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 14:
         case 17:
         case 22:
         case 26:
         case 29:
         case 36:
         case 38:
         case 40:
         case 41:
         case 47:
         case 55:
         case 57:
         case 60:
         case 64:
         case 66:
         case 67:
         case 69:
         case 72:
         case 86:
         case 87:
         case 100:
         case 101:
         case 102:
         case 103:
            this.StatementExpressionList();
            break;
         case 12:
         case 13:
         case 15:
         case 16:
         case 18:
         case 19:
         case 20:
         case 21:
         case 23:
         case 24:
         case 25:
         case 27:
         case 28:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 37:
         case 39:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 56:
         case 58:
         case 59:
         case 61:
         case 62:
         case 63:
         case 65:
         case 68:
         case 70:
         case 71:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         default:
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      }

   }

   public final void TypedVariableDeclaration() throws ParseException {
      BSHTypedVariableDeclaration var1 = new BSHTypedVariableDeclaration(33);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);
      Object var3 = null;

      try {
         Modifiers var4 = this.Modifiers(2, false);
         this.Type();
         this.VariableDeclarator();

         while(true) {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 79:
               this.jj_consume_token(79);
               this.VariableDeclarator();
               break;
            default:
               this.jjtree.closeNodeScope(var1, true);
               var2 = false;
               this.jjtreeCloseNodeScope(var1);
               var1.modifiers = var4;
               return;
            }
         }
      } catch (Throwable var10) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var10 instanceof RuntimeException) {
            throw (RuntimeException)var10;
         } else if (var10 instanceof ParseException) {
            throw (ParseException)var10;
         } else {
            throw (Error)var10;
         }
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }
   }

   public final void StatementExpressionList() throws ParseException {
      BSHStatementExpressionList var1 = new BSHStatementExpressionList(34);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.StatementExpression();

         while(true) {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 79:
               this.jj_consume_token(79);
               this.StatementExpression();
               break;
            default:
               return;
            }
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         } else if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         } else {
            throw (Error)var8;
         }
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }
   }

   public final void ForUpdate() throws ParseException {
      this.StatementExpressionList();
   }

   public final void BreakStatement() throws ParseException {
      BSHReturnStatement var1 = new BSHReturnStatement(35);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(12);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 69:
            this.jj_consume_token(69);
         default:
            this.jj_consume_token(78);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.kind = 12;
         }
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void ContinueStatement() throws ParseException {
      BSHReturnStatement var1 = new BSHReturnStatement(35);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(19);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 69:
            this.jj_consume_token(69);
         default:
            this.jj_consume_token(78);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.kind = 19;
         }
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void ReturnStatement() throws ParseException {
      BSHReturnStatement var1 = new BSHReturnStatement(35);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(46);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
         case 14:
         case 17:
         case 22:
         case 26:
         case 29:
         case 36:
         case 38:
         case 40:
         case 41:
         case 47:
         case 55:
         case 57:
         case 60:
         case 64:
         case 66:
         case 67:
         case 69:
         case 72:
         case 86:
         case 87:
         case 100:
         case 101:
         case 102:
         case 103:
            this.Expression();
         case 12:
         case 13:
         case 15:
         case 16:
         case 18:
         case 19:
         case 20:
         case 21:
         case 23:
         case 24:
         case 25:
         case 27:
         case 28:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 37:
         case 39:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 56:
         case 58:
         case 59:
         case 61:
         case 62:
         case 63:
         case 65:
         case 68:
         case 70:
         case 71:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         default:
            this.jj_consume_token(78);
            this.jjtree.closeNodeScope(var1, true);
            var2 = false;
            this.jjtreeCloseNodeScope(var1);
            var1.kind = 46;
         }
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void SynchronizedStatement() throws ParseException {
      BSHBlock var1 = new BSHBlock(25);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(51);
         this.jj_consume_token(72);
         this.Expression();
         this.jj_consume_token(73);
         this.Block();
         this.jjtree.closeNodeScope(var1, true);
         var2 = false;
         this.jjtreeCloseNodeScope(var1);
         var1.isSynchronized = true;
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void ThrowStatement() throws ParseException {
      BSHThrowStatement var1 = new BSHThrowStatement(36);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);

      try {
         this.jj_consume_token(53);
         this.Expression();
         this.jj_consume_token(78);
      } catch (Throwable var8) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var8 instanceof RuntimeException) {
            throw (RuntimeException)var8;
         }

         if (var8 instanceof ParseException) {
            throw (ParseException)var8;
         }

         throw (Error)var8;
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }

   }

   public final void TryStatement() throws ParseException {
      BSHTryStatement var1 = new BSHTryStatement(37);
      boolean var2 = true;
      this.jjtree.openNodeScope(var1);
      this.jjtreeOpenNodeScope(var1);
      boolean var3 = false;

      try {
         this.jj_consume_token(56);
         this.Block();

         while(true) {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 16:
               this.jj_consume_token(16);
               this.jj_consume_token(72);
               this.FormalParameter();
               this.jj_consume_token(73);
               this.Block();
               var3 = true;
               break;
            default:
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 28:
                  this.jj_consume_token(28);
                  this.Block();
                  var3 = true;
               }

               this.jjtree.closeNodeScope(var1, true);
               var2 = false;
               this.jjtreeCloseNodeScope(var1);
               if (!var3) {
                  throw this.generateParseException();
               }

               return;
            }
         }
      } catch (Throwable var9) {
         if (var2) {
            this.jjtree.clearNodeScope(var1);
            var2 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var9 instanceof RuntimeException) {
            throw (RuntimeException)var9;
         } else if (var9 instanceof ParseException) {
            throw (ParseException)var9;
         } else {
            throw (Error)var9;
         }
      } finally {
         if (var2) {
            this.jjtree.closeNodeScope(var1, true);
            this.jjtreeCloseNodeScope(var1);
         }

      }
   }

   private final boolean jj_2_1(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_1();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_2(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_2();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_3(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_3();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_4(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_4();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_5(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_5();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_6(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_6();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_7(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_7();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_8(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_8();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_9(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_9();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_10(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_10();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_11(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_11();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_12(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_12();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_13(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_13();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_14(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_14();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_15(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_15();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_16(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_16();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_17(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_17();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_18(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_18();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_19(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_19();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_20(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_20();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_21(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_21();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_22(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_22();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_23(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_23();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_24(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_24();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_25(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_25();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_26(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_26();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_27(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_27();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_28(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_28();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_29(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_29();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_30(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_30();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_2_31(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      try {
         return !this.jj_3_31();
      } catch (Parser.LookaheadSuccess var3) {
         return true;
      }
   }

   private final boolean jj_3R_46() {
      return this.jj_3R_91();
   }

   private final boolean jj_3R_28() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_46()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_47()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_48()) {
               this.jj_scanpos = var1;
               if (this.jj_3R_49()) {
                  this.jj_scanpos = var1;
                  if (this.jj_3_28()) {
                     this.jj_scanpos = var1;
                     if (this.jj_3R_50()) {
                        this.jj_scanpos = var1;
                        if (this.jj_3R_51()) {
                           this.jj_scanpos = var1;
                           if (this.jj_3R_52()) {
                              return true;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private final boolean jj_3_23() {
      return this.jj_3R_28();
   }

   private final boolean jj_3R_161() {
      if (this.jj_3R_164()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_169());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_38() {
      if (this.jj_scan_token(74)) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3_23());

         this.jj_scanpos = var1;
         return this.jj_scan_token(75);
      }
   }

   private final boolean jj_3R_158() {
      if (this.jj_3R_161()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_167());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_40() {
      if (this.jj_scan_token(69)) {
         return true;
      } else if (this.jj_scan_token(89)) {
         return true;
      } else {
         return this.jj_3R_45();
      }
   }

   private final boolean jj_3R_156() {
      if (this.jj_scan_token(88)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else if (this.jj_scan_token(89)) {
         return true;
      } else {
         return this.jj_3R_108();
      }
   }

   private final boolean jj_3R_165() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(108)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(109)) {
            return true;
         }
      }

      return this.jj_3R_158();
   }

   private final boolean jj_3R_153() {
      if (this.jj_3R_158()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_165());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_90() {
      return this.jj_3R_124();
   }

   private final boolean jj_3R_89() {
      return this.jj_3R_123();
   }

   private final boolean jj_3R_88() {
      return this.jj_3R_122();
   }

   private final boolean jj_3R_162() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(98)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(99)) {
            return true;
         }
      }

      return this.jj_3R_153();
   }

   private final boolean jj_3R_87() {
      return this.jj_3R_121();
   }

   private final boolean jj_3R_148() {
      if (this.jj_3R_153()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_162());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_86() {
      return this.jj_3R_120();
   }

   private final boolean jj_3R_85() {
      return this.jj_3R_119();
   }

   private final boolean jj_3R_84() {
      return this.jj_3R_118();
   }

   private final boolean jj_3R_159() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(96)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(97)) {
            return true;
         }
      }

      return this.jj_3R_148();
   }

   private final boolean jj_3R_83() {
      return this.jj_3R_117();
   }

   private final boolean jj_3R_135() {
      if (this.jj_3R_148()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_159());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_82() {
      return this.jj_3R_116();
   }

   private final boolean jj_3R_81() {
      return this.jj_3R_115();
   }

   private final boolean jj_3R_80() {
      return this.jj_3R_114();
   }

   private final boolean jj_3R_108() {
      if (this.jj_3R_135()) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_156()) {
            this.jj_scanpos = var1;
         }

         return false;
      }
   }

   private final boolean jj_3R_79() {
      return this.jj_3R_113();
   }

   private final boolean jj_3R_78() {
      if (this.jj_3R_112()) {
         return true;
      } else {
         return this.jj_scan_token(78);
      }
   }

   private final boolean jj_3_17() {
      return this.jj_3R_38();
   }

   private final boolean jj_3R_77() {
      return this.jj_3R_38();
   }

   private final boolean jj_3R_45() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3_22()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_77()) {
            this.jj_scanpos = var1;
            if (this.jj_scan_token(78)) {
               this.jj_scanpos = var1;
               if (this.jj_3R_78()) {
                  this.jj_scanpos = var1;
                  if (this.jj_3R_79()) {
                     this.jj_scanpos = var1;
                     if (this.jj_3R_80()) {
                        this.jj_scanpos = var1;
                        if (this.jj_3R_81()) {
                           this.jj_scanpos = var1;
                           if (this.jj_3R_82()) {
                              this.jj_scanpos = var1;
                              this.lookingAhead = true;
                              this.jj_semLA = this.isRegularForStatement();
                              this.lookingAhead = false;
                              if (!this.jj_semLA || this.jj_3R_83()) {
                                 this.jj_scanpos = var1;
                                 if (this.jj_3R_84()) {
                                    this.jj_scanpos = var1;
                                    if (this.jj_3R_85()) {
                                       this.jj_scanpos = var1;
                                       if (this.jj_3R_86()) {
                                          this.jj_scanpos = var1;
                                          if (this.jj_3R_87()) {
                                             this.jj_scanpos = var1;
                                             if (this.jj_3R_88()) {
                                                this.jj_scanpos = var1;
                                                if (this.jj_3R_89()) {
                                                   this.jj_scanpos = var1;
                                                   if (this.jj_3R_90()) {
                                                      return true;
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private final boolean jj_3_22() {
      return this.jj_3R_40();
   }

   private final boolean jj_3R_34() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(81)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(120)) {
            this.jj_scanpos = var1;
            if (this.jj_scan_token(121)) {
               this.jj_scanpos = var1;
               if (this.jj_scan_token(127)) {
                  this.jj_scanpos = var1;
                  if (this.jj_scan_token(118)) {
                     this.jj_scanpos = var1;
                     if (this.jj_scan_token(119)) {
                        this.jj_scanpos = var1;
                        if (this.jj_scan_token(122)) {
                           this.jj_scanpos = var1;
                           if (this.jj_scan_token(126)) {
                              this.jj_scanpos = var1;
                              if (this.jj_scan_token(124)) {
                                 this.jj_scanpos = var1;
                                 if (this.jj_scan_token(128)) {
                                    this.jj_scanpos = var1;
                                    if (this.jj_scan_token(129)) {
                                       this.jj_scanpos = var1;
                                       if (this.jj_scan_token(130)) {
                                          this.jj_scanpos = var1;
                                          if (this.jj_scan_token(131)) {
                                             this.jj_scanpos = var1;
                                             if (this.jj_scan_token(132)) {
                                                this.jj_scanpos = var1;
                                                if (this.jj_scan_token(133)) {
                                                   return true;
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private final boolean jj_3R_111() {
      if (this.jj_scan_token(79)) {
         return true;
      } else {
         return this.jj_3R_29();
      }
   }

   private final boolean jj_3R_160() {
      if (this.jj_scan_token(76)) {
         return true;
      } else {
         return this.jj_scan_token(77);
      }
   }

   private final boolean jj_3R_152() {
      if (this.jj_3R_69()) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3_17()) {
            this.jj_scanpos = var1;
         }

         return false;
      }
   }

   private final boolean jj_3R_157() {
      if (this.jj_3R_160()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_160());

         this.jj_scanpos = var1;
         return this.jj_3R_97();
      }
   }

   private final boolean jj_3_8() {
      if (this.jj_3R_33()) {
         return true;
      } else {
         return this.jj_3R_34();
      }
   }

   private final boolean jj_3_20() {
      if (this.jj_scan_token(76)) {
         return true;
      } else {
         return this.jj_scan_token(77);
      }
   }

   private final boolean jj_3R_151() {
      return this.jj_3R_150();
   }

   private final boolean jj_3_19() {
      if (this.jj_scan_token(76)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else {
         return this.jj_scan_token(77);
      }
   }

   private final boolean jj_3R_107() {
      if (this.jj_3R_33()) {
         return true;
      } else if (this.jj_3R_34()) {
         return true;
      } else {
         return this.jj_3R_39();
      }
   }

   private final boolean jj_3_21() {
      if (this.jj_3_19()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3_19());

         this.jj_scanpos = var1;

         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3_20());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_150() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3_21()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_157()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_71() {
      return this.jj_3R_108();
   }

   private final boolean jj_3R_39() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_70()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_71()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_70() {
      return this.jj_3R_107();
   }

   private final boolean jj_3R_145() {
      if (this.jj_scan_token(40)) {
         return true;
      } else if (this.jj_3R_29()) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_151()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_152()) {
               return true;
            }
         }

         return false;
      }
   }

   private final boolean jj_3_18() {
      if (this.jj_scan_token(40)) {
         return true;
      } else if (this.jj_3R_36()) {
         return true;
      } else {
         return this.jj_3R_150();
      }
   }

   private final boolean jj_3R_130() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3_18()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_145()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_147() {
      if (this.jj_scan_token(79)) {
         return true;
      } else {
         return this.jj_3R_39();
      }
   }

   private final boolean jj_3R_76() {
      if (this.jj_3R_29()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_111());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_134() {
      if (this.jj_3R_39()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_147());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_106() {
      return this.jj_3R_134();
   }

   private final boolean jj_3_7() {
      if (this.jj_scan_token(80)) {
         return true;
      } else {
         return this.jj_scan_token(69);
      }
   }

   private final boolean jj_3R_69() {
      if (this.jj_scan_token(72)) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_106()) {
            this.jj_scanpos = var1;
         }

         return this.jj_scan_token(73);
      }
   }

   private final boolean jj_3R_29() {
      if (this.jj_scan_token(69)) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3_7());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_68() {
      return this.jj_scan_token(22);
   }

   private final boolean jj_3R_67() {
      return this.jj_scan_token(29);
   }

   private final boolean jj_3R_155() {
      return this.jj_scan_token(26);
   }

   private final boolean jj_3R_66() {
      return this.jj_scan_token(38);
   }

   private final boolean jj_3R_65() {
      return this.jj_scan_token(36);
   }

   private final boolean jj_3R_154() {
      return this.jj_scan_token(55);
   }

   private final boolean jj_3R_149() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_154()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_155()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_64() {
      return this.jj_scan_token(47);
   }

   private final boolean jj_3R_56() {
      return this.jj_3R_29();
   }

   private final boolean jj_3R_63() {
      return this.jj_scan_token(14);
   }

   private final boolean jj_3R_62() {
      return this.jj_scan_token(17);
   }

   private final boolean jj_3R_61() {
      return this.jj_scan_token(11);
   }

   private final boolean jj_3R_36() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_61()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_62()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_63()) {
               this.jj_scanpos = var1;
               if (this.jj_3R_64()) {
                  this.jj_scanpos = var1;
                  if (this.jj_3R_65()) {
                     this.jj_scanpos = var1;
                     if (this.jj_3R_66()) {
                        this.jj_scanpos = var1;
                        if (this.jj_3R_67()) {
                           this.jj_scanpos = var1;
                           if (this.jj_3R_68()) {
                              return true;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private final boolean jj_3R_144() {
      return this.jj_scan_token(57);
   }

   private final boolean jj_3R_74() {
      return this.jj_3R_32();
   }

   private final boolean jj_3R_42() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_73()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_74()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_73() {
      return this.jj_scan_token(57);
   }

   private final boolean jj_3R_143() {
      return this.jj_scan_token(41);
   }

   private final boolean jj_3_6() {
      if (this.jj_scan_token(76)) {
         return true;
      } else {
         return this.jj_scan_token(77);
      }
   }

   private final boolean jj_3R_142() {
      return this.jj_3R_149();
   }

   private final boolean jj_3R_55() {
      return this.jj_3R_36();
   }

   private final boolean jj_3R_110() {
      if (this.jj_scan_token(79)) {
         return true;
      } else {
         return this.jj_3R_109();
      }
   }

   private final boolean jj_3R_141() {
      return this.jj_scan_token(67);
   }

   private final boolean jj_3R_32() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_55()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_56()) {
            return true;
         }
      }

      do {
         var1 = this.jj_scanpos;
      } while(!this.jj_3_6());

      this.jj_scanpos = var1;
      return false;
   }

   private final boolean jj_3R_140() {
      return this.jj_scan_token(66);
   }

   private final boolean jj_3R_190() {
      if (this.jj_scan_token(28)) {
         return true;
      } else {
         return this.jj_3R_38();
      }
   }

   private final boolean jj_3_4() {
      if (this.jj_scan_token(79)) {
         return true;
      } else {
         return this.jj_3R_31();
      }
   }

   private final boolean jj_3R_189() {
      if (this.jj_scan_token(16)) {
         return true;
      } else if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_109()) {
         return true;
      } else if (this.jj_scan_token(73)) {
         return true;
      } else {
         return this.jj_3R_38();
      }
   }

   private final boolean jj_3R_136() {
      return this.jj_scan_token(69);
   }

   private final boolean jj_3_5() {
      if (this.jj_3R_32()) {
         return true;
      } else {
         return this.jj_scan_token(69);
      }
   }

   private final boolean jj_3R_75() {
      if (this.jj_3R_109()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_110());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_109() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3_5()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_136()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_124() {
      if (this.jj_scan_token(56)) {
         return true;
      } else if (this.jj_3R_38()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_189());

         this.jj_scanpos = var1;
         var1 = this.jj_scanpos;
         if (this.jj_3R_190()) {
            this.jj_scanpos = var1;
         }

         return false;
      }
   }

   private final boolean jj_3R_43() {
      if (this.jj_scan_token(72)) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_75()) {
            this.jj_scanpos = var1;
         }

         return this.jj_scan_token(73);
      }
   }

   private final boolean jj_3R_163() {
      if (this.jj_3R_31()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3_4());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_139() {
      return this.jj_scan_token(64);
   }

   private final boolean jj_3R_97() {
      if (this.jj_scan_token(74)) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_163()) {
            this.jj_scanpos = var1;
         }

         var1 = this.jj_scanpos;
         if (this.jj_scan_token(79)) {
            this.jj_scanpos = var1;
         }

         return this.jj_scan_token(75);
      }
   }

   private final boolean jj_3R_30() {
      if (this.jj_scan_token(80)) {
         return true;
      } else {
         return this.jj_scan_token(104);
      }
   }

   private final boolean jj_3R_123() {
      if (this.jj_scan_token(53)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else {
         return this.jj_scan_token(78);
      }
   }

   private final boolean jj_3R_180() {
      if (this.jj_scan_token(81)) {
         return true;
      } else {
         return this.jj_3R_31();
      }
   }

   private final boolean jj_3R_54() {
      return this.jj_3R_39();
   }

   private final boolean jj_3R_188() {
      return this.jj_3R_39();
   }

   private final boolean jj_3R_53() {
      return this.jj_3R_97();
   }

   private final boolean jj_3R_31() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_53()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_54()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_122() {
      if (this.jj_scan_token(51)) {
         return true;
      } else if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else if (this.jj_scan_token(73)) {
         return true;
      } else {
         return this.jj_3R_38();
      }
   }

   private final boolean jj_3R_177() {
      if (this.jj_scan_token(79)) {
         return true;
      } else {
         return this.jj_3R_176();
      }
   }

   private final boolean jj_3R_210() {
      if (this.jj_scan_token(79)) {
         return true;
      } else {
         return this.jj_3R_112();
      }
   }

   private final boolean jj_3R_121() {
      if (this.jj_scan_token(46)) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_188()) {
            this.jj_scanpos = var1;
         }

         return this.jj_scan_token(78);
      }
   }

   private final boolean jj_3R_129() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_138()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_139()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_140()) {
               this.jj_scanpos = var1;
               if (this.jj_3R_141()) {
                  this.jj_scanpos = var1;
                  if (this.jj_3R_142()) {
                     this.jj_scanpos = var1;
                     if (this.jj_3R_143()) {
                        this.jj_scanpos = var1;
                        if (this.jj_3R_144()) {
                           return true;
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private final boolean jj_3R_138() {
      return this.jj_scan_token(60);
   }

   private final boolean jj_3R_146() {
      return this.jj_3R_69();
   }

   private final boolean jj_3R_176() {
      if (this.jj_scan_token(69)) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_180()) {
            this.jj_scanpos = var1;
         }

         return false;
      }
   }

   private final boolean jj_3R_105() {
      return this.jj_3R_129();
   }

   private final boolean jj_3R_120() {
      if (this.jj_scan_token(19)) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_scan_token(69)) {
            this.jj_scanpos = var1;
         }

         return this.jj_scan_token(78);
      }
   }

   private final boolean jj_3R_119() {
      if (this.jj_scan_token(12)) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_scan_token(69)) {
            this.jj_scanpos = var1;
         }

         return this.jj_scan_token(78);
      }
   }

   private final boolean jj_3R_195() {
      return this.jj_3R_205();
   }

   private final boolean jj_3R_128() {
      if (this.jj_scan_token(34)) {
         return true;
      } else if (this.jj_scan_token(104)) {
         return true;
      } else {
         return this.jj_scan_token(78);
      }
   }

   private final boolean jj_3R_133() {
      if (this.jj_scan_token(74)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else {
         return this.jj_scan_token(75);
      }
   }

   private final boolean jj_3R_205() {
      if (this.jj_3R_112()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_210());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_132() {
      if (this.jj_scan_token(80)) {
         return true;
      } else if (this.jj_scan_token(69)) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_146()) {
            this.jj_scanpos = var1;
         }

         return false;
      }
   }

   private final boolean jj_3_3() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(48)) {
         this.jj_scanpos = var1;
      }

      if (this.jj_scan_token(34)) {
         return true;
      } else if (this.jj_3R_29()) {
         return true;
      } else {
         var1 = this.jj_scanpos;
         if (this.jj_3R_30()) {
            this.jj_scanpos = var1;
         }

         return this.jj_scan_token(78);
      }
   }

   private final boolean jj_3R_94() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3_3()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_128()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_93() {
      if (this.jj_3R_41()) {
         return true;
      } else if (this.jj_3R_32()) {
         return true;
      } else if (this.jj_3R_176()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_177());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_131() {
      if (this.jj_scan_token(76)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else {
         return this.jj_scan_token(77);
      }
   }

   private final boolean jj_3R_95() {
      if (this.jj_scan_token(42)) {
         return true;
      } else {
         return this.jj_3R_29();
      }
   }

   private final boolean jj_3_2() {
      if (this.jj_scan_token(69)) {
         return true;
      } else {
         return this.jj_scan_token(72);
      }
   }

   private final boolean jj_3R_175() {
      return this.jj_3R_38();
   }

   private final boolean jj_3_16() {
      if (this.jj_scan_token(80)) {
         return true;
      } else {
         return this.jj_scan_token(13);
      }
   }

   private final boolean jj_3R_104() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3_16()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_131()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_132()) {
               this.jj_scanpos = var1;
               if (this.jj_3R_133()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private final boolean jj_3R_174() {
      if (this.jj_scan_token(54)) {
         return true;
      } else {
         return this.jj_3R_76();
      }
   }

   private final boolean jj_3_15() {
      if (this.jj_3R_32()) {
         return true;
      } else if (this.jj_scan_token(80)) {
         return true;
      } else {
         return this.jj_scan_token(13);
      }
   }

   private final boolean jj_3_31() {
      if (this.jj_3R_41()) {
         return true;
      } else if (this.jj_3R_32()) {
         return true;
      } else {
         return this.jj_scan_token(69);
      }
   }

   private final boolean jj_3_14() {
      return this.jj_3R_37();
   }

   private final boolean jj_3R_126() {
      return this.jj_scan_token(69);
   }

   private final boolean jj_3R_127() {
      if (this.jj_3R_42()) {
         return true;
      } else {
         return this.jj_scan_token(69);
      }
   }

   private final boolean jj_3R_92() {
      if (this.jj_3R_41()) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_126()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_127()) {
               return true;
            }
         }

         if (this.jj_3R_43()) {
            return true;
         } else {
            var1 = this.jj_scanpos;
            if (this.jj_3R_174()) {
               this.jj_scanpos = var1;
            }

            var1 = this.jj_scanpos;
            if (this.jj_3R_175()) {
               this.jj_scanpos = var1;
               if (this.jj_scan_token(78)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   private final boolean jj_3R_204() {
      return this.jj_3R_205();
   }

   private final boolean jj_3R_103() {
      return this.jj_3R_29();
   }

   private final boolean jj_3R_203() {
      return this.jj_3R_93();
   }

   private final boolean jj_3R_194() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_203()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_204()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_102() {
      return this.jj_3R_32();
   }

   private final boolean jj_3R_58() {
      return this.jj_3R_104();
   }

   private final boolean jj_3R_125() {
      return this.jj_scan_token(37);
   }

   private final boolean jj_3R_101() {
      return this.jj_3R_37();
   }

   private final boolean jj_3R_100() {
      return this.jj_3R_130();
   }

   private final boolean jj_3R_99() {
      if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else {
         return this.jj_scan_token(73);
      }
   }

   private final boolean jj_3R_137() {
      if (this.jj_scan_token(30)) {
         return true;
      } else if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_32()) {
         return true;
      } else if (this.jj_scan_token(69)) {
         return true;
      } else if (this.jj_scan_token(89)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else if (this.jj_scan_token(73)) {
         return true;
      } else {
         return this.jj_3R_45();
      }
   }

   private final boolean jj_3R_184() {
      if (this.jj_scan_token(23)) {
         return true;
      } else {
         return this.jj_3R_45();
      }
   }

   private final boolean jj_3R_173() {
      if (this.jj_scan_token(33)) {
         return true;
      } else {
         return this.jj_3R_76();
      }
   }

   private final boolean jj_3R_57() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_98()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_99()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_100()) {
               this.jj_scanpos = var1;
               if (this.jj_3R_101()) {
                  this.jj_scanpos = var1;
                  if (this.jj_3R_102()) {
                     this.jj_scanpos = var1;
                     if (this.jj_3R_103()) {
                        return true;
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private final boolean jj_3R_98() {
      return this.jj_3R_129();
   }

   private final boolean jj_3R_172() {
      if (this.jj_scan_token(25)) {
         return true;
      } else {
         return this.jj_3R_29();
      }
   }

   private final boolean jj_3_30() {
      if (this.jj_scan_token(30)) {
         return true;
      } else if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_scan_token(69)) {
         return true;
      } else if (this.jj_scan_token(89)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else if (this.jj_scan_token(73)) {
         return true;
      } else {
         return this.jj_3R_45();
      }
   }

   private final boolean jj_3R_118() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3_30()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_137()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_37() {
      if (this.jj_3R_29()) {
         return true;
      } else {
         return this.jj_3R_69();
      }
   }

   private final boolean jj_3R_185() {
      return this.jj_3R_194();
   }

   private final boolean jj_3R_91() {
      if (this.jj_3R_41()) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_scan_token(13)) {
            this.jj_scanpos = var1;
            if (this.jj_3R_125()) {
               return true;
            }
         }

         if (this.jj_scan_token(69)) {
            return true;
         } else {
            var1 = this.jj_scanpos;
            if (this.jj_3R_172()) {
               this.jj_scanpos = var1;
            }

            var1 = this.jj_scanpos;
            if (this.jj_3R_173()) {
               this.jj_scanpos = var1;
            }

            return this.jj_3R_38();
         }
      }
   }

   private final boolean jj_3_13() {
      if (this.jj_scan_token(72)) {
         return true;
      } else {
         return this.jj_3R_36();
      }
   }

   private final boolean jj_3R_187() {
      return this.jj_3R_195();
   }

   private final boolean jj_3R_186() {
      return this.jj_3R_39();
   }

   private final boolean jj_3R_33() {
      if (this.jj_3R_57()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_58());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_217() {
      if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_32()) {
         return true;
      } else if (this.jj_scan_token(73)) {
         return true;
      } else {
         return this.jj_3R_208();
      }
   }

   private final boolean jj_3R_216() {
      if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_32()) {
         return true;
      } else if (this.jj_scan_token(73)) {
         return true;
      } else {
         return this.jj_3R_191();
      }
   }

   private final boolean jj_3R_117() {
      if (this.jj_scan_token(30)) {
         return true;
      } else if (this.jj_scan_token(72)) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_185()) {
            this.jj_scanpos = var1;
         }

         if (this.jj_scan_token(78)) {
            return true;
         } else {
            var1 = this.jj_scanpos;
            if (this.jj_3R_186()) {
               this.jj_scanpos = var1;
            }

            if (this.jj_scan_token(78)) {
               return true;
            } else {
               var1 = this.jj_scanpos;
               if (this.jj_3R_187()) {
                  this.jj_scanpos = var1;
               }

               if (this.jj_scan_token(73)) {
                  return true;
               } else {
                  return this.jj_3R_45();
               }
            }
         }
      }
   }

   private final boolean jj_3R_214() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_216()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_217()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3_12() {
      if (this.jj_3R_33()) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_scan_token(100)) {
            this.jj_scanpos = var1;
            if (this.jj_scan_token(101)) {
               return true;
            }
         }

         return false;
      }
   }

   private final boolean jj_3R_219() {
      return this.jj_3R_33();
   }

   private final boolean jj_3R_116() {
      if (this.jj_scan_token(21)) {
         return true;
      } else if (this.jj_3R_45()) {
         return true;
      } else if (this.jj_scan_token(59)) {
         return true;
      } else if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else if (this.jj_scan_token(73)) {
         return true;
      } else {
         return this.jj_scan_token(78);
      }
   }

   private final boolean jj_3_11() {
      if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_29()) {
         return true;
      } else {
         return this.jj_scan_token(76);
      }
   }

   private final boolean jj_3R_218() {
      if (this.jj_3R_33()) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_scan_token(100)) {
            this.jj_scanpos = var1;
            if (this.jj_scan_token(101)) {
               return true;
            }
         }

         return false;
      }
   }

   private final boolean jj_3R_215() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_218()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_219()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_72() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(43)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(44)) {
            this.jj_scanpos = var1;
            if (this.jj_scan_token(45)) {
               this.jj_scanpos = var1;
               if (this.jj_scan_token(51)) {
                  this.jj_scanpos = var1;
                  if (this.jj_scan_token(27)) {
                     this.jj_scanpos = var1;
                     if (this.jj_scan_token(39)) {
                        this.jj_scanpos = var1;
                        if (this.jj_scan_token(52)) {
                           this.jj_scanpos = var1;
                           if (this.jj_scan_token(58)) {
                              this.jj_scanpos = var1;
                              if (this.jj_scan_token(10)) {
                                 this.jj_scanpos = var1;
                                 if (this.jj_scan_token(48)) {
                                    this.jj_scanpos = var1;
                                    if (this.jj_scan_token(49)) {
                                       return true;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private final boolean jj_3R_115() {
      if (this.jj_scan_token(59)) {
         return true;
      } else if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else if (this.jj_scan_token(73)) {
         return true;
      } else {
         return this.jj_3R_45();
      }
   }

   private final boolean jj_3R_60() {
      if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_29()) {
         return true;
      } else if (this.jj_scan_token(73)) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_scan_token(87)) {
            this.jj_scanpos = var1;
            if (this.jj_scan_token(86)) {
               this.jj_scanpos = var1;
               if (this.jj_scan_token(72)) {
                  this.jj_scanpos = var1;
                  if (this.jj_scan_token(69)) {
                     this.jj_scanpos = var1;
                     if (this.jj_scan_token(40)) {
                        this.jj_scanpos = var1;
                        if (this.jj_3R_105()) {
                           return true;
                        }
                     }
                  }
               }
            }
         }

         return false;
      }
   }

   private final boolean jj_3R_59() {
      if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_29()) {
         return true;
      } else if (this.jj_scan_token(76)) {
         return true;
      } else {
         return this.jj_scan_token(77);
      }
   }

   private final boolean jj_3_9() {
      return this.jj_3R_35();
   }

   private final boolean jj_3_29() {
      return this.jj_3R_28();
   }

   private final boolean jj_3R_114() {
      if (this.jj_scan_token(32)) {
         return true;
      } else if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else if (this.jj_scan_token(73)) {
         return true;
      } else if (this.jj_3R_45()) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_184()) {
            this.jj_scanpos = var1;
         }

         return false;
      }
   }

   private final boolean jj_3R_41() {
      Token var1;
      do {
         var1 = this.jj_scanpos;
      } while(!this.jj_3R_72());

      this.jj_scanpos = var1;
      return false;
   }

   private final boolean jj_3R_35() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3_10()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_59()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_60()) {
               return true;
            }
         }
      }

      return false;
   }

   private final boolean jj_3_10() {
      if (this.jj_scan_token(72)) {
         return true;
      } else {
         return this.jj_3R_36();
      }
   }

   private final boolean jj_3R_213() {
      return this.jj_3R_215();
   }

   private final boolean jj_3R_212() {
      return this.jj_3R_214();
   }

   private final boolean jj_3R_202() {
      if (this.jj_scan_token(20)) {
         return true;
      } else {
         return this.jj_scan_token(89);
      }
   }

   private final boolean jj_3R_211() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(87)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(86)) {
            return true;
         }
      }

      return this.jj_3R_191();
   }

   private final boolean jj_3R_208() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_211()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_212()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_213()) {
               return true;
            }
         }
      }

      return false;
   }

   private final boolean jj_3R_201() {
      if (this.jj_scan_token(15)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else {
         return this.jj_scan_token(89);
      }
   }

   private final boolean jj_3R_193() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_201()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_202()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_183() {
      if (this.jj_3R_193()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3_29());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_207() {
      if (this.jj_scan_token(101)) {
         return true;
      } else {
         return this.jj_3R_33();
      }
   }

   private final boolean jj_3_1() {
      return this.jj_3R_28();
   }

   private final boolean jj_3R_113() {
      if (this.jj_scan_token(50)) {
         return true;
      } else if (this.jj_scan_token(72)) {
         return true;
      } else if (this.jj_3R_39()) {
         return true;
      } else if (this.jj_scan_token(73)) {
         return true;
      } else if (this.jj_scan_token(74)) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_183());

         this.jj_scanpos = var1;
         return this.jj_scan_token(75);
      }
   }

   private final boolean jj_3R_209() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(104)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(105)) {
            this.jj_scanpos = var1;
            if (this.jj_scan_token(111)) {
               return true;
            }
         }
      }

      return this.jj_3R_191();
   }

   private final boolean jj_3R_206() {
      if (this.jj_scan_token(100)) {
         return true;
      } else {
         return this.jj_3R_33();
      }
   }

   private final boolean jj_3R_199() {
      return this.jj_3R_208();
   }

   private final boolean jj_3R_198() {
      return this.jj_3R_207();
   }

   private final boolean jj_3R_197() {
      return this.jj_3R_206();
   }

   private final boolean jj_3R_196() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(102)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(103)) {
            return true;
         }
      }

      return this.jj_3R_191();
   }

   private final boolean jj_3R_191() {
      Token var1 = this.jj_scanpos;
      if (this.jj_3R_196()) {
         this.jj_scanpos = var1;
         if (this.jj_3R_197()) {
            this.jj_scanpos = var1;
            if (this.jj_3R_198()) {
               this.jj_scanpos = var1;
               if (this.jj_3R_199()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private final boolean jj_3R_44() {
      if (this.jj_scan_token(54)) {
         return true;
      } else {
         return this.jj_3R_76();
      }
   }

   private final boolean jj_3R_112() {
      return this.jj_3R_39();
   }

   private final boolean jj_3R_181() {
      if (this.jj_3R_191()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_209());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_200() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(102)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(103)) {
            return true;
         }
      }

      return this.jj_3R_181();
   }

   private final boolean jj_3R_178() {
      if (this.jj_3R_181()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_200());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_96() {
      return this.jj_scan_token(68);
   }

   private final boolean jj_3R_192() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(112)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(113)) {
            this.jj_scanpos = var1;
            if (this.jj_scan_token(114)) {
               this.jj_scanpos = var1;
               if (this.jj_scan_token(115)) {
                  this.jj_scanpos = var1;
                  if (this.jj_scan_token(116)) {
                     this.jj_scanpos = var1;
                     if (this.jj_scan_token(117)) {
                        return true;
                     }
                  }
               }
            }
         }
      }

      return this.jj_3R_178();
   }

   private final boolean jj_3R_171() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(90)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(95)) {
            return true;
         }
      }

      return this.jj_3R_166();
   }

   private final boolean jj_3R_170() {
      if (this.jj_3R_178()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_192());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_52() {
      return this.jj_3R_96();
   }

   private final boolean jj_3R_182() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(84)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(85)) {
            this.jj_scanpos = var1;
            if (this.jj_scan_token(82)) {
               this.jj_scanpos = var1;
               if (this.jj_scan_token(83)) {
                  this.jj_scanpos = var1;
                  if (this.jj_scan_token(91)) {
                     this.jj_scanpos = var1;
                     if (this.jj_scan_token(92)) {
                        this.jj_scanpos = var1;
                        if (this.jj_scan_token(93)) {
                           this.jj_scanpos = var1;
                           if (this.jj_scan_token(94)) {
                              return true;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return this.jj_3R_170();
   }

   private final boolean jj_3_27() {
      if (this.jj_3R_41()) {
         return true;
      } else if (this.jj_3R_32()) {
         return true;
      } else {
         return this.jj_scan_token(69);
      }
   }

   private final boolean jj_3R_51() {
      return this.jj_3R_95();
   }

   private final boolean jj_3R_168() {
      if (this.jj_3R_170()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_182());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_50() {
      return this.jj_3R_94();
   }

   private final boolean jj_3_26() {
      if (this.jj_3R_41()) {
         return true;
      } else if (this.jj_scan_token(69)) {
         return true;
      } else if (this.jj_3R_43()) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_44()) {
            this.jj_scanpos = var1;
         }

         return this.jj_scan_token(74);
      }
   }

   private final boolean jj_3R_179() {
      if (this.jj_scan_token(35)) {
         return true;
      } else {
         return this.jj_3R_32();
      }
   }

   private final boolean jj_3_28() {
      return this.jj_3R_45();
   }

   private final boolean jj_3R_166() {
      if (this.jj_3R_168()) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_3R_179()) {
            this.jj_scanpos = var1;
         }

         return false;
      }
   }

   private final boolean jj_3_25() {
      if (this.jj_3R_41()) {
         return true;
      } else if (this.jj_3R_42()) {
         return true;
      } else if (this.jj_scan_token(69)) {
         return true;
      } else {
         return this.jj_scan_token(72);
      }
   }

   private final boolean jj_3R_49() {
      if (this.jj_3R_93()) {
         return true;
      } else {
         return this.jj_scan_token(78);
      }
   }

   private final boolean jj_3_24() {
      if (this.jj_3R_41()) {
         return true;
      } else {
         Token var1 = this.jj_scanpos;
         if (this.jj_scan_token(13)) {
            this.jj_scanpos = var1;
            if (this.jj_scan_token(37)) {
               return true;
            }
         }

         return false;
      }
   }

   private final boolean jj_3R_167() {
      if (this.jj_scan_token(110)) {
         return true;
      } else {
         return this.jj_3R_161();
      }
   }

   private final boolean jj_3R_48() {
      return this.jj_3R_92();
   }

   private final boolean jj_3R_164() {
      if (this.jj_3R_166()) {
         return true;
      } else {
         Token var1;
         do {
            var1 = this.jj_scanpos;
         } while(!this.jj_3R_171());

         this.jj_scanpos = var1;
         return false;
      }
   }

   private final boolean jj_3R_47() {
      return this.jj_3R_92();
   }

   private final boolean jj_3R_169() {
      Token var1 = this.jj_scanpos;
      if (this.jj_scan_token(106)) {
         this.jj_scanpos = var1;
         if (this.jj_scan_token(107)) {
            return true;
         }
      }

      return this.jj_3R_164();
   }

   public Parser(InputStream var1) {
      this.jj_input_stream = new JavaCharStream(var1, 1, 1);
      this.token_source = new ParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
   }

   public void ReInit(InputStream var1) {
      this.jj_input_stream.ReInit((InputStream)var1, 1, 1);
      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jjtree.reset();
   }

   public Parser(Reader var1) {
      this.jj_input_stream = new JavaCharStream(var1, 1, 1);
      this.token_source = new ParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
   }

   public void ReInit(Reader var1) {
      this.jj_input_stream.ReInit((Reader)var1, 1, 1);
      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jjtree.reset();
   }

   public Parser(ParserTokenManager var1) {
      this.token_source = var1;
      this.token = new Token();
      this.jj_ntk = -1;
   }

   public void ReInit(ParserTokenManager var1) {
      this.token_source = var1;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jjtree.reset();
   }

   private final Token jj_consume_token(int var1) throws ParseException {
      Token var2;
      if ((var2 = this.token).next != null) {
         this.token = this.token.next;
      } else {
         this.token = this.token.next = this.token_source.getNextToken();
      }

      this.jj_ntk = -1;
      if (this.token.kind == var1) {
         return this.token;
      } else {
         this.token = var2;
         throw this.generateParseException();
      }
   }

   private final boolean jj_scan_token(int var1) {
      if (this.jj_scanpos == this.jj_lastpos) {
         --this.jj_la;
         if (this.jj_scanpos.next == null) {
            this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken();
         } else {
            this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next;
         }
      } else {
         this.jj_scanpos = this.jj_scanpos.next;
      }

      if (this.jj_scanpos.kind != var1) {
         return true;
      } else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) {
         throw this.jj_ls;
      } else {
         return false;
      }
   }

   public final Token getNextToken() {
      if (this.token.next != null) {
         this.token = this.token.next;
      } else {
         this.token = this.token.next = this.token_source.getNextToken();
      }

      this.jj_ntk = -1;
      return this.token;
   }

   public final Token getToken(int var1) {
      Token var2 = this.lookingAhead ? this.jj_scanpos : this.token;

      for(int var3 = 0; var3 < var1; ++var3) {
         if (var2.next != null) {
            var2 = var2.next;
         } else {
            var2 = var2.next = this.token_source.getNextToken();
         }
      }

      return var2;
   }

   private final int jj_ntk() {
      return (this.jj_nt = this.token.next) == null ? (this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind) : (this.jj_ntk = this.jj_nt.kind);
   }

   public ParseException generateParseException() {
      Token var1 = this.token.next;
      int var2 = var1.beginLine;
      int var3 = var1.beginColumn;
      String var4 = var1.kind == 0 ? ParserConstants.tokenImage[0] : var1.image;
      return new ParseException("Parse error at line " + var2 + ", column " + var3 + ".  Encountered: " + var4);
   }

   public final void enable_tracing() {
   }

   public final void disable_tracing() {
   }

   private static final class LookaheadSuccess extends Error {
      private LookaheadSuccess() {
      }

      // $FF: synthetic method
      LookaheadSuccess(Object var1) {
         this();
      }
   }
}
