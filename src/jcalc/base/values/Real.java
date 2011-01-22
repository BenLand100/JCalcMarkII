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
 * Real.java
 *
 * Created on May 8, 2007, 4:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jcalc.base.values;

import java.util.regex.*;
import jcalc.base.*;

/**
 *
 * @author Benjamin J. Land
 */
public class Real extends Value {
    
    public static final Real INSTANCE = new Real(0,1);
    public static final Pattern REPEATER = Pattern.compile("(\\d+)(?:\\1)+");
    public static final long MAX_PRECISION = 100000000000L;
    
    private long numer;
    private long denom;
    
    /** Creates a new instance of Real */
    public Real(long numer, long denom) {
        this.numer = numer;
        this.denom = denom;
    }
    
    /**
     * Makes a Real if possible, otherwise a NumberWrapper
     */
    public Value fromDouble(double value) {
        String dec = Double.toString(value);
        String whole = dec.substring(0, dec.indexOf("."));
        Matcher m = REPEATER.matcher(dec.substring(dec.indexOf(".") + 1));
        if (m.find() && dec.length() > 5) {
            String rep = m.group(1);
            if (dec.length() - m.regionEnd() < rep.length()) {
                long denom = (long) Math.pow(10L,m.start()) * (((long) Math.pow(10L, rep.length())) - 1L);
                long numer = (new Long(rep).longValue()) + denom * (new Long(whole).longValue());
                return new Real(numer, denom).simplify();
            }
        }
        Real r = new Real((long)(value * MAX_PRECISION), MAX_PRECISION).simplify();
        if (r.denom >= MAX_PRECISION) return super.fromDouble(value);
        return r;
    }
    
    public int precedence() {
        return Value.REAL;
    }
    
    public Value subtract(Value a, Value b) {
        if (a instanceof Real && b instanceof Real) {
            return new Real((((Real)a).numer * ((Real)b).denom) - (((Real)b).numer * ((Real)a).denom), ((Real)a).denom * ((Real)b).denom).simplify();
        } else {
            return fromDouble(a.doubleValue() - b.doubleValue());
        }
    }
    
    public Value pow(Value a, Value b) {
        if (a instanceof Real && b instanceof Real) {
            Value denom = fromDouble(Math.pow(((Real)a).numer, b.doubleValue()));
            Value numer = fromDouble(Math.pow(((Real)a).denom, b.doubleValue()));
            return divide(denom, numer);
        } else {
            return fromDouble(Math.pow(a.doubleValue(),b.doubleValue()));
        }
    }
    
    public Value multiply(Value a, Value b) {
        if (a instanceof Real && b instanceof Real) {
            return new Real(((Real)a).numer * ((Real)b).numer, ((Real)a).denom * ((Real)b).denom).simplify();
        } else {
            return fromDouble(a.doubleValue() * b.doubleValue());
        }
    }
    
    public Value modulus(Value a, Value b) {
        if (a instanceof Real && b instanceof Real) {
            Real floor = new Real((long)(a.doubleValue() / b.doubleValue()), 1L);
            return subtract(a, multiply(b, floor));
        } else {
            return fromDouble(a.doubleValue() % b.doubleValue());
        }
    }
    
    public Value add(Value a, Value b) {
        if ((a instanceof Real) && (b instanceof Real)) {
            return new Real((((Real)a).numer * ((Real)b).denom) + (((Real)b).numer * ((Real)a).denom), ((Real)a).denom * ((Real)b).denom).simplify();
        } else {
            return fromDouble(a.doubleValue() + b.doubleValue());
        }
    }
    
    public Value divide(Value a, Value b) {
        if (a instanceof Real && b instanceof Real) {
            return new Real(((Real)a).numer * ((Real)b).denom, ((Real)a).denom * ((Real)b).numer).simplify();
        } else {
            return fromDouble(a.doubleValue() / b.doubleValue());
        }
    }
    
    public Value positate(Value a) {
        return new Real(Math.abs(((Real)a).numer), Math.abs(((Real)a).denom));
    }
    
    public Value negate(Value a) {
        return new Real(((Real)a).numer, -((Real)a).denom);
    }
    
    public int intValue() {
        return (int) (doubleValue());
    }
    
    public long longValue() {
        return (long) (doubleValue());
    }
    
    public float floatValue() {
        return (float) (doubleValue());
    }
    
    public double doubleValue() {
        return (double) numer / (double) denom;
    }
    
    public String toString() {
        if (denom == 1) return Long.toString(numer);
        if (numer == 0) return "0";
        return numer + "/" + denom;
    }
    
    public boolean equals(Object o) {
        return (o instanceof Real) && ((Real)o).numer == numer && ((Real)o).denom == denom;
    }
    
    public int hashCode() {
        return (new Long(numer).hashCode() ^ new Long(denom).hashCode()) * 37;
    }
    
    /**
     * Puts this Real into lowest terms
     */
    protected Real simplify() {
        if (numer % denom == 0) {
            numer /= denom;
            denom = 1L;
        }
        long gcd = gcd(numer, denom);
        numer = numer / gcd;
        denom = denom / gcd;
        return this;
    }
    
    private long gcd(long a, long b) {
        if (b==0)
            return a;
        else
            return gcd(b, a % b);
    }
    
}
