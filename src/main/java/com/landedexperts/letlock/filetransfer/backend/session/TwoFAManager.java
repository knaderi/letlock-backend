    /*******************************************************************************
     * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
     *  Unauthorized copying of this file, via any medium is strictly prohibited
     *  Proprietary and confidential
     *  Written by Irina Soboleva - 2021
     ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.session;

    import java.util.Base64;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Random;

    public class TwoFAManager {
        private static final long VERIFICATION_CODE_SIZE = 6;
        private static TwoFAManager singleInstance = null;

        public static TwoFAManager getInstance() {
            if (singleInstance == null) {
                singleInstance = new TwoFAManager();
            }
            return singleInstance;
        }

        private final Random random;
        private final Map<String, AuthSession> twoFATokens;

        private TwoFAManager() {
            random = new Random();
            twoFATokens = new HashMap<>();
        }

        /*
         * Generates a temporary session token for second step of the user authentication
         */
        public String generateAuthToken(final long userId) {
            /* Generate the token */
            byte[] randomBytes = new byte[128];
            random.nextBytes(randomBytes);
            String token = "TEMP" + Base64.getEncoder().encodeToString(randomBytes);

            /* Generate the user's authentication session and associate it the token */
            twoFATokens.put(token, new AuthSession(userId));

            return token;
        }
        
        /* 
         * Checks if the token is active
        */
       public boolean isActive(final String token) {
           boolean result = false;
           if (twoFATokens.containsKey(token)) {
               result = twoFATokens.get(token).isActive();
               if (!result) {
                   cleanSession(token);
               }
           }
           return result;
       }
        
        /*
         * Get the user id (for the database) associated with this token
         * Returned value to be used internally only
         */
        public long getUserId(final String token) {

            long result = -1;
            if (isActive(token)) {
                AuthSession authSession = twoFATokens.get(token);
                result = authSession.getUserId();
                authSession.extend();
            }
            return result;
        }
        
        /*
         * Generate a verification code to send to the user with the given token
         */
        public String generateCode(final String token) {
            String code = ""; 
            int[] ints = random.ints(VERIFICATION_CODE_SIZE, 0, 10).toArray();
            for (int i = 0; i < ints.length; i++) {
                code = code + ints[i];
            }
            twoFATokens.get(token).setCode(code);
            return code;
        }
        
        /*
         * Verify the code provided by the user with the given token
         * Return verification result including number of wrong attempts
         */
        public VerificationResult verifyCode(final String token, final String code) {
            VerificationResult result = twoFATokens.get(token).verifyCode(code);
            
            if (result.getValid()) {
                cleanSession(token);
            }
            return result;
        }

        /*
         * Remove the temporary session associated with the token
         */
        public void cleanSession(final String token) {
            twoFATokens.remove(token);
        }


        /*
         * Remove non active temporary sessions
         */
        public void clean() {
            for (Map.Entry<String, AuthSession> entry : twoFATokens.entrySet()) {
                if (!entry.getValue().isActive()) {
                    cleanSession(entry.getKey());
                }
            }
        }

    

}
