package soot.plugins.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
   namespace = "http://github.com/Sable/soot/plugins",
   name = "phase-plugin"
)
public class PhasePluginDescription extends PluginDescription {
   private String phaseName;
   private String className;

   @XmlAttribute(
      name = "class",
      required = true
   )
   public String getClassName() {
      return this.className;
   }

   public void setClassName(String name) {
      this.className = name;
   }

   @XmlAttribute(
      name = "phase",
      required = true
   )
   public String getPhaseName() {
      return this.phaseName;
   }

   public void setPhaseName(String name) {
      this.phaseName = name;
   }

   public String toString() {
      return "<PhasePluginDescription name=" + this.getPhaseName() + " class= " + this.getClassName() + ">";
   }
}
