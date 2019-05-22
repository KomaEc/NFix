package org.apache.tools.tar;

public interface TarConstants {
   int NAMELEN = 100;
   int MODELEN = 8;
   int UIDLEN = 8;
   int GIDLEN = 8;
   int CHKSUMLEN = 8;
   int SIZELEN = 12;
   long MAXSIZE = 8589934591L;
   int MAGICLEN = 8;
   int MODTIMELEN = 12;
   int UNAMELEN = 32;
   int GNAMELEN = 32;
   int DEVLEN = 8;
   byte LF_OLDNORM = 0;
   byte LF_NORMAL = 48;
   byte LF_LINK = 49;
   byte LF_SYMLINK = 50;
   byte LF_CHR = 51;
   byte LF_BLK = 52;
   byte LF_DIR = 53;
   byte LF_FIFO = 54;
   byte LF_CONTIG = 55;
   String TMAGIC = "ustar";
   String GNU_TMAGIC = "ustar  ";
   String GNU_LONGLINK = "././@LongLink";
   byte LF_GNUTYPE_LONGNAME = 76;
}
