package org.jf.util.jcommander;

import com.beust.jcommander.JCommander;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.util.ConsoleUtil;

public abstract class Command {
   @Nonnull
   protected final List<JCommander> commandAncestors;

   public Command(@Nonnull List<JCommander> commandAncestors) {
      this.commandAncestors = commandAncestors;
   }

   public void usage() {
      System.out.println((new HelpFormatter()).width(ConsoleUtil.getConsoleWidth()).format(this.getCommandHierarchy()));
   }

   protected void setupCommand(JCommander jc) {
   }

   protected JCommander getJCommander() {
      JCommander parentJc = (JCommander)Iterables.getLast(this.commandAncestors);
      return (JCommander)parentJc.getCommands().get(((ExtendedParameters)this.getClass().getAnnotation(ExtendedParameters.class)).commandName());
   }

   public List<JCommander> getCommandHierarchy() {
      List<JCommander> commandHierarchy = Lists.newArrayList((Iterable)this.commandAncestors);
      commandHierarchy.add(this.getJCommander());
      return commandHierarchy;
   }

   public abstract void run();
}
