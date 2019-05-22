package groovy.grape;

import groovy.lang.Grab;
import groovy.lang.GrabConfig;
import groovy.lang.GrabExclude;
import groovy.lang.GrabResolver;
import groovy.lang.Grapes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ImportNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.ASTTransformationVisitor;
import org.codehaus.groovy.transform.GroovyASTTransformation;

@GroovyASTTransformation(
   phase = CompilePhase.CONVERSION
)
public class GrabAnnotationTransformation extends ClassCodeVisitorSupport implements ASTTransformation {
   private static final String GRAB_CLASS_NAME = Grab.class.getName();
   private static final String GRAB_DOT_NAME;
   private static final String GRAB_SHORT_NAME;
   private static final String GRABEXCLUDE_CLASS_NAME;
   private static final String GRABEXCLUDE_DOT_NAME;
   private static final String GRABEXCLUDE_SHORT_NAME;
   private static final String GRABCONFIG_CLASS_NAME;
   private static final String GRABCONFIG_DOT_NAME;
   private static final String GRABCONFIG_SHORT_NAME;
   private static final String GRAPES_CLASS_NAME;
   private static final String GRAPES_DOT_NAME;
   private static final String GRAPES_SHORT_NAME;
   private static final String GRABRESOLVER_CLASS_NAME;
   private static final String GRAPERESOLVER_DOT_NAME;
   private static final String GRABRESOLVER_SHORT_NAME;
   private static final ClassNode THREAD_CLASSNODE;
   private static final List<String> GRABEXCLUDE_REQUIRED;
   private static final List<String> GRAPERESOLVER_REQUIRED;
   private static final List<String> GRAB_REQUIRED;
   private static final List<String> GRAB_OPTIONAL;
   private static final Collection<String> GRAB_ALL;
   private static final Pattern IVY_PATTERN;
   boolean allowShortGrab;
   Set<String> grabAliases;
   List<AnnotationNode> grabAnnotations;
   boolean allowShortGrabExcludes;
   Set<String> grabExcludeAliases;
   List<AnnotationNode> grabExcludeAnnotations;
   boolean allowShortGrabConfig;
   Set<String> grabConfigAliases;
   List<AnnotationNode> grabConfigAnnotations;
   boolean allowShortGrapes;
   Set<String> grapesAliases;
   List<AnnotationNode> grapesAnnotations;
   boolean allowShortGrabResolver;
   Set<String> grabResolverAliases;
   List<AnnotationNode> grabResolverAnnotations;
   SourceUnit sourceUnit;
   ClassLoader loader;
   boolean initContextClassLoader;

   private static String dotName(String className) {
      return className.substring(className.lastIndexOf("."));
   }

   private static String shortName(String className) {
      return className.substring(1);
   }

   public SourceUnit getSourceUnit() {
      return this.sourceUnit;
   }

   public void visit(ASTNode[] nodes, SourceUnit source) {
      this.sourceUnit = source;
      this.loader = null;
      this.initContextClassLoader = false;
      ModuleNode mn = (ModuleNode)nodes[0];
      this.allowShortGrab = true;
      this.allowShortGrabExcludes = true;
      this.allowShortGrabConfig = true;
      this.allowShortGrapes = true;
      this.allowShortGrabResolver = true;
      this.grabAliases = new HashSet();
      this.grabExcludeAliases = new HashSet();
      this.grabConfigAliases = new HashSet();
      this.grapesAliases = new HashSet();
      this.grabResolverAliases = new HashSet();
      Iterator i$ = mn.getImports().iterator();

      while(true) {
         while(i$.hasNext()) {
            ImportNode im = (ImportNode)i$.next();
            String alias = im.getAlias();
            String className = im.getClassName();
            if ((!className.endsWith(GRAB_DOT_NAME) || alias != null && alias.length() != 0) && !GRAB_CLASS_NAME.equals(alias)) {
               if (GRAB_CLASS_NAME.equals(className)) {
                  this.grabAliases.add(im.getAlias());
               }
            } else {
               this.allowShortGrab = false;
            }

            if ((!className.endsWith(GRAPES_DOT_NAME) || alias != null && alias.length() != 0) && !GRAPES_CLASS_NAME.equals(alias)) {
               if (GRAPES_CLASS_NAME.equals(className)) {
                  this.grapesAliases.add(im.getAlias());
               }
            } else {
               this.allowShortGrapes = false;
            }

            if (className.endsWith(GRAPERESOLVER_DOT_NAME) && (alias == null || alias.length() == 0) || GRABRESOLVER_CLASS_NAME.equals(alias)) {
               this.allowShortGrabResolver = false;
            } else if (GRABRESOLVER_CLASS_NAME.equals(className)) {
               this.grabResolverAliases.add(im.getAlias());
            }
         }

         List<Map<String, Object>> grabMaps = new ArrayList();
         List<Map<String, Object>> grabExcludeMaps = new ArrayList();
         Iterator i$ = this.sourceUnit.getAST().getClasses().iterator();

         label238:
         while(true) {
            ClassNode grapeClassNode;
            Iterator i$;
            AnnotationNode node;
            HashMap grabMap;
            ClassNode classNode;
            Iterator i$;
            String s;
            Expression member;
            label218:
            do {
               if (!i$.hasNext()) {
                  if (!grabMaps.isEmpty()) {
                     Map<String, Object> basicArgs = new HashMap();
                     basicArgs.put("classLoader", this.loader != null ? this.loader : this.sourceUnit.getClassLoader());
                     if (!grabExcludeMaps.isEmpty()) {
                        basicArgs.put("excludes", grabExcludeMaps);
                     }

                     try {
                        Grape.grab(basicArgs, (Map[])grabMaps.toArray(new Map[grabMaps.size()]));
                        ASTTransformationVisitor.addGlobalTransformsAfterGrab();
                     } catch (RuntimeException var18) {
                        source.addException(var18);
                     }
                  }

                  return;
               }

               classNode = (ClassNode)i$.next();
               this.grabAnnotations = new ArrayList();
               this.grabExcludeAnnotations = new ArrayList();
               this.grabConfigAnnotations = new ArrayList();
               this.grapesAnnotations = new ArrayList();
               this.grabResolverAnnotations = new ArrayList();
               this.visitClass(classNode);
               grapeClassNode = new ClassNode(Grape.class);
               Expression value;
               if (!this.grabResolverAnnotations.isEmpty()) {
                  i$ = this.grabResolverAnnotations.iterator();

                  label165:
                  while(true) {
                     label163:
                     while(true) {
                        if (!i$.hasNext()) {
                           break label165;
                        }

                        node = (AnnotationNode)i$.next();
                        grabMap = new HashMap();
                        value = node.getMember("value");
                        ConstantExpression ce = null;
                        if (value != null && value instanceof ConstantExpression) {
                           ce = (ConstantExpression)value;
                        }

                        String sval = null;
                        if (ce != null && ce.getValue() instanceof String) {
                           sval = (String)ce.getValue();
                        }

                        Iterator i$;
                        String s;
                        Expression member;
                        if (sval != null && sval.length() > 0) {
                           i$ = GRAPERESOLVER_REQUIRED.iterator();

                           while(i$.hasNext()) {
                              s = (String)i$.next();
                              member = node.getMember(s);
                              if (member != null) {
                                 this.addError("The attribute \"" + s + "\" conflicts with attribute 'value' in @" + node.getClassNode().getNameWithoutPackage() + " annotations", node);
                                 continue label163;
                              }
                           }

                           grabMap.put("name", sval);
                           grabMap.put("root", sval);
                        } else {
                           i$ = GRAPERESOLVER_REQUIRED.iterator();

                           while(i$.hasNext()) {
                              s = (String)i$.next();
                              member = node.getMember(s);
                              if (member == null) {
                                 this.addError("The missing attribute \"" + s + "\" is required in @" + node.getClassNode().getNameWithoutPackage() + " annotations", node);
                                 continue label163;
                              }

                              if (member != null && !(member instanceof ConstantExpression)) {
                                 this.addError("Attribute \"" + s + "\" has value " + member.getText() + " but should be an inline constant in @" + node.getClassNode().getNameWithoutPackage() + " annotations", node);
                                 continue label163;
                              }

                              grabMap.put(s, ((ConstantExpression)member).getValue());
                           }
                        }

                        Grape.addResolver(grabMap);
                     }
                  }
               }

               if (!this.grapesAnnotations.isEmpty()) {
                  i$ = this.grapesAnnotations.iterator();

                  label183:
                  while(true) {
                     while(true) {
                        if (!i$.hasNext()) {
                           break label183;
                        }

                        node = (AnnotationNode)i$.next();
                        Expression init = node.getMember("initClass");
                        value = node.getMember("value");
                        if (value instanceof ListExpression) {
                           Iterator i$ = ((ListExpression)value).getExpressions().iterator();

                           while(i$.hasNext()) {
                              Object o = i$.next();
                              if (o instanceof ConstantExpression) {
                                 this.extractGrab(init, (ConstantExpression)o);
                              }
                           }
                        } else if (value instanceof ConstantExpression) {
                           this.extractGrab(init, (ConstantExpression)value);
                        }
                     }
                  }
               }

               if (!this.grabConfigAnnotations.isEmpty()) {
                  i$ = this.grabConfigAnnotations.iterator();

                  while(i$.hasNext()) {
                     node = (AnnotationNode)i$.next();
                     this.checkForClassLoader(node);
                     this.checkForInitContextClassLoader(node);
                  }

                  this.addInitContextClassLoaderIfNeeded(classNode);
               }

               if (!this.grabExcludeAnnotations.isEmpty()) {
                  i$ = this.grabExcludeAnnotations.iterator();

                  while(true) {
                     label211:
                     while(true) {
                        if (!i$.hasNext()) {
                           continue label218;
                        }

                        node = (AnnotationNode)i$.next();
                        grabMap = new HashMap();
                        this.checkForConvenienceForm(node, true);
                        i$ = GRABEXCLUDE_REQUIRED.iterator();

                        while(i$.hasNext()) {
                           s = (String)i$.next();
                           member = node.getMember(s);
                           if (member == null) {
                              this.addError("The missing attribute \"" + s + "\" is required in @" + node.getClassNode().getNameWithoutPackage() + " annotations", node);
                              continue label211;
                           }

                           if (member != null && !(member instanceof ConstantExpression)) {
                              this.addError("Attribute \"" + s + "\" has value " + member.getText() + " but should be an inline constant in @" + node.getClassNode().getNameWithoutPackage() + " annotations", node);
                              continue label211;
                           }

                           grabMap.put(s, ((ConstantExpression)member).getValue());
                        }

                        grabExcludeMaps.add(grabMap);
                     }
                  }
               }
            } while(this.grabAnnotations.isEmpty());

            i$ = this.grabAnnotations.iterator();

            while(true) {
               label234:
               while(true) {
                  if (!i$.hasNext()) {
                     continue label238;
                  }

                  node = (AnnotationNode)i$.next();
                  grabMap = new HashMap();
                  this.checkForConvenienceForm(node, false);
                  i$ = GRAB_ALL.iterator();

                  while(i$.hasNext()) {
                     s = (String)i$.next();
                     member = node.getMember(s);
                     if (member == null && !GRAB_OPTIONAL.contains(s)) {
                        this.addError("The missing attribute \"" + s + "\" is required in @" + node.getClassNode().getNameWithoutPackage() + " annotations", node);
                        continue label234;
                     }

                     if (member != null && !(member instanceof ConstantExpression)) {
                        this.addError("Attribute \"" + s + "\" has value " + member.getText() + " but should be an inline constant in @" + node.getClassNode().getNameWithoutPackage() + " annotations", node);
                        continue label234;
                     }

                     if (node.getMember(s) != null) {
                        grabMap.put(s, ((ConstantExpression)member).getValue());
                     }
                  }

                  grabMaps.add(grabMap);
                  this.callGrabAsStaticInitIfNeeded(classNode, grapeClassNode, node, grabExcludeMaps);
               }
            }
         }
      }
   }

   private void callGrabAsStaticInitIfNeeded(ClassNode classNode, ClassNode grapeClassNode, AnnotationNode node, List<Map<String, Object>> grabExcludeMaps) {
      if (node.getMember("initClass") == null || node.getMember("initClass") == ConstantExpression.TRUE) {
         List<Statement> grabInitializers = new ArrayList();
         MapExpression me = new MapExpression();
         Iterator i$ = GRAB_REQUIRED.iterator();

         String s;
         while(i$.hasNext()) {
            s = (String)i$.next();
            me.addMapEntryExpression(new ConstantExpression(s), node.getMember(s));
         }

         i$ = GRAB_OPTIONAL.iterator();

         while(i$.hasNext()) {
            s = (String)i$.next();
            if (node.getMember(s) != null) {
               me.addMapEntryExpression(new ConstantExpression(s), node.getMember(s));
            }
         }

         ArgumentListExpression grabArgs;
         if (grabExcludeMaps.isEmpty()) {
            grabArgs = new ArgumentListExpression(me);
         } else {
            MapExpression args = new MapExpression();
            ListExpression list = new ListExpression();
            Iterator i$ = grabExcludeMaps.iterator();

            while(true) {
               if (!i$.hasNext()) {
                  args.addMapEntryExpression(new ConstantExpression("excludes"), list);
                  grabArgs = new ArgumentListExpression(args, me);
                  break;
               }

               Map<String, Object> map = (Map)i$.next();
               Set<Entry<String, Object>> entries = map.entrySet();
               MapExpression inner = new MapExpression();
               Iterator i$ = entries.iterator();

               while(i$.hasNext()) {
                  Entry<String, Object> entry = (Entry)i$.next();
                  inner.addMapEntryExpression(new ConstantExpression(entry.getKey()), new ConstantExpression(entry.getValue()));
               }

               list.addExpression(inner);
            }
         }

         grabInitializers.add(new ExpressionStatement(new StaticMethodCallExpression(grapeClassNode, "grab", grabArgs)));
         classNode.addStaticInitializerStatements(grabInitializers, true);
      }

   }

   private void addInitContextClassLoaderIfNeeded(ClassNode classNode) {
      if (this.initContextClassLoader) {
         Statement initStatement = new ExpressionStatement(new MethodCallExpression(new StaticMethodCallExpression(THREAD_CLASSNODE, "currentThread", ArgumentListExpression.EMPTY_ARGUMENTS), "setContextClassLoader", new MethodCallExpression(new MethodCallExpression(VariableExpression.THIS_EXPRESSION, "getClass", MethodCallExpression.NO_ARGUMENTS), "getClassLoader", ArgumentListExpression.EMPTY_ARGUMENTS)));
         classNode.addObjectInitializerStatements(initStatement);
      }

   }

   private void checkForClassLoader(AnnotationNode node) {
      Object val = node.getMember("systemClassLoader");
      if (val != null && val instanceof ConstantExpression) {
         Object systemClassLoaderObject = ((ConstantExpression)val).getValue();
         if (systemClassLoaderObject instanceof Boolean) {
            Boolean systemClassLoader = (Boolean)systemClassLoaderObject;
            if (systemClassLoader) {
               this.loader = ClassLoader.getSystemClassLoader();
            }

         }
      }
   }

   private void checkForInitContextClassLoader(AnnotationNode node) {
      Object val = node.getMember("initContextClassLoader");
      if (val != null && val instanceof ConstantExpression) {
         Object initContextClassLoaderObject = ((ConstantExpression)val).getValue();
         if (initContextClassLoaderObject instanceof Boolean) {
            this.initContextClassLoader = (Boolean)initContextClassLoaderObject;
         }
      }
   }

   private void checkForConvenienceForm(AnnotationNode node, boolean exclude) {
      Object val = node.getMember("value");
      if (val != null && val instanceof ConstantExpression) {
         Object allParts = ((ConstantExpression)val).getValue();
         if (allParts instanceof String) {
            String allstr = (String)allParts;
            if (allstr.contains("#")) {
               Matcher m = IVY_PATTERN.matcher(allstr);
               if (!m.find()) {
                  return;
               }

               if (m.group(1) == null || m.group(2) == null) {
                  return;
               }

               node.addMember("module", new ConstantExpression(m.group(2)));
               node.addMember("group", new ConstantExpression(m.group(1)));
               if (m.group(6) != null) {
                  node.addMember("conf", new ConstantExpression(m.group(6)));
               }

               if (m.group(4) != null) {
                  node.addMember("version", new ConstantExpression(m.group(4)));
               } else if (!exclude) {
                  node.addMember("version", new ConstantExpression("*"));
               }

               node.getMembers().remove("value");
            } else if (allstr.contains(":")) {
               String ext = "";
               String[] parts;
               if (allstr.contains("@")) {
                  parts = allstr.split("@");
                  if (parts.length > 2) {
                     return;
                  }

                  allstr = parts[0];
                  ext = parts[1];
               }

               parts = allstr.split(":");
               if (parts.length > 4) {
                  return;
               }

               if (parts.length > 3) {
                  node.addMember("classifier", new ConstantExpression(parts[3]));
               }

               if (parts.length > 2) {
                  node.addMember("version", new ConstantExpression(parts[2]));
               } else if (!exclude) {
                  node.addMember("version", new ConstantExpression("*"));
               }

               if (ext.length() > 0) {
                  node.addMember("ext", new ConstantExpression(ext));
               }

               node.addMember("module", new ConstantExpression(parts[1]));
               node.addMember("group", new ConstantExpression(parts[0]));
               node.getMembers().remove("value");
            }

         }
      }
   }

   private void extractGrab(Expression init, ConstantExpression ce) {
      if (ce.getValue() instanceof AnnotationNode) {
         AnnotationNode annotation = (AnnotationNode)ce.getValue();
         if (init != null && annotation.getMember("initClass") != null) {
            annotation.setMember("initClass", init);
         }

         String name = annotation.getClassNode().getName();
         if (GRAB_CLASS_NAME.equals(name) || this.allowShortGrab && GRAB_SHORT_NAME.equals(name) || this.grabAliases.contains(name)) {
            this.grabAnnotations.add(annotation);
         }

         if (GRABEXCLUDE_CLASS_NAME.equals(name) || this.allowShortGrabExcludes && GRABEXCLUDE_SHORT_NAME.equals(name) || this.grabExcludeAliases.contains(name)) {
            this.grabExcludeAnnotations.add(annotation);
         }

         if (GRABCONFIG_CLASS_NAME.equals(name) || this.allowShortGrabConfig && GRABCONFIG_SHORT_NAME.equals(name) || this.grabConfigAliases.contains(name)) {
            this.grabConfigAnnotations.add(annotation);
         }

         if (GRABRESOLVER_CLASS_NAME.equals(name) || this.allowShortGrabResolver && GRABRESOLVER_SHORT_NAME.equals(name) || this.grabResolverAliases.contains(name)) {
            this.grabResolverAnnotations.add(annotation);
         }
      }

   }

   public void visitAnnotations(AnnotatedNode node) {
      super.visitAnnotations(node);
      Iterator i$ = node.getAnnotations().iterator();

      while(true) {
         AnnotationNode an;
         String name;
         do {
            if (!i$.hasNext()) {
               return;
            }

            an = (AnnotationNode)i$.next();
            name = an.getClassNode().getName();
            if (GRAB_CLASS_NAME.equals(name) || this.allowShortGrab && GRAB_SHORT_NAME.equals(name) || this.grabAliases.contains(name)) {
               this.grabAnnotations.add(an);
            }

            if (GRABEXCLUDE_CLASS_NAME.equals(name) || this.allowShortGrabExcludes && GRABEXCLUDE_SHORT_NAME.equals(name) || this.grabExcludeAliases.contains(name)) {
               this.grabExcludeAnnotations.add(an);
            }

            if (GRABCONFIG_CLASS_NAME.equals(name) || this.allowShortGrabConfig && GRABCONFIG_SHORT_NAME.equals(name) || this.grabConfigAliases.contains(name)) {
               this.grabConfigAnnotations.add(an);
            }

            if (GRAPES_CLASS_NAME.equals(name) || this.allowShortGrapes && GRAPES_SHORT_NAME.equals(name) || this.grapesAliases.contains(name)) {
               this.grapesAnnotations.add(an);
            }
         } while(!GRABRESOLVER_CLASS_NAME.equals(name) && (!this.allowShortGrabResolver || !GRABRESOLVER_SHORT_NAME.equals(name)) && !this.grabResolverAliases.contains(name));

         this.grabResolverAnnotations.add(an);
      }
   }

   static {
      GRAB_DOT_NAME = GRAB_CLASS_NAME.substring(GRAB_CLASS_NAME.lastIndexOf("."));
      GRAB_SHORT_NAME = GRAB_DOT_NAME.substring(1);
      GRABEXCLUDE_CLASS_NAME = GrabExclude.class.getName();
      GRABEXCLUDE_DOT_NAME = dotName(GRABEXCLUDE_CLASS_NAME);
      GRABEXCLUDE_SHORT_NAME = shortName(GRABEXCLUDE_DOT_NAME);
      GRABCONFIG_CLASS_NAME = GrabConfig.class.getName();
      GRABCONFIG_DOT_NAME = dotName(GRABCONFIG_CLASS_NAME);
      GRABCONFIG_SHORT_NAME = shortName(GRABCONFIG_DOT_NAME);
      GRAPES_CLASS_NAME = Grapes.class.getName();
      GRAPES_DOT_NAME = dotName(GRAPES_CLASS_NAME);
      GRAPES_SHORT_NAME = shortName(GRAPES_DOT_NAME);
      GRABRESOLVER_CLASS_NAME = GrabResolver.class.getName();
      GRAPERESOLVER_DOT_NAME = dotName(GRABRESOLVER_CLASS_NAME);
      GRABRESOLVER_SHORT_NAME = shortName(GRAPERESOLVER_DOT_NAME);
      THREAD_CLASSNODE = new ClassNode(Thread.class);
      GRABEXCLUDE_REQUIRED = Arrays.asList("group", "module");
      GRAPERESOLVER_REQUIRED = Arrays.asList("name", "root");
      GRAB_REQUIRED = Arrays.asList("group", "module", "version");
      GRAB_OPTIONAL = Arrays.asList("classifier", "transitive", "conf", "ext");
      GRAB_ALL = DefaultGroovyMethods.plus((Collection)GRAB_REQUIRED, (Collection)GRAB_OPTIONAL);
      IVY_PATTERN = Pattern.compile("([a-zA-Z0-9-/._+=]+)#([a-zA-Z0-9-/._+=]+)(;([a-zA-Z0-9-/.\\(\\)\\[\\]\\{\\}_+=,:@][a-zA-Z0-9-/.\\(\\)\\]\\{\\}_+=,:@]*))?(\\[([a-zA-Z0-9-/._+=,]*)\\])?");
   }
}
