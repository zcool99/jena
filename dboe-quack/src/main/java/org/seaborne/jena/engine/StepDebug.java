/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  See the NOTICE file distributed with this work for additional
 *  information regarding copyright ownership.
 */

package org.seaborne.jena.engine;

import java.util.List ;

import org.seaborne.jena.engine.row.RowListBase ;
import org.slf4j.Logger ;

/** Evaluate and print */
public class StepDebug<X> implements Step<X> {
    private static Logger log = Quack.debugLog ;
    public StepDebug() {}
    
    @Override
    public RowList<X> execute(RowList<X> input) {
        if ( log.isDebugEnabled() ) {
            List<Row<X>> list = input.toList() ;
            log.debug("Rows = "+list.size()) ;
            for ( Row<X> r : list )
                log.debug("  "+r) ;
            input = new RowListBase<X>(input.vars(), list) ;
        }
        return input ;
    }

    @Override
    public String toString() { return "Step/Debug" ; }
}

