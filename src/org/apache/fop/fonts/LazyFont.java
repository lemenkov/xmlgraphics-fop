/*
 * $Id$
 * Copyright (C) 2001-2003 The Apache Software Foundation. All rights reserved.
 * For details on use and redistribution please refer to the
 * LICENSE file included with these sources.
 */

package org.apache.fop.fonts;

//Java
import java.util.Map;

//FOP
import org.apache.fop.render.pdf.FontReader;

/**
 * This class is used to defer the loading of a font until it is really used.
 */
public class LazyFont extends Font implements FontDescriptor {

    private String metricsFileName = null;
    private String fontEmbedPath = null;
    private boolean useKerning = false;

    private boolean isMetricsLoaded = false;
    private Font realFont = null;
    private FontDescriptor realFontDescriptor = null;

    /**
     * Main constructor
     * @param fontEmbedPath path to embeddable file (may be null)
     * @param metricsFileName path to the metrics XML file
     * @param useKerning True, if kerning should be enabled
     */
    public LazyFont(String fontEmbedPath, String metricsFileName, boolean useKerning) {
        this.metricsFileName = metricsFileName;
        this.fontEmbedPath = fontEmbedPath;
        this.useKerning = useKerning;
    }

    private void load() {
        if (!isMetricsLoaded) {
            isMetricsLoaded = true;
            try {
                /**@todo Possible thread problem here */

                FontReader reader = new FontReader(metricsFileName);
                reader.setKerningEnabled(useKerning);
                reader.setFontEmbedPath(fontEmbedPath);
                realFont = reader.getFont();
                if (realFont instanceof FontDescriptor) {
                    realFontDescriptor = (FontDescriptor) realFont;
                }
                // System.out.println("Metrics " + metricsFileName + " loaded.");
            } catch (Exception ex) {
                /**@todo Log this exception */
                //log.error("Failed to read font metrics file "
                //                     + metricsFileName
                //                     + " : " + ex.getMessage());
            }
        }
    }

    /**
     * Gets the real font.
     * @return the real font
     */
    public Font getRealFont() {
        load();
        return realFont;
    }

    // ---- Font ----
    /**
     * @see org.apache.fop.fonts.Font#getEncoding()
     */
    public String getEncoding() {
        load();
        return realFont.getEncoding();
    }

    /**
     * @see org.apache.fonts.pdf.Font#mapChar(char)
     */
    public char mapChar(char c) {
        load();
        return realFont.mapChar(c);
    }

    /**
     * @see org.apache.fop.fonts.Font#isMultiByte()
     */
    public boolean isMultiByte() {
        return realFont.isMultiByte();
    }

    // ---- FontMetrics interface ----
    /**
     * @see org.apache.fop.fonts.FontMetrics#getFontName()
     */
    public String getFontName() {
        load();
        return realFont.getFontName();
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getAscender(int)
     */
    public int getAscender(int size) {
        load();
        return realFont.getAscender(size);
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getCapHeight(int)
     */
    public int getCapHeight(int size) {
        load();
        return realFont.getCapHeight(size);
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getDescender(int)
     */
    public int getDescender(int size) {
        load();
        return realFont.getDescender(size);
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getXHeight(int)
     */
    public int getXHeight(int size) {
        load();
        return realFont.getXHeight(size);
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getWidth(int, int)
     */
    public int getWidth(int i, int size) {
        load();
        return realFont.getWidth(i, size);
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getWidths()
     */
    public int[] getWidths() {
        load();
        return realFont.getWidths();
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#hasKerningInfo()
     */
    public boolean hasKerningInfo() {
        load();
        return realFont.hasKerningInfo();
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getKerningInfo()
     */
    public Map getKerningInfo() {
        load();
        return realFont.getKerningInfo();
    }

    // ---- FontDescriptor interface ----
    /**
     * @see org.apache.fop.fonts.FontDescriptor#getCapHeight()
     */
    public int getCapHeight() {
        load();
        return realFontDescriptor.getCapHeight();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getDescender()
     */
    public int getDescender() {
        load();
        return realFontDescriptor.getDescender();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getAscender()
     */
    public int getAscender() {
        load();
        return realFontDescriptor.getAscender();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getFlags()
     */
    public int getFlags() {
        load();
        return realFontDescriptor.getFlags();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getFontBBox()
     */
    public int[] getFontBBox() {
        load();
        return realFontDescriptor.getFontBBox();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getItalicAngle()
     */
    public int getItalicAngle() {
        load();
        return realFontDescriptor.getItalicAngle();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getStemV()
     */
    public int getStemV() {
        load();
        return realFontDescriptor.getStemV();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getFontType()
     */
    public FontType getFontType() {
        load();
        return realFontDescriptor.getFontType();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#isEmbeddable()
     */
    public boolean isEmbeddable() {
        load();
        return realFontDescriptor.isEmbeddable();
    }

}

