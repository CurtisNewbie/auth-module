# authmodule

Module for web security, user authentication, and integration with **auth-service** (https://github.com/CurtisNewbie/auth-service)

## Modules and Dependencies

This project depends on the following modules that you must manually install (using `mvn clean install`).

- auth-service-remote
    - description: API layer for using auth-service (via RPC calls)
    - url: https://github.com/CurtisNewbie/auth-service
    - branch: main 

- common-module
    - description: for common utility classes 
    - url: https://github.com/CurtisNewbie/common-module
    - branch: main

- service-module
    - description: import dependencies for a Dubbo service
    - url: https://github.com/CurtisNewbie/service-module
    - branch: main

- log-tracing-module
    - desription: for log tracing between web endpoints and service layers
    - url: https://github.com/CurtisNewbie/log-tracing-module
    - branch: main