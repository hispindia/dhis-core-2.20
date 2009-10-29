/*
 * Copyright (c) 2004-2007, University of Oslo
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
package org.hisp.dhis.reportexcel.excelitemgroup.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.comparator.OrganisationUnitGroupNameComparator;
import org.hisp.dhis.reportexcel.excelitem.ExcelItemGroup;
import org.hisp.dhis.reportexcel.excelitem.ExcelItemService;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * @version $Id$
 */
public class OpenUpdateOrganisationUnitGroupForCategoryAction implements Action {
	// -------------------------------------------
	// Dependency
	// -------------------------------------------

	private ExcelItemService excelItemService;

	private OrganisationUnitGroupService organisationUnitGroupService;

	// -------------------------------------------
	// Input & Output
	// -------------------------------------------

	private Integer id;

	private List<OrganisationUnitGroup> availableOrganisationUnitGroups;

	private List<OrganisationUnitGroup> selectedOrganisationUnitGroups;

	private ExcelItemGroup excelItemGroup;

	// -------------------------------------------
	// Getter & Setter
	// -------------------------------------------
	
	public void setExcelItemService(ExcelItemService excelItemService) {
		this.excelItemService = excelItemService;
	}

	public List<OrganisationUnitGroup> getSelectedOrganisationUnitGroups() {
		return selectedOrganisationUnitGroups;
	}

	public ExcelItemGroup getExcelItemGroup() {
		return excelItemGroup;
	}

	public List<OrganisationUnitGroup> getAvailableOrganisationUnitGroups() {
		return availableOrganisationUnitGroups;
	}

	public void setOrganisationUnitGroupService(
			OrganisationUnitGroupService organisationUnitGroupService) {
		this.organisationUnitGroupService = organisationUnitGroupService;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	// -------------------------------------------
	// Action implementation
	// -------------------------------------------

	public String execute() throws Exception {

		this.excelItemGroup = excelItemService.getExcelItemGroup(id);

		this.availableOrganisationUnitGroups = new ArrayList<OrganisationUnitGroup>(
				this.organisationUnitGroupService
						.getAllOrganisationUnitGroups());

		this.selectedOrganisationUnitGroups = excelItemGroup
				.getOrganisationUnitGroups();

		availableOrganisationUnitGroups
				.removeAll(selectedOrganisationUnitGroups);

		Collections.sort(this.availableOrganisationUnitGroups,
				new OrganisationUnitGroupNameComparator());

		return SUCCESS;
	}

}
