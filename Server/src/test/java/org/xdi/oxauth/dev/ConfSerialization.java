/*
 * oxAuth is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package org.xdi.oxauth.dev;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.xdi.oxauth.model.config.BaseDnConfiguration;
import org.xdi.oxauth.model.config.ClaimMappingConfiguration;
import org.xdi.oxauth.model.config.StaticConf;
import org.xdi.oxauth.model.error.ErrorMessage;
import org.xdi.oxauth.model.error.ErrorMessages;
import org.xdi.oxauth.model.jwk.JSONWebKeySet;
import org.xdi.oxauth.util.ServerUtil;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9 November 12, 2014
 */

public class ConfSerialization {

    public static final String CONFIG_FOLDER = "U:\\own\\project\\oxAuth\\Server\\src\\main\\webapp\\WEB-INF\\";

    public static <T> T loadXml(String p_fileName, Class p_clazz) {
        final URL url = ConfSerialization.class.getResource(p_fileName);
        try {
            System.out.println("Loading configuration file: " + url);

            JAXBContext jc = JAXBContext.newInstance(p_clazz);
            Unmarshaller u = jc.createUnmarshaller();
            return (T) u.unmarshal(url);
        } catch (JAXBException e) {
            System.out.println("Failed to get the configuration file: " + url);
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T loadJson(File p_file, Class p_clazz) {
        try {
            return (T) ServerUtil.createJsonMapper().readValue(p_file, p_clazz);
        } catch (Exception e) {
            System.out.println("Failed to load json from file: " + p_file.getPath());
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void errorXmlDeserializer() throws IOException {
        final ErrorMessages objFromXml = loadXml("oxauth-errors.xml", ConfSerialization.class);
        Assert.assertNotNull(objFromXml);

        final String jsonStr = ServerUtil.createJsonMapper().writeValueAsString(objFromXml);
        System.out.println(jsonStr);
    }

    @Test
    public void errorJsonDeserializer() throws IOException {
        final ErrorMessages object = loadJson(new File(CONFIG_FOLDER + "oxauth-errors.json"), ErrorMessages.class);
        Assert.assertTrue(object != null && notEmpty(object.getAuthorize()) &&
                notEmpty(object.getFederation()) && notEmpty(object.getUma()) && notEmpty(object.getUserInfo()) &&
                notEmpty(object.getClientInfo()) && notEmpty(object.getToken()) && notEmpty(object.getEndSession()));
    }

    @Test
    public void webKeysJsonDeserializer() throws IOException {
        final JSONWebKeySet obj = loadJson(new File(CONFIG_FOLDER + "oxauth-web-keys.json"), JSONWebKeySet.class);
        Assert.assertTrue(obj != null && obj.getKeys() != null && !obj.getKeys().isEmpty());
    }

    private static boolean notEmpty(List<ErrorMessage> p_list) {
        return p_list != null && !p_list.isEmpty();
    }

    @Test
    public void claims() throws IOException {

        final BaseDnConfiguration baseDn = new BaseDnConfiguration();
        baseDn.setAppliance("ou=appliances,o=gluu");
        baseDn.setPeople("ou=people,o=@!1111,o=gluu");
        baseDn.setClients("ou=clients,o=@!1111,o=gluu");
        baseDn.setScopes("ou=scopes,o=@!1111,o=gluu");
        baseDn.setAttributes("ou=attributes,o=@!1111,o=gluu");
        baseDn.setSessionId("ou=session,o=@!1111,o=gluu");
        baseDn.setFederationMetadata("ou=metadata,ou=federation,o=@!1111,o=gluu");
        baseDn.setFederationRP("ou=rp,ou=federation,o=@!1111,o=gluu");
        baseDn.setFederationOP("ou=op,ou=federation,o=@!1111,o=gluu");
        baseDn.setFederationRequest("ou=request,ou=federation,o=@!1111,o=gluu");
        baseDn.setFederationTrust("ou=trust,ou=federation,o=@!1111,o=gluu");

        final List<ClaimMappingConfiguration> claimMappingList = new ArrayList<ClaimMappingConfiguration>();
        claimMappingList.add(new ClaimMappingConfiguration("uid", "sub"));
        claimMappingList.add(new ClaimMappingConfiguration("displayName", "name"));
        claimMappingList.add(new ClaimMappingConfiguration("givenName", "given_name"));
        claimMappingList.add(new ClaimMappingConfiguration("sn", "family_name"));
        claimMappingList.add(new ClaimMappingConfiguration("photo1", "picture"));
        claimMappingList.add(new ClaimMappingConfiguration("timezone", "zoneinfo"));
        claimMappingList.add(new ClaimMappingConfiguration("preferredLanguage", "locale"));
        claimMappingList.add(new ClaimMappingConfiguration("mail", "email"));
        claimMappingList.add(new ClaimMappingConfiguration("homePostalAddress", "formatted"));
        claimMappingList.add(new ClaimMappingConfiguration("street", "street_address"));
        claimMappingList.add(new ClaimMappingConfiguration("l", "locality"));
        claimMappingList.add(new ClaimMappingConfiguration("st", "region"));
        claimMappingList.add(new ClaimMappingConfiguration("postalCode", "postal_code"));
        claimMappingList.add(new ClaimMappingConfiguration("telephoneNumber", "phone_number"));

        final StaticConf c = new StaticConf();
        c.setClaimMapping(claimMappingList);
        c.setBaseDn(baseDn);

        final String jsonStr = ServerUtil.createJsonMapper().writeValueAsString(c);
        System.out.println(jsonStr);
    }
}