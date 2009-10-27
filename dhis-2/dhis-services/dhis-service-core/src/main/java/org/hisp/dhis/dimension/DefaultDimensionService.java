package org.hisp.dhis.dimension;

/*
 * Copyright (c) 2004-2007, University of Oslo
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

import java.util.ArrayList;
import java.util.Collection;

import org.hisp.dhis.common.Dimension;
import org.hisp.dhis.common.DimensionOption;
import org.hisp.dhis.common.DimensionSet;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;

/**
 * @author Lars Helge Overland
 * @version $Id: Indicator.java 5540 2008-08-19 10:47:07Z larshelg $
 */
public class DefaultDimensionService
    implements DimensionService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private IndicatorService indicatorService;
    
    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private DataElementCategoryService categoryService;

    public void setCategoryService( DataElementCategoryService categoryService )
    {
        this.categoryService = categoryService;
    }

    // -------------------------------------------------------------------------
    // DimensionService implementation
    // -------------------------------------------------------------------------

    public Collection<DimensionSet> getAllDimensionSets()
    {
        Collection<DimensionSet> dimensionSets = new ArrayList<DimensionSet>();
        
        for ( DataElement dataElement : dataElementService.getAllDataElements() )
        {
            if ( dataElement.isDimensionSet() )
            {
                dimensionSets.add( dataElement );
            }
        }
        
        for ( Indicator indicator : indicatorService.getAllIndicators() )
        {
            if ( indicator.isDimensionSet() )
            {
                dimensionSets.add( indicator );
            }
        }
        
        dimensionSets.addAll( categoryService.getAllDataElementCategoryCombos() );
        
        return dimensionSets;
    }
    
    public Collection<Dimension> getAllDimensions()
    {
        Collection<Dimension> dimensions = new ArrayList<Dimension>();
        
        dimensions.addAll( dataElementService.getAllDataElementGroupSets() );
        dimensions.addAll( indicatorService.getAllIndicatorGroupSets() );
        dimensions.addAll( categoryService.getAllDataElementCategories() );
        
        return dimensions;
    }
    
    public Collection<DimensionOption> getAllDimensionOptions()
    {
        Collection<DimensionOption> dimensionOptions = new ArrayList<DimensionOption>();
        
        dimensionOptions.addAll( dataElementService.getAllDataElementGroups() );
        dimensionOptions.addAll( indicatorService.getAllIndicatorGroups() );
        dimensionOptions.addAll( categoryService.getAllDataElementCategoryOptions() );
        
        return dimensionOptions;
    }
}
