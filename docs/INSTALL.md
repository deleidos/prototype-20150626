# Installation
The following instructions provide the detailed steps to recreate the prototype on a CentOS 7 machine.  The CentOS 7 machine can be 
either a bare-metal install or VM and running on premise or in the cloud provider of your choosing.  The machine to run the prototype has 
the following minimum requirements:

* 2.5GHz Dual-Core Intel or AMD CPU
* 3GB of RAM
* 20GB of disk space
* 100Mbps network connectivity with external access
* CentOS 7.0 (verified against CentOS 7.0.1406).
** The current prototype is running in AWS and using the official CentOS 7 (x86_64) with Updates HVM AMI available in the AWS Marketplace (https://aws.amazon.com/marketplace/pp/B00O7WM7QW/ref=srh_res_product_title?ie=UTF8&sr=0-2&qid=1435867213346).
   At the time the prototype was launched, the AMI ID of the official CentOS 7 AMI used is: ami-96a818fe in the us-east-1 region.

For those not using the AWS AMI (ami-96a818fe), perform a "Minimum Install" of CentOS 7 using the GUI installer that comes with it by default; when prompted, 
create a 'centos' user and set the root user's password.  The 'centos' user must have password-less sudo access in order to run the installation Ansible playbook.
Please use the procedures of your choosing to provide the level of access required.  After the install is complete, ensure that a working network connection 
exists and you are able to reach external sites, such as dockerhub.com.

For those using the AWS AMI (ami-96a818fe), use the AWS console and launch a c3.large instance, but configure the root volume to be 
at least 20GB.  Once the instance is accessible via SSH, log into the instance using your SSH key as the 'centos' user, and issue the following to 
grow the size of the root volume from the default 8GB to 20GB:
The install of gdisk allows for the filesystem to auto expand at start-up for this AMI.
```
sudo yum -y install epel-release
sudo yum -y install gdisk
sudo reboot
```

***

The machine where Ansible runs is typically known as the "Control Machine."  This machine has very minimal requirements as its only purpose is to invoke the Ansible
playbook, which configures the machine to run the prototype.  That being said, the following are the minimum requirements for the "Control Machine":

* 1.5GHz Intel or AMD CPU
* 512MB of RAM
* 10GB of disk space
* 100Mbps network connectivity with SSH access to the machine to be configured to run the prototype
* CentOS 7.0 (verified against CentOS 7.0.1406).

To configure the "Control Machine," perform a "Minimum Install" of CentOS 7 using the GUI installer that comes with it by default, and create the user you desire
to run the Ansible playbook.  After the OS of the "Control Machine" has been installed, log into it and change over to the root user:
```
su - 
<when prompted, enter the root password>
```

As root, install Ansible by running the following:
```
yum -y install epel-release
yum -y install gcc sshpass python-pip python-devel wget
	
pip install ansible==1.8.3 docker-py==1.2.2 boto==2.32.0
	
mkdir -p /etc/ansible
echo '' > /etc/ansible/hosts 
```


***


At this point, the "Control Machine" is sufficiently configured to run the Ansible playbook to configure the machine to run the prototype.  
To configure the machine to run the prototype, execute the following as the non-root user you created at the time of the "Control Machine" install in order
to download the Ansible playbook and prep it for execution:
```
cd $HOME
wget --no-check-certificate -O deployment-playbook.tar.gz "https://jenkins.openfda.deleidos.com/job/prototype-20150626_master/ws/deployment/target/deployment-playbook.tar.gz"
tar -xzvf ./deployment-playbook.tar.gz
```

Using the editor of your choosing, open the ''centos7-standalone/inventory'' and modify the ''public_config'' entry to be the accessible IP
address of the machine you have configured to run the prototype.  Then, save and exit the editor and run the following to 
execute the playbook as the non-root user:
```
cd $HOME/centos7-standalone
ansible-playbook site.yml -i inventory -l public_config -v
```

If you are going to be using a "Control Machine" and target machine for the prototype in AWS, you will have to 
modify the ''private_config'' entry to be the private IP of the EC2 instance to run the prototype, and execute the playbook as the non-root user using
the ''private_config'' section of the inventory. e.g.)
```
cd $HOME/centos7-standalone
ansible-playbook site.yml -i inventory -l private_config -v
```


The playbook will install the necessary packages to run Docker, then load the required Docker container images and 
start them with the proper configuration.  Once complete, the prototype's UI should be accessible via http://IP_OF_MACHINE_RUNNING_PROTOTYPE in either Chrome or Firefox.
