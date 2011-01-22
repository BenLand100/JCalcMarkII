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
 * This interface defines a Token that requires a Field for compiled calculations
 */

package jcalc.parser;

import com.sun.org.apache.bcel.internal.generic.ReferenceType;

/**
 *
 * @author Benjamin J. Land
 */
public interface FieldToken extends Comparable<FieldToken> {
    
    public ReferenceType getObjectType();
    
    public Object getFieldObject();
    
    public Class<?> getFieldClass();
    
    public String getName();
    
    public boolean equals(Object o);
    
    public int hashCode();
    
    public int compareTo(FieldToken f);
    
}
