# prototype-20150626

## Demo
Check out the demo at http://openfda.deleidos.com

[Version 1.0](https://github.com/deleidos/prototype-20150626/tree/1.0.0) of the prototype was created on June 26th at 1:50pm EST.  

## Status
Continuous Integration:
[![Build Status](https://jenkins.openfda.deleidos.com/buildStatus/icon?job=prototype-20150626_master)](https://jenkins.openfda.deleidos.com/job/prototype-20150626_master)

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

![](https://raw.githubusercontent.com/deleidos/prototype-20150626/master/docs/archive/scrum_diagram1.png)

At the beginning of the prototyping effort, a product manager was designated and a multidisciplinary team assembled. The team was drawn from Leidos' DigitalEdge Team, which is a highly collaborative team supporting numerous ongoing efforts.

Generally, the team operates on short iterations of two weeks, called sprints, with daily status meetings. For this rapid prototype response, the team operated on 24-hours Sprints for the first three business days and then settled into daily scrums for the remainder of the prototyping period before submission. During this time, the team intermixed meetings with the "People" every other day and adjusted course based upon feedback, as well as usability testing. 

In addition to the team working with the end-user to create the prototype, the team also began setting up and working in the DevOps environment. This included setting up a GitHub repository, Jenkins, an AWS account and automated processes. 
  
In summary, for iterative projects such as this prototype, Leidos employs an agile development methodology as it is especially well suited to programs with unclear or evolving mission requirements. For more than a decade, Leidos has fine-tuned our agile development process across many customer engagements to produce optimized, cost effective and mission impacting results. We believe implementation of an agile approach will best enable our clients to thrive and adapt to changing needs, resources and schedules.  

The wiki contains [a more specific presentation of the timeline unfolding using this approach](https://github.com/deleidos/prototype-20150626/wiki/Prototype-Timeline). 

## Design

In creation of the prototype, the following human-centered design techniques were utilitized: (1) adopting multidisciplinary skills and perspectives; (2) involvment of the consumer in the design and production process and (3) an iterative design process.

For the sake of this prototype, a couple individuals outside of the design/development team took on the role of "People". They asked narrowed to asking two questions: (1) "Which drugs have been recalled in their state?" and (2) "For a given manufacturer, which states were affected by drugs produced by the manufacturer?".

## Development Focus

This prototype was built to highlight the technical aspects of (1) consuming data from a publically available API; (2) enriching the data record by transforming the freeform text field "distribution_pattern" into structured geographic elements; (3) leveraging a populated data repository to create query-time summarizations and publishing those results via a ReST API; (4) presenting the data elements in an intuitive GUI; and (5) enabling the GUI to add supplementary information by dynamically querying the source API.

![](https://raw.githubusercontent.com/deleidos/prototype-20150626/master/docs/archive/prototype_architecture-v1b.png)

## Build and Install

Once the [dependencies are satisfied](https://github.com/deleidos/prototype-20150626/wiki#build-and-install), building the code including execution of applicable tests, can be achieved by executing the following where you have cloned this repo:

```bash
mvn clean install
``` 

## Software Licensing and Technologies

The software stack for this prototype includes [numerous open source licensed technologies](https://github.com/deleidos/prototype-20150626/wiki#licenses). Specifically, Bootstrap, Docker, AngularJS, CentOS, MongoDB, Leaflet and DigitalEdge, to name at least five modern and open-sourced technologies.  

Leidos' DigitalEdge Team is well versed in many open source technologies. For this prototype, we took the opportunity to learn AngularJS and found it to be a very useful framework.

## Documentation
Be sure to [check out the wiki](https://github.com/deleidos/prototype-20150626/wiki) to learn more about the prototype, the team and the process:
+ [Timeline of Prototype Creation](https://github.com/deleidos/prototype-20150626/wiki/Prototype-Timeline)
+ [DevOps Details](https://github.com/deleidos/prototype-20150626/wiki/DevOps)
+ [Add More!](https://github.com/deleidos/prototype-20150626/wiki)

&copy; Leidos 2015
