package bsh.util;

import bsh.ConsoleInterface;
import java.awt.Color;

public interface GUIConsoleInterface extends ConsoleInterface {
   void print(Object var1, Color var2);

   void setNameCompletion(NameCompletion var1);

   void setWaitFeedback(boolean var1);
}
