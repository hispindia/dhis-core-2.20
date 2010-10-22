package org.hisp.dhis.reportexcel.jchart.action;

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

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.jchart.JChart;
import org.hisp.dhis.jchart.JChartSeries;
import org.hisp.dhis.jchart.JChartSevice;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.UserStore;

import com.opensymphony.xwork2.Action;

/**
 * @author Tran Thanh Tri
 */

public class AddJChartAction
    implements Action
{
    // -------------------------------------------
    // Dependency
    // -------------------------------------------

    private JChartSevice jchartService;

    public void setJchartService( JChartSevice jchartService )
    {
        this.jchartService = jchartService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }
   
    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    // -------------------------------------------
    // Input
    // -------------------------------------------

    private String title;

    public void setTitle( String title )
    {
        this.title = title;
    }

    private String legend;

    public void setLegend( String legend )
    {
        this.legend = legend;
    }

    private String categoryType;

    public void setCategoryType( String categoryType )
    {
        this.categoryType = categoryType;
    }

    private String loadPeriodBy;

    public void setLoadPeriodBy( String loadPeriodBy )
    {
        this.loadPeriodBy = loadPeriodBy;
    }

    private String periodType;

    public void setPeriodType( String periodType )
    {
        this.periodType = periodType;
    }

    private List<String> seriesTypes;

    public void setSeriesTypes( List<String> seriesTypes )
    {
        this.seriesTypes = seriesTypes;
    }

    private List<Integer> periodIds = new ArrayList<Integer>();

    public void setPeriodIds( List<Integer> periodIds )
    {
        this.periodIds = periodIds;
    }

    private List<Integer> indicatorIds;

    public void setIndicatorIds( List<Integer> indicatorIds )
    {
        this.indicatorIds = indicatorIds;
    }

    private List<String> colors;

    public void setColors( List<String> colors )
    {
        this.colors = colors;
    }

    @Override
    public String execute()
        throws Exception
    {
        JChart jChart = new JChart();
        jChart.setTitle( title );
        jChart.setLegend( legend );   
        jChart.setLoadPeriodBy( loadPeriodBy );
        jChart.setPeriodType( periodService.getPeriodTypeByName( periodType ) );
        jChart.setCategoryType( categoryType );

        jChart.setStoredby( currentUserService.getCurrentUsername() );
        
        if ( jChart.isOrganisationUnitCategory() )
        {
            jChart.setLoadPeriodBy( JChart.LOAD_PERIOD_SELECTED );
        }

        for ( int i = 0; i < indicatorIds.size(); i++ )
        {
            JChartSeries s = new JChartSeries( colors.get( i ) );
            s.setIndicator( indicatorService.getIndicator( indicatorIds.get( i ) ) );
            s.setType( seriesTypes.get( i ) );

            jChart.addSeries( s );
        }

        if ( categoryType.equals( JChart.PERIOD_CATEGORY ) && loadPeriodBy.equals( JChart.LOAD_PERIOD_SELECTED ) )
        {
            jChart.setLoadPeriodBy( loadPeriodBy );

            for ( Integer id : periodIds )
            {
                jChart.addPeriod( periodService.getPeriod( id ) );
            }

        }

        jchartService.addJChart( jChart );

        return SUCCESS;
    }
}
