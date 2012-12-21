package org.hisp.dhis.analytics;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.common.Dxf2Namespace;
import org.hisp.dhis.system.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement( localName = "dxf2", namespace = Dxf2Namespace.NAMESPACE )
public class DataQueryParams
{
    public static final String INDICATOR_DIM_ID = "in";
    public static final String DATAELEMENT_DIM_ID = "de";
    public static final String CATEGORYOPTIONCOMBO_DIM_ID = "coc";
    public static final String PERIOD_DIM_ID = "pe";
    public static final String ORGUNIT_DIM_ID = "ou";
    public static final String VALUE_ID = "value";
    
    private static final String LEVEL_PREFIX = "uidlevel";
    
    private Map<String, List<String>> dimensions = new HashMap<String, List<String>>();
    
    private boolean categories = false;

    private Map<String, List<String>> filters = new HashMap<String, List<String>>();
    
    // -------------------------------------------------------------------------
    // Transient properties
    // -------------------------------------------------------------------------
    
    private transient String tableName;

    private transient String periodType;
    
    private transient int organisationUnitLevel;
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
  
    public DataQueryParams()
    {
    }
    
    public DataQueryParams( Map<String, List<String>> dimensions, boolean categories, Map<String, List<String>> filters )
    {
        this.dimensions = dimensions;
        this.categories = categories;
        this.filters = filters;
    }
    
    public DataQueryParams( DataQueryParams params )
    {
        this.dimensions = new HashMap<String, List<String>>( params.getDimensions() );
        this.categories = params.isCategories();
        this.filters = new HashMap<String, List<String>>( params.getFilters() );
        
        this.tableName = params.getTableName();
        this.periodType = params.getPeriodType();
        this.organisationUnitLevel = params.getOrganisationUnitLevel();
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public List<String> getDimensionNames()
    {
        List<String> list = new ArrayList<String>();

        list.addAll( dimensions.keySet() );
        
        if ( categories )
        {
            list.add( CATEGORYOPTIONCOMBO_DIM_ID );
        }
        
        if ( list.contains( PERIOD_DIM_ID ) && periodType != null )
        {
            list.set( list.indexOf( PERIOD_DIM_ID ), periodType );
        }
        
        if ( list.contains( ORGUNIT_DIM_ID ) && organisationUnitLevel != 0 )
        {
            list.set( list.indexOf( ORGUNIT_DIM_ID ), LEVEL_PREFIX + organisationUnitLevel );
        }
                
        return list;
    }
        
    public Map<String, List<String>> getDimensionMap()
    {
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        map.putAll( dimensions );
        
        if ( periodType != null )
        {
            map.put( periodType, dimensions.get( PERIOD_DIM_ID ) );
        }
        
        if ( organisationUnitLevel != 0 )
        {
            map.put( LEVEL_PREFIX + organisationUnitLevel, dimensions.get( ORGUNIT_DIM_ID ) );
        }
        
        return map;
    }
    
    public void setDimension( String dimension, List<String> values )
    {
        dimensions.put( dimension, values );
    }
    
    public Collection<String> dimensionsAsFilters()
    {
        return CollectionUtils.intersection( dimensions.keySet(), filters.keySet() );
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( categories ? 1231 : 1237);
        result = prime * result + ( ( dimensions == null ) ? 0 : dimensions.hashCode() );
        result = prime * result + ( ( filters == null ) ? 0 : filters.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        
        if ( object == null )
        {
            return false;
        }
        
        if ( getClass() != object.getClass() )
        {
            return false;
        }
        
        DataQueryParams other = (DataQueryParams) object;
        
        if ( dimensions == null )
        {
            if ( other.dimensions != null )
            {
                return false;
            }
        }
        else if ( !dimensions.equals( other.dimensions ) )
        {
            return false;
        }
        
        if ( categories != other.categories )
        {
            return false;
        }

        if ( filters == null )
        {
            if ( other.filters != null )
            {
                return false;
            }
        }
        else if ( !filters.equals( other.filters ) )
        {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString()
    {
        return dimensions != null ? dimensions.toString() : "";
    }
        
    // -------------------------------------------------------------------------
    // Get and set methods for serialize properties
    // -------------------------------------------------------------------------
  
    @JsonProperty( value = "dimensions" )
    public Map<String, List<String>> getDimensions()
    {
        return dimensions;
    }

    public void setDimensions( Map<String, List<String>> dimensions )
    {
        this.dimensions = dimensions;
    }

    @JsonProperty( value = "categories" )
    public boolean isCategories()
    {
        return categories;
    }

    public void setCategories( boolean categories )
    {
        this.categories = categories;
    }

    @JsonProperty( value = "filters" )
    public Map<String, List<String>> getFilters()
    {
        return filters;
    }

    public void setFilters( Map<String, List<String>> filters )
    {
        this.filters = filters;
    }

    // -------------------------------------------------------------------------
    // Get and set methods for transient properties
    // -------------------------------------------------------------------------
  
    public String getTableName()
    {
        return tableName;
    }

    public void setTableName( String tableName )
    {
        this.tableName = tableName;
    }

    public String getPeriodType()
    {
        return periodType;
    }

    public void setPeriodType( String periodType )
    {
        this.periodType = periodType;
    }

    public int getOrganisationUnitLevel()
    {
        return organisationUnitLevel;
    }

    public void setOrganisationUnitLevel( int organisationUnitLevel )
    {
        this.organisationUnitLevel = organisationUnitLevel;
    }
    
    // -------------------------------------------------------------------------
    // Get and set helpers
    // -------------------------------------------------------------------------
  
    public List<String> getDatElements()
    {
        return dimensions.get( DATAELEMENT_DIM_ID );
    }
    
    public void setDataElements( List<String> dataElements )
    {
        dimensions.put( DATAELEMENT_DIM_ID, dataElements );
    }
    
    public List<String> getPeriods()
    {
        return dimensions.get( PERIOD_DIM_ID );
    }
    
    public void setPeriods( List<String> periods )
    {
        dimensions.put( PERIOD_DIM_ID, periods );
    }

    public List<String> getOrganisationUnits()
    {
        return dimensions.get( ORGUNIT_DIM_ID );
    }
    
    public void setOrganisationUnits( List<String> organisationUnits )
    {
        this.dimensions.put( ORGUNIT_DIM_ID, organisationUnits );
    }
}
