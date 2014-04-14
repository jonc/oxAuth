package org.xdi.oxauth.ws.rs;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jboss.seam.mock.EnhancedMockHttpServletRequest;
import org.jboss.seam.mock.EnhancedMockHttpServletResponse;
import org.jboss.seam.mock.ResourceRequestEnvironment;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.xdi.oxauth.BaseTest;
import org.xdi.oxauth.client.RegisterRequest;
import org.xdi.oxauth.client.RegisterResponse;
import org.xdi.oxauth.model.common.AuthenticationMethod;
import org.xdi.oxauth.model.common.SubjectType;
import org.xdi.oxauth.model.crypto.signature.SignatureAlgorithm;
import org.xdi.oxauth.model.register.ApplicationType;
import org.xdi.oxauth.model.register.RegisterResponseParam;
import org.xdi.oxauth.model.uma.TestUtil;
import org.xdi.oxauth.model.util.StringUtils;
import org.xdi.util.Util;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;
import static org.xdi.oxauth.model.register.RegisterRequestParam.*;
import static org.xdi.oxauth.model.register.RegisterResponseParam.*;

/**
 * Functional tests for Client Registration Web Services (embedded)
 *
 * @author Javier Rojas Blum Date: 01.11.2012
 */
public class RegistrationRestWebServiceEmbeddedTest extends BaseTest {

    private String registrationAccessToken1;
    private String registrationClientUri1;

    @Parameters({"registerPath", "redirectUris"})
    @Test
    public void requestClientAssociate1(final String registerPath, final String redirectUris) throws Exception {

        new ResourceRequestEnvironment.ResourceRequest(new ResourceRequestEnvironment(this),
                ResourceRequestEnvironment.Method.POST, registerPath) {

            @Override
            protected void prepareRequest(EnhancedMockHttpServletRequest request) {
                try {
                    super.prepareRequest(request);

                    RegisterRequest registerRequest = new RegisterRequest(ApplicationType.WEB, "oxAuth test app",
                            StringUtils.spaceSeparatedToList(redirectUris));

                    request.setContentType(MediaType.APPLICATION_JSON);
                    String registerRequestContent = registerRequest.getJSONParameters().toString(4);
                    request.setContent(registerRequestContent.getBytes());
                } catch (JSONException e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                }
            }

            @Override
            protected void onResponse(EnhancedMockHttpServletResponse response) {
                super.onResponse(response);
                showResponse("requestClientAssociate1", response);

                assertEquals(response.getStatus(), 200, "Unexpected response code. " + response.getContentAsString());
                assertNotNull(response.getContentAsString(), "Unexpected result: " + response.getContentAsString());
                try {
                    final RegisterResponse registerResponse = RegisterResponse.valueOf(response.getContentAsString());
                    ClientTestUtil.assert_(registerResponse);

                    registrationAccessToken1 = registerResponse.getRegistrationAccessToken();
                    registrationClientUri1 = registerResponse.getRegistrationClientUri();
                } catch (Exception e) {
                    e.printStackTrace();
                    fail(e.getMessage() + "\nResponse was: " + response.getContentAsString());
                }
            }
        }.run();
    }

    @Parameters({"registerPath", "redirectUris", "sectorIdentifierUri"})
    @Test
    public void requestClientAssociate2(final String registerPath, final String redirectUris,
                                        final String sectorIdentifierUri) throws Exception {

        new ResourceRequestEnvironment.ResourceRequest(new ResourceRequestEnvironment(this),
                ResourceRequestEnvironment.Method.POST, registerPath) {

            @Override
            protected void prepareRequest(EnhancedMockHttpServletRequest request) {
                try {
                    super.prepareRequest(request);

                    List<String> contacts = Arrays.asList("test_user@gluu.org", "javier@gluu.org");

                    RegisterRequest registerRequest = new RegisterRequest(ApplicationType.WEB, "oxAuth test app",
                            StringUtils.spaceSeparatedToList(redirectUris));
                    registerRequest.setContacts(contacts);
                    registerRequest.setLogoUri("http://www.gluu.org/wp-content/themes/gluursn/images/logo.png");
                    registerRequest.setClientUri("http://www.gluu.org/company/team");
                    registerRequest.setTokenEndpointAuthMethod(AuthenticationMethod.CLIENT_SECRET_JWT);
                    registerRequest.setPolicyUri("http://www.gluu.org/policy");
                    registerRequest.setJwksUri("http://www.gluu.org/jwks");
                    registerRequest.setSectorIdentifierUri(sectorIdentifierUri);
                    registerRequest.setSubjectType(SubjectType.PUBLIC);
                    registerRequest.setRequestObjectSigningAlg(SignatureAlgorithm.RS256);

                    request.setContentType(MediaType.APPLICATION_JSON);
                    String registerRequestContent = registerRequest.getJSONParameters().toString(4);
                    request.setContent(registerRequestContent.getBytes());
                } catch (JSONException e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                }
            }

            @Override
            protected void onResponse(EnhancedMockHttpServletResponse response) {
                super.onResponse(response);
                showResponse("requestClientAssociate2", response);

                assertEquals(response.getStatus(), 200, "Unexpected response code. " + response.getContentAsString());
                assertNotNull(response.getContentAsString(), "Unexpected result: " + response.getContentAsString());
                try {
                    final RegisterResponse registerResponse = RegisterResponse.valueOf(response.getContentAsString());
                    ClientTestUtil.assert_(registerResponse);

                    JSONObject jsonObj = new JSONObject(response.getContentAsString());

                    // Registered Metadata
                    assertTrue(jsonObj.has(CLIENT_URI.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    fail(e.getMessage() + "\nResponse was: " + response.getContentAsString());
                }
            }
        }.run();
    }

    @Parameters({"registerPath"})
    @Test(dependsOnMethods = "requestClientAssociate1")
    public void requestClientRead(final String registerPath) throws Exception {

        new ResourceRequestEnvironment.ResourceRequest(new ResourceRequestEnvironment(this),
                ResourceRequestEnvironment.Method.GET, registerPath) {

            @Override
            protected void prepareRequest(EnhancedMockHttpServletRequest request) {
                super.prepareRequest(request);

                request.addHeader("Authorization", "Bearer " + registrationAccessToken1);
                request.setContentType(MediaType.APPLICATION_JSON);
                request.setQueryString(registrationClientUri1.substring(registrationClientUri1.indexOf("?") + 1));
            }

            @Override
            protected void onResponse(EnhancedMockHttpServletResponse response) {
                super.onResponse(response);
                showResponse("requestClientRead", response);
                readResponseAssert(response);
            }
        }.run();
    }

    public static void readResponseAssert(EnhancedMockHttpServletResponse response) {
        assertEquals(response.getStatus(), 200, "Unexpected response code. " + response.getContentAsString());
        assertNotNull(response.getContentAsString(), "Unexpected result: " + response.getContentAsString());
        try {
            JSONObject jsonObj = new JSONObject(response.getContentAsString());
            assertTrue(jsonObj.has(RegisterResponseParam.CLIENT_ID.toString()));
            assertTrue(jsonObj.has(CLIENT_SECRET.toString()));
            assertTrue(jsonObj.has(CLIENT_ID_ISSUED_AT.toString()));
            assertTrue(jsonObj.has(CLIENT_SECRET_EXPIRES_AT.toString()));

            // Registered Metadata
            assertTrue(jsonObj.has(REDIRECT_URIS.toString()));
            assertTrue(jsonObj.has(APPLICATION_TYPE.toString()));
            assertTrue(jsonObj.has(CLIENT_NAME.toString()));
            assertTrue(jsonObj.has(ID_TOKEN_SIGNED_RESPONSE_ALG.toString()));
            assertTrue(jsonObj.has("scopes"));
        } catch (JSONException e) {
            e.printStackTrace();
            fail(e.getMessage() + "\nResponse was: " + response.getContentAsString());
        }
    }

    @Parameters({"registerPath", "redirectUris"})
    @Test(dependsOnMethods = "requestClientAssociate1")
    public void requestClientUpdate(final String registerPath, final String redirectUris) throws Exception {
        final String contactEmailNewValue = "yuriy@gluu.org";
        final String logoUriNewValue = "http://www.gluu.org/test/yuriy/logo.png";
        final String clientUriNewValue = "http://www.gluu.org/company/yuriy";
        new ResourceRequestEnvironment.ResourceRequest(new ResourceRequestEnvironment(this),
                ResourceRequestEnvironment.Method.PUT, registerPath) {

            @Override
            protected void prepareRequest(EnhancedMockHttpServletRequest request) {
                super.prepareRequest(request);
                try {

                    final RegisterRequest registerRequest = new RegisterRequest(ApplicationType.WEB, "oxAuth test app",
                            StringUtils.spaceSeparatedToList(redirectUris));

                    registerRequest.setContacts(Arrays.asList("test_user@gluu.org", contactEmailNewValue));
                    registerRequest.setLogoUri(logoUriNewValue);
                    registerRequest.setClientUri(clientUriNewValue);

                    final String registerRequestContent = registerRequest.getJSONParameters().toString(4);
                    request.addHeader("Authorization", "Bearer " + registrationAccessToken1);
                    request.setContentType(MediaType.APPLICATION_JSON);
                    request.setQueryString(registrationClientUri1.substring(registrationClientUri1.indexOf("?") + 1));

                    request.setContent(registerRequestContent.getBytes(Util.UTF8));
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
            }

            @Override
            protected void onResponse(EnhancedMockHttpServletResponse response) {
                super.onResponse(response);
                showResponse("requestClientRead", response);

                readResponseAssert(response);

                try {
                    // check whether values are really updated
                    RegisterRequest r = RegisterRequest.fromJson(response.getContentAsString());
                    assertTrue(r.getContacts() != null && r.getContacts().contains(contactEmailNewValue));
                    assertTrue(r.getClientUri().equals(clientUriNewValue));
                    assertTrue(r.getLogoUri().equals(logoUriNewValue));
                } catch (JSONException e) {
                    e.printStackTrace();
                    fail(e.getMessage() + "\nResponse was: " + response.getContentAsString());
                }
            }
        }.run();
    }

    @Parameters({"registerPath"})
    @Test
    public void requestClientRegistrationFail1(final String registerPath) throws Exception {

        new ResourceRequestEnvironment.ResourceRequest(new ResourceRequestEnvironment(this),
                ResourceRequestEnvironment.Method.POST, registerPath) {

            @Override
            protected void prepareRequest(EnhancedMockHttpServletRequest request) {
                try {
                    super.prepareRequest(request);

                    RegisterRequest registerRequest = new RegisterRequest(null, null, null);

                    request.setContentType(MediaType.APPLICATION_JSON);
                    String registerRequestContent = registerRequest.getJSONParameters().toString(4);
                    request.setContent(registerRequestContent.getBytes());
                } catch (JSONException e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                }
            }

            @Override
            protected void onResponse(EnhancedMockHttpServletResponse response) {
                super.onResponse(response);
                showResponse("requestClientRegistrationFail 1", response);

                assertEquals(response.getStatus(), 400, "Unexpected response code. " + response.getContentAsString());
                TestUtil.assertErrorResponse(response.getContentAsString());
            }
        }.run();
    }

    @Parameters({"registerPath"})
    @Test
    public void requestClientRegistrationFail2(final String registerPath) throws Exception {

        new ResourceRequestEnvironment.ResourceRequest(new ResourceRequestEnvironment(this),
                ResourceRequestEnvironment.Method.POST, registerPath) {

            @Override
            protected void prepareRequest(EnhancedMockHttpServletRequest request) {
                try {
                    super.prepareRequest(request);

                    RegisterRequest registerRequest = new RegisterRequest(ApplicationType.WEB, "oxAuth test app", null); // Missing redirect URIs

                    request.setContentType(MediaType.APPLICATION_JSON);
                    String registerRequestContent = registerRequest.getJSONParameters().toString(4);
                    request.setContent(registerRequestContent.getBytes());
                } catch (JSONException e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                }
            }

            @Override
            protected void onResponse(EnhancedMockHttpServletResponse response) {
                super.onResponse(response);
                showResponse("requestClientRegistrationFail 2", response);

                assertEquals(response.getStatus(), 400, "Unexpected response code. " + response.getContentAsString());
                TestUtil.assertErrorResponse(response.getContentAsString());
            }
        }.run();
    }

    @Parameters({"registerPath"})
    @Test
    public void requestClientRegistrationFail3(final String registerPath) throws Exception {

        new ResourceRequestEnvironment.ResourceRequest(new ResourceRequestEnvironment(this),
                ResourceRequestEnvironment.Method.POST, registerPath) {

            @Override
            protected void prepareRequest(EnhancedMockHttpServletRequest request) {
                try {
                    super.prepareRequest(request);

                    RegisterRequest registerRequest = new RegisterRequest(ApplicationType.WEB, "oxAuth test app",
                            Arrays.asList("https://client.example.com/cb#fail_fragment"));

                    request.setContentType(MediaType.APPLICATION_JSON);
                    String registerRequestContent = registerRequest.getJSONParameters().toString(4);
                    request.setContent(registerRequestContent.getBytes());
                } catch (JSONException e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                }
            }

            @Override
            protected void onResponse(EnhancedMockHttpServletResponse response) {
                super.onResponse(response);
                showResponse("requestClientRegistrationFail3", response);

                assertEquals(response.getStatus(), 400, "Unexpected response code. " + response.getContentAsString());
                TestUtil.assertErrorResponse(response.getContentAsString());
            }
        }.run();
    }
}