/*-- $Id$ -- 

 ============================================================================
                   The Apache Software License, Version 1.1
 ============================================================================
 
    Copyright (C) 1999 The Apache Software Foundation. All rights reserved.
 
 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:
 
 1. Redistributions of  source code must  retain the above copyright  notice,
    this list of conditions and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.
 
 3. The end-user documentation included with the redistribution, if any, must
    include  the following  acknowledgment:  "This product includes  software
    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
    Alternately, this  acknowledgment may  appear in the software itself,  if
    and wherever such third-party acknowledgments normally appear.
 
 4. The names "Fop" and  "Apache Software Foundation"  must not be used to
    endorse  or promote  products derived  from this  software without  prior
    written permission. For written permission, please contact
    apache@apache.org.
 
 5. Products  derived from this software may not  be called "Apache", nor may
    "Apache" appear  in their name,  without prior written permission  of the
    Apache Software Foundation.
 
 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Apache Software  Foundation and was  originally created by
 James Tauber <jtauber@jtauber.com>. For more  information on the Apache 
 Software Foundation, please see <http://www.apache.org/>.
 
 */
package org.apache.fop.fo;



// FOP

import org.apache.fop.apps.FOPException;

import org.apache.fop.layout.Area;



// Java

import java.util.Vector;



/**

 * base class for nodes in the formatting object tree

 */

abstract public class FONode {



    protected FObj parent;

    protected Vector children = new Vector();



    /** value of marker before layout begins */

    public final static int START = -1000;



    /** value of marker after break-after */

    public final static int BREAK_AFTER = -1001;



    /** 

     * where the layout was up to.

     *  for FObjs it is the child number

     *  for FOText it is the character number

     */

    protected int marker = START;



    protected boolean isInLabel = false;

    protected boolean isInListBody = false;

    protected boolean isInTableCell = false;



    protected int bodyIndent;

    protected int distanceBetweenStarts;

    protected int labelSeparation;



    protected int forcedStartOffset = 0;

    protected int forcedWidth = 0;



    protected FONode(FObj parent) {

	this.parent = parent;

    }



    public void setIsInLabel() {

	this.isInLabel = true;

    }



    public void setIsInListBody() {

	this.isInListBody = true;

    }



    public void setIsInTableCell() {

	this.isInTableCell = true;

    }



    public void setDistanceBetweenStarts(int distance) {

	this.distanceBetweenStarts = distance;

    }



    public void setLabelSeparation(int separation) {

	this.labelSeparation = separation;

    }



    public void setBodyIndent(int indent) {

	this.bodyIndent = indent;

    }



    public void forceStartOffset(int offset) {

	this.forcedStartOffset = offset;

    }



    public void forceWidth(int width) {

	this.forcedWidth = width;

    }



    public void resetMarker() {

	this.marker = START;

	int numChildren = this.children.size();

	for (int i = 0; i < numChildren; i++) {

	    ((FONode) children.elementAt(i)).resetMarker();

	}

    }



    protected void addChild(FONode child) {

	children.addElement(child);

    }



    public FObj getParent() {

	return this.parent;

    }



    /* status */

    /* layout was fully completed */

    public final static int OK = 1;

    /* none of the formatting object could be laid out because the

       containing area was full (end of page) */

    public final static int AREA_FULL_NONE = 2;

    /* some of the formatting object could not be laid out because the

       containing area was full (end of page) */

    public final static int AREA_FULL_SOME = 3;

    /* force page break */

    public final static int FORCE_PAGE_BREAK = 4;

    public final static int FORCE_PAGE_BREAK_EVEN = 5;

    public final static int FORCE_PAGE_BREAK_ODD = 6;



    abstract public int layout(Area area)

	throws FOPException;

}

