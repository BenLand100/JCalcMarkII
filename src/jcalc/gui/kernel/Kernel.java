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

package jcalc.gui.kernel;

import javax.swing.*;
import jcalc.base.*;

/**
 * Class Kernel
 * @author Benjamin J. Land
 */
public class Kernel extends JScrollPane {
    
    BaseCalc bc;
    KernelArea ta;
    
    /**
     * Creates a new instance of Kernel 
     */
    public Kernel(BaseCalc bc) {
        super();
        setName("Kernel");
        this.bc = bc;
        ta = new KernelArea(bc);
        this.setViewportView(ta);
    }
    
}
