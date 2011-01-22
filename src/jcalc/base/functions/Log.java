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

/**
 * The <code>Log</code> class defines a general logarithm function. It can be
 * invoked via the command Log[base, number] or, the common logarithm,
 * Log[number]. This function complies with <code>Value</code> precision,
 * however is not garunteed to return a <code>Number</code> of the same type as
 * the <code>Value</code>'s given as arguments.
 * <p>
 *  Mathematicaly, this class represents:
 * <br /> value = Frac[base,number]
 * <br /> base ^ value = number
 *
 *
 * @author Benjamin J. Land
 * @version 1.0
 */
public class Log extends Function {
    
    /**
     * The name of this function.
     */
    public static String NAME = "Log";
    /**
     * The instance used to acccess <code>Functions</code> polymorphic methods.
     */
    public static Log INSTANCE = new Log();
    
    /**
     *
     * Creates a new <code>Frac</code> object. Use {@link #INSTANCE} whenever possible.
     */
    public Log() {
        super(NAME);
    }
    
    /**
     * Evaluates the logarithm of a number. Takes exactly two arguments, or ome
     * argument. Does not garuntee that the result will be an instance of either
     * of the argument types.
     * @param args args[0] is the base of the logarithm or number to take the
     *             common log of. args[1] is the number that will have its
     *             logarithm taken if args[0] is the base.
     * @return The mathematical logarithm represended by the type of args[1] if
     *         preasent, args[0] otherwise.
     * @throws RuntimeException If the number of args is not equal to two or one.
     */
    public Number valueAt(Number... args) {
        if (args.length == 2) {
            if (args[1] instanceof MathExtended) {
                MathExtended me = (MathExtended)args[1];
                return null;
            } else if (args[1] instanceof Value) {
                return ((Value)args[1]).fromDouble(Math.log(args[1].doubleValue())/ Math.log(args[0].doubleValue()));
            } else {
                return Math.log(args[1].doubleValue())/ Math.log(args[0].doubleValue());
            }
        } else if (args.length == 1) {
            if (args[0] instanceof Value) {
                return ((Value)args[0]).fromDouble(Math.log10(args[0].doubleValue()));
            } else {
                return Math.log10(args[0].doubleValue());
            }
        } else {
            throw new RuntimeException("Invalid arguments to Log");
        }
    }
    
}
