# prototype-20150626

## Demo
Click below to view the working prototype and enjoy:  
http://openfda.deleidos.com

## Status
Continuous Integration:
[![Build Status](https://jenkins.openfda.deleidos.com/buildStatus/icon?job=Deploy_Prototype)](https://jenkins.openfda.deleidos.com/job/Deploy_Prototype/)

Continuous Deployment:
[![Deployment Status](https://jenkins.openfda.deleidos.com/job/Deploy_Prototype/badge/icon)](https://jenkins.openfda.deleidos.com/job/Deploy_Prototype/)  

## Approach
The creation of this prototype employed the [Scrum](https://en.wikipedia.org/wiki/Scrum_(software_development)) methodology and principals of the [Lean Startup](https://en.wikipedia.org/wiki/Lean_startup).  

Scrum focuses on developing value-added features collaboratively with the end users emphasizing frequent communication at all levels and delivering working software within short release cycles. This process is especially valuable when detailed requirements are not well understood early in the program while still supporting the release of new capabilities delivered within months.  

Leidos’ SCRUM agile development methodology emphasizes:
+	User-driven participation in software requirements, reviews, and testing of releases  
+	Close partnership and interaction with data owners, to ease integration and reduce risks  
+	Streamlined agility by process reduction, design simplification, and incubation of innovative ideas  
+	A scaled-up set of agile management processes, to support larger, more diverse teams as well as apply agility across all organizations of the program  

For the sake of this prototype, a couple individuals outside of the design/development team took on the role of "People". They asked two questions: (1) "Which drugs have been recalled in their state?" and (2) "For a given manufacturer, which states were affected by drugs produced by the manufacturer?".

Generally, the team operates on short iterations of two weeks, called sprints, with daily status meetings. For this rapid prototype response, the team operated on 24-hours Sprints for the first three business days and then settled into daily scrums for the remainder of the prototyping period before submission. During this time, the team intermixed meetings with the "People" every other day and adjusted course based upon feedback, as well as usability testing. 

In addition to the team working with the end-user to create the prototype, the team also began setting up and working in the DevOps environment. This included setting up a GitHub repository, Jenkins, an AWS account and automated processes. The wiki contains [a more specific presentation of this timeline](https://github.com/deleidos/prototype-20150626/wiki/Prototype-Timeline). 
  
In summary, for iterative projects such as this prototype, Leidos will employ an agile development methodology as it is especially well suited to programs with unclear or evolving mission requirements. For more than a decade, Leidos has fine-tuned our agile development process across many customer engagements to produce optimized, cost effective and mission impacting results. We believe implementation of an agile approach will best enable our clients to thrive and adapt to changing needs, resources and schedules.  

## Technical Focus

This prototype is meant to highlight the technical aspects of (1) consuming data from a publically available API; (2) enriching the data record by transforming a freeform text field into structured elements; (3) leveraging a populated data repository to create query-time summarizations and publishing those results via a ReST API; (4) presenting the data elements in an intuitive GUI; and (5) enabling the GUI to add supplementary information by dynamically querying the source API.

## Build and Install

Once the [dependencies are satisfied](https://github.com/deleidos/prototype-20150626/wiki#build-and-install), building the code including execution of applicable tests, can be achieved by executing the following where you have cloned this repo:

```bash
mvn clean install
``` 

## Software Stack and Licensing

The software stack for this prototype includes numerous open source technologies. Specifically, Docker, AngularJS, CentOS, MongoDB and DigitalEdge, to name five modern and open-sourced technologies. The entire software stack is [licensed under an open source license](https://github.com/deleidos/prototype-20150626/wiki#licenses).

## Wiki
Be sure to check out the wiki to learn more about the prototype, the team and the process:
+ [Timeline of Prototype Creation](https://github.com/deleidos/prototype-20150626/wiki/Prototype-Timeline)
+ [DevOps Details](https://github.com/deleidos/prototype-20150626/wiki/DevOps)
+ [HowTo Build and Install](https://github.com/deleidos/prototype-20150626/wiki#build-and-install)

&copy; Leidos 2015
