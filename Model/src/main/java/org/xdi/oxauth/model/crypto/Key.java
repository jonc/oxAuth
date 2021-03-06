/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.xdi.oxauth.model.crypto;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.xdi.oxauth.model.common.JSONable;
import org.xdi.oxauth.model.util.StringUtils;

/**
 * @author Javier Rojas Blum
 * @version 0.9, 09/23/2014
 */
public class Key<E extends  PrivateKey, F extends PublicKey> implements JSONable {

    private String keyType;
    private String use;
    private String algorithm;
    private String keyId;
    private Object curve;
    private E privateKey;
    private F publicKey;
    private Certificate certificate;

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public Object getCurve() {
        return curve;
    }

    public void setCurve(Object curve) {
        this.curve = curve;
    }

    public E getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(E privateKey) {
        this.privateKey = privateKey;
    }

    public F getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(F publicKey) {
        this.publicKey = publicKey;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("keyType", getKeyType());
        jsonObject.put("use", getUse());
        jsonObject.put("algorithm", getAlgorithm());
        jsonObject.put("keyId", getKeyId());
        jsonObject.put("curve", getCurve());
        jsonObject.put("privateKey", getPrivateKey().toJSONObject());
        jsonObject.put("publicKey", getPublicKey().toJSONObject());
        jsonObject.put("certificateChain", getCertificate().toJSONArray());

        return jsonObject;
    }

    @Override
    public String toString() {
        try {
            return toJSONObject().toString(4).replace("\\/", "/");
        } catch (JSONException e) {
            return StringUtils.EMPTY_STRING;
        } catch (Exception e) {
            return StringUtils.EMPTY_STRING;
        }
    }
}