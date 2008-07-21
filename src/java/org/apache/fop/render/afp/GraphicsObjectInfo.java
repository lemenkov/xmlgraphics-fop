/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package org.apache.fop.render.afp;

import org.apache.xmlgraphics.util.MimeConstants;

/**
 *  A graphics object info which contains necessary painting objects
 */
public class GraphicsObjectInfo extends DataObjectInfo {

    private GraphicsObjectPainter painter;

    /**
     * Returns the graphics painter
     * 
     * @return the graphics painter
     */
    public GraphicsObjectPainter getPainter() {
        return painter;
    }

    /**
     * Sets the graphics painter
     * 
     * @param graphicsPainter the graphics painter
     */
    public void setPainter(GraphicsObjectPainter graphicsPainter) {
        this.painter = graphicsPainter;
    }
    
    /** {@inheritDoc} */
    public String toString() {
        return "GraphicsObjectInfo{" + super.toString() + "}";
    }

    /** {@inheritDoc} */
    public String getMimeType() {
        return MimeConstants.MIME_SVG;
    }
}
