package com.paymybuddy.paymybuddy.controller;

import java.math.BigDecimal;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.service.UserManagementConnexionService;

@Controller
public class PagesController {

	@Autowired
	private UserManagementConnexionService UserManagementConnexionService;

	@Autowired
	private TransactionService transactionService;

	@GetMapping("/")
	public String index(HttpServletRequest request, Model model) {
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String loginPage(HttpServletRequest request, Model model) {
		User user = getTheUserConnection(request);
		if (user == null) {
			model.addAttribute("user", new User());
			return "login";
		} else {
			return "redirect:/transferIn";
		}
	}

	@PostMapping("/login")
	public String loginPage(HttpServletRequest request, Model model, @ModelAttribute User user) {
		if (user != null) {
			Optional<User> userLogin = UserManagementConnexionService.login(user);
			if (!userLogin.isEmpty()) {
				request.getSession().setAttribute("userID", userLogin.get().getId());
				return "redirect:/transferIn";
			}
		}
		model.addAttribute("wrongMessage", "Bad email or password");
		return "login";
	}

	@GetMapping("/logoff")
	public String logoffPage(HttpServletRequest request, Model model) {
		request.getSession().setAttribute("userID", null);
		return "redirect:/login";
	}

	@GetMapping("/registration")
	public String registrationPage(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}

	@PostMapping("/registration")
	public String registrationPage(@ModelAttribute User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "registration";
		}
		UserManagementConnexionService.registration(user);
		return "redirect:/transferIn";
	}

	@GetMapping("/transferIn")
	public String tranferInHome(HttpServletRequest request,
			@ModelAttribute(name = "messageAddConnection") String messageAddConnection,
			@ModelAttribute(name = "messageTransfertInConnection") String messageTransfertInConnection,
			@ModelAttribute(name = "messageTransfertInAmount") String messageTransfertInAmount,
			Model model) {
		User user = getTheUserConnection(request);
		if (user == null) {
			return "redirect:/";
		}
		model.addAttribute("balance", user.getBalance());
		model.addAttribute("connections", user.getContacts());
		model.addAttribute("transactions", user.getTransactionInternals());
		model.addAttribute("messageAddConnection", messageAddConnection);
		model.addAttribute("messageTransfertInConnection", messageTransfertInConnection);
		model.addAttribute("messageTransfertInAmount", messageTransfertInAmount);
		return "transferIn";
	}

	@PostMapping("/transferTransactionInternal")
	public String transferTransactionInternal(HttpServletRequest request, @RequestParam Integer contactID,
			@RequestParam String description, @RequestParam(required = false) BigDecimal amount, Model model,
			RedirectAttributes ra) {
		User user = getTheUserConnection(request);
		if (user == null) {
			return "redirect:/";
		}

		if (contactID == -1) {
			ra.addAttribute("messageTransfertInConnection", "Empty Connection");
		} else if (amount == null) {
			ra.addAttribute("messageTransfertInAmount", "Amount empty");
		} else if (amount.signum() < 1) {
			ra.addAttribute("messageTransfertInAmount", "Amount negatif");
		} else if (transactionService.addTransactionInternal(user, contactID, description, amount) == null) {
			ra.addAttribute("messageTransfertInAmount", "Your account does not have enough money");
		}

		return "redirect:/transferIn";
	}

	@PostMapping("/addConnection")
	public String addConnection(HttpServletRequest request, @RequestParam String emailConnection, Model model,
			RedirectAttributes ra) {
		User user = getTheUserConnection(request);
		if (user == null) {
			return "redirect:/";
		}

		if (emailConnection.isEmpty()) {
			ra.addAttribute("messageAddConnection", "Email is empty");
		} else if (UserManagementConnexionService.addConnection(user, emailConnection) == null) {
			ra.addAttribute("messageAddConnection", "Contact not found at this email");
		} else {
			ra.addAttribute("messageAddConnection", "Contact added");
		}

		return "redirect:/transferIn";
	}
	
	@GetMapping("/transferOut")
	public String tranferOutHome(HttpServletRequest request, Model model,
			@ModelAttribute(name = "messageTransactionBankingIn") String messageTransactionBankingIn,
			@ModelAttribute(name = "messageTransactionBankingOut") String messageTransactionBankingOut) {
		User user = getTheUserConnection(request);
		if (user == null) {
			return "redirect:/";
		}

		model.addAttribute("balance", user.getBalance());
		model.addAttribute("transactions", user.getTransactionBankings());
		model.addAttribute("transactionsOther", user.getTransactionInternalsReceived());
		
		model.addAttribute("messageTransactionBankingIn", messageTransactionBankingIn);
		model.addAttribute("messageTransactionBankingOut", messageTransactionBankingOut);

		return "transferOut";
	}

	@PostMapping("/transferTransactionBankingIn")
	public String transferTransactionBankingIn(HttpServletRequest request, @RequestParam BigDecimal amount, Model model,RedirectAttributes ra) {
		User user = getTheUserConnection(request);
		if (user == null) {
			return "redirect:/";
		}
		
		transactionService.addTransactionBankingIn(user, amount);

		return "redirect:/transferOut";
	}
	
	@PostMapping("/transferTransactionBankingOut")
	public String transferTransactionBankingOut(HttpServletRequest request, @RequestParam BigDecimal amount, Model model,RedirectAttributes ra) {
		User user = getTheUserConnection(request);
		if (user == null) {
			return "redirect:/";
		}
		
		if (user.getBalance().compareTo(amount)>=0) {
			transactionService.addTransactionBankingOut(user, amount);
		}
		else {
			ra.addAttribute("messageTransactionBankingOut", "Insufficient balance");
		}

		return "redirect:/transferOut";
	}

	private User getTheUserConnection(HttpServletRequest request) {
		Integer userID = (Integer) request.getSession().getAttribute("userID");
		if (userID == null) {
			return null;
		} else {
			return UserManagementConnexionService.findById(userID);
		}
	}
}
