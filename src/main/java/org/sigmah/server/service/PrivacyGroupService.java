package org.sigmah.server.service;

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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.sigmah.server.dispatch.impl.UserDispatch.UserExecutionContext;
import org.sigmah.server.domain.User;
import org.sigmah.server.domain.profile.PrivacyGroup;
import org.sigmah.server.service.base.AbstractEntityService;
import org.sigmah.server.service.util.PropertyMap;
import org.sigmah.shared.dispatch.CommandException;
import org.sigmah.shared.dto.profile.PrivacyGroupDTO;

import com.google.inject.Singleton;

/**
 * Create privacy group policy.
 * 
 * @author nrebiai
 * @author Maxime Lombard (mlombard@ideia.fr)
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Singleton
public class PrivacyGroupService extends AbstractEntityService<PrivacyGroup, Integer, PrivacyGroupDTO> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrivacyGroup create(PropertyMap properties, final UserExecutionContext context) {

		final User executingUser = context.getUser();

		// get privacy group that need to be saved from properties
		final Number code = (Number) properties.get(PrivacyGroupDTO.CODE);
		final String title = properties.get(PrivacyGroupDTO.TITLE);

		// Save privacy group
		if (code == null || title == null) {
			throw new IllegalArgumentException("Invalid argument.");
		}

		PrivacyGroup pgToPersist = null;
		List<PrivacyGroup> privacyGroups = new ArrayList<PrivacyGroup>();

		final TypedQuery<PrivacyGroup> query =
				em().createQuery("SELECT p FROM PrivacyGroup p WHERE p.code = :code and p.organization.id = :orgid ORDER BY p.id", entityClass);
		query.setParameter("orgid", executingUser.getOrganization().getId());
		query.setParameter("code", new Integer(code.intValue()));

		privacyGroups.addAll(query.getResultList());

		if (privacyGroups.size() != 0) {
			pgToPersist = privacyGroups.get(0);
			pgToPersist.setCode(code.intValue());
			pgToPersist.setTitle(title);
			pgToPersist.setOrganization(executingUser.getOrganization());
			pgToPersist = em().merge(pgToPersist);
			pgToPersist.setUpdated(true);

		} else {
			pgToPersist = new PrivacyGroup();
			pgToPersist.setCode(code.intValue());
			pgToPersist.setTitle(title);
			pgToPersist.setOrganization(executingUser.getOrganization());
			em().persist(pgToPersist);
			pgToPersist.setUpdated(false);
		}

		em().flush();
		return pgToPersist;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrivacyGroup update(Integer entityId, PropertyMap changes, final UserExecutionContext context) throws CommandException {

		final PrivacyGroup privacyGroup = em().find(entityClass, entityId);

		if (privacyGroup == null) {
			throw new CommandException("Privacy group with id #" + entityId + " cannot be found.");
		}

		privacyGroup.setTitle((String) changes.get(PrivacyGroupDTO.TITLE));
		// Code should not be updated here ; too many risks.

		em().persist(privacyGroup);

		em().flush();

		return privacyGroup;
	}

}
