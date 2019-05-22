package org.apache.maven.project.interpolation;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.project.DefaultProjectBuilderConfiguration;
import org.apache.maven.project.ProjectBuilderConfiguration;
import org.apache.maven.project.path.PathTranslator;
import org.codehaus.plexus.interpolation.AbstractValueSource;
import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.interpolation.InterpolationPostProcessor;
import org.codehaus.plexus.interpolation.Interpolator;
import org.codehaus.plexus.interpolation.MapBasedValueSource;
import org.codehaus.plexus.interpolation.ObjectBasedValueSource;
import org.codehaus.plexus.interpolation.PrefixAwareRecursionInterceptor;
import org.codehaus.plexus.interpolation.PrefixedObjectValueSource;
import org.codehaus.plexus.interpolation.PrefixedValueSourceWrapper;
import org.codehaus.plexus.interpolation.RecursionInterceptor;
import org.codehaus.plexus.interpolation.ValueSource;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public abstract class AbstractStringBasedModelInterpolator extends AbstractLogEnabled implements ModelInterpolator, Initializable {
   private static final List<String> PROJECT_PREFIXES = Arrays.asList("pom.", "project.");
   private static final List<String> TRANSLATED_PATH_EXPRESSIONS;
   private PathTranslator pathTranslator;
   private Interpolator interpolator;
   private RecursionInterceptor recursionInterceptor;

   protected AbstractStringBasedModelInterpolator(PathTranslator pathTranslator) {
      this.pathTranslator = pathTranslator;
   }

   protected AbstractStringBasedModelInterpolator() {
   }

   public Model interpolate(Model model, Map<String, ?> context) throws ModelInterpolationException {
      return this.interpolate(model, context, true);
   }

   /** @deprecated */
   public Model interpolate(Model model, Map<String, ?> context, boolean strict) throws ModelInterpolationException {
      Properties props = new Properties();
      props.putAll(context);
      return this.interpolate(model, (File)null, (new DefaultProjectBuilderConfiguration()).setExecutionProperties(props), true);
   }

   public Model interpolate(Model model, File projectDir, ProjectBuilderConfiguration config, boolean debugEnabled) throws ModelInterpolationException {
      StringWriter sWriter = new StringWriter(1024);
      MavenXpp3Writer writer = new MavenXpp3Writer();

      try {
         writer.write(sWriter, model);
      } catch (IOException var13) {
         throw new ModelInterpolationException("Cannot serialize project model for interpolation.", var13);
      }

      String serializedModel = sWriter.toString();
      serializedModel = this.interpolate(serializedModel, model, projectDir, config, debugEnabled);
      StringReader sReader = new StringReader(serializedModel);
      MavenXpp3Reader modelReader = new MavenXpp3Reader();

      try {
         model = modelReader.read((Reader)sReader);
         return model;
      } catch (IOException var11) {
         throw new ModelInterpolationException("Cannot read project model from interpolating filter of serialized version.", var11);
      } catch (XmlPullParserException var12) {
         throw new ModelInterpolationException("Cannot read project model from interpolating filter of serialized version.", var12);
      }
   }

   public String interpolate(String src, Model model, File projectDir, ProjectBuilderConfiguration config, boolean debug) throws ModelInterpolationException {
      String var8;
      try {
         List<ValueSource> valueSources = this.createValueSources(model, projectDir, config);
         List<InterpolationPostProcessor> postProcessors = this.createPostProcessors(model, projectDir, config);
         var8 = this.interpolateInternal(src, valueSources, postProcessors, debug);
      } finally {
         this.interpolator.clearAnswers();
      }

      return var8;
   }

   protected List<ValueSource> createValueSources(Model model, final File projectDir, final ProjectBuilderConfiguration config) {
      String timestampFormat = "yyyyMMdd-HHmm";
      Properties modelProperties = model.getProperties();
      if (modelProperties != null) {
         timestampFormat = modelProperties.getProperty("maven.build.timestamp.format", timestampFormat);
      }

      ValueSource modelValueSource1 = new PrefixedObjectValueSource(PROJECT_PREFIXES, model, false);
      ValueSource modelValueSource2 = new ObjectBasedValueSource(model);
      ValueSource basedirValueSource = new PrefixedValueSourceWrapper(new AbstractValueSource(false) {
         public Object getValue(String expression) {
            return projectDir != null && "basedir".equals(expression) ? projectDir.getAbsolutePath() : null;
         }
      }, PROJECT_PREFIXES, true);
      ValueSource baseUriValueSource = new PrefixedValueSourceWrapper(new AbstractValueSource(false) {
         public Object getValue(String expression) {
            return projectDir != null && "baseUri".equals(expression) ? projectDir.getAbsoluteFile().toURI().toString() : null;
         }
      }, PROJECT_PREFIXES, false);
      List<ValueSource> valueSources = new ArrayList(9);
      valueSources.add(basedirValueSource);
      valueSources.add(baseUriValueSource);
      valueSources.add(new BuildTimestampValueSource(config.getBuildStartTime(), timestampFormat));
      valueSources.add(modelValueSource1);
      valueSources.add(new MapBasedValueSource(config.getUserProperties()));
      valueSources.add(new MapBasedValueSource(modelProperties));
      valueSources.add(new MapBasedValueSource(config.getExecutionProperties()));
      valueSources.add(new AbstractValueSource(false) {
         public Object getValue(String expression) {
            return config.getExecutionProperties().getProperty("env." + expression);
         }
      });
      valueSources.add(modelValueSource2);
      return valueSources;
   }

   protected List<InterpolationPostProcessor> createPostProcessors(Model model, File projectDir, ProjectBuilderConfiguration config) {
      return Collections.singletonList(new PathTranslatingPostProcessor(PROJECT_PREFIXES, TRANSLATED_PATH_EXPRESSIONS, projectDir, this.pathTranslator));
   }

   protected String interpolateInternal(String src, List<ValueSource> valueSources, List<InterpolationPostProcessor> postProcessors, boolean debug) throws ModelInterpolationException {
      if (src.indexOf("${") < 0) {
         return src;
      } else {
         Logger logger = this.getLogger();
         String result = src;
         synchronized(this) {
            Iterator i$ = valueSources.iterator();

            ValueSource vs;
            while(i$.hasNext()) {
               vs = (ValueSource)i$.next();
               this.interpolator.addValueSource(vs);
            }

            i$ = postProcessors.iterator();

            InterpolationPostProcessor postProcessor;
            while(i$.hasNext()) {
               postProcessor = (InterpolationPostProcessor)i$.next();
               this.interpolator.addPostProcessor(postProcessor);
            }

            boolean var19 = false;

            try {
               try {
                  var19 = true;
                  result = this.interpolator.interpolate(result, this.recursionInterceptor);
               } catch (InterpolationException var20) {
                  throw new ModelInterpolationException(var20.getMessage(), var20);
               }

               if (debug) {
                  List<Object> feedback = this.interpolator.getFeedback();
                  if (feedback != null && !feedback.isEmpty()) {
                     logger.debug("Maven encountered the following problems during initial POM interpolation:");
                     Object last = null;
                     Iterator i$ = feedback.iterator();

                     while(i$.hasNext()) {
                        Object next = i$.next();
                        if (next instanceof Throwable) {
                           if (last == null) {
                              logger.debug("", (Throwable)next);
                           } else {
                              logger.debug(String.valueOf(last), (Throwable)next);
                           }
                        } else {
                           if (last != null) {
                              logger.debug(String.valueOf(last));
                           }

                           last = next;
                        }
                     }

                     if (last != null) {
                        logger.debug(String.valueOf(last));
                     }
                  }
               }

               this.interpolator.clearFeedback();
               var19 = false;
            } finally {
               if (var19) {
                  Iterator i$ = valueSources.iterator();

                  while(i$.hasNext()) {
                     ValueSource vs = (ValueSource)i$.next();
                     this.interpolator.removeValuesSource(vs);
                  }

                  i$ = postProcessors.iterator();

                  while(i$.hasNext()) {
                     InterpolationPostProcessor postProcessor = (InterpolationPostProcessor)i$.next();
                     this.interpolator.removePostProcessor(postProcessor);
                  }

               }
            }

            i$ = valueSources.iterator();

            while(i$.hasNext()) {
               vs = (ValueSource)i$.next();
               this.interpolator.removeValuesSource(vs);
            }

            i$ = postProcessors.iterator();

            while(i$.hasNext()) {
               postProcessor = (InterpolationPostProcessor)i$.next();
               this.interpolator.removePostProcessor(postProcessor);
            }

            return result;
         }
      }
   }

   protected RecursionInterceptor getRecursionInterceptor() {
      return this.recursionInterceptor;
   }

   protected void setRecursionInterceptor(RecursionInterceptor recursionInterceptor) {
      this.recursionInterceptor = recursionInterceptor;
   }

   protected abstract Interpolator createInterpolator();

   public void initialize() throws InitializationException {
      this.interpolator = this.createInterpolator();
      this.recursionInterceptor = new PrefixAwareRecursionInterceptor(PROJECT_PREFIXES);
   }

   protected final Interpolator getInterpolator() {
      return this.interpolator;
   }

   static {
      List<String> translatedPrefixes = new ArrayList();
      translatedPrefixes.add("build.directory");
      translatedPrefixes.add("build.outputDirectory");
      translatedPrefixes.add("build.testOutputDirectory");
      translatedPrefixes.add("build.sourceDirectory");
      translatedPrefixes.add("build.testSourceDirectory");
      translatedPrefixes.add("build.scriptSourceDirectory");
      translatedPrefixes.add("reporting.outputDirectory");
      TRANSLATED_PATH_EXPRESSIONS = translatedPrefixes;
   }
}
