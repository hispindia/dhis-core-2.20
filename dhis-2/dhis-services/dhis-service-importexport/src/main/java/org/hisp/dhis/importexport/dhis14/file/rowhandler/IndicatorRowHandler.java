package org.hisp.dhis.importexport.dhis14.file.rowhandler;

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

import static org.hisp.dhis.importexport.dhis14.util.Dhis14ExpressionConverter.convertExpressionFromDhis14;

import java.util.Map;

import org.amplecode.quick.BatchHandler;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.analysis.ImportAnalyser;
import org.hisp.dhis.importexport.importer.IndicatorImporter;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;

import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * @author Lars Helge Overland
 * @version $Id: IndicatorRowHandler.java 6461 2008-11-24 11:32:37Z larshelg $
 */
public class IndicatorRowHandler
    extends IndicatorImporter implements RowHandler
{
    private Map<Object, Integer> indicatorTypeMap;
    
    private Map<Object, Integer> dataElementMap;
    
    private DataElementCategoryOptionCombo categoryOptionCombo;
    
    private ImportParams params;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public IndicatorRowHandler( BatchHandler<Indicator> batchHandler, 
        ImportObjectService importObjectService,
        IndicatorService indicatorService,
        Map<Object, Integer> indicatorTypeMap, 
        Map<Object, Integer> dataElementMap,
        DataElementCategoryOptionCombo categoryOptionCombo,
        ImportParams params,
        ImportAnalyser importAnalyser )
    {
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.indicatorService = indicatorService;
        this.indicatorTypeMap = indicatorTypeMap;
        this.dataElementMap = dataElementMap;
        this.categoryOptionCombo = categoryOptionCombo;
        this.params = params;
        this.importAnalyser = importAnalyser;
    }
    
    // -------------------------------------------------------------------------
    // RowHandler implementation
    // -------------------------------------------------------------------------

    public void handleRow( Object object )
    {
        final Indicator indicator = (Indicator) object;
        
        if ( indicator.getAlternativeName() != null && indicator.getAlternativeName().trim().length() == 0 )
        {
            indicator.setAlternativeName( null );
        }
        
        if ( indicator.getCode() != null && indicator.getCode().trim().length() == 0 )
        {
            indicator.setCode( null );
        }
        
        indicator.getIndicatorType().setId( indicatorTypeMap.get( indicator.getIndicatorType().getId() ) );            
        
        indicator.setNumerator( convertExpressionFromDhis14( indicator.getNumerator(), dataElementMap, categoryOptionCombo.getId(), indicator.getName() ) );
        indicator.setDenominator( convertExpressionFromDhis14( indicator.getDenominator(), dataElementMap, categoryOptionCombo.getId(), indicator.getName() ) );
        
        importObject( indicator, params );
    }
}
