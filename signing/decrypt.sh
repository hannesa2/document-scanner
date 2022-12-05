#!/usr/bin/env bash

pwd

if [[ -z "$CRYPT_PASS" ]]
then
   read -sp 'Password: ' CRYPT_PASS
   if [[ -z "$CRYPT_PASS" ]]
   then
      echo "\$CRYPT_PASS Still empty"
      exit 1
   fi
else
   echo "\$CRYPT_PASS available"
fi

pushd signing

# to encrypt
#openssl aes-256-cbc -salt -pbkdf2 -salt -k "$CRYPT_PASS" -in release.keystore -out release.keystore.enc
#openssl aes-256-cbc -salt -pbkdf2 -k "$CRYPT_PASS" -in ./app/google-services.json -out ./app/google-services.json.enc

# Ubuntu 18.04 (openssl 1.1.0g+) needs -md md5
# https://askubuntu.com/questions/1067762/unable-to-decrypt-text-files-with-openssl-on-ubuntu-18-04/1076708
echo "decrypt release.keystore"
openssl aes-256-cbc -d -salt -pbkdf2 -k "$CRYPT_PASS" -in release.keystore.enc -out release.keystore
echo "decrypt ../app/google-services.json"
openssl aes-256-cbc -d -salt -pbkdf2 -k "$CRYPT_PASS" -in ../app/google-services.json.enc -out ../app/google-services.json

popd 1>/dev/null