package org.netbeans.lib.cvsclient.commandLine;

import java.io.PrintStream;
import org.netbeans.lib.cvsclient.event.CVSListener;

public interface ListenerProvider {
   CVSListener createCVSListener(PrintStream var1, PrintStream var2);
}
