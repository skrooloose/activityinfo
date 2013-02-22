package org.activityinfo.server.endpoint.jsonrpc;

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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.endpoint.gwtrpc.CommandServlet;
import org.activityinfo.server.endpoint.jsonrpc.serde.ModelDataSerializer;
import org.activityinfo.server.endpoint.jsonrpc.serde.SyncRegionUpdateSerializer;
import org.activityinfo.server.util.logging.LogException;
import org.activityinfo.shared.auth.AnonymousUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.InvalidAuthTokenException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * An adapter class for the GWT-RPC interface that allows non-GWT clients to
 * execute commands using the {@code Command} encoded as JSON.
 * 
 */
@Singleton
public class JsonCommandServlet extends HttpServlet {

    private final CommandServlet commandServlet;
    private final Gson gson;

    @Inject
    public JsonCommandServlet(CommandServlet commandServlet) {
        this.commandServlet = commandServlet;
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(AdminEntityDTO.class,
                new ModelDataSerializer())
            .registerTypeAdapter(SyncRegionUpdate.class,
                new SyncRegionUpdateSerializer())
            .create();
    }

    /**
     * Only handles commands for {@code Command} classes prefixed by "Get"
     * Properties of the {@code Command} object should be provided by url
     * parameters
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        try {

            String commandName = "org.activityinfo.shared.command.Get"
                + parseCommandName(req);
            Command command = unmarshalCommandFromParameters(commandName, req);

            CommandResult result = commandServlet.execute(getAuthToken(req),
                command);

            resp.setContentType("application/json");
            resp.getWriter().print(gson.toJson(result));

        } catch (BadRequestException e) {
            resp.sendError(e.getStatusCode(), e.getMessage());
        } catch (InvalidAuthTokenException e) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (CommandException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                e.getMessage());
        }
    }

    private String getAuthToken(HttpServletRequest req) {

        String authToken = req.getHeader("X-ActivityInfo-AuthToken");
        if (authToken != null && authToken.length() != 0) {
            return authToken;
        }

        authToken = req.getParameter("authToken");
        if (authToken == null) {
            return AnonymousUser.AUTHTOKEN;
        }

        return authToken;
    }

    private String parseCommandName(HttpServletRequest req)
        throws BadRequestException {
        String uri = req.getRequestURI();
        int slash = uri.lastIndexOf('/');
        if (slash == -1) {
            throw new BadRequestException(
                "Expected url in the form /rpc/CommandName");
        }
        return uri.substring(slash + 1);
    }

    @LogException
    private Command unmarshalCommandFromParameters(String commandName,
        HttpServletRequest req) throws ServletException, BadRequestException {
        Command command;

        try {
            command = (Command) Class.forName(commandName).newInstance();
        } catch (InstantiationException e) {
            throw new ServletException(
                "Exception instantiating Command object", e);
        } catch (IllegalAccessException e) {
            throw new ServletException(
                "Exception instantiating Command object", e);
        } catch (ClassNotFoundException e) {
            throw new ServletException(
                "Exception instantiating Command object", e);
        }

        // look for setters
        for (Method method : command.getClass().getMethods()) {
            if (isSetter(method)) {
                String property = propertyNameFromSetter(method);
                if (req.getParameterValues(property) != null) {
                    try {
                        method.invoke(
                            command,
                            convert(property, req.getParameter(property),
                                method.getParameterTypes()[0]));
                    } catch (IllegalAccessException e) {
                        throw new ServletException(e);
                    } catch (InvocationTargetException e) {
                        throw new ServletException(e);
                    }
                }
            }
        }

        return command;
    }

    private Object convert(String name, String parameter, Class aClass)
        throws BadRequestException, ServletException {
        try {
            if (aClass.equals(String.class)) {
                return parameter;
            } else if (aClass.equals(Integer.class)
                || aClass.equals(Integer.TYPE)) {
                return Integer.parseInt(parameter);
            } else if (aClass.equals(Double.class)
                || aClass.equals(Double.TYPE)) {
                return Double.parseDouble(parameter);
            } else if (aClass.equals(Long.class) || aClass.equals(Long.TYPE)) {
                return Long.parseLong(parameter);
            } else if (aClass.equals(Boolean.class)
                || aClass.equals(Boolean.TYPE)) {
                return "false".equals(parameter.toLowerCase());
            } else {
                throw new ServletException(
                    "No converter implemented yet for type " + aClass.getName());
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("Could not parse parameter '" + name
                + "' as integer");
        }
    }

    private String propertyNameFromSetter(Method method) {
        return method.getName().substring(3, 4).toLowerCase()
            + method.getName().substring(4);
    }

    private boolean isSetter(Method method) {
        return method.getName().startsWith("set")
            && Modifier.isPublic(method.getModifiers()) &&
            !Modifier.isStatic(method.getModifiers())
            && method.getParameterTypes().length == 1;
    }
}
