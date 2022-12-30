package com.loam.stoody.service.communication.sms;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.loam.stoody.global.constants.ProjectConfigurationVariables;
import com.loam.stoody.global.logger.ConsoleColors;
import com.loam.stoody.global.logger.StoodyLogger;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SmsSenderService {

    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    @Value("${twilio.trial_number}")
    private String TWILIO_NUMBER;

    private static final LoadingCache otpCache;

    private static final long EXPIRE_MINS = 5l;

    static {
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
                .build(new CacheLoader() {
                    @Override
                    public Object load(Object o) throws Exception {
                        return 0;
                    }
                });
    }

    public void sendOTP(String username, String toNumber) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(username, otp);

        // If you want OTP to be sent to User's number, you have to change stoodyEnvironment value!
        if(ProjectConfigurationVariables.stoodyEnvironment.equals(ProjectConfigurationVariables.developmentMode)){
            StoodyLogger.DebugLog(ConsoleColors.CYAN, "Use this OTP to sign in:"+String.valueOf(otp));
        }else {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                            new PhoneNumber(toNumber),
                            new PhoneNumber(TWILIO_NUMBER),
                            "Your Stoody OTP is:" + otp)
                    .create();
        }
    }

    public boolean validateOTP(String token, String username) {
        var _loadingCache = otpCache.getIfPresent(username);
        if(_loadingCache == null) return false;

        String savedToken = _loadingCache.toString();
        if (savedToken == null) return false;

        return savedToken.equals(token);
    }

    // TODO: aleemkhowaja, this is not used.
    public void isSmsDelivered() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        ListenableFuture<ResourceSet<Message>> future = Message.reader().readAsync();
        Futures.addCallback(
                future,
                new FutureCallback<ResourceSet<Message>>() {
                    public void onSuccess(ResourceSet<Message> messages) {
                        for (Message message : messages) {
                            System.out.println(message.getSid() + " : " + message.getStatus());
                        }
                    }

                    public void onFailure(Throwable t) {
                        System.out.println("Failed to get message status: " + t.getMessage());
                    }
                });
    }
}