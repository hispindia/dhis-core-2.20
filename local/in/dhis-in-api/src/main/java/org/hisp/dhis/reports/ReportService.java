package org.hisp.dhis.reports;

/*
 * Copyright (c) 2004-2005, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in element and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of element code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may
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

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.program.Program;

public interface ReportService
{
    String ID = ReportService.class.getName();

    // -------------------------------------------------------------------------
    // Report_in
    // -------------------------------------------------------------------------

    int addReport( Report_in report );

    void updateReport( Report_in report );

    void deleteReport( Report_in report );

    Report_in getReport( int id );

    Report_in getReportByName( String name );

    Collection<Report_in> getReportBySource( OrganisationUnit source );

    Collection<Report_in> getAllReports();

    Collection<Report_in> getReportsByReportType( String reportType );

    Collection<Report_in> getReportsByPeriodType( PeriodType periodType );

    Collection<Report_in> getReportsByPeriodAndReportType( PeriodType periodType, String reportType );

    Collection<Report_in> getReportsByPeriodSourceAndReportType( PeriodType periodType, OrganisationUnit source, String reportType );
	
    // get Patients List ByOrgUnit
    
    //Collection<Patient> getPatientByOrgUnit( OrganisationUnit organisationUnit );
    
    // get Programs List ByOrgUnit
    Collection<Program> getProgramsByOrgUnit( OrganisationUnit organisationUnit );

    // get Patients List By OrgUnit and Program
    //Collection<Patient> getPatientByOrgUnitAndProgram( OrganisationUnit organisationUnit, Program program );
	
    // -------------------------------------------------------------------------
    // Report_in Design
    // -------------------------------------------------------------------------

    Collection<Report_inDesign> getReportDesign( Report_in report );

    // -------------------------------------------------------------------------
    // 
    // -------------------------------------------------------------------------

    List<Calendar> getStartingEndingPeriods( String deType, Date startDate, Date endDate );

    String getRAFolderName();

    List<Integer> getLinelistingRecordNos( OrganisationUnit organisationUnit, Period period, String lltype );

    // -------------------------------------------------------------------------
    // ReportService Design 
    // -------------------------------------------------------------------------

    List<Period> getMonthlyPeriods( Date start, Date end );

    PeriodType getPeriodTypeObject( String periodTypeName );

    Period getPeriodByMonth( int month, int year, PeriodType periodType );

    List<OrganisationUnit> getAllChildren( OrganisationUnit selecteOU );

    PeriodType getDataElementPeriodType( DataElement de );
    
    // -------------------------------------------------------------------------
    // ReportService for Report Result Action
    // -------------------------------------------------------------------------
    
    List<Report_inDesign> getReportDesign( String fileName );
    
    
    List<Report_inDesign> getReportDesignForGlobalSetting( String fileName );
    
    List<Report_inDesign> getReportDesignForTracker( String fileName );
    
    List<Report_inDesign> getReportDesignForHeader( String fileName );
    
    

    List<Report_inDesign> getDistrictFeedbackReportDesign( String fileName );
    
    String getResultDataValue( String formula, Date startDate, Date endDate, OrganisationUnit organisationUnit , String reportModelTB );
    
    String getIndividualResultDataValue( String formula, Date startDate, Date endDate, OrganisationUnit organisationUnit, String reportModelTB );
    
    String getResultIndicatorValue( String formula, Date startDate, Date endDate, OrganisationUnit organisationUnit );
    
    String getIndividualResultIndicatorValue( String formula, Date startDate, Date endDate, OrganisationUnit organisationUnit );
    
    String getBooleanDataValue( String formula, Date startDate, Date endDate, OrganisationUnit organisationUnit , String reportModelTB );
    
    List<Calendar> getStartingEndingPeriods( String deType , Period selectedPeriod );
    
    Period getPreviousPeriod( Period selectedPeriod );
    
    String getResultSurveyValue( String formula, OrganisationUnit organisationUnit );

    String getSurveyDesc( String formula );

    String getResultDataValueFromAggregateTable( String formula, Collection<Integer> periodIds, OrganisationUnit organisationUnit , String reportModelTB );

    String getAggCountForTextData( String formula, Date startDate, Date endDate, OrganisationUnit organisationUnit );
    
    String getCountForTextData( String formula, Date startDate, Date endDate, OrganisationUnit organisationUnit );

    String getDataelementIds( List<Report_inDesign> reportDesignList );
    
    String getAggVal( String expression, Map<String, String> aggDeMap );

    double getIndividualIndicatorValue( Indicator indicator, OrganisationUnit orgunit, Date startDate, Date endDate );
    
    Map<Integer, Integer> getOrgunitLevelMap( );
    
    String getDataelementIdsAsString( List<Indicator> indicatorList );

    String getResultDataValueFromAggregateTable( String formula, String periodIdsByComma, Integer orgunitId );
    
    Map<String, String> getResultDataValueFromAggregateTable( Integer orgunitId, String dataElmentIdsByComma, String periodIdsByComma );
    
    Map<String, String> getAggDataFromDataValueTable( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodsByComma );

    Map<String, String> getResultDataValueFromAggregateTable( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    Map<String, List<String>> getIndicatorDataValueFromAggregateTable( Integer orgunitId, String indicatorIdsByComma, Integer periodId );
    
    Map<String, String> getResultDataValueFromAggregateTableByPeriodAgg( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );

    Map<String, String> getAggDataFromDataValueTableByDeAndPeriodwise( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    Map<String, String> getAggDataFromDataValueTableByDeAndOrgUnitwise( String orgUnitIdsByComma, String dataElmentIdsByComma, String startDate, String endDate );
    
    Map<String, String> getAggDataFromDataValueTableByDeAndOrgUnitwise( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    Map<String, String> getCapturedDataFromDataValueTableByDeAndOrgUnitwise( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    Map<String, String> getDataFromDataValueTable( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    Map<String, String> getDataFromDataValueTableByPeriodAgg( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    List<Report_inDesign> getReportDesignWithMergeCells( String fileName );
    
    //Map<String, String> getLLDeathDataFromLLDataValueTable( Integer orgunitId, String dataElmentIdsForLLDeathByComma, String periodIdsByComma , Integer recordNo );
    Map<String, String> getLLDeathDataFromLLDataValueTable( Integer orgunitId, String dataElmentIdsForLLDeathByComma, String periodIdsByComma , String recordNoByComma );
    
    Map<String, String> getAggDataFromDataValueTableForOrgUnitWise( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    Map<String, String> getAggDataFromAggDataValueTableForOrgUnitWise( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    Map<String, String> getResultDataValueFromDataValueTable( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    Map<String, String> getAggDataFromAggDataValueTable( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    Integer getOrgunitCountByOrgunitGroup( String orgunitGroupIdsByComma, Integer orgUnitId );
    
    Integer getReportingOrgunitCountByDataset( Integer dataSetId, Integer orgUnitId, Integer periodId );
    
    Integer getReportingOrgunitCountByDataset( Integer dataSetId, Integer orgUnitId );
    
    String getDataelementIdsByStype( List<Report_inDesign> reportDesignList, String sType );
    
    Map<String, String> getAggNonNumberDataFromDataValueTable( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    //
    String getResultDataValueForOrgUnitGroupMember( String formula, String childOrgUnitsByComma ,Date startDate, Date endDate ,String reportModelTB );
    
    Integer getDataCountFromDataValueTable( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    Map<String, String> getBatchDataFromDataValueTable( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    List<Report_inDesign> getHeaderInfo( String fileName );
    
    String getTextDataFromDataValueTable( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
	
    Map<String, String> getResultDataFromDataValueTable( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    // Get Data value for Latest Period
    
    DataValue getLatestDataValue( DataElement dataElement, DataElementCategoryOptionCombo optionCombo, OrganisationUnit organisationUnit );
    
    // Methods for Lock Exception
    void deleteLockException( String orgUnitIdsByComma, String periodIdsByComma, String dataSetIdsByComma );
    void createBatchLockExceptions( String insertQuery );
    Boolean getLockException( Integer organisationUnitId, Integer periodId, Integer dataSetId );
    void deleteLockException( DataSet dataSet, Period period, OrganisationUnit organisationUnit );
    
    
    // methods for getting Data from data value for GOI Monthly Report
    Map<String, String> getDataFromDataValueTableForGoiMonthly( String orgUnitIdsByComma, String dataElmentIdsByComma, String periodIdsByComma );
    
    List<Integer> getDataElementIds(List<Report_inDesign> liat );
    
    Collection<Report_in> getAllSchedulableReports();

    Collection<Report_in> getAllScheduledReports();

    Collection<Report_in> getAllNonSchedulableRports();

    Collection<Report_in> getAllNonScheduledRports();

    Collection<Report_in> getAllSchedulabledEmailableReports();

    Collection<Report_in> getAllSchedulabledNonEmailableReports();
    
    // New Line Listing Report Related methods
    
    Set<Integer> getProgramStageInstanceIds( Integer programId, Integer programStageId, Integer organisationUnitId, Period period );
    Map<String, String> getTrackedEntityDataValue( Integer programId, Integer programStageId, String orgUnitIdsByComma, String dataElementIdsByComma, Period period );
    
    
}
