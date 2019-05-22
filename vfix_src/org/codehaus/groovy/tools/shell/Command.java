package org.codehaus.groovy.tools.shell;

import com.gzoltar.shaded.jline.Completor;
import java.util.List;

public interface Command {
   String getName();

   String getShortcut();

   Completor getCompletor();

   String getDescription();

   String getUsage();

   String getHelp();

   List getAliases();

   Object execute(List var1);

   boolean getHidden();
}
