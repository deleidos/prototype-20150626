## Build and Install

**Prerequisites**  
It is assumed that the OS used to build the software does not require a proxy in order access to the internet for things like yum installs, maven dependency
resolution, etc....  If you are behind a proxy, please refer to the technology specific documentation for the prerequisites below 
on how to configure their installation and use to work behind a proxy.

**If building the code on a Windows 7 or better OS**  
* Sun JDK 1.7.x
* Apache Maven 3.x
* MongoDB 2.4.x

For Windows users who would like to use Virtualbox and Vagrant to provision a Centos 7 VM, Vagrantfile(s) are provided in this repo's 
docker project for use.  Please see a brief description at the top of the Vagrantfile contained wherein for additional information.

**If building on a Centos 7.x**  
* OpenJDK 1.7.x
* Apache Maven 3.3.x
* Docker 1.6.x
* MongoDB 2.4.x

Once the above dependencies are satisfied, building the code including execution of applicable tests, can be
achieved by executing the following where you have cloned this repo:

```bash
mvn clean install
``` 

