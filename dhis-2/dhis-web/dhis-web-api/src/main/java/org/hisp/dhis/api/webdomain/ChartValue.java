package org.hisp.dhis.api.webdomain;

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

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.common.DxfNamespaces;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement( localName = "dxf2", namespace = DxfNamespaces.DXF_2_0)
public class ChartValue
{
    private List<String[]> values = new ArrayList<String[]>();
    
    private List<String> periods = new ArrayList<String>();
    
    private List<String> data = new ArrayList<String>();
    
    private List<String> orgUnits = new ArrayList<String>();

    @JsonProperty( value = "v" )
    public List<String[]> getValues()
    {
        return values;
    }

    public void setValues( List<String[]> values )
    {
        this.values = values;
    }

    @JsonProperty( value = "p" )
    public List<String> getPeriods()
    {
        return periods;
    }

    public void setPeriods( List<String> periods )
    {
        this.periods = periods;
    }

    @JsonProperty( value = "d" )
    public List<String> getData()
    {
        return data;
    }

    public void setData( List<String> data )
    {
        this.data = data;
    }

    @JsonProperty( value = "o" )
    public List<String> getOrgUnits()
    {
        return orgUnits;
    }

    public void setOrgUnits( List<String> orgUnits )
    {
        this.orgUnits = orgUnits;
    }
}
