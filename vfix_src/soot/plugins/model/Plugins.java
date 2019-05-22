package soot.plugins.model;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
   namespace = "http://github.com/Sable/soot/plugins",
   name = "soot-plugins"
)
public class Plugins {
   private final List<PluginDescription> pluginDescriptions = new LinkedList();

   @XmlElementRefs({@XmlElementRef(
   name = "phase-plugin",
   type = PhasePluginDescription.class
)})
   public List<PluginDescription> getPluginDescriptions() {
      return this.pluginDescriptions;
   }
}
