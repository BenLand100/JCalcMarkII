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

package jcalc.base.values;

import jcalc.base.*;


public class NumberWrapper extends Value {
    
    public static final NumberWrapper INSTANCE = new NumberWrapper(0D);
    
    private Number num;
    
    public NumberWrapper(Number num) {
        this.num = num;
    }
    
    public int precedence() {
        return 0;
    }
    
    public Value pow(Value a, Value b) {
        return fromDouble(Math.pow(a.doubleValue(),b.doubleValue()));
    }
    
    public Value add(Value a, Value b) {
        return fromDouble(a.doubleValue() + b.doubleValue());
    }
    
    public Value subtract(Value a, Value b) {
        return fromDouble(a.doubleValue() - b.doubleValue());
    }
    
    public Value multiply(Value a, Value b) {
        return fromDouble(a.doubleValue() * b.doubleValue());
    }
    
    public Value divide(Value a, Value b) {
        return fromDouble(a.doubleValue() / b.doubleValue());
    }
    
    public Value modulus(Value a, Value b) {
        return fromDouble(a.doubleValue() % b.doubleValue());
    }
    
    public Value negate(Value a) {
        return fromDouble(-a.doubleValue());
    }
    
    public Value positate(Value a) {
        return fromDouble(Math.abs(a.doubleValue()));
    }
    
    public int intValue() {
        return num.intValue();
    }
    
    public long longValue() {
        return num.longValue();
    }
    
    public float floatValue() {
        return num.floatValue();
    }
    
    public double doubleValue() {
        return num.doubleValue();
    }
    
    public String toString() {
        return Double.toString(doubleValue());
    }
    
    public boolean equals(Object o) {
        return (o instanceof NumberWrapper) && ((NumberWrapper) o).doubleValue()  == doubleValue();
    }
    
    public int hashCode() {
        return (0x67AB29DE ^ num.hashCode()) * 37;
    }
    
}
