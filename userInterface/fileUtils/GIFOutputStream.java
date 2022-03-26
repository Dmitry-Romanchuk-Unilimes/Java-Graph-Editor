// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.fileUtils;

import java.awt.*;
import java.awt.image.PixelGrabber;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

public class GIFOutputStream extends FilterOutputStream
{
    public static final int ORIGINAL_COLOR = 0;
    public static final int BLACK_AND_WHITE = 1;
    public static final int GRAYSCALE_16 = 2;
    public static final int GRAYSCALE_256 = 3;
    public static final int STANDARD_16_COLORS = 4;
    public static final int STANDARD_256_COLORS = 5;
    public static final int DITHERED_216_COLORS = 6;
    public static final int NO_ERROR = 0;
    public static final int IMAGE_LOAD_FAILED = 1;
    public static final int TOO_MANY_COLORS = 2;
    public static final int INVALID_COLOR_MODE = 3;
    protected static final int BLACK_INDEX = 0;
    protected static final int WHITE_INDEX = 1;
    protected static final int[] standard16;
    protected static final int[] standard256;
    protected static int[][] ditherPattern;
    protected int errorStatus;
    protected int rl_pixel;
    protected int rl_basecode;
    protected int rl_count;
    protected int rl_table_pixel;
    protected int rl_table_max;
    protected boolean just_cleared;
    protected int out_bits;
    protected int out_bits_init;
    protected int out_count;
    protected int out_bump;
    protected int out_bump_init;
    protected int out_clear;
    protected int out_clear_init;
    protected int max_ocodes;
    protected int code_clear;
    protected int code_eof;
    protected int obuf;
    protected int obits;
    protected byte[] oblock;
    protected int oblen;
    protected static final int GIFBITS = 12;
    
    static {
        standard16 = new int[] { 0, 16711680, 65280, 255, 65535, 16711935, 16776960, 8388608, 32768, 128, 32896, 8388736, 8421376, 8421504, 12632256, 16777215 };
        standard256 = new int[256];
        GIFOutputStream.ditherPattern = new int[][] { { 8, 184, 248, 216 }, { 120, 56, 152, 88 }, { 40, 232, 24, 200 }, { 168, 104, 136, 72 } };
        GIFOutputStream.standard256[0] = 0;
        int n = 40;
        for (int r = 0; r < 6; ++r) {
            for (int g = 0; g < 6; ++g) {
                for (int b = 0; b < 6; ++b) {
                    GIFOutputStream.standard256[n++] = (3342336 * r | 13056 * g | 51 * b);
                }
            }
        }
        n = 1;
        for (int j = 0; j < 10; ++j) {
            GIFOutputStream.standard256[j + 1] = 1118481 * n;
            GIFOutputStream.standard256[j + 11] = 17 * n;
            GIFOutputStream.standard256[j + 21] = 4352 * n;
            GIFOutputStream.standard256[j + 31] = 1114112 * n;
            if (++n % 3 == 0) {
                ++n;
            }
        }
    }
    
    public static int writeGIF(final OutputStream out, final Image image) throws IOException {
        return writeGIF(out, image, 0, null);
    }
    
    public static int writeGIF(final OutputStream out, final Image image, final int colorMode) throws IOException {
        return writeGIF(out, image, colorMode, null);
    }
    
    public static int writeGIF(final OutputStream out, final Image image, final int colorMode, final Color transparentColor) throws IOException {
        final GIFOutputStream gifOut = new GIFOutputStream(out);
        gifOut.write(image, colorMode, transparentColor);
        return gifOut.getErrorStatus();
    }
    
    public GIFOutputStream(final OutputStream out) {
        super(out);
        this.errorStatus = 0;
        this.oblock = new byte[256];
    }
    
    public int getErrorStatus() {
        return this.errorStatus;
    }
    
    public void write(final Image image) throws IOException {
        this.write(image, 0, null);
    }
    
    public void write(final Image image, final int colorMode) throws IOException {
        this.write(image, colorMode, null);
    }
    
    public void write(final Image image, final Color transparentColor) throws IOException {
        this.write(image, 0, transparentColor);
    }
    
    public void write(final Image image, final int colorMode, final Color transparentColor) throws IOException {
        this.errorStatus = 0;
        if (image == null) {
            return;
        }
        final PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, true);
        try {
            pg.grabPixels();
        }
        catch (InterruptedException e) {
            this.errorStatus = 1;
            return;
        }
        if ((pg.status() & 0x80) != 0x0) {
            this.errorStatus = 1;
            return;
        }
        int[] pixels = (int[])pg.getPixels();
        final int width = pg.getWidth();
        final int height = pg.getHeight();
        int colorCount = 0;
        int[] colorTable = null;
        byte[] bytePixels = null;
        switch (colorMode) {
            case 0: {
                final Hashtable colorSet = this.getColorSet(pixels);
                colorCount = colorSet.size();
                if (colorCount > 256) {
                    this.errorStatus = 2;
                    return;
                }
                colorTable = this.createColorTable(colorSet, colorCount);
                bytePixels = this.createBytePixels(pixels, colorSet);
                break;
            }
            case 1: {
                colorCount = 2;
                colorTable = this.createBWTable();
                bytePixels = this.createBWBytePixels(pixels);
                break;
            }
            case 2: {
                colorCount = 16;
                colorTable = this.create16GrayTable();
                bytePixels = this.create16GrayBytePixels(pixels);
                break;
            }
            case 3: {
                colorCount = 256;
                colorTable = this.create256GrayTable();
                bytePixels = this.create256GrayBytePixels(pixels);
                break;
            }
            case 4: {
                colorCount = 16;
                colorTable = this.createStd16ColorTable();
                bytePixels = this.createStd16ColorBytePixels(pixels);
                break;
            }
            case 5: {
                colorCount = 256;
                colorTable = this.createStd256ColorTable();
                bytePixels = this.createStd256ColorBytePixels(pixels, width, false);
                break;
            }
            case 6: {
                colorCount = 216;
                colorTable = this.createStd216ColorTable();
                bytePixels = this.createStd256ColorBytePixels(pixels, width, true);
                break;
            }
            default: {
                this.errorStatus = 3;
                return;
            }
        }
        pixels = null;
        int cc1 = colorCount - 1;
        int bitsPerPixel = 0;
        while (cc1 != 0) {
            ++bitsPerPixel;
            cc1 >>= 1;
        }
        this.writeGIFHeader(width, height, bitsPerPixel);
        this.writeColorTable(colorTable, bitsPerPixel);
        if (transparentColor != null) {
            this.writeGraphicControlExtension(transparentColor, colorTable);
        }
        this.writeImageDescriptor(width, height);
        this.writeCompressedImageData(bytePixels, bitsPerPixel);
        this.write(0);
        this.write(59);
    }
    
    protected Hashtable getColorSet(final int[] pixels) {
        final Hashtable colorSet = new Hashtable();
        final boolean[] checked = new boolean[pixels.length];
        int needsChecking = pixels.length;
        int colorIndex = 0;
        for (int j = 0; j < pixels.length && needsChecking > 0; ++j) {
            if (!checked[j]) {
                final int color = pixels[j] & 0xFFFFFF;
                checked[j] = true;
                --needsChecking;
                final Integer key = new Integer(color);
                colorSet.put(key, new Integer(colorIndex));
                if (++colorIndex > 256) {
                    break;
                }
                for (int j2 = j + 1; j2 < pixels.length; ++j2) {
                    if ((pixels[j2] & 0xFFFFFF) == color) {
                        checked[j2] = true;
                        --needsChecking;
                    }
                }
            }
        }
        if (colorIndex == 1) {
            if (colorSet.get(new Integer(0)) == null) {
                colorSet.put(new Integer(0), new Integer(1));
            }
            else {
                colorSet.put(new Integer(16777215), new Integer(1));
            }
        }
        return colorSet;
    }
    
    protected int[] createColorTable(final Hashtable colorSet, final int colorCount) {
        final int[] colorTable = new int[colorCount];
        final Enumeration e = colorSet.keys();
        while (e.hasMoreElements()) {
            final Integer key = e.nextElement();
            colorTable[colorSet.get(key)] = key;
        }
        return colorTable;
    }
    
    protected byte[] createBytePixels(final int[] pixels, final Hashtable colorSet) {
        final byte[] bytePixels = new byte[pixels.length];
        for (int j = 0; j < pixels.length; ++j) {
            final Integer key = new Integer(pixels[j] & 0xFFFFFF);
            final int colorIndex = colorSet.get(key);
            bytePixels[j] = (byte)colorIndex;
        }
        return bytePixels;
    }
    
    protected int[] createBWTable() {
        final int[] colorTable = { 0, 16777215 };
        return colorTable;
    }
    
    protected byte[] createBWBytePixels(final int[] pixels) {
        final byte[] bytePixels = new byte[pixels.length];
        for (int j = 0; j < pixels.length; ++j) {
            if (this.grayscaleValue(pixels[j]) < 128) {
                bytePixels[j] = 0;
            }
            else {
                bytePixels[j] = 1;
            }
        }
        return bytePixels;
    }
    
    protected int[] create16GrayTable() {
        final int[] colorTable = new int[16];
        for (int j = 0; j < 16; ++j) {
            colorTable[j] = 1118481 * j;
        }
        return colorTable;
    }
    
    protected byte[] create16GrayBytePixels(final int[] pixels) {
        final byte[] bytePixels = new byte[pixels.length];
        for (int j = 0; j < pixels.length; ++j) {
            bytePixels[j] = (byte)(this.grayscaleValue(pixels[j]) / 16);
        }
        return bytePixels;
    }
    
    protected int[] create256GrayTable() {
        final int[] colorTable = new int[256];
        for (int j = 0; j < 256; ++j) {
            colorTable[j] = 65793 * j;
        }
        return colorTable;
    }
    
    protected byte[] create256GrayBytePixels(final int[] pixels) {
        final byte[] bytePixels = new byte[pixels.length];
        for (int j = 0; j < pixels.length; ++j) {
            bytePixels[j] = (byte)this.grayscaleValue(pixels[j]);
        }
        return bytePixels;
    }
    
    protected int[] createStd16ColorTable() {
        final int[] colorTable = new int[16];
        for (int j = 0; j < 16; ++j) {
            colorTable[j] = GIFOutputStream.standard16[j];
        }
        return colorTable;
    }
    
    protected byte[] createStd16ColorBytePixels(final int[] pixels) {
        final byte[] bytePixels = new byte[pixels.length];
        int minError = 0;
        for (int j = 0; j < pixels.length; ++j) {
            final int color = pixels[j] & 0xFFFFFF;
            int minIndex = -1;
            for (int k = 0; k < 16; ++k) {
                final int error = this.colorMatchError(color, GIFOutputStream.standard16[k]);
                if (error < minError || minIndex < 0) {
                    minError = error;
                    minIndex = k;
                }
            }
            bytePixels[j] = (byte)minIndex;
        }
        return bytePixels;
    }
    
    protected int[] createStd256ColorTable() {
        final int[] colorTable = new int[256];
        for (int j = 0; j < 256; ++j) {
            colorTable[j] = GIFOutputStream.standard256[j];
        }
        return colorTable;
    }
    
    protected int[] createStd216ColorTable() {
        final int[] colorTable = new int[216];
        colorTable[0] = 0;
        for (int j = 1; j < 216; ++j) {
            colorTable[j] = GIFOutputStream.standard256[j + 40];
        }
        return colorTable;
    }
    
    protected byte[] createStd256ColorBytePixels(final int[] pixels, final int width, final boolean dither) {
        final byte[] bytePixels = new byte[pixels.length];
        int minError = 0;
        for (int j = 0; j < pixels.length; ++j) {
            final int color = pixels[j] & 0xFFFFFF;
            int minIndex = -1;
            final int r = (color & 0xFF0000) >> 16;
            final int g = (color & 0xFF00) >> 8;
            final int b = color & 0xFF;
            int r2 = r / 51;
            int g2 = g / 51;
            int b2 = b / 51;
            if (dither) {
                final int x = j % width;
                final int y = j / width;
                final int threshold = GIFOutputStream.ditherPattern[x % 4][y % 4] / 5;
                if (r2 < 5 && r % 51 >= threshold) {
                    ++r2;
                }
                if (g2 < 5 && g % 51 >= threshold) {
                    ++g2;
                }
                if (b2 < 5 && b % 51 >= threshold) {
                    ++b2;
                }
                bytePixels[j] = (byte)(r2 * 36 + g2 * 6 + b2);
            }
            else {
                for (int r3 = r2; r3 <= r2 + 1 && r3 < 6; ++r3) {
                    for (int g3 = g2; g3 <= g2 + 1 && g3 < 6; ++g3) {
                        for (int b3 = b2; b3 <= b2 + 1 && b3 < 6; ++b3) {
                            int sampleIndex = 40 + r3 * 36 + g3 * 6 + b3;
                            if (sampleIndex == 40) {
                                sampleIndex = 0;
                            }
                            final int error = this.colorMatchError(color, GIFOutputStream.standard256[sampleIndex]);
                            if (error < minError || minIndex < 0) {
                                minError = error;
                                minIndex = sampleIndex;
                            }
                        }
                    }
                }
                int shadeBase;
                int shadeIndex;
                if (r > g && r > b) {
                    shadeBase = 30;
                    shadeIndex = (r + 8) / 17;
                }
                else if (g > r && g > b) {
                    shadeBase = 20;
                    shadeIndex = (g + 8) / 17;
                }
                else {
                    shadeBase = 10;
                    shadeIndex = (b + 8) / 17;
                }
                if (shadeIndex > 0) {
                    shadeIndex -= shadeIndex / 3;
                    final int sampleIndex = shadeBase + shadeIndex;
                    final int error = this.colorMatchError(color, GIFOutputStream.standard256[sampleIndex]);
                    if (error < minError || minIndex < 0) {
                        minError = error;
                        minIndex = sampleIndex;
                    }
                }
                shadeIndex = (this.grayscaleValue(color) + 8) / 17;
                if (shadeIndex > 0) {
                    final int sampleIndex;
                    shadeIndex = (sampleIndex = shadeIndex - shadeIndex / 3);
                    final int error = this.colorMatchError(color, GIFOutputStream.standard256[sampleIndex]);
                    if (error < minError || minIndex < 0) {
                        minError = error;
                        minIndex = sampleIndex;
                    }
                }
                bytePixels[j] = (byte)minIndex;
            }
        }
        return bytePixels;
    }
    
    protected int grayscaleValue(final int color) {
        final int r = (color & 0xFF0000) >> 16;
        final int g = (color & 0xFF00) >> 8;
        final int b = color & 0xFF;
        return (r * 30 + g * 59 + b * 11) / 100;
    }
    
    protected int colorMatchError(final int color1, final int color2) {
        final int r1 = (color1 & 0xFF0000) >> 16;
        final int g1 = (color1 & 0xFF00) >> 8;
        final int b1 = color1 & 0xFF;
        final int r2 = (color2 & 0xFF0000) >> 16;
        final int g2 = (color2 & 0xFF00) >> 8;
        final int b2 = color2 & 0xFF;
        final int dr = (r2 - r1) * 30;
        final int dg = (g2 - g1) * 59;
        final int db = (b2 - b1) * 11;
        return (dr * dr + dg * dg + db * db) / 100;
    }
    
    protected void writeGIFHeader(final int width, final int height, final int bitsPerPixel) throws IOException {
        this.write(71);
        this.write(73);
        this.write(70);
        this.write(56);
        this.write(57);
        this.write(97);
        this.writeGIFWord(width);
        this.writeGIFWord(height);
        int packedBits = 128;
        packedBits |= (bitsPerPixel - 1 << 4 | bitsPerPixel - 1);
        this.write(packedBits);
        this.write(0);
        this.write(0);
    }
    
    protected void writeColorTable(final int[] colorTable, final int bitsPerPixel) throws IOException {
        for (int colorCount = 1 << bitsPerPixel, j = 0; j < colorCount; ++j) {
            if (j < colorTable.length) {
                this.writeGIFColor(colorTable[j]);
            }
            else {
                this.writeGIFColor(0);
            }
        }
    }
    
    protected void writeGraphicControlExtension(final Color transparentColor, final int[] colorTable) throws IOException {
        for (int j = 0; j < colorTable.length; ++j) {
            if (colorTable[j] == (transparentColor.getRGB() & 0xFFFFFF)) {
                this.write(33);
                this.write(249);
                this.write(4);
                this.write(1);
                this.write(0);
                this.write(0);
                this.write(j);
                this.write(0);
            }
        }
    }
    
    protected void writeImageDescriptor(final int width, final int height) throws IOException {
        this.write(44);
        this.writeGIFWord(0);
        this.writeGIFWord(0);
        this.writeGIFWord(width);
        this.writeGIFWord(height);
        this.write(0);
    }
    
    protected void writeGIFWord(final short word) throws IOException {
        this.writeGIFWord((int)word);
    }
    
    protected void writeGIFWord(final int word) throws IOException {
        this.write(word & 0xFF);
        this.write((word & 0xFF00) >> 8);
    }
    
    protected void writeGIFColor(final Color color) throws IOException {
        this.writeGIFColor(color.getRGB());
    }
    
    protected void writeGIFColor(final int color) throws IOException {
        this.write((color & 0xFF0000) >> 16);
        this.write((color & 0xFF00) >> 8);
        this.write(color & 0xFF);
    }
    
    protected void writeCompressedImageData(final byte[] bytePixels, final int bitsPerPixel) throws IOException {
        int init_bits = bitsPerPixel;
        if (init_bits < 2) {
            init_bits = 2;
        }
        this.write(init_bits);
        this.obuf = 0;
        this.obits = 0;
        this.oblen = 0;
        this.code_clear = 1 << init_bits;
        this.code_eof = this.code_clear + 1;
        this.rl_basecode = this.code_eof + 1;
        this.out_bump_init = (1 << init_bits) - 1;
        this.out_clear_init = ((init_bits <= 2) ? 9 : (this.out_bump_init - 1));
        this.out_bits_init = init_bits + 1;
        this.max_ocodes = 4096 - ((1 << this.out_bits_init - 1) + 3);
        this.did_clear();
        this.output(this.code_clear);
        this.rl_count = 0;
        for (int j = 0; j < bytePixels.length; ++j) {
            int c = bytePixels[j];
            if (c < 0) {
                c += 256;
            }
            if (this.rl_count > 0 && c != this.rl_pixel) {
                this.rl_flush();
            }
            if (this.rl_pixel == c) {
                ++this.rl_count;
            }
            else {
                this.rl_pixel = c;
                this.rl_count = 1;
            }
        }
        if (this.rl_count > 0) {
            this.rl_flush();
        }
        this.output(this.code_eof);
        this.output_flush();
    }
    
    protected void write_block() throws IOException {
        this.write(this.oblen);
        this.write(this.oblock, 0, this.oblen);
        this.oblen = 0;
    }
    
    protected void block_out(final int c) throws IOException {
        this.oblock[this.oblen++] = (byte)c;
        if (this.oblen >= 255) {
            this.write_block();
        }
    }
    
    protected void block_flush() throws IOException {
        if (this.oblen > 0) {
            this.write_block();
        }
    }
    
    protected void output(final int val) throws IOException {
        this.obuf |= val << this.obits;
        this.obits += this.out_bits;
        while (this.obits >= 8) {
            this.block_out(this.obuf & 0xFF);
            this.obuf >>= 8;
            this.obits -= 8;
        }
    }
    
    protected void output_flush() throws IOException {
        if (this.obits > 0) {
            this.block_out(this.obuf);
        }
        this.block_flush();
    }
    
    protected void did_clear() throws IOException {
        this.out_bits = this.out_bits_init;
        this.out_bump = this.out_bump_init;
        this.out_clear = this.out_clear_init;
        this.out_count = 0;
        this.rl_table_max = 0;
        this.just_cleared = true;
    }
    
    protected void output_plain(final int c) throws IOException {
        this.just_cleared = false;
        this.output(c);
        ++this.out_count;
        if (this.out_count >= this.out_bump) {
            ++this.out_bits;
            this.out_bump += 1 << this.out_bits - 1;
        }
        if (this.out_count >= this.out_clear) {
            this.output(this.code_clear);
            this.did_clear();
        }
    }
    
    protected int isqrt(final int x) {
        if (x < 2) {
            return x;
        }
        int v;
        int r;
        for (v = x, r = 1; v != 0; v >>= 2, r <<= 1) {}
        while (true) {
            v = (x / r + r) / 2;
            if (v == r || v == r + 1) {
                break;
            }
            r = v;
        }
        return r;
    }
    
    protected int compute_triangle_count(int count, final int nrepcodes) {
        int cost = 0;
        for (int perrep = nrepcodes * (nrepcodes + 1) / 2; count >= perrep; count -= perrep) {
            cost += nrepcodes;
        }
        if (count > 0) {
            int n;
            for (n = this.isqrt(count); n * (n + 1) >= 2 * count; --n) {}
            while (n * (n + 1) < 2 * count) {
                ++n;
            }
            cost += n;
        }
        return cost;
    }
    
    protected void max_out_clear() {
        this.out_clear = this.max_ocodes;
    }
    
    protected void reset_out_clear() throws IOException {
        this.out_clear = this.out_clear_init;
        if (this.out_count >= this.out_clear) {
            this.output(this.code_clear);
            this.did_clear();
        }
    }
    
    protected void rl_flush_fromclear(int count) throws IOException {
        this.max_out_clear();
        this.rl_table_pixel = this.rl_pixel;
        int n = 1;
        while (count > 0) {
            if (n == 1) {
                this.rl_table_max = 1;
                this.output_plain(this.rl_pixel);
                --count;
            }
            else if (count >= n) {
                this.rl_table_max = n;
                this.output_plain(this.rl_basecode + n - 2);
                count -= n;
            }
            else if (count == 1) {
                ++this.rl_table_max;
                this.output_plain(this.rl_pixel);
                count = 0;
            }
            else {
                ++this.rl_table_max;
                this.output_plain(this.rl_basecode + count - 2);
                count = 0;
            }
            if (this.out_count == 0) {
                n = 1;
            }
            else {
                ++n;
            }
        }
        this.reset_out_clear();
    }
    
    protected void rl_flush_clearorrep(int count) throws IOException {
        final int withclr = 1 + this.compute_triangle_count(count, this.max_ocodes);
        if (withclr < count) {
            this.output(this.code_clear);
            this.did_clear();
            this.rl_flush_fromclear(count);
        }
        else {
            while (count > 0) {
                this.output_plain(this.rl_pixel);
                --count;
            }
        }
    }
    
    protected void rl_flush_withtable(final int count) throws IOException {
        int repmax = count / this.rl_table_max;
        int leftover = count % this.rl_table_max;
        int repleft = (leftover != 0) ? 1 : 0;
        if (this.out_count + repmax + repleft > this.max_ocodes) {
            repmax = this.max_ocodes - this.out_count;
            leftover = count - repmax * this.rl_table_max;
            repleft = 1 + this.compute_triangle_count(leftover, this.max_ocodes);
        }
        if (1 + this.compute_triangle_count(count, this.max_ocodes) < repmax + repleft) {
            this.output(this.code_clear);
            this.did_clear();
            this.rl_flush_fromclear(count);
            return;
        }
        this.max_out_clear();
        while (repmax > 0) {
            this.output_plain(this.rl_basecode + this.rl_table_max - 2);
            --repmax;
        }
        if (leftover != 0) {
            if (this.just_cleared) {
                this.rl_flush_fromclear(leftover);
            }
            else if (leftover == 1) {
                this.output_plain(this.rl_pixel);
            }
            else {
                this.output_plain(this.rl_basecode + leftover - 2);
            }
        }
        this.reset_out_clear();
    }
    
    protected void rl_flush() throws IOException {
        if (this.rl_count == 1) {
            this.output_plain(this.rl_pixel);
            this.rl_count = 0;
            return;
        }
        if (this.just_cleared) {
            this.rl_flush_fromclear(this.rl_count);
        }
        else if (this.rl_table_max < 2 || this.rl_table_pixel != this.rl_pixel) {
            this.rl_flush_clearorrep(this.rl_count);
        }
        else {
            this.rl_flush_withtable(this.rl_count);
        }
        this.rl_count = 0;
    }
}
