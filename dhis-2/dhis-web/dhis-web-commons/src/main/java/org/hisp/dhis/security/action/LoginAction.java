package org.hisp.dhis.security.action;

/*
 * Copyright (c) 2004-2015, University of Oslo
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

import java.util.ArrayList;

import org.apache.velocity.Template;
import org.hisp.dhis.constant.Constant;
import org.hisp.dhis.constant.ConstantService;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.hisp.dhis.i18n.ui.resourcebundle.ResourceBundleManager;
import org.hisp.dhis.loginattempt.LoginAttempt;
import org.hisp.dhis.loginattempt.LoginAttemptService;
import org.hisp.dhis.security.CustomExceptionMappingAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;

import com.opensymphony.xwork2.Action;

/**
 * @author mortenoh
 */
public class LoginAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DeviceResolver deviceResolver;

    public void setDeviceResolver( DeviceResolver deviceResolver )
    {
        this.deviceResolver = deviceResolver;
    }

    @Autowired
    private ResourceBundleManager resourceBundleManager;

    @Autowired
    private LoginAttemptService loginAttemptService;
    
    @Autowired
    private ConstantService constantService;

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    public Boolean failed = false;

    private String j_username;

    Constant cons = new Constant();
    
    private int userattempt;
    
    public int getUserattempt()
    {
        return userattempt;
    }

    public void setUserattempt( int userattempt )
    {
        this.userattempt = userattempt;
    }

    
 private int blockedhour;
 
    public int getBlockedhour()
{
    return blockedhour;
}

public void setBlockedhour( int blockedhour )
{
    this.blockedhour = blockedhour;
}

    private int difference;

    public int getDifference()
    {
        return difference;
    }

    public void setDifference( int difference )
    {
        this.difference = difference;
    }

    public void setFailed( Boolean failed )
    {
        this.failed = failed;
    }

    public Boolean getFailed()
    {
        return failed;
    }

    private List<Locale> availableLocales;

    public List<Locale> getAvailableLocales()
    {
        return availableLocales;
    }


    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
   
        CustomExceptionMappingAuthenticationFailureHandler custom = new CustomExceptionMappingAuthenticationFailureHandler();
       
        Constant con = constantService.getConstantByName( "BlockHour" );
        double const1 = con.getValue();
        blockedhour = (int) const1;
       difference = custom.diff;
        userattempt = custom.attempt;
        
        System.out.println("userAttemp"+userattempt);
        System.out.println("difference"+difference);
        System.out.println("failed"+failed);


        Device device = deviceResolver.resolveDevice( ServletActionContext.getRequest() );

        ServletActionContext.getResponse().addHeader( "Login-Page", "true" );

        if ( device.isMobile() || device.isTablet() )
        {
            return "mobile";
        }

        availableLocales = new ArrayList<>( resourceBundleManager.getAvailableLocales() );

        return "standard";
    }
}
