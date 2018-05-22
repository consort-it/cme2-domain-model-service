package com.consort.security;

import org.pac4j.core.authorization.authorizer.RequireAnyAttributeAuthorizer;

public class DomainModelServiceAttributeAuthorizer extends RequireAnyAttributeAuthorizer {

    public DomainModelServiceAttributeAuthorizer(final String attribute, final String valueToMatch) {
        super(valueToMatch);
        setElements(attribute);
    }
}
