package org.sigmah.shared.dto.reminder;

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

import java.util.List;

import org.sigmah.shared.dto.base.AbstractModelDataEntityDTO;

/**
 * DTO mapping class for entity reminder.MonitoredPointList.
 * 
 * @author tmi
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class MonitoredPointListDTO extends AbstractModelDataEntityDTO<Integer> {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1001655394559887157L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEntityName() {
		return "reminder.MonitoredPointList";
	}

	// Monitored points
	public List<MonitoredPointDTO> getPoints() {
		return get("points");
	}

	public void setPoints(List<MonitoredPointDTO> points) {
		set("points", points);
	}
}
