package org.jf.util.jcommander;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.internal.Lists;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.jf.util.WrappedIndentingWriter;

public class HelpFormatter {
   private int width = 80;

   @Nonnull
   public HelpFormatter width(int width) {
      this.width = width;
      return this;
   }

   @Nonnull
   private static ExtendedParameters getExtendedParameters(JCommander jc) {
      ExtendedParameters anno = (ExtendedParameters)jc.getObjects().get(0).getClass().getAnnotation(ExtendedParameters.class);
      if (anno == null) {
         throw new IllegalStateException("All commands should have an ExtendedParameters annotation");
      } else {
         return anno;
      }
   }

   @Nonnull
   private static List<String> getCommandAliases(JCommander jc) {
      return Lists.newArrayList((Object[])getExtendedParameters(jc).commandAliases());
   }

   private static boolean includeParametersInUsage(@Nonnull JCommander jc) {
      return getExtendedParameters(jc).includeParametersInUsage();
   }

   @Nonnull
   private static String getPostfixDescription(@Nonnull JCommander jc) {
      return getExtendedParameters(jc).postfixDescription();
   }

   private int getParameterArity(ParameterDescription param) {
      if (param.getParameter().arity() > 0) {
         return param.getParameter().arity();
      } else {
         Class<?> type = param.getParameterized().getType();
         return type != Boolean.TYPE && type != Boolean.class ? 1 : 0;
      }
   }

   private List<ParameterDescription> getSortedParameters(JCommander jc) {
      List<ParameterDescription> parameters = Lists.newArrayList((Collection)jc.getParameters());
      final Pattern pattern = Pattern.compile("^-*(.*)$");
      Collections.sort(parameters, new Comparator<ParameterDescription>() {
         public int compare(ParameterDescription o1, ParameterDescription o2) {
            Matcher matcher = pattern.matcher(o1.getParameter().names()[0]);
            if (matcher.matches()) {
               String s1 = matcher.group(1);
               matcher = pattern.matcher(o2.getParameter().names()[0]);
               if (matcher.matches()) {
                  String s2 = matcher.group(1);
                  return s1.compareTo(s2);
               } else {
                  throw new IllegalStateException();
               }
            } else {
               throw new IllegalStateException();
            }
         }
      });
      return parameters;
   }

   @Nonnull
   public String format(@Nonnull JCommander... jc) {
      return this.format(Arrays.asList(jc));
   }

   @Nonnull
   public String format(@Nonnull List<JCommander> commandHierarchy) {
      try {
         StringWriter stringWriter = new StringWriter();
         WrappedIndentingWriter writer = new WrappedIndentingWriter(stringWriter, this.width - 5, this.width);
         JCommander leafJc = (JCommander)Iterables.getLast(commandHierarchy);
         writer.write("usage:");
         writer.indent(2);
         Iterator var5 = commandHierarchy.iterator();

         while(var5.hasNext()) {
            JCommander jc = (JCommander)var5.next();
            writer.write(" ");
            writer.write(ExtendedCommands.commandName(jc));
         }

         if (includeParametersInUsage(leafJc)) {
            var5 = leafJc.getParameters().iterator();

            while(var5.hasNext()) {
               ParameterDescription param = (ParameterDescription)var5.next();
               if (!param.getParameter().hidden()) {
                  writer.write(" [");
                  writer.write(param.getParameter().getParameter().names()[0]);
                  writer.write("]");
               }
            }
         } else if (!leafJc.getParameters().isEmpty()) {
            writer.write(" [<options>]");
         }

         if (!leafJc.getCommands().isEmpty()) {
            writer.write(" [<command [<args>]]");
         }

         String postfixDescription;
         if (leafJc.getMainParameter() != null) {
            String[] argumentNames = ExtendedCommands.parameterArgumentNames(leafJc.getMainParameter());
            if (argumentNames.length == 0) {
               writer.write(" <args>");
            } else {
               postfixDescription = argumentNames[0];
               boolean writeAngleBrackets = !postfixDescription.startsWith("<") && !postfixDescription.startsWith("[");
               writer.write(" ");
               if (writeAngleBrackets) {
                  writer.write("<");
               }

               writer.write(argumentNames[0]);
               if (writeAngleBrackets) {
                  writer.write(">");
               }
            }
         }

         writer.deindent(2);
         String commandDescription = ExtendedCommands.getCommandDescription(leafJc);
         if (commandDescription != null) {
            writer.write("\n");
            writer.write(commandDescription);
         }

         if (!leafJc.getParameters().isEmpty() || leafJc.getMainParameter() != null) {
            writer.write("\n\nOptions:");
            writer.indent(2);
            Iterator var20 = this.getSortedParameters(leafJc).iterator();

            label150:
            while(true) {
               ParameterDescription param;
               do {
                  if (!var20.hasNext()) {
                     if (leafJc.getMainParameter() != null) {
                        String[] argumentNames = ExtendedCommands.parameterArgumentNames(leafJc.getMainParameter());
                        writer.write("\n");
                        writer.indent(4);
                        if (argumentNames.length > 0) {
                           writer.write("<");
                           writer.write(argumentNames[0]);
                           writer.write(">");
                        } else {
                           writer.write("<args>");
                        }

                        if (leafJc.getMainParameterDescription() != null) {
                           writer.write(" - ");
                           writer.write(leafJc.getMainParameterDescription());
                        }

                        writer.deindent(4);
                     }

                     writer.deindent(2);
                     break label150;
                  }

                  param = (ParameterDescription)var20.next();
               } while(param.getParameter().hidden());

               writer.write("\n");
               writer.indent(4);
               if (!param.getNames().isEmpty()) {
                  writer.write(Joiner.on(',').join((Object[])param.getParameter().names()));
               }

               if (this.getParameterArity(param) > 0) {
                  String[] argumentNames = ExtendedCommands.parameterArgumentNames(param);

                  for(int i = 0; i < this.getParameterArity(param); ++i) {
                     writer.write(" ");
                     if (i < argumentNames.length) {
                        writer.write("<");
                        writer.write(argumentNames[i]);
                        writer.write(">");
                     } else {
                        writer.write("<arg>");
                     }
                  }
               }

               if (param.getDescription() != null && !param.getDescription().isEmpty()) {
                  writer.write(" - ");
                  writer.write(param.getDescription());
               }

               if (param.getDefault() != null) {
                  String defaultValue = null;
                  if (param.getParameterized().getType() != Boolean.class && param.getParameterized().getType() != Boolean.TYPE) {
                     if (List.class.isAssignableFrom(param.getParameterized().getType())) {
                        if (!((List)param.getDefault()).isEmpty()) {
                           defaultValue = param.getDefault().toString();
                        }
                     } else {
                        defaultValue = param.getDefault().toString();
                     }
                  } else if ((Boolean)param.getDefault()) {
                     defaultValue = "True";
                  }

                  if (defaultValue != null) {
                     writer.write(" (default: ");
                     writer.write(defaultValue);
                     writer.write(")");
                  }
               }

               writer.deindent(4);
            }
         }

         if (!leafJc.getCommands().isEmpty()) {
            writer.write("\n\nCommands:");
            writer.indent(2);
            List<Entry<String, JCommander>> entryList = Lists.newArrayList((Collection)leafJc.getCommands().entrySet());
            Collections.sort(entryList, new Comparator<Entry<String, JCommander>>() {
               public int compare(Entry<String, JCommander> o1, Entry<String, JCommander> o2) {
                  return ((String)o1.getKey()).compareTo((String)o2.getKey());
               }
            });
            Iterator var24 = entryList.iterator();

            while(var24.hasNext()) {
               Entry<String, JCommander> entry = (Entry)var24.next();
               String commandName = (String)entry.getKey();
               JCommander command = (JCommander)entry.getValue();
               Object arg = command.getObjects().get(0);
               Parameters parametersAnno = (Parameters)arg.getClass().getAnnotation(Parameters.class);
               if (!parametersAnno.hidden()) {
                  writer.write("\n");
                  writer.indent(4);
                  writer.write(commandName);
                  List<String> aliases = getCommandAliases(command);
                  if (!aliases.isEmpty()) {
                     writer.write("(");
                     writer.write(Joiner.on(',').join((Iterable)aliases));
                     writer.write(")");
                  }

                  String commandDesc = leafJc.getCommandDescription(commandName);
                  if (commandDesc != null) {
                     writer.write(" - ");
                     writer.write(commandDesc);
                  }

                  writer.deindent(4);
               }
            }

            writer.deindent(2);
         }

         postfixDescription = getPostfixDescription(leafJc);
         if (!postfixDescription.isEmpty()) {
            writer.write("\n\n");
            writer.write(postfixDescription);
         }

         writer.flush();
         return stringWriter.getBuffer().toString();
      } catch (IOException var15) {
         throw new RuntimeException(var15);
      }
   }
}
