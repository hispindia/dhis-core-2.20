package org.hisp.dhis.mapping.action;

/*
 * Copyright (c) 2004-2010, University of Oslo
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

import com.opensymphony.xwork2.Action;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hisp.dhis.mapping.MapLegendSet;
import org.hisp.dhis.mapping.MappingService;

/**
 * @author Jan Henrik Overland
 * @version $Id$
 */
public class GetMapLegendSetsByTypeAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private MappingService mappingService;

    public void setMappingService( MappingService mappingService )
    {
        this.mappingService = mappingService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String type;

    public void setType( String type )
    {
        this.type = type;
    }

    private String symbolizer;

    public void setSymbolizer( String symbolizer )
    {
        this.symbolizer = symbolizer;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<MapLegendSet> object;

    public List<MapLegendSet> getObject()
    {
        return this.object;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        object = new ArrayList<MapLegendSet>( mappingService.getMapLegendSetsByType( type ) );

        if ( symbolizer != null )
        {
            Collection<MapLegendSet> remove = new ArrayList<MapLegendSet>();

            for ( MapLegendSet legendSet : object )
            {
                if ( legendSet.getSymbolizer() != null )
                {
                    if ( (symbolizer.equals( MappingService.MAP_LEGEND_SYMBOLIZER_COLOR ) && legendSet.getSymbolizer()
                        .equals( MappingService.MAP_LEGEND_SYMBOLIZER_IMAGE ))
                        || (symbolizer.equals( MappingService.MAP_LEGEND_SYMBOLIZER_IMAGE ) && legendSet
                            .getSymbolizer().equals( MappingService.MAP_LEGEND_SYMBOLIZER_COLOR )) )
                    {
                        remove.add( legendSet );
                    }
                }

                else
                {
                    if ( symbolizer.equals( MappingService.MAP_LEGEND_SYMBOLIZER_IMAGE ) )
                    {
                        remove.add( legendSet );
                    }
                }
            }

            object.removeAll( remove );
        }

        return SUCCESS;
    }
}