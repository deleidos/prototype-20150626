# prototype-20150626

Build:
[![Build Status](http://ec2-52-4-234-87.compute-1.amazonaws.com:8080/buildStatus/icon?job=prototype-20150626_master)](http://ec2-52-4-234-87.compute-1.amazonaws.com:8080/job/prototype-20150626_master/)

Deployment:
[![Deployment Status](http://ec2-52-4-234-87.compute-1.amazonaws.com:8080/job/Deploy_Prototype/badge/icon)](http://ec2-52-4-234-87.compute-1.amazonaws.com:8080/job/Deploy_Prototype/)


# Continuous Integration
Jenkins is an awesome piece of software that we have utilized for the continuous integration environment.  Jenkins jobs
are configured to both build and test the software frequently, and also orchestrate the deployment of the Docker containers supporting 
the prototype to the provisioned EC2 instances. Status of the compilation/testing/packaging of the software can 
be viewed at the top of this page via the "Build Status" button.

Typically a Jenkins master/slave CI environment but for the purposes of simplicity and the size of the prototype, a simple single instance 
configuration of Jenkins was provisioned.

# Configuration Management and Continuous Deployment
Ansible was used to orchestrate the installation and configuration of a Centos 7 OS (currently using the official EC2 AMI) used for hosting the prototype. A 
Jenkins job allows for the automated deployment of the prototype to one or more Centos 7 instances, but the Ansible playbook can be run outside of the Jenkins environment 
as was done for the development and testing of the deployment approach.  A Vagrantfile is provided in the deployment project which will allow you 
to configure a simple control machine outside of the Jenkins environment to use for development/testing.  The status of the deploy can be viewed at the 
top of this page via the "Deployment Status" button.
