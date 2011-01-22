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
 * Class Expression
 * @author Benjamin J. Land
 */
public abstract class Expression extends Number {
    
    String data;
    
    /** Creates a new instance of Expression */
    public Expression(String data) {
        this.data = data;
    }
    
    public abstract Number evaluate();
    
    public String getData() {
        return data;
    }
    
    public int intValue() {
        return evaluate().intValue();
    }
    
    public long longValue() {
        return evaluate().longValue();
    }
    
    public float floatValue() {
        return evaluate().floatValue();
    }
    
    public double doubleValue() {
        return evaluate().doubleValue();
    }
    
    public int hashCode() {
        return data.hashCode();
    }
    
    public boolean equals(Object o) {
        return getClass().isInstance(o) && ((Expression)o).data.equals(data);
    }
    
}
