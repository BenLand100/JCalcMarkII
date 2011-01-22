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

package jcalc.parser.tokens.operators;

import jcalc.base.Value;
import jcalc.parser.Token;
import jcalc.parser.tokens.*;
import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.ObjectType;
import com.sun.org.apache.bcel.internal.generic.Type;

/**
 * Class Exponent
 * @author Benjamin J. Land
 */
public class Exponent extends Operator {
    
    /** Creates a new instance of Exponent */
    public Exponent() {
        super("^");
    }
    
    public Token clone() {
        return new Exponent();
    }
    
    public int precedence() {
        return 3;
    }
    
    public static Number pow(Number a, Number b) {
        boolean aV = a instanceof Value;
        boolean bV = b instanceof Value;
        if (aV && bV) {
            return Value.dominant((Value)a,(Value)b).pow((Value)a, (Value)b);
        } else if (aV) {
            return ((Value)a).pow((Value)a,b);
        } else if (bV) {
            return ((Value)b).pow(a,(Value)b);
        } else {
            return Math.pow(a.doubleValue(), b.doubleValue());
        }
    }
    
    public Number preform(Number a, Number b) {
        return pow(a, b);
    }
    
    public void genCode(ClassGen cg, ConstantPoolGen cp, InstructionList il, InstructionFactory f) {
        il.append(f.createInvoke("jcalc.parser.tokens.operators.Exponent", "pow", new ObjectType("java.lang.Number"), new Type[] { new ObjectType("java.lang.Number"), new ObjectType("java.lang.Number") }, Constants.INVOKESTATIC));
    }
    
}
