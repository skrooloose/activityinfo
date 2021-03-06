package org.activityinfo.shared.command.result;

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

/**
 * Result of commands which create a new entity.
 * 
 * @see org.activityinfo.shared.command.CreateEntity
 * @see org.activityinfo.shared.command.CreateReport
 * 
 * @author Alex Bertram
 */
public class CreateResult implements CommandResult {

    private static final long serialVersionUID = -2196195672020302549L;

    private int newId;

    protected CreateResult() {

    }

    public CreateResult(int newId) {
        this.newId = newId;
    }

    /**
     * Gets the primary key of the new entity.
     * 
     * @return the primary key of the new entity that was generated by the
     *         server.
     */
    public int getNewId() {
        return newId;
    }

    public void setNewId(int newId) {
        this.newId = newId;
    }

}
