/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.xdi.oxauth.model.jwk;

/**
 * @author Javier Rojas Blum Date: 03.19.2013
 */
public interface JWKParameter {

    public static final String JSON_WEB_KEY_SET = "keys";
    public static final String JSON_WEB_KEY = "key";
    public static final String PRIVATE_KEY = "privateKey";
    public static final String PUBLIC_KEY = "publicKey";
    public static final String CERTIFICATE = "certificate";

    // Common
    public static final String KEY_TYPE = "kty";
    public static final String KEY_USE = "use";
    public static final String ALGORITHM = "alg";
    public static final String KEY_ID = "kid";

    // Public Key
    public static final String MODULUS = "n";
    public static final String EXPONENT = "e";
    public static final String CURVE = "crv";
    public static final String X = "x";
    public static final String Y = "y";

    // Private Key
    public static final String PRIVATE_EXPONENT = "d";

    // Symmetric Key
    public static final String KEY_VALUE = "k";

    // X.509 Certificate Chain
    public static final String X5C = "x5c";
}