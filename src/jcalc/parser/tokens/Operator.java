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

import jcalc.parser.Token;
import jcalc.parser.tokens.operators.*;

/**
 * Class Operator
 * @author Benjamin J. Land
 */
public abstract class Operator extends Token {
    
    public static Operator genOperator(String data) {
        if (data.length() == 1) {
            switch (data.charAt(0)) {
                case '^': return new Exponent();
                case '%': return new Modulus();
                case '/': return new Divide();
                case '*': return new Multiply();
                case '+': return new Add();
                case '-': return new Subtract();
            }
        } else {
        }
        throw new RuntimeException("Invalid Operator: " + data);
    }
    
    /** Creates a new instance of Operator */
    public Operator(String data) {
        super(data);
    }
    
    public abstract Number preform(Number a, Number b);
    
    public abstract int precedence();
    
    public boolean precedes(Operator o) {
        return precedence() >= o.precedence();
    }
    
}
