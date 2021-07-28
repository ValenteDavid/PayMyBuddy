package com.paymybuddy.paymybuddy.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.paymybuddy.PayMyBuddyApplication;
import com.paymybuddy.paymybuddy.model.User;

@SpringBootTest(classes = PayMyBuddyApplication.class)
@AutoConfigureMockMvc
class PagesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testIndex() throws Exception {
		this.mockMvc.perform(get("/"))
				.andExpect(view().name("redirect:/login"));
	}

	@Test
	void testLoginPage_HttpServletRequest_Model_WhenUserIsNull() throws Exception {
		this.mockMvc.perform(get("/login"))
				.andExpect(view().name("login"));
	}

	@Test
	void testLoginPage_HttpServletRequest_Model_WhenUserIsNotNull() throws Exception {
		this.mockMvc.perform(get("/login").sessionAttr("userID", 1))
				.andExpect(view().name("redirect:/transferIn"));
	}

	@Test
	void testLoginPage_HttpServletRequestModel_User() throws Exception {
		this.mockMvc.perform(post("/login")
				.flashAttr("user", new User()))
				.andExpect(view().name("login"));
	}

	@Test
	void testLogoffPage() throws Exception {
		this.mockMvc.perform(get("/logoff"))
				.andExpect(view().name("redirect:/login"));
	}

	@Test
	void testRegistrationPage_Model() throws Exception {
		this.mockMvc.perform(get("/"))
				.andExpect(view().name("redirect:/login"));
	}

	@Test
	void testRegistrationPage_User_BindingResult() throws Exception {
		this.mockMvc.perform(post("/registration")
				.flashAttr("user", new User("test@email.com", "mdp", "NameTest")))
				.andExpect(view().name("redirect:/transferIn"));
	}

	@Test
	void testTranferInHome_WhenUserIsNull() throws Exception {
		this.mockMvc.perform(get("/transferIn"))
				.andExpect(view().name("redirect:/"));
	}

	@Test
	@Transactional
	void testTranferInHome_WhenUserIsNotNull() throws Exception {
		this.mockMvc.perform(get("/transferIn").sessionAttr("userID", 1))
				.andExpect(view().name("transferIn"))
				.andExpect(model().attributeExists("balance"))
				.andExpect(model().attributeExists("connections"))
				.andExpect(model().attributeExists("transactions"))
				.andExpect(model().attributeExists("messageAddConnection"))
				.andExpect(model().attributeExists("messageTransfertInConnection"))
				.andExpect(model().attributeExists("messageTransfertInAmount"));
	}

	@Test
	void testTransferTransactionInternal_UserIsNull() throws Exception {
		this.mockMvc.perform(post("/transferTransactionInternal")
				.param("contactID", "2")
				.param("description", "")
				.param("amount", "10"))
				.andExpect(view().name("redirect:/"));
	}

	@Test
	void testTransferTransactionInternal() throws Exception {
		this.mockMvc.perform(post("/transferTransactionInternal").sessionAttr("userID", 1)
				.param("contactID", "2")
				.param("description", "")
				.param("amount", "10"))
				.andExpect(view().name("redirect:/transferIn"));
	}

	@Test
	void testTransferTransactionInternal_BadContactID() throws Exception {
		this.mockMvc.perform(post("/transferTransactionInternal").sessionAttr("userID", 1)
				.param("contactID", "-1")
				.param("description", "")
				.param("amount", "10"))
				.andExpect(view().name("redirect:/transferIn"))
				.andExpect(model().attribute("messageTransfertInConnection", "Empty Connection"));
	}

	@Test
	void testTransferTransactionInternal_BadAmountNull() throws Exception {
		this.mockMvc.perform(post("/transferTransactionInternal").sessionAttr("userID", 1)
				.param("contactID", "2")
				.param("description", ""))
				.andExpect(view().name("redirect:/transferIn"))
				.andExpect(model().attribute("messageTransfertInAmount", "Amount empty"));
	}

	@Test
	void testTransferTransactionInternal_BadAmountSignum() throws Exception {
		this.mockMvc.perform(post("/transferTransactionInternal").sessionAttr("userID", 1)
				.param("contactID", "2")
				.param("description", "")
				.param("amount", "-1"))
				.andExpect(view().name("redirect:/transferIn"))
				.andExpect(model().attribute("messageTransfertInAmount", "Amount negatif"));
	}

	@Test
	void testAddConnection() throws Exception {
		this.mockMvc.perform(post("/addConnection")
				.param("emailConnection", "test@email.com"))
				.andExpect(view().name("redirect:/"));
	}
	
	@Test
	void testAddConnection_EmailEmpty() throws Exception {
		this.mockMvc.perform(post("/addConnection").sessionAttr("userID", 1)
				.param("emailConnection", ""))
				.andExpect(view().name("redirect:/transferIn"))
				.andExpect(model().attribute("messageAddConnection", "Email is empty"));
	}



	@Test
	void testTranferOutHome_UserIsNull() throws Exception {
		this.mockMvc.perform(get("/transferOut"))
				.andExpect(view().name("redirect:/"));
	}

	@Test
	@Transactional
	void testTranferOutHome_UserIsNotNull() throws Exception {
		this.mockMvc.perform(get("/transferOut").sessionAttr("userID", 1))
				.andExpect(view().name("transferOut"))
				.andExpect(model().attributeExists("balance"))
				.andExpect(model().attributeExists("transactions"))
				.andExpect(model().attributeExists("transactionsOther"))
				.andExpect(model().attributeExists("messageTransactionBankingIn"))
				.andExpect(model().attributeExists("messageTransactionBankingOut"));
	}

	@Test
	void testTransferTransactionBankingIn() throws Exception {
		this.mockMvc.perform(post("/transferTransactionBankingIn")
				.param("amount", "1"))
				.andExpect(view().name("redirect:/"));
	}

	@Test
	void testTransferTransactionBankingOut() throws Exception {
		this.mockMvc.perform(post("/transferTransactionBankingOut")
				.param("amount", "1"))
				.andExpect(view().name("redirect:/"));
	}

}
