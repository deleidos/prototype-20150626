# prototype-20150626

# Demo
Check out the demo at http://openfda.deleidos.com

[Version 1.0](https://github.com/deleidos/prototype-20150626/tree/1.0.0) of the prototype was created on June 26th at 1:50pm EST.  

***

## Build and Install

Once the [dependencies are satisfied](https://github.com/deleidos/prototype-20150626/wiki#build-and-install), building the code including execution of applicable tests, can be achieved by executing the following where you have cloned this repo:

```bash
mvn clean install
``` 

***

# Approach
The creation of this prototype employed the [Scrum](https://en.wikipedia.org/wiki/Scrum_(software_development)) methodology and principals of the [Lean Startup](https://en.wikipedia.org/wiki/Lean_startup).  

Scrum focuses on developing value-added features collaboratively with the end users emphasizing frequent communication at all levels and delivering working software within short release cycles. This process is especially valuable when detailed requirements are not well understood early in the program while still supporting the release of new capabilities delivered within months.  

Leidos’ SCRUM agile development methodology emphasizes:
+	User-driven participation in software requirements, reviews, and testing of releases  
+	Close partnership and interaction with data owners, to ease integration and reduce risks  
+	Streamlined agility by process reduction, design simplification, and incubation of innovative ideas  
+	A scaled-up set of agile management processes, to support larger, more diverse teams as well as apply agility across all organizations of the program  

![](https://raw.githubusercontent.com/deleidos/prototype-20150626/master/docs/archive/scrum_diagram1.png)

A description of this approach, as applied to the prototype, is as follows:

***

## Day #0: Wednesday, June 18th
RFQ is issued.

## Day #1: Thursday, June 18th

###Authorization and Accountability
Line management authorizes a product manager to assemble a team and create a prototype for submission. ([See the email granting authorization and accountability](https://github.com/deleidos/prototype-20150626/blob/master/docs/archive/authorization.txt))

## Day #2: Friday, June 19th

###Assembly of Multidisciplinary and Collaborative Team
A subset of the Leidos DigitalEdge Team, consisting of a product manager, technical architect, agile coach, interaction designer, front-end web developer, back-end developer and devops engineer, were assembled on Friday afternoon. 

###Human-Centered Design Technique #1: Adopting multidisciplinary skills and perspectives
Since the data source was dictated by the RFQ, the team took the approach of first understanding the data and how it could highlight requirements specified. Implicitedly, the RFQ was a stakeholder in the process, which the team interpretted to mean it would define the relevant marketspace the product manager and team was serving.  

The multidisciplinary team discussed the data available and possibilities for new ways to serve the marketplace. In the ensuing discussion, the team began to focus on remixing the data to answer the question "Which drugs have been recalled in their state?".  

Following this, the technical architect presented options for the prototype given the data available on openFDA and the known timeline for delivery of the prototype. A preliminary architecture was created.  
![](https://raw.githubusercontent.com/deleidos/prototype-20150626/master/docs/archive/whiteboard-drawing_20150619.JPG) 
The interaction designer proposed the following GUI mockup:
![](https://raw.githubusercontent.com/deleidos/prototype-20150626/master/docs/archive/prototype-gui-mockup1.png)

###Human-Centered Design Technique #2: Iterative Design Process
Given this architecture and proposed GUI, the team discussed the prototype and populated tasks into VersionOne. This was the beginning of the interative process in agile, where tasks are specified and estimated, and updated continously during the effort.
![](https://raw.githubusercontent.com/deleidos/prototype-20150626/master/docs/archive/versionone-tasks-screencapture-20150619.jpg)

## Day #3: Monday, June 22nd
  
###Ground Work
The day was mostly spent performing groundwork for the prototype. The Team met mid-day to review progress and update the plan for the next 24 hours. (This was in addition to other non-prototype projects the team was juggling)  

###Responsive Design
An approach using bootstrap was decided on to ensure the prototype would work on multiple devices.

###Modern and Open-Source Technologies
The software stack was chosen for this prototype to include only [open source licensed technologies](https://github.com/deleidos/prototype-20150626/tree/master/docs/licenses). Numerous open source packages are included. Highlighted are Bootstrap, Docker, AngularJS, CentOS, Java, JavaScript, MongoDB, Leaflet and DigitalEdge, to name at least five modern and open-sourced technologies.  

Each of these included open source packages are pre-existing. DigitalEdge is a new addition to open source by Leidos, and is a container-based toolkit for creating big data solutions.

## Day #4: Tuesday, June 23rd

###Human-Centered Design Technique #3: Involvement of the consumer in the design and production process
Now that the team had a notion of what the data could support, it was time to consult with the "People" who were in the food &amp; drug marketspace. For the sake of this prototype, a couple individuals outside of the design/development team took on the role of "People". In reviewing the data with the team, the "People" focused on the particular question of "For a given manufacturer, which states were affected by drugs produced by the manufacturer?".

This was slightly different from the premise that was originally drawn from the data on Friday. It sought to address a customer who was in charge of PR for a manufacturer, and wished to know which states had been most affected by recalls, thus steering where to direct communications about the recalls.

Consequently, the plan for the prototype was narrowed to highlight (1) consuming data from a publically available API; (2) enriching the data record by transforming the freeform text field "distribution_pattern" into structured geographic elements; (3) leveraging a populated data repository to create query-time summarizations and publishing those results via a ReST API; (4) presenting edited elements in an intuitive GUI; and (5) remixing additional information by dynamically querying the source API.

![](https://raw.githubusercontent.com/deleidos/prototype-20150626/master/docs/archive/prototype_architecture-v1b.png)

###Continuous Integration Stood-up
Jenkins was stoodup to poll the repository, and configured to both build and test the software frequently, and also orchestrate the deployment of the Docker containers supporting the prototype to the provisioned EC2 instance. View the "[Build Status](https://jenkins.openfda.deleidos.com/buildStatus/icon?job=prototype-20150626_master)".

###IaaS Provider
Amazon Web Services was selected to be the deployment environment.  
![](https://raw.githubusercontent.com/deleidos/prototype-20150626/master/docs/archive/aws-devtest3-screenshot1.png)

## Day #5: Wednesday, June 24th

###Continuous Deployment Stood-up
A Jenkins job allows for the automated deployment of the prototype to one or more Centos 7 instances.   A Vagrantfile is provided in the deployment project which allows configuration of a simple control machine outside of the Jenkins environment to use for development/testing.  View the "[![Deployment Status](https://jenkins.openfda.deleidos.com/job/Deploy_Prototype/badge/icon)](https://jenkins.openfda.deleidos.com/job/Deploy_Prototype/)". 

## Configuration Management 
Ansible was used to orchestrate the installation and configuration of a Centos 7 OS (currently using the official Centos 7 EC2 Amazon Machine Image (AMI) from the AWS Marketplace) used for hosting the prototype.  For alternate deployment environments, any Centos 7 OS (bare metal on prem or in the cloud, or cloud provided VMs) will suffice.

## Continuous Monitoring
Since the prototype is deployed to a Docker execution environment the traditional OS metrics tools (Cloudwatch, Ganglia, Graphite, Nagios, etc..) could be used however; it was chosen to use the similarly awesome Cadvisor monitoring tool to perform both traditional OS and container monitoring.  The Cadvisor interface is 
view-able here: http://openfda.deleidos.com:8080  

Application health monitoring is also something that is traditionally configured to provide near real time information pertaining to the health of the various tiers of the application.  For the purposes of the prototype, a simple set of monitoring check(s) have been added with a very simple notification path in Jenkins.

## Container-based Deployment
Docker was utilized for containerization of the deployed software. The decision to use the power of the Linux container technology, facilitated by Docker, and the decoupling of the deployment of the containers allows for the solution to run either on-prem, or in other cloud providers. Also, by using an Ansible playbook to provision the Centos 7 OS and not a container orchestration tool like the AWS Container Service or Kubernetes, allows for both the documentation of the OS packages required to run the prototype as well as the ability to provide a heterogeneous execution environment for the application (1 on prem, 1 in AWS, 1 in GCE; all load balanced through a proxy).

## Day #6: Thursday, June 25th

###Further Iterations
The product manager and interaction designer met again with the "People" to show them the latest version of the prototype and get feedback for further refinements.  

A slightly different layout was discussed to better utilize real estate in different responsive modes. Also, scope was narrowed for Friday to focus on making the GUI stable for rendering remixed data.

###Design Style Guide
A [Color Palette](https://raw.githubusercontent.com/deleidos/prototype-20150626/master/docs/archive/ColorPalette.png) was created for this prototype.

## Day #7: Friday, June 26th

###Unit Testing and Code Coverage
Unit tests were written and committed to the repo. The DevOps environment was configured to [calculate code coverage](https://jenkins.openfda.deleidos.com/job/prototype-20150626_master/lastBuild/jacoco/).
![](https://raw.githubusercontent.com/deleidos/prototype-20150626/master/docs/archive/codecoverage1.png)

###API Documentation
Documentation was added for the [Mongo REST API](http://openfda.deleidos.com/apidocs/swaggerui).

###Usability Testing
Usability testing was conducted by the summer interns, and issues identified were entered into the Issues page on GitHub

## Day #8: Monday, June 29th

###Issues Work-off
High priority issues were addressed. Low-priority issues were left on the backlog.
![](https://raw.githubusercontent.com/deleidos/prototype-20150626/master/docs/archive/issues1.jpg)

&copy; Leidos 2015
