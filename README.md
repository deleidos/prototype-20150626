# prototype-20150626

Build Status:
[![Build Status](http://ec2-52-4-234-87.compute-1.amazonaws.com:8080/buildStatus/icon?job=prototype-20150626_master)](http://ec2-52-4-234-87.compute-1.amazonaws.com:8080/job/prototype-20150626_master/)

Deployment Status:
[![Deployment Status](http://ec2-52-4-234-87.compute-1.amazonaws.com:8080/job/Deploy_Prototype/badge/icon)](http://ec2-52-4-234-87.compute-1.amazonaws.com:8080/job/Deploy_Prototype/)

# Building the code
## Prerequisites
It is assumed that the OS used to build the software does not require a proxy in order access to the internet for things like yum installs, maven dependency
resolution, etc....  If you are behind a proxy, please refer to the technology specific documentation for the prerequisites below 
on how to configure their installation and use to work behind a proxy.

### If building the code on a Windows 7 or better OS
* Sun JDK 1.7.x
* Apache Maven 3.x
* MongoDB 3.x

For Windows users who would like to use Virtualbox and Vagrant to provision a Centos 7 VM, Vagrantfile(s) are provided in this repo's 
docker & deployment projects for use.  Please see a brief description at the top of each respectively for additional information.

### If building on a Centos 7 or better OS
* OpenJDK 1.7.x
* Apache Maven 3.3.x
* Docker 1.6.x
* MongoDB 3.x

Once the above dependencies are satisfied, building the code including execution of applicable tests, can be
achieved by executing the following where you have cloned this repo:

```bash
mvn clean install
``` 
 

# Continuous Integration
Jenkins is an awesome piece of software that we have utilized for the continuous integration environment.  Jenkins jobs
are configured to both build and test the software frequently, and also orchestrate the deployment of the Docker containers supporting 
the prototype to the provisioned EC2 instances. Status of the compilation/testing/packaging of the software can 
be viewed at the top of this page via the "Build Status" button.

Typically a Jenkins master/slave CI environment but for the purposes of simplicity and the size of the prototype, a simple single instance 
configuration of Jenkins was provisioned.

# Deployment
Ansible was used to orchestrate the installation and configuration of a Centos 7 OS (currently using the official EC2 AMI) used for hosting the prototype. A 
Jenkins job allows for the automated deployment of the prototype to one or more Centos 7 instances, but the Ansible playbook can be run outside of the Jenkins environment 
as was done for the development and testing of the deployment approach.  A Vagrantfile is provided in the deployment project which will allow you 
to configure a simple control machine outside of the Jenkins environment to use for development/testing.  The status of the deploy can be viewed at the 
top of this page via the "Deployment Status" button.