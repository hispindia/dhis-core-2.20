package org.hisp.dhis.aggregation.impl.cache;

/*
 * Copyright (c) 2004-2013, University of Oslo
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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitHierarchy;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.system.util.ConversionUtils;

/**
 * @author Lars Helge Overland
 * @version $Id: MemoryAggregationCache.java 5280 2008-05-28 10:10:29Z larshelg $
 */
public class MemoryAggregationCache
    implements AggregationCache
{
    private OrganisationUnitHierarchy hierarchyCache = null;    
    private Map<String, Period> periodCache = new HashMap<String, Period>();    
    private Map<String, Collection<Integer>> intersectingPeriodsCache = new HashMap<String, Collection<Integer>>();
    
    private static final String SEPARATOR = "-";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }
    
    private AggregationService aggregationService;

    public void setAggregationService( AggregationService aggregationService )
    {
        this.aggregationService = aggregationService;
    }

    // -------------------------------------------------------------------------
    // AggregationCache implementation
    // -------------------------------------------------------------------------

    public OrganisationUnitHierarchy getOrganisationUnitHierarchy()
    {
        if ( hierarchyCache != null )
        {
            return hierarchyCache;
        }
        
        hierarchyCache = organisationUnitService.getOrganisationUnitHierarchy();
        
        return hierarchyCache;
    }
    
    public Period getPeriod( int periodId )
    {
        String key = String.valueOf( periodId );
        
        Period period = periodCache.get( key );
        
        if ( period != null )
        {
            return period;
        }
        
        period = periodService.getPeriod( periodId );
        
        periodCache.put( key, period );
        
        return period;
    }
    
    public Collection<Integer> getIntersectingPeriodIds( Date startDate, Date endDate )
    {
        String key = startDate.toString() + SEPARATOR + endDate.toString();
        
        Collection<Integer> periodIds = intersectingPeriodsCache.get( key );
        
        if ( periodIds != null )
        {
            return periodIds;
        }
        
        periodIds = ConversionUtils.getIdentifiers( Period.class, periodService.getIntersectingPeriods( startDate, endDate ) );
                
        intersectingPeriodsCache.put( key, periodIds );
        
        return periodIds;
    }    
    
    public Double getAggregatedDataValue( DataElement dataElement, DataElementCategoryOptionCombo optionCombo, Date startDate, Date endDate, OrganisationUnit organisationUnit )
    {
        return aggregationService.getAggregatedDataValue( dataElement, optionCombo, startDate, endDate, organisationUnit );
    }

    public void clearCache()
    {
        hierarchyCache = null;
        periodCache.clear();
        intersectingPeriodsCache.clear();
    }
}
