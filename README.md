Domain model service which retrieves all definition objects from swagger.yaml files of specified services from the gitlab-adapter service.

When started the service provides following paths:

- on port 8080:

* GET:  /services?resolveReferences=true&serviceNames=service1,service2,service3  
getting objects from specified services, if resolveReferences is set to true, then all #ref and #all of entries of the yaml file are resolved to the fully qualified objects/definitions


- on port 8081:

* GET: /health
* GET: /metrics/
* GET: /metrics/dummyParameter  with DummyParameter as String
