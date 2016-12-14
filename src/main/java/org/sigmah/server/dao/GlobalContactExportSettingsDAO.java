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

import org.sigmah.server.dao.base.DAO;
import org.sigmah.server.domain.Organization;
import org.sigmah.server.domain.export.GlobalContactExportSettings;

/**
 * {@link GlobalContactExportSettings} DAO interface.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public interface GlobalContactExportSettingsDAO extends DAO<GlobalContactExportSettings, Integer> {

	/**
	 * Finds the given {@code organizationId} corresponding {@link GlobalContactExportSettings}.
	 *
	 * @param organizationId
	 *          The {@link Organization} id.
	 * @return The given {@code organizationId} corresponding {@link GlobalContactExportSettings}.
	 */
	GlobalContactExportSettings getGlobalExportSettingsByOrganization(Integer organizationId);

}
