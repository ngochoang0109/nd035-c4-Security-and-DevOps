package com.fpt.nd035c4SecurityandDevOps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fpt.nd035c4SecurityandDevOps.model.requests.AuthenticationRequest;
import com.fpt.nd035c4SecurityandDevOps.model.requests.CreateUserRequest;
import com.fpt.nd035c4SecurityandDevOps.model.requests.ModifyCartRequest;
import com.fpt.nd035c4SecurityandDevOps.model.responses.AuthenticationResponse;
import com.fpt.nd035c4SecurityandDevOps.model.responses.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SareetaApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private String authenJson = "";

	private final int ITEM_ID_1 = 1;
	private final int ITEM_QUANTITY_5 = 5;

	@BeforeAll
	public static void initData() {
	}

	@Test
	public void createUser() throws Exception {
		CreateUserRequest userRequest = this.getUserRequest();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(userRequest);
		mockMvc.perform(post(new URI("/api/user/create"))
						.content(json)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	@Test
	public void loginTest() throws Exception {
		CreateUserRequest userRequest = this.getUserRequest();
		this.createUser();
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setUsername(userRequest.getUsername());
		authenticationRequest.setPassword(userRequest.getPassword());
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(authenticationRequest);
		MvcResult result = mockMvc.perform(post(new URI("/api/user/login"))
						.content(json)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(status().isOk()).andReturn();
		String contentResult = result.getResponse().getContentAsString();
		AuthenticationResponse authenticationResponse = objectMapper.readValue(contentResult, AuthenticationResponse.class);
		this.authenJson = authenticationResponse.getAccessToken();
	}

	@Test
	public void getUserByUsernameTest() throws Exception {
		CreateUserRequest userRequest = this.getUserRequest();
		this.loginTest();
		MvcResult result = mockMvc.perform(get(new URI("/api/user/" + userRequest.getUsername()))
				.header("Authorization", this.authenJson))
				.andExpect(status().isOk()).andReturn();
		String contentResult = result.getResponse().getContentAsString();
		UserResponse actual = objectMapper.readValue(contentResult, UserResponse.class);
		Assertions.assertEquals(userRequest.getUsername(), actual.getUsername());
	}

	@Test
	public void addToCart() throws Exception {
		CreateUserRequest userRequest = this.getUserRequest();
		this.loginTest();
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setUsername(userRequest.getUsername());
		modifyCartRequest.setItemId(this.ITEM_ID_1);
		modifyCartRequest.setQuantity(this.ITEM_QUANTITY_5);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(modifyCartRequest);
		mockMvc.perform(post(new URI("/api/cart/addToCart"))
						.header("Authorization", this.authenJson)
						.content(json)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	public void removeItemFromCart() throws Exception {
		CreateUserRequest userRequest = this.getUserRequest();
		this.loginTest();
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setUsername(userRequest.getUsername());
		modifyCartRequest.setItemId(this.ITEM_ID_1);
		modifyCartRequest.setQuantity(this.ITEM_QUANTITY_5);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(modifyCartRequest);
		mockMvc.perform(post(new URI("/api/cart/modifyItemOfCart"))
						.header("Authorization", this.authenJson)
						.content(json)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();
	}

	private CreateUserRequest getUserRequest(){
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setUsername("hoangtn");
		createUserRequest.setPassword("12345678");
		createUserRequest.setConfirmPassword("12345678");
		return createUserRequest;
	}
}
