package org.hisp.dhis.importexport.action.exp;

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

import static org.hisp.dhis.datamart.DataMartInternalProcess.PROCESS_TYPE;
import static org.hisp.dhis.system.util.DateUtils.getMediumDate;
import static org.hisp.dhis.util.InternalProcessUtil.PROCESS_KEY_EXPORT;
import static org.hisp.dhis.util.InternalProcessUtil.setCurrentRunningProcess;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.amplecode.cave.process.ProcessCoordinator;
import org.amplecode.cave.process.ProcessExecutor;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.datamart.DataMartExport;
import org.hisp.dhis.datamart.DataMartInternalProcess;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.oust.manager.SelectionTreeManager;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork2.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ExportDataMartAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProcessCoordinator processCoordinator;

    public void setProcessCoordinator( ProcessCoordinator processCoordinator )
    {
        this.processCoordinator = processCoordinator;
    }
    
    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }
    
    private SelectionTreeManager selectionTreeManager;

    public void setSelectionTreeManager( SelectionTreeManager selectionTreeManager )
    {
        this.selectionTreeManager = selectionTreeManager;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Collection<String> selectedDataSets;

    public void setSelectedDataSets( Collection<String> selectedDataSets )
    {
        this.selectedDataSets = selectedDataSets;
    }

    private String startDate;

    public void setStartDate( String startDate )
    {
        this.startDate = startDate;
    }
    
    private String endDate;

    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
    }

    private int dataSourceLevel;

    public void setDataSourceLevel( int dataSourceLevel )
    {
        this.dataSourceLevel = dataSourceLevel;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    // TODO CompleteDataSetRegistrations
    // TODO intersecting periods?
    
    public String execute()
    {
        DataMartExport export = new DataMartExport();

        // ---------------------------------------------------------------------
        // Get DataElements
        // ---------------------------------------------------------------------

        if ( selectedDataSets != null )
        {
            Set<DataElement> distinctDataElements = new HashSet<DataElement>();
            
            for ( String dataSetId : selectedDataSets )
            {
                DataSet dataSet = dataSetService.getDataSet( Integer.parseInt( dataSetId ) );
                
                distinctDataElements.addAll( dataSet.getDataElements() );
            }
            
            export.setDataElements( distinctDataElements );
        }
        
        // ---------------------------------------------------------------------
        // Get Periods
        // ---------------------------------------------------------------------

        if ( startDate != null && startDate.trim().length() > 0 && endDate != null && endDate.trim().length() > 0 )
        {
            Date selectedStartDate = getMediumDate( startDate );
        
            Date selectedEndDate = getMediumDate( endDate );
        
            export.getPeriods().addAll( periodService.getPeriodsBetweenDates( selectedStartDate, selectedEndDate ) );
        }
        
        // ---------------------------------------------------------------------
        // Get OrganisationUnit
        // ---------------------------------------------------------------------
        
        Collection<OrganisationUnit> selectedUnits = selectionTreeManager.getSelectedOrganisationUnits();
        
        if ( selectedUnits != null )
        {
            for ( OrganisationUnit unit : selectedUnits )
            {
                export.getOrganisationUnits().addAll( organisationUnitService.getOrganisationUnitsAtLevel( dataSourceLevel, unit ) );
            }
        }

        // ---------------------------------------------------------------------
        // Start DataMartInternalProcess
        // ---------------------------------------------------------------------
        
        String owner = currentUserService.getCurrentUsername();
        
        ProcessExecutor executor = processCoordinator.newProcess( PROCESS_TYPE, owner );
        
        DataMartInternalProcess process = (DataMartInternalProcess) executor.getProcess();
        
        process.setProperties( export );
        
        processCoordinator.requestProcessExecution( executor );
        
        setCurrentRunningProcess( PROCESS_KEY_EXPORT, executor.getId() );
        
        return SUCCESS;
    }
}
