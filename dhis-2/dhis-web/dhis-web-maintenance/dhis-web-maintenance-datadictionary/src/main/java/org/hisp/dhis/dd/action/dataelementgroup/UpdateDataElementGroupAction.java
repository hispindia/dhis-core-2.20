package org.hisp.dhis.dd.action.dataelementgroup;

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

import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;

import com.opensymphony.xwork2.Action;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: UpdateDataElementGroupAction.java 2869 2007-02-20 14:26:09Z
 *          andegje $
 */
public class UpdateDataElementGroupAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private Set<String> groupMembers = new HashSet<String>();

    public void setGroupMembers( Set<String> groupMembers )
    {
        this.groupMembers = groupMembers;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private DataElementGroup dataElementGroup;

    public DataElementGroup getDataElementGroup()
    {
        return dataElementGroup;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        dataElementGroup = dataElementService.getDataElementGroup( id );

        if ( name != null && name.trim().length() > 0 )
        {
            dataElementGroup.setName( name );
        }

        if ( !groupMembers.isEmpty() )
        {
            Set<DataElement> members = new HashSet<DataElement>();

            for ( String id : groupMembers )
            {
                members.add( dataElementService.getDataElement( Integer.parseInt( id ) ) );
            }

            dataElementGroup.updateDataElements( members );
        }

        dataElementService.updateDataElementGroup( dataElementGroup );

        return SUCCESS;
    }
}
