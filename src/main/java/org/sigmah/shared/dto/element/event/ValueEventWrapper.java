package org.sigmah.shared.dto.element.event;

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

import java.io.Serializable;
import java.util.Set;

import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.referential.ValueEventChangeType;
import org.sigmah.shared.dto.value.TripletValueDTO;

/**
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class ValueEventWrapper implements Serializable {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 8800087226429558970L;

	private FlexibleElementDTO sourceElement;
	private TripletValueDTO tripletValue;
	private String singleValue;
	private Set<Integer> multivaluedIdsValue;
	private ValueEventChangeType changeType;
	private boolean isProjectCountryChanged;
	private Integer iterationId;

	/**
	 * @return the isProjectCountryChanged
	 */
	public boolean isProjectCountryChanged() {
		return isProjectCountryChanged;
	}

	/**
	 * @param isProjectCountryChanged
	 *          the isProjectCountryChanged to set
	 */
	public void setProjectCountryChanged(boolean isProjectCountryChanged) {
		this.isProjectCountryChanged = isProjectCountryChanged;
	}

	public ValueEventWrapper() {
	}

	public FlexibleElementDTO getSourceElement() {
		return sourceElement;
	}

	public void setSourceElement(FlexibleElementDTO sourceElement) {
		this.sourceElement = sourceElement;
	}

	public ValueEventChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ValueEventChangeType changeType) {
		this.changeType = changeType;
	}

	public TripletValueDTO getTripletValue() {
		return tripletValue;
	}

	public void setTripletValue(TripletValueDTO tripletValue) {
		this.tripletValue = tripletValue;
	}

	public String getSingleValue() {
		return singleValue;
	}

	public void setSingleValue(String singleValue) {
		this.singleValue = singleValue;
	}

	public Set<Integer> getMultivaluedIdsValue() {
		return multivaluedIdsValue;
	}

	public void setMultivaluedIdsValue(Set<Integer> multivaluedIdsValue) {
		this.multivaluedIdsValue = multivaluedIdsValue;
	}

	public Integer getIterationId() {
		return iterationId;
	}

	public void setIterationId(Integer iterationId) {
		this.iterationId = iterationId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changeType == null) ? 0 : changeType.hashCode());
		result = prime * result + ((tripletValue == null) ? 0 : tripletValue.hashCode());
		result = prime * result + ((singleValue == null) ? 0 : singleValue.hashCode());
		result = prime * result + ((multivaluedIdsValue == null) ? 0 : multivaluedIdsValue.hashCode());
		result = prime * result + ((sourceElement == null) ? 0 : sourceElement.hashCode());
		result = prime * result + ((iterationId == null) ? 0 : iterationId.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValueEventWrapper other = (ValueEventWrapper) obj;
		if (changeType != other.changeType)
			return false;
		if (tripletValue == null) {
			if (other.tripletValue != null)
				return false;
		} else if (!tripletValue.equals(other.tripletValue))
			return false;
		if (singleValue == null) {
			if (other.singleValue != null)
				return false;
		} else if (!singleValue.equals(other.singleValue))
			return false;
		if (multivaluedIdsValue == null) {
			if (other.multivaluedIdsValue != null)
				return false;
		} else if (!multivaluedIdsValue.equals(other.multivaluedIdsValue))
			return false;
		if (sourceElement == null) {
			if (other.sourceElement != null)
				return false;
		} else if (!sourceElement.equals(other.sourceElement))
			return false;
		if (iterationId == null) {
			if (other.iterationId != null)
				return false;
		} else if (!iterationId.equals(other.iterationId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ValueEventWrapper [sourceElement="
			+ sourceElement
			+ ", tripletValue="
			+ tripletValue
			+ ", singleValue="
			+ singleValue
			+ ", multivaluedIdsValue="
			+ multivaluedIdsValue
			+ ", changeType="
			+ changeType
			+ ", isProjectCountryChanged="
			+ isProjectCountryChanged
			+ ", iterationId="
			+ iterationId
			+ "]";
	}

}
