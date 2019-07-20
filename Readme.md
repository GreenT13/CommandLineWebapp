[![Build Status](https://travis-ci.org/GreenT13/CommandLineWebapp.svg?branch=master)](https://travis-ci.org/GreenT13/CommandLineWebapp)

# My command line web application!
I really like programming, but I dislike creating a GUI for my applications. I like console applications, but I also want to access my code everywhere.
So I combined both of best worlds: I created a webapp with a terminal layout! How nerdy can you get :)

Click here to go to the website: https://command-line-webapp.herokuapp.com/.

## Project setup
Usual SPA + REST-services structure with Java backend:
* Frontend: ReactJS
* Backend: Spring Boot
* Database: PostgreSQL

My project gets build with travis-ci (see build passing icon on the top of this page), and is deployed to heroku.

Note: I don't build ReactJS with npm using the maven-frontend-plugin (a lot of people seem to do this).
I don't want this, since it slows my builds quite a lot. I develop, build and run the projects separately.
When deploying the application, I do need to copy the frontend files to resources/static folder.
Obviously I have automated this by adding the deploy command to the package.json.

You could go one step further by creating separate Github repositories, but I don't want that.
The project is small enough (at this point) that I can afford to place everything here. This is just easier for me.

## Project architecture
The very core of the project is: every command that gets typed, gets send to the backend and is processed fully.
The output is send back to the frontend, and that is it.

Everything around is, is just making it easier for the user. Below the features that are also part of the core.

### Commands in the frontend
I want my backend to be stateless, which means some commands must be done in the frontend (like history).
Also some commands are just really frontend commands (like clear).
Here is a full list of commands that is programmed in the frontend 
* history (showing the history of every command)
* clear (clear the whole screen)
* ???

### Plugin commands
Commands can be added to this web application, by following this API: TODO.
I am really proud to say that you can also add commands during runtime! You can just upload your jar and it works, really cool.

TODO: explain this more in detail on how this works. Maybe refer to another framework?
Awesome stuff with classloaders blabla.

# Installing and working with this project locally
TODO

# Todo list
## Frontend
* Add history command
* Add clear command

## Backend
* Create plugin architecture
* Output my CSV in ASCII art?
* Write tests using JUnit 5 instead of 4.
