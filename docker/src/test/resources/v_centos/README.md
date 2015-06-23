The v_centos is a directory that contain Vagrant config files to spin up Virtual Machines, Centos, 
using VirtualBox & Vagrant locally to test building container images from this project.  To use them, after
you have both VirtualBox and Vagrant installed, simply from a CMD prompt, run 

vagrant up


The provisioned VM will have all the necessary components to be able to test out the building of the docker 
containers either manually via the docker build or through the Maven building of this project which should
do the same.