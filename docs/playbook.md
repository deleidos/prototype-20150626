Who are the primary users?
- Communication directors for drug manufacturers.
-	Nationwide chain of hospitals, drug stores, etc. Anyone distributing pharmaceuticals over a wide supply chain. Especially if the supply chain is not integrated.

What user needs will this service address?
-	Visual perspective of recalled drugs by geography.

Why does the user want or need this service?
-	PR response to highest affected areas.

Which people will have the most difficulty with the service?
-	TBD

Which research methods were used?
-	In person interviews during prototyping.
-	Daily review and planning meetings involving multidisciplinary skills and perspectives.
-	Agile, iterative, software design and development process

What were the key findings?
-	The geography data published at OpenFDA for recall enforcement is currently oriented toward the manufacturer. However, the geography data associated from the distribution perspective is published in a freeform text field, making it challenging to render on a map for visualization, as well as being able to search for normalized geographies (i.e. MD vs. Maryland).

How were the findings documented? Where can future team members access the documentation?
-	The findings were documented in the project repository, under “docs”.

How often are you testing with “people”?
-	Every other business day during prototyping phase.

What are the different ways (both online and offline) that people currently accomplish the task the digital service is designed to help with?
-	This prototype assumes that isn’t being done automatically based on the data structure provided. For the sake of this prototype, there was not adequate time to determine.

Where are user pain points in the current way people accomplish the task?
-	TBD.

Where does this specific project fit into the larger way people currently obtain the service being offered?
-	TBD.

What metrics will best indicate how well the service is working for its users?
-	Use of Google Analytics for adoption rate.

What primary tasks is the user trying to accomplish?
-	TBD.

Play #3
Is the language as plain and universal as possible?
- The language is what the data is provided in.

What languages is your service offered in?
- English

If a user needs help while using the service, how do they go about getting it?
- N/A. It's just a prototype. They can fill in an issue on GitHub

How does the service’s design visually relate to other government services?
- The graphics were extended from the openFDA website.

Play #4

How long did it take to ship the MVP? If it hasn't shipped yet, when will it?
- Six business days

How long does it take for a production deployment?
- Six to eight weeks.

How many days or weeks are in each iteration/sprint?
- For this prototype, a sprint was 24 hours.
- In general, a sprint is two weeks long

Which version control system is being used?
- Git

How are bugs tracked and tickets issued? What tool is used?
- Git’s Issues tracker

How is the feature backlog managed? What tool is used?
- VersionOne

How often do you review and reprioritize the feature and bug backlog?
- On Sprint boundaries

How do you collect user feedback during development? How is that feedback used to improve the service?
- In person interviews and demos, plus usability testing.

At each stage of usability testing, which gaps were identified in addressing user needs?
- Critical navigation issues.

Play #5

What is the scope of the project? What are the key deliverables?
- Create a prototype using the data at OpenFDA. See RFQ

What are the milestones? How frequent are they?
Three milestones:
Initial GUI and system
Intermediate GUI and system
Final delivery
Every other day

What are the performance metrics defined in the contract (e.g., response time, system uptime, time period to address priority issues)?
- N/A

Play #6

Who is the product owner?
- Matt V. played the role of the product owner

What organizational changes have been made to ensure the product owner has sufficient authority over and support for the project?
- Gary R granted Matt V the necessary authority and support.

What does it take for the product owner to add or remove a feature from the service?
- A hour to a couple days. Depends on complexity, etc.

Play #7

Play #8 checklist
Choose software frameworks that are commonly used by private-sector companies creating similar services
Whenever possible, ensure that software can be deployed on a variety of commodity hardware types
Ensure that each project has clear, understandable instructions for setting up a local development environment, and that team members can be quickly added or removed from projects
Consider open source software solutions at every layer of the stack

Play #8 key questions

What is your development stack and why did you choose it?

Which databases are you using and why did you choose them?
- MongoDB

How long does it take for a new team member to start developing?
- Depending upon expertise, two to four weeks.

Play #9 checklist

Resources are provisioned on demand
Resources scale based on real-time user demand
Resources are provisioned through an API
Resources are available in multiple regions
We only pay for resources we use
Static assets are served through a content delivery network
Application is hosted on commodity hardware

key questions

Where is your service hosted?
- Amazon Web Services

What hardware does your service use to run?
- Virtualized

What is the demand or usage pattern for your service?
- N/A

What happens to your service when it experiences a surge in traffic or load?
- Remains fixed. Prototype is not configured for auto-scaling per estimated load of GSA evaluation.

How much capacity is available in your hosting environment?
- Plenty, if needed.

How long does it take you to provision a new resource, like an application server?

How have you designed your service to scale based on demand?

The service is container based and could be switched to take advantage of AWS’s auto-scaling for containers.

How are you paying for your hosting infrastructure (e.g., by the minute, hourly, daily, monthly, fixed)?
- Hourly charges. Billed monthly.

Is your service hosted in multiple regions, availability zones, or data centers?
- The prototype is hosted in a single region, in a single availability zone. However, AWS has multiple world-wide regions, etc, if needed.

In the event of a catastrophic disaster to a datacenter, how long will it take to have the service operational?
- The prototype could be stood up in a new region in under an hour.
- An operation system would be configured to failover in seconds.

What would be the impact of a prolonged downtime window?
- Loss of evaluation opportunity for response.

What data redundancy do you have built into the system, and what would be the impact of a catastrophic data loss?
- N/A

How often do you need to contact a person from your hosting provider to get resources or to fix an issue?
- Infrequently.

Play #10 checklist

Create automated tests that verify all user-facing functionality
Create unit and integration tests to verify modules and components
Run tests automatically as part of the build process
Perform deployments automatically with deployment scripts, continuous delivery services, or similar techniques
Conduct load and performance tests at regular intervals, including before public launch

key questions

What percentage of the code base is covered by automated tests?
- Approximately 80%
How long does it take to build, test, and deploy a typical bug fix?
- Under an hour. Depends on complexity.
How long does it take to build, test, and deploy a new feature into production?
- A few minutes
How frequently are builds created?
- As needed
What test tools are used?
- Jacco
- Unit Tests
Which deployment automation or continuous integration tools are used?
- Jenkins and Ansible
What is the estimated maximum number of concurrent users who will want to use the system?
- Unknown
How many simultaneous users could the system handle, according to the most recent capacity test?
- Unknown.
How does the service perform when you exceed the expected target usage volume? Does it degrade gracefully or catastrophically?
- Catastrophic. However, it's a prototype.
What is your scaling strategy when demand increases suddenly?
- AWS's container scaling service

Play #11 checklist

Contact the appropriate privacy or legal officer of the department or agency to determine whether a System of Records Notice (SORN), Privacy Impact Assessment, or other review should be conducted
Determine, in consultation with a records officer, what data is collected and why, how it is used or shared, how it is stored and secured, and how long it is kept
Determine, in consultation with a privacy specialist, whether and how users are notified about how personal information is collected and used, including whether a privacy policy is needed and where it should appear, and how users will be notified in the event of a security breach
Consider whether the user should be able to access, delete, or remove their information from the service
“Pre-certify” the hosting infrastructure used for the project using FedRAMP
Use deployment scripts to ensure configuration of production environment remains consistent and controllable

key questions

Does the service collect personal information from the user? How is the user notified of this collection?
- No
Does it collect more information than necessary? Could the data be used in ways an average user wouldn't expect?
- No
How does a user access, correct, delete, or remove personal information?
- N/A
Will any of the personal information stored in the system be shared with other services, people, or partners?
- N/A
How and how often is the service tested for security vulnerabilities?
- N/A
How can someone from the public report a security issue?
- An issue can be reported as a bug: https://github.com/deleidos/prototype-20150626/issues

Play #12 checklist

Monitor system-level resource utilization in real time
Monitor system performance in real-time (e.g. response time, latency, throughput, and error rates)
Ensure monitoring can measure median, 95th percentile, and 98th percentile performance
Create automated alerts based on this monitoring
Track concurrent users in real-time, and monitor user behaviors in the aggregate to determine how well the service meets user needs
Publish metrics internally
Publish metrics externally
Use an experimentation tool that supports multivariate testing in production

key questions

What are the key metrics for the service?
- This is a prototype.
How have these metrics performed over the life of the service?
- N/A
Which system monitoring tools are in place?
- Monitoring whether the prototype is up and alive
What is the targeted average response time for your service? What percent of requests take more than 1 second, 2 seconds, 4 seconds, and 8 seconds?
- N/A
What is the average response time and percentile breakdown (percent of requests taking more than 1s, 2s, 4s, and 8s) for the top 10 transactions?
- N/A
What is the volume of each of your service’s top 10 transactions? What is the percentage of transactions started vs. completed?
- N/A
What is your service’s monthly uptime target?
- N/A
What is your service’s monthly uptime percentage, including scheduled maintenance? Excluding scheduled maintenance?
- N/A
How does your team receive automated alerts when incidents occur?
- Yes
How does your team respond to incidents? What is your post-mortem process?
- The engineering team is notified and an individual will self-assign for the task. Every incident is handled stand-alone and based upon severity of the incident.
Which tools are in place to measure user behavior?
- N/A
What tools or technologies are used for A/B testing?
- N/A for prototype. Otherwise, depends upon client budget and needs.
How do you measure customer satisfaction?
- N/A for prototype.

Play #13 checklist

Offer users a mechanism to report bugs and issues, and be responsive to these reports

Bugs and issues can be reported here: https://github.com/deleidos/prototype-20150626/issues

Provide datasets to the public, in their entirety, through bulk downloads and APIs (application programming interfaces)
 - Source data can be accessed here: http://open.fda.gov
 - Enriched data can be queried here: http://openfda.deleidos.com/apidocs/swaggerui

Ensure that data from the service is explicitly in the public domain, and that rights are waived globally via an international public domain dedication, such as the “Creative Commons Zero” waiver
- Source data governed by terms on https://open.fda.gov/terms/

Catalog data in the agency’s enterprise data inventory and add any public datasets to the agency’s public data listing
- N/A

Ensure that we maintain the rights to all data developed by third parties in a manner that is releasable and reusable at no cost to the public
- N/A

Ensure that we maintain contractual rights to all custom software developed by third parties in a manner that is publishable and reusable at no cost
- All custom software released under Apache 2.0 or other FOSS license

When appropriate, create an API for third parties and internal users to interact with the service directly
- Done ReSTfully

When appropriate, publish source code of projects or components online
- Done in GitHub

When appropriate, share your development process and progress publicly
- Done in GitHub

Play #13 key questions

How are you collecting user feedback for bugs and issues?
- In GitHub: https://github.com/deleidos/prototype-20150626/issues

If there is an API, what capabilities does it provide? Who uses it? How is it documented?
- We use it. It's documented by SwaggerUI.

If the codebase has not been released under an open source license, explain why.
- N/A

What components are made available to the public as open source?
- All components related to the prototype are open source

What datasets are made available to the public?
- All the datasets are public on http://open.fda.gov

