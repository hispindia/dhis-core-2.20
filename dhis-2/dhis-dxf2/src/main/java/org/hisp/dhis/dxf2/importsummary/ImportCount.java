package org.hisp.dhis.dxf2.importsummary;

/*
 * Copyright (c) 2011, University of Oslo
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement( localName = "count" )
public class ImportCount
{
    private String object;

    private int imports;

    private int updates;

    private int ignores;

    public ImportCount( String object )
    {
        this.object = object;
    }

    public ImportCount( String object, int imports, int updates, int ignores )
    {
        this.object = object;
        this.imports = imports;
        this.updates = updates;
        this.ignores = ignores;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getObject()
    {
        return object;
    }

    public void setObject( String object )
    {
        this.object = object;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public int getImports()
    {
        return imports;
    }

    public void setImports( int imports )
    {
        this.imports = imports;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public int getUpdates()
    {
        return updates;
    }

    public void setUpdates( int updates )
    {
        this.updates = updates;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public int getIgnores()
    {
        return ignores;
    }

    public void setIgnores( int ignores )
    {
        this.ignores = ignores;
    }

    @Override
    public String toString()
    {
        return "[object='" + object + "'" +
            ", imports=" + imports +
            ", updates=" + updates +
            ", ignores=" + ignores + "]";
    }
}
