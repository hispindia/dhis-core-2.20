package org.hisp.dhis.program;

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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.programattributevalue.ProgramAttributeValue;
import org.hisp.dhis.programattributevalue.ProgramAttributeValueService;
import org.hisp.dhis.system.deletion.DeletionHandler;

/**
 * @author Quang Nguyen
 * @version $Id$
 */
public class ProgramInstanceDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramInstanceService programInstanceService;

    public void setProgramInstanceService( ProgramInstanceService programInstanceService )
    {
        this.programInstanceService = programInstanceService;
    }

    private PatientDataValueService patientDataValueService;

    public void setPatientDataValueService( PatientDataValueService patientDataValueService )
    {
        this.patientDataValueService = patientDataValueService;
    }

    public ProgramStageDataElementService programStageDEService;

    public void setProgramStageDEService( ProgramStageDataElementService programStageDEService )
    {
        this.programStageDEService = programStageDEService;
    }
    
    private ProgramAttributeValueService programAttributeValueService;

    public void setProgramAttributeValueService( ProgramAttributeValueService programAttributeValueService )
    {
        this.programAttributeValueService = programAttributeValueService;
    }
    
    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return ProgramInstance.class.getSimpleName();
    }

    @Override
    public void deletePatient( Patient patient )
    {
        Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstances( patient );

        // ---------------------------------------------------------------------
        // Delete Patient data values
        // ---------------------------------------------------------------------
        
        Set<PatientDataValue> dataValues = new HashSet<PatientDataValue>();

        for ( ProgramInstance programInstance : programInstances )
        {
            dataValues.addAll( patientDataValueService
                .getPatientDataValues( programInstance.getProgramStageInstances() ) );
        }

        if ( dataValues != null && dataValues.size() > 0 )
        {
            for ( PatientDataValue dataValue : dataValues )
            {
                patientDataValueService.deletePatientDataValue( dataValue );
            }
        }
        
        // ---------------------------------------------------------------------
        // Delete Program attribute values
        // ---------------------------------------------------------------------

        for ( ProgramInstance programInstance : programInstances )
        {
            Collection<ProgramAttributeValue> attributeValues = programAttributeValueService
                .getProgramAttributeValues( programInstance );
            
            for ( ProgramAttributeValue attributeValue : attributeValues )
            {
                programAttributeValueService.deleteProgramAttributeValue( attributeValue );
            }

        }

        // ---------------------------------------------------------------------
        // Delete Program Instances
        // ---------------------------------------------------------------------
        
        if ( programInstances != null && programInstances.size() > 0 )
        {
            for ( ProgramInstance programInstance : programInstances )
            {
                programInstanceService.deleteProgramInstance( programInstance );
            }
        }
        
    }

    @Override
    public void deleteProgram( Program program )
    {
        // ---------------------------------------------------------------------
        // Delete Patient data values
        // ---------------------------------------------------------------------

        Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstances( program );

        Set<PatientDataValue> dataValues = new HashSet<PatientDataValue>();

        for ( ProgramInstance programInstance : programInstances )
        {
            Collection<ProgramStageInstance> stageInstances = programInstance.getProgramStageInstances();
            if ( stageInstances != null && stageInstances.size() > 0 )
            {
                dataValues.addAll( patientDataValueService.getPatientDataValues( stageInstances ) );
            }
        }

        if ( dataValues != null && dataValues.size() > 0 )
        {
            for ( PatientDataValue dataValue : dataValues )
            {
                patientDataValueService.deletePatientDataValue( dataValue );
            }
        }

        // ---------------------------------------------------------------------
        // Delete Program attribute values
        // ---------------------------------------------------------------------
        
        for ( ProgramInstance programInstance : programInstances )
        {
            Collection<ProgramAttributeValue> attributeValues = programAttributeValueService
                .getProgramAttributeValues( programInstance );

            for ( ProgramAttributeValue attributeValue : attributeValues )
            {
                programAttributeValueService.deleteProgramAttributeValue( attributeValue );
            }

        }
        
        // ---------------------------------------------------------------------
        // Delete Program Instances
        // ---------------------------------------------------------------------

        if ( programInstances != null && programInstances.size() > 0 )
        {
            for ( ProgramInstance programInstance : programInstances )
            {
                programInstanceService.deleteProgramInstance( programInstance );
            }
        }
    }

}
