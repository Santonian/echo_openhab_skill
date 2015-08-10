# echo_openhab_skill

# MongoDB Connection



# Generating self-signed certifacate for Amazon

following Amazon Guide:
https://developer.amazon.com/appsandservices/solutions/alexa/alexa-skills-kit/docs/testing-an-alexa-skill#create-a-private-key-and-self-signed-certificate-for-testing

Then:
keytool -import -file server.crt -keystore keystore.jks

However that did not work, I got an "no cipher suite in common" exception on my WebService , so I had to do this:

openssl pkcs12 -export -name myservercert -in cert.pem -inkey privkey.pem -out keystore.p12
keytool -importkeystore -destkeystore keystore.jks -srckeystore keystore.p12 -srcstoretype pkcs12 -alias myservercert

There is most probably a sollution to do this in one command. But that worked for me.