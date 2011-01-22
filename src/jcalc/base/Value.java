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

/*
 * Value.java
 *
 * Created on May 6, 2007, 9:16 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package jcalc.base;

import jcalc.base.values.NumberWrapper;

/**
 *
 * @author Benjamin J. Land
 */
public abstract class Value extends Number implements Comparable<Number> {
    
    public static final int REAL = 1;
    public static final int COMPLEX = 2;
    public static final int LIST = 3;
    
    public abstract int precedence();
    
    public abstract Value pow(Value a, Value b);
    
    public abstract Value add(Value a, Value b);
    
    public abstract Value subtract(Value a, Value b);
    
    public abstract Value multiply(Value a, Value b);
    
    public abstract Value divide(Value a, Value b);
    
    public abstract Value modulus(Value a, Value b);
    
    public abstract Value negate(Value a);
    
    public abstract Value positate(Value a);
    
    public Value pow(Number a, Number b) {
        return pow(fromDouble(a.doubleValue()), fromDouble(b.doubleValue()));
    }
    
    public Value pow(Number a, Value b) {
        return pow(fromDouble(a.doubleValue()), b);
    }
    
    public Value pow(Value a, Number b) {
        return pow(a, fromDouble(b.doubleValue()));
    }
    
    public Value add(Number a, Number b) {
        return add(fromDouble(a.doubleValue()), fromDouble(b.doubleValue()));
    }
    
    public Value add(Number a, Value b) {
        return add(fromDouble(a.doubleValue()), b);
    }
    
    public Value add(Value a, Number b) {
        return add(a, fromDouble(b.doubleValue()));
    }
    
    public Value subtract(Number a, Number b) {
        return subtract(fromDouble(a.doubleValue()), fromDouble(b.doubleValue()));
    }
    
    public Value subtract(Number a, Value b) {
        return subtract(fromDouble(a.doubleValue()), b);
    }
    
    public Value subtract(Value a, Number b) {
        return subtract(a, fromDouble(b.doubleValue()));
    }
    
    public Value multiply(Number a, Number b) {
        return multiply(fromDouble(a.doubleValue()), fromDouble(b.doubleValue()));
    }
    
    public Value multiply(Number a, Value b) {
        return multiply(fromDouble(a.doubleValue()), b);
    }
    
    public Value multiply(Value a, Number b) {
        return multiply(a, fromDouble(b.doubleValue()));
    }
    
    public Value divide(Number a, Number b) {
        return divide(fromDouble(a.doubleValue()), fromDouble(b.doubleValue()));
    }
    
    public Value divide(Number a, Value b) {
        return divide(fromDouble(a.doubleValue()), b);
    }
    
    public Value divide(Value a, Number b) {
        return divide(a, fromDouble(b.doubleValue()));
    }
    
    public Value modulus(Number a, Number b) {
        return modulus(fromDouble(a.doubleValue()), fromDouble(b.doubleValue()));
    }
    
    public Value modulus(Number a, Value b) {
        return modulus(fromDouble(a.doubleValue()), b);
    }
    
    public Value modulus(Value a, Number b) {
        return modulus(a, fromDouble(b.doubleValue()));
    }
    
    public static Value dominant(Value a, Value b) {
        return a.precedence() >= b.precedence() ? a : b;
    }
    
    public abstract int hashCode();
    
    public abstract boolean equals(Object other);
    
    public int compareTo(Number other) {
        return Double.compare(doubleValue(), other.doubleValue());
    }
    
    public Value fromDouble(double value) {
        return new NumberWrapper(value);
    }

    
}
