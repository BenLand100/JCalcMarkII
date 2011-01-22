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
import jcalc.base.values.Real;

/**
 * The <code>Frac</code> class defines a function to convert <code>Number</code>'s
 * to fractions, or <code>Real</code> instances. It can be invoked via the 
 * command Frac[number]. If the number cannot be represented by a <code>Real</code>, 
 * it will be returned unchanged.
 * 
 * @author Benjamin J. Land
 * @version 1.0
 */
public class Frac extends Function {
    
    /**
     * The name of this function.
     */
    public static String NAME = "Frac";
    /**
     * The instance used to acccess <code>Functions</code> polymorphic methods.
     */
    public static Frac INSTANCE = new Frac();
    
    /**
     * 
     * Creates a new <code>Frac</code> object. Use {@link #INSTANCE} whenever possible.
     */
    public Frac() {
        super(NAME);
    }
    
    /**
     * Converts a <code>Number</code> to a <code>Real</code> casted as a 
     * <code>Number</code>. If the <code>Number</code> cannot be converted to a
     * <code>Real</code> then the <code>Number</code> will be returned
     * @param args args[0] is the number to be converted.
     * @return args[0] as a <code>Real</code>, if possible. Otherwise args[0].
     * @throws RuntimeException If the number of args is not equal to one. 
     */
    public Number valueAt(Number... args) {
       if (args.length == 1) {
           Value real = Real.INSTANCE.fromDouble(args[0].doubleValue());
           if (real instanceof Real) {
               return real;
           } else {
               return args[0];
           }
       } else {
           throw new RuntimeException("Invalid arguments to Frac");
       }      
    }
    
}
