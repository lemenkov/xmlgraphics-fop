/* $Id$
 * Copyright (C) 2001 The Apache Software Foundation. All rights reserved.
 * For details on use and redistribution please refer to the
 * LICENSE file included with these sources.
 */

package org.apache.fop.render.ps;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public interface Filter {

    public void doFilter(InputStream in,
                         OutputStream out) throws IOException;
}

