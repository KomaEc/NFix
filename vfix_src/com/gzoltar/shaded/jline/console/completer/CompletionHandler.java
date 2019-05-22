package com.gzoltar.shaded.jline.console.completer;

import com.gzoltar.shaded.jline.console.ConsoleReader;
import java.io.IOException;
import java.util.List;

public interface CompletionHandler {
   boolean complete(ConsoleReader var1, List<CharSequence> var2, int var3) throws IOException;
}
