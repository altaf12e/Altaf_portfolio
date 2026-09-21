package com.portfolio.app.service;

import com.portfolio.app.model.ContactMessage;
import com.portfolio.app.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactService(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    public ContactMessage saveMessage(ContactMessage message) {
        return contactMessageRepository.save(message);
    }

    public List<ContactMessage> getAllMessages() {
        return contactMessageRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<ContactMessage> getMessageById(Long id) {
        return contactMessageRepository.findById(id);
    }

    public void markAsRead(Long id) {
        contactMessageRepository.findById(id).ifPresent(msg -> {
            msg.setReadStatus(true);
            contactMessageRepository.save(msg);
        });
    }

    public void markAsReplied(Long id) {
        contactMessageRepository.findById(id).ifPresent(msg -> {
            msg.setReplied(true);
            msg.setReadStatus(true);
            contactMessageRepository.save(msg);
        });
    }

    public void deleteMessage(Long id) {
        contactMessageRepository.deleteById(id);
    }

    public long getUnreadCount() {
        return contactMessageRepository.countByReadStatusFalse();
    }

    public long getTotalMessagesCount() {
        return contactMessageRepository.count();
    }
}

