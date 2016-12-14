package org.sigmah.server.dao;

/*
 * #%L
 * Sigmah
 * %%
 * Copyright (C) 2010 - 2016 URD
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Date;
import java.util.List;

import org.sigmah.server.dao.base.DAO;
import org.sigmah.server.domain.ContactModel;
import org.sigmah.server.domain.Organization;
import org.sigmah.server.domain.ProjectModel;
import org.sigmah.server.domain.export.GlobalContactExport;
import org.sigmah.server.domain.export.GlobalContactExportSettings;
import org.sigmah.server.domain.export.GlobalExport;
import org.sigmah.server.domain.export.GlobalExportSettings;

/**
 * Global Export DAO interface.
 * 
 * @author sherzod
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public interface GlobalExportDAO extends DAO<GlobalExport, Integer> {

	List<ProjectModel> getProjectModelsByOrganization(Organization organization);

	List<ContactModel> getContactModels();

	List<GlobalExport> getGlobalExports(Date from, Date to);

	List<GlobalExport> getOlderExports(Date oldDate, Organization organization);

	List<GlobalExportSettings> getGlobalExportSettings();

	List<GlobalContactExport> getGlobalContactExports(Date from, Date to);

	List<GlobalContactExport> getOlderContactExports(Date oldDate, Organization organization);

	List<GlobalContactExportSettings> getGlobalContactExportSettings();

}
