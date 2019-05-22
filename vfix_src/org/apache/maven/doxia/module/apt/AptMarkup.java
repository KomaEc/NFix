package org.apache.maven.doxia.module.apt;

import org.apache.maven.doxia.markup.TextMarkup;
import org.codehaus.plexus.util.StringUtils;

public interface AptMarkup extends TextMarkup {
   char BACKSLASH = '\\';
   char COLON = ':';
   char COMMENT = '~';
   char NUMBERING = '1';
   char NUMBERING_LOWER_ALPHA_CHAR = 'a';
   char NUMBERING_LOWER_ROMAN_CHAR = 'i';
   char NUMBERING_UPPER_ALPHA_CHAR = 'A';
   char NUMBERING_UPPER_ROMAN_CHAR = 'I';
   char PAGE_BREAK = '\f';
   char PERCENT = '%';
   char TAB = '\t';
   String ANCHOR_END_MARKUP = String.valueOf('}');
   String ANCHOR_START_MARKUP = String.valueOf('{');
   String BOLD_END_MARKUP = StringUtils.repeat(String.valueOf('>'), 2);
   String BOLD_START_MARKUP = StringUtils.repeat(String.valueOf('<'), 2);
   String BOXED_VERBATIM_START_MARKUP = '+' + StringUtils.repeat(String.valueOf('-'), 6) + '+';
   String HEADER_START_MARKUP = ' ' + StringUtils.repeat(String.valueOf('-'), 5);
   String HORIZONTAL_RULE_MARKUP = StringUtils.repeat(String.valueOf('='), 8);
   String ITALIC_END_MARKUP = String.valueOf('>');
   String ITALIC_START_MARKUP = String.valueOf('<');
   String LINK_END_MARKUP = StringUtils.repeat(String.valueOf('}'), 2);
   String LINK_START_1_MARKUP = StringUtils.repeat(String.valueOf('{'), 3);
   String LINK_START_2_MARKUP = String.valueOf('}');
   String LIST_END_MARKUP = '[' + String.valueOf(']');
   String LIST_START_MARKUP = String.valueOf('*');
   String MONOSPACED_END_MARKUP = StringUtils.repeat(String.valueOf('>'), 3);
   String MONOSPACED_START_MARKUP = StringUtils.repeat(String.valueOf('<'), 3);
   String NON_BOXED_VERBATIM_START_MARKUP = StringUtils.repeat(String.valueOf('-'), 6);
   String NON_BREAKING_SPACE_MARKUP = '\\' + String.valueOf(' ');
   String PAGE_BREAK_MARKUP = String.valueOf('\f');
   String SECTION_TITLE_START_MARKUP = String.valueOf('*');
   String TABLE_CELL_SEPARATOR_MARKUP = String.valueOf('|');
   String TABLE_COL_CENTERED_ALIGNED_MARKUP = StringUtils.repeat(String.valueOf('-'), 2) + String.valueOf('*');
   String TABLE_COL_LEFT_ALIGNED_MARKUP = StringUtils.repeat(String.valueOf('-'), 2) + String.valueOf('+');
   String TABLE_COL_RIGHT_ALIGNED_MARKUP = StringUtils.repeat(String.valueOf('-'), 2) + String.valueOf(':');
   String TABLE_ROW_SEPARATOR_MARKUP = String.valueOf('|');
   String TABLE_ROW_START_MARKUP = '*' + StringUtils.repeat(String.valueOf('-'), 2);
   String BOXED_VERBATIM_END_MARKUP = BOXED_VERBATIM_START_MARKUP;
   String NON_BOXED_VERBATIM_END_MARKUP = NON_BOXED_VERBATIM_START_MARKUP;
}
