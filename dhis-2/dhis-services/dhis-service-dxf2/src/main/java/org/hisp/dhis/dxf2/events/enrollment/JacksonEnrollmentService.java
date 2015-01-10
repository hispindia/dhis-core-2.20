package org.hisp.dhis.dxf2.events.enrollment;

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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.hisp.dhis.dxf2.importsummary.ImportSummaries;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.hisp.dhis.importexport.ImportStrategy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
@Transactional
public class JacksonEnrollmentService extends AbstractEnrollmentService
{
    // -------------------------------------------------------------------------
    // EnrollmentService Impl
    // -------------------------------------------------------------------------

    private final static ObjectMapper xmlMapper = new XmlMapper();

    private final static ObjectMapper jsonMapper = new ObjectMapper();

    @SuppressWarnings( "unchecked" )
    private static <T> T fromXml( InputStream inputStream, Class<?> clazz ) throws IOException
    {
        return (T) xmlMapper.readValue( inputStream, clazz );
    }

    @SuppressWarnings( "unchecked" )
    private static <T> T fromXml( String input, Class<?> clazz ) throws IOException
    {
        return (T) xmlMapper.readValue( input, clazz );
    }

    @SuppressWarnings( "unchecked" )
    private static <T> T fromJson( InputStream inputStream, Class<?> clazz ) throws IOException
    {
        return (T) jsonMapper.readValue( inputStream, clazz );
    }

    @SuppressWarnings( "unchecked" )
    private static <T> T fromJson( String input, Class<?> clazz ) throws IOException
    {
        return (T) jsonMapper.readValue( input, clazz );
    }

    static
    {
        xmlMapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true );
        xmlMapper.configure( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true );
        xmlMapper.configure( DeserializationFeature.WRAP_EXCEPTIONS, true );
        jsonMapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true );
        jsonMapper.configure( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true );
        jsonMapper.configure( DeserializationFeature.WRAP_EXCEPTIONS, true );
    }

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------

    @Override
    public ImportSummaries addEnrollmentsJson( InputStream inputStream, ImportStrategy strategy ) throws IOException
    {
        String input = StreamUtils.copyToString( inputStream, Charset.forName( "UTF-8" ) );
        List<Enrollment> enrollments = new ArrayList<>();

        try
        {
            Enrollments fromJson = fromJson( input, Enrollments.class );
            enrollments.addAll( fromJson.getEnrollments() );
        }
        catch ( Exception ex )
        {
            Enrollment fromJson = fromJson( input, Enrollment.class );
            enrollments.add( fromJson );
        }

        return addEnrollments( enrollments, strategy );
    }

    @Override
    public ImportSummaries addEnrollmentsXml( InputStream inputStream, ImportStrategy strategy ) throws IOException
    {
        String input = StreamUtils.copyToString( inputStream, Charset.forName( "UTF-8" ) );
        List<Enrollment> enrollments = new ArrayList<>();

        try
        {
            Enrollments fromXml = fromXml( input, Enrollments.class );
            enrollments.addAll( fromXml.getEnrollments() );
        }
        catch ( Exception ex )
        {
            Enrollment fromXml = fromXml( input, Enrollment.class );
            enrollments.add( fromXml );
        }

        return addEnrollments( enrollments, strategy );
    }

    private ImportSummaries addEnrollments( List<Enrollment> enrollments, ImportStrategy strategy )
    {
        ImportSummaries importSummaries = new ImportSummaries();

        Enrollments create = new Enrollments();
        Enrollments update = new Enrollments();

        if ( strategy.isCreate() )
        {
            create.getEnrollments().addAll( enrollments );
        }
        else if ( strategy.isCreateAndUpdate() )
        {
            for ( Enrollment enrollment : enrollments )
            {
                if ( StringUtils.isEmpty( enrollment.getEnrollment() ) )
                {
                    create.getEnrollments().add( enrollment );
                }
                else
                {
                    if ( programInstanceService.getProgramInstance( enrollment.getEnrollment() ) == null )
                    {
                        create.getEnrollments().add( enrollment );
                    }
                    else
                    {
                        update.getEnrollments().add( enrollment );
                    }
                }
            }
        }

        for ( Enrollment enrollment : create.getEnrollments() )
        {
            importSummaries.addImportSummary( addEnrollment( enrollment ) );
        }

        for ( Enrollment enrollment : update.getEnrollments() )
        {
            importSummaries.addImportSummary( updateEnrollment( enrollment ) );
        }

        return importSummaries;
    }

    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------

    @Override
    public ImportSummary updateEnrollmentJson( String id, InputStream inputStream ) throws IOException
    {
        Enrollment enrollment = fromJson( inputStream, Enrollment.class );
        enrollment.setEnrollment( id );

        return updateEnrollment( enrollment );
    }

    @Override
    public ImportSummary updateEnrollmentXml( String id, InputStream inputStream ) throws IOException
    {
        Enrollment enrollment = fromXml( inputStream, Enrollment.class );
        enrollment.setEnrollment( id );

        return updateEnrollment( enrollment );
    }
}
