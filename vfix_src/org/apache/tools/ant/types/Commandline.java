package org.apache.tools.ant.types;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.util.StringUtils;

public class Commandline implements Cloneable {
   private static final boolean IS_WIN_9X = Os.isFamily("win9x");
   private Vector arguments = new Vector();
   private String executable = null;
   protected static final String DISCLAIMER;

   public Commandline(String toProcess) {
      String[] tmp = translateCommandline(toProcess);
      if (tmp != null && tmp.length > 0) {
         this.setExecutable(tmp[0]);

         for(int i = 1; i < tmp.length; ++i) {
            this.createArgument().setValue(tmp[i]);
         }
      }

   }

   public Commandline() {
   }

   public Commandline.Argument createArgument() {
      return this.createArgument(false);
   }

   public Commandline.Argument createArgument(boolean insertAtStart) {
      Commandline.Argument argument = new Commandline.Argument();
      if (insertAtStart) {
         this.arguments.insertElementAt(argument, 0);
      } else {
         this.arguments.addElement(argument);
      }

      return argument;
   }

   public void setExecutable(String executable) {
      if (executable != null && executable.length() != 0) {
         this.executable = executable.replace('/', File.separatorChar).replace('\\', File.separatorChar);
      }
   }

   public String getExecutable() {
      return this.executable;
   }

   public void addArguments(String[] line) {
      for(int i = 0; i < line.length; ++i) {
         this.createArgument().setValue(line[i]);
      }

   }

   public String[] getCommandline() {
      List commands = new LinkedList();
      ListIterator list = commands.listIterator();
      this.addCommandToList(list);
      String[] result = new String[commands.size()];
      return (String[])((String[])commands.toArray(result));
   }

   public void addCommandToList(ListIterator list) {
      if (this.executable != null) {
         list.add(this.executable);
      }

      this.addArgumentsToList(list);
   }

   public String[] getArguments() {
      List result = new ArrayList(this.arguments.size() * 2);
      this.addArgumentsToList(result.listIterator());
      String[] res = new String[result.size()];
      return (String[])((String[])result.toArray(res));
   }

   public void addArgumentsToList(ListIterator list) {
      for(int i = 0; i < this.arguments.size(); ++i) {
         Commandline.Argument arg = (Commandline.Argument)this.arguments.elementAt(i);
         String[] s = arg.getParts();
         if (s != null) {
            for(int j = 0; j < s.length; ++j) {
               list.add(s[j]);
            }
         }
      }

   }

   public String toString() {
      return toString(this.getCommandline());
   }

   public static String quoteArgument(String argument) {
      if (argument.indexOf("\"") > -1) {
         if (argument.indexOf("'") > -1) {
            throw new BuildException("Can't handle single and double quotes in same argument");
         } else {
            return '\'' + argument + '\'';
         }
      } else {
         return argument.indexOf("'") <= -1 && argument.indexOf(" ") <= -1 && (!IS_WIN_9X || argument.indexOf(59) == -1) ? argument : '"' + argument + '"';
      }
   }

   public static String toString(String[] line) {
      if (line != null && line.length != 0) {
         StringBuffer result = new StringBuffer();

         for(int i = 0; i < line.length; ++i) {
            if (i > 0) {
               result.append(' ');
            }

            result.append(quoteArgument(line[i]));
         }

         return result.toString();
      } else {
         return "";
      }
   }

   public static String[] translateCommandline(String toProcess) {
      if (toProcess != null && toProcess.length() != 0) {
         int normal = false;
         int inQuote = true;
         int inDoubleQuote = true;
         int state = 0;
         StringTokenizer tok = new StringTokenizer(toProcess, "\"' ", true);
         Vector v = new Vector();
         StringBuffer current = new StringBuffer();
         boolean lastTokenHasBeenQuoted = false;

         while(true) {
            while(tok.hasMoreTokens()) {
               String nextTok = tok.nextToken();
               switch(state) {
               case 1:
                  if ("'".equals(nextTok)) {
                     lastTokenHasBeenQuoted = true;
                     state = 0;
                  } else {
                     current.append(nextTok);
                  }
                  continue;
               case 2:
                  if ("\"".equals(nextTok)) {
                     lastTokenHasBeenQuoted = true;
                     state = 0;
                  } else {
                     current.append(nextTok);
                  }
                  continue;
               }

               if ("'".equals(nextTok)) {
                  state = 1;
               } else if ("\"".equals(nextTok)) {
                  state = 2;
               } else if (" ".equals(nextTok)) {
                  if (lastTokenHasBeenQuoted || current.length() != 0) {
                     v.addElement(current.toString());
                     current = new StringBuffer();
                  }
               } else {
                  current.append(nextTok);
               }

               lastTokenHasBeenQuoted = false;
            }

            if (lastTokenHasBeenQuoted || current.length() != 0) {
               v.addElement(current.toString());
            }

            if (state != 1 && state != 2) {
               String[] args = new String[v.size()];
               v.copyInto(args);
               return args;
            }

            throw new BuildException("unbalanced quotes in " + toProcess);
         }
      } else {
         return new String[0];
      }
   }

   public int size() {
      return this.getCommandline().length;
   }

   public Object clone() {
      try {
         Commandline c = (Commandline)super.clone();
         c.arguments = (Vector)this.arguments.clone();
         return c;
      } catch (CloneNotSupportedException var2) {
         throw new BuildException(var2);
      }
   }

   public void clear() {
      this.executable = null;
      this.arguments.removeAllElements();
   }

   public void clearArgs() {
      this.arguments.removeAllElements();
   }

   public Commandline.Marker createMarker() {
      return new Commandline.Marker(this.arguments.size());
   }

   public String describeCommand() {
      return describeCommand(this);
   }

   public String describeArguments() {
      return describeArguments(this);
   }

   public static String describeCommand(Commandline line) {
      return describeCommand(line.getCommandline());
   }

   public static String describeArguments(Commandline line) {
      return describeArguments(line.getArguments());
   }

   public static String describeCommand(String[] args) {
      if (args != null && args.length != 0) {
         StringBuffer buf = new StringBuffer("Executing '");
         buf.append(args[0]);
         buf.append("'");
         if (args.length > 1) {
            buf.append(" with ");
            buf.append(describeArguments(args, 1));
         } else {
            buf.append(DISCLAIMER);
         }

         return buf.toString();
      } else {
         return "";
      }
   }

   public static String describeArguments(String[] args) {
      return describeArguments(args, 0);
   }

   protected static String describeArguments(String[] args, int offset) {
      if (args != null && args.length > offset) {
         StringBuffer buf = new StringBuffer("argument");
         if (args.length > offset) {
            buf.append("s");
         }

         buf.append(":").append(StringUtils.LINE_SEP);

         for(int i = offset; i < args.length; ++i) {
            buf.append("'").append(args[i]).append("'").append(StringUtils.LINE_SEP);
         }

         buf.append(DISCLAIMER);
         return buf.toString();
      } else {
         return "";
      }
   }

   public Iterator iterator() {
      return this.arguments.iterator();
   }

   static {
      DISCLAIMER = StringUtils.LINE_SEP + "The ' characters around the executable and arguments are" + StringUtils.LINE_SEP + "not part of the command." + StringUtils.LINE_SEP;
   }

   public class Marker {
      private int position;
      private int realPos = -1;

      Marker(int position) {
         this.position = position;
      }

      public int getPosition() {
         if (this.realPos == -1) {
            this.realPos = Commandline.this.executable == null ? 0 : 1;

            for(int i = 0; i < this.position; ++i) {
               Commandline.Argument arg = (Commandline.Argument)Commandline.this.arguments.elementAt(i);
               this.realPos += arg.getParts().length;
            }
         }

         return this.realPos;
      }
   }

   public static class Argument extends ProjectComponent {
      private String[] parts;

      public void setValue(String value) {
         this.parts = new String[]{value};
      }

      public void setLine(String line) {
         if (line != null) {
            this.parts = Commandline.translateCommandline(line);
         }
      }

      public void setPath(Path value) {
         this.parts = new String[]{value.toString()};
      }

      public void setPathref(Reference value) {
         Path p = new Path(this.getProject());
         p.setRefid(value);
         this.parts = new String[]{p.toString()};
      }

      public void setFile(File value) {
         this.parts = new String[]{value.getAbsolutePath()};
      }

      public String[] getParts() {
         return this.parts;
      }
   }
}
