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
import jcalc.base.values.*;
import jcalc.parser.Compiler;
import jcalc.parser.*;

/**
 * The JCalc subsystem. This class holds refrences to all extra objects used 
 * by various parts of JCalc. This class is specificaly responsible for
 * managing <code>Variables</code> and <code>Functions</code> belonging to this
 * instance of JCalc. It also contains various settings used globally in Jcalc.
 *
 * @author Benjamin J. Land
 * @version 1.0
 */
public class BaseCalc {
    
    /**
     * The Lexer used by this JCalc instance.
     */
    private Lexer instanceLexer = new Lexer(this);
    /**
     * A <code>TreeMap</code> containing all <code>Function</code>s used in this
     * JCalc instance.
     */
    private TreeMap<String,Function> functions = new TreeMap<String,Function>();
    /**
     * A <code>TreeMap</code> containing all <code>Variable</code>s used in this
     * JCalc instance.
     */
    private TreeMap<String,Variable> variables = new TreeMap<String,Variable>();
    
    /**
     * The Pi constant represented by an unmodifiable <code>Variable</code>.
     */
    public static final Variable Pi = new Variable("Pi") {
        public Number getValue() { return Math.PI; }
        public void setValue(Number n) { }
        public String toString() { return Double.toString(Math.PI); }
    };
    /**
     * Euler's constant represented by an unmodifiable <code>Variable</code>.
     */
    public static final Variable e = new Variable("e") {
        public Number getValue() { return Math.E; }
        public void setValue(Number n) { }
        public String toString() { return Double.toString(Math.E); }
    };
    /**
     * The constant i (Sqrt[-1]) represented by an unmodifiable <code>Variable</code>.
     */
    public static final Variable i = new Variable("i") {
        public Number getValue() { return Complex.i; }
        public void setValue(Number n) { }
        public String toString() { return Complex.i.toString(); }
    };
    
    /**
     * Creates a new default JCalc instance.
     */
    public BaseCalc() {
        defaultVars();
        defaultFunctions();
    }
    
    /**
     * Adds all default <code>Function</code>s to this JCalc instance.
     */
    private void defaultFunctions() {
        functions.putAll(Function.getPreDefined());
        String[] _0arg = new String[] { "random" };
        String[] _0argNames = new String[] { "Random" };
        String[] _1arg = new String[] { "abs", "acos", "asin", "atan", "cbrt", "ceil", "cos", "cosh", "exp", "floor", "log", "rint", "signum", "sin", "sinh", "sqrt", "tan", "tanh", "toDegrees", "toRadians" };
        String[] _1argNames = new String[] { "Abs", "ACos", "ASin", "ATan", "Cbrt", "Ceil", "Cos", "Cosh", "Exp", "Floor", "Ln", "Round", "Sign", "Sin", "Sinh", "Sqrt", "Tan", "Tanh", "Degrees", "Radians" };
        String[] _2arg = new String[] { "atan2", "hypot", "max", "min", "pow" };
        String[] _2argNames = new String[] { "ATan2", "Hypot", "Max", "Min", "Pow" };
        Class<Math> math = Math.class;
        for (int i = 0; i < _0arg.length; i++)
            regFunction(Compiler.genMathFunction(_0arg[i], _0argNames[i], 0));
        for (int i = 0; i < _1arg.length; i++)
            regFunction(Compiler.genMathFunction(_1arg[i], _1argNames[i], 1));
        for (int i = 0; i < _2arg.length; i++)
            regFunction(Compiler.genMathFunction(_2arg[i], _2argNames[i], 2));
    }
    
    /**
     * Adds all default <code>Variable</code>s to this JCalc instance.
     */
    private void defaultVars() {
        regVariable(Pi);
        regVariable(e);
        regVariable(i);
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            regVariable(new Variable(Character.toString(letter)));
        }
    }
    /**
     * Returns the <code>Lexer</code> used by this instance.
     * @return The <code>Lexer</code>
     */
    public Lexer getLexer() {
        return instanceLexer;
    }
    
    /**
     * Gets a variable from this instance.
     * @param name The <code>Variable</code>'s name as stored in the HashMap.
     * @return The <code>Variable</code> if it exists, <code>null</code> otherwise.
     */
    public Variable getVariable(String name) {
        return variables.get(name);
    }
    
    /**
     * Gets a function from this instance.
     * @param name The <code>Function</code>'s name as stored in the HashMap.
     * @return The <code>Function</code> if it exists, <code>null</code> otherwise.
     */
    public Function getFunction(String name) {
        return functions.get(name);
    }
    
    /**
     * Evaluates a mathmatical expression. Allows the <code>BaseCalc</code> to 
     * control what evaluation method is used.
     * @param expression The mathmatical expression.
     * @return A <code>Number</code> representing the value of the expression.
     * @throws RuntimeException All lexical errors plus any evaluation errors.
     */
    public Number evaluate(String expression) {
        return Compiler.genExpression(instanceLexer.analyse(expression)).evaluate();
    }
    
    /**
     * Registers a <code>Variable</code> under the alias <code>name</code>
     * @param name The <code>Variable</code>'s alias.
     * @param variable The <code>Variable</code> to be registered.
     * @return <code>true</code> if the <code>Variable</code> did not exist 
     *         under this alias, <code>false</code> if it did.
     */
    public boolean regVariable(String name, Variable variable) {
        if (variables.containsKey(name)) return false;
        variables.put(name, variable);
        return true;
    }
    
    /**
     * Registers a <code>Function</code> under the alias <code>name</code>
     * @param name The <code>Function</code>'s alias.
     * @param function The <code>Function</code> to be registered.
     * @return <code>true</code> if the <code>Function</code> did not exist 
     *         under this alias, <code>false</code> if it did.
     */
    public boolean regFunction(String name, Function function) {
        if (functions.containsKey(name)) return false;
        functions.put(name, function);
        return true;
    }
    
    /**
     * Registers a <code>Variable</code> under its own name. This method uses 
     * <code>regVariable(String name, Variable variable)</code> to register 
     * variables.
     * @param variable The <code>Variable</code> to be registered.
     * @return <code>true</code> if the <code>Variable</code> did not exist 
     *         under its name, <code>false</code> if it did.
     * @see #regVariable(String,Variable)
     */
    public boolean regVariable(Variable variable) {
        return regVariable(variable.getName(), variable);
    }
    
    /**
     * Registers a <code>Function</code> under its own name. This method uses 
     * <code>regFunction(String name, Function function)</code> to register 
     * functions.
     * @param function The <code>Function</code> to be registered.
     * @return <code>true</code> if the <code>Function</code> did not exist 
     *         under its name, <code>false</code> if it did.
     * @see #regFunction(String,Function)
     */
    public boolean regFunction(Function function) {
        return regFunction(function.getName(), function);
    }
    
    /**
     * Returns the Value representing the precision requested by this instance.
     * Allows this <code>BaseCalc</code> to control the precision of math 
     * throughout the subsystem.
     * @return A <code>Value</code> representing the precision requested by this
     *         instance.
     */
    public Value getPrecision() {
        return Complex.INSTANCE;
    }
    
}
