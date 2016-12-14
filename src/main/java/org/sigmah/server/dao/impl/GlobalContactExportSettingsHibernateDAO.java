package org.sigmah.server.dao.impl;

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

import javax.persistence.TypedQuery;

import org.sigmah.server.dao.GlobalContactExportSettingsDAO;
import org.sigmah.server.dao.base.AbstractDAO;
import org.sigmah.server.domain.export.GlobalContactExportSettings;

public class GlobalContactExportSettingsHibernateDAO extends AbstractDAO<GlobalContactExportSettings, Integer> implements GlobalContactExportSettingsDAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GlobalContactExportSettings getGlobalExportSettingsByOrganization(final Integer organizationId) {

		final TypedQuery<GlobalContactExportSettings> query = em().createQuery("FROM GlobalContactExportSettings ges WHERE ges.organization.id = :organizationId", entityClass);
		query.setParameter("organizationId", organizationId);

		return query.getSingleResult();
	}

}
