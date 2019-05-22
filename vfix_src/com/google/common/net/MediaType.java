package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Beta
@GwtCompatible
@Immutable
public final class MediaType {
   private static final String CHARSET_ATTRIBUTE = "charset";
   private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS;
   private static final CharMatcher TOKEN_MATCHER;
   private static final CharMatcher QUOTED_TEXT_MATCHER;
   private static final CharMatcher LINEAR_WHITE_SPACE;
   private static final String APPLICATION_TYPE = "application";
   private static final String AUDIO_TYPE = "audio";
   private static final String IMAGE_TYPE = "image";
   private static final String TEXT_TYPE = "text";
   private static final String VIDEO_TYPE = "video";
   private static final String WILDCARD = "*";
   private static final Map<MediaType, MediaType> KNOWN_TYPES;
   public static final MediaType ANY_TYPE;
   public static final MediaType ANY_TEXT_TYPE;
   public static final MediaType ANY_IMAGE_TYPE;
   public static final MediaType ANY_AUDIO_TYPE;
   public static final MediaType ANY_VIDEO_TYPE;
   public static final MediaType ANY_APPLICATION_TYPE;
   public static final MediaType CACHE_MANIFEST_UTF_8;
   public static final MediaType CSS_UTF_8;
   public static final MediaType CSV_UTF_8;
   public static final MediaType HTML_UTF_8;
   public static final MediaType I_CALENDAR_UTF_8;
   public static final MediaType PLAIN_TEXT_UTF_8;
   public static final MediaType TEXT_JAVASCRIPT_UTF_8;
   public static final MediaType TSV_UTF_8;
   public static final MediaType VCARD_UTF_8;
   public static final MediaType WML_UTF_8;
   public static final MediaType XML_UTF_8;
   public static final MediaType BMP;
   public static final MediaType CRW;
   public static final MediaType GIF;
   public static final MediaType ICO;
   public static final MediaType JPEG;
   public static final MediaType PNG;
   public static final MediaType PSD;
   public static final MediaType SVG_UTF_8;
   public static final MediaType TIFF;
   public static final MediaType WEBP;
   public static final MediaType MP4_AUDIO;
   public static final MediaType MPEG_AUDIO;
   public static final MediaType OGG_AUDIO;
   public static final MediaType WEBM_AUDIO;
   public static final MediaType MP4_VIDEO;
   public static final MediaType MPEG_VIDEO;
   public static final MediaType OGG_VIDEO;
   public static final MediaType QUICKTIME;
   public static final MediaType WEBM_VIDEO;
   public static final MediaType WMV;
   public static final MediaType APPLICATION_XML_UTF_8;
   public static final MediaType ATOM_UTF_8;
   public static final MediaType BZIP2;
   public static final MediaType EPUB;
   public static final MediaType FORM_DATA;
   public static final MediaType KEY_ARCHIVE;
   public static final MediaType APPLICATION_BINARY;
   public static final MediaType GZIP;
   public static final MediaType JAVASCRIPT_UTF_8;
   public static final MediaType JSON_UTF_8;
   public static final MediaType KML;
   public static final MediaType KMZ;
   public static final MediaType MBOX;
   public static final MediaType MICROSOFT_EXCEL;
   public static final MediaType MICROSOFT_POWERPOINT;
   public static final MediaType MICROSOFT_WORD;
   public static final MediaType OCTET_STREAM;
   public static final MediaType OGG_CONTAINER;
   public static final MediaType OOXML_DOCUMENT;
   public static final MediaType OOXML_PRESENTATION;
   public static final MediaType OOXML_SHEET;
   public static final MediaType OPENDOCUMENT_GRAPHICS;
   public static final MediaType OPENDOCUMENT_PRESENTATION;
   public static final MediaType OPENDOCUMENT_SPREADSHEET;
   public static final MediaType OPENDOCUMENT_TEXT;
   public static final MediaType PDF;
   public static final MediaType POSTSCRIPT;
   public static final MediaType PROTOBUF;
   public static final MediaType RDF_XML_UTF_8;
   public static final MediaType RTF_UTF_8;
   public static final MediaType SHOCKWAVE_FLASH;
   public static final MediaType SKETCHUP;
   public static final MediaType TAR;
   public static final MediaType XHTML_UTF_8;
   public static final MediaType XRD_UTF_8;
   public static final MediaType ZIP;
   private final String type;
   private final String subtype;
   private final ImmutableListMultimap<String, String> parameters;
   private static final Joiner.MapJoiner PARAMETER_JOINER;

   private static MediaType createConstant(String type, String subtype) {
      return addKnownType(new MediaType(type, subtype, ImmutableListMultimap.of()));
   }

   private static MediaType createConstantUtf8(String type, String subtype) {
      return addKnownType(new MediaType(type, subtype, UTF_8_CONSTANT_PARAMETERS));
   }

   private static MediaType addKnownType(MediaType mediaType) {
      KNOWN_TYPES.put(mediaType, mediaType);
      return mediaType;
   }

   private MediaType(String type, String subtype, ImmutableListMultimap<String, String> parameters) {
      this.type = type;
      this.subtype = subtype;
      this.parameters = parameters;
   }

   public String type() {
      return this.type;
   }

   public String subtype() {
      return this.subtype;
   }

   public ImmutableListMultimap<String, String> parameters() {
      return this.parameters;
   }

   private Map<String, ImmutableMultiset<String>> parametersAsMap() {
      return Maps.transformValues((Map)this.parameters.asMap(), new Function<Collection<String>, ImmutableMultiset<String>>() {
         public ImmutableMultiset<String> apply(Collection<String> input) {
            return ImmutableMultiset.copyOf((Iterable)input);
         }
      });
   }

   public Optional<Charset> charset() {
      ImmutableSet<String> charsetValues = ImmutableSet.copyOf((Collection)this.parameters.get("charset"));
      switch(charsetValues.size()) {
      case 0:
         return Optional.absent();
      case 1:
         return Optional.of(Charset.forName((String)Iterables.getOnlyElement(charsetValues)));
      default:
         throw new IllegalStateException("Multiple charset values defined: " + charsetValues);
      }
   }

   public MediaType withoutParameters() {
      return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
   }

   public MediaType withParameters(Multimap<String, String> parameters) {
      return create(this.type, this.subtype, parameters);
   }

   public MediaType withParameter(String attribute, String value) {
      Preconditions.checkNotNull(attribute);
      Preconditions.checkNotNull(value);
      String normalizedAttribute = normalizeToken(attribute);
      ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
      Iterator i$ = this.parameters.entries().iterator();

      while(i$.hasNext()) {
         Entry<String, String> entry = (Entry)i$.next();
         String key = (String)entry.getKey();
         if (!normalizedAttribute.equals(key)) {
            builder.put(key, entry.getValue());
         }
      }

      builder.put(normalizedAttribute, normalizeParameterValue(normalizedAttribute, value));
      MediaType mediaType = new MediaType(this.type, this.subtype, builder.build());
      return (MediaType)Objects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
   }

   public MediaType withCharset(Charset charset) {
      Preconditions.checkNotNull(charset);
      return this.withParameter("charset", charset.name());
   }

   public boolean hasWildcard() {
      return "*".equals(this.type) || "*".equals(this.subtype);
   }

   public boolean is(MediaType mediaTypeRange) {
      return (mediaTypeRange.type.equals("*") || mediaTypeRange.type.equals(this.type)) && (mediaTypeRange.subtype.equals("*") || mediaTypeRange.subtype.equals(this.subtype)) && this.parameters.entries().containsAll(mediaTypeRange.parameters.entries());
   }

   public static MediaType create(String type, String subtype) {
      return create(type, subtype, ImmutableListMultimap.of());
   }

   static MediaType createApplicationType(String subtype) {
      return create("application", subtype);
   }

   static MediaType createAudioType(String subtype) {
      return create("audio", subtype);
   }

   static MediaType createImageType(String subtype) {
      return create("image", subtype);
   }

   static MediaType createTextType(String subtype) {
      return create("text", subtype);
   }

   static MediaType createVideoType(String subtype) {
      return create("video", subtype);
   }

   private static MediaType create(String type, String subtype, Multimap<String, String> parameters) {
      Preconditions.checkNotNull(type);
      Preconditions.checkNotNull(subtype);
      Preconditions.checkNotNull(parameters);
      String normalizedType = normalizeToken(type);
      String normalizedSubtype = normalizeToken(subtype);
      Preconditions.checkArgument(!"*".equals(normalizedType) || "*".equals(normalizedSubtype), "A wildcard type cannot be used with a non-wildcard subtype");
      ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
      Iterator i$ = parameters.entries().iterator();

      while(i$.hasNext()) {
         Entry<String, String> entry = (Entry)i$.next();
         String attribute = normalizeToken((String)entry.getKey());
         builder.put(attribute, normalizeParameterValue(attribute, (String)entry.getValue()));
      }

      MediaType mediaType = new MediaType(normalizedType, normalizedSubtype, builder.build());
      return (MediaType)Objects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
   }

   private static String normalizeToken(String token) {
      Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(token));
      return Ascii.toLowerCase(token);
   }

   private static String normalizeParameterValue(String attribute, String value) {
      return "charset".equals(attribute) ? Ascii.toLowerCase(value) : value;
   }

   public static MediaType parse(String input) {
      Preconditions.checkNotNull(input);
      MediaType.Tokenizer tokenizer = new MediaType.Tokenizer(input);

      try {
         String type = tokenizer.consumeToken(TOKEN_MATCHER);
         tokenizer.consumeCharacter('/');
         String subtype = tokenizer.consumeToken(TOKEN_MATCHER);

         ImmutableListMultimap.Builder parameters;
         String attribute;
         String value;
         for(parameters = ImmutableListMultimap.builder(); tokenizer.hasMore(); parameters.put(attribute, value)) {
            tokenizer.consumeCharacter(';');
            tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
            attribute = tokenizer.consumeToken(TOKEN_MATCHER);
            tokenizer.consumeCharacter('=');
            if ('"' != tokenizer.previewChar()) {
               value = tokenizer.consumeToken(TOKEN_MATCHER);
            } else {
               tokenizer.consumeCharacter('"');
               StringBuilder valueBuilder = new StringBuilder();

               while('"' != tokenizer.previewChar()) {
                  if ('\\' == tokenizer.previewChar()) {
                     tokenizer.consumeCharacter('\\');
                     valueBuilder.append(tokenizer.consumeCharacter(CharMatcher.ASCII));
                  } else {
                     valueBuilder.append(tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
                  }
               }

               value = valueBuilder.toString();
               tokenizer.consumeCharacter('"');
            }
         }

         return create(type, subtype, parameters.build());
      } catch (IllegalStateException var8) {
         throw new IllegalArgumentException(var8);
      }
   }

   public boolean equals(@Nullable Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof MediaType)) {
         return false;
      } else {
         MediaType that = (MediaType)obj;
         return this.type.equals(that.type) && this.subtype.equals(that.subtype) && this.parametersAsMap().equals(that.parametersAsMap());
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.type, this.subtype, this.parametersAsMap());
   }

   public String toString() {
      StringBuilder builder = (new StringBuilder()).append(this.type).append('/').append(this.subtype);
      if (!this.parameters.isEmpty()) {
         builder.append("; ");
         Multimap<String, String> quotedParameters = Multimaps.transformValues((ListMultimap)this.parameters, new Function<String, String>() {
            public String apply(String value) {
               return MediaType.TOKEN_MATCHER.matchesAllOf(value) ? value : MediaType.escapeAndQuote(value);
            }
         });
         PARAMETER_JOINER.appendTo((StringBuilder)builder, (Iterable)quotedParameters.entries());
      }

      return builder.toString();
   }

   private static String escapeAndQuote(String value) {
      StringBuilder escaped = (new StringBuilder(value.length() + 16)).append('"');
      char[] arr$ = value.toCharArray();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         char ch = arr$[i$];
         if (ch == '\r' || ch == '\\' || ch == '"') {
            escaped.append('\\');
         }

         escaped.append(ch);
      }

      return escaped.append('"').toString();
   }

   static {
      UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of("charset", Ascii.toLowerCase(Charsets.UTF_8.name()));
      TOKEN_MATCHER = CharMatcher.ASCII.and(CharMatcher.JAVA_ISO_CONTROL.negate()).and(CharMatcher.isNot(' ')).and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
      QUOTED_TEXT_MATCHER = CharMatcher.ASCII.and(CharMatcher.noneOf("\"\\\r"));
      LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
      KNOWN_TYPES = Maps.newHashMap();
      ANY_TYPE = createConstant("*", "*");
      ANY_TEXT_TYPE = createConstant("text", "*");
      ANY_IMAGE_TYPE = createConstant("image", "*");
      ANY_AUDIO_TYPE = createConstant("audio", "*");
      ANY_VIDEO_TYPE = createConstant("video", "*");
      ANY_APPLICATION_TYPE = createConstant("application", "*");
      CACHE_MANIFEST_UTF_8 = createConstantUtf8("text", "cache-manifest");
      CSS_UTF_8 = createConstantUtf8("text", "css");
      CSV_UTF_8 = createConstantUtf8("text", "csv");
      HTML_UTF_8 = createConstantUtf8("text", "html");
      I_CALENDAR_UTF_8 = createConstantUtf8("text", "calendar");
      PLAIN_TEXT_UTF_8 = createConstantUtf8("text", "plain");
      TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8("text", "javascript");
      TSV_UTF_8 = createConstantUtf8("text", "tab-separated-values");
      VCARD_UTF_8 = createConstantUtf8("text", "vcard");
      WML_UTF_8 = createConstantUtf8("text", "vnd.wap.wml");
      XML_UTF_8 = createConstantUtf8("text", "xml");
      BMP = createConstant("image", "bmp");
      CRW = createConstant("image", "x-canon-crw");
      GIF = createConstant("image", "gif");
      ICO = createConstant("image", "vnd.microsoft.icon");
      JPEG = createConstant("image", "jpeg");
      PNG = createConstant("image", "png");
      PSD = createConstant("image", "vnd.adobe.photoshop");
      SVG_UTF_8 = createConstantUtf8("image", "svg+xml");
      TIFF = createConstant("image", "tiff");
      WEBP = createConstant("image", "webp");
      MP4_AUDIO = createConstant("audio", "mp4");
      MPEG_AUDIO = createConstant("audio", "mpeg");
      OGG_AUDIO = createConstant("audio", "ogg");
      WEBM_AUDIO = createConstant("audio", "webm");
      MP4_VIDEO = createConstant("video", "mp4");
      MPEG_VIDEO = createConstant("video", "mpeg");
      OGG_VIDEO = createConstant("video", "ogg");
      QUICKTIME = createConstant("video", "quicktime");
      WEBM_VIDEO = createConstant("video", "webm");
      WMV = createConstant("video", "x-ms-wmv");
      APPLICATION_XML_UTF_8 = createConstantUtf8("application", "xml");
      ATOM_UTF_8 = createConstantUtf8("application", "atom+xml");
      BZIP2 = createConstant("application", "x-bzip2");
      EPUB = createConstant("application", "epub+zip");
      FORM_DATA = createConstant("application", "x-www-form-urlencoded");
      KEY_ARCHIVE = createConstant("application", "pkcs12");
      APPLICATION_BINARY = createConstant("application", "binary");
      GZIP = createConstant("application", "x-gzip");
      JAVASCRIPT_UTF_8 = createConstantUtf8("application", "javascript");
      JSON_UTF_8 = createConstantUtf8("application", "json");
      KML = createConstant("application", "vnd.google-earth.kml+xml");
      KMZ = createConstant("application", "vnd.google-earth.kmz");
      MBOX = createConstant("application", "mbox");
      MICROSOFT_EXCEL = createConstant("application", "vnd.ms-excel");
      MICROSOFT_POWERPOINT = createConstant("application", "vnd.ms-powerpoint");
      MICROSOFT_WORD = createConstant("application", "msword");
      OCTET_STREAM = createConstant("application", "octet-stream");
      OGG_CONTAINER = createConstant("application", "ogg");
      OOXML_DOCUMENT = createConstant("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
      OOXML_PRESENTATION = createConstant("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
      OOXML_SHEET = createConstant("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      OPENDOCUMENT_GRAPHICS = createConstant("application", "vnd.oasis.opendocument.graphics");
      OPENDOCUMENT_PRESENTATION = createConstant("application", "vnd.oasis.opendocument.presentation");
      OPENDOCUMENT_SPREADSHEET = createConstant("application", "vnd.oasis.opendocument.spreadsheet");
      OPENDOCUMENT_TEXT = createConstant("application", "vnd.oasis.opendocument.text");
      PDF = createConstant("application", "pdf");
      POSTSCRIPT = createConstant("application", "postscript");
      PROTOBUF = createConstant("application", "protobuf");
      RDF_XML_UTF_8 = createConstantUtf8("application", "rdf+xml");
      RTF_UTF_8 = createConstantUtf8("application", "rtf");
      SHOCKWAVE_FLASH = createConstant("application", "x-shockwave-flash");
      SKETCHUP = createConstant("application", "vnd.sketchup.skp");
      TAR = createConstant("application", "x-tar");
      XHTML_UTF_8 = createConstantUtf8("application", "xhtml+xml");
      XRD_UTF_8 = createConstantUtf8("application", "xrd+xml");
      ZIP = createConstant("application", "zip");
      PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");
   }

   private static final class Tokenizer {
      final String input;
      int position = 0;

      Tokenizer(String input) {
         this.input = input;
      }

      String consumeTokenIfPresent(CharMatcher matcher) {
         Preconditions.checkState(this.hasMore());
         int startPosition = this.position;
         this.position = matcher.negate().indexIn(this.input, startPosition);
         return this.hasMore() ? this.input.substring(startPosition, this.position) : this.input.substring(startPosition);
      }

      String consumeToken(CharMatcher matcher) {
         int startPosition = this.position;
         String token = this.consumeTokenIfPresent(matcher);
         Preconditions.checkState(this.position != startPosition);
         return token;
      }

      char consumeCharacter(CharMatcher matcher) {
         Preconditions.checkState(this.hasMore());
         char c = this.previewChar();
         Preconditions.checkState(matcher.matches(c));
         ++this.position;
         return c;
      }

      char consumeCharacter(char c) {
         Preconditions.checkState(this.hasMore());
         Preconditions.checkState(this.previewChar() == c);
         ++this.position;
         return c;
      }

      char previewChar() {
         Preconditions.checkState(this.hasMore());
         return this.input.charAt(this.position);
      }

      boolean hasMore() {
         return this.position >= 0 && this.position < this.input.length();
      }
   }
}
