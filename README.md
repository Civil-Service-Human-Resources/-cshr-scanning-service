# cshr-scanning-service
A proxy service for virus scanning files.

#ClamAV Docker
ClamAV is used as the scanner and you can make use of a docker instance of this that has been built by the HomeOffice.

You can obtain the latest image by executing this command:

docker pull quay.io/ukhomeofficedigital/clamav

To start this image run the following:

docker run -i -p 3310:3310 quay.io/ukhomeofficedigital/clamav:latest /bin/bash

If this is run in your local dev environment the default values for the environment variables will be sufficient.

## Environment variables
The following variables need to be set when starting the container:

1. **av.service.hostname** - The name of the host where the clamav docker container is running. 
1. **av.service.port** - The port where the clamav docker container is running. 
1. **spring.security.user.name** - The basic auth username required to access this service. The default developer value must be overridden for all non developer environments.
1. **spring.security.user.password** - The basic auth password required to access this service. The default developer value must be overridden for all non developer environments.