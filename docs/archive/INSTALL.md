# Installation
The following instructions provide the detailed steps to re-create the prototype on a Centos 7 machine.  The Centos 7 machine can be 
either a bare-metal install or a VM in the cloud provider of choice.  The machine to run the prototype has 
the following minimum requirements:

* 1 2.5GHz Dual-Core Intel or AMD CPU
* 3GB of RAM
* 20GB of Disk Space
* 100Mbps Network Connectivity with external access
* Centos 7.0 (verified against Centos 7.0.1406)
** The current prototype is running in AWS and using the official CentOS 7 (x86_64) with Updates HVM AMI available in the AWS Marketplace (https://aws.amazon.com/marketplace/pp/B00O7WM7QW/ref=srh_res_product_title?ie=UTF8&sr=0-2&qid=1435867213346).
   At the time the prototype was launched, the AMI id of the official CentOS 7 AMI used is: ami-96a818fe in the us-east-1 region.

For those not using the AWS AMI (ami-96a818fe), perform a "Minimum Install" of Centos 7 using the GUI installer that comes with it by default, and when prompted, 
create a 'centos' user and set the root user's password.  The 'centos' user must have password-less sudo access in order to run the installation Ansible playbook.
Please use the procedures of your choosing to provide this level of access required.  After the install is complete, ensure that a working network connection 
exists and you are able to reach external sites like dockerhub.com.

For those using the AWS AMI (ami-96a818fe), use the AWS console and launch a c3.large instance, but configure the root volume to be 
at least 20 GB.  Once the instance is accessible via ssh, log into the instance using your ssh key as the 'centos' user, and issue the following to 
grow the size of the root volume from the default 8 GB to 20 GB:
The install of gdisk allows for the filesystem to auto expand at startup for this AMI.
```
sudo yum -y install epel-release
sudo yum -y install gdisk
sudo reboot
```



The machine where Ansible runs is typically known as the "Control Machine".  This machine has very minimial requirements as it's only purpose is to invoke the Ansible
playbook which configures the 

## Control Machine Pre-requisits
* Centos 7 VM