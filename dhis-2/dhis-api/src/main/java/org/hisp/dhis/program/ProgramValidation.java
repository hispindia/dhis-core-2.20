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

package org.hisp.dhis.program;

import java.io.Serializable;

import org.hisp.dhis.expression.Operator;

/**
 * @author Chau Thu Tran
 * @version $ ProgramValidation.java Apr 28, 2011 10:27:29 AM $
 */
public class ProgramValidation
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 4785165717118297802L;

    public static final String SEPARATOR_ID = "\\.";

    public static final String SEPARATOR_OBJECT = ":";

    public static String OBJECT_PROGRAM_STAGE_DATAELEMENT = "DE";
    

    public static final int BEFORE_CURRENT_DATE = 1;

    public static final int BEFORE_OR_EQUALS_TO_CURRENT_DATE = 2;

    public static final int AFTER_CURRENT_DATE = 3;

    public static final int AFTER_OR_EQUALS_TO_CURRENT_DATE = 4;
    

    public static final int BEFORE_DUE_DATE = -1;

    public static final int BEFORE_OR_EQUALS_TO_DUE_DATE = -2;

    public static final int AFTER_DUE_DATE = -3;

    public static final int AFTER_OR_EQUALS_TO_DUE_DATE = -4;
    
    public static final int BEFORE_DUE_DATE_PLUS_OR_MINUS_MAX_DAYS = -5;
    

    public static final String NOT_NULL_VALUE_IN_EXPRESSION = "{NOT-NULL-VALUE}";

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private int id;

    private String description;

    private ProgramExpression leftSide;
    
    private Operator operator;

    private ProgramExpression rightSide;

    private Program program;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ProgramValidation()
    {

    }

    public ProgramValidation( String description, ProgramExpression leftSide, ProgramExpression rightSide, Program program )
    {
        this.description = description;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.program = program;
    }

    // -------------------------------------------------------------------------
    // hashCode() and equals()
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((leftSide == null) ? 0 : leftSide.hashCode());
        result = prime * result + ((program == null) ? 0 : program.hashCode());
        result = prime * result + ((rightSide == null) ? 0 : rightSide.hashCode());
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj == null )
        {
            return false;
        }

        if ( getClass() != obj.getClass() )
        {
            return false;
        }

        ProgramValidation other = (ProgramValidation) obj;

        if ( leftSide == null )
        {
            if ( other.leftSide != null )
            {
                return false;
            }
        }
        else if ( !leftSide.equals( other.leftSide ) )
        {
            return false;
        }

        if ( program == null )
        {
            if ( other.program != null )
            {
                return false;
            }
        }
        else if ( !program.equals( other.program ) )
        {
            return false;
        }

        if ( rightSide == null )
        {
            if ( other.rightSide != null )
            {
                return false;
            }
        }
        else if ( !rightSide.equals( other.rightSide ) )
        {
            return false;
        }

        return true;
    }

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public ProgramExpression getLeftSide()
    {
        return leftSide;
    }

    public void setLeftSide( ProgramExpression leftSide )
    {
        this.leftSide = leftSide;
    }

    public ProgramExpression getRightSide()
    {
        return rightSide;
    }

    public void setRightSide( ProgramExpression rightSide )
    {
        this.rightSide = rightSide;
    }

    public Program getProgram()
    {
        return program;
    }

    public void setProgram( Program program )
    {
        this.program = program;
    }

    public Operator getOperator()
    {
        return operator;
    }

    public void setOperator( Operator operator )
    {
        this.operator = operator;
    }
}
