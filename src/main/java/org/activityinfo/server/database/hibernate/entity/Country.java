package org.activityinfo.server.database.hibernate.entity;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@JsonAutoDetect(JsonMethod.NONE)
@NamedQuery(name = "queryAllCountriesAlphabetically",
    query = "select c from Country c order by c.name")
public class Country implements Serializable {

    private int id;
    private String name;
    private Bounds bounds;

    private Set<AdminLevel> adminLevels = new HashSet<AdminLevel>(0);
    private Set<LocationType> locationTypes = new HashSet<LocationType>(0);

    private String codeISO;

    public Country() {
    }

    /**
     * Gets the country's id
     * 
     * @return the country's id
     */
    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CountryId", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    /**
     * Sets the country's id
     * 
     * @param id
     *            the country's id
     */
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty
    @Column(name = "Name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    /**
     * A short, human-readable name of the Country
     * 
     * @param name
     *            A short, human-readable name of the Country
     */
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("code")
    @Column(name = "ISO2", length = 2)
    public String getCodeISO() {
        return this.codeISO;
    }

    public void setCodeISO(String codeISO) {
        this.codeISO = codeISO;
    }

    /**
     * The geographic bounds of this Country. Bounds for the Country cannot be
     * null.
     * 
     * @return tbe geogaphics bounds of this Country
     */
    @Embedded
    @JsonProperty
    @AttributeOverrides({
        @AttributeOverride(name = "x1", column = @Column(nullable = false)),
        @AttributeOverride(name = "y1", column = @Column(nullable = false)),
        @AttributeOverride(name = "x2", column = @Column(nullable = false)),
        @AttributeOverride(name = "y2", column = @Column(nullable = false))
    })
    public Bounds getBounds() {
        return this.bounds;
    }

    /**
     * Sets the country's geographic bounds. Bounds for the Country c
     * 
     * @param bounds
     */
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    /**
     * Gets all the administrative levels for this Country
     * 
     * @return a list of all the administrative levels in this Country
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "country")
    @org.hibernate.annotations.OrderBy(clause = "AdminLevelId")
    public Set<AdminLevel> getAdminLevels() {
        return this.adminLevels;
    }

    /**
     * Sets the administrative levels for this Country
     * 
     * @param adminLevels
     */
    public void setAdminLevels(Set<AdminLevel> adminLevels) {
        this.adminLevels = adminLevels;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "country")
    public Set<LocationType> getLocationTypes() {
        return locationTypes;
    }

    public void setLocationTypes(Set<LocationType> types) {
        this.locationTypes = types;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Country");
        sb.append("\nname:");
        sb.append(this.getName());
        sb.append("\niso2:");
        sb.append(this.getCodeISO());
        sb.append("\nbounds:");
        sb.append(this.getBounds());
        return sb.toString();
    }
}
