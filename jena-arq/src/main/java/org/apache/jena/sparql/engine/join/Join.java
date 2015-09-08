/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.sparql.engine.join;

import static org.apache.jena.sparql.algebra.JoinType.LEFT ;
import static org.apache.jena.sparql.algebra.JoinType.PLAIN ;

import java.util.List ;

import org.apache.jena.atlas.iterator.Iter ;
import org.apache.jena.atlas.lib.DS ;
import org.apache.jena.sparql.algebra.Algebra ;
import org.apache.jena.sparql.algebra.JoinType ;
import org.apache.jena.sparql.algebra.Table ;
import org.apache.jena.sparql.algebra.TableFactory ;
import org.apache.jena.sparql.engine.ExecutionContext ;
import org.apache.jena.sparql.engine.QueryIterator ;
import org.apache.jena.sparql.engine.binding.Binding ;
import org.apache.jena.sparql.engine.iterator.QueryIterPlainWrapper ;
import org.apache.jena.sparql.engine.main.OpExecutor ;
import org.apache.jena.sparql.engine.ref.TableJoin ;
import org.apache.jena.sparql.expr.ExprList ;

/** API to various join algorithms */
public class Join {
    // See also package org.apache.jena.sparql.engine.index

    /**
     * Standard entry point to a join of two streams.
     * This is not a substitution/index join.
     * (See {@link OpExecutor} for streamed execution using substitution).
     * @param left
     * @param right
     * @param execCxt
     * @return QueryIterator
     */
    public static QueryIterator join(QueryIterator left, QueryIterator right, ExecutionContext execCxt) {
        return joinWorker(left, right, PLAIN, null, execCxt) ;
    }
   
    /** Standard entry point to a left join of two streams.
     * This is not a substitution/index join.
     * (See {@link OpExecutor} for streamed execution using substitution).
     *
     * @param left
     * @param right
     * @param conditions
     * @param execCxt
     * @return QueryIterator
     */
    public static QueryIterator leftJoin(QueryIterator left, QueryIterator right, ExprList conditions, ExecutionContext execCxt) {
        return joinWorker(left, right, LEFT, conditions, execCxt) ;
    }
   

    /**
     * Standard entry point to a join of two streams.
     * This is not a substitution/index join.
     * (See {@link OpExecutor} for streamed execution using substitution).
     * @param left
     * @param right
     * @param joinType
     * @param conditions
     * @param execCxt
     * @return QueryIterator
     */
    public static QueryIterator joinWorker(QueryIterator left, QueryIterator right, 
                                           JoinType joinType, ExprList conditions,
                                           ExecutionContext execCxt) {
        // Catch easy cases.
        // If left or right is a root, do fast.
//        if ( right.isEmpty() ) {
//            if ( joinType == PLAIN ) {
//                // No rows - no match
//                left.close() ;
//                return QueryIterNullIterator.create(execCxt) ;
//            }
//            else
//                // Left join - pass out left rows regardless of conditions.
//                return left ;
//        }
//        
//        if ( TableUnit.isTableUnit(right) )
//            return applyConditions(left, conditions, execCxt) ;
//        return joinWorkerN(left, right, joinType, conditions, execCxt) ;
        // XXX TEMPORARY!
        //throw new NotImplemented() ;
        Table t = TableFactory.create(right) ;
        return TableJoin.joinWorker(left, t, joinType, conditions, execCxt) ;
    }
    
    
    /** Evaluate a hash join.  This code materializes the left into a probe table
     * then hash joins from the right.  
     * 
     * @param joinKey   The key for the probe table.
     * @param left      Left hand side
     * @param right     Right hand side
     * @param cxt       ExecutionContext
     * @return          QueryIterator
     */
    public static QueryIterator hashJoin(JoinKey joinKey, QueryIterator left, QueryIterator right, ExecutionContext cxt) {
        return new QueryIterHashJoin(joinKey, left, right, cxt) ;
    }

    /** Very simple, materializing version - useful for debugging. 
     * Does <b>not</b> scale. 
     */
    public static QueryIterator innerLoopJoinBasic(QueryIterator left, QueryIterator right, ExecutionContext cxt) {
        List<Binding> leftRows = Iter.toList(left) ;
        List<Binding> output = DS.list() ;
        for ( ; right.hasNext() ; ) {
            Binding row2 = right.next() ;
            for ( Binding row1 : leftRows ) {
                Binding r = Algebra.merge(row1, row2) ;
                if ( r != null )
                    output.add(r) ;
            }
        }
        return new QueryIterPlainWrapper(output.iterator(), cxt) ;
    }

    /** Inner loops join.  This code materializes the left ino the inner loop tabole and
     *  then streams on the right.
     *  
     * @param left      Left hand side
     * @param right     Right hand side
     * @param cxt       ExecutionContext
     * @return          QueryIterator
     */ 
    public static QueryIterator innerLoopJoin(QueryIterator left, QueryIterator right, ExecutionContext cxt) {
        return new QueryIterNestedLoopJoin(left, right, cxt) ;
    }
}

