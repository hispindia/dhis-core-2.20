/*
 * Copyright (c) 2004-2009, University of Oslo
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
package org.hisp.dhis.caseentry.action.caseentry;

import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;

import com.opensymphony.xwork2.Action;

/**
 * @author Viet Nguyen
 *
 * @version $Id$
 */
public class CompleteDataEntryAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramStageInstanceService programStageInstanceService;

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }
    

    // -------------------------------------------------------------------------
    // Input / Output
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    private Integer programId;

    public void setProgramId( Integer programId )
    {
        this.programId = programId;
    }

    public Integer getProgramId()
    {
        return programId;
    }

    private Integer programStageId;

    public Integer getProgramStageId()
    {
        return programStageId;
    }

    public void setProgramStageId( Integer programStageId )
    {
        this.programStageId = programStageId;
    }
    
    public Integer programStageInstanceId;
    
    public Integer getProgramStageInstanceId()
    {
        return programStageInstanceId;
    }

    public void setProgramStageInstanceId( Integer programStageInstanceId )
    {
        this.programStageInstanceId = programStageInstanceId;
    }
    
    public String data;
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        ProgramStageInstance programStageInstance  = programStageInstanceService.getProgramStageInstance( programStageInstanceId );
        System.out.println("view after: "+programStageInstance.isCompleted());
        if( programStageInstance == null )
        {
            return SUCCESS;
        }
        
        programStageInstance.setCompleted( true );
        System.out.println("programId "+programId);
        System.out.println("programStageId "+programStageId);
        System.out.println("programStageInstanceId "+programStageInstanceId);
        System.out.println(" updateProgramStageIntance");
        
        programStageInstanceService.updateProgramStageInstance( programStageInstance );
        
        
        System.out.println("endupdateProgramStageIntance");
        ProgramStageInstance programStageInstance1  = programStageInstanceService.getProgramStageInstance( programStageInstanceId );
        System.out.println("view after: "+programStageInstance1.isCompleted());
        
        
        return SUCCESS;
    }

}
