package org.hisp.dhis.dataadmin.action.sqlview;

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

import static org.hisp.dhis.sqlview.ResourceTableNameMap.getIgnoredNameMap;

import java.util.Map;

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.sqlview.SqlViewService;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Dang Duy Hieu
 * @version $Id ValidateAddUpdateSqlViewAction.java July 07, 2010$
 */
public class ValidateAddUpdateSqlViewAction
    extends ActionSupport
{
    private static final String ADD = "add";

    private static final String SEMICOLON = ";";
    
    private static final String SEPERATE = "|";

    private static final String REGEX_SELECT_QUERY = "^(?i)\\s*select\\s{1,}.+$";

    private static final String REGEX_SELECT_INTO_QUERY = " into ";

    private static final String PREFIX_REGEX_IGNORE_TABLES_QUERY = "^(?i).+((?<=[^\\d\\w])(";

    private static final String SUFFIX_REGEX_IGNORE_TABLES_QUERY = ")(?=[^\\d\\w])).*$";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SqlViewService sqlViewService;

    public void setSqlViewService( SqlViewService sqlViewService )
    {
        this.sqlViewService = sqlViewService;
    }

    // -------------------------------------------------------------------------
    // I18n
    // -------------------------------------------------------------------------

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String mode;

    public void setMode( String mode )
    {
        this.mode = mode;
    }

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String sqlquery;

    public void setSqlquery( String sqlquery )
    {
        this.sqlquery = sqlquery;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String message;

    public String getMessage()
    {
        return message;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        message = "";

        if ( (name == null) || (name.trim() == "") )
        {
            message = i18n.getString( "name_is_null" );

            return INPUT;
        }

        if ( mode.equals( ADD ) && (sqlViewService.getSqlView( name ) != null) )
        {
            message = i18n.getString( "name_in_used" );

            return INPUT;
        }

        if ( (sqlquery == null) || (sqlquery.trim() == "") )
        {
            message = i18n.getString( "sqlquery_is_empty" );

            return INPUT;
        }

        final String ignoredRegex = this.setUpIgnoredRegex();
        
        sqlquery = sqlViewService.makeUpForQueryStatement( sqlquery );

        for ( String s : sqlquery.split( SEMICOLON ) )
        {
            String tmp = new String( s.toLowerCase() );

            if ( !s.matches( REGEX_SELECT_QUERY ) || tmp.contains( REGEX_SELECT_INTO_QUERY ) )
            {
                message = i18n.getString( "sqlquery_is_invalid" ) + "<br/>" + i18n.getString( "sqlquery_is_welformed" );

                return INPUT;
            }

            if ( tmp.matches( ignoredRegex ) )
            {
                message = i18n.getString( "sqlquery_is_not_allowed" );

                return INPUT;
            }
        }

        message = sqlViewService.testSqlGrammar( sqlquery );

        if ( !message.equals( "" ) )
        {
            return INPUT;
        }

        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private String setUpIgnoredRegex()
    {   
        int i = 0;
        int len = getIgnoredNameMap().size();
        
        StringBuffer ignoredRegex = new StringBuffer( PREFIX_REGEX_IGNORE_TABLES_QUERY );

        for ( Map.Entry<String, String> entry : getIgnoredNameMap().entrySet() )
        {            
            ignoredRegex.append( entry.getValue() );
            
            if ( ++i < len )
            {
                ignoredRegex.append( SEPERATE );                
            }
        }

        ignoredRegex.append( SUFFIX_REGEX_IGNORE_TABLES_QUERY );

        return ignoredRegex.toString();
    }
}
