package java_cup.runtime;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ScannerBuffer implements Scanner {
   private Scanner inner;
   private List<Symbol> buffer = new LinkedList();

   public ScannerBuffer(Scanner inner) {
      this.inner = inner;
   }

   public List<Symbol> getBuffered() {
      return Collections.unmodifiableList(this.buffer);
   }

   public Symbol next_token() throws Exception {
      Symbol buffered = this.inner.next_token();
      this.buffer.add(buffered);
      return buffered;
   }
}
