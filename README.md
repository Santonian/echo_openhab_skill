# echo_openhab_skill

This is an Skill for Amazon Echo. 
The intention of this Skill is to receive commands from Alexa and call openHab per REST interface to perform diffrent openHab actions like
- turn on/off lights or sockets
- raise or lower rollershutter
- aks for temperature values
- ...

The skill is designed to call a local openHab installation. There is not authentication implemented to call openHab via username/password and https. (However this could be added easily)


## Requirements


### MongoDB Connection
For storing the various openHab items, you need a MongoDB Installation.
You can get your MongoDB here: https://www.mongodb.org/


### Generating self-signed certifacate for Amazon
For the Skill Webservice to be accessible from the Amazon Server, you need an SSL certificate for the https connection. You can use a self signed certificate for testing purposes or if you are the only one using the skill.

To create a Self Signed Certificate you can follow the Amazon Guide:
https://developer.amazon.com/appsandservices/solutions/alexa/alexa-skills-kit/docs/testing-an-alexa-skill#create-a-private-key-and-self-signed-certificate-for-testing

After that, do this:
keytool -import -file server.crt -keystore keystore.jks

However that did not work for me, I got an "no cipher suite in common" exception on my WebService , so I had to do this:

openssl pkcs12 -export -name myservercert -in cert.pem -inkey privkey.pem -out keystore.p12
keytool -importkeystore -destkeystore keystore.jks -srckeystore keystore.p12 -srcstoretype pkcs12 -alias myservercert

There is most probably a sollution to do this in one command. But the above worked for me.