package com.consort.entities;

public class IdUtils {
    public static String generateId(String serviceName, String name) {
        return serviceName + "_" + name;
    }

    public static void validateDomainModelObject(DomainModel model) throws Exception {
        final String currentId = model.getId();
        final String calculatedId = generateId(model.getServiceName(), model.getName());
        if (!currentId.equals(calculatedId)) {
            throw new Exception("Validation of DomainModel '" + model.getServiceName() + "/" + model.getName()
                    + "' failed. Id '" + currentId + "' is invalid, should have been '" + calculatedId + "'");
        }
    }
}