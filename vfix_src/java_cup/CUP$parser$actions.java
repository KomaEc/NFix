package java_cup;

import java.util.Hashtable;
import java.util.Stack;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;

class CUP$parser$actions {
   protected final int MAX_RHS = 200;
   protected production_part[] rhs_parts = new production_part[200];
   protected int rhs_pos = 0;
   protected String multipart_name = new String();
   protected Stack multipart_names = new Stack();
   protected Hashtable symbols = new Hashtable();
   protected Hashtable non_terms = new Hashtable();
   protected non_terminal start_nt = null;
   protected non_terminal lhs_nt;
   int _cur_prec = 0;
   int _cur_side = -1;
   private int cur_debug_id = 0;
   private final parser parser;

   protected production_part add_lab(production_part part, String lab) throws internal_error {
      return (production_part)(lab != null && !part.is_action() ? new symbol_part(((symbol_part)part).the_symbol(), lab) : part);
   }

   protected void new_rhs() {
      this.rhs_pos = 0;
   }

   protected void add_rhs_part(production_part part) throws Exception {
      if (this.rhs_pos >= 200) {
         throw new Exception("Internal Error: Productions limited to 200 symbols and actions");
      } else {
         this.rhs_parts[this.rhs_pos] = part;
         ++this.rhs_pos;
      }
   }

   protected void update_precedence(int p) {
      this._cur_side = p;
      ++this._cur_prec;
   }

   protected void add_precedence(String term) {
      if (term == null) {
         System.err.println("Unable to add precedence to nonexistent terminal");
      } else {
         symbol_part sp = (symbol_part)this.symbols.get(term);
         if (sp == null) {
            System.err.println("Could find terminal " + term + " while declaring precedence");
         } else {
            symbol sym = sp.the_symbol();
            if (sym instanceof terminal) {
               ((terminal)sym).set_precedence(this._cur_side, this._cur_prec);
            } else {
               System.err.println("Precedence declaration: Can't find terminal " + term);
            }
         }
      }

   }

   public int get_new_debug_id() {
      return this.cur_debug_id++;
   }

   public String attach_debug_symbol(int id, String code) {
      return !this.parser.debugSymbols ? code : "//@@CUPDBG" + id + "\n" + code;
   }

   CUP$parser$actions(parser parser) {
      this.parser = parser;
   }

   public final Symbol CUP$parser$do_action(int CUP$parser$act_num, lr_parser CUP$parser$parser, Stack CUP$parser$stack, int CUP$parser$top) throws Exception {
      Symbol CUP$parser$result;
      String RESULT;
      int the_idleft;
      int the_idright;
      String term_name;
      non_terminal this_nt;
      int argright;
      String labid;
      int argleft;
      Object RESULT;
      switch(CUP$parser$act_num) {
      case 0:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
         the_idright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
         Object start_val = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("$START", 0, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), start_val);
         CUP$parser$parser.done_parsing();
         return CUP$parser$result;
      case 1:
         RESULT = null;
         this.symbols.put("error", new symbol_part(terminal.error));
         this.non_terms.put("$START", non_terminal.START_nt);
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$0", 46, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 2:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("spec", 0, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 8), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 3:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("spec", 0, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 4:
         RESULT = null;
         emit.package_name = this.multipart_name;
         this.multipart_name = new String();
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$1", 47, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 5:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("package_spec", 1, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 6:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("package_spec", 1, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 7:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("import_list", 2, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 8:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("import_list", 2, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 9:
         RESULT = null;
         emit.import_list.push(this.multipart_name);
         this.multipart_name = new String();
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$2", 48, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 10:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("import_spec", 13, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 11:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_name", 35, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 12:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
         the_idright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
         term_name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         emit.parser_class_name = term_name;
         emit.symbol_const_class_name = term_name + "Sym";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("class_name", 35, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 13:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("code_part", 5, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 14:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("code_part", 5, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 15:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("code_part", 5, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 16:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("code_part", 5, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 17:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("code_parts", 4, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 18:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("code_parts", 4, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 19:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
         the_idright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
         term_name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         if (emit.action_code != null) {
            ErrorManager.getManager().emit_warning("Redundant action code (skipping)");
         } else {
            emit.action_code = this.attach_debug_symbol(this.get_new_debug_id(), term_name);
         }

         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("action_code_part", 3, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 20:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
         the_idright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
         term_name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         if (emit.parser_code != null) {
            ErrorManager.getManager().emit_warning("Redundant parser code (skipping)");
         } else {
            emit.parser_code = this.attach_debug_symbol(this.get_new_debug_id(), term_name);
         }

         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("parser_code_part", 8, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 21:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
         the_idright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
         term_name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         if (emit.init_code != null) {
            ErrorManager.getManager().emit_warning("Redundant init code (skipping)");
         } else {
            emit.init_code = this.attach_debug_symbol(this.get_new_debug_id(), term_name);
         }

         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("init_code", 15, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 22:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
         the_idright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
         term_name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         if (emit.scan_code != null) {
            ErrorManager.getManager().emit_warning("Redundant scan code (skipping)");
         } else {
            emit.scan_code = this.attach_debug_symbol(this.get_new_debug_id(), term_name);
         }

         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("scan_code", 16, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 23:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("symbol_list", 9, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 24:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("symbol_list", 9, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 25:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("symbol", 17, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 26:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("symbol", 17, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 27:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("symbol", 17, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 28:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("symbol", 17, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 29:
         RESULT = null;
         this.multipart_name = new String();
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$3", 49, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 30:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("symbol", 17, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 31:
         RESULT = null;
         this.multipart_name = new String();
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$4", 50, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 32:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("symbol", 17, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 33:
         RESULT = null;
         this.multipart_name = new String();
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$5", 51, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 34:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("declares_term", 33, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 35:
         RESULT = null;
         this.multipart_name = new String();
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$6", 52, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 36:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("declares_non_term", 34, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 37:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("term_name_list", 19, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 38:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("term_name_list", 19, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 39:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("non_term_name_list", 20, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 40:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("non_term_name_list", 20, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 41:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("precedence_list", 29, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 42:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("precedence_list", 29, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 43:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("precedence_l", 32, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 44:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("precedence_l", 32, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 45:
         RESULT = null;
         this.update_precedence(0);
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$7", 53, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 46:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("preced", 30, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 47:
         RESULT = null;
         this.update_precedence(1);
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$8", 54, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 48:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("preced", 30, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 49:
         RESULT = null;
         this.update_precedence(2);
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$9", 55, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 50:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("preced", 30, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 51:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("terminal_list", 31, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 52:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("terminal_list", 31, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 53:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         this.add_precedence(term_name);
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("terminal_id", 40, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), term_name);
         return CUP$parser$result;
      case 54:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         if (this.symbols.get(term_name) == null) {
            ErrorManager.getManager().emit_error("Terminal \"" + term_name + "\" has not been declared");
         }

         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("term_id", 41, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), term_name);
         return CUP$parser$result;
      case 55:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         this_nt = (non_terminal)this.non_terms.get(term_name);
         if (this_nt == null) {
            ErrorManager.getManager().emit_error("Start non terminal \"" + term_name + "\" has not been declared");
         } else {
            this.start_nt = this_nt;
            this.new_rhs();
            this.add_rhs_part(this.add_lab(new symbol_part(this.start_nt), "start_val"));
            this.add_rhs_part(new symbol_part(terminal.EOF));
            if (!emit._xmlactions) {
               this.add_rhs_part(new action_part("RESULT = start_val;"));
            }

            emit.start_production = new production(non_terminal.START_nt, this.rhs_parts, this.rhs_pos);
            this.new_rhs();
         }

         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$10", 56, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 56:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         the_idleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
         the_idright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
         term_name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("start_spec", 10, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 57:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("start_spec", 10, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 58:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("production_list", 11, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 59:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("production_list", 11, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 60:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         this.lhs_nt = (non_terminal)this.non_terms.get(term_name);
         if (this.lhs_nt == null && ErrorManager.getManager().getErrorCount() == 0) {
            ErrorManager.getManager().emit_warning("LHS non terminal \"" + term_name + "\" has not been declared");
         }

         this.new_rhs();
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$11", 57, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 61:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
         the_idleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).left;
         the_idright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).right;
         term_name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("production", 21, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 62:
         RESULT = null;
         ErrorManager.getManager().emit_error("Syntax Error");
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$12", 58, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 63:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("production", 21, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 64:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("rhs_list", 26, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 65:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("rhs_list", 26, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 66:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         this_nt = null;
         if (this.lhs_nt != null) {
            symbol sym;
            if (term_name == null) {
               System.err.println("No terminal for contextual precedence");
               sym = null;
            } else {
               sym = ((symbol_part)this.symbols.get(term_name)).the_symbol();
            }

            if (sym != null && sym instanceof terminal) {
               new production(this.lhs_nt, this.rhs_parts, this.rhs_pos, ((terminal)sym).precedence_num(), ((terminal)sym).precedence_side());
               ((symbol_part)this.symbols.get(term_name)).the_symbol().note_use();
            } else {
               System.err.println("Invalid terminal " + term_name + " for contextual precedence assignment");
               new production(this.lhs_nt, this.rhs_parts, this.rhs_pos);
            }

            if (this.start_nt == null) {
               this.start_nt = this.lhs_nt;
               this.new_rhs();
               this.add_rhs_part(this.add_lab(new symbol_part(this.start_nt), "start_val"));
               this.add_rhs_part(new symbol_part(terminal.EOF));
               if (!emit._xmlactions) {
                  this.add_rhs_part(new action_part("RESULT = start_val;"));
               }

               if (sym != null && sym instanceof terminal) {
                  emit.start_production = new production(non_terminal.START_nt, this.rhs_parts, this.rhs_pos, ((terminal)sym).precedence_num(), ((terminal)sym).precedence_side());
               } else {
                  emit.start_production = new production(non_terminal.START_nt, this.rhs_parts, this.rhs_pos);
               }

               this.new_rhs();
            }
         }

         this.new_rhs();
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("rhs", 27, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 67:
         RESULT = null;
         if (this.lhs_nt != null) {
            new production(this.lhs_nt, this.rhs_parts, this.rhs_pos);
            if (this.start_nt == null) {
               this.start_nt = this.lhs_nt;
               this.new_rhs();
               this.add_rhs_part(this.add_lab(new symbol_part(this.start_nt), "start_val"));
               this.add_rhs_part(new symbol_part(terminal.EOF));
               if (!emit._xmlactions) {
                  this.add_rhs_part(new action_part("RESULT = start_val;"));
               }

               emit.start_production = new production(non_terminal.START_nt, this.rhs_parts, this.rhs_pos);
               this.new_rhs();
            }
         }

         this.new_rhs();
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("rhs", 27, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 68:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("prod_part_list", 22, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 69:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("prod_part_list", 22, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 70:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
         the_idright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
         term_name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         argleft = ((Symbol)CUP$parser$stack.peek()).left;
         argright = ((Symbol)CUP$parser$stack.peek()).right;
         labid = (String)((Symbol)CUP$parser$stack.peek()).value;
         production_part symb = (production_part)this.symbols.get(term_name);
         if (symb == null) {
            if (ErrorManager.getManager().getErrorCount() == 0) {
               ErrorManager.getManager().emit_error("java_cup.runtime.Symbol \"" + term_name + "\" has not been declared");
            }
         } else {
            this.add_rhs_part(this.add_lab(symb, labid));
         }

         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("prod_part", 23, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 71:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         this.add_rhs_part(new action_part(this.attach_debug_symbol(this.get_new_debug_id(), term_name)));
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("prod_part", 23, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 72:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("opt_label", 39, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), term_name);
         return CUP$parser$result;
      case 73:
         RESULT = null;
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("opt_label", 39, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 74:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         this.multipart_name = this.multipart_name.concat("." + term_name);
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("multipart_id", 12, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 75:
         RESULT = null;
         this.multipart_names.push(this.multipart_name);
         this.multipart_name = "";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("NT$13", 59, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 76:
         RESULT = null;
         RESULT = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 3)).value;
         the_idleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).left;
         the_idright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).right;
         term_name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1)).value;
         this.multipart_name = ((String)this.multipart_names.pop()).concat("<" + term_name + ">");
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("multipart_id", 12, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 4), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 77:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         this.multipart_name = this.multipart_name.concat(term_name);
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("multipart_id", 12, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 78:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("typearglist", 43, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), term_name);
         return CUP$parser$result;
      case 79:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).left;
         the_idright = ((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).right;
         term_name = (String)((Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2)).value;
         argleft = ((Symbol)CUP$parser$stack.peek()).left;
         argright = ((Symbol)CUP$parser$stack.peek()).right;
         labid = (String)((Symbol)CUP$parser$stack.peek()).value;
         RESULT = term_name + "," + labid;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("typearglist", 43, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 80:
         RESULT = null;
         RESULT = this.multipart_name;
         this.multipart_name = new String();
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("typearguement", 44, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 81:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("typearguement", 44, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), term_name);
         return CUP$parser$result;
      case 82:
         RESULT = null;
         RESULT = " ? ";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("wildcard", 45, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 83:
         RESULT = null;
         RESULT = " ? extends " + this.multipart_name;
         this.multipart_name = new String();
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("wildcard", 45, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 84:
         RESULT = null;
         RESULT = " ? super " + this.multipart_name;
         this.multipart_name = new String();
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("wildcard", 45, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 85:
         RESULT = null;
         this.multipart_name = this.multipart_name.concat(".*");
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("import_id", 14, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 86:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("import_id", 14, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 87:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("type_id", 18, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 88:
         RESULT = null;
         this.multipart_name = this.multipart_name.concat("[]");
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("type_id", 18, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 2), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 89:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         if (this.symbols.get(term_name) != null) {
            ErrorManager.getManager().emit_error("java_cup.runtime.Symbol \"" + term_name + "\" has already been declared");
         } else {
            if (this.multipart_name.equals("")) {
               this.multipart_name = "Object";
            }

            this.symbols.put(term_name, new symbol_part(new terminal(term_name, this.multipart_name)));
         }

         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("new_term_id", 24, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 90:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         if (this.symbols.get(term_name) != null) {
            ErrorManager.getManager().emit_error("java_cup.runtime.Symbol \"" + term_name + "\" has already been declared");
         } else {
            if (this.multipart_name.equals("")) {
               this.multipart_name = "Object";
            }

            this_nt = new non_terminal(term_name, this.multipart_name);
            this.non_terms.put(term_name, this_nt);
            this.symbols.put(term_name, new symbol_part(this_nt));
         }

         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("new_non_term_id", 25, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 91:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("nt_id", 36, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), term_name);
         return CUP$parser$result;
      case 92:
         RESULT = null;
         ErrorManager.getManager().emit_error("Illegal use of reserved word");
         RESULT = "ILLEGAL";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("nt_id", 36, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 93:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("symbol_id", 37, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), term_name);
         return CUP$parser$result;
      case 94:
         RESULT = null;
         ErrorManager.getManager().emit_error("Illegal use of reserved word");
         RESULT = "ILLEGAL";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("symbol_id", 37, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 95:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("label_id", 38, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), term_name);
         return CUP$parser$result;
      case 96:
         RESULT = null;
         the_idleft = ((Symbol)CUP$parser$stack.peek()).left;
         the_idright = ((Symbol)CUP$parser$stack.peek()).right;
         term_name = (String)((Symbol)CUP$parser$stack.peek()).value;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), term_name);
         return CUP$parser$result;
      case 97:
         RESULT = null;
         RESULT = "code";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 98:
         RESULT = null;
         RESULT = "action";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 99:
         RESULT = null;
         RESULT = "parser";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 100:
         RESULT = null;
         RESULT = "terminal";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 101:
         RESULT = null;
         RESULT = "non";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 102:
         RESULT = null;
         RESULT = "nonterminal";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 103:
         RESULT = null;
         RESULT = "init";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 104:
         RESULT = null;
         RESULT = "scan";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 105:
         RESULT = null;
         RESULT = "with";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 106:
         RESULT = null;
         RESULT = "start";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 107:
         RESULT = null;
         RESULT = "precedence";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 108:
         RESULT = null;
         RESULT = "left";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 109:
         RESULT = null;
         RESULT = "right";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 110:
         RESULT = null;
         RESULT = "nonassoc";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 111:
         RESULT = null;
         ErrorManager.getManager().emit_error("Illegal use of reserved word");
         RESULT = "ILLEGAL";
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("robust_id", 42, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 112:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("non_terminal", 7, (Symbol)CUP$parser$stack.elementAt(CUP$parser$top - 1), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 113:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("non_terminal", 7, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 114:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("opt_semi", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 115:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("opt_semi", 6, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      case 116:
         RESULT = null;
         CUP$parser$result = this.parser.getSymbolFactory().newSymbol("empty", 28, (Symbol)CUP$parser$stack.peek(), (Symbol)CUP$parser$stack.peek(), RESULT);
         return CUP$parser$result;
      default:
         throw new Exception("Invalid action number found in internal parse table");
      }
   }
}
