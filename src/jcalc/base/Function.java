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

import java.util.*;
import jcalc.base.functions.*;
import jcalc.parser.*;
import jcalc.parser.Compiler;

/**
 * The Function class abstracts the mathmatical concept of a function relative
 * to one or more values. All classes that extend Function are required to
 * override <code>valueAt(Number... args)</code>. It is imperative that
 * subclasses of Function each have a unique name, or override the
 * <code>hashCode()</code> method.
 *
 * @author Benjamin J. Land
 * @version 1.0
 */
public abstract class Function {
    
    /**
     * This functions name, immutable.
     */
    private String name;
    
    /**
     * This constructor must be called, as it sets the name of the Function
     * @param name The name of the Function
     */
    public Function(String name) {
        this.name = name;
    }
    
    /**
     * The core of the Function class. May take any number of arguments in the
     * format of Numbers, and may return any subclass of Number
     * @param args Var-args array of arguments
     * @return The value of this function relative to <code>args</code>
     */
    public abstract Number valueAt(Number... args);
    
    /**
     * Convience method, uses <code>valueAt(Number... args)</code>
     * @param args Same arguments for <code>valueAt</code>
     * @return A double retieved via the <code>doubleValue()</code> method of
     *         the result of <code>valueAt</code>
     * @see #valueAt(Number... args)
     */
    public double doubleAt(Number... args) {
        return valueAt(args).doubleValue();
    }
    
    /**
     * The name of this Function. Should be unique for each Function implementation.
     * @return The Function name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Tests to see if this Function is equal to another
     * @param o The other Function
     * @return <code>false</code> if o is not an instance of Function or the
     *         Functions do not have the same name. <code>true</code> if o is a
     *         Function and they both have the same name.
     */
    public boolean equals(Object o) {
        return (o instanceof Function) && ((Function)o).name.equals(name);
    }
    
    /**
     * Returns the hash code of this Function
     * @return The hash code
     */
    public int hashCode() {
        return (name.hashCode() ^ 0x64398962) * 57;
    }
    
    /**
     * Generates the argument list for this function. Used by the
     * {@link jcalc.parser.Lexer Lexer} class to deduce how to send arguments
     * to this functions <code>valueAt</code> method. Should only be overridden
     * if the implementation requires non-standard handling of its arguments.
     * @param args List of String objects to be parsed into arguments
     * @param baseCalc The BaseCalc instance this analysis is using
     * @return A Vector of Number objects to be used as arguments to the
     *         <code>valueAt</code> method
     * @see #valueAt(Number...)
     */
    public Vector<Expression> simplifyArgs(List<String> args, BaseCalc baseCalc) {
        Lexer l = baseCalc.getLexer();
        Vector<Expression> result = new Vector<Expression>();
        for (String arg : args)
            result.add(Compiler.genExpression(l.analyse(arg)));
        return result;
    }
    
    /**
     * Creates a map of all pre-defined Functions. These Functions can be found
     * in the <code>jcalc.base.functions</code> package.
     * @return A map of all pre-defined Functions.
     * @see jcalc.base.functions
     */
    public static HashMap<String, Function> getPreDefined() {
        HashMap<String, Function> map = new HashMap<String, Function>();
        map.put(Root.NAME, Root.INSTANCE);
        map.put(Frac.NAME, Frac.INSTANCE);
        map.put(Log.NAME, Log.INSTANCE);
        return map;
    }
    
}
