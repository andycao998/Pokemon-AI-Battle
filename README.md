# Pokemon AI Battle
Pokemon random battle simulator against ChatGPT

![battle_1](https://github.com/user-attachments/assets/c28a854b-4c36-45ea-87f3-a116d6344788) <br/>
![battle_2](https://github.com/user-attachments/assets/96a59c0e-c410-49b2-b13b-33f2225bee37) <br/>
![battle_3](https://github.com/user-attachments/assets/b659af12-8988-4c61-b863-ec0c4c70c9e3) <br/>

## Description

This project is a Pokemon battle simulation system that allows players to battle using random (WIP) Pokemon against an AI. Supporting MVC architecture, it consists of a backend for handling game logic and the battle simulation, a frontend to display the battle interface and battle events, and two-way communication between the components using REST API and Spring server sent events. Players can choose moves, switch Pokemon, and interact with an AI that attempts to react to their moves.

### Background

This is a personal project that served as a challenge to create a full-stack project on something I love, Pokemon. Along the way, I was also forced to use frameworks and libraries I had never worked with before, like React and Spring Boot. Particularly, working with both a frontend and a backend and trying to get them to work together was great experience. 

## Technologies
### Backend
- Java <br/>
- Spring Boot (Spring AI): Integrated with OpenAI API <br/>
- Spring SSE (Server Sent Events): Forward battle events to frontend <br/>
- RAG (Retrieval-Augmented Generation): Built into AI system to simulate battle decisions based on accurate information on Pokemon battle mechanics, moves, etc.

### Frontend
- React JS

### Other
- REST API: For managing frontend-backend interaction.

## Work In Progress
https://pokemon-ai-battle.netlify.app/
