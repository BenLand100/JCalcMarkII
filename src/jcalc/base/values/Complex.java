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
 * Complex.java
 *
 * Created on May 10, 2007, 4:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jcalc.base.values;

import jcalc.base.*;

/**
 *
 * @author Benjamin J. Land
 */
public class Complex extends Value implements MathExtended<Complex> {
    
    public static final Complex i = new Complex(0,1);
    public static final Complex INSTANCE = new Complex(0,0);
    
    private double real;
    private double imaginary;
    
    /** Creates a new instance of Complex */
    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }
    
    public double realPart() {
        return real;
    }
    
    public double imagPart() {
        return imaginary;
    }
    
    public int precedence() {
        return Value.COMPLEX;
    }
    
    public Value subtract(Value a, Value b) {
        Complex ac = (Complex) (a instanceof Complex ? a : fromDouble(a.doubleValue()));
        Complex bc = (Complex) (b instanceof Complex ? b : fromDouble(b.doubleValue()));
        return new Complex(ac.realPart() - bc.realPart(), ac.imagPart() - bc.imagPart());
    }
    
    public Value add(Value a, Value b) {
        Complex ac = (Complex) (a instanceof Complex ? a : fromDouble(a.doubleValue()));
        Complex bc = (Complex) (b instanceof Complex ? b : fromDouble(b.doubleValue()));
        return new Complex(ac.realPart() + bc.realPart(), ac.imagPart() + bc.imagPart());
    }
    
    public Value divide(Value a, Value b) {
        Complex ac = (Complex) (a instanceof Complex ? a : fromDouble(a.doubleValue()));
        Complex bc = (Complex) (b instanceof Complex ? b : fromDouble(b.doubleValue()));
        double denom = bc.realPart() * bc.realPart() + bc.imagPart() * bc.imagPart();
        double real = ac.realPart()*bc.realPart() + ac.imagPart()*bc.imagPart();
        double imag = ac.imagPart()*bc.realPart() - ac.realPart()*bc.imagPart();
        return new Complex(real / denom, imag / denom);
    }
    
    public Value multiply(Value a, Value b) {
        Complex ac = (Complex) (a instanceof Complex ? a : fromDouble(a.doubleValue()));
        Complex bc = (Complex) (b instanceof Complex ? b : fromDouble(b.doubleValue()));
        return new Complex(ac.realPart()*bc.realPart() - ac.imagPart()*bc.imagPart(), ac.realPart()*bc.imagPart() + ac.imagPart()*bc.realPart());
    }
    
    public Value pow(Value a, Value b) {
        Complex ac = (Complex) (a instanceof Complex ? a : fromDouble(a.doubleValue()));
        Complex bc = (Complex) (b instanceof Complex ? b : fromDouble(b.doubleValue()));
        Complex result = ((Complex)multiply(bc, ac.log())).exp();
        if ((result.imaginary > 0D && result.imaginary < 1e-15) || (result.imaginary < 0D && result.imaginary > -1e-15 )) result.imaginary = 0D;
        if ((result.real > 0D && result.real < 1e-14) || (result.real < 0D && result.real > -1e-14 )) result.real = 0D;
        return result;
    }
    
    public Value modulus(Value a, Value b) {
        throw new RuntimeException("No complex modulus (%) defined");
    }
    
    public Value negate(Value a) {
        return ((Complex)a).opposite();
    }
    
    public Value positate(Value a) {
        return new Complex(+((Complex)a).realPart(), +((Complex)a).imagPart());
    }
    
    public double absolute() {
        return Math.sqrt(realPart() * realPart() + imagPart() * imagPart());
    }
    
    public Complex opposite() {
        return new Complex(-realPart(), -imagPart());
    }
    
    /**
     * Computes the conjugate of this complex number.
     * @return The conjugate
     */
    public Complex conjugate() {
        return new Complex(realPart(), -imagPart());
    }
    
    /**
     * Computes the angle of this complex number.
     * @return The angle: ATan2[imagPart(), realPart()]
     */
    public double argument() {
        return Math.atan2(imagPart(),realPart());
    }
    
    /**
     * Computes the natural expontinal of this complex number.
     * @return The natural expontinal
     */
    public Complex exp() {
        double exp = Math.exp(realPart());
        return new Complex(exp * Math.cos(imagPart()), exp * Math.sin(imagPart()));
    }
    
    /**
     * Computes the natural logarithm of this complex number.
     * @return The natural logarithm
     */
    public Complex log() {
        double real = absolute();
        double imag = argument();
        if (imag > Math.PI) imag -= 2D * Math.PI;
        return new Complex(Math.log(real), imag);
    }
    
    /**
     * Computes the square root of this complex number.
     * @return The square root
     */
    public Complex sqrt() {
        double r = absolute();
        double real= Math.sqrt(0.5D * (r+realPart()));
        double imag= Math.sqrt(0.5D * (r-realPart()));
        if (imagPart() < 0.0) imag = -imag;
        return new Complex(real,imag);
    }
    
    public Complex cos() {
        return (Complex) divide(add(((Complex)multiply(i, this)).exp(), ((Complex)multiply(negate(i), this)).exp()), 2D);
    }
    
    public Complex sin() {
        return (Complex) divide(subtract(((Complex)multiply(i, this)).exp(), ((Complex)multiply(negate(i), this)).exp()), multiply(2D, i));
    }
    
    public Complex tan() {
        return (Complex) divide(sin(), cos());
    }
    
    public Complex asin() {
        return (Complex) (multiply(negate(i),((Complex)add(multiply(i,this),((Complex)subtract(1,pow(this,2D))).sqrt())).log()));
    }
    
    public Complex acos() {
        return (Complex)
        (add(Math.PI/2D,multiply(i,((Complex)add(multiply(i,this),((Complex)subtract(1,pow(this,2D))).sqrt() )).log())));
    }
    
    public Complex atan() {
        return (Complex) (multiply(divide(1,2D),subtract(((Complex)subtract(1,multiply(i,this))).log(),((Complex)add(1,multiply(i,this))).log())));
    }
    
    public double doubleValue() {
        if (imaginary == 0) {
            return real;
        }
        throw new RuntimeException("Error: Complex");
    }
    
    public float floatValue() {
        return (float) doubleValue();
    }
    
    public int intValue() {
        return (int) doubleValue();
    }
    
    public long longValue() {
        return (long) doubleValue();
    }
    
    public int hashCode() {
        return (new Double(imagPart()).hashCode() ^ new Double(realPart()).hashCode() ^ 0x6548395) * 43;
    }
    
    public boolean equals(Object other) {
        return (other instanceof Complex) && ((Complex)other).imagPart() == imagPart() && ((Complex)other).realPart() == realPart();
    }
    
    public Value fromDouble(double d) {
        return new Complex(d,0);
    }
    
    public String toString() {
        if (realPart() == 0 && imagPart() == 0) {
            return "0";
        } else if (imagPart() == 1 && realPart() == 0) {
            return "i";
        } else if (realPart() == 0) {
            return String.valueOf(imagPart()) + "*i";
        } else if (imagPart() == 0) {
            return String.valueOf(realPart());
        } else {
            return String.valueOf(realPart()) + (imagPart() < 0 ? "" : "+") + String.valueOf(imagPart()) + "*i";
        }
    }
    
}
