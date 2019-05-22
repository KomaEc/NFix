package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.UnsupportedEncodingException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.launch.Locator;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;

public class ManifestClassPath extends Task {
   private String name;
   private File dir;
   private int maxParentLevels = 2;
   private Path path;

   public void execute() {
      if (this.name == null) {
         throw new BuildException("Missing 'property' attribute!");
      } else if (this.dir == null) {
         throw new BuildException("Missing 'jarfile' attribute!");
      } else if (this.getProject().getProperty(this.name) != null) {
         throw new BuildException("Property '" + this.name + "' already set!");
      } else if (this.path == null) {
         throw new BuildException("Missing nested <classpath>!");
      } else {
         FileUtils fileUtils = FileUtils.getFileUtils();
         this.dir = fileUtils.normalize(this.dir.getAbsolutePath());
         File currDir = this.dir;
         String[] dirs = new String[this.maxParentLevels + 1];

         for(int i = 0; i < this.maxParentLevels + 1; ++i) {
            dirs[i] = currDir.getAbsolutePath() + File.separatorChar;
            currDir = currDir.getParentFile();
            if (currDir == null) {
               this.maxParentLevels = i + 1;
               break;
            }
         }

         String[] elements = this.path.list();
         StringBuffer buffer = new StringBuffer();
         StringBuffer element = new StringBuffer();

         for(int i = 0; i < elements.length; ++i) {
            File pathEntry = new File(elements[i]);
            pathEntry = fileUtils.normalize(pathEntry.getAbsolutePath());
            String fullPath = pathEntry.getAbsolutePath();
            String relPath = null;

            for(int j = 0; j <= this.maxParentLevels; ++j) {
               String dir = dirs[j];
               if (fullPath.startsWith(dir)) {
                  element.setLength(0);

                  for(int k = 0; k < j; ++k) {
                     element.append("..");
                     element.append(File.separatorChar);
                  }

                  element.append(fullPath.substring(dir.length()));
                  relPath = element.toString();
                  break;
               }
            }

            if (relPath == null) {
               throw new BuildException("No suitable relative path from " + this.dir + " to " + fullPath);
            }

            if (File.separatorChar != '/') {
               relPath = relPath.replace(File.separatorChar, '/');
            }

            if (pathEntry.isDirectory()) {
               relPath = relPath + '/';
            }

            try {
               relPath = Locator.encodeURI(relPath);
            } catch (UnsupportedEncodingException var14) {
               throw new BuildException(var14);
            }

            buffer.append(relPath);
            buffer.append(' ');
         }

         this.getProject().setNewProperty(this.name, buffer.toString().trim());
      }
   }

   public void setProperty(String name) {
      this.name = name;
   }

   public void setJarFile(File jarfile) {
      File parent = jarfile.getParentFile();
      if (!parent.isDirectory()) {
         throw new BuildException("Jar's directory not found: " + parent);
      } else {
         this.dir = parent;
      }
   }

   public void setMaxParentLevels(int levels) {
      this.maxParentLevels = levels;
   }

   public void addClassPath(Path path) {
      this.path = path;
   }
}
