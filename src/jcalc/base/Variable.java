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

/**
 * Class Variable
 * @author Benjamin J. Land
 */
public class Variable extends Number {
    
    private Number value;
    private String name;
    
    /** Creates a new instance of Variable */
    public Variable(String name, Number initialValue) {
        this.name = name;
        value = initialValue;
    }
    
    public Variable(String name) {
        this.name = name;
        value = new Double(0D);
    }
    
    public String getName() {
        return name;
    }
    
    public Number getValue() {
        return value;
    }
    
    public void setValue(Number value) {
        this.value = value;
    }
    
    public int intValue() {
        return getValue().intValue();
    }
    
    public long longValue() {
        return getValue().longValue();
    }
    
    public float floatValue() {
        return getValue().floatValue();
    }
    
    public double doubleValue() {
        return getValue().doubleValue();
    }
    
    public void setInt(int n) {
        setValue(n);
    }
    
    public void setLong(long n) {
        setValue(n);
    }
    
    public void setFloat(float n) {
        setValue(n);
    }
    
    public void setDouble(double n) {
        setValue(n);
    }
    
    public boolean equals(Object o) {
        return (o instanceof Variable) && ((Variable)o).getName().equals(getName());
    }
    
    public int hashCode() {
        return getName().hashCode();
    }
    
    public String toString() {
        return getValue().toString();
    }
    
}
