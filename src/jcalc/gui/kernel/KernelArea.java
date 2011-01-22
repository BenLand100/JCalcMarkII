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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import jcalc.base.*;

/**
 * Class KernelArea
 * @author Benjamin J. Land
 */
public class KernelArea extends JTextArea implements KeyListener {
    
    Pattern in = Pattern.compile("In\\[(\\d+)\\]");
    Pattern out = Pattern.compile("Out\\[(\\d+)\\]");
    Pattern ans = Pattern.compile("Ans");
    Vector<String> input = new Vector<String>();
    Vector<String> output = new Vector<String>();
    BaseCalc bc;
    
    /**
     * Creates a new instance of KernelArea 
     */
    public KernelArea(BaseCalc bc) {
        super("In[0]:= ");
        this.bc = bc;
        setFont(new Font("Courier New", Font.PLAIN, 16));
        setCaretPosition(8);
        getAbstractDocument().setDocumentFilter(new KernelFilter(":= "));
        addKeyListener(this);
        setLineWrap(true);
        setBorder(new EmptyBorder(5,5,5,5));
    }
    
    
    
    private int getInputOffset() {
        try {
            return getText().lastIndexOf(":= ") + 3;
        } catch (Exception e) {
            return -1;
        }
    }
    
    public AbstractDocument getAbstractDocument() {
        return (AbstractDocument) getDocument();
    }
    
    public void keyPressed(KeyEvent e) { }
    
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ENTER) {
            StringBuilder exp = new StringBuilder(getText().substring(getInputOffset(), getText().length()));
            if (exp.toString().trim().length() == 0) {
                if (input.size() != 0) {
                    exp = new StringBuilder(input.lastElement());
                } else {
                    exp = new StringBuilder("0");
                }
                append(exp.toString());
            }
            input.add(exp.toString());
            String value;
            try {
                Matcher m = in.matcher(exp);
                while (m.find()) {
                    exp.replace(m.start(), m.end(),"("+input.get(Integer.valueOf(m.group(1)))+")");
                    m.reset(exp);
                }
                m = out.matcher(exp);
                while (m.find()) {
                    exp.replace(m.start(), m.end(),"("+output.get(Integer.valueOf(m.group(1)))+")");
                    m.reset(exp);
                }
                m = ans.matcher(exp);
                while (m.find()) {
                    if (output.size() == 0) 
                        exp.replace(m.start(), m.end(),"0");
                    else
                        exp.replace(m.start(), m.end(),"("+output.lastElement()+")");
                    m.reset(exp);
                }
                value = bc.evaluate(exp.toString()).toString();
            } catch (Exception x) {
                value = x.getMessage() == null ? "Error" : x.getMessage();
                x.printStackTrace();
            }
            output.add(value);
            append("\n\nOut[" + (output.size() - 1) + "]:= " + value + "\n\nIn[" + input.size() + "]:= ");
        }
    }
    
    public void keyTyped(KeyEvent e) { }
    
}
