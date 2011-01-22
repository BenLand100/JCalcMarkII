/**
 *  Copyright 2009 by Benjamin J. Land (a.k.a. BenLand100)
 *
 *  This file is part of JCalc.
 *
 *  JCalc is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  JTuner is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with JCalc. If not, see <http://www.gnu.org/licenses/>.
 */

package jcalc.gui.kernel;

import javax.swing.text.*;

/**
 * Class KernelFilter
 * @author Benjamin J. Land
 */
public class KernelFilter extends DocumentFilter {
    
    private final String promptString;
    
    /**
     * Creates a new instance of KernelFilter 
     */
    public KernelFilter(String promptString) {
        this.promptString = promptString;
    }
    
    private int getEditOffset(String data) {
        return data.lastIndexOf(promptString) + promptString.length();
    }
    
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        Document doc = fb.getDocument();
        int editOffset = getEditOffset(doc.getText(0, doc.getLength()));
        if (offset >= editOffset && !text.equals("\n")) {
            fb.insertString(offset, text, attr);
        }
    }
    
    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
        Document doc = fb.getDocument();
        int editOffset = getEditOffset(doc.getText(0, doc.getLength()));
        if (offset >= editOffset) {
            fb.remove(offset, length);
        }
    }
    
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        int editOffset = getEditOffset(doc.getText(0, doc.getLength()));
        if (offset >= editOffset && !text.equals("\n")) {
            fb.replace(offset, length, text, attrs);
        }
    }
    
}
