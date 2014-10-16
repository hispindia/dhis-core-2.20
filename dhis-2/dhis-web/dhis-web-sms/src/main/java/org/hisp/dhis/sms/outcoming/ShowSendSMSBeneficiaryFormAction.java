package org.hisp.dhis.sms.outcoming;

/*
 * Copyright (c) 2004-2014, University of Oslo
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
import java.util.Map;

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.sms.outbound.OutboundSmsTransportService;
import org.hisp.dhis.trackedentity.TrackedEntityAttribute;
import org.hisp.dhis.trackedentity.TrackedEntityAttributeService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;

/**
 * @author Abyot Asalefew Gizaw
 * @version $Id$
 */
public class ShowSendSMSBeneficiaryFormAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    @Autowired
    private OutboundSmsTransportService transportService;

    @Autowired
    private OrganisationUnitSelectionManager selectionManager;

    @Autowired
    private TrackedEntityAttributeService patientAttributeService;

    @Autowired
    private ProgramService programService;

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private Collection<TrackedEntityAttribute> patientAttributes;

    public Collection<TrackedEntityAttribute> getPatientAttributes()
    {
        return patientAttributes;
    }

    private Collection<Program> programs;

    public Collection<Program> getPrograms()
    {
        return programs;
    }

    private OrganisationUnit organisationUnit;

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    private int status;

    public int getStatus()
    {
        return status;
    }

    public Map<String, String> getGatewayMap()
    {
        return transportService.getGatewayMap();
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        patientAttributes = patientAttributeService.getAllTrackedEntityAttributes();

        programs = programService.getProgramsByCurrentUser();

        organisationUnit = selectionManager.getSelectedOrganisationUnit();

        if ( organisationUnit == null )
        {
            status = 1;
        }
        
        return SUCCESS;
    }
}
