package jasmin;

import jas.Base64;
import jas.ElemValPair;
import jas.GenericAttr;
import jas.VisibilityAnnotationAttr;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Stack;
import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
import java_cup.runtime.lr_parser;

public class parser extends lr_parser {
   protected static final short[][] _production_table = getFromFile("tables.out/shortarray_0.obj");
   protected static final short[][] _action_table = getFromFile("tables.out/shortarray_1.obj");
   protected static final short[][] _reduce_table = getFromFile("tables.out/shortarray_2.obj");
   protected parser.CUP$parser$actions action_obj;
   public Scanner scanner;
   public ClassFile classFile;

   public final Class getSymbolContainer() {
      return sym.class;
   }

   /** @deprecated */
   @Deprecated
   public parser() {
   }

   /** @deprecated */
   @Deprecated
   public parser(java_cup.runtime.Scanner s) {
      super(s);
   }

   public parser(java_cup.runtime.Scanner s, SymbolFactory sf) {
      super(s, sf);
   }

   public short[][] production_table() {
      return _production_table;
   }

   public static short[][] getFromFile(String filename) {
      try {
         ClassLoader cl = java_cup.parser.class.getClassLoader();
         InputStream is = cl.getResourceAsStream(filename);
         ObjectInputStream ois = new ObjectInputStream(is);
         short[][] sa2 = (short[][])((short[][])ois.readObject());
         return sa2;
      } catch (Throwable var5) {
         throw new RuntimeException("oups: " + var5);
      }
   }

   public short[][] action_table() {
      return _action_table;
   }

   public short[][] reduce_table() {
      return _reduce_table;
   }

   protected void init_actions() {
      this.action_obj = new parser.CUP$parser$actions(this);
   }

   public Symbol do_action(int act_num, lr_parser parser, Stack stack, int top) throws Exception {
      return this.action_obj.CUP$parser$do_action(act_num, parser, stack, top);
   }

   public int start_state() {
      return 0;
   }

   public int start_production() {
      return 1;
   }

   public int EOF_sym() {
      return 0;
   }

   public int error_sym() {
      return 1;
   }

   public void user_init() throws Exception {
      this.action_obj.scanner = this.scanner;
      this.action_obj.classFile = this.classFile;
   }

   public Symbol scan() throws Exception {
      return this.scanner.next_token();
   }

   public void report_error(String message, Object info) {
      this.classFile.report_error("Warning - " + message);
   }

   public void report_fatal_error(String message, Object info) {
      this.classFile.report_error("Error - " + message);
   }

   parser(ClassFile classFile, Scanner scanner) {
      this.scanner = scanner;
      this.classFile = classFile;
   }

   class CUP$parser$actions {
      short access_val;
      public Scanner scanner;
      public ClassFile classFile;
      private final parser parser;

      CUP$parser$actions(parser parser) {
         this.parser = parser;
      }

      public final Symbol CUP$parser$do_action_part00000000(int CUP$parser$act_num, lr_parser CUP$parser$parser, Stack CUP$parser$stack, int CUP$parser$top) throws Exception {
         Symbol CUP$parser$result;
         Object RESULT;
         int labelleft;
         int labelright;
         String label;
         int nameleft;
         int nameright;
         String name;
         int sigright;
         String sig;
         int vleft;
         int vright;
         Object v;
         int dep_attrleft;
         int dep_attrright;
         Object dep_attr;
         int sig_attrleft;
         int sig_attrright;
         Object sig_attr;
         int vis_annot_attr1left;
         int vis_annot_attr1right;
         Object vis_annot_attr1;
         int vis_annot_attr2left;
         int vis_annot_attr2right;
         Object vis_annot_attr2;
         Short i;
         Object annot_attr;
         int sigleft;
         String RESULTxx;
         switch(CUP$parser$act_num) {
         case 0:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("jas_file", 22, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 12), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 1:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            annot_attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("$START", 0, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), annot_attr);
            CUP$parser$parser.done_parsing();
            return CUP$parser$result;
         case 2:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            label = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            this.classFile.setSource(label);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("source_spec", 36, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 3:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("source_spec", 36, (Symbol)CUP$parser$stack.peek(), (Object)RESULT);
            return CUP$parser$result;
         case 4:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            i = (Short)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            this.classFile.setClass(name, (short)(i.intValue() | 32));
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_spec", 10, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 5:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            i = (Short)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            this.classFile.setClass(name, (short)(i.intValue() | 512));
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_spec", 10, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 6:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.peek()).left;
            labelright = ((Symbol)CUP$parser$stack.peek()).right;
            label = (String)((Symbol)CUP$parser$stack.peek()).value;
            RESULTxx = ScannerUtils.convertDots(label);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 7:
            RESULT = null;
            RESULTxx = "from";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 8:
            RESULT = null;
            RESULTxx = "to";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 9:
            RESULT = null;
            RESULTxx = "using";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 10:
            RESULT = null;
            RESULTxx = "is";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 11:
            RESULT = null;
            RESULTxx = "method";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 12:
            RESULT = null;
            RESULTxx = "lookupswitch";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 13:
            RESULT = null;
            RESULTxx = "tableswitch";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 14:
            RESULT = null;
            RESULTxx = "default";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 15:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.peek()).left;
            labelright = ((Symbol)CUP$parser$stack.peek()).right;
            label = (String)((Symbol)CUP$parser$stack.peek()).value;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Word_plus_keywords", 1, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), label);
            return CUP$parser$result;
         case 16:
            RESULT = null;
            RESULTxx = "aaload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 17:
            RESULT = null;
            RESULTxx = "aastore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 18:
            RESULT = null;
            RESULTxx = "aconst_null";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 19:
            RESULT = null;
            RESULTxx = "aload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 20:
            RESULT = null;
            RESULTxx = "aload_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 21:
            RESULT = null;
            RESULTxx = "aload_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 22:
            RESULT = null;
            RESULTxx = "aload_2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 23:
            RESULT = null;
            RESULTxx = "aload_3";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 24:
            RESULT = null;
            RESULTxx = "anewarray";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 25:
            RESULT = null;
            RESULTxx = "areturn";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 26:
            RESULT = null;
            RESULTxx = "arraylength";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 27:
            RESULT = null;
            RESULTxx = "astore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 28:
            RESULT = null;
            RESULTxx = "astore_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 29:
            RESULT = null;
            RESULTxx = "astore_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 30:
            RESULT = null;
            RESULTxx = "astore_2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 31:
            RESULT = null;
            RESULTxx = "astore_3";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 32:
            RESULT = null;
            RESULTxx = "athrow";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 33:
            RESULT = null;
            RESULTxx = "baload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 34:
            RESULT = null;
            RESULTxx = "bastore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 35:
            RESULT = null;
            RESULTxx = "bipush";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 36:
            RESULT = null;
            RESULTxx = "breakpoint";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 37:
            RESULT = null;
            RESULTxx = "caload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 38:
            RESULT = null;
            RESULTxx = "castore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 39:
            RESULT = null;
            RESULTxx = "checkcast";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 40:
            RESULT = null;
            RESULTxx = "d2f";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 41:
            RESULT = null;
            RESULTxx = "d2i";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 42:
            RESULT = null;
            RESULTxx = "d2l";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 43:
            RESULT = null;
            RESULTxx = "dadd";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 44:
            RESULT = null;
            RESULTxx = "daload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 45:
            RESULT = null;
            RESULTxx = "dastore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 46:
            RESULT = null;
            RESULTxx = "dcmpg";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 47:
            RESULT = null;
            RESULTxx = "dcmpl";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 48:
            RESULT = null;
            RESULTxx = "dconst_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 49:
            RESULT = null;
            RESULTxx = "dconst_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 50:
            RESULT = null;
            RESULTxx = "ddiv";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 51:
            RESULT = null;
            RESULTxx = "dload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 52:
            RESULT = null;
            RESULTxx = "dload_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 53:
            RESULT = null;
            RESULTxx = "dload_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 54:
            RESULT = null;
            RESULTxx = "dload_2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 55:
            RESULT = null;
            RESULTxx = "dload_3";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 56:
            RESULT = null;
            RESULTxx = "dmul";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 57:
            RESULT = null;
            RESULTxx = "dneg";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 58:
            RESULT = null;
            RESULTxx = "drem";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 59:
            RESULT = null;
            RESULTxx = "dreturn";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 60:
            RESULT = null;
            RESULTxx = "dstore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 61:
            RESULT = null;
            RESULTxx = "dstore_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 62:
            RESULT = null;
            RESULTxx = "dstore_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 63:
            RESULT = null;
            RESULTxx = "dstore_2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 64:
            RESULT = null;
            RESULTxx = "dstore_3";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 65:
            RESULT = null;
            RESULTxx = "dsub";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 66:
            RESULT = null;
            RESULTxx = "dup";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 67:
            RESULT = null;
            RESULTxx = "dup2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 68:
            RESULT = null;
            RESULTxx = "dup2_x1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 69:
            RESULT = null;
            RESULTxx = "dup2_x2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 70:
            RESULT = null;
            RESULTxx = "dup_x1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 71:
            RESULT = null;
            RESULTxx = "dup_x2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 72:
            RESULT = null;
            RESULTxx = "f2d";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 73:
            RESULT = null;
            RESULTxx = "f2i";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 74:
            RESULT = null;
            RESULTxx = "f2l";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 75:
            RESULT = null;
            RESULTxx = "fadd";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 76:
            RESULT = null;
            RESULTxx = "faload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 77:
            RESULT = null;
            RESULTxx = "fastore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 78:
            RESULT = null;
            RESULTxx = "fcmpg";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 79:
            RESULT = null;
            RESULTxx = "fcmpl";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 80:
            RESULT = null;
            RESULTxx = "fconst_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 81:
            RESULT = null;
            RESULTxx = "fconst_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 82:
            RESULT = null;
            RESULTxx = "fconst_2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 83:
            RESULT = null;
            RESULTxx = "fdiv";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 84:
            RESULT = null;
            RESULTxx = "fload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 85:
            RESULT = null;
            RESULTxx = "fload_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 86:
            RESULT = null;
            RESULTxx = "fload_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 87:
            RESULT = null;
            RESULTxx = "fload_2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 88:
            RESULT = null;
            RESULTxx = "fload_3";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 89:
            RESULT = null;
            RESULTxx = "fmul";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 90:
            RESULT = null;
            RESULTxx = "fneg";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 91:
            RESULT = null;
            RESULTxx = "frem";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 92:
            RESULT = null;
            RESULTxx = "freturn";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 93:
            RESULT = null;
            RESULTxx = "fstore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 94:
            RESULT = null;
            RESULTxx = "fstore_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 95:
            RESULT = null;
            RESULTxx = "fstore_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 96:
            RESULT = null;
            RESULTxx = "fstore_2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 97:
            RESULT = null;
            RESULTxx = "fstore_3";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 98:
            RESULT = null;
            RESULTxx = "fsub";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 99:
            RESULT = null;
            RESULTxx = "getfield";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 100:
            RESULT = null;
            RESULTxx = "getstatic";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 101:
            RESULT = null;
            RESULTxx = "goto";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 102:
            RESULT = null;
            RESULTxx = "goto_w";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 103:
            RESULT = null;
            RESULTxx = "i2d";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 104:
            RESULT = null;
            RESULTxx = "i2f";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 105:
            RESULT = null;
            RESULTxx = "i2l";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 106:
            RESULT = null;
            RESULTxx = "iadd";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 107:
            RESULT = null;
            RESULTxx = "iaload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 108:
            RESULT = null;
            RESULTxx = "iand";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 109:
            RESULT = null;
            RESULTxx = "iastore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 110:
            RESULT = null;
            RESULTxx = "iconst_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 111:
            RESULT = null;
            RESULTxx = "iconst_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 112:
            RESULT = null;
            RESULTxx = "iconst_2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 113:
            RESULT = null;
            RESULTxx = "iconst_3";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 114:
            RESULT = null;
            RESULTxx = "iconst_4";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 115:
            RESULT = null;
            RESULTxx = "iconst_5";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 116:
            RESULT = null;
            RESULTxx = "iconst_m1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 117:
            RESULT = null;
            RESULTxx = "idiv";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 118:
            RESULT = null;
            RESULTxx = "if_acmpeq";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 119:
            RESULT = null;
            RESULTxx = "if_acmpne";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 120:
            RESULT = null;
            RESULTxx = "if_icmpeq";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 121:
            RESULT = null;
            RESULTxx = "if_icmpge";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 122:
            RESULT = null;
            RESULTxx = "if_icmpgt";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 123:
            RESULT = null;
            RESULTxx = "if_icmple";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 124:
            RESULT = null;
            RESULTxx = "if_icmplt";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 125:
            RESULT = null;
            RESULTxx = "if_icmpne";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 126:
            RESULT = null;
            RESULTxx = "ifeq";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 127:
            RESULT = null;
            RESULTxx = "ifge";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 128:
            RESULT = null;
            RESULTxx = "ifgt";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 129:
            RESULT = null;
            RESULTxx = "ifle";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 130:
            RESULT = null;
            RESULTxx = "iflt";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 131:
            RESULT = null;
            RESULTxx = "ifne";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 132:
            RESULT = null;
            RESULTxx = "ifnonnull";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 133:
            RESULT = null;
            RESULTxx = "ifnull";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 134:
            RESULT = null;
            RESULTxx = "iinc";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 135:
            RESULT = null;
            RESULTxx = "iload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 136:
            RESULT = null;
            RESULTxx = "iload_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 137:
            RESULT = null;
            RESULTxx = "iload_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 138:
            RESULT = null;
            RESULTxx = "iload_2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 139:
            RESULT = null;
            RESULTxx = "iload_3";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 140:
            RESULT = null;
            RESULTxx = "imul";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 141:
            RESULT = null;
            RESULTxx = "ineg";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 142:
            RESULT = null;
            RESULTxx = "instanceof";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 143:
            RESULT = null;
            RESULTxx = "int2byte";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 144:
            RESULT = null;
            RESULTxx = "int2char";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 145:
            RESULT = null;
            RESULTxx = "int2short";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 146:
            RESULT = null;
            RESULTxx = "i2b";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 147:
            RESULT = null;
            RESULTxx = "i2c";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 148:
            RESULT = null;
            RESULTxx = "i2s";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 149:
            RESULT = null;
            RESULTxx = "invokeinterface";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 150:
            RESULT = null;
            RESULTxx = "invokenonvirtual";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 151:
            RESULT = null;
            RESULTxx = "invokespecial";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 152:
            RESULT = null;
            RESULTxx = "invokestatic";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 153:
            RESULT = null;
            RESULTxx = "invokevirtual";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 154:
            RESULT = null;
            RESULTxx = "invokedynamic";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 155:
            RESULT = null;
            RESULTxx = "ior";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 156:
            RESULT = null;
            RESULTxx = "irem";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 157:
            RESULT = null;
            RESULTxx = "ireturn";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 158:
            RESULT = null;
            RESULTxx = "ishl";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 159:
            RESULT = null;
            RESULTxx = "ishr";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 160:
            RESULT = null;
            RESULTxx = "istore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 161:
            RESULT = null;
            RESULTxx = "istore_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 162:
            RESULT = null;
            RESULTxx = "istore_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 163:
            RESULT = null;
            RESULTxx = "istore_2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 164:
            RESULT = null;
            RESULTxx = "istore_3";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 165:
            RESULT = null;
            RESULTxx = "isub";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 166:
            RESULT = null;
            RESULTxx = "iushr";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 167:
            RESULT = null;
            RESULTxx = "ixor";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 168:
            RESULT = null;
            RESULTxx = "jsr";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 169:
            RESULT = null;
            RESULTxx = "jsr_w";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 170:
            RESULT = null;
            RESULTxx = "l2d";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 171:
            RESULT = null;
            RESULTxx = "l2f";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 172:
            RESULT = null;
            RESULTxx = "l2i";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 173:
            RESULT = null;
            RESULTxx = "ladd";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 174:
            RESULT = null;
            RESULTxx = "laload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 175:
            RESULT = null;
            RESULTxx = "land";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 176:
            RESULT = null;
            RESULTxx = "lastore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 177:
            RESULT = null;
            RESULTxx = "lcmp";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 178:
            RESULT = null;
            RESULTxx = "lconst_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 179:
            RESULT = null;
            RESULTxx = "lconst_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 180:
            RESULT = null;
            RESULTxx = "ldc";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 181:
            RESULT = null;
            RESULTxx = "ldc_w";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 182:
            RESULT = null;
            RESULTxx = "ldc2_w";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 183:
            RESULT = null;
            RESULTxx = "ldiv";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 184:
            RESULT = null;
            RESULTxx = "lload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 185:
            RESULT = null;
            RESULTxx = "lload_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 186:
            RESULT = null;
            RESULTxx = "lload_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 187:
            RESULT = null;
            RESULTxx = "lload_2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 188:
            RESULT = null;
            RESULTxx = "lload_3";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 189:
            RESULT = null;
            RESULTxx = "lmul";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 190:
            RESULT = null;
            RESULTxx = "lneg";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 191:
            RESULT = null;
            RESULTxx = "lookupswitch";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 192:
            RESULT = null;
            RESULTxx = "lor";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 193:
            RESULT = null;
            RESULTxx = "lrem";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 194:
            RESULT = null;
            RESULTxx = "lreturn";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 195:
            RESULT = null;
            RESULTxx = "lshl";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 196:
            RESULT = null;
            RESULTxx = "lshr";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 197:
            RESULT = null;
            RESULTxx = "lstore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 198:
            RESULT = null;
            RESULTxx = "lstore_0";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 199:
            RESULT = null;
            RESULTxx = "lstore_1";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 200:
            RESULT = null;
            RESULTxx = "lstore_2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 201:
            RESULT = null;
            RESULTxx = "lstore_3";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 202:
            RESULT = null;
            RESULTxx = "lsub";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 203:
            RESULT = null;
            RESULTxx = "lushr";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 204:
            RESULT = null;
            RESULTxx = "lxor";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 205:
            RESULT = null;
            RESULTxx = "monitorenter";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 206:
            RESULT = null;
            RESULTxx = "monitorexit";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 207:
            RESULT = null;
            RESULTxx = "multianewarray";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 208:
            RESULT = null;
            RESULTxx = "new";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 209:
            RESULT = null;
            RESULTxx = "newarray";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 210:
            RESULT = null;
            RESULTxx = "nop";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 211:
            RESULT = null;
            RESULTxx = "pop";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 212:
            RESULT = null;
            RESULTxx = "pop2";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 213:
            RESULT = null;
            RESULTxx = "putfield";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 214:
            RESULT = null;
            RESULTxx = "putstatic";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 215:
            RESULT = null;
            RESULTxx = "ret";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 216:
            RESULT = null;
            RESULTxx = "ret_w";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 217:
            RESULT = null;
            RESULTxx = "return";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 218:
            RESULT = null;
            RESULTxx = "saload";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 219:
            RESULT = null;
            RESULTxx = "sastore";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 220:
            RESULT = null;
            RESULTxx = "sipush";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 221:
            RESULT = null;
            RESULTxx = "swap";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 222:
            RESULT = null;
            RESULTxx = "tableswitch";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 223:
            RESULT = null;
            RESULTxx = "wide";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("Insn", 0, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 224:
            RESULT = null;
            this.access_val = 0;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$0", 89, (Symbol)CUP$parser$stack.peek(), (Object)RESULT);
            return CUP$parser$result;
         case 225:
            RESULT = null;
            Short RESULTx = (Short)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            RESULTx = new Short(this.access_val);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access", 88, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 226:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_list", 8, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 227:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_list", 8, (Symbol)CUP$parser$stack.peek(), (Object)RESULT);
            return CUP$parser$result;
         case 228:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_items", 7, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 229:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_items", 7, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 230:
            RESULT = null;
            this.access_val = (short)(this.access_val | 1);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 231:
            RESULT = null;
            this.access_val = (short)(this.access_val | 2);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 232:
            RESULT = null;
            this.access_val = (short)(this.access_val | 4);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 233:
            RESULT = null;
            this.access_val = (short)(this.access_val | 8);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 234:
            RESULT = null;
            this.access_val = (short)(this.access_val | 16);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 235:
            RESULT = null;
            this.access_val = (short)(this.access_val | 32);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 236:
            RESULT = null;
            this.access_val = (short)(this.access_val | 64);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 237:
            RESULT = null;
            this.access_val = (short)(this.access_val | 128);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 238:
            RESULT = null;
            this.access_val = (short)(this.access_val | 256);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 239:
            RESULT = null;
            this.access_val = (short)(this.access_val | 512);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 240:
            RESULT = null;
            this.access_val = (short)(this.access_val | 1024);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 241:
            RESULT = null;
            this.access_val = (short)(this.access_val | 2048);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 242:
            RESULT = null;
            this.access_val = (short)(this.access_val | 8192);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 243:
            RESULT = null;
            this.access_val = (short)(this.access_val | 16384);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("access_item", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 244:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            label = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            this.classFile.setSuperClass(label);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("super_spec", 40, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 245:
            RESULT = null;
            this.classFile.setNoSuperClass();
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("super_spec", 40, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 246:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("impls", 19, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 247:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("impls", 19, (Symbol)CUP$parser$stack.peek(), (Object)RESULT);
            return CUP$parser$result;
         case 248:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("implements_list", 20, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 249:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("implements_list", 20, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 250:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            label = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            this.classFile.addInterface(label);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("implements_spec", 21, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 251:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.peek()).left;
            labelright = ((Symbol)CUP$parser$stack.peek()).right;
            annot_attr = ((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addClassDeprAttr(annot_attr);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_depr_attr", 71, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 252:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.peek()).left;
            labelright = ((Symbol)CUP$parser$stack.peek()).right;
            annot_attr = ((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addClassSigAttr(annot_attr);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_sig_attr", 72, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 253:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.peek()).left;
            labelright = ((Symbol)CUP$parser$stack.peek()).right;
            annot_attr = ((Symbol)CUP$parser$stack.peek()).value;
            if (annot_attr != null) {
               if (((VisibilityAnnotationAttr)annot_attr).getKind().equals("RuntimeVisible")) {
                  this.classFile.addClassAnnotAttrVisible(annot_attr);
               } else {
                  this.classFile.addClassAnnotAttrInvisible(annot_attr);
               }
            }

            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_annotation_attr", 79, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 254:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            annot_attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            Object annot_attr2 = ((Symbol)CUP$parser$stack.peek()).value;
            if (((VisibilityAnnotationAttr)annot_attr).getKind().equals("RuntimeVisible")) {
               this.classFile.addClassAnnotAttrVisible(annot_attr);
            } else {
               this.classFile.addClassAnnotAttrInvisible(annot_attr);
            }

            if (((VisibilityAnnotationAttr)annot_attr2).getKind().equals("RuntimeVisible")) {
               this.classFile.addClassAnnotAttrVisible(annot_attr2);
            } else {
               this.classFile.addClassAnnotAttrInvisible(annot_attr2);
            }

            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_annotation_attr", 79, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 255:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("fields", 17, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 256:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("fields", 17, (Symbol)CUP$parser$stack.peek(), (Object)RESULT);
            return CUP$parser$result;
         case 257:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("field_list", 15, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 258:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("field_list", 15, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 259:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("field_item", 58, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 260:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("field_item", 58, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 261:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).right;
            i = (Short)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).value;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).right;
            name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).value;
            sigleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
            sigright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
            sig = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
            vleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
            vright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
            v = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
            dep_attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            dep_attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            dep_attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            sig_attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            sig_attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            sig_attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            vis_annot_attr1left = ((Symbol)CUP$parser$stack.peek()).left;
            vis_annot_attr1right = ((Symbol)CUP$parser$stack.peek()).right;
            vis_annot_attr1 = ((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addField((short)i.intValue(), name, sig, v, dep_attr, sig_attr, vis_annot_attr1, (Object)null);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("field_spec", 16, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 8), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 262:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 8)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 8)).right;
            i = (Short)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 8)).value;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).right;
            name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).value;
            sigleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).left;
            sigright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).right;
            sig = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).value;
            vleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
            vright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
            v = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
            dep_attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            dep_attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            dep_attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            sig_attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            sig_attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            sig_attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            vis_annot_attr1left = ((Symbol)CUP$parser$stack.peek()).left;
            vis_annot_attr1right = ((Symbol)CUP$parser$stack.peek()).right;
            vis_annot_attr1 = ((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addField((short)i.intValue(), name, sig, v, "synth", dep_attr, sig_attr, vis_annot_attr1, (Object)null);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("field_spec", 16, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 9), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 263:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 8)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 8)).right;
            i = (Short)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 8)).value;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).right;
            name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).value;
            sigleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).left;
            sigright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).right;
            sig = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).value;
            vleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
            vright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
            v = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
            dep_attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
            dep_attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
            dep_attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
            sig_attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            sig_attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            sig_attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            vis_annot_attr1left = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            vis_annot_attr1right = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            vis_annot_attr1 = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            vis_annot_attr2left = ((Symbol)CUP$parser$stack.peek()).left;
            vis_annot_attr2right = ((Symbol)CUP$parser$stack.peek()).right;
            vis_annot_attr2 = ((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addField((short)i.intValue(), name, sig, v, dep_attr, sig_attr, vis_annot_attr1, vis_annot_attr2);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("field_spec", 16, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 9), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 264:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 9)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 9)).right;
            i = (Short)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 9)).value;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 8)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 8)).right;
            name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 8)).value;
            sigleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).left;
            sigright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).right;
            sig = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).value;
            vleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).left;
            vright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).right;
            v = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).value;
            dep_attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
            dep_attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
            dep_attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
            sig_attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            sig_attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            sig_attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            vis_annot_attr1left = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            vis_annot_attr1right = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            vis_annot_attr1 = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            vis_annot_attr2left = ((Symbol)CUP$parser$stack.peek()).left;
            vis_annot_attr2right = ((Symbol)CUP$parser$stack.peek()).right;
            vis_annot_attr2 = ((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addField((short)i.intValue(), name, sig, v, "synth", dep_attr, sig_attr, vis_annot_attr1, vis_annot_attr2);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("field_spec", 16, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 10), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 265:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.peek()).left;
            labelright = ((Symbol)CUP$parser$stack.peek()).right;
            annot_attr = ((Symbol)CUP$parser$stack.peek()).value;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("optional_default", 2, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), annot_attr);
            return CUP$parser$result;
         case 266:
            RESULT = null;
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("optional_default", 2, (Symbol)CUP$parser$stack.peek(), (Object)RESULT);
            return CUP$parser$result;
         case 267:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.peek()).left;
            labelright = ((Symbol)CUP$parser$stack.peek()).right;
            Integer ix = (Integer)((Symbol)CUP$parser$stack.peek()).value;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("item", 3, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), ix);
            return CUP$parser$result;
         case 268:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.peek()).left;
            labelright = ((Symbol)CUP$parser$stack.peek()).right;
            Number n = (Number)((Symbol)CUP$parser$stack.peek()).value;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("item", 3, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), n);
            return CUP$parser$result;
         case 269:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.peek()).left;
            labelright = ((Symbol)CUP$parser$stack.peek()).right;
            label = (String)((Symbol)CUP$parser$stack.peek()).value;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("item", 3, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), label);
            return CUP$parser$result;
         case 270:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("methods", 33, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 271:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("methods", 33, (Symbol)CUP$parser$stack.peek(), (Object)RESULT);
            return CUP$parser$result;
         case 272:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("method_list", 31, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 273:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("method_list", 31, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 274:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("method_item", 54, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 275:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("method_item", 54, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 276:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("method_spec", 32, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 277:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("method_spec", 32, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 278:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            i = (Short)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            String[] split = ScannerUtils.splitMethodSignature(name);
            this.classFile.newMethod(split[0], split[1], i.intValue());
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("defmethod", 12, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 279:
            RESULT = null;
            this.classFile.endMethod();
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("endmethod", 14, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 280:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("statements", 38, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 281:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("statements", 38, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 282:
            RESULT = null;
            this.classFile.setLine(this.scanner.token_line_num);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$1", 90, (Symbol)CUP$parser$stack.peek(), (Object)RESULT);
            return CUP$parser$result;
         case 283:
            RESULT = null;
            RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("statement", 37, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 284:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("stmnt", 39, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 285:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("stmnt", 39, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 286:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("stmnt", 39, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 287:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("stmnt", 39, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 288:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("stmnt", 39, (Symbol)CUP$parser$stack.peek(), (Object)RESULT);
            return CUP$parser$result;
         case 289:
            RESULT = null;
            labelleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            labelright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            label = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            this.classFile.plantLabel(label);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("label", 23, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 290:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 291:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 292:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 293:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 294:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 295:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 296:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 297:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 298:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 299:
            RESULT = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         default:
            throw new Exception("Invalid action number " + CUP$parser$act_num + "found in internal parse table");
         }
      }

      public final Symbol CUP$parser$do_action_part00000001(int CUP$parser$act_num, lr_parser CUP$parser$parser, Stack CUP$parser$stack, int CUP$parser$top) throws Exception {
         Symbol CUP$parser$result;
         ElemValPair RESULTx;
         int nameleft;
         int nameright;
         String w;
         int attrleft;
         int attrright;
         Object attr;
         int cnameleft;
         int cnameright;
         String cname;
         int dleft;
         int dright;
         String desc;
         Object kind;
         byte[] data;
         Integer v;
         ArrayList RESULT;
         Integer n2;
         String n3;
         Integer low;
         VisibilityAnnotationAttr RESULTxxx;
         Number n;
         String RESULTxxxx;
         switch(CUP$parser$act_num) {
         case 300:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 301:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 302:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("directive", 13, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 303:
            RESULTx = null;
            this.classFile.addMethSynthAttr();
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("meth_synth_attr", 68, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 304:
            RESULTx = null;
            this.classFile.addMethDeprAttr();
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("meth_depr_attr", 69, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 305:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            w = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addMethSigAttr(w);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("meth_sig_attr", 70, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 306:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
            kind = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            RESULTxxx = this.classFile.makeVisibilityAnnotation(kind, attr);
            if (kind.equals("RuntimeVisible")) {
               this.classFile.addMethAnnotAttrVisible(RESULTxxx);
            } else {
               this.classFile.addMethAnnotAttrInvisible(RESULTxxx);
            }

            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("meth_annotation_attr", 78, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULTxxx);
            return CUP$parser$result;
         case 307:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
            kind = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            Object RESULTxxxxx = this.classFile.makeParameterVisibilityAnnotation(kind, attr);
            if (kind.equals("RuntimeVisible")) {
               this.classFile.addMethParamAnnotAttrVisible(RESULTxxxxx);
            } else {
               this.classFile.addMethParamAnnotAttrInvisible(RESULTxxxxx);
            }

            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("meth_param_annotation_attr", 82, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5), (Symbol)CUP$parser$stack.peek(), RESULTxxxxx);
            return CUP$parser$result;
         case 308:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            kind = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            attr = ((Symbol)CUP$parser$stack.peek()).value;
            RESULT = this.classFile.mergeNewAnnotAttr(kind, attr);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("annotation_attr_list", 83, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 309:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            kind = ((Symbol)CUP$parser$stack.peek()).value;
            RESULT = this.classFile.makeNewAnnotAttrList(kind);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("annotation_attr_list", 83, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 310:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            kind = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            this.classFile.addMethAnnotDefault(kind);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("meth_annotation_default_attr", 81, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 311:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            desc = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addSootCodeAttr(w, desc);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("code_attr_expr", 50, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 312:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).right;
            low = (Integer)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
            cnameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
            cnameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
            cname = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
            dleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            dright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            n3 = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            int elableft = ((Symbol)CUP$parser$stack.peek()).left;
            int elabright = ((Symbol)CUP$parser$stack.peek()).right;
            String elab = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addVar(n3, elab, desc, cname, low);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("var_expr", 49, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 313:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
            low = (Integer)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            cnameleft = ((Symbol)CUP$parser$stack.peek()).left;
            cnameright = ((Symbol)CUP$parser$stack.peek()).right;
            cname = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addVar((String)null, (String)null, desc, cname, low);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("var_expr", 49, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 314:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            v = (Integer)((Symbol)CUP$parser$stack.peek()).value;
            if (!w.equals("locals") && !w.equals("vars")) {
               if (w.equals("stack")) {
                  this.classFile.setStackSize((short)v);
               } else {
                  this.classFile.report_error(".limit expected \"stack\" or \"locals\", but got " + w);
               }
            } else {
               this.classFile.setVarSize((short)v);
            }

            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("limit_expr", 24, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 315:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            low = (Integer)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addLine(low);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("line_expr", 43, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 316:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            w = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addThrow(w);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("throws_expr", 48, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 317:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
            cnameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            cnameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            cname = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            dleft = ((Symbol)CUP$parser$stack.peek()).left;
            dright = ((Symbol)CUP$parser$stack.peek()).right;
            n3 = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.addCatch(w, desc, cname, n3);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("catch_expr", 9, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 318:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            v = (Integer)((Symbol)CUP$parser$stack.peek()).value;
            this.scanner.dict.put(w, v);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("set_expr", 34, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 319:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            desc = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.scanner.dict.put(w, desc);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("set_expr", 34, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 320:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            n = (Number)((Symbol)CUP$parser$stack.peek()).value;
            this.scanner.dict.put(w, n);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("set_expr", 34, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 321:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            desc = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.scanner.dict.put(w, desc);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("set_expr", 34, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 322:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("instruction", 18, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 323:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("instruction", 18, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 324:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            w = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.plant(w);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 325:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            v = (Integer)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            cnameleft = ((Symbol)CUP$parser$stack.peek()).left;
            cnameright = ((Symbol)CUP$parser$stack.peek()).right;
            n2 = (Integer)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.plant(w, v, n2);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 326:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            v = (Integer)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.plant(w, v);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 327:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            n = (Number)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.plant(w, n);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 328:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            desc = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.plant(w, desc);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 329:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            cnameleft = ((Symbol)CUP$parser$stack.peek()).left;
            cnameright = ((Symbol)CUP$parser$stack.peek()).right;
            n2 = (Integer)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.plant(w, desc, n2);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 330:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            cnameleft = ((Symbol)CUP$parser$stack.peek()).left;
            cnameright = ((Symbol)CUP$parser$stack.peek()).right;
            cname = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.plant(w, desc, cname);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 331:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            cnameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            cnameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            cname = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            dleft = ((Symbol)CUP$parser$stack.peek()).left;
            dright = ((Symbol)CUP$parser$stack.peek()).right;
            n3 = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.plant(w, desc, cname, n3);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 332:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            desc = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.plantString(w, desc);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("simple_instruction", 35, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 333:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("complex_instruction", 11, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 334:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("complex_instruction", 11, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 335:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("lookup", 25, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 336:
            RESULTx = null;
            this.classFile.newLookupswitch();
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("lookup_args", 26, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 337:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("lookup_list_t", 29, (Symbol)CUP$parser$stack.peek(), (Object)RESULTx);
            return CUP$parser$result;
         case 338:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("lookup_list_t", 29, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 339:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("lookup_list", 30, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 340:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("lookup_list", 30, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 341:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
            low = (Integer)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            this.classFile.addLookupswitch(low, desc);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("lookup_entry", 28, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 342:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            w = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.endLookupswitch(w);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("lookup_default", 27, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 343:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("table", 41, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 344:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            low = (Integer)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            this.classFile.newTableswitch(low);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("table_args", 42, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 345:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            low = (Integer)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            v = (Integer)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            this.classFile.newTableswitch(low, v);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("table_args", 42, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 346:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("table_list_t", 46, (Symbol)CUP$parser$stack.peek(), (Object)RESULTx);
            return CUP$parser$result;
         case 347:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("table_list_t", 46, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 348:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("table_list", 47, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 349:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("table_list", 47, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 350:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            this.classFile.addTableswitch(w);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("table_entry", 45, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 351:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            w = (String)((Symbol)CUP$parser$stack.peek()).value;
            this.classFile.endTableswitch(w);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("table_default", 44, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 352:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_attrs", 51, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 353:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_attrs", 51, (Symbol)CUP$parser$stack.peek(), (Object)RESULTx);
            return CUP$parser$result;
         case 354:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_attr_list", 52, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 355:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_attr_list", 52, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 356:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            data = Base64.decode(desc.toCharArray());
            this.classFile.addGenericAttrToClass(new GenericAttr(w, data));
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_attr_spec", 53, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 357:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("method_attrs", 55, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 358:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("method_attr_list", 56, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 359:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("method_attr_list", 56, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 360:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            data = Base64.decode(desc.toCharArray());
            this.classFile.addGenericAttrToMethod(w, data);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("method_attr_spec", 57, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 361:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("field_attrs", 59, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 362:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("field_attr_list", 60, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 363:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("field_attr_list", 60, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 364:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            data = Base64.decode(desc.toCharArray());
            this.classFile.addGenericAttrToField(w, data);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("field_attr_spec", 61, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 365:
            RESULTx = null;
            this.classFile.addInnerClassAttr();
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("inner_class_attr", 62, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 366:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("inner_class_attr", 62, (Symbol)CUP$parser$stack.peek(), (Object)RESULTx);
            return CUP$parser$result;
         case 367:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("inner_class_attr_list", 63, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 368:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("inner_class_attr_list", 63, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 369:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
            cnameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
            cnameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
            cname = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
            dleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
            dright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
            Short d = (Short)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
            this.classFile.addInnerClassSpec(w, desc, cname, (short)d.intValue());
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("inner_class_attr_spec", 64, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 370:
            RESULTx = null;
            this.classFile.endInnerClassAttr();
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("end_inner_class_attr", 65, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 371:
            RESULTx = null;
            this.classFile.addClassSynthAttr();
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("synth_attr", 66, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 372:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("synth_attr", 66, (Symbol)CUP$parser$stack.peek(), (Object)RESULTx);
            return CUP$parser$result;
         case 373:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            cnameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            cnameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            cname = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            this.classFile.addEnclMethAttr(w, desc, cname);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("encl_meth_attr", 67, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 374:
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("encl_meth_attr", 67, (Symbol)CUP$parser$stack.peek(), (Object)RESULTx);
            return CUP$parser$result;
         case 375:
            RESULTx = null;
            RESULTxxxx = "deprecated";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("deprecated_attr", 4, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULTxxxx);
            return CUP$parser$result;
         case 376:
            RESULTx = null;
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("deprecated_attr", 4, (Symbol)CUP$parser$stack.peek(), (Object)RESULTx);
            return CUP$parser$result;
         case 377:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("signature_attr", 5, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), w);
            return CUP$parser$result;
         case 378:
            RESULTx = null;
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("signature_attr", 5, (Symbol)CUP$parser$stack.peek(), (Object)RESULTx);
            return CUP$parser$result;
         case 379:
            RESULTx = null;
            RESULTxxxx = "RuntimeVisible";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("visibility_type", 73, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxxxx);
            return CUP$parser$result;
         case 380:
            RESULTx = null;
            RESULTxxxx = "RuntimeInvisible";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("visibility_type", 73, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxxxx);
            return CUP$parser$result;
         case 381:
            RESULTx = null;
            RESULTxxxx = "RuntimeVisibleParameter";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("param_visibility_type", 84, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxxxx);
            return CUP$parser$result;
         case 382:
            RESULTx = null;
            RESULTxxxx = "RuntimeInvisibleParameter";
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("param_visibility_type", 84, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULTxxxx);
            return CUP$parser$result;
         case 383:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            kind = ((Symbol)CUP$parser$stack.peek()).value;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("annotation_attr_opt", 85, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), kind);
            return CUP$parser$result;
         case 384:
            RESULTx = null;
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("annotation_attr_opt", 85, (Symbol)CUP$parser$stack.peek(), (Object)RESULTx);
            return CUP$parser$result;
         case 385:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
            kind = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
            attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
            RESULTxxx = this.classFile.makeVisibilityAnnotation(kind, attr);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("annotation_attr", 74, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5), (Symbol)CUP$parser$stack.peek(), RESULTxxx);
            return CUP$parser$result;
         case 386:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            kind = ((Symbol)CUP$parser$stack.peek()).value;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("annotation_list_opt", 86, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), kind);
            return CUP$parser$result;
         case 387:
            RESULTx = null;
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("annotation_list_opt", 86, (Symbol)CUP$parser$stack.peek(), (Object)RESULTx);
            return CUP$parser$result;
         case 388:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            kind = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            attr = ((Symbol)CUP$parser$stack.peek()).value;
            RESULT = this.classFile.mergeNewAnnotation(kind, attr);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("annotation_list", 75, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 389:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            kind = ((Symbol)CUP$parser$stack.peek()).value;
            RESULT = this.classFile.makeNewAnnotationList(kind);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("annotation_list", 75, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 390:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
            attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
            Object RESULTxx = this.classFile.makeAnnotation(w, attr);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("annotation", 76, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 6), (Symbol)CUP$parser$stack.peek(), RESULTxx);
            return CUP$parser$result;
         case 391:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            kind = ((Symbol)CUP$parser$stack.peek()).value;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair_list_opt", 87, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), kind);
            return CUP$parser$result;
         case 392:
            RESULTx = null;
            RESULTx = null;
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair_list_opt", 87, (Symbol)CUP$parser$stack.peek(), (Object)RESULTx);
            return CUP$parser$result;
         case 393:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            kind = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            attrleft = ((Symbol)CUP$parser$stack.peek()).left;
            attrright = ((Symbol)CUP$parser$stack.peek()).right;
            attr = ((Symbol)CUP$parser$stack.peek()).value;
            RESULT = this.classFile.mergeNewElemValPair(kind, attr);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair_list", 77, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 394:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.peek()).left;
            nameright = ((Symbol)CUP$parser$stack.peek()).right;
            kind = ((Symbol)CUP$parser$stack.peek()).value;
            RESULT = this.classFile.makeNewElemValPairList(kind);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair_list", 77, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
            return CUP$parser$result;
         case 395:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            v = (Integer)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            RESULTx = this.classFile.makeConstantElem(w, 'I', v);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 396:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            v = (Integer)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            RESULTx = this.classFile.makeConstantElem(w, 'S', v);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 397:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            v = (Integer)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            RESULTx = this.classFile.makeConstantElem(w, 'B', v);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 398:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            v = (Integer)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            RESULTx = this.classFile.makeConstantElem(w, 'C', v);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 399:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            v = (Integer)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            RESULTx = this.classFile.makeConstantElem(w, 'Z', v);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 400:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            RESULTx = this.classFile.makeConstantElem(w, 'J', attr);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 401:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            RESULTx = this.classFile.makeConstantElem(w, 'F', attr);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 402:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            RESULTx = this.classFile.makeConstantElem(w, 'D', attr);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 403:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            RESULTx = this.classFile.makeConstantElem(w, 's', desc);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 404:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            cnameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            cnameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            cname = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            RESULTx = this.classFile.makeEnumElem(w, 'e', desc, cname);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 405:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
            desc = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
            RESULTx = this.classFile.makeClassElem(w, 'c', desc);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 406:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
            attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
            RESULTx = this.classFile.makeArrayElem(w, '[', attr);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         case 407:
            RESULTx = null;
            nameleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).left;
            nameright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).right;
            w = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 5)).value;
            attrleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).left;
            attrright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).right;
            attr = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
            RESULTx = this.classFile.makeAnnotElem(w, '@', attr);
            CUP$parser$result = this.parser.getSymbolFactory().newSymbol("elem_val_pair", 80, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 7), (Symbol)CUP$parser$stack.peek(), RESULTx);
            return CUP$parser$result;
         default:
            throw new Exception("Invalid action number " + CUP$parser$act_num + "found in internal parse table");
         }
      }

      public final Symbol CUP$parser$do_action(int CUP$parser$act_num, lr_parser CUP$parser$parser, Stack CUP$parser$stack, int CUP$parser$top) throws Exception {
         switch(CUP$parser$act_num / 300) {
         case 0:
            return this.CUP$parser$do_action_part00000000(CUP$parser$act_num, CUP$parser$parser, CUP$parser$stack, CUP$parser$top);
         case 1:
            return this.CUP$parser$do_action_part00000001(CUP$parser$act_num, CUP$parser$parser, CUP$parser$stack, CUP$parser$top);
         default:
            throw new Exception("Invalid action number found in internal parse table");
         }
      }
   }
}
