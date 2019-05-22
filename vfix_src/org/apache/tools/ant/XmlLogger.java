package org.apache.tools.ant;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.tools.ant.util.DOMElementWriter;
import org.apache.tools.ant.util.StringUtils;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class XmlLogger implements BuildLogger {
   private int msgOutputLevel = 4;
   private PrintStream outStream;
   private static DocumentBuilder builder = getDocumentBuilder();
   private static final String BUILD_TAG = "build";
   private static final String TARGET_TAG = "target";
   private static final String TASK_TAG = "task";
   private static final String MESSAGE_TAG = "message";
   private static final String NAME_ATTR = "name";
   private static final String TIME_ATTR = "time";
   private static final String PRIORITY_ATTR = "priority";
   private static final String LOCATION_ATTR = "location";
   private static final String ERROR_ATTR = "error";
   private static final String STACKTRACE_TAG = "stacktrace";
   private Document doc;
   private Hashtable tasks;
   private Hashtable targets;
   private Hashtable threadStacks;
   private XmlLogger.TimedElement buildElement;

   private static DocumentBuilder getDocumentBuilder() {
      try {
         return DocumentBuilderFactory.newInstance().newDocumentBuilder();
      } catch (Exception var1) {
         throw new ExceptionInInitializerError(var1);
      }
   }

   public XmlLogger() {
      this.doc = builder.newDocument();
      this.tasks = new Hashtable();
      this.targets = new Hashtable();
      this.threadStacks = new Hashtable();
      this.buildElement = null;
   }

   public void buildStarted(BuildEvent event) {
      this.buildElement = new XmlLogger.TimedElement();
      this.buildElement.startTime = System.currentTimeMillis();
      this.buildElement.element = this.doc.createElement("build");
   }

   public void buildFinished(BuildEvent event) {
      long totalTime = System.currentTimeMillis() - this.buildElement.startTime;
      this.buildElement.element.setAttribute("time", DefaultLogger.formatTime(totalTime));
      if (event.getException() != null) {
         this.buildElement.element.setAttribute("error", event.getException().toString());
         Throwable t = event.getException();
         Text errText = this.doc.createCDATASection(StringUtils.getStackTrace(t));
         Element stacktrace = this.doc.createElement("stacktrace");
         stacktrace.appendChild(errText);
         this.buildElement.element.appendChild(stacktrace);
      }

      String outFilename = event.getProject().getProperty("XmlLogger.file");
      if (outFilename == null) {
         outFilename = "log.xml";
      }

      String xslUri = event.getProject().getProperty("ant.XmlLogger.stylesheet.uri");
      if (xslUri == null) {
         xslUri = "log.xsl";
      }

      OutputStreamWriter out = null;

      try {
         OutputStream stream = this.outStream;
         if (stream == null) {
            stream = new FileOutputStream(outFilename);
         }

         out = new OutputStreamWriter((OutputStream)stream, "UTF8");
         out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
         if (xslUri.length() > 0) {
            out.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + xslUri + "\"?>\n\n");
         }

         (new DOMElementWriter()).write(this.buildElement.element, out, 0, "\t");
         out.flush();
      } catch (IOException var15) {
         throw new BuildException("Unable to write log file", var15);
      } finally {
         if (out != null) {
            try {
               out.close();
            } catch (IOException var14) {
            }
         }

      }

      this.buildElement = null;
   }

   private Stack getStack() {
      Stack threadStack = (Stack)this.threadStacks.get(Thread.currentThread());
      if (threadStack == null) {
         threadStack = new Stack();
         this.threadStacks.put(Thread.currentThread(), threadStack);
      }

      return threadStack;
   }

   public void targetStarted(BuildEvent event) {
      Target target = event.getTarget();
      XmlLogger.TimedElement targetElement = new XmlLogger.TimedElement();
      targetElement.startTime = System.currentTimeMillis();
      targetElement.element = this.doc.createElement("target");
      targetElement.element.setAttribute("name", target.getName());
      this.targets.put(target, targetElement);
      this.getStack().push(targetElement);
   }

   public void targetFinished(BuildEvent event) {
      Target target = event.getTarget();
      XmlLogger.TimedElement targetElement = (XmlLogger.TimedElement)this.targets.get(target);
      if (targetElement != null) {
         long totalTime = System.currentTimeMillis() - targetElement.startTime;
         targetElement.element.setAttribute("time", DefaultLogger.formatTime(totalTime));
         XmlLogger.TimedElement parentElement = null;
         Stack threadStack = this.getStack();
         if (!threadStack.empty()) {
            XmlLogger.TimedElement poppedStack = (XmlLogger.TimedElement)threadStack.pop();
            if (poppedStack != targetElement) {
               throw new RuntimeException("Mismatch - popped element = " + poppedStack + " finished target element = " + targetElement);
            }

            if (!threadStack.empty()) {
               parentElement = (XmlLogger.TimedElement)threadStack.peek();
            }
         }

         if (parentElement == null) {
            this.buildElement.element.appendChild(targetElement.element);
         } else {
            parentElement.element.appendChild(targetElement.element);
         }
      }

      this.targets.remove(target);
   }

   public void taskStarted(BuildEvent event) {
      XmlLogger.TimedElement taskElement = new XmlLogger.TimedElement();
      taskElement.startTime = System.currentTimeMillis();
      taskElement.element = this.doc.createElement("task");
      Task task = event.getTask();
      String name = event.getTask().getTaskName();
      if (name == null) {
         name = "";
      }

      taskElement.element.setAttribute("name", name);
      taskElement.element.setAttribute("location", event.getTask().getLocation().toString());
      this.tasks.put(task, taskElement);
      this.getStack().push(taskElement);
   }

   public void taskFinished(BuildEvent event) {
      Task task = event.getTask();
      XmlLogger.TimedElement taskElement = (XmlLogger.TimedElement)this.tasks.get(task);
      if (taskElement != null) {
         long totalTime = System.currentTimeMillis() - taskElement.startTime;
         taskElement.element.setAttribute("time", DefaultLogger.formatTime(totalTime));
         Target target = task.getOwningTarget();
         XmlLogger.TimedElement targetElement = null;
         if (target != null) {
            targetElement = (XmlLogger.TimedElement)this.targets.get(target);
         }

         if (targetElement == null) {
            this.buildElement.element.appendChild(taskElement.element);
         } else {
            targetElement.element.appendChild(taskElement.element);
         }

         Stack threadStack = this.getStack();
         if (!threadStack.empty()) {
            XmlLogger.TimedElement poppedStack = (XmlLogger.TimedElement)threadStack.pop();
            if (poppedStack != taskElement) {
               throw new RuntimeException("Mismatch - popped element = " + poppedStack + " finished task element = " + taskElement);
            }
         }

         this.tasks.remove(task);
      } else {
         throw new RuntimeException("Unknown task " + task + " not in " + this.tasks);
      }
   }

   private XmlLogger.TimedElement getTaskElement(Task task) {
      XmlLogger.TimedElement element = (XmlLogger.TimedElement)this.tasks.get(task);
      if (element != null) {
         return element;
      } else {
         Enumeration e = this.tasks.keys();

         Task key;
         do {
            if (!e.hasMoreElements()) {
               return null;
            }

            key = (Task)e.nextElement();
         } while(!(key instanceof UnknownElement) || ((UnknownElement)key).getTask() != task);

         return (XmlLogger.TimedElement)this.tasks.get(key);
      }
   }

   public void messageLogged(BuildEvent event) {
      int priority = event.getPriority();
      if (priority <= this.msgOutputLevel) {
         Element messageElement = this.doc.createElement("message");
         String name = "debug";
         switch(event.getPriority()) {
         case 0:
            name = "error";
            break;
         case 1:
            name = "warn";
            break;
         case 2:
            name = "info";
            break;
         default:
            name = "debug";
         }

         messageElement.setAttribute("priority", name);
         Throwable ex = event.getException();
         CDATASection messageText;
         if (4 <= this.msgOutputLevel && ex != null) {
            messageText = this.doc.createCDATASection(StringUtils.getStackTrace(ex));
            Element stacktrace = this.doc.createElement("stacktrace");
            stacktrace.appendChild(messageText);
            this.buildElement.element.appendChild(stacktrace);
         }

         messageText = this.doc.createCDATASection(event.getMessage());
         messageElement.appendChild(messageText);
         XmlLogger.TimedElement parentElement = null;
         Task task = event.getTask();
         Target target = event.getTarget();
         if (task != null) {
            parentElement = this.getTaskElement(task);
         }

         if (parentElement == null && target != null) {
            parentElement = (XmlLogger.TimedElement)this.targets.get(target);
         }

         if (parentElement != null) {
            parentElement.element.appendChild(messageElement);
         } else {
            this.buildElement.element.appendChild(messageElement);
         }

      }
   }

   public void setMessageOutputLevel(int level) {
      this.msgOutputLevel = level;
   }

   public void setOutputPrintStream(PrintStream output) {
      this.outStream = new PrintStream(output, true);
   }

   public void setEmacsMode(boolean emacsMode) {
   }

   public void setErrorPrintStream(PrintStream err) {
   }

   private static class TimedElement {
      private long startTime;
      private Element element;

      private TimedElement() {
      }

      public String toString() {
         return this.element.getTagName() + ":" + this.element.getAttribute("name");
      }

      // $FF: synthetic method
      TimedElement(Object x0) {
         this();
      }
   }
}
