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

package jcalc.base.functions;

import jcalc.base.*;
import jcalc.base.values.Complex;

/**
 * The <code>Root</code> class defines a general root function. It can be invoked
 * via the command Root[principle, number]. This function complies with
 * <code>Value</code> precision, however is not garunteed to return a
 * <code>Number</code> of the same type as the <code>Value</code>'s given as
 * arguments.
 * <p>
 *  Mathematicaly, this class represents:
 * <br /> value = Root[principle,number]
 * <br /> value ^ principle = number
 *
 * @author Benjamin J. Land
 * @version 1.0
 */
public class Root extends Function {
    
    /**
     * The name of this function.
     */
    public static String NAME = "Root";
    /**
     * The instance used to acccess <code>Functions</code> polymorphic methods.
     */
    public static Root INSTANCE = new Root();
    
    /**
     * Creates a new <code>Root</code> object. Use {@link #INSTANCE} whenever possible.
     */
    public Root() {
        super(NAME);
    }
    
    /**
     * Evaluates the root of a number. Takes exactly two arguments. Does not
     * garuntee that the result will be an instance of either of the argument
     * types.
     * @param args args[0] is the principle or the nth root.
     *             args[1] is the number that will have its root taken.
     * @return The mathematical equivlent of (args[1]) ^ (1D / args[0])
     * @throws RuntimeException If the number of args is not equal to two.
     */
    public Number valueAt(Number... args) {
        if (args.length != 2) throw new RuntimeException("Invalid arguments to Root");
        if (!(args[1] instanceof Complex) && args[1].doubleValue() < 0D) args[1] = new Complex(args[1].doubleValue(),0);
        if (args[1] instanceof Value && args[0] instanceof Value) {
            return Value.dominant((Value)args[0], (Value)args[1]).pow((Value)args[1], ((Value)args[0]).divide(1.0D,(Value)args[0]));
        } else if (args[1] instanceof Value) {
            return ((Value)args[1]).pow((Value)args[1], 1.0D / args[0].doubleValue());
        } else if (args[0] instanceof Value) {
            return ((Value)args[0]).pow(args[1], ((Value)args[0]).divide(1.0D, (Value)args[0]));
        } else {
            return Math.pow(args[1].doubleValue(), 1D / args[0].doubleValue());
        }
    }
    
}
