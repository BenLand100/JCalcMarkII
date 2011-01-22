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

package jcalc.base;

import java.util.TreeSet;

/**
 * Classes that implement <code>MathExtended</code> define sellect methods of 
 * <code>Math</code> class that allow those methods to be preformed on 
 * nonstandard numbers. For instance, the <code>Complex</code> class defines
 * these methods to allow calculations on complex numbers. These functions are
 * preformed on the instance used to invoke them, but do not change that 
 * instance, rather they return a new instance representing the value.
 * 
 * @author Benjamin J. Land
 */
public interface MathExtended<E extends Number> {
    
    public static final String[] functions = new String[] {
        "sin","cos","tan","asin","acos","atan","exp","log","sqrt"
    };
    
    /**
     * The sine function.
     * @return The sine of this instance.
     */
    public E sin();
    
    /**
     * The cosine function.
     * @return The cosine of this instance.
     */
    public E cos();
    
    /**
     * The tangent function.
     * @return The tangent of this instance.
     */
    public E tan();
    
    /**
     * The inverse sine function.
     * @return The inverse sine of this instance.
     */
    public E asin();
    
    /**
     * The inverse cosine function.
     * @return The inverse cosine of this instance.
     */
    public E acos();
    
    /**
     * The inverse tangent function.
     * @return The inverse tangent of this instance.
     */
    public E atan();
    
    /**
     * The natural exponential (e^x) function.
     * @return The natural exponential of this instance.
     */
    public E exp();
    
    /**
     * The natural logarithm function.
     * @return The natural logarithm of this instance.
     */
    public E log();
    
    /**
     * The square root function.
     * @return The square root of this instance.
     */
    public E sqrt();
    
}
