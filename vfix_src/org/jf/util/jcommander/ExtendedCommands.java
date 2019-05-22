package org.jf.util.jcommander;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;
import com.beust.jcommander.Parameterized;
import com.beust.jcommander.Parameters;
import java.lang.reflect.Field;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ExtendedCommands {
   @Nonnull
   private static ExtendedParameters getExtendedParameters(Object command) {
      ExtendedParameters anno = (ExtendedParameters)command.getClass().getAnnotation(ExtendedParameters.class);
      if (anno == null) {
         throw new IllegalStateException("All extended commands should have an ExtendedParameters annotation: " + command.getClass().getCanonicalName());
      } else {
         return anno;
      }
   }

   @Nonnull
   public static String commandName(JCommander jc) {
      return getExtendedParameters(jc.getObjects().get(0)).commandName();
   }

   @Nonnull
   public static String commandName(Object command) {
      return getExtendedParameters(command).commandName();
   }

   @Nonnull
   public static String[] commandAliases(JCommander jc) {
      return commandAliases(jc.getObjects().get(0));
   }

   @Nonnull
   public static String[] commandAliases(Object command) {
      return getExtendedParameters(command).commandAliases();
   }

   public static boolean includeParametersInUsage(JCommander jc) {
      return includeParametersInUsage(jc.getObjects().get(0));
   }

   public static boolean includeParametersInUsage(Object command) {
      return getExtendedParameters(command).includeParametersInUsage();
   }

   @Nonnull
   public static String postfixDescription(JCommander jc) {
      return postfixDescription(jc.getObjects().get(0));
   }

   @Nonnull
   public static String postfixDescription(Object command) {
      return getExtendedParameters(command).postfixDescription();
   }

   public static void addExtendedCommand(JCommander jc, Command command) {
      jc.addCommand(commandName((Object)command), command, commandAliases((Object)command));
      command.setupCommand(command.getJCommander());
   }

   @Nonnull
   public static String[] parameterArgumentNames(ParameterDescription parameterDescription) {
      Parameterized parameterized = parameterDescription.getParameterized();
      Class cls = parameterDescription.getObject().getClass();
      Field field = null;

      while(cls != Object.class) {
         try {
            field = cls.getDeclaredField(parameterized.getName());
            break;
         } catch (NoSuchFieldException var5) {
            cls = cls.getSuperclass();
         }
      }

      assert field != null;

      ExtendedParameter extendedParameter = (ExtendedParameter)field.getAnnotation(ExtendedParameter.class);
      return extendedParameter != null ? extendedParameter.argumentNames() : new String[0];
   }

   @Nullable
   public static JCommander getSubcommand(JCommander jc, String commandName) {
      if (jc.getCommands().containsKey(commandName)) {
         return (JCommander)jc.getCommands().get(commandName);
      } else {
         Iterator var2 = jc.getCommands().values().iterator();

         while(var2.hasNext()) {
            JCommander command = (JCommander)var2.next();
            String[] var4 = commandAliases(command);
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String alias = var4[var6];
               if (commandName.equals(alias)) {
                  return command;
               }
            }
         }

         return null;
      }
   }

   @Nullable
   public static String getCommandDescription(@Nonnull JCommander jc) {
      Parameters parameters = (Parameters)jc.getObjects().get(0).getClass().getAnnotation(Parameters.class);
      return parameters == null ? null : parameters.commandDescription();
   }
}
