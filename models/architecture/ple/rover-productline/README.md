Structure of the model(s)
===

Use Case Model
---
Contains the top level Use Cases. Typically, they will be accompanied 
by requirements and linked to them. This has been omitted here.

The top-level use cases will be the anchor points for the features of 
the product line. 

System Architecture Model
---
Contains the physical and functional break down of the system.
Also, it shows where each function is executed.
The functional break-down results in software or hardware functions 
(modeled as activities). 
All functions will be allocated to blocks of the physical breakdown.  

Software Architecture Model
---
Shows the architecture of the software. This models picks up at the 
functions modeled in the *System Architecture Model* and maps them to 
*use cases* of the software.
The model shows the component structure, the interactions realizing 
the use cases, the realization as artifacts, and the 
deployment of artifacts.

Product Line Engineering
===

Profile
---
The profile defines 9 stereotypes:

* Feature (Class): Features will be modeled using Classes in the 
variant models. They will be mapped to system functions and elements.
Features will be selected or de-selected by *Variants*.
* Variant (Class): Variants will be  modeled using Classes. Variants 
are linked to *Features* to express their functional and structural 
configuration. 

* optional (Association): A Link between a parent and a child Feature.
Marks the child Feature as optional choice.
* mandatory (Association): A Link between a parent and a child Feature.
Marks the child Feature as mandatory choice.

* Select (Dependency): A link between a *Variant* and a child Feature.
Marks the Feature as part of the *Variant* configuration.
* Unselect (Dependency): A link between a *Variant* and a child Feature.
Marks the Feature as not in the *Variant* configuration.
* Imply (Dependency): A link between a *Feature* and an element of the 
system architecture. The element is either a *block* or a *use case*. 
If the *Feature* is selected for a *Variant* the system architecture 
element has to be part of the final product.

* XOR (Constraint): A link between two *Features*. Both can not be 
selected by a *Variant*.
* OR (Constraint): A link between two *Features*. Either one or both 
can be selected by a *Variant*.

Feature and Variant Model
---
The Feature and Variant model contains 
* the Feature Model containing all Features, their hierarchy and 
constraints between them.
* the implications of Features on the system architecture
* Variants and their specific configuration


