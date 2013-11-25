package org.hisp.dhis.program;

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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.common.Grid;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.message.MessageConversation;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.sms.outbound.OutboundSms;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public interface ProgramStageInstanceService
{
    String ID = ProgramStageInstanceService.class.getName();

    /**
     * Adds an {@link PatientAttribute}
     * 
     * @param patientAttribute The to PatientAttribute add.
     * 
     * @return A generated unique id of the added {@link PatientAttribute}.
     */
    int addProgramStageInstance( ProgramStageInstance programStageInstance );

    /**
     * Deletes a {@link PatientAttribute}.
     * 
     * @param patientAttribute the PatientAttribute to delete.
     */
    void deleteProgramStageInstance( ProgramStageInstance programStageInstance );

    /**
     * Updates an {@link PatientAttribute}.
     * 
     * @param patientAttribute the PatientAttribute to update.
     */
    void updateProgramStageInstance( ProgramStageInstance programStageInstance );

    /**
     * Returns a {@link PatientAttribute}.
     * 
     * @param id the id of the PatientAttribute to return.
     * 
     * @return the PatientAttribute with the given id
     */
    ProgramStageInstance getProgramStageInstance( int id );

    /**
     * Returns the {@link PatientAttribute} with the given UID.
     * 
     * @param uid the UID.
     * @return the PatientAttribute with the given UID, or null if no match.
     */
    ProgramStageInstance getProgramStageInstance( String uid );

    /**
     * Retrieve an event on a program instance and a program stage. For
     * repeatable stage, the system returns the last event
     * 
     * @param programInstance ProgramInstance
     * @param programStage ProgramStage
     * 
     * @return ProgramStageInstance
     */
    ProgramStageInstance getProgramStageInstance( ProgramInstance programInstance, ProgramStage programStage );

    /**
     * Retrieve an event list on a program instance and a program stage
     * 
     * @param programInstance ProgramInstance
     * @param programStage ProgramStage
     * 
     * @return ProgramStageInstance
     */
    Collection<ProgramStageInstance> getProgramStageInstances( ProgramInstance programInstance,
        ProgramStage programStage );

    /**
     * Retrieve an event list on program instance list with a certain status
     * 
     * @param programInstances ProgramInstance list
     * @param completed Optional flag to only get completed (<code>true</code> )
     *        or uncompleted (<code>false</code>) instances.
     * 
     * @return ProgramStageInstance list
     */
    Collection<ProgramStageInstance> getProgramStageInstances( Collection<ProgramInstance> programInstances,
        boolean completed );

    /**
     * Get statuses of events
     * 
     * @param programStageInstances ProgramStageInstance list
     * 
     * @return Map< ProgramStageInstance ID, status >
     */
    Map<Integer, Integer> statusProgramStageInstances( Collection<ProgramStageInstance> programStageInstances );

    /**
     * Get all events by patient, optionally filtering by completed.
     * 
     * @param patient Patient
     * 
     * @param completed - optional flag to only get completed (
     *        <code>true</code> ) or uncompleted (<code>false</code>) instances.
     * 
     * @return ProgramStageInstance list
     */
    List<ProgramStageInstance> getProgramStageInstances( Patient patient, Boolean completed );

    /**
     * Get an event report of program instance
     * 
     * @param programInstance ProgramInstance
     * @param format I18nFormat object
     * @param i18n I18n object
     * 
     * @return List of grids. Each grid is included all information of a event
     */
    List<Grid> getProgramStageInstancesReport( ProgramInstance programInstance, I18nFormat format, I18n i18n );

    /**
     * Remove events without any data values
     * 
     * @param programStage Empty events belong to this program stage are removed
     * @param organisationUnit Specify an orgunit where empty events belong to
     */
    void removeEmptyEvents( ProgramStage programStage, OrganisationUnit organisationUnit );

    /**
     * Create relationship between an OutboundSms with many events.
     * 
     * @param programStageInstances Event list
     * @param outboundSms OutboundSms object
     */
    void updateProgramStageInstances( Collection<Integer> programStageInstances, OutboundSms outboundSms );

    /**
     * Retrieve scheduled list of patients registered
     * 
     * @return A SchedulingProgramObject list
     */
    Collection<SchedulingProgramObject> getSendMesssageEvents();

    /**
     * Get/export statistical report of a program
     * 
     * @param program Program needs to report
     * @param orgunitIds The ids of orgunits where the events happened
     * @param after Optional date the instance should be on or after.
     * @param before Optional date the instance should be on or before.
     * @param i18n I18n object
     * @param format I18nFormat
     * 
     * @return Program report
     */
    Grid getStatisticalReport( Program program, Collection<Integer> orgunitIds, Date startDate, Date endDate,
        I18n i18n, I18nFormat format );

    /**
     * Get details of events which meets the criteria in statistical report
     * 
     * @param programStage The program stage needs to get details
     * @param orgunitIds The ids of orgunits where the events happened
     * @param after Optional date the instance should be on or after.
     * @param before Optional date the instance should be on or before.
     * @param status The status of event. There are four statuses for events,
     *        includes COMPLETED_STATUS, VISITED_STATUS, FUTURE_VISIT_STATUS,
     *        LATE_VISIT_STATUS
     * @param min
     * @param max
     */
    List<ProgramStageInstance> getStatisticalProgramStageDetailsReport( ProgramStage programStage,
        Collection<Integer> orgunitIds, Date startDate, Date endDate, int status, Integer max, Integer min );

    /**
     * Get events of a program by report date
     * 
     * @param program Program
     * @param orgunitIds The ids of orgunits where the events happened
     * @param after Optional date the instance should be on or after.
     * @param before Optional date the instance should be on or before.
     * @param completed optional flag to only get completed (<code>true</code> )
     *        or uncompleted (<code>false</code>) or all (<code>null</code>)
     *        instances.
     * 
     * @return ProgramStageInstance list
     */
    Collection<ProgramStageInstance> getProgramStageInstances( Program program, Collection<Integer> orgunitIds,
        Date startDate, Date endDate, Boolean completed );

    /**
     * Get the number of over due events of a program stage in a certain period
     * 
     * @param programStage ProgramStage
     * @param orgunitIds The ids of orgunits where the events happened
     * @param after Optional date the instance should be on or after.
     * @param before Optional date the instance should be on or before.
     * 
     * @return A number
     */
    int getOverDueEventCount( ProgramStage programStage, Collection<Integer> orgunitIds, Date startDate, Date endDate );

    /**
     * Get the number of program instances completed, includes all program stage
     * instances were completed
     * 
     * @param program Program
     * @param orgunitIds The ids of orgunits where the events happened
     * @param after Optional date the instance should be on or after.
     * @param before Optional date the instance should be on or before.
     * @param status The status of event. There are four statuses for events,
     *        includes COMPLETED_STATUS, VISITED_STATUS, FUTURE_VISIT_STATUS,
     *        LATE_VISIT_STATUS
     * @return A number
     */
    int averageNumberCompletedProgramInstance( Program program, Collection<Integer> orgunitIds, Date startDate,
        Date endDate, int status );

    /**
     * Get ids of orgunits where events happened in a period
     * 
     * @param startDate The start date for retrieving on report date
     * @param endDate The end date for retrieving on report date
     * 
     * @return The ids of orgunits
     */
    Collection<Integer> getOrganisationUnitIds( Date startDate, Date endDate );

    /**
     * Get/Export a report about the number of events of a program completed on
     * a orgunit
     * 
     * @param orgunitIds The ids of orgunits where the events happened
     * @param program The program needs for reporting
     * @param after Optional date the instance should be on or after.
     * @param before Optional date the instance should be on or before.
     * 
     * @return Grid
     */
    Grid getCompletenessProgramStageInstance( Collection<Integer> orgunits, Program program, String startDate,
        String endDate, I18n i18n );

    /**
     * Send messages as SMS defined for a program-stage
     * 
     * @param programStageInstance ProgramStageInstance
     * @param status The time to send message, send when to complete an event or
     *        send by scheduled days
     * @param format I18nFormat object
     * 
     * @return OutboundSms list
     */
    Collection<OutboundSms> sendMessages( ProgramStageInstance programStageInstance, int status, I18nFormat format );

    /**
     * Send messages defined as DHIS messages for a program-stage
     * 
     * @param programStageInstance ProgramStageInstance
     * @param status The time to send message, send when a person enrolled an
     *        program or complete a program or send by scheduled days
     * @param format I18nFormat object
     * 
     * @return MessageConversation list
     */
    Collection<MessageConversation> sendMessageConversations( ProgramStageInstance programStageInstance, int status,
        I18nFormat format );

    /**
     * Complete an event. Besides, program template messages will be send if it
     * was defined to send when to complete this program
     * 
     * @param programInstance ProgramInstance
     * @param format I18nFormat
     */
    void completeProgramStageInstance( ProgramStageInstance programStageInstance, I18nFormat format );

    /**
     * Set report date and orgunit where an event happened for the event
     * 
     * @param programStageInstance ProgramStageInstance
     * @param executionDate Report date
     * @param organisationUnit Orgunit where the event happens
     */
    void setExecutionDate( ProgramStageInstance programStageInstance, Date executionDate,
        OrganisationUnit organisationUnit );

    /**
     * For the first case of an anonymous program, the program-instance doesn't
     * exist, So system has to create a program-instance and
     * program-stage-instance. The similar thing happens for single event with
     * registration.
     * 
     * @param patient Patient
     * @param program Single event without registration
     * @param executionDate Report date of the event
     * @param organisationUnit Orgunit where the event happens
     * 
     * @return ProgramStageInstance ProgramStageInstance object
     */
    ProgramStageInstance createProgramStageInstance( Patient patient, Program program, Date executionDate,
        OrganisationUnit organisationUnit );

    Grid searchEvents( ProgramStage programStage, List<TabularEventColumn> columns,
        Collection<Integer> organisationUnits, Date startDate, Date endDate, Boolean completed, Integer min,
        Integer max, I18n i18n );

    int searchEventsCount( ProgramStage programStage, List<TabularEventColumn> columns,
        Collection<Integer> organisationUnits, Boolean completed, Date startDate, Date endDate );
}
