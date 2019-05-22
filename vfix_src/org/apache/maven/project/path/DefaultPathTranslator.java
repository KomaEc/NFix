package org.apache.maven.project.path;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Resource;

public class DefaultPathTranslator implements PathTranslator {
   private static final String[] BASEDIR_EXPRESSIONS = new String[]{"${basedir}", "${pom.basedir}", "${project.basedir}"};

   public void alignToBaseDirectory(Model model, File basedir) {
      if (basedir != null) {
         Build build = model.getBuild();
         if (build != null) {
            build.setDirectory(this.alignToBaseDirectory(build.getDirectory(), basedir));
            build.setSourceDirectory(this.alignToBaseDirectory(build.getSourceDirectory(), basedir));
            build.setTestSourceDirectory(this.alignToBaseDirectory(build.getTestSourceDirectory(), basedir));
            Iterator i = build.getResources().iterator();

            Resource resource;
            while(i.hasNext()) {
               resource = (Resource)i.next();
               resource.setDirectory(this.alignToBaseDirectory(resource.getDirectory(), basedir));
            }

            i = build.getTestResources().iterator();

            while(i.hasNext()) {
               resource = (Resource)i.next();
               resource.setDirectory(this.alignToBaseDirectory(resource.getDirectory(), basedir));
            }

            if (build.getFilters() != null) {
               List filters = new ArrayList();
               Iterator i = build.getFilters().iterator();

               while(i.hasNext()) {
                  String filter = (String)i.next();
                  filters.add(this.alignToBaseDirectory(filter, basedir));
               }

               build.setFilters(filters);
            }

            build.setOutputDirectory(this.alignToBaseDirectory(build.getOutputDirectory(), basedir));
            build.setTestOutputDirectory(this.alignToBaseDirectory(build.getTestOutputDirectory(), basedir));
         }

         Reporting reporting = model.getReporting();
         if (reporting != null) {
            reporting.setOutputDirectory(this.alignToBaseDirectory(reporting.getOutputDirectory(), basedir));
         }

      }
   }

   public String alignToBaseDirectory(String path, File basedir) {
      if (basedir == null) {
         return path;
      } else if (path == null) {
         return null;
      } else {
         String s = this.stripBasedirToken(path);
         File file = new File(s);
         if (file.isAbsolute()) {
            s = file.getPath();
         } else if (file.getPath().startsWith(File.separator)) {
            s = file.getAbsolutePath();
         } else {
            s = (new File((new File(basedir, s)).toURI().normalize())).getAbsolutePath();
         }

         return s;
      }
   }

   private String stripBasedirToken(String s) {
      if (s != null) {
         String basedirExpr = null;

         for(int i = 0; i < BASEDIR_EXPRESSIONS.length; ++i) {
            basedirExpr = BASEDIR_EXPRESSIONS[i];
            if (s.startsWith(basedirExpr)) {
               break;
            }

            basedirExpr = null;
         }

         if (basedirExpr != null) {
            if (s.length() > basedirExpr.length()) {
               s = this.chopLeadingFileSeparator(s.substring(basedirExpr.length()));
            } else {
               s = ".";
            }
         }
      }

      return s;
   }

   private String chopLeadingFileSeparator(String path) {
      if (path != null && (path.startsWith("/") || path.startsWith("\\"))) {
         path = path.substring(1);
      }

      return path;
   }

   public void unalignFromBaseDirectory(Model model, File basedir) {
      if (basedir != null) {
         Build build = model.getBuild();
         if (build != null) {
            build.setDirectory(this.unalignFromBaseDirectory(build.getDirectory(), basedir));
            build.setSourceDirectory(this.unalignFromBaseDirectory(build.getSourceDirectory(), basedir));
            build.setTestSourceDirectory(this.unalignFromBaseDirectory(build.getTestSourceDirectory(), basedir));
            build.setScriptSourceDirectory(this.unalignFromBaseDirectory(build.getScriptSourceDirectory(), basedir));
            Iterator i = build.getResources().iterator();

            Resource resource;
            while(i.hasNext()) {
               resource = (Resource)i.next();
               resource.setDirectory(this.unalignFromBaseDirectory(resource.getDirectory(), basedir));
            }

            i = build.getTestResources().iterator();

            while(i.hasNext()) {
               resource = (Resource)i.next();
               resource.setDirectory(this.unalignFromBaseDirectory(resource.getDirectory(), basedir));
            }

            if (build.getFilters() != null) {
               List filters = new ArrayList();
               Iterator i = build.getFilters().iterator();

               while(i.hasNext()) {
                  String filter = (String)i.next();
                  filters.add(this.unalignFromBaseDirectory(filter, basedir));
               }

               build.setFilters(filters);
            }

            build.setOutputDirectory(this.unalignFromBaseDirectory(build.getOutputDirectory(), basedir));
            build.setTestOutputDirectory(this.unalignFromBaseDirectory(build.getTestOutputDirectory(), basedir));
         }

         Reporting reporting = model.getReporting();
         if (reporting != null) {
            reporting.setOutputDirectory(this.unalignFromBaseDirectory(reporting.getOutputDirectory(), basedir));
         }

      }
   }

   public String unalignFromBaseDirectory(String path, File basedir) {
      if (basedir == null) {
         return path;
      } else if (path == null) {
         return null;
      } else {
         path = path.trim();
         String base = basedir.getAbsolutePath();
         if (path.startsWith(base)) {
            path = this.chopLeadingFileSeparator(path.substring(base.length()));
         }

         if (!(new File(path)).isAbsolute()) {
            path = path.replace('\\', '/');
         }

         return path;
      }
   }
}
