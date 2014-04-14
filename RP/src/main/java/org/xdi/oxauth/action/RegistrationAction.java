package org.xdi.oxauth.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.log.Log;
import org.xdi.oxauth.client.RegisterClient;
import org.xdi.oxauth.client.RegisterRequest;
import org.xdi.oxauth.client.RegisterResponse;
import org.xdi.oxauth.model.common.AuthenticationMethod;
import org.xdi.oxauth.model.common.GrantType;
import org.xdi.oxauth.model.common.ResponseType;
import org.xdi.oxauth.model.common.SubjectType;
import org.xdi.oxauth.model.crypto.encryption.BlockEncryptionAlgorithm;
import org.xdi.oxauth.model.crypto.encryption.KeyEncryptionAlgorithm;
import org.xdi.oxauth.model.crypto.signature.SignatureAlgorithm;
import org.xdi.oxauth.model.register.ApplicationType;
import org.xdi.oxauth.model.util.StringUtils;

import java.util.List;

/**
 * @author Javier Rojas Blum Date: 02.20.2012
 */
@Name("registrationAction")
@Scope(ScopeType.SESSION)
@AutoCreate
public class RegistrationAction {

    @Logger
    private Log log;

    private String registrationEndpoint;
    private String redirectUris;
    private List<ResponseType> responseTypes;
    private List<GrantType> grantTypes;
    private ApplicationType applicationType;
    private String contacts;
    private String clientName;
    private String logoUri;
    private String clientUri;
    private AuthenticationMethod tokenEndpointAuthMethod;
    private String policyUri;
    private String tosUri;
    private String jwksUri;
    private String sectorIdentifierUri;
    private SubjectType subjectType;
    private SignatureAlgorithm requestObjectSigningAlg;
    private SignatureAlgorithm userInfoSignedResponseAlg;
    private KeyEncryptionAlgorithm userInfoEncryptedResponseAlg;
    private BlockEncryptionAlgorithm userInfoEncryptedResponseEnc;
    private SignatureAlgorithm idTokenSignedResponseAlg;
    private KeyEncryptionAlgorithm idTokenEncryptedResponseAlg;
    private BlockEncryptionAlgorithm idTokenEncryptedResponseEnc;
    private Integer defaultMaxAge;
    private Boolean requireAuthTime;
    private String defaultAcrValues;
    private String initiateLoginUri;
    private String postLogoutRedirectUris;
    private String requestUris;

    private String registrationClientUri;
    private String registrationAccessToken;

    private boolean showResults;
    private String requestString;
    private String responseString;

    private boolean showClientReadResults;
    private String clientReadRequestString;
    private String clientReadResponseString;

    @In
    private AuthorizationAction authorizationAction;
    @In
    private TokenAction tokenAction;

    public void exec() {
        try {
            RegisterRequest request = new RegisterRequest(applicationType, clientName, StringUtils.spaceSeparatedToList(redirectUris));
            request.setResponseTypes(responseTypes);
            request.setGrantTypes(grantTypes);
            request.setContacts(StringUtils.spaceSeparatedToList(contacts));
            request.setLogoUri(logoUri);
            request.setClientUri(clientUri);
            request.setTokenEndpointAuthMethod(tokenEndpointAuthMethod);
            request.setPolicyUri(policyUri);
            request.setTosUri(tosUri);
            request.setJwksUri(jwksUri);
            request.setSectorIdentifierUri(sectorIdentifierUri);
            request.setSubjectType(subjectType);
            request.setRequestObjectSigningAlg(requestObjectSigningAlg);
            request.setUserInfoSignedResponseAlg(userInfoSignedResponseAlg);
            request.setUserInfoEncryptedResponseAlg(userInfoEncryptedResponseAlg);
            request.setUserInfoEncryptedResponseEnc(userInfoEncryptedResponseEnc);
            request.setIdTokenSignedResponseAlg(idTokenSignedResponseAlg);
            request.setIdTokenEncryptedResponseAlg(idTokenEncryptedResponseAlg);
            request.setIdTokenEncryptedResponseEnc(idTokenEncryptedResponseEnc);
            request.setDefaultMaxAge(defaultMaxAge);
            request.setRequireAuthTime(requireAuthTime);
            request.setDefaultAcrValues(StringUtils.spaceSeparatedToList(defaultAcrValues));
            request.setInitiateLoginUri(initiateLoginUri);
            request.setPostLogoutRedirectUris(StringUtils.spaceSeparatedToList(postLogoutRedirectUris));
            request.setRequestUris(StringUtils.spaceSeparatedToList(requestUris));

            RegisterClient client = new RegisterClient(registrationEndpoint);
            client.setRequest(request);
            RegisterResponse response = client.exec();

            if (response.getStatus() == 200) {
                registrationClientUri = response.getRegistrationClientUri();
                registrationAccessToken = response.getRegistrationAccessToken();
                authorizationAction.setClientId(response.getClientId());
                authorizationAction.setClientSecret(response.getClientSecret());
                if (request.getRedirectUris() != null && request.getRedirectUris().size() > 0) {
                    authorizationAction.setRedirectUri(request.getRedirectUris().get(0));
                }
                tokenAction.setClientId(response.getClientId());
                tokenAction.setClientSecret(response.getClientSecret());
            }

            showResults = true;
            requestString = client.getRequestAsString();
            responseString = client.getResponseAsString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void execClientRead() {
        try {
            RegisterRequest registerRequest = new RegisterRequest(registrationAccessToken);

            RegisterClient client = new RegisterClient(registrationClientUri);
            client.setRequest(registerRequest);
            client.exec();

            showClientReadResults = true;
            clientReadRequestString = client.getRequestAsString();
            clientReadResponseString = client.getResponseAsString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public String getRegistrationEndpoint() {
        return registrationEndpoint;
    }

    public void setRegistrationEndpoint(String registrationEndpoint) {
        this.registrationEndpoint = registrationEndpoint;
    }

    public String getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    public List<ResponseType> getResponseTypes() {
        return responseTypes;
    }

    public void setResponseTypes(List<ResponseType> responseTypes) {
        this.responseTypes = responseTypes;
    }

    public List<GrantType> getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(List<GrantType> grantTypes) {
        this.grantTypes = grantTypes;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getClientUri() {
        return clientUri;
    }

    public void setClientUri(String clientUri) {
        this.clientUri = clientUri;
    }

    public AuthenticationMethod getTokenEndpointAuthMethod() {
        return tokenEndpointAuthMethod;
    }

    public void setTokenEndpointAuthMethod(AuthenticationMethod tokenEndpointAuthMethod) {
        this.tokenEndpointAuthMethod = tokenEndpointAuthMethod;
    }

    public String getPolicyUri() {
        return policyUri;
    }

    public void setPolicyUri(String policyUri) {
        this.policyUri = policyUri;
    }

    public String getTosUri() {
        return tosUri;
    }

    public void setTosUri(String tosUri) {
        this.tosUri = tosUri;
    }

    public String getJwksUri() {
        return jwksUri;
    }

    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }

    public String getSectorIdentifierUri() {
        return sectorIdentifierUri;
    }

    public void setSectorIdentifierUri(String sectorIdentifierUri) {
        this.sectorIdentifierUri = sectorIdentifierUri;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    public SignatureAlgorithm getRequestObjectSigningAlg() {
        return requestObjectSigningAlg;
    }

    public void setRequestObjectSigningAlg(SignatureAlgorithm requestObjectSigningAlg) {
        this.requestObjectSigningAlg = requestObjectSigningAlg;
    }

    public SignatureAlgorithm getUserInfoSignedResponseAlg() {
        return userInfoSignedResponseAlg;
    }

    public void setUserInfoSignedResponseAlg(SignatureAlgorithm userInfoSignedResponseAlg) {
        this.userInfoSignedResponseAlg = userInfoSignedResponseAlg;
    }

    public KeyEncryptionAlgorithm getUserInfoEncryptedResponseAlg() {
        return userInfoEncryptedResponseAlg;
    }

    public void setUserInfoEncryptedResponseAlg(KeyEncryptionAlgorithm userInfoEncryptedResponseAlg) {
        this.userInfoEncryptedResponseAlg = userInfoEncryptedResponseAlg;
    }

    public BlockEncryptionAlgorithm getUserInfoEncryptedResponseEnc() {
        return userInfoEncryptedResponseEnc;
    }

    public void setUserInfoEncryptedResponseEnc(BlockEncryptionAlgorithm userInfoEncryptedResponseEnc) {
        this.userInfoEncryptedResponseEnc = userInfoEncryptedResponseEnc;
    }

    public SignatureAlgorithm getIdTokenSignedResponseAlg() {
        return idTokenSignedResponseAlg;
    }

    public void setIdTokenSignedResponseAlg(SignatureAlgorithm idTokenSignedResponseAlg) {
        this.idTokenSignedResponseAlg = idTokenSignedResponseAlg;
    }

    public KeyEncryptionAlgorithm getIdTokenEncryptedResponseAlg() {
        return idTokenEncryptedResponseAlg;
    }

    public void setIdTokenEncryptedResponseAlg(KeyEncryptionAlgorithm idTokenEncryptedResponseAlg) {
        this.idTokenEncryptedResponseAlg = idTokenEncryptedResponseAlg;
    }

    public BlockEncryptionAlgorithm getIdTokenEncryptedResponseEnc() {
        return idTokenEncryptedResponseEnc;
    }

    public void setIdTokenEncryptedResponseEnc(BlockEncryptionAlgorithm idTokenEncryptedResponseEnc) {
        this.idTokenEncryptedResponseEnc = idTokenEncryptedResponseEnc;
    }

    public Integer getDefaultMaxAge() {
        return defaultMaxAge;
    }

    public void setDefaultMaxAge(Integer defaultMaxAge) {
        this.defaultMaxAge = defaultMaxAge;
    }

    public Boolean getRequireAuthTime() {
        return requireAuthTime;
    }

    public void setRequireAuthTime(Boolean requireAuthTime) {
        this.requireAuthTime = requireAuthTime;
    }

    public String getDefaultAcrValues() {
        return defaultAcrValues;
    }

    public void setDefaultAcrValues(String defaultAcrValues) {
        this.defaultAcrValues = defaultAcrValues;
    }

    public String getInitiateLoginUri() {
        return initiateLoginUri;
    }

    public void setInitiateLoginUri(String initiateLoginUri) {
        this.initiateLoginUri = initiateLoginUri;
    }

    public String getPostLogoutRedirectUris() {
        return postLogoutRedirectUris;
    }

    public void setPostLogoutRedirectUris(String postLogoutRedirectUris) {
        this.postLogoutRedirectUris = postLogoutRedirectUris;
    }

    public String getRequestUris() {
        return requestUris;
    }

    public void setRequestUris(String requestUris) {
        this.requestUris = requestUris;
    }

    public String getRegistrationClientUri() {
        return registrationClientUri;
    }

    public void setRegistrationClientUri(String registrationClientUri) {
        this.registrationClientUri = registrationClientUri;
    }

    public String getRegistrationAccessToken() {
        return registrationAccessToken;
    }

    public void setRegistrationAccessToken(String registrationAccessToken) {
        this.registrationAccessToken = registrationAccessToken;
    }

    public boolean isShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }

    public String getRequestString() {
        return requestString;
    }

    public void setRequestString(String requestString) {
        this.requestString = requestString;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public boolean isShowClientReadResults() {
        return showClientReadResults;
    }

    public void setShowClientReadResults(boolean showClientReadResults) {
        this.showClientReadResults = showClientReadResults;
    }

    public String getClientReadRequestString() {
        return clientReadRequestString;
    }

    public void setClientReadRequestString(String clientReadRequestString) {
        this.clientReadRequestString = clientReadRequestString;
    }

    public String getClientReadResponseString() {
        return clientReadResponseString;
    }

    public void setClientReadResponseString(String clientReadResponseString) {
        this.clientReadResponseString = clientReadResponseString;
    }
}