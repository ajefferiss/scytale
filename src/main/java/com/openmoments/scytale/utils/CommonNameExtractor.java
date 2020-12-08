package com.openmoments.scytale.utils;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.util.Optional;

public final class CommonNameExtractor {
    public CommonNameExtractor() {}

    public String extract(String subjectDN) throws InvalidNameException {
        Optional<Rdn> rdn = new LdapName(subjectDN).getRdns()
                .stream()
                .filter(r -> r.getType().equalsIgnoreCase("CN"))
                .findFirst();

        if (rdn.isPresent()) {
            return rdn.get().getValue().toString();
        }

        return "";
    }
}
