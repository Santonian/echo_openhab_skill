# echo_openhab_skill

This is a Skill for the Amazon Echo (Alexa) device. I used JAVA and the DropWizard framework. 
The skill can handle diffrent commands to operate a local openHab Installation per REST interface and perform diffrent openHab actions like:
- turn on/off lights or sockets
- dimm lights
- raise or lower rollershutter
- aks for temperature values
- operating the tv
- setting the color of RGB lights

The skill is designed to call a local openHab installation. There is not authentication implemented to call openHab via username/password and https. (However this could be added easily)



## SetUp and Requirements

### General


- you need an account at the amazon developer page (https://developer.amazon.com) and create a skill for your echo device. If you have a amazon account you can just log in with that. (You should read this first: https://developer.amazon.com/appsandservices/solutions/alexa/alexa-skills-kit/getting-started-guide)

- the intent schema and sample utterances I used can be found in the src/main/resources/slu directory

- you need to download your copy of alexa-skills-kit-x.x.jar from the amazon server and add it to your CLASSPATH. It is not included in the project

- you have to edit the echo.yaml file to fit your needs

- to start the project, you can run the EchoOpenhabSkillApplication and add "server echo.yaml" as arguments

- I added a UI for storing the openHab items in the MongoDb. It is accessible with this URL https://localhost/items.html. Keep in mind, that I have no security implemented for the UI! (I am planing to do that)


### MongoDB Connection
For storing the various openHab items, you need a MongoDB Installation. The configuration is in the echo.yaml file.
You can get your MongoDB here: https://www.mongodb.org/


### Generating self-signed certifacate for Amazon
For the Skill Webservice to be accessible from the Amazon Server, you need an SSL certificate for the https connection. You can use a self signed certificate for testing purposes. That's what I did, because it's just me using the skill.

To create a Self Signed Certificate you can follow the Amazon Guide:
https://developer.amazon.com/appsandservices/solutions/alexa/alexa-skills-kit/docs/testing-an-alexa-skill#create-a-private-key-and-self-signed-certificate-for-testing

After that, do this:
keytool -import -file certificate.pem -keystore keystore.jks

However that did not work for me, I got an "no cipher suite in common" exception on my WebService , so I had to do this:

openssl pkcs12 -export -name myservercert -in certificate.pem -inkey private-key.pem -out keystore.p12
keytool -importkeystore -destkeystore keystore.jks -srckeystore keystore.p12 -srcstoretype pkcs12 -alias myservercert

There is most probably a sollution to do this in one command. But the above worked for me.

## ToDo

- I did not implement the security features mentinoned here: https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/handling-requests-sent-by-alexa That is my next to do, but it's not done yet.

- I like to add some more intents for diffrent stuff, like setting desired room temperature or asking for the open/close status of all the windows

- Implementing new intent features recently released by Amazon. For example this stuff: https://developer.amazon.com/public/solutions/alexa/alexa-skills-kit/docs/implementing-the-built-in-intents


## Thoughts

- This is a experimental project! 

- Right now, I don't know how to publish this skill, so any echo user could use it. There is no possibility to store some kind of user account information for a echo skill (like an openHab account)

- I would really like to see a german speaking alexa
 
 

  