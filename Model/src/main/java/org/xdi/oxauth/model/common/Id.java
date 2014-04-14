package org.xdi.oxauth.model.common;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.resteasy.annotations.providers.jaxb.IgnoreMediaTypes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 26/06/2013
 */
@IgnoreMediaTypes("application/*+json")
@XmlRootElement
public class Id implements Serializable {

    private String id;

    public Id() {
    }

    public Id(String p_id) {
        id = p_id;
    }

    @JsonProperty(value = "id")
    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String p_id) {
        id = p_id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Id");
        sb.append("{id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}