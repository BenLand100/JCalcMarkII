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

package jcalc.parser;

import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionList;

/**
 * Class Token
 * @author Benjamin J. Land
 */
public abstract class Token {
    
    private String data;
    
    /** Creates a new instance of Token */
    public Token(String data) {
        this.data = data;
    }
    
    /**
     * This method is responsible for including this token in compiled calculations
     */
    public abstract void genCode(ClassGen cg, ConstantPoolGen cp, InstructionList il, InstructionFactory f);
    
    public String getData() {
        return data;
    }
    
    public String toString() {
        return data;
    }
    
    public boolean equals(Object o) {
        return getClass().isInstance(o) && ((Token)o).data.equals(data);
    }
    
    public abstract Token clone();
    
    public int hashCode() {
        return data.hashCode();
    }
    
}
