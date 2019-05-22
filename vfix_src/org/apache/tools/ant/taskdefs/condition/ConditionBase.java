package org.apache.tools.ant.taskdefs.condition;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.Available;
import org.apache.tools.ant.taskdefs.Checksum;
import org.apache.tools.ant.taskdefs.UpToDate;

public abstract class ConditionBase extends ProjectComponent implements DynamicElement {
   private static final String CONDITION_ANTLIB = "antlib:org.apache.tools.ant.types.conditions:";
   private String taskName = "condition";
   private Vector conditions = new Vector();

   protected ConditionBase() {
      this.taskName = "component";
   }

   protected ConditionBase(String taskName) {
      this.taskName = taskName;
   }

   protected int countConditions() {
      return this.conditions.size();
   }

   protected final Enumeration getConditions() {
      return this.conditions.elements();
   }

   public void setTaskName(String name) {
      this.taskName = name;
   }

   public String getTaskName() {
      return this.taskName;
   }

   public void addAvailable(Available a) {
      this.conditions.addElement(a);
   }

   public void addChecksum(Checksum c) {
      this.conditions.addElement(c);
   }

   public void addUptodate(UpToDate u) {
      this.conditions.addElement(u);
   }

   public void addNot(Not n) {
      this.conditions.addElement(n);
   }

   public void addAnd(And a) {
      this.conditions.addElement(a);
   }

   public void addOr(Or o) {
      this.conditions.addElement(o);
   }

   public void addEquals(Equals e) {
      this.conditions.addElement(e);
   }

   public void addOs(Os o) {
      this.conditions.addElement(o);
   }

   public void addIsSet(IsSet i) {
      this.conditions.addElement(i);
   }

   public void addHttp(Http h) {
      this.conditions.addElement(h);
   }

   public void addSocket(Socket s) {
      this.conditions.addElement(s);
   }

   public void addFilesMatch(FilesMatch test) {
      this.conditions.addElement(test);
   }

   public void addContains(Contains test) {
      this.conditions.addElement(test);
   }

   public void addIsTrue(IsTrue test) {
      this.conditions.addElement(test);
   }

   public void addIsFalse(IsFalse test) {
      this.conditions.addElement(test);
   }

   public void addIsReference(IsReference i) {
      this.conditions.addElement(i);
   }

   public void addIsFileSelected(IsFileSelected test) {
      this.conditions.addElement(test);
   }

   public void add(Condition c) {
      this.conditions.addElement(c);
   }

   public Object createDynamicElement(String name) {
      Object cond = ComponentHelper.getComponentHelper(this.getProject()).createComponent("antlib:org.apache.tools.ant.types.conditions:" + name);
      if (!(cond instanceof Condition)) {
         return null;
      } else {
         this.log("Dynamically discovered '" + name + "' " + cond, 4);
         this.add((Condition)cond);
         return cond;
      }
   }
}
