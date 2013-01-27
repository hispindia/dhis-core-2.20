package org.hisp.dhis.analytics.data;

/*
 * Copyright (c) 2004-2012, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
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

import static org.hisp.dhis.common.IdentifiableObjectUtils.getList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.analytics.AggregationType;
import org.hisp.dhis.analytics.AnalyticsManager;
import org.hisp.dhis.analytics.DataQueryParams;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.YearlyPeriodType;
import org.hisp.dhis.system.util.ListMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AnalyticsManagerTest
    extends DhisSpringTest
{
    @Autowired
    private AnalyticsManager analyticsManager;
    
    @Test
    public void testReplaceDataPeriodsWithAggregationPeriods()
    {
        Period y2012 = createPeriod( "2012" );
                
        DataQueryParams params = new DataQueryParams();
        params.setDataElements( getList( createDataElement( 'A' ), createDataElement( 'B' ) ) );
        params.setPeriods( getList( y2012 ) );
        params.setOrganisationUnits( getList( createOrganisationUnit( 'A' ) ) );
        params.setDataPeriodType( new YearlyPeriodType() );
        params.setAggregationType( AggregationType.AVERAGE_INT_DISAGGREGATION );
        
        Map<String, Double> dataValueMap = new HashMap<String, Double>();
        dataValueMap.put( BASE_UID + "A-2012-" + BASE_UID + "A", 1d );
        dataValueMap.put( BASE_UID + "B-2012-" + BASE_UID + "A", 1d );
        
        ListMap<IdentifiableObject, IdentifiableObject> dataPeriodAggregationPeriodMap = new ListMap<IdentifiableObject, IdentifiableObject>();
        dataPeriodAggregationPeriodMap.putValue( y2012, createPeriod( "2012Q1" ) );
        dataPeriodAggregationPeriodMap.putValue( y2012, createPeriod( "2012Q2" ) );
        dataPeriodAggregationPeriodMap.putValue( y2012, createPeriod( "2012Q3" ) );
        dataPeriodAggregationPeriodMap.putValue( y2012, createPeriod( "2012Q4" ) );
        
        analyticsManager.replaceDataPeriodsWithAggregationPeriods( dataValueMap, params, dataPeriodAggregationPeriodMap );
        
        assertEquals( 8, dataValueMap.size() );
        
        assertTrue( dataValueMap.keySet().contains( BASE_UID + "A-2012Q1-" + BASE_UID + "A" ) );
        assertTrue( dataValueMap.keySet().contains( BASE_UID + "A-2012Q2-" + BASE_UID + "A" ) );
        assertTrue( dataValueMap.keySet().contains( BASE_UID + "A-2012Q3-" + BASE_UID + "A" ) );
        assertTrue( dataValueMap.keySet().contains( BASE_UID + "A-2012Q4-" + BASE_UID + "A" ) );
        assertTrue( dataValueMap.keySet().contains( BASE_UID + "B-2012Q1-" + BASE_UID + "A" ) );
        assertTrue( dataValueMap.keySet().contains( BASE_UID + "B-2012Q2-" + BASE_UID + "A" ) );
        assertTrue( dataValueMap.keySet().contains( BASE_UID + "B-2012Q3-" + BASE_UID + "A" ) );
        assertTrue( dataValueMap.keySet().contains( BASE_UID + "B-2012Q4-" + BASE_UID + "A" ) );
    }
}
