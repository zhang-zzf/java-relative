package com.github.learn.bdd;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhoneBookService {

    private final PhoneBookRepository phoneBookRepository;

    /**
     * Register a contact
     *
     * @param name  Contact name
     * @param phone Phone number
     */
    public void register(String name, String phone) {
        if (!name.isEmpty() && !phone.isEmpty() && !phoneBookRepository.contains(name)) {
            phoneBookRepository.insert(name, phone);
        }
    }

    /**
     * Search for a phone number by contact name
     *
     * @param name Contact name
     * @return Phone number
     */
    public String search(String name) {
        if (!name.isEmpty() && phoneBookRepository.contains(name)) {
            return phoneBookRepository.getPhoneNumberByContactName(name);
        }
        return null;
    }

}
