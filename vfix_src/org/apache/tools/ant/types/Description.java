package org.apache.tools.ant.types;

import java.util.Vector;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnknownElement;
import org.apache.tools.ant.helper.ProjectHelperImpl;

public class Description extends DataType {
   public void addText(String text) {
      ProjectHelper ph = ProjectHelper.getProjectHelper();
      if (ph instanceof ProjectHelperImpl) {
         String currentDescription = this.getProject().getDescription();
         if (currentDescription == null) {
            this.getProject().setDescription(text);
         } else {
            this.getProject().setDescription(currentDescription + text);
         }

      }
   }

   public static String getDescription(Project project) {
      Vector targets = (Vector)project.getReference("ant.targets");
      if (targets == null) {
         return null;
      } else {
         StringBuffer description = new StringBuffer();

         for(int i = 0; i < targets.size(); ++i) {
            Target t = (Target)targets.elementAt(i);
            concatDescriptions(project, t, description);
         }

         return description.toString();
      }
   }

   private static void concatDescriptions(Project project, Target t, StringBuffer description) {
      if (t != null) {
         Vector tasks = findElementInTarget(project, t, "description");
         if (tasks != null) {
            for(int i = 0; i < tasks.size(); ++i) {
               Task task = (Task)tasks.elementAt(i);
               if (task instanceof UnknownElement) {
                  UnknownElement ue = (UnknownElement)task;
                  String descComp = ue.getWrapper().getText().toString();
                  if (descComp != null) {
                     description.append(project.replaceProperties(descComp));
                  }
               }
            }

         }
      }
   }

   private static Vector findElementInTarget(Project project, Target t, String name) {
      Task[] tasks = t.getTasks();
      Vector elems = new Vector();

      for(int i = 0; i < tasks.length; ++i) {
         if (name.equals(tasks[i].getTaskName())) {
            elems.addElement(tasks[i]);
         }
      }

      return elems;
   }
}
