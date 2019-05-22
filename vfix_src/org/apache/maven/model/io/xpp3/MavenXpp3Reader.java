package org.apache.maven.model.io.xpp3;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.maven.model.Activation;
import org.apache.maven.model.ActivationFile;
import org.apache.maven.model.ActivationOS;
import org.apache.maven.model.ActivationProperty;
import org.apache.maven.model.Build;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.ConfigurationContainer;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.DeploymentRepository;
import org.apache.maven.model.Developer;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Extension;
import org.apache.maven.model.FileSet;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.License;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.Model;
import org.apache.maven.model.ModelBase;
import org.apache.maven.model.Notifier;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Parent;
import org.apache.maven.model.PatternSet;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginConfiguration;
import org.apache.maven.model.PluginContainer;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.Prerequisites;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Relocation;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.ReportSet;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.apache.maven.model.RepositoryBase;
import org.apache.maven.model.RepositoryPolicy;
import org.apache.maven.model.Resource;
import org.apache.maven.model.Scm;
import org.apache.maven.model.Site;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class MavenXpp3Reader {
   private boolean addDefaultEntities = true;

   private boolean checkFieldWithDuplicate(XmlPullParser parser, String tagName, String alias, Set parsed) throws XmlPullParserException {
      if (!parser.getName().equals(tagName) && !parser.getName().equals(alias)) {
         return false;
      } else if (parsed.contains(tagName)) {
         throw new XmlPullParserException("Duplicated tag: '" + tagName + "'", parser, (Throwable)null);
      } else {
         parsed.add(tagName);
         return true;
      }
   }

   public boolean getAddDefaultEntities() {
      return this.addDefaultEntities;
   }

   private boolean getBooleanValue(String s, String attribute, XmlPullParser parser) throws XmlPullParserException {
      return this.getBooleanValue(s, attribute, parser, (String)null);
   }

   private boolean getBooleanValue(String s, String attribute, XmlPullParser parser, String defaultValue) throws XmlPullParserException {
      if (s != null && s.length() != 0) {
         return Boolean.valueOf(s);
      } else {
         return defaultValue != null ? Boolean.valueOf(defaultValue) : false;
      }
   }

   private byte getByteValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Byte.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a byte", parser, (Throwable)null);
            }
         }
      }

      return 0;
   }

   private char getCharacterValue(String s, String attribute, XmlPullParser parser) throws XmlPullParserException {
      return s != null ? s.charAt(0) : '\u0000';
   }

   private Date getDateValue(String s, String attribute, XmlPullParser parser) throws XmlPullParserException {
      return this.getDateValue(s, attribute, (String)null, parser);
   }

   private Date getDateValue(String s, String attribute, String dateFormat, XmlPullParser parser) throws XmlPullParserException {
      if (s != null) {
         String effectiveDateFormat = dateFormat;
         if (dateFormat == null) {
            effectiveDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
         }

         if ("long".equals(effectiveDateFormat)) {
            try {
               return new Date(Long.parseLong(s));
            } catch (NumberFormatException var7) {
               throw new XmlPullParserException(var7.getMessage());
            }
         } else {
            try {
               DateFormat dateParser = new SimpleDateFormat(effectiveDateFormat, Locale.US);
               return dateParser.parse(s);
            } catch (ParseException var8) {
               throw new XmlPullParserException(var8.getMessage());
            }
         }
      } else {
         return null;
      }
   }

   private double getDoubleValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Double.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a floating point number", parser, (Throwable)null);
            }
         }
      }

      return 0.0D;
   }

   private float getFloatValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Float.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a floating point number", parser, (Throwable)null);
            }
         }
      }

      return 0.0F;
   }

   private int getIntegerValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Integer.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be an integer", parser, (Throwable)null);
            }
         }
      }

      return 0;
   }

   private long getLongValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Long.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a long integer", parser, (Throwable)null);
            }
         }
      }

      return 0L;
   }

   private String getRequiredAttributeValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s == null && strict) {
         throw new XmlPullParserException("Missing required value for attribute '" + attribute + "'", parser, (Throwable)null);
      } else {
         return s;
      }
   }

   private short getShortValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Short.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a short integer", parser, (Throwable)null);
            }
         }
      }

      return 0;
   }

   private String getTrimmedValue(String s) {
      if (s != null) {
         s = s.trim();
      }

      return s;
   }

   private Activation parseActivation(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Activation activation = new Activation();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "activeByDefault", (String)null, parsed)) {
               activation.setActiveByDefault(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "activeByDefault", parser, "false"));
            } else if (this.checkFieldWithDuplicate(parser, "jdk", (String)null, parsed)) {
               activation.setJdk(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "os", (String)null, parsed)) {
               activation.setOs(this.parseActivationOS("os", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "property", (String)null, parsed)) {
               activation.setProperty(this.parseActivationProperty("property", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "file", (String)null, parsed)) {
               activation.setFile(this.parseActivationFile("file", parser, strict));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return activation;
      }
   }

   private ActivationFile parseActivationFile(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      ActivationFile activationFile = new ActivationFile();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "missing", (String)null, parsed)) {
               activationFile.setMissing(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "exists", (String)null, parsed)) {
               activationFile.setExists(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return activationFile;
      }
   }

   private ActivationOS parseActivationOS(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      ActivationOS activationOS = new ActivationOS();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               activationOS.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "family", (String)null, parsed)) {
               activationOS.setFamily(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "arch", (String)null, parsed)) {
               activationOS.setArch(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "version", (String)null, parsed)) {
               activationOS.setVersion(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return activationOS;
      }
   }

   private ActivationProperty parseActivationProperty(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      ActivationProperty activationProperty = new ActivationProperty();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               activationProperty.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "value", (String)null, parsed)) {
               activationProperty.setValue(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return activationProperty;
      }
   }

   private Build parseBuild(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Build build = new Build();
      HashSet parsed = new HashSet();

      while(true) {
         label179:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "sourceDirectory", (String)null, parsed)) {
               build.setSourceDirectory(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "scriptSourceDirectory", (String)null, parsed)) {
               build.setScriptSourceDirectory(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "testSourceDirectory", (String)null, parsed)) {
               build.setTestSourceDirectory(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "outputDirectory", (String)null, parsed)) {
               build.setOutputDirectory(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "testOutputDirectory", (String)null, parsed)) {
               build.setTestOutputDirectory(this.getTrimmedValue(parser.nextText()));
            } else {
               ArrayList plugins;
               if (this.checkFieldWithDuplicate(parser, "extensions", (String)null, parsed)) {
                  plugins = new ArrayList();
                  build.setExtensions(plugins);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label179;
                        }

                        if (parser.getName().equals("extension")) {
                           plugins.add(this.parseExtension("extension", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (this.checkFieldWithDuplicate(parser, "defaultGoal", (String)null, parsed)) {
                  build.setDefaultGoal(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "resources", (String)null, parsed)) {
                  plugins = new ArrayList();
                  build.setResources(plugins);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label179;
                        }

                        if (parser.getName().equals("resource")) {
                           plugins.add(this.parseResource("resource", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (this.checkFieldWithDuplicate(parser, "testResources", (String)null, parsed)) {
                  plugins = new ArrayList();
                  build.setTestResources(plugins);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label179;
                        }

                        if (parser.getName().equals("testResource")) {
                           plugins.add(this.parseResource("testResource", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (this.checkFieldWithDuplicate(parser, "directory", (String)null, parsed)) {
                  build.setDirectory(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "finalName", (String)null, parsed)) {
                  build.setFinalName(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "filters", (String)null, parsed)) {
                  plugins = new ArrayList();
                  build.setFilters(plugins);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label179;
                        }

                        if (parser.getName().equals("filter")) {
                           plugins.add(this.getTrimmedValue(parser.nextText()));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (this.checkFieldWithDuplicate(parser, "pluginManagement", (String)null, parsed)) {
                  build.setPluginManagement(this.parsePluginManagement("pluginManagement", parser, strict));
               } else if (!this.checkFieldWithDuplicate(parser, "plugins", (String)null, parsed)) {
                  if (strict) {
                     throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                  }

                  while(parser.next() != 3) {
                  }
               } else {
                  plugins = new ArrayList();
                  build.setPlugins(plugins);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label179;
                        }

                        if (parser.getName().equals("plugin")) {
                           plugins.add(this.parsePlugin("plugin", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               }
            }
         }

         return build;
      }
   }

   private BuildBase parseBuildBase(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      BuildBase buildBase = new BuildBase();
      HashSet parsed = new HashSet();

      while(true) {
         label134:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "defaultGoal", (String)null, parsed)) {
               buildBase.setDefaultGoal(this.getTrimmedValue(parser.nextText()));
            } else {
               ArrayList plugins;
               if (this.checkFieldWithDuplicate(parser, "resources", (String)null, parsed)) {
                  plugins = new ArrayList();
                  buildBase.setResources(plugins);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label134;
                        }

                        if (parser.getName().equals("resource")) {
                           plugins.add(this.parseResource("resource", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (this.checkFieldWithDuplicate(parser, "testResources", (String)null, parsed)) {
                  plugins = new ArrayList();
                  buildBase.setTestResources(plugins);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label134;
                        }

                        if (parser.getName().equals("testResource")) {
                           plugins.add(this.parseResource("testResource", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (this.checkFieldWithDuplicate(parser, "directory", (String)null, parsed)) {
                  buildBase.setDirectory(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "finalName", (String)null, parsed)) {
                  buildBase.setFinalName(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "filters", (String)null, parsed)) {
                  plugins = new ArrayList();
                  buildBase.setFilters(plugins);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label134;
                        }

                        if (parser.getName().equals("filter")) {
                           plugins.add(this.getTrimmedValue(parser.nextText()));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (this.checkFieldWithDuplicate(parser, "pluginManagement", (String)null, parsed)) {
                  buildBase.setPluginManagement(this.parsePluginManagement("pluginManagement", parser, strict));
               } else if (!this.checkFieldWithDuplicate(parser, "plugins", (String)null, parsed)) {
                  if (strict) {
                     throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                  }

                  while(parser.next() != 3) {
                  }
               } else {
                  plugins = new ArrayList();
                  buildBase.setPlugins(plugins);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label134;
                        }

                        if (parser.getName().equals("plugin")) {
                           plugins.add(this.parsePlugin("plugin", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               }
            }
         }

         return buildBase;
      }
   }

   private CiManagement parseCiManagement(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      CiManagement ciManagement = new CiManagement();
      HashSet parsed = new HashSet();

      while(true) {
         label51:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "system", (String)null, parsed)) {
               ciManagement.setSystem(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               ciManagement.setUrl(this.getTrimmedValue(parser.nextText()));
            } else if (!this.checkFieldWithDuplicate(parser, "notifiers", (String)null, parsed)) {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            } else {
               List notifiers = new ArrayList();
               ciManagement.setNotifiers(notifiers);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label51;
                     }

                     if (parser.getName().equals("notifier")) {
                        notifiers.add(this.parseNotifier("notifier", parser, strict));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            }
         }

         return ciManagement;
      }
   }

   private ConfigurationContainer parseConfigurationContainer(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      ConfigurationContainer configurationContainer = new ConfigurationContainer();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "inherited", (String)null, parsed)) {
               configurationContainer.setInherited(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "configuration", (String)null, parsed)) {
               configurationContainer.setConfiguration(Xpp3DomBuilder.build(parser));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return configurationContainer;
      }
   }

   private Contributor parseContributor(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Contributor contributor = new Contributor();
      HashSet parsed = new HashSet();

      while(true) {
         label75:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               contributor.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "email", (String)null, parsed)) {
               contributor.setEmail(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               contributor.setUrl(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "organization", "organisation", parsed)) {
               contributor.setOrganization(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "organizationUrl", "organisationUrl", parsed)) {
               contributor.setOrganizationUrl(this.getTrimmedValue(parser.nextText()));
            } else if (!this.checkFieldWithDuplicate(parser, "roles", (String)null, parsed)) {
               if (this.checkFieldWithDuplicate(parser, "timezone", (String)null, parsed)) {
                  contributor.setTimezone(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "properties", (String)null, parsed)) {
                  while(parser.nextTag() == 2) {
                     String key = parser.getName();
                     String value = parser.nextText().trim();
                     contributor.addProperty(key, value);
                  }
               } else {
                  if (strict) {
                     throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                  }

                  while(parser.next() != 3) {
                  }
               }
            } else {
               List roles = new ArrayList();
               contributor.setRoles(roles);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label75;
                     }

                     if (parser.getName().equals("role")) {
                        roles.add(this.getTrimmedValue(parser.nextText()));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            }
         }

         return contributor;
      }
   }

   private Dependency parseDependency(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Dependency dependency = new Dependency();
      HashSet parsed = new HashSet();

      while(true) {
         label75:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "groupId", (String)null, parsed)) {
               dependency.setGroupId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "artifactId", (String)null, parsed)) {
               dependency.setArtifactId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "version", (String)null, parsed)) {
               dependency.setVersion(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "type", (String)null, parsed)) {
               dependency.setType(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "classifier", (String)null, parsed)) {
               dependency.setClassifier(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "scope", (String)null, parsed)) {
               dependency.setScope(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "systemPath", (String)null, parsed)) {
               dependency.setSystemPath(this.getTrimmedValue(parser.nextText()));
            } else if (!this.checkFieldWithDuplicate(parser, "exclusions", (String)null, parsed)) {
               if (this.checkFieldWithDuplicate(parser, "optional", (String)null, parsed)) {
                  dependency.setOptional(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "optional", parser, "false"));
               } else {
                  if (strict) {
                     throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                  }

                  while(parser.next() != 3) {
                  }
               }
            } else {
               List exclusions = new ArrayList();
               dependency.setExclusions(exclusions);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label75;
                     }

                     if (parser.getName().equals("exclusion")) {
                        exclusions.add(this.parseExclusion("exclusion", parser, strict));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            }
         }

         return dependency;
      }
   }

   private DependencyManagement parseDependencyManagement(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      DependencyManagement dependencyManagement = new DependencyManagement();
      HashSet parsed = new HashSet();

      while(true) {
         label31:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "dependencies", (String)null, parsed)) {
               List dependencies = new ArrayList();
               dependencyManagement.setDependencies(dependencies);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label31;
                     }

                     if (parser.getName().equals("dependency")) {
                        dependencies.add(this.parseDependency("dependency", parser, strict));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return dependencyManagement;
      }
   }

   private DeploymentRepository parseDeploymentRepository(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      DeploymentRepository deploymentRepository = new DeploymentRepository();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "uniqueVersion", (String)null, parsed)) {
               deploymentRepository.setUniqueVersion(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "uniqueVersion", parser, "true"));
            } else if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               deploymentRepository.setId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               deploymentRepository.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               deploymentRepository.setUrl(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "layout", (String)null, parsed)) {
               deploymentRepository.setLayout(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return deploymentRepository;
      }
   }

   private Developer parseDeveloper(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Developer developer = new Developer();
      HashSet parsed = new HashSet();

      while(true) {
         label79:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               developer.setId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               developer.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "email", (String)null, parsed)) {
               developer.setEmail(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               developer.setUrl(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "organization", "organisation", parsed)) {
               developer.setOrganization(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "organizationUrl", "organisationUrl", parsed)) {
               developer.setOrganizationUrl(this.getTrimmedValue(parser.nextText()));
            } else if (!this.checkFieldWithDuplicate(parser, "roles", (String)null, parsed)) {
               if (this.checkFieldWithDuplicate(parser, "timezone", (String)null, parsed)) {
                  developer.setTimezone(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "properties", (String)null, parsed)) {
                  while(parser.nextTag() == 2) {
                     String key = parser.getName();
                     String value = parser.nextText().trim();
                     developer.addProperty(key, value);
                  }
               } else {
                  if (strict) {
                     throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                  }

                  while(parser.next() != 3) {
                  }
               }
            } else {
               List roles = new ArrayList();
               developer.setRoles(roles);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label79;
                     }

                     if (parser.getName().equals("role")) {
                        roles.add(this.getTrimmedValue(parser.nextText()));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            }
         }

         return developer;
      }
   }

   private DistributionManagement parseDistributionManagement(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      DistributionManagement distributionManagement = new DistributionManagement();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "repository", (String)null, parsed)) {
               distributionManagement.setRepository(this.parseDeploymentRepository("repository", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "snapshotRepository", (String)null, parsed)) {
               distributionManagement.setSnapshotRepository(this.parseDeploymentRepository("snapshotRepository", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "site", (String)null, parsed)) {
               distributionManagement.setSite(this.parseSite("site", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "downloadUrl", (String)null, parsed)) {
               distributionManagement.setDownloadUrl(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "relocation", (String)null, parsed)) {
               distributionManagement.setRelocation(this.parseRelocation("relocation", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "status", (String)null, parsed)) {
               distributionManagement.setStatus(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return distributionManagement;
      }
   }

   private Exclusion parseExclusion(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Exclusion exclusion = new Exclusion();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "artifactId", (String)null, parsed)) {
               exclusion.setArtifactId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "groupId", (String)null, parsed)) {
               exclusion.setGroupId(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return exclusion;
      }
   }

   private Extension parseExtension(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Extension extension = new Extension();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "groupId", (String)null, parsed)) {
               extension.setGroupId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "artifactId", (String)null, parsed)) {
               extension.setArtifactId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "version", (String)null, parsed)) {
               extension.setVersion(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return extension;
      }
   }

   private FileSet parseFileSet(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      FileSet fileSet = new FileSet();
      HashSet parsed = new HashSet();

      while(true) {
         label72:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "directory", (String)null, parsed)) {
               fileSet.setDirectory(this.getTrimmedValue(parser.nextText()));
            } else {
               ArrayList excludes;
               if (this.checkFieldWithDuplicate(parser, "includes", (String)null, parsed)) {
                  excludes = new ArrayList();
                  fileSet.setIncludes(excludes);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label72;
                        }

                        if (parser.getName().equals("include")) {
                           excludes.add(this.getTrimmedValue(parser.nextText()));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (!this.checkFieldWithDuplicate(parser, "excludes", (String)null, parsed)) {
                  if (strict) {
                     throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                  }

                  while(parser.next() != 3) {
                  }
               } else {
                  excludes = new ArrayList();
                  fileSet.setExcludes(excludes);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label72;
                        }

                        if (parser.getName().equals("exclude")) {
                           excludes.add(this.getTrimmedValue(parser.nextText()));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               }
            }
         }

         return fileSet;
      }
   }

   private IssueManagement parseIssueManagement(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      IssueManagement issueManagement = new IssueManagement();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "system", (String)null, parsed)) {
               issueManagement.setSystem(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               issueManagement.setUrl(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return issueManagement;
      }
   }

   private License parseLicense(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      License license = new License();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               license.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               license.setUrl(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "distribution", (String)null, parsed)) {
               license.setDistribution(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "comments", (String)null, parsed)) {
               license.setComments(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return license;
      }
   }

   private MailingList parseMailingList(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      MailingList mailingList = new MailingList();
      HashSet parsed = new HashSet();

      while(true) {
         label63:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               mailingList.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "subscribe", (String)null, parsed)) {
               mailingList.setSubscribe(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "unsubscribe", (String)null, parsed)) {
               mailingList.setUnsubscribe(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "post", (String)null, parsed)) {
               mailingList.setPost(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "archive", (String)null, parsed)) {
               mailingList.setArchive(this.getTrimmedValue(parser.nextText()));
            } else if (!this.checkFieldWithDuplicate(parser, "otherArchives", (String)null, parsed)) {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            } else {
               List otherArchives = new ArrayList();
               mailingList.setOtherArchives(otherArchives);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label63;
                     }

                     if (parser.getName().equals("otherArchive")) {
                        otherArchives.add(this.getTrimmedValue(parser.nextText()));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            }
         }

         return mailingList;
      }
   }

   private Model parseModel(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Model model = new Model();
      Set parsed = new HashSet();
      int eventType = parser.getEventType();

      label341:
      for(boolean foundRoot = false; eventType != 1; eventType = parser.next()) {
         if (eventType == 2) {
            if (parser.getName().equals(tagName)) {
               foundRoot = true;
            } else {
               if (strict && !foundRoot) {
                  throw new XmlPullParserException("Expected root element '" + tagName + "' but found '" + parser.getName() + "'", parser, (Throwable)null);
               }

               if (this.checkFieldWithDuplicate(parser, "modelVersion", (String)null, parsed)) {
                  model.setModelVersion(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "parent", (String)null, parsed)) {
                  model.setParent(this.parseParent("parent", parser, strict));
               } else if (this.checkFieldWithDuplicate(parser, "groupId", (String)null, parsed)) {
                  model.setGroupId(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "artifactId", (String)null, parsed)) {
                  model.setArtifactId(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "version", (String)null, parsed)) {
                  model.setVersion(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "packaging", (String)null, parsed)) {
                  model.setPackaging(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
                  model.setName(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "description", (String)null, parsed)) {
                  model.setDescription(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
                  model.setUrl(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "inceptionYear", (String)null, parsed)) {
                  model.setInceptionYear(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "organization", "organisation", parsed)) {
                  model.setOrganization(this.parseOrganization("organization", parser, strict));
               } else {
                  ArrayList dependencies;
                  if (this.checkFieldWithDuplicate(parser, "licenses", (String)null, parsed)) {
                     dependencies = new ArrayList();
                     model.setLicenses(dependencies);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label341;
                           }

                           if (parser.getName().equals("license")) {
                              dependencies.add(this.parseLicense("license", parser, strict));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (this.checkFieldWithDuplicate(parser, "mailingLists", (String)null, parsed)) {
                     dependencies = new ArrayList();
                     model.setMailingLists(dependencies);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label341;
                           }

                           if (parser.getName().equals("mailingList")) {
                              dependencies.add(this.parseMailingList("mailingList", parser, strict));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (this.checkFieldWithDuplicate(parser, "developers", (String)null, parsed)) {
                     dependencies = new ArrayList();
                     model.setDevelopers(dependencies);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label341;
                           }

                           if (parser.getName().equals("developer")) {
                              dependencies.add(this.parseDeveloper("developer", parser, strict));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (this.checkFieldWithDuplicate(parser, "contributors", (String)null, parsed)) {
                     dependencies = new ArrayList();
                     model.setContributors(dependencies);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label341;
                           }

                           if (parser.getName().equals("contributor")) {
                              dependencies.add(this.parseContributor("contributor", parser, strict));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (this.checkFieldWithDuplicate(parser, "issueManagement", (String)null, parsed)) {
                     model.setIssueManagement(this.parseIssueManagement("issueManagement", parser, strict));
                  } else if (this.checkFieldWithDuplicate(parser, "scm", (String)null, parsed)) {
                     model.setScm(this.parseScm("scm", parser, strict));
                  } else if (this.checkFieldWithDuplicate(parser, "ciManagement", (String)null, parsed)) {
                     model.setCiManagement(this.parseCiManagement("ciManagement", parser, strict));
                  } else if (this.checkFieldWithDuplicate(parser, "prerequisites", (String)null, parsed)) {
                     model.setPrerequisites(this.parsePrerequisites("prerequisites", parser, strict));
                  } else if (this.checkFieldWithDuplicate(parser, "build", (String)null, parsed)) {
                     model.setBuild(this.parseBuild("build", parser, strict));
                  } else if (this.checkFieldWithDuplicate(parser, "profiles", (String)null, parsed)) {
                     dependencies = new ArrayList();
                     model.setProfiles(dependencies);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label341;
                           }

                           if (parser.getName().equals("profile")) {
                              dependencies.add(this.parseProfile("profile", parser, strict));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (this.checkFieldWithDuplicate(parser, "distributionManagement", (String)null, parsed)) {
                     model.setDistributionManagement(this.parseDistributionManagement("distributionManagement", parser, strict));
                  } else if (this.checkFieldWithDuplicate(parser, "modules", (String)null, parsed)) {
                     dependencies = new ArrayList();
                     model.setModules(dependencies);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label341;
                           }

                           if (parser.getName().equals("module")) {
                              dependencies.add(this.getTrimmedValue(parser.nextText()));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (this.checkFieldWithDuplicate(parser, "repositories", (String)null, parsed)) {
                     dependencies = new ArrayList();
                     model.setRepositories(dependencies);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label341;
                           }

                           if (parser.getName().equals("repository")) {
                              dependencies.add(this.parseRepository("repository", parser, strict));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (this.checkFieldWithDuplicate(parser, "pluginRepositories", (String)null, parsed)) {
                     dependencies = new ArrayList();
                     model.setPluginRepositories(dependencies);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label341;
                           }

                           if (parser.getName().equals("pluginRepository")) {
                              dependencies.add(this.parseRepository("pluginRepository", parser, strict));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  } else if (!this.checkFieldWithDuplicate(parser, "dependencies", (String)null, parsed)) {
                     if (this.checkFieldWithDuplicate(parser, "reports", (String)null, parsed)) {
                        model.setReports(Xpp3DomBuilder.build(parser));
                     } else if (this.checkFieldWithDuplicate(parser, "reporting", (String)null, parsed)) {
                        model.setReporting(this.parseReporting("reporting", parser, strict));
                     } else if (this.checkFieldWithDuplicate(parser, "dependencyManagement", (String)null, parsed)) {
                        model.setDependencyManagement(this.parseDependencyManagement("dependencyManagement", parser, strict));
                     } else if (this.checkFieldWithDuplicate(parser, "properties", (String)null, parsed)) {
                        while(parser.nextTag() == 2) {
                           String key = parser.getName();
                           String value = parser.nextText().trim();
                           model.addProperty(key, value);
                        }
                     } else if (strict) {
                        throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                     }
                  } else {
                     dependencies = new ArrayList();
                     model.setDependencies(dependencies);

                     while(true) {
                        while(true) {
                           if (parser.nextTag() != 2) {
                              continue label341;
                           }

                           if (parser.getName().equals("dependency")) {
                              dependencies.add(this.parseDependency("dependency", parser, strict));
                           } else {
                              if (strict) {
                                 throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                              }

                              while(parser.next() != 3) {
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return model;
   }

   private ModelBase parseModelBase(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      ModelBase modelBase = new ModelBase();
      HashSet parsed = new HashSet();

      while(true) {
         label142:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "distributionManagement", (String)null, parsed)) {
               modelBase.setDistributionManagement(this.parseDistributionManagement("distributionManagement", parser, strict));
            } else {
               ArrayList dependencies;
               if (this.checkFieldWithDuplicate(parser, "modules", (String)null, parsed)) {
                  dependencies = new ArrayList();
                  modelBase.setModules(dependencies);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label142;
                        }

                        if (parser.getName().equals("module")) {
                           dependencies.add(this.getTrimmedValue(parser.nextText()));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (this.checkFieldWithDuplicate(parser, "repositories", (String)null, parsed)) {
                  dependencies = new ArrayList();
                  modelBase.setRepositories(dependencies);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label142;
                        }

                        if (parser.getName().equals("repository")) {
                           dependencies.add(this.parseRepository("repository", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (this.checkFieldWithDuplicate(parser, "pluginRepositories", (String)null, parsed)) {
                  dependencies = new ArrayList();
                  modelBase.setPluginRepositories(dependencies);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label142;
                        }

                        if (parser.getName().equals("pluginRepository")) {
                           dependencies.add(this.parseRepository("pluginRepository", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (!this.checkFieldWithDuplicate(parser, "dependencies", (String)null, parsed)) {
                  if (this.checkFieldWithDuplicate(parser, "reports", (String)null, parsed)) {
                     modelBase.setReports(Xpp3DomBuilder.build(parser));
                  } else if (this.checkFieldWithDuplicate(parser, "reporting", (String)null, parsed)) {
                     modelBase.setReporting(this.parseReporting("reporting", parser, strict));
                  } else if (this.checkFieldWithDuplicate(parser, "dependencyManagement", (String)null, parsed)) {
                     modelBase.setDependencyManagement(this.parseDependencyManagement("dependencyManagement", parser, strict));
                  } else if (this.checkFieldWithDuplicate(parser, "properties", (String)null, parsed)) {
                     while(parser.nextTag() == 2) {
                        String key = parser.getName();
                        String value = parser.nextText().trim();
                        modelBase.addProperty(key, value);
                     }
                  } else {
                     if (strict) {
                        throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                     }

                     while(parser.next() != 3) {
                     }
                  }
               } else {
                  dependencies = new ArrayList();
                  modelBase.setDependencies(dependencies);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label142;
                        }

                        if (parser.getName().equals("dependency")) {
                           dependencies.add(this.parseDependency("dependency", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               }
            }
         }

         return modelBase;
      }
   }

   private Notifier parseNotifier(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Notifier notifier = new Notifier();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "type", (String)null, parsed)) {
               notifier.setType(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "sendOnError", (String)null, parsed)) {
               notifier.setSendOnError(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "sendOnError", parser, "true"));
            } else if (this.checkFieldWithDuplicate(parser, "sendOnFailure", (String)null, parsed)) {
               notifier.setSendOnFailure(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "sendOnFailure", parser, "true"));
            } else if (this.checkFieldWithDuplicate(parser, "sendOnSuccess", (String)null, parsed)) {
               notifier.setSendOnSuccess(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "sendOnSuccess", parser, "true"));
            } else if (this.checkFieldWithDuplicate(parser, "sendOnWarning", (String)null, parsed)) {
               notifier.setSendOnWarning(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "sendOnWarning", parser, "true"));
            } else if (this.checkFieldWithDuplicate(parser, "address", (String)null, parsed)) {
               notifier.setAddress(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "configuration", (String)null, parsed)) {
               while(parser.nextTag() == 2) {
                  String key = parser.getName();
                  String value = parser.nextText().trim();
                  notifier.addConfiguration(key, value);
               }
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return notifier;
      }
   }

   private Organization parseOrganization(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Organization organization = new Organization();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               organization.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               organization.setUrl(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return organization;
      }
   }

   private Parent parseParent(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Parent parent = new Parent();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "artifactId", (String)null, parsed)) {
               parent.setArtifactId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "groupId", (String)null, parsed)) {
               parent.setGroupId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "version", (String)null, parsed)) {
               parent.setVersion(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "relativePath", (String)null, parsed)) {
               parent.setRelativePath(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return parent;
      }
   }

   private PatternSet parsePatternSet(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      PatternSet patternSet = new PatternSet();
      HashSet parsed = new HashSet();

      while(true) {
         label56:
         while(parser.nextTag() == 2) {
            ArrayList excludes;
            if (this.checkFieldWithDuplicate(parser, "includes", (String)null, parsed)) {
               excludes = new ArrayList();
               patternSet.setIncludes(excludes);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label56;
                     }

                     if (parser.getName().equals("include")) {
                        excludes.add(this.getTrimmedValue(parser.nextText()));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            } else if (!this.checkFieldWithDuplicate(parser, "excludes", (String)null, parsed)) {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            } else {
               excludes = new ArrayList();
               patternSet.setExcludes(excludes);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label56;
                     }

                     if (parser.getName().equals("exclude")) {
                        excludes.add(this.getTrimmedValue(parser.nextText()));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            }
         }

         return patternSet;
      }
   }

   private Plugin parsePlugin(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Plugin plugin = new Plugin();
      HashSet parsed = new HashSet();

      while(true) {
         label96:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "groupId", (String)null, parsed)) {
               plugin.setGroupId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "artifactId", (String)null, parsed)) {
               plugin.setArtifactId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "version", (String)null, parsed)) {
               plugin.setVersion(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "extensions", (String)null, parsed)) {
               plugin.setExtensions(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "extensions", parser, "false"));
            } else {
               ArrayList dependencies;
               if (this.checkFieldWithDuplicate(parser, "executions", (String)null, parsed)) {
                  dependencies = new ArrayList();
                  plugin.setExecutions(dependencies);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label96;
                        }

                        if (parser.getName().equals("execution")) {
                           dependencies.add(this.parsePluginExecution("execution", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (!this.checkFieldWithDuplicate(parser, "dependencies", (String)null, parsed)) {
                  if (this.checkFieldWithDuplicate(parser, "goals", (String)null, parsed)) {
                     plugin.setGoals(Xpp3DomBuilder.build(parser));
                  } else if (this.checkFieldWithDuplicate(parser, "inherited", (String)null, parsed)) {
                     plugin.setInherited(this.getTrimmedValue(parser.nextText()));
                  } else if (this.checkFieldWithDuplicate(parser, "configuration", (String)null, parsed)) {
                     plugin.setConfiguration(Xpp3DomBuilder.build(parser));
                  } else {
                     if (strict) {
                        throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                     }

                     while(parser.next() != 3) {
                     }
                  }
               } else {
                  dependencies = new ArrayList();
                  plugin.setDependencies(dependencies);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label96;
                        }

                        if (parser.getName().equals("dependency")) {
                           dependencies.add(this.parseDependency("dependency", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               }
            }
         }

         return plugin;
      }
   }

   private PluginConfiguration parsePluginConfiguration(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      PluginConfiguration pluginConfiguration = new PluginConfiguration();
      HashSet parsed = new HashSet();

      while(true) {
         label47:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "pluginManagement", (String)null, parsed)) {
               pluginConfiguration.setPluginManagement(this.parsePluginManagement("pluginManagement", parser, strict));
            } else if (!this.checkFieldWithDuplicate(parser, "plugins", (String)null, parsed)) {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            } else {
               List plugins = new ArrayList();
               pluginConfiguration.setPlugins(plugins);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label47;
                     }

                     if (parser.getName().equals("plugin")) {
                        plugins.add(this.parsePlugin("plugin", parser, strict));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            }
         }

         return pluginConfiguration;
      }
   }

   private PluginContainer parsePluginContainer(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      PluginContainer pluginContainer = new PluginContainer();
      HashSet parsed = new HashSet();

      while(true) {
         label31:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "plugins", (String)null, parsed)) {
               List plugins = new ArrayList();
               pluginContainer.setPlugins(plugins);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label31;
                     }

                     if (parser.getName().equals("plugin")) {
                        plugins.add(this.parsePlugin("plugin", parser, strict));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return pluginContainer;
      }
   }

   private PluginExecution parsePluginExecution(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      PluginExecution pluginExecution = new PluginExecution();
      HashSet parsed = new HashSet();

      while(true) {
         label59:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               pluginExecution.setId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "phase", (String)null, parsed)) {
               pluginExecution.setPhase(this.getTrimmedValue(parser.nextText()));
            } else if (!this.checkFieldWithDuplicate(parser, "goals", (String)null, parsed)) {
               if (this.checkFieldWithDuplicate(parser, "inherited", (String)null, parsed)) {
                  pluginExecution.setInherited(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "configuration", (String)null, parsed)) {
                  pluginExecution.setConfiguration(Xpp3DomBuilder.build(parser));
               } else {
                  if (strict) {
                     throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                  }

                  while(parser.next() != 3) {
                  }
               }
            } else {
               List goals = new ArrayList();
               pluginExecution.setGoals(goals);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label59;
                     }

                     if (parser.getName().equals("goal")) {
                        goals.add(this.getTrimmedValue(parser.nextText()));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            }
         }

         return pluginExecution;
      }
   }

   private PluginManagement parsePluginManagement(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      PluginManagement pluginManagement = new PluginManagement();
      HashSet parsed = new HashSet();

      while(true) {
         label31:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "plugins", (String)null, parsed)) {
               List plugins = new ArrayList();
               pluginManagement.setPlugins(plugins);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label31;
                     }

                     if (parser.getName().equals("plugin")) {
                        plugins.add(this.parsePlugin("plugin", parser, strict));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return pluginManagement;
      }
   }

   private Prerequisites parsePrerequisites(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Prerequisites prerequisites = new Prerequisites();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "maven", (String)null, parsed)) {
               prerequisites.setMaven(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return prerequisites;
      }
   }

   private Profile parseProfile(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Profile profile = new Profile();
      HashSet parsed = new HashSet();

      while(true) {
         label154:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               profile.setId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "activation", (String)null, parsed)) {
               profile.setActivation(this.parseActivation("activation", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "build", (String)null, parsed)) {
               profile.setBuild(this.parseBuildBase("build", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "distributionManagement", (String)null, parsed)) {
               profile.setDistributionManagement(this.parseDistributionManagement("distributionManagement", parser, strict));
            } else {
               ArrayList dependencies;
               if (this.checkFieldWithDuplicate(parser, "modules", (String)null, parsed)) {
                  dependencies = new ArrayList();
                  profile.setModules(dependencies);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label154;
                        }

                        if (parser.getName().equals("module")) {
                           dependencies.add(this.getTrimmedValue(parser.nextText()));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (this.checkFieldWithDuplicate(parser, "repositories", (String)null, parsed)) {
                  dependencies = new ArrayList();
                  profile.setRepositories(dependencies);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label154;
                        }

                        if (parser.getName().equals("repository")) {
                           dependencies.add(this.parseRepository("repository", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (this.checkFieldWithDuplicate(parser, "pluginRepositories", (String)null, parsed)) {
                  dependencies = new ArrayList();
                  profile.setPluginRepositories(dependencies);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label154;
                        }

                        if (parser.getName().equals("pluginRepository")) {
                           dependencies.add(this.parseRepository("pluginRepository", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (!this.checkFieldWithDuplicate(parser, "dependencies", (String)null, parsed)) {
                  if (this.checkFieldWithDuplicate(parser, "reports", (String)null, parsed)) {
                     profile.setReports(Xpp3DomBuilder.build(parser));
                  } else if (this.checkFieldWithDuplicate(parser, "reporting", (String)null, parsed)) {
                     profile.setReporting(this.parseReporting("reporting", parser, strict));
                  } else if (this.checkFieldWithDuplicate(parser, "dependencyManagement", (String)null, parsed)) {
                     profile.setDependencyManagement(this.parseDependencyManagement("dependencyManagement", parser, strict));
                  } else if (this.checkFieldWithDuplicate(parser, "properties", (String)null, parsed)) {
                     while(parser.nextTag() == 2) {
                        String key = parser.getName();
                        String value = parser.nextText().trim();
                        profile.addProperty(key, value);
                     }
                  } else {
                     if (strict) {
                        throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                     }

                     while(parser.next() != 3) {
                     }
                  }
               } else {
                  dependencies = new ArrayList();
                  profile.setDependencies(dependencies);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label154;
                        }

                        if (parser.getName().equals("dependency")) {
                           dependencies.add(this.parseDependency("dependency", parser, strict));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               }
            }
         }

         return profile;
      }
   }

   private Relocation parseRelocation(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Relocation relocation = new Relocation();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "groupId", (String)null, parsed)) {
               relocation.setGroupId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "artifactId", (String)null, parsed)) {
               relocation.setArtifactId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "version", (String)null, parsed)) {
               relocation.setVersion(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "message", (String)null, parsed)) {
               relocation.setMessage(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return relocation;
      }
   }

   private ReportPlugin parseReportPlugin(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      ReportPlugin reportPlugin = new ReportPlugin();
      HashSet parsed = new HashSet();

      while(true) {
         label63:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "groupId", (String)null, parsed)) {
               reportPlugin.setGroupId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "artifactId", (String)null, parsed)) {
               reportPlugin.setArtifactId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "version", (String)null, parsed)) {
               reportPlugin.setVersion(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "inherited", (String)null, parsed)) {
               reportPlugin.setInherited(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "configuration", (String)null, parsed)) {
               reportPlugin.setConfiguration(Xpp3DomBuilder.build(parser));
            } else if (!this.checkFieldWithDuplicate(parser, "reportSets", (String)null, parsed)) {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            } else {
               List reportSets = new ArrayList();
               reportPlugin.setReportSets(reportSets);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label63;
                     }

                     if (parser.getName().equals("reportSet")) {
                        reportSets.add(this.parseReportSet("reportSet", parser, strict));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            }
         }

         return reportPlugin;
      }
   }

   private ReportSet parseReportSet(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      ReportSet reportSet = new ReportSet();
      HashSet parsed = new HashSet();

      while(true) {
         label55:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               reportSet.setId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "configuration", (String)null, parsed)) {
               reportSet.setConfiguration(Xpp3DomBuilder.build(parser));
            } else if (this.checkFieldWithDuplicate(parser, "inherited", (String)null, parsed)) {
               reportSet.setInherited(this.getTrimmedValue(parser.nextText()));
            } else if (!this.checkFieldWithDuplicate(parser, "reports", (String)null, parsed)) {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            } else {
               List reports = new ArrayList();
               reportSet.setReports(reports);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label55;
                     }

                     if (parser.getName().equals("report")) {
                        reports.add(this.getTrimmedValue(parser.nextText()));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            }
         }

         return reportSet;
      }
   }

   private Reporting parseReporting(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Reporting reporting = new Reporting();
      HashSet parsed = new HashSet();

      while(true) {
         label51:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "excludeDefaults", (String)null, parsed)) {
               reporting.setExcludeDefaultsValue(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "outputDirectory", (String)null, parsed)) {
               reporting.setOutputDirectory(this.getTrimmedValue(parser.nextText()));
            } else if (!this.checkFieldWithDuplicate(parser, "plugins", (String)null, parsed)) {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            } else {
               List plugins = new ArrayList();
               reporting.setPlugins(plugins);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label51;
                     }

                     if (parser.getName().equals("plugin")) {
                        plugins.add(this.parseReportPlugin("plugin", parser, strict));
                     } else {
                        if (strict) {
                           throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                        }

                        while(parser.next() != 3) {
                        }
                     }
                  }
               }
            }
         }

         return reporting;
      }
   }

   private Repository parseRepository(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Repository repository = new Repository();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "releases", (String)null, parsed)) {
               repository.setReleases(this.parseRepositoryPolicy("releases", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "snapshots", (String)null, parsed)) {
               repository.setSnapshots(this.parseRepositoryPolicy("snapshots", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               repository.setId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               repository.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               repository.setUrl(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "layout", (String)null, parsed)) {
               repository.setLayout(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return repository;
      }
   }

   private RepositoryBase parseRepositoryBase(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      RepositoryBase repositoryBase = new RepositoryBase();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               repositoryBase.setId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               repositoryBase.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               repositoryBase.setUrl(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "layout", (String)null, parsed)) {
               repositoryBase.setLayout(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return repositoryBase;
      }
   }

   private RepositoryPolicy parseRepositoryPolicy(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      RepositoryPolicy repositoryPolicy = new RepositoryPolicy();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "enabled", (String)null, parsed)) {
               repositoryPolicy.setEnabled(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "enabled", parser, "true"));
            } else if (this.checkFieldWithDuplicate(parser, "updatePolicy", (String)null, parsed)) {
               repositoryPolicy.setUpdatePolicy(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "checksumPolicy", (String)null, parsed)) {
               repositoryPolicy.setChecksumPolicy(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return repositoryPolicy;
      }
   }

   private Resource parseResource(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Resource resource = new Resource();
      HashSet parsed = new HashSet();

      while(true) {
         label84:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "targetPath", (String)null, parsed)) {
               resource.setTargetPath(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "filtering", (String)null, parsed)) {
               resource.setFiltering(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "filtering", parser, "false"));
            } else if (this.checkFieldWithDuplicate(parser, "mergeId", (String)null, parsed)) {
               resource.setMergeId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "directory", (String)null, parsed)) {
               resource.setDirectory(this.getTrimmedValue(parser.nextText()));
            } else {
               ArrayList excludes;
               if (this.checkFieldWithDuplicate(parser, "includes", (String)null, parsed)) {
                  excludes = new ArrayList();
                  resource.setIncludes(excludes);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label84;
                        }

                        if (parser.getName().equals("include")) {
                           excludes.add(this.getTrimmedValue(parser.nextText()));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               } else if (!this.checkFieldWithDuplicate(parser, "excludes", (String)null, parsed)) {
                  if (strict) {
                     throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                  }

                  while(parser.next() != 3) {
                  }
               } else {
                  excludes = new ArrayList();
                  resource.setExcludes(excludes);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label84;
                        }

                        if (parser.getName().equals("exclude")) {
                           excludes.add(this.getTrimmedValue(parser.nextText()));
                        } else {
                           if (strict) {
                              throw new XmlPullParserException("Unrecognised association: '" + parser.getName() + "'", parser, (Throwable)null);
                           }

                           while(parser.next() != 3) {
                           }
                        }
                     }
                  }
               }
            }
         }

         return resource;
      }
   }

   private Scm parseScm(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Scm scm = new Scm();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "connection", (String)null, parsed)) {
               scm.setConnection(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "developerConnection", (String)null, parsed)) {
               scm.setDeveloperConnection(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "tag", (String)null, parsed)) {
               scm.setTag(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               scm.setUrl(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return scm;
      }
   }

   private Site parseSite(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Site site = new Site();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "id", (String)null, parsed)) {
               site.setId(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               site.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "url", (String)null, parsed)) {
               site.setUrl(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return site;
      }
   }

   public Model read(Reader reader, boolean strict) throws IOException, XmlPullParserException {
      XmlPullParser parser = new MXParser();
      parser.setInput(reader);
      if (this.addDefaultEntities) {
         parser.defineEntityReplacementText("nbsp", " ");
         parser.defineEntityReplacementText("iexcl", "¡");
         parser.defineEntityReplacementText("cent", "¢");
         parser.defineEntityReplacementText("pound", "£");
         parser.defineEntityReplacementText("curren", "¤");
         parser.defineEntityReplacementText("yen", "¥");
         parser.defineEntityReplacementText("brvbar", "¦");
         parser.defineEntityReplacementText("sect", "§");
         parser.defineEntityReplacementText("uml", "¨");
         parser.defineEntityReplacementText("copy", "©");
         parser.defineEntityReplacementText("ordf", "ª");
         parser.defineEntityReplacementText("laquo", "«");
         parser.defineEntityReplacementText("not", "¬");
         parser.defineEntityReplacementText("shy", "\u00ad");
         parser.defineEntityReplacementText("reg", "®");
         parser.defineEntityReplacementText("macr", "¯");
         parser.defineEntityReplacementText("deg", "°");
         parser.defineEntityReplacementText("plusmn", "±");
         parser.defineEntityReplacementText("sup2", "²");
         parser.defineEntityReplacementText("sup3", "³");
         parser.defineEntityReplacementText("acute", "´");
         parser.defineEntityReplacementText("micro", "µ");
         parser.defineEntityReplacementText("para", "¶");
         parser.defineEntityReplacementText("middot", "·");
         parser.defineEntityReplacementText("cedil", "¸");
         parser.defineEntityReplacementText("sup1", "¹");
         parser.defineEntityReplacementText("ordm", "º");
         parser.defineEntityReplacementText("raquo", "»");
         parser.defineEntityReplacementText("frac14", "¼");
         parser.defineEntityReplacementText("frac12", "½");
         parser.defineEntityReplacementText("frac34", "¾");
         parser.defineEntityReplacementText("iquest", "¿");
         parser.defineEntityReplacementText("Agrave", "À");
         parser.defineEntityReplacementText("Aacute", "Á");
         parser.defineEntityReplacementText("Acirc", "Â");
         parser.defineEntityReplacementText("Atilde", "Ã");
         parser.defineEntityReplacementText("Auml", "Ä");
         parser.defineEntityReplacementText("Aring", "Å");
         parser.defineEntityReplacementText("AElig", "Æ");
         parser.defineEntityReplacementText("Ccedil", "Ç");
         parser.defineEntityReplacementText("Egrave", "È");
         parser.defineEntityReplacementText("Eacute", "É");
         parser.defineEntityReplacementText("Ecirc", "Ê");
         parser.defineEntityReplacementText("Euml", "Ë");
         parser.defineEntityReplacementText("Igrave", "Ì");
         parser.defineEntityReplacementText("Iacute", "Í");
         parser.defineEntityReplacementText("Icirc", "Î");
         parser.defineEntityReplacementText("Iuml", "Ï");
         parser.defineEntityReplacementText("ETH", "Ð");
         parser.defineEntityReplacementText("Ntilde", "Ñ");
         parser.defineEntityReplacementText("Ograve", "Ò");
         parser.defineEntityReplacementText("Oacute", "Ó");
         parser.defineEntityReplacementText("Ocirc", "Ô");
         parser.defineEntityReplacementText("Otilde", "Õ");
         parser.defineEntityReplacementText("Ouml", "Ö");
         parser.defineEntityReplacementText("times", "×");
         parser.defineEntityReplacementText("Oslash", "Ø");
         parser.defineEntityReplacementText("Ugrave", "Ù");
         parser.defineEntityReplacementText("Uacute", "Ú");
         parser.defineEntityReplacementText("Ucirc", "Û");
         parser.defineEntityReplacementText("Uuml", "Ü");
         parser.defineEntityReplacementText("Yacute", "Ý");
         parser.defineEntityReplacementText("THORN", "Þ");
         parser.defineEntityReplacementText("szlig", "ß");
         parser.defineEntityReplacementText("agrave", "à");
         parser.defineEntityReplacementText("aacute", "á");
         parser.defineEntityReplacementText("acirc", "â");
         parser.defineEntityReplacementText("atilde", "ã");
         parser.defineEntityReplacementText("auml", "ä");
         parser.defineEntityReplacementText("aring", "å");
         parser.defineEntityReplacementText("aelig", "æ");
         parser.defineEntityReplacementText("ccedil", "ç");
         parser.defineEntityReplacementText("egrave", "è");
         parser.defineEntityReplacementText("eacute", "é");
         parser.defineEntityReplacementText("ecirc", "ê");
         parser.defineEntityReplacementText("euml", "ë");
         parser.defineEntityReplacementText("igrave", "ì");
         parser.defineEntityReplacementText("iacute", "í");
         parser.defineEntityReplacementText("icirc", "î");
         parser.defineEntityReplacementText("iuml", "ï");
         parser.defineEntityReplacementText("eth", "ð");
         parser.defineEntityReplacementText("ntilde", "ñ");
         parser.defineEntityReplacementText("ograve", "ò");
         parser.defineEntityReplacementText("oacute", "ó");
         parser.defineEntityReplacementText("ocirc", "ô");
         parser.defineEntityReplacementText("otilde", "õ");
         parser.defineEntityReplacementText("ouml", "ö");
         parser.defineEntityReplacementText("divide", "÷");
         parser.defineEntityReplacementText("oslash", "ø");
         parser.defineEntityReplacementText("ugrave", "ù");
         parser.defineEntityReplacementText("uacute", "ú");
         parser.defineEntityReplacementText("ucirc", "û");
         parser.defineEntityReplacementText("uuml", "ü");
         parser.defineEntityReplacementText("yacute", "ý");
         parser.defineEntityReplacementText("thorn", "þ");
         parser.defineEntityReplacementText("yuml", "ÿ");
         parser.defineEntityReplacementText("OElig", "Œ");
         parser.defineEntityReplacementText("oelig", "œ");
         parser.defineEntityReplacementText("Scaron", "Š");
         parser.defineEntityReplacementText("scaron", "š");
         parser.defineEntityReplacementText("Yuml", "Ÿ");
         parser.defineEntityReplacementText("circ", "ˆ");
         parser.defineEntityReplacementText("tilde", "˜");
         parser.defineEntityReplacementText("ensp", " ");
         parser.defineEntityReplacementText("emsp", " ");
         parser.defineEntityReplacementText("thinsp", " ");
         parser.defineEntityReplacementText("zwnj", "\u200c");
         parser.defineEntityReplacementText("zwj", "\u200d");
         parser.defineEntityReplacementText("lrm", "\u200e");
         parser.defineEntityReplacementText("rlm", "\u200f");
         parser.defineEntityReplacementText("ndash", "–");
         parser.defineEntityReplacementText("mdash", "—");
         parser.defineEntityReplacementText("lsquo", "‘");
         parser.defineEntityReplacementText("rsquo", "’");
         parser.defineEntityReplacementText("sbquo", "‚");
         parser.defineEntityReplacementText("ldquo", "“");
         parser.defineEntityReplacementText("rdquo", "”");
         parser.defineEntityReplacementText("bdquo", "„");
         parser.defineEntityReplacementText("dagger", "†");
         parser.defineEntityReplacementText("Dagger", "‡");
         parser.defineEntityReplacementText("permil", "‰");
         parser.defineEntityReplacementText("lsaquo", "‹");
         parser.defineEntityReplacementText("rsaquo", "›");
         parser.defineEntityReplacementText("euro", "€");
         parser.defineEntityReplacementText("fnof", "ƒ");
         parser.defineEntityReplacementText("Alpha", "Α");
         parser.defineEntityReplacementText("Beta", "Β");
         parser.defineEntityReplacementText("Gamma", "Γ");
         parser.defineEntityReplacementText("Delta", "Δ");
         parser.defineEntityReplacementText("Epsilon", "Ε");
         parser.defineEntityReplacementText("Zeta", "Ζ");
         parser.defineEntityReplacementText("Eta", "Η");
         parser.defineEntityReplacementText("Theta", "Θ");
         parser.defineEntityReplacementText("Iota", "Ι");
         parser.defineEntityReplacementText("Kappa", "Κ");
         parser.defineEntityReplacementText("Lambda", "Λ");
         parser.defineEntityReplacementText("Mu", "Μ");
         parser.defineEntityReplacementText("Nu", "Ν");
         parser.defineEntityReplacementText("Xi", "Ξ");
         parser.defineEntityReplacementText("Omicron", "Ο");
         parser.defineEntityReplacementText("Pi", "Π");
         parser.defineEntityReplacementText("Rho", "Ρ");
         parser.defineEntityReplacementText("Sigma", "Σ");
         parser.defineEntityReplacementText("Tau", "Τ");
         parser.defineEntityReplacementText("Upsilon", "Υ");
         parser.defineEntityReplacementText("Phi", "Φ");
         parser.defineEntityReplacementText("Chi", "Χ");
         parser.defineEntityReplacementText("Psi", "Ψ");
         parser.defineEntityReplacementText("Omega", "Ω");
         parser.defineEntityReplacementText("alpha", "α");
         parser.defineEntityReplacementText("beta", "β");
         parser.defineEntityReplacementText("gamma", "γ");
         parser.defineEntityReplacementText("delta", "δ");
         parser.defineEntityReplacementText("epsilon", "ε");
         parser.defineEntityReplacementText("zeta", "ζ");
         parser.defineEntityReplacementText("eta", "η");
         parser.defineEntityReplacementText("theta", "θ");
         parser.defineEntityReplacementText("iota", "ι");
         parser.defineEntityReplacementText("kappa", "κ");
         parser.defineEntityReplacementText("lambda", "λ");
         parser.defineEntityReplacementText("mu", "μ");
         parser.defineEntityReplacementText("nu", "ν");
         parser.defineEntityReplacementText("xi", "ξ");
         parser.defineEntityReplacementText("omicron", "ο");
         parser.defineEntityReplacementText("pi", "π");
         parser.defineEntityReplacementText("rho", "ρ");
         parser.defineEntityReplacementText("sigmaf", "ς");
         parser.defineEntityReplacementText("sigma", "σ");
         parser.defineEntityReplacementText("tau", "τ");
         parser.defineEntityReplacementText("upsilon", "υ");
         parser.defineEntityReplacementText("phi", "φ");
         parser.defineEntityReplacementText("chi", "χ");
         parser.defineEntityReplacementText("psi", "ψ");
         parser.defineEntityReplacementText("omega", "ω");
         parser.defineEntityReplacementText("thetasym", "ϑ");
         parser.defineEntityReplacementText("upsih", "ϒ");
         parser.defineEntityReplacementText("piv", "ϖ");
         parser.defineEntityReplacementText("bull", "•");
         parser.defineEntityReplacementText("hellip", "…");
         parser.defineEntityReplacementText("prime", "′");
         parser.defineEntityReplacementText("Prime", "″");
         parser.defineEntityReplacementText("oline", "‾");
         parser.defineEntityReplacementText("frasl", "⁄");
         parser.defineEntityReplacementText("weierp", "℘");
         parser.defineEntityReplacementText("image", "ℑ");
         parser.defineEntityReplacementText("real", "ℜ");
         parser.defineEntityReplacementText("trade", "™");
         parser.defineEntityReplacementText("alefsym", "ℵ");
         parser.defineEntityReplacementText("larr", "←");
         parser.defineEntityReplacementText("uarr", "↑");
         parser.defineEntityReplacementText("rarr", "→");
         parser.defineEntityReplacementText("darr", "↓");
         parser.defineEntityReplacementText("harr", "↔");
         parser.defineEntityReplacementText("crarr", "↵");
         parser.defineEntityReplacementText("lArr", "⇐");
         parser.defineEntityReplacementText("uArr", "⇑");
         parser.defineEntityReplacementText("rArr", "⇒");
         parser.defineEntityReplacementText("dArr", "⇓");
         parser.defineEntityReplacementText("hArr", "⇔");
         parser.defineEntityReplacementText("forall", "∀");
         parser.defineEntityReplacementText("part", "∂");
         parser.defineEntityReplacementText("exist", "∃");
         parser.defineEntityReplacementText("empty", "∅");
         parser.defineEntityReplacementText("nabla", "∇");
         parser.defineEntityReplacementText("isin", "∈");
         parser.defineEntityReplacementText("notin", "∉");
         parser.defineEntityReplacementText("ni", "∋");
         parser.defineEntityReplacementText("prod", "∏");
         parser.defineEntityReplacementText("sum", "∑");
         parser.defineEntityReplacementText("minus", "−");
         parser.defineEntityReplacementText("lowast", "∗");
         parser.defineEntityReplacementText("radic", "√");
         parser.defineEntityReplacementText("prop", "∝");
         parser.defineEntityReplacementText("infin", "∞");
         parser.defineEntityReplacementText("ang", "∠");
         parser.defineEntityReplacementText("and", "∧");
         parser.defineEntityReplacementText("or", "∨");
         parser.defineEntityReplacementText("cap", "∩");
         parser.defineEntityReplacementText("cup", "∪");
         parser.defineEntityReplacementText("int", "∫");
         parser.defineEntityReplacementText("there4", "∴");
         parser.defineEntityReplacementText("sim", "∼");
         parser.defineEntityReplacementText("cong", "≅");
         parser.defineEntityReplacementText("asymp", "≈");
         parser.defineEntityReplacementText("ne", "≠");
         parser.defineEntityReplacementText("equiv", "≡");
         parser.defineEntityReplacementText("le", "≤");
         parser.defineEntityReplacementText("ge", "≥");
         parser.defineEntityReplacementText("sub", "⊂");
         parser.defineEntityReplacementText("sup", "⊃");
         parser.defineEntityReplacementText("nsub", "⊄");
         parser.defineEntityReplacementText("sube", "⊆");
         parser.defineEntityReplacementText("supe", "⊇");
         parser.defineEntityReplacementText("oplus", "⊕");
         parser.defineEntityReplacementText("otimes", "⊗");
         parser.defineEntityReplacementText("perp", "⊥");
         parser.defineEntityReplacementText("sdot", "⋅");
         parser.defineEntityReplacementText("lceil", "⌈");
         parser.defineEntityReplacementText("rceil", "⌉");
         parser.defineEntityReplacementText("lfloor", "⌊");
         parser.defineEntityReplacementText("rfloor", "⌋");
         parser.defineEntityReplacementText("lang", "〈");
         parser.defineEntityReplacementText("rang", "〉");
         parser.defineEntityReplacementText("loz", "◊");
         parser.defineEntityReplacementText("spades", "♠");
         parser.defineEntityReplacementText("clubs", "♣");
         parser.defineEntityReplacementText("hearts", "♥");
         parser.defineEntityReplacementText("diams", "♦");
      }

      parser.next();
      return this.parseModel("project", parser, strict);
   }

   public Model read(Reader reader) throws IOException, XmlPullParserException {
      return this.read(reader, true);
   }

   public Model read(InputStream in, boolean strict) throws IOException, XmlPullParserException {
      Reader reader = ReaderFactory.newXmlReader(in);
      return this.read((Reader)reader, strict);
   }

   public Model read(InputStream in) throws IOException, XmlPullParserException {
      Reader reader = ReaderFactory.newXmlReader(in);
      return this.read((Reader)reader);
   }

   public void setAddDefaultEntities(boolean addDefaultEntities) {
      this.addDefaultEntities = addDefaultEntities;
   }
}
