package org.sigmah.shared.computation.instruction;

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

import org.sigmah.shared.dto.ProjectFundingDTO;

/**
 * Scope the next function call to search inside funded projects.
 * 
 * @author Raphaël Calabro (raphael.calabro@netapsys.fr)
 */
public class FundedProjects extends AbstractScopeFunction {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "fundedProjects";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function instantiate() {
		return new FundedProjects();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProjectFundingDTO.LinkedProjectType getLinkedProjectType() {
		return ProjectFundingDTO.LinkedProjectType.FUNDED_PROJECT;
	}

}
