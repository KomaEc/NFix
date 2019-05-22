package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.XMLCatalog;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

public class XmlProperty extends Task {
   private Resource src;
   private String prefix = "";
   private boolean keepRoot = true;
   private boolean validate = false;
   private boolean collapseAttributes = false;
   private boolean semanticAttributes = false;
   private boolean includeSemanticAttribute = false;
   private File rootDirectory = null;
   private Hashtable addedAttributes = new Hashtable();
   private XMLCatalog xmlCatalog = new XMLCatalog();
   private static final String ID = "id";
   private static final String REF_ID = "refid";
   private static final String LOCATION = "location";
   private static final String VALUE = "value";
   private static final String PATH = "path";
   private static final String PATHID = "pathid";
   private static final String[] ATTRIBUTES = new String[]{"id", "refid", "location", "value", "path", "pathid"};
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$XmlProperty;

   public void init() {
      super.init();
      this.xmlCatalog.setProject(this.getProject());
   }

   protected EntityResolver getEntityResolver() {
      return this.xmlCatalog;
   }

   public void execute() throws BuildException {
      Resource r = this.getResource();
      if (r == null) {
         String msg = "XmlProperty task requires a source resource";
         throw new BuildException(msg);
      } else {
         try {
            this.log("Loading " + this.src, 3);
            if (r.isExists()) {
               DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
               factory.setValidating(this.validate);
               factory.setNamespaceAware(false);
               DocumentBuilder builder = factory.newDocumentBuilder();
               builder.setEntityResolver(this.getEntityResolver());
               Document document = null;
               if (this.src instanceof FileResource) {
                  document = builder.parse(((FileResource)this.src).getFile());
               } else {
                  document = builder.parse(this.src.getInputStream());
               }

               Element topElement = document.getDocumentElement();
               this.addedAttributes = new Hashtable();
               if (this.keepRoot) {
                  this.addNodeRecursively(topElement, this.prefix, (Object)null);
               } else {
                  NodeList topChildren = topElement.getChildNodes();
                  int numChildren = topChildren.getLength();

                  for(int i = 0; i < numChildren; ++i) {
                     this.addNodeRecursively(topChildren.item(i), this.prefix, (Object)null);
                  }
               }
            } else {
               this.log("Unable to find property resource: " + r, 3);
            }

         } catch (SAXException var9) {
            Exception x = var9;
            if (var9.getException() != null) {
               x = var9.getException();
            }

            throw new BuildException("Failed to load " + this.src, (Throwable)x);
         } catch (ParserConfigurationException var10) {
            throw new BuildException(var10);
         } catch (IOException var11) {
            throw new BuildException("Failed to load " + this.src, var11);
         }
      }
   }

   private void addNodeRecursively(Node node, String prefix, Object container) {
      String nodePrefix = prefix;
      if (node.getNodeType() != 3) {
         if (prefix.trim().length() > 0) {
            nodePrefix = prefix + ".";
         }

         nodePrefix = nodePrefix + node.getNodeName();
      }

      Object nodeObject = this.processNode(node, nodePrefix, container);
      if (node.hasChildNodes()) {
         NodeList nodeChildren = node.getChildNodes();
         int numChildren = nodeChildren.getLength();

         for(int i = 0; i < numChildren; ++i) {
            this.addNodeRecursively(nodeChildren.item(i), nodePrefix, nodeObject);
         }
      }

   }

   void addNodeRecursively(Node node, String prefix) {
      this.addNodeRecursively(node, prefix, (Object)null);
   }

   public Object processNode(Node node, String prefix, Object container) {
      Object addedPath = null;
      String id = null;
      if (node.hasAttributes()) {
         NamedNodeMap nodeAttributes = node.getAttributes();
         Node idNode = nodeAttributes.getNamedItem("id");
         id = this.semanticAttributes && idNode != null ? idNode.getNodeValue() : null;

         for(int i = 0; i < nodeAttributes.getLength(); ++i) {
            Node attributeNode = nodeAttributes.item(i);
            String nodeName;
            String attributeValue;
            if (!this.semanticAttributes) {
               nodeName = this.getAttributeName(attributeNode);
               attributeValue = this.getAttributeValue(attributeNode);
               this.addProperty(prefix + nodeName, attributeValue, (String)null);
            } else {
               nodeName = attributeNode.getNodeName();
               attributeValue = this.getAttributeValue(attributeNode);
               Path containingPath = container != null && container instanceof Path ? (Path)container : null;
               if (!nodeName.equals("id")) {
                  if (containingPath != null && nodeName.equals("path")) {
                     containingPath.setPath(attributeValue);
                  } else if (container instanceof Path && nodeName.equals("refid")) {
                     containingPath.setPath(attributeValue);
                  } else if (container instanceof Path && nodeName.equals("location")) {
                     containingPath.setLocation(this.resolveFile(attributeValue));
                  } else if (nodeName.equals("pathid")) {
                     if (container != null) {
                        throw new BuildException("XmlProperty does not support nested paths");
                     }

                     addedPath = new Path(this.getProject());
                     this.getProject().addReference(attributeValue, addedPath);
                  } else {
                     String attributeName = this.getAttributeName(attributeNode);
                     this.addProperty(prefix + attributeName, attributeValue, id);
                  }
               }
            }
         }
      }

      String nodeText = null;
      boolean emptyNode = false;
      boolean semanticEmptyOverride = false;
      if (node.getNodeType() == 1 && this.semanticAttributes && node.hasAttributes() && (node.getAttributes().getNamedItem("value") != null || node.getAttributes().getNamedItem("location") != null || node.getAttributes().getNamedItem("refid") != null || node.getAttributes().getNamedItem("path") != null || node.getAttributes().getNamedItem("pathid") != null)) {
         semanticEmptyOverride = true;
      }

      if (node.getNodeType() == 3) {
         nodeText = this.getAttributeValue(node);
      } else if (node.getNodeType() == 1 && node.getChildNodes().getLength() == 1 && node.getFirstChild().getNodeType() == 4) {
         nodeText = node.getFirstChild().getNodeValue();
         if ("".equals(nodeText) && !semanticEmptyOverride) {
            emptyNode = true;
         }
      } else if (node.getNodeType() == 1 && node.getChildNodes().getLength() == 0 && !semanticEmptyOverride) {
         nodeText = "";
         emptyNode = true;
      } else if (node.getNodeType() == 1 && node.getChildNodes().getLength() == 1 && node.getFirstChild().getNodeType() == 3 && "".equals(node.getFirstChild().getNodeValue()) && !semanticEmptyOverride) {
         nodeText = "";
         emptyNode = true;
      }

      if (nodeText != null) {
         if (this.semanticAttributes && id == null && container instanceof String) {
            id = (String)container;
         }

         if (nodeText.trim().length() != 0 || emptyNode) {
            this.addProperty(prefix, nodeText, id);
         }
      }

      return addedPath != null ? addedPath : id;
   }

   private void addProperty(String name, String value, String id) {
      String msg = name + ":" + value;
      if (id != null) {
         msg = msg + "(id=" + id + ")";
      }

      this.log(msg, 4);
      if (this.addedAttributes.containsKey(name)) {
         value = (String)this.addedAttributes.get(name) + "," + value;
         this.getProject().setProperty(name, value);
         this.addedAttributes.put(name, value);
      } else if (this.getProject().getProperty(name) == null) {
         this.getProject().setNewProperty(name, value);
         this.addedAttributes.put(name, value);
      } else {
         this.log("Override ignored for property " + name, 3);
      }

      if (id != null) {
         this.getProject().addReference(id, value);
      }

   }

   private String getAttributeName(Node attributeNode) {
      String attributeName = attributeNode.getNodeName();
      if (this.semanticAttributes) {
         if (attributeName.equals("refid")) {
            return "";
         } else {
            return isSemanticAttribute(attributeName) && !this.includeSemanticAttribute ? "" : "." + attributeName;
         }
      } else {
         return this.collapseAttributes ? "." + attributeName : "(" + attributeName + ")";
      }
   }

   private static boolean isSemanticAttribute(String attributeName) {
      for(int i = 0; i < ATTRIBUTES.length; ++i) {
         if (attributeName.equals(ATTRIBUTES[i])) {
            return true;
         }
      }

      return false;
   }

   private String getAttributeValue(Node attributeNode) {
      String nodeValue = attributeNode.getNodeValue().trim();
      if (this.semanticAttributes) {
         String attributeName = attributeNode.getNodeName();
         nodeValue = this.getProject().replaceProperties(nodeValue);
         if (attributeName.equals("location")) {
            File f = this.resolveFile(nodeValue);
            return f.getPath();
         }

         if (attributeName.equals("refid")) {
            Object ref = this.getProject().getReference(nodeValue);
            if (ref != null) {
               return ref.toString();
            }
         }
      }

      return nodeValue;
   }

   public void setFile(File src) {
      this.setSrcResource(new FileResource(src));
   }

   public void setSrcResource(Resource src) {
      if (src.isDirectory()) {
         throw new BuildException("the source can't be a directory");
      } else if (src instanceof FileResource && !this.supportsNonFileResources()) {
         throw new BuildException("Only FileSystem resources are supported.");
      } else {
         this.src = src;
      }
   }

   public void addConfigured(ResourceCollection a) {
      if (a.size() != 1) {
         throw new BuildException("only single argument resource collections are supported as archives");
      } else {
         this.setSrcResource((Resource)a.iterator().next());
      }
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix.trim();
   }

   public void setKeeproot(boolean keepRoot) {
      this.keepRoot = keepRoot;
   }

   public void setValidate(boolean validate) {
      this.validate = validate;
   }

   public void setCollapseAttributes(boolean collapseAttributes) {
      this.collapseAttributes = collapseAttributes;
   }

   public void setSemanticAttributes(boolean semanticAttributes) {
      this.semanticAttributes = semanticAttributes;
   }

   public void setRootDirectory(File rootDirectory) {
      this.rootDirectory = rootDirectory;
   }

   public void setIncludeSemanticAttribute(boolean includeSemanticAttribute) {
      this.includeSemanticAttribute = includeSemanticAttribute;
   }

   public void addConfiguredXMLCatalog(XMLCatalog catalog) {
      this.xmlCatalog.addConfiguredXMLCatalog(catalog);
   }

   protected File getFile() {
      return this.src instanceof FileResource ? ((FileResource)this.src).getFile() : null;
   }

   protected Resource getResource() {
      File f = this.getFile();
      return (Resource)(f != null ? new FileResource(f) : this.src);
   }

   protected String getPrefix() {
      return this.prefix;
   }

   protected boolean getKeeproot() {
      return this.keepRoot;
   }

   protected boolean getValidate() {
      return this.validate;
   }

   protected boolean getCollapseAttributes() {
      return this.collapseAttributes;
   }

   protected boolean getSemanticAttributes() {
      return this.semanticAttributes;
   }

   protected File getRootDirectory() {
      return this.rootDirectory;
   }

   protected boolean getIncludeSementicAttribute() {
      return this.includeSemanticAttribute;
   }

   private File resolveFile(String fileName) {
      return this.rootDirectory == null ? FILE_UTILS.resolveFile(this.getProject().getBaseDir(), fileName) : FILE_UTILS.resolveFile(this.rootDirectory, fileName);
   }

   protected boolean supportsNonFileResources() {
      return this.getClass().equals(class$org$apache$tools$ant$taskdefs$XmlProperty == null ? (class$org$apache$tools$ant$taskdefs$XmlProperty = class$("org.apache.tools.ant.taskdefs.XmlProperty")) : class$org$apache$tools$ant$taskdefs$XmlProperty);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
