package org.hisp.dhis.common;

/*
 * Copyright (c) 2004-2014, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Lars Helge Overland
 */
public class DimensionalObjectUtilsTest
{
    @Test
    public void testGetPrettyFilter()
    {        
        assertEquals( "< 5, = Discharged", DimensionalObjectUtils.getPrettyFilter( "LT:5:EQ:Discharged" ) );
        assertEquals( ">= 10, Female", DimensionalObjectUtils.getPrettyFilter( "GE:10:LIKE:Female" ) );
        assertEquals( "> 20, Discharged, Transferred", DimensionalObjectUtils.getPrettyFilter( "GT:20:IN:Discharged;Transferred" ) );
        assertEquals( null, DimensionalObjectUtils.getPrettyFilter( null ) );
        assertEquals( null, DimensionalObjectUtils.getPrettyFilter( "uid" ) );
    }
}
