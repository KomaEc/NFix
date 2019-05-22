package org.apache.velocity.runtime.parser;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.Macro;
import org.apache.velocity.runtime.directive.MacroParseException;
import org.apache.velocity.runtime.parser.node.ASTAddNode;
import org.apache.velocity.runtime.parser.node.ASTAndNode;
import org.apache.velocity.runtime.parser.node.ASTAssignment;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.ASTComment;
import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.ASTDivNode;
import org.apache.velocity.runtime.parser.node.ASTEQNode;
import org.apache.velocity.runtime.parser.node.ASTElseIfStatement;
import org.apache.velocity.runtime.parser.node.ASTElseStatement;
import org.apache.velocity.runtime.parser.node.ASTEscape;
import org.apache.velocity.runtime.parser.node.ASTEscapedDirective;
import org.apache.velocity.runtime.parser.node.ASTExpression;
import org.apache.velocity.runtime.parser.node.ASTFalse;
import org.apache.velocity.runtime.parser.node.ASTFloatingPointLiteral;
import org.apache.velocity.runtime.parser.node.ASTGENode;
import org.apache.velocity.runtime.parser.node.ASTGTNode;
import org.apache.velocity.runtime.parser.node.ASTIdentifier;
import org.apache.velocity.runtime.parser.node.ASTIfStatement;
import org.apache.velocity.runtime.parser.node.ASTIntegerLiteral;
import org.apache.velocity.runtime.parser.node.ASTIntegerRange;
import org.apache.velocity.runtime.parser.node.ASTLENode;
import org.apache.velocity.runtime.parser.node.ASTLTNode;
import org.apache.velocity.runtime.parser.node.ASTMap;
import org.apache.velocity.runtime.parser.node.ASTMethod;
import org.apache.velocity.runtime.parser.node.ASTModNode;
import org.apache.velocity.runtime.parser.node.ASTMulNode;
import org.apache.velocity.runtime.parser.node.ASTNENode;
import org.apache.velocity.runtime.parser.node.ASTNotNode;
import org.apache.velocity.runtime.parser.node.ASTObjectArray;
import org.apache.velocity.runtime.parser.node.ASTOrNode;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.ASTSetDirective;
import org.apache.velocity.runtime.parser.node.ASTStop;
import org.apache.velocity.runtime.parser.node.ASTStringLiteral;
import org.apache.velocity.runtime.parser.node.ASTSubtractNode;
import org.apache.velocity.runtime.parser.node.ASTText;
import org.apache.velocity.runtime.parser.node.ASTTrue;
import org.apache.velocity.runtime.parser.node.ASTWord;
import org.apache.velocity.runtime.parser.node.ASTprocess;
import org.apache.velocity.runtime.parser.node.SimpleNode;

public class Parser implements ParserTreeConstants, ParserConstants {
   protected JJTParserState jjtree;
   private Hashtable directives;
   String currentTemplateName;
   VelocityCharStream velcharstream;
   private RuntimeServices rsvc;
   public ParserTokenManager token_source;
   public Token token;
   public Token jj_nt;
   private int jj_ntk;
   private Token jj_scanpos;
   private Token jj_lastpos;
   private int jj_la;
   public boolean lookingAhead;
   private boolean jj_semLA;
   private int jj_gen;
   private final int[] jj_la1;
   private static int[] jj_la1_0;
   private static int[] jj_la1_1;
   private static int[] jj_la1_2;
   private final Parser.JJCalls[] jj_2_rtns;
   private boolean jj_rescan;
   private int jj_gc;
   private final Parser.LookaheadSuccess jj_ls;
   private Vector jj_expentries;
   private int[] jj_expentry;
   private int jj_kind;
   private int[] jj_lasttokens;
   private int jj_endpos;

   public Parser(RuntimeServices rs) {
      this((CharStream)(new VelocityCharStream(new ByteArrayInputStream("\n".getBytes()), 1, 1)));
      this.velcharstream = new VelocityCharStream(new ByteArrayInputStream("\n".getBytes()), 1, 1);
      this.rsvc = rs;
   }

   public SimpleNode parse(Reader reader, String templateName) throws ParseException {
      SimpleNode sn = null;
      this.currentTemplateName = templateName;

      try {
         this.token_source.clearStateVars();
         this.velcharstream.ReInit((Reader)reader, 1, 1);
         this.ReInit((CharStream)this.velcharstream);
         sn = this.process();
      } catch (MacroParseException var5) {
         this.rsvc.getLog().error("Parser Error: #macro() : " + templateName, var5);
         throw var5;
      } catch (ParseException var6) {
         this.rsvc.getLog().error("Parser Exception: " + templateName, var6);
         throw new TemplateParseException(var6.currentToken, var6.expectedTokenSequences, var6.tokenImage, this.currentTemplateName);
      } catch (TokenMgrError var7) {
         throw new ParseException("Lexical error: " + var7.toString());
      } catch (Exception var8) {
         this.rsvc.getLog().error("Parser Error: " + templateName, var8);
      }

      this.currentTemplateName = "";
      return sn;
   }

   public void setDirectives(Hashtable directives) {
      this.directives = directives;
   }

   public Directive getDirective(String directive) {
      return (Directive)this.directives.get(directive);
   }

   public boolean isDirective(String directive) {
      return this.directives.containsKey(directive);
   }

   private String escapedDirective(String strImage) {
      int iLast = strImage.lastIndexOf("\\");
      String strDirective = strImage.substring(iLast + 1);
      boolean bRecognizedDirective = false;
      if (this.isDirective(strDirective.substring(1))) {
         bRecognizedDirective = true;
      } else if (this.rsvc.isVelocimacro(strDirective.substring(1), this.currentTemplateName)) {
         bRecognizedDirective = true;
      } else if (strDirective.substring(1).equals("if") || strDirective.substring(1).equals("end") || strDirective.substring(1).equals("set") || strDirective.substring(1).equals("else") || strDirective.substring(1).equals("elseif") || strDirective.substring(1).equals("stop")) {
         bRecognizedDirective = true;
      }

      return bRecognizedDirective ? strImage.substring(0, iLast / 2) + strDirective : strImage;
   }

   public final SimpleNode process() throws ParseException {
      ASTprocess jjtn000 = new ASTprocess(this, 0);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         while(true) {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 8:
            case 9:
            case 11:
            case 12:
            case 18:
            case 19:
            case 20:
            case 21:
            case 23:
            case 24:
            case 27:
            case 47:
            case 50:
            case 52:
            case 53:
            case 57:
            case 58:
            case 62:
            case 63:
            case 64:
            case 65:
               this.Statement();
               break;
            case 10:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 22:
            case 25:
            case 26:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 48:
            case 49:
            case 51:
            case 54:
            case 55:
            case 56:
            case 59:
            case 60:
            case 61:
            default:
               this.jj_la1[0] = this.jj_gen;
               this.jj_consume_token(0);
               this.jjtree.closeNodeScope(jjtn000, true);
               jjtc000 = false;
               ASTprocess var3 = jjtn000;
               return var3;
            }
         }
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         } else {
            throw (Error)var7;
         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }
   }

   public final void Statement() throws ParseException {
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 47:
         this.IfStatement();
         break;
      case 50:
         this.StopStatement();
         break;
      default:
         this.jj_la1[1] = this.jj_gen;
         if (this.jj_2_1(2)) {
            this.Reference();
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 8:
            case 9:
            case 20:
            case 21:
            case 27:
            case 52:
            case 53:
            case 63:
            case 64:
            case 65:
               this.Text();
               break;
            case 10:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 22:
            case 25:
            case 26:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 54:
            case 55:
            case 56:
            case 59:
            case 60:
            case 61:
            case 62:
            default:
               this.jj_la1[2] = this.jj_gen;
               this.jj_consume_token(-1);
               throw new ParseException();
            case 11:
               this.EscapedDirective();
               break;
            case 12:
               this.SetDirective();
               break;
            case 18:
            case 23:
            case 24:
               this.Comment();
               break;
            case 19:
               this.Escape();
               break;
            case 57:
            case 58:
               this.Directive();
            }
         }
      }

   }

   public final void EscapedDirective() throws ParseException {
      ASTEscapedDirective jjtn000 = new ASTEscapedDirective(this, 2);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         Token t = null;
         t = this.jj_consume_token(11);
         this.jjtree.closeNodeScope(jjtn000, true);
         jjtc000 = false;
         t.image = this.escapedDirective(t.image);
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void Escape() throws ParseException {
      ASTEscape jjtn000 = new ASTEscape(this, 3);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         Token t = null;
         int count = 0;
         boolean control = false;

         do {
            t = this.jj_consume_token(19);
            ++count;
         } while(this.jj_2_2(2));

         this.jjtree.closeNodeScope(jjtn000, true);
         jjtc000 = false;
         switch(t.next.kind) {
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
            control = true;
         default:
            if (this.isDirective(t.next.image.substring(1))) {
               control = true;
            } else if (this.rsvc.isVelocimacro(t.next.image.substring(1), this.currentTemplateName)) {
               control = true;
            }

            jjtn000.val = "";

            for(int i = 0; i < count; ++i) {
               jjtn000.val = jjtn000.val + (control ? "\\" : "\\\\");
            }

         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }
   }

   public final void Comment() throws ParseException {
      ASTComment jjtn000 = new ASTComment(this, 4);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 18:
            this.jj_consume_token(18);
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 22:
               this.jj_consume_token(22);
               return;
            default:
               this.jj_la1[3] = this.jj_gen;
               return;
            }
         case 23:
            this.jj_consume_token(23);
            break;
         case 24:
            this.jj_consume_token(24);
            break;
         default:
            this.jj_la1[4] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void FloatingPointLiteral() throws ParseException {
      ASTFloatingPointLiteral jjtn000 = new ASTFloatingPointLiteral(this, 5);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(53);
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void IntegerLiteral() throws ParseException {
      ASTIntegerLiteral jjtn000 = new ASTIntegerLiteral(this, 6);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(52);
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void StringLiteral() throws ParseException {
      ASTStringLiteral jjtn000 = new ASTStringLiteral(this, 7);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(27);
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void Identifier() throws ParseException {
      ASTIdentifier jjtn000 = new ASTIdentifier(this, 8);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(62);
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void Word() throws ParseException {
      ASTWord jjtn000 = new ASTWord(this, 9);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(57);
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final int DirectiveArg() throws ParseException {
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 27:
         this.StringLiteral();
         return 7;
      case 52:
         this.IntegerLiteral();
         return 6;
      case 57:
         this.Word();
         return 9;
      case 62:
      case 64:
         this.Reference();
         return 16;
      default:
         this.jj_la1[5] = this.jj_gen;
         if (this.jj_2_3(Integer.MAX_VALUE)) {
            this.IntegerRange();
            return 14;
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 1:
               this.ObjectArray();
               return 13;
            case 6:
               this.Map();
               return 12;
            case 28:
               this.True();
               return 17;
            case 29:
               this.False();
               return 18;
            case 53:
               this.FloatingPointLiteral();
               return 5;
            default:
               this.jj_la1[6] = this.jj_gen;
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }
      }
   }

   public final SimpleNode Directive() throws ParseException {
      ASTDirective jjtn000 = new ASTDirective(this, 10);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);
      Token t = null;
      int argPos = false;
      boolean isVM = false;
      boolean doItNow = false;

      ASTDirective var30;
      try {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 57:
            t = this.jj_consume_token(57);
            break;
         case 58:
            t = this.jj_consume_token(58);
            break;
         default:
            this.jj_la1[7] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         }

         String directiveName;
         if (t.kind == 58) {
            directiveName = t.image.substring(2, t.image.length() - 1);
         } else {
            directiveName = t.image.substring(1);
         }

         Directive d = (Directive)this.directives.get(directiveName);
         if (directiveName.equals("macro")) {
            doItNow = true;
         }

         jjtn000.setDirectiveName(directiveName);
         ASTDirective var9;
         int directiveType;
         if (d == null) {
            isVM = this.rsvc.isVelocimacro(directiveName, this.currentTemplateName);
            if (!isVM) {
               this.token_source.stateStackPop();
               this.token_source.inDirective = false;
               var9 = jjtn000;
               return var9;
            }

            directiveType = 2;
         } else {
            directiveType = d.getType();
         }

         this.token_source.SwitchTo(0);
         int argPos = 0;
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 26:
            this.jj_consume_token(26);
            break;
         default:
            this.jj_la1[8] = this.jj_gen;
         }

         this.jj_consume_token(8);

         label653:
         while(true) {
            if (!this.jj_2_4(2)) {
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 26:
                  this.jj_consume_token(26);
                  break label653;
               default:
                  this.jj_la1[12] = this.jj_gen;
                  break label653;
               }
            }

            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 26:
               this.jj_consume_token(26);
               break;
            default:
               this.jj_la1[9] = this.jj_gen;
            }

            label590:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 3:
               this.jj_consume_token(3);
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 26:
                  this.jj_consume_token(26);
                  break label590;
               default:
                  this.jj_la1[10] = this.jj_gen;
                  break label590;
               }
            default:
               this.jj_la1[11] = this.jj_gen;
            }

            int argType = this.DirectiveArg();
            if (argType == 9) {
               if ((!doItNow || argPos != 0) && (!t.image.equals("#foreach") && !t.image.equals("#{foreach}") || argPos != 1)) {
                  throw new MacroParseException("Invalid arg #" + argPos + " in " + (isVM ? "VM " : "directive ") + t.image, this.currentTemplateName, t);
               }
            } else if (doItNow && argPos == 0) {
               throw new MacroParseException("Invalid first arg in #macro() directive - must be a word token (no ' or \" surrounding)", this.currentTemplateName, t);
            }

            ++argPos;
         }

         this.jj_consume_token(9);
         if (directiveType == 2) {
            var9 = jjtn000;
            return var9;
         }

         ASTBlock jjtn001 = new ASTBlock(this, 11);
         boolean jjtc001 = true;
         this.jjtree.openNodeScope(jjtn001);

         try {
            label664:
            while(true) {
               this.Statement();
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 8:
               case 9:
               case 11:
               case 12:
               case 18:
               case 19:
               case 20:
               case 21:
               case 23:
               case 24:
               case 27:
               case 47:
               case 50:
               case 52:
               case 53:
               case 57:
               case 58:
               case 62:
               case 63:
               case 64:
               case 65:
                  break;
               case 10:
               case 13:
               case 14:
               case 15:
               case 16:
               case 17:
               case 22:
               case 25:
               case 26:
               case 28:
               case 29:
               case 30:
               case 31:
               case 32:
               case 33:
               case 34:
               case 35:
               case 36:
               case 37:
               case 38:
               case 39:
               case 40:
               case 41:
               case 42:
               case 43:
               case 44:
               case 45:
               case 46:
               case 48:
               case 49:
               case 51:
               case 54:
               case 55:
               case 56:
               case 59:
               case 60:
               case 61:
               default:
                  this.jj_la1[13] = this.jj_gen;
                  break label664;
               }
            }
         } catch (Throwable var24) {
            if (jjtc001) {
               this.jjtree.clearNodeScope(jjtn001);
               jjtc001 = false;
            } else {
               this.jjtree.popNode();
            }

            if (var24 instanceof RuntimeException) {
               throw (RuntimeException)var24;
            }

            if (var24 instanceof ParseException) {
               throw (ParseException)var24;
            }

            throw (Error)var24;
         } finally {
            if (jjtc001) {
               this.jjtree.closeNodeScope(jjtn001, true);
            }

         }

         this.jj_consume_token(46);
         this.jjtree.closeNodeScope(jjtn000, true);
         jjtc000 = false;
         if (doItNow) {
            Macro.processAndRegister(this.rsvc, t, jjtn000, this.currentTemplateName);
         }

         var30 = jjtn000;
      } catch (Throwable var26) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var26 instanceof RuntimeException) {
            throw (RuntimeException)var26;
         }

         if (var26 instanceof ParseException) {
            throw (ParseException)var26;
         }

         throw (Error)var26;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

      return var30;
   }

   public final void Map() throws ParseException {
      ASTMap jjtn000 = new ASTMap(this, 12);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(6);
         if (this.jj_2_5(2)) {
            this.Parameter();
            this.jj_consume_token(5);
            this.Parameter();

            label123:
            while(true) {
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 3:
                  this.jj_consume_token(3);
                  this.Parameter();
                  this.jj_consume_token(5);
                  this.Parameter();
                  break;
               default:
                  this.jj_la1[14] = this.jj_gen;
                  break label123;
               }
            }
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 26:
               this.jj_consume_token(26);
               break;
            default:
               this.jj_la1[15] = this.jj_gen;
            }
         }

         this.jj_consume_token(7);
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void ObjectArray() throws ParseException {
      ASTObjectArray jjtn000 = new ASTObjectArray(this, 13);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(1);
         label126:
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 1:
         case 6:
         case 26:
         case 27:
         case 28:
         case 29:
         case 52:
         case 53:
         case 62:
         case 64:
            this.Parameter();

            while(true) {
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 3:
                  this.jj_consume_token(3);
                  this.Parameter();
                  break;
               default:
                  this.jj_la1[16] = this.jj_gen;
                  break label126;
               }
            }
         default:
            this.jj_la1[17] = this.jj_gen;
         }

         this.jj_consume_token(2);
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void IntegerRange() throws ParseException {
      ASTIntegerRange jjtn000 = new ASTIntegerRange(this, 14);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(1);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 26:
            this.jj_consume_token(26);
            break;
         default:
            this.jj_la1[18] = this.jj_gen;
         }

         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 52:
            this.IntegerLiteral();
            break;
         case 62:
         case 64:
            this.Reference();
            break;
         default:
            this.jj_la1[19] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         }

         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 26:
            this.jj_consume_token(26);
            break;
         default:
            this.jj_la1[20] = this.jj_gen;
         }

         this.jj_consume_token(4);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 26:
            this.jj_consume_token(26);
            break;
         default:
            this.jj_la1[21] = this.jj_gen;
         }

         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 52:
            this.IntegerLiteral();
            break;
         case 62:
         case 64:
            this.Reference();
            break;
         default:
            this.jj_la1[22] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         }

         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 26:
            this.jj_consume_token(26);
            break;
         default:
            this.jj_la1[23] = this.jj_gen;
         }

         this.jj_consume_token(2);
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void Parameter() throws ParseException {
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 26:
         this.jj_consume_token(26);
         break;
      default:
         this.jj_la1[24] = this.jj_gen;
      }

      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 27:
         this.StringLiteral();
         break;
      case 52:
         this.IntegerLiteral();
         break;
      default:
         this.jj_la1[25] = this.jj_gen;
         if (this.jj_2_6(Integer.MAX_VALUE)) {
            this.IntegerRange();
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 1:
               this.ObjectArray();
               break;
            case 6:
               this.Map();
               break;
            case 28:
               this.True();
               break;
            case 29:
               this.False();
               break;
            case 53:
               this.FloatingPointLiteral();
               break;
            case 62:
            case 64:
               this.Reference();
               break;
            default:
               this.jj_la1[26] = this.jj_gen;
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }
      }

      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 26:
         this.jj_consume_token(26);
         break;
      default:
         this.jj_la1[27] = this.jj_gen;
      }

   }

   public final void Method() throws ParseException {
      ASTMethod jjtn000 = new ASTMethod(this, 15);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.Identifier();
         this.jj_consume_token(8);
         label126:
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 1:
         case 6:
         case 26:
         case 27:
         case 28:
         case 29:
         case 52:
         case 53:
         case 62:
         case 64:
            this.Parameter();

            while(true) {
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 3:
                  this.jj_consume_token(3);
                  this.Parameter();
                  break;
               default:
                  this.jj_la1[28] = this.jj_gen;
                  break label126;
               }
            }
         default:
            this.jj_la1[29] = this.jj_gen;
         }

         this.jj_consume_token(10);
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void Reference() throws ParseException {
      ASTReference jjtn000 = new ASTReference(this, 16);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 62:
            this.jj_consume_token(62);

            while(this.jj_2_7(2)) {
               this.jj_consume_token(63);
               if (this.jj_2_8(3)) {
                  this.Method();
               } else {
                  switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 62:
                     this.Identifier();
                     break;
                  default:
                     this.jj_la1[30] = this.jj_gen;
                     this.jj_consume_token(-1);
                     throw new ParseException();
                  }
               }
            }

            return;
         case 64:
            this.jj_consume_token(64);
            this.jj_consume_token(62);

            while(this.jj_2_9(2)) {
               this.jj_consume_token(63);
               if (this.jj_2_10(3)) {
                  this.Method();
               } else {
                  switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 62:
                     this.Identifier();
                     break;
                  default:
                     this.jj_la1[31] = this.jj_gen;
                     this.jj_consume_token(-1);
                     throw new ParseException();
                  }
               }
            }

            this.jj_consume_token(65);
            return;
         default:
            this.jj_la1[32] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         } else {
            throw (Error)var7;
         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }
   }

   public final void True() throws ParseException {
      ASTTrue jjtn000 = new ASTTrue(this, 17);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(28);
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void False() throws ParseException {
      ASTFalse jjtn000 = new ASTFalse(this, 18);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(29);
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void Text() throws ParseException {
      ASTText jjtn000 = new ASTText(this, 19);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 8:
            this.jj_consume_token(8);
            break;
         case 9:
            this.jj_consume_token(9);
            break;
         case 20:
            this.jj_consume_token(20);
            break;
         case 21:
            this.jj_consume_token(21);
            break;
         case 27:
            this.jj_consume_token(27);
            break;
         case 52:
            this.jj_consume_token(52);
            break;
         case 53:
            this.jj_consume_token(53);
            break;
         case 63:
            this.jj_consume_token(63);
            break;
         case 64:
            this.jj_consume_token(64);
            break;
         case 65:
            this.jj_consume_token(65);
            break;
         default:
            this.jj_la1[33] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void IfStatement() throws ParseException {
      ASTIfStatement jjtn000 = new ASTIfStatement(this, 20);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(47);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 26:
            this.jj_consume_token(26);
            break;
         default:
            this.jj_la1[34] = this.jj_gen;
         }

         this.jj_consume_token(8);
         this.Expression();
         this.jj_consume_token(9);
         ASTBlock jjtn001 = new ASTBlock(this, 11);
         boolean jjtc001 = true;
         this.jjtree.openNodeScope(jjtn001);

         try {
            label357:
            while(true) {
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 8:
               case 9:
               case 11:
               case 12:
               case 18:
               case 19:
               case 20:
               case 21:
               case 23:
               case 24:
               case 27:
               case 47:
               case 50:
               case 52:
               case 53:
               case 57:
               case 58:
               case 62:
               case 63:
               case 64:
               case 65:
                  this.Statement();
                  break;
               case 10:
               case 13:
               case 14:
               case 15:
               case 16:
               case 17:
               case 22:
               case 25:
               case 26:
               case 28:
               case 29:
               case 30:
               case 31:
               case 32:
               case 33:
               case 34:
               case 35:
               case 36:
               case 37:
               case 38:
               case 39:
               case 40:
               case 41:
               case 42:
               case 43:
               case 44:
               case 45:
               case 46:
               case 48:
               case 49:
               case 51:
               case 54:
               case 55:
               case 56:
               case 59:
               case 60:
               case 61:
               default:
                  this.jj_la1[35] = this.jj_gen;
                  break label357;
               }
            }
         } catch (Throwable var16) {
            if (jjtc001) {
               this.jjtree.clearNodeScope(jjtn001);
               jjtc001 = false;
            } else {
               this.jjtree.popNode();
            }

            if (var16 instanceof RuntimeException) {
               throw (RuntimeException)var16;
            }

            if (var16 instanceof ParseException) {
               throw (ParseException)var16;
            }

            throw (Error)var16;
         } finally {
            if (jjtc001) {
               this.jjtree.closeNodeScope(jjtn001, true);
            }

         }

         label368:
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 48:
            while(true) {
               this.ElseIfStatement();
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 48:
                  break;
               default:
                  this.jj_la1[36] = this.jj_gen;
                  break label368;
               }
            }
         default:
            this.jj_la1[37] = this.jj_gen;
         }

         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 49:
            this.ElseStatement();
            break;
         default:
            this.jj_la1[38] = this.jj_gen;
         }

         this.jj_consume_token(46);
      } catch (Throwable var18) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
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
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void ElseStatement() throws ParseException {
      ASTElseStatement jjtn000 = new ASTElseStatement(this, 21);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(49);
         ASTBlock jjtn001 = new ASTBlock(this, 11);
         boolean jjtc001 = true;
         this.jjtree.openNodeScope(jjtn001);

         try {
            while(true) {
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 8:
               case 9:
               case 11:
               case 12:
               case 18:
               case 19:
               case 20:
               case 21:
               case 23:
               case 24:
               case 27:
               case 47:
               case 50:
               case 52:
               case 53:
               case 57:
               case 58:
               case 62:
               case 63:
               case 64:
               case 65:
                  this.Statement();
                  break;
               case 10:
               case 13:
               case 14:
               case 15:
               case 16:
               case 17:
               case 22:
               case 25:
               case 26:
               case 28:
               case 29:
               case 30:
               case 31:
               case 32:
               case 33:
               case 34:
               case 35:
               case 36:
               case 37:
               case 38:
               case 39:
               case 40:
               case 41:
               case 42:
               case 43:
               case 44:
               case 45:
               case 46:
               case 48:
               case 49:
               case 51:
               case 54:
               case 55:
               case 56:
               case 59:
               case 60:
               case 61:
               default:
                  this.jj_la1[39] = this.jj_gen;
                  return;
               }
            }
         } catch (Throwable var16) {
            if (jjtc001) {
               this.jjtree.clearNodeScope(jjtn001);
               jjtc001 = false;
            } else {
               this.jjtree.popNode();
            }

            if (var16 instanceof RuntimeException) {
               throw (RuntimeException)var16;
            } else if (var16 instanceof ParseException) {
               throw (ParseException)var16;
            } else {
               throw (Error)var16;
            }
         } finally {
            if (jjtc001) {
               this.jjtree.closeNodeScope(jjtn001, true);
            }

         }
      } catch (Throwable var18) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var18 instanceof RuntimeException) {
            throw (RuntimeException)var18;
         } else if (var18 instanceof ParseException) {
            throw (ParseException)var18;
         } else {
            throw (Error)var18;
         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }
   }

   public final void ElseIfStatement() throws ParseException {
      ASTElseIfStatement jjtn000 = new ASTElseIfStatement(this, 22);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(48);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 26:
            this.jj_consume_token(26);
            break;
         default:
            this.jj_la1[40] = this.jj_gen;
         }

         this.jj_consume_token(8);
         this.Expression();
         this.jj_consume_token(9);
         ASTBlock jjtn001 = new ASTBlock(this, 11);
         boolean jjtc001 = true;
         this.jjtree.openNodeScope(jjtn001);

         try {
            while(true) {
               switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 8:
               case 9:
               case 11:
               case 12:
               case 18:
               case 19:
               case 20:
               case 21:
               case 23:
               case 24:
               case 27:
               case 47:
               case 50:
               case 52:
               case 53:
               case 57:
               case 58:
               case 62:
               case 63:
               case 64:
               case 65:
                  this.Statement();
                  break;
               case 10:
               case 13:
               case 14:
               case 15:
               case 16:
               case 17:
               case 22:
               case 25:
               case 26:
               case 28:
               case 29:
               case 30:
               case 31:
               case 32:
               case 33:
               case 34:
               case 35:
               case 36:
               case 37:
               case 38:
               case 39:
               case 40:
               case 41:
               case 42:
               case 43:
               case 44:
               case 45:
               case 46:
               case 48:
               case 49:
               case 51:
               case 54:
               case 55:
               case 56:
               case 59:
               case 60:
               case 61:
               default:
                  this.jj_la1[41] = this.jj_gen;
                  return;
               }
            }
         } catch (Throwable var16) {
            if (jjtc001) {
               this.jjtree.clearNodeScope(jjtn001);
               jjtc001 = false;
            } else {
               this.jjtree.popNode();
            }

            if (var16 instanceof RuntimeException) {
               throw (RuntimeException)var16;
            } else if (var16 instanceof ParseException) {
               throw (ParseException)var16;
            } else {
               throw (Error)var16;
            }
         } finally {
            if (jjtc001) {
               this.jjtree.closeNodeScope(jjtn001, true);
            }

         }
      } catch (Throwable var18) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var18 instanceof RuntimeException) {
            throw (RuntimeException)var18;
         } else if (var18 instanceof ParseException) {
            throw (ParseException)var18;
         } else {
            throw (Error)var18;
         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }
   }

   public final void SetDirective() throws ParseException {
      ASTSetDirective jjtn000 = new ASTSetDirective(this, 23);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(12);
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 26:
            this.jj_consume_token(26);
            break;
         default:
            this.jj_la1[42] = this.jj_gen;
         }

         this.Reference();
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 26:
            this.jj_consume_token(26);
            break;
         default:
            this.jj_la1[43] = this.jj_gen;
         }

         this.jj_consume_token(45);
         this.Expression();
         this.jj_consume_token(9);
         this.token_source.inSet = false;
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 30:
            this.jj_consume_token(30);
            break;
         default:
            this.jj_la1[44] = this.jj_gen;
         }
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void StopStatement() throws ParseException {
      ASTStop jjtn000 = new ASTStop(this, 24);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.jj_consume_token(50);
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, 0);
         }

      }

   }

   public final void Expression() throws ParseException {
      ASTExpression jjtn000 = new ASTExpression(this, 25);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.ConditionalOrExpression();
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }

      }

   }

   public final void Assignment() throws ParseException {
      ASTAssignment jjtn000 = new ASTAssignment(this, 26);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      try {
         this.PrimaryExpression();
         this.jj_consume_token(45);
         this.Expression();
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, 2);
         }

      }

   }

   public final void ConditionalOrExpression() throws ParseException {
      this.ConditionalAndExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 37:
            this.jj_consume_token(37);
            ASTOrNode jjtn001 = new ASTOrNode(this, 27);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);

            try {
               this.ConditionalAndExpression();
               break;
            } catch (Throwable var7) {
               if (jjtc001) {
                  this.jjtree.clearNodeScope(jjtn001);
                  jjtc001 = false;
               } else {
                  this.jjtree.popNode();
               }

               if (var7 instanceof RuntimeException) {
                  throw (RuntimeException)var7;
               }

               if (var7 instanceof ParseException) {
                  throw (ParseException)var7;
               }

               throw (Error)var7;
            } finally {
               if (jjtc001) {
                  this.jjtree.closeNodeScope(jjtn001, 2);
               }

            }
         default:
            this.jj_la1[45] = this.jj_gen;
            return;
         }
      }
   }

   public final void ConditionalAndExpression() throws ParseException {
      this.EqualityExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 36:
            this.jj_consume_token(36);
            ASTAndNode jjtn001 = new ASTAndNode(this, 28);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);

            try {
               this.EqualityExpression();
               break;
            } catch (Throwable var7) {
               if (jjtc001) {
                  this.jjtree.clearNodeScope(jjtn001);
                  jjtc001 = false;
               } else {
                  this.jjtree.popNode();
               }

               if (var7 instanceof RuntimeException) {
                  throw (RuntimeException)var7;
               }

               if (var7 instanceof ParseException) {
                  throw (ParseException)var7;
               }

               throw (Error)var7;
            } finally {
               if (jjtc001) {
                  this.jjtree.closeNodeScope(jjtn001, 2);
               }

            }
         default:
            this.jj_la1[46] = this.jj_gen;
            return;
         }
      }
   }

   public final void EqualityExpression() throws ParseException {
      this.RelationalExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 42:
         case 43:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 42:
               this.jj_consume_token(42);
               ASTEQNode jjtn001 = new ASTEQNode(this, 29);
               boolean jjtc001 = true;
               this.jjtree.openNodeScope(jjtn001);

               try {
                  this.RelationalExpression();
                  continue;
               } catch (Throwable var15) {
                  if (jjtc001) {
                     this.jjtree.clearNodeScope(jjtn001);
                     jjtc001 = false;
                  } else {
                     this.jjtree.popNode();
                  }

                  if (var15 instanceof RuntimeException) {
                     throw (RuntimeException)var15;
                  }

                  if (var15 instanceof ParseException) {
                     throw (ParseException)var15;
                  }

                  throw (Error)var15;
               } finally {
                  if (jjtc001) {
                     this.jjtree.closeNodeScope(jjtn001, 2);
                  }

               }
            case 43:
               this.jj_consume_token(43);
               ASTNENode jjtn002 = new ASTNENode(this, 30);
               boolean jjtc002 = true;
               this.jjtree.openNodeScope(jjtn002);

               try {
                  this.RelationalExpression();
                  continue;
               } catch (Throwable var17) {
                  if (jjtc002) {
                     this.jjtree.clearNodeScope(jjtn002);
                     jjtc002 = false;
                  } else {
                     this.jjtree.popNode();
                  }

                  if (var17 instanceof RuntimeException) {
                     throw (RuntimeException)var17;
                  }

                  if (var17 instanceof ParseException) {
                     throw (ParseException)var17;
                  }

                  throw (Error)var17;
               } finally {
                  if (jjtc002) {
                     this.jjtree.closeNodeScope(jjtn002, 2);
                  }

               }
            default:
               this.jj_la1[48] = this.jj_gen;
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         default:
            this.jj_la1[47] = this.jj_gen;
            return;
         }
      }
   }

   public final void RelationalExpression() throws ParseException {
      this.AdditiveExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 38:
         case 39:
         case 40:
         case 41:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 38:
               this.jj_consume_token(38);
               ASTLTNode jjtn001 = new ASTLTNode(this, 31);
               boolean jjtc001 = true;
               this.jjtree.openNodeScope(jjtn001);

               try {
                  this.AdditiveExpression();
                  continue;
               } catch (Throwable var45) {
                  if (jjtc001) {
                     this.jjtree.clearNodeScope(jjtn001);
                     jjtc001 = false;
                  } else {
                     this.jjtree.popNode();
                  }

                  if (var45 instanceof RuntimeException) {
                     throw (RuntimeException)var45;
                  }

                  if (var45 instanceof ParseException) {
                     throw (ParseException)var45;
                  }

                  throw (Error)var45;
               } finally {
                  if (jjtc001) {
                     this.jjtree.closeNodeScope(jjtn001, 2);
                  }

               }
            case 39:
               this.jj_consume_token(39);
               ASTLENode jjtn003 = new ASTLENode(this, 33);
               boolean jjtc003 = true;
               this.jjtree.openNodeScope(jjtn003);

               try {
                  this.AdditiveExpression();
                  continue;
               } catch (Throwable var47) {
                  if (jjtc003) {
                     this.jjtree.clearNodeScope(jjtn003);
                     jjtc003 = false;
                  } else {
                     this.jjtree.popNode();
                  }

                  if (var47 instanceof RuntimeException) {
                     throw (RuntimeException)var47;
                  }

                  if (var47 instanceof ParseException) {
                     throw (ParseException)var47;
                  }

                  throw (Error)var47;
               } finally {
                  if (jjtc003) {
                     this.jjtree.closeNodeScope(jjtn003, 2);
                  }

               }
            case 40:
               this.jj_consume_token(40);
               ASTGTNode jjtn002 = new ASTGTNode(this, 32);
               boolean jjtc002 = true;
               this.jjtree.openNodeScope(jjtn002);

               try {
                  this.AdditiveExpression();
                  continue;
               } catch (Throwable var49) {
                  if (jjtc002) {
                     this.jjtree.clearNodeScope(jjtn002);
                     jjtc002 = false;
                  } else {
                     this.jjtree.popNode();
                  }

                  if (var49 instanceof RuntimeException) {
                     throw (RuntimeException)var49;
                  }

                  if (var49 instanceof ParseException) {
                     throw (ParseException)var49;
                  }

                  throw (Error)var49;
               } finally {
                  if (jjtc002) {
                     this.jjtree.closeNodeScope(jjtn002, 2);
                  }

               }
            case 41:
               this.jj_consume_token(41);
               ASTGENode jjtn004 = new ASTGENode(this, 34);
               boolean jjtc004 = true;
               this.jjtree.openNodeScope(jjtn004);

               try {
                  this.AdditiveExpression();
                  continue;
               } catch (Throwable var43) {
                  if (jjtc004) {
                     this.jjtree.clearNodeScope(jjtn004);
                     jjtc004 = false;
                  } else {
                     this.jjtree.popNode();
                  }

                  if (var43 instanceof RuntimeException) {
                     throw (RuntimeException)var43;
                  }

                  if (var43 instanceof ParseException) {
                     throw (ParseException)var43;
                  }

                  throw (Error)var43;
               } finally {
                  if (jjtc004) {
                     this.jjtree.closeNodeScope(jjtn004, 2);
                  }

               }
            default:
               this.jj_la1[50] = this.jj_gen;
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         default:
            this.jj_la1[49] = this.jj_gen;
            return;
         }
      }
   }

   public final void AdditiveExpression() throws ParseException {
      this.MultiplicativeExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 31:
         case 32:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 31:
               this.jj_consume_token(31);
               ASTSubtractNode jjtn002 = new ASTSubtractNode(this, 36);
               boolean jjtc002 = true;
               this.jjtree.openNodeScope(jjtn002);

               try {
                  this.MultiplicativeExpression();
                  continue;
               } catch (Throwable var17) {
                  if (jjtc002) {
                     this.jjtree.clearNodeScope(jjtn002);
                     jjtc002 = false;
                  } else {
                     this.jjtree.popNode();
                  }

                  if (var17 instanceof RuntimeException) {
                     throw (RuntimeException)var17;
                  }

                  if (var17 instanceof ParseException) {
                     throw (ParseException)var17;
                  }

                  throw (Error)var17;
               } finally {
                  if (jjtc002) {
                     this.jjtree.closeNodeScope(jjtn002, 2);
                  }

               }
            case 32:
               this.jj_consume_token(32);
               ASTAddNode jjtn001 = new ASTAddNode(this, 35);
               boolean jjtc001 = true;
               this.jjtree.openNodeScope(jjtn001);

               try {
                  this.MultiplicativeExpression();
                  continue;
               } catch (Throwable var15) {
                  if (jjtc001) {
                     this.jjtree.clearNodeScope(jjtn001);
                     jjtc001 = false;
                  } else {
                     this.jjtree.popNode();
                  }

                  if (var15 instanceof RuntimeException) {
                     throw (RuntimeException)var15;
                  }

                  if (var15 instanceof ParseException) {
                     throw (ParseException)var15;
                  }

                  throw (Error)var15;
               } finally {
                  if (jjtc001) {
                     this.jjtree.closeNodeScope(jjtn001, 2);
                  }

               }
            default:
               this.jj_la1[52] = this.jj_gen;
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         default:
            this.jj_la1[51] = this.jj_gen;
            return;
         }
      }
   }

   public final void MultiplicativeExpression() throws ParseException {
      this.UnaryExpression();

      while(true) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 33:
         case 34:
         case 35:
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 33:
               this.jj_consume_token(33);
               ASTMulNode jjtn001 = new ASTMulNode(this, 37);
               boolean jjtc001 = true;
               this.jjtree.openNodeScope(jjtn001);

               try {
                  this.UnaryExpression();
                  continue;
               } catch (Throwable var29) {
                  if (jjtc001) {
                     this.jjtree.clearNodeScope(jjtn001);
                     jjtc001 = false;
                  } else {
                     this.jjtree.popNode();
                  }

                  if (var29 instanceof RuntimeException) {
                     throw (RuntimeException)var29;
                  }

                  if (var29 instanceof ParseException) {
                     throw (ParseException)var29;
                  }

                  throw (Error)var29;
               } finally {
                  if (jjtc001) {
                     this.jjtree.closeNodeScope(jjtn001, 2);
                  }

               }
            case 34:
               this.jj_consume_token(34);
               ASTDivNode jjtn002 = new ASTDivNode(this, 38);
               boolean jjtc002 = true;
               this.jjtree.openNodeScope(jjtn002);

               try {
                  this.UnaryExpression();
                  continue;
               } catch (Throwable var31) {
                  if (jjtc002) {
                     this.jjtree.clearNodeScope(jjtn002);
                     jjtc002 = false;
                  } else {
                     this.jjtree.popNode();
                  }

                  if (var31 instanceof RuntimeException) {
                     throw (RuntimeException)var31;
                  }

                  if (var31 instanceof ParseException) {
                     throw (ParseException)var31;
                  }

                  throw (Error)var31;
               } finally {
                  if (jjtc002) {
                     this.jjtree.closeNodeScope(jjtn002, 2);
                  }

               }
            case 35:
               this.jj_consume_token(35);
               ASTModNode jjtn003 = new ASTModNode(this, 39);
               boolean jjtc003 = true;
               this.jjtree.openNodeScope(jjtn003);

               try {
                  this.UnaryExpression();
                  continue;
               } catch (Throwable var27) {
                  if (jjtc003) {
                     this.jjtree.clearNodeScope(jjtn003);
                     jjtc003 = false;
                  } else {
                     this.jjtree.popNode();
                  }

                  if (var27 instanceof RuntimeException) {
                     throw (RuntimeException)var27;
                  }

                  if (var27 instanceof ParseException) {
                     throw (ParseException)var27;
                  }

                  throw (Error)var27;
               } finally {
                  if (jjtc003) {
                     this.jjtree.closeNodeScope(jjtn003, 2);
                  }

               }
            default:
               this.jj_la1[54] = this.jj_gen;
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         default:
            this.jj_la1[53] = this.jj_gen;
            return;
         }
      }
   }

   public final void UnaryExpression() throws ParseException {
      if (this.jj_2_11(2)) {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 26:
            this.jj_consume_token(26);
            break;
         default:
            this.jj_la1[55] = this.jj_gen;
         }

         this.jj_consume_token(44);
         ASTNotNode jjtn001 = new ASTNotNode(this, 40);
         boolean jjtc001 = true;
         this.jjtree.openNodeScope(jjtn001);

         try {
            this.UnaryExpression();
         } catch (Throwable var7) {
            if (jjtc001) {
               this.jjtree.clearNodeScope(jjtn001);
               jjtc001 = false;
            } else {
               this.jjtree.popNode();
            }

            if (var7 instanceof RuntimeException) {
               throw (RuntimeException)var7;
            }

            if (var7 instanceof ParseException) {
               throw (ParseException)var7;
            }

            throw (Error)var7;
         } finally {
            if (jjtc001) {
               this.jjtree.closeNodeScope(jjtn001, 1);
            }

         }
      } else {
         switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 1:
         case 6:
         case 8:
         case 26:
         case 27:
         case 28:
         case 29:
         case 52:
         case 53:
         case 62:
         case 64:
            this.PrimaryExpression();
            break;
         default:
            this.jj_la1[56] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      }

   }

   public final void PrimaryExpression() throws ParseException {
      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 26:
         this.jj_consume_token(26);
         break;
      default:
         this.jj_la1[57] = this.jj_gen;
      }

      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 27:
         this.StringLiteral();
         break;
      case 52:
         this.IntegerLiteral();
         break;
      case 62:
      case 64:
         this.Reference();
         break;
      default:
         this.jj_la1[58] = this.jj_gen;
         if (this.jj_2_12(Integer.MAX_VALUE)) {
            this.IntegerRange();
         } else {
            switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 1:
               this.ObjectArray();
               break;
            case 6:
               this.Map();
               break;
            case 8:
               this.jj_consume_token(8);
               this.Expression();
               this.jj_consume_token(9);
               break;
            case 28:
               this.True();
               break;
            case 29:
               this.False();
               break;
            case 53:
               this.FloatingPointLiteral();
               break;
            default:
               this.jj_la1[59] = this.jj_gen;
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         }
      }

      switch(this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
      case 26:
         this.jj_consume_token(26);
         break;
      default:
         this.jj_la1[60] = this.jj_gen;
      }

   }

   private final boolean jj_2_1(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_1();
         return var2;
      } catch (Parser.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(0, xla);
      }

      return var3;
   }

   private final boolean jj_2_2(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_2();
         return var2;
      } catch (Parser.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(1, xla);
      }

      return var3;
   }

   private final boolean jj_2_3(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_3();
         return var2;
      } catch (Parser.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(2, xla);
      }

      return var3;
   }

   private final boolean jj_2_4(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_4();
         return var2;
      } catch (Parser.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(3, xla);
      }

      return var3;
   }

   private final boolean jj_2_5(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_5();
         return var2;
      } catch (Parser.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(4, xla);
      }

      return var3;
   }

   private final boolean jj_2_6(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_6();
         return var2;
      } catch (Parser.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(5, xla);
      }

      return var3;
   }

   private final boolean jj_2_7(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_7();
         return var2;
      } catch (Parser.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(6, xla);
      }

      return var3;
   }

   private final boolean jj_2_8(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_8();
         return var2;
      } catch (Parser.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(7, xla);
      }

      return var3;
   }

   private final boolean jj_2_9(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_9();
         return var2;
      } catch (Parser.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(8, xla);
      }

      return var3;
   }

   private final boolean jj_2_10(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_10();
         return var2;
      } catch (Parser.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(9, xla);
      }

      return var3;
   }

   private final boolean jj_2_11(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_11();
         return var2;
      } catch (Parser.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(10, xla);
      }

      return var3;
   }

   private final boolean jj_2_12(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_12();
         return var2;
      } catch (Parser.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(11, xla);
      }

      return var3;
   }

   private final boolean jj_3R_82() {
      if (this.jj_scan_token(3)) {
         return true;
      } else {
         return this.jj_3R_25();
      }
   }

   private final boolean jj_3_8() {
      return this.jj_3R_29();
   }

   private final boolean jj_3R_26() {
      return this.jj_3R_20();
   }

   private final boolean jj_3R_66() {
      return this.jj_scan_token(29);
   }

   private final boolean jj_3R_65() {
      return this.jj_scan_token(28);
   }

   private final boolean jj_3_9() {
      if (this.jj_scan_token(63)) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3_10()) {
            this.jj_scanpos = xsp;
            if (this.jj_3R_30()) {
               return true;
            }
         }

         return false;
      }
   }

   private final boolean jj_3R_57() {
      if (this.jj_3R_25()) {
         return true;
      } else {
         Token xsp;
         do {
            xsp = this.jj_scanpos;
         } while(!this.jj_3R_82());

         this.jj_scanpos = xsp;
         return false;
      }
   }

   private final boolean jj_3_7() {
      if (this.jj_scan_token(63)) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3_8()) {
            this.jj_scanpos = xsp;
            if (this.jj_3R_28()) {
               return true;
            }
         }

         return false;
      }
   }

   private final boolean jj_3_2() {
      return this.jj_scan_token(19);
   }

   private final boolean jj_3R_35() {
      if (this.jj_scan_token(64)) {
         return true;
      } else if (this.jj_scan_token(62)) {
         return true;
      } else {
         Token xsp;
         do {
            xsp = this.jj_scanpos;
         } while(!this.jj_3_9());

         this.jj_scanpos = xsp;
         return this.jj_scan_token(65);
      }
   }

   private final boolean jj_3_12() {
      if (this.jj_scan_token(1)) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_scan_token(26)) {
            this.jj_scanpos = xsp;
         }

         xsp = this.jj_scanpos;
         if (this.jj_3R_32()) {
            this.jj_scanpos = xsp;
            if (this.jj_3R_33()) {
               return true;
            }
         }

         xsp = this.jj_scanpos;
         if (this.jj_scan_token(26)) {
            this.jj_scanpos = xsp;
         }

         return this.jj_scan_token(4);
      }
   }

   private final boolean jj_3R_34() {
      if (this.jj_scan_token(62)) {
         return true;
      } else {
         Token xsp;
         do {
            xsp = this.jj_scanpos;
         } while(!this.jj_3_7());

         this.jj_scanpos = xsp;
         return false;
      }
   }

   private final boolean jj_3R_81() {
      return this.jj_scan_token(8);
   }

   private final boolean jj_3R_80() {
      return this.jj_3R_66();
   }

   private final boolean jj_3R_79() {
      return this.jj_3R_65();
   }

   private final boolean jj_3R_20() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3R_34()) {
         this.jj_scanpos = xsp;
         if (this.jj_3R_35()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3R_78() {
      return this.jj_3R_64();
   }

   private final boolean jj_3R_77() {
      return this.jj_3R_63();
   }

   private final boolean jj_3R_76() {
      return this.jj_3R_62();
   }

   private final boolean jj_3R_75() {
      return this.jj_3R_61();
   }

   private final boolean jj_3R_74() {
      return this.jj_3R_36();
   }

   private final boolean jj_3R_73() {
      return this.jj_3R_20();
   }

   private final boolean jj_3_6() {
      if (this.jj_scan_token(1)) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_scan_token(26)) {
            this.jj_scanpos = xsp;
         }

         xsp = this.jj_scanpos;
         if (this.jj_3R_26()) {
            this.jj_scanpos = xsp;
            if (this.jj_3R_27()) {
               return true;
            }
         }

         xsp = this.jj_scanpos;
         if (this.jj_scan_token(26)) {
            this.jj_scanpos = xsp;
         }

         return this.jj_scan_token(4);
      }
   }

   private final boolean jj_3R_29() {
      if (this.jj_3R_56()) {
         return true;
      } else if (this.jj_scan_token(8)) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3R_57()) {
            this.jj_scanpos = xsp;
         }

         return this.jj_scan_token(10);
      }
   }

   private final boolean jj_3R_72() {
      return this.jj_3R_60();
   }

   private final boolean jj_3R_67() {
      Token xsp = this.jj_scanpos;
      if (this.jj_scan_token(26)) {
         this.jj_scanpos = xsp;
      }

      xsp = this.jj_scanpos;
      if (this.jj_3R_72()) {
         this.jj_scanpos = xsp;
         if (this.jj_3R_73()) {
            this.jj_scanpos = xsp;
            if (this.jj_3R_74()) {
               this.jj_scanpos = xsp;
               if (this.jj_3R_75()) {
                  this.jj_scanpos = xsp;
                  if (this.jj_3R_76()) {
                     this.jj_scanpos = xsp;
                     if (this.jj_3R_77()) {
                        this.jj_scanpos = xsp;
                        if (this.jj_3R_78()) {
                           this.jj_scanpos = xsp;
                           if (this.jj_3R_79()) {
                              this.jj_scanpos = xsp;
                              if (this.jj_3R_80()) {
                                 this.jj_scanpos = xsp;
                                 if (this.jj_3R_81()) {
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

      return false;
   }

   private final boolean jj_3R_55() {
      return this.jj_3R_62();
   }

   private final boolean jj_3R_54() {
      return this.jj_3R_20();
   }

   private final boolean jj_3R_53() {
      return this.jj_3R_66();
   }

   private final boolean jj_3R_85() {
      if (this.jj_scan_token(3)) {
         return true;
      } else if (this.jj_3R_25()) {
         return true;
      } else if (this.jj_scan_token(5)) {
         return true;
      } else {
         return this.jj_3R_25();
      }
   }

   private final boolean jj_3R_52() {
      return this.jj_3R_65();
   }

   private final boolean jj_3R_31() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_11()) {
         this.jj_scanpos = xsp;
         if (this.jj_3R_58()) {
            return true;
         }
      }

      return false;
   }

   private final boolean jj_3_11() {
      Token xsp = this.jj_scanpos;
      if (this.jj_scan_token(26)) {
         this.jj_scanpos = xsp;
      }

      if (this.jj_scan_token(44)) {
         return true;
      } else {
         return this.jj_3R_31();
      }
   }

   private final boolean jj_3R_58() {
      return this.jj_3R_67();
   }

   private final boolean jj_3R_51() {
      return this.jj_3R_64();
   }

   private final boolean jj_3R_50() {
      return this.jj_3R_63();
   }

   private final boolean jj_3R_49() {
      return this.jj_3R_61();
   }

   private final boolean jj_3R_48() {
      return this.jj_3R_36();
   }

   private final boolean jj_3R_47() {
      return this.jj_3R_60();
   }

   private final boolean jj_3R_22() {
      return this.jj_3R_36();
   }

   private final boolean jj_3R_84() {
      return this.jj_3R_36();
   }

   private final boolean jj_3R_69() {
      return this.jj_3R_36();
   }

   private final boolean jj_3R_86() {
      if (this.jj_scan_token(3)) {
         return true;
      } else {
         return this.jj_3R_25();
      }
   }

   private final boolean jj_3R_25() {
      Token xsp = this.jj_scanpos;
      if (this.jj_scan_token(26)) {
         this.jj_scanpos = xsp;
      }

      xsp = this.jj_scanpos;
      if (this.jj_3R_47()) {
         this.jj_scanpos = xsp;
         if (this.jj_3R_48()) {
            this.jj_scanpos = xsp;
            if (this.jj_3R_49()) {
               this.jj_scanpos = xsp;
               if (this.jj_3R_50()) {
                  this.jj_scanpos = xsp;
                  if (this.jj_3R_51()) {
                     this.jj_scanpos = xsp;
                     if (this.jj_3R_52()) {
                        this.jj_scanpos = xsp;
                        if (this.jj_3R_53()) {
                           this.jj_scanpos = xsp;
                           if (this.jj_3R_54()) {
                              this.jj_scanpos = xsp;
                              if (this.jj_3R_55()) {
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

      xsp = this.jj_scanpos;
      if (this.jj_scan_token(26)) {
         this.jj_scanpos = xsp;
      }

      return false;
   }

   private final boolean jj_3_1() {
      return this.jj_3R_20();
   }

   private final boolean jj_3R_21() {
      return this.jj_3R_20();
   }

   private final boolean jj_3R_83() {
      return this.jj_3R_20();
   }

   private final boolean jj_3R_68() {
      return this.jj_3R_20();
   }

   private final boolean jj_3R_71() {
      if (this.jj_3R_25()) {
         return true;
      } else {
         Token xsp;
         do {
            xsp = this.jj_scanpos;
         } while(!this.jj_3R_86());

         this.jj_scanpos = xsp;
         return false;
      }
   }

   private final boolean jj_3R_61() {
      if (this.jj_scan_token(1)) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_scan_token(26)) {
            this.jj_scanpos = xsp;
         }

         xsp = this.jj_scanpos;
         if (this.jj_3R_68()) {
            this.jj_scanpos = xsp;
            if (this.jj_3R_69()) {
               return true;
            }
         }

         xsp = this.jj_scanpos;
         if (this.jj_scan_token(26)) {
            this.jj_scanpos = xsp;
         }

         if (this.jj_scan_token(4)) {
            return true;
         } else {
            xsp = this.jj_scanpos;
            if (this.jj_scan_token(26)) {
               this.jj_scanpos = xsp;
            }

            xsp = this.jj_scanpos;
            if (this.jj_3R_83()) {
               this.jj_scanpos = xsp;
               if (this.jj_3R_84()) {
                  return true;
               }
            }

            xsp = this.jj_scanpos;
            if (this.jj_scan_token(26)) {
               this.jj_scanpos = xsp;
            }

            return this.jj_scan_token(2);
         }
      }
   }

   private final boolean jj_3R_64() {
      if (this.jj_scan_token(1)) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3R_71()) {
            this.jj_scanpos = xsp;
         }

         return this.jj_scan_token(2);
      }
   }

   private final boolean jj_3R_46() {
      return this.jj_3R_66();
   }

   private final boolean jj_3R_70() {
      Token xsp = this.jj_scanpos;
      if (this.jj_scan_token(26)) {
         this.jj_scanpos = xsp;
      }

      return false;
   }

   private final boolean jj_3_5() {
      if (this.jj_3R_25()) {
         return true;
      } else if (this.jj_scan_token(5)) {
         return true;
      } else if (this.jj_3R_25()) {
         return true;
      } else {
         Token xsp;
         do {
            xsp = this.jj_scanpos;
         } while(!this.jj_3R_85());

         this.jj_scanpos = xsp;
         return false;
      }
   }

   private final boolean jj_3R_45() {
      return this.jj_3R_65();
   }

   private final boolean jj_3R_63() {
      if (this.jj_scan_token(6)) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3_5()) {
            this.jj_scanpos = xsp;
            if (this.jj_3R_70()) {
               return true;
            }
         }

         return this.jj_scan_token(7);
      }
   }

   private final boolean jj_3_3() {
      if (this.jj_scan_token(1)) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_scan_token(26)) {
            this.jj_scanpos = xsp;
         }

         xsp = this.jj_scanpos;
         if (this.jj_3R_21()) {
            this.jj_scanpos = xsp;
            if (this.jj_3R_22()) {
               return true;
            }
         }

         xsp = this.jj_scanpos;
         if (this.jj_scan_token(26)) {
            this.jj_scanpos = xsp;
         }

         return this.jj_scan_token(4);
      }
   }

   private final boolean jj_3R_44() {
      return this.jj_3R_64();
   }

   private final boolean jj_3R_43() {
      return this.jj_3R_63();
   }

   private final boolean jj_3R_42() {
      return this.jj_3R_62();
   }

   private final boolean jj_3R_41() {
      return this.jj_3R_61();
   }

   private final boolean jj_3R_40() {
      return this.jj_3R_36();
   }

   private final boolean jj_3R_39() {
      return this.jj_3R_60();
   }

   private final boolean jj_3R_23() {
      if (this.jj_scan_token(3)) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_scan_token(26)) {
            this.jj_scanpos = xsp;
         }

         return false;
      }
   }

   private final boolean jj_3R_38() {
      return this.jj_3R_59();
   }

   private final boolean jj_3R_37() {
      return this.jj_3R_20();
   }

   private final boolean jj_3R_24() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3R_37()) {
         this.jj_scanpos = xsp;
         if (this.jj_3R_38()) {
            this.jj_scanpos = xsp;
            if (this.jj_3R_39()) {
               this.jj_scanpos = xsp;
               if (this.jj_3R_40()) {
                  this.jj_scanpos = xsp;
                  if (this.jj_3R_41()) {
                     this.jj_scanpos = xsp;
                     if (this.jj_3R_42()) {
                        this.jj_scanpos = xsp;
                        if (this.jj_3R_43()) {
                           this.jj_scanpos = xsp;
                           if (this.jj_3R_44()) {
                              this.jj_scanpos = xsp;
                              if (this.jj_3R_45()) {
                                 this.jj_scanpos = xsp;
                                 if (this.jj_3R_46()) {
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

      return false;
   }

   private final boolean jj_3R_59() {
      return this.jj_scan_token(57);
   }

   private final boolean jj_3R_56() {
      return this.jj_scan_token(62);
   }

   private final boolean jj_3_4() {
      Token xsp = this.jj_scanpos;
      if (this.jj_scan_token(26)) {
         this.jj_scanpos = xsp;
      }

      xsp = this.jj_scanpos;
      if (this.jj_3R_23()) {
         this.jj_scanpos = xsp;
      }

      return this.jj_3R_24();
   }

   private final boolean jj_3R_60() {
      return this.jj_scan_token(27);
   }

   private final boolean jj_3R_30() {
      return this.jj_3R_56();
   }

   private final boolean jj_3R_36() {
      return this.jj_scan_token(52);
   }

   private final boolean jj_3R_28() {
      return this.jj_3R_56();
   }

   private final boolean jj_3R_62() {
      return this.jj_scan_token(53);
   }

   private final boolean jj_3R_33() {
      return this.jj_3R_36();
   }

   private final boolean jj_3R_32() {
      return this.jj_3R_20();
   }

   private final boolean jj_3R_27() {
      return this.jj_3R_36();
   }

   private final boolean jj_3_10() {
      return this.jj_3R_29();
   }

   private static void jj_la1_0() {
      jj_la1_0 = new int[]{163322624, 0, 163322624, 4194304, 25427968, 134217728, 805306434, 0, 67108864, 67108864, 67108864, 8, 67108864, 163322624, 8, 67108864, 8, 1006633026, 67108864, 0, 67108864, 67108864, 0, 67108864, 67108864, 134217728, 805306434, 67108864, 8, 1006633026, 0, 0, 0, 137364224, 67108864, 163322624, 0, 0, 0, 163322624, 67108864, 163322624, 67108864, 67108864, 1073741824, 0, 0, 0, 0, 0, 0, Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0, 67108864, 1006633282, 67108864, 134217728, 805306690, 67108864};
   }

   private static void jj_la1_1() {
      jj_la1_1 = new int[]{-969637888, 294912, -2043674624, 0, 0, 1108344832, 2097152, 100663296, 0, 0, 0, 0, 0, -969637888, 0, 0, 0, 1076887552, 0, 1074790400, 0, 0, 1074790400, 0, 0, 1048576, 1075838976, 0, 0, 1076887552, 1073741824, 1073741824, 1073741824, -2144337920, 0, -969637888, 65536, 65536, 131072, -969637888, 0, -969637888, 0, 0, 0, 32, 16, 3072, 3072, 960, 960, 1, 1, 14, 14, 0, 1076887552, 0, 1074790400, 2097152, 0};
   }

   private static void jj_la1_2() {
      jj_la1_2 = new int[]{3, 0, 3, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 3, 0, 3, 0, 0, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0};
   }

   public Parser(CharStream stream) {
      this.jjtree = new JJTParserState();
      this.directives = new Hashtable(0);
      this.currentTemplateName = "";
      this.velcharstream = null;
      this.rsvc = null;
      this.lookingAhead = false;
      this.jj_la1 = new int[61];
      this.jj_2_rtns = new Parser.JJCalls[12];
      this.jj_rescan = false;
      this.jj_gc = 0;
      this.jj_ls = new Parser.LookaheadSuccess();
      this.jj_expentries = new Vector();
      this.jj_kind = -1;
      this.jj_lasttokens = new int[100];
      this.token_source = new ParserTokenManager(stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      int i;
      for(i = 0; i < 61; ++i) {
         this.jj_la1[i] = -1;
      }

      for(i = 0; i < this.jj_2_rtns.length; ++i) {
         this.jj_2_rtns[i] = new Parser.JJCalls();
      }

   }

   public void ReInit(CharStream stream) {
      this.token_source.ReInit(stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jjtree.reset();
      this.jj_gen = 0;

      int i;
      for(i = 0; i < 61; ++i) {
         this.jj_la1[i] = -1;
      }

      for(i = 0; i < this.jj_2_rtns.length; ++i) {
         this.jj_2_rtns[i] = new Parser.JJCalls();
      }

   }

   public Parser(ParserTokenManager tm) {
      this.jjtree = new JJTParserState();
      this.directives = new Hashtable(0);
      this.currentTemplateName = "";
      this.velcharstream = null;
      this.rsvc = null;
      this.lookingAhead = false;
      this.jj_la1 = new int[61];
      this.jj_2_rtns = new Parser.JJCalls[12];
      this.jj_rescan = false;
      this.jj_gc = 0;
      this.jj_ls = new Parser.LookaheadSuccess();
      this.jj_expentries = new Vector();
      this.jj_kind = -1;
      this.jj_lasttokens = new int[100];
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      int i;
      for(i = 0; i < 61; ++i) {
         this.jj_la1[i] = -1;
      }

      for(i = 0; i < this.jj_2_rtns.length; ++i) {
         this.jj_2_rtns[i] = new Parser.JJCalls();
      }

   }

   public void ReInit(ParserTokenManager tm) {
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jjtree.reset();
      this.jj_gen = 0;

      int i;
      for(i = 0; i < 61; ++i) {
         this.jj_la1[i] = -1;
      }

      for(i = 0; i < this.jj_2_rtns.length; ++i) {
         this.jj_2_rtns[i] = new Parser.JJCalls();
      }

   }

   private final Token jj_consume_token(int kind) throws ParseException {
      Token oldToken;
      if ((oldToken = this.token).next != null) {
         this.token = this.token.next;
      } else {
         this.token = this.token.next = this.token_source.getNextToken();
      }

      this.jj_ntk = -1;
      if (this.token.kind != kind) {
         this.token = oldToken;
         this.jj_kind = kind;
         throw this.generateParseException();
      } else {
         ++this.jj_gen;
         if (++this.jj_gc > 100) {
            this.jj_gc = 0;

            for(int i = 0; i < this.jj_2_rtns.length; ++i) {
               for(Parser.JJCalls c = this.jj_2_rtns[i]; c != null; c = c.next) {
                  if (c.gen < this.jj_gen) {
                     c.first = null;
                  }
               }
            }
         }

         return this.token;
      }
   }

   private final boolean jj_scan_token(int kind) {
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

      if (this.jj_rescan) {
         int i = 0;

         Token tok;
         for(tok = this.token; tok != null && tok != this.jj_scanpos; tok = tok.next) {
            ++i;
         }

         if (tok != null) {
            this.jj_add_error_token(kind, i);
         }
      }

      if (this.jj_scanpos.kind != kind) {
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
      ++this.jj_gen;
      return this.token;
   }

   public final Token getToken(int index) {
      Token t = this.lookingAhead ? this.jj_scanpos : this.token;

      for(int i = 0; i < index; ++i) {
         if (t.next != null) {
            t = t.next;
         } else {
            t = t.next = this.token_source.getNextToken();
         }
      }

      return t;
   }

   private final int jj_ntk() {
      return (this.jj_nt = this.token.next) == null ? (this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind) : (this.jj_ntk = this.jj_nt.kind);
   }

   private void jj_add_error_token(int kind, int pos) {
      if (pos < 100) {
         if (pos == this.jj_endpos + 1) {
            this.jj_lasttokens[this.jj_endpos++] = kind;
         } else if (this.jj_endpos != 0) {
            this.jj_expentry = new int[this.jj_endpos];

            for(int i = 0; i < this.jj_endpos; ++i) {
               this.jj_expentry[i] = this.jj_lasttokens[i];
            }

            boolean exists = false;
            Enumeration e = this.jj_expentries.elements();

            label48:
            do {
               int[] oldentry;
               do {
                  if (!e.hasMoreElements()) {
                     break label48;
                  }

                  oldentry = (int[])e.nextElement();
               } while(oldentry.length != this.jj_expentry.length);

               exists = true;

               for(int i = 0; i < this.jj_expentry.length; ++i) {
                  if (oldentry[i] != this.jj_expentry[i]) {
                     exists = false;
                     break;
                  }
               }
            } while(!exists);

            if (!exists) {
               this.jj_expentries.addElement(this.jj_expentry);
            }

            if (pos != 0) {
               this.jj_lasttokens[(this.jj_endpos = pos) - 1] = kind;
            }
         }

      }
   }

   public ParseException generateParseException() {
      this.jj_expentries.removeAllElements();
      boolean[] la1tokens = new boolean[68];

      int i;
      for(i = 0; i < 68; ++i) {
         la1tokens[i] = false;
      }

      if (this.jj_kind >= 0) {
         la1tokens[this.jj_kind] = true;
         this.jj_kind = -1;
      }

      int j;
      for(i = 0; i < 61; ++i) {
         if (this.jj_la1[i] == this.jj_gen) {
            for(j = 0; j < 32; ++j) {
               if ((jj_la1_0[i] & 1 << j) != 0) {
                  la1tokens[j] = true;
               }

               if ((jj_la1_1[i] & 1 << j) != 0) {
                  la1tokens[32 + j] = true;
               }

               if ((jj_la1_2[i] & 1 << j) != 0) {
                  la1tokens[64 + j] = true;
               }
            }
         }
      }

      for(i = 0; i < 68; ++i) {
         if (la1tokens[i]) {
            this.jj_expentry = new int[1];
            this.jj_expentry[0] = i;
            this.jj_expentries.addElement(this.jj_expentry);
         }
      }

      this.jj_endpos = 0;
      this.jj_rescan_token();
      this.jj_add_error_token(0, 0);
      int[][] exptokseq = new int[this.jj_expentries.size()][];

      for(j = 0; j < this.jj_expentries.size(); ++j) {
         exptokseq[j] = (int[])this.jj_expentries.elementAt(j);
      }

      return new ParseException(this.token, exptokseq, ParserConstants.tokenImage);
   }

   public final void enable_tracing() {
   }

   public final void disable_tracing() {
   }

   private final void jj_rescan_token() {
      this.jj_rescan = true;

      for(int i = 0; i < 12; ++i) {
         try {
            Parser.JJCalls p = this.jj_2_rtns[i];

            do {
               if (p.gen > this.jj_gen) {
                  this.jj_la = p.arg;
                  this.jj_lastpos = this.jj_scanpos = p.first;
                  switch(i) {
                  case 0:
                     this.jj_3_1();
                     break;
                  case 1:
                     this.jj_3_2();
                     break;
                  case 2:
                     this.jj_3_3();
                     break;
                  case 3:
                     this.jj_3_4();
                     break;
                  case 4:
                     this.jj_3_5();
                     break;
                  case 5:
                     this.jj_3_6();
                     break;
                  case 6:
                     this.jj_3_7();
                     break;
                  case 7:
                     this.jj_3_8();
                     break;
                  case 8:
                     this.jj_3_9();
                     break;
                  case 9:
                     this.jj_3_10();
                     break;
                  case 10:
                     this.jj_3_11();
                     break;
                  case 11:
                     this.jj_3_12();
                  }
               }

               p = p.next;
            } while(p != null);
         } catch (Parser.LookaheadSuccess var3) {
         }
      }

      this.jj_rescan = false;
   }

   private final void jj_save(int index, int xla) {
      Parser.JJCalls p;
      for(p = this.jj_2_rtns[index]; p.gen > this.jj_gen; p = p.next) {
         if (p.next == null) {
            p = p.next = new Parser.JJCalls();
            break;
         }
      }

      p.gen = this.jj_gen + xla - this.jj_la;
      p.first = this.token;
      p.arg = xla;
   }

   static {
      jj_la1_0();
      jj_la1_1();
      jj_la1_2();
   }

   static final class JJCalls {
      int gen;
      Token first;
      int arg;
      Parser.JJCalls next;
   }

   private static final class LookaheadSuccess extends Error {
      private LookaheadSuccess() {
      }

      // $FF: synthetic method
      LookaheadSuccess(Object x0) {
         this();
      }
   }
}
