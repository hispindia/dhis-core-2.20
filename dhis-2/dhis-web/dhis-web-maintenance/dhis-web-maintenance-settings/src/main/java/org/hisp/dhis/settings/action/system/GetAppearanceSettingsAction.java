package org.hisp.dhis.settings.action.system;

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

import com.opensymphony.xwork2.Action;
import org.hisp.dhis.appmanager.App;
import org.hisp.dhis.appmanager.AppManager;
import org.hisp.dhis.i18n.locale.LocaleManager;
import org.hisp.dhis.setting.StyleManager;
import org.hisp.dhis.setting.SystemSettingManager;
import org.hisp.dhis.commons.filter.Filter;
import org.hisp.dhis.commons.filter.FilterUtils;
import org.hisp.dhis.webportal.module.Module;
import org.hisp.dhis.webportal.module.ModuleManager;
import org.hisp.dhis.webportal.module.StartableModuleFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;

/**
 * @author Lars Helge Overland
 */
public class GetAppearanceSettingsAction
    implements Action
{
    private static final Filter<Module> startableFilter = new StartableModuleFilter();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    @Autowired
    private SystemSettingManager systemSettingManager;

    @Autowired
    private ModuleManager moduleManager;

    @Autowired
    private StyleManager styleManager;

    @Autowired
    private LocaleManager localeManager;

    @Autowired
    private AppManager appManager;

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<String> flags = new ArrayList<>();

    public List<String> getFlags()
    {
        return flags;
    }

    private List<Module> modules = new ArrayList<>();

    public List<Module> getModules()
    {
        return modules;
    }

    private List<App> apps = new ArrayList<>();

    public List<App> getApps()
    {
        return apps;
    }

    private SortedMap<String, String> styles;

    public SortedMap<String, String> getStyles()
    {
        return styles;
    }

    private String currentStyle;

    public String getCurrentStyle()
    {
        return currentStyle;
    }

    private List<Locale> availableLocales = new ArrayList<>();

    public List<Locale> getAvailableLocales()
    {
        return availableLocales;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
    {
        availableLocales = localeManager.getAvailableLocales();

        styles = styleManager.getStyles();

        currentStyle = styleManager.getSystemStyle();

        flags = systemSettingManager.getFlags();

        modules = moduleManager.getMenuModules();

        apps = appManager.getApps();

        FilterUtils.filter( modules, startableFilter );

        return SUCCESS;
    }
}
