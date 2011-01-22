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

package jcalc.parser;

/**
 * The <code>StaticToken</code> interface defines a token to be unchangeable. 
 * All <code>Token</code> subclasses are supposed to be immutable, but the value 
 * of them is prone to change, in other words they are not static at runtime. 
 * Any class that implements <code>StaticToken</code> is garunteed to have a 
 * static value independent of outside change.
 * <p>
 * This interface requires no methods, as its only purpous is to define static,
 * simplifiable <code>Token</code>s for the <code>Lexer</code> and the 
 * <code>Trees</code> classes.
 *
 * @author Benjamin J. Land
 * @version 1.0
 */
public interface StaticToken {
    
}
