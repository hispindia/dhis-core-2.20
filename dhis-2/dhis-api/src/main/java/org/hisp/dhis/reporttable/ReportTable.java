package org.hisp.dhis.reporttable;

/*
 * Copyright (c) 2004-2012, University of Oslo
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

import static org.hisp.dhis.common.DimensionalObject.DATA_X_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.ORGUNIT_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.PERIOD_DIM_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hisp.dhis.common.BaseAnalyticalObject;
import org.hisp.dhis.common.BaseNameableObject;
import org.hisp.dhis.common.CombinationGenerator;
import org.hisp.dhis.common.DimensionalObject;
import org.hisp.dhis.common.DxfNamespaces;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.common.GridHeader;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.NameableObject;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.DimensionalView;
import org.hisp.dhis.common.view.ExportView;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.user.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * The ReportTable object represents a customizable database table. It has
 * features like crosstabulation, relative periods, parameters, and display
 * columns.
 *
 * @author Lars Helge Overland
 */
@JacksonXmlRootElement( localName = "reportTable", namespace = DxfNamespaces.DXF_2_0)
public class ReportTable
    extends BaseAnalyticalObject
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 5618655666320890565L;
    
    public static final String DATAELEMENT_ID = "dataelementid";
    public static final String CATEGORYCOMBO_ID = "categoryoptioncomboid";
    public static final String CATEGORYOPTION_ID = "categoryoptionid";

    public static final String INDICATOR_ID = "indicatorid";
    public static final String INDICATOR_UID = "indicatoruid";
    public static final String INDICATOR_NAME = "indicatorname";
    public static final String INDICATOR_CODE = "indicatorcode";
    public static final String INDICATOR_DESCRIPTION = "indicatordescription";

    public static final String DATASET_ID = "datasetid";

    public static final String PERIOD_ID = "periodid";
    public static final String PERIOD_UID = "perioduid";
    public static final String PERIOD_NAME = "periodname";
    public static final String PERIOD_CODE = "periodcode";
    public static final String PERIOD_DESCRIPTION = "perioddescription";

    public static final String ORGANISATIONUNIT_ID = "organisationunitid";
    public static final String ORGANISATIONUNIT_UID = "organisationunituid";
    public static final String ORGANISATIONUNIT_NAME = "organisationunitname";
    public static final String ORGANISATIONUNIT_CODE = "organisationunitcode";
    public static final String ORGANISATIONUNIT_DESCRIPTION = "organisationunitdescription";

    public static final String ORGANISATIONUNITGROUP_ID = "organisationunitgroupid";
    public static final String ORGANISATIONUNITGROUP_UID = "organisationunitgroupuid";
    public static final String ORGANISATIONUNITGROUP_NAME = "organisationunitgroupname";
    public static final String ORGANISATIONUNITGROUP_CODE = "organisationunitgroupcode";
    public static final String ORGANISATIONUNITGROUP_DESCRIPTION = "organisationunitgroupdescription";

    public static final String REPORTING_MONTH_COLUMN_NAME = "reporting_month_name";
    public static final String PARAM_ORGANISATIONUNIT_COLUMN_NAME = "param_organisationunit_name";
    public static final String ORGANISATION_UNIT_IS_PARENT_COLUMN_NAME = "organisation_unit_is_parent";

    public static final String SEPARATOR = "_";
    public static final String SPACE = " ";
    public static final String KEY_ORGUNIT_GROUPSET = "orgunit_groupset_";

    public static final String TOTAL_COLUMN_NAME = "total";
    public static final String TOTAL_COLUMN_PRETTY_NAME = "Total";

    public static final String DISPLAY_DENSITY_COMFORTABLE = "comfortable";
    public static final String DISPLAY_DENSITY_NORMAL = "normal";
    public static final String DISPLAY_DENSITY_COMPACT = "compact";
    
    public static final String FONT_SIZE_LARGE = "large";
    public static final String FONT_SIZE_NORMAL = "normal";
    public static final String FONT_SIZE_SMALL = "small";

    public static final String NUMBER_FORMATTING_COMMA = "comma";
    public static final String NUMBER_FORMATTING_SPACE = "space";
    public static final String NUMBER_FORMATTING_NONE = "none";
    
    public static final int ASC = -1;
    public static final int DESC = 1;
    public static final int NONE = 0;

    public static final Map<String, String> PRETTY_COLUMNS = new HashMap<String, String>()
    {
        {
            put( CATEGORYCOMBO_ID, "Category combination ID" );
            put( INDICATOR_ID, "Indicator ID" );
            put( PERIOD_ID, "Period ID" );
            put( ORGANISATIONUNIT_ID, "Organisation unit ID" );
            put( ORGANISATIONUNITGROUP_ID, "Organisation unit group ID" );
        }
    };

    public static final Map<Class<? extends NameableObject>, String> CLASS_ID_MAP = new HashMap<Class<? extends NameableObject>, String>()
    {
        {
            put( Indicator.class, INDICATOR_ID );
            put( DataElement.class, DATAELEMENT_ID );
            put( DataElementCategoryOptionCombo.class, CATEGORYCOMBO_ID );
            put( DataElementCategoryOption.class, CATEGORYOPTION_ID );
            put( DataSet.class, DATASET_ID );
            put( Period.class, PERIOD_ID );
            put( OrganisationUnit.class, ORGANISATIONUNIT_ID );
            put( OrganisationUnitGroup.class, ORGANISATIONUNITGROUP_ID );
        }
    };

    private static final String EMPTY = "";

    private static final NameableObject[] IRT = new NameableObject[0];
    private static final NameableObject[][] IRT2D = new NameableObject[0][];

    private static final String ILLEGAL_FILENAME_CHARS_REGEX = "[/\\?%*:|\"'<>.]";

    // -------------------------------------------------------------------------
    // Persisted properties
    // -------------------------------------------------------------------------

    /**
     * Indicates whether the ReportTable contains regression columns.
     */
    private boolean regression;

    /**
     * Indicates whether the ReportTable contains cumulative columns.
     */
    private boolean cumulative;

    /**
     * Dimensions to crosstabulate / use as columns.
     */
    private List<String> columnDimensions = new ArrayList<String>();
    
    /**
     * Dimensions to use as rows.
     */
    private List<String> rowDimensions = new ArrayList<String>();
    
    /**
     * Dimensions to use as filter.
     */
    private List<String> filterDimensions = new ArrayList<String>();
    
    /**
     * The ReportParams of the ReportTable.
     */
    private ReportParams reportParams;

    /**
     * The sort order if any applied to the last column of the table.
     */
    private Integer sortOrder;

    /**
     * Indicates whether the table should be limited from top by this value.
     */
    private Integer topLimit;

    /**
     * Indicates rendering of sub-totals for the table.
     */
    private boolean totals;

    /**
     * Indicates rendering of sub-totals for the table.
     */
    private boolean subtotals;

    /**
     * Indicates rendering of empty rows for the table.
     */
    private boolean hideEmptyRows;
    
    /**
     * Indicates rendering of number formatting for the table.
     */
    private String digitGroupSeparator;
    
    /**
     * The display density of the text in the table.
     */
    private String displayDensity;
    
    /**
     * The font size of the text in the table.
     */
    private String fontSize;
    
    // -------------------------------------------------------------------------
    // Transient properties
    // -------------------------------------------------------------------------

    /**
     * All crosstabulated columns.
     */
    private List<List<NameableObject>> gridColumns = new ArrayList<List<NameableObject>>();

    /**
     * All rows.
     */
    private List<List<NameableObject>> gridRows = new ArrayList<List<NameableObject>>();

    /**
     * The name of the reporting month based on the report param.
     */
    private String reportingPeriodName;

    /**
     * The parent organisation unit.
     */
    private OrganisationUnit parentOrganisationUnit;

    /**
     * The category option combos derived from the dimension set.
     */
    private List<DataElementCategoryOptionCombo> categoryOptionCombos = new ArrayList<DataElementCategoryOptionCombo>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Constructor for persistence purposes.
     */
    public ReportTable()
    {
    }

    /**
     * Default constructor.
     *
     * @param name the name.
     * @param dataElements the data elements.
     * @param indicators the indicators.
     * @param dataSets the data sets.
     * @param periods the periods. Cannot have the name property set.
     * @param relativePeriods the relative periods. These periods must have the
     *                        name property set. Not persisted.
     * @param organisationUnits the organisation units.
     * @param relativeUnits the organisation units. Not persisted.
     * @param doIndicators indicating whether indicators should be crosstabulated.
     * @param doPeriods indicating whether periods should be crosstabulated.
     * @param doUnits indicating whether organisation units should be crosstabulated.
     * @param relatives the relative periods.
     * @param i18nFormat the i18n format. Not persisted.
     */
    public ReportTable( String name, List<DataElement> dataElements, List<Indicator> indicators,
                        List<DataSet> dataSets, List<Period> periods,
                        List<OrganisationUnit> organisationUnits,
                        boolean doIndicators, boolean doPeriods, boolean doUnits, RelativePeriods relatives, ReportParams reportParams,
                        String reportingPeriodName )
    {
        this.name = name;
        this.dataElements = dataElements;
        this.indicators = indicators;
        this.dataSets = dataSets;
        this.periods = periods;
        this.organisationUnits = organisationUnits;
        this.relatives = relatives;
        this.reportParams = reportParams;
        this.reportingPeriodName = reportingPeriodName;
        
        if ( doIndicators )
        {
            columnDimensions.add( DATA_X_DIM_ID );
        }
        else
        {
            rowDimensions.add( DATA_X_DIM_ID );
        }
        
        if ( doPeriods )
        {
            columnDimensions.add( PERIOD_DIM_ID );
        }
        else
        {
            rowDimensions.add( PERIOD_DIM_ID );
        }
        
        if ( doUnits )
        {
            columnDimensions.add( ORGUNIT_DIM_ID );
        }
        else
        {
            rowDimensions.add( ORGUNIT_DIM_ID );
        }
    }

    // -------------------------------------------------------------------------
    // Init
    // -------------------------------------------------------------------------

    public void init( User user, Date date, OrganisationUnit organisationUnit, I18nFormat format )
    {
        verify( ( periods != null && !periods.isEmpty() ) || hasRelativePeriods(), "Must contain periods or relative periods" );

        this.relativePeriodDate = date;
        
        // Handle report parameters
        
        if ( hasRelativePeriods() )
        {
            this.reportingPeriodName = relatives.getReportingPeriodName( date, format );
        }

        if ( organisationUnit != null && hasReportParams() && reportParams.isParamParentOrganisationUnit() )
        {
            organisationUnit.setCurrentParent( true );
            this.parentOrganisationUnit = organisationUnit;
            addTransientOrganisationUnits( organisationUnit.getChildren() );
            addTransientOrganisationUnit( organisationUnit );
        }

        if ( organisationUnit != null && hasReportParams() && reportParams.isParamOrganisationUnit() )
        {
            this.parentOrganisationUnit = organisationUnit;
            addTransientOrganisationUnit( organisationUnit );
        }

        // Populate grid
        
        this.populateGridColumnsAndRows( date, user, format );
        
        if ( isDimensional() )
        {
            categoryOptionCombos = new ArrayList<DataElementCategoryOptionCombo>( getCategoryCombo().getOptionCombos() );
            verify( nonEmptyLists( categoryOptionCombos ) == 1, "Category option combos size must be larger than 0" );
        }

        // Allow for no columns or rows
        
        addIfEmpty( gridColumns ); 
        addIfEmpty( gridRows );
    }
    
    // -------------------------------------------------------------------------
    // Public methods
    // -------------------------------------------------------------------------
    
    public void populateGridColumnsAndRows( Date date, User user, I18nFormat format )
    {
        List<NameableObject[]> tableColumns = new ArrayList<NameableObject[]>();
        List<NameableObject[]> tableRows = new ArrayList<NameableObject[]>();
        
        for ( String dimension : columnDimensions )
        {
            tableColumns.add( getDimensionalObject( dimension, date, user, false, format ).getItems().toArray( IRT ) );
        }
        
        for ( String dimension : rowDimensions )
        {
            tableRows.add( getDimensionalObject( dimension, date, user, true, format ).getItems().toArray( IRT ) );
        }

        gridColumns = new CombinationGenerator<NameableObject>( tableColumns.toArray( IRT2D ) ).getCombinations();
        gridRows = new CombinationGenerator<NameableObject>( tableRows.toArray( IRT2D ) ).getCombinations();        
    }
    
    @Override
    public void populateAnalyticalProperties()
    {
        for ( String column : columnDimensions )
        {
            columns.addAll( getDimensionalObjectList( column ) );
        }
        
        for ( String row : rowDimensions )
        {
            rows.addAll( getDimensionalObjectList( row ) );
        }
        
        for ( String filter : filterDimensions )
        {
            filters.addAll( getDimensionalObjectList( filter ) );
        }
    }
            
    private DataElementCategoryCombo getCategoryCombo()
    {
        if ( dataElements != null && !dataElements.isEmpty() )
        {
            return dataElements.get( 0 ).getCategoryCombo();
        }
        
        return null;
    }

    /**
     * Indicates whether this ReportTable is multi-dimensional.
     */
    public boolean isDimensional()
    {
        return dataElements != null && !dataElements.isEmpty() && columnDimensions.contains( DimensionalObject.CATEGORYOPTIONCOMBO_DIM_ID );
    }

    /**
     * Generates a pretty column name based on short-names of the argument
     * objects. Null arguments are ignored in the name.
     */
    public static String getPrettyColumnName( List<NameableObject> objects )
    {
        StringBuffer buffer = new StringBuffer();

        for ( NameableObject object : objects )
        {
            buffer.append( object != null ? ( object.getShortName() + SPACE ) : EMPTY );
        }

        return buffer.length() > 0 ? buffer.substring( 0, buffer.lastIndexOf( SPACE ) ) : TOTAL_COLUMN_PRETTY_NAME;
    }

    /**
     * Generates a column name based on short-names of the argument objects.
     * Null arguments are ignored in the name.
     * 
     * The period column name must be static when on columns so it can be
     * re-used in reports, hence the name property is used which will be formatted
     * only when the period dimension is on rows.
     */
    public static String getColumnName( List<NameableObject> objects )
    {
        StringBuffer buffer = new StringBuffer();

        for ( NameableObject object : objects )
        {
            if ( object != null && object instanceof Period )
            {
                buffer.append( object.getName() + SEPARATOR );
            }
            else
            {
                buffer.append( object != null ? ( object.getShortName() + SEPARATOR ) : EMPTY );
            }
        }

        String column = columnEncode( buffer.toString() );

        return column.length() > 0 ? column.substring( 0, column.lastIndexOf( SEPARATOR ) ) : TOTAL_COLUMN_NAME;
    }

    /**
     * Generates a string which is acceptable as a filename.
     */
    public static String columnEncode( String string )
    {
        if ( string != null )
        {
            string = string.replaceAll( "<", "_lt" );
            string = string.replaceAll( ">", "_gt" );
            string = string.replaceAll( ILLEGAL_FILENAME_CHARS_REGEX, EMPTY );
            string = string.length() > 255 ? string.substring( 0, 255 ) : string;
            string = string.toLowerCase();
        }

        return string;
    }

    /**
     * Checks whether the given List of IdentifiableObjects contains an object
     * which is an OrganisationUnit and has the currentParent property set to
     * true.
     *
     * @param objects the List of IdentifiableObjects.
     */
    public static boolean isCurrentParent( List<? extends IdentifiableObject> objects )
    {
        for ( IdentifiableObject object : objects )
        {
            if ( object != null && object instanceof OrganisationUnit && ((OrganisationUnit) object).isCurrentParent() )
            {
                return true;
            }
        }

        return false;
    }
    
    /**
     * Returns null-safe sort order, none if null.
     */
    public int sortOrder()
    {
        return sortOrder != null ? sortOrder : NONE;
    }

    /**
     * Returns null-safe top limit, 0 if null;
     */
    public int topLimit()
    {
        return topLimit != null ? topLimit : 0;
    }

    /**
     * Tests whether this report table has report params.
     */
    public boolean hasReportParams()
    {
        return reportParams != null;
    }

    /**
     * Returns the name of the parent organisation unit, or an empty string if null.
     */
    public String getParentOrganisationUnitName()
    {
        return parentOrganisationUnit != null ? parentOrganisationUnit.getName() : EMPTY;
    }

    public boolean isDoIndicators()
    {
        return columnDimensions.contains( DATA_X_DIM_ID );
    }

    public void setDoIndicators( boolean doIndicators )
    {
        this.columnDimensions.remove( DATA_X_DIM_ID );
        
        if ( doIndicators )
        {
            this.columnDimensions.add( DATA_X_DIM_ID );
        }
    }
    
    public boolean isDoPeriods()
    {
        return columnDimensions.contains( PERIOD_DIM_ID );
    }

    public void setDoPeriods( boolean doPeriods )
    {
        this.columnDimensions.remove( PERIOD_DIM_ID );
        
        if ( doPeriods )
        {
            this.columnDimensions.add( PERIOD_DIM_ID );
        }
    }
    
    public boolean isDoUnits()
    {
        return columnDimensions.contains( ORGUNIT_DIM_ID );
    }
    
    public void setDoUnits( boolean doUnits )
    {
        this.columnDimensions.remove( ORGUNIT_DIM_ID );
        
        if ( doUnits )
        {
            this.columnDimensions.add( ORGUNIT_DIM_ID );
        }
    }

    /**
     * Generates a grid for this report table based on the given aggregate value
     * map.
     *
     * @param grid the grid, should be empty and not null.
     * @param valueMap the mapping of identifiers to aggregate values.
     * @param format the I18nFormat.
     * @return a grid.
     */
    public Grid getGrid( Grid grid, Map<String, Double> valueMap )
    {
        final String subtitle = StringUtils.trimToEmpty( getParentOrganisationUnitName() ) + SPACE
            + StringUtils.trimToEmpty( getReportingPeriodName() );

        grid.setTitle( getName() + " - " + subtitle );

        // ---------------------------------------------------------------------
        // Headers
        // ---------------------------------------------------------------------

        for ( String row : getRowDimensions() )
        {
            String name = StringUtils.defaultIfEmpty( DimensionalObject.PRETTY_NAMES.get( row ), row );
            
            grid.addHeader( new GridHeader( name + " ID", row + "_id", String.class.getName(), true, true ) );
            grid.addHeader( new GridHeader( name, row + "_name", String.class.getName(), false, true ) );
            grid.addHeader( new GridHeader( name + " code", row + "_code", String.class.getName(), true, true ) );
            grid.addHeader( new GridHeader( name + " description", row + "_description", String.class.getName(), true, true ) );
        }
        
        grid.addHeader( new GridHeader( "Reporting month", REPORTING_MONTH_COLUMN_NAME,
            String.class.getName(), true, true ) );
        grid.addHeader( new GridHeader( "Organisation unit parameter",
            PARAM_ORGANISATIONUNIT_COLUMN_NAME, String.class.getName(), true, true ) );
        grid.addHeader( new GridHeader( "Organisation unit is parent",
            ORGANISATION_UNIT_IS_PARENT_COLUMN_NAME, String.class.getName(), true, true ) );

        final int startColumnIndex = grid.getHeaders().size();
        final int numberOfColumns = getGridColumns().size();

        for ( List<NameableObject> column : getGridColumns() )
        {
            grid.addHeader( new GridHeader( getPrettyColumnName( column ), getColumnName( column ), Double.class
                .getName(), false, false ) );
        }

        // ---------------------------------------------------------------------
        // Values
        // ---------------------------------------------------------------------

        for ( List<NameableObject> row : getGridRows() )
        {
            grid.addRow();

            // -----------------------------------------------------------------
            // Row meta data
            // -----------------------------------------------------------------

            for ( NameableObject object : row )
            {
                grid.addValue( object.getUid() );
                grid.addValue( object.getName() );
                grid.addValue( object.getCode() );
                grid.addValue( object.getDescription() );
            }
            
            grid.addValue( getReportingPeriodName() );
            grid.addValue( getParentOrganisationUnitName() );
            grid.addValue( isCurrentParent( row ) ? "Yes" : "No" );

            // -----------------------------------------------------------------
            // Row data values
            // -----------------------------------------------------------------

            for ( List<NameableObject> column : getGridColumns() )
            {
                String key = BaseAnalyticalObject.getId( column, row );
                
                Double value = valueMap.get( key );
                
                grid.addValue( value );
            }
        }

        if ( isRegression() )
        {
            grid.addRegressionToGrid( startColumnIndex, numberOfColumns );
        }

        if ( isCumulative() )
        {
            grid.addCumulativesToGrid( startColumnIndex, numberOfColumns );
        }

        // ---------------------------------------------------------------------
        // Sort and limit
        // ---------------------------------------------------------------------

        if ( sortOrder() != ReportTable.NONE )
        {
            grid.sortGrid( grid.getWidth(), sortOrder() );
        }

        if ( topLimit() > 0 )
        {
            grid.limitGrid( topLimit() );
        }

        return grid;
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Adds an empty list of NameableObjects to the given list if empty.
     */
    private static void addIfEmpty( List<List<NameableObject>> list )
    {
        if ( list != null && list.size() == 0 )
        {
            list.add( Arrays.asList( new NameableObject[0] ) );
        }
    }

    /**
     * Returns the number of empty lists among the argument lists.
     */
    private static int nonEmptyLists( List<?>... lists )
    {
        int nonEmpty = 0;

        for ( List<?> list : lists )
        {
            if ( list != null && list.size() > 0 )
            {
                ++nonEmpty;
            }
        }

        return nonEmpty;
    }

    /**
     * Supportive method.
     */
    private static void verify( boolean expression, String falseMessage )
    {
        if ( !expression )
        {
            throw new IllegalStateException( falseMessage );
        }
    }
    
    /**
     * Gets the real Nameable class in case of a proxy.
     */
    @SuppressWarnings("unchecked")
    public static Class<? extends NameableObject> getNameableClass( Class<?> clazz )
    {
        while ( clazz != null )
        {       
            if ( BaseNameableObject.class.equals( clazz.getSuperclass() ) )
            {
                return (Class<? extends NameableObject>) clazz;
            }
            
            clazz = clazz.getSuperclass();
        }
        
        throw new IllegalStateException( "Class is not a Nameable object: " + clazz );
    }    

    // -------------------------------------------------------------------------
    // Get- and set-methods for persisted properties
    // -------------------------------------------------------------------------

    @Override
    public boolean haveUniqueNames()
    {
        return false;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class, DimensionalView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isRegression()
    {
        return regression;
    }

    public void setRegression( boolean regression )
    {
        this.regression = regression;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class, DimensionalView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isCumulative()
    {
        return cumulative;
    }

    public void setCumulative( boolean cumulative )
    {
        this.cumulative = cumulative;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "columnDimensions", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "column", namespace = DxfNamespaces.DXF_2_0)
    public List<String> getColumnDimensions()
    {
        return columnDimensions;
    }

    public void setColumnDimensions( List<String> columnDimensions )
    {
        this.columnDimensions = columnDimensions;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "rowDimensions", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "row", namespace = DxfNamespaces.DXF_2_0)
    public List<String> getRowDimensions()
    {
        return rowDimensions;
    }

    public void setRowDimensions( List<String> rowDimensions )
    {
        this.rowDimensions = rowDimensions;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "filterDimensions", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "filter", namespace = DxfNamespaces.DXF_2_0)
    public List<String> getFilterDimensions()
    {
        return filterDimensions;
    }

    public void setFilterDimensions( List<String> filterDimensions )
    {
        this.filterDimensions = filterDimensions;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class, DimensionalView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public ReportParams getReportParams()
    {
        return reportParams;
    }

    public void setReportParams( ReportParams reportParams )
    {
        this.reportParams = reportParams;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class, DimensionalView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder( Integer sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class, DimensionalView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Integer getTopLimit()
    {
        return topLimit;
    }

    public void setTopLimit( Integer topLimit )
    {
        this.topLimit = topLimit;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class, DimensionalView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isTotals()
    {
        return totals;
    }

    public void setTotals( boolean totals )
    {
        this.totals = totals;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class, DimensionalView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isSubtotals()
    {
        return subtotals;
    }

    public void setSubtotals( boolean subtotals )
    {
        this.subtotals = subtotals;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class, DimensionalView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isHideEmptyRows()
    {
        return hideEmptyRows;
    }

    public void setHideEmptyRows( boolean hideEmptyRows )
    {
        this.hideEmptyRows = hideEmptyRows;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class, DimensionalView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public String getDigitGroupSeparator()
    {
        return digitGroupSeparator;
    }

    public void setDigitGroupSeparator( String digitGroupSeparator )
    {
        this.digitGroupSeparator = digitGroupSeparator;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class, DimensionalView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public String getDisplayDensity()
    {
        return displayDensity;
    }

    public void setDisplayDensity( String displayDensity )
    {
        this.displayDensity = displayDensity;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class, DimensionalView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public String getFontSize()
    {
        return fontSize;
    }

    public void setFontSize( String fontSize )
    {
        this.fontSize = fontSize;
    }

    // -------------------------------------------------------------------------
    // Get- and set-methods for transient properties
    // -------------------------------------------------------------------------

    @JsonIgnore
    public String getReportingPeriodName()
    {
        return reportingPeriodName;
    }

    @JsonIgnore
    public void setReportingPeriodName( String reportingPeriodName )
    {
        this.reportingPeriodName = reportingPeriodName;
    }

    @JsonIgnore
    public List<List<NameableObject>> getGridColumns()
    {
        return gridColumns;
    }

    @JsonIgnore
    public List<List<NameableObject>> getGridRows()
    {
        return gridRows;
    }

    @JsonIgnore
    public OrganisationUnit getParentOrganisationUnit()
    {
        return parentOrganisationUnit;
    }

    @JsonIgnore
    public void setParentOrganisationUnit( OrganisationUnit parentOrganisationUnit )
    {
        this.parentOrganisationUnit = parentOrganisationUnit;
    }

    @JsonIgnore
    public List<DataElementCategoryOptionCombo> getCategoryOptionCombos()
    {
        return categoryOptionCombos;
    }

    @JsonIgnore
    public void setCategoryOptionCombos( List<DataElementCategoryOptionCombo> categoryOptionCombos )
    {
        this.categoryOptionCombos = categoryOptionCombos;
    }

    @Override
    public void mergeWith( IdentifiableObject other )
    {
        super.mergeWith( other );

        if ( other.getClass().isInstance( this ) )
        {
            ReportTable reportTable = (ReportTable) other;

            regression = reportTable.isRegression();
            cumulative = reportTable.isCumulative();
            reportParams = reportTable.getReportParams() == null ? reportParams : reportTable.getReportParams();
            sortOrder = reportTable.getSortOrder() == null ? sortOrder : reportTable.getSortOrder();
            topLimit = reportTable.getTopLimit() == null ? topLimit : reportTable.getTopLimit();
            totals = reportTable.isTotals();
            subtotals = reportTable.isSubtotals();
            hideEmptyRows = reportTable.isHideEmptyRows();
            digitGroupSeparator = reportTable.getDigitGroupSeparator();
            displayDensity = reportTable.getDisplayDensity();
            fontSize = reportTable.getFontSize();
            
            columnDimensions.clear();
            columnDimensions.addAll( reportTable.getColumnDimensions() );
            
            rowDimensions.clear();
            rowDimensions.addAll( reportTable.getRowDimensions() );
            
            filterDimensions.clear();
            filterDimensions.addAll( reportTable.getFilterDimensions() );
        }
    }
}
