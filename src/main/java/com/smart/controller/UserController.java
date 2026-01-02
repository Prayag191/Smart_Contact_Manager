package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.repository.ContactRepository;
import com.smart.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    //  DASHBOARD 
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        model.addAttribute("user", user);
        model.addAttribute("title", "Dashboard");

        return "user/dashboard"; // templates/user/dashboard.html
    }

    //  ADD CONTACT PAGE 
    @GetMapping("/add-contact")
    public String addContact(Model model) {

        model.addAttribute("contact", new Contact());
        model.addAttribute("title", "Add Contact");

        return "user/add_contact";
    }

    //  SAVE CONTACT 
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact,
                                 Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        contact.setUser(user);
        user.getContacts().add(contact);

        userRepository.save(user);

        return "redirect:/user/show-contacts";
    }

    //  SHOW CONTACTS 
    @GetMapping("/show-contacts")
    public String showContacts(Model model, Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        List<Contact> contacts = user.getContacts();

        model.addAttribute("contacts", contacts);
        model.addAttribute("title", "My Contacts");

        return "user/show_contacts";
    }

    //  VIEW CONTACT 
    @GetMapping("/contact/{id}")
    public String viewContact(@PathVariable("id") Integer id,
                              Model model,
                              Principal principal) {

        Contact contact = contactRepository.findById(id).get();

        model.addAttribute("contact", contact);
        model.addAttribute("title", "View Contact");

        return "user/view_contact";
    }

    //  EDIT CONTACT PAGE
    @GetMapping("/update/{id}")
    public String updateContact(@PathVariable("id") Integer id,
                                Model model) {

        Contact contact = contactRepository.findById(id).get();

        model.addAttribute("contact", contact);
        model.addAttribute("title", "Edit Contact");

        return "user/edit_contact";
    }

    //  UPDATE CONTACT 
    @PostMapping("/update-contact")
    public String updateHandler(@ModelAttribute Contact contact,
                                Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        contact.setUser(user);
        contactRepository.save(contact);

        return "redirect:/user/show-contacts";
        
    }

    // DELETE CONTACT 
    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") Integer id,
                                Principal principal) {

        Contact contact = contactRepository.findById(id).get();
        contactRepository.delete(contact);

        return "redirect:/user/show-contacts";
    }
}
