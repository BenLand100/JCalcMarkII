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

package jcalc.parser.tokens;

import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import jcalc.parser.*;

/**
 * Class Grouper
 * @author Benjamin J. Land
 */
public class Grouper extends Token {
    
    private boolean opener;
    
    /** Creates a new instance of Grouper */
    public Grouper(String data) {
        super(data);
        switch(data.charAt(0)) {
            case '{':
            case '[':
            case '(':
                opener = true;
                break;
            case '}':
            case ']':
            case ')':
                opener = false;
                break;
            default:
                throw new RuntimeException("Bad Grouper: " + data);
        }
    }
    
    public boolean isCloser() {
        return !opener;
    }
    
    public boolean isOpener() {
        return opener;
    }
    
    public Token clone() {
        return new Grouper(getData());
    }
    
    public void genCode(ClassGen cg, ConstantPoolGen cp, InstructionList il, InstructionFactory f) {
        throw new RuntimeException("Groupers can not be generated.");
    }
    
}
