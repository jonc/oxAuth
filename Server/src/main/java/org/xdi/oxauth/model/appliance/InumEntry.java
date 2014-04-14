package org.xdi.oxauth.model.appliance;

import org.gluu.site.ldap.persistence.annotation.LdapAttribute;
import org.gluu.site.ldap.persistence.annotation.LdapEntry;
import org.gluu.site.ldap.persistence.annotation.LdapObjectClass;

/**
 * Provides global inum search ability.
 * @author Oleksiy Tataryn
 *
 */
@LdapEntry
@LdapObjectClass(values = { "top" })
public class InumEntry extends Entry {

	@LdapAttribute(ignoreDuringUpdate = true)
	private String inum;

	/**
	 * @param inum the inum to set
	 */
	public void setInum(String inum) {
		this.inum = inum;
	}


	/**
	 * @return the inum
	 */
	public String getInum() {
		return inum;
	}


	@Override
	public String toString() {
		return String.format("Entry [dn=%s, inum=%s]", getDn(), getInum());
	}


}