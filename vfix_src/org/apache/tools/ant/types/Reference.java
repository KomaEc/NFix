package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public class Reference {
   private String refid;
   private Project project;

   /** @deprecated */
   public Reference() {
   }

   /** @deprecated */
   public Reference(String id) {
      this.setRefId(id);
   }

   public Reference(Project p, String id) {
      this.setRefId(id);
      this.setProject(p);
   }

   public void setRefId(String id) {
      this.refid = id;
   }

   public String getRefId() {
      return this.refid;
   }

   public void setProject(Project p) {
      this.project = p;
   }

   public Project getProject() {
      return this.project;
   }

   public Object getReferencedObject(Project fallback) throws BuildException {
      if (this.refid == null) {
         throw new BuildException("No reference specified");
      } else {
         Object o = this.project == null ? fallback.getReference(this.refid) : this.project.getReference(this.refid);
         if (o == null) {
            throw new BuildException("Reference " + this.refid + " not found.");
         } else {
            return o;
         }
      }
   }

   public Object getReferencedObject() throws BuildException {
      if (this.project == null) {
         throw new BuildException("No project set on reference to " + this.refid);
      } else {
         return this.getReferencedObject(this.project);
      }
   }
}
