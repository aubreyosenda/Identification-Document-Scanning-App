package com.vanatel.sidar.Service.Impl.IdGenerators;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service

public class CompanyIdGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateCompanyId()
    {
        StringBuilder companyId = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(CHARACTERS.length());
            companyId.append(CHARACTERS.charAt(index));
        }

        return companyId.toString();
    }
}
