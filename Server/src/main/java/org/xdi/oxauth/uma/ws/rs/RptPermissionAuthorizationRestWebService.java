/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.xdi.oxauth.uma.ws.rs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.xdi.oxauth.model.uma.RptAuthorizationRequest;
import org.xdi.oxauth.model.uma.UmaConstants;

import com.wordnik.swagger.annotations.Api;

/**
 * The endpoint at which the requester asks for authorization to have a new permission.
 * 
 * @author Yuriy Movchan Date: 10/25/2012
 */
@Path("/requester/perm")
@Api(value="/requester/perm", description = "RPT authorization endpoint. RPT is authorized with new permission(s).")
public interface RptPermissionAuthorizationRestWebService {

	@POST
	@Consumes({ UmaConstants.JSON_MEDIA_TYPE })
	@Produces({ UmaConstants.JSON_MEDIA_TYPE })
	public Response requestRptPermissionAuthorization(
            @HeaderParam("Authorization") String authorization,
			@HeaderParam("Host") String amHost,
            RptAuthorizationRequest rptAuthorizationRequest,
            @Context HttpServletRequest httpRequest);

}