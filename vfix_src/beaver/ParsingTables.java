package beaver;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

public final class ParsingTables {
   private final short[] actions;
   final short[] lookaheads;
   final int[] actn_offsets;
   private final int[] goto_offsets;
   private final short[] default_actions;
   final int[] rule_infos;
   final short error_symbol_id;
   final boolean compressed;
   final int n_term;
   static final int UNUSED_OFFSET = Integer.MIN_VALUE;

   public ParsingTables(Class impl_class) {
      this(getSpecAsResourceStream(impl_class));
   }

   public ParsingTables(String spec) {
      this((InputStream)(new ByteArrayInputStream(decode(spec))));
   }

   private ParsingTables(InputStream in) {
      try {
         DataInputStream data = new DataInputStream(new InflaterInputStream(in));

         try {
            int len = data.readInt();
            this.actions = new short[len];

            int min_nt_id;
            for(min_nt_id = 0; min_nt_id < len; ++min_nt_id) {
               this.actions[min_nt_id] = data.readShort();
            }

            this.lookaheads = new short[len];

            for(min_nt_id = 0; min_nt_id < len; ++min_nt_id) {
               this.lookaheads[min_nt_id] = data.readShort();
            }

            len = data.readInt();
            this.actn_offsets = new int[len];

            for(min_nt_id = 0; min_nt_id < len; ++min_nt_id) {
               this.actn_offsets[min_nt_id] = data.readInt();
            }

            this.goto_offsets = new int[len];

            for(min_nt_id = 0; min_nt_id < len; ++min_nt_id) {
               this.goto_offsets[min_nt_id] = data.readInt();
            }

            len = data.readInt();
            this.compressed = len != 0;
            if (this.compressed) {
               this.default_actions = new short[len];

               for(min_nt_id = 0; min_nt_id < len; ++min_nt_id) {
                  this.default_actions[min_nt_id] = data.readShort();
               }
            } else {
               this.default_actions = null;
            }

            min_nt_id = Integer.MAX_VALUE;
            len = data.readInt();
            this.rule_infos = new int[len];

            for(int i = 0; i < len; ++i) {
               this.rule_infos[i] = data.readInt();
               min_nt_id = Math.min(min_nt_id, this.rule_infos[i] >>> 16);
            }

            this.n_term = min_nt_id;
            this.error_symbol_id = data.readShort();
         } finally {
            data.close();
         }
      } catch (IOException var10) {
         throw new IllegalStateException("cannot initialize parser tables: " + var10.getMessage());
      }
   }

   final short findFirstTerminal(int state) {
      int offset = this.actn_offsets[state];

      for(short term_id = offset < 0 ? (short)(-offset) : 0; term_id < this.n_term; ++term_id) {
         int index = offset + term_id;
         if (index >= this.lookaheads.length) {
            break;
         }

         if (this.lookaheads[index] == term_id) {
            return term_id;
         }
      }

      return -1;
   }

   final short findParserAction(int state, short lookahead) {
      int index = this.actn_offsets[state];
      if (index != Integer.MIN_VALUE) {
         index += lookahead;
         if (0 <= index && index < this.actions.length && this.lookaheads[index] == lookahead) {
            return this.actions[index];
         }
      }

      return this.compressed ? this.default_actions[state] : 0;
   }

   final short findNextState(int state, short lookahead) {
      int index = this.goto_offsets[state];
      if (index != Integer.MIN_VALUE) {
         index += lookahead;
         if (0 <= index && index < this.actions.length && this.lookaheads[index] == lookahead) {
            return this.actions[index];
         }
      }

      return this.compressed ? this.default_actions[state] : 0;
   }

   static byte[] decode(String spec) {
      char[] chars = spec.toCharArray();
      if (chars.length % 4 != 0) {
         throw new IllegalArgumentException("corrupted encoding");
      } else {
         int len = chars.length / 4 * 3;
         byte[] bytes = new byte[chars[chars.length - 1] == '=' ? (chars[chars.length - 2] == '=' ? len - 2 : len - 1) : len];
         len -= 3;
         int ci = 0;

         int bi;
         int acc;
         for(bi = 0; bi < len; bytes[bi++] = (byte)(acc & 255)) {
            acc = decode(chars[ci++]) << 18 | decode(chars[ci++]) << 12 | decode(chars[ci++]) << 6 | decode(chars[ci++]);
            bytes[bi++] = (byte)(acc >> 16);
            bytes[bi++] = (byte)(acc >> 8 & 255);
         }

         acc = decode(chars[ci++]) << 18 | decode(chars[ci++]) << 12 | decode(chars[ci++]) << 6 | decode(chars[ci++]);
         bytes[bi++] = (byte)(acc >> 16);
         if (bi < bytes.length) {
            bytes[bi++] = (byte)(acc >> 8 & 255);
            if (bi < bytes.length) {
               bytes[bi++] = (byte)(acc & 255);
            }
         }

         return bytes;
      }
   }

   static int decode(char c) {
      if (c <= '9') {
         if (c >= '0') {
            return c - 48;
         }

         if (c == '#') {
            return 62;
         }

         if (c == '$') {
            return 63;
         }
      } else if (c <= 'Z') {
         if (c >= 'A') {
            return c - 65 + 10;
         }

         if (c == '=') {
            return 0;
         }
      } else if ('a' <= c && c <= 'z') {
         return c - 97 + 36;
      }

      throw new IllegalStateException("illegal encoding character '" + c + "'");
   }

   static InputStream getSpecAsResourceStream(Class impl_class) {
      String name = impl_class.getName();
      name = name.substring(name.lastIndexOf(46) + 1) + ".spec";
      InputStream spec_stream = impl_class.getResourceAsStream(name);
      if (spec_stream == null) {
         throw new IllegalStateException("parser specification not found");
      } else {
         return spec_stream;
      }
   }
}
