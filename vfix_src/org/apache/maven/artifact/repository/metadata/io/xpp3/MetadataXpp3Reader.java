package org.apache.maven.artifact.repository.metadata.io.xpp3;

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
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.Plugin;
import org.apache.maven.artifact.repository.metadata.Snapshot;
import org.apache.maven.artifact.repository.metadata.Versioning;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class MetadataXpp3Reader {
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

   private Metadata parseMetadata(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Metadata metadata = new Metadata();
      Set parsed = new HashSet();
      int eventType = parser.getEventType();

      label69:
      for(boolean foundRoot = false; eventType != 1; eventType = parser.next()) {
         if (eventType == 2) {
            if (parser.getName().equals(tagName)) {
               foundRoot = true;
            } else {
               if (strict && !foundRoot) {
                  throw new XmlPullParserException("Expected root element '" + tagName + "' but found '" + parser.getName() + "'", parser, (Throwable)null);
               }

               if (this.checkFieldWithDuplicate(parser, "groupId", (String)null, parsed)) {
                  metadata.setGroupId(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "artifactId", (String)null, parsed)) {
                  metadata.setArtifactId(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "version", (String)null, parsed)) {
                  metadata.setVersion(this.getTrimmedValue(parser.nextText()));
               } else if (this.checkFieldWithDuplicate(parser, "versioning", (String)null, parsed)) {
                  metadata.setVersioning(this.parseVersioning("versioning", parser, strict));
               } else if (!this.checkFieldWithDuplicate(parser, "plugins", (String)null, parsed)) {
                  if (strict) {
                     throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                  }
               } else {
                  List plugins = new ArrayList();
                  metadata.setPlugins(plugins);

                  while(true) {
                     while(true) {
                        if (parser.nextTag() != 2) {
                           continue label69;
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
      }

      return metadata;
   }

   private Plugin parsePlugin(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Plugin plugin = new Plugin();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "name", (String)null, parsed)) {
               plugin.setName(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "prefix", (String)null, parsed)) {
               plugin.setPrefix(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "artifactId", (String)null, parsed)) {
               plugin.setArtifactId(this.getTrimmedValue(parser.nextText()));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return plugin;
      }
   }

   private Snapshot parseSnapshot(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Snapshot snapshot = new Snapshot();
      HashSet parsed = new HashSet();

      while(true) {
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "timestamp", (String)null, parsed)) {
               snapshot.setTimestamp(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "buildNumber", (String)null, parsed)) {
               snapshot.setBuildNumber(this.getIntegerValue(this.getTrimmedValue(parser.nextText()), "buildNumber", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "localCopy", (String)null, parsed)) {
               snapshot.setLocalCopy(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "localCopy", parser, "false"));
            } else {
               if (strict) {
                  throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
               }

               while(parser.next() != 3) {
               }
            }
         }

         return snapshot;
      }
   }

   private Versioning parseVersioning(String tagName, XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      Versioning versioning = new Versioning();
      HashSet parsed = new HashSet();

      while(true) {
         label59:
         while(parser.nextTag() == 2) {
            if (this.checkFieldWithDuplicate(parser, "latest", (String)null, parsed)) {
               versioning.setLatest(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "release", (String)null, parsed)) {
               versioning.setRelease(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "snapshot", (String)null, parsed)) {
               versioning.setSnapshot(this.parseSnapshot("snapshot", parser, strict));
            } else if (!this.checkFieldWithDuplicate(parser, "versions", (String)null, parsed)) {
               if (this.checkFieldWithDuplicate(parser, "lastUpdated", (String)null, parsed)) {
                  versioning.setLastUpdated(this.getTrimmedValue(parser.nextText()));
               } else {
                  if (strict) {
                     throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
                  }

                  while(parser.next() != 3) {
                  }
               }
            } else {
               List versions = new ArrayList();
               versioning.setVersions(versions);

               while(true) {
                  while(true) {
                     if (parser.nextTag() != 2) {
                        continue label59;
                     }

                     if (parser.getName().equals("version")) {
                        versions.add(this.getTrimmedValue(parser.nextText()));
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

         return versioning;
      }
   }

   public Metadata read(Reader reader, boolean strict) throws IOException, XmlPullParserException {
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
      return this.parseMetadata("metadata", parser, strict);
   }

   public Metadata read(Reader reader) throws IOException, XmlPullParserException {
      return this.read(reader, true);
   }

   public Metadata read(InputStream in, boolean strict) throws IOException, XmlPullParserException {
      Reader reader = ReaderFactory.newXmlReader(in);
      return this.read((Reader)reader, strict);
   }

   public Metadata read(InputStream in) throws IOException, XmlPullParserException {
      Reader reader = ReaderFactory.newXmlReader(in);
      return this.read((Reader)reader);
   }

   public void setAddDefaultEntities(boolean addDefaultEntities) {
      this.addDefaultEntities = addDefaultEntities;
   }
}
