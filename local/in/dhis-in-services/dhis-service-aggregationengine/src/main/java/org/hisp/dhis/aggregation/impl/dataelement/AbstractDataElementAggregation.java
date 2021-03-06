package org.hisp.dhis.aggregation.impl.dataelement;

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

import org.hisp.dhis.aggregation.AggregationStore;
import org.hisp.dhis.aggregation.impl.cache.AggregationCache;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.organisationunit.OrganisationUnit;

/**
 * @author Lars Helge Overland
 * @version $Id: AbstractDataElementAggregation.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public abstract class AbstractDataElementAggregation
{
    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    protected final String TRUE = "true";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    protected AggregationCache aggregationCache;

    public void setAggregationCache( AggregationCache aggregationCache )
    {
        this.aggregationCache = aggregationCache;
    }

    protected AggregationStore aggregationStore;

    public void setAggregationStore( AggregationStore aggregationStore )
    {
        this.aggregationStore = aggregationStore;
    }

    // -------------------------------------------------------------------------
    // Abstract aggregation class
    // -------------------------------------------------------------------------
    
    /**
     * Main method for aggregation.
     * 
     * @param dataElement the dataelement
     * @param optionCombo the CategoryOptionCombo
     * @param aggregationStartDate Start date
     * @param aggregationEndDate End date
     * @param organisationUnit The organisationUnit
     */
    public abstract Double getAggregatedValue( DataElement dataElement, DataElementCategoryOptionCombo optionCombo, Date startDate, Date endDate, OrganisationUnit organisationUnit );
    
    /**
     * Retrieves the datavalues registered for the given dataelement 
     * (specifcally for the given optionCombo) source, hierarchy, 
     * startdate and enddate.
     * 
     * @param dataElementId the dataelement id
     * @param optionComboId the CategoryOptionCombo id
     * @param organisationUnitId The organisationunit id
     * @param hierarchy the organisation unit hierarchy
     * @return collection a datavalues
     */
    protected abstract Collection<DataValue> getDataValues( Integer dataElementId, Integer optionComboId, Integer organisationUnitId,
        Date startDate, Date endDate );    
    
    protected abstract double[] getAggregateOfValues( Collection<DataValue> values, Date startDate, Date endDate,
        Date aggregationStartDate, Date aggregationEndDate );
    
    /**
     * Iterates over the given organisation unit hierarchies and finds 1) the
     * sum of the data values and 2) the number of days within start- and
     * end-date for the periods registered with the data values.
     * 
     * @param dataElementId id of the dataelement
     * @param optionComboId id of the CategoryOptionCombo
     * @param aggregationStartDate Start date
     * @param aggregationEndDate End date
     * @param hierarchies The relevant organisation unit hierarchies
     * @param source The source
     * @return double array with sum of the data values at position 0 and number
     *         of relevant days at position 1
     */
    protected double[] getSumAndRelevantDays( DataElement dataElement, DataElementCategoryOptionCombo categoryOptionCombo, Date aggregationStartDate,
        Date aggregationEndDate, Integer organisationUnitId )
    {
        Integer categoryOptionComboId = categoryOptionCombo != null ? categoryOptionCombo.getId() : null;
        
        Collection<DataValue> dataValues = getDataValues( dataElement.getId(), categoryOptionComboId, organisationUnitId,
            aggregationStartDate, aggregationEndDate );

        return getAggregateOfValues( dataValues, aggregationStartDate, aggregationEndDate,
            aggregationStartDate, aggregationEndDate );
    }    
}
