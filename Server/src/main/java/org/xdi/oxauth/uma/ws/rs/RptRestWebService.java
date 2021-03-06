/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.xdi.oxauth.uma.ws.rs;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.xdi.oxauth.model.uma.UmaConstants;

import com.wordnik.swagger.annotations.Api;

/**
 * The endpoint at which the requester asks the AM to issue an RPT relating to
 * this requesting party, host, and AM.
 * 
 * @author Yuriy Movchan Date: 10/16/2012
 */
@Path("/requester/rpt")
@Api(value = "/requester/rpt", description = "The endpoint at which the requester asks the AM to issue an RPT")
public interface RptRestWebService {

	@POST
	@Produces({ UmaConstants.JSON_MEDIA_TYPE })
	public Response getRequesterPermissionToken(@HeaderParam("Authorization") String authorization,
			@HeaderParam("Host") String host);

}