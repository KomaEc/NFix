package beaver;

import java.io.IOException;

public abstract class Parser {
   protected final ParsingTables tables;
   protected final short accept_action_id;
   protected short[] states;
   protected int top;
   protected Symbol[] _symbols;
   protected Parser.Events report;

   protected Parser(ParsingTables tables) {
      this.tables = tables;
      this.accept_action_id = (short)(~tables.rule_infos.length);
      this.states = new short[256];
   }

   public Object parse(Scanner source) throws IOException, Parser.Exception {
      this.init();
      return this.parse(new Parser.TokenStream(source));
   }

   public Object parse(Scanner source, short alt_goal_marker_id) throws IOException, Parser.Exception {
      this.init();
      Parser.TokenStream in = new Parser.TokenStream(source, new Symbol(alt_goal_marker_id));
      return this.parse(in);
   }

   private Object parse(Parser.TokenStream in) throws IOException, Parser.Exception {
      while(true) {
         Symbol token = in.nextToken();

         while(true) {
            short act = this.tables.findParserAction(this.states[this.top], token.id);
            if (act > 0) {
               this.shift(token, act);
               break;
            }

            Symbol nt;
            if (act == this.accept_action_id) {
               nt = this._symbols[this.top];
               this._symbols = null;
               return nt.value;
            }

            if (act >= 0) {
               this.report.syntaxError(token);
               this.recoverFromError(token, in);
               break;
            }

            nt = this.reduce(~act);
            act = this.tables.findNextState(this.states[this.top], nt.id);
            if (act <= 0) {
               if (act == this.accept_action_id) {
                  this._symbols = null;
                  return nt.value;
               }

               throw new IllegalStateException("Cannot shift a nonterminal");
            }

            this.shift(nt, act);
         }
      }
   }

   protected abstract Symbol invokeReduceAction(int var1, int var2);

   private void init() {
      if (this.report == null) {
         this.report = new Parser.Events();
      }

      this._symbols = new Symbol[this.states.length];
      this.top = 0;
      this._symbols[this.top] = new Symbol("none");
      this.states[this.top] = 1;
   }

   private void increaseStackCapacity() {
      short[] new_states = new short[this.states.length * 2];
      System.arraycopy(this.states, 0, new_states, 0, this.states.length);
      this.states = new_states;
      Symbol[] new_stack = new Symbol[this.states.length];
      System.arraycopy(this._symbols, 0, new_stack, 0, this._symbols.length);
      this._symbols = new_stack;
   }

   private void shift(Symbol sym, short goto_state) {
      if (++this.top == this.states.length) {
         this.increaseStackCapacity();
      }

      this._symbols[this.top] = sym;
      this.states[this.top] = goto_state;
   }

   private Symbol reduce(int rule_id) {
      int rule_info = this.tables.rule_infos[rule_id];
      int rhs_size = rule_info & '\uffff';
      this.top -= rhs_size;
      Symbol lhs_sym = this.invokeReduceAction(rule_id, this.top);
      lhs_sym.id = (short)(rule_info >>> 16);
      if (rhs_size == 0) {
         lhs_sym.start = lhs_sym.end = this._symbols[this.top].end;
      } else {
         lhs_sym.start = this._symbols[this.top + 1].start;
         lhs_sym.end = this._symbols[this.top + rhs_size].end;
      }

      return lhs_sym;
   }

   protected void recoverFromError(Symbol token, Parser.TokenStream in) throws IOException, Parser.Exception {
      if (token.id == 0) {
         throw new Parser.Exception("Cannot recover from the syntax error");
      } else {
         Parser.Simulator sim = new Parser.Simulator();
         in.alloc(3);
         short current_state = this.states[this.top];
         Symbol term;
         if (!this.tables.compressed) {
            short first_term_id = this.tables.findFirstTerminal(current_state);
            if (first_term_id >= 0) {
               term = new Symbol(first_term_id, this._symbols[this.top].end, token.start);
               in.insert(term, token);
               in.rewind();
               if (sim.parse(in)) {
                  in.rewind();
                  this.report.missingTokenInserted(term);
                  return;
               }

               int offset = this.tables.actn_offsets[current_state];

               short term_id;
               int index;
               for(term_id = (short)(first_term_id + 1); term_id < this.tables.n_term; ++term_id) {
                  index = offset + term_id;
                  if (index >= this.tables.lookaheads.length) {
                     break;
                  }

                  if (this.tables.lookaheads[index] == term_id) {
                     term.id = term_id;
                     in.rewind();
                     if (sim.parse(in)) {
                        in.rewind();
                        this.report.missingTokenInserted(term);
                        return;
                     }
                  }
               }

               in.remove(1);
               term.start = token.start;
               term.end = token.end;

               for(term_id = first_term_id; term_id < this.tables.n_term; ++term_id) {
                  index = offset + term_id;
                  if (index >= this.tables.lookaheads.length) {
                     break;
                  }

                  if (this.tables.lookaheads[index] == term_id) {
                     term.id = term_id;
                     in.rewind();
                     if (sim.parse(in)) {
                        in.rewind();
                        this.report.misspelledTokenReplaced(term);
                        return;
                     }
                  }
               }

               in.remove(0);
            }
         }

         if (sim.parse(in)) {
            in.rewind();
            this.report.unexpectedTokenRemoved(token);
         } else {
            Symbol first_sym = token;
            term = token;

            do {
               short goto_state;
               if ((goto_state = this.tables.findNextState(this.states[this.top], this.tables.error_symbol_id)) > 0) {
                  Symbol error = new Symbol(this.tables.error_symbol_id, first_sym.start, token.end);
                  this.shift(error, goto_state);
                  in.rewind();

                  while(!sim.parse(in)) {
                     term = in.remove(0);
                     if (term.id == 0) {
                        throw new Parser.Exception("Cannot recover from the syntax error");
                     }

                     in.rewind();
                  }

                  error.end = term.end;
                  in.rewind();
                  this.report.errorPhraseRemoved(error);
                  return;
               }

               first_sym = this._symbols[this.top];
            } while(--this.top >= 0);

            throw new Parser.Exception("Cannot recover from the syntax error");
         }
      }
   }

   public class Simulator {
      private short[] states;
      private int top;
      private int min_top;

      public boolean parse(Parser.TokenStream in) throws IOException {
         this.initStack();

         do {
            Symbol token = in.nextToken();

            while(true) {
               short act = Parser.this.tables.findParserAction(this.states[this.top], token.id);
               if (act > 0) {
                  this.shift(act);
                  break;
               }

               if (act == Parser.this.accept_action_id) {
                  return true;
               }

               if (act >= 0) {
                  return false;
               }

               short nt_id = this.reduce(~act);
               act = Parser.this.tables.findNextState(this.states[this.top], nt_id);
               if (act <= 0) {
                  return act == Parser.this.accept_action_id;
               }

               this.shift(act);
            }
         } while(!in.isFull());

         return true;
      }

      private void initStack() throws IOException {
         if (this.states == null || this.states.length < Parser.this.states.length) {
            this.states = new short[Parser.this.states.length];
            this.min_top = 0;
         }

         System.arraycopy(Parser.this.states, this.min_top, this.states, this.min_top, (this.top = Parser.this.top) + 1);
      }

      private void increaseStackCapacity() {
         short[] new_states = new short[this.states.length * 2];
         System.arraycopy(this.states, 0, new_states, 0, this.states.length);
         this.states = new_states;
      }

      private void shift(short state) {
         if (++this.top == this.states.length) {
            this.increaseStackCapacity();
         }

         this.states[this.top] = state;
      }

      private short reduce(int rule_id) {
         int rule_info = Parser.this.tables.rule_infos[rule_id];
         int rhs_size = rule_info & '\uffff';
         this.top -= rhs_size;
         this.min_top = Math.min(this.min_top, this.top);
         return (short)(rule_info >>> 16);
      }
   }

   public class TokenStream {
      private Scanner scanner;
      private Symbol[] buffer;
      private int n_marked;
      private int n_read;
      private int n_written;

      public TokenStream(Scanner scanner) {
         this.scanner = scanner;
      }

      public TokenStream(Scanner scanner, Symbol first_symbol) {
         this(scanner);
         this.alloc(1);
         this.buffer[0] = first_symbol;
         ++this.n_written;
      }

      public Symbol nextToken() throws IOException {
         if (this.buffer != null) {
            if (this.n_read < this.n_written) {
               return this.buffer[this.n_read++];
            }

            if (this.n_written < this.n_marked) {
               ++this.n_read;
               return this.buffer[this.n_written++] = this.readToken();
            }

            this.buffer = null;
         }

         return this.readToken();
      }

      public void alloc(int size) {
         this.buffer = new Symbol[(this.n_marked = size) + 1];
         this.n_read = this.n_written = 0;
      }

      public void rewind() {
         this.n_read = 0;
      }

      public void insert(Symbol t0, Symbol t1) {
         if (this.buffer.length - this.n_written < 2) {
            throw new IllegalStateException("not enough space in the buffer");
         } else {
            System.arraycopy(this.buffer, 0, this.buffer, 2, this.n_written);
            this.buffer[0] = t0;
            this.buffer[1] = t1;
            this.n_written += 2;
         }
      }

      public Symbol remove(int i) {
         Symbol token = this.buffer[i];

         int last;
         for(last = this.n_written - 1; i < last; this.buffer[i++] = this.buffer[i]) {
         }

         this.n_written = last;
         return token;
      }

      boolean isFull() {
         return this.n_read == this.n_marked;
      }

      private Symbol readToken() throws IOException {
         while(true) {
            try {
               return this.scanner.nextToken();
            } catch (Scanner.Exception var2) {
               Parser.this.report.scannerError(var2);
            }
         }
      }
   }

   public static class Events {
      public void scannerError(Scanner.Exception e) {
         System.err.print("Scanner Error:");
         if (e.line > 0) {
            System.err.print(e.line);
            System.err.print(',');
            System.err.print(e.column);
            System.err.print(':');
         }

         System.err.print(' ');
         System.err.println(e.getMessage());
      }

      public void syntaxError(Symbol token) {
         System.err.print(':');
         System.err.print(Symbol.getLine(token.start));
         System.err.print(',');
         System.err.print(Symbol.getColumn(token.start));
         System.err.print('-');
         System.err.print(Symbol.getLine(token.end));
         System.err.print(',');
         System.err.print(Symbol.getColumn(token.end));
         System.err.print(": Syntax Error: unexpected token ");
         if (token.value != null) {
            System.err.print('"');
            System.err.print(token.value);
            System.err.println('"');
         } else {
            System.err.print('#');
            System.err.println(token.id);
         }

      }

      public void unexpectedTokenRemoved(Symbol token) {
         System.err.print(':');
         System.err.print(Symbol.getLine(token.start));
         System.err.print(',');
         System.err.print(Symbol.getColumn(token.start));
         System.err.print('-');
         System.err.print(Symbol.getLine(token.end));
         System.err.print(',');
         System.err.print(Symbol.getColumn(token.end));
         System.err.print(": Recovered: removed unexpected token ");
         if (token.value != null) {
            System.err.print('"');
            System.err.print(token.value);
            System.err.println('"');
         } else {
            System.err.print('#');
            System.err.println(token.id);
         }

      }

      public void missingTokenInserted(Symbol token) {
         System.err.print(':');
         System.err.print(Symbol.getLine(token.start));
         System.err.print(',');
         System.err.print(Symbol.getColumn(token.start));
         System.err.print('-');
         System.err.print(Symbol.getLine(token.end));
         System.err.print(',');
         System.err.print(Symbol.getColumn(token.end));
         System.err.print(": Recovered: inserted missing token ");
         if (token.value != null) {
            System.err.print('"');
            System.err.print(token.value);
            System.err.println('"');
         } else {
            System.err.print('#');
            System.err.println(token.id);
         }

      }

      public void misspelledTokenReplaced(Symbol token) {
         System.err.print(':');
         System.err.print(Symbol.getLine(token.start));
         System.err.print(',');
         System.err.print(Symbol.getColumn(token.start));
         System.err.print('-');
         System.err.print(Symbol.getLine(token.end));
         System.err.print(',');
         System.err.print(Symbol.getColumn(token.end));
         System.err.print(": Recovered: replaced unexpected token with ");
         if (token.value != null) {
            System.err.print('"');
            System.err.print(token.value);
            System.err.println('"');
         } else {
            System.err.print('#');
            System.err.println(token.id);
         }

      }

      public void errorPhraseRemoved(Symbol error) {
         System.err.print(':');
         System.err.print(Symbol.getLine(error.start));
         System.err.print(',');
         System.err.print(Symbol.getColumn(error.start));
         System.err.print('-');
         System.err.print(Symbol.getLine(error.end));
         System.err.print(',');
         System.err.print(Symbol.getColumn(error.end));
         System.err.println(": Recovered: removed error phrase");
      }
   }

   public static class Exception extends java.lang.Exception {
      Exception(String msg) {
         super(msg);
      }
   }
}
