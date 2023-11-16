# LLAPSearch



There is a town that requires some resources (food, materials, energy) for the citizens inside or for the establishment of new buildings. The town has a prosperity level in- dicating how well its people are doing. Buildings need to be established to make the town more prosperous and these buildings require some resources. For this project, your task is to design a search agent responsible for finding a plan that will help this town reach a prosperity level of 100. Different actions can affect the prosperity and resources differently.
You have a budget of 100,000 to spend and there is no additional source of income. There is a limit for the amount of resources you can store in this town at any time (50 units per resource). Resources deplete with every action the agent does. To increase the level of a resource, a delivery has to be requested. Consuming resources also results in spending money which is the cost of these resources.
Given an initial description of the state and the possible actions, the agent should be able to search for a plan (if such a plan exists) to get this town to prosperity level 100. In the following sections there are more details on the agent, actions and the implementation
